/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ferreteriacruz.Vistas;

/**
 *
 * @author USUARIO
 */
import ferreteriacruz.entity.Categoria;
import ferreteriacruz.Services.CategoriaService;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import ferreteriacruz.Services.MarcaService;
import ferreteriacruz.Services.ProductoService;
import ferreteriacruz.entity.Marca;
import ferreteriacruz.entity.Producto;
import java.util.List;



public class Admin extends javax.swing.JFrame {

// Service (capa intermedia)
    private final CategoriaService categoriaService = new CategoriaService();
// Modelo de la tabla
    private DefaultTableModel modeloCategoria;
    
    private final MarcaService marcaService = new MarcaService();
    private DefaultTableModel modeloMarca;
    
    private final ProductoService productoService = new ProductoService();
    private DefaultTableModel modeloProducto;
    
    

    /**
     * Creates new form Admin
     */
    public Admin() {
        initComponents();
        cargarEstados();
        configurarTabla();
        listarCategorias();
        
        //=======================//
        configurarTablaMarcas();
        cargarEstadosMarcas();
        listarMarcas();
        
        //========================//
        configurarTablaProductos();
        cargarComboCategorias();
        cargarComboMarcas();
        cargarComboEstado();
    }

    private void cargarEstados() {
        jComboBoxEstadoCategoria.removeAllItems();
        jComboBoxEstadoCategoria.addItem("ACTIVO");
        jComboBoxEstadoCategoria.addItem("INACTIVO");
    }

    private void configurarTabla() {
        modeloCategoria = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Estado"}, 0
        );
        tablaCategoria.setModel(modeloCategoria);
    }

    private void listarCategorias() {
        modeloCategoria.setRowCount(0);
        try {
            for (Categoria c : categoriaService.listarCategorias()) {
                modeloCategoria.addRow(new Object[]{
                    c.getIdCategoria(),
                    c.getNombre(),
                    c.isActivo() ? "ACTIVO" : "INACTIVO"
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al listar categorías");
        }
    }

    private void tablaCategoriaMouseClicked(java.awt.event.MouseEvent evt) {

        int fila = tablaCategoria.getSelectedRow();

        jTextNombreCategoria.setText(modeloCategoria.getValueAt(fila, 1).toString());
        jComboBoxEstadoCategoria.setSelectedItem(
                modeloCategoria.getValueAt(fila, 2).toString()
        );
    }

    private void limpiarFormulario() {
        jTextNombreCategoria.setText("");
        jComboBoxEstadoCategoria.setSelectedIndex(0);
        tablaCategoria.clearSelection();
    }
    
    
    //====================================================//

    
    private void configurarTablaMarcas() {
    modeloMarca = new DefaultTableModel(
        new Object[]{"ID", "Nombre", "Estado"}, 0
    );
    jTableMarcas.setModel(modeloMarca);
}

private void cargarEstadosMarcas() {
    jComboBoxEstadoMarcas.removeAllItems();
    jComboBoxEstadoMarcas.addItem("ACTIVO");
    jComboBoxEstadoMarcas.addItem("INACTIVO");
}

private void listarMarcas() {
    modeloMarca.setRowCount(0);
    try {
        for (Marca m : marcaService.listarMarcas()) {
            modeloMarca.addRow(new Object[]{
                m.getIdMarca(),
                m.getNombre(),
                m.isActivo() ? "ACTIVO" : "INACTIVO"
            });
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al listar marcas");
    }
}
private void jTableMarcasMouseClicked(java.awt.event.MouseEvent evt) {
    int fila = jTableMarcas.getSelectedRow();
    jTextNombreMarca.setText(modeloMarca.getValueAt(fila, 1).toString());
}

  


//========================================0//
private void cargarComboEstado() {
    jComboBoxEstadoProducto.removeAllItems();
    jComboBoxEstadoProducto.addItem("ACTIVO");
    jComboBoxEstadoProducto.addItem("INACTIVO");
}

private void configurarTablaProductos() {
    modeloProducto = new DefaultTableModel(
        new Object[]{"ID", "Nombre", "SKU", "Marca", "Categoria", "Precio", "Stock", "Estado"}, 0
    );
    jTableProductos.setModel(modeloProducto);
}


private List<Marca> listaMarcas;
private List<Categoria> listaCategorias;

private void cargarComboMarcas() {
    jComboBoxMarcaProducto.removeAllItems();
    try {
        listaMarcas = marcaService.listarMarcas(); // guardamos la lista real
        for (Marca m : listaMarcas) {
            if (m.isActivo()) {
                jComboBoxMarcaProducto.addItem(m.getNombre()); // SOLO String
            }
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al cargar marcas");
    }
}


private void cargarComboCategorias() {
    jComboBoxCategoriaProducto.removeAllItems();
    try {
        listaCategorias = categoriaService.listarCategorias();
        for (Categoria c : listaCategorias) {
            if (c.isActivo()) {
                jComboBoxCategoriaProducto.addItem(c.getNombre());
            }
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al cargar categorías");
    }
}




   

private void listarProductos() {
    modeloProducto.setRowCount(0);
    try {
        for (Producto p : productoService.listarProductos()) {
            modeloProducto.addRow(new Object[]{
                p.getIdProducto(),
                p.getNombre(),
                p.getSku(),
                p.getMarca(),
                p.getCategoria(),
                p.getPrecio(),
                p.getStock(),
                p.isActivo() ? "ACTIVO" : "INACTIVO"
            });
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al listar productos");
    }
}

private Marca obtenerMarcaSeleccionada() {
    String nombreMarca = jComboBoxMarcaProducto.getSelectedItem().toString();
    for (Marca m : listaMarcas) {
        if (m.getNombre().equals(nombreMarca)) {
            return m;
        }
    }
    return null;
}
private Categoria obtenerCategoriaSeleccionada() {
    String nombreCategoria = jComboBoxCategoriaProducto.getSelectedItem().toString();
    for (Categoria c : listaCategorias) {
        if (c.getNombre().equals(nombreCategoria)) {
            return c;
        }
    }
    return null;
}



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextNombreCategoria = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jComboBoxEstadoCategoria = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaCategoria = new javax.swing.JTable();
        jButtonAgregarCategoria = new javax.swing.JButton();
        jButtonEliminarCategoria = new javax.swing.JButton();
        jButtonActualizarCategoria = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jTextBuscarcategoria = new javax.swing.JTextField();
        jButtonBuscarCategoria = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTextNombreProducto = new javax.swing.JTextField();
        jComboBoxMarcaProducto = new javax.swing.JComboBox<>();
        jSpinnerPrecioProducto = new javax.swing.JSpinner();
        jSpinnerStockProducto = new javax.swing.JSpinner();
        jSpinnerStockMinimoProducto = new javax.swing.JSpinner();
        jComboBoxEstadoProducto = new javax.swing.JComboBox<>();
        jComboBoxCategoriaProducto = new javax.swing.JComboBox<>();
        jTextSKU = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableProductos = new javax.swing.JTable();
        jSeparator2 = new javax.swing.JSeparator();
        jButtonAgregarProducto = new javax.swing.JButton();
        jButtonActualizarProducto = new javax.swing.JButton();
        jButtonEliminarProducto = new javax.swing.JButton();
        jButtonBuscarProducto = new javax.swing.JButton();
        jTextBuscarProducto = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jTextNombreMarca = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jComboBoxEstadoMarcas = new javax.swing.JComboBox<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableMarcas = new javax.swing.JTable();
        jSeparator3 = new javax.swing.JSeparator();
        jButtonAgregarMarcas = new javax.swing.JButton();
        jButtonActualizarMarcas = new javax.swing.JButton();
        jButtonEliminarMarcas = new javax.swing.JButton();
        jButtonBuscarMarcas = new javax.swing.JButton();
        jTextBuscarMarcas = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Modulo de categorias");

        jLabel2.setText("nombre:");

        jLabel3.setText("Estado : ");

        jComboBoxEstadoCategoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        tablaCategoria.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tablaCategoria);

        jButtonAgregarCategoria.setText("Agregar");
        jButtonAgregarCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAgregarCategoriaActionPerformed(evt);
            }
        });

        jButtonEliminarCategoria.setText("Eliminar");
        jButtonEliminarCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEliminarCategoriaActionPerformed(evt);
            }
        });

        jButtonActualizarCategoria.setText("Actualizar");
        jButtonActualizarCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonActualizarCategoriaActionPerformed(evt);
            }
        });

        jTextBuscarcategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextBuscarcategoriaActionPerformed(evt);
            }
        });

        jButtonBuscarCategoria.setText("Buscar");
        jButtonBuscarCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuscarCategoriaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButtonEliminarCategoria)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonActualizarCategoria)
                        .addGap(53, 53, 53))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(45, 45, 45)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addGap(18, 18, 18)
                                        .addComponent(jComboBoxEstadoCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jTextNombreCategoria))
                                    .addComponent(jLabel1)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(78, 78, 78)
                                .addComponent(jButtonAgregarCategoria))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextBuscarcategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonBuscarCategoria)
                        .addGap(39, 39, 39)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(jLabel1)
                        .addGap(39, 39, 39)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jTextNombreCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jComboBoxEstadoCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addComponent(jButtonAgregarCategoria)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonEliminarCategoria)
                            .addComponent(jButtonActualizarCategoria))
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextBuscarcategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonBuscarCategoria)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Categorias", jPanel1);

        jLabel4.setText("Modulo de Productos");

        jLabel5.setText("Nombre :");

        jLabel6.setText("SKU :");

        jLabel7.setText("Precio :");

        jLabel8.setText("Stock :");

        jLabel9.setText("Stock minimo :");

        jLabel10.setText("Estado :");

        jLabel11.setText("Categoria :");

        jLabel12.setText("marca :");

        jTextNombreProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextNombreProductoActionPerformed(evt);
            }
        });

        jComboBoxMarcaProducto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBoxEstadoProducto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBoxCategoriaProducto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jTableProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(jTableProductos);

        jButtonAgregarProducto.setText("Agregar");
        jButtonAgregarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAgregarProductoActionPerformed(evt);
            }
        });

        jButtonActualizarProducto.setText("Actualizar");
        jButtonActualizarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonActualizarProductoActionPerformed(evt);
            }
        });

        jButtonEliminarProducto.setText("Eliminar");
        jButtonEliminarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEliminarProductoActionPerformed(evt);
            }
        });

        jButtonBuscarProducto.setText("Buscar");
        jButtonBuscarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuscarProductoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel10))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jComboBoxMarcaProducto, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jSpinnerStockProducto)
                                        .addComponent(jComboBoxEstadoProducto, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jTextNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextSKU)
                                    .addComponent(jSpinnerPrecioProducto)
                                    .addComponent(jSpinnerStockMinimoProducto)
                                    .addComponent(jComboBoxCategoriaProducto, 0, 113, Short.MAX_VALUE))
                                .addGap(35, 35, 35))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(jButtonActualizarProducto)
                                .addGap(93, 93, 93)
                                .addComponent(jButtonEliminarProducto)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jSeparator2))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                        .addGap(119, 119, 119)
                                        .addComponent(jLabel4))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                        .addGap(123, 123, 123)
                                        .addComponent(jButtonAgregarProducto)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jTextBuscarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonBuscarProducto)
                        .addGap(80, 80, 80)))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel4)
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jTextSKU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jComboBoxMarcaProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jSpinnerPrecioProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jSpinnerStockProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jSpinnerStockMinimoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jComboBoxEstadoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(jComboBoxCategoriaProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonAgregarProducto)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                        .addComponent(jButtonActualizarProducto)
                        .addGap(18, 18, 18))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonEliminarProducto)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonBuscarProducto)
                    .addComponent(jTextBuscarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44))
        );

        jTabbedPane1.addTab("Productos", jPanel2);

        jLabel13.setText("Modulo de marcas");

        jLabel14.setText("Nombre");

        jLabel15.setText("Estado :");

        jComboBoxEstadoMarcas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jTableMarcas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(jTableMarcas);

        jButtonAgregarMarcas.setText("Agregar");
        jButtonAgregarMarcas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAgregarMarcasActionPerformed(evt);
            }
        });

        jButtonActualizarMarcas.setText("Actualizar");
        jButtonActualizarMarcas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonActualizarMarcasActionPerformed(evt);
            }
        });

        jButtonEliminarMarcas.setText("Eliminar");
        jButtonEliminarMarcas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEliminarMarcasActionPerformed(evt);
            }
        });

        jButtonBuscarMarcas.setText("Buscar");
        jButtonBuscarMarcas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuscarMarcasActionPerformed(evt);
            }
        });

        jTextBuscarMarcas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextBuscarMarcasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(56, 56, 56)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jTextBuscarMarcas)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButtonBuscarMarcas)
                                        .addGap(27, 27, 27))
                                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(44, 44, 44)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel15)
                                    .addComponent(jLabel14))
                                .addGap(32, 32, 32)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextNombreMarca)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jButtonAgregarMarcas)
                                            .addComponent(jComboBoxEstadoMarcas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, Short.MAX_VALUE)))))
                        .addGap(53, 53, 53))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jButtonActualizarMarcas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonEliminarMarcas)
                        .addGap(79, 79, 79)))
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jTextNombreMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jComboBoxEstadoMarcas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonAgregarMarcas)
                .addGap(22, 22, 22)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonActualizarMarcas)
                    .addComponent(jButtonEliminarMarcas))
                .addGap(18, 18, 18)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonBuscarMarcas)
                    .addComponent(jTextBuscarMarcas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );

        jTabbedPane1.addTab("Marcas", jPanel3);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 784, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 447, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Usuarios", jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextBuscarcategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextBuscarcategoriaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextBuscarcategoriaActionPerformed

    private void jButtonAgregarCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAgregarCategoriaActionPerformed
        // TODO add your handling code here:
        try {
            Categoria c = new Categoria();
            c.setNombre(jTextNombreCategoria.getText());
            c.setActivo(true); // siempre inicia activo

            categoriaService.registrarCategoria(c);

            JOptionPane.showMessageDialog(this, "Categoría registrada correctamente");
            listarCategorias();
            limpiarFormulario();

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al registrar categoría");
        }
    }//GEN-LAST:event_jButtonAgregarCategoriaActionPerformed

    private void jButtonActualizarCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonActualizarCategoriaActionPerformed
        // TODO add your handling code here:
        int fila = tablaCategoria.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una categoría");
            return;
        }

        try {
            Categoria c = new Categoria();
            c.setIdCategoria((int) modeloCategoria.getValueAt(fila, 0));
            c.setNombre(jTextNombreCategoria.getText());
            c.setActivo(jComboBoxEstadoCategoria.getSelectedItem().equals("ACTIVO"));

            categoriaService.actualizarCategoria(c);

            JOptionPane.showMessageDialog(this, "Categoría actualizada");
            listarCategorias();
            limpiarFormulario();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar categoría");
        }

    }//GEN-LAST:event_jButtonActualizarCategoriaActionPerformed

    private void jButtonEliminarCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEliminarCategoriaActionPerformed
        // TODO add your handling code here:

        int fila = tablaCategoria.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una categoría");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Desea desactivar la categoría?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                int id = (int) modeloCategoria.getValueAt(fila, 0);
                categoriaService.eliminarCategoria(id);
                listarCategorias();
                limpiarFormulario();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar categoría");
            }

        }

    }//GEN-LAST:event_jButtonEliminarCategoriaActionPerformed

    private void jButtonBuscarCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscarCategoriaActionPerformed
        // TODO add your handling code here:

        String texto = jTextBuscarcategoria.getText().toLowerCase();
        modeloCategoria.setRowCount(0);

        try {
            for (Categoria c : categoriaService.listarCategorias()) {
                if (c.getNombre().toLowerCase().contains(texto)) {
                    modeloCategoria.addRow(new Object[]{
                        c.getIdCategoria(),
                        c.getNombre(),
                        c.isActivo() ? "ACTIVO" : "INACTIVO"
                    });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al buscar");
        }


    }//GEN-LAST:event_jButtonBuscarCategoriaActionPerformed

    private void jTextNombreProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextNombreProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextNombreProductoActionPerformed

    private void jButtonAgregarMarcasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAgregarMarcasActionPerformed
        // TODO add your handling code here:
        try {
        Marca m = new Marca();
        m.setNombre(jTextNombreMarca.getText());
        m.setActivo(true);

        marcaService.registrarMarca(m);

        JOptionPane.showMessageDialog(this, "Marca registrada");
        listarMarcas();
        jTextNombreMarca.setText("");

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage());
    }
        
    }//GEN-LAST:event_jButtonAgregarMarcasActionPerformed

    private void jButtonActualizarMarcasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonActualizarMarcasActionPerformed
        // TODO add your handling code here:
            int fila = jTableMarcas.getSelectedRow();

    if (fila == -1) {
        JOptionPane.showMessageDialog(this, "Seleccione una marca");
        return;
    }

    try {
        int idMarca = Integer.parseInt(modeloMarca.getValueAt(fila, 0).toString());
        String nombre = jTextNombreMarca.getText();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío");
            return;
        }

        Marca m = new Marca();
        m.setIdMarca(idMarca);
        m.setNombre(nombre);

        marcaService.actualizarMarca(m);

        JOptionPane.showMessageDialog(this, "Marca actualizada");
        listarMarcas();
        jTextNombreMarca.setText("");

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al actualizar marca");
    }
        
    }//GEN-LAST:event_jButtonActualizarMarcasActionPerformed

    private void jButtonEliminarMarcasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEliminarMarcasActionPerformed
        // TODO add your handling code here:
        int fila = jTableMarcas.getSelectedRow();

    if (fila == -1) {
        JOptionPane.showMessageDialog(this, "Seleccione una marca");
        return;
    }

    int confirmacion = JOptionPane.showConfirmDialog(
            this,
            "¿Desea desactivar esta marca?",
            "Confirmar",
            JOptionPane.YES_NO_OPTION
    );

    if (confirmacion != JOptionPane.YES_OPTION) return;

    try {
        int idMarca = Integer.parseInt(modeloMarca.getValueAt(fila, 0).toString());
        marcaService.eliminarMarca(idMarca);

        JOptionPane.showMessageDialog(this, "Marca desactivada");
        listarMarcas();
        jTextNombreMarca.setText("");

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al eliminar marca");
    }
        
    }//GEN-LAST:event_jButtonEliminarMarcasActionPerformed

    private void jButtonBuscarMarcasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscarMarcasActionPerformed
        // TODO add your handling code here:

    String texto = jTextBuscarMarcas.getText();

    modeloMarca.setRowCount(0);

    try {
        for (Marca m : marcaService.buscarMarcas(texto)) {
            modeloMarca.addRow(new Object[]{
                m.getIdMarca(),
                m.getNombre(),
                m.isActivo() ? "ACTIVO" : "INACTIVO"
            });
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error en búsqueda");
    }
   
    }//GEN-LAST:event_jButtonBuscarMarcasActionPerformed

    private void jTextBuscarMarcasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextBuscarMarcasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextBuscarMarcasActionPerformed

    private void jButtonAgregarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAgregarProductoActionPerformed
    
         try {
        Producto p = new Producto();

        p.setNombre(jTextNombreProducto.getText());
        p.setSku(jTextSKU.getText());
        p.setPrecio(Double.parseDouble(jSpinnerPrecioProducto.getValue().toString()));
        p.setStock(Integer.parseInt(jSpinnerStockProducto.getValue().toString()));
        p.setStockMinimo(Integer.parseInt(jSpinnerStockMinimoProducto.getValue().toString()));
        p.setActivo(true);

        // 🔥 AQUÍ ESTÁ LA CLAVE
        // 🔑 Conversión correcta desde String
        Marca marca = obtenerMarcaSeleccionada();
        Categoria categoria = obtenerCategoriaSeleccionada();

        if (marca == null || categoria == null) {
            JOptionPane.showMessageDialog(this, "Seleccione marca y categoría válidas");
            return;
        }

        p.setMarca(marca);
        p.setCategoria(categoria);

        productoService.registrarProducto(p);

        JOptionPane.showMessageDialog(this, "Producto registrado correctamente");
        listarProductos();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al registrar producto: " + e.getMessage());
    }


    }//GEN-LAST:event_jButtonAgregarProductoActionPerformed

    private void jButtonActualizarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonActualizarProductoActionPerformed
        // TODO add your handling code here:

    int fila = jTableProductos.getSelectedRow();
    if (fila == -1) {
        JOptionPane.showMessageDialog(this, "Seleccione un producto");
        return;
    }

    try {
        Producto p = new Producto();
        p.setIdProducto((int) modeloProducto.getValueAt(fila, 0));
        p.setNombre(jTextNombreProducto.getText());
        p.setPrecio((double) jSpinnerPrecioProducto.getValue());
        p.setStock((int) jSpinnerStockProducto.getValue());
        p.setActivo(jComboBoxEstadoProducto.getSelectedItem().equals("ACTIVO"));

        productoService.actualizarProducto(p);

        JOptionPane.showMessageDialog(this, "Producto actualizado");
        listarProductos();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al actualizar producto");
    }

        
    }//GEN-LAST:event_jButtonActualizarProductoActionPerformed

    private void jButtonEliminarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEliminarProductoActionPerformed
        // TODO add your handling code here:
        

    int fila = jTableProductos.getSelectedRow();
    if (fila == -1) {
        JOptionPane.showMessageDialog(this, "Seleccione un producto");
        return;
    }

    int confirmacion = JOptionPane.showConfirmDialog(
            this,
            "¿Desea desactivar este producto?",
            "Confirmar",
            JOptionPane.YES_NO_OPTION
    );

    if (confirmacion == JOptionPane.YES_OPTION) {
        try {
            int id = (int) modeloProducto.getValueAt(fila, 0);
            productoService.eliminarProducto(id);
            listarProductos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar producto");
        }
    }

        
    }//GEN-LAST:event_jButtonEliminarProductoActionPerformed

    private void jButtonBuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscarProductoActionPerformed
        // TODO add your handling code here:

    String texto = jTextBuscarProducto.getText();
    modeloProducto.setRowCount(0);

    try {
        for (Producto p : productoService.buscarPorNombre(texto)) {
            modeloProducto.addRow(new Object[]{
                p.getIdProducto(),
                p.getNombre(),
                p.getSku(),
                p.getMarca(),
                p.getCategoria(),
                p.getPrecio(),
                p.getStock(),
                p.isActivo() ? "ACTIVO" : "INACTIVO"
            });
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al buscar producto");
    }

        
    }//GEN-LAST:event_jButtonBuscarProductoActionPerformed

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Admin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonActualizarCategoria;
    private javax.swing.JButton jButtonActualizarMarcas;
    private javax.swing.JButton jButtonActualizarProducto;
    private javax.swing.JButton jButtonAgregarCategoria;
    private javax.swing.JButton jButtonAgregarMarcas;
    private javax.swing.JButton jButtonAgregarProducto;
    private javax.swing.JButton jButtonBuscarCategoria;
    private javax.swing.JButton jButtonBuscarMarcas;
    private javax.swing.JButton jButtonBuscarProducto;
    private javax.swing.JButton jButtonEliminarCategoria;
    private javax.swing.JButton jButtonEliminarMarcas;
    private javax.swing.JButton jButtonEliminarProducto;
    private javax.swing.JComboBox<String> jComboBoxCategoriaProducto;
    private javax.swing.JComboBox<String> jComboBoxEstadoCategoria;
    private javax.swing.JComboBox<String> jComboBoxEstadoMarcas;
    private javax.swing.JComboBox<String> jComboBoxEstadoProducto;
    private javax.swing.JComboBox<String> jComboBoxMarcaProducto;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSpinner jSpinnerPrecioProducto;
    private javax.swing.JSpinner jSpinnerStockMinimoProducto;
    private javax.swing.JSpinner jSpinnerStockProducto;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTableMarcas;
    private javax.swing.JTable jTableProductos;
    private javax.swing.JTextField jTextBuscarMarcas;
    private javax.swing.JTextField jTextBuscarProducto;
    private javax.swing.JTextField jTextBuscarcategoria;
    private javax.swing.JTextField jTextNombreCategoria;
    private javax.swing.JTextField jTextNombreMarca;
    private javax.swing.JTextField jTextNombreProducto;
    private javax.swing.JTextField jTextSKU;
    private javax.swing.JTable tablaCategoria;
    // End of variables declaration//GEN-END:variables
}
