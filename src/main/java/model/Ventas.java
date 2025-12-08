package model;

import java.util.Date;

public class Ventas {
    private int id;
    private Date fecha_venta;
    private double total;
    
    public Ventas(){}
    
    public void setId(int id){ this.id = id; }
    public void setFecha_venta(Date fecha_venta){ this.fecha_venta = fecha_venta; }
    public void setTotal(double total){ this.total = total; }
    public int getId(){ return id; }
    public Date getFecha_venta(){ return fecha_venta; }
    public double getTotal(){ return total; }
    
    
}
