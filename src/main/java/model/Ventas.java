package model;
import java.sql.Timestamp;
import java.util.List;

public class Ventas {
    private int id;
    private Timestamp fechaVenta;
    private double total;
    private List<DetalleVenta> detalles; 

    public Ventas(){}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Timestamp getFechaVenta() { return fechaVenta; }
    public void setFechaVenta(Timestamp fechaVenta) { this.fechaVenta = fechaVenta; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public List<DetalleVenta> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleVenta> detalles) { this.detalles = detalles; }
}