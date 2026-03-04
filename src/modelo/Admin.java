
package modelo;

public class Admin {
    private int id;
    private String usuario;
    private String password;
    private String nombre;

    public Admin() {
    }

    public Admin(int id, String usuario, String password, String nombre) {
        this.id = id;
        this.usuario = usuario;
        this.password = password;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
     
}
