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
import dw.editora.service.ArtigoService;

@CrossOrigin(origins = "*") 
@RestController 
@RequestMapping("/api/artigos") // Mapeia o recurso na raiz da classe
public class ArtigoController {

    // A dependência agora é do Service, não do Repository
    private final ArtigoService service;

    public ArtigoController(ArtigoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Artigo>> getAllArtigos(@RequestParam(required = false) String titulo) {
        try {
            List<Artigo> artigos = service.listarArtigos(titulo);
            
            if (artigos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(artigos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Artigo> getArtigo(@PathVariable Long id) {
        // Uso de programação funcional (map) para simplificar o Optional
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Artigo> createArtigo(@RequestBody Artigo artigo) {
        try {
            Artigo artigoSalvo = service.salvar(artigo);
            return ResponseEntity.status(HttpStatus.CREATED).body(artigoSalvo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Artigo> updateArtigo(@PathVariable Long id, @RequestBody Artigo artigo) {
        return service.atualizar(id, artigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtigo(@PathVariable Long id) {
        try {
            if (service.deletar(id)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllArtigos() {
        try {
            service.deletarTodos();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/publicados")
    public ResponseEntity<List<Artigo>> getAllPublicados() {
        try {
            List<Artigo> artigos = service.listarPublicados();
            if (artigos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(artigos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}