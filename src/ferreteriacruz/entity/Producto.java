/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ferreteriacruz.entity;

/**
 *
 * @author USUARIO
 */

public class Producto {

    private int idProducto;
    private String nombre;
    private String sku;
    private double precio;
    private int stock;
    private int stockMinimo;
    private boolean activo;

    // Relaciones
    private Marca marca;
    private Categoria categoria;

    public Producto() {
    }

    public Producto(int idProducto, String nombre, String sku, double precio,
                    int stock, int stockMinimo, boolean activo,
                    Marca marca, Categoria categoria) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.sku = sku;
        this.precio = precio;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
        this.activo = activo;
        this.marca = marca;
        this.categoria = categoria;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
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

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}