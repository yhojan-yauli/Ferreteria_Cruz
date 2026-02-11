package com.ferreteria.dao;

import com.ferreteria.modelo.Usuario;
import com.ferreteria.util.ConexionDB;
import java.sql.*;

public class UsuarioDAOImpl {

    // Consulta que une Usuarios con Roles para saber qué permisos tiene
    private static final String SQL_LOGIN = 
            "SELECT u.*, r.nombre as nombre_rol " +
            "FROM usuarios u " +
            "INNER JOIN roles r ON u.id_rol = r.id_rol " +
            "WHERE u.username = ? AND u.password = ? AND u.activo = true";

    public Usuario login(String user, String pass) {
        Usuario usuario = null;
        
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_LOGIN)) {
            
            ps.setString(1, user);
            ps.setString(2, pass); // Nota: En producción esto debería estar encriptado (Hash)
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setNombreCompleto(rs.getString("nombre_completo"));
                    usuario.setUsername(rs.getString("username"));
                    usuario.setPassword(rs.getString("password"));
                    usuario.setIdRol(rs.getInt("id_rol"));
                    usuario.setActivo(rs.getBoolean("activo"));
                    
                    // Obtenemos el nombre del rol (Ej: "Administrador")
                    usuario.setNombreRol(rs.getString("nombre_rol"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en login: " + e.getMessage());
        }
        return usuario;
    }
}