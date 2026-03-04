package com.ferreteria.vista;

import com.ferreteria.dao.ProductoDAO;
import com.ferreteria.dao.ProductoDAOImpl;
import com.ferreteria.modelo.Producto;
import com.ferreteria.util.ComboBoxItem;
import java.awt.Color;
import java.awt.Component;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class PanelInventario extends javax.swing.JPanel {

    // --- 1. INSTANCIAS Y VARIABLES GLOBALES ---
    private ProductoDAO productoDAO = new ProductoDAOImpl();
    private int idProductoSeleccionado = -1;

    public PanelInventario() {
        initComponents(); // Carga el diseño
        cargarCategorias();
        configurarTabla(); 
        cargarTabla(""); 
    }

    private void configurarTabla() {
        String[] titulos = {"ID", "Código", "Nombre", "Marca", "Stock", "St. Mín.", "P. Venta", "P. Compra", "Cat. ID", "Ubicación"};
        DefaultTableModel modelo = new DefaultTableModel(null, titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaProductos.setModel(modelo);

        int[] anchos = {30, 70, 150, 80, 50, 50, 70, 70, 50, 80};
        for (int i = 0; i < tablaProductos.getColumnCount(); i++) {
            if (i < anchos.length) tablaProductos.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }

        try {
            tablaProductos.getColumnModel().getColumn(4).setCellRenderer(new StockAlertaRenderer());
        } catch (Exception e) {
            System.err.println("Error renderer: " + e.getMessage());
        }
    }

    private void cargarTabla(String criterio) {
        DefaultTableModel modelo = (DefaultTableModel) tablaProductos.getModel();
        modelo.setRowCount(0);
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
            modelo.addRow(fila);
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
        txtStock.setText("0"); // Reinicia a 0
        txtUbicacion.setText("");
        spnStockMinimo.setValue(0);
        cmbCategoria.setSelectedIndex(0);
        
        idProductoSeleccionado = -1;
        btnGuardar.setEnabled(true);
        btnEditar.setEnabled(false);
    }                              

   
    // ==========================================================
    // CLASE INTERNA PARA PINTAR CELDAS (ALERTA DE STOCK)
    // ==========================================================
    public class StockAlertaRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            try {
                // Stock Actual (col 4) vs Stock Mínimo (col 5)
                int stockActual = Integer.parseInt(table.getValueAt(row, 4).toString());
                int stockMinimo = Integer.parseInt(table.getValueAt(row, 5).toString());

                if (stockActual <= stockMinimo) {
                    c.setBackground(Color.RED);
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
                // Si falla, dejar por defecto
            }
            return c;
        }
    }

   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtStock = new javax.swing.JTextField();
        txtCodigo = new javax.swing.JTextField();
        txtMarca = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaRetirosCurso = new javax.swing.JTable();
        btnEditar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblNumero = new javax.swing.JLabel();
        lblNumero1 = new javax.swing.JLabel();
        lblNumero2 = new javax.swing.JLabel();
        lblNumero3 = new javax.swing.JLabel();
        lblNumero4 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        btnEliminar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        txtPrecioVenta = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        cmbCategoria = new javax.swing.JComboBox();
        spnStockMinimo = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        txtUbicacion = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtPrecioCompra = new javax.swing.JTextField();
        btnBuscarInventario = new javax.swing.JButton();
        txtBuscarInventario = new javax.swing.JTextField();
        scrollPane2 = new java.awt.ScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaProductos = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));

        txtStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtStockActionPerformed(evt);
            }
        });

        txtCodigo.setEditable(false);
        txtCodigo.setMinimumSize(new java.awt.Dimension(150, 50));
        txtCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoActionPerformed(evt);
            }
        });

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

        btnEditar.setBackground(new java.awt.Color(243, 156, 18));
        btnEditar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnEditar.setForeground(new java.awt.Color(255, 255, 255));
        btnEditar.setText("Editar");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        btnGuardar.setBackground(new java.awt.Color(243, 156, 18));
        btnGuardar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(0, 102, 102));
        jPanel2.setForeground(new java.awt.Color(153, 204, 0));

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("Inventario");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(264, 264, 264)
                .addComponent(lblTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, 889, Short.MAX_VALUE)
                .addGap(293, 293, 293))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(lblTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                .addGap(24, 24, 24))
        );

        lblNumero.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblNumero.setText("Codigo:");

        lblNumero1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblNumero1.setText("Precio Venta:");

        lblNumero2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblNumero2.setText("Marca:");

        lblNumero3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblNumero3.setText("Stock Inicial:");

        lblNumero4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblNumero4.setText("Nombre:");

        jLabel1.setText("Stock Mínimo");

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        txtPrecioVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrecioVentaActionPerformed(evt);
            }
        });

        jLabel2.setText("Categoría");

        cmbCategoria.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setText("Ubicación");

        jLabel4.setText("Precio Compra:");

        txtPrecioCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrecioCompraActionPerformed(evt);
            }
        });

        btnBuscarInventario.setText("🔍 BUSCAR");
        btnBuscarInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarInventarioActionPerformed(evt);
            }
        });

        txtBuscarInventario.setText(" ");
        txtBuscarInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarInventarioActionPerformed(evt);
            }
        });

        tablaProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tablaProductos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tablaProductosKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tablaProductos);

        scrollPane2.add(jScrollPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(90, 90, 90)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblNumero3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblNumero1, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                                    .addComponent(lblNumero2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblNumero4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblNumero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(41, 41, 41)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(txtNombre, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(txtMarca, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(txtStock)
                                                    .addComponent(txtPrecioVenta, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE))
                                                .addGroup(layout.createSequentialGroup()
                                                    .addGap(6, 6, 6)
                                                    .addComponent(txtPrecioCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addComponent(spnStockMinimo, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(txtBuscarInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(35, 35, 35)
                                        .addComponent(btnBuscarInventario))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel4))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addComponent(jLabel2)
                                        .addGap(231, 231, 231))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(219, 219, 219)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(cmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtUbicacion, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 622, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(99, 99, 99))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBuscarInventario)
                    .addComponent(txtBuscarInventario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNumero)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNumero4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNumero2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(txtMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(lblNumero1)
                                .addGap(18, 18, 18))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPrecioVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNumero3)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(txtPrecioCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(spnStockMinimo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(cmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(txtUbicacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(scrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnLimpiar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
    // 1. Validaciones básicas
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
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Los precios y stock deben ser números válidos.");
            return;
        }

        ComboBoxItem itemCat = (ComboBoxItem) cmbCategoria.getSelectedItem();
        if (itemCat.getId() == 0) {
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
            JOptionPane.showMessageDialog(this, "Error al registrar. Verifique que el código no esté repetido.");
        }
    
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
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
            
            // --- AQUÍ ESTÁ EL CAMBIO PARA QUE PUEDAS EDITAR EL STOCK ---
            p.setStockActual(Integer.parseInt(txtStock.getText()));
            
        } catch (Exception e) {
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
            JOptionPane.showMessageDialog(this, "Error al modificar.");
        }
    }//GEN-LAST:event_btnEditarActionPerformed

    private void txtCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoActionPerformed

    }//GEN-LAST:event_txtCodigoActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
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
                JOptionPane.showMessageDialog(this, "Error al eliminar.");
            }
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void txtPrecioVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrecioVentaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrecioVentaActionPerformed

    private void txtPrecioCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrecioCompraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrecioCompraActionPerformed

    private void txtBuscarInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarInventarioActionPerformed
        // TODO add your handling code here:// Asegúrate de haber creado el campo txtBuscarInventario
        String texto = txtBuscarInventario.getText().trim();
        cargarTabla(texto);
    }//GEN-LAST:event_txtBuscarInventarioActionPerformed

    private void btnBuscarInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarInventarioActionPerformed
        // TODO add your handling code here:
        // Buscar al dar Enter
        String texto = txtBuscarInventario.getText().trim();
        cargarTabla(texto);
    }//GEN-LAST:event_btnBuscarInventarioActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        // TODO add your handling code here:
        limpiarFormulario();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void txtStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtStockActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStockActionPerformed

    private void tablaProductosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaProductosKeyPressed
        // TODO add your handling code here:
        int fila = tablaProductos.getSelectedRow();
    
    if (fila >= 0) {
        try {
            // Depuración: Si sale este mensaje en la consola de abajo, el clic SÍ funciona
            System.out.println("Fila seleccionada: " + fila); 

            // Columna 0: ID (Invisible o visible)
            idProductoSeleccionado = Integer.parseInt(tablaProductos.getValueAt(fila, 0).toString());

            // Columna 1: Código
            txtCodigo.setText(tablaProductos.getValueAt(fila, 1).toString());

            // Columna 2: Nombre
            txtNombre.setText(tablaProductos.getValueAt(fila, 2).toString());

            // Columna 3: Marca
            txtMarca.setText(tablaProductos.getValueAt(fila, 3).toString());

            // Columna 4: Stock
            txtStock.setText(tablaProductos.getValueAt(fila, 4).toString());

            // Columna 5: Stock Mínimo
            spnStockMinimo.setValue(Integer.parseInt(tablaProductos.getValueAt(fila, 5).toString()));

            // Columna 6 y 7: Precios (Cambiamos coma por punto por si acaso)
            txtPrecioVenta.setText(tablaProductos.getValueAt(fila, 6).toString().replace(",", "."));
            txtPrecioCompra.setText(tablaProductos.getValueAt(fila, 7).toString().replace(",", "."));

            // Columna 9: Ubicación (Saltamos la 8 que es Categoria ID)
            if (tablaProductos.getValueAt(fila, 9) != null) {
                txtUbicacion.setText(tablaProductos.getValueAt(fila, 9).toString());
            }

            // Activar botones
            btnGuardar.setEnabled(false);
            btnEditar.setEnabled(true);
            
        } catch (Exception e) {
            System.err.println("Error al seleccionar: " + e.getMessage());
        }
    }
    }//GEN-LAST:event_tablaProductosKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarInventario;
    public javax.swing.JButton btnEditar;
    public javax.swing.JButton btnEliminar;
    public javax.swing.JButton btnGuardar;
    public javax.swing.JButton btnLimpiar;
    private javax.swing.JComboBox cmbCategoria;
    public javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    public javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    public javax.swing.JLabel lblNumero;
    public javax.swing.JLabel lblNumero1;
    public javax.swing.JLabel lblNumero2;
    public javax.swing.JLabel lblNumero3;
    public javax.swing.JLabel lblNumero4;
    private javax.swing.JLabel lblTitulo;
    private java.awt.ScrollPane scrollPane2;
    private javax.swing.JSpinner spnStockMinimo;
    private javax.swing.JTable tablaProductos;
    private javax.swing.JTable tablaRetirosCurso;
    private javax.swing.JTextField txtBuscarInventario;
    public javax.swing.JTextField txtCodigo;
    public javax.swing.JTextField txtMarca;
    public javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPrecioCompra;
    public javax.swing.JTextField txtPrecioVenta;
    public javax.swing.JTextField txtStock;
    private javax.swing.JTextField txtUbicacion;
    // End of variables declaration//GEN-END:variables
// --- CLASE RENDERIZADORA PARA PINTAR CELDAS ---
}

