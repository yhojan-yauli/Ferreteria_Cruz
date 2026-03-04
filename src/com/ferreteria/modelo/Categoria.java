package com.ferreteria.modelo;

public class Categoria {
    
    private int idCategoria;
    private String nombre;
    private String descripcion;
    private boolean activo;

    // Constructor vacío
    public Categoria() {
    }

    // Constructor con parámetros (opcional, pero útil)
    public Categoria(int idCategoria, String nombre, String descripcion, boolean activo) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activo = activo;
    }

    // ==========================================
    // GETTERS Y SETTERS
    // ==========================================
    
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    // El toString() es vital para que al meter la categoría en un 
    // JComboBox, se muestre el nombre y no un código raro de memoria.
    @Override
    public String toString() {
        return this.nombre;
    }
}