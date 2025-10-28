package com.agenda.agendapersonal.controlador;

import com.agenda.agendapersonal.dao.CategoriaDAO;
import com.agenda.agendapersonal.modelo.Categoria;
import com.agenda.agendapersonal.modelo.CategoriaDTO;
import com.agenda.agendapersonal.config.Constantes;
import java.util.List;

public class CategoriaControlador {
    
    private CategoriaDAO categoriaDAO;
    private UsuarioControlador usuarioControlador;
    
    public CategoriaControlador(UsuarioControlador usuarioControlador) {
        this.categoriaDAO = new CategoriaDAO();
        this.usuarioControlador = usuarioControlador;
    }
    
    /**
     * Crear una nueva categoría
     */
    public UsuarioControlador.ResultadoOperacion crearCategoria(String nombre) {
        // Verificar usuario autenticado
        if (!usuarioControlador.hayUsuarioAutenticado()) {
            return new UsuarioControlador.ResultadoOperacion(false, "Debe iniciar sesión para crear categorías");
        }
        
        // Validar nombre
        UsuarioControlador.ResultadoOperacion validacion = validarNombreCategoria(nombre);
        if (!validacion.isExitoso()) {
            return validacion;
        }
        
        String nombreLimpio = nombre.trim();
        
        // Verificar si ya existe la categoría
        if (categoriaDAO.existeNombre(nombreLimpio)) {
            // Si existe, asociarla al usuario actual
            Categoria categoriaExistente = categoriaDAO.buscarPorNombre(nombreLimpio);
            if (categoriaExistente != null) {
                categoriaDAO.asociarConUsuario(categoriaExistente.getIdCategoria(), 
                        usuarioControlador.getUsuarioActual().getIdUsuario());
                return new UsuarioControlador.ResultadoOperacion(true, "Categoría agregada a tu lista", categoriaExistente);
            }
        }
        
        // Crear nueva categoría
        Categoria categoria = new Categoria(nombreLimpio);
        
        if (categoriaDAO.crear(categoria)) {
            // Asociar con el usuario actual
            categoriaDAO.asociarConUsuario(categoria.getIdCategoria(), 
                    usuarioControlador.getUsuarioActual().getIdUsuario());
            return new UsuarioControlador.ResultadoOperacion(true, "Categoría creada exitosamente", categoria);
        } else {
            return new UsuarioControlador.ResultadoOperacion(false, "Error al crear la categoría");
        }
    }
    
    /**
     * Crear o obtener categoría (para uso interno)
     */
    public Categoria crearOObtenerCategoria(String nombre) {
        if (!usuarioControlador.hayUsuarioAutenticado() || nombre == null || nombre.trim().isEmpty()) {
            return null;
        }
        
        String nombreLimpio = nombre.trim();
        int idCategoria = categoriaDAO.crearOObtener(nombreLimpio, 
                usuarioControlador.getUsuarioActual().getIdUsuario());
        
        if (idCategoria > 0) {
            return categoriaDAO.buscarPorId(idCategoria);
        }
        
        return null;
    }
    
    /**
     * Actualizar una categoría
     */
    public UsuarioControlador.ResultadoOperacion actualizarCategoria(int idCategoria, String nuevoNombre) {
        // Verificar usuario autenticado
        if (!usuarioControlador.hayUsuarioAutenticado()) {
            return new UsuarioControlador.ResultadoOperacion(false, "Debe iniciar sesión");
        }
        
        // Validar nombre
        UsuarioControlador.ResultadoOperacion validacion = validarNombreCategoria(nuevoNombre);
        if (!validacion.isExitoso()) {
            return validacion;
        }
        
        // Buscar la categoría
        Categoria categoria = categoriaDAO.buscarPorId(idCategoria);
        if (categoria == null) {
            return new UsuarioControlador.ResultadoOperacion(false, "Categoría no encontrada");
        }
        
        String nombreLimpio = nuevoNombre.trim();
        
        // Verificar si el nuevo nombre ya existe (y no es la misma categoría)
        if (!nombreLimpio.equals(categoria.getNombre()) && categoriaDAO.existeNombre(nombreLimpio)) {
            return new UsuarioControlador.ResultadoOperacion(false, "Ya existe una categoría con ese nombre");
        }
        
        // Actualizar categoría
        categoria.setNombre(nombreLimpio);
        
        if (categoriaDAO.actualizar(categoria)) {
            return new UsuarioControlador.ResultadoOperacion(true, "Categoría actualizada exitosamente", categoria);
        } else {
            return new UsuarioControlador.ResultadoOperacion(false, "Error al actualizar la categoría");
        }
    }
    
    /**
     * Eliminar una categoría
     */
    public UsuarioControlador.ResultadoOperacion eliminarCategoria(int idCategoria) {
        // Verificar usuario autenticado
        if (!usuarioControlador.hayUsuarioAutenticado()) {
            return new UsuarioControlador.ResultadoOperacion(false, "Debe iniciar sesión");
        }
        
        // Buscar la categoría
        Categoria categoria = categoriaDAO.buscarPorId(idCategoria);
        if (categoria == null) {
            return new UsuarioControlador.ResultadoOperacion(false, "Categoría no encontrada");
        }
        
        // Primero desasociar de usuario actual
        boolean desasociada = categoriaDAO.desasociarDeUsuario(idCategoria, 
                usuarioControlador.getUsuarioActual().getIdUsuario());
        
        if (desasociada) {
            return new UsuarioControlador.ResultadoOperacion(true, "Categoría removida de tu lista");
        } else {
            return new UsuarioControlador.ResultadoOperacion(false, "Error al remover la categoría");
        }
    }
    
    /**
     * Obtener categorías del usuario actual
     */
    public List<Categoria> obtenerCategoriasUsuario() {
        if (!usuarioControlador.hayUsuarioAutenticado()) {
            return List.of();
        }
        return categoriaDAO.listarPorUsuario(usuarioControlador.getUsuarioActual().getIdUsuario());
    }
    
    /**
     * Obtener todas las categorías disponibles
     */
    public List<Categoria> obtenerTodasLasCategorias() {
        return categoriaDAO.listarTodas();
    }
    
    /**
     * Buscar categoría por nombre
     */
    public Categoria buscarCategoriaPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return null;
        }
        return categoriaDAO.buscarPorNombre(nombre.trim());
    }
    
    /**
     * Buscar categoría por ID
     */
    public Categoria buscarCategoriaPorId(int idCategoria) {
        return categoriaDAO.buscarPorId(idCategoria);
    }
    
    /**
     * Asociar categoría existente con usuario actual
     */
    public UsuarioControlador.ResultadoOperacion asociarCategoriaConUsuario(int idCategoria) {
        // Verificar usuario autenticado
        if (!usuarioControlador.hayUsuarioAutenticado()) {
            return new UsuarioControlador.ResultadoOperacion(false, "Debe iniciar sesión");
        }
        
        // Buscar la categoría
        Categoria categoria = categoriaDAO.buscarPorId(idCategoria);
        if (categoria == null) {
            return new UsuarioControlador.ResultadoOperacion(false, "Categoría no encontrada");
        }
        
        // Asociar con usuario
        boolean asociada = categoriaDAO.asociarConUsuario(idCategoria, 
                usuarioControlador.getUsuarioActual().getIdUsuario());
        
        if (asociada) {
            return new UsuarioControlador.ResultadoOperacion(true, "Categoría agregada a tu lista", categoria);
        } else {
            return new UsuarioControlador.ResultadoOperacion(true, "La categoría ya estaba en tu lista", categoria);
        }
    }
    
    /**
     * Obtener vista de categorías con estadísticas
     */
    public List<CategoriaDTO> obtenerVistaCategorias() {
        return categoriaDAO.obtenerVistaCategorias();
    }
    
    /**
     * Obtener vista de categorías del usuario actual
     */
    public List<CategoriaDTO> obtenerVistaCategoriasUsuario() {
        if (!usuarioControlador.hayUsuarioAutenticado()) {
            return List.of();
        }
        return categoriaDAO.obtenerVistaCategoriasUsuario(usuarioControlador.getUsuarioActual().getIdUsuario());
    }
    
    /**
     * Buscar categorías por texto
     */
    public List<Categoria> buscarCategorias(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return obtenerCategoriasUsuario();
        }
        
        String textoBusqueda = texto.trim().toLowerCase();
        return obtenerCategoriasUsuario().stream()
                .filter(c -> c.getNombre().toLowerCase().contains(textoBusqueda))
                .toList();
    }
    
    /**
     * Obtener categorías sugeridas (más usadas)
     */
    public List<CategoriaDTO> obtenerCategoriasSugeridas() {
        return categoriaDAO.obtenerVistaCategorias().stream()
                .filter(c -> c.getTotalEventos() > 0)
                .sorted((a, b) -> Long.compare(b.getTotalEventos(), a.getTotalEventos()))
                .limit(10)
                .toList();
    }
    
    /**
     * Validar nombre de categoría
     */
    private UsuarioControlador.ResultadoOperacion validarNombreCategoria(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return new UsuarioControlador.ResultadoOperacion(false, "El nombre de la categoría es requerido");
        }
        
        String nombreLimpio = nombre.trim();
        
        if (nombreLimpio.length() > Constantes.CATEGORIA_MAX_LENGTH) {
            return new UsuarioControlador.ResultadoOperacion(false, 
                    "El nombre de la categoría es demasiado largo (máximo " + Constantes.CATEGORIA_MAX_LENGTH + " caracteres)");
        }
        
        if (nombreLimpio.length() < 2) {
            return new UsuarioControlador.ResultadoOperacion(false, "El nombre de la categoría debe tener al menos 2 caracteres");
        }
        
        // Validar caracteres especiales (solo letras, números, espacios y algunos símbolos)
        if (!nombreLimpio.matches("^[a-zA-ZáéíóúñüÁÉÍÓÚÑÜ0-9\\s\\-_]+$")) {
            return new UsuarioControlador.ResultadoOperacion(false, "El nombre contiene caracteres no válidos");
        }
        
        return new UsuarioControlador.ResultadoOperacion(true, "Nombre válido");
    }
    
    /**
     * Verificar si el usuario actual puede modificar una categoría
     */
    public boolean puedeModificarCategoria(int idCategoria) {
        if (!usuarioControlador.hayUsuarioAutenticado()) {
            return false;
        }
        
        List<Categoria> categoriasUsuario = obtenerCategoriasUsuario();
        return categoriasUsuario.stream()
                .anyMatch(c -> c.getIdCategoria() == idCategoria);
    }
    
    /**
     * Obtener estadísticas de categorías
     */
    public String obtenerEstadisticasCategorias() {
        if (!usuarioControlador.hayUsuarioAutenticado()) {
            return "No hay usuario autenticado";
        }
        
        List<CategoriaDTO> categorias = obtenerVistaCategoriasUsuario();
        long totalCategorias = categorias.size();
        long categoriasConEventos = categorias.stream()
                .filter(c -> c.getEventosEnCategoria() > 0)
                .count();
        long totalEventos = categorias.stream()
                .mapToLong(CategoriaDTO::getEventosEnCategoria)
                .sum();
        
        return String.format("Categorías: %d total, %d con eventos, %d eventos totales", 
                           totalCategorias, categoriasConEventos, totalEventos);
    }
}