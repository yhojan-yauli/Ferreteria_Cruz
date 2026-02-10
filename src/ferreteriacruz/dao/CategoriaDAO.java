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

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    // CREAR
    public void registrar(Categoria c) throws SQLException {
        String sql = "INSERT INTO categoria (nombre, activo) VALUES (?, true)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getNombre());
            ps.executeUpdate();
        }
    }

    // ACTUALIZAR
    public void actualizar(Categoria c) throws SQLException {
        String sql = "UPDATE categoria SET nombre=? WHERE id_categoria=?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getNombre());
            ps.setInt(2, c.getIdCategoria());
            ps.executeUpdate();
        }
    }

    // ELIMINACIÓN LÓGICA
    public void desactivar(int idCategoria) throws SQLException {
        String sql = "UPDATE categoria SET activo=false WHERE id_categoria=?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCategoria);
            ps.executeUpdate();
        }
    }

    // LISTAR
    public List<Categoria> listar() throws SQLException {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM categoria WHERE activo=true";

        try (Connection con = Conexion.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Categoria c = new Categoria();
                c.setIdCategoria(rs.getInt("id_categoria"));
                c.setNombre(rs.getString("nombre"));
                c.setActivo(rs.getBoolean("activo"));
                lista.add(c);
            }
        }
        return lista;
    }
}