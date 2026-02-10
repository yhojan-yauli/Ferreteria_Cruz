/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ferreteriacruz.dao;

import ferreteriacruz.Conexion.Conexion;
import ferreteriacruz.entity.Usuario;
import java.sql.*;

public class UsuarioDAO {

    public Usuario login(String email, String password) {

        Usuario usuario = null;
        String sql = "SELECT * FROM usuarios WHERE email = ? AND password = ?";

            try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setEmail(rs.getString("email"));
                usuario.setRol(rs.getString("rol"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuario;
    }
}