package com.ferreteria.util;

import java.awt.*;
import java.awt.print.PrinterException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GeneradorTicketVirtual extends JDialog {

    private JTextArea txtTicket;

    public GeneradorTicketVirtual(Frame parent, String numTicket, String nombreCliente, String metodoPago, double total, DefaultTableModel carrito) {
        super(parent, "Ticket de Venta", true);
        initComponents(numTicket, nombreCliente, metodoPago, total, carrito);
    }

    private void initComponents(String numTicket, String nombreCliente, String metodoPago, double total, DefaultTableModel carrito) {
        this.setLayout(new BorderLayout());
        this.setSize(350, 550);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        // --- ÁREA DEL TICKET ---
        txtTicket = new JTextArea();
        // USAR FUENTE MONOESPACIADA ES OBLIGATORIO PARA QUE LAS COLUMNAS SE ALINEEN
        txtTicket.setFont(new Font("Monospaced", Font.PLAIN, 12)); 
        txtTicket.setEditable(false);
        txtTicket.setBackground(new Color(255, 255, 250)); // Color tipo papel térmico
        txtTicket.setMargin(new Insets(10, 10, 10, 10));

        // Construir el diseño del ticket
        txtTicket.setText(construirTextoTicket(numTicket, nombreCliente, metodoPago, total, carrito));

        JScrollPane scroll = new JScrollPane(txtTicket);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        this.add(scroll, BorderLayout.CENTER);

        // --- BOTÓN DE IMPRIMIR ---
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSur.setBackground(Color.WHITE);
        JButton btnImprimir = new JButton("🖨️ Imprimir Ticket");
        btnImprimir.setBackground(new Color(41, 128, 185));
        btnImprimir.setForeground(Color.WHITE);
        btnImprimir.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnImprimir.addActionListener(e -> {
            try {
                // Esto abre el cuadro de diálogo de impresión de Windows automáticamente
                boolean impreso = txtTicket.print();
                if (impreso) {
                    JOptionPane.showMessageDialog(this, "Enviado a la impresora correctamente.");
                }
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(this, "Error de impresión: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panelSur.add(btnImprimir);
        this.add(panelSur, BorderLayout.SOUTH);
    }

    private String construirTextoTicket(String numTicket, String cliente, String pago, double total, DefaultTableModel carrito) {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String fechaActual = sdf.format(new Date());

        sb.append("========================================\n");
        sb.append("            FERRETERIA CRUZ             \n");
        sb.append("      Mercado 24 de Junio - Puesto D107 \n");
        sb.append("         Villa El Salvador, Lima        \n");
        sb.append("========================================\n");
        sb.append("TICKET N°: ").append(numTicket).append("\n");
        sb.append("FECHA    : ").append(fechaActual).append("\n");
        sb.append("CLIENTE  : ").append(cliente != null && !cliente.isEmpty() ? cliente : "PÚBLICO GENERAL").append("\n");
        sb.append("PAGO     : ").append(pago).append("\n");
        sb.append("----------------------------------------\n");
        sb.append(String.format("%-4s %-18s %-7s %-7s\n", "CANT", "PRODUCTO", "P.UNIT", "SUBTOT"));
        sb.append("----------------------------------------\n");

        // Extraer los productos del carrito
        for (int i = 0; i < carrito.getRowCount(); i++) {
            String cant = carrito.getValueAt(i, 3).toString();
            String prod = carrito.getValueAt(i, 2).toString();
            String pUnit = carrito.getValueAt(i, 4).toString();
            String subT = carrito.getValueAt(i, 5).toString();

            // Truncar el nombre del producto si es muy largo para que no rompa el diseño
            if (prod.length() > 17) {
                prod = prod.substring(0, 17);
            }

            sb.append(String.format("%-4s %-18s %-7s %-7s\n", cant, prod, pUnit, subT));
        }

        sb.append("----------------------------------------\n");
        
        // Cálculos
        double igv = total * 0.18; // Asumiendo que el total es la suma final
        double subtotal = total - igv;

        sb.append(String.format("%-25s S/ %8.2f\n", "OP. GRAVADA:", subtotal));
        sb.append(String.format("%-25s S/ %8.2f\n", "IGV (18%):", igv));
        sb.append(String.format("%-25s S/ %8.2f\n", "TOTAL A PAGAR:", total));
        sb.append("========================================\n");
        sb.append("        ¡GRACIAS POR SU COMPRA!         \n");
        sb.append("  Revise su mercadería, no hay cambios  \n");
        sb.append("========================================\n");

        return sb.toString();
    }
}