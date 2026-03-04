package com.ferreteria.vista;

import com.ferreteria.dao.ProductoDAO;
import com.ferreteria.dao.ProductoDAOImpl;
import com.ferreteria.modelo.Producto;
import com.ferreteria.util.ComboBoxItem;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class PanelInventario1 extends JPanel {

    // --- 1. INSTANCIAS Y VARIABLES GLOBALES ---
    private ProductoDAO productoDAO;
    private int idProductoSeleccionado = -1;

    // --- Componentes Visuales ---
    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtMarca;
    private JTextField txtStock;
    private JSpinner spnStockMinimo;
    private JTextField txtPrecioCompra;
    private JTextField txtPrecioVenta;
    private JComboBox<ComboBoxItem> cmbCategoria;
    private JTextField txtUbicacion;
    private JTextField txtBuscarInventario;

    private JButton btnGuardar;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnBuscar;

    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;

    public PanelInventario1() {
        productoDAO = new ProductoDAOImpl();
        initComponents();
        cargarCategorias();
        configurarTabla();
        cargarTabla("");
    }

    private void initComponents() {
        this.setLayout(new BorderLayout(15, 15));
        this.setBackground(new Color(245, 245, 245));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- TÍTULO ---
        JLabel lblTitulo = new JLabel("INVENTARIO DE PRODUCTOS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(40, 40, 40));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(lblTitulo, BorderLayout.NORTH);

        // --- PANEL CENTRAL DIVIDIDO (Formulario Arriba, Tabla Abajo) ---
        JPanel panelCentral = new JPanel(new BorderLayout(0, 15));
        panelCentral.setOpaque(false);

        // --- 1. FORMULARIO SUPERIOR ---
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Datos del Producto", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12), new Color(100, 100, 100)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Fila 1
        gbc.gridx = 0; gbc.gridy = 0; panelFormulario.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        txtCodigo = new JTextField(15);
        panelFormulario.add(txtCodigo, gbc);

        gbc.gridx = 2; gbc.gridy = 0; panelFormulario.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0;
        txtNombre = new JTextField(20);
        panelFormulario.add(txtNombre, gbc);

        gbc.gridx = 4; gbc.gridy = 0; panelFormulario.add(new JLabel("Marca:"), gbc);
        gbc.gridx = 5; gbc.gridy = 0;
        txtMarca = new JTextField(15);
        panelFormulario.add(txtMarca, gbc);

        // Fila 2
        gbc.gridx = 0; gbc.gridy = 1; panelFormulario.add(new JLabel("Stock Inicial:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        txtStock = new JTextField("0", 15);
        panelFormulario.add(txtStock, gbc);

        gbc.gridx = 2; gbc.gridy = 1; panelFormulario.add(new JLabel("Stock Mínimo:"), gbc);
        gbc.gridx = 3; gbc.gridy = 1;
        spnStockMinimo = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
        panelFormulario.add(spnStockMinimo, gbc);

        gbc.gridx = 4; gbc.gridy = 1; panelFormulario.add(new JLabel("Categoría:"), gbc);
        gbc.gridx = 5; gbc.gridy = 1;
        cmbCategoria = new JComboBox<>();
        panelFormulario.add(cmbCategoria, gbc);

        // Fila 3
        gbc.gridx = 0; gbc.gridy = 2; panelFormulario.add(new JLabel("P. Compra (S/):"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        txtPrecioCompra = new JTextField(15);
        panelFormulario.add(txtPrecioCompra, gbc);

        gbc.gridx = 2; gbc.gridy = 2; panelFormulario.add(new JLabel("P. Venta (S/):"), gbc);
        gbc.gridx = 3; gbc.gridy = 2;
        txtPrecioVenta = new JTextField(15);
        panelFormulario.add(txtPrecioVenta, gbc);

        gbc.gridx = 4; gbc.gridy = 2; panelFormulario.add(new JLabel("Ubicación:"), gbc);
        gbc.gridx = 5; gbc.gridy = 2;
        txtUbicacion = new JTextField(15);
        panelFormulario.add(txtUbicacion, gbc);

        // Botones de Acción (Debajo del formulario)
        JPanel panelBotonesAccion = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelBotonesAccion.setOpaque(false);
        
        btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(39, 174, 96)); // Verde
        btnGuardar.setForeground(Color.WHITE);
        
        btnEditar = new JButton("Editar");
        btnEditar.setEnabled(false);
        
        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(231, 76, 60)); // Rojo
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setEnabled(false);
        
        btnLimpiar = new JButton("Limpiar Todo");

        panelBotonesAccion.add(btnGuardar);
        panelBotonesAccion.add(btnEditar);
        panelBotonesAccion.add(btnEliminar);
        panelBotonesAccion.add(btnLimpiar);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 6;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 8, 8, 8);
        panelFormulario.add(panelBotonesAccion, gbc);

        panelCentral.add(panelFormulario, BorderLayout.NORTH);

        // --- 2. ÁREA DE BÚSQUEDA Y TABLA ---
        JPanel panelInferior = new JPanel(new BorderLayout(0, 10));
        panelInferior.setOpaque(false);

        // Buscador
        JPanel panelBuscador = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBuscador.setOpaque(false);
        panelBuscador.add(new JLabel("Buscar Producto: "));
        txtBuscarInventario = new JTextField(25);
        btnBuscar = new JButton("Buscar");
        panelBuscador.add(txtBuscarInventario);
        panelBuscador.add(btnBuscar);
        
        panelInferior.add(panelBuscador, BorderLayout.NORTH);

        // Tabla
        String[] titulos = {"ID", "Código", "Nombre", "Marca", "Stock", "St. Mín.", "P. Venta", "P. Compra", "Cat. ID", "Ubicación"};
        modeloTabla = new DefaultTableModel(null, titulos) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setRowHeight(25);
        
        JScrollPane scrollTabla = new JScrollPane(tablaProductos);
        panelInferior.add(scrollTabla, BorderLayout.CENTER);

        panelCentral.add(panelInferior, BorderLayout.CENTER);

        this.add(panelCentral, BorderLayout.CENTER);

        // --- ASIGNAR EVENTOS ---
        agregarEventos();
    }

    private void configurarTabla() {
        int[] anchos = {30, 70, 180, 100, 50, 50, 70, 70, 50, 80};
        for (int i = 0; i < tablaProductos.getColumnCount(); i++) {
            if (i < anchos.length) {
                tablaProductos.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
            }
        }
        try {
            tablaProductos.getColumnModel().getColumn(4).setCellRenderer(new StockAlertaRenderer());
        } catch (Exception e) {
            System.err.println("Error renderer: " + e.getMessage());
        }
    }

    private void cargarTabla(String criterio) {
        modeloTabla.setRowCount(0);
        List<Producto> lista;

        if (criterio == null || criterio.trim().isEmpty()) {
            lista = productoDAO.listarTodos(true);
        } else {
            lista = productoDAO.buscarPorCriterio(criterio);
        }

        for (Producto p : lista) {
            Object[] fila = {
                p.getIdProducto(), p.getCodigo(), p.getNombre(), p.getMarca(),
                p.getStockActual(), p.getStockMinimo(),
                String.format("%.2f", p.getPrecioVenta()),
                String.format("%.2f", p.getPrecioCompra()),
                p.getIdCategoria(), p.getUbicacion()
            };
            modeloTabla.addRow(fila);
        }
    }

    private void cargarCategorias() {
        List<ComboBoxItem> lista = productoDAO.listarCategoriasCombo();
        cmbCategoria.removeAllItems();
        cmbCategoria.addItem(new ComboBoxItem(0, "--- Seleccione ---"));
        for (ComboBoxItem item : lista) {
            cmbCategoria.addItem(item);
        }
    }

    private void limpiarFormulario() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtMarca.setText("");
        txtPrecioCompra.setText("");
        txtPrecioVenta.setText("");
        txtStock.setText("0");
        txtUbicacion.setText("");
        spnStockMinimo.setValue(0);

        if (cmbCategoria.getItemCount() > 0) {
            cmbCategoria.setSelectedIndex(0);
        }

        idProductoSeleccionado = -1;
        btnGuardar.setEnabled(true);
        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);
        txtBuscarInventario.setText("");
        cargarTabla("");
        tablaProductos.clearSelection();
    }

    private void agregarEventos() {
        // Clic en la Tabla para seleccionar un producto
        tablaProductos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int fila = tablaProductos.getSelectedRow();
                if (fila >= 0) {
                    try {
                        idProductoSeleccionado = Integer.parseInt(tablaProductos.getValueAt(fila, 0).toString());
                        txtCodigo.setText(tablaProductos.getValueAt(fila, 1).toString());
                        txtNombre.setText(tablaProductos.getValueAt(fila, 2).toString());
                        txtMarca.setText(tablaProductos.getValueAt(fila, 3).toString());
                        txtStock.setText(tablaProductos.getValueAt(fila, 4).toString());
                        spnStockMinimo.setValue(Integer.parseInt(tablaProductos.getValueAt(fila, 5).toString()));
                        txtPrecioVenta.setText(tablaProductos.getValueAt(fila, 6).toString().replace(",", "."));
                        txtPrecioCompra.setText(tablaProductos.getValueAt(fila, 7).toString().replace(",", "."));
                        
                        // Seleccionar categoría correcta
                        int idCat = Integer.parseInt(tablaProductos.getValueAt(fila, 8).toString());
                        for (int i = 0; i < cmbCategoria.getItemCount(); i++) {
                            ComboBoxItem item = (ComboBoxItem) cmbCategoria.getItemAt(i);
                            if (item.getId() == idCat) {
                                cmbCategoria.setSelectedIndex(i);
                                break;
                            }
                        }

                        if (tablaProductos.getValueAt(fila, 9) != null) {
                            txtUbicacion.setText(tablaProductos.getValueAt(fila, 9).toString());
                        } else {
                            txtUbicacion.setText("");
                        }

                        btnGuardar.setEnabled(false);
                        btnEditar.setEnabled(true);
                        btnEliminar.setEnabled(true);
                    } catch (Exception e) {
                        System.err.println("Error al seleccionar producto: " + e.getMessage());
                    }
                }
            }
        });

        btnLimpiar.addActionListener(e -> limpiarFormulario());

        btnBuscar.addActionListener(e -> cargarTabla(txtBuscarInventario.getText().trim()));
        
        txtBuscarInventario.addActionListener(e -> cargarTabla(txtBuscarInventario.getText().trim())); // Buscar al dar Enter

        btnGuardar.addActionListener(e -> {
            if (txtCodigo.getText().isEmpty() || txtNombre.getText().isEmpty() || txtPrecioVenta.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor complete Código, Nombre y Precio Venta.");
                return;
            }

            Producto p = new Producto();
            p.setCodigo(txtCodigo.getText());
            p.setNombre(txtNombre.getText());
            p.setMarca(txtMarca.getText());

            try {
                p.setPrecioVenta(Double.parseDouble(txtPrecioVenta.getText()));
                String pCompra = txtPrecioCompra.getText();
                p.setPrecioCompra(pCompra.isEmpty() ? 0.0 : Double.parseDouble(pCompra));
                p.setStockActual(Integer.parseInt(txtStock.getText()));
                p.setStockMinimo((Integer) spnStockMinimo.getValue());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Los precios y stock deben ser números válidos.");
                return;
            }

            ComboBoxItem itemCat = (ComboBoxItem) cmbCategoria.getSelectedItem();
            if (itemCat == null || itemCat.getId() == 0) {
                JOptionPane.showMessageDialog(this, "Seleccione una categoría válida.");
                return;
            }
            p.setIdCategoria(itemCat.getId());
            p.setUbicacion(txtUbicacion.getText());

            if (productoDAO.registrar(p)) {
                JOptionPane.showMessageDialog(this, "Producto registrado correctamente.");
                cargarTabla("");
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar. Verifique que el código no esté repetido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnEditar.addActionListener(e -> {
            if (idProductoSeleccionado == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un producto.");
                return;
            }

            Producto p = new Producto();
            p.setIdProducto(idProductoSeleccionado);
            p.setCodigo(txtCodigo.getText());
            p.setNombre(txtNombre.getText());
            p.setMarca(txtMarca.getText());

            try {
                p.setPrecioVenta(Double.parseDouble(txtPrecioVenta.getText()));
                p.setPrecioCompra(Double.parseDouble(txtPrecioCompra.getText()));
                p.setStockMinimo((Integer) spnStockMinimo.getValue());
                p.setStockActual(Integer.parseInt(txtStock.getText()));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Revise los campos numéricos.");
                return;
            }

            ComboBoxItem itemCat = (ComboBoxItem) cmbCategoria.getSelectedItem();
            p.setIdCategoria(itemCat.getId());
            p.setUbicacion(txtUbicacion.getText());

            if (productoDAO.modificar(p)) {
                JOptionPane.showMessageDialog(this, "Producto modificado.");
                cargarTabla(txtBuscarInventario.getText().trim());
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "Error al modificar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnEliminar.addActionListener(e -> {
            if (idProductoSeleccionado == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un producto para eliminar.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que desea eliminar este producto?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (productoDAO.eliminarLogico(idProductoSeleccionado)) {
                    JOptionPane.showMessageDialog(this, "Producto eliminado.");
                    cargarTabla("");
                    limpiarFormulario();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // --- CLASE RENDERIZADORA PARA PINTAR CELDAS ---
    public class StockAlertaRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            try {
                int stockActual = Integer.parseInt(table.getValueAt(row, 4).toString());
                int stockMinimo = Integer.parseInt(table.getValueAt(row, 5).toString());

                if (stockActual <= stockMinimo) {
                    c.setBackground(new Color(255, 102, 102)); // Rojo claro
                    c.setForeground(Color.WHITE);
                } else {
                    if (!isSelected) {
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);
                    } else {
                        c.setBackground(table.getSelectionBackground());
                        c.setForeground(table.getSelectionForeground());
                    }
                }
            } catch (Exception e) {
                // Si falla, ignorar el coloreado
            }
            return c;
        }
    }
}