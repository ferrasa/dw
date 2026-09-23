package dw.editora.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dw.editora.entity.Artigo;

/*
 * INVERSÃO DE CONTROLE (IoC) NO SPRING DATA JPA:
 * O programador não implementa esta interface! Ao estender JpaRepository, o Spring entra
 * em ação (Inversão de Controle) e cria a classe concreta que implementa todos os métodos
 * de CRUD (save, delete, findAll, etc.) em tempo de execução.
 */
public interface ArtigoRepository extends JpaRepository<Artigo, Long> {
    
    // Método customizado derivado do nome (Query Methods)
    // O Spring gera o comando SQL automaticamente lendo "findBy" + "Publicado"
    List<Artigo> findByPublicado(boolean publicado);

    // O Spring interpreta este nome e gera um SQL com a cláusula "LIKE %titulo%" ignorando maiúsculas e minúsculas
    List<Artigo> findByTituloContainingIgnoreCase(String titulo);
}

