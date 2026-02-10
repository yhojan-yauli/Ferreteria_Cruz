/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ferreteriacruz.Conexion;

/**
 *
 * @author USUARIO
 */

//import javax.swing.JOptionPane;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Conexion { 
    
    protected static final String URL = "jdbc:mysql://localhost:3306/Ferreteria_cruz";
    protected static final String USER = "root";
    protected static final String PASSWORD = "root";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    
    /*  public static void testConnection() {
        try {
            Connection connection = getConnection();
            if (connection != null) {
                JOptionPane.showMessageDialog(null, "Conexión a la base de datos establecida correctamente.");
                connection.close();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos: " + ex.getMessage());
        }
    } */
}
