
package com.ferreteria.modelo;

public class DetalleVenta {
    
    private int idDetalle;
    private int idVenta;      // FK hacia la cabecera
    private int idProducto;   // FK hacia el producto
    private int cantidad;
    // Usamos double para precios. En sistemas financieros muy estrictos se usaría BigDecimal.
    private double precioUnitario; 
    private double subtotal;

    // Atributos auxiliares (NO están en la tabla detalle_ventas, pero sirven para mostrar en la tabla de la pantalla de ventas)
    private String nombreProductoAux; // Para mostrar el nombre en el carrito
    private String codigoProductoAux; // Para mostrar el código en el carrito

    public DetalleVenta() {
    }

    // --- GETTERS Y SETTERS ---

    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    // Getters y Setters auxiliares
    public String getNombreProductoAux() { return nombreProductoAux; }
    public void setNombreProductoAux(String nombreProductoAux) { this.nombreProductoAux = nombreProductoAux; }
    public String getCodigoProductoAux() { return codigoProductoAux; }
    public void setCodigoProductoAux(String codigoProductoAux) { this.codigoProductoAux = codigoProductoAux; }
    
    // Método útil para calcular el subtotal automáticamente antes de guardar
    public void calcularSubtotal() {
        this.subtotal = this.cantidad * this.precioUnitario;
    }
}