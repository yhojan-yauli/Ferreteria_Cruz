package com.ferreteria.dao;

import com.ferreteria.modelo.DetalleVenta;
import com.ferreteria.modelo.Venta;
import com.ferreteria.util.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentaDAOImpl {

    // SQL para la cabecera (necesitamos que devuelva el ID generado)
    private static final String SQL_INSERT_VENTA = "INSERT INTO ventas (numero_comprobante, total_venta, id_usuario, id_cliente) VALUES (?, ?, ?, ?)";
    
    // SQL para el detalle
    private static final String SQL_INSERT_DETALLE = "INSERT INTO detalle_ventas (id_venta, id_producto, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
    
    // SQL CRÍTICO: Actualizar el stock
    private static final String SQL_UPDATE_STOCK = "UPDATE productos SET stock_actual = stock_actual - ? WHERE id_producto = ?";


    /**
     * Método principal que realiza toda la transacción de venta.
     * @param venta El objeto Venta que contiene dentro la lista de detalles.
     * @return true si todo salió bien, false si algo falló.
     */
    public boolean registrarVentaTransaccion(Venta venta) {
        Connection con = null;
        PreparedStatement psVenta = null;
        PreparedStatement psDetalle = null;
        PreparedStatement psStock = null;
        ResultSet rsKeys = null;
        boolean exito = false;

        try {
            con = ConexionDB.getConexion();
            // 1. ¡IMPORTANTE! Desactivamos el guardado automático para iniciar una transacción manual.
            con.setAutoCommit(false);

            // --- PASO A: Guardar Cabecera ---
            // Le pedimos que nos devuelva la clave generada (el id_venta)
            psVenta = con.prepareStatement(SQL_INSERT_VENTA, Statement.RETURN_GENERATED_KEYS);
            psVenta.setString(1, venta.getNumeroComprobante());
            psVenta.setDouble(2, venta.getTotalVenta());
            psVenta.setInt(3, venta.getIdUsuario());
            
            // Manejo del cliente nulo (venta rápida)
            if (venta.getIdCliente() > 0) {
                psVenta.setInt(4, venta.getIdCliente());
            } else {
                psVenta.setNull(4, java.sql.Types.INTEGER);
            }
            
            psVenta.executeUpdate();

            // Recuperar el ID de la venta recién creada
            rsKeys = psVenta.getGeneratedKeys();
            int idVentaGenerado = 0;
            if (rsKeys.next()) {
                idVentaGenerado = rsKeys.getInt(1);
                venta.setIdVenta(idVentaGenerado); // Lo guardamos en el objeto por si acaso
            } else {
                throw new SQLException("No se pudo obtener el ID de la venta.");
            }

            // --- PASO B & C (Bucle): Guardar Detalles y Actualizar Stock ---
            psDetalle = con.prepareStatement(SQL_INSERT_DETALLE);
            psStock = con.prepareStatement(SQL_UPDATE_STOCK);

            // Recorremos la lista de productos del carrito
            for (DetalleVenta detalle : venta.getDetalles()) {
                // B.1. Insertar detalle
                psDetalle.setInt(1, idVentaGenerado); // Usamos el ID de cabecera
                psDetalle.setInt(2, detalle.getIdProducto());
                psDetalle.setInt(3, detalle.getCantidad());
                psDetalle.setDouble(4, detalle.getPrecioUnitario());
                psDetalle.setDouble(5, detalle.getSubtotal());
                psDetalle.executeUpdate();

                // C.1. Descontar Stock
                psStock.setInt(1, detalle.getCantidad()); // Cantidad a restar
                psStock.setInt(2, detalle.getIdProducto()); // ID del producto
                int stockAfectado = psStock.executeUpdate();
                
                // Validación extra de seguridad: si no se actualizó ningún producto (ej. ID incorrecto)
                if (stockAfectado == 0) {
                     throw new SQLException("Error al actualizar stock del producto ID: " + detalle.getIdProducto());
                }
            }

            // --- FIN DE LA TRANSACCIÓN ---
            // Si llegamos hasta aquí sin errores, confirmamos todo.
            con.commit();
            exito = true;
            System.out.println("¡Transacción de venta completada con éxito!");

        } catch (SQLException e) {
            // SI ALGO FALLÓ EN CUALQUIER PASO: Deshacemos todo.
            System.err.println("Error CRÍTICO en la transacción de venta: " + e.getMessage());
            if (con != null) {
                try {
                    System.err.println("Intentando hacer ROLLBACK...");
                    con.rollback(); // Deshacer cambios
                    System.out.println("ROLLBACK ejecutado.");
                } catch (SQLException exRollback) {
                    System.err.println("Error grave al hacer rollback: " + exRollback.getMessage());
                }
            }
        } finally {
            // Cerramos recursos manualmente en orden inverso
            try {
                if (rsKeys != null) rsKeys.close();
                if (psVenta != null) psVenta.close();
                if (psDetalle != null) psDetalle.close();
                if (psStock != null) psStock.close();
                // Es importante volver a activar el autoCommit al devolver la conexión al pool
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException e) {
                System.err.println("Error cerrando recursos: " + e.getMessage());
            }
        }
        return exito;
    }

    // ====================================================================
    // NUEVO MÉTODO PARA EL APARTADO DE FACTURAS / REPORTES
    // ====================================================================
    public List<Object[]> listarReporteVentas(String codigoFiltro, String fechaFiltro) {
        List<Object[]> lista = new ArrayList<>();
        
        // Hacemos una consulta base. 
        // Asumimos que tu columna de fecha se llama 'fecha_venta'. Si se llama diferente en BD, cámbialo aquí.
        String sql = "SELECT numero_comprobante, id_cliente, id_usuario, fecha_venta, total_venta FROM ventas WHERE 1=1 ";
        
        if (codigoFiltro != null && !codigoFiltro.trim().isEmpty()) {
            sql += " AND numero_comprobante LIKE '%" + codigoFiltro + "%'";
        }
        
        if (fechaFiltro != null && !fechaFiltro.trim().isEmpty()) {
            // SOLUCIÓN APLICADA AQUÍ: Usar LIKE para evitar problemas con la hora de la BD
            sql += " AND fecha_venta LIKE '" + fechaFiltro + "%'"; 
        }
        
        sql += " ORDER BY id_venta DESC"; // Las más recientes primero

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
            while (rs.next()) {
                Object[] fila = new Object[5];
                fila[0] = rs.getString("numero_comprobante");
                
                // Mostrar RUC/DNI (si el id es 0 o null, fue público general)
                int idCli = rs.getInt("id_cliente");
                fila[1] = (idCli > 0) ? String.valueOf(idCli) : "Público General"; 
                
                // Mostrar el empleado (Ejemplo: EMP-1)
                fila[2] = "EMP-" + rs.getInt("id_usuario"); 
                
                // Fecha y Total
                fila[3] = rs.getString("fecha_venta");
                fila[4] = String.format("%.2f", rs.getDouble("total_venta"));
                
                lista.add(fila);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar facturas: " + e.getMessage());
        }
        return lista;
    }
    public double obtenerTotalEfectivoHoy(int idUsuario) {
    double totalEfectivo = 0.0;
    // Sumamos el total_venta SOLO de hoy, de este usuario, y que pagaron en EFECTIVO
    String sql = "SELECT SUM(total_venta) AS total FROM ventas "
               + "WHERE DATE(fecha_venta) = CURDATE() "
               + "AND id_usuario = ? "
               + "AND metodo_pago = 'EFECTIVO'";
               
    try (java.sql.Connection con = com.ferreteria.util.ConexionDB.getConexion();
         java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
        
        ps.setInt(1, idUsuario);
        
        try (java.sql.ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                totalEfectivo = rs.getDouble("total");
            }
        }
    } catch (java.sql.SQLException e) {
        System.err.println("Error al calcular caja: " + e.getMessage());
    }
    return totalEfectivo;
}
}