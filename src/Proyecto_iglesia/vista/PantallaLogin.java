package Proyecto_iglesia.vista;

import Proyecto_iglesia.persistencia.GestorAutenticacion;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// CORRECCIÓN AQUÍ: Debe extender de JFrame para ser una ventana
public class PantallaLogin extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnIngresar;
    private GestorAutenticacion gestorAuth;

    public PantallaLogin() {
        // 1. Configuración de la Ventana
        setTitle("Sistema de Gestión Iglesia - Acceso");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en pantalla
        setLayout(new GridBagLayout()); // Diseño centrado

        gestorAuth = new GestorAutenticacion();
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // 1. Instanciamos el controlador para los idiomas
        Proyecto_iglesia.servicio.ControladorPrincipal ctrl = Proyecto_iglesia.servicio.ControladorPrincipal.getInstancia();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Título (Traducido) ---
        JLabel lblTitulo = new JLabel(ctrl.getTexto("login.titulo"));
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(lblTitulo, gbc);

        // --- Usuario (Traducido) ---
        gbc.gridwidth = 1; gbc.gridy = 1;
        add(new JLabel(ctrl.getTexto("login.usuario")), gbc);

        txtUsuario = new JTextField(15);
        gbc.gridx = 1;
        add(txtUsuario, gbc);

        // --- Contraseña (Traducido) ---
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel(ctrl.getTexto("login.clave")), gbc);

        txtPassword = new JPasswordField(15);
        gbc.gridx = 1;
        add(txtPassword, gbc);

        // --- Botón Ingresar (Traducido) ---
        btnIngresar = new JButton(ctrl.getTexto("login.boton"));

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        add(btnIngresar, gbc);

        // --- ACCIÓN DEL BOTÓN ---
        btnIngresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                procesarLogin();
            }
        });
    }

    private void procesarLogin() {
        // 1. Obtener datos de los campos
        String usuario = txtUsuario.getText();

        // IMPORTANTE: JPasswordField devuelve un arreglo de caracteres (char[]),
        // es obligatorio convertirlo a String así:
        String password = new String(txtPassword.getPassword());

        // 2. Llamar a tu lógica de Backend (GestorAutenticacion)
        String rol = gestorAuth.autenticar(usuario, password);

        // 3. Tomar decisión según el resultado
        if (rol != null) {
            // --- CASO ÉXITO ---
            this.dispose(); // Cerrar login

            // Abrir menú principal
            MenuPrincipal menu = new MenuPrincipal(rol);
            menu.setVisible(true);

        } else {
            // --- CASO ERROR ---
            JOptionPane.showMessageDialog(this,
                    "Usuario o contraseña incorrectos.",
                    "Error de Acceso",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}