package Proyecto_iglesia.dao;

import Proyecto_iglesia.modelo.Reunion;
import Proyecto_iglesia.persistencia.ConexionSQLite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar las Reuniones en la base de datos.
 * Implementa la lógica CRUD completa requerida para agendar y finalizar reuniones.
 */
public class ReunionDAO implements ICRUD<Reunion> {

    private final Connection conexion;

    public ReunionDAO() {
        this.conexion = ConexionSQLite.getInstancia().conectar();
    }

    /**
     * Registra una nueva reunión en el sistema.
     * Estado inicial por defecto: ABIERTA (finalizada = 0).
     */
    @Override
    public boolean crear(Reunion r) {
        String sql = "INSERT INTO reunion (idGrupo, fecha, tema, responsable, finalizada, minuta) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, r.getIdGrupo());
            stmt.setString(2, r.getFecha());
            stmt.setString(3, r.getTema());
            stmt.setString(4, r.getResponsable());
            stmt.setInt(5, r.isFinalizada() ? 1 : 0); // SQLite usa 1/0 para boolean
            stmt.setString(6, r.getMinuta() != null ? r.getMinuta() : "");
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error creando reunión: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca una reunión por su ID.
     * VITAL para el proceso de "Tomar Asistencia" y "Finalizar".
     */
    @Override
    public Reunion leer(int id) {
        String sql = "SELECT * FROM reunion WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearReunion(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error leyendo reunión: " + e.getMessage());
        }
        return null; // Si retorna null, el sistema dirá "No se pudo cargar"
    }

    /**
     * Actualiza los datos de una reunión.
     * Se usa principalmente para FINALIZAR la reunión (guardar minuta y cambiar estado).
     */
    @Override
    public boolean actualizar(Reunion r) {
        String sql = "UPDATE reunion SET idGrupo=?, fecha=?, tema=?, responsable=?, finalizada=?, minuta=? WHERE id=?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, r.getIdGrupo());
            stmt.setString(2, r.getFecha());
            stmt.setString(3, r.getTema());
            stmt.setString(4, r.getResponsable());
            stmt.setInt(5, r.isFinalizada() ? 1 : 0);
            stmt.setString(6, r.getMinuta());
            stmt.setInt(7, r.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error actualizando reunión: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM reunion WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error eliminando reunión: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Reunion> listarTodos() {
        List<Reunion> lista = new ArrayList<>();
        String sql = "SELECT * FROM reunion ORDER BY fecha DESC"; // Las más recientes primero
        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearReunion(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listando reuniones: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Método auxiliar para convertir un registro SQL a un objeto Java.
     */
    private Reunion mapearReunion(ResultSet rs) throws SQLException {
        Reunion r = new Reunion();
        r.setId(rs.getInt("id"));
        r.setIdGrupo(rs.getInt("idGrupo"));
        r.setFecha(rs.getString("fecha"));
        r.setTema(rs.getString("tema"));
        r.setResponsable(rs.getString("responsable"));

        // Convertir 1/0 a true/false
        r.setFinalizada(rs.getInt("finalizada") == 1);

        // Manejo de nulos para la minuta
        String minuta = rs.getString("minuta");
        r.setMinuta(minuta != null ? minuta : "");

        return r;
    }
}