package model;

import factory.Conector;
import java.sql.*;
import java.util.ArrayList;
import modelDominio.Album;
import modelDominio.Artista;
import modelDominio.Musica;
import modelDominio.Tag;

public class MusicaDao {

    private Connection con;

    public MusicaDao() {
        this.con = Conector.getConnection();
    }

    public boolean insertMusica(Object[] musicaAlbum) {
        Musica m;
        Album a;
        boolean res;
        if (musicaAlbum.length == 2) {
            if (musicaAlbum[0] != null && musicaAlbum[1] != null) {
                if (musicaAlbum[0] instanceof Musica && musicaAlbum[1] instanceof Album) {
                    m = (Musica) musicaAlbum[0];
                    a = (Album) musicaAlbum[1];
                    System.out.println("musica: "+m.getNome());
                    System.out.println("Album: "+a.getNome());
                    System.out.println("id do album: "+a.getIdAlbum());
                    
                    System.out.println("Tudo certo com a música, prosseguindo para a comunicação com o banco");
                    PreparedStatement stmtInsertMusica = null, stmtAssociaTag = null;
                    ResultSet resKeysMusica = null, resKeysTagAssociacao = null, resKeysFeat = null;
                    try {
                        con.setAutoCommit(false);

                        String sql = "INSERT INTO musica(idMusica, nomeMusica, audio, fkAlbum)"
                                + " VALUES(null,?,?,?)";
                        stmtInsertMusica = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                        stmtInsertMusica.setString(1, m.getNome());
                        stmtInsertMusica.setBytes(2, m.getAudio());
                        stmtInsertMusica.setInt(3, a.getIdAlbum());

                        stmtInsertMusica.execute();

                        resKeysMusica = stmtInsertMusica.getGeneratedKeys();
                        if (resKeysMusica.next()) {
                            int idMusica = resKeysMusica.getInt(1);
                            sql = "INSERT INTO tagsDeMusica(idTagDeMusica,fkTag,fkMusica)"
                                    + " VALUES(null,?,?)";
                            stmtAssociaTag = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                            stmtAssociaTag.setInt(1, m.getListaTags().get(0).getIdTag());
                            stmtAssociaTag.setInt(2, idMusica);

                            stmtAssociaTag.execute();

                            resKeysTagAssociacao = stmtAssociaTag.getGeneratedKeys();
                            if (resKeysTagAssociacao.next()) {
                                if (m.getColaboradores() == null) { // não é necessário associar os colaboradores
                                    res = true;
                                    System.out.println("musica sem colaboradores");
                                } else {
                                    for (int i = 0; i < m.getColaboradores().size(); i++) {
                                        PreparedStatement stmtFeat;
                                        Artista feat = m.getColaboradores().get(i);
                                        sql = "INSERT INTO colaboracoes(idColaboracoes,fkMusica,fkUsuario)"
                                                + " VALUES(null,?,?)";
                                        stmtFeat = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                                        stmtFeat.setInt(1, idMusica);
                                        stmtFeat.setInt(2, feat.getCodUsuario());
                                        stmtFeat.execute();
                                        resKeysFeat = stmtFeat.getGeneratedKeys();

                                        if (resKeysFeat.next()) {
                                            //insert com sucesso
                                            res = true;
                                        } else {
                                            //deu erro no insert
                                            System.out.println("Erro no insert de colaboladores");
                                            res = false;
                                            break;
                                        }
                                    }
                                }
                            } else {
                                System.out.println("estilo não associado com musica");
                                res = false;
                            }
                        } else {
                            System.out.println("música não foi inserida");
                            res = false;
                        }
                        con.commit();
                        res = true;
                    } catch (SQLException e) {
                        res = false;
                        e.printStackTrace();
                        System.out.println("Erro!");
                        try {
                            con.rollback();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    } finally {
                        try {
                            if (stmtInsertMusica != null) {
                                stmtInsertMusica.close();
                            }
                            if (stmtAssociaTag != null) {
                                stmtAssociaTag.close();
                            }
                            
                            if (con != null) {
                                con.close();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                    res = false;
                    System.out.println("musicaAlbum[0] deve ser Musica e musicaAlbum[1] deve ser Album");
                }
            } else {
                res = false;
                System.out.println("Musica e album de musicaAlbum[] não devem ser NULL");
            }
        } else {
            res = false;
            System.out.println("musicaAlbum deve ter dois elementos!");
        }
        System.out.println("res: "+res);
        return res;
    }

    public boolean deleteMusica(Musica m) {
        PreparedStatement stmt = null;
        boolean res;
        if (m != null) {
            try {
                String sql = "DELETE FROM musica WHERE idMusica = ?";
                stmt = con.prepareStatement(sql);
                stmt.setInt(1, m.getIdMusica());
                stmt.execute();
                res = true;
            } catch (SQLException e) {
                res = false;
                e.printStackTrace();
            } finally {
                try {
                    if (con != null) {
                        con.close();
                    }
                    if (stmt != null) {
                        stmt.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Musica não pode ser Null!");
            res = false;
        }

        return res;
    }

    public ArrayList<Musica> listaMusica() {
        Statement stmt;
        ArrayList<Musica> listaMusicas = new ArrayList();
        //armazenando-os aqui em cima para não ser necessário filtrar dps
        Musica m = null;
        int idMusica = 0;
        try {
            String sql = consultaMusicaComTagWhere("");
            stmt = con.createStatement();
            ResultSet res = stmt.executeQuery(sql);

            while (res.next()) {
                //se for a primeira vez (null)
                //  ou se idMusica do ROW atual for diferente do anterior (não é uma repetição)
                if (m == null || idMusica != res.getInt("idMusica")) {
                    Artista art = new Artista(res.getInt("idUsuarioPrincipal"),
                            res.getString("nomePrincipal"),
                            res.getString("emailPrincipal"),
                            res.getString("senhaPrincipal"),
                            res.getString("nomeArtisticoPrincipal"),
                            res.getBytes("fotoPerfilPrincipal"));

                    m = new Musica(res.getInt("idMusica"),
                            res.getString("nomeMusica"),
                            res.getBytes("audio"),
                            new ArrayList(),
                            new ArrayList(),
                            art);

                    listaMusicas.add(m);
                    idMusica = m.getIdMusica();
                }

                Tag novaTag = new Tag(res.getInt("idTag"), res.getString("nomeTag"));
                //se está vazia ou se o ultimo elemento da lista tem um id diferente do atual
                boolean tagNaoConsta = true;
                for (Tag tag : m.getListaTags()) {
                    if (tag.getIdTag() == novaTag.getIdTag()
                            || tag.getTagTexto().equals(novaTag.getTagTexto())) {
                        tagNaoConsta = false;
                        break;
                    }
                }
                if (tagNaoConsta) {
                    m.getListaTags().add(novaTag);
                }

                int idUsuarioFeat = res.getInt("idUsuarioFeat");
                if (!res.wasNull()) {
                    Artista colaborador = new Artista(idUsuarioFeat,
                            res.getString("nomeFeat"),
                            res.getString("emailFeat"),
                            res.getString("senhaFeat"),
                            res.getString("nomeArtisticoFeat"));

                    if (!m.getColaboradores().contains(colaborador)) {
                        m.getColaboradores().add(colaborador); // Evita duplicatas
                    }
                }
            }
            res.close();
        } catch (SQLException e) {
            System.out.println("ERRO SQL: " + e.getMessage());
            listaMusicas = null;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("ERRO SQL: " + e.getMessage());
            }
        }
        return listaMusicas;
    }

    public ArrayList<Musica> listaMusica(Tag tagWhere) {
        PreparedStatement stmt;
        ArrayList<Musica> listaMusicas = new ArrayList();
        //armazenando-os aqui em cima para não ser necessário filtrar dps
        Musica m = null;
        int idMusica = 0;
        try {
            String sql = consultaMusicaComTagWhere("WHERE musica.idMusica IN ("
                    + " SELECT fkMusica"
                    + " FROM tagsDeMusica"
                    + " WHERE fkTag = ?"
                    + ")");
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, tagWhere.getIdTag());
            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                //se for a primeira vez (null)
                //  ou se idMusica do ROW atual for diferente do anterior (não é uma repetição)
                if (m == null || idMusica != res.getInt("idMusica")) {
                    Artista art = new Artista(res.getInt("idUsuarioPrincipal"),
                            res.getString("nomePrincipal"),
                            res.getString("emailPrincipal"),
                            res.getString("senhaPrincipal"),
                            res.getString("nomeArtisticoPrincipal"),
                            res.getBytes("fotoPerfilPrincipal"));

                    m = new Musica(res.getInt("idMusica"),
                            res.getString("nomeMusica"),
                            res.getBytes("audio"),
                            new ArrayList(),
                            new ArrayList(),
                            art);

                    listaMusicas.add(m);
                    idMusica = m.getIdMusica();
                }

                Tag novaTag = new Tag(res.getInt("idTag"), res.getString("nomeTag"));
                //se está vazia ou se o ultimo elemento da lista tem um id diferente do atual
                boolean tagNaoConsta = true;
                for (Tag tag : m.getListaTags()) {
                    if (tag.getIdTag() == novaTag.getIdTag()
                            || tag.getTagTexto().equals(novaTag.getTagTexto())) {
                        tagNaoConsta = false;
                        break;
                    }
                }
                if (tagNaoConsta) {
                    m.getListaTags().add(novaTag);
                }

                int idUsuarioFeat = res.getInt("idUsuarioFeat");
                if (!res.wasNull()) {
                    Artista colaborador = new Artista(idUsuarioFeat,
                            res.getString("nomeFeat"),
                            res.getString("emailFeat"),
                            res.getString("senhaFeat"),
                            res.getString("nomeArtisticoFeat"));

                    if (!m.getColaboradores().contains(colaborador)) {
                        m.getColaboradores().add(colaborador); // Evita duplicatas
                    }
                }
            }
            res.close();
        } catch (SQLException e) {
            System.out.println("ERRO SQL: " + e.getMessage());
            listaMusicas = null;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("ERRO SQL: " + e.getMessage());
            }
        }
        return listaMusicas;
    }

    public ArrayList<Musica> listaMusica(Artista artistaWhere) {
        PreparedStatement stmt;
        ArrayList<Musica> listaMusicas = new ArrayList();
        //armazenando-os aqui em cima para não ser necessário filtrar dps
        Musica m = null;
        int idMusica = 0;
        try {
            String sql = consultaMusicaComTagWhere("WHERE artistaPrincipal.idUsuario = ?");
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, artistaWhere.getCodUsuario());
            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                //se for a primeira vez (null)
                //  ou se idMusica do ROW atual for diferente do anterior (não é uma repetição)
                if (m == null || idMusica != res.getInt("idMusica")) {
                    Artista art = new Artista(res.getInt("idUsuarioPrincipal"),
                            res.getString("nomePrincipal"),
                            res.getString("emailPrincipal"),
                            res.getString("senhaPrincipal"),
                            res.getString("nomeArtisticoPrincipal"),
                            res.getBytes("fotoPerfilPrincipal"));

                    m = new Musica(res.getInt("idMusica"),
                            res.getString("nomeMusica"),
                            res.getBytes("audio"),
                            new ArrayList(),
                            new ArrayList(),
                            art);

                    listaMusicas.add(m);
                    idMusica = m.getIdMusica();
                }

                Tag novaTag = new Tag(res.getInt("idTag"), res.getString("nomeTag"));
                //se está vazia ou se o ultimo elemento da lista tem um id diferente do atual
                boolean tagNaoConsta = true;
                for (Tag tag : m.getListaTags()) {
                    if (tag.getIdTag() == novaTag.getIdTag()
                            || tag.getTagTexto().equals(novaTag.getTagTexto())) {
                        tagNaoConsta = false;
                        break;
                    }
                }
                if (tagNaoConsta) {
                    m.getListaTags().add(novaTag);
                }

                int idUsuarioFeat = res.getInt("idUsuarioFeat");
                if (!res.wasNull()) {
                    Artista colaborador = new Artista(idUsuarioFeat,
                            res.getString("nomeFeat"),
                            res.getString("emailFeat"),
                            res.getString("senhaFeat"),
                            res.getString("nomeArtisticoFeat"));

                    if (!m.getColaboradores().contains(colaborador)) {
                        m.getColaboradores().add(colaborador); // Evita duplicatas
                    }
                }
            }
            res.close();
        } catch (SQLException e) {
            System.out.println("ERRO SQL: " + e.getMessage());
            listaMusicas = null;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("ERRO SQL: " + e.getMessage());
            }
        }
        return listaMusicas;
    }
    
    public ArrayList<Musica> listaMusica(Album albumWhere){
        PreparedStatement stmt;
        ArrayList<Musica> listaMusicas = new ArrayList();
        //armazenando-os aqui em cima para não ser necessário filtrar dps
        Musica m = null;
        int idMusica = 0;
        try {
            String sql = consultaMusicaComTagWhere("WHERE album.idAlbum = ?");
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, albumWhere.getIdAlbum());
            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                //se for a primeira vez (null)
                //  ou se idMusica do ROW atual for diferente do anterior (não é uma repetição)
                if (m == null || idMusica != res.getInt("idMusica")) {
                    Artista art = new Artista(res.getInt("idUsuarioPrincipal"),
                            res.getString("nomePrincipal"),
                            res.getString("emailPrincipal"),
                            res.getString("senhaPrincipal"),
                            res.getString("nomeArtisticoPrincipal"),
                            res.getBytes("fotoPerfilPrincipal"));

                    m = new Musica(res.getInt("idMusica"),
                            res.getString("nomeMusica"),
                            res.getBytes("audio"),
                            new ArrayList(),
                            new ArrayList(),
                            art);

                    listaMusicas.add(m);
                    idMusica = m.getIdMusica();
                }

                Tag novaTag = new Tag(res.getInt("idTag"), res.getString("nomeTag"));
                //se está vazia ou se o ultimo elemento da lista tem um id diferente do atual
                boolean tagNaoConsta = true;
                for (Tag tag : m.getListaTags()) {
                    if (tag.getIdTag() == novaTag.getIdTag()
                            || tag.getTagTexto().equals(novaTag.getTagTexto())) {
                        tagNaoConsta = false;
                        break;
                    }
                }
                if (tagNaoConsta) {
                    m.getListaTags().add(novaTag);
                }

                int idUsuarioFeat = res.getInt("idUsuarioFeat");
                if (!res.wasNull()) {
                    Artista colaborador = new Artista(idUsuarioFeat,
                            res.getString("nomeFeat"),
                            res.getString("emailFeat"),
                            res.getString("senhaFeat"),
                            res.getString("nomeArtisticoFeat"));

                    if (!m.getColaboradores().contains(colaborador)) {
                        m.getColaboradores().add(colaborador); // Evita duplicatas
                    }
                }
            }
            res.close();
        } catch (SQLException e) {
            System.out.println("ERRO SQL: " + e.getMessage());
            listaMusicas = null;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("ERRO SQL: " + e.getMessage());
            }
        }
        return listaMusicas;
    }
    
    public ArrayList<Musica> listaMusica(ArrayList<Tag> listaTagWhere){
        PreparedStatement stmt;
        ArrayList<Musica> listaMusicas = new ArrayList();
        //armazenando-os aqui em cima para não ser necessário filtrar dps
        Musica m = null;
        int idMusica = 0;
        try {
            
            String clausulaWhereComPlaceHolder = "";
            for(int i = 0; i < listaTagWhere.size(); i++){
                clausulaWhereComPlaceHolder += "fkTag = ?";
                
                //se não estiver na ultima posição da lista
                if(i < listaTagWhere.size()-1){
                    clausulaWhereComPlaceHolder += " OR ";
                }
            }
            
            //O distinct garante que será retornado apenas um id de musica, não pegando a mesma musica muitas vezes
            String sql = consultaMusicaComTagWhere("WHERE musica.idMusica IN ("
                    + " SELECT DISTINCT fkMusica"
                    + " FROM tagsDeMusica"
                    + " WHERE "+clausulaWhereComPlaceHolder
                    + ")");
            stmt = con.prepareStatement(sql);
            
            for(int i = 0; i < listaTagWhere.size(); i++){
                Tag t = listaTagWhere.get(i);
                stmt.setInt((i+1), t.getIdTag()); 
            }
            
            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                //se for a primeira vez (null)
                //  ou se idMusica do ROW atual for diferente do anterior (não é uma repetição)
                if (m == null || idMusica != res.getInt("idMusica")) {
                    Artista art = new Artista(res.getInt("idUsuarioPrincipal"),
                            res.getString("nomePrincipal"),
                            res.getString("emailPrincipal"),
                            res.getString("senhaPrincipal"),
                            res.getString("nomeArtisticoPrincipal"),
                            res.getBytes("fotoPerfilPrincipal"));

                    m = new Musica(res.getInt("idMusica"),
                            res.getString("nomeMusica"),
                            res.getBytes("audio"),
                            new ArrayList(),
                            new ArrayList(),
                            art);

                    listaMusicas.add(m);
                    idMusica = m.getIdMusica();
                }

                Tag novaTag = new Tag(res.getInt("idTag"), res.getString("nomeTag"));
                //se está vazia ou se o ultimo elemento da lista tem um id diferente do atual
                boolean tagNaoConsta = true;
                for (Tag tag : m.getListaTags()) {
                    if (tag.getIdTag() == novaTag.getIdTag()
                            || tag.getTagTexto().equals(novaTag.getTagTexto())) {
                        tagNaoConsta = false;
                        break;
                    }
                }
                if (tagNaoConsta) {
                    m.getListaTags().add(novaTag);
                }

                int idUsuarioFeat = res.getInt("idUsuarioFeat");
                if (!res.wasNull()) {
                    Artista colaborador = new Artista(idUsuarioFeat,
                            res.getString("nomeFeat"),
                            res.getString("emailFeat"),
                            res.getString("senhaFeat"),
                            res.getString("nomeArtisticoFeat"));

                    if (!m.getColaboradores().contains(colaborador)) {
                        m.getColaboradores().add(colaborador); // Evita duplicatas
                    }
                }
            }
            res.close();
        } catch (SQLException e) {
            System.out.println("ERRO SQL: " + e.getMessage());
            listaMusicas = null;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("ERRO SQL: " + e.getMessage());
            }
        }
        return listaMusicas;
    }

    public Object[] listaMusicaComAlbum(Artista artistaWhere) {
        PreparedStatement stmt;
        ArrayList<Musica> listaMusicas = new ArrayList();
        ArrayList<Album> listaAlbuns = new ArrayList();

        Object[] listaMusicasEListaAlbuns = {listaMusicas, listaAlbuns};

        //armazenando-os aqui em cima para não ser necessário filtrar dps
        Musica m = null;
        int idMusica = 0;
        try {
            String sql = consultaMusicaComTagWhere("WHERE artistaPrincipal.idUsuario = ?");
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, artistaWhere.getCodUsuario());
            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                //se for a primeira vez (null)
                //  ou se idMusica do ROW atual for diferente do anterior (não é uma repetição)
                if (m == null || idMusica != res.getInt("idMusica")) {
                    Artista art = new Artista(res.getInt("idUsuarioPrincipal"),
                            res.getString("nomePrincipal"),
                            res.getString("emailPrincipal"),
                            res.getString("senhaPrincipal"),
                            res.getString("nomeArtisticoPrincipal"),
                            res.getBytes("fotoPerfilPrincipal"));

                    m = new Musica(res.getInt("idMusica"),
                            res.getString("nomeMusica"),
                            res.getBytes("audio"),
                            new ArrayList(),
                            new ArrayList(),
                            art);

                    listaMusicas.add(m);

                    Album album = new Album(res.getInt("idAlbum"),
                            res.getString("nomeAlbum"),
                            art,
                            res.getBytes("imagem"));
                    idMusica = m.getIdMusica();

                    listaAlbuns.add(album);
                }

                Tag novaTag = new Tag(res.getInt("idTag"), res.getString("nomeTag"));
                //se está vazia ou se o ultimo elemento da lista tem um id diferente do atual
                boolean tagNaoConsta = true;
                for (Tag tag : m.getListaTags()) {
                    if (tag.getIdTag() == novaTag.getIdTag()
                            || tag.getTagTexto().equals(novaTag.getTagTexto())) {
                        tagNaoConsta = false;
                        break;
                    }
                }
                if (tagNaoConsta) {
                    m.getListaTags().add(novaTag);
                }

                int idUsuarioFeat = res.getInt("idUsuarioFeat");
                if (!res.wasNull()) {
                    Artista colaborador = new Artista(idUsuarioFeat,
                            res.getString("nomeFeat"),
                            res.getString("emailFeat"),
                            res.getString("senhaFeat"),
                            res.getString("nomeArtisticoFeat"));

                    if (!m.getColaboradores().contains(colaborador)) {
                        m.getColaboradores().add(colaborador); // Evita duplicatas
                    }
                }
            }
            res.close();
        } catch (SQLException e) {
            System.out.println("ERRO SQL: " + e.getMessage());
            listaMusicas = null;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("ERRO SQL: " + e.getMessage());
            }
        }
        return listaMusicasEListaAlbuns;
    }

    private String consultaMusicaComTagWhere(String where) {
        return "SELECT musica.idMusica, musica.audio, musica.fkAlbum as fkAlbumDaMusica, musica.nomeMusica,"
                + " tag.idTag,"
                + " tag.nome as nomeTag,"
                + " album.idAlbum, album.imagem, album.nomeAlbum,"
                + " artistaPrincipal.idUsuario as idUsuarioPrincipal,"
                + " artistaPrincipal.nome as nomePrincipal,"
                + " artistaPrincipal.email as emailPrincipal,"
                + " artistaPrincipal.senha as senhaPrincipal,"
                + " artistaPrincipal.nomeArtistico as nomeArtisticoPrincipal,"
                + " artistaPrincipal.fotoPerfil as fotoPerfilPrincipal,"
                + " feat.idUsuario as idUsuarioFeat,"
                + " feat.nome as nomeFeat,"
                + " feat.email as emailFeat,"
                + " feat.senha as senhaFeat,"
                + " feat.nomeArtistico as nomeArtisticoFeat"
                + " FROM musica "
                + " INNER JOIN tagsDeMusica tgMus ON musica.idMusica = tgMus.fkMusica"
                + " INNER JOIN tag ON tgMus.fkTag = tag.idTag"
                + " INNER JOIN album ON musica.fkAlbum = album.idAlbum"
                + " INNER JOIN usuario artistaPrincipal ON album.fkUsuario = artistaPrincipal.idUsuario"
                + " LEFT JOIN colaboracoes ON musica.idMusica = colaboracoes.fkMusica"
                + " LEFT JOIN usuario feat ON colaboracoes.fkUsuario = feat.idUsuario"
                + " " + where;
    }

}
