package model;

import factory.Conector;
import java.sql.*;
import modelDominio.Tag;
import modelDominio.Usuario;

public class UsuarioTagDao {
    
    Connection con;
    
    public UsuarioTagDao(){
        con = Conector.getConnection();
    }
    
    public boolean vincularUsuarioTag(Object[] tagEUsuario){
        PreparedStatement stmt = null;
        boolean result = false;
        Tag t = (Tag)tagEUsuario[0];
        Usuario u = (Usuario)tagEUsuario[1];
        try {
            con.setAutoCommit(false);
            String sql = "INSERT INTO tagsdousuario(idTagDoUsuario,fkTag,fkUsuario) VALUES(null,?,?)";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, t.getIdTag());
            stmt.setInt(2, u.getCodUsuario());
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
    
    public boolean desvincularUsuarioTag(Object[] tagEUsuario){
        PreparedStatement stmt = null;
        boolean result = false;
        Tag t = (Tag)tagEUsuario[0];
        Usuario u = (Usuario)tagEUsuario[1];
        try {
            con.setAutoCommit(false);
            String sql = "DELETE FROM tagsdousuario WHERE fkTag = ? AND fkUsuario = ?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, t.getIdTag());
            stmt.setInt(2, u.getCodUsuario());
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
    
}
