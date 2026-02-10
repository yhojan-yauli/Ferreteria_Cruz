/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ferreteriacruz.Services;

/**
 *
 * @author USUARIO
 */

import ferreteriacruz.dao.MarcaDAO;
import ferreteriacruz.entity.Marca;

import java.sql.SQLException;
import java.util.List;

public class MarcaService {

    private final MarcaDAO dao = new MarcaDAO();

    public void registrarMarca(Marca m) throws SQLException {
        if (m.getNombre() == null || m.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la marca es obligatorio");
        }
        dao.registrar(m);
    }

    public void actualizarMarca(Marca m) throws SQLException {
        dao.actualizar(m);
    }

    public void eliminarMarca(int idMarca) throws SQLException {
        dao.desactivar(idMarca);
    }

    public List<Marca> listarMarcas() throws SQLException {
        return dao.listar();
    }
    
    public List<Marca> buscarMarcas(String nombre) throws SQLException {
    return dao.buscarPorNombre(nombre);
}
}