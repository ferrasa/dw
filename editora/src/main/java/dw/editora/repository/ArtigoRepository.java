package dw.editora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import dw.editora.model.Artigo;

import java.util.List;

public interface ArtigoRepository extends JpaRepository<Artigo, Long>{

    List<Artigo> findByPublicado(boolean publicado);

    List<Artigo> findByTituloContaining(String titulo);
    
}
