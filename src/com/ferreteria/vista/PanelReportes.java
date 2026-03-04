package com.ferreteria.vista;

import com.ferreteria.dao.VentaDAOImpl;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JDateChooser;

public class PanelReportes extends JPanel {

    // Componentes Visuales
    private JLabel lblTitulo;
    private JPanel panelBuscador;
    private JLabel lblFactura;
    private JTextField txtFactura;
    private JLabel lblFecha;
    private JDateChooser dateFecha;
    private JButton btnBuscarCodigo;
    private JButton btnBuscarFecha;
    private JButton btnRestaurar;
    private JButton btnAbrir;
    private JButton btnExportarExcel; // <--- NUEVO BOTÓN
    private JTable tablaFacturas;
    private JScrollPane scrollTabla;

    // Logica de Datos
    private VentaDAOImpl ventaDAO;

    public PanelReportes() {
        ventaDAO = new VentaDAOImpl();
        initComponents();
        configurarEstilo();
        cargarTabla(null, null); // Cargar todo al iniciar
    }

    private void initComponents() {
        // Configuramos este panel principal
        this.setLayout(new BorderLayout(10, 10)); 
        this.setBackground(new Color(245, 245, 245)); 
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); 

        // --- 1. TÍTULO SUPERIOR ---
        lblTitulo = new JLabel("HISTORIAL DE FACTURAS Y VENTAS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(40, 40, 40));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(lblTitulo, BorderLayout.NORTH);

        // --- 2. CONTENEDOR CENTRAL (Tabla + Buscador) ---
        JPanel panelCentral = new JPanel(new BorderLayout(0, 15));
        panelCentral.setOpaque(false);

        // --- 2.1 EL BUSCADOR (Elegante y organizado) ---
        panelBuscador = new JPanel(new GridBagLayout());
        panelBuscador.setBackground(Color.WHITE);
        panelBuscador.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), 
                "Criterios de Búsqueda", TitledBorder.LEFT, TitledBorder.TOP, 
                new Font("Segoe UI", Font.BOLD, 12), new Color(100, 100, 100)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 1: Buscar por Código
        lblFactura = new JLabel("Código de Factura:");
        lblFactura.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtFactura = new JTextField(15);
        btnBuscarCodigo = new JButton("Buscar Código");
        configurarBotonOscuro(btnBuscarCodigo);

        gbc.gridx = 0; gbc.gridy = 0; panelBuscador.add(lblFactura, gbc);
        gbc.gridx = 1; gbc.gridy = 0; panelBuscador.add(txtFactura, gbc);
        gbc.gridx = 2; gbc.gridy = 0; panelBuscador.add(btnBuscarCodigo, gbc);

        // Fila 2: Buscar por Fecha
        lblFecha = new JLabel("Fecha de Venta:");
        lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateFecha = new JDateChooser();
        dateFecha.setPreferredSize(new Dimension(150, 25));
        btnBuscarFecha = new JButton("Buscar Fecha");
        configurarBotonOscuro(btnBuscarFecha);

        gbc.gridx = 0; gbc.gridy = 1; panelBuscador.add(lblFecha, gbc);
        gbc.gridx = 1; gbc.gridy = 1; panelBuscador.add(dateFecha, gbc);
        gbc.gridx = 2; gbc.gridy = 1; panelBuscador.add(btnBuscarFecha, gbc);

        panelCentral.add(panelBuscador, BorderLayout.NORTH);

        // --- 2.2 LA TABLA ---
        String[] titulos = {"Código Factura", "RUC/ID Cliente", "Código Empleado", "Fecha", "Total (S/)"};
        DefaultTableModel modelo = new DefaultTableModel(null, titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        tablaFacturas = new JTable(modelo);
        tablaFacturas.setRowHeight(25);
        tablaFacturas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        scrollTabla = new JScrollPane(tablaFacturas);
        panelCentral.add(scrollTabla, BorderLayout.CENTER);

        this.add(panelCentral, BorderLayout.CENTER);

        // --- 3. BOTONES INFERIORES ---
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelInferior.setOpaque(false);
        
        btnRestaurar = new JButton("Restaurar Todo");
        btnRestaurar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        btnAbrir = new JButton("Abrir Detalle");
        btnAbrir.setFont(new Font("Segoe UI", Font.BOLD, 12));

        // --- BOTÓN EXCEL (Estilo Verde) ---
        btnExportarExcel = new JButton("📊 Exportar a Excel");
        btnExportarExcel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnExportarExcel.setBackground(new Color(39, 174, 96));
        btnExportarExcel.setForeground(Color.WHITE);
        
        panelInferior.add(btnExportarExcel); // Añadido a la interfaz
        panelInferior.add(btnRestaurar);
        panelInferior.add(btnAbrir);
        
        this.add(panelInferior, BorderLayout.SOUTH);

        // --- 4. AÑADIR LOS EVENTOS (Listeners) ---
        agregarEventos();
    }

    private void configurarBotonOscuro(JButton btn) {
        btn.setBackground(new Color(40, 40, 40));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
    }

    private void configurarEstilo() {
        // Método por si quieres hacer más ajustes de diseño después
    }

    // ==================================================================================
    // LÓGICA DE NEGOCIO Y EVENTOS
    // ==================================================================================

    private void cargarTabla(String codigo, String fecha) {
        DefaultTableModel modelo = (DefaultTableModel) tablaFacturas.getModel();
        modelo.setRowCount(0); // Limpiar

        List<Object[]> facturas = ventaDAO.listarReporteVentas(codigo, fecha);
        
        for (Object[] fac : facturas) {
            modelo.addRow(fac);
        }
    }

    private void agregarEventos() {
        
        // --- Evento: Buscar por Código ---
        btnBuscarCodigo.addActionListener(e -> {
            String codigo = txtFactura.getText().trim();
            if (codigo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Escriba el código de la factura a buscar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            cargarTabla(codigo, null);
            if(dateFecha != null) {
                dateFecha.setDate(null);
            }
        });

        // --- Evento: Buscar por Fecha ---
        btnBuscarFecha.addActionListener(e -> {
            if (dateFecha.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione una fecha del calendario.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String fechaFormateada = sdf.format(dateFecha.getDate());
            
            cargarTabla(null, fechaFormateada);
            txtFactura.setText(""); 
        });

        // --- Evento: Restaurar ---
        btnRestaurar.addActionListener(e -> {
            txtFactura.setText("");
            if(dateFecha != null) {
                dateFecha.setDate(null);
            }
            cargarTabla(null, null); // Vuelve a mostrar todo el historial
        });

        // --- Evento: Exportar a Excel ---
        btnExportarExcel.addActionListener(e -> {
            if (tablaFacturas.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No hay datos en la tabla para exportar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Llama a la clase utilitaria que creamos. Asegúrate de que el paquete sea el correcto.
            com.ferreteria.util.ExportarExcel.exportar(tablaFacturas, "Reporte_Ventas");
        });

        // --- Evento: Abrir ---
        btnAbrir.addActionListener(e -> {
            int filaSeleccionada = tablaFacturas.getSelectedRow();

            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione una factura de la tabla para abrir.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Obtenemos el código y el total de la tabla (Columna 0 y Columna 4)
            String codigoFactura = tablaFacturas.getValueAt(filaSeleccionada, 0).toString();
            String total = tablaFacturas.getValueAt(filaSeleccionada, 4).toString();

            String mensaje = "Has seleccionado abrir la factura: " + codigoFactura + "\n"
                           + "Por un total de: S/ " + total + "\n\n"
                           + "Esta funcionalidad está lista para ser conectada con\n"
                           + "la generación de PDF o la vista detallada.";
                           
            JOptionPane.showMessageDialog(this, mensaje, "Factura Seleccionada", JOptionPane.INFORMATION_MESSAGE);
        });
    }
}