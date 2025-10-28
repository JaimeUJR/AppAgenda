package com.agenda.agendapersonal.modelo;

/**
 * DTO (Data Transfer Object) para representar los datos de la vista v_eventos
 * Esta clase corresponde a la vista de la base de datos que une eventos con
 * usuarios y categorías
 * 
 * @author JaimeSQL
 */
public class EventoDTO {

    private int idEvento;
    private String titulo;
    private String descripcion;
    private String fechaInicio; // Ya formateada desde la vista
    private String fechaFin; // Ya formateada desde la vista
    private String ubicacion;
    private String recordatorio; // "🔔 Sí" o "🔕 No"
    private String estado; // Estado como string
    private String usuario; // Nombre completo del usuario
    private String categorias; // Categorías concatenadas

    // Constructores
    public EventoDTO() {
    }

    public EventoDTO(int idEvento, String titulo, String descripcion, String fechaInicio,
            String fechaFin, String ubicacion, String recordatorio, String estado,
            String usuario, String categorias) {
        this.idEvento = idEvento;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.ubicacion = ubicacion;
        this.recordatorio = recordatorio;
        this.estado = estado;
        this.usuario = usuario;
        this.categorias = categorias;
    }

    // Getters y Setters
    public int getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getRecordatorio() {
        return recordatorio;
    }

    public void setRecordatorio(String recordatorio) {
        this.recordatorio = recordatorio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getCategorias() {
        return categorias;
    }

    public void setCategorias(String categorias) {
        this.categorias = categorias;
    }

    // Métodos de utilidad
    public boolean tieneRecordatorio() {
        return recordatorio != null && recordatorio.contains("🔔");
    }

    public String getFechaCompleta() {
        if (fechaFin == null || fechaFin.isEmpty()) {
            return fechaInicio;
        }
        return fechaInicio + " - " + fechaFin;
    }

    public String getResumen() {
        StringBuilder sb = new StringBuilder();
        sb.append(titulo);

        if (ubicacion != null && !ubicacion.isEmpty()) {
            sb.append(" (").append(ubicacion).append(")");
        }

        if (categorias != null && !categorias.isEmpty()) {
            sb.append(" [").append(categorias).append("]");
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("EventoDTO{id=%d, titulo='%s', fecha='%s', usuario='%s'}",
                idEvento, titulo, fechaInicio, usuario);
    }
}