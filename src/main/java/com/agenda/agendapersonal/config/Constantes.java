package com.agenda.agendapersonal.config;

/**
 * Clase de constantes para la aplicación de Agenda Personal
 * 
 * @author JaimeSQL
 */
public class Constantes {
    
    // Configuración de base de datos
    public static final String BD_NOMBRE = "agenda_personal";
    public static final String BD_HOST_DEFECTO = "localhost";
    public static final String BD_PUERTO_DEFECTO = "3306";
    public static final String BD_USUARIO_DEFECTO = "root";
    
    // Configuración de la aplicación
    public static final String APP_NOMBRE = "Agenda Personal";
    public static final String APP_VERSION = "1.0.0";
    public static final String APP_AUTOR = "JaimeSQL";
    
    // Configuración de UI
    public static final int VENTANA_ANCHO_DEFECTO = 800;
    public static final int VENTANA_ALTO_DEFECTO = 600;
    public static final int VENTANA_ANCHO_MINIMO = 600;
    public static final int VENTANA_ALTO_MINIMO = 400;
    
    // Formatos de fecha
    public static final String FORMATO_FECHA = "dd/MM/yyyy";
    public static final String FORMATO_FECHA_HORA = "dd/MM/yyyy HH:mm";
    public static final String FORMATO_HORA = "HH:mm";
    
    // Configuración de recordatorios
    public static final int RECORDATORIO_MINUTOS_DEFECTO = 15;
    public static final int RECORDATORIO_MAXIMO_DIAS = 30;
    
    // Límites de texto
    public static final int TITULO_MAX_LENGTH = 200;
    public static final int DESCRIPCION_MAX_LENGTH = 1000;
    public static final int UBICACION_MAX_LENGTH = 200;
    public static final int CATEGORIA_MAX_LENGTH = 50;
    public static final int USUARIO_MAX_LENGTH = 50;
    public static final int EMAIL_MAX_LENGTH = 100;
    public static final int NOMBRE_MAX_LENGTH = 100;
    
    // Mensajes de validación
    public static final String MSG_CAMPO_REQUERIDO = "Este campo es requerido";
    public static final String MSG_EMAIL_INVALIDO = "Email inválido";
    public static final String MSG_FECHA_INVALIDA = "Fecha inválida";
    public static final String MSG_FECHA_PASADA = "La fecha no puede ser en el pasado";
    public static final String MSG_FECHA_FIN_MENOR = "La fecha fin debe ser posterior a la fecha inicio";
    
    // Colores para estados (en formato hex)
    public static final String COLOR_PENDIENTE = "#FFA500"; // Naranja
    public static final String COLOR_COMPLETADO = "#32CD32"; // Verde
    public static final String COLOR_CANCELADO = "#FF6B6B"; // Rojo
    
    // Iconos unicode
    public static final String ICONO_RECORDATORIO_SI = "🔔";
    public static final String ICONO_RECORDATORIO_NO = "🔕";
    public static final String ICONO_PENDIENTE = "⏳";
    public static final String ICONO_COMPLETADO = "✅";
    public static final String ICONO_CANCELADO = "❌";
    public static final String ICONO_USUARIO = "👤";
    public static final String ICONO_CATEGORIA = "📁";
    public static final String ICONO_EVENTO = "📅";
    public static final String ICONO_UBICACION = "📍";
    
    // Configuración de paginación
    public static final int REGISTROS_POR_PAGINA = 20;
    public static final int MAX_REGISTROS_BUSQUEDA = 100;
    
    // Configuración de archivos
    public static final String DIRECTORIO_BACKUP = "backup";
    public static final String EXTENSION_BACKUP = ".sql";
    public static final String PREFIJO_BACKUP = "agenda_backup_";
    
    // Constructor privado para evitar instanciación
    private Constantes() {
        throw new UnsupportedOperationException("Esta es una clase de utilidades");
    }
    
    /**
     * Obtiene la URL completa de la base de datos
     */
    public static String obtenerUrlBD(String host, String puerto, String baseDatos) {
        return String.format("jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=UTF-8&serverTimezone=America/Bogota&autoReconnect=true&useSSL=false",
                           host, puerto, baseDatos);
    }
    
    /**
     * Obtiene la URL de base de datos por defecto
     */
    public static String obtenerUrlBDDefecto() {
        return obtenerUrlBD(BD_HOST_DEFECTO, BD_PUERTO_DEFECTO, BD_NOMBRE);
    }
    
    /**
     * Obtiene el título completo de la aplicación
     */
    public static String obtenerTituloCompleto() {
        return String.format("%s v%s", APP_NOMBRE, APP_VERSION);
    }
}