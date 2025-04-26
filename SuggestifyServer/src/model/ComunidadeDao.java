package model;

import factory.Conector;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import modelDominio.Artista;
import modelDominio.Comum;
import modelDominio.Comunidade;
import modelDominio.Tag;
import modelDominio.Usuario;

public class ComunidadeDao {

    private Connection con;

    public ComunidadeDao() {
        con = Conector.getConnection();
    }

    public boolean insereComunidade(Artista a, byte[] backgroundPadrao, String[] corFundoPostECorFontePost) {
        PreparedStatement stmt = null;
        boolean result;

        try {
            String sql = "insert into comunidade(idComunidade,fotoComunidade,fkUsuarioPrincipal,corFundoDoPost,corFonteDoPost)"
                    + " values(null,?,?,?,?)";
            stmt = con.prepareStatement(sql);
            stmt.setBytes(1, backgroundPadrao);
            stmt.setInt(2, a.getCodUsuario());
            stmt.setString(3, corFundoPostECorFontePost[0]);
            stmt.setString(4, corFundoPostECorFontePost[1]);
            stmt.execute();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        } finally {
            try {
                con.close();
                stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public Comunidade getComunidadePeloArtistaId(int idArtista) {
        PreparedStatement stmt = null;
        Comunidade com = null;
        try {
            String sql = "select * from comunidade com"
                    + " INNER JOIN usuario u"
                    + " ON u.idUsuario = com.fkUsuarioPrincipal"
                    + " WHERE fkUsuarioPrincipal = ? ";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, idArtista);
            ResultSet res = stmt.executeQuery();
            if (res.next()) {
                com = new Comunidade(res.getInt("idComunidade"),
                        new Artista(res.getInt("idUsuario"),
                                res.getString("nome"),
                                res.getString("email"),
                                res.getString("senha"),
                                res.getString("nomeArtistico"),
                                res.getBytes("fotoPerfil"),
                                res.getString("bio")),
                        res.getBytes("fotoComunidade"),
                        res.getString("corFundoDoPost"),
                        res.getString("corFonteDoPost"));
            }
            res.close();
        } catch (Exception e) {
            e.printStackTrace();
            com = null;
        } finally {
            try {
                con.close();
                stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return com;
    }

    public Comunidade getComunidadePeloId(int idComunidade) {
        PreparedStatement stmt = null;
        Comunidade com = null;
        try {
            String sql = "select * from comunidade com"
                    + " INNER JOIN usuario u"
                    + " ON u.idUsuario = com.fkUsuarioPrincipal"
                    + " WHERE idComunidade = ? ";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, idComunidade);
            ResultSet res = stmt.executeQuery();
            if (res.next()) {
                com = new Comunidade(res.getInt("idComunidade"),
                        new Artista(res.getInt("idUsuario"),
                                res.getString("nome"),
                                res.getString("email"),
                                res.getString("senha"),
                                res.getString("nomeArtistico"),
                                res.getBytes("fotoPerfil"),
                                res.getString("bio")),
                        res.getBytes("fotoComunidade"),
                        res.getString("corFundoDoPost"),
                        res.getString("corFonteDoPost"));
            }
            res.close();
        } catch (Exception e) {
            e.printStackTrace();
            com = null;
        } finally {
            try {
                con.close();
                stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (com == null) {
            System.err.println("COMUNIDADE NULL");
        } else {
            System.out.println("Comunidade encontrada!");
            if (com.getArtista() == null) {
                System.err.println("ARTISTA NULO");
            } else {
                System.out.println("artista encontrado!");
            }
        }
        return com;
    }

    public ArrayList<Comunidade> getListaComunidade() {
        Statement stmt;
        ArrayList<Comunidade> listaComunidades = new ArrayList();
        try {
            String sql = "SELECT * FROM comunidade"
                    + " INNER JOIN usuario artista ON comunidade.fkUsuarioPrincipal = artista.idUsuario";
            stmt = con.createStatement();
            ResultSet res = stmt.executeQuery(sql);

            while (res.next()) {
                Artista a = new Artista(res.getInt("idUsuario"),
                        res.getString("nome"),
                        res.getString("email"),
                        res.getString("senha"),
                        res.getString("nomeArtistico"),
                        res.getBytes("fotoPerfil"));

                Comunidade c = new Comunidade(res.getInt("idComunidade"),
                        a,
                        res.getBytes("fotoComunidade"),
                        res.getString("corFundoDoPost"),
                        res.getString("corFonteDoPost"));
                listaComunidades.add(c);
            }
            stmt.close();
            res.close();
        } catch (SQLException e) {
            System.out.println("ERRO SQL: " + e.getMessage());
            listaComunidades = null;
            try {
                con.rollback();
            } catch (SQLException e1) {
                System.out.println("ERRO SQL: " + e1.getMessage());
            }
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("ERRO SQL: " + e.getMessage());
            }
        }
        return listaComunidades;
    }

    public ArrayList<Comunidade> getListaComunidade(Usuario u){
        PreparedStatement stmt;
        ArrayList<Comunidade> listaComunidades = new ArrayList();
        try {
            String sql = "SELECT comunidade.*, artista.* from comunidade"
                    +" INNER JOIN usuario artista ON comunidade.fkUsuarioPrincipal = artista.idUsuario" 
                    +" INNER JOIN usuariosDaComunidade uc ON comunidade.idComunidade = uc.fkComunidade" 
                    +" INNER JOIN usuario comum ON uc.fkUsuario = comum.idUsuario" 
                    +" WHERE comum.idUsuario = ?";
                    
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, u.getCodUsuario());
            
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                
                String bio = res.getString("bio");
                Artista a;
                if(res.wasNull()){ //sem bio
                    a = new Artista(res.getInt("idUsuario"),
                        res.getString("nome"),
                        res.getString("email"),
                        res.getString("senha"),
                        res.getString("nomeArtistico"),
                        res.getBytes("fotoPerfil"));
                } else {//com bio
                    a = new Artista(res.getInt("idUsuario"),
                        res.getString("nome"),
                        res.getString("email"),
                        res.getString("senha"),
                        res.getString("nomeArtistico"),
                        res.getBytes("fotoPerfil"),
                        bio);
                }
                
                Comunidade c = new Comunidade(res.getInt("idComunidade"),
                        a,
                        res.getBytes("fotoComunidade"),
                        res.getString("corFundoDoPost"),
                        res.getString("corFonteDoPost"));
                listaComunidades.add(c);
            }
            stmt.close();
            res.close();
        } catch (SQLException e) {
            System.out.println("ERRO SQL: " + e.getMessage());
            listaComunidades = null;
            try {
                con.rollback();
            } catch (SQLException e1) {
                System.out.println("ERRO SQL: " + e1.getMessage());
            }
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("ERRO SQL: " + e.getMessage());
            }
        }
        return listaComunidades;
    }
    
    public ArrayList<Comunidade> getListaComunidade(ArrayList<Tag> listaTagWhere){
        PreparedStatement stmt;
        ArrayList<Comunidade> listaComunidades = new ArrayList();
        try {
            
            String clausulaWhereComPlaceHolder = "";
            for(int i = 0; i < listaTagWhere.size(); i++){
                clausulaWhereComPlaceHolder += "fkTag = ?";
                
                //se não estiver na ultima posição da lista
                if(i < listaTagWhere.size()-1){
                    clausulaWhereComPlaceHolder += " OR ";
                }
            }
            
            String sql = "SELECT DISTINCT comunidade.*, artista.* from comunidade"
                    +" INNER JOIN usuario artista ON comunidade.fkUsuarioPrincipal = artista.idUsuario" 
                    +" LEFT JOIN usuariosDaComunidade uc ON comunidade.idComunidade = uc.fkComunidade" 
                    +" LEFT JOIN usuario comum ON uc.fkUsuario = comum.idUsuario" 
                    +" WHERE artista.idUsuario IN ("
                    +" SELECT DISTINCT fkUsuario"
                    +" FROM tagsDoUsuario"
                    +" WHERE " + clausulaWhereComPlaceHolder
                    +")";
                    
            System.out.println("SQL: "+sql);
            
            stmt = con.prepareStatement(sql);
            
            for(int i = 0; i < listaTagWhere.size(); i++){
                Tag t = listaTagWhere.get(i);
                stmt.setInt((i+1), t.getIdTag()); 
            }
            
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                
                String bio = res.getString("bio");
                Artista a;
                if(res.wasNull()){ //sem bio
                    a = new Artista(res.getInt("idUsuario"),
                        res.getString("nome"),
                        res.getString("email"),
                        res.getString("senha"),
                        res.getString("nomeArtistico"),
                        res.getBytes("fotoPerfil"));
                } else {//com bio
                    a = new Artista(res.getInt("idUsuario"),
                        res.getString("nome"),
                        res.getString("email"),
                        res.getString("senha"),
                        res.getString("nomeArtistico"),
                        res.getBytes("fotoPerfil"),
                        bio);
                }
                
                Comunidade c = new Comunidade(res.getInt("idComunidade"),
                        a,
                        res.getBytes("fotoComunidade"),
                        res.getString("corFundoDoPost"),
                        res.getString("corFonteDoPost"));
                System.out.println("comunidade recomendada: "+c.getArtista().getNomeArtistico());
                listaComunidades.add(c);
            }
            stmt.close();
            res.close();
        } catch (SQLException e) {
            System.out.println("ERRO SQL: " + e.getMessage());
            listaComunidades = null;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("ERRO SQL: " + e.getMessage());
            }
        }
        if(listaComunidades != null){
            System.out.println("Quantidade de comunidades recomendadas: "+listaComunidades.size());
        } else{
            System.out.println("lista de comunidades recomendadas NULL");
        }
        
        return listaComunidades;
    }

    public boolean editarComunidade(Comunidade c) {
        PreparedStatement stmtComunidade;
        PreparedStatement stmtArtista;
        boolean res = false;
        System.out.println("Recebido - Cor Fundo: " + c.getCorFundoPost());
        System.out.println("Recebido - Cor Fonte: " + c.getCorFontePost());
        try {
            con.setAutoCommit(false);
            String sqlComunidade = "UPDATE comunidade SET fotoComunidade = ?, corFundoDoPost = ?, corFonteDoPost = ? WHERE idComunidade = ?";
            String sqlArtista = "UPDATE usuario SET nomeArtistico = ?, fotoPerfil = ?, bio = ? WHERE idUsuario = ?";

            stmtComunidade = con.prepareStatement(sqlComunidade);
            stmtArtista = con.prepareStatement(sqlArtista);

            stmtComunidade.setBytes(1, c.getFotoBackground());
            stmtComunidade.setString(2, c.getCorFundoPost());

            System.out.println("cor fundo post: " + c.getCorFundoPost());

            stmtComunidade.setString(3, c.getCorFontePost());

            System.out.println("cor fonte post: " + c.getCorFontePost());
            stmtComunidade.setInt(4, c.getIdComunidade());

            stmtArtista.setString(1, c.getArtista().getNomeArtistico());
            stmtArtista.setBytes(2, c.getArtista().getFotoPerfil());
            System.out.println("bio recebida pre update: " + c.getArtista().getBio());
            stmtArtista.setString(3, c.getArtista().getBio());
            stmtArtista.setInt(4, c.getArtista().getCodUsuario());

            System.out.println("Atualizando - Cor Fundo: " + c.getCorFundoPost());
            System.out.println("Atualizando - Cor Fonte: " + c.getCorFontePost());
            stmtComunidade.execute();
            stmtArtista.execute();

            con.commit();
            res = true;
            stmtComunidade.close();
            stmtArtista.close();
        } catch (SQLException e) {
            res = false;
            System.out.println("Erro SQL: " + e.getMessage());
            try {
                con.rollback();
            } catch (SQLException e1) {
                System.out.println("Erro SQL: " + e1.getMessage());
            }
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("Erro SQL: " + e.getMessage());
            }
        }
        return res;
    }
}
