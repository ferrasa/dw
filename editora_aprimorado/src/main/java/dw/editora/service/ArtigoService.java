package dw.editora.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import dw.editora.entity.Artigo;
import dw.editora.repository.ArtigoRepository;

// @Service diz ao Spring que esta classe contém a lógica de negócio
@Service
public class ArtigoService {

    private final ArtigoRepository repository;

    public ArtigoService(ArtigoRepository repository) {
        this.repository = repository;
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
        // Regras de negócio, validações complexas ou envio de emails entrariam aqui
        return repository.save(artigo);
    }

    public Optional<Artigo> atualizar(Long id, Artigo artigoAtualizado) {
        return repository.findById(id).map(artigo -> {
            artigo.setTitulo(artigoAtualizado.getTitulo());
            artigo.setResumo(artigoAtualizado.getResumo());
            artigo.setPublicado(artigoAtualizado.isPublicado());
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
}