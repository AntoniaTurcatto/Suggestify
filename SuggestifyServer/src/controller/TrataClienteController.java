package controller;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import modelDominio.*;
import model.*;

public class TrataClienteController extends Thread {

    private int idUnico;
    private Socket cliente;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public TrataClienteController(int idUnico, Socket cliente) {
        this.idUnico = idUnico;
        this.cliente = cliente;

        try {
            out = new ObjectOutputStream(cliente.getOutputStream());
            in = new ObjectInputStream(cliente.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String comando;
        System.out.println("Esperando comandos do cliente" + idUnico);
        //tratamento de comandos
        try {
            comando = (String) in.readObject();
            while (!comando.equalsIgnoreCase("fim")) {
                System.out.println("Cliente " + idUnico + " enviou o comando " + comando);

                if (comando.equalsIgnoreCase("UsuarioEfetuarLogin")) {
                    // enviando o OK para o cliente
                    out.writeObject("ok");
                    // recebendo o usuaário do cliente
                    Usuario user = (Usuario) in.readObject();
                    System.out.println(user.toString());

                    // o NULO indiica que o usuário nao existe
                    UsuarioDao usdao = new UsuarioDao();
                    Usuario userLogin = usdao.efetuarLogin(user);
                    out.writeObject(userLogin);

                } else if (comando.equalsIgnoreCase("UsuarioEfetuarLoginComId")) {
                    // enviando o OK para o cliente
                    out.writeObject("ok");
                    // recebendo o usuaário do cliente
                    Usuario user = (Usuario) in.readObject();
                    System.out.println(user.toString());

                    // o NULO indiica que o usuário nao existe
                    UsuarioDao usdao = new UsuarioDao();
                    Usuario userLogin = usdao.efetuarLoginComId(user);
                    out.writeObject(userLogin);

                } else if (comando.equalsIgnoreCase("CarregaUsuarioComumComComunidadesETags")) {
                    out.writeObject("ok");
                    out.writeObject(new UsuarioDao().carregaUsuarioComumComComunidadesETags((Comum) in.readObject()));

                } else if (comando.equalsIgnoreCase("CadastrarUsuario")) {
                    out.writeObject("ok");
                    out.writeObject(new UsuarioDao().cadastrar((Usuario) in.readObject()));

                } else if (comando.equalsIgnoreCase("CriarComunidade")) {
                    out.writeObject("ok");
                    byte[] background = (byte[]) in.readObject();
                    out.writeObject("ok");
                    Artista artista = (Artista) in.readObject();
                    out.writeObject("ok");
                    //String[] corFundoPostECorFontePost é o próximo
                    //[0] = corFundoPost
                    //[1] = corFontePost
                    out.writeObject(new ComunidadeDao().insereComunidade(artista, background, (String[]) in.readObject()));

                } else if (comando.equalsIgnoreCase("CarregaPostsComunidade")) {
                    out.writeObject("ok");
                    out.writeObject(new PublicacaoDao().carregaPostsComunidade((Comunidade) in.readObject()));

                } else if (comando.equalsIgnoreCase("GetComunidadePeloArtistaId")) {
                    out.writeObject("ok");
                    out.writeObject(new ComunidadeDao().getComunidadePeloArtistaId((int) in.readObject()));

                } else if (comando.equalsIgnoreCase("EditarUsuario")) {
                    out.writeObject("ok");
                    out.writeObject(new UsuarioDao().editarUsuario((Usuario) in.readObject()));

                } else if (comando.equalsIgnoreCase("GetListaTags")) {
                    out.writeObject(new TagDao().getListaTags());

                } else if (comando.equalsIgnoreCase("GetListaTagsUsuario")) {
                    out.writeObject("ok");
                    System.out.println("Enviando ok");
                    out.writeObject(new TagDao().getListaTags((Usuario) in.readObject()));

                } else if (comando.equalsIgnoreCase("InserirTagSemUsuario")) {
                    out.writeObject("ok");
                    out.writeObject(new TagDao().inserirTagSemUsuario((Tag) in.readObject()));

                } else if (comando.equalsIgnoreCase("EditarTag")) {
                    out.writeObject("ok");
                    out.writeObject(new TagDao().editarTag((Tag) in.readObject()));

                } else if (comando.equalsIgnoreCase("ExcluirTag")) {
                    out.writeObject("ok");
                    out.writeObject(new TagDao().excluirTag((Tag) in.readObject()));

                } else if (comando.equalsIgnoreCase("VincularUsuarioTag")) {
                    out.writeObject("ok");
                    out.writeObject(new UsuarioTagDao().vincularUsuarioTag((Object[]) in.readObject()));

                } else if (comando.equalsIgnoreCase("DesvincularUsuarioTag")) {
                    out.writeObject("ok");
                    out.writeObject(new UsuarioTagDao().desvincularUsuarioTag((Object[]) in.readObject()));

                } else if (comando.equalsIgnoreCase("InserePublicacaoComum")) {
                    out.writeObject("ok");
                    //[0] = publicacaoComum
                    //[1] = comunidade
                    out.writeObject(new PublicacaoDao().inserePublicacaoComum((Object[]) in.readObject()));

                } else if (comando.equalsIgnoreCase("Curtir")) {
                    out.writeObject("ok");
                    //[0] = publicacao;
                    //[1] = usuario;
                    Object[] publicacaoUsuario = (Object[]) in.readObject();
                    out.writeObject(new PublicacaoDao().curtir((Publicacao) publicacaoUsuario[0])
                            && new PublicacaoDao().criaAssociacaoUsuarioPublicacaoLike(publicacaoUsuario));

                } else if (comando.equalsIgnoreCase("UsuarioJaCurtiu")) {
                    out.writeObject("ok");
                    //[0] = publicacao;
                    //[1] = usuario;
                    out.writeObject(new PublicacaoDao().usuarioJaCurtiu((Object[]) in.readObject()));

                } else if (comando.equalsIgnoreCase("Descurtir")) {
                    out.writeObject("ok");
                    //[0] = publicacao;
                    //[1] = usuario;
                    Object[] publicacaoUsuario = (Object[]) in.readObject();
                    out.writeObject(new PublicacaoDao().descurtir((Publicacao) publicacaoUsuario[0])
                            && new PublicacaoDao().deletaAssociacaoUsuarioPublicacaoLike(publicacaoUsuario));

                } else if (comando.equalsIgnoreCase("EditarComunidade")) {
                    out.writeObject("ok");
                    Comunidade c = (Comunidade) in.readObject();
                    System.out.println("imediatamente após receber, cor fundo: " + c.getCorFundoPost());
                    System.out.println("imediatamente após receber, cor fonte: " + c.getCorFontePost());
                    out.writeObject(new ComunidadeDao().editarComunidade(c));

                } else if (comando.equalsIgnoreCase("GetListaComunidade")) {
                    out.writeObject(new ComunidadeDao().getListaComunidade());
                    
                } else if (comando.equalsIgnoreCase("GetListaComunidadeUsuario")) { 
                    out.writeObject("ok");
                    out.writeObject(new ComunidadeDao().getListaComunidade((Usuario)in.readObject())); 

                } else if (comando.equalsIgnoreCase("GetListaAlbum")) {
                    out.writeObject(new AlbumDao().getListaAlbum());

                } else if (comando.equalsIgnoreCase("GetListaAlbumTag")) {
                    out.writeObject("ok");
                    out.writeObject(new AlbumDao().getListaAlbum((Tag) in.readObject()));

                } else if (comando.equalsIgnoreCase("ListaMusica")) {
                    out.writeObject(new MusicaDao().listaMusica());

                } else if (comando.equalsIgnoreCase("ListaMusicaTag")) {
                    out.writeObject("ok");
                    out.writeObject(new MusicaDao().listaMusica((Tag) in.readObject()));

                } else if (comando.equalsIgnoreCase("ListaMusicaArtista")) {
                    out.writeObject("ok");
                    out.writeObject(new MusicaDao().listaMusica((Artista) in.readObject()));
                    
                } else if(comando.equalsIgnoreCase("ListaMusicaAlbum")){
                    out.writeObject("ok");
                    out.writeObject(new MusicaDao().listaMusica((Album)in.readObject()));

                } else if (comando.equalsIgnoreCase("CarregaPostsUsuario")) {
                    out.writeObject("ok");
                    out.writeObject(new PublicacaoDao().carregaPostsUsuario((Usuario) in.readObject()));

                } else if (comando.equalsIgnoreCase("ListaMusicaComAlbum")) {
                    out.writeObject("ok");
                    out.writeObject(new MusicaDao().listaMusicaComAlbum((Artista) in.readObject()));

                } else if (comando.equalsIgnoreCase("GetListaTagsDeMusica")) {
                    out.writeObject("ok");
                    out.writeObject(new TagDao().getListaTags((Musica) in.readObject()));

                } else if (comando.equalsIgnoreCase("VinculaUsuarioComunidade")) {
                    out.writeObject("ok");
                    Object[] uComumEComunidade = (Object[]) in.readObject();
                    out.writeObject(new UsuarioComunidadeDao()
                            .vinculaUsuarioComunidade((Comum) uComumEComunidade[0], (Comunidade) uComumEComunidade[1]));

                } else if (comando.equalsIgnoreCase("DesvinculaUsuarioComunidade")) {
                    out.writeObject("ok");
                    Object[] uComumEComunidade = (Object[]) in.readObject();
                    out.writeObject(new UsuarioComunidadeDao()
                            .desvinculaUsuarioComunidade((Comum) uComumEComunidade[0], (Comunidade) uComumEComunidade[1]));

                } else if (comando.equalsIgnoreCase("GetListaComunidadesDoUsuario")) {
                    out.writeObject("ok");
                    out.writeObject(new UsuarioComunidadeDao().getListaComunidadesDoUsuario((Comum) in.readObject()));

                    
                //SUGESTOES E TAGS
                } else if (comando.equalsIgnoreCase("SugerirTag")) {
                    out.writeObject("ok");
                    //tag = [0], tagPai = [1], conteudo(Comunidade, Musica ou Album) = [2]
                    out.writeObject(new TagDao().sugerirTag((Object[])in.readObject()));
                    
                } else if (comando.equalsIgnoreCase("ExcluirSugestao")) {
                    out.writeObject("ok");
                    //tag = [0], conteudo = [1]
                    out.writeObject(new TagDao().excluirSugestao((Object[])in.readObject()));
                    
                } else if(comando.equalsIgnoreCase("GetListaSugestoes")){
                    out.writeObject("ok");
                    //o casting do conteudo para seu conteudo específico (comunidade, musica ou album)
                    //  será feito na DAO
                    out.writeObject(new TagDao().getListaSugestoes((Object) in.readObject())); 
                    
                } else if(comando.equalsIgnoreCase("ValidarSugestao")){
                    out.writeObject("ok");
                    //tag = [0];
                    //conteudo = [1];
                    out.writeObject(new TagDao().validarSugestao((Object[])in.readObject())); 
                
                } else if(comando.equalsIgnoreCase("VincularTagGeralNaoSugerida")){
                    out.writeObject("ok");
                    //tag[0]
                    //conteudo = [1] 
                    out.writeObject(new TagDao().vincularTagGeralNaoSugerida((Object[])in.readObject()));
                    
                } else if(comando.equalsIgnoreCase("DesvincularTagGeralNaoSugerida")){
                    out.writeObject("ok");
                    //tag[0]
                    //conteudo = [1] 
                    out.writeObject(new TagDao().desvincularTagGeralNaoSugerida((Object[])in.readObject()));
                    
                } else if(comando.equalsIgnoreCase("GetComunidadePeloId")){
                    out.writeObject("ok");
                    out.writeObject(new ComunidadeDao().getComunidadePeloId((int)in.readObject()));
                    
                } else if(comando.equalsIgnoreCase("GetListaAlbumDoArtista")){
                    out.writeObject("ok");
                    out.writeObject(new AlbumDao().getListaAlbum((Artista)in.readObject()));
                    
                } else if(comando.equalsIgnoreCase("GetListaTagsAlbum")){
                    out.writeObject("ok"); 
                    out.writeObject(new TagDao().getListaTags((Album)in.readObject()));
                
                } else if(comando.equalsIgnoreCase("InsertAlbum")){
                    out.writeObject("ok");
                    out.writeObject(new AlbumDao().insertAlbum((Album)in.readObject()));
                    
                } else if(comando.equalsIgnoreCase("DeleteAlbum")) {
                    out.writeObject("ok");
                    out.writeObject(new AlbumDao().deleteAlbum((Album)in.readObject())); 
                    
                } else if(comando.equalsIgnoreCase("GetListaArtistas")){
                    out.writeObject(new UsuarioDao().getListaArtistas()); 
                    
                } else if(comando.equalsIgnoreCase("InsertMusica")){
                    out.writeObject("ok");
                    System.out.println("Musica recebida");
                    out.writeObject(new MusicaDao().insertMusica((Object[])in.readObject()));
                
                } else if(comando.equalsIgnoreCase("DeleteMusica")){
                    out.writeObject("ok");
                    out.writeObject(new MusicaDao().deleteMusica((Musica)in.readObject()));
                    
                } else if(comando.equalsIgnoreCase("ConfirmarSenha")){
                    out.writeObject("ok");
                    out.writeObject(new UsuarioDao().confirmarSenha((Object[])in.readObject())); 
                    
                } else if(comando.equalsIgnoreCase("DeleteUsuario")){
                    out.writeObject("ok");
                    out.writeObject(new UsuarioDao().deleteUsuario((Usuario)in.readObject())); 
                
                } else if(comando.equalsIgnoreCase("GetTagsRecomendadas")){
                    out.writeObject("ok");
                    out.writeObject(new TagDao().getTagsRecomendadas((Tag)in.readObject())); 
                
                } else if(comando.equalsIgnoreCase("ListaMusicaDeListaDeTag")){
                    out.writeObject("ok");
                    out.writeObject(new MusicaDao().listaMusica((ArrayList<Tag>)in.readObject()));
                    
                } else if(comando.equalsIgnoreCase("ListaAlbumDeListaDeTag")){
                    out.writeObject("ok");
                    out.writeObject(new AlbumDao().getListaAlbum((ArrayList<Tag>)in.readObject()));
                     
                } else if(comando.equalsIgnoreCase("ListaComunidadeDeListaDeTag")){
                    out.writeObject("ok");
                    out.writeObject(new ComunidadeDao().getListaComunidade((ArrayList<Tag>)in.readObject())); 
                    
                } else if(comando.equalsIgnoreCase("GetAlbumPelaMusica")){
                    out.writeObject("ok");
                    out.writeObject(new AlbumDao().getAlbum((Musica)in.readObject())); 
                    
                } else if(comando.equalsIgnoreCase("ExcluirPublicacoesDeUsuarioNaComunidade")){
                    out.writeObject("ok");
                    out.writeObject(new PublicacaoDao().excluirPublicacoesDeUsuarioNaComunidade((Object[])in.readObject()));
                    
                }

                //  NÃO APAGAR ESSA LINHA, 
                //É A RESPONSÁVEL POR LER O PRÓXIMO COMANDO
                comando = (String) in.readObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //fechando conexão
        try {
            System.out.println("cliente " + idUnico + " fechou a conexão.");
            in.close();
            out.close();
            cliente.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
