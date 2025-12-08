
package service;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Usuario;
import service.LoginService;

@WebServlet(name = "Login", urlPatterns = {"/api/login"})
public class Login extends HttpServlet {

    private final LoginService loginService = new LoginService();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String cont = request.getParameter("cont");
        Usuario user = loginService.login(nombre, cont);
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        if(user != null){
            out.println("{\"status\":\"yes\""+"\"}");
        } else {
            out.println("{\"status\":\"no\"}");            
        }
    }
}
