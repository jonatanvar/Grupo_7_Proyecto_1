package Proyecto_iglesia.persistencia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Clase encargada de la seguridad y control de acceso.
 * Lee las credenciales desde un archivo de texto plano para no tener datos sensibles en código.
 * Cumple con el requisito de persistencia en archivos.
 */
public class GestorAutenticacion {

    // Nombre del archivo físico. Debe estar en la raíz del proyecto (junto a src)
    private static final String ARCHIVO_USUARIOS = "usuarios.txt";

    /**
     * Verifica si el usuario y contraseña existen en el archivo de texto.
     * Realiza limpieza de espacios en blanco para evitar errores de lectura.
     *
     * @param usuario User ingresado por el usuario.
     * @param password Clave ingresada.
     * @return El ROL del usuario si es válido (ej: "LIDER_IGLESIA"), o null si falló.
     */
    public String autenticar(String usuario, String password) {
        // Estructura try-with-resources para cerrar el archivo automáticamente
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_USUARIOS))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                // El formato del archivo es: usuario,password,rol
                String[] partes = linea.split(",");

                // Verificamos que la línea tenga las 3 partes para no dar error
                if (partes.length == 3) {
                    // .trim() elimina espacios invisibles al inicio o final
                    String u = partes[0].trim();
                    String p = partes[1].trim();
                    String r = partes[2].trim();

                    // Comparamos (ignorando mayúsculas en usuario, exacto en clave)
                    if (u.equalsIgnoreCase(usuario) && p.equals(password)) {
                        return r; // ¡Login Exitoso! Devolvemos el Rol
                    }
                }
            }
        } catch (IOException e) {
            System.err.println(" Error CRÍTICO leyendo el archivo de usuarios.");
            System.err.println("Mensaje: " + e.getMessage());
            // Esto imprime dónde está buscando Java el archivo actualmente
            System.err.println("Java está buscando en: " + System.getProperty("user.dir"));
        }
        return null; // Login Fallido

    }
    /**
     * Método NUEVO para registrar usuarios en el archivo de texto.
     * Se usa cuando el Pastor crea un nuevo líder en el sistema.
     */
    public boolean registrarUsuario(String usuario, String password, String rol) {
        // Usamos FileWriter con 'true' para agregar al final sin borrar lo que ya existe
        try (java.io.FileWriter fw = new java.io.FileWriter(ARCHIVO_USUARIOS, true);
             java.io.BufferedWriter bw = new java.io.BufferedWriter(fw);
             java.io.PrintWriter out = new java.io.PrintWriter(bw)) {

            // Escribimos en el formato: usuario,password,rol
            out.println(usuario + "," + password + "," + rol);
            return true;

        } catch (IOException e) {
            System.err.println("Error escribiendo usuario: " + e.getMessage());
            return false;
        }
    }
}