package com.ferreteria.modelo;

public class Usuario {
    private int idUsuario;
    private String nombreCompleto;
    private String username;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    private int idRol; // 1 para Admin, 2 para Vendedor según tu BD
    private int activo;

    // --- MÉTODOS PARA SOLUCIONAR EL ERROR DE COMPILACIÓN ---
    
    // Este método permite al MainFrame saber el rol como texto
    public String getRol() {
        return (this.idRol == 1) ? "Administrador" : "Vendedor";
    }

    // Este método soluciona el error en la línea 294 del MainFrame
    public void setRol(String rolNombre) {
        if (rolNombre.equalsIgnoreCase("Administrador")) {
            this.idRol = 1;
        } else {
            this.idRol = 2;
        }
    }

    // Getters y Setters básicos
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public int getIdRol() { return idRol; }
    public void setIdRol(int idRol) { this.idRol = idRol; }
    // En com.ferreteria.modelo.Usuario
public int getActivo() {
    return activo;
}

public void setActivo(int activo) {
    this.activo = activo;
}
}