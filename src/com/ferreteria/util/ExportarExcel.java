package com.ferreteria.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExportarExcel {

    public static void exportar(JTable tabla, String nombreHoja) {
        // 1. Abrir ventana para que el dueño elija dónde guardar
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Reporte Excel");
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos de Excel (*.xlsx)", "xlsx");
        fileChooser.setFileFilter(filtro);

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File archivoGuardar = fileChooser.getSelectedFile();
            
            // Asegurar que termine en .xlsx
            String ruta = archivoGuardar.getAbsolutePath();
            if (!ruta.toLowerCase().endsWith(".xlsx")) {
                ruta += ".xlsx";
            }

            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet hoja = workbook.createSheet(nombreHoja);

                // 2. Crear estilo para la Cabecera (Negrita y fondo gris)
                CellStyle estiloCabecera = workbook.createCellStyle();
                Font fuenteCabecera = workbook.createFont();
                fuenteCabecera.setBold(true);
                estiloCabecera.setFont(fuenteCabecera);
                estiloCabecera.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                estiloCabecera.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                // 3. Imprimir los títulos de las columnas
                Row filaCabecera = hoja.createRow(0);
                for (int c = 0; c < tabla.getColumnCount(); c++) {
                    Cell celda = filaCabecera.createCell(c);
                    celda.setCellValue(tabla.getColumnName(c));
                    celda.setCellStyle(estiloCabecera);
                }

                // 4. Imprimir los datos de la tabla
                for (int r = 0; r < tabla.getRowCount(); r++) {
                    Row filaDatos = hoja.createRow(r + 1);
                    for (int c = 0; c < tabla.getColumnCount(); c++) {
                        Cell celda = filaDatos.createCell(c);
                        Object valor = tabla.getValueAt(r, c);
                        if (valor != null) {
                            celda.setCellValue(valor.toString());
                        }
                    }
                }

                // 5. Autoajustar el ancho de las columnas
                for (int c = 0; c < tabla.getColumnCount(); c++) {
                    hoja.autoSizeColumn(c);
                }

                // 6. Escribir el archivo en el disco
                try (FileOutputStream out = new FileOutputStream(ruta)) {
                    workbook.write(out);
                }

                JOptionPane.showMessageDialog(null, "¡Reporte exportado exitosamente a Excel!", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error al guardar el archivo. Verifique que no esté abierto por otro programa.\n" + e.getMessage(), "Error Fatal", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}