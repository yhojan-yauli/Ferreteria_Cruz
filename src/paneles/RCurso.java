
package paneles;

import conexion.Conexion;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class RCurso extends javax.swing.JPanel {

    public RCurso() {
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
                txtCodigoRetiro.setText("RC" + rs.getInt("codigo"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error generando código: " + e.getMessage());
        }
    }

    private void registrarRetiro() {
        String fechaHora = txtFechaHora.getText();
        String codAlumnoStr = txtCodigoAlumno1.getText(); // Nuevo campo
        String codCursoStr = txtCodigoCurso.getText();
        String motivo = txtMotivo.getText();

        if (codAlumnoStr.isEmpty() || codCursoStr.isEmpty() || motivo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa todos los campos (Alumno, Curso y Motivo).");
            return;
        }

        try {
            int codAlumno = Integer.parseInt(codAlumnoStr);
            int codCurso = Integer.parseInt(codCursoStr);
            
            try (Connection conn = Conexion.getConnection()) {
                // Buscar la matrícula del alumno en ese curso específico
                String sqlMatricula = "SELECT m.numMatricula FROM matricula m " +
                                     "WHERE m.codAlumno = ? AND m.codCurso = ? " +
                                     "AND NOT EXISTS (SELECT 1 FROM retiro r WHERE r.numMatricula = m.numMatricula)";
                PreparedStatement psMatricula = conn.prepareStatement(sqlMatricula);
                psMatricula.setInt(1, codAlumno);
                psMatricula.setInt(2, codCurso);
                ResultSet rsMatricula = psMatricula.executeQuery();
                
                if (!rsMatricula.next()) {
                    JOptionPane.showMessageDialog(this, 
                        "El alumno no está matriculado en ese curso o ya fue retirado.");
                    return;
                }
                
                int numMatricula = rsMatricula.getInt("numMatricula");
                
                // Generar número de retiro
                String sqlMaxRetiro = "SELECT IFNULL(MAX(numRetiro), 0) + 1 AS siguiente FROM retiro";
                Statement stMax = conn.createStatement();
                ResultSet rsMax = stMax.executeQuery(sqlMaxRetiro);
                rsMax.next();
                int numRetiro = rsMax.getInt("siguiente");
                
                // Separar fecha y hora
                String[] partes = fechaHora.split(" ");
                String fecha = partes[0];
                String hora = partes[1];
                
                // Insertar retiro
                String sqlRetiro = "INSERT INTO retiro (numRetiro, numMatricula, fecha, hora) VALUES (?, ?, ?, ?)";
                PreparedStatement psRetiro = conn.prepareStatement(sqlRetiro);
                psRetiro.setInt(1, numRetiro);
                psRetiro.setInt(2, numMatricula);
                psRetiro.setString(3, fecha);
                psRetiro.setString(4, hora);
                psRetiro.executeUpdate();

                JOptionPane.showMessageDialog(this, 
                    "Retiro de curso registrado correctamente.\n" +
                    "Alumno: " + codAlumno + "\n" +
                    "Curso: " + codCurso + "\n" +
                    "Motivo: " + motivo);
                
                limpiarCampos();
                generarCodigo();
                cargarRetiros();
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El código de alumno y curso deben ser números.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al registrar retiro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cancelarRetiro() {
        int fila = tablaRetirosCurso.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un retiro para cancelar.");
            return;
        }

        int numRetiro = Integer.parseInt(tablaRetirosCurso.getValueAt(fila, 0).toString());
        int codCurso = Integer.parseInt(tablaRetirosCurso.getValueAt(fila, 1).toString());

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas cancelar este retiro del curso?", 
                "Confirmación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = Conexion.getConnection()) {
                // Eliminar el retiro
                String delete = "DELETE FROM retiro WHERE numRetiro = ?";
                PreparedStatement ps = conn.prepareStatement(delete);
                ps.setInt(1, numRetiro);
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
        modelo.addColumn("CODIGO RETIRO");
        modelo.addColumn("CODIGO CURSO");
        modelo.addColumn("MOTIVO");
        modelo.addColumn("FECHA Y HORA");

        try (Connection conn = Conexion.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(
                 "SELECT r.numRetiro, c.codCurso, r.fecha, r.hora " +
                 "FROM retiro r " +
                 "INNER JOIN matricula m ON r.numMatricula = m.numMatricula " +
                 "INNER JOIN curso c ON m.codCurso = c.codCurso " +
                 "ORDER BY r.numRetiro DESC")) {

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("numRetiro"),
                    rs.getInt("codCurso"),
                    "N/A", // Motivo no existe en BD
                    rs.getString("fecha") + " " + rs.getString("hora")
                });
            }

            tablaRetirosCurso.setModel(modelo);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar retiros: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void limpiarCampos() {
        txtCodigoAlumno1.setText(""); // Nuevo campo
        txtCodigoCurso.setText("");
        txtMotivo.setText("");
        mostrarFechaHora();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtFechaHora = new javax.swing.JTextField();
        txtCodigoRetiro = new javax.swing.JTextField();
        txtCodigoCurso = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtMotivo = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaRetirosCurso = new javax.swing.JTable();
        btnCancelar = new javax.swing.JButton();
        btnRegistrar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblNumero = new javax.swing.JLabel();
        lblNumero1 = new javax.swing.JLabel();
        lblNumero2 = new javax.swing.JLabel();
        lblNumero3 = new javax.swing.JLabel();
        lblNumero4 = new javax.swing.JLabel();
        txtCodigoAlumno1 = new javax.swing.JTextField();

        setBackground(new java.awt.Color(255, 255, 255));

        txtFechaHora.setEditable(false);

        txtCodigoRetiro.setEditable(false);
        txtCodigoRetiro.setMinimumSize(new java.awt.Dimension(150, 50));
        txtCodigoRetiro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoRetiroActionPerformed(evt);
            }
        });

        txtMotivo.setColumns(20);
        txtMotivo.setRows(5);
        jScrollPane2.setViewportView(txtMotivo);

        tablaRetirosCurso.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "CODIGO RETIRO", "CODIGO CURSO", "MOTIVO", "FECHA Y HORA"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tablaRetirosCurso);

        btnCancelar.setBackground(new java.awt.Color(243, 156, 18));
        btnCancelar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnCancelar.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelar.setText("Cancelar Retiro");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnRegistrar.setBackground(new java.awt.Color(243, 156, 18));
        btnRegistrar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnRegistrar.setForeground(new java.awt.Color(255, 255, 255));
        btnRegistrar.setText("Registrar Retiro");
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(0, 102, 102));
        jPanel2.setForeground(new java.awt.Color(153, 204, 0));

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("GESTIÓN DE RETIRO CURSO");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(264, 264, 264)
                .addComponent(lblTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
                .addGap(293, 293, 293))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(lblTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(24, 24, 24))
        );

        lblNumero.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblNumero.setText("Codigo Retiro:");

        lblNumero1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblNumero1.setText("Motivo:");

        lblNumero2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblNumero2.setText("Codigo Curso:");

        lblNumero3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblNumero3.setText("Fecha y Hora:");

        lblNumero4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblNumero4.setText("Codigo Alumno:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnRegistrar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCancelar)
                        .addGap(46, 46, 46))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblNumero2, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtCodigoRetiro, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtCodigoCurso, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblNumero1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblNumero3, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtFechaHora, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblNumero4, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtCodigoAlumno1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNumero)
                            .addComponent(txtCodigoRetiro, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNumero4)
                            .addComponent(txtCodigoAlumno1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                                .addComponent(txtCodigoCurso, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(42, 42, 42)
                                .addComponent(lblNumero2)
                                .addGap(56, 56, 56)
                                .addComponent(lblNumero1)))
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNumero3)
                            .addComponent(txtFechaHora, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(53, 53, 53)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(100, 100, 100))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtCodigoRetiroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoRetiroActionPerformed
        
    }//GEN-LAST:event_txtCodigoRetiroActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        cancelarRetiro();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
        registrarRetiro();
    }//GEN-LAST:event_btnRegistrarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblNumero;
    private javax.swing.JLabel lblNumero1;
    private javax.swing.JLabel lblNumero2;
    private javax.swing.JLabel lblNumero3;
    private javax.swing.JLabel lblNumero4;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTable tablaRetirosCurso;
    private javax.swing.JTextField txtCodigoAlumno1;
    private javax.swing.JTextField txtCodigoCurso;
    private javax.swing.JTextField txtCodigoRetiro;
    private javax.swing.JTextField txtFechaHora;
    private javax.swing.JTextArea txtMotivo;
    // End of variables declaration//GEN-END:variables
}
