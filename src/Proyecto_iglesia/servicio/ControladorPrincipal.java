package Proyecto_iglesia.servicio;

import Proyecto_iglesia.dao.GrupoDAO;
import Proyecto_iglesia.dao.MiembroDAO;
import Proyecto_iglesia.dao.ReunionDAO;
import Proyecto_iglesia.modelo.Grupo;
import Proyecto_iglesia.modelo.Miembro;
import Proyecto_iglesia.modelo.Reunion;
import Proyecto_iglesia.util.Validador;
import Proyecto_iglesia.vista.PantallaLogin;

import javax.swing.*;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class ControladorPrincipal {

    private static ControladorPrincipal instancia;
    private MiembroDAO miembroDAO;
    private GrupoDAO grupoDAO;

    // Variable para los idiomas (puede ser null si falla)
    private ResourceBundle bundleIdiomas;

    private ControladorPrincipal() {
        this.miembroDAO = new MiembroDAO();
        this.grupoDAO = new GrupoDAO();
    }

    public static ControladorPrincipal getInstancia() {
        if (instancia == null) {
            instancia = new ControladorPrincipal();
        }
        return instancia;
    }

    public void iniciarSistema() {
        SwingUtilities.invokeLater(() -> {
            PantallaLogin login = new PantallaLogin();
            login.setVisible(true);
        });
    }

    // =========================================================
    // GESTIÓN DE IDIOMAS (BLINDADA)
    // =========================================================

    public void configurarIdioma(String codigoIdioma) {
        try {
            Locale locale = new Locale(codigoIdioma);
            // Intenta cargar "textos_es.properties" o "textos_en.properties"
            this.bundleIdiomas = ResourceBundle.getBundle("textos", locale);
        } catch (Exception e) {
            // SI FALLA, NO HACEMOS NADA.
            // Solo imprimimos el error en consola, pero NO lanzamos la excepción al Main.
            System.err.println("⚠️ ADVERTENCIA: No se encontraron los archivos de idioma en la carpeta 'src'.");
            System.err.println("El sistema iniciará mostrando las claves de texto.");
            this.bundleIdiomas = null; // Se queda nulo, pero el programa sigue.
        }
    }

    public String getTexto(String clave) {
        // Si el archivo no cargó, devolvemos la clave para que el botón tenga texto al menos
        if (bundleIdiomas == null) {
            return "[" + clave + "]";
        }
        try {
            return bundleIdiomas.getString(clave);
        } catch (Exception e) {
            return "?" + clave + "?";
        }
    }

    // =========================================================
    // LÓGICA DE NEGOCIO (MVC)
    // =========================================================

    public boolean registrarMiembro(String nombre, String apellido, String telefono, String correo, String rol) {
        if (Validador.esTextoVacio(nombre) || Validador.esTextoVacio(apellido)) {
            JOptionPane.showMessageDialog(null, "Nombre y Apellido son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (!Validador.esCorreoValido(correo)) {
            JOptionPane.showMessageDialog(null, "El correo no tiene un formato válido.", "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        Miembro nuevo = new Miembro();
        nuevo.setNombre(nombre);
        nuevo.setApellido(apellido);
        nuevo.setTelefono(telefono);
        nuevo.setCorreo(correo);
        nuevo.setRol(rol);

        if (miembroDAO.crear(nuevo)) {
            JOptionPane.showMessageDialog(null, "✅ Miembro registrado con éxito.");

            // Lógica de creación de usuario
            if (!rol.equalsIgnoreCase("MIEMBRO")) {
                int respuesta = JOptionPane.showConfirmDialog(null,
                        "El rol asignado (" + rol + ") permite acceso al sistema.\n¿Desea crear usuario y contraseña?",
                        "Crear Credenciales", JOptionPane.YES_NO_OPTION);

                if (respuesta == JOptionPane.YES_OPTION) {
                    String user = JOptionPane.showInputDialog(null, "Ingrese Usuario:");
                    String pass = JOptionPane.showInputDialog(null, "Ingrese Contraseña:");

                    if (user != null && pass != null) {
                        Proyecto_iglesia.persistencia.GestorAutenticacion auth = new Proyecto_iglesia.persistencia.GestorAutenticacion();
                        if (auth.registrarUsuario(user, pass, rol)) {
                            JOptionPane.showMessageDialog(null, "Usuario creado.");
                        }
                    }
                }
            }
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Error de base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean registrarGrupo(String nombre, String descripcion, String seleccionComboLider) {
        if (Validador.esTextoVacio(nombre)) {
            JOptionPane.showMessageDialog(null, "Nombre obligatorio.", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (seleccionComboLider == null) {
            JOptionPane.showMessageDialog(null, "Seleccione un líder.", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            Grupo nuevo = new Grupo();
            nuevo.setNombre(nombre);
            nuevo.setDescripcion(descripcion);
            nuevo.setIdIglesia(1);
            String idStr = seleccionComboLider.split(" - ")[0];
            nuevo.setIdLider(Integer.parseInt(idStr));

            if (grupoDAO.crear(nuevo)) {
                JOptionPane.showMessageDialog(null, "Grupo creado.");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean agendarReunion(String fecha, String tema, String responsable, String seleccionComboGrupo) {
        if (Validador.esTextoVacio(tema) || seleccionComboGrupo == null) return false;

        try {
            Reunion r = new Reunion();
            r.setFecha(fecha);
            r.setTema(tema);
            r.setResponsable(responsable);
            r.setIdGrupo(Integer.parseInt(seleccionComboGrupo.split(" - ")[0]));

            Proyecto_iglesia.dao.ReunionDAO rDao = new Proyecto_iglesia.dao.ReunionDAO();
            if (rDao.crear(r)) {
                JOptionPane.showMessageDialog(null, "Reunión agendada.");
                return true;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public List<Miembro> obtenerTodosLosMiembros() { return miembroDAO.listarTodos(); }
    public List<Grupo> obtenerTodosLosGrupos() { return grupoDAO.listarTodos(); }
    public List<Reunion> obtenerTodasLasReuniones() {
        return new Proyecto_iglesia.dao.ReunionDAO().listarTodos();
    }
}