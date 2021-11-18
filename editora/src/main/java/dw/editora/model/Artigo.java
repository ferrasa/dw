package dw.editora.model;

import javax.persistence.*;

@Entity
@Table(name = "artigo")
public class Artigo {

    private long id;

    private String titulo;

    private String resumo;

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

	public void setPublicado(boolean ispublicado) {
		this.publicado = ispublicado;
	}

	@Override
	public String toString() {
		return "Artigo [id=" + id + ", titulo=" + titulo + ", resumo=" + resumo + ", publicado=" + publicado + "]";
	}
}

