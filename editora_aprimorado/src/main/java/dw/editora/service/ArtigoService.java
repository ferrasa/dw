
package dw.editora.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import dw.editora.entity.Artigo;
import dw.editora.exception.RecursoNaoEncontradoException;
import dw.editora.repository.ArtigoRepository;
import dw.editora.repository.AutorRepository;

// Contém a lógica de negócio dos artigos
@Service
public class ArtigoService {

    private final ArtigoRepository repository;
    private final AutorRepository autorRepository;

    public ArtigoService(
            ArtigoRepository repository,
            AutorRepository autorRepository) {
        this.repository = repository;
        this.autorRepository = autorRepository;
    }

    public List<Artigo> listarArtigos(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            return repository.findAll();
        }

        return repository.findByTituloContainingIgnoreCase(titulo);
    }

    public Optional<Artigo> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Artigo salvar(Artigo artigo) {
        if (artigo.getAutor() == null
                || artigo.getAutor().getId() == null) {
            throw new IllegalArgumentException(
                    "É obrigatório informar o ID do autor.");
        }

        Long autorId = artigo.getAutor().getId();

        var autor = autorRepository.findById(autorId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Autor não encontrado com o ID: " + autorId));

        artigo.setAutor(autor);

        return repository.save(artigo);
    }

    public Optional<Artigo> atualizar(
            Long id, Artigo artigoAtualizado) {
        return repository.findById(id).map(artigo -> {
            artigo.setTitulo(artigoAtualizado.getTitulo());
            artigo.setResumo(artigoAtualizado.getResumo());
            artigo.setPublicado(artigoAtualizado.isPublicado());
            if (artigoAtualizado.getAutor() != null && artigoAtualizado.getAutor().getId() != null) {
                Long autorId = artigoAtualizado.getAutor().getId();
                var autor = autorRepository.findById(autorId)
                        .orElseThrow(() -> new RecursoNaoEncontradoException(
                                "Autor não encontrado com o ID: " + autorId));
                artigo.setAutor(autor);
            }
            return repository.save(artigo);
        });
    }

    public boolean deletar(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }

        return false;
    }

    public void deletarTodos() {
        repository.deleteAll();
    }

    public List<Artigo> listarPublicados() {
        return repository.findByPublicado(true);
    }

    public List<Artigo> listarPorAutor(Long autorId) {
        return repository.findByAutorId(autorId);
    }
}
