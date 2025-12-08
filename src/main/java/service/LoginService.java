package service;

import dao.UsuarioDAO;
import model.Usuario;

public class LoginService {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    
    public Usuario login(String nombre, String cont){
        return usuarioDAO.login(nombre, cont);
    }
}
