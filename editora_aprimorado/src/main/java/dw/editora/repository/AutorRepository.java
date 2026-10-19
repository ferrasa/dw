package dw.editora.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import dw.editora.entity.Autor;

/*
 * INVERSÃO DE CONTROLE (IoC) NO SPRING DATA JPA:
 * O programador não implementa esta interface! Ao estender JpaRepository, o Spring entra
 * em ação (Inversão de Controle) e cria a classe concreta que implementa todos os métodos
 * de CRUD (save, delete, findAll, etc.) em tempo de execução.
 */
public interface AutorRepository extends JpaRepository<Autor, Long> {

    List<Autor> findByNomeContainingIgnoreCase(String nome);

}