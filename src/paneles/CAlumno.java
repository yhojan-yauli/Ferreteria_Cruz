/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package paneles;

import dao.AlumnoDAO;
import dao.CursoDAO;
import modelo.Alumno;
import modelo.Curso;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Font;
import java.util.List;
import modelo.Curso;

public class CAlumno extends javax.swing.JPanel {
    
    private AlumnoDAO alumnoDAO;
    private CursoDAO cursoDAO;
    private DefaultTableModel modeloTablaAlumno;
    private DefaultTableModel modeloTablaCurso;

    /**
     * Creates new form CAlumno
     */
    public CAlumno() {
        initComponents();
        inicializarVariables();
        configurarTablas();
        personalizarEstilos();
         rbAlumno.setSelected(true);          // Por defecto: consulta alumno
    scrollAlumno.setVisible(true);       // Mostrar tabla alumno
    lblCursoTitulo.setVisible(false);    // Oculto hasta que haya curso matriculado
    scrollCurso.setVisible(false);       // Oculto al inicio
    }
    
    private void inicializarVariables() {
        alumnoDAO = new AlumnoDAO();
        cursoDAO = new CursoDAO();
    }
    
     private void configurarTablas() {
        // Configurar tabla de alumnos
        modeloTablaAlumno = new DefaultTableModel(
            new Object[][]{},
            new String[]{
                "Código", "Nombres", "Apellidos", "DNI", 
                "Edad", "Celular", "Estado"
            }
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblResultados.setModel(modeloTablaAlumno);
        
        // Configurar anchos de columnas
        tblResultados.getColumnModel().getColumn(0).setPreferredWidth(80);
        tblResultados.getColumnModel().getColumn(1).setPreferredWidth(150);
        tblResultados.getColumnModel().getColumn(2).setPreferredWidth(150);
        tblResultados.getColumnModel().getColumn(3).setPreferredWidth(100);
        tblResultados.getColumnModel().getColumn(4).setPreferredWidth(60);
        tblResultados.getColumnModel().getColumn(5).setPreferredWidth(100);
        tblResultados.getColumnModel().getColumn(6).setPreferredWidth(100);
        
        tblResultados.setRowHeight(25);
        
        // Configurar tabla de cursos
        modeloTablaCurso = new DefaultTableModel(
            new Object[][]{},
            new String[]{
                "Código", "Asignatura", "Ciclo", "Créditos", "Horas"
            }
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblCurso.setModel(modeloTablaCurso);
        
        tblCurso.getColumnModel().getColumn(0).setPreferredWidth(80);
        tblCurso.getColumnModel().getColumn(1).setPreferredWidth(250);
        tblCurso.getColumnModel().getColumn(2).setPreferredWidth(80);
        tblCurso.getColumnModel().getColumn(3).setPreferredWidth(80);
        tblCurso.getColumnModel().getColumn(4).setPreferredWidth(80);
        
        tblCurso.setRowHeight(25);
        
        // Ocultar inicialmente
        scrollCurso.setVisible(false);
        lblCursoTitulo.setVisible(false);
    }
    
    // Personalizar Estilos
    private void personalizarEstilos() {
        Color colorHeader = new Color(41, 128, 185);
        Font fuenteHeader = new Font("Segoe UI", Font.BOLD, 12);
        
        // Header tabla alumnos
        tblResultados.getTableHeader().setBackground(colorHeader);
        tblResultados.getTableHeader().setForeground(Color.WHITE);
        tblResultados.getTableHeader().setFont(fuenteHeader);
        
        // Header tabla cursos
        tblCurso.getTableHeader().setBackground(colorHeader);
        tblCurso.getTableHeader().setForeground(Color.WHITE);
        tblCurso.getTableHeader().setFont(fuenteHeader);
    }
    
    //Buscar Alumno

private void buscarAlumno(int codAlumno) {
    limpiarTablaAlumno();
    limpiarTablaCurso();
    
    Alumno alumno = alumnoDAO.buscarPorCodigo(codAlumno);
    
    if (alumno == null) {
        JOptionPane.showMessageDialog(this,
            "No se encontró ningún alumno con el código: " + codAlumno,
            "Alumno no encontrado",
            JOptionPane.INFORMATION_MESSAGE);
        scrollCurso.setVisible(false);
        lblCursoTitulo.setVisible(false);
        return;
    }
    
    Object[] fila = {
        alumno.getCodAlumno(),
        alumno.getNombres(),
        alumno.getApellidos(),
        alumno.getDni(),
        alumno.getEdad(),
        alumno.getCelular(),
        alumno.getEstadoTexto()
    };
    
    modeloTablaAlumno.addRow(fila);
    
    // Si el alumno está matriculado, obtener TODOS sus cursos
    if (alumno.getEstado() == 1) {
        List<Curso> cursos = alumnoDAO.obtenerCursosMatriculados(codAlumno);
        
        if (cursos != null && !cursos.isEmpty()) {
            scrollCurso.setVisible(true);
            lblCursoTitulo.setVisible(true);
            lblCursoTitulo.setText("CURSOS MATRICULADOS:"); // Cambiar a plural
            
            // Agregar TODOS los cursos a la tabla
            for (Curso curso : cursos) {
                Object[] filaCurso = {
                    curso.getCodCurso(),
                    curso.getAsignatura(),
                    curso.getCiclo(),
                    curso.getCreditos(),
                    curso.getHoras()
                };
                modeloTablaCurso.addRow(filaCurso);
            }
        } else {
            scrollCurso.setVisible(false);
            lblCursoTitulo.setVisible(false);
        }
    } else {
        scrollCurso.setVisible(false);
        lblCursoTitulo.setVisible(false);
    }
}
    //Buscar Curso
 private void buscarCurso(int codCurso) {
    limpiarTablaCurso();

    Curso curso = cursoDAO.buscarPorCodigo(codCurso);

    if (curso == null) {
        JOptionPane.showMessageDialog(this,
            "No se encontró ningún curso con el código: " + codCurso,
            "Curso no encontrado",
            JOptionPane.INFORMATION_MESSAGE);
        return;
    }

    // Aseguramos que está visible la tabla de cursos
    scrollCurso.setVisible(true);
    scrollAlumno.setVisible(false);
    lblCursoTitulo.setVisible(false);

    // Llenar tabla de cursos
    Object[] fila = {
        curso.getCodCurso(),
        curso.getAsignatura(),
        curso.getCiclo(),
        curso.getCreditos(),
        curso.getHoras()
    };
    modeloTablaCurso.addRow(fila);

    panelPrincipal.revalidate();
    panelPrincipal.repaint();
}
    
    //Limpiar
    private void limpiar() {
        txtCodigo.setText("");
        limpiarTablaAlumno();
        limpiarTablaCurso();
        scrollCurso.setVisible(false);
        lblCursoTitulo.setVisible(false);
        
        modeloTablaAlumno.setColumnIdentifiers(new String[]{
            "Código", "Nombres", "Apellidos", "DNI", 
            "Edad", "Celular", "Estado"
        });
        
        rbAlumno.setSelected(true);
        txtCodigo.requestFocus();
    }
    
    //Limpiar Tabla Alumno
    private void limpiarTablaAlumno() {
        while (modeloTablaAlumno.getRowCount() > 0) {
            modeloTablaAlumno.removeRow(0);
        }
    }
    
    //Limpiar Tabla Curso
    private void limpiarTablaCurso() {
        while (modeloTablaCurso.getRowCount() > 0) {
            modeloTablaCurso.removeRow(0);
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        buttonGroup5 = new javax.swing.ButtonGroup();
        jCheckBox1 = new javax.swing.JCheckBox();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        panelPrincipal = new javax.swing.JPanel();
        lblTipo = new javax.swing.JLabel();
        rbAlumno = new javax.swing.JRadioButton();
        rbCurso = new javax.swing.JRadioButton();
        lblCodigo = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        scrollAlumno = new javax.swing.JScrollPane();
        tblResultados = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblCursoTitulo = new javax.swing.JLabel();
        scrollCurso = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCurso = new javax.swing.JTable();
        btnLimpiar = new javax.swing.JButton();

        jCheckBox1.setText("jCheckBox1");

        setLayout(new java.awt.BorderLayout());

        panelPrincipal.setBackground(new java.awt.Color(255, 255, 255));

        lblTipo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTipo.setText("Tipo de Consulta:");

        buttonGroup1.add(rbAlumno);
        rbAlumno.setText("Alumno");
        rbAlumno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbAlumnoActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbCurso);
        rbCurso.setText("Curso");
        rbCurso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbCursoActionPerformed(evt);
            }
        });

        lblCodigo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblCodigo.setText("Codigo:");

        txtCodigo.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        txtCodigo.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoActionPerformed(evt);
            }
        });

        btnBuscar.setBackground(new java.awt.Color(41, 128, 185));
        btnBuscar.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscar.setText("🔍 Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        tblResultados.setFont(new java.awt.Font("Mongolian Baiti", 0, 11)); // NOI18N
        tblResultados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Codigo", "Nombres", "Apellidos", "DNI", "Edad", "Celular", "Estado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblResultados.setGridColor(new java.awt.Color(0, 102, 204));
        tblResultados.setRowHeight(25);
        tblResultados.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblResultados.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblResultados.setShowGrid(false);
        tblResultados.setShowVerticalLines(true);
        tblResultados.getTableHeader().setReorderingAllowed(false);
        scrollAlumno.setViewportView(tblResultados);

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));
        jPanel1.setForeground(new java.awt.Color(153, 204, 0));

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("CONSULTA DE ALUMNOS Y CURSOS");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(264, 264, 264)
                .addComponent(lblTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(293, 293, 293))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(lblTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(24, 24, 24))
        );

        lblCursoTitulo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblCursoTitulo.setForeground(new java.awt.Color(41, 128, 185));
        lblCursoTitulo.setText("CURSO MATRICULADO:");

        tblCurso.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null}
            },
            new String [] {
                "Código", "Asignatura", "Ciclo", "Créditos", "Horas"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCurso.setGridColor(new java.awt.Color(0, 0, 153));
        tblCurso.setSelectionForeground(new java.awt.Color(0, 102, 153));
        tblCurso.setShowVerticalLines(true);
        tblCurso.getTableHeader().setResizingAllowed(false);
        tblCurso.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblCurso);

        scrollCurso.setViewportView(jScrollPane1);

        btnLimpiar.setBackground(new java.awt.Color(243, 156, 18));
        btnLimpiar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLimpiar.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelPrincipalLayout = new javax.swing.GroupLayout(panelPrincipal);
        panelPrincipal.setLayout(panelPrincipalLayout);
        panelPrincipalLayout.setHorizontalGroup(
            panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelPrincipalLayout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addComponent(lblTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(47, 47, 47)
                .addComponent(rbAlumno, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(43, 43, 43)
                .addComponent(rbCurso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(476, 476, 476))
            .addGroup(panelPrincipalLayout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addComponent(lblCodigo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(txtCodigo)
                .addGap(205, 205, 205)
                .addComponent(btnBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(297, 297, 297))
            .addGroup(panelPrincipalLayout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addComponent(scrollAlumno)
                .addGap(78, 78, 78))
            .addGroup(panelPrincipalLayout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addComponent(lblCursoTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(629, 629, 629))
            .addGroup(panelPrincipalLayout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addComponent(scrollCurso)
                .addGap(78, 78, 78))
            .addGroup(panelPrincipalLayout.createSequentialGroup()
                .addGap(258, 258, 258)
                .addComponent(btnLimpiar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(530, 530, 530))
        );
        panelPrincipalLayout.setVerticalGroup(
            panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPrincipalLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(31, 31, 31)
                .addGroup(panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelPrincipalLayout.createSequentialGroup()
                        .addComponent(lblTipo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(2, 2, 2))
                    .addGroup(panelPrincipalLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rbAlumno, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(rbCurso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(14, 14, 14)
                .addGroup(panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelPrincipalLayout.createSequentialGroup()
                        .addComponent(btnBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(4, 4, 4))
                    .addGroup(panelPrincipalLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelPrincipalLayout.createSequentialGroup()
                                .addComponent(lblCodigo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(3, 3, 3))
                            .addComponent(txtCodigo))))
                .addGap(30, 30, 30)
                .addComponent(scrollAlumno, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(lblCursoTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                .addGap(12, 12, 12)
                .addComponent(scrollCurso, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                .addGap(29, 29, 29)
                .addComponent(btnLimpiar, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                .addGap(144, 144, 144))
        );

        add(panelPrincipal, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void rbAlumnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbAlumnoActionPerformed
        // TODO add your handling code here:
lblCodigo.setText("Código de alumno:");
    txtCodigo.setText("");
    txtCodigo.requestFocus();

    // Tabla principal: alumno
    modeloTablaAlumno.setColumnIdentifiers(new String[]{
        "Código", "Nombres", "Apellidos", "DNI", "Edad", "Celular", "Estado"
    });
    limpiarTablaAlumno();
    limpiarTablaCurso();

    // Visibilidad
    scrollAlumno.setVisible(true);       // Mostrar alumnos
    lblCursoTitulo.setVisible(false);    // Se mostrará solo si tiene curso
    scrollCurso.setVisible(false);       // De momento oculto

    panelPrincipal.revalidate();
    panelPrincipal.repaint();
    }//GEN-LAST:event_rbAlumnoActionPerformed

    private void rbCursoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbCursoActionPerformed
        // TODO add your handling code here:
         lblCodigo.setText("Código de curso:");
    txtCodigo.setText("");
    txtCodigo.requestFocus();

    limpiarTablaAlumno();
    limpiarTablaCurso();

    // Visibilidad
    scrollAlumno.setVisible(false);      // Ocultar tabla de alumnos
    lblCursoTitulo.setVisible(false);    // No aplica aquí
    scrollCurso.setVisible(true);        // Mostrar tabla de cursos

    panelPrincipal.revalidate();
    panelPrincipal.repaint();
    }//GEN-LAST:event_rbCursoActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
         String codigoTexto = txtCodigo.getText().trim();

    if (codigoTexto.isEmpty()) {
        JOptionPane.showMessageDialog(this,
            "Por favor, ingrese un código",
            "Campo vacío",
            JOptionPane.WARNING_MESSAGE);
        txtCodigo.requestFocus();
        return;
    }

    int codigo;
    try {
        codigo = Integer.parseInt(codigoTexto);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this,
            "El código debe ser un número entero",
            "Código inválido",
            JOptionPane.ERROR_MESSAGE);
        txtCodigo.requestFocus();
        return;
    }

    if (rbAlumno.isSelected()) {
        scrollAlumno.setVisible(true);
        buscarAlumno(codigo);
    } else if (rbCurso.isSelected()) {
        scrollCurso.setVisible(true);
        buscarCurso(codigo);
    } else {
        JOptionPane.showMessageDialog(this,
            "Seleccione Alumno o Curso.",
            "Sin tipo de consulta",
            JOptionPane.WARNING_MESSAGE);
    }

    panelPrincipal.revalidate();
    panelPrincipal.repaint();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void txtCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiar();
    }//GEN-LAST:event_btnLimpiarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCodigo;
    private javax.swing.JLabel lblCursoTitulo;
    private javax.swing.JLabel lblTipo;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel panelPrincipal;
    private javax.swing.JRadioButton rbAlumno;
    private javax.swing.JRadioButton rbCurso;
    private javax.swing.JScrollPane scrollAlumno;
    private javax.swing.JScrollPane scrollCurso;
    private javax.swing.JTable tblCurso;
    private javax.swing.JTable tblResultados;
    private javax.swing.JTextField txtCodigo;
    // End of variables declaration//GEN-END:variables
}
