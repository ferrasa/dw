// Define o pacote (namespace) onde esta classe está localizada.
// É uma convenção organizar as classes de modelo (entidades) em um pacote "model".
package dw.editora.model;

// Importa a anotação @Column da especificação Jakarta Persistence.
// Esta anotação é usada para especificar o mapeamento de um atributo para uma coluna da tabela.
import jakarta.persistence.Column;
// Importa a anotação @Entity, que marca esta classe como uma entidade JPA.
// Isso significa que ela será mapeada para uma tabela em um banco de dados.
import jakarta.persistence.Entity;
// Importa a anotação @GeneratedValue, que especifica como o valor da chave primária é gerado.
import jakarta.persistence.GeneratedValue;
// Importa a enumeração GenerationType, que contém as estratégias de geração de chave (ex: IDENTITY, AUTO).
import jakarta.persistence.GenerationType;
// Importa a anotação @Id, que designa um atributo como a chave primária da entidade.
import jakarta.persistence.Id;
// Importa a anotação @Table, que permite especificar o nome da tabela no banco de dados.
import jakarta.persistence.Table;

// @Entity: Anotação fundamental que informa ao provedor JPA (Hibernate) que esta classe é uma entidade
// e seus objetos devem ser persistidos no banco de dados.
@Entity
// @Table(name = "artigo"): Especifica que esta entidade deve ser mapeada para uma tabela chamada "artigo".
// Se omitido, o nome da tabela seria o nome da classe por padrão (neste caso, "Artigo").
@Table(name = "artigo")
// Declaração da classe pública 'Artigo'. Esta classe representa um artigo no sistema.
public class Artigo {
    
    // @Id: Marca o campo 'id' como a chave primária da tabela.
    // Toda entidade JPA precisa ter uma chave primária.
    @Id
    // @GeneratedValue: Configura a estratégia de geração para a chave primária.
    // strategy=GenerationType.IDENTITY: Delega a geração do ID ao banco de dados, que usará uma coluna de auto-incremento.
    // É uma estratégia comum e eficiente, especialmente com PostgreSQL.
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    // Declara o atributo 'id' do tipo 'long' para armazenar o identificador único do artigo.
    private long id;

    // @Column: Mapeia o atributo 'titulo' para uma coluna na tabela.
    // nullable = false: Define que a coluna no banco de dados não pode conter valores nulos (NOT NULL).
    // length = 80: Define o tamanho máximo da coluna para 80 caracteres (ex: VARCHAR(80)).
    @Column(nullable = false, length = 80)
    // Declara o atributo 'titulo' do tipo 'String'.
    private String titulo;

    // @Column(nullable = false): Mapeia o atributo 'resumo' para uma coluna que não pode ser nula.
    @Column(nullable = false)
    // Declara o atributo 'resumo' do tipo 'String'.
    private String resumo;

    // @Column(columnDefinition = "..."): Permite definir a DDL (Data Definition Language) exata para a coluna.
    // "BOOLEAN DEFAULT true": Cria a coluna como booleana e define seu valor padrão como 'true' no banco de dados.
    // Isso garante que novos artigos sejam publicados por padrão, mesmo que o valor não seja explicitamente definido.
    @Column(columnDefinition = "BOOLEAN DEFAULT true")
    // Declara o atributo 'publicado' do tipo 'boolean'.
    // A inicialização '= true' define o valor padrão para novos objetos Artigo criados em Java.
    private boolean publicado = true;

    // Construtor padrão (sem argumentos).
    // O JPA exige um construtor sem argumentos para poder criar instâncias da entidade ao recuperá-las do banco de dados.
    public Artigo() {

	}

    // Construtor parametrizado para facilitar a criação de novos objetos Artigo com valores iniciais.
	public Artigo(String titulo, String resumo, boolean publicado) {
        // 'this.titulo' se refere ao atributo da classe, enquanto 'titulo' se refere ao parâmetro do método.
		this.titulo = titulo;
		this.resumo = resumo;
		this.publicado = publicado;
	}

    // Método Getter para o atributo 'id'. Permite o acesso ao valor do id de fora da classe.
    public long getId() {
        return this.id;
    }

    // Método Setter para o atributo 'id'. Permite a modificação do valor do id.
    public void setId(long id) {
        this.id = id;
    }

    // Método Getter para o atributo 'titulo'.
    public String getTitulo() {
        return this.titulo;
    }

    // Método Setter para o atributo 'titulo'.
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    // Método Getter para o atributo 'resumo'.
    public String getResumo() {
        return this.resumo;
    }

    // Método Setter para o atributo 'resumo'.
    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

    // Método Getter para o atributo booleano 'publicado'.
    // Por convenção do JavaBeans, getters para booleanos podem começar com "is" em vez de "get".
    public boolean isPublicado() {
        return this.publicado;
    }

    // Método Setter para o atributo 'publicado'.
    public void setPublicado(boolean publicado) {
        this.publicado = publicado;
    }

    // @Override: Indica que este método está sobrescrevendo o método toString() da classe Object.
    @Override
    // O método toString() retorna uma representação textual do objeto.
    // É extremamente útil para logging e depuração, pois permite imprimir o estado do objeto de forma legível.
    public String toString() {
		return "Artigo [id=" + id + ", titulo=" + titulo + ", resumo=" + resumo + ", publicado=" + publicado + "]";
	}

}