package Proyecto_iglesia.vista;

import Proyecto_iglesia.dao.AsistenciaDAO;
import Proyecto_iglesia.dao.GrupoDAO;
import Proyecto_iglesia.dao.MiembroDAO;
import Proyecto_iglesia.dao.ReunionDAO;
import Proyecto_iglesia.modelo.Grupo;
import Proyecto_iglesia.modelo.Miembro;
import Proyecto_iglesia.modelo.Reunion;
import Proyecto_iglesia.modelo.Asistencia;
import Proyecto_iglesia.servicio.Estadisticas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Clase principal de la Interfaz Gráfica.
 * Gestiona la navegación y seguridad basada en roles.
 * Contiene los submódulos en ventanas modales (JDialog) para no saturar el UML.
 *
 * @author Tu Nombre
 * @version 1.0
 */
public class MenuPrincipal extends JFrame {

    private String rolUsuario;

    /**
     * Constructor que inicializa el menú principal con el rol del usuario.
     *
     * @param rol El rol del usuario autenticado (MIEMBRO, LIDER_GRUPO, LIDER_IGLESIA)
     */
    public MenuPrincipal(String rol) {
        // Limpiamos espacios en blanco por si acaso vienen del txt
        this.rolUsuario = rol.trim();
        configurarVentana();
        inicializarComponentes();
    }

    /**
     * Configura las propiedades básicas de la ventana principal.
     */
    private void configurarVentana() {
        // Obtenemos el título traducido desde el controlador
        String titulo = "⛪ " + Proyecto_iglesia.servicio.ControladorPrincipal.getInstancia().getTexto("menu.titulo");
        setTitle(titulo);

        setSize(1100, 700); // Tamaño aumentado para mejor diseño
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    /**
     * Inicializa todos los componentes visuales del menú principal.
     * Incluye encabezado, botones del dashboard y pie de página con versículo bíblico.
     */
    private void inicializarComponentes() {
        // Instancia del controlador para las traducciones
        Proyecto_iglesia.servicio.ControladorPrincipal ctrl = Proyecto_iglesia.servicio.ControladorPrincipal.getInstancia();

        // --- BARRA SUPERIOR (ENCABEZADO) ---
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(30, 39, 46)); // Azul oscuro corporativo más intenso
        panelSuperior.setPreferredSize(new Dimension(0, 80));
        panelSuperior.setLayout(new BorderLayout());

        // Texto de bienvenida traducido + Rol
        String textoBienvenida = "⛪ " + ctrl.getTexto("menu.bienvenida") + " " + rolUsuario;
        JLabel lblBienvenida = new JLabel(textoBienvenida, SwingConstants.CENTER);
        lblBienvenida.setForeground(Color.WHITE);
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 24));

        panelSuperior.add(lblBienvenida, BorderLayout.CENTER);
        add(panelSuperior, BorderLayout.NORTH);

        // --- BOTONES DEL MENÚ (DASHBOARD STYLE) ---
        JPanel panelBotones = new JPanel(new GridLayout(2, 3, 25, 25));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panelBotones.setBackground(new Color(45, 52, 54)); // Fondo gris oscuro

        // 1. GESTIÓN DE MIEMBROS
        JButton btnMiembros = crearBotonDashboard(
                "👥",
                ctrl.getTexto("menu.btn.miembros"),
                new Color(52, 152, 219) // Azul
        );
        btnMiembros.addActionListener(e -> abrirGestionMiembros());

        // 2. GESTIÓN DE GRUPOS
        JButton btnGrupos = crearBotonDashboard(
                "👨‍👩‍👧‍👦",
                ctrl.getTexto("menu.btn.grupos"),
                new Color(155, 89, 182) // Púrpura
        );
        btnGrupos.addActionListener(e -> {
            if (esPastor()) {
                abrirGestionGrupos();
            } else {
                mostrarError("Solo el Líder de Iglesia puede gestionar grupos.");
            }
        });

        // 3. GESTIÓN DE REUNIONES
        JButton btnReuniones = crearBotonDashboard(
                "📅",
                ctrl.getTexto("menu.btn.reuniones"),
                new Color(26, 188, 156) // Verde azulado
        );
        btnReuniones.addActionListener(e -> {
            if (esPastor() || esLiderGrupo()) {
                abrirGestionReuniones();
            } else {
                mostrarError("Necesitas ser Líder de Grupo para agendar reuniones.");
            }
        });

        // 4. REPORTES
        JButton btnReportes = crearBotonDashboard(
                "📊",
                ctrl.getTexto("menu.btn.reportes"),
                new Color(230, 126, 34) // Naranja
        );
        btnReportes.addActionListener(e -> {
            if (esPastor()) {
                mostrarReportesElegante(); // CAMBIO: Ahora usa el método elegante
            } else {
                mostrarError("Información Confidencial. Acceso denegado.");
            }
        });

        // 5. CONFIGURACIÓN IGLESIA
        JButton btnConfig = crearBotonDashboard(
                "⚙️",
                ctrl.getTexto("menu.btn.iglesia"),
                new Color(52, 73, 94) // Azul grisáceo
        );
        // Ahora TODOS pueden entrar a ver la información
        btnConfig.addActionListener(e -> abrirConfiguracionIglesia());

        // 6. SALIR
        JButton btnSalir = crearBotonDashboard(
                "🚪",
                ctrl.getTexto("menu.btn.salir"),
                new Color(192, 57, 43) // Rojo
        );
        btnSalir.addActionListener(e -> {
            this.dispose();
            new PantallaLogin().setVisible(true);
        });

        // Agregar botones al panel en orden
        panelBotones.add(btnMiembros);
        panelBotones.add(btnGrupos);
        panelBotones.add(btnReuniones);
        panelBotones.add(btnReportes);
        panelBotones.add(btnConfig);
        panelBotones.add(btnSalir);

        add(panelBotones, BorderLayout.CENTER);

        // --- PIE DE PÁGINA (FOOTER) ---
        JPanel panelFooter = new JPanel();
        panelFooter.setBackground(new Color(20, 20, 20)); // Negro oscuro
        panelFooter.setPreferredSize(new Dimension(0, 70));
        panelFooter.setLayout(new BorderLayout());
        panelFooter.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        String textoVersiculo = "<html><center><i>\"para que si tardo, sepas cómo debes conducirte en la casa de Dios,<br>" +
                "que es la iglesia del Dios viviente, columna y baluarte de la verdad.\"<br>" +
                "<b>1 TIMOTEO 3:15</b></i></center></html>";

        JLabel lblVersiculo = new JLabel(textoVersiculo, SwingConstants.CENTER);
        lblVersiculo.setForeground(new Color(255, 215, 0)); // Dorado
        lblVersiculo.setFont(new Font("Georgia", Font.ITALIC, 13));

        panelFooter.add(lblVersiculo, BorderLayout.CENTER);
        add(panelFooter, BorderLayout.SOUTH);
    }

    /**
     * Crea un botón estilizado para el dashboard con icono y texto.
     *
     * @param icono Emoji o símbolo que representa la función del botón
     * @param texto Etiqueta descriptiva del botón
     * @param color Color de fondo del botón
     * @return JButton configurado con el estilo dashboard
     */
    private JButton crearBotonDashboard(String icono, String texto, Color color) {
        String html = "<html><body style='text-align:center; width:100%;'>" +
                "<div style='font-size:48px; margin:0 auto; padding:10px 0;'>" + icono + "</div>" +
                "<div style='font-size:14px; font-weight:bold; margin-top:5px;'>" + texto + "</div>" +
                "</body></html>";

        JButton boton = new JButton(html);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setHorizontalAlignment(SwingConstants.CENTER);
        boton.setVerticalAlignment(SwingConstants.CENTER);
        boton.setHorizontalTextPosition(SwingConstants.CENTER);
        boton.setVerticalTextPosition(SwingConstants.CENTER);

        // Efecto hover
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(color.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(color);
            }
        });

        return boton;
    }

    // =================================================================
    // MÉTODOS AUXILIARES DE SEGURIDAD
    // =================================================================

    /**
     * Verifica si el usuario actual tiene rol de Pastor (LIDER_IGLESIA).
     *
     * @return true si el usuario es Pastor, false en caso contrario
     */
    private boolean esPastor() {
        return rolUsuario.equalsIgnoreCase("LIDER_IGLESIA");
    }

    /**
     * Verifica si el usuario actual tiene rol de Líder de Grupo.
     *
     * @return true si el usuario es Líder de Grupo, false en caso contrario
     */
    private boolean esLiderGrupo() {
        return rolUsuario.equalsIgnoreCase("LIDER_GRUPO");
    }

    /**
     * Muestra un mensaje de error de acceso denegado.
     *
     * @param mensaje Descripción específica de la restricción
     */
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, "⛔ ACCESO DENEGADO\n" + mensaje, "Seguridad", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Muestra un diálogo elegante con las estadísticas generales de la iglesia.
     * Usa un diseño de tarjetas (cards) con colores y tipografía moderna.
     * Solo accesible para usuarios con rol de Pastor.
     */
    private void mostrarReportesElegante() {
        // Obtener estadísticas
        Estadisticas stats = new Estadisticas();

        // Crear ventana
        JDialog ventana = new JDialog(this, "📊 Reportes Gerenciales", true);
        ventana.setSize(900, 600);
        ventana.setLocationRelativeTo(this);
        ventana.setLayout(new BorderLayout());

        // Panel principal con fondo oscuro
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(new Color(30, 39, 46));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Título
        JLabel lblTitulo = new JLabel("📊 Estadísticas Generales", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // Panel de tarjetas (2x2 Grid)
        JPanel panelTarjetas = new JPanel(new GridLayout(2, 2, 20, 20));
        panelTarjetas.setBackground(new Color(30, 39, 46));

        // TARJETA 1: Total Miembros
        JPanel card1 = crearTarjetaEstadistica(
                "👥",
                "Total Miembros",
                String.valueOf(stats.getTotalMiembros()),
                new Color(52, 152, 219)
        );

        // TARJETA 2: Grupos Activos
        JPanel card2 = crearTarjetaEstadistica(
                "👨‍👩‍👧‍👦",
                "Grupos Activos",
                String.valueOf(stats.getTotalGrupos()),
                new Color(155, 89, 182)
        );

        // TARJETA 3: Total Reuniones
        JPanel card3 = crearTarjetaEstadistica(
                "📅",
                "Reuniones Realizadas",
                String.valueOf(stats.getTotalReuniones()),
                new Color(26, 188, 156)
        );

        // TARJETA 4: Asistencia Global
        JPanel card4 = crearTarjetaEstadistica(
                "📈",
                "Asistencia Promedio",
                String.format("%.1f%%", stats.getPorcentajeAsistenciaGlobal()),
                new Color(230, 126, 34)
        );

        panelTarjetas.add(card1);
        panelTarjetas.add(card2);
        panelTarjetas.add(card3);
        panelTarjetas.add(card4);

        panelPrincipal.add(panelTarjetas, BorderLayout.CENTER);

        // Botón Cerrar
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setBackground(new Color(52, 73, 94));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCerrar.setFocusPainted(false);
        btnCerrar.addActionListener(e -> ventana.dispose());

        JPanel panelBoton = new JPanel();
        panelBoton.setBackground(new Color(30, 39, 46));
        panelBoton.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        panelBoton.add(btnCerrar);
        panelPrincipal.add(panelBoton, BorderLayout.SOUTH);

        ventana.add(panelPrincipal);
        ventana.setVisible(true);
    }

    /**
     * Crea una tarjeta visual para mostrar una estadística.
     *
     * @param icono Emoji representativo
     * @param titulo Título de la estadística
     * @param valor Valor numérico a mostrar
     * @param color Color de fondo de la tarjeta
     * @return JPanel configurado como tarjeta
     */
    /**
     * Muestra el reporte.
     * Corrección: Conecta datos reales y usa el diseño de tarjeta centrado.
     */
    private void mostrarReportes() { // O mostrarReportesElegante, como lo tengas llamado
        JDialog ventana = new JDialog(this, "Reportes Gerenciales", true);
        ventana.setSize(900, 500);
        ventana.setLocationRelativeTo(this);
        ventana.setLayout(new BorderLayout(20, 20));

        // Fondo oscuro para todo el dialogo
        ventana.getContentPane().setBackground(new Color(40, 40, 40));

        // 1. Título
        JLabel lblTitulo = new JLabel("Tablero de Control", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        ventana.add(lblTitulo, BorderLayout.NORTH);

        // 2. DATOS REALES (Tu backend)
        Proyecto_iglesia.servicio.Estadisticas stats = new Proyecto_iglesia.servicio.Estadisticas();
        int totalMiembros = stats.getTotalMiembros();
        int totalGrupos = stats.getTotalGrupos();
        int totalReuniones = stats.getTotalReuniones();
        // Formatear porcentaje (ej: 85.5%)
        String asistencia = String.format("%.1f%%", stats.getPorcentajeAsistenciaGlobal());

        // 3. PANEL DE TARJETAS
        JPanel panelCentral = new JPanel(new GridLayout(2, 2, 20, 20));
        panelCentral.setBackground(new Color(40, 40, 40)); // Que coincida con el fondo
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));

        // Usamos el método de diseño que encontraste, pero con datos reales
        panelCentral.add(crearTarjetaEstadistica("👥", "Total Miembros", String.valueOf(totalMiembros), new Color(52, 152, 219)));
        panelCentral.add(crearTarjetaEstadistica("🏘️", "Grupos Activos", String.valueOf(totalGrupos), new Color(155, 89, 182)));
        panelCentral.add(crearTarjetaEstadistica("📅", "Reuniones", String.valueOf(totalReuniones), new Color(46, 204, 113)));
        panelCentral.add(crearTarjetaEstadistica("📈", "Asistencia Promedio", asistencia, new Color(241, 196, 15)));

        ventana.add(panelCentral, BorderLayout.CENTER);
        ventana.setVisible(true);
    }

    /**
     * Método de Diseño.
     */
    /**
     * Método de Diseño .
     */
    private JPanel crearTarjetaEstadistica(String icono, String titulo, String valor, Color color) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(color);

        // CAMBIO 1: Reduje el margen interno (de 20 a 10) para ganar espacio
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.darker(), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Icono
        JLabel lblIcono = new JLabel(icono);
        // CAMBIO 2: Fuente un poco más chica (40) para equilibrar
        lblIcono.setFont(new Font("SansSerif", Font.PLAIN, 40));
        lblIcono.setForeground(Color.WHITE);
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Título
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Bajé a 14
        lblTitulo.setForeground(new Color(240, 240, 240));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Espacio entre título y valor
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // Valor (El Dato Real)
        JLabel lblValor = new JLabel(valor);
        // CAMBIO 3: Fuente reducida a 36 (Antes 48) para que no se corte abajo
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblValor.setForeground(Color.WHITE);
        lblValor.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Usamos "Glue" (Pegamento elástico) para centrar verticalmente
        tarjeta.add(Box.createVerticalGlue());
        tarjeta.add(lblIcono);
        tarjeta.add(lblValor); // Puse el valor en medio para que destaque más
        tarjeta.add(lblTitulo);
        tarjeta.add(Box.createVerticalGlue());

        return tarjeta;
    }
    // =================================================================
    // MÓDULO 1: GESTIÓN DE MIEMBROS
    // =================================================================

    /**
     * Abre una ventana modal para gestionar miembros de la iglesia.
     * Permite listar, registrar nuevos miembros y acceder al expediente personal de cada uno.
     */
    private void abrirGestionMiembros() {
        // Instancia del Controlador
        Proyecto_iglesia.servicio.ControladorPrincipal controlador = Proyecto_iglesia.servicio.ControladorPrincipal.getInstancia();

        JDialog ventana = new JDialog(this, "Gestión de Miembros", true);
        ventana.setSize(950, 550);
        ventana.setLocationRelativeTo(this);
        ventana.setLayout(new BorderLayout(10, 10));

        // 1. PRIMERO DEFINIMOS LA TABLA
        DefaultTableModel modelo = new DefaultTableModel(new String[]{"ID", "Nombre", "Apellido", "Rol", "Correo"}, 0);
        JTable tabla = new JTable(modelo);

        // Función local para refrescar datos
        Runnable cargarDatos = () -> {
            modelo.setRowCount(0);
            List<Miembro> lista = controlador.obtenerTodosLosMiembros();
            for (Miembro m : lista) {
                modelo.addRow(new Object[]{
                        m.getId(),
                        m.getNombre(),
                        m.getApellido(),
                        m.getRol(),
                        m.getCorreo()
                });
            }
        };
        cargarDatos.run();

        // 2. FORMULARIO
        JPanel panelForm = new JPanel(new GridLayout(7, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createTitledBorder("Nuevo Miembro"));

        JTextField txtNombre = new JTextField();
        JTextField txtApellido = new JTextField();
        JTextField txtCorreo = new JTextField();

        JComboBox<String> cmbRol = new JComboBox<>();
        cmbRol.addItem("MIEMBRO");

        if (esPastor()) {
            cmbRol.addItem("LIDER_GRUPO");
            cmbRol.addItem("LIDER_IGLESIA");
        }

        panelForm.add(new JLabel(controlador.getTexto("lbl.nombre")));
        panelForm.add(txtNombre);

        panelForm.add(new JLabel(controlador.getTexto("lbl.apellido")));
        panelForm.add(txtApellido);

        panelForm.add(new JLabel(controlador.getTexto("lbl.correo")));
        panelForm.add(txtCorreo);

        panelForm.add(new JLabel(controlador.getTexto("lbl.rol")));
        panelForm.add(cmbRol);

        // 3. BOTONES
        JButton btnGuardar = new JButton(controlador.getTexto("btn.guardar"));
        btnGuardar.setBackground(new Color(46, 204, 113));
        btnGuardar.setForeground(Color.WHITE);

        JButton btnExpediente = new JButton("Ver Expediente");
        btnExpediente.setBackground(new Color(243, 156, 18));
        btnExpediente.setForeground(Color.WHITE);

        panelForm.add(new JLabel(""));
        panelForm.add(btnGuardar);

        panelForm.add(new JLabel("Acciones:"));
        panelForm.add(btnExpediente);

        ventana.add(panelForm, BorderLayout.WEST);
        ventana.add(new JScrollPane(tabla), BorderLayout.CENTER);

        // EVENTOS
        btnGuardar.addActionListener(e -> {
            boolean exito = controlador.registrarMiembro(
                    txtNombre.getText(),
                    txtApellido.getText(),
                    "",
                    txtCorreo.getText(),
                    (String) cmbRol.getSelectedItem()
            );

            if (exito) {
                txtNombre.setText("");
                txtApellido.setText("");
                txtCorreo.setText("");
                cmbRol.setSelectedIndex(0);
                cargarDatos.run();
            }
        });

        btnExpediente.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(ventana, "Seleccione un miembro de la tabla primero.");
                return;
            }

            int idMiembro = (int) tabla.getValueAt(fila, 0);
            String nombreMiembro = (String) tabla.getValueAt(fila, 1);

            abrirDetalleExpediente(ventana, idMiembro, nombreMiembro);
        });

        ventana.setVisible(true);
    }

    // =================================================================
    // MÓDULO 2: GESTIÓN DE GRUPOS
    // =================================================================

    /**
     * Abre una ventana modal para gestionar grupos de la iglesia.
     * Permite crear nuevos grupos y visualizar los existentes.
     * Solo accesible para usuarios con rol de Pastor.
     */
    private void abrirGestionGrupos() {
        Proyecto_iglesia.servicio.ControladorPrincipal controlador = Proyecto_iglesia.servicio.ControladorPrincipal.getInstancia();
        MiembroDAO miembroDAO = new MiembroDAO();

        JDialog ventana = new JDialog(this, "Gestión de Grupos", true);
        ventana.setSize(900, 500);
        ventana.setLocationRelativeTo(this);
        ventana.setLayout(new BorderLayout(10, 10));

        JPanel panelForm = new JPanel(new GridLayout(6, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createTitledBorder("Nuevo Grupo"));

        JTextField txtNombre = new JTextField();
        JTextField txtDescripcion = new JTextField();
        JComboBox<String> cmbLider = new JComboBox<>();

        for (Miembro m : miembroDAO.listarTodos()) {
            cmbLider.addItem(m.getId() + " - " + m.getNombre() + " " + m.getApellido());
        }

        panelForm.add(new JLabel(controlador.getTexto("lbl.nombre")));
        panelForm.add(txtNombre);
        panelForm.add(new JLabel(controlador.getTexto("lbl.descripcion")));
        panelForm.add(txtDescripcion);
        panelForm.add(new JLabel(controlador.getTexto("lbl.lider")));
        panelForm.add(cmbLider);

        JButton btnGuardar = new JButton(controlador.getTexto("btn.guardar"));
        btnGuardar.setBackground(new Color(52, 152, 219));
        btnGuardar.setForeground(Color.WHITE);
        panelForm.add(new JLabel(""));
        panelForm.add(btnGuardar);

        ventana.add(panelForm, BorderLayout.WEST);

        DefaultTableModel modelo = new DefaultTableModel(new String[]{"ID", "Nombre", "Descripción", "ID Líder"}, 0);
        JTable tabla = new JTable(modelo);
        ventana.add(new JScrollPane(tabla), BorderLayout.CENTER);

        Runnable cargarDatos = () -> {
            modelo.setRowCount(0);
            List<Grupo> lista = controlador.obtenerTodosLosGrupos();
            for (Grupo g : lista) {
                modelo.addRow(new Object[]{g.getId(), g.getNombre(), g.getDescripcion(), g.getIdLider()});
            }
        };
        cargarDatos.run();

        btnGuardar.addActionListener(e -> {
            boolean exito = controlador.registrarGrupo(
                    txtNombre.getText(),
                    txtDescripcion.getText(),
                    (String) cmbLider.getSelectedItem()
            );

            if (exito) {
                txtNombre.setText("");
                txtDescripcion.setText("");
                cargarDatos.run();
            }
        });

        ventana.setVisible(true);
    }

    // =================================================================
    // MÓDULO 3: GESTIÓN DE REUNIONES (REFACTORIZADO)
    // =================================================================

    /**
     * Abre una ventana modal para gestionar reuniones de grupos.
     * Permite agendar nuevas reuniones con validación estricta y tomar asistencia.
     * Accesible para Pastores y Líderes de Grupo.
     */
    private void abrirGestionReuniones() {
        Proyecto_iglesia.servicio.ControladorPrincipal ctrl = Proyecto_iglesia.servicio.ControladorPrincipal.getInstancia();

        JDialog ventana = new JDialog(this, "Gestión de Reuniones", true);
        ventana.setSize(1000, 600);
        ventana.setLocationRelativeTo(this);
        ventana.setLayout(new BorderLayout(10, 10));

        ReunionDAO reunionDAO = new ReunionDAO();
        GrupoDAO grupoDAO = new GrupoDAO();

        // --- PANEL FORMULARIO CON GRIDBAG LAYOUT ---
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Agendar Nueva Reunión"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 1: Grupo y Fecha
        gbc.gridx = 0; gbc.gridy = 0;
        panelForm.add(new JLabel("Grupo:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        JComboBox<String> cmbGrupo = new JComboBox<>();
        for (Grupo g : grupoDAO.listarTodos()) cmbGrupo.addItem(g.getId() + " - " + g.getNombre());
        panelForm.add(cmbGrupo, gbc);

        gbc.gridx = 2; gbc.gridy = 0;
        panelForm.add(new JLabel(ctrl.getTexto("lbl.fecha")), gbc);

        gbc.gridx = 3; gbc.gridy = 0;
        JTextField txtFecha = new JTextField("2025-11-25", 10);
        panelForm.add(txtFecha, gbc);

        // Fila 2: Tema y Responsable
        gbc.gridx = 0; gbc.gridy = 1;
        panelForm.add(new JLabel(ctrl.getTexto("lbl.tema")), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        JTextField txtTema = new JTextField(15);
        panelForm.add(txtTema, gbc);

        gbc.gridx = 2; gbc.gridy = 1;
        panelForm.add(new JLabel(ctrl.getTexto("lbl.responsable")), gbc);

        gbc.gridx = 3; gbc.gridy = 1;
        JTextField txtResp = new JTextField(10);
        panelForm.add(txtResp, gbc);

        // Fila 3: Botón Agendar
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.gridwidth = 2;
        JButton btnGuardar = new JButton(ctrl.getTexto("btn.agendar"));
        btnGuardar.setBackground(new Color(46, 204, 113));
        btnGuardar.setForeground(Color.WHITE);
        panelForm.add(btnGuardar, gbc);

        ventana.add(panelForm, BorderLayout.NORTH);

        // --- TABLA ---
        DefaultTableModel modelo = new DefaultTableModel(new String[]{"ID", "ID Grupo", "Fecha", "Tema", "Estado"}, 0);
        JTable tabla = new JTable(modelo);
        ventana.add(new JScrollPane(tabla), BorderLayout.CENTER);

        Runnable cargarDatos = () -> {
            modelo.setRowCount(0);
            for (Reunion r : reunionDAO.listarTodos()) {
                modelo.addRow(new Object[]{r.getId(), r.getIdGrupo(), r.getFecha(), r.getTema(), r.isFinalizada() ? "CERRADA" : "ABIERTA"});
            }
        };
        cargarDatos.run();

        // --- VALIDACIÓN Y GUARDADO ---
        btnGuardar.addActionListener(e -> {
            // VALIDACIÓN ESTRICTA
            if (txtTema.getText().trim().isEmpty() ||
                    txtFecha.getText().trim().isEmpty() ||
                    txtResp.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(ventana,
                        "⚠️ Todos los campos son obligatorios:\n- Tema\n- Fecha\n- Responsable",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                Reunion r = new Reunion();
                r.setFecha(txtFecha.getText().trim());
                r.setTema(txtTema.getText().trim());
                r.setResponsable(txtResp.getText().trim());
                String sel = (String) cmbGrupo.getSelectedItem();
                if (sel != null) r.setIdGrupo(Integer.parseInt(sel.split(" - ")[0]));

                if (reunionDAO.crear(r)) {
                    JOptionPane.showMessageDialog(ventana, "✅ Reunión agendada exitosamente!");
                    // Limpiar campos
                    txtTema.setText("");
                    txtFecha.setText("2025-11-25");
                    txtResp.setText("");
                    cargarDatos.run();
                } else {
                    JOptionPane.showMessageDialog(ventana, "❌ Error al guardar la reunión.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(ventana, "❌ Error: " + ex.getMessage());
            }
        });

        // --- BOTÓN TOMAR ASISTENCIA ---
        JButton btnAsistencia = new JButton(ctrl.getTexto("btn.tomar_asistencia"));
        btnAsistencia.setBackground(new Color(52, 152, 219));
        btnAsistencia.setForeground(Color.WHITE);
        btnAsistencia.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(ventana, "⚠️ Seleccione una reunión de la tabla primero.");
                return;
            }

            int idReunion = (int) tabla.getValueAt(fila, 0);
            String estado = (String) tabla.getValueAt(fila, 4);

            if (estado.equals("CERRADA")) {
                JOptionPane.showMessageDialog(ventana, "ℹ️ Esta reunión ya fue finalizada.");
            } else {
                // CAMBIO: Pasamos el Runnable para que se actualice la tabla al regresar
                abrirTomarAsistencia(idReunion, cargarDatos);
            }
        });

        ventana.add(btnAsistencia, BorderLayout.SOUTH);
        ventana.setVisible(true);
    }

// =================================================================
// MÓDULO 4: CONTROL DE ASISTENCIA (CORREGIDO)
// =================================================================

    /**
     * Abre una ventana modal para registrar asistencia en una reunión específica.
     *
     * @param idReunion ID de la reunión en la que se tomará asistencia
     * @param actualizarTabla Runnable que actualiza la tabla de reuniones al finalizar
     */
    private void abrirTomarAsistencia(int idReunion, Runnable actualizarTabla) {
        JDialog ventana = new JDialog(this, "Control de Asistencia - ID: " + idReunion, true);
        ventana.setSize(800, 500);
        ventana.setLocationRelativeTo(this);
        ventana.setLayout(new BorderLayout());

        MiembroDAO mDAO = new MiembroDAO();
        AsistenciaDAO aDAO = new AsistenciaDAO();
        ReunionDAO reunionDAO = new ReunionDAO();

        DefaultTableModel modelo = new DefaultTableModel(new String[]{"ID", "Nombre", "Estado"}, 0);
        JTable tabla = new JTable(modelo);
        ventana.add(new JScrollPane(tabla), BorderLayout.CENTER);

        Runnable cargar = () -> {
            modelo.setRowCount(0);
            List<Miembro> miembros = mDAO.listarTodos();
            List<Asistencia> asistencias = aDAO.listarPorReunion(idReunion);

            for (Miembro m : miembros) {
                String estado = "PENDIENTE";
                for (Asistencia a : asistencias) {
                    if (a.getIdMiembro() == m.getId()) {
                        estado = a.isAsistio() ? "✅ PRESENTE" : "❌ AUSENTE";
                        break;
                    }
                }
                modelo.addRow(new Object[]{m.getId(), m.getNombre() + " " + m.getApellido(), estado});
            }
        };
        cargar.run();

        JPanel panelSur = new JPanel();
        JButton btnSi = new JButton("Marcar PRESENTE");
        btnSi.setBackground(new Color(46, 204, 113));
        btnSi.setForeground(Color.WHITE);

        JButton btnNo = new JButton("Marcar AUSENTE");
        btnNo.setBackground(new Color(231, 76, 60));
        btnNo.setForeground(Color.WHITE);

        JButton btnFin = new JButton("Finalizar Reunión");
        btnFin.setBackground(new Color(52, 73, 94));
        btnFin.setForeground(Color.WHITE);

        btnSi.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila != -1) {
                aDAO.crear(new Asistencia(0, idReunion, (int)tabla.getValueAt(fila, 0), true));
                cargar.run();
            }
        });

        btnNo.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila != -1) {
                Asistencia a = new Asistencia(0, idReunion, (int)tabla.getValueAt(fila, 0), false);
                a.setExcusa("Falta");
                aDAO.crear(a);
                cargar.run();
            }
        });

        // CORRECCIÓN: Actualiza el estado en la BD antes de cerrar
        btnFin.addActionListener(e -> {
            String minuta = JOptionPane.showInputDialog(ventana,
                    "Ingrese la minuta o resumen de la reunión:",
                    "Finalizar Reunión",
                    JOptionPane.QUESTION_MESSAGE);

            if (minuta != null && !minuta.trim().isEmpty()) {
                // Obtener la reunión y actualizarla
                Reunion reunion = reunionDAO.leer(idReunion);
                if (reunion != null) {
                    reunion.setFinalizada(true);
                    reunion.setMinuta(minuta.trim());

                    if (reunionDAO.actualizar(reunion)) {
                        JOptionPane.showMessageDialog(ventana, "✅ Reunión finalizada y guardada correctamente.");
                        ventana.dispose();
                        // Actualizar la tabla de reuniones en la ventana padre
                        if (actualizarTabla != null) {
                            actualizarTabla.run();
                        }
                    } else {
                        JOptionPane.showMessageDialog(ventana, "❌ Error al actualizar el estado de la reunión.");
                    }
                } else {
                    JOptionPane.showMessageDialog(ventana, "❌ No se pudo cargar la reunión.");
                }
            }
        });

        panelSur.add(btnSi); panelSur.add(btnNo); panelSur.add(btnFin);
        ventana.add(panelSur, BorderLayout.SOUTH);
        ventana.setVisible(true);
    }

// =================================================================
// MÓDULO 5: CONFIGURACIÓN DE IGLESIA (Datos Institucionales)
// =================================================================

    /**
     * Abre una ventana modal con la información institucional de la iglesia.
     * TODOS los usuarios pueden ver la información.
     * SOLO el Pastor puede editarla y guardar cambios.
     */
    private void abrirConfiguracionIglesia() {
        JDialog ventana = new JDialog(this, "Información de la Iglesia", true);
        ventana.setSize(500, 400);
        ventana.setLocationRelativeTo(this);
        ventana.setLayout(new GridLayout(7, 2, 10, 10));

        ((JPanel)ventana.getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // DAO
        Proyecto_iglesia.dao.IglesiaDAO iglesiaDAO = new Proyecto_iglesia.dao.IglesiaDAO();
        Proyecto_iglesia.modelo.Iglesia miIglesia = iglesiaDAO.leer(1);

        JTextField txtNombre = new JTextField(miIglesia.getNombre());
        JTextField txtPastor = new JTextField(miIglesia.getPastorPrincipal());
        JTextField txtDireccion = new JTextField(miIglesia.getDireccion());
        JTextField txtTelefono = new JTextField(miIglesia.getTelefono());
        JTextField txtEmail = new JTextField(miIglesia.getEmail());
        JTextField txtDenom = new JTextField("Evangélica");

        // LÓGICA DE PERMISOS: Si NO es pastor, los campos son solo lectura
        if (!esPastor()) {
            txtNombre.setEditable(false);
            txtPastor.setEditable(false);
            txtDireccion.setEditable(false);
            txtTelefono.setEditable(false);
            txtEmail.setEditable(false);
            txtDenom.setEditable(false);

            // Cambiar color de fondo para indicar que es solo lectura
            Color colorSoloLectura = new Color(240, 240, 240);
            txtNombre.setBackground(colorSoloLectura);
            txtPastor.setBackground(colorSoloLectura);
            txtDireccion.setBackground(colorSoloLectura);
            txtTelefono.setBackground(colorSoloLectura);
            txtEmail.setBackground(colorSoloLectura);
            txtDenom.setBackground(colorSoloLectura);
        }

        ventana.add(new JLabel("Nombre Iglesia:")); ventana.add(txtNombre);
        ventana.add(new JLabel("Pastor Principal:")); ventana.add(txtPastor);
        ventana.add(new JLabel("Denominación:")); ventana.add(txtDenom);
        ventana.add(new JLabel("Dirección:")); ventana.add(txtDireccion);
        ventana.add(new JLabel("Teléfono:")); ventana.add(txtTelefono);
        ventana.add(new JLabel("Email:")); ventana.add(txtEmail);

        // SOLO agregar el botón de guardar si es Pastor
        if (esPastor()) {
            JButton btnGuardar = new JButton("Actualizar Datos");
            btnGuardar.setBackground(new Color(52, 73, 94));
            btnGuardar.setForeground(Color.WHITE);

            ventana.add(new JLabel(""));
            ventana.add(btnGuardar);

            // Acción Guardar
            btnGuardar.addActionListener(e -> {
                miIglesia.setNombre(txtNombre.getText());
                miIglesia.setPastorPrincipal(txtPastor.getText());
                miIglesia.setDireccion(txtDireccion.getText());
                miIglesia.setTelefono(txtTelefono.getText());
                miIglesia.setEmail(txtEmail.getText());

                if (iglesiaDAO.actualizar(miIglesia)) {
                    JOptionPane.showMessageDialog(ventana, "✅ Datos de la iglesia actualizados.");
                    ventana.dispose();
                } else {
                    JOptionPane.showMessageDialog(ventana, "❌ Error al actualizar.");
                }
            });
        } else {
            // Si no es pastor, agregar etiqueta informativa
            JLabel lblInfo = new JLabel("ℹ️ Solo lectura");
            lblInfo.setForeground(new Color(52, 73, 94));
            ventana.add(lblInfo);
            ventana.add(new JLabel(""));
        }

        ventana.setVisible(true);
    }

    /**
     * Abre una ventana modal con el expediente detallado de un miembro.
     *
     * @param padre Ventana padre desde la que se invoca
     * @param idMiembro ID del miembro cuyo expediente se visualizará
     * @param nombre Nombre del miembro para mostrar en el título
     */
    private void abrirDetalleExpediente(JDialog padre, int idMiembro, String nombre) {
        JDialog ventExp = new JDialog(padre, "Expediente de: " + nombre, true);
        ventExp.setSize(500, 400);
        ventExp.setLocationRelativeTo(padre);
        ventExp.setLayout(new GridLayout(5, 1, 10, 10));
        ((JPanel)ventExp.getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // DAO
        Proyecto_iglesia.dao.ExpedienteDAO expDAO = new Proyecto_iglesia.dao.ExpedienteDAO();
        Proyecto_iglesia.modelo.Expediente expediente = expDAO.leerPorMiembro(idMiembro);

        // Campos
        JTextArea txtNotas = new JTextArea(3, 20);
        txtNotas.setBorder(BorderFactory.createTitledBorder("Notas Pastorales"));

        JTextField txtHistorial = new JTextField();
        txtHistorial.setBorder(BorderFactory.createTitledBorder("Historial Ministerial"));

        JTextField txtObservaciones = new JTextField();
        txtObservaciones.setBorder(BorderFactory.createTitledBorder("Otras Observaciones"));

        // Si ya existe, llenamos los datos
        if (expediente != null) {
            txtNotas.setText(expediente.getNotas());
            txtHistorial.setText(expediente.getHistorialMinisterial());
            txtObservaciones.setText(expediente.getObservaciones());
        }

        JButton btnGuardarExp = new JButton("Guardar Cambios en Expediente");
        btnGuardarExp.setBackground(new Color(52, 152, 219));
        btnGuardarExp.setForeground(Color.WHITE);

        ventExp.add(new JScrollPane(txtNotas));
        ventExp.add(txtHistorial);
        ventExp.add(txtObservaciones);
        ventExp.add(btnGuardarExp);

        // Acción
        btnGuardarExp.addActionListener(evt -> {
            Proyecto_iglesia.modelo.Expediente nuevoExp = new Proyecto_iglesia.modelo.Expediente();
            nuevoExp.setIdMiembro(idMiembro);
            nuevoExp.setNotas(txtNotas.getText());
            nuevoExp.setHistorialMinisterial(txtHistorial.getText());
            nuevoExp.setObservaciones(txtObservaciones.getText());

            if (expediente == null) {
                expDAO.crear(nuevoExp);
            } else {
                expDAO.actualizar(nuevoExp);
            }
            JOptionPane.showMessageDialog(ventExp, "✅ Expediente actualizado.");
            ventExp.dispose();
        });

        ventExp.setVisible(true);
    }
}