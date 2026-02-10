package ferreteriacruz.Services;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author USUARIO
 */


import ferreteriacruz.dao.CategoriaDAO;
import ferreteriacruz.entity.Categoria;

import java.sql.SQLException;
import java.util.List;

public class CategoriaService {

    private final CategoriaDAO dao = new CategoriaDAO();

    public void registrarCategoria(Categoria c) throws SQLException {
        if (c.getNombre() == null || c.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio");
        }
        dao.registrar(c);
    }

    public void actualizarCategoria(Categoria c) throws SQLException {
        dao.actualizar(c);
    }

    public void eliminarCategoria(int idCategoria) throws SQLException {
        dao.desactivar(idCategoria);
    }

    public List<Categoria> listarCategorias() throws SQLException {
        return dao.listar();
    }
}