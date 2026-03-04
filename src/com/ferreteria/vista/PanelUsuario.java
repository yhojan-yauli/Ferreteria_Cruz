package com.ferreteria.vista;

import com.ferreteria.dao.UsuarioDAOImpl;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class PanelUsuario extends JPanel {

    // --- Variables ---
// --- Instancias ---
private UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl(); // Agrega esto
private int idUsuarioSeleccionado = -1;
    // --- Componentes Visuales del Formulario ---
    private JTextField txtId; 
    private JTextField txtNombreCompleto;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbRol;
    private JComboBox<String> cmbEstado;

    // --- Botones ---
    private JButton btnGuardar;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    // --- Tabla ---
    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;

    public PanelUsuario() {
        initComponents();
        // cargarTabla(); // Lo descomentarás cuando tengas el DAO
        cargarTabla(); // ¡Quita el comentario aquí!
    }

    private void initComponents() {
        this.setLayout(new BorderLayout(15, 15));
        this.setBackground(new Color(245, 245, 245));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- 1. TÍTULO ---
        JLabel lblTitulo = new JLabel("GESTIÓN DE USUARIOS DEL SISTEMA");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(40, 40, 40));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(lblTitulo, BorderLayout.NORTH);

        // --- 2. FORMULARIO LATERAL (Izquierda) ---
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), 
                "Datos del Usuario", TitledBorder.LEFT, TitledBorder.TOP, 
                new Font("Segoe UI", Font.BOLD, 12), new Color(100, 100, 100)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        txtId = new JTextField("0");
        txtId.setVisible(false);

        // Nombre Completo
        gbc.gridx = 0; gbc.gridy = 0; panelFormulario.add(new JLabel("Nombre Completo:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; 
        txtNombreCompleto = new JTextField(15);
        panelFormulario.add(txtNombreCompleto, gbc);

        // Username
        gbc.gridx = 0; gbc.gridy = 1; panelFormulario.add(new JLabel("Usuario (Login):"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; 
        txtUsername = new JTextField(15);
        panelFormulario.add(txtUsername, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 2; panelFormulario.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; 
        txtPassword = new JPasswordField(15);
        panelFormulario.add(txtPassword, gbc);

        // ID Rol
        gbc.gridx = 0; gbc.gridy = 3; panelFormulario.add(new JLabel("Rol:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; 
        cmbRol = new JComboBox<>(new String[]{"1 - Administrador", "2 - Vendedor"});
        panelFormulario.add(cmbRol, gbc);

        // Activo
        gbc.gridx = 0; gbc.gridy = 4; panelFormulario.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; 
        cmbEstado = new JComboBox<>(new String[]{"1 - Activo", "0 - Inactivo"});
        panelFormulario.add(cmbEstado, gbc);

        // --- Botones ---
        JPanel panelBotones = new JPanel(new GridLayout(2, 2, 10, 10));
        panelBotones.setOpaque(false);
        
        btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(39, 174, 96));
        btnGuardar.setForeground(Color.WHITE);
        
        btnLimpiar = new JButton("Limpiar");
        
        btnEditar = new JButton("Editar");
        btnEditar.setEnabled(false); 
        
        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(231, 76, 60));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setEnabled(false);
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        panelFormulario.add(panelBotones, gbc);

        this.add(panelFormulario, BorderLayout.WEST);

        // --- 3. TABLA CENTRAL ---
        String[] columnas = {"ID", "Nombre Completo", "Usuario", "Rol ID", "Estado"};
        modeloTabla = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setRowHeight(25);
        tablaUsuarios.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JScrollPane scrollTabla = new JScrollPane(tablaUsuarios);
        this.add(scrollTabla, BorderLayout.CENTER);
btnGuardar.addActionListener(e -> {
    // 1. Validación de campos
    String password = new String(txtPassword.getPassword());
    if (txtNombreCompleto.getText().isEmpty() || txtUsername.getText().isEmpty() || password.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Nombre, Usuario y Contraseña son obligatorios.");
        return;
    }

    // 2. Creamos el objeto Usuario con los datos del formulario
    com.ferreteria.modelo.Usuario nuevo = new com.ferreteria.modelo.Usuario();
    nuevo.setNombreCompleto(txtNombreCompleto.getText().trim());
    nuevo.setUsername(txtUsername.getText().trim());
    nuevo.setPassword(password); // En un proyecto real, aquí deberías encriptarla
    
    // Obtenemos el ID del Rol: 1 (Admin) o 2 (Vendedor)
    nuevo.setIdRol(cmbRol.getSelectedIndex() + 1); 
    
    // Obtenemos el Estado: 1 (Activo) o 0 (Inactivo)
    nuevo.setActivo(cmbEstado.getSelectedIndex() == 0 ? 1 : 0);

    // 3. Enviamos a la Base de Datos a través del DAO
    if (usuarioDAO.registrar(nuevo)) {
        JOptionPane.showMessageDialog(this, "¡Usuario guardado exitosamente!");
        
        // 4. PASO CRÍTICO: Refrescar la tabla para ver el nuevo usuario
        cargarTabla(); 
        
        // 5. Limpiar para el siguiente registro
        limpiarFormulario();
    } else {
        JOptionPane.showMessageDialog(this, "Error al guardar. Verifique que el username no esté duplicado.", "Error", JOptionPane.ERROR_MESSAGE);
    }
});
        // --- 4. EVENTOS ---
        agregarEventos();
    }

    private void limpiarFormulario() {
        txtId.setText("0");
        txtNombreCompleto.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        cmbRol.setSelectedIndex(0);
        cmbEstado.setSelectedIndex(0);
        
        idUsuarioSeleccionado = -1;
        btnGuardar.setEnabled(true);
        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);
        tablaUsuarios.clearSelection();
    }
private void cargarTabla() {
    modeloTabla.setRowCount(0); // Limpia la tabla visual antes de cargar
    
    // Llamamos al método que creamos en el UsuarioDAOImpl
    java.util.List<com.ferreteria.modelo.Usuario> lista = usuarioDAO.listarTodos();
    
    for (com.ferreteria.modelo.Usuario u : lista) {
        Object[] fila = {
            u.getIdUsuario(),
            u.getNombreCompleto(),
            u.getUsername(),
            u.getIdRol(),
            (u.getActivo() == 1 ? "Activo" : "Inactivo")
        };
        modeloTabla.addRow(fila); // Agrega la fila a la tabla
    }
}
    private void agregarEventos() {
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        // Clic en la tabla
        tablaUsuarios.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int fila = tablaUsuarios.getSelectedRow();
                if (fila >= 0) {
                    idUsuarioSeleccionado = Integer.parseInt(tablaUsuarios.getValueAt(fila, 0).toString());
                    txtNombreCompleto.setText(tablaUsuarios.getValueAt(fila, 1).toString());
                    txtUsername.setText(tablaUsuarios.getValueAt(fila, 2).toString());
                    
                    // Rol
                    String rolId = tablaUsuarios.getValueAt(fila, 3).toString();
                    if (rolId.equals("1")) cmbRol.setSelectedIndex(0); // Admin
                    else cmbRol.setSelectedIndex(1); // Vendedor
                    
                    // Estado (Activo)
                    String estado = tablaUsuarios.getValueAt(fila, 4).toString();
                    if (estado.equals("1") || estado.equalsIgnoreCase("Activo")) cmbEstado.setSelectedIndex(0);
                    else cmbEstado.setSelectedIndex(1);
                    
                    txtPassword.setText(""); // Por seguridad
                    
                    btnGuardar.setEnabled(false);
                    btnEditar.setEnabled(true);
                    btnEliminar.setEnabled(true);
                }
            }
        });

        // Botón Guardar
        btnGuardar.addActionListener(e -> {
            if (txtNombreCompleto.getText().isEmpty() || txtUsername.getText().isEmpty() || txtPassword.getPassword().length == 0) {
                JOptionPane.showMessageDialog(this, "Nombre, Usuario y Contraseña son obligatorios.");
                return;
            }
            
            // Lógica pendiente de conectar con DAO
            JOptionPane.showMessageDialog(this, "Datos listos para enviar a la BD:\nNombre: " + txtNombreCompleto.getText() + "\nRol ID: " + (cmbRol.getSelectedIndex() + 1));
        });
    }
}