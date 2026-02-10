/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ferreteriacruz.entity;

/**
 *
 * @author USUARIO
 */

public class Marca {

    private int idMarca;
    private String nombre;
    private boolean activo;

    public Marca() {
    }

    public Marca(int idMarca, String nombre, boolean activo) {
        this.idMarca = idMarca;
        this.nombre = nombre;
        this.activo = activo;
    }

    public int getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
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
