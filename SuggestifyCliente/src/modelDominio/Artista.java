package modelDominio;

import java.io.Serializable;
import java.util.ArrayList;

public class Artista extends Usuario implements Serializable {

    private static final long serialVersionUID = 123456789;
    private String nomeArtistico;

    public Artista(String nomeOuEmail, String senha){
        super(nomeOuEmail,senha);
    }
    
    public Artista(int codUsuario, String nomeUsuario, String email, String senha, ArrayList<Tag> listaTags, String nomeArtistico, byte[] fotoPerfil, String bio) {
        super(codUsuario, nomeUsuario, email, senha, listaTags, fotoPerfil, bio);
        this.nomeArtistico = nomeArtistico;
    }

    //sem listaTags com bio
    public Artista(int codUsuario, String nomeUsuario, String email, String senha, String nomeArtistico, byte[] fotoPerfil, String bio) {
        super(codUsuario, nomeUsuario, email, senha, fotoPerfil, bio);
        this.nomeArtistico = nomeArtistico;
    }

    public Artista(int codUsuario, String nomeUsuario, String email, String senha, ArrayList<Tag> listaTags, String nomeArtistico, byte[] fotoPerfil) {
        super(codUsuario, nomeUsuario, email, senha, listaTags, fotoPerfil);
        this.nomeArtistico = nomeArtistico;
    }

    //sem listaTags
    public Artista(int codUsuario, String nomeUsuario, String email, String senha, String nomeArtistico, byte[] fotoPerfil) {
        super(codUsuario, nomeUsuario, email, senha, fotoPerfil);
        this.nomeArtistico = nomeArtistico;
        super.listaTags = new ArrayList();
    }

    //sem listaTags
    public Artista(String nomeUsuario, String email, String senha, String nomeArtistico, byte[] fotoPerfil) {
        super(nomeUsuario, email, senha, fotoPerfil);
        this.nomeArtistico = nomeArtistico;
        super.listaTags = new ArrayList();
    }

    
    
    public Artista(int codUsuario) {
        super(codUsuario);
    }
    
    //sem foto e bio e com lista tags
    public Artista(int codUsuario, String nomeUsuario, String email, String senha, ArrayList<Tag> listaTags, String nomeArtistico) {
        super(codUsuario, nomeUsuario, email, senha, listaTags);
        this.nomeArtistico = nomeArtistico;
    }

    //igual sem Id
    public Artista(String nomeUsuario, String email, String senha, ArrayList<Tag> listaTags, String nomeArtistico) {
        super(nomeUsuario, email, senha, listaTags);
        this.nomeArtistico = nomeArtistico;
    }

    //sem foto, bio nem lista tags
    public Artista(int codUsuario, String nomeUsuario, String email, String senha, String nomeArtistico) {
        super(codUsuario,nomeUsuario, email, senha);
        this.nomeArtistico = nomeArtistico;
    }

    //igual só que sem ID
    public Artista(String nomeUsuario, String email, String senha, String nomeArtistico) {
        super(nomeUsuario, email, senha);
        this.nomeArtistico = nomeArtistico;
    }

    public String getNomeArtistico() {
        return nomeArtistico;
    }

    public void setNomeArtistico(String nomeArtistico) {
        this.nomeArtistico = nomeArtistico;
    }

}
