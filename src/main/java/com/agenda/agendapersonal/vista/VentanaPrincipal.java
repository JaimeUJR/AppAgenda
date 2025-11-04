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

public class VentanaPrincipal extends JFrame {

    private AgendaControlador agendaControlador;
    private JPanel panelPrincipal;
    private JLabel lblEstado;
    private JLabel lblUsuario;

    private JPanel panelLogin;
    private JPanel panelDashboard;
    
    private JTable tablaTareas;
    private DefaultTableModel modeloTabla;
    
    private JButton btnTodas;
    private JButton btnPendientes;
    private JButton btnCompletadas;
    private FiltroTarea filtroActual = FiltroTarea.TODAS;
    
    // Campos de login para acceso global
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnCerrarSesion;
    
    public enum FiltroTarea {
        TODAS, PENDIENTES, COMPLETADAS
    }

    public VentanaPrincipal() {
        this.agendaControlador = new AgendaControlador();
        initComponents();
        inicializarAplicacion();
    }

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

        // Panel derecho con usuario y botón cerrar sesión
        JPanel panelDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        panelDerecho.add(lblUsuario);

        // Crear botón cerrar sesión (inicialmente oculto)
        btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnCerrarSesion.setForeground(new Color(51, 51, 51));
        btnCerrarSesion.setBackground(new Color(220, 53, 69));
        btnCerrarSesion.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.setVisible(false); // Oculto inicialmente
        
        // Efecto hover para el botón
        btnCerrarSesion.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCerrarSesion.setBackground(new Color(200, 35, 51));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCerrarSesion.setBackground(new Color(220, 53, 69));
            }
        });
        
        // Acción del botón cerrar sesión
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
        
        panelDerecho.add(btnCerrarSesion);

        panelEstado.add(lblEstado, BorderLayout.WEST);
        panelEstado.add(panelDerecho, BorderLayout.EAST);

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
        txtUsuario = new JTextField(20);

        JLabel lblPassword = new JLabel("Contraseña:");
        txtPassword = new JPasswordField(20);

        // Botones
        JButton btnLogin = new JButton("Iniciar Sesión");
        JButton btnTest = new JButton("Usuario de Prueba");
        JButton btnCrearCuenta = new JButton("Crear Cuenta");

        // Layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelLogin.add(lblTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelLogin.add(lblUsuario, gbc);
        gbc.gridx = 1;
        panelLogin.add(txtUsuario, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panelLogin.add(lblPassword, gbc);
        gbc.gridx = 1;
        panelLogin.add(txtPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelLogin.add(btnLogin, gbc);

        gbc.gridy = 4;
        panelLogin.add(btnTest, gbc);

        gbc.gridy = 5;
        panelLogin.add(btnCrearCuenta, gbc);

        btnLogin.addActionListener(e -> {
            String usuario = txtUsuario.getText();
            String password = new String(txtPassword.getPassword());
            realizarLogin(usuario, password);
        });

        btnTest.addActionListener(e -> realizarLogin("jperez", "password"));

        btnCrearCuenta.addActionListener(e -> {
            FormularioRegistro formulario = new FormularioRegistro(this);
            formulario.setVisible(true);
        });

        txtPassword.addActionListener(e -> {
            String usuario = txtUsuario.getText();
            String password = new String(txtPassword.getPassword());
            realizarLogin(usuario, password);
        });

        panelPrincipal.add(panelLogin, "LOGIN");
    }

    private void crearPanelDashboard() {
        panelDashboard = new JPanel(new BorderLayout());
        panelDashboard.setBackground(new Color(240, 242, 245));

        JPanel headerPanel = crearHeaderModerno();
        JPanel estadisticasPanel = crearPanelEstadisticas();
        JPanel filtrosPanel = crearPanelFiltros();
        JPanel tablaPanel = crearPanelTabla();

        JPanel centralPanel = new JPanel(new BorderLayout(0, 25));
        centralPanel.setBackground(new Color(240, 242, 245));
        centralPanel.setBorder(BorderFactory.createEmptyBorder(25, 35, 35, 35));

        centralPanel.add(estadisticasPanel, BorderLayout.NORTH);

        JPanel filtrosYTabla = new JPanel(new BorderLayout(0, 20));
        filtrosYTabla.setBackground(new Color(240, 242, 245));
        filtrosYTabla.add(filtrosPanel, BorderLayout.NORTH);
        filtrosYTabla.add(tablaPanel, BorderLayout.CENTER);

        centralPanel.add(filtrosYTabla, BorderLayout.CENTER);

        panelDashboard.add(headerPanel, BorderLayout.NORTH);
        panelDashboard.add(centralPanel, BorderLayout.CENTER);

        panelPrincipal.add(panelDashboard, "DASHBOARD");
    }

    private JPanel crearHeaderModerno() {
        JPanel headerContainer = new JPanel(new BorderLayout());
        headerContainer.setBackground(new Color(240, 242, 245));
        headerContainer.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        // Panel principal del header
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(new Color(101, 116, 205));
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(101, 116, 205), 15, true),
                BorderFactory.createEmptyBorder(30, 35, 30, 35)));

        JLabel lblTitulo = new JLabel("📋 Gestor de Tareas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

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
        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 0));
        panel.setBackground(new Color(240, 242, 245));

        List<Evento> eventos = agendaControlador.getEventoControlador().obtenerEventosUsuario();
        int totalTareas = eventos.size();
        int pendientes = (int) eventos.stream().filter(e -> e.getEstado() == EstadoEvento.PENDIENTE).count();
        int completadas = (int) eventos.stream().filter(e -> e.getEstado() == EstadoEvento.COMPLETADO).count();

        panel.add(crearTarjetaEstadistica("TOTAL TAREAS", String.valueOf(totalTareas), new Color(52, 144, 220), "📊"));
        panel.add(crearTarjetaEstadistica("PENDIENTES", String.valueOf(pendientes), new Color(255, 159, 67), "⏳"));
        panel.add(crearTarjetaEstadistica("COMPLETADAS", String.valueOf(completadas), new Color(95, 195, 134), "✅"));

        return panel;
    }

    private JPanel crearTarjetaEstadistica(String titulo, String valor, Color colorBorde, String icono) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(colorBorde);

        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorBorde, 3, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 40));
        lblValor.setForeground(Color.WHITE);
        lblValor.setAlignmentX(Component.LEFT_ALIGNMENT);

        tarjeta.add(lblTitulo);
        tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(lblValor);

        return tarjeta;
    }

    private JPanel crearPanelFiltros() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 242, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel filtrosIzq = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        filtrosIzq.setBackground(new Color(240, 242, 245));

        btnTodas = crearBotonFiltro("Todas", true);
        btnPendientes = crearBotonFiltro("Pendientes", false);
        btnCompletadas = crearBotonFiltro("Completadas", false);
        
        btnTodas.addActionListener(e -> aplicarFiltro(FiltroTarea.TODAS));
        btnPendientes.addActionListener(e -> aplicarFiltro(FiltroTarea.PENDIENTES));
        btnCompletadas.addActionListener(e -> aplicarFiltro(FiltroTarea.COMPLETADAS));

        filtrosIzq.add(btnTodas);
        filtrosIzq.add(Box.createHorizontalStrut(8));
        filtrosIzq.add(btnPendientes);
        filtrosIzq.add(Box.createHorizontalStrut(8));
        filtrosIzq.add(btnCompletadas);

        JPanel accionesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        accionesPanel.setBackground(new Color(240, 242, 245));

        JButton btnNuevaTarea = new JButton("+ Nueva Tarea");
        btnNuevaTarea.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnNuevaTarea.setBackground(new Color(101, 116, 205));
        btnNuevaTarea.setForeground(new Color(50, 50, 50));
        btnNuevaTarea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(101, 116, 205), 3, true),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)));
        btnNuevaTarea.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNuevaTarea.setFocusPainted(false);

        btnNuevaTarea.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnNuevaTarea.setBackground(new Color(88, 101, 180));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnNuevaTarea.setBackground(new Color(101, 116, 205));
            }
        });

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
                    BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        } else {
            boton.setBackground(Color.WHITE);
            boton.setForeground(new Color(50, 50, 50)); // Gris muy oscuro
            boton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(222, 226, 230), 3, true), // Borde más delgado
                    BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        }

        if (!activo) {
            boton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    boton.setBackground(new Color(248, 249, 250));
                    boton.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(101, 116, 205), 3, true),
                            BorderFactory.createEmptyBorder(10, 20, 10, 20)));
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    boton.setBackground(Color.WHITE);
                    boton.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(222, 226, 230), 3, true),
                            BorderFactory.createEmptyBorder(10, 20, 10, 20)));
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
                BorderFactory.createLineBorder(new Color(222, 226, 230), 3, true),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));

        String[] columnas = {"ID", "ESTADO", "TÍTULO", "DESCRIPCIÓN", "FECHA INICIO"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaTareas = new JTable(modeloTabla);
        tablaTareas.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablaTareas.setRowHeight(60);
        tablaTareas.setGridColor(new Color(233, 236, 239));
        tablaTareas.setSelectionBackground(new Color(232, 240, 254));
        tablaTareas.setSelectionForeground(new Color(33, 37, 41));
        tablaTareas.setBackground(new Color(248, 249, 250));
        tablaTareas.setForeground(new Color(33, 37, 41));
        tablaTareas.setShowVerticalLines(true);
        tablaTareas.setShowHorizontalLines(true);
        tablaTareas.setIntercellSpacing(new Dimension(0, 1));

        tablaTareas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablaTareas.getTableHeader().setBackground(new Color(101, 116, 205));
        tablaTareas.getTableHeader().setForeground(new Color(50, 50, 50));
        tablaTareas.getTableHeader().setBorder(BorderFactory.createEmptyBorder(18, 15, 18, 15));
        tablaTareas.getTableHeader().setReorderingAllowed(false);
        tablaTareas.getTableHeader().setOpaque(true);

        tablaTareas.getColumnModel().getColumn(0).setPreferredWidth(40);
        tablaTareas.getColumnModel().getColumn(1).setPreferredWidth(80);
        tablaTareas.getColumnModel().getColumn(2).setPreferredWidth(210);
        tablaTareas.getColumnModel().getColumn(3).setPreferredWidth(310);
        tablaTareas.getColumnModel().getColumn(4).setPreferredWidth(180);
        
        cargarDatosTabla(modeloTabla);

        tablaTareas.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int fila = tablaTareas.getSelectedRow();
                    if (fila >= 0) {
                        toggleEstadoTarea(fila);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaTareas);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        panel.add(scrollPane, BorderLayout.CENTER);
        panelContainer.add(panel, BorderLayout.CENTER);

        return panelContainer;
    }

    private void cargarDatosTabla(DefaultTableModel modelo) {
        cargarDatosFiltrados();
    }

    private void toggleEstadoTarea(int fila) {
        try {
            Object idObj = modeloTabla.getValueAt(fila, 0);
            if (idObj == null) {
                return;
            }
            
            int idEvento = (Integer) idObj;
            
            Object estadoObj = modeloTabla.getValueAt(fila, 1);
            EstadoEvento estadoActual = (EstadoEvento) estadoObj;
            
            EstadoEvento nuevoEstado;
            if (estadoActual == EstadoEvento.COMPLETADO) {
                nuevoEstado = EstadoEvento.PENDIENTE;
            } else {
                nuevoEstado = EstadoEvento.COMPLETADO;
            }
            
            UsuarioControlador.ResultadoOperacion resultado = 
                agendaControlador.getEventoControlador().cambiarEstadoEvento(idEvento, nuevoEstado);
            
            if (resultado.isExitoso()) {
                modeloTabla.setValueAt(nuevoEstado, fila, 1);
                
                String mensaje = nuevoEstado == EstadoEvento.COMPLETADO ? 
                    "✅ Tarea marcada como completada" : 
                    "⏳ Tarea marcada como pendiente";
                
                lblEstado.setText(mensaje);
                lblEstado.setForeground(nuevoEstado == EstadoEvento.COMPLETADO ? 
                    new Color(40, 167, 69) : new Color(255, 193, 7));
                
                Timer timer = new Timer(3000, e -> {
                    lblEstado.setText("Sistema listo");
                    lblEstado.setForeground(new Color(108, 117, 125));
                });
                timer.setRepeats(false);
                timer.start();
                
                SwingUtilities.invokeLater(() -> cargarDatosFiltrados());
                
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error al actualizar el estado de la tarea: " + resultado.getMensaje(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error inesperado: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirFormularioNuevaTarea() {
        FormularioNuevaTarea formulario = new FormularioNuevaTarea(this, agendaControlador);
        formulario.setVisible(true);
    }

    public void actualizarDatos() {
        if (agendaControlador.getUsuarioControlador().hayUsuarioAutenticado()) {
            SwingUtilities.invokeLater(() -> {
                crearPanelDashboard();
                mostrarPanelDashboard();
                
                SwingUtilities.invokeLater(() -> {
                    actualizarEstadoFiltros();
                    cargarDatosFiltrados();
                });
            });
        }
    }

    private void aplicarFiltro(FiltroTarea filtro) {
        this.filtroActual = filtro;
        
        actualizarEstadoFiltros();
        cargarDatosFiltrados();
        
        String mensaje = switch (filtro) {
            case TODAS -> "📋 Mostrando todas las tareas";
            case PENDIENTES -> "⏳ Mostrando tareas pendientes";
            case COMPLETADAS -> "✅ Mostrando tareas completadas";
        };
        
        lblEstado.setText(mensaje);
        lblEstado.setForeground(new Color(108, 117, 125));
        
        Timer timer = new Timer(2000, e -> {
            lblEstado.setText("Sistema listo");
            lblEstado.setForeground(new Color(108, 117, 125));
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    private void actualizarEstadoFiltros() {
        configurarBotonFiltro(btnTodas, false);
        configurarBotonFiltro(btnPendientes, false);
        configurarBotonFiltro(btnCompletadas, false);
        
        switch (filtroActual) {
            case TODAS -> configurarBotonFiltro(btnTodas, true);
            case PENDIENTES -> configurarBotonFiltro(btnPendientes, true);
            case COMPLETADAS -> configurarBotonFiltro(btnCompletadas, true);
        }
    }
    
    private void configurarBotonFiltro(JButton boton, boolean activo) {
        if (activo) {
            boton.setBackground(new Color(101, 116, 205));
            boton.setForeground(new Color(50, 50, 50));
            boton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(101, 116, 205), 3, true),
                    BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        } else {
            boton.setBackground(Color.WHITE);
            boton.setForeground(new Color(50, 50, 50));
            boton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(222, 226, 230), 3, true),
                    BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        }
    }
    
    private void cargarDatosFiltrados() {
        modeloTabla.setRowCount(0);

        List<Evento> eventos = agendaControlador.getEventoControlador().obtenerEventosUsuario();

        List<Evento> eventosFiltrados = eventos.stream()
            .filter(evento -> {
                return switch (filtroActual) {
                    case TODAS -> true;
                    case PENDIENTES -> evento.getEstado() == EstadoEvento.PENDIENTE;
                    case COMPLETADAS -> evento.getEstado() == EstadoEvento.COMPLETADO;
                };
            })
            .toList();

        for (Evento evento : eventosFiltrados) {
            Object[] fila = {
                evento.getIdEvento(),
                evento.getEstado(),
                evento.getTitulo(),
                evento.getDescripcion() != null ? evento.getDescripcion() : "",
                evento.getFechaInicioFormateada()
            };
            modeloTabla.addRow(fila);
        }
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
            JOptionPane.showMessageDialog(this, resultado.getMensaje(), "Error de inicialización",
                    JOptionPane.ERROR_MESSAGE);
        }

        actualizarInterfaz();
    }

    private void realizarLogin(String usuario, String password) {
        System.out.println("DEBUG: Intentando login con usuario: " + usuario);

        if (usuario.trim().isEmpty() || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Usuario y contraseña son requeridos", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            UsuarioControlador.ResultadoOperacion resultado = agendaControlador.getUsuarioControlador()
                    .iniciarSesion(usuario, password);

            System.out.println("DEBUG: Resultado del login: " + resultado.isExitoso());
            System.out.println("DEBUG: Mensaje: " + resultado.getMensaje());

            if (resultado.isExitoso()) {
                System.out.println("DEBUG: Login exitoso, mostrando dashboard");
                mostrarPanelDashboard();
                actualizarInterfaz();
                actualizarDatos();
                actualizarEstado("Sesión iniciada correctamente");
                System.out.println("DEBUG: Dashboard mostrado");
            } else {
                System.out.println("DEBUG: Login falló: " + resultado.getMensaje());
                JOptionPane.showMessageDialog(this, resultado.getMensaje(), "Error de login",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            System.out.println("DEBUG: Excepción durante login: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error inesperado: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
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
            btnCerrarSesion.setVisible(true); // Mostrar botón cuando esté autenticado
            System.out.println("DEBUG: Mostrando dashboard para usuario: " + usuario.getNombreParaMostrar());
            mostrarPanelDashboard();
        } else {
            lblUsuario.setText("No autenticado");
            btnCerrarSesion.setVisible(false); // Ocultar botón cuando no esté autenticado
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

    private void cerrarSesion() {
        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de que deseas cerrar sesión?",
                "Confirmar Cierre de Sesión",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            // Cerrar sesión en el controlador
            agendaControlador.getUsuarioControlador().cerrarSesion();
            
            // Limpiar campos de login
            txtUsuario.setText("");
            txtPassword.setText("");
            
            // Volver al panel de login
            mostrarPanelLogin();
            
            // Enfocar el campo de usuario
            SwingUtilities.invokeLater(() -> txtUsuario.requestFocus());
            
            JOptionPane.showMessageDialog(
                    this,
                    "Sesión cerrada exitosamente",
                    "Sesión Cerrada",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

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