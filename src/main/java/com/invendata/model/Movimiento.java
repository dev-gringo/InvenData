package com.invendata.model;

import java.sql.Timestamp;

public class Movimiento {
    private int id;
    private int productoId;
    private int usuarioId;
    private String tipo; // ENTRADA o SALIDA
    private int cantidad;
    private Timestamp fecha;

    // Constructor vacío
    public Movimiento() {
    }

    // Constructor con parámetros
    public Movimiento(int id, int productoId, int usuarioId, String tipo, int cantidad, Timestamp fecha) {
        this.id = id;
        this.productoId = productoId;
        this.usuarioId = usuarioId;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.fecha = fecha;
    }

    // Métodos Getter y Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }
}