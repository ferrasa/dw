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
import dw.editora.service.ArtigoService;
import jakarta.validation.Valid;

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
        List<Artigo> artigos = service.listarArtigos(titulo);
        return ResponseEntity.ok(artigos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Artigo> getArtigo(@PathVariable Long id) {
        // Uso de programação funcional (map) para simplificar o Optional
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Artigo> createArtigo(@Valid @RequestBody Artigo artigo) {
        Artigo artigoSalvo = service.salvar(artigo);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(artigoSalvo.getId())
                .toUri();
        return ResponseEntity.created(location).body(artigoSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Artigo> updateArtigo(@PathVariable Long id, @Valid @RequestBody Artigo artigo) {
        return service.atualizar(id, artigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtigo(@PathVariable Long id) {
        if (service.deletar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllArtigos() {
        service.deletarTodos();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/publicados")
    public ResponseEntity<List<Artigo>> getAllPublicados() {
        List<Artigo> artigos = service.listarPublicados();
        return ResponseEntity.ok(artigos);
    }
}