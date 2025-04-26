package model;

import factory.Conector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelDominio.Artista;
import modelDominio.Comum;
import modelDominio.Comunidade;
import modelDominio.Tag;
import modelDominio.Usuario;

public class UsuarioDao {

    private Connection con;

    public UsuarioDao() {
        con = Conector.getConnection();
    }

    public Usuario efetuarLogin(Usuario u) {
        PreparedStatement stmt;
        Usuario usuarioLogado = null;
        List<Comunidade> listaComunidadesResultSet = new ArrayList<>();
        List<Tag> listaTagsResultSet = new ArrayList<>();
        try {
            String sql;
            if (u instanceof Comum) {
                System.out.println("Comum");
                sql = "select comum.*,"
                        + " tag.idTag,"
                        + " tag.nome as nomeTag,"
                        + " comunidade.*,"
                        + " artista.idUsuario as idArtista,"
                        + " artista.nome as nomeArtista,"
                        + " artista.email as emailArtista,"
                        + " artista.senha as senhaArtista,"
                        + " artista.nomeArtistico,"
                        + " artista.fotoPerfil as fotoPerfilArtista,"
                        + " artista.bio as bioArtista"
                        + " from usuario comum"
                        + " LEFT JOIN tagsDoUsuario tgu"
                        + " ON tgu.fkUsuario = comum.idUsuario"
                        + " LEFT JOIN tag"
                        + " ON tgu.fkTag = tag.idTag"
                        + " LEFT JOIN usuariosDaComunidade ucomu"
                        + " ON ucomu.fkUsuario = comum.idUsuario"
                        + " LEFT JOIN comunidade"
                        + " on comunidade.idComunidade = ucomu.fkComunidade"
                        + " LEFT JOIN usuario artista"
                        + " ON artista.idUsuario = comunidade.fkUsuarioPrincipal"
                        + " where comum.senha = ? AND (comum.nome = ? OR comum.email = ?) AND comum.tipo = ?";//para garantir que busque apenas usuario comuns
            } else { //artista
                System.out.println("Artista");
                sql = "select usuario.*, tag.idTag, tag.nome as nomeTag from usuario"
                        + " LEFT JOIN tagsDoUsuario tgu"
                        + " ON tgu.fkUsuario = usuario.idUsuario"
                        + " LEFT JOIN tag"
                        + " ON tgu.fkTag = tag.idTag"
                        + " where senha = ? AND (usuario.nome = ? OR usuario.email = ?) AND usuario.tipo = ?"; //para garantir que busque apenas artistas
            }
            stmt = con.prepareStatement(sql);
            stmt.setString(1, u.getSenha());
            stmt.setString(2, u.getNomeUsuario());
            stmt.setString(3, u.getEmail());

            if (u instanceof Comum) {
                stmt.setInt(4, 0);
            } else {
                stmt.setInt(4, 1);//artista
            }

            ResultSet res = stmt.executeQuery();
            if (res != null) {
                System.out.println("res != null");
            }
            //se encontrou resultado
            if (res.next()) {
                System.out.println("res nomeUser: " + res.getString("nome"));
                if (u instanceof Comum) {
                    System.out.println("tipo Comum");
                    usuarioLogado = new Comum(res.getInt("idUsuario"),
                            res.getString("nome"),
                            res.getString("email"),
                            res.getString("senha"),
                            new ArrayList<>(),
                            new ArrayList<>(),
                            res.getBytes("fotoPerfil"),
                            res.getString("bio"));
                } else { //artista
                    System.out.println("tipo Artista");
                    usuarioLogado = new Artista(res.getInt("idUsuario"),
                            res.getString("nome"),
                            res.getString("email"),
                            res.getString("senha"),
                            new ArrayList<>(),
                            res.getString("nomeArtistico"),
                            res.getBytes("fotoPerfil"),
                            res.getString("bio"));
                }
                System.out.println("cod usuario obtido: " + usuarioLogado.getCodUsuario());
                int idTag;

                do {
                    idTag = res.getInt("idTag");
                    if (!res.wasNull()) {
                        System.out.println("TAG ENCONTRADA: " + idTag);
                        String textoTag = res.getString("nomeTag");
                        listaTagsResultSet.add(new Tag(idTag, textoTag));
                    } else {
                        System.out.println("TAG NÃO ENCONTRADA");
                    }
                    if (u instanceof Comum) {
                        res.getInt("idComunidade");
                        if (!res.wasNull()) {
                            int idComunidade = res.getInt("idComunidade");

                            /*
                            artista.idUsuario as idArtista,
                            artista.nome as nomeArtista,
                            artista.email as emailArtista,
                            artista.senha as senhaArtista,
                            artista.nomeArtistico,
                            artista.fotoPerfil as fotoPerfilArtista,
                            artista.bio as bioArtista
                             */
                            Artista artistaComunidade = new Artista(res.getInt("idArtista"),
                                    res.getString("nomeArtista"),
                                    res.getString("emailArtista"),
                                    res.getString("senhaArtista"),
                                    res.getString("nomeArtistico"),
                                    res.getBytes("fotoPerfilArtista"));

                            listaComunidadesResultSet.add(new Comunidade(idComunidade,
                                    artistaComunidade,
                                    res.getBytes("fotoComunidade"),
                                    res.getString("corFundoDoPost"),
                                    res.getString("corFonteDoPost")));
                        }
                    }

                } while (res.next());

                //terminou de pegar todos os dados do cursor, agora basta filtrar a lista e adiciona ao usuario logado
                //filtrando lista tags e adicionando ao usuario:
                for (int i = 0; i < listaTagsResultSet.size(); i++) {
                    //se for o primeiro row pego OU se o último adicionado na listaTags do usuario é diferente do atual do for
                    if (i == 0 || usuarioLogado.getListaTags().getLast().getIdTag() != listaTagsResultSet.get(i).getIdTag()) {
                        usuarioLogado.getListaTags().add(listaTagsResultSet.get(i));
                    }
                }

                //filtrando a lista de comunidades inscritas
                if (usuarioLogado instanceof Comum uComum) {
                    for (int i = 0; i < listaComunidadesResultSet.size(); i++) {
                        if (i == 0 || uComum.getListaComunidadesInscritas().getLast().getIdComunidade() != listaComunidadesResultSet.get(i).getIdComunidade()) {
                            uComum.getListaComunidadesInscritas().add(listaComunidadesResultSet.get(i));
                        }
                    }
                }
            } else {
                usuarioLogado = null;
            }
            stmt.close();
            res.close();
        } catch (SQLException e) {
            usuarioLogado = null;
            System.out.println("ERRO SQL: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("ERRO SQL: " + e.getMessage());
                e.printStackTrace();
            }
        }
        //System.out.println("return nome:" + usuarioLogado.getNomeUsuario());
        return usuarioLogado;
    }

    public Usuario efetuarLoginComId(Usuario u) {

        PreparedStatement stmt;
        Usuario usuarioLogado = null;
        List<Comunidade> listaComunidadesResultSet = new ArrayList<>();
        List<Tag> listaTagsResultSet = new ArrayList<>();
        try {
            String sql;
            if (u instanceof Comum) {
                System.out.println("Comum");
                sql = "select comum.*,"
                        + " tag.idTag,"
                        + " tag.nome as nomeTag,"
                        + " comunidade.*,"
                        + " artista.idUsuario as idArtista,"
                        + " artista.nome as nomeArtista,"
                        + " artista.email as emailArtista,"
                        + " artista.senha as senhaArtista,"
                        + " artista.nomeArtistico,"
                        + " artista.fotoPerfil as fotoPerfilArtista,"
                        + " artista.bio as bioArtista"
                        + " from usuario comum"
                        + " LEFT JOIN tagsDoUsuario tgu"
                        + " ON tgu.fkUsuario = comum.idUsuario"
                        + " LEFT JOIN tag"
                        + " ON tgu.fkTag = tag.idTag"
                        + " LEFT JOIN usuariosDaComunidade ucomu"
                        + " ON ucomu.fkUsuario = comum.idUsuario"
                        + " LEFT JOIN comunidade"
                        + " on comunidade.idComunidade = ucomu.fkComunidade"
                        + " LEFT JOIN usuario artista"
                        + " ON artista.idUsuario = comunidade.fkUsuarioPrincipal"
                        + " where comum.idUsuario = ?";
            } else { //artista
                System.out.println("Artista");
                sql = "select * from usuario"
                        + " LEFT JOIN tagsDoUsuario tgu"
                        + " ON tgu.fkUsuario = usuario.idUsuario"
                        + " LEFT JOIN tag"
                        + " ON tgu.fkTag = tag.idTag"
                        + " where usuario.idUsuario = ?";
            }
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, u.getCodUsuario());

            ResultSet res = stmt.executeQuery();
            if (res != null) {
                System.out.println("res != null");
            }
            //se encontrou resultado
            if (res.next()) {
                System.out.println("res nomeUser: " + res.getString("nome"));
                if (u instanceof Comum) {
                    usuarioLogado = new Comum(res.getInt("idUsuario"),
                            res.getString("nome"),
                            res.getString("email"),
                            res.getString("senha"),
                            new ArrayList<>(),
                            new ArrayList<>(),
                            res.getBytes("fotoPerfil"),
                            res.getString("bio"));
                } else { //artista
                    usuarioLogado = new Artista(res.getInt("idUsuario"),
                            res.getString("nome"),
                            res.getString("email"),
                            res.getString("senha"),
                            new ArrayList<>(),
                            res.getString("nomeArtistico"),
                            res.getBytes("fotoPerfil"),
                            res.getString("bio"));
                }
                System.out.println("usuario obtido: " + usuarioLogado.toString());
                int idTag;

                do {
                    res.getInt("idTag");
                    if (!res.wasNull()) {
                        idTag = res.getInt("idTag");
                        String textoTag = res.getString("nomeTag");
                        listaTagsResultSet.add(new Tag(idTag, textoTag));
                    }
                    if (u instanceof Comum) {
                        res.getInt("idComunidade");
                        if (!res.wasNull()) {
                            int idComunidade = res.getInt("idComunidade");

                            /*
                            artista.idUsuario as idArtista,
                            artista.nome as nomeArtista,
                            artista.email as emailArtista,
                            artista.senha as senhaArtista,
                            artista.nomeArtistico,
                            artista.fotoPerfil as fotoPerfilArtista,
                            artista.bio as bioArtista
                             */
                            Artista artistaComunidade = new Artista(res.getInt("idArtista"),
                                    res.getString("nomeArtista"),
                                    res.getString("emailArtista"),
                                    res.getString("senhaArtista"),
                                    res.getString("nomeArtistico"),
                                    res.getBytes("fotoPerfilArtista"));

                            listaComunidadesResultSet.add(new Comunidade(idComunidade,
                                    artistaComunidade,
                                    res.getBytes("fotoComunidade"),
                                    res.getString("corFundoDoPost"),
                                    res.getString("corFonteDoPost")));
                        }
                    }

                } while (res.next());

                //terminou de pegar todos os dados do cursor, agora basta filtrar a lista e adiciona ao usuario logado
                //filtrando lista tags e adicionando ao usuario:
                for (int i = 0; i < listaTagsResultSet.size(); i++) {
                    //se for o primeiro row pego OU se o último adicionado na listaTags do usuario é diferente do atual do for
                    if (i == 0 || usuarioLogado.getListaTags().getLast().getIdTag() != listaTagsResultSet.get(i).getIdTag()) {
                        usuarioLogado.getListaTags().add(listaTagsResultSet.get(i));
                    }
                }

                //filtrando a lista de comunidades inscritas
                if (usuarioLogado instanceof Comum uComum) {
                    for (int i = 0; i < listaComunidadesResultSet.size(); i++) {
                        if (i == 0 || uComum.getListaComunidadesInscritas().getLast().getIdComunidade() != listaComunidadesResultSet.get(i).getIdComunidade()) {
                            uComum.getListaComunidadesInscritas().add(listaComunidadesResultSet.get(i));
                        }
                    }
                }
            }
            stmt.close();
            res.close();
        } catch (SQLException e) {
            usuarioLogado = null;
            System.out.println("ERRO SQL: " + e.getMessage());
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("ERRO SQL: " + e.getMessage());
            }
        }
        System.out.println("return nome:" + usuarioLogado.getNomeUsuario());
        return usuarioLogado;

    }

    public boolean cadastrar(Usuario user) {
        PreparedStatement stmt = null;
        boolean result;
        try {
            /* ARTISTA
            insert into usuario(idUsuario,nome,email,senha,tipo,nomeArtistico)
            VALUES(1,'user','usuario@gmail.com','7c4a8d09ca3762af61e59520943dc26494f8941b',1(ARTISTA),'dieguim DJ');
            COMUM
            insert into usuario(idUsuario,nome,email,senha,tipo,nomeArtistico)
            VALUES(2,'user2','usuario2@gmail.com','7c4a8d09ca3762af61e59520943dc26494f8941b',0(COMUM), null);
             */

            //EXPLICACAO:
            /*
            
            O FROM DUAL no MySQL (e outros bancos, como Oracle)
            é usado para selecionar valores sem precisar acessar uma tabela real. 
            A tabela DUAL é uma tabela fictícia que existe apenas para esse propósito.

            Por que usar o FROM DUAL?
            Selecionar sem acessar uma tabela real: 
            Quando você precisa executar uma consulta SELECT para calcular 
            ou gerar valores que não dependem de uma tabela existente.

            Inserções baseadas em condições: Em um INSERT ... SELECT, 
            você precisa de uma "origem" para o SELECT. Quando não há uma tabela real envolvida, usamos DUAL.
            
             */
            String sql = "INSERT INTO usuario (idUsuario, nome, email, senha, tipo, nomeArtistico, fotoPerfil, bio)"
                    + " SELECT NULL, ?, ?, ?, ?, ?, ?, ?"
                    + " FROM DUAL"
                    + " WHERE NOT EXISTS ("
                    + " SELECT 1"
                    + " FROM usuario"
                    + " WHERE (nome = ? OR email = ?)"
                    + ")";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, user.getNomeUsuario());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getSenha());
            if (user instanceof Artista) {
                stmt.setInt(4, 1); // ARTSITA
                stmt.setString(5, ((Artista) user).getNomeArtistico());
            } else {
                stmt.setInt(4, 0); // COMUM
                stmt.setNull(5, java.sql.Types.VARCHAR);
            }
            stmt.setBytes(6, user.getFotoPerfil());
            stmt.setNull(7, java.sql.Types.VARCHAR); //bio: o padrão é começar null

            stmt.setString(8, user.getNomeUsuario());
            stmt.setString(9, user.getEmail());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Usuário inserido com sucesso.");
                result = true;
            } else {
                System.out.println("nome ou email indisponíveis!");
                result = false;
            }
            stmt.close();
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
            result = false;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return result;
    }

    public boolean editarUsuario(Usuario user) {
        PreparedStatement stmt = null;
        PreparedStatement checkStmt = null;
        boolean result = false;

        try {
            con.setAutoCommit(false);

            String checkSql = "SELECT COUNT(*) FROM usuario WHERE (nome = ? OR email = ?) AND idUsuario != ?";
            checkStmt = con.prepareStatement(checkSql);
            checkStmt.setString(1, user.getNomeUsuario());
            checkStmt.setString(2, user.getEmail());
            checkStmt.setInt(3, user.getCodUsuario());

            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Nome ou email já em uso.");
                result = false;
                return result;
            }
            rs.close();
            checkStmt.close();

            String sql;
            if (user instanceof Comum) {
                //COMUM - sem nomeArtistico
                sql = "update usuario set nome =?, "
                        + "email=?, "
                        + "senha=?, "
                        + "fotoPerfil=?, "
                        + "bio=? "
                        + "WHERE idUsuario = ?";
            } else {//ARTISTA - com tudo
                sql = "update usuario set nome =?, "
                        + "email=?, "
                        + "senha=?, "
                        + "nomeArtistico=?, "
                        + "fotoPerfil=?, "
                        + "bio=? "
                        + "WHERE idUsuario = ?";
            }
            stmt = con.prepareStatement(sql);
            stmt.setString(1, user.getNomeUsuario());
            System.out.println("Novo username: " + user.getNomeUsuario());
            stmt.setString(2, user.getEmail());
            System.out.println("Novo email: " + user.getEmail());
            stmt.setString(3, user.getSenha());
            System.out.println("Nova senha: " + user.getSenha());

            if (user instanceof Comum) {
                stmt.setBytes(4, user.getFotoPerfil());
                stmt.setString(5, user.getBio());
                stmt.setInt(6, user.getCodUsuario());
            } else {
                stmt.setString(4, ((Artista) user).getNomeArtistico());
                stmt.setBytes(5, user.getFotoPerfil());
                stmt.setString(6, user.getBio());
                stmt.setInt(7, user.getCodUsuario());
            }
            System.out.println("codUsuario: " + user.getCodUsuario());
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                result = true;
                //executar a transação
                con.commit();
            } else {
                result = false;
                System.out.println("nome ou email indisponíveis!");
            }

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            result = false;
            try {
                //voltando pro estado anterior
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                //fechando a conexao
                con.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return result;
    }

    //igual ao efetuar login, mas sem senha, só id
    public Comum carregaUsuarioComumComComunidadesETags(Comum uCom) {
        PreparedStatement stmt;
        Comum usuario = null;
        List<Comunidade> listaComunidadesResultSet = new ArrayList<>();
        List<Tag> listaTagsResultSet = new ArrayList<>();
        try {
            String sql = "select comum.*,"
                    + " tag.idTag,"
                    + " tag.nome as nomeTag,"
                    + " comunidade.*,"
                    + " artista.idUsuario as idArtista,"
                    + " artista.nome as nomeArtista,"
                    + " artista.email as emailArtista,"
                    + " artista.senha as senhaArtista,"
                    + " artista.nomeArtistico,"
                    + " artista.fotoPerfil as fotoPerfilArtista,"
                    + " artista.bio as bioArtista"
                    + " from usuario comum"
                    + " LEFT JOIN tagsDoUsuario tgu"
                    + " ON tgu.fkUsuario = comum.idUsuario"
                    + " LEFT JOIN tag"
                    + " ON tgu.fkTag = tag.idTag"
                    + " LEFT JOIN usuariosDaComunidade ucomu"
                    + " ON ucomu.fkUsuario = comum.idUsuario"
                    + " LEFT JOIN comunidade"
                    + " on comunidade.idComunidade = ucomu.fkComunidade"
                    + " LEFT JOIN usuario artista"
                    + " ON artista.idUsuario = comunidade.fkUsuarioPrincipal"
                    + " where comum.idUsuario = ?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, uCom.getCodUsuario());

            ResultSet res = stmt.executeQuery();
            //se encontrou resultado
            if (res.next()) {

                usuario = new Comum(res.getInt("idUsuario"),
                        res.getString("nome"),
                        res.getString("email"),
                        res.getString("senha"),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        res.getBytes("fotoPerfil"),
                        res.getString("bio"));

                do {
                    res.getInt("idTag");
                    if (!res.wasNull() && listaTagsResultSet != null) {
                        int idTag = res.getInt("idTag");
                        String textoTag = res.getString("nomeTag");
                        listaTagsResultSet.add(new Tag(idTag, textoTag));
                    } else {
                        listaTagsResultSet = null;
                    }

                    res.getInt("idComunidade");
                    if (!res.wasNull() && listaComunidadesResultSet != null) {
                        int idComunidade = res.getInt("idComunidade");
                        Artista artistaComunidade = new Artista(res.getInt("idArtista"),
                                res.getString("nomeArtista"),
                                res.getString("emailArtista"),
                                res.getString("senhaArtista"),
                                res.getString("nomeArtistico"),
                                res.getBytes("fotoPerfilArtista"));

                        listaComunidadesResultSet.add(new Comunidade(idComunidade,
                                artistaComunidade,
                                res.getBytes("fotoComunidade"),
                                res.getString("corFundoDoPost"),
                                res.getString("corFonteDoPost")));
                    } else {
                        listaComunidadesResultSet = null;
                    }

                } while (res.next());

                //terminou de pegar todos os dados do cursor, agora basta filtrar a lista e adiciona ao usuario logado
                //filtrando lista tags e adicionando ao usuario:
                if (listaTagsResultSet != null) {
                    for (int i = 0; i < listaTagsResultSet.size(); i++) {
                        //se for o primeiro row pego OU se o último adicionado na listaTags do usuario é diferente do atual do for
                        if (i == 0 || usuario.getListaTags().getLast().getIdTag() != listaTagsResultSet.get(i).getIdTag()) {
                            usuario.getListaTags().add(listaTagsResultSet.get(i));
                        }
                    }
                }

                //filtrando a lista de comunidades inscritas
                if (listaComunidadesResultSet != null) {
                    for (int i = 0; i < listaComunidadesResultSet.size(); i++) {
                        if (i == 0 || usuario.getListaComunidadesInscritas().getLast().getIdComunidade() != listaComunidadesResultSet.get(i).getIdComunidade()) {
                            usuario.getListaComunidadesInscritas().add(listaComunidadesResultSet.get(i));
                        }
                    }
                }
            }
            stmt.close();
            res.close();
        } catch (SQLException e) {
            usuario = null;
            System.out.println("ERRO SQL: " + e.getMessage());
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("ERRO SQL: " + e.getMessage());
            }
        }
        return usuario;
    }

    public ArrayList<Artista> getListaArtistas() {
        Statement stmt = null;
        ArrayList<Artista> listaArt = new ArrayList();
        Artista a = null;
        int idUsuario = -1;
        try {
            String sql = "select usuario.*, tag.idTag, tag.nome as nomeTag from usuario"
                    + " LEFT JOIN tagsDoUsuario tgu"
                    + " ON tgu.fkUsuario = usuario.idUsuario"
                    + " LEFT JOIN tag"
                    + " ON tgu.fkTag = tag.idTag"
                    + " where usuario.tipo = 1";
            stmt = con.createStatement();
            ResultSet res = stmt.executeQuery(sql);
            while (res.next()) {
                if (a == null || res.getInt("idUsuario") != idUsuario) {
                    idUsuario = res.getInt("idUsuario");
                    a = new Artista(idUsuario,
                            res.getString("nome"),
                            res.getString("email"),
                            res.getString("senha"),
                            new ArrayList(),
                            res.getString("nomeArtistico"));

                    listaArt.add(a);
                }
                int idTag = res.getInt("idTag");
                if (!res.wasNull()) {
                    a.getListaTags().add(new Tag(idTag, res.getString("nomeTag")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            listaArt = null;
        }
        return listaArt;
    }

    public boolean confirmarSenha(Object[] usuarioESenhaATestar) {
        if (usuarioESenhaATestar == null) {
            System.out.println("usuarioESenhaATestar não pode ser NULL");
            return false;

        } else if (usuarioESenhaATestar.length != 2) {
            System.out.println("usuarioESenhaATestar deve ter 2 elementos");
            return false;

        } else if (usuarioESenhaATestar[0] == null || usuarioESenhaATestar[1] == null) {
            System.out.println("usuarioESenhaATestar não deve ter elementos nulos");
            return false;

        } else if (!(usuarioESenhaATestar[0] instanceof Usuario) || !(usuarioESenhaATestar[1] instanceof String)) {
            System.out.println("usuarioESenhaATestar[0] deve ser Usuario e usuarioESenhaATestar[1] deve ser String");
            return false;

        }

        Usuario u = (Usuario) usuarioESenhaATestar[0];
        String senha = (String) usuarioESenhaATestar[1];

        PreparedStatement stmt = null;
        ResultSet res = null;
        boolean sucesso;

        try {

            String sql = "SELECT idUsuario FROM usuario"
                    + " WHERE senha = ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, senha);
            res = stmt.executeQuery();

            if (res.next()) {
                sucesso = true;
            } else {
                sucesso = false;
            }

        } catch (SQLException e) {
            sucesso = false;
            e.printStackTrace();
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return sucesso;
    }

    public boolean deleteUsuario(Usuario u) {
        if (u == null) {
            System.out.println("Usuario não pode ser null");
            return false;
        }

        PreparedStatement stmt = null;
        boolean res;

        try {
            String sql = "DELETE FROM usuario WHERE idUsuario = ?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, u.getCodUsuario());

            stmt.execute();
            res = true;
        } catch (SQLException e) {
            res = false;
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return res;
    }
}
