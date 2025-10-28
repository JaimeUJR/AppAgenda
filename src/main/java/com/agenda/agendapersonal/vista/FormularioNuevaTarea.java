package com.agenda.agendapersonal.vista;

import com.agenda.agendapersonal.controlador.AgendaControlador;
import com.agenda.agendapersonal.controlador.UsuarioControlador;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Calendar;

public class FormularioNuevaTarea extends JDialog {

    private AgendaControlador agendaControlador;
    private VentanaPrincipal ventanaPadre;

    // Componentes del formulario
    private JTextField txtTitulo;
    private JTextArea txtDescripcion;
    private JSpinner spnFechaInicio;
    private JSpinner spnFechaFin;
    private JTextField txtUbicacion;
    private JCheckBox chkRecordatorio;

    public FormularioNuevaTarea(VentanaPrincipal padre, AgendaControlador controlador) {
        super(padre, "Nueva Tarea", true);
        this.ventanaPadre = padre;
        this.agendaControlador = controlador;

        initComponents();
        configurarEventos();
    }

    private void initComponents() {
        setSize(650, 750); // Aumenté el tamaño para mejor visualización
        setLocationRelativeTo(ventanaPadre);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel principal con fondo gris claro
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(new Color(240, 242, 245));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Header del formulario
        JPanel headerPanel = crearHeader();

        // Panel del formulario
        JPanel formularioPanel = crearFormulario();

        // Panel de botones
        JPanel botonesPanel = crearPanelBotones();

        panelPrincipal.add(headerPanel, BorderLayout.NORTH);
        panelPrincipal.add(formularioPanel, BorderLayout.CENTER);
        panelPrincipal.add(botonesPanel, BorderLayout.SOUTH);

        // Agregar JScrollPane para permitir scroll en todo el formulario
        JScrollPane scrollPane = new JScrollPane(panelPrincipal);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Scroll más suave

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel crearHeader() {
        JPanel headerContainer = new JPanel(new BorderLayout());
        headerContainer.setBackground(new Color(240, 242, 245));
        headerContainer.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(new Color(101, 116, 205));
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(101, 116, 205), 10, true),
                BorderFactory.createEmptyBorder(25, 30, 25, 30)));

        JLabel titulo = new JLabel("📝 Nueva Tarea");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("Completa los campos para crear una nueva tarea");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(255, 255, 255, 200));
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(titulo);
        header.add(Box.createVerticalStrut(5));
        header.add(subtitulo);

        headerContainer.add(header, BorderLayout.CENTER);
        return headerContainer;
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 226, 230), 3, true),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)));

        // Título
        panel.add(crearCampo("Título:", txtTitulo = new JTextField()));
        panel.add(Box.createVerticalStrut(20));

        // Descripción
        txtDescripcion = new JTextArea(4, 0);
        txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDescripcion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 226, 230), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        scrollDesc.setBorder(BorderFactory.createLineBorder(new Color(222, 226, 230), 1));
        panel.add(crearCampoConComponente("Descripción:", scrollDesc));
        panel.add(Box.createVerticalStrut(20));

        // Fecha de inicio
        Calendar cal = Calendar.getInstance();
        spnFechaInicio = new JSpinner(new SpinnerDateModel(cal.getTime(), null, null, Calendar.MINUTE));
        spnFechaInicio.setEditor(new JSpinner.DateEditor(spnFechaInicio, "dd/MM/yyyy HH:mm"));
        spnFechaInicio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(crearCampoConComponente("Fecha de Inicio:", spnFechaInicio));
        panel.add(Box.createVerticalStrut(20));

        // Fecha de fin (una hora después)
        cal.add(Calendar.HOUR_OF_DAY, 1);
        spnFechaFin = new JSpinner(new SpinnerDateModel(cal.getTime(), null, null, Calendar.MINUTE));
        spnFechaFin.setEditor(new JSpinner.DateEditor(spnFechaFin, "dd/MM/yyyy HH:mm"));
        spnFechaFin.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(crearCampoConComponente("Fecha de Fin:", spnFechaFin));
        panel.add(Box.createVerticalStrut(20));

        // Ubicación
        panel.add(crearCampo("Ubicación:", txtUbicacion = new JTextField()));
        panel.add(Box.createVerticalStrut(20));

        // Recordatorio
        chkRecordatorio = new JCheckBox("Activar recordatorio");
        chkRecordatorio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chkRecordatorio.setBackground(Color.WHITE);
        chkRecordatorio.setForeground(new Color(50, 50, 50));
        panel.add(chkRecordatorio);

        return panel;
    }

    private JPanel crearCampo(String etiqueta, JTextField campo) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel(etiqueta);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(50, 50, 50));

        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 226, 230), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        panel.add(label, BorderLayout.NORTH);
        panel.add(campo, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearCampoConComponente(String etiqueta, JComponent componente) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel(etiqueta);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(50, 50, 50));

        panel.add(label, BorderLayout.NORTH);
        panel.add(componente, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panel.setBackground(new Color(240, 242, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Botón Cancelar
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnCancelar.setBackground(Color.WHITE);
        btnCancelar.setForeground(new Color(50, 50, 50));
        btnCancelar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 226, 230), 3, true),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)));
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.setFocusPainted(false);

        // Botón Guardar
        JButton btnGuardar = new JButton("Guardar Tarea");
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGuardar.setBackground(new Color(101, 116, 205));
        btnGuardar.setForeground(new Color(50, 50, 50));
        btnGuardar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(101, 116, 205), 3, true),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)));
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.setFocusPainted(false);

        // Efectos hover
        btnCancelar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCancelar.setBackground(new Color(248, 249, 250));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCancelar.setBackground(Color.WHITE);
            }
        });

        btnGuardar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnGuardar.setBackground(new Color(88, 101, 180));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnGuardar.setBackground(new Color(101, 116, 205));
            }
        });

        panel.add(btnCancelar);
        panel.add(btnGuardar);

        return panel;
    }

    private void configurarEventos() {
        // Obtener el panel principal desde el JScrollPane
        JScrollPane scrollPane = (JScrollPane) getContentPane().getComponent(0);
        JPanel panelPrincipal = (JPanel) scrollPane.getViewport().getView();
        JPanel botonesPanel = (JPanel) panelPrincipal.getComponent(2); // Panel de botones es el tercer componente

        // Buscar botones en el panel de botones
        Component[] components = botonesPanel.getComponents();

        for (Component comp : components) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                if (btn.getText().equals("Cancelar")) {
                    btn.addActionListener(e -> dispose());
                } else if (btn.getText().equals("Guardar Tarea")) {
                    btn.addActionListener(e -> guardarTarea());
                }
            }
        }
    }

    private void guardarTarea() {
        try {
            // Validar campos obligatorios
            if (txtTitulo.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El título es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Obtener fechas de los spinners
            Date fechaInicioDate = (Date) spnFechaInicio.getValue();
            Date fechaFinDate = (Date) spnFechaFin.getValue();

            // Convertir Date a LocalDateTime
            LocalDateTime fechaInicio = fechaInicioDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            LocalDateTime fechaFin = fechaFinDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            // Validar que la fecha de fin sea posterior a la de inicio
            if (fechaFin.isBefore(fechaInicio)) {
                JOptionPane.showMessageDialog(this,
                        "La fecha de fin debe ser posterior a la fecha de inicio",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Crear la tarea usando el controlador
            UsuarioControlador.ResultadoOperacion resultado = agendaControlador.getEventoControlador().crearEvento(
                    txtTitulo.getText().trim(),
                    txtDescripcion.getText().trim(),
                    fechaInicio,
                    fechaFin,
                    txtUbicacion.getText().trim(),
                    chkRecordatorio.isSelected(),
                    java.util.Arrays.asList() // Lista vacía de etiquetas por ahora
            );

            if (resultado.isExitoso()) {
                JOptionPane.showMessageDialog(this,
                        "Tarea creada exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);

                // Cerrar formulario y actualizar ventana principal
                dispose();

                // Actualizar la ventana principal
                if (ventanaPadre != null) {
                    ventanaPadre.actualizarDatos();
                }

            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al crear la tarea: " + resultado.getMensaje(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error inesperado: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}