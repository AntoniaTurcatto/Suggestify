package modelDominio;

import java.io.Serializable;
import java.util.ArrayList;

public class Musica implements Serializable{
    
    private static final long serialVersionUID = 123456789;
    private int idMusica;
    private byte[] audio;
    private ArrayList<Tag> listaTags;
    private ArrayList<Artista> colaboradores;
    private Artista artistaPrincipal;
    private String nome;

    public Musica(int idMusica,String nome, byte[] audio, ArrayList<Tag> listaTags, ArrayList<Artista> colaboradores, Artista artistaPrincipal) {
        this.idMusica = idMusica;
        this.audio = audio;
        this.nome = nome;
        this.listaTags = listaTags;
        this.colaboradores = colaboradores;
        this.artistaPrincipal = artistaPrincipal;
    }

    //sem colaborador
    public Musica(int idMusica, String nome,byte[] audio, ArrayList<Tag> listaTags, Artista artistaPrincipal) {
        this.idMusica = idMusica;
        this.audio = audio;
        this.nome = nome;
        this.listaTags = listaTags;
        this.artistaPrincipal = artistaPrincipal;
        colaboradores = new ArrayList();
    }

    public Musica(String nome, byte[] audio, ArrayList<Tag> listaTags, ArrayList<Artista> colaboradores, Artista artistaPrincipal) {
        this.nome = nome;
        this.audio = audio;
        this.listaTags = listaTags;
        this.colaboradores = colaboradores;
        this.artistaPrincipal = artistaPrincipal;
    }

    //sem colaborador
    public Musica(String nome, byte[] audio, ArrayList<Tag> listaTags, Artista artistaPrincipal) {
        this.audio = audio;
        this.nome = nome;
        this.listaTags = listaTags;
        this.artistaPrincipal = artistaPrincipal;
        colaboradores = new ArrayList();
    }

    public Musica(int idMusica) {
        this.idMusica = idMusica;
    }

    public int getIdMusica() {
        return idMusica;
    }

    public void setIdMusica(int idMusica) {
        this.idMusica = idMusica;
    }

    public byte[] getAudio() {
        return audio;
    }

    public void setAudio(byte[] audio) {
        this.audio = audio;
    }

    public ArrayList<Tag> getListaTags() {
        return listaTags;
    }

    public void setListaTags(ArrayList<Tag> listaTags) {
        this.listaTags = listaTags;
    }

    public ArrayList<Artista> getColaboradores() {
        return colaboradores;
    }

    public void setColaboradores(ArrayList<Artista> colaboradores) {
        this.colaboradores = colaboradores;
    }

    public Artista getArtistaPrincipal() {
        return artistaPrincipal;
    }

    public void setArtistaPrincipal(Artista artistaPrincipal) {
        this.artistaPrincipal = artistaPrincipal;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    
}
