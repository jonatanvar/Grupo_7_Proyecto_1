package Proyecto_iglesia.dao;

import Proyecto_iglesia.modelo.Iglesia;
import Proyecto_iglesia.persistencia.ConexionSQLite;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IglesiaDAO implements ICRUD<Iglesia> {
    private Connection conexion;

    public IglesiaDAO() {
        this.conexion = ConexionSQLite.getInstancia().conectar();
    }

    @Override
    public Iglesia leer(int id) {
        String sql = "SELECT * FROM iglesia WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Iglesia(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("pastorPrincipal")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean actualizar(Iglesia iglesia) {
        String sql = "UPDATE iglesia SET nombre=?, direccion=?, telefono=?, email=?, pastorPrincipal=? WHERE id=?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, iglesia.getNombre());
            stmt.setString(2, iglesia.getDireccion());
            stmt.setString(3, iglesia.getTelefono());
            stmt.setString(4, iglesia.getEmail());
            stmt.setString(5, iglesia.getPastorPrincipal());
            stmt.setInt(6, iglesia.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Implementaciones vacías o básicas para cumplir contrato
    @Override public boolean crear(Iglesia i) { return false; } // Normalmente ya existe 1
    @Override public boolean eliminar(int id) { return false; }
    @Override public List<Iglesia> listarTodos() { return new ArrayList<>(); }
}