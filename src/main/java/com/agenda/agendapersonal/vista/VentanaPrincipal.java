package com.agenda.agendapersonal.vista;

import com.agenda.agendapersonal.controlador.AgendaControlador;
import com.agenda.agendapersonal.controlador.UsuarioControlador;
import com.agenda.agendapersonal.config.Constantes;
import com.agenda.agendapersonal.modelo.Usuario;
import com.agenda.agendapersonal.modelo.Evento;
import com.agenda.agendapersonal.modelo.EstadoEvento;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

/**
 * Ventana principal de la aplicación Agenda Personal
 * 
 * @author JaimeSQL
 */
public class VentanaPrincipal extends JFrame {
    
    private AgendaControlador agendaControlador;
    private JPanel panelPrincipal;
    private JLabel lblEstado;
    private JLabel lblUsuario;
    
    // Paneles de contenido
    private JPanel panelLogin;
    private JPanel panelDashboard;
    
    public VentanaPrincipal() {
        this.agendaControlador = new AgendaControlador();
        initComponents();
        inicializarAplicacion();
    }
    
    /**
     * Inicializar componentes de la interfaz
     */
    private void initComponents() {
        setTitle(Constantes.obtenerTituloCompleto());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1200, 800);
        setMinimumSize(new Dimension(1000, 700));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        crearComponentes();
        configurarEventos();
        mostrarPanelLogin();
    }
    
    private void crearComponentes() {
        // Panel principal con CardLayout
        panelPrincipal = new JPanel(new CardLayout());
        
        // Panel de estado
        JPanel panelEstado = new JPanel(new BorderLayout());
        panelEstado.setBorder(BorderFactory.createEtchedBorder());
        
        lblEstado = new JLabel("Listo");
        lblEstado.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        lblUsuario = new JLabel("No autenticado");
        lblUsuario.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        lblUsuario.setHorizontalAlignment(SwingConstants.RIGHT);
        
        panelEstado.add(lblEstado, BorderLayout.WEST);
        panelEstado.add(lblUsuario, BorderLayout.EAST);
        
        // Crear paneles
        crearPanelLogin();
        crearPanelDashboard();
        
        // Agregar a ventana
        add(panelPrincipal, BorderLayout.CENTER);
        add(panelEstado, BorderLayout.SOUTH);
    }
    
    private void crearPanelLogin() {
        panelLogin = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Título
        JLabel lblTitulo = new JLabel("🏠 Agenda Personal");
        lblTitulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Campos
        JLabel lblUsuario = new JLabel("Usuario:");
        JTextField txtUsuario = new JTextField(20);
        
        JLabel lblPassword = new JLabel("Contraseña:");
        JPasswordField txtPassword = new JPasswordField(20);
        
        // Botones
        JButton btnLogin = new JButton("Iniciar Sesión");
        JButton btnTest = new JButton("Usuario de Prueba");
        
        // Layout
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panelLogin.add(lblTitulo, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        panelLogin.add(lblUsuario, gbc);
        gbc.gridx = 1;
        panelLogin.add(txtUsuario, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panelLogin.add(lblPassword, gbc);
        gbc.gridx = 1;
        panelLogin.add(txtPassword, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        panelLogin.add(btnLogin, gbc);
        
        gbc.gridy = 4;
        panelLogin.add(btnTest, gbc);
        
        // Eventos
        btnLogin.addActionListener(e -> {
            String usuario = txtUsuario.getText();
            String password = new String(txtPassword.getPassword());
            realizarLogin(usuario, password);
        });
        
        btnTest.addActionListener(e -> realizarLogin("jperez", "password"));
        
        // Enter para login
        txtPassword.addActionListener(e -> {
            String usuario = txtUsuario.getText();
            String password = new String(txtPassword.getPassword());
            realizarLogin(usuario, password);
        });
        
        panelPrincipal.add(panelLogin, "LOGIN");
    }
    
    private void crearPanelDashboard() {
        panelDashboard = new JPanel(new BorderLayout());
        panelDashboard.setBackground(new Color(240, 242, 245)); // Fondo gris claro
        
        // ===== HEADER PRINCIPAL =====
        JPanel headerPanel = crearHeaderModerno();
        
        // ===== ESTADÍSTICAS =====
        JPanel estadisticasPanel = crearPanelEstadisticas();
        
        // ===== FILTROS Y NUEVA TAREA =====
        JPanel filtrosPanel = crearPanelFiltros();
        
        // ===== TABLA DE TAREAS =====
        JPanel tablaPanel = crearPanelTabla();
        
        // ===== PANEL CENTRAL (Estadísticas + Filtros + Tabla) =====
        JPanel centralPanel = new JPanel(new BorderLayout(0, 25));
        centralPanel.setBackground(new Color(240, 242, 245)); // Fondo gris claro
        centralPanel.setBorder(BorderFactory.createEmptyBorder(25, 35, 35, 35));
        
        centralPanel.add(estadisticasPanel, BorderLayout.NORTH);
        
        JPanel filtrosYTabla = new JPanel(new BorderLayout(0, 20));
        filtrosYTabla.setBackground(new Color(240, 242, 245)); // Fondo gris claro
        filtrosYTabla.add(filtrosPanel, BorderLayout.NORTH);
        filtrosYTabla.add(tablaPanel, BorderLayout.CENTER);
        
        centralPanel.add(filtrosYTabla, BorderLayout.CENTER);
        
        // ===== ENSAMBLAR DASHBOARD =====
        panelDashboard.add(headerPanel, BorderLayout.NORTH);
        panelDashboard.add(centralPanel, BorderLayout.CENTER);
        
        panelPrincipal.add(panelDashboard, "DASHBOARD");
    }
    
    private JPanel crearHeaderModerno() {
        // Container con padding
        JPanel headerContainer = new JPanel(new BorderLayout());
        headerContainer.setBackground(new Color(240, 242, 245));
        headerContainer.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        
        // Header principal redondeado
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(new Color(101, 116, 205)); // Color morado del diseño
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(101, 116, 205), 15, true), // Bordes redondeados simulados
            BorderFactory.createEmptyBorder(30, 35, 30, 35)
        ));
        
        // Título principal
        JLabel lblTitulo = new JLabel("📋 Gestor de Tareas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Subtítulo
        JLabel lblSubtitulo = new JLabel("Organiza y gestiona tus eventos y tareas diarias");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSubtitulo.setForeground(new Color(255, 255, 255, 200));
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        header.add(lblTitulo);
        header.add(Box.createVerticalStrut(8));
        header.add(lblSubtitulo);
        
        headerContainer.add(header, BorderLayout.CENTER);
        return headerContainer;
    }
    
    private JPanel crearPanelEstadisticas() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 0)); // Espacio más pequeño entre tarjetas
        panel.setBackground(new Color(240, 242, 245)); // Fondo gris claro
        
        // Obtener estadísticas reales
        List<Evento> eventos = agendaControlador.getEventoControlador().obtenerEventosUsuario();
        int totalTareas = eventos.size();
        int pendientes = (int) eventos.stream().filter(e -> e.getEstado() == EstadoEvento.PENDIENTE).count();
        int completadas = (int) eventos.stream().filter(e -> e.getEstado() == EstadoEvento.COMPLETADO).count();
        
        // Crear tarjetas de estadísticas con fondo de color
        panel.add(crearTarjetaEstadistica("TOTAL TAREAS", String.valueOf(totalTareas), new Color(52, 144, 220), "📊"));
        panel.add(crearTarjetaEstadistica("PENDIENTES", String.valueOf(pendientes), new Color(255, 159, 67), "⏳"));
        panel.add(crearTarjetaEstadistica("COMPLETADAS", String.valueOf(completadas), new Color(95, 195, 134), "✅"));
        
        return panel;
    }
    
    private JPanel crearTarjetaEstadistica(String titulo, String valor, Color colorBorde, String icono) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(colorBorde); // Fondo con el color en lugar del borde
        
        // Crear tarjeta más pequeña con bordes redondeados más delgados
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(colorBorde, 3, true), // Borde más delgado
            BorderFactory.createEmptyBorder(20, 20, 20, 20) // Padding más pequeño
        ));
        
        // Título con mejor contraste sobre fondo de color
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 13)); // Fuente ligeramente más grande
        lblTitulo.setForeground(Color.WHITE); // Texto blanco sobre fondo de color
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Valor más pequeño con contraste mejorado
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 40)); // Fuente más grande para mejor legibilidad
        lblValor.setForeground(Color.WHITE); // Texto blanco sobre fondo de color
        lblValor.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        tarjeta.add(lblTitulo);
        tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(lblValor);
        
        return tarjeta;
    }
    
    private JPanel crearPanelFiltros() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 242, 245)); // Fondo gris claro
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Panel izquierdo - Filtros
        JPanel filtrosIzq = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        filtrosIzq.setBackground(new Color(240, 242, 245));
        
        // Botones de filtro con mejor estilo
        JButton btnTodas = crearBotonFiltro("Todas", true);
        JButton btnPendientes = crearBotonFiltro("Pendientes", false);
        JButton btnCompletadas = crearBotonFiltro("Completadas", false);
        
        filtrosIzq.add(btnTodas);
        filtrosIzq.add(Box.createHorizontalStrut(8));
        filtrosIzq.add(btnPendientes);
        filtrosIzq.add(Box.createHorizontalStrut(8));
        filtrosIzq.add(btnCompletadas);
        
        // Panel derecho - Botón Nueva Tarea
        JPanel accionesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        accionesPanel.setBackground(new Color(240, 242, 245));
        
        // Botón Nueva Tarea como en el diseño
        JButton btnNuevaTarea = new JButton("+ Nueva Tarea");
        btnNuevaTarea.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnNuevaTarea.setBackground(new Color(101, 116, 205));
        btnNuevaTarea.setForeground(new Color(50, 50, 50)); // Gris muy oscuro igual que los otros
        btnNuevaTarea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(101, 116, 205), 3, true), // Borde más delgado
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        btnNuevaTarea.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNuevaTarea.setFocusPainted(false);
        
        // Efectos hover
        btnNuevaTarea.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnNuevaTarea.setBackground(new Color(88, 101, 180));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnNuevaTarea.setBackground(new Color(101, 116, 205));
            }
        });
        
        // Eventos
        btnNuevaTarea.addActionListener(e -> abrirFormularioNuevaTarea());
        
        accionesPanel.add(btnNuevaTarea);
        
        panel.add(filtrosIzq, BorderLayout.WEST);
        panel.add(accionesPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JButton crearBotonFiltro(String texto, boolean activo) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setFocusPainted(false);
        
        if (activo) {
            boton.setBackground(new Color(101, 116, 205));
            boton.setForeground(new Color(50, 50, 50)); // Gris muy oscuro igual que los otros
            boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(101, 116, 205), 3, true), // Borde más delgado
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
            ));
        } else {
            boton.setBackground(Color.WHITE);
            boton.setForeground(new Color(50, 50, 50)); // Gris muy oscuro
            boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 226, 230), 3, true), // Borde más delgado
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
            ));
        }
        
        // Efecto hover como en el diseño
        if (!activo) {
            boton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    boton.setBackground(new Color(248, 249, 250));
                    boton.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(101, 116, 205), 3, true),
                        BorderFactory.createEmptyBorder(10, 20, 10, 20)
                    ));
                }
                
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    boton.setBackground(Color.WHITE);
                    boton.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(222, 226, 230), 3, true),
                        BorderFactory.createEmptyBorder(10, 20, 10, 20)
                    ));
                }
            });
        }
        
        return boton;
    }
    
    private JPanel crearPanelTabla() {
        JPanel panelContainer = new JPanel(new BorderLayout());
        panelContainer.setBackground(new Color(240, 242, 245));
        panelContainer.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(222, 226, 230), 3, true), // Borde más delgado
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        
        // Crear tabla
        String[] columnas = {"ID", "TÍTULO", "DESCRIPCIÓN", "FECHA INICIO"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabla de solo lectura
            }
        };
        
        JTable tabla = new JTable(modelo);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabla.setRowHeight(60); // Filas más altas como en el diseño
        tabla.setGridColor(new Color(233, 236, 239));
        tabla.setSelectionBackground(new Color(232, 240, 254));
        tabla.setSelectionForeground(new Color(33, 37, 41));
        tabla.setBackground(Color.GRAY);
        tabla.setForeground(new Color(33, 37, 41)); // Texto más oscuro y visible
        tabla.setShowVerticalLines(true);
        tabla.setShowHorizontalLines(true);
        tabla.setIntercellSpacing(new Dimension(0, 1));
        
        // Header de la tabla como en el diseño con bordes redondeados
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabla.getTableHeader().setBackground(new Color(101, 116, 205)); // Color azul del diseño
        tabla.getTableHeader().setForeground(new Color(50, 50, 50)); // Gris muy oscuro para mejor legibilidad
        tabla.getTableHeader().setBorder(BorderFactory.createEmptyBorder(18, 15, 18, 15));
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.getTableHeader().setOpaque(true);
        
        // Configurar columnas
        tabla.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID
        tabla.getColumnModel().getColumn(1).setPreferredWidth(250); // Título
        tabla.getColumnModel().getColumn(2).setPreferredWidth(350); // Descripción
        tabla.getColumnModel().getColumn(3).setPreferredWidth(180); // Fecha
        
        // Cargar datos
        cargarDatosTabla(modelo);
        
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panelContainer.add(panel, BorderLayout.CENTER);
        
        return panelContainer;
    }
    
    private void cargarDatosTabla(DefaultTableModel modelo) {
        // Limpiar tabla
        modelo.setRowCount(0);
        
        // Obtener eventos del usuario actual
        List<Evento> eventos = agendaControlador.getEventoControlador().obtenerEventosUsuario();
        
        // Agregar filas a la tabla
        for (Evento evento : eventos) {
            Object[] fila = {
                evento.getIdEvento(),
                evento.getTitulo(),
                evento.getDescripcion() != null ? evento.getDescripcion() : "",
                evento.getFechaInicioFormateada()
            };
            modelo.addRow(fila);
        }
    }
    
    private void abrirFormularioNuevaTarea() {
        JOptionPane.showMessageDialog(this, 
            "Función 'Nueva Tarea' - Aquí se abriría el formulario de creación", 
            "Nueva Tarea", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void configurarEventos() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                salirAplicacion();
            }
        });
    }
    
    private void inicializarAplicacion() {
        UsuarioControlador.ResultadoOperacion resultado = agendaControlador.inicializarAplicacion();
        
        if (resultado.isExitoso()) {
            actualizarEstado("Aplicación inicializada correctamente");
        } else {
            actualizarEstado("Error: " + resultado.getMensaje());
            JOptionPane.showMessageDialog(this, resultado.getMensaje(), "Error de inicialización", JOptionPane.ERROR_MESSAGE);
        }
        
        actualizarInterfaz();
    }
    
    private void realizarLogin(String usuario, String password) {
        System.out.println("DEBUG: Intentando login con usuario: " + usuario);
        
        if (usuario.trim().isEmpty() || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Usuario y contraseña son requeridos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            UsuarioControlador.ResultadoOperacion resultado = 
                    agendaControlador.getUsuarioControlador().iniciarSesion(usuario, password);
            
            System.out.println("DEBUG: Resultado del login: " + resultado.isExitoso());
            System.out.println("DEBUG: Mensaje: " + resultado.getMensaje());
            
            if (resultado.isExitoso()) {
                System.out.println("DEBUG: Login exitoso, mostrando dashboard");
                mostrarPanelDashboard();
                actualizarInterfaz();
                actualizarEstado("Sesión iniciada correctamente");
                System.out.println("DEBUG: Dashboard mostrado");
            } else {
                System.out.println("DEBUG: Login falló: " + resultado.getMensaje());
                JOptionPane.showMessageDialog(this, resultado.getMensaje(), "Error de login", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            System.out.println("DEBUG: Excepción durante login: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error inesperado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cerrarSesion() {
        int opcion = JOptionPane.showConfirmDialog(this, 
                "¿Está seguro que desea cerrar la sesión?", 
                "Confirmar cierre de sesión", 
                JOptionPane.YES_NO_OPTION);
        
        if (opcion == JOptionPane.YES_OPTION) {
            agendaControlador.getUsuarioControlador().cerrarSesion();
            mostrarPanelLogin();
            actualizarInterfaz();
            actualizarEstado("Sesión cerrada");
        }
    }
    
    private void mostrarPanelLogin() {
        CardLayout cl = (CardLayout) panelPrincipal.getLayout();
        cl.show(panelPrincipal, "LOGIN");
    }
    
    private void mostrarPanelDashboard() {
        CardLayout cl = (CardLayout) panelPrincipal.getLayout();
        cl.show(panelPrincipal, "DASHBOARD");
        
        // Actualizar información del dashboard cuando se muestra
        if (agendaControlador.getUsuarioControlador().hayUsuarioAutenticado()) {
            // Solo actualizar datos sin recrear el panel
            System.out.println("DEBUG: Mostrando dashboard para usuario autenticado");
        }
    }
    
    private void actualizarInterfaz() {
        boolean usuarioAutenticado = agendaControlador.getUsuarioControlador().hayUsuarioAutenticado();
        System.out.println("DEBUG: Actualizando interfaz. Usuario autenticado: " + usuarioAutenticado);
        
        if (usuarioAutenticado) {
            Usuario usuario = agendaControlador.getUsuarioControlador().getUsuarioActual();
            lblUsuario.setText("👤 " + usuario.getNombreParaMostrar());
            System.out.println("DEBUG: Mostrando dashboard para usuario: " + usuario.getNombreParaMostrar());
            mostrarPanelDashboard();
        } else {
            lblUsuario.setText("No autenticado");
            System.out.println("DEBUG: Mostrando panel login");
            mostrarPanelLogin();
        }
    }
    
    private void actualizarEstado(String mensaje) {
        lblEstado.setText(mensaje);
    }
    
    private void salirAplicacion() {
        int opcion = JOptionPane.showConfirmDialog(this, 
                "¿Está seguro que desea salir de la aplicación?", 
                "Confirmar salida", 
                JOptionPane.YES_NO_OPTION);
        
        if (opcion == JOptionPane.YES_OPTION) {
            agendaControlador.finalizarAplicacion();
            System.exit(0);
        }
    }
    
    /**
     * Método principal para ejecutar la aplicación
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Usando Look and Feel por defecto");
        }
        
        SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true);
        });
    }
}