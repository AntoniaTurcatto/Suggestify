package modelDominio;

import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable{

   
    private static final long serialVersionUID = 123456789;
    private int idAlbum;
    private ArrayList<Musica> listaMusicas;
    private ArrayList<Tag> listaTags;
    private Artista artista;
    private byte[] imagem;
    private String nome;

    public Album(int idAlbum, String nome, Artista artista, byte[] imagem, ArrayList<Tag> listaTags) {
        this.idAlbum = idAlbum;
        this.listaTags = listaTags;
        this.artista = artista;
        this.imagem = imagem;
        this.nome = nome;
    }

    public Album(String nome, ArrayList<Tag> listaTags, Artista artista, byte[] imagem) {
        this.nome = nome;
        this.listaTags = listaTags;
        this.artista = artista;
        this.imagem = imagem;
    }
    
    public Album(int idAlbum,String nome, ArrayList<Musica> listaMusicas, ArrayList<Tag> listaTags, Artista artista, byte[] imagem) {
        this.idAlbum = idAlbum;
        this.listaMusicas = listaMusicas;
        this.listaTags = listaTags;
        this.artista = artista;
        this.imagem = imagem;
        this.nome = nome;
    }

    public Album(String nome, ArrayList<Musica> listaMusicas, ArrayList<Tag> listaTags, Artista artista, byte[] imagem) {
        this.listaMusicas = listaMusicas;
        this.listaTags = listaTags;
        this.artista = artista;
        this.imagem = imagem;
        this.nome = nome;
    }
    
    

    public Album(int idAlbum, String nome, ArrayList<Musica> listaMusicas, Artista artista, byte[] imagem) {
        this.idAlbum = idAlbum;
        this.listaMusicas = listaMusicas;
        this.artista = artista;
        this.imagem = imagem;
        this.nome = nome;
    }

    //sem listaMusicas
    public Album(int idAlbum, String nome, Artista artista, byte[] imagem) {
        this.idAlbum = idAlbum;
        this.artista = artista;
        this.imagem = imagem;
        listaMusicas = new ArrayList();
        this.nome = nome;
    }

    //sem listaMusicas
    public Album(Artista artista, String nome, byte[] imagem) {
        this.artista = artista;
        this.imagem = imagem;
        this.nome = nome;
        listaMusicas = new ArrayList();
    }

    public Album(int idAlbum) {
        this.idAlbum = idAlbum;
    }

    public int getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(int idAlbum) {
        this.idAlbum = idAlbum;
    }

    public ArrayList<Musica> getListaMusicas() {
        return listaMusicas;
    }

    public void setListaMusicas(ArrayList<Musica> listaMusicas) {
        this.listaMusicas = listaMusicas;
    }

    public Artista getArtista() {
        return artista;
    }

    public void setArtista(Artista artista) {
        this.artista = artista;
    }

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }

    public ArrayList<Tag> getListaTags() {
        return listaTags;
    }

    public void setListaTags(ArrayList<Tag> listaTags) {
        this.listaTags = listaTags;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    

}
