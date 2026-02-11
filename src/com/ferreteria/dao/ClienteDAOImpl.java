package com.ferreteria.dao;

import com.ferreteria.modelo.Cliente;
import com.ferreteria.util.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteDAOImpl {

    // Método simple para buscar un cliente cuando escriben su DNI en la pantalla de ventas
    
public Cliente buscarPorDniRuc(String dni) {
    Cliente c = null;
    String sql = "SELECT * FROM clientes WHERE dni_ruc = ?";
    
    try (Connection con = ConexionDB.getConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {
        
        ps.setString(1, dni);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            c = new Cliente();
            c.setIdCliente(rs.getInt("id_cliente"));
            c.setDniRuc(rs.getString("dni_ruc"));
            
            // CAMBIA ESTAS LÍNEAS:
            c.setNombres(rs.getString("nombres"));      // Antes decía nombre_completo
            c.setApellidos(rs.getString("apellidos"));  // Nueva columna
            c.setCelular(rs.getString("celular"));      // Antes decía telefono
            
            c.setDireccion(rs.getString("direccion"));
            c.setEmail(rs.getString("email"));
        }
    } catch (SQLException e) {
        System.out.println("Error al buscar cliente: " + e.getMessage());
    }
    return c;
}
    
    // Aquí podrías agregar métodos para registrar nuevos clientes, etc.
}