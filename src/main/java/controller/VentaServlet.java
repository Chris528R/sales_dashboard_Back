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

    // REGISTRAR / ACTUALIZAR UINA VENTA
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

            // Validar que vengan datos
            if (idsStr == null || cantsStr == null || preciosStr == null) {
                throw new Exception("Faltan datos de los productos");
            }

            String[] idsArray = idsStr.split(",");
            String[] cantsArray = cantsStr.split(",");
            String[] preciosArray = preciosStr.split(",");

            List<DetalleVenta> detalles = new ArrayList<>();

            // CONSTRUIR LA LISTA DE DETALLES
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

            // DECIDIR SI ES REGISTRAR O ACTUALIZAR SEGÚN SI LLEGA EL ID
            String idStr = request.getParameter("id");
            boolean exito = false;

            if (idStr != null && !idStr.isEmpty()) {
                // ACTUALIZAR 
                int idVenta = Integer.parseInt(idStr);
                venta.setId(idVenta); 
                exito = service.actualizarVenta(venta, detalles);
            } else {
                // REGISTRAR NUEVO 
                exito = service.registrarVenta(venta, detalles);
            }

            // RESPONDER
            try ( PrintWriter out = response.getWriter()) {
                out.print(exito ? "{\"status\":\"success\"}" : "{\"status\":\"error\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            try ( PrintWriter out = response.getWriter()) {
                String errorMsg = e.getMessage().replace("\"", "'");
                out.print("{\"status\":\"error\", \"message\":\"" + errorMsg + "\"}");
            }
        }
    }

    // MOSTRAR VENTAS
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setAccessControlHeaders(response);
        response.setContentType("application/json;charset=UTF-8");

        String idStr = request.getParameter("id");

        try ( PrintWriter out = response.getWriter()) {

            if (idStr == null) {
                // LISTAR TODAS LAS VENTAS
                List<Ventas> lista = service.listarVentas();
                StringBuilder json = new StringBuilder("[");
                for (int i = 0; i < lista.size(); i++) {
                    Ventas v = lista.get(i);
                    json.append("{")
                            .append("\"id\":").append(v.getId()).append(",")
                            .append("\"fecha\":\"").append(v.getFechaVenta()).append("\",")
                            .append("\"total\":").append(v.getTotal())
                            .append("}");
                    if (i < lista.size() - 1) {
                        json.append(",");
                    }
                }
                json.append("]");
                out.print(json.toString());

            } else {
                // VER DETALLE DE UNA VENTA ESPECÍFICA
                int idVenta = Integer.parseInt(idStr);
                List<DetalleVenta> detalles = service.obtenerDetalles(idVenta);

                StringBuilder json = new StringBuilder("[");
                for (int i = 0; i < detalles.size(); i++) {
                    DetalleVenta d = detalles.get(i);
                    json.append("{")
                            .append("\"id_producto\":").append(d.getIdProducto()).append(",")
                            .append("\"nombre\":\"").append(d.getNombre()).append("\",")
                            .append("\"cantidad\":").append(d.getCantidad()).append(",")
                            .append("\"precio\":").append(d.getPrecioUnitario()).append(",")
                            .append("\"subtotal\":").append(d.getSubtotal())
                            .append("}");
                    if (i < detalles.size() - 1) {
                        json.append(",");
                    }
                }
                json.append("]");
                out.print(json.toString());
            }
        }
    }

    // Eliminar venta (DELETE)
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setAccessControlHeaders(response);
        response.setContentType("application/json;charset=UTF-8");

        String idParam = request.getParameter("id");

        boolean eliminado = false;

        if (idParam != null) {
            int id = Integer.parseInt(idParam);
            eliminado = service.eliminarVenta(id);
        }

        try ( PrintWriter out = response.getWriter()) {
            if (eliminado) {
                out.print("{\"status\":\"success\"}");
            } else {
                out.print("{\"status\":\"error\"}");
            }
            out.flush();
        }
    }
}
