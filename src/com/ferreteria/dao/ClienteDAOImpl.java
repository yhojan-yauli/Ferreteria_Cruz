package com.ferreteria.dao;

import com.ferreteria.modelo.Cliente;
import com.ferreteria.util.ConexionDB;
import java.sql.*;
import java.util.ArrayList; // IMPORTANTE: Faltaba esta importación
import java.util.List;

public class ClienteDAOImpl {

    // 1. LISTAR TODOS LOS CLIENTES (Para llenar la tabla)
    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Cliente c = new Cliente();
                c.setIdCliente(rs.getInt("id_cliente"));
                c.setDniRuc(rs.getString("dni_ruc"));
                c.setNombres(rs.getString("nombres"));
                c.setApellidos(rs.getString("apellidos"));
                c.setCelular(rs.getString("celular"));
                c.setDireccion(rs.getString("direccion"));
                c.setEmail(rs.getString("email"));
                lista.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar: " + e.getMessage());
        }
        return lista;
    }

    // 2. BUSCAR POR DNI (Para la pantalla de ventas)
    public Cliente buscarPorDniRuc(String dni) {
        Cliente c = null;
        String sql = "SELECT * FROM clientes WHERE dni_ruc = ?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    c = new Cliente();
                    c.setIdCliente(rs.getInt("id_cliente"));
                    c.setDniRuc(rs.getString("dni_ruc"));
                    c.setNombres(rs.getString("nombres"));
                    c.setApellidos(rs.getString("apellidos"));
                    c.setCelular(rs.getString("celular"));
                    c.setDireccion(rs.getString("direccion"));
                    c.setEmail(rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar cliente: " + e.getMessage());
        }
        return c;
    }

    // 3. REGISTRAR NUEVO CLIENTE (Para el botón Guardar del panel)
   public boolean registrar(Cliente c) {
    String sql = "INSERT INTO clientes (dni_ruc, nombres, apellidos, celular, direccion, email) VALUES (?,?,?,?,?,?)";
    try (java.sql.Connection con = com.ferreteria.util.ConexionDB.getConexion();
         java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, c.getDniRuc());
        ps.setString(2, c.getNombres());
        ps.setString(3, c.getApellidos());
        ps.setString(4, c.getCelular());
        ps.setString(5, c.getDireccion());
        ps.setString(6, c.getEmail());
        return ps.executeUpdate() > 0;
    } catch (java.sql.SQLException e) {
        System.err.println("Error al registrar cliente: " + e.getMessage());
        return false;
    }
}
}