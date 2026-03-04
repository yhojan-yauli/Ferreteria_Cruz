package modelo;

public class Alumno {

    private int codAlumno;
    private String nombres;
    private String apellidos;
    private String dni;
    private String password; 
    private int edad;
    private int celular;
    private int estado;
 

    public Alumno() {
    }

   public Alumno(int codAlumno, String nombres, String apellidos, String dni, String password, int edad, int celular, int estado) {
    this.codAlumno = codAlumno;
    this.nombres = nombres;
    this.apellidos = apellidos;
    this.dni = dni;
    this.password = password; //NUEVA INCORPORACIÓN
    this.edad = edad;
    this.celular = celular;
    this.estado = estado;
}
   
   
   // Constructor SIN password (para compatibilidad con código existente)
public Alumno(int codAlumno, String nombres, String apellidos, String dni, int edad, int celular, int estado) {
    this.codAlumno = codAlumno;
    this.nombres = nombres;
    this.apellidos = apellidos;
    this.dni = dni;
    this.password = null;  // Password null por defecto
    this.edad = edad;
    this.celular = celular;
    this.estado = estado;
}

    public int getCodAlumno() {
        return codAlumno;
    }

    public void setCodAlumno(int codAlumno) {
        this.codAlumno = codAlumno;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }
    
        public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public int getCelular() {
        return celular;
    }

    public void setCelular(int celular) {
        this.celular = celular;
    }

   
    
    
    public int getEstado() {
        return estado;
    }

    
    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getEstadoTexto() {
        return switch (estado) {
            case 0 ->
                "Registrado";
            case 1 ->
                "Matriculado";
            case 2 ->
                "Retirado";
            default ->
                "Desconocido";
        };
    }
}
