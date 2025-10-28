package com.agenda.agendapersonal.vista;

import com.agenda.agendapersonal.controlador.AgendaControlador;
import com.agenda.agendapersonal.controlador.UsuarioControlador;
import com.agenda.agendapersonal.config.Constantes;
import com.agenda.agendapersonal.modelo.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
        setSize(800, 600);
        setMinimumSize(new Dimension(600, 400));
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
        
        // Panel superior con información
        JPanel panelInfo = new JPanel();
        panelInfo.setBorder(BorderFactory.createTitledBorder("Dashboard"));
        
        JTextArea txtInfo = new JTextArea(8, 50);
        txtInfo.setEditable(false);
        txtInfo.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        txtInfo.setText("Bienvenido a la Agenda Personal\n\nEsta aplicación te permite:\n• Gestionar eventos y citas\n• Organizar por categorías\n• Ver resúmenes y estadísticas\n\nUsa los botones de abajo para comenzar.");
        
        panelInfo.add(new JScrollPane(txtInfo));
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        
        JButton btnNuevoEvento = new JButton("📅 Nuevo Evento");
        JButton btnListarEventos = new JButton("📋 Ver Eventos");
        JButton btnCategorias = new JButton("📁 Categorías");
        JButton btnCerrarSesion = new JButton("🚪 Cerrar Sesión");
        
        panelBotones.add(btnNuevoEvento);
        panelBotones.add(btnListarEventos);
        panelBotones.add(btnCategorias);
        panelBotones.add(btnCerrarSesion);
        
        // Eventos de botones
        btnNuevoEvento.addActionListener(e -> mostrarMensaje("Nuevo Evento", "Funcionalidad por implementar"));
        btnListarEventos.addActionListener(e -> mostrarMensaje("Lista de Eventos", "Funcionalidad por implementar"));
        btnCategorias.addActionListener(e -> mostrarMensaje("Categorías", "Funcionalidad por implementar"));
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
        
        panelDashboard.add(panelInfo, BorderLayout.CENTER);
        panelDashboard.add(panelBotones, BorderLayout.SOUTH);
        
        panelPrincipal.add(panelDashboard, "DASHBOARD");
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
        if (usuario.trim().isEmpty() || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Usuario y contraseña son requeridos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        UsuarioControlador.ResultadoOperacion resultado = 
                agendaControlador.getUsuarioControlador().iniciarSesion(usuario, password);
        
        if (resultado.isExitoso()) {
            mostrarPanelDashboard();
            actualizarInterfaz();
            actualizarEstado("Sesión iniciada correctamente");
        } else {
            JOptionPane.showMessageDialog(this, resultado.getMensaje(), "Error de login", JOptionPane.ERROR_MESSAGE);
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
        
        // Actualizar información del dashboard
        if (agendaControlador.getUsuarioControlador().hayUsuarioAutenticado()) {
            Usuario user = agendaControlador.getUsuarioControlador().getUsuarioActual();
            String resumen = String.format("👋 ¡Hola %s!\n\n📊 Resumen de tu agenda:\n• Total eventos: %d\n• Eventos hoy: %d\n• Categorías: %d\n\n✨ ¡Listo para gestionar tu agenda!",
                    user.getNombreParaMostrar(),
                    agendaControlador.getEventoControlador().obtenerEventosUsuario().size(),
                    agendaControlador.getEventoControlador().obtenerEventosHoy().size(),
                    agendaControlador.getCategoriaControlador().obtenerCategoriasUsuario().size());
            
            // Buscar y actualizar el JTextArea
            Component[] components = ((JPanel) panelDashboard.getComponent(0)).getComponents();
            for (Component comp : components) {
                if (comp instanceof JScrollPane) {
                    JTextArea textArea = (JTextArea) ((JScrollPane) comp).getViewport().getView();
                    textArea.setText(resumen);
                    break;
                }
            }
        }
    }
    
    private void actualizarInterfaz() {
        boolean usuarioAutenticado = agendaControlador.getUsuarioControlador().hayUsuarioAutenticado();
        
        if (usuarioAutenticado) {
            Usuario usuario = agendaControlador.getUsuarioControlador().getUsuarioActual();
            lblUsuario.setText("👤 " + usuario.getNombreParaMostrar());
            mostrarPanelDashboard();
        } else {
            lblUsuario.setText("No autenticado");
            mostrarPanelLogin();
        }
    }
    
    private void actualizarEstado(String mensaje) {
        lblEstado.setText(mensaje);
    }
    
    private void mostrarMensaje(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
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