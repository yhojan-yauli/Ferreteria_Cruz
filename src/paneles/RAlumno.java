
package paneles;

import conexion.Conexion;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class RAlumno extends javax.swing.JPanel {



    public RAlumno() {
        initComponents();
        mostrarFechaHora();
        generarCodigo();
        cargarRetiros();
    }

    private void mostrarFechaHora() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        txtFechaHora.setText(sdf.format(new Date()));
    }

    private void generarCodigo() {
        try (Connection conn = Conexion.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT IFNULL(MAX(numRetiro), 0) + 1 AS codigo FROM retiro")) {
            if (rs.next()) {
                txtCodigoRetiro.setText("R" + rs.getInt("codigo"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error generando código: " + e.getMessage());
        }
    }

private void registrarRetiro() {
    String fechaHora = txtFechaHora.getText();
    String codAlumnoStr = txtAlumno.getText();
    String motivo = txtMotivo.getText();

    if (codAlumnoStr.isEmpty() || motivo.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Completa todos los campos");
        return;
    }

    try {
        int codAlumno = Integer.parseInt(codAlumnoStr);
        
        try (Connection conn = Conexion.getConnection()) {
            // Buscar una matrícula activa del alumno
            String sqlMatricula = "SELECT m.numMatricula FROM matricula m " +
                                 "WHERE m.codAlumno = ? " +
                                 "AND NOT EXISTS (SELECT 1 FROM retiro r WHERE r.numMatricula = m.numMatricula) " +
                                 "LIMIT 1";
            PreparedStatement psMatricula = conn.prepareStatement(sqlMatricula);
            psMatricula.setInt(1, codAlumno);
            ResultSet rsMatricula = psMatricula.executeQuery();
            
            if (!rsMatricula.next()) {
                JOptionPane.showMessageDialog(this, "El alumno no tiene matrículas activas para retirar.");
                return;
            }
            
            int numMatricula = rsMatricula.getInt("numMatricula");
            
            // Generar el siguiente número de retiro manualmente
            String sqlMaxRetiro = "SELECT IFNULL(MAX(numRetiro), 0) + 1 AS siguiente FROM retiro";
            Statement stMax = conn.createStatement();
            ResultSet rsMax = stMax.executeQuery(sqlMaxRetiro);
            rsMax.next();
            int numRetiro = rsMax.getInt("siguiente");
            
            // Separar fecha y hora
            String[] partes = fechaHora.split(" ");
            String fecha = partes[0];
            String hora = partes[1];
            
            // Insertar retiro INCLUYENDO numRetiro manualmente
            String sqlRetiro = "INSERT INTO retiro (numRetiro, numMatricula, fecha, hora) VALUES (?, ?, ?, ?)";
            PreparedStatement psRetiro = conn.prepareStatement(sqlRetiro);
            psRetiro.setInt(1, numRetiro);
            psRetiro.setInt(2, numMatricula);
            psRetiro.setString(3, fecha);
            psRetiro.setString(4, hora);
            psRetiro.executeUpdate();

            // Actualizar estado del alumno a "Retirado" (estado = 2)
            String updateAlumno = "UPDATE alumno SET estado = 2 WHERE codAlumno = ?";
            PreparedStatement psUpdate = conn.prepareStatement(updateAlumno);
            psUpdate.setInt(1, codAlumno);
            psUpdate.executeUpdate();

            JOptionPane.showMessageDialog(this, "Retiro registrado correctamente.\nMotivo: " + motivo);
            limpiarCampos();
            generarCodigo();
            cargarRetiros();
        }

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "El código del alumno debe ser un número.");
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al registrar: " + e.getMessage());
        e.printStackTrace();
    }
}

    private void cancelarRetiro() {
        int fila = tablaRetiros.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un retiro para cancelar.");
            return;
        }

        int numRetiro = Integer.parseInt(tablaRetiros.getValueAt(fila, 0).toString());
        int codAlumno = Integer.parseInt(tablaRetiros.getValueAt(fila, 2).toString());

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas cancelar este retiro?", "Confirmación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = Conexion.getConnection()) {
                // Eliminar el retiro
                String delete = "DELETE FROM retiro WHERE numRetiro = ?";
                PreparedStatement ps = conn.prepareStatement(delete);
                ps.setInt(1, numRetiro);
                ps.executeUpdate();

                // Actualizar estado del alumno a "Matriculado" (estado = 1)
                String update = "UPDATE alumno SET estado = 1 WHERE codAlumno = ?";
                ps = conn.prepareStatement(update);
                ps.setInt(1, codAlumno);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Retiro cancelado correctamente.");
                cargarRetiros();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al cancelar retiro: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void cargarRetiros() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Nro Retiro");
        modelo.addColumn("Nro Matrícula");
        modelo.addColumn("Código Alumno");
        modelo.addColumn("Alumno");
        modelo.addColumn("Curso");
        modelo.addColumn("Fecha");
        modelo.addColumn("Hora");

        try (Connection conn = Conexion.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(
                "SELECT r.numRetiro, r.numMatricula, a.codAlumno, " +
                "CONCAT(a.nombres, ' ', a.apellidos) as alumno, " +
                "c.asignatura, r.fecha, r.hora " +
                "FROM retiro r " +
                "INNER JOIN matricula m ON r.numMatricula = m.numMatricula " +
                "INNER JOIN alumno a ON m.codAlumno = a.codAlumno " +
                "INNER JOIN curso c ON m.codCurso = c.codCurso " +
                "ORDER BY r.numRetiro DESC")) {

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("numRetiro"),
                    rs.getInt("numMatricula"),
                    rs.getInt("codAlumno"),
                    rs.getString("alumno"),
                    rs.getString("asignatura"),
                    rs.getString("fecha"),
                    rs.getString("hora")
                });
            }

            tablaRetiros.setModel(modelo);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar retiros: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void limpiarCampos() {
        txtAlumno.setText("");
        txtMotivo.setText("");
        mostrarFechaHora();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaRetiros = new javax.swing.JTable();
        txtCodigoRetiro = new javax.swing.JTextField();
        txtFechaHora = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtMotivo = new javax.swing.JTextArea();
        txtAlumno = new javax.swing.JTextField();
        btnRegistrar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        lblNumero = new javax.swing.JLabel();
        lblNumero1 = new javax.swing.JLabel();
        lblNumero2 = new javax.swing.JLabel();
        lblNumero3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jPanel1AncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        tablaRetiros.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        tablaRetiros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "CODIGO RETIRO", "CODIGO ALUMNO", "MOTIVO", "FECHA Y HORA"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablaRetiros);

        txtCodigoRetiro.setEditable(false);

        txtFechaHora.setEditable(false);

        txtMotivo.setColumns(20);
        txtMotivo.setRows(5);
        jScrollPane3.setViewportView(txtMotivo);

        btnRegistrar.setBackground(new java.awt.Color(243, 156, 18));
        btnRegistrar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnRegistrar.setForeground(new java.awt.Color(255, 255, 255));
        btnRegistrar.setText("Registrar Retiro");
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });

        btnCancelar.setBackground(new java.awt.Color(243, 156, 18));
        btnCancelar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnCancelar.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelar.setText("Cancelar Retiro");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        lblNumero.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblNumero.setText("Codigo Retiro:");

        lblNumero1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblNumero1.setText("Codigo Alumno:");

        lblNumero2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblNumero2.setText("Motivo del Retiro:");

        lblNumero3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblNumero3.setText("Fecha y Hora:");

        jPanel2.setBackground(new java.awt.Color(0, 102, 102));
        jPanel2.setForeground(new java.awt.Color(153, 204, 0));

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("GESTIÓN DE RETIRO ALUMNO");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(264, 264, 264)
                .addComponent(lblTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(293, 293, 293))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(lblTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(lblNumero2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblNumero1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblNumero, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRegistrar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(lblNumero3, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(68, 68, 68)
                        .addComponent(btnCancelar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCodigoRetiro, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtFechaHora, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(37, 37, 37)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(103, 103, 103))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCodigoRetiro, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNumero))
                        .addGap(44, 44, 44)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNumero1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(41, 41, 41))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(55, 55, 55)
                                .addComponent(lblNumero2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtFechaHora, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNumero3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(54, 54, 54)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 501, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(61, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
        registrarRetiro();
    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        cancelarRetiro();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void jPanel1AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jPanel1AncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel1AncestorAdded


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblNumero;
    private javax.swing.JLabel lblNumero1;
    private javax.swing.JLabel lblNumero2;
    private javax.swing.JLabel lblNumero3;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTable tablaRetiros;
    private javax.swing.JTextField txtAlumno;
    private javax.swing.JTextField txtCodigoRetiro;
    private javax.swing.JTextField txtFechaHora;
    private javax.swing.JTextArea txtMotivo;
    // End of variables declaration//GEN-END:variables
}
