package com.agenda.agendapersonal.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConexionBD {
    
    private static final Logger logger = LoggerFactory.getLogger(ConexionBD.class);
    private static ConexionBD instancia;
    private static Properties propiedades;
    
    // Configuración de base de datos
    private String url;
    private String usuario;
    private String password;
    private String driver;
    
    // Constructor privado para Singleton
    private ConexionBD() {
        cargarPropiedades();
        inicializarConfiguracion();
    }
    
    /**
     * Obtiene la instancia única de ConexionBD
     */
    public static synchronized ConexionBD getInstancia() {
        if (instancia == null) {
            instancia = new ConexionBD();
        }
        return instancia;
    }
    
    /**
     * Carga las propiedades desde el archivo database.properties
     */
    private void cargarPropiedades() {
        propiedades = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("database.properties")) {
            if (input == null) {
                logger.warn("No se encontró database.properties, usando valores por defecto");
                establecerValoresPorDefecto();
                return;
            }
            propiedades.load(input);
            logger.info("Propiedades de base de datos cargadas exitosamente");
        } catch (IOException e) {
            logger.error("Error al cargar database.properties: " + e.getMessage(), e);
            establecerValoresPorDefecto();
        }
    }
    
    /**
     * Establece valores por defecto si no se encuentra el archivo de propiedades
     */
    private void establecerValoresPorDefecto() {
        propiedades.setProperty("db.url", "jdbc:mysql://localhost:3306/agenda_personal");
        propiedades.setProperty("db.username", "root");
        propiedades.setProperty("db.password", "");
        propiedades.setProperty("db.driver", "com.mysql.cj.jdbc.Driver");
        propiedades.setProperty("db.url.complete", 
            "jdbc:mysql://localhost:3306/agenda_personal?useUnicode=true&characterEncoding=UTF-8&serverTimezone=America/Bogota&autoReconnect=true&useSSL=false");
    }
    
    /**
     * Inicializa la configuración de la base de datos
     */
    private void inicializarConfiguracion() {
        this.url = propiedades.getProperty("db.url.complete", propiedades.getProperty("db.url"));
        this.usuario = propiedades.getProperty("db.username");
        this.password = propiedades.getProperty("db.password");
        this.driver = propiedades.getProperty("db.driver");
        
        // Cargar el driver
        try {
            Class.forName(driver);
            logger.info("Driver MySQL cargado exitosamente");
        } catch (ClassNotFoundException e) {
            logger.error("Error al cargar el driver MySQL: " + e.getMessage(), e);
            throw new RuntimeException("No se pudo cargar el driver de MySQL", e);
        }
    }
    
    /**
     * Obtiene una nueva conexión a la base de datos
     */
    public Connection obtenerConexion() throws SQLException {
        try {
            Connection conexion = DriverManager.getConnection(url, usuario, password);
            logger.debug("Conexión a base de datos establecida");
            return conexion;
        } catch (SQLException e) {
            logger.error("Error al conectar con la base de datos: " + e.getMessage(), e);
            throw new SQLException("No se pudo establecer conexión con la base de datos", e);
        }
    }
    
    /**
     * Cierra una conexión de forma segura
     */
    public void cerrarConexion(Connection conexion) {
        if (conexion != null) {
            try {
                if (!conexion.isClosed()) {
                    conexion.close();
                    logger.debug("Conexión cerrada exitosamente");
                }
            } catch (SQLException e) {
                logger.error("Error al cerrar la conexión: " + e.getMessage(), e);
            }
        }
    }
    
    /**
     * Verifica si la conexión a la base de datos está disponible
     */
    public boolean verificarConexion() {
        try (Connection conexion = obtenerConexion()) {
            return conexion != null && !conexion.isClosed();
        } catch (SQLException e) {
            logger.error("Error al verificar la conexión: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Obtiene información de la configuración actual
     */
    public String getInformacionConexion() {
        return String.format("BD: %s, Usuario: %s, Driver: %s", 
                           propiedades.getProperty("db.url"), usuario, driver);
    }
    
    // Getters para acceso a la configuración
    public String getUrl() { return url; }
    public String getUsuario() { return usuario; }
    public String getDriver() { return driver; }
    
    /**
     * Método para testing - permite cambiar la configuración
     */
    public void configurarParaTesting(String urlTest, String usuarioTest, String passwordTest) {
        this.url = urlTest;
        this.usuario = usuarioTest;
        this.password = passwordTest;
        logger.info("Configuración cambiada para testing");
    }
}