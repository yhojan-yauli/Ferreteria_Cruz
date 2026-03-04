package paneles;

import dao.AlumnoDAO;
import dao.CursoDAO;
import dao.MatriculaDAO;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.Alumno;
import modelo.Curso;
import modelo.Matricula;


public class lorena extends javax.swing.JPanel {
    private MatriculaDAO matriculaDao;
    private AlumnoDAO alumnoDao;
    private CursoDAO cursoDao;
    private List<Alumno> listAlumnos = new ArrayList<>();
    private List<Curso> listCursos = new ArrayList<>();
    private Alumno alumnoLogueado;

   
    public lorena(){
        this(null);
    }
    public lorena(Alumno alumno) {
       this.alumnoLogueado=alumno;
        initComponents();
       
        matriculaDao = new MatriculaDAO();
        alumnoDao = new AlumnoDAO();
        cursoDao = new CursoDAO();
        cargarDatosIniciales();
         configurarBusquedaAlumno();
    }
    
    private void cargarDatosIniciales(){
        txtNroMatricula.setText(""+ matriculaDao.correlativoMatricula());
        cargarAlumnos();
        cargarCursos();
    }
    private void configurarBusquedaAlumno() {
    // Agregar listener para buscar mientras escribe
    txtBuscarAlumno.addKeyListener(new java.awt.event.KeyAdapter() {
        @Override
        public void keyReleased(java.awt.event.KeyEvent evt) {
            buscarAlumnoPorNombre(txtBuscarAlumno.getText());
        }
    });
}
    private void cargarAlumnos(){
        DefaultTableModel df = (DefaultTableModel)tablaAlumnos.getModel();
        df.setRowCount(0);
        
       // Si hay alumno logueado, mostrar solo ese alumno
        if (alumnoLogueado != null) {
            df.addRow(new Object[]{
                alumnoLogueado.getCodAlumno(),
                alumnoLogueado.getDni(),
                alumnoLogueado.getNombres(),
                alumnoLogueado.getApellidos()
            });
            listAlumnos.clear();
            listAlumnos.add(alumnoLogueado);
        } else {
            // Si no hay alumno logueado, mostrar todos (para admin)
            listAlumnos = alumnoDao.listarAlumnos();
            
            for(Alumno obj : listAlumnos){
                df.addRow(new Object[]{
                    obj.getCodAlumno(),
                    obj.getDni(),
                    obj.getNombres(),
                    obj.getApellidos()
            });
            }
        }
    }
   private void buscarAlumnoPorNombre(String nombre) {
    DefaultTableModel df = (DefaultTableModel)tablaAlumnos.getModel();
    df.setRowCount(0);
    listAlumnos.clear();
    
    if (nombre.trim().isEmpty()) {
        // Si el campo está vacío, cargar todos los alumnos
        cargarAlumnos();
        return;
    }
    
    // Buscar alumnos que contengan ese nombre (sin importar mayúsculas/minúsculas)
    List<Alumno> todosLosAlumnos = alumnoDao.listarAlumnos();
    
    for (Alumno alumno : todosLosAlumnos) {
        String nombreCompleto = (alumno.getNombres() + " " + alumno.getApellidos()).toLowerCase();
        if (nombreCompleto.contains(nombre.toLowerCase())) {
            // Si hay alumno logueado, solo mostrar ese alumno
            if (alumnoLogueado != null) {
                if (alumno.getCodAlumno() == alumnoLogueado.getCodAlumno()) {
                    df.addRow(new Object[]{
                        alumno.getDni(),
                        alumno.getNombres(),
                        alumno.getApellidos()
                    });
                    listAlumnos.add(alumno);
                }
            } else {
                // Si no hay alumno logueado (admin), mostrar todos los coincidentes
                df.addRow(new Object[]{
                    alumno.getDni(),
                    alumno.getNombres(),
                    alumno.getApellidos()
                });
                listAlumnos.add(alumno);
            }
        }
    }
    
    // Si no encuentra nada, simplemente deja la tabla vacía (sin mensaje)
}
      private void cargarCursos(){
        DefaultTableModel df = (DefaultTableModel)tablaCursos.getModel();
        df.setRowCount(0);
        
        listCursos = cursoDao.listarCursos();
        
        for(Curso obj : listCursos){
            df.addRow(new Object[]{
                obj.getCodCurso(),
                obj.getAsignatura(),
                obj.getCiclo(),
                obj.getCreditos(),
                obj.getHoras()
            });
            
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelPrincipal = new javax.swing.JPanel();
        lblCodigo = new javax.swing.JLabel();
        lblCursoTitulo = new javax.swing.JLabel();
        btnProcesarMatricula = new javax.swing.JButton();
        txtNroMatricula = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaAlumnos = new javax.swing.JTable();
        lblCursoTitulo1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaCursos = new javax.swing.JTable();
        lblCursoTitulo2 = new javax.swing.JLabel();
        txtBuscarAlumno = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();

        panelPrincipal.setBackground(new java.awt.Color(255, 255, 255));

        lblCodigo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblCodigo.setText("Nro Matricula:");

        lblCursoTitulo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblCursoTitulo.setForeground(new java.awt.Color(41, 128, 185));
        lblCursoTitulo.setText("LISTADO DE ALUMNOS");

        btnProcesarMatricula.setBackground(new java.awt.Color(243, 156, 18));
        btnProcesarMatricula.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnProcesarMatricula.setForeground(new java.awt.Color(255, 255, 255));
        btnProcesarMatricula.setText("Procesar Matricula");
        btnProcesarMatricula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcesarMatriculaActionPerformed(evt);
            }
        });

        txtNroMatricula.setText("txtNroMatricula");

        tablaAlumnos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "DNI", "NOMBRES", "APELLIDOS"
            }
        ));
        jScrollPane1.setViewportView(tablaAlumnos);

        lblCursoTitulo1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblCursoTitulo1.setForeground(new java.awt.Color(41, 128, 185));
        lblCursoTitulo1.setText("LISTADO DE CURSOS");

        tablaCursos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CODIGO", "ASIGNATURA", "CICLO", "CREDITOS", "HORAS"
            }
        ));
        jScrollPane2.setViewportView(tablaCursos);

        lblCursoTitulo2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblCursoTitulo2.setForeground(new java.awt.Color(41, 128, 185));
        lblCursoTitulo2.setText("BUSCAR ALUMNO:");

        txtBuscarAlumno.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        txtBuscarAlumno.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtBuscarAlumno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarAlumnoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelPrincipalLayout = new javax.swing.GroupLayout(panelPrincipal);
        panelPrincipal.setLayout(panelPrincipalLayout);
        panelPrincipalLayout.setHorizontalGroup(
            panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPrincipalLayout.createSequentialGroup()
                .addGroup(panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelPrincipalLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(lblCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtNroMatricula)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelPrincipalLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCursoTitulo1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(panelPrincipalLayout.createSequentialGroup()
                                .addGroup(panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelPrincipalLayout.createSequentialGroup()
                                        .addComponent(lblCursoTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(244, 244, 244)
                                        .addComponent(lblCursoTitulo2, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtBuscarAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(btnProcesarMatricula, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 915, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 915, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 36, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        panelPrincipalLayout.setVerticalGroup(
            panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCodigo, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                    .addComponent(txtNroMatricula))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblCursoTitulo2, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                        .addComponent(txtBuscarAlumno))
                    .addComponent(lblCursoTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(lblCursoTitulo1, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(btnProcesarMatricula, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addGap(55, 55, 55))
        );

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));
        jPanel1.setForeground(new java.awt.Color(153, 204, 0));

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("REGISTRO DE MATRICULA");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(264, 264, 264)
                .addComponent(lblTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
                .addGap(293, 293, 293))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(lblTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelPrincipal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnProcesarMatriculaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcesarMatriculaActionPerformed
                                                   
                                                       
    if (tablaAlumnos.getSelectedRow() == -1) {
        JOptionPane.showMessageDialog(null, "Seleccione un alumno a matricular");
        return;
    }
    if (tablaCursos.getSelectedRow() == -1) {
        JOptionPane.showMessageDialog(null, "Seleccione un curso a matricular");
        return;
    }
    
    try {
        Alumno alumnoSeleccionado = listAlumnos.get(tablaAlumnos.getSelectedRow());
        
        // Si hay alumno logueado, validar que solo se matricule a sí mismo
        if (alumnoLogueado != null && alumnoSeleccionado.getCodAlumno() != alumnoLogueado.getCodAlumno()) {
            JOptionPane.showMessageDialog(this,
                "Solo puedes matricularte a ti mismo",
                "Acción no permitida",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Curso curso = listCursos.get(tablaCursos.getSelectedRow());
        
        // Verificar si ya está matriculado en ese curso
        if (matriculaDao.yaEstaMatriculado(alumnoSeleccionado.getCodAlumno(), curso.getCodCurso())) {
            JOptionPane.showMessageDialog(null, 
                "El alumno ya está matriculado en este curso",
                "Matrícula duplicada",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Matricula matricula = new Matricula();
        matricula.setCodAlumno(alumnoSeleccionado.getCodAlumno());
        matricula.setCodCurso(curso.getCodCurso());
        
        int resultado = matriculaDao.insertarMatricula(matricula);
        
        if (resultado > 0) {
            // Actualizar estado del alumno a "Matriculado" (estado = 1)
            alumnoSeleccionado.setEstado(1);
            alumnoDao.actualizarAlumno(alumnoSeleccionado);
            
            txtNroMatricula.setText("" + matriculaDao.correlativoMatricula());
            cargarAlumnos();
            
            JOptionPane.showMessageDialog(null, 
                "Matrícula procesada correctamente\n" +
                "Alumno: " + alumnoSeleccionado.getNombres() + "\n" +
                "Curso: " + curso.getAsignatura());
            
            // Limpiar selecciones
            tablaAlumnos.clearSelection();
            tablaCursos.clearSelection();
        } else {
            JOptionPane.showMessageDialog(null, "No se procesó la matrícula. Error en la base de datos.");
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, "Error al procesar matrícula: " + ex.getMessage());
        ex.printStackTrace();
    }

    }//GEN-LAST:event_btnProcesarMatriculaActionPerformed

    private void txtBuscarAlumnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarAlumnoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarAlumnoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnProcesarMatricula;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblCodigo;
    private javax.swing.JLabel lblCursoTitulo;
    private javax.swing.JLabel lblCursoTitulo1;
    private javax.swing.JLabel lblCursoTitulo2;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel panelPrincipal;
    private javax.swing.JTable tablaAlumnos;
    private javax.swing.JTable tablaCursos;
    private javax.swing.JTextField txtBuscarAlumno;
    private javax.swing.JLabel txtNroMatricula;
    // End of variables declaration//GEN-END:variables
}
