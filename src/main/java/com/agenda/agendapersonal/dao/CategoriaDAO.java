package com.agenda.agendapersonal.dao;

import com.agenda.agendapersonal.config.ConexionBD;
import com.agenda.agendapersonal.modelo.Categoria;
import com.agenda.agendapersonal.modelo.CategoriaDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {
    
    private ConexionBD conexionBD;
    
    public CategoriaDAO() {
        this.conexionBD = ConexionBD.getInstancia();
    }
    
    /**
     * Crear una nueva categoría
     */
    public boolean crear(Categoria categoria) {
        String sql = "INSERT INTO categorias (nombre) VALUES (?)";
        
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, categoria.getNombre());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        categoria.setIdCategoria(rs.getInt(1));
                    }
                }
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al crear categoría: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Crear o obtener categoría (usando el stored procedure)
     */
    public int crearOObtener(String nombre, int idUsuario) {
        String sql = "CALL sp_crear_o_obtener_categoria(?, ?, @id_categoria)";
        
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombre);
            stmt.setInt(2, idUsuario);
            
            stmt.execute();
            
            // Obtener el ID de la categoría
            try (PreparedStatement stmtResult = conn.prepareStatement("SELECT @id_categoria as id_categoria");
                 ResultSet rs = stmtResult.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_categoria");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al crear u obtener categoría: " + e.getMessage());
        }
        
        return -1;
    }
    
    /**
     * Buscar categoría por ID
     */
    public Categoria buscarPorId(int idCategoria) {
        String sql = "SELECT id_categoria, nombre, fecha_creacion FROM categorias WHERE id_categoria = ?";
        
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCategoria);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearCategoria(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar categoría por ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Buscar categoría por nombre
     */
    public Categoria buscarPorNombre(String nombre) {
        String sql = "SELECT id_categoria, nombre, fecha_creacion FROM categorias WHERE nombre = ?";
        
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombre);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearCategoria(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar categoría por nombre: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Actualizar categoría
     */
    public boolean actualizar(Categoria categoria) {
        String sql = "UPDATE categorias SET nombre = ? WHERE id_categoria = ?";
        
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, categoria.getNombre());
            stmt.setInt(2, categoria.getIdCategoria());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar categoría: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Eliminar categoría
     */
    public boolean eliminar(int idCategoria) {
        String sql = "DELETE FROM categorias WHERE id_categoria = ?";
        
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCategoria);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar categoría: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Listar todas las categorías
     */
    public List<Categoria> listarTodas() {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT id_categoria, nombre, fecha_creacion FROM categorias ORDER BY nombre";
        
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                categorias.add(mapearCategoria(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar categorías: " + e.getMessage());
        }
        
        return categorias;
    }
    
    /**
     * Listar categorías de un usuario
     */
    public List<Categoria> listarPorUsuario(int idUsuario) {
        String sql = """
            SELECT c.id_categoria, c.nombre, c.fecha_creacion 
            FROM categorias c
            INNER JOIN usuario_categoria uc ON c.id_categoria = uc.id_categoria
            WHERE uc.id_usuario = ?
            ORDER BY c.nombre
            """;
        
        List<Categoria> categorias = new ArrayList<>();
        
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    categorias.add(mapearCategoria(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar categorías por usuario: " + e.getMessage());
        }
        
        return categorias;
    }
    
    /**
     * Asociar categoría con usuario
     */
    public boolean asociarConUsuario(int idCategoria, int idUsuario) {
        String sql = "INSERT IGNORE INTO usuario_categoria (id_usuario, id_categoria) VALUES (?, ?)";
        
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idCategoria);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al asociar categoría con usuario: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Desasociar categoría de usuario
     */
    public boolean desasociarDeUsuario(int idCategoria, int idUsuario) {
        String sql = "DELETE FROM usuario_categoria WHERE id_usuario = ? AND id_categoria = ?";
        
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idCategoria);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al desasociar categoría de usuario: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Obtener vista de categorías con estadísticas
     */
    public List<CategoriaDTO> obtenerVistaCategorias() {
        List<CategoriaDTO> categorias = new ArrayList<>();
        String sql = "SELECT id_categoria, nombre, fecha_creacion, usuarios_usando, total_eventos, usuarios FROM v_categorias";
        
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                CategoriaDTO dto = new CategoriaDTO(
                    rs.getInt("id_categoria"),
                    rs.getString("nombre"),
                    rs.getTimestamp("fecha_creacion").toLocalDateTime(),
                    rs.getLong("usuarios_usando"),
                    rs.getLong("total_eventos"),
                    rs.getString("usuarios")
                );
                categorias.add(dto);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener vista de categorías: " + e.getMessage());
        }
        
        return categorias;
    }
    
    /**
     * Obtener vista de categorías por usuario
     */
    public List<CategoriaDTO> obtenerVistaCategoriasUsuario(int idUsuario) {
        List<CategoriaDTO> categorias = new ArrayList<>();
        String sql = "SELECT id_usuario, usuario, id_categoria, categoria, eventos_en_categoria FROM v_categorias_usuario WHERE id_usuario = ?";
        
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CategoriaDTO dto = new CategoriaDTO(
                        rs.getInt("id_usuario"),
                        rs.getString("usuario"),
                        rs.getInt("id_categoria"),
                        rs.getString("categoria"),
                        rs.getLong("eventos_en_categoria")
                    );
                    categorias.add(dto);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener vista de categorías por usuario: " + e.getMessage());
        }
        
        return categorias;
    }
    
    /**
     * Verificar si existe una categoría con el nombre dado
     */
    public boolean existeNombre(String nombre) {
        String sql = "SELECT COUNT(*) FROM categorias WHERE nombre = ?";
        
        try (Connection conn = conexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombre);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al verificar nombre de categoría: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Mapea un ResultSet a un objeto Categoria
     */
    private Categoria mapearCategoria(ResultSet rs) throws SQLException {
        Categoria categoria = new Categoria();
        categoria.setIdCategoria(rs.getInt("id_categoria"));
        categoria.setNombre(rs.getString("nombre"));
        
        Timestamp fechaCreacion = rs.getTimestamp("fecha_creacion");
        if (fechaCreacion != null) {
            categoria.setFechaCreacion(fechaCreacion.toLocalDateTime());
        }
        
        return categoria;
    }
}