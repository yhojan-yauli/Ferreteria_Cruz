/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import conexion.Conexion;
import java.sql.*;
import java.util.ArrayList;
import modelo.Curso;
import modelo.Alumno;// ---Aylin
import modelo.Retiro;

public class RetiroDAO {
    public int insertarRetiro(Retiro retiro) {
        String sql = "CALL sp_InsertarRetiro(?, ?, ?, ?, ?)";
        int resultado = 0;
        
        try (Connection conn = Conexion.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            cstmt.setInt(1, retiro.getNumRetiro());
            cstmt.setInt(2, retiro.getNumMatricula());
            cstmt.setString(3, retiro.getFecha());
            cstmt.setString(4, retiro.getHora());
            cstmt.registerOutParameter(5, Types.INTEGER);
            
            cstmt.execute();
            resultado = cstmt.getInt(5);
            
        } catch (SQLException e) {
            e.printStackTrace();
            resultado = -99;
        }
        
        return resultado;
    }
    // Buscar por numero ---Aylin
    public Retiro buscarPorNumero(int numRetiro) {
        Retiro retiro = null;
        String sql = "SELECT * FROM retiro WHERE numRetiro = ?";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, numRetiro);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                retiro = new Retiro(
                    rs.getInt("numRetiro"),
                    rs.getInt("numMatricula"),
                    rs.getString("fecha"),
                    rs.getString("hora")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return retiro;
    }
    // obtener ---Aylin
    public Alumno obtenerAlumnoDeRetiro(int numRetiro) {
        Alumno alumno = null;
        String sql = "SELECT a.* FROM alumno a " +
                     "INNER JOIN matricula m ON a.codAlumno = m.codAlumno " +
                     "INNER JOIN retiro r ON m.numMatricula = r.numMatricula " +
                     "WHERE r.numRetiro = ?";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, numRetiro);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                alumno = new Alumno(
                    rs.getInt("codAlumno"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("dni"),
                    rs.getInt("edad"),
                    rs.getInt("celular"),
                    rs.getInt("estado")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return alumno;
    }
    // Obtener ---Aylin
    public Curso obtenerCursoDeRetiro(int numRetiro) {
        Curso curso = null;
        String sql = "SELECT c.* FROM curso c " +
                     "INNER JOIN matricula m ON c.codCurso = m.codCurso " +
                     "INNER JOIN retiro r ON m.numMatricula = r.numMatricula " +
                     "WHERE r.numRetiro = ?";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, numRetiro);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                curso = new Curso(
                    rs.getInt("codCurso"),
                    rs.getString("asignatura"),
                    rs.getInt("ciclo"),
                    rs.getInt("creditos"),
                    rs.getInt("horas")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return curso;
    }
    
}
