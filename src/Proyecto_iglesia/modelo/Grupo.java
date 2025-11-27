package Proyecto_iglesia.modelo;

/**
 * Representa un grupo de crecimiento dentro de la iglesia.
 * Extiende de EntidadBase para heredar ID y métodos obligatorios.
 */
public class Grupo extends EntidadBase { // <--- YA NO ES ABSTRACT

    private String nombre;
    private String descripcion;
    private int idLider;   // FK
    private int idIglesia; // FK

    public Grupo() {
        super();
    }

    public Grupo(int id, String nombre, String descripcion, int idLider, int idIglesia) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.idLider = idLider;
        this.idIglesia = idIglesia;
    }

    // =========================================================
    // MÉTODOS OBLIGATORIOS (Que faltaban)
    // =========================================================

    @Override
    public boolean validar() {
        // Un grupo debe tener nombre obligatoriamente
        return nombre != null && !nombre.isEmpty();
    }

    @Override
    public String obtenerNombreTabla() {
        // Nombre exacto de la tabla en SQLite
        return "grupo";
    }

    // =========================================================
    // GETTERS Y SETTERS
    // =========================================================

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getIdLider() { return idLider; }
    public void setIdLider(int idLider) { this.idLider = idLider; }

    public int getIdIglesia() { return idIglesia; }
    public void setIdIglesia(int idIglesia) { this.idIglesia = idIglesia; }

    @Override
    public String toString() {
        return nombre;
    }
}