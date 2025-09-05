package dw.editora.model;

public class Artigo {

    private long id;

    private String titulo;

    private String resumo;

    private boolean publicado;

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
