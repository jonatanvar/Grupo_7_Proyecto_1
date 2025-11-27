package Proyecto_iglesia.modelo;

public class Reunion extends EntidadBase {
    private int idGrupo;
    private String fecha; // Formato: "YYYY-MM-DD"
    private String tema;
    private String descripcion;
    private String responsable;
    private boolean finalizada;
    private String minuta;

    public Reunion() {}

    public Reunion(int id, int idGrupo, String fecha, String tema, String responsable) {
        this.id = id;
        this.idGrupo = idGrupo;
        this.fecha = fecha;
        this.tema = tema;
        this.responsable = responsable;
        this.finalizada = false; // Por defecto empieza abierta
    }

    // Lógica de Negocio requerida en el diagrama
    public void finalizarReunion(String textoMinuta) {
        this.minuta = textoMinuta;
        this.finalizada = true;
    }

    @Override
    public boolean validar() {
        // Regla: Debe tener tema, fecha y pertenecer a un grupo
        return tema != null && !tema.isEmpty() && idGrupo > 0 && fecha != null;
    }

    @Override
    public String obtenerNombreTabla() {
        return "reunion";
    }

    // Getters y Setters
    public int getIdGrupo() { return idGrupo; }
    public void setIdGrupo(int idGrupo) { this.idGrupo = idGrupo; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getTema() { return tema; }
    public void setTema(String tema) { this.tema = tema; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getResponsable() { return responsable; }
    public void setResponsable(String responsable) { this.responsable = responsable; }
    public boolean isFinalizada() { return finalizada; }
    public void setFinalizada(boolean finalizada) { this.finalizada = finalizada; }
    public String getMinuta() { return minuta; }
    public void setMinuta(String minuta) { this.minuta = minuta; }

    @Override
    public String toString() {
        return fecha + " - " + tema;
    }
}