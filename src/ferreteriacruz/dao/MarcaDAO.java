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
import ferreteriacruz.entity.Marca;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MarcaDAO {

    // CREAR
    public void registrar(Marca m) throws SQLException {
        String sql = "INSERT INTO marca (nombre, activo) VALUES (?, true)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, m.getNombre());
            ps.executeUpdate();
        }
    }

    // ACTUALIZAR
    public void actualizar(Marca m) throws SQLException {
        String sql = "UPDATE marca SET nombre=? WHERE id_marca=?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, m.getNombre());
            ps.setInt(2, m.getIdMarca());
            ps.executeUpdate();
        }
    }

    // ELIMINACIÓN LÓGICA
    public void desactivar(int idMarca) throws SQLException {
        String sql = "UPDATE marca SET activo=false WHERE id_marca=?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idMarca);
            ps.executeUpdate();
        }
    }

    // LISTAR
    public List<Marca> listar() throws SQLException {
        List<Marca> lista = new ArrayList<>();
        String sql = "SELECT * FROM marca WHERE activo=true";

        try (Connection con = Conexion.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Marca m = new Marca();
                m.setIdMarca(rs.getInt("id_marca"));
                m.setNombre(rs.getString("nombre"));
                m.setActivo(rs.getBoolean("activo"));
                lista.add(m);
            }
        }
        return lista;
    }
    
    
    
    public List<Marca> buscarPorNombre(String nombre) throws SQLException {

    List<Marca> lista = new ArrayList<>();
    String sql = "SELECT * FROM marca WHERE nombre LIKE ? AND activo=true";

    try (Connection con = Conexion.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, "%" + nombre + "%");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Marca m = new Marca();
            m.setIdMarca(rs.getInt("id_marca"));
            m.setNombre(rs.getString("nombre"));
            m.setActivo(rs.getBoolean("activo"));
            lista.add(m);
        }
    }
    return lista;
}
  
}