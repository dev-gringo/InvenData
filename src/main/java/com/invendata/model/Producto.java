package com.invendata.model;

public class Producto {
    private int id;
    private String nombre;
    private String categoria;
    private double precio;
    private int stock;
    private int stockMinimo;
    private String estado; // ACTIVO o INACTIVO (Borrado lógico)

    // Constructor vacío
    public Producto() {
    }

    // Constructor con parámetros (Para crear productos nuevos)
    public Producto(int id, String nombre, String categoria, double precio, int stock, int stockMinimo, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
        this.estado = estado;
    }

    // Métodos Getter y Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}