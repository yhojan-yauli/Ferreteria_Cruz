package com.ferreteria.vista;

import com.ferreteria.dao.CategoriaDAOImpl;
import com.ferreteria.modelo.Categoria;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class PanelCategorias extends JPanel {

    // Componentes del Formulario
    private JTextField txtId;
    private JTextField txtNombre;
    private JTextArea txtDescripcion;
    private JComboBox<String> cmbEstado;
    
    // Botones
    private JButton btnGuardar;
    private JButton btnLimpiar;
    
    // Tabla
    private JTable tablaCategorias;
    private DefaultTableModel modeloTabla;
    
    // DAO
    private CategoriaDAOImpl categoriaDAO;

    public PanelCategorias() {
        categoriaDAO = new CategoriaDAOImpl();
        initComponents();
        cargarTabla();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout(15, 15));
        this.setBackground(new Color(245, 245, 245));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- 1. TÍTULO ---
        JLabel lblTitulo = new JLabel("GESTIÓN DE CATEGORÍAS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(40, 40, 40));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(lblTitulo, BorderLayout.NORTH);

        // --- 2. FORMULARIO LATERAL (Izquierda) ---
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), 
                "Datos de Categoría", TitledBorder.LEFT, TitledBorder.TOP, 
                new Font("Segoe UI", Font.BOLD, 12), new Color(100, 100, 100)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Fila 0: ID (Oculto o bloqueado, solo para saber si editamos)
        txtId = new JTextField("0");
        txtId.setVisible(false); // Lo mantenemos oculto al usuario

        // Fila 1: Nombre
        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        txtNombre = new JTextField(15);
        panelFormulario.add(txtNombre, gbc);

        // Fila 2: Descripción
        gbc.gridx = 0; gbc.gridy = 2;
        panelFormulario.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        txtDescripcion = new JTextArea(3, 15);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panelFormulario.add(new JScrollPane(txtDescripcion), gbc);

        // Fila 3: Estado
        gbc.gridx = 0; gbc.gridy = 3;
        panelFormulario.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        cmbEstado = new JComboBox<>(new String[]{"Activo", "Inactivo"});
        panelFormulario.add(cmbEstado, gbc);

        // Fila 4: Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelBotones.setOpaque(false);
        
        btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(0, 102, 102));
        btnGuardar.setForeground(Color.WHITE);
        
        btnLimpiar = new JButton("Limpiar");
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnLimpiar);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panelFormulario.add(panelBotones, gbc);

        this.add(panelFormulario, BorderLayout.WEST);

        // --- 3. TABLA CENTRAL (Derecha) ---
        String[] columnas = {"ID", "Nombre", "Descripción", "Estado"};
        modeloTabla = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        tablaCategorias = new JTable(modeloTabla);
        tablaCategorias.setRowHeight(25);
        JScrollPane scrollTabla = new JScrollPane(tablaCategorias);
        
        this.add(scrollTabla, BorderLayout.CENTER);

        // --- 4. EVENTOS ---
        agregarEventos();
    }

    // =========================================================
    // LÓGICA Y EVENTOS
    // =========================================================

   private void cargarTabla() {
        modeloTabla.setRowCount(0); // Limpiar la tabla
        List<Categoria> lista = categoriaDAO.listarTodas();
        
        // --- ¡AGREGA ESTA LÍNEA DE PRUEBA! ---
        JOptionPane.showMessageDialog(this, "Categorías encontradas en la BD: " + lista.size());
        
        for (Categoria cat : lista) {
            Object[] fila = {
                cat.getIdCategoria(),
                cat.getNombre(),
                cat.getDescripcion(),
                cat.isActivo() ? "Activo" : "Inactivo"
            };
            modeloTabla.addRow(fila);
        }
    }
    private void limpiarFormulario() {
        txtId.setText("0");
        txtNombre.setText("");
        txtDescripcion.setText("");
        cmbEstado.setSelectedIndex(0);
        tablaCategorias.clearSelection();
    }

    private void agregarEventos() {
        // Evento Botón Limpiar
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        // Evento Botón Guardar
        btnGuardar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String descripcion = txtDescripcion.getText().trim();
            
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre es obligatorio.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Categoria cat = new Categoria();
            cat.setNombre(nombre);
            cat.setDescripcion(descripcion);
            cat.setActivo(cmbEstado.getSelectedIndex() == 0); // 0 = Activo, 1 = Inactivo

            int id = Integer.parseInt(txtId.getText());
            
            if (id == 0) {
                // Es una categoría nueva
                if (categoriaDAO.insertar(cat)) {
                    JOptionPane.showMessageDialog(this, "Categoría registrada con éxito.");
                } else {
                    JOptionPane.showMessageDialog(this, "Error al registrar la categoría.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Aquí iría la lógica de modificar si quieres implementarla después
                // cat.setIdCategoria(id);
                // categoriaDAO.modificar(cat);
                JOptionPane.showMessageDialog(this, "Modificación en desarrollo...");
            }

            limpiarFormulario();
            cargarTabla();
        });
    }
}