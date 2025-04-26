package model;

import factory.Conector;
import java.sql.*;
import java.util.ArrayList;
import modelDominio.Album;
import modelDominio.Artista;
import modelDominio.Musica;
import modelDominio.Tag;

public class AlbumDao {

    private Connection con;

    public AlbumDao() {
        this.con = Conector.getConnection();
    }

    public boolean insertAlbum(Album a) {
        PreparedStatement stmtAlbum = null;
        PreparedStatement stmtTagAlbum = null;
        boolean res;

        try {
            con.setAutoCommit(false);
            String sql = "INSERT INTO album(idAlbum,fkUsuario,nomeAlbum,imagem)"
                    + " VALUES (null,?,?,?)";
            stmtAlbum = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmtAlbum.setInt(1, a.getArtista().getCodUsuario());
            stmtAlbum.setString(2, a.getNome());
            stmtAlbum.setBytes(3, a.getImagem());

            stmtAlbum.execute();

            ResultSet generatedKeys = stmtAlbum.getGeneratedKeys();

            if (generatedKeys.next()) {
                sql = "INSERT INTO tagsDoAlbum(idTagsDoAlbum,fkTag,fkAlbum)"
                        + " VALUES(null,?,?)";
                stmtTagAlbum = con.prepareStatement(sql);
                stmtTagAlbum.setInt(1, a.getListaTags().get(0).getIdTag());
                System.out.println("idTag: " + a.getListaTags().get(0).getIdTag());
                stmtTagAlbum.setInt(2, generatedKeys.getInt(1));
                System.out.println("algumId: " + generatedKeys.getInt(1));
                stmtTagAlbum.execute();
                res = true;
            } else {
                res = false;
            }
            con.commit();
        } catch (SQLException e) {
            res = false;
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                if (stmtAlbum != null) {
                    stmtAlbum.close();
                }
                if (stmtTagAlbum != null) {
                    stmtTagAlbum.close();
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

    public boolean deleteAlbum(Album a) {
        PreparedStatement stmt = null;
        boolean res;
        try {
            con.setAutoCommit(false);
            String sql = "DELETE FROM album WHERE idAlbum = ?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, a.getIdAlbum());

            stmt.execute();
            con.commit();
            res = true;
        } catch (SQLException e) {
            res = false;
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
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

    public Album getAlbum(Musica m) {
        PreparedStatement stmt = null;
        Album a = null;
        try {

            String sql = "SELECT album.*, "
                    + " artista.*,"
                    + " tag.idTag,"
                    + " tag.nome as nomeTag"
                    + " FROM album"
                    + " INNER JOIN usuario artista ON album.fkUsuario = artista.idUsuario"
                    + " INNER JOIN tagsDoAlbum tgAlbum ON album.idAlbum = fkAlbum"
                    + " INNER JOIN tag ON tgAlbum.fkTag = idTag "
                    + " INNER JOIN musica ON musica.fkAlbum = album.idAlbum"
                    + " WHERE musica.idMusica = ?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, m.getIdMusica());
            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                if (a == null) {
                    Artista art = new Artista(res.getInt("idUsuario"),
                            res.getString("nome"),
                            res.getString("email"),
                            res.getString("senha"),
                            res.getString("nomeArtistico"),
                            res.getBytes("fotoPerfil"));

                    a = new Album(res.getInt("idAlbum"),
                            res.getString("nomeAlbum"),
                            art,
                            res.getBytes("imagem"),
                            new ArrayList());
                }
                int idTag = res.getInt("idTag");
                if (!res.wasNull()) {
                    Tag tg = new Tag(idTag,
                            res.getString("nomeTag"));

                    a.getListaTags().add(tg);
                }
            }
            res.close();
        } catch (SQLException e) {
            a = null;
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
        return a;
    }

    public ArrayList<Album> getListaAlbum() {
        Statement stmt;
        ArrayList<Album> listaAlbuns = new ArrayList();
        Album a = null;
        int idAlbum = 0;
        try {
            String sql = consultaListaAlbunsComTag("");
            stmt = con.createStatement();
            ResultSet res = stmt.executeQuery(sql);
            while (res.next()) {
                //para criar um novo album se for a primeira vez percorrendo o while
                //  ou se o idAlbum do ROW atual for diferente do guardado anterior
                if (a == null || idAlbum != res.getInt("idAlbum")) {
                    Artista art = new Artista(res.getInt("idUsuario"),
                            res.getString("nome"),
                            res.getString("email"),
                            res.getString("senha"),
                            res.getString("nomeArtistico"),
                            res.getBytes("fotoPerfil"));

                    a = new Album(res.getInt("idAlbum"),
                            res.getString("nomeAlbum"),
                            art,
                            res.getBytes("imagem"),
                            new ArrayList());

                    listaAlbuns.add(a);
                    idAlbum = a.getIdAlbum();
                }

                Tag tg = new Tag(res.getInt("idTag"),
                        res.getString("nomeTag"));

                a.getListaTags().add(tg);
            }
            res.close();
        } catch (SQLException e) {
            listaAlbuns = null;
            System.out.println("ERRO SQL: " + e.getMessage());
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("ERRO SQL: " + e.getMessage());
            }
        }
        return listaAlbuns;
    }

    public ArrayList<Album> getListaAlbum(Artista artista) {
        PreparedStatement stmt;
        ArrayList<Album> listaAlbuns = new ArrayList();
        System.out.println("artista id: " + artista.getCodUsuario());
        Album a = null;
        int idAlbum = 0;
        try {
            String sql = consultaListaAlbunsComTag("WHERE artista.idUsuario = ?");
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, artista.getCodUsuario());
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                //para criar um novo album se for a primeira vez percorrendo o while
                //  ou se o idAlbum do ROW atual for diferente do guardado anterior
                if (a == null || idAlbum != res.getInt("idAlbum")) {
                    Artista art = new Artista(res.getInt("idUsuario"),
                            res.getString("nome"),
                            res.getString("email"),
                            res.getString("senha"),
                            res.getString("nomeArtistico"),
                            res.getBytes("fotoPerfil"));

                    a = new Album(res.getInt("idAlbum"),
                            res.getString("nomeAlbum"),
                            art,
                            res.getBytes("imagem"),
                            new ArrayList());

                    listaAlbuns.add(a);
                    System.out.println("Adicionando album " + a.getNome() + " a lista");
                    idAlbum = a.getIdAlbum();
                }
                int idTag = res.getInt("idTag");
                if (!res.wasNull()) {
                    Tag tg = new Tag(idTag,
                            res.getString("nomeTag"));

                    a.getListaTags().add(tg);
                }
            }
            res.close();
        } catch (SQLException e) {
            listaAlbuns = null;
            System.out.println("ERRO SQL: " + e.getMessage());
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("ERRO SQL: " + e.getMessage());
            }
        }
        System.out.println("lista: " + listaAlbuns);
        return listaAlbuns;
    }

    public ArrayList<Album> getListaAlbum(Tag tagWhere) {
        PreparedStatement stmt;
        ArrayList<Album> listaAlbuns = new ArrayList();
        Album a = null;
        int idAlbum = 0;
        try {
            String sql = consultaListaAlbunsComTag("WHERE album.idAlbum IN ("
                    + " SELECT fkAlbum"
                    + " FROM tagsDoAlbum"
                    + " WHERE fkTag = ?"
                    + ")");
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, tagWhere.getIdTag());
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                //para criar um novo album se for a primeira vez percorrendo o while
                //  ou se o idAlbum do ROW atual for diferente do guardado anterior
                if (a == null || idAlbum != res.getInt("idAlbum")) {
                    Artista art = new Artista(res.getInt("idUsuario"),
                            res.getString("nome"),
                            res.getString("email"),
                            res.getString("senha"),
                            res.getString("nomeArtistico"),
                            res.getBytes("fotoPerfil"));

                    a = new Album(res.getInt("idAlbum"),
                            res.getString("nomeAlbum"),
                            art,
                            res.getBytes("imagem"),
                            new ArrayList());

                    listaAlbuns.add(a);
                    idAlbum = a.getIdAlbum();
                }

                Tag tg = new Tag(res.getInt("idTag"),
                        res.getString("nomeTag"));

                a.getListaTags().add(tg);
            }
            res.close();
        } catch (SQLException e) {
            listaAlbuns = null;
            System.out.println("ERRO SQL: " + e.getMessage());
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("ERRO SQL: " + e.getMessage());
            }
        }
        return listaAlbuns;
    }

    public ArrayList<Album> getListaAlbum(ArrayList<Tag> listaTagWhere) {
        PreparedStatement stmt;
        ArrayList<Album> listaAlbuns = new ArrayList();
        Album a = null;
        int idAlbum = 0;
        try {

            String clausulaWhereComPlaceHolder = "";
            for (int i = 0; i < listaTagWhere.size(); i++) {
                clausulaWhereComPlaceHolder += "fkTag = ?";

                //se não estiver na ultima posição da lista
                if (i < listaTagWhere.size() - 1) {
                    clausulaWhereComPlaceHolder += " OR ";
                }
            }

            String sql = consultaListaAlbunsComTag("WHERE album.idAlbum IN ("
                    + " SELECT DISTINCT fkAlbum"
                    + " FROM tagsDoAlbum"
                    + " WHERE " + clausulaWhereComPlaceHolder
                    + ")");
            stmt = con.prepareStatement(sql);

            for (int i = 0; i < listaTagWhere.size(); i++) {
                Tag t = listaTagWhere.get(i);
                stmt.setInt((i + 1), t.getIdTag());
            }

            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                //para criar um novo album se for a primeira vez percorrendo o while
                //  ou se o idAlbum do ROW atual for diferente do guardado anterior
                if (a == null || idAlbum != res.getInt("idAlbum")) {
                    Artista art = new Artista(res.getInt("idUsuario"),
                            res.getString("nome"),
                            res.getString("email"),
                            res.getString("senha"),
                            res.getString("nomeArtistico"),
                            res.getBytes("fotoPerfil"));

                    a = new Album(res.getInt("idAlbum"),
                            res.getString("nomeAlbum"),
                            art,
                            res.getBytes("imagem"),
                            new ArrayList());

                    listaAlbuns.add(a);
                    idAlbum = a.getIdAlbum();
                }

                Tag tg = new Tag(res.getInt("idTag"),
                        res.getString("nomeTag"));

                a.getListaTags().add(tg);
            }
            res.close();
        } catch (SQLException e) {
            listaAlbuns = null;
            System.out.println("ERRO SQL: " + e.getMessage());
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("ERRO SQL: " + e.getMessage());
            }
        }
        return listaAlbuns;
    }

    //se não tiver WHERE, colocar string Blank
    private String consultaListaAlbunsComTag(String where) {
        return "SELECT album.*, artista.*,"
                + " tag.idTag,"
                + " tag.nome as nomeTag"
                + " FROM album "
                + " INNER JOIN usuario artista ON album.fkUsuario = artista.idUsuario"
                + " INNER JOIN tagsDoAlbum tgAlbum ON album.idAlbum = fkAlbum"
                + " INNER JOIN tag ON tgAlbum.fkTag = idTag "
                + where;
    }

}
