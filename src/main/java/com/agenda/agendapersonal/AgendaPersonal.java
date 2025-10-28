package com.agenda.agendapersonal;

import com.agenda.agendapersonal.vista.VentanaPrincipal;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class AgendaPersonal {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Usando Look and Feel por defecto: " + e.getMessage());
        }
        
        System.out.println("=== AGENDA PERSONAL ===");
        System.out.println("Versión: 1.0.0");
        System.out.println("Iniciando aplicación...");
        
        SwingUtilities.invokeLater(() -> {
            try {
                VentanaPrincipal ventana = new VentanaPrincipal();
                ventana.setVisible(true);
                System.out.println("✅ Interfaz gráfica iniciada correctamente");
            } catch (Exception e) {
                System.err.println("❌ Error al iniciar la interfaz gráfica: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
