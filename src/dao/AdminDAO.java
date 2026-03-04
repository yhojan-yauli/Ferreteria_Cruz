
package dao;

import java.sql.*;
import conexion.Conexion;
import modelo.Admin;

public class AdminDAO {
    
    // Login admin solo con contraseña
public Admin loginAdminSoloPassword(String password) {
    Admin admin = null;
    String sql = "SELECT * FROM admin WHERE password = ?";
    
    try (Connection conn = Conexion.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setString(1, password);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            admin = new Admin();
            admin.setId(rs.getInt("idAdmin"));
            admin.setUsuario(rs.getString("usuario"));
            admin.setNombre(rs.getString("nombre"));
        }
        
    } catch (SQLException e) {
        System.out.println("Error en login: " + e.getMessage());
    }
    
    return admin;
}
    
    
  public Admin loginAdmin(String usuario, String password, String nombre) {
    Admin admin = null;
    String sql = "SELECT * FROM admin WHERE usuario = ? AND password = ? AND nombre = ?";
    
    try (Connection conn = Conexion.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setString(1, usuario);
        ps.setString(2, password);
        ps.setString(3, nombre);
        
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            admin = new Admin();
            admin.setId(rs.getInt("idAdmin"));
            admin.setUsuario(rs.getString("usuario"));
            admin.setNombre(rs.getString("nombre"));
        }
        
    } catch (SQLException e) {
        System.out.println("Error en login: " + e.getMessage());
    }
    
    return admin;
}
}
