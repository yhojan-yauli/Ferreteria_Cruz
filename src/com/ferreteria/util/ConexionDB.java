package com.ferreteria.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    // 1. Configuración de la Base de Datos (¡CAMBIA ESTO CON TUS DATOS!)
    private static final String URL = "jdbc:mysql://localhost:3306/ferreteria_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; // Tu usuario de MySQL
    private static final String PASSWORD = "root"; // Tu contraseña de MySQL

    private static Connection conexion = null;

    // Constructor privado para evitar instanciación externa
    private ConexionDB() { }

    // Método estático para obtener la instancia única de la conexión
    public static Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                // Cargar el driver (necesario en versiones antiguas de Java/JDBC, opcional en nuevas)
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                } catch (ClassNotFoundException e) {
                    System.err.println("Error al cargar el driver JDBC: " + e.getMessage());
                }
                
                // Establecer la conexión
                conexion = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Conexión a BD exitosa.");
            }
        } catch (SQLException e) {
            System.err.println("Error al conectar a la Base de Datos: " + e.getMessage());
            // En un sistema real, aquí podrías lanzar una excepción personalizada
        }
        return conexion;
    }

    // Método para cerrar la conexión (opcional, útil al cerrar la app)
    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}