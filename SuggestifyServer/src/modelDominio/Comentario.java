package modelDominio;

import java.io.Serializable;

public class Comentario extends Publicacao implements Serializable{

    private static final long serialVersionUID = 123456789;

    public Comentario(int idPublicacao, Usuario usuario, String texto, int likes) {
        super(idPublicacao, usuario, texto, likes);
    }

    public Comentario(Usuario usuario, String texto, int likes) {
        super(usuario, texto, likes);
    }

    public Comentario(int idPublicacao) {
        super(idPublicacao);
    }

}
