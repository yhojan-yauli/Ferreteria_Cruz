package com.ferreteria.dao;

import com.ferreteria.modelo.Categoria;
import com.ferreteria.util.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAOImpl {

    // Consultas SQL corregidas con la columna "activa"
    private static final String SQL_SELECT = "SELECT id_categoria, nombre, descripcion, activa FROM categorias";
    private static final String SQL_INSERT = "INSERT INTO categorias (nombre, descripcion, activa) VALUES (?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE categorias SET nombre = ?, descripcion = ?, activa = ? WHERE id_categoria = ?";
    private static final String SQL_DELETE = "UPDATE categorias SET activa = 0 WHERE id_categoria = ?";

    // 1. LISTAR TODAS LAS CATEGORÍAS
    public List<Categoria> listarTodas() {
        List<Categoria> lista = new ArrayList<>();
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT);
             ResultSet rs = ps.executeQuery()) {
             
            while (rs.next()) {
                Categoria cat = new Categoria();
                cat.setIdCategoria(rs.getInt("id_categoria"));
                cat.setNombre(rs.getString("nombre"));
                cat.setDescripcion(rs.getString("descripcion"));
                // Aquí leemos de la columna correcta: "activa"
                cat.setActivo(rs.getBoolean("activa")); 
                lista.add(cat);
            }
        } catch (SQLException e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Error BD: " + e.getMessage());
        }
        return lista;
    }

    // 2. REGISTRAR NUEVA CATEGORÍA
    public boolean insertar(Categoria cat) {
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT)) {
             
            ps.setString(1, cat.getNombre());
            ps.setString(2, cat.getDescripcion());
            ps.setBoolean(3, cat.isActivo());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar categoría: " + e.getMessage());
            return false;
        }
    }

    // 3. MODIFICAR CATEGORÍA EXISTENTE
    public boolean modificar(Categoria cat) {
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_UPDATE)) {
             
            ps.setString(1, cat.getNombre());
            ps.setString(2, cat.getDescripcion());
            ps.setBoolean(3, cat.isActivo());
            ps.setInt(4, cat.getIdCategoria());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al modificar categoría: " + e.getMessage());
            return false;
        }
    }

    // 4. ELIMINAR (Desactivar) CATEGORÍA
    public boolean eliminar(int idCategoria) {
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_DELETE)) {
             
            ps.setInt(1, idCategoria);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar categoría: " + e.getMessage());
            return false;
        }
    }
}