package service;

import dao.VentasDAO;
import model.DetalleVenta;
import model.Ventas;
import java.util.List;

public class VentaService {
    private final VentasDAO ventaDAO = new VentasDAO();

    public boolean registrarVenta(Ventas venta, List<DetalleVenta> detalles) {
        return ventaDAO.registrarVenta(venta, detalles);
    }
    
    public List<Ventas> listarVentas() {
        return ventaDAO.listarVentas();
    }
    
    public List<DetalleVenta> obtenerDetalles(int idVenta) {
        return ventaDAO.obtenerDetalles(idVenta);
    }
    
    public boolean eliminarVenta(int idVenta){
        return ventaDAO.eliminarVenta(idVenta);
    }
    
    public boolean actualizarVenta(Ventas ventaAct, List<DetalleVenta> nuevosDetalles){
        return ventaDAO.actualizarVenta(ventaAct, nuevosDetalles);
    }
}