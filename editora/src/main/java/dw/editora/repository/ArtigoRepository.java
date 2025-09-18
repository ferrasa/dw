// Define o pacote (namespace) onde esta interface está localizada.
// A organização em pacotes é uma prática fundamental em Java para estruturar o projeto.
package dw.editora.repository;

// Importa a interface List do pacote de utilitários do Java.
// Uma List é uma coleção ordenada de objetos, que será usada como tipo de retorno para as buscas.
import java.util.List;

// Importa a interface JpaRepository do framework Spring Data JPA.
// Esta é a interface principal que fornecerá os métodos CRUD e outras funcionalidades de acesso a dados.
import org.springframework.data.jpa.repository.JpaRepository;

// Importa a classe de modelo (entidade) Artigo.
// O repositório precisa saber qual entidade ele irá gerenciar.
import dw.editora.model.Artigo;

// Declaração da interface pública ArtigoRepository.
// 'public' significa que pode ser acessada por qualquer outra classe no projeto.
// 'interface' define um contrato de métodos que uma classe pode implementar. Neste caso, o Spring criará a implementação automaticamente.
// 'extends JpaRepository<Artigo, Long>' é a parte mais importante.
//   - 'extends JpaRepository': Faz com que ArtigoRepository herde todos os métodos padrão de um repositório JPA,
//     como save(), findById(), findAll(), delete(), etc. Não precisamos escrever a implementação para esses métodos.
//   - '<Artigo, Long>': São os parâmetros de tipo genérico.
//     - 'Artigo': Informa ao Spring Data JPA que esta interface gerencia a entidade 'Artigo'.
//     - 'Long': Informa o tipo de dado da chave primária (@Id) da entidade 'Artigo'.
public interface ArtigoRepository extends JpaRepository<Artigo, Long> {
 
    // Declaração de um método de busca customizado, conhecido como "derived query" ou "query method".
    // O Spring Data JPA irá "ler" o nome do método e criar automaticamente a consulta correspondente.
    // 'findByPublicado' se traduz em uma consulta SQL como: "SELECT * FROM artigo WHERE publicado = ?"
    // O parâmetro 'boolean publicado' será o valor usado na cláusula WHERE.
    // Retorna uma lista de artigos que correspondem ao critério.
    List<Artigo> findByPublicado(boolean publicado);

    // Declaração de outro método de busca customizado.
    // 'findByTituloContaining' se traduz em uma consulta SQL com o operador LIKE: "SELECT * FROM artigo WHERE titulo LIKE '%...%'"
    // A palavra-chave 'Containing' instrui o Spring a procurar por artigos cujo título contenha a string fornecida no parâmetro.
    // O parâmetro 'String titulo' é o texto a ser procurado.
    // Retorna uma lista de artigos que correspondem ao critério de busca.
    List<Artigo> findByTituloContaining(String titulo);
}