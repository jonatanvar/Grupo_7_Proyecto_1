package Proyecto_iglesia.modelo;

public class Asistencia extends EntidadBase {
    private int idReunion;
    private int idMiembro;
    private boolean asistio;
    private String excusa;

    public Asistencia() {}

    public Asistencia(int id, int idReunion, int idMiembro, boolean asistio) {
        this.id = id;
        this.idReunion = idReunion;
        this.idMiembro = idMiembro;
        this.asistio = asistio;
    }

    @Override
    public boolean validar() {
        return idReunion > 0 && idMiembro > 0;
    }

    @Override
    public String obtenerNombreTabla() {
        return "asistencia";
    }

    // Getters y Setters
    public int getIdReunion() { return idReunion; }
    public void setIdReunion(int idReunion) { this.idReunion = idReunion; }
    public int getIdMiembro() { return idMiembro; }
    public void setIdMiembro(int idMiembro) { this.idMiembro = idMiembro; }
    public boolean isAsistio() { return asistio; }
    public void setAsistio(boolean asistio) { this.asistio = asistio; }
    public String getExcusa() { return excusa; }
    public void setExcusa(String excusa) { this.excusa = excusa; }
}