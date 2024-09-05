package dw.editora.control;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public  ResponseEntity< List<Artigo> > getAllArtigos(){
        try {
            List<Artigo> la = new ArrayList<Artigo>();

            rep.findAll().forEach(la::add);
            if (la.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(la, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}
