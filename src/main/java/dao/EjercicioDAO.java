package dao;

import model.Ejercicio;
import config.DB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EjercicioDAO {

    public List<Ejercicio> listar() {
        List<Ejercicio> lista = new ArrayList<>();
        String sql = "SELECT * FROM ejercicios ORDER BY id_ejercicio DESC";
        try (Connection conn = DB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Ejercicio e = new Ejercicio();
                e.setId(rs.getInt("id_ejercicio"));
                e.setTitulo(rs.getString("titulo"));
                e.setNivel(rs.getString("nivel"));
                e.setContenidoJson(rs.getString("contenido_json")); 
                e.setMejorTiempo(rs.getInt("mejor_tiempo"));
                lista.add(e);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }

    public Ejercicio obtenerPorId(int id) {
        Ejercicio e = null;
        String sql = "SELECT * FROM ejercicios WHERE id_ejercicio = ?";
        try (Connection conn = DB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                e = new Ejercicio();
                e.setId(rs.getInt("id_ejercicio"));
                e.setTitulo(rs.getString("titulo"));
                e.setNivel(rs.getString("nivel"));
                e.setContenidoJson(rs.getString("contenido_json"));
            }
        } catch (Exception ex) { ex.printStackTrace(); }
        return e;
    }

    public boolean guardar(Ejercicio e) {
        String sql = "INSERT INTO ejercicios (titulo, nivel, contenido_json) VALUES (?, ?, ?)";
        try (Connection conn = DB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getTitulo());
            ps.setString(2, e.getNivel());
            ps.setString(3, e.getContenidoJson());
            return ps.executeUpdate() > 0;
        } catch (Exception ex) { ex.printStackTrace(); return false; }
    }

    public boolean actualizar(Ejercicio e) {
        String sql = "UPDATE ejercicios SET titulo=?, nivel=?, contenido_json=? WHERE id_ejercicio=?";
        try (Connection conn = DB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getTitulo());
            ps.setString(2, e.getNivel());
            ps.setString(3, e.getContenidoJson());
            ps.setInt(4, e.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception ex) { ex.printStackTrace(); return false; }
    }
    
    public boolean eliminar(int id) {
        String sql = "DELETE FROM ejercicios WHERE id_ejercicio=?";
        try (Connection conn = DB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception ex) { ex.printStackTrace(); return false; }
    }
    
    public boolean actualizarMejorTiempo(int id, int tiempoNuevo) {
        String sql = "UPDATE ejercicios SET mejor_tiempo = ? " +
                     "WHERE id_ejercicio = ? AND (mejor_tiempo = 0 OR mejor_tiempo > ?)";
        
        try (Connection conn = DB.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, tiempoNuevo);
            ps.setInt(2, id);
            ps.setInt(3, tiempoNuevo);
            ps.executeUpdate(); 
            return true;
            
        } catch (Exception ex) { 
            ex.printStackTrace(); 
            return false; 
        }
    }
}