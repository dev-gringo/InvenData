package com.invendata.model;

import java.sql.Timestamp;

/**
 * Clase Modelo que representa un movimiento de inventario.
 * Se han añadido campos para nombres (Producto/Usuario) para facilitar
 * la visualización en reportes y el historial.
 */
public class Movimiento {
    private int id;
    private int productoId;
    private int usuarioId;
    private String tipo; // ENTRADA o SALIDA
    private int cantidad;
    private Timestamp fecha;
    private String nombreProducto; // Para mostrar el nombre en lugar del ID
    private String nombreUsuario;  // Para saber quién hizo el movimiento

    // Constructor vacío
    public Movimiento() {
    }

    // Constructor con parámetros (actualizado)
    public Movimiento(int id, int productoId, int usuarioId, String tipo, int cantidad, Timestamp fecha) {
        this.id = id;
        this.productoId = productoId;
        this.usuarioId = usuarioId;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.fecha = fecha;
    }

    // --- MÉTODOS GETTER Y SETTER (Existentes) ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProductoId() { return productoId; }
    public void setProductoId(int productoId) { this.productoId = productoId; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public Timestamp getFecha() { return fecha; }
    public void setFecha(Timestamp fecha) { this.fecha = fecha; }

    // --- NUEVOS GETTERS Y SETTERS PARA EL HISTORIAL ---
    
    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
}