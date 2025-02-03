package modelDominio;

import java.io.Serializable;
import java.util.ArrayList;

public class Usuario implements Serializable {

    private static final long serialVersionUID = 123456789;
    protected int codUsuario;
    protected String nomeUsuario;
    protected String email;
    protected String senha;
    protected ArrayList<Tag> listaTags;
    protected byte[] fotoPerfil;
    protected String bio;

    public Usuario(int codUsuario, String nomeUsuario, String email, String senha, ArrayList<Tag> listaTags, byte[] fotoPerfil, String bio) {
        this.codUsuario = codUsuario;
        this.nomeUsuario = nomeUsuario;
        this.email = email;
        this.senha = senha;
        this.listaTags = listaTags;
        this.fotoPerfil = fotoPerfil;
        this.bio = bio;
    }

    //com bio sem listaTags
    public Usuario(int codUsuario, String nomeUsuario, String email, String senha, byte[] fotoPerfil, String bio) {
        this.codUsuario = codUsuario;
        this.nomeUsuario = nomeUsuario;
        this.email = email;
        this.senha = senha;
        this.fotoPerfil = fotoPerfil;
        this.bio = bio;
    }

    public Usuario(int codUsuario, String nomeUsuario, String email, String senha, ArrayList<Tag> listaTags, byte[] fotoPerfil) {
        this.codUsuario = codUsuario;
        this.nomeUsuario = nomeUsuario;
        this.email = email;
        this.senha = senha;
        this.listaTags = listaTags;
        this.fotoPerfil = fotoPerfil;
    }

    //sem listaTags
    public Usuario(int codUsuario, String nomeUsuario, String email, String senha, byte[] fotoPerfil) {
        this.codUsuario = codUsuario;
        this.nomeUsuario = nomeUsuario;
        this.email = email;
        this.senha = senha;
        this.fotoPerfil = fotoPerfil;
        listaTags = new ArrayList();
    }

    //sem listaTags
    public Usuario(String nomeUsuario, String email, String senha, byte[] fotoPerfil) {
        this.nomeUsuario = nomeUsuario;
        this.email = email;
        this.senha = senha;
        this.fotoPerfil = fotoPerfil;
        listaTags = new ArrayList();
    }

    //SÓ PARA LOGIN
    public Usuario(String nomeUsuarioOuEmail, String senha) {
        this.nomeUsuario = nomeUsuarioOuEmail;
        this.email = nomeUsuarioOuEmail;
        this.senha = senha;
    }

    //sem foto e bio e com lista tags
    public Usuario(int codUsuario, String nomeUsuario, String email, String senha, ArrayList<Tag> listaTags) {
        this.codUsuario = codUsuario;
        this.nomeUsuario = nomeUsuario;
        this.email = email;
        this.senha = senha;
        this.listaTags = listaTags;
    }

    //igual sem Id
    public Usuario(String nomeUsuario, String email, String senha, ArrayList<Tag> listaTags) {
        this.nomeUsuario = nomeUsuario;
        this.email = email;
        this.senha = senha;
        this.listaTags = listaTags;
    }

    //sem foto, bio nem lista tags
    public Usuario(int codUsuario, String nomeUsuario, String email, String senha) {
        this.codUsuario = codUsuario;
        this.nomeUsuario = nomeUsuario;
        this.email = email;
        this.senha = senha;
    }

    //igual só que sem ID
    public Usuario(String nomeUsuario, String email, String senha) {
        this.nomeUsuario = nomeUsuario;
        this.email = email;
        this.senha = senha;
    }

    public Usuario(int codUsuario) {
        this.codUsuario = codUsuario;
    }

    public int getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(int codUsuario) {
        this.codUsuario = codUsuario;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public ArrayList<Tag> getListaTags() {
        if (listaTags == null) {
            listaTags = new ArrayList();
        }
        return listaTags;
    }

    public void setListaTags(ArrayList<Tag> listaTags) {
        this.listaTags = listaTags;
    }

    public byte[] getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(byte[] fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

}
