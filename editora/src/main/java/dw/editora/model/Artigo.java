// Camada Model do padrão MVC.
package dw.editora.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// @Entity avisa ao JPA/Hibernate que esta classe é uma entidade de banco de dados (o 'M' do MVC)
@Entity
@Table(name = "artigo") // Mapeia explicitamente para a tabela chamada "artigo"
public class Artigo {
    
    // @Id define que este atributo é a chave primária
    @Id
    // @GeneratedValue delega ao banco de dados a responsabilidade de gerar o ID (auto-incremento)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    // Configurações da coluna: não pode ser nulo e tem tamanho máximo de 80 caracteres
    @Column(nullable = false, length = 80)
    private String titulo;
    
    @Column(nullable = false)
    private String resumo;

    // Define um valor padrão na estrutura do banco de dados (true por padrão)
    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE") 
    private boolean publicado;
      
    // Construtor vazio exigido pelo framework JPA
    public Artigo() {
	}

    // Construtor utilitário para facilitar a criação de objetos no Controller
	public Artigo(String titulo, String resumo, boolean publicado) {
		this.titulo = titulo;
		this.resumo = resumo;
		this.publicado = publicado;
	}

    // Getters e Setters (Encapsulamento - princípio básico de Orientação a Objetos)
    public long getId() { return this.id; }
    public void setId(long id) { this.id = id; }
    public String getTitulo() { return this.titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getResumo() { return this.resumo; }
    public void setResumo(String resumo) { this.resumo = resumo; }
    public boolean isPublicado() { return this.publicado; }
    public void setPublicado(boolean publicado) { this.publicado = publicado; }

    @Override
    public String toString() {
		return "Artigo [id=" + id + ", titulo=" + titulo + ", resumo=" + resumo + ", publicado=" + publicado + "]";
	}
}