/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ferreteriacruz.dao;

/**
 *
 * @author USUARIO
 */
import ferreteriacruz.Conexion.Conexion;
import ferreteriacruz.entity.Categoria;
import ferreteriacruz.entity.Marca;
import ferreteriacruz.entity.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    // CREAR
    public void registrar(Producto p) throws SQLException {
        String sql = """
            INSERT INTO producto
            (nombre, sku, precio, stock, stock_minimo, activo, id_marca, id_categoria)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getSku());
            ps.setDouble(3, p.getPrecio());
            ps.setInt(4, p.getStock());
            ps.setInt(5, p.getStockMinimo());
            ps.setBoolean(6, p.isActivo());
            ps.setInt(7, p.getMarca().getIdMarca());
            ps.setInt(8, p.getCategoria().getIdCategoria());

            ps.executeUpdate();
        }
    }

    // ACTUALIZAR
    public void actualizar(Producto p) throws SQLException {
        String sql = """
            UPDATE producto SET
            nombre=?, sku=?, precio=?, stock=?, stock_minimo=?,
            id_marca=?, id_categoria=?
            WHERE id_producto=?
        """;

        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getSku());
            ps.setDouble(3, p.getPrecio());
            ps.setInt(4, p.getStock());
            ps.setInt(5, p.getStockMinimo());
            ps.setInt(6, p.getMarca().getIdMarca());
            ps.setInt(7, p.getCategoria().getIdCategoria());
            ps.setInt(8, p.getIdProducto());

            ps.executeUpdate();
        }
    }

    // ELIMINACIÓN LÓGICA
    public void desactivar(int idProducto) throws SQLException {
        String sql = "UPDATE producto SET activo=false WHERE id_producto=?";

        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProducto);
            ps.executeUpdate();
        }
    }

    // LISTAR
    public List<Producto> listar() throws SQLException {

        List<Producto> lista = new ArrayList<>();

        String sql = """
        SELECT p.*, 
               m.id_marca, m.nombre AS marca_nombre,
               c.id_categoria, c.nombre AS categoria_nombre
        FROM producto p
        JOIN marca m ON p.id_marca = m.id_marca
        JOIN categoria c ON p.id_categoria = c.id_categoria
        WHERE p.activo = true
    """;

        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Marca m = new Marca();
                m.setIdMarca(rs.getInt("id_marca"));
                m.setNombre(rs.getString("marca_nombre"));

                Categoria c = new Categoria();
                c.setIdCategoria(rs.getInt("id_categoria"));
                c.setNombre(rs.getString("categoria_nombre"));

                Producto p = new Producto();
                p.setIdProducto(rs.getInt("id_producto"));
                p.setNombre(rs.getString("nombre"));
                p.setSku(rs.getString("sku"));
                p.setPrecio(rs.getDouble("precio"));
                p.setStock(rs.getInt("stock"));
                p.setStockMinimo(rs.getInt("stock_minimo"));
                p.setActivo(rs.getBoolean("activo"));
                p.setMarca(m);
                p.setCategoria(c);

                lista.add(p);
            }
        }
        return lista;
    }

    // BÚSQUEDA POR NOMBRE
    
    public List<Producto> buscarPorNombre(String nombre) throws SQLException {

    List<Producto> lista = new ArrayList<>();

    String sql = """
        SELECT p.*, 
               m.id_marca, m.nombre AS marca_nombre,
               c.id_categoria, c.nombre AS categoria_nombre
        FROM producto p
        JOIN marca m ON p.id_marca = m.id_marca
        JOIN categoria c ON p.id_categoria = c.id_categoria
        WHERE p.nombre LIKE ? AND p.activo = true
    """;

    try (Connection con = Conexion.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, "%" + nombre + "%");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            Marca m = new Marca(
                rs.getInt("id_marca"),
                rs.getString("marca_nombre"),
                true
            );

            Categoria c = new Categoria(
                rs.getInt("id_categoria"),
                rs.getString("categoria_nombre"),
                true
            );

            Producto p = new Producto();
            p.setIdProducto(rs.getInt("id_producto"));
            p.setNombre(rs.getString("nombre"));
            p.setPrecio(rs.getDouble("precio"));
            p.setMarca(m);
            p.setCategoria(c);

            lista.add(p);
        }
    }
    return lista;
}

    // BÚSQUEDA POR RANGO DE PRECIOS
   public List<Producto> buscarPorPrecio(double min, double max) throws SQLException {

    List<Producto> lista = new ArrayList<>();

    String sql = """
        SELECT p.*, 
               m.id_marca, m.nombre AS marca_nombre,
               c.id_categoria, c.nombre AS categoria_nombre
        FROM producto p
        JOIN marca m ON p.id_marca = m.id_marca
        JOIN categoria c ON p.id_categoria = c.id_categoria
        WHERE p.precio BETWEEN ? AND ? AND p.activo = true
    """;

    try (Connection con = Conexion.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setDouble(1, min);
        ps.setDouble(2, max);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            Producto p = new Producto();
            p.setIdProducto(rs.getInt("id_producto"));
            p.setNombre(rs.getString("nombre"));
            p.setPrecio(rs.getDouble("precio"));
            p.setStock(rs.getInt("stock"));

            lista.add(p);
        }
    }
    return lista;
}
    
    
}
