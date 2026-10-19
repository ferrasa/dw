
package dw.editora.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import dw.editora.entity.Artigo;
import dw.editora.entity.Autor;
import dw.editora.repository.AutorRepository;

@Service
public class AutorService {

    private final AutorRepository repository;
    private final ArtigoService artigoService;

    public AutorService(
            AutorRepository repository,
            ArtigoService artigoService) {
        this.repository = repository;
        this.artigoService = artigoService;
    }

    public List<Autor> listarNomeAutor(String nome) {
        if (nome == null || nome.isBlank()) {
            return repository.findAll();
        }

        return repository.findByNomeContainingIgnoreCase(nome);
    }

    public Optional<Autor> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Autor salvar(Autor autor) {
        return repository.save(autor);
    }

    public Optional<Autor> atualizar(
            Long id,
            Autor autorAtualizado) {
        return repository.findById(id).map(autor -> {
            autor.setNome(autorAtualizado.getNome());
            autor.setEmail(autorAtualizado.getEmail());
            autor.setBiografia(autorAtualizado.getBiografia());

            return repository.save(autor);
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

    public List<Autor> listarAutores() {
        return repository.findAll();
    }

    public List<Artigo> listarArtigosPorAutor(Long autorId) {
        return artigoService.listarPorAutor(autorId);
    }
}
