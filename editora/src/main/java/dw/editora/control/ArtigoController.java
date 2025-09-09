package dw.editora.control;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import dw.editora.model.Artigo;
import dw.editora.repository.ArtigoRepository;


@RestController
public class ArtigoController {
    
    @Autowired
    ArtigoRepository rep;

    @GetMapping("/")
    public ResponseEntity< List<Artigo> > getAllArtigos(){
        return null;
    }
}
