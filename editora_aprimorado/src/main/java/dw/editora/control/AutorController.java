
package dw.editora.control;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import dw.editora.entity.Artigo;
import dw.editora.entity.Autor;
import dw.editora.service.AutorService;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/autores")
public class AutorController {

    private final AutorService service;

    public AutorController(AutorService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Autor>> getAllAutores(
            @RequestParam(required = false) String nome) {
        List<Autor> autores;
        if (nome != null && !nome.isBlank()) {
            autores = service.listarNomeAutor(nome);
        } else {
            autores = service.listarAutores();
        }
        return ResponseEntity.ok(autores);
    }

    @GetMapping("/nome")
    public ResponseEntity<List<Autor>> getAutoresPorNome(
            @RequestParam("filtro") String nome) {
        List<Autor> autores = service.listarNomeAutor(nome);
        return ResponseEntity.ok(autores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Autor> getAutor(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Autor> createAutor(@Valid @RequestBody Autor autor) {
        Autor autorSalvo = service.salvar(autor);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(autorSalvo.getId())
                .toUri();

        return ResponseEntity.created(location).body(autorSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Autor> updateAutor(
            @PathVariable Long id,
            @Valid @RequestBody Autor autor) {
        return service.atualizar(id, autor)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAutor(@PathVariable Long id) {
        if (service.deletar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllAutores() {
        service.deletarTodos();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/artigos")
    public ResponseEntity<List<Artigo>> getArtigosPorAutor(
            @PathVariable Long id) {
        List<Artigo> artigos = service.listarArtigosPorAutor(id);
        return ResponseEntity.ok(artigos);
    }
}
