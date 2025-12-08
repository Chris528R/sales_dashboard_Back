package model;

import java.util.Date;

public class Producto {
    private int id;
    private String nombre;
    private String descripcion;
    private String tipo;
    private String categoria;
    private double precio;
    private int stock;
    private String unidad_medida;
    private Date fecha_registro;
    
    public Producto() {}
    
    public int getId(){ return id; }
    public String getNombre(){ return nombre; }
    public String getDescripcion(){ return descripcion; }
    public String getTipo(){ return tipo; }
    public String getCategoria(){ return categoria; }
    public double getPrecio(){ return precio; }
    public int getStock(){ return stock; }
    public String getUnidad(){ return unidad_medida; }
    public Date getFecha(){ return fecha_registro; }
    
    public void setId( int id){ this.id = id; }
    public void setNombre( String nombre ){ this.nombre = nombre; }
    public void setDescripcion(String descripcion){ this.descripcion = descripcion; }
    public void setTipo(String tipo){ this.tipo = tipo; }
    public void setCategoria(String categoria){ this.categoria = categoria; }
    public void setPrecio(double precio){ this.precio = precio; }
    public void setStock(int stock){ this.stock = stock; }
    public void setUnidad(String unidad_medida){ this.unidad_medida = unidad_medida; }
    public void setFecha(Date fecha_registro){ this.fecha_registro = fecha_registro; }
    
}
