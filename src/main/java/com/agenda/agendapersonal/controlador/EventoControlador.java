package com.agenda.agendapersonal.controlador;

import com.agenda.agendapersonal.dao.EventoDAO;
import com.agenda.agendapersonal.dao.CategoriaDAO;
import com.agenda.agendapersonal.modelo.*;
import com.agenda.agendapersonal.config.Constantes;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

/**
 * Controlador para la gestión de eventos
 * 
 * @author JaimeSQL
 */
public class EventoControlador {
    
    private EventoDAO eventoDAO;
    private CategoriaDAO categoriaDAO;
    private UsuarioControlador usuarioControlador;
    
    public EventoControlador(UsuarioControlador usuarioControlador) {
        this.eventoDAO = new EventoDAO();
        this.categoriaDAO = new CategoriaDAO();
        this.usuarioControlador = usuarioControlador;
    }
    
    /**
     * Crear un nuevo evento
     */
    public UsuarioControlador.ResultadoOperacion crearEvento(String titulo, String descripcion, 
            LocalDateTime fechaInicio, LocalDateTime fechaFin, String ubicacion, 
            boolean recordatorio, List<String> nombresCategoria) {
        
        // Verificar usuario autenticado
        if (!usuarioControlador.hayUsuarioAutenticado()) {
            return new UsuarioControlador.ResultadoOperacion(false, "Debe iniciar sesión para crear eventos");
        }
        
        // Validar datos del evento
        UsuarioControlador.ResultadoOperacion validacion = validarDatosEvento(titulo, descripcion, fechaInicio, fechaFin, ubicacion);
        if (!validacion.isExitoso()) {
            return validacion;
        }
        
        // Crear el evento
        Evento evento = new Evento(
            usuarioControlador.getUsuarioActual().getIdUsuario(),
            titulo, descripcion, fechaInicio, fechaFin, ubicacion, recordatorio
        );
        
        // Procesar categorías
        if (nombresCategoria != null && !nombresCategoria.isEmpty()) {
            for (String nombreCategoria : nombresCategoria) {
                if (nombreCategoria != null && !nombreCategoria.trim().isEmpty()) {
                    int idCategoria = categoriaDAO.crearOObtener(nombreCategoria.trim(), 
                            usuarioControlador.getUsuarioActual().getIdUsuario());
                    if (idCategoria > 0) {
                        Categoria categoria = new Categoria(idCategoria, nombreCategoria.trim());
                        evento.agregarCategoria(categoria);
                    }
                }
            }
        }
        
        // Guardar en base de datos
        if (eventoDAO.crear(evento)) {
            return new UsuarioControlador.ResultadoOperacion(true, "Evento creado exitosamente", evento);
        } else {
            return new UsuarioControlador.ResultadoOperacion(false, "Error al crear el evento");
        }
    }
    
    /**
     * Actualizar un evento existente
     */
    public UsuarioControlador.ResultadoOperacion actualizarEvento(int idEvento, String titulo, String descripcion,
            LocalDateTime fechaInicio, LocalDateTime fechaFin, String ubicacion, 
            boolean recordatorio, EstadoEvento estado, List<String> nombresCategoria) {
        
        // Verificar usuario autenticado
        if (!usuarioControlador.hayUsuarioAutenticado()) {
            return new UsuarioControlador.ResultadoOperacion(false, "Debe iniciar sesión para actualizar eventos");
        }
        
        // Buscar el evento
        Evento evento = eventoDAO.buscarPorId(idEvento);
        if (evento == null) {
            return new UsuarioControlador.ResultadoOperacion(false, "Evento no encontrado");
        }
        
        // Verificar que el evento pertenece al usuario actual
        if (evento.getIdUsuario() != usuarioControlador.getUsuarioActual().getIdUsuario()) {
            return new UsuarioControlador.ResultadoOperacion(false, "No tiene permisos para modificar este evento");
        }
        
        // Validar datos del evento
        UsuarioControlador.ResultadoOperacion validacion = validarDatosEvento(titulo, descripcion, fechaInicio, fechaFin, ubicacion);
        if (!validacion.isExitoso()) {
            return validacion;
        }
        
        // Actualizar datos del evento
        evento.setTitulo(titulo);
        evento.setDescripcion(descripcion);
        evento.setFechaInicio(fechaInicio);
        evento.setFechaFin(fechaFin);
        evento.setUbicacion(ubicacion);
        evento.setRecordatorio(recordatorio);
        evento.setEstado(estado);
        
        // Procesar categorías
        evento.getCategorias().clear();
        if (nombresCategoria != null && !nombresCategoria.isEmpty()) {
            for (String nombreCategoria : nombresCategoria) {
                if (nombreCategoria != null && !nombreCategoria.trim().isEmpty()) {
                    int idCategoria = categoriaDAO.crearOObtener(nombreCategoria.trim(), 
                            usuarioControlador.getUsuarioActual().getIdUsuario());
                    if (idCategoria > 0) {
                        Categoria categoria = new Categoria(idCategoria, nombreCategoria.trim());
                        evento.agregarCategoria(categoria);
                    }
                }
            }
        }
        
        // Guardar cambios
        if (eventoDAO.actualizar(evento)) {
            return new UsuarioControlador.ResultadoOperacion(true, "Evento actualizado exitosamente", evento);
        } else {
            return new UsuarioControlador.ResultadoOperacion(false, "Error al actualizar el evento");
        }
    }
    
    /**
     * Cambiar estado de un evento
     */
    public UsuarioControlador.ResultadoOperacion cambiarEstadoEvento(int idEvento, EstadoEvento nuevoEstado) {
        // Verificar usuario autenticado
        if (!usuarioControlador.hayUsuarioAutenticado()) {
            return new UsuarioControlador.ResultadoOperacion(false, "Debe iniciar sesión");
        }
        
        // Buscar el evento
        Evento evento = eventoDAO.buscarPorId(idEvento);
        if (evento == null) {
            return new UsuarioControlador.ResultadoOperacion(false, "Evento no encontrado");
        }
        
        // Verificar permisos
        if (evento.getIdUsuario() != usuarioControlador.getUsuarioActual().getIdUsuario()) {
            return new UsuarioControlador.ResultadoOperacion(false, "No tiene permisos para modificar este evento");
        }
        
        // Cambiar estado
        if (eventoDAO.cambiarEstado(idEvento, nuevoEstado)) {
            evento.setEstado(nuevoEstado);
            return new UsuarioControlador.ResultadoOperacion(true, "Estado del evento actualizado", evento);
        } else {
            return new UsuarioControlador.ResultadoOperacion(false, "Error al cambiar el estado del evento");
        }
    }
    
    /**
     * Eliminar un evento
     */
    public UsuarioControlador.ResultadoOperacion eliminarEvento(int idEvento) {
        // Verificar usuario autenticado
        if (!usuarioControlador.hayUsuarioAutenticado()) {
            return new UsuarioControlador.ResultadoOperacion(false, "Debe iniciar sesión");
        }
        
        // Buscar el evento
        Evento evento = eventoDAO.buscarPorId(idEvento);
        if (evento == null) {
            return new UsuarioControlador.ResultadoOperacion(false, "Evento no encontrado");
        }
        
        // Verificar permisos
        if (evento.getIdUsuario() != usuarioControlador.getUsuarioActual().getIdUsuario()) {
            return new UsuarioControlador.ResultadoOperacion(false, "No tiene permisos para eliminar este evento");
        }
        
        // Eliminar evento
        if (eventoDAO.eliminar(idEvento)) {
            return new UsuarioControlador.ResultadoOperacion(true, "Evento eliminado exitosamente");
        } else {
            return new UsuarioControlador.ResultadoOperacion(false, "Error al eliminar el evento");
        }
    }
    
    /**
     * Obtener eventos del usuario actual
     */
    public List<Evento> obtenerEventosUsuario() {
        if (!usuarioControlador.hayUsuarioAutenticado()) {
            return List.of();
        }
        return eventoDAO.listarPorUsuario(usuarioControlador.getUsuarioActual().getIdUsuario());
    }
    
    /**
     * Obtener eventos por fecha
     */
    public List<Evento> obtenerEventosPorFecha(LocalDate fecha) {
        if (!usuarioControlador.hayUsuarioAutenticado()) {
            return List.of();
        }
        
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.atTime(23, 59, 59);
        
        return eventoDAO.listarPorFecha(usuarioControlador.getUsuarioActual().getIdUsuario(), inicio, fin);
    }
    
    /**
     * Obtener eventos por rango de fechas
     */
    public List<Evento> obtenerEventosPorRango(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        if (!usuarioControlador.hayUsuarioAutenticado()) {
            return List.of();
        }
        return eventoDAO.listarPorFecha(usuarioControlador.getUsuarioActual().getIdUsuario(), fechaInicio, fechaFin);
    }
    
    /**
     * Obtener eventos por estado
     */
    public List<Evento> obtenerEventosPorEstado(EstadoEvento estado) {
        if (!usuarioControlador.hayUsuarioAutenticado()) {
            return List.of();
        }
        return eventoDAO.listarPorEstado(usuarioControlador.getUsuarioActual().getIdUsuario(), estado);
    }
    
    /**
     * Buscar eventos por texto
     */
    public List<Evento> buscarEventos(String texto) {
        if (!usuarioControlador.hayUsuarioAutenticado() || texto == null || texto.trim().isEmpty()) {
            return List.of();
        }
        return eventoDAO.buscarPorTexto(usuarioControlador.getUsuarioActual().getIdUsuario(), texto.trim());
    }
    
    /**
     * Obtener evento por ID
     */
    public Evento obtenerEvento(int idEvento) {
        Evento evento = eventoDAO.buscarPorId(idEvento);
        
        // Verificar permisos si hay usuario autenticado
        if (usuarioControlador.hayUsuarioAutenticado() && evento != null) {
            if (evento.getIdUsuario() != usuarioControlador.getUsuarioActual().getIdUsuario()) {
                return null; // No tiene permisos
            }
        }
        
        return evento;
    }
    
    /**
     * Obtener eventos de hoy
     */
    public List<Evento> obtenerEventosHoy() {
        return obtenerEventosPorFecha(LocalDate.now());
    }
    
    /**
     * Obtener eventos próximos (siguientes 7 días)
     */
    public List<Evento> obtenerEventosProximos() {
        if (!usuarioControlador.hayUsuarioAutenticado()) {
            return List.of();
        }
        
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime enUnaSemana = ahora.plusDays(7);
        
        return eventoDAO.listarPorFecha(usuarioControlador.getUsuarioActual().getIdUsuario(), ahora, enUnaSemana);
    }
    
    /**
     * Obtener vista de eventos
     */
    public List<EventoDTO> obtenerVistaEventos() {
        if (!usuarioControlador.hayUsuarioAutenticado()) {
            return List.of();
        }
        return eventoDAO.obtenerVistaEventosPorUsuario(usuarioControlador.getUsuarioActual().getIdUsuario());
    }
    
    /**
     * Validar datos de un evento
     */
    private UsuarioControlador.ResultadoOperacion validarDatosEvento(String titulo, String descripcion, 
            LocalDateTime fechaInicio, LocalDateTime fechaFin, String ubicacion) {
        
        if (titulo == null || titulo.trim().isEmpty()) {
            return new UsuarioControlador.ResultadoOperacion(false, "El título es requerido");
        }
        
        if (titulo.length() > Constantes.TITULO_MAX_LENGTH) {
            return new UsuarioControlador.ResultadoOperacion(false, "El título es demasiado largo");
        }
        
        if (descripcion != null && descripcion.length() > Constantes.DESCRIPCION_MAX_LENGTH) {
            return new UsuarioControlador.ResultadoOperacion(false, "La descripción es demasiado larga");
        }
        
        if (fechaInicio == null) {
            return new UsuarioControlador.ResultadoOperacion(false, "La fecha de inicio es requerida");
        }
        
        if (fechaFin != null && fechaFin.isBefore(fechaInicio)) {
            return new UsuarioControlador.ResultadoOperacion(false, "La fecha fin debe ser posterior a la fecha inicio");
        }
        
        if (ubicacion != null && ubicacion.length() > Constantes.UBICACION_MAX_LENGTH) {
            return new UsuarioControlador.ResultadoOperacion(false, "La ubicación es demasiado larga");
        }
        
        return new UsuarioControlador.ResultadoOperacion(true, "Datos válidos");
    }
    
    /**
     * Marcar evento como completado
     */
    public UsuarioControlador.ResultadoOperacion completarEvento(int idEvento) {
        return cambiarEstadoEvento(idEvento, EstadoEvento.COMPLETADO);
    }
    
    /**
     * Marcar evento como cancelado
     */
    public UsuarioControlador.ResultadoOperacion cancelarEvento(int idEvento) {
        return cambiarEstadoEvento(idEvento, EstadoEvento.CANCELADO);
    }
    
    /**
     * Marcar evento como pendiente
     */
    public UsuarioControlador.ResultadoOperacion reactivarEvento(int idEvento) {
        return cambiarEstadoEvento(idEvento, EstadoEvento.PENDIENTE);
    }
}