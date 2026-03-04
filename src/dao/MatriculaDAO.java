package dao;

import conexion.Conexion;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;
import modelo.Matricula;
import modelo.Alumno;
import modelo.Curso;

public class MatriculaDAO {
    
    public int insertarMatricula(Matricula matricula) {
        String sql = "{CALL sp_InsertarMatricula(?, ?)}";
        
        try (Connection conn = Conexion.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            cstmt.setInt(1, matricula.getCodAlumno());
            cstmt.setInt(2, matricula.getCodCurso());
            cstmt.execute();
            
            return 1;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    public boolean yaEstaMatriculado(int codAlumno, int codCurso) {
        String sql = "SELECT COUNT(*) as total FROM matricula WHERE codAlumno = ? AND codCurso = ?";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, codAlumno);
            ps.setInt(2, codCurso);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("total") > 0;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Matricula> listarMatriculas() {
        List<Matricula> matriculas = new ArrayList<>();
        String sql = "SELECT * FROM matricula ORDER BY numMatricula";

        try (Connection conn = Conexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Matricula matricula = new Matricula(
                    rs.getInt("numMatricula"),
                    rs.getInt("codAlumno"),
                    rs.getInt("codCurso"),
                    rs.getString("fecha"),
                    rs.getString("hora")
                );
                matriculas.add(matricula);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return matriculas;
    }
    
    public Matricula buscarPorNumero(int numMatricula) {
        Matricula m = null;
        String sql = "SELECT * FROM matricula WHERE numMatricula = ?";
        
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, numMatricula);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                m = new Matricula();
                m.setNumMatricula(rs.getInt("numMatricula"));
                m.setCodAlumno(rs.getInt("codAlumno"));
                m.setCodCurso(rs.getInt("codCurso"));
                m.setFecha(rs.getString("fecha"));
                m.setHora(rs.getString("hora"));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }

    public Alumno obtenerAlumnoDeMatricula(int numMatricula) {
        Alumno alumno = null;
        String sql = "SELECT a.* FROM alumno a INNER JOIN matricula m ON a.codAlumno = m.codAlumno WHERE m.numMatricula = ?";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, numMatricula);
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
    
    public Curso obtenerCursoDeMatricula(int numMatricula) {
        Curso curso = null;
        String sql = "SELECT c.* FROM curso c INNER JOIN matricula m ON c.codCurso = m.codCurso WHERE m.numMatricula = ?";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, numMatricula);
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

    public boolean existeMatriculaEnCurso(int codCurso) {
        String sql = "SELECT COUNT(*) FROM matricula WHERE codCurso = ?";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, codCurso);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int correlativoMatricula() {
        int correlativo = 0;
        String sql = "SELECT IFNULL(MAX(nummatricula), 0) + 1 FROM matricula";

        try (Connection conn = Conexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
               correlativo = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return correlativo;
    }
}