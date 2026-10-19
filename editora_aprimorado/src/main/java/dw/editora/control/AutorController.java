
package dw.editora.control;

import java.util.List;

import org.springframework.http.HttpStatus;
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

import dw.editora.entity.Artigo;
import dw.editora.entity.Autor;
import dw.editora.service.AutorService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/autores")
public class AutorController {

    private final AutorService service;

    public AutorController(AutorService service) {
        this.service = service;
    }

    @GetMapping("/nome")
    public ResponseEntity<List<Autor>> getAutoresPorNome(
            @RequestParam("filtro") String nome) {
        try {
            List<Autor> autores = service.listarNomeAutor(nome);

            if (autores.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(autores);
        } catch (Exception e) {
            return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Autor> getAutor(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Autor> createAutor(@RequestBody Autor autor) {
        try {
            Autor autorSalvo = service.salvar(autor);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(autorSalvo);
        } catch (Exception e) {
            return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Autor> updateAutor(
            @PathVariable Long id,
            @RequestBody Autor autor) {
        return service.atualizar(id, autor)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAutor(@PathVariable Long id) {
        try {
            if (service.deletar(id)) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllAutores() {
        try {
            service.deletarTodos();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Autor>> getAllAutores(
            @RequestParam(required = false) String nome) {
        try {
            List<Autor> autores = service.listarAutores();
            return ResponseEntity.ok(autores);
        } catch (Exception e) {
            return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/artigos")
    public ResponseEntity<List<Artigo>> getArtigosPorAutor(
            @PathVariable Long id) {

        if (service.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Artigo> artigos = service.listarArtigosPorAutor(id);

        return ResponseEntity.ok(artigos);
    }
}
