package modelDominio;

import java.io.Serializable;
import java.util.ArrayList;

public class PublicacaoComum extends Publicacao implements Serializable {

    private static final long serialVersionUID = 123456789;
    private byte[] imagem;
    private ArrayList<Comentario> listaComentarios;

    public PublicacaoComum(int idPublicacao, Usuario usuario, String texto, int likes, byte[] imagem, ArrayList<Comentario> listaComentarios) {
        super(idPublicacao, usuario, texto, likes);
        this.imagem = imagem;
        this.listaComentarios = listaComentarios;
    }

    public PublicacaoComum(int idPublicacao, Usuario usuario, int likes, byte[] imagem) {
        super(idPublicacao, usuario, likes);
        this.imagem = imagem;
        this.listaComentarios = listaComentarios;
    }

    //sem listaComentarios
    public PublicacaoComum(int idPublicacao, Usuario usuario, String texto, int likes, byte[] imagem) {
        super(idPublicacao, usuario, texto, likes);
        this.imagem = imagem;
        listaComentarios = new ArrayList();
    }

    public PublicacaoComum(int idPublicacao, Usuario usuario, String texto, int likes, ArrayList<Comentario> listaComentarios) {
        super(idPublicacao, usuario, texto, likes);
        this.listaComentarios = listaComentarios;
    }

    //sem listaComentarios
    public PublicacaoComum(int idPublicacao, Usuario usuario, String texto, int likes) {
        super(idPublicacao, usuario, texto, likes);
        listaComentarios = new ArrayList();
    }

    //SEM ID-------------------------------
    public PublicacaoComum(Usuario usuario, String texto, int likes, byte[] imagem, ArrayList<Comentario> listaComentarios) {
        super(usuario, texto, likes);
        this.imagem = imagem;
        this.listaComentarios = listaComentarios;
    }

    //sem listaComentarios
    public PublicacaoComum(Usuario usuario, String texto, int likes, byte[] imagem) {
        super(usuario, texto, likes);
        this.imagem = imagem;
        listaComentarios = new ArrayList();
    }

    public PublicacaoComum(Usuario usuario, String texto, int likes, ArrayList<Comentario> listaComentarios) {
        super(usuario, texto, likes);
        this.listaComentarios = listaComentarios;
    }

    //sem listaComentarios
    public PublicacaoComum(Usuario usuario, String texto, int likes) {
        super(usuario, texto, likes);
        listaComentarios = new ArrayList();
    }

    public PublicacaoComum(int idPublicacao) {
        super(idPublicacao);
    }

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }

    public ArrayList<Comentario> getListaComentarios() {
        return listaComentarios;
    }

    public void setListaComentarios(ArrayList<Comentario> listaComentarios) {
        this.listaComentarios = listaComentarios;
    }

}
