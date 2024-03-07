package dw.editora.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "artigo")
public class Artigo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String titulo;

    @Column
    private String resumo;

    @Column
    private boolean publicado;

    public Artigo() {

	}

	public Artigo(String titulo, String resumo, boolean publicado) {
		this.titulo = titulo;
		this.resumo = resumo;
		this.publicado = publicado;
	}

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getResumo() {
        return resumo;
    }
    public void setResumo(String resumo) {
        this.resumo = resumo;
    }
    public boolean isPublicado() {
        return publicado;
    }
    public void setPublicado(boolean publicado) {
        this.publicado = publicado;
    }


}
