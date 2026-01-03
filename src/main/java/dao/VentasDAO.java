package dao;

import model.Ventas;
import model.DetalleVenta; 
import config.DB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentasDAO {

    // Listar todas las ventas
    public List<Ventas> listarVentas() {
        List<Ventas> lista = new ArrayList<>();
        String sql = "SELECT * FROM ventas ORDER BY fecha_venta DESC";
        
        try (Connection conn = DB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while(rs.next()){
                Ventas v = new Ventas();
                v.setId(rs.getInt("id_venta"));
                v.setFechaVenta(rs.getTimestamp("fecha_venta"));
                v.setTotal(rs.getDouble("total"));
                lista.add(v);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }

    // Para obtener toda la informacion de una venta
    public List<DetalleVenta> obtenerDetalles(int idVenta) {
        List<DetalleVenta> detalles = new ArrayList<>();
        // Hacemos JOIN con productos para saber el nombre del producto vendido
        String sql = "SELECT d.*, p.nombre as nombre_producto FROM detalle_venta d " +
                     "JOIN productos p ON d.id_producto = p.id_producto " +
                     "WHERE d.id_venta = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idVenta);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                DetalleVenta dv = new DetalleVenta();
                dv.setId(rs.getInt("id_detalle"));
                dv.setIdVenta(rs.getInt("id_venta"));
                dv.setIdProducto(rs.getInt("id_producto"));
                dv.setCantidad(rs.getInt("cantidad"));
                dv.setPrecioUnitario(rs.getDouble("precio_unitario"));
                dv.setSubtotal(rs.getDouble("subtotal"));
                dv.setNombre(rs.getString("nombre_producto"));
                detalles.add(dv);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return detalles;
    }
    
    // Resgistrar venta total
    public boolean registrarVenta(Ventas venta, List<DetalleVenta> detalles) {
        Connection conn = null;
        PreparedStatement psVenta = null;
        PreparedStatement psDetalle = null;
        PreparedStatement psStock = null;
        
        String sqlVenta = "INSERT INTO ventas (total) VALUES (?)";
        String sqlDetalle = "INSERT INTO detalle_venta (id_venta, id_producto, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
        String sqlUpdateStock = "UPDATE productos SET stock = stock - ? WHERE id_producto = ?";

        try {
            conn = DB.getConnection();
            conn.setAutoCommit(false);

            psVenta = conn.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS);
            psVenta.setDouble(1, venta.getTotal());
            psVenta.executeUpdate();

            int idVentaGenerado = 0;
            ResultSet rs = psVenta.getGeneratedKeys();
            if (rs.next()) {
                idVentaGenerado = rs.getInt(1);
            } else {
                throw new SQLException("No se generó el ID de venta, cancelando...");
            }

            psDetalle = conn.prepareStatement(sqlDetalle);
            psStock = conn.prepareStatement(sqlUpdateStock);

            for (DetalleVenta dv : detalles) {
                psDetalle.setInt(1, idVentaGenerado);
                psDetalle.setInt(2, dv.getIdProducto());
                psDetalle.setInt(3, dv.getCantidad());
                psDetalle.setDouble(4, dv.getPrecioUnitario());
                psDetalle.setDouble(5, dv.getSubtotal());
                psDetalle.executeUpdate(); 

                psStock.setInt(1, dv.getCantidad());
                psStock.setInt(2, dv.getIdProducto());
                psStock.executeUpdate();
            }

            
            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            // ROLLBACK (Si algo falló, deshacer todo lo que se hizo en este intento)
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        } finally {
            try {
                if (psVenta != null) psVenta.close();
                if (psDetalle != null) psDetalle.close();
                if (psStock != null) psStock.close();
                if (conn != null) conn.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }
    
    // Eliminar venta
    public boolean eliminarVenta(int idVenta) {
        Connection conn = null;
        PreparedStatement psStock = null;
        PreparedStatement psDelete = null;
        
        List<DetalleVenta> detalles = obtenerDetalles(idVenta); //Obtenemos la informacion de la venta para poder regresar al stock anterior
        
        String sqlUpdateStock = "UPDATE productos SET stock = stock + ? WHERE id_producto = ?";
        String sqlDeleteVenta = "DELETE FROM ventas WHERE id_venta = ?";

        try {
            conn = DB.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción

            // Devolver productos al Stock
            psStock = conn.prepareStatement(sqlUpdateStock);
            for (DetalleVenta dv : detalles) {
                psStock.setInt(1, dv.getCantidad());
                psStock.setInt(2, dv.getIdProducto());
                psStock.addBatch(); // Usamos batch por eficiencia
            }
            psStock.executeBatch();

            // Eliminar la venta
            psDelete = conn.prepareStatement(sqlDeleteVenta);
            psDelete.setInt(1, idVenta);
            int rows = psDelete.executeUpdate();

            if (rows > 0) {
                conn.commit(); // Confirmar cambios
                return true;
            } else {
                conn.rollback();
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        } finally {
            try {
                if (psStock != null) psStock.close();
                if (psDelete != null) psDelete.close();
                if (conn != null) conn.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }
    
    // Actualizar venta
    public boolean actualizarVenta(Ventas ventaEditada, List<DetalleVenta> nuevosDetalles) {
        Connection conn = null;
        PreparedStatement psStockDevolver = null;
        PreparedStatement psBorrarDetalles = null;
        PreparedStatement psUpdateVenta = null;
        PreparedStatement psInsertDetalle = null;
        PreparedStatement psStockDescontar = null;

        // Recuperamos los detalles antes de actualizar para devolver ese stock específico
        List<DetalleVenta> detallesViejos = obtenerDetalles(ventaEditada.getId());

        String sqlStockMas = "UPDATE productos SET stock = stock + ? WHERE id_producto = ?";
        String sqlBorrarDetalles = "DELETE FROM detalle_venta WHERE id_venta = ?";
        String sqlUpdateVenta = "UPDATE ventas SET total = ? WHERE id_venta = ?";
        String sqlInsertDetalle = "INSERT INTO detalle_venta (id_venta, id_producto, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
        String sqlStockMenos = "UPDATE productos SET stock = stock - ? WHERE id_producto = ?";

        try {
            conn = DB.getConnection();
            conn.setAutoCommit(false);

            // Devolver stock de la venta antigua
            psStockDevolver = conn.prepareStatement(sqlStockMas);
            for (DetalleVenta dv : detallesViejos) {
                psStockDevolver.setInt(1, dv.getCantidad());
                psStockDevolver.setInt(2, dv.getIdProducto());
                psStockDevolver.addBatch();
            }
            psStockDevolver.executeBatch();

            // Borrar detalles antiguos
            psBorrarDetalles = conn.prepareStatement(sqlBorrarDetalles);
            psBorrarDetalles.setInt(1, ventaEditada.getId());
            psBorrarDetalles.executeUpdate();

            // Actualizar Total nuevo
            psUpdateVenta = conn.prepareStatement(sqlUpdateVenta);
            psUpdateVenta.setDouble(1, ventaEditada.getTotal());
            psUpdateVenta.setInt(2, ventaEditada.getId());
            psUpdateVenta.executeUpdate();

            // Insertar nuevos detalles y descontar nuevo stock
            psInsertDetalle = conn.prepareStatement(sqlInsertDetalle);
            psStockDescontar = conn.prepareStatement(sqlStockMenos);

            for (DetalleVenta dv : nuevosDetalles) {
                // Insertar detalle
                psInsertDetalle.setInt(1, ventaEditada.getId());
                psInsertDetalle.setInt(2, dv.getIdProducto());
                psInsertDetalle.setInt(3, dv.getCantidad());
                psInsertDetalle.setDouble(4, dv.getPrecioUnitario());
                psInsertDetalle.setDouble(5, dv.getSubtotal());
                psInsertDetalle.addBatch();

                // Descontar stock
                psStockDescontar.setInt(1, dv.getCantidad());
                psStockDescontar.setInt(2, dv.getIdProducto());
                psStockDescontar.addBatch();
            }
            psInsertDetalle.executeBatch();
            psStockDescontar.executeBatch();

            // Si no hubo errores, confirmamos todo
            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        } finally {
            try {
                if (psStockDevolver != null) psStockDevolver.close();
                if (psBorrarDetalles != null) psBorrarDetalles.close();
                if (psUpdateVenta != null) psUpdateVenta.close();
                if (psInsertDetalle != null) psInsertDetalle.close();
                if (psStockDescontar != null) psStockDescontar.close();
                if (conn != null) conn.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}