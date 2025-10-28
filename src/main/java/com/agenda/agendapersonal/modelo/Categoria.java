package com.agenda.agendapersonal.modelo;

import java.time.LocalDateTime;
import java.util.Objects;

public class Categoria {

    private int idCategoria;
    private String nombre;
    private LocalDateTime fechaCreacion;

    // Constructores
    public Categoria() {
        this.fechaCreacion = LocalDateTime.now();
    }

    public Categoria(String nombre) {
        this();
        this.nombre = nombre;
    }

    public Categoria(int idCategoria, String nombre) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
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

    // Métodos de utilidad
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Categoria categoria = (Categoria) obj;
        return idCategoria == categoria.idCategoria;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCategoria);
    }

    @Override
    public String toString() {
        return nombre; // Para mostrar en ComboBox y listas
    }

    /**
     * Representación completa del objeto
     */
    public String toStringCompleto() {
        return String.format("Categoria{id=%d, nombre='%s', fechaCreacion=%s}",
                idCategoria, nombre, fechaCreacion);
    }

    /**
     * Verifica si el nombre de la categoría es válido
     */
    public boolean esNombreValido() {
        return nombre != null && !nombre.trim().isEmpty() && nombre.length() <= 50;
    }
}