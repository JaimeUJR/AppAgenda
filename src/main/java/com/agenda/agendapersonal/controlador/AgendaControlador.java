package com.agenda.agendapersonal.controlador;

import com.agenda.agendapersonal.config.ConexionBD;
import com.agenda.agendapersonal.modelo.Usuario;

/**
 * Controlador principal que coordina todos los demás controladores
 * Actúa como fachada para la aplicación
 * 
 * @author JaimeSQL
 */
public class AgendaControlador {
    
    private UsuarioControlador usuarioControlador;
    private EventoControlador eventoControlador;
    private CategoriaControlador categoriaControlador;
    private ConexionBD conexionBD;
    
    public AgendaControlador() {
        inicializarControladores();
        verificarConexionBD();
    }
    
    /**
     * Inicializar todos los controladores
     */
    private void inicializarControladores() {
        this.usuarioControlador = new UsuarioControlador();
        this.eventoControlador = new EventoControlador(usuarioControlador);
        this.categoriaControlador = new CategoriaControlador(usuarioControlador);
        this.conexionBD = ConexionBD.getInstancia();
    }
    
    /**
     * Verificar conexión a la base de datos
     */
    private void verificarConexionBD() {
        if (!conexionBD.verificarConexion()) {
            System.err.println("⚠️ ADVERTENCIA: No se pudo conectar a la base de datos");
            System.err.println("   Verifique la configuración en database.properties");
            System.err.println("   Información de conexión: " + conexionBD.getInformacionConexion());
        } else {
            System.out.println("✅ Conexión a base de datos establecida correctamente");
        }
    }
    
    /**
     * Inicializar la aplicación
     */
    public UsuarioControlador.ResultadoOperacion inicializarAplicacion() {
        try {
            // Verificar conexión a BD
            if (!conexionBD.verificarConexion()) {
                return new UsuarioControlador.ResultadoOperacion(false, 
                    "No se pudo conectar a la base de datos. Verifique la configuración.");
            }
            
            System.out.println("🚀 Aplicación Agenda Personal inicializada");
            System.out.println("📄 Versión: 1.0.0");
            System.out.println("👤 Autor: JaimeSQL");
            System.out.println("💾 Base de datos: Conectada");
            
            return new UsuarioControlador.ResultadoOperacion(true, "Aplicación inicializada correctamente");
            
        } catch (Exception e) {
            return new UsuarioControlador.ResultadoOperacion(false, 
                "Error al inicializar la aplicación: " + e.getMessage());
        }
    }
    
    /**
     * Finalizar la aplicación
     */
    public void finalizarAplicacion() {
        try {
            // Cerrar sesión si hay usuario autenticado
            if (usuarioControlador.hayUsuarioAutenticado()) {
                Usuario usuario = usuarioControlador.getUsuarioActual();
                usuarioControlador.cerrarSesion();
                System.out.println("👋 Sesión cerrada para: " + usuario.getNombreParaMostrar());
            }
            
            System.out.println("🛑 Aplicación finalizada correctamente");
            
        } catch (Exception e) {
            System.err.println("Error al finalizar aplicación: " + e.getMessage());
        }
    }
    
    /**
     * Obtener información del estado de la aplicación
     */
    public String obtenerEstadoAplicacion() {
        StringBuilder estado = new StringBuilder();
        
        estado.append("=== ESTADO DE LA APLICACIÓN ===\n");
        estado.append("📱 Aplicación: Agenda Personal v1.0.0\n");
        estado.append("💾 Base de datos: ");
        estado.append(conexionBD.verificarConexion() ? "✅ Conectada" : "❌ Desconectada");
        estado.append("\n");
        
        if (usuarioControlador.hayUsuarioAutenticado()) {
            Usuario usuario = usuarioControlador.getUsuarioActual();
            estado.append("👤 Usuario: ").append(usuario.getNombreParaMostrar());
            estado.append(" (").append(usuario.getNombreUsuario()).append(")\n");
            
            // Estadísticas rápidas
            int totalEventos = eventoControlador.obtenerEventosUsuario().size();
            int totalCategorias = categoriaControlador.obtenerCategoriasUsuario().size();
            int eventosHoy = eventoControlador.obtenerEventosHoy().size();
            
            estado.append("📊 Estadísticas:\n");
            estado.append("   • Eventos totales: ").append(totalEventos).append("\n");
            estado.append("   • Categorías: ").append(totalCategorias).append("\n");
            estado.append("   • Eventos hoy: ").append(eventosHoy).append("\n");
        } else {
            estado.append("👤 Usuario: No autenticado\n");
        }
        
        estado.append("🔗 Conexión BD: ").append(conexionBD.getInformacionConexion()).append("\n");
        estado.append("===============================");
        
        return estado.toString();
    }
    
    /**
     * Obtener resumen rápido para dashboard
     */
    public String obtenerResumenDashboard() {
        if (!usuarioControlador.hayUsuarioAutenticado()) {
            return "No hay usuario autenticado";
        }
        
        Usuario usuario = usuarioControlador.getUsuarioActual();
        int eventosHoy = eventoControlador.obtenerEventosHoy().size();
        int eventosProximos = eventoControlador.obtenerEventosProximos().size();
        int totalCategorias = categoriaControlador.obtenerCategoriasUsuario().size();
        
        return String.format("👋 ¡Hola %s!\n📅 Eventos hoy: %d\n⏰ Próximos 7 días: %d\n📁 Categorías: %d",
                           usuario.getNombreParaMostrar(), eventosHoy, eventosProximos, totalCategorias);
    }
    
    /**
     * Verificar si la aplicación está lista para usar
     */
    public boolean estaListaParaUsar() {
        return conexionBD.verificarConexion();
    }
    
    /**
     * Realizar diagnóstico de la aplicación
     */
    public String realizarDiagnostico() {
        StringBuilder diagnostico = new StringBuilder();
        
        diagnostico.append("=== DIAGNÓSTICO DE LA APLICACIÓN ===\n\n");
        
        // Verificar conexión BD
        diagnostico.append("🔍 Verificando conexión a base de datos...\n");
        boolean conexionOK = conexionBD.verificarConexion();
        diagnostico.append("   Resultado: ").append(conexionOK ? "✅ OK" : "❌ ERROR").append("\n");
        if (!conexionOK) {
            diagnostico.append("   ⚠️ Verifique:\n");
            diagnostico.append("      - MySQL está ejecutándose\n");
            diagnostico.append("      - Base de datos 'agenda_personal' existe\n");
            diagnostico.append("      - Credenciales en database.properties\n");
        }
        diagnostico.append("\n");
        
        // Verificar controladores
        diagnostico.append("🔍 Verificando controladores...\n");
        diagnostico.append("   UsuarioControlador: ").append(usuarioControlador != null ? "✅ OK" : "❌ ERROR").append("\n");
        diagnostico.append("   EventoControlador: ").append(eventoControlador != null ? "✅ OK" : "❌ ERROR").append("\n");
        diagnostico.append("   CategoriaControlador: ").append(categoriaControlador != null ? "✅ OK" : "❌ ERROR").append("\n");
        diagnostico.append("\n");
        
        // Estado de sesión
        diagnostico.append("🔍 Verificando sesión de usuario...\n");
        if (usuarioControlador.hayUsuarioAutenticado()) {
            Usuario usuario = usuarioControlador.getUsuarioActual();
            diagnostico.append("   Usuario autenticado: ✅ ").append(usuario.getNombreParaMostrar()).append("\n");
        } else {
            diagnostico.append("   Usuario autenticado: ❌ No hay sesión activa\n");
        }
        diagnostico.append("\n");
        
        // Resumen final
        boolean todoBien = conexionOK && usuarioControlador != null && eventoControlador != null && categoriaControlador != null;
        diagnostico.append("=== RESUMEN ===\n");
        diagnostico.append("Estado general: ").append(todoBien ? "✅ TODO OK" : "⚠️ HAY PROBLEMAS").append("\n");
        
        if (!todoBien) {
            diagnostico.append("\n💡 RECOMENDACIONES:\n");
            if (!conexionOK) {
                diagnostico.append("   • Verificar configuración de base de datos\n");
                diagnostico.append("   • Asegurar que MySQL esté ejecutándose\n");
                diagnostico.append("   • Revisar archivo database.properties\n");
            }
        }
        
        diagnostico.append("=====================================");
        
        return diagnostico.toString();
    }
    
    // Getters para acceso a los controladores
    public UsuarioControlador getUsuarioControlador() {
        return usuarioControlador;
    }
    
    public EventoControlador getEventoControlador() {
        return eventoControlador;
    }
    
    public CategoriaControlador getCategoriaControlador() {
        return categoriaControlador;
    }
    
    public ConexionBD getConexionBD() {
        return conexionBD;
    }
}