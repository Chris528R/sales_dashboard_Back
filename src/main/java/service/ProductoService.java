package service;

import dao.ProductoDAO;
import model.Producto;
import java.util.List;

public class ProductoService {

    private final ProductoDAO productoDAO = new ProductoDAO();

    public List<Producto> listar() {
        return productoDAO.listar();
    }

    public boolean registrar(Producto p) {
        return productoDAO.registrar(p);
    }

    public boolean actualizar(Producto p) {
        return productoDAO.actualizar(p);
    }

    public boolean eliminar(int id) {
        return productoDAO.eliminar(id);
    }
}
