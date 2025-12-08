
package model;

public class Usuario {
    private int id;
    private String nombre;
    private String contrasenia;

    public Usuario() {}

    public Usuario(int id, String nombre, String contrasenia) {
        this.id = id;
        this.nombre = nombre;
        this.contrasenia = contrasenia;
    }
    
    public void setId(int id){ this.id = id; }
    public void setNombre(String nombre){ this.nombre = nombre; }
    public void setContrasenia(String contrasenia){ this.contrasenia = contrasenia; }
    public int getId(){ return id; }
    public String getNombre(){ return nombre; }
    public String getContrasenia(){ return contrasenia; }
  
    
}