package dao;

import config.DB;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoriaDAO {

    public List<Map<String, Object>> listarTodas() {
        List<Map<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT * FROM categorias ORDER BY nombre_categoria ASC";

        try ( Connection conn = DB.getConnection();  
                Statement stmt = conn.createStatement();  
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", rs.getInt("id_categoria"));
                map.put("nombre", rs.getString("nombre_categoria"));
                lista.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean crear(String nombre) {
        String sql = "INSERT INTO categorias (nombre_categoria) VALUES (?)";
        try ( Connection conn = DB.getConnection();  
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM categorias WHERE id_categoria = ?";
        try ( Connection conn = DB.getConnection();  
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
