/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ferreteriacruz.Services;

/**
 *
 * @author USUARIO
 */

import ferreteriacruz.dao.ProductoDAO;
import ferreteriacruz.entity.Producto;

import java.sql.SQLException;
import java.util.List;

public class ProductoService {

    private final ProductoDAO dao = new ProductoDAO();

    public void registrarProducto(Producto p) throws SQLException {
        if (p.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }
        if (p.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        dao.registrar(p);
    }

    public void actualizarProducto(Producto p) throws SQLException {
        dao.actualizar(p);
    }

    public void eliminarProducto(int idProducto) throws SQLException {
        dao.desactivar(idProducto);
    }

    public List<Producto> listarProductos() throws SQLException {
        return dao.listar();
    }

    public List<Producto> buscarPorNombre(String nombre) throws SQLException {
        return dao.buscarPorNombre(nombre);
    }

    public List<Producto> buscarPorPrecio(double min, double max) throws SQLException {
        return dao.buscarPorPrecio(min, max);
    }

    // ALERTA DE STOCK MÍNIMO (Sprint 1)
    public boolean stockCritico(Producto p) {
        return p.getStock() <= p.getStockMinimo();
    }
}