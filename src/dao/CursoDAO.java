/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import conexion.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.Curso;


public class CursoDAO {
    public int insertarCurso(Curso curso) {
        String sql = "INSERT INTO curso (codCurso, asignatura, ciclo, creditos, horas) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, curso.getCodCurso());
            ps.setString(2, curso.getAsignatura());
            ps.setInt(3, curso.getCiclo());
            ps.setInt(4, curso.getCreditos());
            ps.setInt(5, curso.getHoras());

            int filas = ps.executeUpdate();
            return (filas > 0) ? 1 : 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            // PK duplicada (codCurso ya existe)
            return -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -99;
        }
    }

    // LISTAR CURSOS (ordenados por código)
    public List<Curso> listarCursos() {
        List<Curso> cursos = new ArrayList<>();
        String sql = "SELECT codCurso, asignatura, ciclo, creditos, horas FROM curso ORDER BY codCurso";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Curso c = new Curso(
                        rs.getInt("codCurso"),
                        rs.getString("asignatura"),
                        rs.getInt("ciclo"),
                        rs.getInt("creditos"),
                        rs.getInt("horas")
                );
                cursos.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cursos;
    }

    // BUSCAR POR CÓDIGO
    public Curso buscarPorCodigo(int codCurso) {
        String sql = "SELECT codCurso, asignatura, ciclo, creditos, horas FROM curso WHERE codCurso = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, codCurso);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Curso(
                            rs.getInt("codCurso"),
                            rs.getString("asignatura"),
                            rs.getInt("ciclo"),
                            rs.getInt("creditos"),
                            rs.getInt("horas")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ACTUALIZAR (no cambia codCurso)
    public boolean actualizarCurso(Curso curso) {
        String sql = "UPDATE curso SET asignatura = ?, ciclo = ?, creditos = ?, horas = ? WHERE codCurso = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, curso.getAsignatura());
            ps.setInt(2, curso.getCiclo());
            ps.setInt(3, curso.getCreditos());
            ps.setInt(4, curso.getHoras());
            ps.setInt(5, curso.getCodCurso());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ELIMINAR
    public boolean eliminarCurso(int codCurso) {
        String sql = "DELETE FROM curso WHERE codCurso = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, codCurso);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
