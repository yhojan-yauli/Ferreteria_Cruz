package com.ferreteria.vista;

import com.ferreteria.dao.ClienteDAOImpl;
import com.ferreteria.dao.ProductoDAOImpl;
import com.ferreteria.dao.VentaDAOImpl;
import com.ferreteria.modelo.Cliente;
import com.ferreteria.modelo.DetalleVenta;
import com.ferreteria.modelo.Producto;
import com.ferreteria.modelo.Usuario;
import com.ferreteria.modelo.Venta;
// import com.ferreteria.util.ComboBoxItem; // YA NO SE NECESITA
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class PanelVentas2 extends javax.swing.JPanel {

    // --- VARIABLES GLOBALES PARA MANEJAR EL ESTADO DE LA VENTA ---
    private DefaultTableModel modeloTablaCarrito;
    private double totalVentaCalculado = 0.0;
    private javax.swing.JComboBox<String> cmbMetodoPago;
    // Objetos para guardar temporalmente lo que se selecciona
    private Cliente clienteActual = null;
    private Producto productoSeleccionadoActual = null;
    
    // Usuario que está usando el sistema (necesario para registrar quién hizo la venta)
    private Usuario usuarioVendedor; 

    // DAOs necesarios
    private ProductoDAOImpl productoDAO = new ProductoDAOImpl();
    private ClienteDAOImpl clienteDAO = new ClienteDAOImpl();
    private VentaDAOImpl ventaDAO = new VentaDAOImpl();

    /**
     * Constructor.
     * @param usuario El vendedor que ha iniciado sesión.
     */
    public PanelVentas2(Usuario usuario) {
        this.usuarioVendedor = usuario; // Guardamos quién es el vendedor
        initComponents(); // Carga el diseño gráfico
        initPersonalizado(); // Carga nuestra lógica extra
        cmbMetodoPago = new javax.swing.JComboBox<>();
cmbMetodoPago.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "EFECTIVO", "TARJETA", "YAPE / PLIN (QR)" }));
cmbMetodoPago.setFont(new java.awt.Font("Segoe UI", 1, 14));
    }

    /**
     * Inicialización de cosas que no hace el diseñador visual.
     */
    private void initPersonalizado() {
        // 1. Configurar la fecha actual
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        lblFecha.setText(sdf.format(new Date()));
        
        // 2. Mostrar el nombre del vendedor
        if (usuarioVendedor != null) {
            lblVendedor.setText(usuarioVendedor.getNombreCompleto());
        }

        // 3. Configurar la tabla del carrito
        configurarTablaCarrito();

        // 4. Iniciar totales en cero
        calcularTotales();
        
        // 5. Asegurar que el foco inicie en la búsqueda de cliente
        txtRucCliente.requestFocus();
    }

    private void configurarTablaCarrito() {
        // Definimos las columnas de la tabla de ventas
        String[] titulos = {"ID Prod", "Código", "Producto / Modelo", "Cantidad", "Precio Unit.", "Subtotal"};
        modeloTablaCarrito = new DefaultTableModel(null, titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Que no se puedan editar las celdas directamente
            }
        };
        tablaCarrito.setModel(modeloTablaCarrito);
        
        // Ocultar la columna ID (índice 0) porque no es necesario que el cliente la vea
        tablaCarrito.getColumnModel().getColumn(0).setMinWidth(0);
        tablaCarrito.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaCarrito.getColumnModel().getColumn(0).setWidth(0);
    }

    // =========================================================================
    // MÉTODOS DE CÁLCULOS Y LIMPIEZA
    // =========================================================================

    // Calcula la suma de la columna "Subtotal" de la tabla
    private void calcularTotales() {
        totalVentaCalculado = 0.0;
        // Recorremos las filas de la tabla. El subtotal está en la columna índice 5.
        for (int i = 0; i < tablaCarrito.getRowCount(); i++) {
            try {
                double subtotalFila = Double.parseDouble(tablaCarrito.getValueAt(i, 5).toString().replace(",", "."));
                totalVentaCalculado += subtotalFila;
            } catch (NumberFormatException e) {
                System.err.println("Error al leer monto de fila " + i);
            }
        }
        // Actualizamos los campos de texto
        txtSubTotal.setText(String.format("%.2f", totalVentaCalculado));
        txtMontoFinal.setText(String.format("%.2f", totalVentaCalculado));
    }

    // Limpia toda la pantalla después de una venta exitosa
    private void limpiarVentaCompleta() {
        // Limpiar cliente
        clienteActual = null;
        txtRucCliente.setText("");
        txtNombreCliente.setText("");
        txtDireccionCliente.setText("");
        
        // Limpiar carrito
        modeloTablaCarrito.setRowCount(0); // Borra todas las filas
        
        // Limpiar zona de producto
        limpiarZonaProducto();
        
        // Resetear totales
        calcularTotales();
        
        txtRucCliente.requestFocus();
    }
    
    // CORREGIDO: Ya no usa los combos viejos
    private void limpiarZonaProducto() {
        productoSeleccionadoActual = null;
        txtBuscarProducto.setText(""); // Limpiamos el buscador
        txtStock.setText("");
        txtCodigoProd.setText("");
        txtPrecio.setText("");
        txtMontoItem.setText("");
        spnCantidad.setValue(1);
    }

    // =========================================================================
    // NUEVA LÓGICA PARA EL BUSCADOR INTELIGENTE
    // =========================================================================

    /**
     * Método principal de búsqueda. Se ejecuta al dar clic en "Buscar" o presionar Enter.
     */
   private void buscarProducto() {
        String texto = txtBuscarProducto.getText().trim();
        System.out.println("--- DEBUG: Entrando a buscarProducto(). Texto: '" + texto + "' ---");

        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escriba el nombre o código del producto.", "Atención", JOptionPane.WARNING_MESSAGE);
            txtBuscarProducto.requestFocus();
            return;
        }

        try {
            // 1. Llamamos al DAO
            System.out.println("--- DEBUG: Consultando a la base de datos... ---");
            List<Producto> resultados = productoDAO.buscarProductosParaVenta(texto);
            System.out.println("--- DEBUG: Búsqueda SQL terminada. Productos encontrados: " + resultados.size() + " ---");

            // 2. Evaluamos los resultados
            if (resultados.isEmpty()) {
                System.out.println("--- DEBUG: Bloque IF - Cero resultados ---");
                JOptionPane.showMessageDialog(this, "No se encontraron productos que coincidan con: " + texto, "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
                limpiarZonaProducto();
                txtBuscarProducto.requestFocus();
                
            } else if (resultados.size() == 1) {
                System.out.println("--- DEBUG: Bloque IF - ¡Un producto encontrado! Llenando datos... ---");
                Producto p = resultados.get(0);
                seleccionarProductoEncontrado(p);
                
                txtBuscarProducto.setText(""); // Limpiamos el buscador
                spnCantidad.requestFocus();    // Pasamos el foco al spinner
                System.out.println("--- DEBUG: Cajas de texto llenadas con éxito ---");
                
            } else {
                System.out.println("--- DEBUG: Bloque IF - Múltiples resultados ---");
                String mensaje = "Se encontraron " + resultados.size() + " coincidencias.\n"
                               + "Por favor, sea más específico.\n\nEjemplos:";
                
                for(int i=0; i < Math.min(resultados.size(), 3); i++) {
                    mensaje += "\n- " + resultados.get(i).getNombre() + " ("+ resultados.get(i).getCodigo() +")";
                }
                JOptionPane.showMessageDialog(this, mensaje, "Múltiples resultados", JOptionPane.WARNING_MESSAGE);
                txtBuscarProducto.requestFocus();
            }
            
        } catch (Exception e) {
            // SI ALGO FALLA SILENCIOSAMENTE, ESTO LO ATRAPARÁ Y LO GRITARÁ EN LA CONSOLA
            System.err.println("!!! ERROR FATAL INTERNO DURANTE LA BÚSQUEDA !!!");
            System.err.println("Mensaje de error: " + e.getMessage());
            e.printStackTrace(); 
            JOptionPane.showMessageDialog(this, "Ocurrió un error al cargar el producto. Revise la consola.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void seleccionarProductoEncontrado(Producto p) {
        this.productoSeleccionadoActual = p;
        System.out.println("--- DEBUG: Ejecutando seleccionarProductoEncontrado() para: " + p.getNombre() + " ---");

        txtStock.setText(String.valueOf(p.getStockActual()));
        txtCodigoProd.setText(p.getCodigo());
        
        // Cuidado aquí: a veces el String.format con comas y puntos según el país causa errores
        String precioFormateado = String.format("%.2f", p.getPrecioVenta());
        txtPrecio.setText(precioFormateado);
        
        spnCantidad.setValue(1);
        calcularMontoItem(); // Llamamos al cálculo
    }

    private void calcularMontoItem() {
        if (productoSeleccionadoActual != null) {
            try {
                System.out.println("--- DEBUG: Entrando a calcularMontoItem() ---");
                
                // FORMA SEGURA DE OBTENER EL VALOR DEL SPINNER PARA EVITAR CLASSCASTEXCEPTION
                int cant = Integer.parseInt(spnCantidad.getValue().toString());
                double precio = productoSeleccionadoActual.getPrecioVenta();
                double monto = precio * cant;
                
                txtMontoItem.setText(String.format("%.2f", monto));
                System.out.println("--- DEBUG: Cálculo exitoso. Monto: " + monto + " ---");
            } catch (Exception e) {
                System.err.println("!!! ERROR AL CALCULAR MONTO ITEM: " + e.getMessage());
            }
        } else {
            txtMontoItem.setText("");
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblAlumnos = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnAgregar = new javax.swing.JButton();
        btnRegistrarVenta = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        lblTitulo4 = new javax.swing.JLabel();
        panelAlumno = new javax.swing.JPanel();
        lblTitulo9 = new javax.swing.JLabel();
        lblApellidos2 = new javax.swing.JLabel();
        lblDni2 = new javax.swing.JLabel();
        txtDireccionCliente = new javax.swing.JTextField();
        lblCelular2 = new javax.swing.JLabel();
        txtNombreCliente = new javax.swing.JTextField();
        txtApellidos = new javax.swing.JTextField();
        txtDni = new javax.swing.JTextField();
        txtCelular = new javax.swing.JTextField();
        lblCodAlumno2 = new javax.swing.JLabel();
        lblNombres2 = new javax.swing.JLabel();
        txtRucCliente = new javax.swing.JTextField();
        btnBuscarCliente = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        scrollPane1 = new java.awt.ScrollPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaCarrito = new javax.swing.JTable();
        lblFecha = new javax.swing.JLabel();
        lblVendedor = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        spnCantidad = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtSubTotal = new javax.swing.JTextField();
        txtMontoFinal = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtStock = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtCodigoProd = new javax.swing.JTextField();
        txtPrecio = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtMontoItem = new javax.swing.JTextField();
        txtBuscarProducto = new javax.swing.JTextField();
        btnBuscarProd = new javax.swing.JButton();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jTable1);

        tblAlumnos.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        tblAlumnos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Código", "Nombres", "Apellidos", "DNI", "Edad", "Celular", "Estado"
            }
        ));
        tblAlumnos.setShowHorizontalLines(true);
        tblAlumnos.setShowVerticalLines(true);
        tblAlumnos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAlumnosMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblAlumnos);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        btnGuardar.setBackground(new java.awt.Color(243, 156, 18));
        btnGuardar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnAgregar.setBackground(new java.awt.Color(243, 156, 18));
        btnAgregar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAgregar.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregar.setText("agregar");
        btnAgregar.setMaximumSize(new java.awt.Dimension(80, 23));
        btnAgregar.setMinimumSize(new java.awt.Dimension(80, 23));
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        btnRegistrarVenta.setBackground(new java.awt.Color(243, 156, 18));
        btnRegistrarVenta.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnRegistrarVenta.setForeground(new java.awt.Color(255, 255, 255));
        btnRegistrarVenta.setText("registrar venta");
        btnRegistrarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarVentaActionPerformed(evt);
            }
        });

        btnEliminar.setBackground(new java.awt.Color(231, 28, 36));
        btnEliminar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnEliminar.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        jPanel5.setBackground(new java.awt.Color(0, 102, 102));
        jPanel5.setForeground(new java.awt.Color(153, 204, 0));
        jPanel5.setPreferredSize(new java.awt.Dimension(880, 70));

        lblTitulo4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTitulo4.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo4.setText("ventas");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(244, 244, 244)
                .addComponent(lblTitulo4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(300, 300, 300))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(lblTitulo4, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelAlumno.setBackground(new java.awt.Color(248, 249, 250));
        panelAlumno.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(41, 128, 185), 1, true));
        panelAlumno.setToolTipText("");
        panelAlumno.setName(""); // NOI18N
        panelAlumno.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitulo9.setBackground(new java.awt.Color(25, 42, 86));
        lblTitulo9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTitulo9.setForeground(new java.awt.Color(25, 42, 86));
        lblTitulo9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo9.setText("INGRESAR DATOS");
        panelAlumno.add(lblTitulo9, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 1, -1, -1));

        lblApellidos2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblApellidos2.setText("Apellido:");
        panelAlumno.add(lblApellidos2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 132, 150, 32));

        lblDni2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblDni2.setText("DNI:");
        panelAlumno.add(lblDni2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 183, 150, 32));

        txtDireccionCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDireccionClienteActionPerformed(evt);
            }
        });
        panelAlumno.add(txtDireccionCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 300, 234, 32));

        lblCelular2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblCelular2.setText("Celular:");
        panelAlumno.add(lblCelular2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 150, 32));

        txtNombreCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreClienteActionPerformed(evt);
            }
        });
        panelAlumno.add(txtNombreCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 80, 234, 35));

        txtApellidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtApellidosActionPerformed(evt);
            }
        });
        panelAlumno.add(txtApellidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 130, 234, 36));

        txtDni.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDniActionPerformed(evt);
            }
        });
        panelAlumno.add(txtDni, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 180, 234, 32));
        panelAlumno.add(txtCelular, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 240, 234, 32));

        lblCodAlumno2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblCodAlumno2.setText("direccion:");
        panelAlumno.add(lblCodAlumno2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 300, 150, 32));

        lblNombres2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblNombres2.setText("Nombre:");
        panelAlumno.add(lblNombres2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 150, 32));

        txtRucCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRucClienteActionPerformed(evt);
            }
        });

        btnBuscarCliente.setBackground(new java.awt.Color(41, 128, 185));
        btnBuscarCliente.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscarCliente.setText("🔍 Buscar");
        btnBuscarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarClienteActionPerformed(evt);
            }
        });

        jLabel1.setText("RUC:");

        tablaCarrito.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(tablaCarrito);

        scrollPane1.add(jScrollPane3);

        jLabel2.setText("BUSCAR PRODUCTO (Nombre/Código):");

        jLabel4.setText("cantidad");

        jLabel5.setText("subtotal");

        jLabel6.setText("monto final");

        txtSubTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSubTotalActionPerformed(evt);
            }
        });

        jLabel7.setText("stock");

        txtStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtStockActionPerformed(evt);
            }
        });

        jLabel8.setText("Codigo_producto:");

        txtCodigoProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoProdActionPerformed(evt);
            }
        });

        jLabel9.setText("Precio:");

        jLabel10.setText("Monto:");

        txtMontoItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMontoItemActionPerformed(evt);
            }
        });

        txtBuscarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarProductoActionPerformed(evt);
            }
        });
        txtBuscarProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarProductoKeyPressed(evt);
            }
        });

        btnBuscarProd.setText("buscar");
        btnBuscarProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarProdActionPerformed(evt);
            }
        });
        btnBuscarProd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnBuscarProdKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 1462, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnBuscarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6)
                        .addComponent(txtRucCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(70, 70, 70)
                        .addComponent(lblFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(95, 95, 95)
                        .addComponent(lblVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(panelAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(38, 38, 38)
                                .addComponent(txtBuscarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnBuscarProd))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(43, 43, 43)
                                .addComponent(scrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 581, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(19, 19, 19)
                                        .addComponent(jLabel4)
                                        .addGap(45, 45, 45)
                                        .addComponent(spnCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(76, 76, 76)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel8)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGap(24, 24, 24))))))
                        .addGap(33, 33, 33)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(36, 36, 36)
                                .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtCodigoProd, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtMontoItem, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(692, 692, 692)
                        .addComponent(btnRegistrarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(136, 136, 136)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(127, 127, 127)
                                .addComponent(txtMontoFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(256, 256, 256)
                                .addComponent(jLabel5)
                                .addGap(127, 127, 127)
                                .addComponent(txtSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(0, 34, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(6, 6, 6)
                        .addComponent(btnBuscarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(txtRucCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(panelAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, 431, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabel2))
                            .addComponent(txtBuscarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBuscarProd))
                        .addGap(44, 44, 44)
                        .addComponent(jLabel8)
                        .addGap(9, 9, 9)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(spnCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(6, 6, 6)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(17, 17, 17)
                                .addComponent(jLabel9)))
                        .addGap(10, 10, 10)
                        .addComponent(scrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabel7))
                            .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(42, 42, 42)
                        .addComponent(txtCodigoProd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(txtMontoItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(jLabel5))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(txtSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(txtMontoFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRegistrarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarClienteActionPerformed
  String rucDni = txtRucCliente.getText().trim();
    if (rucDni.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Ingrese un RUC o DNI para buscar.", "Atención", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // Llamamos al DAO (Asegúrate que tu DAO ya use la tabla nueva con nombres y apellidos)
    clienteActual = clienteDAO.buscarPorDniRuc(rucDni);
    
    if (clienteActual != null) {
        // RELLENAMOS CADA CAMPO POR SEPARADO
        txtNombreCliente.setText(clienteActual.getNombres()); // Solo el nombre
        txtApellidos.setText(clienteActual.getApellidos()); // Solo el apellido
        txtDni.setText(clienteActual.getDniRuc()); // El DNI
        txtCelular.setText(clienteActual.getCelular()); // El celular
        txtDireccionCliente.setText(clienteActual.getDireccion()); // La dirección
        
        // Pasamos el foco al buscador de productos para seguir la venta
        txtBuscarProducto.requestFocus(); 
    } else {
        JOptionPane.showMessageDialog(this, "Cliente no encontrado.", "Info", JOptionPane.INFORMATION_MESSAGE);
        
        // Limpiamos los campos para que no quede basura de búsquedas anteriores
        limpiarCamposCliente();
    }
}

// Método auxiliar para limpiar los TXT del cliente
private void limpiarCamposCliente() {
    txtNombreCliente.setText("");
    txtApellidos.setText("");
    txtDni.setText("");
    txtCelular.setText("");
    txtDireccionCliente.setText("");
    clienteActual = null;
    }//GEN-LAST:event_btnBuscarClienteActionPerformed

    private void txtDniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDniActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDniActionPerformed

    private void txtApellidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtApellidosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtApellidosActionPerformed

    private void btnRegistrarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarVentaActionPerformed
  System.out.println("\n>>> INICIO DEBUG: Click en botón REGISTRAR VENTA <<<");

        // 1. Validar que haya al menos un producto en el carrito
        if (modeloTablaCarrito == null || modeloTablaCarrito.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "El carrito está vacío. Agregue productos primero.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            txtBuscarProducto.requestFocus();
            return;
        }

        // 2. Pedir confirmación al usuario antes de guardar
        String montoString = String.format("%.2f", totalVentaCalculado);
        int confirmacion = JOptionPane.showConfirmDialog(this, 
                "¿Está seguro de registrar esta venta por el total de S/ " + montoString + "?", 
                "Confirmar Venta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                
        if (confirmacion != JOptionPane.YES_OPTION) {
            return; // Si dice que NO, cancelamos la acción
        }

        try {
            // 3. Crear el objeto Venta Principal
            Venta nuevaVenta = new Venta();
            
            // Generamos un número de comprobante temporal (Ej: "V-12345")
            // En un sistema real esto vendría de una tabla correlativa
            String numComprobante = "V-" + (System.currentTimeMillis() % 100000);
            nuevaVenta.setNumeroComprobante(numComprobante);
            nuevaVenta.setTotalVenta(totalVentaCalculado);
            
            // Asignar ID del Vendedor (Usuario)
            if (usuarioVendedor != null) {
                nuevaVenta.setIdUsuario(usuarioVendedor.getIdUsuario()); 
            } else {
                nuevaVenta.setIdUsuario(1); // ID por defecto en caso de estar probando sin Login
            }

            // Asignar ID del Cliente (Si buscó uno, lo usa; si no, va como 0 / Público en general)
            if (clienteActual != null) {
                nuevaVenta.setIdCliente(clienteActual.getIdCliente());
            } else {
                nuevaVenta.setIdCliente(0);
            }

            // 4. Llenar los detalles de la venta leyendo la tabla
            java.util.List<DetalleVenta> listaDetalles = new java.util.ArrayList<>();
            
            for (int i = 0; i < modeloTablaCarrito.getRowCount(); i++) {
                DetalleVenta detalle = new DetalleVenta();
                
                // Extraemos los datos de cada columna (¡Ojo con los índices de las columnas!)
                // Columna 0: ID Producto
                detalle.setIdProducto(Integer.parseInt(modeloTablaCarrito.getValueAt(i, 0).toString()));
                // Columna 3: Cantidad
                detalle.setCantidad(Integer.parseInt(modeloTablaCarrito.getValueAt(i, 3).toString()));
                // Columna 4: Precio Unitario
                detalle.setPrecioUnitario(Double.parseDouble(modeloTablaCarrito.getValueAt(i, 4).toString().replace(",", ".")));
                // Columna 5: Subtotal
                detalle.setSubtotal(Double.parseDouble(modeloTablaCarrito.getValueAt(i, 5).toString().replace(",", ".")));

                listaDetalles.add(detalle); // Lo agregamos a la lista
            }
            
            // Guardamos la lista de detalles dentro de la venta principal
            nuevaVenta.setDetalles(listaDetalles);

            // 5. ¡LA MAGIA! Llamamos al DAO para que haga la transacción SQL
            System.out.println("--- DEBUG: Enviando venta al DAO... ---");
            boolean exito = ventaDAO.registrarVentaTransaccion(nuevaVenta);

            // 6. Evaluar el resultado
            if (exito) {
                JOptionPane.showMessageDialog(this, 
                        "¡Venta registrada exitosamente!\nN° Comprobante: " + numComprobante, 
                        "Venta Completada", JOptionPane.INFORMATION_MESSAGE);
                        
                // Llamamos a tu método que limpia todo para el siguiente cliente
                limpiarVentaCompleta(); 
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Hubo un error al registrar la venta en la Base de Datos.\nRevise la consola para más detalles.", 
                        "Error Faltal", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            System.err.println("!!! ERROR AL PREPARAR LA VENTA !!!");
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error de datos al intentar registrar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnRegistrarVentaActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed

        System.out.println("\n>>> INICIO DEBUG: Click en botón AGREGAR <<<");

        // 1. Validar si hay producto seleccionado
        // Esta variable se llena SOLO si el botón BUSCAR tuvo éxito antes.
        if (productoSeleccionadoActual == null) {
            System.out.println("ERROR FATAL: La variable 'productoSeleccionadoActual' es NULL.");
            System.out.println("CAUSA PROBABLE: No se ha buscado un producto, o la búsqueda no arrojó resultados exactos.");
            JOptionPane.showMessageDialog(this, "Primero debe BUSCAR y encontrar un producto válido.", "Error", JOptionPane.WARNING_MESSAGE);
            txtBuscarProducto.requestFocus();
            return; // Se detiene aquí
        } else {
            System.out.println("OK: Producto seleccionado: " + productoSeleccionadoActual.getNombre() + " (ID: " + productoSeleccionadoActual.getIdProducto() + ")");
        }

        // 2. Validar cantidad
        int cantidad = (Integer) spnCantidad.getValue();
        System.out.println("INFO: Cantidad solicitada: " + cantidad);

        if (cantidad <= 0) {
             System.out.println("ERROR: Cantidad es menor o igual a cero.");
             JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0.", "Error", JOptionPane.WARNING_MESSAGE);
             return;
        }

        // 3. Validar stock
        System.out.println("INFO: Stock disponible en BD: " + productoSeleccionadoActual.getStockActual());
        if (cantidad > productoSeleccionadoActual.getStockActual()) {
             System.out.println("ERROR: Stock insuficiente.");
             JOptionPane.showMessageDialog(this, "¡Stock insuficiente!\nSolo quedan " + productoSeleccionadoActual.getStockActual() + " unidades.", "Alerta de Stock", JOptionPane.ERROR_MESSAGE);
             return;
        }

        // 4. Intentar agregar a la tabla
        try {
            System.out.println("calculando subtotales...");
            double precioUnit = productoSeleccionadoActual.getPrecioVenta();
            double subtotalItem = precioUnit * cantidad;

            Object[] fila = new Object[6];
            fila[0] = productoSeleccionadoActual.getIdProducto();
            fila[1] = productoSeleccionadoActual.getCodigo();
            fila[2] = productoSeleccionadoActual.getNombre();
            fila[3] = cantidad;
            fila[4] = String.format("%.2f", precioUnit);
            fila[5] = String.format("%.2f", subtotalItem);

            System.out.println("Intentando agregar fila al modelo de la tabla...");
            if (modeloTablaCarrito != null) {
                modeloTablaCarrito.addRow(fila);
                System.out.println(">>> ÉXITO: Fila agregada correctamente a la tabla. <<<");
            } else {
                 System.out.println("ERROR GRAVE: El 'modeloTablaCarrito' es NULL. La tabla no se inicializó bien.");
            }

            // Actualizar totales generales
            calcularTotales();

            // Limpiar la zona de producto
            limpiarZonaProducto();
            txtBuscarProducto.requestFocus();

        } catch (Exception e) {
            System.err.println("!!! EXCEPCIÓN NO CONTROLADA AL AGREGAR !!!");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace(); // Muestra el error completo en rojo
        }
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
     
 
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void txtNombreClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreClienteActionPerformed


// TODO add your handling code here:
    }//GEN-LAST:event_txtNombreClienteActionPerformed

    private void txtRucClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRucClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRucClienteActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
    
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void tblAlumnosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAlumnosMouseClicked
   
    }//GEN-LAST:event_tblAlumnosMouseClicked

    private void txtDireccionClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDireccionClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionClienteActionPerformed

    private void txtSubTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSubTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSubTotalActionPerformed

    private void txtStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtStockActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStockActionPerformed

    private void txtCodigoProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoProdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoProdActionPerformed

    private void txtMontoItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMontoItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMontoItemActionPerformed

    private void btnBuscarProdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnBuscarProdKeyPressed
        
    }//GEN-LAST:event_btnBuscarProdKeyPressed

    private void btnBuscarProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarProdActionPerformed

    }//GEN-LAST:event_btnBuscarProdActionPerformed

    private void txtBuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarProductoActionPerformed
      
    }//GEN-LAST:event_txtBuscarProductoActionPerformed

    private void txtBuscarProductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarProductoKeyPressed
   if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
        System.out.println("--- DEBUG: Enter presionado en la caja de texto ---");
        buscarProducto();
      }
    }//GEN-LAST:event_txtBuscarProductoKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnBuscarCliente;
    private javax.swing.JButton btnBuscarProd;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnRegistrarVenta;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblApellidos2;
    private javax.swing.JLabel lblCelular2;
    private javax.swing.JLabel lblCodAlumno2;
    private javax.swing.JLabel lblDni2;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblNombres2;
    private javax.swing.JLabel lblTitulo4;
    private javax.swing.JLabel lblTitulo9;
    private javax.swing.JLabel lblVendedor;
    private javax.swing.JPanel panelAlumno;
    private java.awt.ScrollPane scrollPane1;
    private javax.swing.JSpinner spnCantidad;
    private javax.swing.JTable tablaCarrito;
    private javax.swing.JTable tblAlumnos;
    private javax.swing.JTextField txtApellidos;
    private javax.swing.JTextField txtBuscarProducto;
    private javax.swing.JTextField txtCelular;
    private javax.swing.JTextField txtCodigoProd;
    private javax.swing.JTextField txtDireccionCliente;
    private javax.swing.JTextField txtDni;
    private javax.swing.JTextField txtMontoFinal;
    private javax.swing.JTextField txtMontoItem;
    private javax.swing.JTextField txtNombreCliente;
    private javax.swing.JTextField txtPrecio;
    private javax.swing.JTextField txtRucCliente;
    private javax.swing.JTextField txtStock;
    private javax.swing.JTextField txtSubTotal;
    // End of variables declaration//GEN-END:variables

}