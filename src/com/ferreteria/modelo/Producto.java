package com.ferreteria.modelo;

public class Producto {

    // Atributos viejos (Sprint 1)
    private int idProducto;
    private String codigo;
    private String nombre;
    private String marca; // Ya la tenías, pero aseguramos
    private double precioVenta;
    private int stockActual;
    private int stockMinimo; // Ya lo tenías, pero aseguramos
    private boolean activo;
private String unidadMedida;

    // --- ATRIBUTOS NUEVOS (SPRINT 2) QUE FALTABAN ---
    private String descripcion;
    private double precioCompra; // Costo
    private String ubicacion;    // HU-19
    
    // Claves Foráneas (FK)
    private int idCategoria;     // HU-02
    private int idProveedor;     // HU-11

    // Constructor vacío
    public Producto() {
    }

    // ==================================================================
    // GETTERS Y SETTERS (Estos son los que el compilador no encontraba)
    // ==================================================================

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    // NUEVO
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    // NUEVO
    public double getPrecioCompra() { return precioCompra; }
    public void setPrecioCompra(double precioCompra) { this.precioCompra = precioCompra; }

    public double getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(double precioVenta) { this.precioVenta = precioVenta; }

    public int getStockActual() { return stockActual; }
    public void setStockActual(int stockActual) { this.stockActual = stockActual; }

    public int getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }

    // NUEVO (HU-19)
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    // NUEVO (HU-02)
    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    // NUEVO (HU-11)
    public int getIdProveedor() { return idProveedor; }
    public void setIdProveedor(int idProveedor) { this.idProveedor = idProveedor; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    // Método toString útil para depurar
    @Override
    public String toString() {
        return nombre; 
    }
}