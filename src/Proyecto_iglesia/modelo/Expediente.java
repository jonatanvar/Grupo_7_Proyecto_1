package Proyecto_iglesia.modelo;

public class Expediente extends Grupo {
    private int idMiembro; // FK única
    private String notas;
    private String historialMinisterial;
    private String observaciones;

    public Expediente() {}

    public Expediente(int id, int idMiembro, String notas, String historial, String observaciones) {
        this.id = id;
        this.idMiembro = idMiembro;
        this.notas = notas;
        this.historialMinisterial = historial;
        this.observaciones = observaciones;
    }

    @Override
    public boolean validar() {
        return idMiembro > 0;
    }

    @Override
    public String obtenerNombreTabla() {
        return "expediente";
    }

    // Getters y Setters
    public int getIdMiembro() { return idMiembro; }
    public void setIdMiembro(int idMiembro) { this.idMiembro = idMiembro; }
    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
    public String getHistorialMinisterial() { return historialMinisterial; }
    public void setHistorialMinisterial(String historialMinisterial) { this.historialMinisterial = historialMinisterial; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}