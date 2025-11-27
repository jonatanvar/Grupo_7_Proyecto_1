package Proyecto_iglesia.modelo;

public class Miembro extends EntidadBase {
    private String nombre;
    private String apellido;
    private String telefono; // <--- ¡ESTE FALTABA!
    private String correo;
    private String rol;
    private int idGrupo; // FK

    public Miembro() { super(); }

    public Miembro(int id, String nombre, String apellido, String telefono, String correo, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.correo = correo;
        this.rol = rol;
    }

    @Override
    public boolean validar() {
        return nombre != null && !nombre.isEmpty() && apellido != null;
    }

    @Override
    public String obtenerNombreTabla() {
        return "miembro";
    }

    // --- GETTERS Y SETTERS ---

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }


    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public int getIdGrupo() { return idGrupo; }
    public void setIdGrupo(int idGrupo) { this.idGrupo = idGrupo; }

    @Override
    public String toString() {
        return nombre + " " + apellido;
    }
}