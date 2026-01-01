package controller;

import model.DetalleVenta;
import model.Ventas;
import service.VentaService;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "VentaServlet", urlPatterns = {"/api/ventas"})
public class VentaServlet extends HttpServlet {

    private final VentaService service = new VentaService();

    // Configuración CORS
    private void setAccessControlHeaders(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "*"); 
        resp.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setAccessControlHeaders(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setAccessControlHeaders(response);
        response.setContentType("application/json;charset=UTF-8");

        try {
            double total = Double.parseDouble(request.getParameter("total"));
            String idsStr = request.getParameter("ids_productos");
            String cantsStr = request.getParameter("cantidades");
            String preciosStr = request.getParameter("precios_unitarios");

            String[] idsArray = idsStr.split(",");
            String[] cantsArray = cantsStr.split(",");
            String[] preciosArray = preciosStr.split(",");

            List<DetalleVenta> detalles = new ArrayList<>();

            for (int i = 0; i < idsArray.length; i++) {
                DetalleVenta dv = new DetalleVenta();
                dv.setIdProducto(Integer.parseInt(idsArray[i]));
                dv.setCantidad(Integer.parseInt(cantsArray[i]));
                
                double precioU = Double.parseDouble(preciosArray[i]);
                dv.setPrecioUnitario(precioU);
                
                dv.setSubtotal(precioU * dv.getCantidad());

                detalles.add(dv);
            }
            
            Ventas venta = new Ventas();
            venta.setTotal(total);

            boolean exito = service.registrarVenta(venta, detalles);

            try (PrintWriter out = response.getWriter()) {
                out.print(exito ? "{\"status\":\"success\"}" : "{\"status\":\"error\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            try (PrintWriter out = response.getWriter()) {
                out.print("{\"status\":\"error\", \"message\":\"Datos inválidos\"}");
            }
        }
    }
}