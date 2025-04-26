package model;

import factory.Conector;
import java.sql.*;
import java.util.ArrayList;
import modelDominio.Artista;
import modelDominio.Comentario;
import modelDominio.Comum;
import modelDominio.Comunidade;
import modelDominio.Publicacao;
import modelDominio.PublicacaoComum;
import modelDominio.Usuario;

public class PublicacaoDao {

    private Connection con;

    public PublicacaoDao() {
        con = Conector.getConnection();
    }

    public ArrayList<PublicacaoComum> carregaPostsComunidade(Comunidade comunidade) {
        PreparedStatement stmt = null;
        ArrayList<PublicacaoComum> posts = new ArrayList();
        PublicacaoComum publicacaoSelecionada = null;
        try {
            String sql = "SELECT posts.idPublicacao,"//POST
                    + " posts.texto as postTexto,"
                    + " posts.likes as postLike,"
                    + " posts.imagem as postImagem,"
                    + " posts.fkUsuario as postIdUsuario,"
                    + " posts.fkComunidade as postIdComunidade,"
                    + " u.idUsuario as idUsuarioPost," //uPOST
                    + " u.nome as usuarioPostNome,"
                    + " u.email as usuarioPostEmail,"
                    + " u.senha as usuarioPostSenha,"
                    + " u.tipo as usuarioPostTipo,"
                    + " u.nomeArtistico as usuarioPostNomeArte,"
                    + " u.fotoPerfil as usuarioPostFoto,"
                    + " com.idComentario," //COM
                    + " com.texto as comTexto,"
                    + " com.likes as comLikes,"
                    + " com.fkUsuario as comIdUsuario,"
                    + " com.fkPublicacao as comIdPublicacao,"
                    + " ucom.idUsuario as idUsuarioCom," //uCOM
                    + " ucom.nome as usuarioComNome,"
                    + " ucom.email as usuarioComEmail,"
                    + " ucom.senha as usuarioComSenha,"
                    + " ucom.tipo as  usuarioComTipo,"
                    + " ucom.nomeArtistico as usuarioComNomeArte,"
                    + " ucom.fotoPerfil as usuarioComFoto" //fim colunas
                    + " FROM publicacao posts"
                    + " INNER join usuario u"
                    + " on u.idUsuario = posts.fkUsuario"
                    + " LEFT JOIN comentario com"
                    + " on posts.idPublicacao = com.fkPublicacao"
                    + " LEFT join usuario ucom"
                    + " on ucom.idUsuario=com.fkUsuario"
                    + " WHERE posts.fkComunidade = ?" //comunidade passada como argumento
                    + " order by posts.idPublicacao DESC";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, comunidade.getIdComunidade());
            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                Usuario u;
                //se a publicacao ainda não foi criada (primeira vez percorrendo o ResultSet) 
                //ou a que foi criada anteriormente tem um iD diferente da do loop atual do ResultSet (terminou de listar os comentarios e está listando um novo post)
                if (publicacaoSelecionada == null || res.getInt("idPublicacao") != publicacaoSelecionada.getIdPublicacao()) {
                    if (res.getInt("usuarioPostTipo") == 1) { //ARTISTA
                        u = new Artista(res.getInt("idUsuarioPost"),
                                res.getString("usuarioPostNome"),
                                res.getString("usuarioPostEmail"),
                                res.getString("usuarioPostSenha"),
                                res.getString("usuarioPostNomeArte"),
                                res.getBytes("usuarioPostFoto"));
                    } else { //COMUM (utilizando o 3° construtor)
                        u = new Comum(res.getInt("idUsuarioPost"),
                                res.getString("usuarioPostNome"),
                                res.getString("usuarioPostEmail"),
                                res.getString("usuarioPostSenha"),
                                res.getBytes("usuarioPostFoto"));
                    }

                    /*
                    public PublicacaoComum(int idPublicacao, Usuario usuario, String texto, int likes, byte[] imagem) {
                        super(idPublicacao, usuario, texto, likes);
                        this.imagem = imagem;
                        listaComentarios = new ArrayList();
                    }
                     */
                    String postTexto = res.getString("postTexto");
                    boolean temTexto = !res.wasNull();

                    byte[] fotoPost = res.getBytes("postImagem");
                    boolean temImagem = !res.wasNull();
                    
                    if(temTexto){
                        
                        if(temImagem){
                            //tem tudo
                            publicacaoSelecionada = new PublicacaoComum(res.getInt("idPublicacao"),
                            u,
                            res.getString("postTexto"),
                            res.getInt("postLike"),
                            res.getBytes("postImagem"));
                        } else {
                            //só texto
                            publicacaoSelecionada = new PublicacaoComum(res.getInt("idPublicacao"),
                            u,
                            res.getString("postTexto"),
                            res.getInt("postLike"));
                        }
                    } else {
                        if(temImagem){
                            //só imagem
                            publicacaoSelecionada = new PublicacaoComum(res.getInt("idPublicacao"),
                            u,
                            res.getInt("postLike"),
                            res.getBytes("postImagem"));
                            
                        }
                    }

                    if(publicacaoSelecionada != null){
                        posts.add(publicacaoSelecionada);
                    }
                }

                //----- AQUI COMEÇA A ADICIONAR COMENTARIO E USUARIO DE COMENTÁRIO
                //se não está nulo
                if (res.getInt("idComentario") != 0) {
                    Usuario ucom;
                    if (res.getInt("usuarioComTipo") == 1) { //ARTISTA
                        ucom = new Artista(res.getInt("idUsuarioCom"),
                                res.getString("usuarioComNome"),
                                res.getString("usuarioComEmail"),
                                res.getString("usuarioComSenha"),
                                res.getString("usuarioComNomeArte"),
                                res.getBytes("usuarioComFoto"));
                    } else { //COMUM (utilizando o 3° construtor)
                        ucom = new Comum(res.getInt("idUsuarioCom"),
                                res.getString("usuarioComNome"),
                                res.getString("usuarioComEmail"),
                                res.getString("usuarioComSenha"),
                                res.getBytes("usuarioComFoto"));
                    }
                    /*
                        public Comentario(int idPublicacao, Usuario usuario, String texto, int likes) {
                            super(idPublicacao, usuario, texto, likes);
                        }
                     */
                    Comentario com = new Comentario(res.getInt("idComentario"),
                            ucom,
                            res.getString("comTexto"),
                            res.getInt("comLikes"));
                    //caso o post foi recém adicionado, adiciona nele
                    //caso foi adicionado no último loop, também adiciona nele
                    posts.getLast().getListaComentarios().add(com);
                }
            }
            res.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(posts.size());
        return posts;
    }

    public ArrayList<PublicacaoComum> carregaPostsUsuario(Usuario uWhere) {
        PreparedStatement stmt = null;
        ArrayList<PublicacaoComum> posts = new ArrayList();
        PublicacaoComum publicacaoSelecionada = null;
        try {
            String sql = "SELECT posts.idPublicacao,"//POST
                    + " posts.texto as postTexto,"
                    + " posts.likes as postLike,"
                    + " posts.imagem as postImagem,"
                    + " posts.fkUsuario as postIdUsuario,"
                    + " posts.fkComunidade as postIdComunidade,"
                    + " u.idUsuario as idUsuarioPost," //uPOST
                    + " u.nome as usuarioPostNome,"
                    + " u.email as usuarioPostEmail,"
                    + " u.senha as usuarioPostSenha,"
                    + " u.tipo as usuarioPostTipo,"
                    + " u.nomeArtistico as usuarioPostNomeArte,"
                    + " u.fotoPerfil as usuarioPostFoto,"
                    + " com.idComentario," //COM
                    + " com.texto as comTexto,"
                    + " com.likes as comLikes,"
                    + " com.fkUsuario as comIdUsuario,"
                    + " com.fkPublicacao as comIdPublicacao,"
                    + " ucom.idUsuario as idUsuarioCom," //uCOM
                    + " ucom.nome as usuarioComNome,"
                    + " ucom.email as usuarioComEmail,"
                    + " ucom.senha as usuarioComSenha,"
                    + " ucom.tipo as  usuarioComTipo,"
                    + " ucom.nomeArtistico as usuarioComNomeArte,"
                    + " ucom.fotoPerfil as usuarioComFoto" //fim colunas
                    + " FROM publicacao posts"
                    + " INNER join usuario u"
                    + " on u.idUsuario = posts.fkUsuario"
                    + " LEFT JOIN comentario com"
                    + " on posts.idPublicacao = com.fkPublicacao"
                    + " LEFT join usuario ucom"
                    + " on ucom.idUsuario=com.fkUsuario"
                    + " WHERE u.idUsuario = ?" //usuario passado como argumento
                    + " order by posts.idPublicacao DESC";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, uWhere.getCodUsuario());
            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                Usuario u;
                //se a publicacao ainda não foi criada (primeira vez percorrendo o ResultSet) 
                //ou a que foi criada anteriormente tem um iD diferente da do loop atual do ResultSet (terminou de listar os comentarios e está listando um novo post)
                if (publicacaoSelecionada == null || res.getInt("idPublicacao") != publicacaoSelecionada.getIdPublicacao()) {
                    if (res.getInt("usuarioPostTipo") == 1) { //ARTISTA
                        u = new Artista(res.getInt("idUsuarioPost"),
                                res.getString("usuarioPostNome"),
                                res.getString("usuarioPostEmail"),
                                res.getString("usuarioPostSenha"),
                                res.getString("usuarioPostNomeArte"),
                                res.getBytes("usuarioPostFoto"));
                    } else { //COMUM (utilizando o 3° construtor)
                        u = new Comum(res.getInt("idUsuarioPost"),
                                res.getString("usuarioPostNome"),
                                res.getString("usuarioPostEmail"),
                                res.getString("usuarioPostSenha"),
                                res.getBytes("usuarioPostFoto"));
                    }

                    /*
                    public PublicacaoComum(int idPublicacao, Usuario usuario, String texto, int likes, byte[] imagem) {
                        super(idPublicacao, usuario, texto, likes);
                        this.imagem = imagem;
                        listaComentarios = new ArrayList();
                    }
                     */
                    publicacaoSelecionada = new PublicacaoComum(res.getInt("idPublicacao"),
                            u,
                            res.getString("postTexto"),
                            res.getInt("postLike"),
                            res.getBytes("postImagem"));
                    posts.add(publicacaoSelecionada);
                }

                //----- AQUI COMEÇA A ADICIONAR COMENTARIO E USUARIO DE COMENTÁRIO
                //se não está nulo
                if (res.getInt("idComentario") != 0) {
                    Usuario ucom;
                    if (res.getInt("usuarioComTipo") == 1) { //ARTISTA
                        ucom = new Artista(res.getInt("idUsuarioCom"),
                                res.getString("usuarioComNome"),
                                res.getString("usuarioComEmail"),
                                res.getString("usuarioComSenha"),
                                res.getString("usuarioComNomeArte"),
                                res.getBytes("usuarioComFoto"));
                    } else { //COMUM (utilizando o 3° construtor)
                        ucom = new Comum(res.getInt("idUsuarioCom"),
                                res.getString("usuarioComNome"),
                                res.getString("usuarioComEmail"),
                                res.getString("usuarioComSenha"),
                                res.getBytes("usuarioComFoto"));
                    }
                    /*
                        public Comentario(int idPublicacao, Usuario usuario, String texto, int likes) {
                            super(idPublicacao, usuario, texto, likes);
                        }
                     */
                    Comentario com = new Comentario(res.getInt("idComentario"),
                            ucom,
                            res.getString("comTexto"),
                            res.getInt("comLikes"));
                    //caso o post foi recém adicionado, adiciona nele
                    //caso foi adicionado no último loop, também adiciona nele
                    posts.getLast().getListaComentarios().add(com);
                }
            }
            res.close();
            stmt.close();
        } catch (SQLException e) {
            posts = null;
            System.out.println("ERRO SQL: " + e.getMessage());
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("ERRO SQL: " + e.getMessage());
            }
        }
        return posts;
    }

    //PublicacaoComum pComum, Comunidade comunidade
    public boolean inserePublicacaoComum(Object[] pComumEComunidade) {
        PublicacaoComum pComum = (PublicacaoComum) pComumEComunidade[0];
        Comunidade comunidade = (Comunidade) pComumEComunidade[1];
        PreparedStatement stmt = null;
        boolean result = false;
        String sql = "INSERT INTO publicacao(idPublicacao,texto,likes,imagem,fkUsuario,fkComunidade)"
                + " VALUES(null,?,?,?,?,?)";
        try {
            con.setAutoCommit(false);
            stmt = con.prepareStatement(sql);
            
            if(pComum.getTexto() != null){
                stmt.setString(1, pComum.getTexto());//texto
                System.out.println("texto NÃO NULL");
            } else {
                stmt.setNull(1, java.sql.Types.VARCHAR);
                System.out.println("texto null");
            }
            
            stmt.setInt(2, 0); //likes
            
            if(pComum.getImagem() != null){
                stmt.setBytes(3, pComum.getImagem());//imagem
                System.out.println("imagem NÃO NULL");
            } else {
                System.out.println("imagem null");
                stmt.setNull(3, java.sql.Types.BLOB); 
            }
            
            stmt.setInt(4, pComum.getUsuario().getCodUsuario());//usuario
            stmt.setInt(5, comunidade.getIdComunidade());//comunidade
            stmt.execute();
            con.commit();
            result = true;
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            result = false;
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public boolean curtir(Publicacao p) {
        PreparedStatement stmt;
        boolean res;
        try {
            con.setAutoCommit(false);
            String sql;
            if (p instanceof PublicacaoComum) {
                sql = "update publicacao set likes = ? where idPublicacao = ?";
            } else { //comentario
                sql = "update comentario set likes = ? where idComentario = ?";
            }
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, p.getLikes() + 1);
            stmt.setInt(2, p.getIdPublicacao());

            stmt.execute();
            con.commit();

            res = true;
            stmt.close();
        } catch (SQLException e) {
            res = false;
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("curtir retorno = " + res);
        return res;
    }

    public boolean descurtir(Publicacao p) {
        PreparedStatement stmt;
        boolean res;
        try {
            con.setAutoCommit(false);
            String sql;
            if (p instanceof PublicacaoComum) {
                sql = "update publicacao set likes = ? where idPublicacao = ?";
            } else { //comentario
                sql = "update comentario set likes = ? where idComentario = ?";
            }
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, p.getLikes() - 1);
            stmt.setInt(2, p.getIdPublicacao());

            stmt.execute();
            con.commit();

            res = true;
            stmt.close();
        } catch (SQLException e) {
            res = false;
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("descurtir retorno = " + res);
        return res;
    }

    public boolean usuarioJaCurtiu(Object[] publicacaoUsuario) {

        Publicacao p = (Publicacao) publicacaoUsuario[0];
        Usuario u = (Usuario) publicacaoUsuario[1];

        PreparedStatement stmt = null;
        boolean res;
        try {
            con.setAutoCommit(false);
            String sql;
            if (p instanceof PublicacaoComum) {
                sql = "SELECT * FROM publicacaoQueUsuarioCurtiu"
                        + " WHERE fkPublicacao=? AND fkUsuario = ?";
            } else { //comentario
                sql = "SELECT * FROM comentarioqueusuariocurtiu"
                        + " WHERE fkComentario=? AND fkUsuario=?";
            }

            stmt = con.prepareStatement(sql);
            stmt.setInt(1, p.getIdPublicacao());
            stmt.setInt(2, u.getCodUsuario());

            //retorna TRUE se retorna um ResultSet
            //false se não encontra registro (ou quando encontra o retorno de um update)
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                res = true;
            } else {
                res = false;
            }
            con.commit();
            stmt.close();
        } catch (SQLException e) {
            res = false;
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("ja curtiu = " + res);
        return res;
    }

    public boolean criaAssociacaoUsuarioPublicacaoLike(Object[] publicacaoUsuario) {
        Publicacao p = (Publicacao) publicacaoUsuario[0];
        Usuario u = (Usuario) publicacaoUsuario[1];

        PreparedStatement stmt = null;
        boolean res;

        try {
            con.setAutoCommit(false);
            String sql;

            if (p instanceof PublicacaoComum) {
                sql = "INSERT INTO publicacaoQueUsuarioCurtiu(idPublicacaoQueUsuarioCurtiu,fkUsuario,fkPublicacao)"
                        + " VALUES(null,?,?)";
            } else { // é comentario
                sql = "INSERT INTO comentarioqueusuariocurtiu(idComentarioQueUsuarioCurtiu,fkUsuario,fkComentario)"
                        + " VALUES(null,?,?)";
            }

            stmt = con.prepareStatement(sql);
            stmt.setInt(1, u.getCodUsuario());
            stmt.setInt(2, p.getIdPublicacao());

            stmt.execute();
            con.commit();
            res = true;
            stmt.close();
        } catch (SQLException e) {
            res = false;
            try {
                con.rollback();
                System.out.println(e.getMessage());
            } catch (SQLException e1) {
                e1.printStackTrace();
                System.out.println(e1.getMessage());
            }
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
        System.out.println("criada a associacao = " + res);
        return res;
    }

    public boolean deletaAssociacaoUsuarioPublicacaoLike(Object[] publicacaoUsuario) {
        Publicacao p = (Publicacao) publicacaoUsuario[0];
        Usuario u = (Usuario) publicacaoUsuario[1];

        PreparedStatement stmt = null;
        boolean res;

        try {
            con.setAutoCommit(false);
            String sql;

            if (p instanceof PublicacaoComum) {
                sql = "DELETE FROM publicacaoQueUsuarioCurtiu WHERE fkPublicacao = ? AND fkUsuario = ?";
            } else {// é comentario
                sql = "DELETE FROM comentarioqueusuariocurtiu WHERE fkComentario = ? AND fkUsuario = ?";
            }
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, p.getIdPublicacao());
            stmt.setInt(2, u.getCodUsuario());

            stmt.execute();
            con.commit();
            res = true;
            stmt.close();
        } catch (SQLException e) {
            res = false;
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("deletada a associacao = " + res);
        return res;
    }
    
    public boolean excluirPublicacoesDeUsuarioNaComunidade(Object[] usuarioEComunidade){
        
        if(usuarioEComunidade == null){
            System.out.println("o parâmetro não pode ser null!");
            return false;
        } else if(usuarioEComunidade.length != 2){
            System.out.println("o parâmetro deve ter 2 itens!");
            return false;
        } else if(!(usuarioEComunidade[0] instanceof Usuario) || !(usuarioEComunidade[1] instanceof Comunidade)){
            System.out.println("usuarioEComunidade[0] deve ser Usuario e usuarioEComunidade[1] deve ser Comunidade");
            return false;
        }
        
        Usuario u = (Usuario)usuarioEComunidade[0];
        Comunidade c = (Comunidade)usuarioEComunidade[1];
        
        
        PreparedStatement stmt = null;
        boolean res;
        
        try {
            con.setAutoCommit(false);
            
            String sql = "DELETE FROM publicacao WHERE fkUsuario = ? AND fkComunidade = ?";
            
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, u.getCodUsuario());
            stmt.setInt(2, c.getIdComunidade());
            
            stmt.execute();
            
            con.commit();
            res = true;
        } catch (SQLException e) {
            res = false;
            e.printStackTrace();
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            
        } finally {
            try {
                if(stmt!=null)stmt.close();
                if(con!=null)con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return res;
    }
}
