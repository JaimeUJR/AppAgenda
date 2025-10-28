package com.agenda.agendapersonal.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Evento {

    private int idEvento;
    private int idUsuario;
    private String titulo;
    private String descripcion;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String ubicacion;
    private boolean recordatorio;
    private EstadoEvento estado;
    private LocalDateTime fechaCreacion;
    private List<Categoria> categorias;

    // Formateadores de fecha
    private static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Constructores
    public Evento() {
        this.estado = EstadoEvento.PENDIENTE;
        this.fechaCreacion = LocalDateTime.now();
        this.categorias = new ArrayList<>();
        this.recordatorio = false;
    }

    public Evento(String titulo, LocalDateTime fechaInicio) {
        this();
        this.titulo = titulo;
        this.fechaInicio = fechaInicio;
    }

    public Evento(int idUsuario, String titulo, String descripcion, LocalDateTime fechaInicio,
            LocalDateTime fechaFin, String ubicacion, boolean recordatorio) {
        this();
        this.idUsuario = idUsuario;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.ubicacion = ubicacion;
        this.recordatorio = recordatorio;
    }

    // Getters y Setters
    public int getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
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

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public boolean isRecordatorio() {
        return recordatorio;
    }

    public void setRecordatorio(boolean recordatorio) {
        this.recordatorio = recordatorio;
    }

    public EstadoEvento getEstado() {
        return estado;
    }

    public void setEstado(EstadoEvento estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias != null ? categorias : new ArrayList<>();
    }

    // Métodos de utilidad para categorías
    public void agregarCategoria(Categoria categoria) {
        if (categoria != null && !this.categorias.contains(categoria)) {
            this.categorias.add(categoria);
        }
    }

    public void removerCategoria(Categoria categoria) {
        this.categorias.remove(categoria);
    }

    public boolean tieneCategoria(Categoria categoria) {
        return this.categorias.contains(categoria);
    }

    public String getCategoriasTexto() {
        if (categorias.isEmpty()) {
            return "Sin categoría";
        }
        return categorias.stream()
                .map(Categoria::getNombre)
                .reduce((a, b) -> a + ", " + b)
                .orElse("Sin categoría");
    }

    // Métodos de formato de fechas
    public String getFechaInicioFormateada() {
        return fechaInicio != null ? fechaInicio.format(FORMATO_FECHA_HORA) : "";
    }

    public String getFechaFinFormateada() {
        return fechaFin != null ? fechaFin.format(FORMATO_FECHA_HORA) : "";
    }

    public String getFechaFormateada() {
        if (fechaInicio == null)
            return "";

        if (fechaFin == null) {
            return fechaInicio.format(FORMATO_FECHA_HORA);
        }

        // Si es el mismo día, mostrar solo la fecha una vez
        if (fechaInicio.toLocalDate().equals(fechaFin.toLocalDate())) {
            return String.format("%s %s - %s",
                    fechaInicio.format(FORMATO_FECHA),
                    fechaInicio.format(DateTimeFormatter.ofPattern("HH:mm")),
                    fechaFin.format(DateTimeFormatter.ofPattern("HH:mm")));
        } else {
            return String.format("%s - %s",
                    fechaInicio.format(FORMATO_FECHA_HORA),
                    fechaFin.format(FORMATO_FECHA_HORA));
        }
    }

    // Métodos de validación
    public boolean esValido() {
        return titulo != null && !titulo.trim().isEmpty() && fechaInicio != null;
    }

    public boolean esFechaValida() {
        if (fechaInicio == null)
            return false;
        if (fechaFin == null)
            return true; // Fecha fin es opcional
        return fechaFin.isAfter(fechaInicio) || fechaFin.equals(fechaInicio);
    }

    public boolean esHoy() {
        if (fechaInicio == null)
            return false;
        return fechaInicio.toLocalDate().equals(LocalDateTime.now().toLocalDate());
    }

    public boolean esFuturo() {
        if (fechaInicio == null)
            return false;
        return fechaInicio.isAfter(LocalDateTime.now());
    }

    public boolean esPasado() {
        if (fechaInicio == null)
            return false;
        return fechaInicio.isBefore(LocalDateTime.now());
    }

    // Métodos de Object
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Evento evento = (Evento) obj;
        return idEvento == evento.idEvento;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEvento);
    }

    @Override
    public String toString() {
        return String.format("Evento{id=%d, titulo='%s', fecha=%s, estado=%s}",
                idEvento, titulo, getFechaFormateada(), estado.getDescripcion());
    }

    /**
     * Representación para mostrar en listas
     */
    public String getTextoParaLista() {
        String icono = recordatorio ? "🔔" : "🔕";
        return String.format("%s %s %s - %s",
                estado.getIcono(), icono, titulo, getFechaFormateada());
    }
}