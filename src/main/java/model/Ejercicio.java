package model;

public class Ejercicio {
    private int id;
    private String titulo;
    private String nivel;
    private String contenidoJson;
    private int mejorTiempo;

    public Ejercicio() {}

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getMejorTiempo() { return mejorTiempo; }
    public void setMejorTiempo(int mejorTiempo) { this.mejorTiempo = mejorTiempo; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }
    public String getContenidoJson() { return contenidoJson; }
    public void setContenidoJson(String contenidoJson) { this.contenidoJson = contenidoJson; }
}