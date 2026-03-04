/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package paneles;

import dao.AlumnoDAO;
import dao.CursoDAO;
import dao.MatriculaDAO;
import dao.RetiroDAO;
import modelo.Alumno;
import modelo.Curso;
import modelo.Matricula;
import modelo.Retiro;
import javax.swing.JOptionPane;
import java.text.SimpleDateFormat;

public class CCurso extends javax.swing.JPanel {

    private MatriculaDAO matriculaDAO;
    private RetiroDAO retiroDAO;
    private AlumnoDAO alumnoDAO;
    private CursoDAO cursoDAO;
    
    public CCurso() {
        initComponents();
        inicializarDAOs();
        configurarInterfaz();
    }
    
       private void inicializarDAOs() {
        matriculaDAO = new MatriculaDAO();
        retiroDAO = new RetiroDAO();
        alumnoDAO = new AlumnoDAO();
        cursoDAO = new CursoDAO();
    }
       
       private void configurarInterfaz() {
        // Seleccionar Matrícula por defecto
        rbMatricula.setSelected(true);
        
        // Limpiar todos los campos
        limpiarCampos();
        
        // Focus en el campo de búsqueda
        txtNumero.requestFocus();
        rbMatricula.setSelected(true);
rbMatriculaActionPerformed(null); // aplica los textos iniciales
    }
       
       private void buscar() {
       String numeroTexto = txtNumero.getText().trim();

    if (numeroTexto.isEmpty()) {
        JOptionPane.showMessageDialog(this,
            "Por favor, ingrese un numero de " +
            (rbMatricula.isSelected() ? "matricula" : "retiro"),
            "Campo vacio",
            JOptionPane.WARNING_MESSAGE);
        txtNumero.requestFocus();
        return;
    }

    int numero;
    try {
        numero = Integer.parseInt(numeroTexto);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this,
            "El numero debe ser un valor numerico valido",
            "Error de formato",
            JOptionPane.ERROR_MESSAGE);
        txtNumero.requestFocus();
        return;
    }

    // SOLO UNA VEZ
    if (rbMatricula.isSelected()) {
        buscarMatricula(numero);
    } else if (rbRetiro.isSelected()) {
        buscarRetiro(numero);
    } else {
        JOptionPane.showMessageDialog(this,
            "Seleccione Matricula o Retiro.",
            "Tipo no seleccionado",
            JOptionPane.WARNING_MESSAGE);
    }
} 
    
   private void buscarMatricula(int numMatricula) {
        // Limpiar campos antes de buscar
        limpiarDatos();
        
        // Buscar la matrícula
        Matricula matricula = matriculaDAO.buscarPorNumero(numMatricula);
        
        if (matricula == null) {
            JOptionPane.showMessageDialog(this,
                "No se encontro ninguna matricula con el numero: " + numMatricula,
                "Matricula no encontrada",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Mostrar datos de la matrícula
        lblNumValor.setText(String.valueOf(matricula.getNumMatricula()));
        
String fecha = matricula.getFecha(); 
String hora  = matricula.getHora();  
if (fecha != null && hora != null) {
    lblFechaValor.setText(fecha + " " + hora);
} else if (fecha != null) {
    lblFechaValor.setText(fecha);
} else {
    lblFechaValor.setText("");
}
        // Buscar y mostrar datos del alumno
        Alumno alumno = alumnoDAO.buscarPorCodigo(matricula.getCodAlumno());
        if (alumno != null) {
            lblNombresValor.setText(alumno.getNombres());
            lblApellidosValor.setText(alumno.getApellidos());
            lblCodigoAlumnoValor.setText(String.valueOf(alumno.getCodAlumno()));
            lblDniValor.setText(alumno.getDni());
            lblEstadoValor.setText(alumno.getEstado() == 1 ? "Matriculado" : "Retirado");
            lblTlfnValor.setText(String.valueOf(alumno.getCelular()));
        }
        
        // Buscar y mostrar datos del curso
        Curso curso = cursoDAO.buscarPorCodigo(matricula.getCodCurso());
        if (curso != null) {
            lblCodigoCursoValor.setText(String.valueOf(curso.getCodCurso()));
            lblCicloValor.setText(String.valueOf(curso.getCiclo()));
            lblAsignaturaValor.setText(curso.getAsignatura());
            lblCreditosValor.setText(String.valueOf(curso.getCreditos()));
            lblHorasValor.setText(String.valueOf(curso.getHoras()));
        }
    }
   
    private void buscarRetiro(int numRetiro) {
        // Limpiar campos antes de buscar
        limpiarDatos();
        
        // Buscar el retiro
        Retiro retiro = retiroDAO.buscarPorNumero(numRetiro);
        
        if (retiro == null) {
            JOptionPane.showMessageDialog(this,
                "No se encontro ningun retiro con el numero: " + numRetiro,
                "Retiro no encontrado",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Mostrar datos del retiro
        lblNumValor.setText(String.valueOf(retiro.getNumRetiro()));
        
        // Formatear fecha y hora
        // Mostrar datos del retiro
lblNumValor.setText(String.valueOf(retiro.getNumRetiro()));

String fecha = retiro.getFecha();
String hora  = retiro.getHora(); // si existe
if (fecha != null && hora != null) {
    lblFechaValor.setText(fecha + " " + hora);
} else if (fecha != null) {
    lblFechaValor.setText(fecha);
} else {
    lblFechaValor.setText("");
}
        
        // Buscar la matrícula asociada al retiro
        Matricula matricula = matriculaDAO.buscarPorNumero(retiro.getNumMatricula());
        
        if (matricula != null) {
            // Buscar y mostrar datos del alumno
            Alumno alumno = alumnoDAO.buscarPorCodigo(matricula.getCodAlumno());
            if (alumno != null) {
                lblNombresValor.setText(alumno.getNombres());
                lblApellidosValor.setText(alumno.getApellidos());
                lblCodigoAlumnoValor.setText(String.valueOf(alumno.getCodAlumno()));
                lblDniValor.setText(alumno.getDni());
                lblEstadoValor.setText(alumno.getEstado() == 1 ? "Matriculado" : "Retirado");
                lblTlfnValor.setText(String.valueOf(alumno.getCelular()));
            }
            
            // Buscar y mostrar datos del curso
            Curso curso = cursoDAO.buscarPorCodigo(matricula.getCodCurso());
            if (curso != null) {
                lblCodigoCursoValor.setText(String.valueOf(curso.getCodCurso()));
                lblCicloValor.setText(String.valueOf(curso.getCiclo()));
                lblAsignaturaValor.setText(curso.getAsignatura());
                lblCreditosValor.setText(String.valueOf(curso.getCreditos()));
                lblHorasValor.setText(String.valueOf(curso.getHoras()));
            }
        }
    }
    
    private void limpiarDatos() {
        // Datos de matrícula/retiro
        lblNumValor.setText("");
        lblFechaValor.setText("");
        
        // Datos del alumno
        lblNombresValor.setText("");
        lblApellidosValor.setText("");
        lblCodigoAlumnoValor.setText("");
        lblDniValor.setText("");
        lblEstadoValor.setText("");
        lblTlfnValor.setText("");
        
        // Datos del curso
        lblCodigoCursoValor.setText("");
        lblCicloValor.setText("");
        lblAsignaturaValor.setText("");
        lblCreditosValor.setText("");
        lblHorasValor.setText("");
    }
    
    private void limpiarCampos() {
        // Limpiar campo de búsqueda
        txtNumero.setText("");
        
        // Limpiar todos los datos
        limpiarDatos();
        
        // Seleccionar Matrícula por defecto
        rbMatricula.setSelected(true);
        
        // Focus en el campo de búsqueda
        txtNumero.requestFocus();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        lblTitulo1 = new javax.swing.JLabel();
        buttonGroup1 = new javax.swing.ButtonGroup();
        panelDatos2 = new javax.swing.JPanel();
        lblTitulo5 = new javax.swing.JLabel();
        lblNumLabel5 = new javax.swing.JLabel();
        txtNumero5 = new javax.swing.JTextField();
        lblNumLabel6 = new javax.swing.JLabel();
        txtNumero6 = new javax.swing.JTextField();
        jToolBar1 = new javax.swing.JToolBar();
        panelPrincipal = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        lblTitulo2 = new javax.swing.JLabel();
        lblTipo = new javax.swing.JLabel();
        rbMatricula = new javax.swing.JRadioButton();
        rbRetiro = new javax.swing.JRadioButton();
        btnBuscar = new javax.swing.JButton();
        panelDatos = new javax.swing.JPanel();
        lblTitulo3 = new javax.swing.JLabel();
        lblNumLabel = new javax.swing.JLabel();
        lblNumLabel2 = new javax.swing.JLabel();
        lblNumValor = new javax.swing.JLabel();
        lblFechaValor = new javax.swing.JLabel();
        lblNumero = new javax.swing.JLabel();
        btnLimpiar = new javax.swing.JButton();
        txtNumero = new javax.swing.JTextField();
        panelCurso = new javax.swing.JPanel();
        lblTitulo6 = new javax.swing.JLabel();
        lblCodigoCursoLabel = new javax.swing.JLabel();
        lblCicloLabel = new javax.swing.JLabel();
        lblCreditosLabel = new javax.swing.JLabel();
        lblHorasLabel = new javax.swing.JLabel();
        lblCreditosValor = new javax.swing.JLabel();
        lblCodigoCursoValor = new javax.swing.JLabel();
        lblCicloValor = new javax.swing.JLabel();
        lblHorasValor = new javax.swing.JLabel();
        lblAsignaturaLabel = new javax.swing.JLabel();
        lblAsignaturaValor = new javax.swing.JLabel();
        panelAlumno = new javax.swing.JPanel();
        lblTitulo7 = new javax.swing.JLabel();
        lblNumLabel18 = new javax.swing.JLabel();
        lblApellidosLabel = new javax.swing.JLabel();
        lblCodigoAlumnoLabel = new javax.swing.JLabel();
        lblDniLabel = new javax.swing.JLabel();
        lblNombresValor = new javax.swing.JLabel();
        lblApellidosValor = new javax.swing.JLabel();
        lblCodigoAlumnoValor = new javax.swing.JLabel();
        lblDniValor = new javax.swing.JLabel();
        lblEstadoLabel = new javax.swing.JLabel();
        lblTlfnLabel = new javax.swing.JLabel();
        lblEstadoValor = new javax.swing.JLabel();
        lblTlfnValor = new javax.swing.JLabel();

        jPanel2.setBackground(new java.awt.Color(25, 42, 86));
        jPanel2.setForeground(new java.awt.Color(153, 204, 0));

        lblTitulo1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTitulo1.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo1.setText("CONSULTA DE ALUMNOS Y CURSOS");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(264, 264, 264)
                .addComponent(lblTitulo1)
                .addContainerGap(238, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(lblTitulo1)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        panelDatos2.setBackground(new java.awt.Color(248, 249, 250));
        panelDatos2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(41, 128, 185), 1, true));
        panelDatos2.setToolTipText("");
        panelDatos2.setName(""); // NOI18N

        lblTitulo5.setBackground(new java.awt.Color(25, 42, 86));
        lblTitulo5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTitulo5.setForeground(new java.awt.Color(25, 42, 86));
        lblTitulo5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo5.setText("DATOS DE MATRICULA");

        lblNumLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblNumLabel5.setText("Numero:");

        txtNumero5.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        txtNumero5.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtNumero5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumero5ActionPerformed(evt);
            }
        });

        lblNumLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblNumLabel6.setText("Fecha:");

        txtNumero6.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        txtNumero6.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtNumero6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumero6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelDatos2Layout = new javax.swing.GroupLayout(panelDatos2);
        panelDatos2.setLayout(panelDatos2Layout);
        panelDatos2Layout.setHorizontalGroup(
            panelDatos2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDatos2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDatos2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDatos2Layout.createSequentialGroup()
                        .addComponent(lblTitulo5)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelDatos2Layout.createSequentialGroup()
                        .addComponent(lblNumLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(txtNumero5, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(lblNumLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(txtNumero6, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelDatos2Layout.setVerticalGroup(
            panelDatos2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDatos2Layout.createSequentialGroup()
                .addComponent(lblTitulo5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelDatos2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNumLabel5)
                    .addComponent(txtNumero5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNumLabel6)
                    .addComponent(txtNumero6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jToolBar1.setRollover(true);

        panelPrincipal.setBackground(new java.awt.Color(255, 255, 255));
        panelPrincipal.setPreferredSize(new java.awt.Dimension(870, 605));
        panelPrincipal.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                panelPrincipalAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        jPanel3.setBackground(new java.awt.Color(0, 102, 102));
        jPanel3.setForeground(new java.awt.Color(153, 204, 0));
        jPanel3.setPreferredSize(new java.awt.Dimension(866, 70));

        lblTitulo2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTitulo2.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo2.setText("CONSULTA DE REGISTRO Y RETIROS");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(273, 273, 273)
                .addComponent(lblTitulo2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(271, 271, 271))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(lblTitulo2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(24, 24, 24))
        );

        lblTipo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTipo.setText("Tipo de Consulta:");

        buttonGroup1.add(rbMatricula);
        rbMatricula.setText("Matricula");
        rbMatricula.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        rbMatricula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbMatriculaActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbRetiro);
        rbRetiro.setText("Retiro");
        rbRetiro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbRetiroActionPerformed(evt);
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

        panelDatos.setBackground(new java.awt.Color(248, 249, 250));
        panelDatos.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(41, 128, 185), 1, true));
        panelDatos.setToolTipText("");
        panelDatos.setName(""); // NOI18N

        lblTitulo3.setBackground(new java.awt.Color(25, 42, 86));
        lblTitulo3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTitulo3.setForeground(new java.awt.Color(25, 42, 86));
        lblTitulo3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo3.setText("DATOS DE MATRICULA");

        lblNumLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblNumLabel.setText("Numero:");

        lblNumLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblNumLabel2.setText("Fecha/Hora:");

        lblNumValor.setBackground(new java.awt.Color(255, 255, 255));
        lblNumValor.setForeground(new java.awt.Color(44, 62, 80));

        lblFechaValor.setBackground(new java.awt.Color(255, 255, 255));
        lblFechaValor.setForeground(new java.awt.Color(44, 62, 80));

        javax.swing.GroupLayout panelDatosLayout = new javax.swing.GroupLayout(panelDatos);
        panelDatos.setLayout(panelDatosLayout);
        panelDatosLayout.setHorizontalGroup(
            panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDatosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDatosLayout.createSequentialGroup()
                        .addComponent(lblTitulo3)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelDatosLayout.createSequentialGroup()
                        .addComponent(lblNumLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblNumValor, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblNumLabel2)
                        .addGap(42, 42, 42)
                        .addComponent(lblFechaValor, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17))))
        );
        panelDatosLayout.setVerticalGroup(
            panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDatosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitulo3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNumLabel)
                    .addComponent(lblNumLabel2)
                    .addComponent(lblNumValor)
                    .addComponent(lblFechaValor))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        lblNumero.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblNumero.setText("Numero:");

        btnLimpiar.setBackground(new java.awt.Color(243, 156, 18));
        btnLimpiar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLimpiar.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        txtNumero.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        txtNumero.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtNumero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeroActionPerformed(evt);
            }
        });

        panelCurso.setBackground(new java.awt.Color(248, 249, 250));
        panelCurso.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(41, 128, 185), 1, true));
        panelCurso.setToolTipText("");
        panelCurso.setName(""); // NOI18N

        lblTitulo6.setBackground(new java.awt.Color(25, 42, 86));
        lblTitulo6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTitulo6.setForeground(new java.awt.Color(25, 42, 86));
        lblTitulo6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo6.setText("DATOS DEL CURSO:");

        lblCodigoCursoLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblCodigoCursoLabel.setText("Codigo:");

        lblCicloLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblCicloLabel.setText("Ciclo:");

        lblCreditosLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblCreditosLabel.setText("Credito:");

        lblHorasLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblHorasLabel.setText("Hora:");

        lblCreditosValor.setBackground(new java.awt.Color(255, 255, 255));
        lblCreditosValor.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 51, 102), 1, true));
        lblCreditosValor.setOpaque(true);

        lblCodigoCursoValor.setBackground(new java.awt.Color(255, 255, 255));
        lblCodigoCursoValor.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 51, 102), 1, true));
        lblCodigoCursoValor.setOpaque(true);

        lblCicloValor.setBackground(new java.awt.Color(255, 255, 255));
        lblCicloValor.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 51, 102), 1, true));
        lblCicloValor.setOpaque(true);

        lblHorasValor.setBackground(new java.awt.Color(255, 255, 255));
        lblHorasValor.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 51, 102), 1, true));
        lblHorasValor.setOpaque(true);

        lblAsignaturaLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblAsignaturaLabel.setText("Curso:");

        lblAsignaturaValor.setBackground(new java.awt.Color(255, 255, 255));
        lblAsignaturaValor.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 51, 102), 1, true));
        lblAsignaturaValor.setOpaque(true);

        javax.swing.GroupLayout panelCursoLayout = new javax.swing.GroupLayout(panelCurso);
        panelCurso.setLayout(panelCursoLayout);
        panelCursoLayout.setHorizontalGroup(
            panelCursoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCursoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCursoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCursoLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(panelCursoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelCursoLayout.createSequentialGroup()
                                .addComponent(lblCreditosLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblCreditosValor, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblHorasLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblHorasValor, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelCursoLayout.createSequentialGroup()
                                .addGroup(panelCursoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblCodigoCursoLabel)
                                    .addComponent(lblAsignaturaLabel))
                                .addGap(18, 18, 18)
                                .addGroup(panelCursoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblAsignaturaValor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(panelCursoLayout.createSequentialGroup()
                                        .addComponent(lblCodigoCursoValor, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                                        .addComponent(lblCicloLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lblCicloValor, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addComponent(lblTitulo6))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelCursoLayout.setVerticalGroup(
            panelCursoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCursoLayout.createSequentialGroup()
                .addComponent(lblTitulo6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelCursoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCursoLayout.createSequentialGroup()
                        .addGroup(panelCursoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelCursoLayout.createSequentialGroup()
                                .addComponent(lblCodigoCursoLabel)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(lblCodigoCursoValor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblCicloValor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelCursoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblAsignaturaLabel)
                            .addComponent(lblAsignaturaValor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(14, 14, 14)
                        .addGroup(panelCursoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblHorasLabel)
                            .addComponent(lblCreditosLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblCreditosValor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblHorasValor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(15, 15, 15))
                    .addGroup(panelCursoLayout.createSequentialGroup()
                        .addComponent(lblCicloLabel)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        panelAlumno.setBackground(new java.awt.Color(248, 249, 250));
        panelAlumno.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(41, 128, 185), 1, true));
        panelAlumno.setToolTipText("");
        panelAlumno.setName(""); // NOI18N

        lblTitulo7.setBackground(new java.awt.Color(25, 42, 86));
        lblTitulo7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTitulo7.setForeground(new java.awt.Color(25, 42, 86));
        lblTitulo7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo7.setText("DATOS DEL ALUMNO:");

        lblNumLabel18.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblNumLabel18.setText("Nombres:");

        lblApellidosLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblApellidosLabel.setText("Apellidos:");

        lblCodigoAlumnoLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblCodigoAlumnoLabel.setText("Codigo:");

        lblDniLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblDniLabel.setText("DNI:");

        lblNombresValor.setBackground(new java.awt.Color(255, 255, 255));
        lblNombresValor.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 51, 102), 1, true));
        lblNombresValor.setOpaque(true);

        lblApellidosValor.setBackground(new java.awt.Color(255, 255, 255));
        lblApellidosValor.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 51, 102), 1, true));
        lblApellidosValor.setOpaque(true);

        lblCodigoAlumnoValor.setBackground(new java.awt.Color(255, 255, 255));
        lblCodigoAlumnoValor.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 51, 102), 1, true));
        lblCodigoAlumnoValor.setOpaque(true);

        lblDniValor.setBackground(new java.awt.Color(255, 255, 255));
        lblDniValor.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 51, 102), 1, true));
        lblDniValor.setOpaque(true);

        lblEstadoLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblEstadoLabel.setText("Estado:");

        lblTlfnLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTlfnLabel.setText("Tlfn:");

        lblEstadoValor.setBackground(new java.awt.Color(255, 255, 255));
        lblEstadoValor.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 51, 102), 1, true));
        lblEstadoValor.setOpaque(true);

        lblTlfnValor.setBackground(new java.awt.Color(255, 255, 255));
        lblTlfnValor.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 51, 102), 1, true));
        lblTlfnValor.setOpaque(true);

        javax.swing.GroupLayout panelAlumnoLayout = new javax.swing.GroupLayout(panelAlumno);
        panelAlumno.setLayout(panelAlumnoLayout);
        panelAlumnoLayout.setHorizontalGroup(
            panelAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAlumnoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAlumnoLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(panelAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelAlumnoLayout.createSequentialGroup()
                                .addComponent(lblCodigoAlumnoLabel)
                                .addGap(18, 18, 18)
                                .addComponent(lblCodigoAlumnoValor, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblDniLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblDniValor, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelAlumnoLayout.createSequentialGroup()
                                .addComponent(lblNumLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblNombresValor, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelAlumnoLayout.createSequentialGroup()
                                .addComponent(lblApellidosLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblApellidosValor, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelAlumnoLayout.createSequentialGroup()
                                .addComponent(lblEstadoLabel)
                                .addGap(18, 18, 18)
                                .addComponent(lblEstadoValor, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblTlfnLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTlfnValor, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(lblTitulo7))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelAlumnoLayout.setVerticalGroup(
            panelAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAlumnoLayout.createSequentialGroup()
                .addComponent(lblTitulo7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblNumLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblNombresValor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblApellidosLabel)
                    .addComponent(lblApellidosValor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(panelAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAlumnoLayout.createSequentialGroup()
                        .addGroup(panelAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDniLabel)
                            .addComponent(lblCodigoAlumnoLabel))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(lblCodigoAlumnoValor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblDniValor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTlfnLabel)
                    .addComponent(lblEstadoLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblEstadoValor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblTlfnValor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(19, 19, 19))
        );

        javax.swing.GroupLayout panelPrincipalLayout = new javax.swing.GroupLayout(panelPrincipal);
        panelPrincipal.setLayout(panelPrincipalLayout);
        panelPrincipalLayout.setHorizontalGroup(
            panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 854, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPrincipalLayout.createSequentialGroup()
                .addGap(108, 108, 108)
                .addGroup(panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelPrincipalLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(lblTipo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(47, 47, 47)
                        .addComponent(rbMatricula, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(43, 43, 43)
                        .addComponent(rbRetiro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(297, 297, 297))
                    .addGroup(panelPrincipalLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(lblNumero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(txtNumero)
                        .addGap(206, 206, 206)
                        .addComponent(btnBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(120, 120, 120))
                    .addComponent(panelDatos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelPrincipalLayout.createSequentialGroup()
                        .addComponent(panelAlumno, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(23, 23, 23)
                        .addComponent(panelCurso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelPrincipalLayout.createSequentialGroup()
                        .addGap(227, 227, 227)
                        .addComponent(btnLimpiar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(355, 355, 355)))
                .addGap(77, 77, 77))
        );
        panelPrincipalLayout.setVerticalGroup(
            panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPrincipalLayout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addGroup(panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelPrincipalLayout.createSequentialGroup()
                        .addComponent(lblTipo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(2, 2, 2))
                    .addGroup(panelPrincipalLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rbMatricula, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(rbRetiro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(20, 20, 20)
                .addGroup(panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelPrincipalLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelPrincipalLayout.createSequentialGroup()
                                .addComponent(lblNumero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(3, 3, 3))
                            .addComponent(txtNumero))
                        .addGap(4, 4, 4)))
                .addGap(18, 18, 18)
                .addComponent(panelDatos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelAlumno, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelPrincipalLayout.createSequentialGroup()
                        .addComponent(panelCurso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(34, 34, 34)))
                .addGap(18, 18, 18)
                .addComponent(btnLimpiar, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addGap(38, 38, 38))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 854, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 542, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void rbMatriculaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbMatriculaActionPerformed
        // TODO add your handling code here:
        lblTitulo2.setText("CONSULTA DE MATRICULAS");
    lblTitulo3.setText("DATOS DE MATRICULA");
    lblNumero.setText("Numero de matricula:");
    
    limpiarDatos();
    txtNumero.setText("");
    txtNumero.requestFocus();
    }//GEN-LAST:event_rbMatriculaActionPerformed

    private void rbRetiroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbRetiroActionPerformed
        // TODO add your handling code here:
         lblTitulo2.setText("CONSULTA DE RETIROS");
    lblTitulo3.setText("DATOS DE RETIRO");
    lblNumero.setText("Numero de retiro:");
    
    limpiarDatos();
    txtNumero.setText("");
    txtNumero.requestFocus();
    }//GEN-LAST:event_rbRetiroActionPerformed

    private void txtNumeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroActionPerformed
        // TODO add your handling code here:
        buscar();
    }//GEN-LAST:event_txtNumeroActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        buscar();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void txtNumero5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumero5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumero5ActionPerformed

    private void txtNumero6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumero6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumero6ActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiarCampos();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void panelPrincipalAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_panelPrincipalAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_panelPrincipalAncestorAdded


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblApellidosLabel;
    private javax.swing.JLabel lblApellidosValor;
    private javax.swing.JLabel lblAsignaturaLabel;
    private javax.swing.JLabel lblAsignaturaValor;
    private javax.swing.JLabel lblCicloLabel;
    private javax.swing.JLabel lblCicloValor;
    private javax.swing.JLabel lblCodigoAlumnoLabel;
    private javax.swing.JLabel lblCodigoAlumnoValor;
    private javax.swing.JLabel lblCodigoCursoLabel;
    private javax.swing.JLabel lblCodigoCursoValor;
    private javax.swing.JLabel lblCreditosLabel;
    private javax.swing.JLabel lblCreditosValor;
    private javax.swing.JLabel lblDniLabel;
    private javax.swing.JLabel lblDniValor;
    private javax.swing.JLabel lblEstadoLabel;
    private javax.swing.JLabel lblEstadoValor;
    private javax.swing.JLabel lblFechaValor;
    private javax.swing.JLabel lblHorasLabel;
    private javax.swing.JLabel lblHorasValor;
    private javax.swing.JLabel lblNombresValor;
    private javax.swing.JLabel lblNumLabel;
    private javax.swing.JLabel lblNumLabel18;
    private javax.swing.JLabel lblNumLabel2;
    private javax.swing.JLabel lblNumLabel5;
    private javax.swing.JLabel lblNumLabel6;
    private javax.swing.JLabel lblNumValor;
    private javax.swing.JLabel lblNumero;
    private javax.swing.JLabel lblTipo;
    private javax.swing.JLabel lblTitulo1;
    private javax.swing.JLabel lblTitulo2;
    private javax.swing.JLabel lblTitulo3;
    private javax.swing.JLabel lblTitulo5;
    private javax.swing.JLabel lblTitulo6;
    private javax.swing.JLabel lblTitulo7;
    private javax.swing.JLabel lblTlfnLabel;
    private javax.swing.JLabel lblTlfnValor;
    private javax.swing.JPanel panelAlumno;
    private javax.swing.JPanel panelCurso;
    private javax.swing.JPanel panelDatos;
    private javax.swing.JPanel panelDatos2;
    private javax.swing.JPanel panelPrincipal;
    private javax.swing.JRadioButton rbMatricula;
    private javax.swing.JRadioButton rbRetiro;
    private javax.swing.JTextField txtNumero;
    private javax.swing.JTextField txtNumero5;
    private javax.swing.JTextField txtNumero6;
    // End of variables declaration//GEN-END:variables
}
