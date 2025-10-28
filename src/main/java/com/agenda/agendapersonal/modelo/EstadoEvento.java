package com.agenda.agendapersonal.modelo;

public enum EstadoEvento {

    PENDIENTE("pendiente", "Pendiente", "⏳"),
    COMPLETADO("completado", "Completado", "✅"),
    CANCELADO("cancelado", "Cancelado", "❌");

    private final String valor;
    private final String descripcion;
    private final String icono;

    EstadoEvento(String valor, String descripcion, String icono) {
        this.valor = valor;
        this.descripcion = descripcion;
        this.icono = icono;
    }

    public String getValor() {
        return valor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getIcono() {
        return icono;
    }

    /**
     * Obtiene el estado desde su valor en base de datos
     */
    public static EstadoEvento fromValor(String valor) {
        for (EstadoEvento estado : EstadoEvento.values()) {
            if (estado.valor.equals(valor)) {
                return estado;
            }
        }
        return PENDIENTE; // Por defecto
    }

    /**
     * Representación para mostrar en UI
     */
    public String getTextoCompleto() {
        return icono + " " + descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}