package Proyecto_iglesia.dao;

import Proyecto_iglesia.modelo.MemoriaPersonal;
import Proyecto_iglesia.persistencia.ConexionSQLite;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemoriaPersonalDAO implements ICRUD<MemoriaPersonal> {
    private Connection conexion;

    public MemoriaPersonalDAO() {
        this.conexion = ConexionSQLite.getInstancia().conectar();
    }

    @Override
    public boolean crear(MemoriaPersonal m) {
        String sql = "INSERT INTO memoria_personal (idReunion, idMiembro, contenido, fechaCreacion) VALUES (?, ?, ?, date('now'))";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, m.getIdReunion());
            stmt.setInt(2, m.getIdMiembro());
            stmt.setString(3, m.getContenido());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<MemoriaPersonal> listarTodos() {
        // Generalmente listarás por miembro, no todas
        return new ArrayList<>();
    }

    // Método específico útil
    public List<MemoriaPersonal> listarPorMiembro(int idMiembro) {
        List<MemoriaPersonal> lista = new ArrayList<>();
        String sql = "SELECT * FROM memoria_personal WHERE idMiembro = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idMiembro);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                MemoriaPersonal m = new MemoriaPersonal();
                m.setId(rs.getInt("id"));
                m.setIdReunion(rs.getInt("idReunion"));
                m.setIdMiembro(rs.getInt("idMiembro"));
                m.setContenido(rs.getString("contenido"));
                lista.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override public MemoriaPersonal leer(int id) { return null; }
    @Override public boolean actualizar(MemoriaPersonal t) { return false; }
    @Override public boolean eliminar(int id) { return false; }
}