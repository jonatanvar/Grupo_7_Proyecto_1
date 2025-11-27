package Proyecto_iglesia.dao;

import Proyecto_iglesia.modelo.Asistencia;
import Proyecto_iglesia.persistencia.ConexionSQLite;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AsistenciaDAO implements ICRUD<Asistencia> {
    private Connection conexion;

    public AsistenciaDAO() {
        this.conexion = ConexionSQLite.getInstancia().conectar();
    }

    @Override
    public boolean crear(Asistencia a) {
        String sql = "INSERT INTO asistencia (idReunion, idMiembro, asistio, excusa, fechaRegistro) VALUES (?, ?, ?, ?, date('now'))";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, a.getIdReunion());
            stmt.setInt(2, a.getIdMiembro());
            stmt.setInt(3, a.isAsistio() ? 1 : 0);
            stmt.setString(4, a.getExcusa());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error registrando asistencia: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Asistencia> listarTodos() {
        // Por lo general, la asistencia se lista POR REUNIÓN, no todas a lo loco.
        // Pero cumplimos con la interfaz.
        return new ArrayList<>();
    }

    /**
     * Método EXTRA específico de Asistencia (No está en ICRUD genérico)
     * Obtiene la lista de asistencia de una reunión específica.
     */
    public List<Asistencia> listarPorReunion(int idReunion) {
        List<Asistencia> lista = new ArrayList<>();
        String sql = "SELECT * FROM asistencia WHERE idReunion = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idReunion);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Asistencia a = new Asistencia();
                a.setId(rs.getInt("id"));
                a.setIdReunion(rs.getInt("idReunion"));
                a.setIdMiembro(rs.getInt("idMiembro"));
                a.setAsistio(rs.getInt("asistio") == 1);
                a.setExcusa(rs.getString("excusa"));
                lista.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override public Asistencia leer(int id) { return null; }
    @Override public boolean actualizar(Asistencia t) { return false; }
    @Override public boolean eliminar(int id) { return false; }
}