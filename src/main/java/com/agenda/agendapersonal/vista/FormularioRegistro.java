package com.agenda.agendapersonal.vista;

import com.agenda.agendapersonal.controlador.UsuarioControlador;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormularioRegistro extends JDialog {
    
    private VentanaPrincipal ventanaPadre;
    private UsuarioControlador usuarioControlador;
    
    private JTextField txtNombreUsuario;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmarPassword;
    private JTextField txtNombreCompleto;
    private JButton btnRegistrar;
    private JButton btnCancelar;
    private JLabel lblMensaje;
    
    public FormularioRegistro(VentanaPrincipal padre) {
        super(padre, "Crear Nueva Cuenta", true);
        this.ventanaPadre = padre;
        this.usuarioControlador = new UsuarioControlador();
        
        initComponents();
        configurarVentana();
        configurarEventos();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Panel principal con padding
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel lblTitulo = new JLabel("🆕 Crear Nueva Cuenta");
        lblTitulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setForeground(new Color(102, 51, 153)); // Morado
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);
        
        // Panel del formulario
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Nombre de Usuario
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("Nombre de Usuario:"), gbc);
        
        gbc.gridx = 1;
        txtNombreUsuario = new JTextField(20);
        txtNombreUsuario.setToolTipText("Ingrese un nombre de usuario único");
        panelFormulario.add(txtNombreUsuario, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(new JLabel("Correo Electrónico:"), gbc);
        
        gbc.gridx = 1;
        txtEmail = new JTextField(20);
        txtEmail.setToolTipText("Ingrese su correo electrónico");
        panelFormulario.add(txtEmail, gbc);
        
        // Nombre Completo
        gbc.gridx = 0; gbc.gridy = 2;
        panelFormulario.add(new JLabel("Nombre Completo:"), gbc);
        
        gbc.gridx = 1;
        txtNombreCompleto = new JTextField(20);
        txtNombreCompleto.setToolTipText("Ingrese su nombre completo");
        panelFormulario.add(txtNombreCompleto, gbc);
        
        // Contraseña
        gbc.gridx = 0; gbc.gridy = 3;
        panelFormulario.add(new JLabel("Contraseña:"), gbc);
        
        gbc.gridx = 1;
        txtPassword = new JPasswordField(20);
        txtPassword.setToolTipText("Mínimo 4 caracteres");
        panelFormulario.add(txtPassword, gbc);
        
        // Confirmar Contraseña
        gbc.gridx = 0; gbc.gridy = 4;
        panelFormulario.add(new JLabel("Confirmar Contraseña:"), gbc);
        
        gbc.gridx = 1;
        txtConfirmarPassword = new JPasswordField(20);
        txtConfirmarPassword.setToolTipText("Repita la contraseña");
        panelFormulario.add(txtConfirmarPassword, gbc);
        
        // Mensaje de estado
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        lblMensaje = new JLabel(" ");
        lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);
        lblMensaje.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        panelFormulario.add(lblMensaje, gbc);
        
        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        
        btnRegistrar = new JButton("✅ Registrar");
        btnRegistrar.setBackground(new Color(40, 167, 69));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setPreferredSize(new Dimension(120, 35));
        
        btnCancelar = new JButton("❌ Cancelar");
        btnCancelar.setBackground(new Color(220, 53, 69));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setPreferredSize(new Dimension(120, 35));
        
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnCancelar);
        
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        add(panelPrincipal);
    }
    
    private void configurarVentana() {
        setSize(450, 400);
        setResizable(false);
        setLocationRelativeTo(ventanaPadre);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    private void configurarEventos() {
        // Botón Registrar
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                procesarRegistro();
            }
        });
        
        // Botón Cancelar
        btnCancelar.addActionListener(e -> dispose());
        
        // Enter en campos de texto para registrar
        ActionListener registrarAction = e -> procesarRegistro();
        txtNombreUsuario.addActionListener(registrarAction);
        txtEmail.addActionListener(registrarAction);
        txtNombreCompleto.addActionListener(registrarAction);
        txtPassword.addActionListener(registrarAction);
        txtConfirmarPassword.addActionListener(registrarAction);
        
        // Escape para cerrar
        getRootPane().registerKeyboardAction(
            e -> dispose(),
            KeyStroke.getKeyStroke("ESCAPE"),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
        
        // Establecer botón por defecto
        getRootPane().setDefaultButton(btnRegistrar);
    }
    
    private void procesarRegistro() {
        // Limpiar mensaje anterior
        lblMensaje.setText(" ");
        lblMensaje.setForeground(Color.BLACK);
        
        // Obtener datos del formulario
        String nombreUsuario = txtNombreUsuario.getText().trim();
        String email = txtEmail.getText().trim();
        String nombreCompleto = txtNombreCompleto.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmarPassword = new String(txtConfirmarPassword.getPassword());
        
        // Validaciones del cliente
        if (nombreUsuario.isEmpty()) {
            mostrarError("El nombre de usuario es requerido");
            txtNombreUsuario.requestFocus();
            return;
        }
        
        if (email.isEmpty()) {
            mostrarError("El correo electrónico es requerido");
            txtEmail.requestFocus();
            return;
        }
        
        if (nombreCompleto.isEmpty()) {
            mostrarError("El nombre completo es requerido");
            txtNombreCompleto.requestFocus();
            return;
        }
        
        if (password.isEmpty()) {
            mostrarError("La contraseña es requerida");
            txtPassword.requestFocus();
            return;
        }
        
        if (password.length() < 4) {
            mostrarError("La contraseña debe tener al menos 4 caracteres");
            txtPassword.requestFocus();
            return;
        }
        
        if (!password.equals(confirmarPassword)) {
            mostrarError("Las contraseñas no coinciden");
            txtConfirmarPassword.requestFocus();
            return;
        }
        
        // Deshabilitar botones durante el proceso
        btnRegistrar.setEnabled(false);
        btnCancelar.setEnabled(false);
        lblMensaje.setText("⏳ Registrando usuario...");
        lblMensaje.setForeground(new Color(102, 51, 153));
        
        // Realizar registro en un hilo separado para no bloquear la UI
        SwingUtilities.invokeLater(() -> {
            try {
                UsuarioControlador.ResultadoOperacion resultado = 
                    usuarioControlador.registrarUsuario(nombreUsuario, email, password, nombreCompleto);
                
                if (resultado.isExitoso()) {
                    mostrarExito("✅ Usuario registrado exitosamente");
                    
                    // Mostrar mensaje de confirmación
                    JOptionPane.showMessageDialog(
                        this,
                        "¡Bienvenido! Tu cuenta ha sido creada exitosamente.\n" +
                        "Ya puedes iniciar sesión con tus credenciales.",
                        "Registro Exitoso",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    
                    // Cerrar formulario después de un breve retraso
                    Timer timer = new Timer(1500, e -> dispose());
                    timer.setRepeats(false);
                    timer.start();
                    
                } else {
                    mostrarError(resultado.getMensaje());
                    rehabilitarBotones();
                }
                
            } catch (Exception ex) {
                mostrarError("Error inesperado: " + ex.getMessage());
                rehabilitarBotones();
                ex.printStackTrace();
            }
        });
    }
    
    private void mostrarError(String mensaje) {
        lblMensaje.setText("❌ " + mensaje);
        lblMensaje.setForeground(new Color(220, 53, 69));
    }
    
    private void mostrarExito(String mensaje) {
        lblMensaje.setText(mensaje);
        lblMensaje.setForeground(new Color(40, 167, 69));
    }
    
    private void rehabilitarBotones() {
        btnRegistrar.setEnabled(true);
        btnCancelar.setEnabled(true);
    }
    
    public void limpiarFormulario() {
        txtNombreUsuario.setText("");
        txtEmail.setText("");
        txtNombreCompleto.setText("");
        txtPassword.setText("");
        txtConfirmarPassword.setText("");
        lblMensaje.setText(" ");
        rehabilitarBotones();
        txtNombreUsuario.requestFocus();
    }
    
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            SwingUtilities.invokeLater(() -> txtNombreUsuario.requestFocus());
        }
    }
}