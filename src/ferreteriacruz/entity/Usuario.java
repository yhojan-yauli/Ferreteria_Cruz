package ferreteriacruz.entity;

public class Usuario {

    private int id;
    private String email;
    private String password;
    private String rol;

    public Usuario() {}

    public Usuario(int id, String email, String password, String rol) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.rol = rol;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}