package dao;

import model.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import config.DB;


public class ProductoDAO {
    public List<Producto> listar() {
        List<Producto> lista = new ArrayList<>();

        try (Connection conn = DB.getConnection()) {
            String sql = "SELECT * FROM productos";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setPrecio(rs.getDouble("precio"));
                p.setStock(rs.getInt("stock"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setTipo(rs.getString("tipo"));
                p.setCategoria(rs.getString("categoria"));
                p.setUnidad(rs.getString("unidad_medida"));
                p.setFecha(rs.getDate("fecha_registro"));
                lista.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
