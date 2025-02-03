package modelDominio;

import java.io.Serializable;
import java.util.ArrayList;

public class Comunidade implements Serializable {

    private static final long serialVersionUID = 123456789;
    private int idComunidade;
    private Artista artista;
    private ArrayList<PublicacaoComum> listaPublicacaoComum;
    private byte[] fotoBackground;
    private String corFundoPost, corFontePost;

    public Comunidade(int idComunidade, Artista artista, ArrayList<PublicacaoComum> listaPublicacaoComum, byte[] fotoBackground, String corFundoPost, String corFontePost) {
        this.idComunidade = idComunidade;
        this.artista = artista;
        this.listaPublicacaoComum = listaPublicacaoComum;
        this.fotoBackground = fotoBackground;
        this.corFundoPost = corFundoPost;
        this.corFontePost = corFontePost;
    }

    //sem listaPublicacao
    public Comunidade(int idComunidade, Artista artista, byte[] fotoBackground, String corFundoPost, String corFontePost) {
        this.idComunidade = idComunidade;
        this.artista = artista;
        this.listaPublicacaoComum = new ArrayList();
        this.fotoBackground = fotoBackground;
        this.corFundoPost = corFundoPost;
        this.corFontePost = corFontePost;
    }

    //sem id sem publicacao
    public Comunidade(Artista artista, byte[] fotoBackground) {
        this.artista = artista;
        this.fotoBackground = fotoBackground;
        listaPublicacaoComum = new ArrayList();
    }

    public Comunidade(int idComunidade) {
        this.idComunidade = idComunidade;
    }

    public int getIdComunidade() {
        return idComunidade;
    }

    public void setIdComunidade(int idComunidade) {
        this.idComunidade = idComunidade;
    }

    public Artista getArtista() {
        return artista;
    }

    public void setArtista(Artista artista) {
        this.artista = artista;
    }

    public ArrayList<PublicacaoComum> getListaPublicacaoComum() {
        return listaPublicacaoComum;
    }

    public void setListaPublicacaoComum(ArrayList<PublicacaoComum> listaPublicacaoComum) {
        this.listaPublicacaoComum = listaPublicacaoComum;
    }

    public byte[] getFotoBackground() {
        return fotoBackground;
    }

    public void setFotoBackground(byte[] fotoBackground) {
        this.fotoBackground = fotoBackground;
    }

    public String getCorFundoPost() {
        return corFundoPost;
    }

    public void setCorFundoPost(String corFundoPost) {
        this.corFundoPost = corFundoPost;
    }

    public String getCorFontePost() {
        return corFontePost;
    }

    public void setCorFontePost(String corFontePost) {
        this.corFontePost = corFontePost;
    }

}
