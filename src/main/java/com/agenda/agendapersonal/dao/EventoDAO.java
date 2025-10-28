package com.agenda.agendapersonal.dao;

import com.agenda.agendapersonal.config.ConexionBD;
import com.agenda.agendapersonal.modelo.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la gestión de eventos en la base de datos
 * 
 * @author JaimeSQL
 */
public class EventoDAO {
    
    private ConexionBD conexionBD;
    
    public EventoDAO() {
        this.conexionBD = ConexionBD.getInstancia();
    }
    
    /**
     * Crear un nuevo evento usando stored procedure
     */
    public boolean crear(Evento evento) {
        String categoriasIds = obtenerCategoriasIds(evento.getCategorias());
        String sql = "CALL sp_crear_evento(?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, evento.getIdUsuario());
            stmt.setString(2, evento.getTitulo());
            stmt.setString(3, evento.getDescripcion());
            stmt.setTimestamp(4, Timestamp.valueOf(evento.getFechaInicio()));
            stmt.setTimestamp(5, evento.getFechaFin() != null ? Timestamp.valueOf(evento.getFechaFin()) : null);
            stmt.setString(6, evento.getUbicacion());
            stmt.setBoolean(7, evento.isRecordatorio());
            stmt.setString(8, categoriasIds);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int idEvento = rs.getInt("id_evento");
                    if (idEvento > 0) {
                        evento.setIdEvento(idEvento);
                        return true;
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al crear evento: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Buscar evento por ID
     */
    public Evento buscarPorId(int idEvento) {
        String sql = """
            SELECT e.id_evento, e.id_usuario, e.titulo, e.descripcion, e.fecha_inicio, 
                   e.fecha_fin, e.ubicacion, e.recordatorio, e.estado, e.fecha_creacion
            FROM eventos e
            WHERE e.id_evento = ?
            """;
        
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idEvento);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Evento evento = mapearEvento(rs);
                    cargarCategorias(evento);
                    return evento;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar evento por ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Actualizar evento usando stored procedure
     */
    public boolean actualizar(Evento evento) {
        String categoriasIds = obtenerCategoriasIds(evento.getCategorias());
        String sql = "CALL sp_actualizar_evento(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, evento.getIdEvento());
            stmt.setString(2, evento.getTitulo());
            stmt.setString(3, evento.getDescripcion());
            stmt.setTimestamp(4, Timestamp.valueOf(evento.getFechaInicio()));
            stmt.setTimestamp(5, evento.getFechaFin() != null ? Timestamp.valueOf(evento.getFechaFin()) : null);
            stmt.setString(6, evento.getUbicacion());
            stmt.setBoolean(7, evento.isRecordatorio());
            stmt.setString(8, evento.getEstado().getValor());
            stmt.setString(9, categoriasIds);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_evento") > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar evento: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Cambiar estado del evento usando stored procedure
     */
    public boolean cambiarEstado(int idEvento, EstadoEvento nuevoEstado) {
        String sql = "CALL sp_cambiar_estado_evento(?, ?)";
        
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idEvento);
            stmt.setString(2, nuevoEstado.getValor());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_evento") > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al cambiar estado de evento: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Eliminar evento usando stored procedure
     */
    public boolean eliminar(int idEvento) {
        String sql = "CALL sp_eliminar_evento(?)";
        
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idEvento);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_evento_eliminado") > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar evento: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Listar eventos por usuario
     */
    public List<Evento> listarPorUsuario(int idUsuario) {
        List<Evento> eventos = new ArrayList<>();
        String sql = """
            SELECT e.id_evento, e.id_usuario, e.titulo, e.descripcion, e.fecha_inicio, 
                   e.fecha_fin, e.ubicacion, e.recordatorio, e.estado, e.fecha_creacion
            FROM eventos e
            WHERE e.id_usuario = ?
            ORDER BY e.fecha_inicio DESC
            """;
        
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Evento evento = mapearEvento(rs);
                    cargarCategorias(evento);
                    eventos.add(evento);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar eventos por usuario: " + e.getMessage());
        }
        
        return eventos;
    }
    
    /**
     * Listar eventos por fecha
     */
    public List<Evento> listarPorFecha(int idUsuario, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<Evento> eventos = new ArrayList<>();
        String sql = """
            SELECT e.id_evento, e.id_usuario, e.titulo, e.descripcion, e.fecha_inicio, 
                   e.fecha_fin, e.ubicacion, e.recordatorio, e.estado, e.fecha_creacion
            FROM eventos e
            WHERE e.id_usuario = ? AND e.fecha_inicio BETWEEN ? AND ?
            ORDER BY e.fecha_inicio ASC
            """;
        
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            stmt.setTimestamp(2, Timestamp.valueOf(fechaInicio));
            stmt.setTimestamp(3, Timestamp.valueOf(fechaFin));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Evento evento = mapearEvento(rs);
                    cargarCategorias(evento);
                    eventos.add(evento);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar eventos por fecha: " + e.getMessage());
        }
        
        return eventos;
    }
    
    /**
     * Listar eventos por estado
     */
    public List<Evento> listarPorEstado(int idUsuario, EstadoEvento estado) {
        List<Evento> eventos = new ArrayList<>();
        String sql = """
            SELECT e.id_evento, e.id_usuario, e.titulo, e.descripcion, e.fecha_inicio, 
                   e.fecha_fin, e.ubicacion, e.recordatorio, e.estado, e.fecha_creacion
            FROM eventos e
            WHERE e.id_usuario = ? AND e.estado = ?
            ORDER BY e.fecha_inicio DESC
            """;
        
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            stmt.setString(2, estado.getValor());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Evento evento = mapearEvento(rs);
                    cargarCategorias(evento);
                    eventos.add(evento);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar eventos por estado: " + e.getMessage());
        }
        
        return eventos;
    }
    
    /**
     * Obtener vista de eventos (usando v_eventos)
     */
    public List<EventoDTO> obtenerVistaEventos() {
        List<EventoDTO> eventos = new ArrayList<>();
        String sql = "SELECT id_evento, titulo, descripcion, fecha_inicio, fecha_fin, ubicacion, recordatorio, estado, usuario, categorias FROM v_eventos";
        
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                EventoDTO dto = new EventoDTO(
                    rs.getInt("id_evento"),
                    rs.getString("titulo"),
                    rs.getString("descripcion"),
                    rs.getString("fecha_inicio"),
                    rs.getString("fecha_fin"),
                    rs.getString("ubicacion"),
                    rs.getString("recordatorio"),
                    rs.getString("estado"),
                    rs.getString("usuario"),
                    rs.getString("categorias")
                );
                eventos.add(dto);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener vista de eventos: " + e.getMessage());
        }
        
        return eventos;
    }
    
    /**
     * Obtener vista de eventos por usuario
     */
    public List<EventoDTO> obtenerVistaEventosPorUsuario(int idUsuario) {
        List<EventoDTO> eventos = new ArrayList<>();
        String sql = """
            SELECT ve.id_evento, ve.titulo, ve.descripcion, ve.fecha_inicio, ve.fecha_fin, 
                   ve.ubicacion, ve.recordatorio, ve.estado, ve.usuario, ve.categorias
            FROM v_eventos ve
            INNER JOIN eventos e ON ve.id_evento = e.id_evento
            WHERE e.id_usuario = ?
            ORDER BY e.fecha_inicio DESC
            """;
        
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    EventoDTO dto = new EventoDTO(
                        rs.getInt("id_evento"),
                        rs.getString("titulo"),
                        rs.getString("descripcion"),
                        rs.getString("fecha_inicio"),
                        rs.getString("fecha_fin"),
                        rs.getString("ubicacion"),
                        rs.getString("recordatorio"),
                        rs.getString("estado"),
                        rs.getString("usuario"),
                        rs.getString("categorias")
                    );
                    eventos.add(dto);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener vista de eventos por usuario: " + e.getMessage());
        }
        
        return eventos;
    }
    
    /**
     * Buscar eventos por texto
     */
    public List<Evento> buscarPorTexto(int idUsuario, String texto) {
        List<Evento> eventos = new ArrayList<>();
        String sql = """
            SELECT e.id_evento, e.id_usuario, e.titulo, e.descripcion, e.fecha_inicio, 
                   e.fecha_fin, e.ubicacion, e.recordatorio, e.estado, e.fecha_creacion
            FROM eventos e
            WHERE e.id_usuario = ? AND (
                e.titulo LIKE ? OR 
                e.descripcion LIKE ? OR 
                e.ubicacion LIKE ?
            )
            ORDER BY e.fecha_inicio DESC
            """;
        
        String patron = "%" + texto + "%";
        
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            stmt.setString(2, patron);
            stmt.setString(3, patron);
            stmt.setString(4, patron);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Evento evento = mapearEvento(rs);
                    cargarCategorias(evento);
                    eventos.add(evento);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar eventos por texto: " + e.getMessage());
        }
        
        return eventos;
    }
    
    /**
     * Mapea un ResultSet a un objeto Evento
     */
    private Evento mapearEvento(ResultSet rs) throws SQLException {
        Evento evento = new Evento();
        evento.setIdEvento(rs.getInt("id_evento"));
        evento.setIdUsuario(rs.getInt("id_usuario"));
        evento.setTitulo(rs.getString("titulo"));
        evento.setDescripcion(rs.getString("descripcion"));
        evento.setUbicacion(rs.getString("ubicacion"));
        evento.setRecordatorio(rs.getBoolean("recordatorio"));
        evento.setEstado(EstadoEvento.fromValor(rs.getString("estado")));
        
        Timestamp fechaInicio = rs.getTimestamp("fecha_inicio");
        if (fechaInicio != null) {
            evento.setFechaInicio(fechaInicio.toLocalDateTime());
        }
        
        Timestamp fechaFin = rs.getTimestamp("fecha_fin");
        if (fechaFin != null) {
            evento.setFechaFin(fechaFin.toLocalDateTime());
        }
        
        Timestamp fechaCreacion = rs.getTimestamp("fecha_creacion");
        if (fechaCreacion != null) {
            evento.setFechaCreacion(fechaCreacion.toLocalDateTime());
        }
        
        return evento;
    }
    
    /**
     * Carga las categorías de un evento
     */
    private void cargarCategorias(Evento evento) {
        String sql = """
            SELECT c.id_categoria, c.nombre, c.fecha_creacion
            FROM categorias c
            INNER JOIN evento_categoria ec ON c.id_categoria = ec.id_categoria
            WHERE ec.id_evento = ?
            """;
        
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, evento.getIdEvento());
            
            try (ResultSet rs = stmt.executeQuery()) {
                List<Categoria> categorias = new ArrayList<>();
                while (rs.next()) {
                    Categoria categoria = new Categoria();
                    categoria.setIdCategoria(rs.getInt("id_categoria"));
                    categoria.setNombre(rs.getString("nombre"));
                    
                    Timestamp fechaCreacion = rs.getTimestamp("fecha_creacion");
                    if (fechaCreacion != null) {
                        categoria.setFechaCreacion(fechaCreacion.toLocalDateTime());
                    }
                    
                    categorias.add(categoria);
                }
                evento.setCategorias(categorias);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al cargar categorías del evento: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene los IDs de categorías como string separado por comas
     */
    private String obtenerCategoriasIds(List<Categoria> categorias) {
        if (categorias == null || categorias.isEmpty()) {
            return null;
        }
        
        return categorias.stream()
                .map(c -> String.valueOf(c.getIdCategoria()))
                .reduce((a, b) -> a + "," + b)
                .orElse(null);
    }
}