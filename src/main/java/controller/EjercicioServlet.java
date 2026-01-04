package controller;

import dao.EjercicioDAO;
import model.Ejercicio;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "EjercicioServlet", urlPatterns = {"/api/ejercicios"})
public class EjercicioServlet extends HttpServlet {

    private final EjercicioDAO dao = new EjercicioDAO();

    // CORS
    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setAccessControlHeaders(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private void setAccessControlHeaders(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }

    // OBTENER PREGUNTAS (Lista o Uno solo)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setAccessControlHeaders(response);
        response.setContentType("application/json;charset=UTF-8");

        String idStr = request.getParameter("id");
        try (PrintWriter out = response.getWriter()) {
            if (idStr == null) {
                // LISTAR TODOS
                List<Ejercicio> lista = dao.listar();
                StringBuilder json = new StringBuilder("[");
                for (int i = 0; i < lista.size(); i++) {
                    Ejercicio e = lista.get(i);
                    json.append(String.format("{\"id\":%d,\"titulo\":\"%s\",\"nivel\":\"%s\",\"mejorTiempo\":\"%d\"}", 
                            e.getId(), e.getTitulo(), e.getNivel(), e.getMejorTiempo()));
                    if (i < lista.size() - 1) json.append(",");
                }
                json.append("]");
                out.print(json.toString());
            } else {
                // OBTENER UNO (Incluye el contenido_json completo)
                Ejercicio e = dao.obtenerPorId(Integer.parseInt(idStr));
                if (e != null) {
                    out.print(String.format("{\"id\":%d,\"titulo\":\"%s\",\"nivel\":\"%s\",\"contenido\":%s}", 
                            e.getId(), e.getTitulo(), e.getNivel(), e.getContenidoJson()));
                } else {
                    out.print("{}");
                }
            }
        }
    }

    // 2. GUARDAR / ACTUALIZAR UNA PREGUNTA
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setAccessControlHeaders(response);
        response.setContentType("application/json;charset=UTF-8");

        try {// DETECTAR SI ES UNA ACTUALIZACIÓN DE TIEMPO (RÉCORD)
            String action = request.getParameter("action");
            
            if ("save_record".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                int tiempo = Integer.parseInt(request.getParameter("tiempo"));
                
                boolean exito = dao.actualizarMejorTiempo(id, tiempo);
                
                try (PrintWriter out = response.getWriter()) {
                    out.print("{\"status\":\"success\"}");
                }
                return; // Terminamos aquí para no ejecutar la lógica de guardar/editar ejercicio
            }
            
            String titulo = request.getParameter("titulo");
            String nivel = request.getParameter("nivel");
            String contenido = request.getParameter("contenido"); 

            Ejercicio e = new Ejercicio();
            e.setTitulo(titulo);
            e.setNivel(nivel);
            e.setContenidoJson(contenido);

            boolean exito;
            String idStr = request.getParameter("id");

            if (idStr != null && !idStr.isEmpty()) {
                e.setId(Integer.parseInt(idStr));
                exito = dao.actualizar(e);
            } else {
                exito = dao.guardar(e);
            }

            try (PrintWriter out = response.getWriter()) {
                out.print(exito ? "{\"status\":\"success\"}" : "{\"status\":\"error\"}");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    // 3. ELIMINAR PREGUNTA
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setAccessControlHeaders(response);
        response.setContentType("application/json;charset=UTF-8");
        String idStr = request.getParameter("id");
        
        boolean exito = false;
        if(idStr != null) exito = dao.eliminar(Integer.parseInt(idStr));
        
        try (PrintWriter out = response.getWriter()) {
             out.print(exito ? "{\"status\":\"success\"}" : "{\"status\":\"error\"}");
        }
    }
}