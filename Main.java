public class Main {
    public static void main(String[] args) {
        // Crear tres estudiantes
        Estudiante estudiante1 = new Estudiante("Carlos", 19, "D98765");
        Estudiante estudiante2 = new Estudiante("Ana", 20, "A12345");
        Estudiante estudiante3 = new Estudiante("Luis", 21, "L54321");

        // Uso del método setGet para modificar y mostrar atributos
        estudiante1.setGet("nombre", "Carlos");
        estudiante1.setGet("edad", "19");
        estudiante1.setGet("matricula", "D98765");

        estudiante2.setGet("nombre", "Ana");
        estudiante2.setGet("edad", "20");
        estudiante2.setGet("matricula", "A12345");

        estudiante3.setGet("nombre", "Luis");
        estudiante3.setGet("edad", "21");
        estudiante3.setGet("matricula", "L54321");

        // Mostrar todos los datos
        estudiante1.mostrarDatos();
        estudiante2.mostrarDatos();
        estudiante3.mostrarDatos();
    }
}
