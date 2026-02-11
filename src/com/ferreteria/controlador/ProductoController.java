package com.ferreteria.controlador;

import com.ferreteria.dao.ProductoDAO;
import com.ferreteria.dao.ProductoDAOImpl;
import com.ferreteria.modelo.Producto;
import com.ferreteria.vista.PanelInventario;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ProductoController implements ActionListener {

    // --- ATRIBUTOS ---
    private final PanelInventario vista; // Referencia a la pantalla
    private final ProductoDAO dao;       // Referencia al objeto que habla con la BD
    private DefaultTableModel modeloTabla; // El modelo que maneja los datos visuales de la JTable
    private int idProductoSeleccionado = 0; // 0 significa que estamos creando uno nuevo, >0 que estamos editando

    // --- CONSTRUCTOR ---
    // Este constructor se ejecuta apenas se abre el panel
    public ProductoController(PanelInventario vista) {
        this.vista = vista;
        this.dao = new ProductoDAOImpl(); // Preparamos el DAO
        
        // 1. Configurar la tabla visualmente
        inicializarTabla();
        // 2. Traer los datos de la base de datos
        llenarTabla();
        
        // 3. Poner "orejas" a los botones para que escuchen los clics
        this.vista.btnGuardar.addActionListener(this);
        this.vista.btnLimpiar.addActionListener(this);
        // TODO: Más adelante agregaremos el listener para eliminar
        
        // 4. Poner "oreja" a la tabla para detectar cuando seleccionan una fila
        this.vista.tablaProductos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarProductoDeTabla();
            }
        });
    }

    // --- MÉTODOS DE AYUDA ---

    // Define las columnas que tendrá la tabla
    private void inicializarTabla() {
        modeloTabla = new DefaultTableModel() {
            // Hacemos que las celdas no sean editables directamente con doble clic
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modeloTabla.addColumn("ID (Oculto)");
        modeloTabla.addColumn("Código");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Marca");
        modeloTabla.addColumn("Precio");
        modeloTabla.addColumn("Stock");
        this.vista.tablaProductos.setModel(modeloTabla);
        
        // Opcional: Ocultar la columna ID (la primera) porque al usuario no le interesa verla
        this.vista.tablaProductos.getColumnModel().getColumn(0).setMinWidth(0);
        this.vista.tablaProductos.getColumnModel().getColumn(0).setMaxWidth(0);
        this.vista.tablaProductos.getColumnModel().getColumn(0).setWidth(0);
    }

    // Va a la base de datos y pinta las filas en la tabla
    public void llenarTabla() {
        modeloTabla.setRowCount(0); // Limpiar tabla anterior
        
        List<Producto> lista = dao.listarTodos(false); // Traer solo activos
        
        for (Producto p : lista) {
            Object[] fila = new Object[6];
            fila[0] = p.getIdProducto(); // Dato clave para editar
            fila[1] = p.getCodigo();
            fila[2] = p.getNombre();
            fila[3] = p.getMarca();
            fila[4] = p.getPrecioVenta();
            fila[5] = p.getStockActual();
            modeloTabla.addRow(fila);
        }
    }

    // --- MÉTODOS QUE REACCIONAN A EVENTOS ---

    // Se ejecuta cuando se hace clic en un botón
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnGuardar) {
            procesarGuardado();
        } else if (e.getSource() == vista.btnLimpiar) {
            limpiarFormulario();
        }
    }

    // Lógica para guardar o actualizar
    private void procesarGuardado() {
        // 1. Validar campos vacíos
        if (vista.txtCodigo.getText().isEmpty() || vista.txtNombre.getText().isEmpty() || vista.txtPrecioVenta.getText().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Código, Nombre y Precio son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // 2. Crear el objeto con datos del formulario
            Producto p = new Producto();
            p.setCodigo(vista.txtCodigo.getText());
            p.setNombre(vista.txtNombre.getText());
            p.setMarca(vista.txtMarca.getText());
            p.setPrecioVenta(Double.parseDouble(vista.txtPrecioVenta.getText()));
            p.setStockMinimo(Integer.parseInt(vista.txtStockMinimo.getText()));

            boolean exito = false;

            if (idProductoSeleccionado == 0) {
                // --- MODO NUEVO PRODUCTO ---
                // El stock inicial solo se pone al crear. Luego se usa el módulo de almacén.
                p.setStockActual(Integer.parseInt(vista.txtStock.getText()));
                exito = dao.registrar(p);
                if (exito) JOptionPane.showMessageDialog(vista, "Producto registrado con éxito.");
            } else {
                // --- MODO ACTUALIZAR PRODUCTO ---
                p.setIdProducto(idProductoSeleccionado);
                // Nota: dao.modificar NO actualiza el stock actual por seguridad.
                exito = dao.modificar(p);
                if (exito) JOptionPane.showMessageDialog(vista, "Datos del producto actualizados.");
            }

            // 3. Si salió bien, refrescar todo
            if (exito) {
                limpiarFormulario();
                llenarTabla();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al guardar en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "El Precio y los Stocks deben ser números válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Cuando hacen clic en una fila de la tabla
    private void seleccionarProductoDeTabla() {
        int fila = vista.tablaProductos.getSelectedRow();
        if (fila >= 0) {
            // Guardamos el ID que está oculto en la columna 0
            idProductoSeleccionado = (int) vista.tablaProductos.getValueAt(fila, 0);
            
            // Pasamos los datos de la tabla a los campos de texto
            vista.txtCodigo.setText(vista.tablaProductos.getValueAt(fila, 1).toString());
            vista.txtNombre.setText(vista.tablaProductos.getValueAt(fila, 2).toString());
            vista.txtMarca.setText(vista.tablaProductos.getValueAt(fila, 3).toString());
            vista.txtPrecioVenta.setText(vista.tablaProductos.getValueAt(fila, 4).toString());
            vista.txtStock.setText(vista.tablaProductos.getValueAt(fila, 5).toString());
            // Nota: El stock mínimo no lo pusimos en la tabla por espacio, así que no se cargará aquí por ahora.
            
            // Cambios visuales para indicar modo edición
            vista.btnGuardar.setText("Actualizar");
            vista.txtStock.setEditable(false); // No dejar editar stock aquí
            vista.txtCodigo.setEditable(false); // Es mejor no cambiar códigos primarios
        }
    }

    private void limpiarFormulario() {
        vista.txtCodigo.setText("");
        vista.txtNombre.setText("");
        vista.txtMarca.setText("");
        vista.txtPrecioVenta.setText("");
        vista.txtStock.setText("0");
        vista.txtStockMinimo.setText("5");
        
        // Resetear estado
        idProductoSeleccionado = 0;
        vista.btnGuardar.setText("Guardar");
        vista.txtStock.setEditable(true);
        vista.txtCodigo.setEditable(true);
        vista.tablaProductos.clearSelection();
    }
}