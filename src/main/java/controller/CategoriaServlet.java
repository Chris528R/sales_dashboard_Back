package controller;

import dao.CategoriaDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "CategoriaServlet", urlPatterns = {"/api/categorias"})
public class CategoriaServlet extends HttpServlet {

    private final CategoriaDAO dao = new CategoriaDAO();

    // Configuración CORS simple
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
        
        // Headers CORS y Tipo JSON
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json;charset=UTF-8");
        
        List<Map<String, Object>> lista = dao.listarTodas();

        try (PrintWriter out = response.getWriter()) {
            StringBuilder json = new StringBuilder("[");
            
            for (int i = 0; i < lista.size(); i++) {
                Map<String, Object> cat = lista.get(i);
                json.append("{")
                    .append("\"id\":").append(cat.get("id")).append(",")
                    .append("\"nombre\":\"").append(cat.get("nombre")).append("\"")
                    .append("}");
                
                if (i < lista.size() - 1) json.append(",");
            }
            json.append("]");
            out.print(json.toString());
        }
    }
}