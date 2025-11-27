package Proyecto_iglesia.dao;

import Proyecto_iglesia.modelo.Expediente;
import Proyecto_iglesia.persistencia.ConexionSQLite;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpedienteDAO implements ICRUD<Expediente> {
    private Connection conexion;

    public ExpedienteDAO() {
        this.conexion = ConexionSQLite.getInstancia().conectar();
    }

    @Override
    public boolean crear(Expediente e) {
        String sql = "INSERT INTO expediente (idMiembro, notas, historialMinisterial, observaciones, fechaCreacion) VALUES (?, ?, ?, ?, date('now'))";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, e.getIdMiembro());
            stmt.setString(2, e.getNotas());
            stmt.setString(3, e.getHistorialMinisterial());
            stmt.setString(4, e.getObservaciones());
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Método Especial: Buscar expediente de un miembro específico
    public Expediente leerPorMiembro(int idMiembro) {
        String sql = "SELECT * FROM expediente WHERE idMiembro = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idMiembro);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Expediente(
                        rs.getInt("id"),
                        rs.getInt("idMiembro"),
                        rs.getString("notas"),
                        rs.getString("historialMinisterial"),
                        rs.getString("observaciones")
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null; // No tiene expediente aun
    }

    // Métodos estándar (vacíos por ahora para cumplir contrato)
    @Override public Expediente leer(int id) { return null; }
    @Override public boolean actualizar(Expediente e) { return false; }
    @Override public boolean eliminar(int id) { return false; }
    @Override public List<Expediente> listarTodos() { return new ArrayList<>(); }
}