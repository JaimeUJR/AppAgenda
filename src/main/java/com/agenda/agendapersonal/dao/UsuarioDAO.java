package com.agenda.agendapersonal.dao;

import com.agenda.agendapersonal.config.ConexionBD;
import com.agenda.agendapersonal.modelo.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    
    private ConexionBD conexionBD;
    
    public UsuarioDAO() {
        this.conexionBD = ConexionBD.getInstancia();
    }
    
    /**
     * Crear un nuevo usuario
     */
    public boolean crear(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre_usuario, email, password, nombre_completo) VALUES (?, ?, SHA2(?, 256), ?)";
        
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, usuario.getNombreUsuario());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getPassword());
            stmt.setString(4, usuario.getNombreCompleto());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        usuario.setIdUsuario(rs.getInt(1));
                    }
                }
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al crear usuario: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Buscar usuario por ID
     */
    public Usuario buscarPorId(int idUsuario) {
        String sql = "SELECT id_usuario, nombre_usuario, email, nombre_completo, fecha_registro, ultimo_acceso FROM usuarios WHERE id_usuario = ?";
        
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Buscar usuario por nombre de usuario
     */
    public Usuario buscarPorNombreUsuario(String nombreUsuario) {
        String sql = "SELECT id_usuario, nombre_usuario, email, nombre_completo, fecha_registro, ultimo_acceso FROM usuarios WHERE nombre_usuario = ?";
        
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombreUsuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por nombre: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Buscar usuario por email
     */
    public Usuario buscarPorEmail(String email) {
        String sql = "SELECT id_usuario, nombre_usuario, email, nombre_completo, fecha_registro, ultimo_acceso FROM usuarios WHERE email = ?";
        
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por email: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Autenticar usuario (verificar credenciales)
     */
    public Usuario autenticar(String nombreUsuario, String password) {
        String sql = "SELECT id_usuario, nombre_usuario, email, password, nombre_completo, fecha_registro, ultimo_acceso FROM usuarios WHERE nombre_usuario = ? AND password = SHA2(?, 256)";
        
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombreUsuario);
            stmt.setString(2, password);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
//                    String passwordBD = rs.getString("password");
                    // Aquí podrías implementar verificación de hash
//                    if (password.equals(passwordBD)) {
                        Usuario usuario = mapearUsuario(rs);
                        actualizarUltimoAcceso(usuario.getIdUsuario());
                        return usuario;
//                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al autenticar usuario: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Actualizar usuario
     */
    public boolean actualizar(Usuario usuario) {
        String sql = "UPDATE usuarios SET nombre_usuario = ?, email = ?, nombre_completo = ? WHERE id_usuario = ?";
        
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, usuario.getNombreUsuario());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getNombreCompleto());
            stmt.setInt(4, usuario.getIdUsuario());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Cambiar password del usuario
     */
    public boolean cambiarPassword(int idUsuario, String nuevoPassword) {
        String sql = "UPDATE usuarios SET password = ? WHERE id_usuario = ?";
        
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nuevoPassword);
            stmt.setInt(2, idUsuario);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al cambiar password: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Actualizar último acceso
     */
    public boolean actualizarUltimoAcceso(int idUsuario) {
        String sql = "UPDATE usuarios SET ultimo_acceso = CURRENT_TIMESTAMP WHERE id_usuario = ?";
        
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar último acceso: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Eliminar usuario
     */
    public boolean eliminar(int idUsuario) {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Listar todos los usuarios
     */
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT id_usuario, nombre_usuario, email, nombre_completo, fecha_registro, ultimo_acceso FROM usuarios ORDER BY nombre_completo";
        
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        }
        
        return usuarios;
    }
    
    /**
     * Verificar si existe un usuario con el nombre de usuario dado
     */
    public boolean existeNombreUsuario(String nombreUsuario) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE nombre_usuario = ?";
        
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombreUsuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al verificar nombre de usuario: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Verificar si existe un usuario con el email dado
     */
    public boolean existeEmail(String email) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE email = ?";
        
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al verificar email: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Mapea un ResultSet a un objeto Usuario
     */
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(rs.getInt("id_usuario"));
        usuario.setNombreUsuario(rs.getString("nombre_usuario"));
        usuario.setEmail(rs.getString("email"));
        usuario.setNombreCompleto(rs.getString("nombre_completo"));
        
        Timestamp fechaRegistro = rs.getTimestamp("fecha_registro");
        if (fechaRegistro != null) {
            usuario.setFechaRegistro(fechaRegistro.toLocalDateTime());
        }
        
        Timestamp ultimoAcceso = rs.getTimestamp("ultimo_acceso");
        if (ultimoAcceso != null) {
            usuario.setUltimoAcceso(ultimoAcceso.toLocalDateTime());
        }
        
        return usuario;
    }
}