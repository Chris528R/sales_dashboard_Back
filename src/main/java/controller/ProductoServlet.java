package controller;

import model.Producto;
import service.ProductoService;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ProductoServlet", urlPatterns = {"/api/productos"})
public class ProductoServlet extends HttpServlet {

    private final ProductoService service = new ProductoService();

    // Configuración CORS
    private void setAccessControlHeaders(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setAccessControlHeaders(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    // Listar productos (GET)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setAccessControlHeaders(response);
        response.setContentType("application/json;charset=UTF-8");

        String filtro = request.getParameter("filtro");
        List<Producto> lista;

        if ("bajo_stock".equals(filtro)) {
            lista = service.listarBajoStock(5); // 5 por default
        } else {
            lista = service.listar();
        }

        try ( PrintWriter out = response.getWriter()) {
            StringBuilder json = new StringBuilder();
            json.append("[");

            for (int i = 0; i < lista.size(); i++) {
                Producto p = lista.get(i);
                json.append("{")
                        .append("\"id\":").append(p.getId()).append(",")
                        .append("\"nombre\":\"").append(p.getNombre()).append("\",")
                        .append("\"descripcion\":\"").append(p.getDescripcion()).append("\",")
                        .append("\"precio\":").append(p.getPrecio()).append(",")
                        .append("\"stock\":").append(p.getStock()).append(",")
                        .append("\"id_categoria\":").append(p.getIdCategoria()).append(",")
                        .append("\"categoria\":\"").append(p.getCategoria()).append("\"")
                        .append("}");
                if (i < lista.size() - 1) {
                    json.append(",");
                }
            }
            json.append("]");
            out.print(json.toString());
        }
    }

    // Registrar / Actualizar productos
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setAccessControlHeaders(response);
        response.setContentType("application/json;charset=UTF-8");

        try {
            String nombre = request.getParameter("nombre");
            String descripcion = request.getParameter("descripcion");
            String tipo = request.getParameter("tipo");

            // Validaciones básicas para evitar NullPointerException si faltan datos
            if (request.getParameter("precio") == null || request.getParameter("stock") == null) {
                throw new Exception("Precio y Stock son obligatorios");
            }

            double precio = Double.parseDouble(request.getParameter("precio"));
            int stock = Integer.parseInt(request.getParameter("stock"));
            int categoria = Integer.parseInt(request.getParameter("categoria"));
            String unidad = request.getParameter("unidad");

            Producto p = new Producto();
            p.setNombre(nombre);
            p.setDescripcion(descripcion);
            p.setTipo(tipo);
            p.setPrecio(precio);
            p.setStock(stock);
            p.setIdCategoria(categoria);
            p.setUnidadMedida(unidad);

            // Verificamos si es actualizacion o registro de nuevo producto
            String idStr = request.getParameter("id");
            boolean exito = false;

            if (idStr != null && !idStr.isEmpty()) {
                // ACTUALIZAR
                int idProducto = Integer.parseInt(idStr);
                p.setId(idProducto); 
                exito = service.actualizar(p);
            } else {
                // CASO REGISTRAR 
                exito = service.registrar(p);
            }

            try ( PrintWriter out = response.getWriter()) {
                out.print(exito ? "{\"status\":\"success\"}" : "{\"status\":\"error\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            try ( PrintWriter out = response.getWriter()) {
                String msg = e.getMessage() != null ? e.getMessage().replace("\"", "'") : "Error desconocido";
                out.print("{\"status\":\"error\", \"message\":\"" + msg + "\"}");
            }
        }
    }

    // Eliminar producto (DELETE)
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setAccessControlHeaders(response);
        response.setContentType("application/json;charset=UTF-8");

        String idParam = request.getParameter("id");

        boolean eliminado = false;

        if (idParam != null) {
            int id = Integer.parseInt(idParam);
            eliminado = service.eliminar(id);
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
