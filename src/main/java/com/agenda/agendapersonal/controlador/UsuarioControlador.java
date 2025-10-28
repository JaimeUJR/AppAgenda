package com.agenda.agendapersonal.controlador;

import com.agenda.agendapersonal.dao.UsuarioDAO;
import com.agenda.agendapersonal.modelo.Usuario;
import com.agenda.agendapersonal.config.Constantes;

public class UsuarioControlador {
    
    private UsuarioDAO usuarioDAO;
    private Usuario usuarioActual;
    
    public UsuarioControlador() {
        this.usuarioDAO = new UsuarioDAO();
    }
    
    public ResultadoOperacion registrarUsuario(String nombreUsuario, String email, String password, String nombreCompleto) {
        // Validaciones
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            return new ResultadoOperacion(false, "El nombre de usuario es requerido");
        }
        
        if (email == null || email.trim().isEmpty()) {
            return new ResultadoOperacion(false, "El email es requerido");
        }
        
        if (!email.contains("@") || !email.contains(".")) {
            return new ResultadoOperacion(false, "El email no tiene un formato válido");
        }
        
        if (password == null || password.length() < 4) {
            return new ResultadoOperacion(false, "La contraseña debe tener al menos 4 caracteres");
        }
        
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
            return new ResultadoOperacion(false, "El nombre completo es requerido");
        }
        
        // Verificar si ya existe el usuario
        if (usuarioDAO.existeNombreUsuario(nombreUsuario)) {
            return new ResultadoOperacion(false, "Ya existe un usuario con ese nombre");
        }
        
        if (usuarioDAO.existeEmail(email)) {
            return new ResultadoOperacion(false, "Ya existe un usuario con ese email");
        }
        
        // Crear usuario
        Usuario usuario = new Usuario(nombreUsuario, email, password, nombreCompleto);
        
        if (usuarioDAO.crear(usuario)) {
            return new ResultadoOperacion(true, "Usuario registrado exitosamente", usuario);
        } else {
            return new ResultadoOperacion(false, "Error al registrar el usuario");
        }
    }
    
    public ResultadoOperacion iniciarSesion(String nombreUsuario, String password) {
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            return new ResultadoOperacion(false, "El nombre de usuario es requerido");
        }
        
        if (password == null || password.trim().isEmpty()) {
            return new ResultadoOperacion(false, "La contraseña es requerida");
        }
        
        Usuario usuario = usuarioDAO.autenticar(nombreUsuario, password);
        
        if (usuario != null) {
            this.usuarioActual = usuario;
            return new ResultadoOperacion(true, "Sesión iniciada exitosamente", usuario);
        } else {
            return new ResultadoOperacion(false, "Credenciales inválidas");
        }
    }
    
    public void cerrarSesion() {
        this.usuarioActual = null;
    }
    
    public ResultadoOperacion actualizarPerfil(String nombreUsuario, String email, String nombreCompleto) {
        if (usuarioActual == null) {
            return new ResultadoOperacion(false, "No hay usuario autenticado");
        }
        
        // Validaciones
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            return new ResultadoOperacion(false, "El nombre de usuario es requerido");
        }
        
        if (email == null || email.trim().isEmpty()) {
            return new ResultadoOperacion(false, "El email es requerido");
        }
        
        if (!email.contains("@") || !email.contains(".")) {
            return new ResultadoOperacion(false, "El email no tiene un formato válido");
        }
        
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
            return new ResultadoOperacion(false, "El nombre completo es requerido");
        }
        
        // Verificar si el nuevo nombre de usuario ya existe (y no es el actual)
        if (!nombreUsuario.equals(usuarioActual.getNombreUsuario()) && usuarioDAO.existeNombreUsuario(nombreUsuario)) {
            return new ResultadoOperacion(false, "Ya existe un usuario con ese nombre");
        }
        
        // Verificar si el nuevo email ya existe (y no es el actual)
        if (!email.equals(usuarioActual.getEmail()) && usuarioDAO.existeEmail(email)) {
            return new ResultadoOperacion(false, "Ya existe un usuario con ese email");
        }
        
        // Actualizar datos
        usuarioActual.setNombreUsuario(nombreUsuario);
        usuarioActual.setEmail(email);
        usuarioActual.setNombreCompleto(nombreCompleto);
        
        if (usuarioDAO.actualizar(usuarioActual)) {
            return new ResultadoOperacion(true, "Perfil actualizado exitosamente", usuarioActual);
        } else {
            return new ResultadoOperacion(false, "Error al actualizar el perfil");
        }
    }
    
    public ResultadoOperacion cambiarPassword(String passwordActual, String passwordNuevo, String confirmarPassword) {
        if (usuarioActual == null) {
            return new ResultadoOperacion(false, "No hay usuario autenticado");
        }
        
        if (passwordActual == null || passwordActual.trim().isEmpty()) {
            return new ResultadoOperacion(false, "La contraseña actual es requerida");
        }
        
        if (passwordNuevo == null || passwordNuevo.length() < 4) {
            return new ResultadoOperacion(false, "La nueva contraseña debe tener al menos 4 caracteres");
        }
        
        if (!passwordNuevo.equals(confirmarPassword)) {
            return new ResultadoOperacion(false, "La confirmación de contraseña no coincide");
        }
        
        // Verificar contraseña actual
        Usuario verificacion = usuarioDAO.autenticar(usuarioActual.getNombreUsuario(), passwordActual);
        if (verificacion == null) {
            return new ResultadoOperacion(false, "La contraseña actual es incorrecta");
        }
        
        // Cambiar contraseña
        if (usuarioDAO.cambiarPassword(usuarioActual.getIdUsuario(), passwordNuevo)) {
            return new ResultadoOperacion(true, "Contraseña cambiada exitosamente");
        } else {
            return new ResultadoOperacion(false, "Error al cambiar la contraseña");
        }
    }
    
    public ResultadoOperacion validarDatosUsuario(String nombreUsuario, String email, String password, String nombreCompleto) {
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            return new ResultadoOperacion(false, "El nombre de usuario es requerido");
        }
        
        if (nombreUsuario.length() > Constantes.USUARIO_MAX_LENGTH) {
            return new ResultadoOperacion(false, "El nombre de usuario es demasiado largo");
        }
        
        if (email == null || email.trim().isEmpty()) {
            return new ResultadoOperacion(false, "El email es requerido");
        }
        
        if (email.length() > Constantes.EMAIL_MAX_LENGTH) {
            return new ResultadoOperacion(false, "El email es demasiado largo");
        }
        
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            return new ResultadoOperacion(false, "El formato del email es inválido");
        }
        
        if (password != null && password.length() < 4) {
            return new ResultadoOperacion(false, "La contraseña debe tener al menos 4 caracteres");
        }
        
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
            return new ResultadoOperacion(false, "El nombre completo es requerido");
        }
        
        if (nombreCompleto.length() > Constantes.NOMBRE_MAX_LENGTH) {
            return new ResultadoOperacion(false, "El nombre completo es demasiado largo");
        }
        
        return new ResultadoOperacion(true, "Datos válidos");
    }
    
    public Usuario buscarUsuario(int idUsuario) {
        return usuarioDAO.buscarPorId(idUsuario);
    }
    
    public boolean hayUsuarioAutenticado() {
        return usuarioActual != null;
    }
    
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
    
    public static class ResultadoOperacion {
        private boolean exitoso;
        private String mensaje;
        private Object datos;
        
        public ResultadoOperacion(boolean exitoso, String mensaje) {
            this.exitoso = exitoso;
            this.mensaje = mensaje;
        }
        
        public ResultadoOperacion(boolean exitoso, String mensaje, Object datos) {
            this.exitoso = exitoso;
            this.mensaje = mensaje;
            this.datos = datos;
        }
        
        public boolean isExitoso() {
            return exitoso;
        }
        
        public String getMensaje() {
            return mensaje;
        }
        
        public Object getDatos() {
            return datos;
        }
        
        @SuppressWarnings("unchecked")
        public <T> T getDatos(Class<T> tipo) {
            return (T) datos;
        }
    }
}