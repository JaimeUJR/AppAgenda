package com.agenda.agendapersonal.modelo;

import java.time.LocalDateTime;
import java.util.Objects;

public class Usuario {

    private int idUsuario;
    private String nombreUsuario;
    private String email;
    private String password;
    private String nombreCompleto;
    private LocalDateTime fechaRegistro;
    private LocalDateTime ultimoAcceso;

    // Constructores
    public Usuario() {
        this.fechaRegistro = LocalDateTime.now();
    }

    public Usuario(String nombreUsuario, String email, String password, String nombreCompleto) {
        this();
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.password = password;
        this.nombreCompleto = nombreCompleto;
    }

    public Usuario(int idUsuario, String nombreUsuario, String email, String nombreCompleto) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.nombreCompleto = nombreCompleto;
    }

    // Getters y Setters
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDateTime getUltimoAcceso() {
        return ultimoAcceso;
    }

    public void setUltimoAcceso(LocalDateTime ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    // Métodos de utilidad
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Usuario usuario = (Usuario) obj;
        return idUsuario == usuario.idUsuario;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUsuario);
    }

    @Override
    public String toString() {
        return String.format("Usuario{id=%d, usuario='%s', email='%s', nombre='%s'}",
                idUsuario, nombreUsuario, email, nombreCompleto);
    }

    /**
     * Verifica si el usuario tiene un email válido
     */
    public boolean tieneEmailValido() {
        return email != null && email.contains("@") && email.contains(".");
    }

    /**
     * Retorna el nombre para mostrar (nombre completo o usuario)
     */
    public String getNombreParaMostrar() {
        return nombreCompleto != null && !nombreCompleto.isEmpty() ? nombreCompleto : nombreUsuario;
    }
}