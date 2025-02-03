package modelDominio;

import java.io.Serializable;

public class Publicacao implements Serializable{
    private static final long serialVersionUID = 123456789;
    protected int idPublicacao;
    protected Usuario usuario;
    protected String texto;
    protected int likes;

    public Publicacao(int idPublicacao, Usuario usuario, String texto, int likes) {
        this.idPublicacao = idPublicacao;
        this.usuario = usuario;
        this.texto = texto;
        this.likes = likes;
    }
    
    public Publicacao(int idPublicacao, Usuario usuario, int likes) {
        this.idPublicacao = idPublicacao;
        this.usuario = usuario;
        this.likes = likes;
    }

    public Publicacao(Usuario usuario, String texto, int likes) {
        this.usuario = usuario;
        this.texto = texto;
        this.likes = likes;
    }

    public Publicacao(int idPublicacao) {
        this.idPublicacao = idPublicacao;
    }
    
    public int getIdPublicacao() {
        return idPublicacao;
    }

    public void setIdPublicacao(int idPublicacao) {
        this.idPublicacao = idPublicacao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
    
    
}
