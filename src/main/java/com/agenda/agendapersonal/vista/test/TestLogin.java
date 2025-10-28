package com.agenda.agendapersonal.vista.test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Prueba simple del login sin dependencias externas
 * 
 * @author JaimeSQL
 */
public class TestLogin extends JFrame {
    
    private JPanel panelPrincipal;
    private JPanel panelLogin;
    private JPanel panelDashboard;
    
    public TestLogin() {
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Test Login - Agenda Personal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Panel principal con CardLayout
        panelPrincipal = new JPanel(new CardLayout());
        
        crearPanelLogin();
        crearPanelDashboard();
        
        add(panelPrincipal, BorderLayout.CENTER);
        
        // Mostrar login inicialmente
        mostrarPanelLogin();
    }
    
    private void crearPanelLogin() {
        panelLogin = new JPanel(new GridBagLayout());
        panelLogin.setBackground(new Color(248, 249, 250));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Título
        JLabel lblTitulo = new JLabel("🏠 Agenda Personal - TEST");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Campos
        JLabel lblUsuario = new JLabel("Usuario:");
        JTextField txtUsuario = new JTextField(20);
        txtUsuario.setText("admin"); // Prellenar para prueba
        
        JLabel lblPassword = new JLabel("Contraseña:");
        JPasswordField txtPassword = new JPasswordField(20);
        txtPassword.setText("123"); // Prellenar para prueba
        
        // Botones
        JButton btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setBackground(new Color(101, 116, 205));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JButton btnTest = new JButton("Login Directo (Test)");
        btnTest.setBackground(new Color(40, 167, 69));
        btnTest.setForeground(Color.WHITE);
        btnTest.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnTest.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
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
        
        btnTest.addActionListener(e -> realizarLoginTest());
        
        panelPrincipal.add(panelLogin, "LOGIN");
    }
    
    private void crearPanelDashboard() {
        panelDashboard = new JPanel(new BorderLayout());
        panelDashboard.setBackground(new Color(248, 249, 250));
        
        // Header moderno
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(new Color(101, 116, 205));
        header.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        JLabel lblTitulo = new JLabel("📋 Gestor de Tareas - TEST");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblSubtitulo = new JLabel("¡Login exitoso! Dashboard cargado correctamente");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSubtitulo.setForeground(new Color(255, 255, 255, 200));
        lblSubtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        header.add(lblTitulo);
        header.add(Box.createVerticalStrut(8));
        header.add(lblSubtitulo);
        
        // Panel central con mensaje de éxito
        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(Color.WHITE);
        centro.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        JLabel lblExito = new JLabel("<html><div style='text-align: center'>" +
            "<h1>✅ ¡LOGIN EXITOSO!</h1>" +
            "<p>El sistema de autenticación está funcionando correctamente.</p>" +
            "<p>Dashboard cargado sin problemas.</p>" +
            "</div></html>");
        lblExito.setHorizontalAlignment(SwingConstants.CENTER);
        lblExito.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        // Botón de cerrar sesión
        JButton btnCerrar = new JButton("🚪 Cerrar Sesión");
        btnCerrar.setBackground(new Color(220, 53, 69));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCerrar.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        btnCerrar.addActionListener(e -> mostrarPanelLogin());
        
        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(btnCerrar);
        
        centro.add(lblExito, BorderLayout.CENTER);
        centro.add(btnPanel, BorderLayout.SOUTH);
        
        panelDashboard.add(header, BorderLayout.NORTH);
        panelDashboard.add(centro, BorderLayout.CENTER);
        
        panelPrincipal.add(panelDashboard, "DASHBOARD");
    }
    
    private void realizarLogin(String usuario, String password) {
        System.out.println("TEST: Intentando login con usuario: " + usuario);
        
        if (usuario.trim().isEmpty() || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Usuario y contraseña son requeridos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Simulación de login exitoso para cualquier usuario/contraseña
        if (usuario.length() > 0 && password.length() > 0) {
            System.out.println("TEST: Login exitoso - mostrando dashboard");
            mostrarPanelDashboard();
            JOptionPane.showMessageDialog(this, "¡Login exitoso!\\nUsuario: " + usuario, "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            System.out.println("TEST: Login falló");
            JOptionPane.showMessageDialog(this, "Login falló", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void realizarLoginTest() {
        System.out.println("TEST: Login directo activado");
        mostrarPanelDashboard();
        JOptionPane.showMessageDialog(this, "¡Dashboard cargado directamente!", "Test Exitoso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void mostrarPanelLogin() {
        System.out.println("TEST: Mostrando panel de login");
        CardLayout cl = (CardLayout) panelPrincipal.getLayout();
        cl.show(panelPrincipal, "LOGIN");
    }
    
    private void mostrarPanelDashboard() {
        System.out.println("TEST: Mostrando panel dashboard");
        CardLayout cl = (CardLayout) panelPrincipal.getLayout();
        cl.show(panelPrincipal, "DASHBOARD");
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Usando Look and Feel por defecto");
        }
        
        SwingUtilities.invokeLater(() -> {
            new TestLogin().setVisible(true);
        });
    }
}