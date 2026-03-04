package com.ferreteria.vista;

import com.ferreteria.dao.ProductoDAOImpl;
import com.ferreteria.modelo.Producto;
import com.ferreteria.util.ComboBoxItem;
import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class PanelListadoProductos1 extends JPanel {

    private ProductoDAOImpl productoDAO = new ProductoDAOImpl();

    // --- Componentes Visuales ---
    private JComboBox<String> cmbCategoria;
    private JComboBox<String> cmbMarca;
    private JComboBox<String> cmbPrecio;
    private JComboBox<String> cmbStock;
    
    private JButton btnFiltroCategoria, btnFiltroMarca, btnFiltroPrecio, btnFiltroStock, btnRestaurar;
    private JTable tablaCatalogo;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscarRapido;

    public PanelListadoProductos1() {
        initComponents();
        configurarTabla();
        cargarDatosIniciales();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout(15, 15));
        this.setBackground(new Color(245, 245, 245));
        this.setBorder(new EmptyBorder(20, 20, 20, 20));

        // ==========================================
        // 1. CABECERA: TÍTULO Y BÚSQUEDA RÁPIDA
        // ==========================================
        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.setOpaque(false);
        
        JLabel lblTitulo = new JLabel("CATÁLOGO DE PRODUCTOS - FERRETERÍA CRUZ");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(41, 128, 185));
        panelNorte.add(lblTitulo, BorderLayout.WEST);

        // Búsqueda rápida por texto
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBusqueda.setOpaque(false);
        panelBusqueda.add(new JLabel("🔍 Búsqueda Rápida:"));
        txtBuscarRapido = new JTextField(20);
        
        // Buscador en tiempo real
        txtBuscarRapido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtrarPorTexto(txtBuscarRapido.getText());
            }
        });
        panelBusqueda.add(txtBuscarRapido);
        panelNorte.add(panelBusqueda, BorderLayout.EAST);

        this.add(panelNorte, BorderLayout.NORTH);

        // ==========================================
        // 2. PANEL DE FILTROS AVANZADOS
        // ==========================================
        JPanel panelFiltros = new JPanel(new GridLayout(1, 4, 15, 0));
        panelFiltros.setBackground(Color.WHITE);
        panelFiltros.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), 
            "Filtros Avanzados", TitledBorder.LEFT, TitledBorder.TOP, 
            new Font("Segoe UI", Font.BOLD, 12)));

        // Creador de bloques de filtro
        panelFiltros.add(crearBloqueFiltro("Categoría:", cmbCategoria = new JComboBox<>(), btnFiltroCategoria = new JButton("Filtrar")));
        panelFiltros.add(crearBloqueFiltro("Marca:", cmbMarca = new JComboBox<>(), btnFiltroMarca = new JButton("Filtrar")));
        panelFiltros.add(crearBloqueFiltro("Precio:", cmbPrecio = new JComboBox<>(), btnFiltroPrecio = new JButton("Filtrar")));
        panelFiltros.add(crearBloqueFiltro("Stock:", cmbStock = new JComboBox<>(), btnFiltroStock = new JButton("Filtrar")));

        // ==========================================
        // 3. CENTRO: TABLA DE PRODUCTOS
        // ==========================================
        JPanel panelCentro = new JPanel(new BorderLayout(0, 10));
        panelCentro.setOpaque(false);
        panelCentro.add(panelFiltros, BorderLayout.NORTH);

        String[] titulos = {"Código", "Categoría (ID)", "Marca", "Nombre / Modelo", "Precio (S/)", "Stock", "Ubicación"};
        modeloTabla = new DefaultTableModel(null, titulos) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        tablaCatalogo = new JTable(modeloTabla);
        tablaCatalogo.setRowHeight(28);
        tablaCatalogo.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaCatalogo.getTableHeader().setBackground(new Color(41, 128, 185));
        tablaCatalogo.getTableHeader().setForeground(Color.WHITE);
        
        // Ajustar anchos de columnas
        tablaCatalogo.getColumnModel().getColumn(3).setPreferredWidth(250); // Nombre más ancho

        JScrollPane scrollTabla = new JScrollPane(tablaCatalogo);
        panelCentro.add(scrollTabla, BorderLayout.CENTER);

        this.add(panelCentro, BorderLayout.CENTER);

        // ==========================================
        // 4. SUR: BOTÓN RESTAURAR
        // ==========================================
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSur.setOpaque(false);
        
        btnRestaurar = new JButton("🔄 Limpiar Filtros y Mostrar Todo");
        btnRestaurar.setBackground(new Color(231, 76, 60));
        btnRestaurar.setForeground(Color.WHITE);
        btnRestaurar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panelSur.add(btnRestaurar);
        
        this.add(panelSur, BorderLayout.SOUTH);

        // Asignar Eventos
        asignarEventosFiltros();
    }

    // --- Método auxiliar para crear diseño de filtros ---
    private JPanel crearBloqueFiltro(String etiqueta, JComboBox<String> combo, JButton boton) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        panel.add(new JLabel(etiqueta), BorderLayout.NORTH);
        panel.add(combo, BorderLayout.CENTER);
        boton.setBackground(new Color(52, 152, 219));
        boton.setForeground(Color.WHITE);
        panel.add(boton, BorderLayout.SOUTH);
        return panel;
    }

    private void configurarTabla() {
        // Ya configurada en initComponents
    }

    private void cargarDatosIniciales() {
        // 1. Categorías
        List<ComboBoxItem> listaCat = productoDAO.listarCategoriasCombo();
        cmbCategoria.removeAllItems();
        cmbCategoria.addItem("Todas las categorías"); 
        for (ComboBoxItem item : listaCat) {
            cmbCategoria.addItem(item.toString());
        }

        // 2. Marcas (Extracción automática)
        cmbMarca.removeAllItems();
        cmbMarca.addItem("Todas las marcas");
        List<Producto> todos = productoDAO.listarTodos(false);
        Set<String> marcasUnicas = new HashSet<>();
        for (Producto p : todos) {
            if (p.getMarca() != null && !p.getMarca().trim().isEmpty()) {
                marcasUnicas.add(p.getMarca().toUpperCase()); 
            }
        }
        for (String marca : marcasUnicas) {
            cmbMarca.addItem(marca);
        }

        // 3. Precios
        cmbPrecio.removeAllItems();
        cmbPrecio.addItem("Todos los precios");
        cmbPrecio.addItem("Menor a S/50");
        cmbPrecio.addItem("S/50 a S/100");
        cmbPrecio.addItem("Mayor a S/100");

        // 4. Stock
        cmbStock.removeAllItems();
        cmbStock.addItem("Todo el inventario");
        cmbStock.addItem("En Stock (>0)");
        cmbStock.addItem("Sin Stock (0)");

        cargarTablaTodos();
    }

    private void cargarTablaTodos() {
        llenarTabla(productoDAO.listarTodos(false));
    }

    private void llenarTabla(List<Producto> lista) {
        modeloTabla.setRowCount(0); 
        for (Producto p : lista) {
            Object[] fila = {
                p.getCodigo(),
                p.getIdCategoria(), 
                p.getMarca(),
                p.getNombre(),
                String.format("%.2f", p.getPrecioVenta()),
                p.getStockActual(),
                p.getUbicacion()
            };
            modeloTabla.addRow(fila);
        }
    }

    // ==========================================
    // LÓGICA DE FILTROS
    // ==========================================
    private void asignarEventosFiltros() {
        
        // Filtro Categoría
        btnFiltroCategoria.addActionListener(e -> {
            int index = cmbCategoria.getSelectedIndex();
            if (index > 0) {
                // Asume que el index del combo coincide con el ID de BD (Mejorar en prod)
                llenarTabla(productoDAO.listarPorCategoria(index));
            } else {
                cargarTablaTodos();
            }
        });

        // Filtro Marca
        btnFiltroMarca.addActionListener(e -> {
            int index = cmbMarca.getSelectedIndex();
            if (index > 0) {
                String marcaElegida = cmbMarca.getSelectedItem().toString().toLowerCase();
                List<Producto> filtrados = new java.util.ArrayList<>();
                for (Producto p : productoDAO.listarTodos(false)) {
                    String m = p.getMarca() != null ? p.getMarca().toLowerCase() : "";
                    if (m.equals(marcaElegida)) filtrados.add(p);
                }
                llenarTabla(filtrados);
            } else {
                cargarTablaTodos();
            }
        });

        // Filtro Precio
        btnFiltroPrecio.addActionListener(e -> {
            int index = cmbPrecio.getSelectedIndex();
            if (index > 0) {
                List<Producto> filtrados = new java.util.ArrayList<>();
                for (Producto p : productoDAO.listarTodos(false)) {
                    double precio = p.getPrecioVenta();
                    if (index == 1 && precio < 50.0) filtrados.add(p);
                    else if (index == 2 && precio >= 50.0 && precio <= 100.0) filtrados.add(p);
                    else if (index == 3 && precio > 100.0) filtrados.add(p);
                }
                llenarTabla(filtrados);
            } else {
                cargarTablaTodos();
            }
        });

        // Filtro Stock
        btnFiltroStock.addActionListener(e -> {
            int index = cmbStock.getSelectedIndex();
            if (index > 0) {
                List<Producto> filtrados = new java.util.ArrayList<>();
                for (Producto p : productoDAO.listarTodos(false)) {
                    int stock = p.getStockActual();
                    if (index == 1 && stock > 0) filtrados.add(p);
                    else if (index == 2 && stock == 0) filtrados.add(p);
                }
                llenarTabla(filtrados);
            } else {
                cargarTablaTodos();
            }
        });

        // Botón Restaurar Todo
        btnRestaurar.addActionListener(e -> {
            cmbCategoria.setSelectedIndex(0);
            cmbMarca.setSelectedIndex(0);
            cmbPrecio.setSelectedIndex(0);
            cmbStock.setSelectedIndex(0);
            txtBuscarRapido.setText("");
            cargarTablaTodos();
        });
    }

    // Buscador en tiempo real por texto (Nombre o Código)
    private void filtrarPorTexto(String texto) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloTabla);
        tablaCatalogo.setRowSorter(sorter);
        if (texto.trim().length() == 0) {
            sorter.setRowFilter(null);
        } else {
            // Busca en todas las columnas, ignorando mayúsculas/minúsculas
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
        }
    }
}