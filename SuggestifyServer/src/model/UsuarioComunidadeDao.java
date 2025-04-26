package model;

import factory.Conector;
import java.sql.*;
import java.util.ArrayList;
import modelDominio.Artista;
import modelDominio.Comum;
import modelDominio.Comunidade;

public class UsuarioComunidadeDao {

    private Connection con;
    
    public UsuarioComunidadeDao(){
        con=Conector.getConnection();
    }
    
    public boolean vinculaUsuarioComunidade(Comum c, Comunidade comu){
        PreparedStatement stmt;
        boolean res;
        try {
            con.setAutoCommit(false);
            String sql = "insert into usuariosDaComunidade(idUsuariosDaComunidade,fkUsuario,fkComunidade)"
                +" values(null,?,?)";
            
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, c.getCodUsuario());
            stmt.setInt(2, comu.getIdComunidade());
            
            stmt.execute();
            con.commit();
            res=true;
            stmt.close();
        } catch (SQLException e) {
            res=false;
            System.out.println("ERRO SQL: "+e.getMessage());
            try {
                con.rollback();
            } catch (Exception e1) {
                System.out.println("ERRO SQL: "+e1.getMessage());
            }
        } finally{
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("ERRO SQL: "+e.getMessage());
            }
        }
        return res;
    }
    
    public boolean desvinculaUsuarioComunidade(Comum c, Comunidade comu){
        PreparedStatement stmt;
        boolean res;
        try {
            con.setAutoCommit(false);
            String sql = " DELETE FROM usuariosDaComunidade WHERE fkUsuario = ? AND fkComunidade = ?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, c.getCodUsuario());
            stmt.setInt(2, comu.getIdComunidade());
            
            stmt.execute();
            con.commit();
            res=true;
            stmt.close();
        } catch (SQLException e) {
            res=false;
            System.out.println("ERRO SQL: "+e.getMessage());
            try {
                con.rollback();
            } catch (Exception e1) {
                System.out.println("ERRO SQL: "+e1.getMessage());
            }
        } finally{
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("ERRO SQL: "+e.getMessage());
            }
        }
        return res;
        
    }
    
    public ArrayList<Comunidade> getListaComunidadesDoUsuario(Comum c){
        PreparedStatement stmt;
        ArrayList<Comunidade> listaComunidades = new ArrayList();
        try {
            String sql ="SELECT comunidade.*, artista.* FROM comunidade" 
                    +" INNER JOIN usuariosDaComunidade uc ON comunidade.idComunidade = uc.fkComunidade" 
                    +" INNER JOIN usuario artista on comunidade.fkUsuarioPrincipal = artista.idUsuario" 
                    +" WHERE uc.fkUsuario = ?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, c.getCodUsuario());
            
            ResultSet res = stmt.executeQuery();
            while(res.next()){
                String bio = res.getString("bio");
                Artista a;
                if(!res.wasNull()){
                    //com bio
                    a = new Artista(res.getInt("idUsuario"),
                            res.getString("nome"),
                            res.getString("email"),
                            res.getString("senha"), 
                            res.getString("nomeArtistico"), 
                            res.getBytes("fotoPerfil"), 
                            bio);
                } else {
                    //sem bio
                    a = new Artista(res.getInt("idUsuario"),
                            res.getString("nome"),
                            res.getString("email"),
                            res.getString("senha"), 
                            res.getString("nomeArtistico"), 
                            res.getBytes("fotoPerfil"));
                }
                
                listaComunidades.add(new Comunidade(res.getInt("idComunidade"),
                        a,
                        res.getBytes("fotoComunidade"), 
                        res.getString("corFundoDoPost"), 
                        res.getString("corFonteDoPost")));
            }
            stmt.close();
            res.close();
        } catch (SQLException e) {
            System.out.println("ERRO SQL: "+e.getMessage());
            listaComunidades = null;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("ERRO SQL: "+e.getMessage());
            }
        }
        return listaComunidades;
    }
    
}
