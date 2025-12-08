package service;

import dao.ProductoDAO;
import java.util.List;
import model.Producto;
public class ProductoService {
    private final ProductoDAO dao = new ProductoDAO();
    
    public List<Producto> listarProductos(){
        return dao.listar();
    }
}
