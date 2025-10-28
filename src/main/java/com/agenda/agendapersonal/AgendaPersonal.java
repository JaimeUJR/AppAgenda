/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.agenda.agendapersonal;

import com.agenda.agendapersonal.vista.VentanaPrincipal;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Clase principal de la aplicación Agenda Personal
 * 
 * @author JaimeSQL
 */
public class AgendaPersonal {

    public static void main(String[] args) {
        // Configurar Look and Feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Usando Look and Feel por defecto: " + e.getMessage());
        }
        
        // Mostrar información de inicio
        System.out.println("=== AGENDA PERSONAL ===");
        System.out.println("Versión: 1.0.0");
        System.out.println("Autor: JaimeSQL");
        System.out.println("Iniciando aplicación...");
        
        // Ejecutar la aplicación en el Event Dispatch Thread
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
