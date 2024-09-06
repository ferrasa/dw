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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dw.editora.model.Artigo;
import dw.editora.repository.ArtigoRepository;

@RestController
public class ArtigoController {

    @Autowired
    ArtigoRepository rep;

    /*
     * GET / : listar todos os artigos
     */

    @GetMapping("/")
    public  ResponseEntity< List<Artigo> > getAllArtigos(@RequestParam(required = false) String titulo){
        try {
            List<Artigo> la = new ArrayList<Artigo>();

            if (titulo == null)
                rep.findAll().forEach(la::add);
            else
                rep.findByTituloContaining(titulo).forEach(la::add);

            if (la.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(la, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * POST / : criar um artigo
     */
    @PostMapping("/")
    public ResponseEntity<Artigo> createArtigo(@RequestBody Artigo ar) {
        try {
            Artigo a = rep.save(new Artigo(ar.getTitulo(), ar.getResumo(), ar.isPublicado()));
            return new ResponseEntity<>(a, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * GET /:id : listar artigo dado um id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Artigo> getArtigoById(@PathVariable("id") long id)
    {
        Optional<Artigo> data = rep.findById(id);

        if (data.isPresent())
            return new ResponseEntity<>(data.get(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
     * PUT /api/artigos/:id : atualizar artigo dado um id
     */
    @PutMapping("/{id}")
    public ResponseEntity<Artigo> updateArtigo(@PathVariable("id") long id, @RequestBody Artigo a)
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

    /*
     * DEL /:id : remover artigo dado um id
     */


    /*
     * DEL / : remover todos os artigos
     */

    
     /*
     * GET /publicado : buscar por artigos publicados 
     */
    

    
}
