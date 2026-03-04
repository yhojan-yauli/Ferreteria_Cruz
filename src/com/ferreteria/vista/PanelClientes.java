package com.ferreteria.vista;

// --- IMPORTACIONES DE DISEÑO (ESTO FALTA) ---
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
// --------------------------------------------

import com.ferreteria.dao.ClienteDAOImpl;
import com.ferreteria.modelo.Cliente;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
public class PanelClientes extends JPanel {

    // --- Variables de Control ---
private ClienteDAOImpl clienteDAO = new ClienteDAOImpl(); // <--- AGREGA ESTA LÍNEA
    private int idClienteSeleccionado = -1;
    // --- Componentes del Formulario ---
    private JTextField txtDniRuc;
    private JTextField txtNombre;
    private JTextField txtTelefono;
    private JTextField txtDireccion;
    private JComboBox<String> cmbTipo; // Natural o Jurídico

    // --- Botones ---
    private JButton btnGuardar, btnEditar, btnEliminar, btnLimpiar;

    // --- Tabla ---
    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;

    public PanelClientes() {
        initComponents();
        cargarTabla(); // Conectar con ClienteDAO después
    }

    private void initComponents() {
        this.setLayout(new BorderLayout(15, 15));
        this.setBackground(new Color(245, 245, 245));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- TÍTULO ---
        JLabel lblTitulo = new JLabel("CARTERA DE CLIENTES");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(lblTitulo, BorderLayout.NORTH);

        // --- PANEL IZQUIERDO: FORMULARIO ---
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Registro de Cliente", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12), new Color(100, 100, 100)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // DNI o RUC
        gbc.gridx = 0; gbc.gridy = 0; panelForm.add(new JLabel("DNI / RUC:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; 
        txtDniRuc = new JTextField(15);
        panelForm.add(txtDniRuc, gbc);

        // Nombre o Razón Social
        gbc.gridx = 0; gbc.gridy = 1; panelForm.add(new JLabel("Nombre / R. Social:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; 
        txtNombre = new JTextField(15);
        panelForm.add(txtNombre, gbc);

        // Teléfono
        gbc.gridx = 0; gbc.gridy = 2; panelForm.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; 
        txtTelefono = new JTextField(15);
        panelForm.add(txtTelefono, gbc);

        // Dirección
        gbc.gridx = 0; gbc.gridy = 3; panelForm.add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; 
        txtDireccion = new JTextField(15);
        panelForm.add(txtDireccion, gbc);

        // Tipo de Cliente
        gbc.gridx = 0; gbc.gridy = 4; panelForm.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; 
        cmbTipo = new JComboBox<>(new String[]{"Persona Natural", "Empresa (Jurídica)"});
        panelForm.add(cmbTipo, gbc);

        // Panel de Botones
        JPanel panelBtns = new JPanel(new GridLayout(2, 2, 10, 10));
        panelBtns.setOpaque(false);
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

        panelBtns.add(btnGuardar);
        panelBtns.add(btnLimpiar);
        panelBtns.add(btnEditar);
        panelBtns.add(btnEliminar);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        panelForm.add(panelBtns, gbc);

        this.add(panelForm, BorderLayout.WEST);

        // --- PANEL DERECHO: TABLA ---
        String[] columnas = {"ID", "DNI/RUC", "Nombre", "Teléfono", "Tipo"};
        modeloTabla = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaClientes = new JTable(modeloTabla);
        tablaClientes.setRowHeight(25);
        
        JScrollPane scroll = new JScrollPane(tablaClientes);
        this.add(scroll, BorderLayout.CENTER);

        configurarEventos();
    }

   private void configurarEventos() {
        btnLimpiar.addActionListener(e -> limpiar());

        // --- BOTÓN GUARDAR ---
        btnGuardar.addActionListener(e -> {
    // 1. Validación: DNI y Nombre son obligatorios en tu BD
    if (txtDniRuc.getText().trim().isEmpty() || txtNombre.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, complete el DNI/RUC y el Nombre.", "Validación", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // 2. Creamos el objeto Cliente con los datos de los campos
    com.ferreteria.modelo.Cliente nuevoCliente = new com.ferreteria.modelo.Cliente();
    nuevoCliente.setDniRuc(txtDniRuc.getText().trim());
    nuevoCliente.setNombres(txtNombre.getText().trim());
    // Si tu formulario no tiene campo de apellidos separado, puedes mandarlo vacío o ajustar el TXT
    nuevoCliente.setApellidos(""); 
    nuevoCliente.setCelular(txtTelefono.getText().trim());
    nuevoCliente.setDireccion(txtDireccion.getText().trim());
    nuevoCliente.setEmail(""); // O el campo que correspondas

    // 3. Intentamos guardar en la Base de Datos
    if (clienteDAO.registrar(nuevoCliente)) {
        JOptionPane.showMessageDialog(this, "¡Cliente guardado con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        
        // 4. IMPORTANTE: Refrescar la tabla para ver el nuevo cliente
        cargarTabla(); 
        
        // 5. Limpiar los cuadros de texto
        limpiar(); 
    } else {
        JOptionPane.showMessageDialog(this, "Error al guardar. Posiblemente el DNI ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
    }
});

        // --- SELECCIÓN DE TABLA ---
        tablaClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tablaClientes.getSelectedRow();
                if (fila >= 0) {
                    idClienteSeleccionado = Integer.parseInt(tablaClientes.getValueAt(fila, 0).toString());
                    txtDniRuc.setText(tablaClientes.getValueAt(fila, 1).toString());
                    txtNombre.setText(tablaClientes.getValueAt(fila, 2).toString());
                    txtTelefono.setText(tablaClientes.getValueAt(fila, 3).toString());
                    cmbTipo.setSelectedItem(tablaClientes.getValueAt(fila, 4).toString());
                    
                    btnGuardar.setEnabled(false);
                    btnEditar.setEnabled(true);
                    btnEliminar.setEnabled(true);
                }
            }
        });
    }
private void cargarTabla() {
    modeloTabla.setRowCount(0); 
    List<Cliente> lista = clienteDAO.listarTodos(); 
    
    for (Cliente c : lista) {
        Object[] fila = {
            c.getIdCliente(),
            c.getDniRuc(),
            c.getNombres() + " " + (c.getApellidos() != null ? c.getApellidos() : ""),
            c.getCelular(),
            // Aquí puedes decidir qué mostrar en la columna "Tipo"
            "Persona Natural" 
        };
        modeloTabla.addRow(fila);
    }
}
    private void limpiar() {
        txtDniRuc.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");
        cmbTipo.setSelectedIndex(0);
        idClienteSeleccionado = -1;
        btnGuardar.setEnabled(true);
        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);
        tablaClientes.clearSelection();
    }
}