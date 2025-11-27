package Proyecto_iglesia.app;

import Proyecto_iglesia.servicio.ControladorPrincipal;
import Proyecto_iglesia.vista.PantallaLogin;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;

public class Main { // <--- NO pongas "extends PantallaLogin" aquí

    public static void main(String[] args) {

        // 1. ACTIVAR EL TEMA VISUAL MODERNO
        try {
            FlatDarkLaf.setup();

            // Personalización de colores y bordes
            UIManager.put("Component.accentColor", Color.decode("#3498db"));
            UIManager.put("Button.arc", 20);
            UIManager.put("Component.arc", 20);
            UIManager.put("TextComponent.arc", 20);
            UIManager.put("ProgressBar.arc", 20);
            UIManager.put("Button.margin", new Insets(10, 20, 10, 20));
            UIManager.put("defaultFont", new Font("Segoe UI", Font.PLAIN, 14));

        } catch (Exception ex) {
            System.err.println("⚠️ No se pudo cargar FlatLaf. Usando diseño por defecto.");
        }

        // 2. SELECCIÓN DE IDIOMA AL INICIO
        Object[] opciones = {"Español", "English"};
        int seleccion = JOptionPane.showOptionDialog(null,
                "Language / Idioma:", "Configuración",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, opciones, opciones[0]);

        // Configuramos el idioma en el Controlador
        ControladorPrincipal controlador = ControladorPrincipal.getInstancia();
        if (seleccion == 1) {
            controlador.configurarIdioma("en");
        } else {
            controlador.configurarIdioma("es");
        }

        // 3. ARRANCAR LA PANTALLA DE LOGIN
        SwingUtilities.invokeLater(() -> {
            System.out.println("🚀 Iniciando sistema...");
            PantallaLogin login = new PantallaLogin();
            login.setVisible(true);
        });
    }
}