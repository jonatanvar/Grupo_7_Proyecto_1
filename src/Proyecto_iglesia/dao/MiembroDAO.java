package Proyecto_iglesia.dao;

import Proyecto_iglesia.modelo.Miembro;
import Proyecto_iglesia.persistencia.ConexionSQLite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de Acceso a Datos (DAO) para la entidad Miembro.
 * Implementa las operaciones CRUD definidas en la interfaz genérica.
 * Utiliza PreparedStatement para seguridad y eficiencia.
 */
public class MiembroDAO implements ICRUD<Miembro> {

    private final Connection conexion;

    public MiembroDAO() {
        this.conexion = ConexionSQLite.getInstancia().conectar();
    }

    /**
     * Inserta un nuevo miembro en la base de datos.
     * @param miembro Objeto con la información a guardar.
     * @return true si se guardó correctamente, false si hubo error.
     */
    @Override
    public boolean crear(Miembro miembro) {
        String sql = "INSERT INTO miembro (nombre, apellido, telefono, correo, rol, fechaIngreso) VALUES (?, ?, ?, ?, ?, date('now'))";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, miembro.getNombre());
            stmt.setString(2, miembro.getApellido());
            stmt.setString(3, miembro.getTelefono()); // Corregido: Usa el teléfono real
            stmt.setString(4, miembro.getCorreo());
            stmt.setString(5, miembro.getRol());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al crear miembro: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca un miembro por su ID primario.
     * @param id Identificador único del miembro.
     * @return Objeto Miembro si existe, null si no.
     */
    @Override
    public Miembro leer(int id) {
        String sql = "SELECT * FROM miembro WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearMiembro(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al leer miembro: " + e.getMessage());
        }
        return null;
    }

    /**
     * Actualiza la información de un miembro existente.
     * @param miembro Objeto con los datos nuevos (debe tener ID).
     * @return true si se actualizó, false si falló.
     */
    @Override
    public boolean actualizar(Miembro miembro) {
        // Corregido: Se agregó el campo 'telefono' a la actualización
        String sql = "UPDATE miembro SET nombre=?, apellido=?, telefono=?, correo=?, rol=? WHERE id=?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, miembro.getNombre());
            stmt.setString(2, miembro.getApellido());
            stmt.setString(3, miembro.getTelefono());
            stmt.setString(4, miembro.getCorreo());
            stmt.setString(5, miembro.getRol());
            stmt.setInt(6, miembro.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar miembro: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina (físicamente) un miembro de la base de datos.
     * @param id Identificador del miembro a borrar.
     * @return true si se eliminó.
     */
    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM miembro WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar miembro: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene el listado completo de miembros.
     * @return Lista de objetos Miembro.
     */
    @Override
    public List<Miembro> listarTodos() {
        List<Miembro> lista = new ArrayList<>();
        String sql = "SELECT * FROM miembro";
        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearMiembro(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar miembros: " + e.getMessage());
        }
        return lista;
    }

    // Método auxiliar para no repetir código de mapeo
    private Miembro mapearMiembro(ResultSet rs) throws SQLException {
        // Corregido: Ahora coincide con el constructor de Miembro (6 parámetros)
        // incluyendo el teléfono que antes faltaba.
        return new Miembro(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("telefono"), // <-- Campo nuevo agregado
                rs.getString("correo"),
                rs.getString("rol")
        );
    }
}