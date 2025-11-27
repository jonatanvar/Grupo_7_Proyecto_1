package Proyecto_iglesia.servicio;

import Proyecto_iglesia.persistencia.ConexionSQLite;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Estadisticas {

    private Connection conexion;

    public Estadisticas() {
        this.conexion = ConexionSQLite.getInstancia().conectar();
    }

    // Método genérico para contar cosas (ahorra código)
    private int contar(String tabla) {
        String sql = "SELECT COUNT(*) FROM " + tabla;
        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTotalMiembros() { return contar("miembro"); }
    public int getTotalGrupos() { return contar("grupo"); }
    public int getTotalReuniones() { return contar("reunion"); }

    // Estadística compleja: Asistencia promedio global
    public double getPorcentajeAsistenciaGlobal() {
        // Total de registros de asistencia marcados como 'PRESENTE' (1)
        int presentes = 0;
        int totalRegistros = 0;

        try {
            String sql = "SELECT COUNT(*) FROM asistencia";
            String sqlPresentes = "SELECT COUNT(*) FROM asistencia WHERE asistio = 1";

            PreparedStatement s1 = conexion.prepareStatement(sql);
            ResultSet r1 = s1.executeQuery();
            if(r1.next()) totalRegistros = r1.getInt(1);

            PreparedStatement s2 = conexion.prepareStatement(sqlPresentes);
            ResultSet r2 = s2.executeQuery();
            if(r2.next()) presentes = r2.getInt(1);

            if (totalRegistros == 0) return 0.0;
            return ((double) presentes / totalRegistros) * 100;

        } catch (Exception e) {
            return 0.0;
        }
    }
}