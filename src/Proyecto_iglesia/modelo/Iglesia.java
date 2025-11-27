package Proyecto_iglesia.modelo;

public class Iglesia extends Miembro {
    private String nombre;
    private String direccion;
    private String telefono;
    private String email;
    private String pastorPrincipal;

    public Iglesia() {}

    public Iglesia(int id, String nombre, String direccion, String telefono, String email, String pastorPrincipal) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.pastorPrincipal = pastorPrincipal;
    }

    @Override
    public boolean validar() {
        return nombre != null && !nombre.isEmpty();
    }

    @Override
    public String obtenerNombreTabla() {
        return "iglesia";
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPastorPrincipal() { return pastorPrincipal; }
    public void setPastorPrincipal(String pastorPrincipal) { this.pastorPrincipal = pastorPrincipal; }

    @Override
    public String toString() {
        return nombre;
    }
}