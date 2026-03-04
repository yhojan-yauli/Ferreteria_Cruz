package com.ferreteria.vista;

import com.ferreteria.dao.ClienteDAOImpl;
import com.ferreteria.dao.ProductoDAOImpl;
import com.ferreteria.dao.VentaDAOImpl;
import com.ferreteria.modelo.Cliente;
import com.ferreteria.modelo.DetalleVenta;
import com.ferreteria.modelo.Producto;
import com.ferreteria.modelo.Usuario;
import com.ferreteria.modelo.Venta;
import com.ferreteria.util.GeneradorTicketVirtual; // Importación del generador de tickets

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class PanelVentas extends JPanel {

    // --- VARIABLES GLOBALES ---
    private DefaultTableModel modeloTablaCarrito;
    private double totalVentaCalculado = 0.0;
    private Cliente clienteActual = null;
    private Producto productoSeleccionadoActual = null;
    private Usuario usuarioVendedor;

    // --- DAOs ---
    private ProductoDAOImpl productoDAO = new ProductoDAOImpl();
    private ClienteDAOImpl clienteDAO = new ClienteDAOImpl();
    private VentaDAOImpl ventaDAO = new VentaDAOImpl();

    // --- COMPONENTES VISUALES ---
    private JTextField txtRucCliente, txtNombreCliente;
    private JButton btnBuscarCliente;
    
    private JTextField txtBuscarProducto, txtStock, txtPrecio;
    private JSpinner spnCantidad;
    private JButton btnBuscarProd, btnAgregarItem, btnEliminarItem;
    
    private JTable tablaCarrito;
    private JLabel lblFecha, lblVendedor;
    private JButton btnArqueoCaja; 
    
    // --- COMPONENTES DE TOTALES Y PAGO ---
    private JComboBox<String> cmbMetodoPago;
    private JLabel lblSubtotal, lblIgv, lblTotal;
    private JButton btnRegistrarVenta;

    public PanelVentas(Usuario usuario) {
        this.usuarioVendedor = usuario;
        initComponents();
        initPersonalizado();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(new Color(245, 245, 245));
        this.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ==========================================
        // 1. PANEL SUPERIOR: CABECERA Y CLIENTE
        // ==========================================
        JPanel panelNorte = new JPanel(new BorderLayout(10, 10));
        panelNorte.setOpaque(false);

        // Cabecera (Título, Fecha, Vendedor, CORTE DE CAJA)
        JPanel panelCabecera = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        panelCabecera.setBackground(new Color(41, 128, 185));
        
        JLabel lblTitulo = new JLabel("NUEVA VENTA - FERRETERÍA CRUZ");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        
        lblFecha = new JLabel("Fecha: --/--/----");
        lblFecha.setForeground(Color.WHITE);
        lblFecha.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        lblVendedor = new JLabel("Vendedor: --------");
        lblVendedor.setForeground(Color.WHITE);
        lblVendedor.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        btnArqueoCaja = new JButton("💵 Corte de Caja");
        btnArqueoCaja.setBackground(new Color(46, 204, 113));
        btnArqueoCaja.setForeground(Color.WHITE);
        btnArqueoCaja.setFont(new Font("Segoe UI", Font.BOLD, 12));

        panelCabecera.add(lblTitulo);
        panelCabecera.add(lblFecha);
        panelCabecera.add(lblVendedor);
        panelCabecera.add(btnArqueoCaja); 
        
        panelNorte.add(panelCabecera, BorderLayout.NORTH);

        // Datos del Cliente
        JPanel panelCliente = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelCliente.setBackground(Color.WHITE);
        panelCliente.setBorder(BorderFactory.createTitledBorder(null, "Datos del Cliente", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Segoe UI", Font.BOLD, 12)));
        
        panelCliente.add(new JLabel("DNI/RUC:"));
        txtRucCliente = new JTextField(12);
        btnBuscarCliente = new JButton("🔍 Buscar");
        panelCliente.add(txtRucCliente);
        panelCliente.add(btnBuscarCliente);
        
        panelCliente.add(new JLabel("Nombre:"));
        txtNombreCliente = new JTextField(25);
        txtNombreCliente.setEditable(false);
        panelCliente.add(txtNombreCliente);
        
        panelNorte.add(panelCliente, BorderLayout.CENTER);
        this.add(panelNorte, BorderLayout.NORTH);

        // ==========================================
        // 2. PANEL CENTRAL: BÚSQUEDA DE PRODUCTO Y CARRITO
        // ==========================================
        JPanel panelCentro = new JPanel(new BorderLayout(10, 10));
        panelCentro.setOpaque(false);

        // Buscador de Producto
        JPanel panelProducto = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelProducto.setBackground(Color.WHITE);
        panelProducto.setBorder(BorderFactory.createTitledBorder(null, "Agregar Producto", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Segoe UI", Font.BOLD, 12)));
        
        panelProducto.add(new JLabel("Buscar (Nombre/Código):"));
        txtBuscarProducto = new JTextField(15);
        btnBuscarProd = new JButton("🔍");
        panelProducto.add(txtBuscarProducto);
        panelProducto.add(btnBuscarProd);
        
        panelProducto.add(new JLabel("Precio S/:"));
        txtPrecio = new JTextField(6);
        txtPrecio.setEditable(false);
        panelProducto.add(txtPrecio);
        
        panelProducto.add(new JLabel("Stock:"));
        txtStock = new JTextField(5);
        txtStock.setEditable(false);
        panelProducto.add(txtStock);
        
        panelProducto.add(new JLabel("Cant:"));
        spnCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        panelProducto.add(spnCantidad);
        
        btnAgregarItem = new JButton("➕ Agregar");
        btnAgregarItem.setBackground(new Color(39, 174, 96));
        btnAgregarItem.setForeground(Color.WHITE);
        panelProducto.add(btnAgregarItem);

        panelCentro.add(panelProducto, BorderLayout.NORTH);

        // Tabla del Carrito
        String[] columnas = {"ID", "Código", "Producto", "Cant.", "Precio U.", "Subtotal"};
        modeloTablaCarrito = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaCarrito = new JTable(modeloTablaCarrito);
        tablaCarrito.setRowHeight(25);
        tablaCarrito.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Ocultar ID del producto (Columna 0)
        tablaCarrito.getColumnModel().getColumn(0).setMinWidth(0);
        tablaCarrito.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaCarrito.getColumnModel().getColumn(0).setWidth(0);
        
        JScrollPane scrollTabla = new JScrollPane(tablaCarrito);
        panelCentro.add(scrollTabla, BorderLayout.CENTER);
        
        // Botón Eliminar del carrito
        JPanel panelAccionesTabla = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelAccionesTabla.setOpaque(false);
        btnEliminarItem = new JButton("❌ Quitar Seleccionado");
        btnEliminarItem.setBackground(new Color(231, 76, 60));
        btnEliminarItem.setForeground(Color.WHITE);
        panelAccionesTabla.add(btnEliminarItem);
        panelCentro.add(panelAccionesTabla, BorderLayout.SOUTH);

        this.add(panelCentro, BorderLayout.CENTER);

        // ==========================================
        // 3. PANEL INFERIOR: TOTALES, IGV Y PAGO
        // ==========================================
        JPanel panelSur = new JPanel(new BorderLayout(15, 15));
        panelSur.setBackground(Color.WHITE);
        panelSur.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Izquierda: Método de Pago
        JPanel panelPago = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelPago.setOpaque(false);
        panelPago.add(new JLabel("MÉTODO DE PAGO:"));
        cmbMetodoPago = new JComboBox<>(new String[]{"EFECTIVO", "TARJETA", "YAPE / PLIN (QR)"});
        cmbMetodoPago.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelPago.add(cmbMetodoPago);
        panelSur.add(panelPago, BorderLayout.WEST);

        // Derecha: Cuadrícula de Totales (Subtotal, IGV, Total)
        JPanel panelTotales = new JPanel(new GridLayout(3, 2, 10, 5));
        panelTotales.setOpaque(false);

        JLabel lblTxtSub = new JLabel("SUBTOTAL: S/ ");
        lblTxtSub.setHorizontalAlignment(SwingConstants.RIGHT);
        lblSubtotal = new JLabel("0.00");
        lblSubtotal.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JLabel lblTxtIgv = new JLabel("IGV (18%): S/ ");
        lblTxtIgv.setHorizontalAlignment(SwingConstants.RIGHT);
        lblIgv = new JLabel("0.00");
        lblIgv.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JLabel lblTxtTotal = new JLabel("TOTAL A PAGAR: S/ ");
        lblTxtTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTxtTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        
        lblTotal = new JLabel("0.00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTotal.setForeground(new Color(192, 57, 43));

        panelTotales.add(lblTxtSub);
        panelTotales.add(lblSubtotal);
        panelTotales.add(lblTxtIgv);
        panelTotales.add(lblIgv);
        panelTotales.add(lblTxtTotal);
        panelTotales.add(lblTotal);

        panelSur.add(panelTotales, BorderLayout.CENTER);

        // Botón Registrar a la derecha del todo
        btnRegistrarVenta = new JButton("🛒 REGISTRAR VENTA");
        btnRegistrarVenta.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegistrarVenta.setBackground(new Color(41, 128, 185));
        btnRegistrarVenta.setForeground(Color.WHITE);
        btnRegistrarVenta.setPreferredSize(new Dimension(200, 50));
        panelSur.add(btnRegistrarVenta, BorderLayout.EAST);

        this.add(panelSur, BorderLayout.SOUTH);

        // --- EVENTOS ---
        configurarEventos();
    }

    private void initPersonalizado() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        lblFecha.setText("Fecha: " + sdf.format(new Date()));
        if (usuarioVendedor != null) {
            lblVendedor.setText("Vendedor: " + usuarioVendedor.getNombreCompleto());
        }
        txtRucCliente.requestFocus();
    }

    // --- LÓGICA DE CÁLCULO DE IGV ---
    private void calcularTotales() {
        double sumaItems = 0.0;
        for (int i = 0; i < tablaCarrito.getRowCount(); i++) {
            sumaItems += Double.parseDouble(tablaCarrito.getValueAt(i, 5).toString().replace(",", "."));
        }
        
        double subtotal = sumaItems;
        double igv = subtotal * 0.18;
        totalVentaCalculado = subtotal + igv; // El Total Final a cobrar
        
        lblSubtotal.setText(String.format("%.2f", subtotal));
        lblIgv.setText(String.format("%.2f", igv));
        lblTotal.setText(String.format("%.2f", totalVentaCalculado));
    }

    private void limpiarZonaProducto() {
        productoSeleccionadoActual = null;
        txtBuscarProducto.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        spnCantidad.setValue(1);
        txtBuscarProducto.requestFocus();
    }

    private void limpiarVentaCompleta() {
        clienteActual = null;
        txtRucCliente.setText("");
        txtNombreCliente.setText("");
        modeloTablaCarrito.setRowCount(0);
        cmbMetodoPago.setSelectedIndex(0);
        limpiarZonaProducto();
        calcularTotales();
        txtRucCliente.requestFocus();
    }

    private void configurarEventos() {
        // --- 0. CORTE DE CAJA ---
        btnArqueoCaja.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, 
                    "Ingrese el monto de CAJA INICIAL (sencillo) con el que empezó el turno (S/):", 
                    "Corte de Caja", JOptionPane.QUESTION_MESSAGE);
                    
            if (input != null && !input.trim().isEmpty()) {
                try {
                    double cajaInicial = Double.parseDouble(input.replace(",", "."));
                    int idVendedor = (usuarioVendedor != null) ? usuarioVendedor.getIdUsuario() : 1;
                    
                    double ventasEfectivo = ventaDAO.obtenerTotalEfectivoHoy(idVendedor);
                    double totalEsperado = cajaInicial + ventasEfectivo;
                    
                    String reporte = "=== REPORTE DE CAJA DIARIO ===\n\n"
                                   + "Vendedor: " + (usuarioVendedor != null ? usuarioVendedor.getNombreCompleto() : "Admin") + "\n"
                                   + "Caja Inicial (Base): S/ " + String.format("%.2f", cajaInicial) + "\n"
                                   + "Ventas en EFECTIVO hoy: S/ " + String.format("%.2f", ventasEfectivo) + "\n"
                                   + "---------------------------------\n"
                                   + "EFECTIVO ESPERADO EN CAJÓN: S/ " + String.format("%.2f", totalEsperado);
                                   
                    JOptionPane.showMessageDialog(this, reporte, "Resultado del Arqueo", JOptionPane.INFORMATION_MESSAGE);
                    
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Por favor, ingrese un monto numérico válido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // --- 1. BUSCAR CLIENTE ---
        btnBuscarCliente.addActionListener(e -> {
            String rucDni = txtRucCliente.getText().trim();
            if (rucDni.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese DNI/RUC.", "Atención", JOptionPane.WARNING_MESSAGE); return;
            }
            clienteActual = clienteDAO.buscarPorDniRuc(rucDni);
            if (clienteActual != null) {
                String nombreCompleto = clienteActual.getNombres() + " " + (clienteActual.getApellidos() != null ? clienteActual.getApellidos() : "");
                txtNombreCliente.setText(nombreCompleto);
                txtBuscarProducto.requestFocus();
            } else {
                JOptionPane.showMessageDialog(this, "Cliente no encontrado.", "Info", JOptionPane.INFORMATION_MESSAGE);
                txtNombreCliente.setText("");
            }
        });

        // --- 2. BUSCAR PRODUCTO ---
        btnBuscarProd.addActionListener(e -> buscarProducto());
        txtBuscarProducto.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) buscarProducto();
            }
        });

        // --- 3. AGREGAR AL CARRITO ---
        btnAgregarItem.addActionListener(e -> {
            if (productoSeleccionadoActual == null) {
                JOptionPane.showMessageDialog(this, "Busque y seleccione un producto primero.", "Atención", JOptionPane.WARNING_MESSAGE); return;
            }
            int cant = (Integer) spnCantidad.getValue();
            if (cant > productoSeleccionadoActual.getStockActual()) {
                JOptionPane.showMessageDialog(this, "¡Stock insuficiente!\nSolo quedan " + productoSeleccionadoActual.getStockActual() + " unidades.", "Stock", JOptionPane.ERROR_MESSAGE); return;
            }
            
            double subtotalItem = productoSeleccionadoActual.getPrecioVenta() * cant;
            modeloTablaCarrito.addRow(new Object[]{
                productoSeleccionadoActual.getIdProducto(),
                productoSeleccionadoActual.getCodigo(),
                productoSeleccionadoActual.getNombre(),
                cant,
                String.format("%.2f", productoSeleccionadoActual.getPrecioVenta()),
                String.format("%.2f", subtotalItem)
            });
            
            calcularTotales();
            limpiarZonaProducto();
        });

        // --- 4. ELIMINAR DEL CARRITO ---
        btnEliminarItem.addActionListener(e -> {
            int fila = tablaCarrito.getSelectedRow();
            if (fila >= 0) {
                modeloTablaCarrito.removeRow(fila);
                calcularTotales();
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla para quitarlo.", "Atención", JOptionPane.WARNING_MESSAGE);
            }
        });

        // --- 5. REGISTRAR VENTA Y GENERAR TICKET ---
        btnRegistrarVenta.addActionListener(e -> {
            if (modeloTablaCarrito.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "El carrito está vacío. Agregue productos primero.", "Atención", JOptionPane.WARNING_MESSAGE); return;
            }
            
            String metodoPago = cmbMetodoPago.getSelectedItem().toString();
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                "¿Registrar venta por S/ " + String.format("%.2f", totalVentaCalculado) + "\nMétodo: " + metodoPago + "?", 
                "Confirmar Venta", JOptionPane.YES_NO_OPTION);
                
            if (confirm == JOptionPane.YES_OPTION) {
                Venta nuevaVenta = new Venta();
                String numComprobante = "V-" + (System.currentTimeMillis() % 100000);
                nuevaVenta.setNumeroComprobante(numComprobante);
                nuevaVenta.setTotalVenta(totalVentaCalculado);
                nuevaVenta.setIdUsuario(usuarioVendedor != null ? usuarioVendedor.getIdUsuario() : 1);
                nuevaVenta.setIdCliente(clienteActual != null ? clienteActual.getIdCliente() : 0);
                
                // nuevaVenta.setMetodoPago(metodoPago); // Asegúrate de descomentar esto si agregaste setMetodoPago en la clase Venta
                
                List<DetalleVenta> detalles = new ArrayList<>();
                for (int i = 0; i < modeloTablaCarrito.getRowCount(); i++) {
                    DetalleVenta d = new DetalleVenta();
                    d.setIdProducto(Integer.parseInt(modeloTablaCarrito.getValueAt(i, 0).toString()));
                    d.setCantidad(Integer.parseInt(modeloTablaCarrito.getValueAt(i, 3).toString()));
                    d.setPrecioUnitario(Double.parseDouble(modeloTablaCarrito.getValueAt(i, 4).toString().replace(",", ".")));
                    d.setSubtotal(Double.parseDouble(modeloTablaCarrito.getValueAt(i, 5).toString().replace(",", ".")));
                    detalles.add(d);
                }
                nuevaVenta.setDetalles(detalles);

                if (ventaDAO.registrarVentaTransaccion(nuevaVenta)) {
                    
                    JOptionPane.showMessageDialog(this, "¡Venta registrada exitosamente!\nN° Comprobante: " + numComprobante, "Venta Completada", JOptionPane.INFORMATION_MESSAGE);
                    
                    // --- GENERAR Y MOSTRAR TICKET VIRTUAL ---
                    String nomCliente = (clienteActual != null) 
                            ? clienteActual.getNombres() + " " + (clienteActual.getApellidos() != null ? clienteActual.getApellidos() : "")
                            : "PÚBLICO GENERAL";

                    DefaultTableModel modeloCarrito = (DefaultTableModel) tablaCarrito.getModel();
                    
                    GeneradorTicketVirtual ticketVisor = new GeneradorTicketVirtual(
                            (Frame) SwingUtilities.getWindowAncestor(this), 
                            numComprobante, 
                            nomCliente, 
                            metodoPago, 
                            totalVentaCalculado, 
                            modeloCarrito
                    );
                    ticketVisor.setVisible(true); // Mostrará la ventanita del ticket para imprimir

                    limpiarVentaCompleta(); // Se limpia la pantalla después de cerrar el ticket
                    
                } else {
                    JOptionPane.showMessageDialog(this, "Hubo un error al registrar la venta en la Base de Datos.", "Error Fatal", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void buscarProducto() {
        String texto = txtBuscarProducto.getText().trim();
        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escriba el nombre o código del producto.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        List<Producto> resultados = productoDAO.buscarProductosParaVenta(texto);
        if (resultados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron productos con: " + texto, "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            limpiarZonaProducto();
        } else {
            productoSeleccionadoActual = resultados.get(0); 
            txtPrecio.setText(String.format("%.2f", productoSeleccionadoActual.getPrecioVenta()));
            txtStock.setText(String.valueOf(productoSeleccionadoActual.getStockActual()));
            spnCantidad.requestFocus();
        }
    }
}