package com.ferreteria.modelo;

import java.sql.Timestamp; // Usamos Timestamp porque la BD tiene DATETIME (fecha + hora)
import java.util.ArrayList;
import java.util.List;

public class Venta {
    
    private int idVenta;
    private String numeroComprobante;
    private Timestamp fechaVenta;
    private double totalVenta;
    private int idUsuario; // El vendedor
    private int idCliente; // El cliente (puede ser 0 si es cliente genérico/nulo)
    private String estado; // 'COMPLETADA', 'ANULADA'
private String metodoPago;
    // --- ATRIBUTO IMPORTANTE PARA LA APLICACIÓN ---
    // Una venta contiene una lista de detalles. 
    // Esto no es una columna en la tabla 'ventas', es una estructura de Java.
    private List<DetalleVenta> detalles;

    public Venta() {
        // Inicializamos la lista para evitar NullPointerException
        this.detalles = new ArrayList<>();
        this.estado = "COMPLETADA"; // Estado por defecto
    }

    // --- GETTERS Y SETTERS ---

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public String getNumeroComprobante() {
        return numeroComprobante;
    }

    public void setNumeroComprobante(String numeroComprobante) {
        this.numeroComprobante = numeroComprobante;
    }

    public Timestamp getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(Timestamp fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public double getTotalVenta() {
        return totalVenta;
    }

    public void setTotalVenta(double totalVenta) {
        this.totalVenta = totalVenta;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Getter y Setter para la lista de detalles
    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }
    
    // Método utilitario para agregar un detalle a la lista fácilmente
    public void agregarDetalle(DetalleVenta detalle) {
        this.detalles.add(detalle);
    }
    public String getMetodoPago() { return metodoPago; }
public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
}