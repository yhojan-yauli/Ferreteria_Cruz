package com.ferreteria.dao;

import com.ferreteria.modelo.Producto;
import com.ferreteria.util.ComboBoxItem;
import com.ferreteria.util.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAOImpl implements ProductoDAO {

    // --- CONSULTAS SQL ---
    private static final String SQL_INSERT = "INSERT INTO productos (codigo, nombre, marca, descripcion, precio_compra, precio_venta, stock_actual, stock_minimo, id_categoria, id_proveedor, unidad_medida, ubicacion, activo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String SQL_SELECT_ALL = "SELECT * FROM productos";
    
    private static final String SQL_SELECT_ACTIVE = "SELECT * FROM productos WHERE activo = true ORDER BY nombre ASC";
    
    private static final String SQL_SELECT_BY_CODE = "SELECT * FROM productos WHERE codigo = ? AND activo = true";
    
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM productos WHERE id_producto = ?";
    
    // CORREGIDO: Se arregló el error de sintaxis y se agregó stock_actual
    private static final String SQL_UPDATE = "UPDATE productos SET codigo=?, nombre=?, marca=?, descripcion=?, precio_compra=?, precio_venta=?, stock_actual=?, stock_minimo=?, id_categoria=?, id_proveedor=?, unidad_medida=?, ubicacion=? WHERE id_producto=?";
    
    private static final String SQL_DELETE_LOGIC = "UPDATE productos SET activo = false WHERE id_producto=?";
    
    private static final String SQL_UPDATE_STOCK = "UPDATE productos SET stock_actual = ? WHERE id_producto = ?";
    
    private static final String SQL_SELECT_CATEGORIAS_COMBO = "SELECT id_categoria, nombre FROM categorias ORDER BY nombre ASC";
    
    private static final String SQL_SELECT_BY_CAT = "SELECT * FROM productos WHERE id_categoria = ? AND activo = true ORDER BY nombre ASC";

    // --- CONSULTAS DE BÚSQUEDA ---
    private static final String SQL_SEARCH_FOR_SALE = "SELECT * FROM productos WHERE (nombre LIKE ? OR codigo LIKE ?) AND activo = true ORDER BY nombre ASC";
    private static final String SQL_SEARCH_CRITERIO = "SELECT * FROM productos WHERE (nombre LIKE ? OR codigo LIKE ? OR marca LIKE ?) AND activo = true ORDER BY nombre ASC";


    @Override
    public boolean registrar(Producto p) {
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT)) {
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getMarca());
            ps.setString(4, p.getDescripcion());
            ps.setDouble(5, p.getPrecioCompra());
            ps.setDouble(6, p.getPrecioVenta());
            ps.setInt(7, p.getStockActual());
            ps.setInt(8, p.getStockMinimo());
            ps.setInt(9, p.getIdCategoria());
            ps.setInt(10, p.getIdProveedor() > 0 ? p.getIdProveedor() : 1);
            
            // --- NUEVOS CAMPOS ---
            ps.setString(11, p.getUnidadMedida());
            ps.setString(12, p.getUbicacion());
            
            ps.setBoolean(13, true); // Activo
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar producto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Producto> listarTodos(boolean incluirInactivos) {
        List<Producto> lista = new ArrayList<>();
        String sql = incluirInactivos ? SQL_SELECT_ALL : SQL_SELECT_ACTIVE;
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearProducto(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar productos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public Producto buscarPorCodigo(String codigo) {
        Producto p = null;
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_CODE)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    p = mapearProducto(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error buscarPorCodigo: " + e.getMessage());
        }
        return p;
    }

    @Override
    public Producto obtenerPorId(int id) {
        Producto p = null;
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    p = mapearProducto(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error obtenerPorId: " + e.getMessage());
        }
        return p;
    }

    @Override
    public boolean modificar(Producto p) {
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_UPDATE)) {
            
            // Coincide con el orden del SQL_UPDATE definido arriba
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getMarca());
            ps.setString(4, p.getDescripcion());
            ps.setDouble(5, p.getPrecioCompra());
            ps.setDouble(6, p.getPrecioVenta());
            
            // CORREGIDO: Ahora sí guardamos el Stock Actual editado
            ps.setInt(7, p.getStockActual()); 
            
            ps.setInt(8, p.getStockMinimo());
            ps.setInt(9, p.getIdCategoria());
            ps.setInt(10, p.getIdProveedor() > 0 ? p.getIdProveedor() : 1);
            
            // --- NUEVOS CAMPOS ---
            ps.setString(11, p.getUnidadMedida());
            ps.setString(12, p.getUbicacion());
            
            // ID para el WHERE (El último parámetro)
            ps.setInt(13, p.getIdProducto());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al modificar: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminarLogico(int idProducto) {
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_DELETE_LOGIC)) {
            ps.setInt(1, idProducto);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizarStock(int idProducto, int nuevoStock) {
         try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_UPDATE_STOCK)) {
            ps.setInt(1, nuevoStock);
            ps.setInt(2, idProducto);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error actualizarStock: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public List<ComboBoxItem> listarCategoriasCombo() {
        List<ComboBoxItem> lista = new ArrayList<>();
        lista.add(new ComboBoxItem(0, "--- Seleccione Categoría ---"));
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_CATEGORIAS_COMBO);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new ComboBoxItem(rs.getInt("id_categoria"), rs.getString("nombre")));
            }
        } catch (SQLException e) {
            System.err.println("Error listarCategoriasCombo: " + e.getMessage());
        }
        return lista;
    }
    
    // Método opcional
    public List<Producto> listarPorCategoria(int idCategoria) {
        List<Producto> lista = new ArrayList<>();
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_CAT)) {
            ps.setInt(1, idCategoria);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearProducto(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error listarPorCategoria: " + e.getMessage());
        }
        return lista;
    }

    // ====================================================================
    // BUSCADORES
    // ====================================================================
    public List<Producto> buscarProductosParaVenta(String texto) {
        List<Producto> lista = new ArrayList<>();
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_SEARCH_FOR_SALE)) {

            String terminoBusqueda = "%" + texto + "%";
            ps.setString(1, terminoBusqueda); 
            ps.setString(2, terminoBusqueda); 

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearProducto(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar productos para venta: " + e.getMessage());
        }
        return lista;
    }

    public List<Producto> buscarPorCriterio(String criterio) {
        List<Producto> lista = new ArrayList<>();
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_SEARCH_CRITERIO)) {

            String terminoBusqueda = "%" + criterio + "%";
            ps.setString(1, terminoBusqueda); 
            ps.setString(2, terminoBusqueda); 
            ps.setString(3, terminoBusqueda); 

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearProducto(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar en inventario: " + e.getMessage());
        }
        return lista;
    }

    // ====================================================================
    // MAPEO
    // ====================================================================
    private Producto mapearProducto(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setIdProducto(rs.getInt("id_producto"));
        p.setCodigo(rs.getString("codigo"));
        p.setNombre(rs.getString("nombre"));
        p.setMarca(rs.getString("marca"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setPrecioCompra(rs.getDouble("precio_compra"));
        p.setPrecioVenta(rs.getDouble("precio_venta"));
        p.setStockActual(rs.getInt("stock_actual"));
        p.setStockMinimo(rs.getInt("stock_minimo"));
        p.setIdCategoria(rs.getInt("id_categoria"));
        p.setIdProveedor(rs.getInt("id_proveedor"));
        p.setActivo(rs.getBoolean("activo"));
        
        // Try-catch para evitar errores si las columnas nuevas no existen en BD antiguas
        try { p.setUnidadMedida(rs.getString("unidad_medida")); } catch(Exception e) {}
        try { p.setUbicacion(rs.getString("ubicacion")); } catch(Exception e) {}
        
        return p;
    }
}