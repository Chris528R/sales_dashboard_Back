package dao;

import model.Producto;
import config.DB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    // Listar todos los productos
    public List<Producto> listar() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.*, c.nombre_categoria FROM productos p "
                + "LEFT JOIN categorias c ON p.id_categoria = c.id_categoria "
                + "ORDER BY p.id_producto DESC";
        try (Connection conn = DB.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Producto p = mapearProducto(rs);
                lista.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Obtener producto por ID
    public Producto obtenerPorId(int id) {
        Producto p = null;
        String sql = "SELECT * FROM productos WHERE id_producto = ?";

        try (Connection conn = DB.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    p = mapearProducto(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }

    // Actualizar un producto
    public boolean actualizar(Producto p) {
        String sql = "UPDATE productos SET nombre=?, descripcion=?, tipo=?, id_categoria=?, "
                + "precio=?, stock=?, unidad_medida=? WHERE id_producto=?";
        try (Connection conn = DB.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setString(3, p.getTipo());
            ps.setInt(4, p.getIdCategoria());
            ps.setDouble(5, p.getPrecio());
            ps.setInt(6, p.getStock());
            ps.setString(7, p.getUnidadMedida());
            ps.setInt(8, p.getId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar un producto
    public boolean eliminar(int id) {
        String sql = "DELETE FROM productos WHERE id_producto = ?";
        try (Connection conn = DB.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Registrar nuevo producto
    public boolean registrar(Producto p) {
        String sql = "INSERT INTO productos (nombre, descripcion, tipo, id_categoria, precio, stock, unidad_medida) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
                     
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setString(3, p.getTipo());
            ps.setInt(4, p.getIdCategoria()); 
            ps.setDouble(5, p.getPrecio());
            ps.setInt(6, p.getStock());
            ps.setString(7, p.getUnidadMedida());

            return ps.executeUpdate() > 0;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método auxiliar para no repetir código
    private Producto mapearProducto(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getInt("id_producto"));
        p.setNombre(rs.getString("nombre"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setTipo(rs.getString("tipo"));
        p.setIdCategoria(rs.getInt("id_categoria"));  
        p.setPrecio(rs.getDouble("precio"));
        p.setStock(rs.getInt("stock"));
        p.setUnidadMedida(rs.getString("unidad_medida"));
        p.setFechaRegistro(rs.getTimestamp("fecha_registro")); 
        return p;
    }
}
