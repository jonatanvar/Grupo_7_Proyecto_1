package Proyecto_iglesia.dao;

import Proyecto_iglesia.modelo.Grupo;
import Proyecto_iglesia.persistencia.ConexionSQLite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar la tabla 'grupo'.
 */
public class GrupoDAO implements ICRUD<Grupo> {

    private final Connection conexion;

    public GrupoDAO() {
        this.conexion = ConexionSQLite.getInstancia().conectar();
    }

    @Override
    public boolean crear(Grupo grupo) {
        String sql = "INSERT INTO grupo (nombre, descripcion, idLider, idIglesia, fechaCreacion) VALUES (?, ?, ?, ?, date('now'))";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, grupo.getNombre());
            stmt.setString(2, grupo.getDescripcion());
            stmt.setInt(3, grupo.getIdLider());
            stmt.setInt(4, grupo.getIdIglesia()); // Asumiendo que siempre es iglesia 1
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error creando grupo: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Grupo leer(int id) {
        // Implementación similar a MiembroDAO si se requiere
        return null;
    }

    @Override
    public boolean actualizar(Grupo grupo) {
        return false; // Implementar si es necesario
    }

    @Override
    public boolean eliminar(int id) {
        return false; // Implementar si es necesario
    }

    @Override
    public List<Grupo> listarTodos() {
        List<Grupo> lista = new ArrayList<>();
        String sql = "SELECT * FROM grupo";
        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
        } catch (SQLException e) {
            System.err.println("Error listando grupos: " + e.getMessage());
        }
        return lista;
    }
}