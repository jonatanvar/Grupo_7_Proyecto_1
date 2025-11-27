package Proyecto_iglesia.modelo;

public abstract class EntidadBase {
    protected int id;

    // Métodos abstractos que TODAS las hijas deben tener
    public abstract boolean validar();
    public abstract String obtenerNombreTabla();

    // Getters y Setters del ID
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
}