package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import modelDominio.Album;
import modelDominio.Artista;
import modelDominio.Comum;
import modelDominio.Comunidade;
import modelDominio.Musica;
import modelDominio.Publicacao;
import modelDominio.PublicacaoComum;
import modelDominio.Tag;
import modelDominio.Usuario;

public class ConexaoController {

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Usuario usuarioLogado;

    public ConexaoController(ObjectInputStream in, ObjectOutputStream out) {
        this.in = in;
        this.out = out;
    }

    //USUARIO
    public Usuario efetuarLogin(Usuario user) {
        try {
            // evniando o comando para o servidor
            out.writeObject("UsuarioEfetuarLogin");
            // recebendo o "OK"
            in.readObject(); // recebendo o "ok"
            // Enviando o usuário que está tentando fazer o login
            out.writeObject(user);
            // recebendo o usuário do servidor
            Usuario userLogin = (Usuario) in.readObject();
            // devolvendo o usuário para a tela
            usuarioLogado = userLogin;
            return userLogin;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean editarUsuario(Artista user) {
        try {
            out.writeObject("EditarUsuario");
            in.readObject(); //ok
            out.writeObject(user);
            return (boolean) in.readObject();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        } catch (ClassNotFoundException classErr) {
            classErr.printStackTrace();
            return false;
        }
    }

    public Usuario carregaUsuarioComumComComunidadesETags(Comum usuarioComum) {
        try {
            out.writeObject("CarregaUsuarioComumComComunidadesETags");
            in.readObject(); //ok
            out.writeObject(usuarioComum);
            return (Comum) in.readObject(); // usuarioComum com as comunidades e tags
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean cadastrarUsuario(Usuario usuario) {
        boolean result;
        try {
            out.writeObject("CadastrarUsuario");
            in.readObject(); // ok
            out.writeObject(usuario);
            result = (boolean) in.readObject();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            result = false;
        } catch (ClassNotFoundException classErr) {
            classErr.printStackTrace();
            result = false;
        }
        return result;
    }

    public boolean criarComunidade(Artista a, byte[] background, String corFundoPostHex, String corFontePostHex) {
        String[] coresFundoFonte = {corFundoPostHex, corFontePostHex};
        //[0] = corFundoPost
        //[1] = corFontePost
        try {
            out.writeObject("CriarComunidade");
            in.readObject(); //ok
            out.writeObject(background);//mandando bg
            in.readObject(); //ok
            out.writeObject(a);
            in.readObject();//ok
            out.writeObject(coresFundoFonte);
            return (boolean) in.readObject();
        } catch (IOException | ClassNotFoundException ioe) {
            ioe.printStackTrace();
            return false;
        }
    }

    public ArrayList<PublicacaoComum> carregaPostsComunidade(Comunidade comunidade) {
        try {
            out.writeObject("CarregaPostsComunidade");
            in.readObject(); //ok
            out.writeObject(comunidade);
            return (ArrayList<PublicacaoComum>) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Comunidade getComunidadePeloArtistaId(int idArtista) {
        try {
            out.writeObject("GetComunidadePeloArtistaId");
            in.readObject();
            out.writeObject(idArtista);
            return (Comunidade) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //TAGS
    public ArrayList<Tag> getListaTags() {
        try {
            out.writeObject("GetListaTags");
            return (ArrayList<Tag>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("ERRO: " + e.getMessage());
            return null;
        }
    }

    public ArrayList<Tag> getListaTags(Musica m) {
        try {
            out.writeObject("GetListaTagsDeMusica");
            in.readObject();//ok
            out.writeObject(m);
            return (ArrayList<Tag>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("ERRO: " + e.getMessage());
            return null;
        }
    }

    public ArrayList<Tag> getListaTags(Usuario c) {
        try {
            out.writeObject("GetListaTagsUsuario");
            System.out.println("Enviado comando: GetListaTagsComunidade");
            in.readObject();//ok
            System.out.println("recebendo ok");
            out.writeObject(c);
            System.out.println("Comunidade enviada");
            return (ArrayList<Tag>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("ERRO: " + e.getMessage());
            return null;
        }
    }

    //ASSOCIATIVA USUARIO-TAGS
    public boolean vincularUsuarioTag(Tag t, Usuario u) {
        Object[] tagEUsuario = {t, u};
        try {
            out.writeObject("VincularUsuarioTag");
            in.readObject();//ok
            out.writeObject(tagEUsuario);
            return (boolean) in.readObject();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        } catch (ClassNotFoundException classErr) {
            classErr.printStackTrace();
            return false;
        }
    }

    public boolean desvincularUsuarioTag(Tag t, Usuario u) {
        Object[] tagEUsuario = {t, u};
        try {
            out.writeObject("DesvincularUsuarioTag");
            in.readObject();//ok
            out.writeObject(tagEUsuario);
            return (boolean) in.readObject();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        } catch (ClassNotFoundException classErr) {
            classErr.printStackTrace();
            return false;
        }
    }

    //POSTS
    public boolean publicarComum(PublicacaoComum pComum, Comunidade com) {
        Object[] postCom = {pComum, com};
        try {
            out.writeObject("InserePublicacaoComum");
            in.readObject();//ok
            //[0] = publicacaoComum
            //[1] = comunidade
            out.writeObject(postCom);
            return (boolean) in.readObject();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        } catch (ClassNotFoundException classErr) {
            classErr.printStackTrace();
            return false;
        }

    }

    //ele cria a associação usuarioCurtiuPublicacao automaticamente no server
    public boolean curtirPublicacaoOuComentario(Publicacao p, Usuario u) {
        Object[] publicacaoUsuario = {p, u};
        //[0] = publicacao;
        //[1] = usuario;
        try {
            out.writeObject("Curtir");
            in.readObject();//ok
            out.writeObject(publicacaoUsuario);
            return (boolean) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    //ele cria a associação usuarioDescurtiuPublicacao automaticamente no server
    public boolean descurtirPublicacaoOuComentario(Publicacao p, Usuario u) {
        Object[] publicacaoUsuario = {p, u};
        //[0] = publicacao;
        //[1] = usuario;
        try {
            out.writeObject("Descurtir");
            in.readObject();//ok
            out.writeObject(publicacaoUsuario);
            return (boolean) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean usuarioJaCurtiu(Publicacao p, Usuario u) {
        Object[] publicacaoUsuario = {p, u};
        //[0] = publicacao;
        //[1] = usuario;
        try {
            out.writeObject("UsuarioJaCurtiu");
            in.readObject();//ok
            out.writeObject(publicacaoUsuario);
            return (boolean) in.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editarComunidade(Comunidade c) {
        try {
            out.writeObject("EditarComunidade");
            in.readObject();//ok
            System.out.println("imediatamente antes de enviar, cor fundo: " + c.getCorFundoPost());
            System.out.println("imediatamente antes de enviar, cor fonte: " + c.getCorFontePost());
            out.writeObject(c);
            return (boolean) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro: " + e.getMessage());
            return false;
        }
    }

    //MUSICA
    public Object[] listaMusicaComAlbum(Artista a) {
        try {
            out.writeObject("ListaMusicaComAlbum");
            in.readObject();
            out.writeObject(a);
            return (Object[]) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("ERRO: " + e.getMessage());
            return null;
        }
    }

    //SUGESTOES
    public boolean sugerirTag(Tag tag, Tag tagPai, Object conteudo) {
        //tag = [0], tagPai = [1], conteudo(Comunidade, Musica ou Album) = [2]
        Object[] tagTagPaiConteudo = {tag, tagPai, conteudo};
        try {
            out.writeObject("SugerirTag");
            in.readObject();//ok
            out.writeObject(tagTagPaiConteudo);
            return (boolean) in.readObject();

        } catch (IOException | ClassNotFoundException err) {
            System.err.println("ERRO EM ConexaoController.sugerirTag() = " + err.getMessage());
            return false;
        }
    }

    public boolean excluirSugestao(Tag tagSugerida, Object conteudo) {
        //tag = [0], conteudo = [1]
        Object[] tagEConteudo = {tagSugerida, conteudo};
        try {
            out.writeObject("ExcluirSugestao");
            in.readObject();
            out.writeObject(tagEConteudo);
            return (boolean) in.readObject();
        } catch (IOException | ClassNotFoundException err) {
            System.err.println("ERRO EM ConexaoController.excluirSugestao() = " + err.getMessage());
            return false;
        }
    }

    public ArrayList<Tag> getListaSugestoes(Object conteudo) {
        try {
            out.writeObject("GetListaSugestoes");
            in.readObject();
            out.writeObject(conteudo);
            return (ArrayList<Tag>) in.readObject();
        } catch (IOException | ClassNotFoundException err) {
            System.err.println("ERRO EM ConexaoController.getListaSugestoes() = " + err.getMessage());
            return null;
        }
    }

    public boolean validarSugestao(Tag t, Object conteudo) {
        //tag = [0];
        //conteudo = [1];
        Object[] tagEConteudo = {t, conteudo};
        try {
            out.writeObject("ValidarSugestao");
            in.readObject();
            out.writeObject(tagEConteudo);
            return (boolean) in.readObject();
        } catch (IOException | ClassNotFoundException err) {
            System.err.println("ERRO EM ConexaoController.validarSugestao() = " + err.getMessage());
            return false;
        }
    }

    public boolean vincularTagGeralNaoSugerida(Tag t, Object conteudo) {
        //tag[0]
        //conteudo = [1] 
        Object[] tagEConteudo = {t, conteudo};
        try {
            out.writeObject("VincularTagGeralNaoSugerida");
            in.readObject();
            out.writeObject(tagEConteudo);
            return (boolean) in.readObject();
        } catch (IOException | ClassNotFoundException err) {
            System.err.println("ERRO EM ConexaoController.vincularTagGeralNaoSugerida() = " + err.getMessage());
            return false;
        }
    }

    public boolean desvincularTagGeralNaoSugerida(Tag t, Object conteudo) {
        //tag[0]
        //conteudo = [1] 
        Object[] tagEConteudo = {t, conteudo};
        try {
            out.writeObject("DesvincularTagGeralNaoSugerida");
            in.readObject();
            out.writeObject(tagEConteudo);
            return (boolean) in.readObject();
        } catch (IOException | ClassNotFoundException err) {
            System.err.println("ERRO EM ConexaoController.desvincularTagGeralNaoSugerida() = " + err.getMessage());
            return false;
        }
    }

    public ArrayList<Tag> getListaTags(Album a) {
        try {
            out.writeObject("GetListaTagsAlbum");
            in.readObject();
            out.writeObject(a);
            return (ArrayList<Tag>) in.readObject();
        } catch (IOException | ClassNotFoundException err) {
            System.err.println("ERRO EM ConexaoController.getListaTags() = " + err.getMessage());
            return null;
        }
    }

    //ALBUM
    public ArrayList<Album> getListaAlbum(Artista a) {
        try {
            out.writeObject("GetListaAlbumDoArtista");
            in.readObject();
            out.writeObject(a);
            return (ArrayList<Album>) in.readObject();
        } catch (IOException | ClassNotFoundException err) {
            System.err.println("ERRO EM ConexaoController.getListaAlbum() = " + err.getMessage());
            return null;
        }
    }

    public boolean insertAlbum(Album a) {
        System.out.println("Album: " + a.getNome());
        try {
            out.writeObject("InsertAlbum");
            in.readObject();
            out.writeObject(a);
            return (boolean) in.readObject();
        } catch (IOException | ClassNotFoundException err) {
            System.err.println("ERRO EM ConexaoController.insertAlbum() = " + err.getMessage());
            return false;
        }
    }

    public boolean deleteAlbum(Album a) {
        try {
            out.writeObject("DeleteAlbum");
            in.readObject();
            out.writeObject(a);
            return (boolean) in.readObject();
        } catch (IOException | ClassNotFoundException err) {
            System.err.println("ERRO EM ConexaoController.deleteAlbum() = " + err.getMessage());
            return false;
        }
    }

    public ArrayList<Artista> getListaArtistas() {
        try {
            out.writeObject("GetListaArtistas");
            return (ArrayList<Artista>) in.readObject();
        } catch (IOException | ClassNotFoundException err) {
            System.err.println("ERRO EM ConexaoController.deleteAlbum() = " + err.getMessage());
            return null;
        }
    }

    public boolean insertMusica(Musica m, Album a) {
        Object[] musicaAlbum = {m, a};
        /*
        m = (Musica) musicaAlbum[0];
        a = (Album) musicaAlbum[1];
         */
        try {
            out.writeObject("InsertMusica");
            in.readObject();
            out.writeObject(musicaAlbum);
            return (boolean)in.readObject();
        } catch (IOException | ClassNotFoundException err) {
            System.err.println("ERRO EM ConexaoController.insertMusica() = " + err.getMessage());
            return false;
        }
    }
    
    public boolean deleteMusica(Musica m){
        try {
            out.writeObject("DeleteMusica");
            in.readObject();
            out.writeObject(m);
            return (boolean)in.readObject();
        } catch (IOException | ClassNotFoundException err) {
            System.err.println("ERRO EM ConexaoController.deleteMusica() = " + err.getMessage());
            return false;
        }
    }

    public boolean confirmarSenha(Artista u, String senhaCriptografada){
        Object[] uSenha = {u,senhaCriptografada};
        try {
            out.writeObject("ConfirmarSenha");
            in.readObject();
            out.writeObject(uSenha);
            return (boolean)in.readObject();
        } catch (IOException | ClassNotFoundException err) {
            System.err.println("ERRO EM ConexaoController.confirmarSenha() = " + err.getMessage());
            return false;
        }
    }
    
    public boolean deleteUsuario(Usuario u){
        try {
            out.writeObject("DeleteUsuario");
            in.readObject();
            out.writeObject(u);
            return (boolean)in.readObject();
        } catch (IOException | ClassNotFoundException err) {
            System.err.println("ERRO EM ConexaoController.deleteUsuario() = " + err.getMessage());
            return false;
        }
    }
    
    //GETTERS E SETTERS USUARIO LOGADO
    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }
}
