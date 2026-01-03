package dao;

import config.DB;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;

public class DashboardDAO {

    // Ventas totales del dia
    public double obtenerVentasHoy() {
        double total = 0;
        String sql = "SELECT SUM(total) FROM ventas WHERE DATE(fecha_venta) = CURDATE()";
        try ( Connection conn = DB.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                total = rs.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    // Productos con stock bajo 
    public List<String> obtenerListaBajoStock(int limite) {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT nombre, stock FROM productos WHERE stock <= ?";
        try ( Connection conn = DB.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limite);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Formato: "Aceite (2)"
                lista.add(rs.getString("nombre") + " (" + rs.getInt("stock") + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Ventas por categoría
    public Map<String, Double> obtenerVentasPorCategoria() {
        Map<String, Double> datos = new HashMap<>();
        String sql = "SELECT c.nombre_categoria, SUM(d.subtotal) as total "
                + "FROM detalle_venta d "
                + "JOIN productos p ON d.id_producto = p.id_producto "
                + "JOIN categorias c ON p.id_categoria = c.id_categoria "
                + "GROUP BY c.nombre_categoria";

        try ( Connection conn = DB.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                datos.put(rs.getString("nombre_categoria"), rs.getDouble("total"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datos;
    }

    // Top 5 Productos más vendidos (ordenados) 
    public Map<String, Integer> obtenerTopProductos() {
        Map<String, Integer> datos = new LinkedHashMap<>();
        String sql = "SELECT p.nombre, SUM(d.cantidad) as total_vendido "
                + "FROM detalle_venta d "
                + "JOIN productos p ON d.id_producto = p.id_producto "
                + "GROUP BY p.id_producto, p.nombre "
                + "ORDER BY total_vendido DESC LIMIT 5";

        try ( Connection conn = DB.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                datos.put(rs.getString("nombre"), rs.getInt("total_vendido"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datos;
    }

    // Obtener las ventas por mes (últimos 6 meses)
    // Retorna un Mapa: Clave=Mes(int), Valor=Total(double)
    public Map<Integer, Double> obtenerVentasPorMes() {
        Map<Integer, Double> datos = new LinkedHashMap<>();
        String sql = "SELECT MONTH(fecha_venta) as mes, SUM(total) as total "
                + "FROM ventas "
                + "WHERE fecha_venta >= DATE_SUB(NOW(), INTERVAL 6 MONTH) "
                + "GROUP BY YEAR(fecha_venta), MONTH(fecha_venta) "
                + "ORDER BY YEAR(fecha_venta), MONTH(fecha_venta)";

        try ( Connection conn = DB.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                datos.put(rs.getInt("mes"), rs.getDouble("total"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datos;
    }
}
