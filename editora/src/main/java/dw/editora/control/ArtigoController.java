package dw.editora.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dw.editora.model.Artigo;
import dw.editora.repository.ArtigoRepository;

@RestController
@RequestMapping("/api")
public class ArtigoController {

    @Autowired
    ArtigoRepository rep;

    @GetMapping("/artigos")
    public ResponseEntity<List<Artigo>> getAllArtigos(@RequestParam(required = false) String titulo)
    {
        try{
                List<Artigo> la = new ArrayList<Artigo>();
                if (titulo == null)
                    rep.findAll().forEach(la::add);
                else
                    rep.findByTituloContaining(titulo).forEach(la::add);
                return new ResponseEntity<>(la, HttpStatus.OK);

        }
        catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/artigos")
    public ResponseEntity<Artigo> createArtigo(@RequestBody Artigo ar){
        try{
            Artigo _a = rep.save(new Artigo(ar.getTitulo(), ar.getResumo(), ar.isPublicado()));

            return new ResponseEntity<>(_a, HttpStatus.CREATED);

        }catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }

    @GetMapping("/artigos/{id}")
    public ResponseEntity<Artigo> getArtigoById(@PathVariable long id)
    {
        Optional<Artigo> data = rep.findById(id);

        if (data.isPresent())
            return new ResponseEntity<>(data.get(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/artigos/{id}")
    public ResponseEntity<Artigo> updateArtigo(@PathVariable long id, @RequestBody Artigo a)
    {
        Optional<Artigo> data = rep.findById(id);

        if (data.isPresent())
        {
            Artigo ar = data.get();
            ar.setPublicado(a.isPublicado());
            ar.setResumo(a.getResumo());
            ar.setTitulo(a.getTitulo());

            return new ResponseEntity<>(rep.save(ar), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    
}
