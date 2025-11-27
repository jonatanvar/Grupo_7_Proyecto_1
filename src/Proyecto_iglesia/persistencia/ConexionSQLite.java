package Proyecto_iglesia.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionSQLite {

    private static ConexionSQLite instancia;
    private Connection conexion;

    //  ESTA ES LA LÍNEA QUE TIENES QUE ARREGLAR:
    // Debe decir solo "iglesia.sqlite" (sin src, ni carpetas)
    private static final String URL = "jdbc:sqlite:iglesia.sqlite";

    private ConexionSQLite() { }

    public static ConexionSQLite getInstancia() {
        if (instancia == null) {
            instancia = new ConexionSQLite();
        }
        return instancia;
    }

    public Connection conectar() {
        try {
            if (conexion == null || conexion.isClosed()) {
                // Esta línea carga el Driver (la librería que descargaste)
                Class.forName("org.sqlite.JDBC");
                conexion = DriverManager.getConnection(URL);
                System.out.println(" Conexión a SQLite establecida correctamente.");
            }
        } catch (ClassNotFoundException e) {
            System.err.println(" ERROR: No se encontró el Driver de SQLite.");
            System.err.println("Asegúrate de agregar la librería 'org.xerial:sqlite-jdbc' en Project Structure.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println(" ERROR: No se encontró el archivo de Base de Datos.");
            System.err.println("Ruta buscada: " + URL);
            e.printStackTrace();
        }
        return conexion;
    }

    public void desconectar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}