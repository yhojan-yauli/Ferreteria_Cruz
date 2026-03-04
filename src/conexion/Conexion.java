
package conexion;

import java.sql.*;

public class Conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/sistema_matricula";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Error al cargar el driver de MySQL", e);
        }
    }

    // Método para probar la conexión
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("¡Conexión exitosa a MySQL!");
        } catch (SQLException e) {
            System.err.println("Error de conexión: " + e.getMessage());
        }
    }
}
