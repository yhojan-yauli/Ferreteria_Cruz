/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package interfaces;

import dao.AlumnoDAO;
import interfaces.JFLogin;
import java.awt.Color;
import java.awt.HeadlessException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.Alumno;

/**
 *
 * @author Cesar
 */
public class JFRegistroAlumnos extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(JFRegistroAlumnos.class.getName());
    private AlumnoDAO alumnoDAO;
    private DefaultTableModel modeloTabla;
    private int codigoAlumnoSeleccionado = -1; // Para saber qué alumno está seleccionado

 private void configurarTabla() {
    // Configurar el modelo de la tabla CON PASSWORD
    modeloTabla = new DefaultTableModel(
        new Object[]{"Código", "Nombres", "Apellidos", "DNI", "Contraseña", "Edad", "Celular"},  // ⬅️ AGREGAR "Contraseña"
        0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    tblAlumnos1.setModel(modeloTabla);
    
    // Ajustar ancho de columnas
    tblAlumnos1.getColumnModel().getColumn(0).setPreferredWidth(60);  // Código
    tblAlumnos1.getColumnModel().getColumn(1).setPreferredWidth(120); // Nombres
    tblAlumnos1.getColumnModel().getColumn(2).setPreferredWidth(120); // Apellidos
    tblAlumnos1.getColumnModel().getColumn(3).setPreferredWidth(80);  // DNI
    tblAlumnos1.getColumnModel().getColumn(4).setPreferredWidth(80);  // Contraseña
    tblAlumnos1.getColumnModel().getColumn(5).setPreferredWidth(50);  // Edad
    tblAlumnos1.getColumnModel().getColumn(6).setPreferredWidth(90);  // Celular
}
private void cargarDatosTabla() {
    try {
        System.out.println("Limpiando tabla...");
        // Limpiar la tabla
        modeloTabla.setRowCount(0);
        
        System.out.println("Obteniendo alumnos de la BD...");
        // Obtener alumnos de la BD
        List<Alumno> alumnos = alumnoDAO.listarAlumnos();
        
        System.out.println("Alumnos obtenidos: " + (alumnos != null ? alumnos.size() : "null"));
        
        if (alumnos == null) {
            System.err.println("La lista de alumnos es NULL");
            return;
        }
        
        // Cargar datos en la tabla
        System.out.println("Cargando " + alumnos.size() + " alumnos en la tabla...");
        for (Alumno alumno : alumnos) {
            System.out.println("Cargando alumno: " + alumno.getNombres());
            Object[] fila = {
                alumno.getCodAlumno(),
                alumno.getNombres(),
                alumno.getApellidos(),
                alumno.getDni(),
                alumno.getPassword(),
                alumno.getEdad(),
                alumno.getCelular()
            };
            modeloTabla.addRow(fila);
        }
        
        System.out.println("Tabla cargada exitosamente");
        
    } catch (Exception e) {
        System.err.println("ERROR en cargarDatosTabla:");
        e.printStackTrace();
    }
}
    private void configurarEventoTabla() {
        // Al hacer clic en una fila, cargar los datos en los campos
        tblAlumnos1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int fila = tblAlumnos1.getSelectedRow();
                if (fila != -1) {
                    cargarDatosEnCampos(fila);
                }
            }
        });
    }

private void cargarDatosEnCampos(int fila) {
    codigoAlumnoSeleccionado = (int) tblAlumnos1.getValueAt(fila, 0);
    txtNombre1.setText(tblAlumnos1.getValueAt(fila, 1).toString());
    txtApellido1.setText(tblAlumnos1.getValueAt(fila, 2).toString());
    txtDNI1.setText(tblAlumnos1.getValueAt(fila, 3).toString());
    txtPassword1.setText(tblAlumnos1.getValueAt(fila, 4).toString());  // ⬅️ CAMBIAR ÍNDICE
    txtEdad1.setText(tblAlumnos1.getValueAt(fila, 5).toString());      // ⬅️ CAMBIAR ÍNDICE
    txtCelular1.setText(tblAlumnos1.getValueAt(fila, 6).toString());   // ⬅️ CAMBIAR ÍNDICE
}
  
   private void limpiarCampos() {
    txtNombre1.setText("");
    txtApellido1.setText("");
    txtDNI1.setText("");
    txtPassword1.setText(""); 
    txtEdad1.setText("");
    txtCelular1.setText("");
    txtBuscar.setText("");
    codigoAlumnoSeleccionado = -1;
    tblAlumnos1.clearSelection();
}

private boolean validarCampos() {
    // Validar campos vacíos
    if (txtNombre1.getText().trim().isEmpty() ||
        txtApellido1.getText().trim().isEmpty() ||
        txtDNI1.getText().trim().isEmpty() ||
        txtPassword1.getText().trim().isEmpty() ||  // ⬅️ AGREGAR ESTO
        txtEdad1.getText().trim().isEmpty() ||
        txtCelular1.getText().trim().isEmpty()) {
        
        JOptionPane.showMessageDialog(this,
            "Por favor complete todos los campos",
            "Campos vacíos",
            JOptionPane.WARNING_MESSAGE);
        return false;
    }
    
    // Validar que nombres y apellidos solo contengan letras y espacios
    String nombre = txtNombre1.getText().trim();
    String apellido = txtApellido1.getText().trim();
    
    if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
        JOptionPane.showMessageDialog(this,
            "El nombre solo debe contener letras",
            "Nombre inválido",
            JOptionPane.WARNING_MESSAGE);
        return false;
    }
    
    if (!apellido.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
        JOptionPane.showMessageDialog(this,
            "El apellido solo debe contener letras",
            "Apellido inválido",
            JOptionPane.WARNING_MESSAGE);
        return false;
    }
    
    // Validar DNI (8 dígitos exactos)
    String dni = txtDNI1.getText().trim();
    if (!dni.matches("\\d{8}")) {
        JOptionPane.showMessageDialog(this,
            "El DNI debe tener exactamente 8 dígitos",
            "DNI inválido",
            JOptionPane.WARNING_MESSAGE);
        return false;
    }
    
    // Validar password (mínimo 6 caracteres)
    String password = txtPassword1.getText().trim();
    if (password.length() < 6) {
        JOptionPane.showMessageDialog(this,
            "La contraseña debe tener al menos 6 caracteres",
            "Contraseña inválida",
            JOptionPane.WARNING_MESSAGE);
        return false;
    }
    
    // Validar edad (números y rango válido)
    try {
        int edad = Integer.parseInt(txtEdad1.getText().trim());
        if (edad < 1 || edad > 120) {
            JOptionPane.showMessageDialog(this,
                "La edad debe estar entre 1 y 120 años",
                "Edad inválida",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this,
            "La edad debe ser un número válido",
            "Edad inválida",
            JOptionPane.ERROR_MESSAGE);
        return false;
    }
    
    // Validar celular (9 dígitos exactos para Perú)
    String celular = txtCelular1.getText().trim();
    if (!celular.matches("\\d{9}")) {
        JOptionPane.showMessageDialog(this,
            "El celular debe tener exactamente 9 dígitos",
            "Celular inválido",
            JOptionPane.WARNING_MESSAGE);
        return false;
    }
    
    return true;
}


public JFRegistroAlumnos() {
    initComponents();
    setLocationRelativeTo(null);
    
    alumnoDAO = new AlumnoDAO();
    configurarTabla();
    cargarDatosTabla();
    configurarEventoTabla();
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblAlumnos1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtEdad1 = new javax.swing.JTextField();
        btnActualizar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnRegistrar3 = new javax.swing.JButton();
        txtNombre1 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtApellido1 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtDNI1 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtCelular1 = new javax.swing.JTextField();
        txtBuscar = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        btnRegistrar4 = new javax.swing.JButton();
        txtPassword1 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(0, 102, 102));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("REGISTRO DE ALUMNOS");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(133, 133, 133))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        tblAlumnos1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CODIGO", "NOMBRE", "APELLIDO", "DNI", "EDAD", "CELULAR"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAlumnos1.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tblAlumnos1);
        if (tblAlumnos1.getColumnModel().getColumnCount() > 0) {
            tblAlumnos1.getColumnModel().getColumn(0).setResizable(false);
            tblAlumnos1.getColumnModel().getColumn(1).setResizable(false);
            tblAlumnos1.getColumnModel().getColumn(2).setResizable(false);
            tblAlumnos1.getColumnModel().getColumn(3).setResizable(false);
            tblAlumnos1.getColumnModel().getColumn(4).setResizable(false);
            tblAlumnos1.getColumnModel().getColumn(5).setResizable(false);
        }

        jLabel1.setFont(new java.awt.Font("Franklin Gothic Demi", 0, 18)); // NOI18N
        jLabel1.setText("NOMBRE");

        btnActualizar.setBackground(new java.awt.Color(255, 153, 51));
        btnActualizar.setFont(new java.awt.Font("Microsoft YaHei", 1, 12)); // NOI18N
        btnActualizar.setForeground(new java.awt.Color(255, 255, 255));
        btnActualizar.setText("ACTUALIZAR");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        btnEliminar.setBackground(new java.awt.Color(255, 153, 51));
        btnEliminar.setFont(new java.awt.Font("Microsoft YaHei", 1, 12)); // NOI18N
        btnEliminar.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminar.setText("ELIMINAR");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnLimpiar.setBackground(new java.awt.Color(255, 153, 51));
        btnLimpiar.setFont(new java.awt.Font("Microsoft YaHei", 1, 12)); // NOI18N
        btnLimpiar.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiar.setText("LIMPIAR");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        btnRegistrar3.setBackground(new java.awt.Color(255, 153, 51));
        btnRegistrar3.setFont(new java.awt.Font("Microsoft YaHei", 1, 12)); // NOI18N
        btnRegistrar3.setForeground(new java.awt.Color(255, 255, 255));
        btnRegistrar3.setText("REGISTRAR");
        btnRegistrar3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrar3ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Franklin Gothic Demi", 0, 18)); // NOI18N
        jLabel7.setText("EDAD:");

        jLabel8.setFont(new java.awt.Font("Franklin Gothic Demi", 0, 18)); // NOI18N
        jLabel8.setText("APELLIDO");

        jLabel9.setFont(new java.awt.Font("Franklin Gothic Demi", 0, 18)); // NOI18N
        jLabel9.setText("DNI:");

        jLabel10.setFont(new java.awt.Font("Franklin Gothic Demi", 0, 18)); // NOI18N
        jLabel10.setText("CELULAR:");

        btnBuscar.setBackground(new java.awt.Color(255, 153, 51));
        btnBuscar.setFont(new java.awt.Font("Microsoft YaHei", 1, 12)); // NOI18N
        btnBuscar.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscar.setText("BUSCAR");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnRegistrar4.setBackground(new java.awt.Color(255, 153, 51));
        btnRegistrar4.setFont(new java.awt.Font("Microsoft YaHei", 1, 12)); // NOI18N
        btnRegistrar4.setForeground(new java.awt.Color(255, 255, 255));
        btnRegistrar4.setText("VOLVER");
        btnRegistrar4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrar4ActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Franklin Gothic Demi", 0, 18)); // NOI18N
        jLabel11.setText("CONTRA:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(26, 26, 26)
                                        .addComponent(txtNombre1, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtApellido1, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txtPassword1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(37, 37, 37)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(btnRegistrar3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtDNI1, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                                        .addComponent(txtCelular1, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtEdad1, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE)))))
                        .addGap(50, 50, 50))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(136, 136, 136)
                        .addComponent(btnRegistrar4, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtNombre1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(17, 17, 17)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtApellido1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(txtDNI1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14, 14, 14)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPassword1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtEdad1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCelular1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnRegistrar3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(39, 39, 39)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35)
                        .addComponent(btnRegistrar4, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 530, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(50, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed

  
    if (codigoAlumnoSeleccionado == -1) {
        JOptionPane.showMessageDialog(this,
            "Por favor seleccione un alumno de la tabla",
            "No hay selección",
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    if (!validarCampos()) {
        return;
    }
    
    // Crear el objeto Alumno con los datos actualizados
    Alumno alumnoActualizado = new Alumno();
    alumnoActualizado.setCodAlumno(codigoAlumnoSeleccionado);
    alumnoActualizado.setNombres(txtNombre1.getText().trim());
    alumnoActualizado.setApellidos(txtApellido1.getText().trim());
    alumnoActualizado.setDni(txtDNI1.getText().trim());
    alumnoActualizado.setPassword(txtPassword1.getText().trim());  // ⬅️ AGREGAR ESTO
    alumnoActualizado.setEdad(Integer.parseInt(txtEdad1.getText().trim()));
    alumnoActualizado.setCelular(Integer.parseInt(txtCelular1.getText().trim()));
    
    // Actualizar en la BD
    if (alumnoDAO.actualizarAlumno(alumnoActualizado)) {
        JOptionPane.showMessageDialog(this,
            "Alumno actualizado exitosamente",
            "Actualización exitosa",
            JOptionPane.INFORMATION_MESSAGE);
        
        limpiarCampos();
        cargarDatosTabla();
    } else {
        JOptionPane.showMessageDialog(this,
            "Error al actualizar el alumno",
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }

        
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
       
         if (codigoAlumnoSeleccionado == -1) {
        JOptionPane.showMessageDialog(this,
            "Por favor seleccione un alumno de la tabla",
            "No hay selección",
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    int confirmacion = JOptionPane.showConfirmDialog(this,
        "¿Está seguro de eliminar este alumno?",
        "Confirmar eliminación",
        JOptionPane.YES_NO_OPTION);
    
    if (confirmacion == JOptionPane.YES_OPTION) {
        if (alumnoDAO.eliminarAlumno(codigoAlumnoSeleccionado)) {
            JOptionPane.showMessageDialog(this,
                "Alumno eliminado exitosamente",
                "Eliminación exitosa",
                JOptionPane.INFORMATION_MESSAGE);
            
            limpiarCampos();
            cargarDatosTabla();
        } else {
            JOptionPane.showMessageDialog(this,
                "Error al eliminar el alumno",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

        
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        
           limpiarCampos();

        
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnRegistrar3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrar3ActionPerformed

   
    if (!validarCampos()) {
        return;
    }
    
    // Verificar que el DNI no esté registrado
    Alumno alumnoExistente = alumnoDAO.buscarPorDNI(txtDNI1.getText().trim());
    if (alumnoExistente != null) {
        JOptionPane.showMessageDialog(this,
            "El DNI ya está registrado",
            "DNI duplicado",
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // Crear el objeto Alumno
    Alumno nuevoAlumno = new Alumno();
    nuevoAlumno.setNombres(txtNombre1.getText().trim());
    nuevoAlumno.setApellidos(txtApellido1.getText().trim());
    nuevoAlumno.setDni(txtDNI1.getText().trim());
    nuevoAlumno.setPassword(txtPassword1.getText().trim());  // ⬅️ AGREGAR ESTO
    nuevoAlumno.setEdad(Integer.parseInt(txtEdad1.getText().trim()));
    nuevoAlumno.setCelular(Integer.parseInt(txtCelular1.getText().trim()));
    
    // Insertar en la BD
    if (alumnoDAO.insertarAlumno(nuevoAlumno)) {
        JOptionPane.showMessageDialog(this,
            "Alumno registrado exitosamente",
            "Registro exitoso",
            JOptionPane.INFORMATION_MESSAGE);
        
        limpiarCampos();
        cargarDatosTabla();
    } else {
        JOptionPane.showMessageDialog(this,
            "Error al registrar el alumno",
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }

    }//GEN-LAST:event_btnRegistrar3ActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
    
        // BOTÓN BUSCAR

    String nombreBuscar = txtBuscar.getText().trim();
    
    if (nombreBuscar.isEmpty()) {
        JOptionPane.showMessageDialog(this,
            "Por favor ingrese un nombre para buscar",
            "Campo vacío",
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    Alumno alumnoEncontrado = alumnoDAO.buscarPorNombre(nombreBuscar);
    
    if (alumnoEncontrado != null) {
        // Cargar datos en los campos
        codigoAlumnoSeleccionado = alumnoEncontrado.getCodAlumno();
        txtNombre1.setText(alumnoEncontrado.getNombres());
        txtApellido1.setText(alumnoEncontrado.getApellidos());
        txtDNI1.setText(alumnoEncontrado.getDni());
        txtEdad1.setText(String.valueOf(alumnoEncontrado.getEdad()));
        txtCelular1.setText(String.valueOf(alumnoEncontrado.getCelular()));
        
        // Resaltar en la tabla
        for (int i = 0; i < tblAlumnos1.getRowCount(); i++) {
            if ((int)tblAlumnos1.getValueAt(i, 0) == alumnoEncontrado.getCodAlumno()) {
                tblAlumnos1.setRowSelectionInterval(i, i);
                tblAlumnos1.scrollRectToVisible(tblAlumnos1.getCellRect(i, 0, true));
                break;
            }
        }
        
    } else {
        JOptionPane.showMessageDialog(this,
            "No se encontró ningún alumno con ese nombre",
            "Alumno no encontrado",
            JOptionPane.INFORMATION_MESSAGE);
    }


        
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnRegistrar4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrar4ActionPerformed
        
    // Cerrar la ventana actual
    this.dispose();
    
    // Abrir el Login
    JFLogin login = new JFLogin();
    login.setVisible(true);
        
    }//GEN-LAST:event_btnRegistrar4ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new JFRegistroAlumnos().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnRegistrar3;
    private javax.swing.JButton btnRegistrar4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblAlumnos1;
    private javax.swing.JTextField txtApellido1;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtCelular1;
    private javax.swing.JTextField txtDNI1;
    private javax.swing.JTextField txtEdad1;
    private javax.swing.JTextField txtNombre1;
    private javax.swing.JTextField txtPassword1;
    // End of variables declaration//GEN-END:variables
}
