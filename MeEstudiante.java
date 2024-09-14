class MeEstudiante {
    private String nombre;
    private int edad;
    private String matricula;

    //constructor
    public MeEstudiante(String nombre, int edad, String matricula) {
        this.nombre = nombre;
        this.edad = edad;
        this.matricula = matricula;
    }

    //metodos set
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    //metodos get
    public String getNombre() {
        return nombre;
    }

    public int getEdad() {
        return edad;
    }

    public String getMatricula() {
        return matricula;
    }

    //imprimir datos
    public void imprimirDatos() {
        System.out.println("Nombre: " + getNombre());
        System.out.println("Edad: " + getEdad());
        System.out.println("Matrícula: " + getMatricula());
    }
}

public class Main {
    public static void main(String[] args) {
        //creacion de tres estudiantes 
        MeEstudiante estudiante1 = new MeEstudiante("Luis", 20, "11234522");
        MeEstudiante estudiante2 = new MeEstudiante("Jose", 21, "11828383");
        MeEstudiante estudiante3 = new MeEstudiante("Mario", 22, "12565422");

        //mostrar datos
        estudiante1.imprimirDatos();
        estudiante2.imprimirDatos();
        estudiante3.imprimirDatos();
    }
}
