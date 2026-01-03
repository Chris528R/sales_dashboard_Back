package controller;

import dao.DashboardDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "DashboardServlet", urlPatterns = {"/api/dashboard"})
public class DashboardServlet extends HttpServlet {

    private final DashboardDAO dao = new DashboardDAO();

    private void setAccessControlHeaders(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setAccessControlHeaders(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setAccessControlHeaders(response);
        response.setContentType("application/json;charset=UTF-8");

        // Obtener datos del DAO
        double ventasHoy = dao.obtenerVentasHoy();
        int bajoStock = dao.contarProductosBajoStock(10); // Alerta si hay menos de 10
        Map<String, Double> ventasPorCat = dao.obtenerVentasPorCategoria();
        Map<String, Integer> topProductos = dao.obtenerTopProductos();

        try (PrintWriter out = response.getWriter()) {
            StringBuilder json = new StringBuilder("{");

            // Datos simples
            json.append("\"ventasHoy\":").append(ventasHoy).append(",");
            json.append("\"productosBajoStock\":").append(bajoStock).append(",");

            // Array Categorías
            json.append("\"ventasPorCategoria\":[");
            int i = 0;
            for (Map.Entry<String, Double> entry : ventasPorCat.entrySet()) {
                json.append("{\"categoria\":\"").append(entry.getKey()).append("\",")
                    .append("\"monto\":").append(entry.getValue()).append("}");
                if (i++ < ventasPorCat.size() - 1) json.append(",");
            }
            json.append("],");

            // Array Top Productos
            json.append("\"topProductos\":[");
            int j = 0;
            for (Map.Entry<String, Integer> entry : topProductos.entrySet()) {
                json.append("{\"producto\":\"").append(entry.getKey()).append("\",")
                    .append("\"cantidad\":").append(entry.getValue()).append("}");
                if (j++ < topProductos.size() - 1) json.append(",");
            }
            json.append("]");

            json.append("}"); // Cerrar objeto principal
            out.print(json.toString());
        }
    }
}