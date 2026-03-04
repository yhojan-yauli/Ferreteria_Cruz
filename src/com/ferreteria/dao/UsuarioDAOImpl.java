package com.ferreteria.dao;

import com.ferreteria.modelo.Usuario;
import com.ferreteria.util.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImpl {
public Usuario login(String user, String pass) {
    Usuario usuario = null;
    // Consulta exacta para tu tabla 'usuarios' en ferreteria_db
    String sql = "SELECT id_usuario, nombre_completo, username, id_rol FROM usuarios "
               + "WHERE username = ? AND password = ? AND activo = 1";
    
    try (java.sql.Connection con = com.ferreteria.util.ConexionDB.getConexion();
         java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
        
        ps.setString(1, user);
        ps.setString(2, pass);
        
        try (java.sql.ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                usuario = new com.ferreteria.modelo.Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombreCompleto(rs.getString("nombre_completo"));
                usuario.setUsername(rs.getString("username"));
                usuario.setIdRol(rs.getInt("id_rol"));
            }
        }
    } catch (java.sql.SQLException e) {
        System.err.println("Error en el método login del DAO: " + e.getMessage());
    }
    return usuario;
}
    // Este es el método que "no encuentra" el compilador
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        // Consulta exacta para tu tabla en MySQL
        String sql = "SELECT id_usuario, nombre_completo, username, id_rol, activo FROM usuarios";
        
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setNombreCompleto(rs.getString("nombre_completo"));
                u.setUsername(rs.getString("username"));
                u.setIdRol(rs.getInt("id_rol"));
                u.setActivo(rs.getInt("activo"));
                lista.add(u);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        }
        return lista;
    }
    public boolean registrar(Usuario u) {
    String sql = "INSERT INTO usuarios (nombre_completo, username, password, id_rol, activo) VALUES (?, ?, ?, ?, ?)";
    
    try (java.sql.Connection con = com.ferreteria.util.ConexionDB.getConexion();
         java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
        
        ps.setString(1, u.getNombreCompleto());
        ps.setString(2, u.getUsername());
        ps.setString(3, u.getPassword());
        ps.setInt(4, u.getIdRol());
        ps.setInt(5, u.getActivo());
        
        return ps.executeUpdate() > 0;
    } catch (java.sql.SQLException e) {
        System.err.println("Error al registrar usuario: " + e.getMessage());
        return false;
    }
}
}