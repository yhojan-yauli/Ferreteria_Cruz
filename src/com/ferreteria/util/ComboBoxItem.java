package com.ferreteria.util;

/**
 * CLASE AYUDANTE CRÍTICA.
 * Sirve para que los menús desplegables (JComboBox) puedan guardar dos datos a la vez:
 * 1. El ID (qué necesitamos para la base de datos).
 * 2. La Descripción (qué le mostramos al usuario).
 */
public class ComboBoxItem {
    private int id;
    private String description;

    // Constructor
    public ComboBoxItem(int id, String description) {
        this.id = id;
        this.description = description;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    // --- ¡MAGIA! ---
    // Java usa este método 'toString' para decidir qué texto mostrar en el menú desplegable.
    // Le decimos que muestre solo la descripción.
    @Override
    public String toString() {
        return description;
    }
    
    // Esto ayuda a Java a comparar si dos ítems son iguales basado en su ID
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ComboBoxItem) {
            ComboBoxItem other = (ComboBoxItem) obj;
            return this.id == other.id;
        }
        return false;
    }
}