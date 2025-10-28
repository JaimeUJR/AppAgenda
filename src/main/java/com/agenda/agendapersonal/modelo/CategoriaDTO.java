package com.agenda.agendapersonal.modelo;

import java.time.LocalDateTime;

public class CategoriaDTO {

    private int idCategoria;
    private String nombre;
    private LocalDateTime fechaCreacion;
    private long usuariosUsando; // Para v_categorias
    private long totalEventos; // Para v_categorias
    private String usuarios; // Para v_categorias (usuarios concatenados)
    private int idUsuario; // Para v_categorias_usuario
    private String usuario; // Para v_categorias_usuario
    private long eventosEnCategoria; // Para v_categorias_usuario

    // Constructores
    public CategoriaDTO() {
    }

    // Constructor para v_categorias
    public CategoriaDTO(int idCategoria, String nombre, LocalDateTime fechaCreacion,
            long usuariosUsando, long totalEventos, String usuarios) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.fechaCreacion = fechaCreacion;
        this.usuariosUsando = usuariosUsando;
        this.totalEventos = totalEventos;
        this.usuarios = usuarios;
    }

    // Constructor para v_categorias_usuario
    public CategoriaDTO(int idUsuario, String usuario, int idCategoria, String nombre,
            long eventosEnCategoria) {
        this.idUsuario = idUsuario;
        this.usuario = usuario;
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.eventosEnCategoria = eventosEnCategoria;
    }

    // Getters y Setters
    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public long getUsuariosUsando() {
        return usuariosUsando;
    }

    public void setUsuariosUsando(long usuariosUsando) {
        this.usuariosUsando = usuariosUsando;
    }

    public long getTotalEventos() {
        return totalEventos;
    }

    public void setTotalEventos(long totalEventos) {
        this.totalEventos = totalEventos;
    }

    public String getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(String usuarios) {
        this.usuarios = usuarios;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public long getEventosEnCategoria() {
        return eventosEnCategoria;
    }

    public void setEventosEnCategoria(long eventosEnCategoria) {
        this.eventosEnCategoria = eventosEnCategoria;
    }

    // Métodos de utilidad
    public String getEstadisticas() {
        if (usuariosUsando > 0 || totalEventos > 0) {
            return String.format("%d usuario(s), %d evento(s)", usuariosUsando, totalEventos);
        }
        return "Sin uso";
    }

    public boolean tieneEventos() {
        return totalEventos > 0 || eventosEnCategoria > 0;
    }

    public boolean esUsadaPorVariosUsuarios() {
        return usuariosUsando > 1;
    }

    public String getDescripcionCompleta() {
        StringBuilder sb = new StringBuilder();
        sb.append(nombre);

        if (totalEventos > 0) {
            sb.append(" (").append(totalEventos).append(" eventos)");
        } else if (eventosEnCategoria > 0) {
            sb.append(" (").append(eventosEnCategoria).append(" eventos)");
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return nombre; // Para usar en ComboBox y listas
    }

    public String toStringDetallado() {
        return String.format("CategoriaDTO{id=%d, nombre='%s', usuarios=%d, eventos=%d}",
                idCategoria, nombre, usuariosUsando, totalEventos);
    }
}