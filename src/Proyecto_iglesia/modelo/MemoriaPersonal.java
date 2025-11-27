package Proyecto_iglesia.modelo;

public class MemoriaPersonal extends EntidadBase {
    private int idReunion;
    private int idMiembro;
    private String contenido;

    public MemoriaPersonal() {}

    public MemoriaPersonal(int id, int idReunion, int idMiembro, String contenido) {
        this.id = id;
        this.idReunion = idReunion;
        this.idMiembro = idMiembro;
        this.contenido = contenido;
    }

    @Override
    public boolean validar() {
        return contenido != null && !contenido.isEmpty();
    }

    @Override
    public String obtenerNombreTabla() {
        return "memoria_personal";
    }

    // Getters y Setters
    public int getIdReunion() { return idReunion; }
    public void setIdReunion(int idReunion) { this.idReunion = idReunion; }
    public int getIdMiembro() { return idMiembro; }
    public void setIdMiembro(int idMiembro) { this.idMiembro = idMiembro; }
    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
}