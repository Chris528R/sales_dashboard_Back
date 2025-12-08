
package model;


public class DetalleVenta {
    private int id;
    private int idVenta;
    private int idProducto;
    private int cantidad;
    private double precioUnitario;

    public DetalleVenta() {}

    public int getId(){return id; }
    public int getIdVenta(){return idVenta; }
    public int getIdProducto(){return idProducto; }
    public int getCantidad(){return cantidad; }
    public double getPrecio(){return precioUnitario; }


    public void setId(int id){this.id = id; }
    public void setIdVenta(int idVenta){this.idVenta =  idVenta; }
    public void setIdProducto(int idProducto){this.idProducto = idProducto; }
    public void setCantidad(int cantidad){this.cantidad =  cantidad; }
    public void setPrecio(double precioUnitario){this.precioUnitario =  precioUnitario; }
}