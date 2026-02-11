package com.ferreteria.dao;

import com.ferreteria.modelo.Producto;
import com.ferreteria.util.ComboBoxItem;
import java.util.List;

public interface ProductoDAO {
    
    // --- MÉTODOS BÁSICOS (CRUD) ---
    boolean registrar(Producto p);
    boolean modificar(Producto p);
    boolean eliminarLogico(int idProducto);
    
    // --- MÉTODOS DE BÚSQUEDA Y LISTADO ---
    Producto obtenerPorId(int id);
    Producto buscarPorCodigo(String codigo);
    List<Producto> listarTodos(boolean incluirInactivos);
    
    // --- MÉTODOS ESPECÍFICOS PARA EL NEGOCIO ---
    boolean actualizarStock(int idProducto, int nuevoStock);
    List<ComboBoxItem> listarCategoriasCombo();
    List<Producto> listarPorCategoria(int idCategoria);
    
    // --- MÉTODOS NUEVOS (Buscadores Inteligentes) ---
    
    /**
     * Busca productos para la pantalla de VENTAS.
     * Filtra por nombre o código.
     */
    List<Producto> buscarProductosParaVenta(String texto);

    /**
     * Busca productos para la pantalla de INVENTARIO.
     * Filtra por nombre, código o marca.
     */
    List<Producto> buscarPorCriterio(String criterio);
}