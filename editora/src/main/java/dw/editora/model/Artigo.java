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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 80, nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String resumo;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean publicado  = true;

    public Artigo(){}

    public Artigo(String titulo, String resumo, boolean publicado) {
		this.titulo = titulo;
		this.resumo = resumo;
		this.publicado = publicado;
	}


    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getResumo() {
        return this.resumo;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

    public boolean isPublicado() {
        return this.publicado;
    }

    public void setPublicado(boolean publicado) {
        this.publicado = publicado;
    }

    @Override
    public String toString() {
		return "Artigo [id=" + id + ", titulo=" + titulo + ", resumo=" + resumo + ", publicado=" + publicado + "]";
	}

    
}
