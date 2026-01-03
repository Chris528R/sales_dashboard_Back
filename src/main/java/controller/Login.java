
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Usuario;
import service.LoginService;
import service.LoginService;

@WebServlet(name = "Login", urlPatterns = {"/api/login"})
public class Login extends HttpServlet {

    private final LoginService loginService = new LoginService();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 1. Configurar Headers
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json"); 

        // 2. Obtener parámetros
        String nombre = request.getParameter("nombre");
        String cont = request.getParameter("cont");

        Usuario user = loginService.login(nombre, cont);

        try (PrintWriter out = response.getWriter()) {
            if(user != null){
                
                out.print("{\"status\":\"success\"}");
            } else {
                out.print("{\"status\":\"no\"}");            
            }
            out.flush();
        }
    }
}
