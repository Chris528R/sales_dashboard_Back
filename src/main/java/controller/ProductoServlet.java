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
        
        try (PrintWriter out = response.getWriter()) {
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
                    .append("\"categoria\":").append(p.getIdCategoria())
                    .append("}");
                if (i < lista.size() - 1) {
                    json.append(",");
                }
            }
            json.append("]");
            out.print(json.toString());
        }
    }

    // Registrar producto (POST)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setAccessControlHeaders(response);
        response.setContentType("application/json;charset=UTF-8");

        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        String tipo = request.getParameter("tipo");
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

        boolean exito = service.registrar(p);

        try (PrintWriter out = response.getWriter()) {
            out.print(exito ? "{\"status\":\"success\"}" : "{\"status\":\"error\"}");
        }
    }
    
    // TODO Actualizar y eliminar producto
    
}