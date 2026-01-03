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

@WebServlet(name = "HistorialVentasServlet", urlPatterns = {"/api/ventas/historial"})
public class HistorialVentasServlet extends HttpServlet {

    private final DashboardDAO dao = new DashboardDAO();
    // Array auxiliar para convertir 1 -> "Enero"
    private final String[] nombresMeses = {"", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", 
                                           "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json;charset=UTF-8");

        // Obtenemos datos: { 1: 500.00, 2: 1200.00, ... }
        Map<Integer, Double> ventasMes = dao.obtenerVentasPorMes();

        try (PrintWriter out = response.getWriter()) {
            StringBuilder json = new StringBuilder("[");
            
            int contador = 0;
            int totalElementos = ventasMes.size();
            
            for (Map.Entry<Integer, Double> entry : ventasMes.entrySet()) {
                int numeroMes = entry.getKey();
                String nombreMes = (numeroMes >= 1 && numeroMes <= 12) ? nombresMeses[numeroMes] : "Desc";
                
                json.append("{")
                    .append("\"mes\":\"").append(nombreMes).append("\",")
                    .append("\"total\":").append(entry.getValue())
                    .append("}");
                
                if (contador < totalElementos - 1) json.append(",");
                contador++;
            }
            
            json.append("]");
            out.print(json.toString());
        }
    }
}