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
}