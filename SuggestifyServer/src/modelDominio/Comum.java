package modelDominio;

import java.io.Serializable;
import java.util.ArrayList;

public class Comum extends Usuario implements Serializable {

    private static final long serialVersionUID = 123456789;
    private ArrayList<Comunidade> listaComunidadesInscritas;

    public Comum(String nomeOuEmail, String senha){
        super(nomeOuEmail,senha);
    }
    
    public Comum(int codUsuario, String nomeUsuario, String email, String senha, ArrayList<Tag> listaTags, ArrayList<Comunidade> listaComunidadesInscritas, byte[] fotoPerfil, String bio) {
        super(codUsuario, nomeUsuario, email, senha, listaTags, fotoPerfil, bio);
        this.listaComunidadesInscritas = listaComunidadesInscritas;
    }

    //com bio sem listaTags
    public Comum(int codUsuario, String nomeUsuario, String email, String senha, ArrayList<Comunidade> listaComunidadesInscritas, byte[] fotoPerfil, String bio) {
        super(codUsuario, nomeUsuario, email, senha, fotoPerfil, bio);
        this.listaComunidadesInscritas = listaComunidadesInscritas;
    }

    public Comum(int codUsuario, String nomeUsuario, String email, String senha, ArrayList<Tag> listaTags, ArrayList<Comunidade> listaComunidadesInscritas, byte[] fotoPerfil) {
        super(codUsuario, nomeUsuario, email, senha, listaTags, fotoPerfil);
        this.listaComunidadesInscritas = listaComunidadesInscritas;
    }

    //sem listaComunidadesInscritas
    public Comum(int codUsuario, String nomeUsuario, String email, String senha, ArrayList<Tag> listaTags, byte[] fotoPerfil) {
        super(codUsuario, nomeUsuario, email, senha, listaTags, fotoPerfil);
        listaComunidadesInscritas = new ArrayList();
    }

    //sem listaComunidadesInscritas nem listaTags
    public Comum(int codUsuario, String nomeUsuario, String email, String senha, byte[] fotoPerfil) {
        super(codUsuario, nomeUsuario, email, senha, fotoPerfil);
        listaComunidadesInscritas = new ArrayList();
        super.listaTags = new ArrayList();
    }

    //sem listaComunidadesInscritas nem listaTags
    public Comum(String nomeUsuario, String email, String senha, byte[] fotoPerfil) {
        super(nomeUsuario, email, senha, fotoPerfil);
        listaComunidadesInscritas = new ArrayList();
        super.listaTags = new ArrayList();
    }

    public Comum(int codUsuario) {
        super(codUsuario);
    }

    public ArrayList<Comunidade> getListaComunidadesInscritas() {
        return listaComunidadesInscritas;
    }

    public void setListaComunidadesInscritas(ArrayList<Comunidade> listaComunidadesInscritas) {
        this.listaComunidadesInscritas = listaComunidadesInscritas;
    }

}
