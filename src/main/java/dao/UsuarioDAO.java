package dao;

import model.Usuario;
import config.DB;
import java.sql.*;
public class UsuarioDAO {
    
    public Usuario login(String nombre, String cont){
        Usuario user = null;
        
        try(Connection conn = DB.getConnection()){
            String sql = "select * from usuario where nombre=? and contrasenia=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nombre);
            stmt.setString(2, cont);
            
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next()){
                user = new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("contrasenia")
                );
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }
}
