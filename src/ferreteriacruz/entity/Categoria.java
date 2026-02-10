/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ferreteriacruz.entity;

/**
 *
 * @author USUARIO
 */

public class Categoria {

    private int idCategoria;
    private String nombre;
    private boolean activo;

    public Categoria() {
    }

    public Categoria(int idCategoria, String nombre, boolean activo) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.activo = activo;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    
@Override
public String toString() {
    return nombre;
}
    
}