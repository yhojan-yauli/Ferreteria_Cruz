package dao;

import modelo.Alumno;
import conexion.Conexion;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;
import modelo.Curso;

public class AlumnoDAO {

    // Insertar alumno
    public boolean insertarAlumno(Alumno alumno) {
        String sql = "INSERT INTO alumno (nombres, apellidos, dni, password, edad, celular, estado) VALUES (?, ?, ?, ?, ?, ?, 0)";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, alumno.getNombres());
            ps.setString(2, alumno.getApellidos());
            ps.setString(3, alumno.getDni());
            ps.setString(4, alumno.getPassword());  // ⬅️ POSICIÓN 4
            ps.setInt(5, alumno.getEdad());         // ⬅️ POSICIÓN 5
            ps.setInt(6, alumno.getCelular());      // ⬅️ POSICIÓN 6
            
            int resultado = ps.executeUpdate();
            return resultado > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Listar todos los alumnos
    public List<Alumno> listarAlumnos() {
        List<Alumno> alumnos = new ArrayList<>();
        String sql = "SELECT * FROM alumno WHERE estado != 2 ORDER BY codAlumno";
        
        try (Connection conn = Conexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Alumno alumno = new Alumno(
                    rs.getInt("codAlumno"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("dni"),
                    rs.getString("password"), 
                    rs.getInt("edad"),
                    rs.getInt("celular"),
                    rs.getInt("estado")
                );
                alumnos.add(alumno);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return alumnos;
    }
    
    // Actualizar alumno
    public boolean actualizarAlumno(Alumno alumno) {
        String sql = "UPDATE alumno SET nombres=?, apellidos=?, dni=?, password=?, edad=?, celular=? WHERE codAlumno=?";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, alumno.getNombres());
            ps.setString(2, alumno.getApellidos());
            ps.setString(3, alumno.getDni());
            ps.setString(4, alumno.getPassword());  // ⬅️ POSICIÓN 4
            ps.setInt(5, alumno.getEdad());         // ⬅️ POSICIÓN 5
            ps.setInt(6, alumno.getCelular());      // ⬅️ POSICIÓN 6
            ps.setInt(7, alumno.getCodAlumno());    // ⬅️ POSICIÓN 7
            
            int resultado = ps.executeUpdate();
            return resultado > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Eliminar alumno (eliminación física)
    public boolean eliminarAlumno(int codAlumno) {
        String sql = "DELETE FROM alumno WHERE codAlumno = ?";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, codAlumno);
            
            int resultado = ps.executeUpdate();
            return resultado > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Buscar alumno por DNI
    public Alumno buscarPorDNI(String dni) {
        Alumno alumno = null;
        String sql = "SELECT * FROM alumno WHERE dni = ?";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, dni);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                alumno = new Alumno(
                    rs.getInt("codAlumno"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("dni"),
                    rs.getString("password"),
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
    
    // Buscar alumno por nombre
    public Alumno buscarPorNombre(String nombre) {
        Alumno alumno = null;
        String sql = "SELECT * FROM alumno WHERE nombres LIKE ? LIMIT 1";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, "%" + nombre + "%");
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                alumno = new Alumno(
                    rs.getInt("codAlumno"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("dni"),
                    rs.getString("password"),
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
    
    // Buscar alumno por Codigo
    public Alumno buscarPorCodigo(int codAlumno) {
        Alumno alumno = null;
        String sql = "SELECT * FROM alumno WHERE codAlumno = ?";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, codAlumno);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                alumno = new Alumno(
                    rs.getInt("codAlumno"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("dni"),
                    rs.getString("password"),  // ⬅️ AGREGADO
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
    
    // Obtener curso matriculado de alumno
    public Curso obtenerCursoMatriculado(int codAlumno) {
        Curso curso = null;
        String sql = "SELECT c.* FROM curso c " +
                     "INNER JOIN matricula m ON c.codCurso = m.codCurso " +
                     "WHERE m.codAlumno = ? AND NOT EXISTS " +
                     "(SELECT 1 FROM retiro r WHERE r.numMatricula = m.numMatricula)";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, codAlumno);
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
    
    // Obtener TODOS los cursos matriculados de un alumno
public List<Curso> obtenerCursosMatriculados(int codAlumno) {
    List<Curso> cursos = new ArrayList<>();
    String sql = "SELECT c.* FROM curso c " +
                 "INNER JOIN matricula m ON c.codCurso = m.codCurso " +
                 "WHERE m.codAlumno = ? " +
                 "AND NOT EXISTS (SELECT 1 FROM retiro r WHERE r.numMatricula = m.numMatricula)";
    
    try (Connection conn = Conexion.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setInt(1, codAlumno);
        ResultSet rs = ps.executeQuery();
        
        while (rs.next()) {
            Curso curso = new Curso(
                rs.getInt("codCurso"),
                rs.getString("asignatura"),
                rs.getInt("ciclo"),
                rs.getInt("creditos"),
                rs.getInt("horas")
            );
            cursos.add(curso);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    return cursos;
}
    
    
    // Login con DNI, Password y Nombre
    public Alumno loginAlumno(String dni, String password, String nombres) {
        Alumno alumno = null;
        String sql = "SELECT * FROM alumno WHERE dni = ? AND password = ? AND nombres = ?";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, dni);
            ps.setString(2, password);
            ps.setString(3, nombres);
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                alumno = new Alumno(
                    rs.getInt("codAlumno"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("dni"),
                    rs.getString("password"),
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
}