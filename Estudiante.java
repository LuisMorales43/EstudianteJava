class Estudiante {
    private String nombre;
    private int edad;
    private String matricula;

    // Constructor
    public Estudiante(String nombre, int edad, String matricula) {
        this.nombre = nombre;
        this.edad = edad;
        this.matricula = matricula;
    }

    // Métodos set y get combinados en un único método setGet
    public void setGet(String atributo, String valor) {
        switch (atributo.toLowerCase()) {
            case "nombre":
                this.nombre = valor;
                break;
            case "edad":
                try {
                    this.edad = Integer.parseInt(valor);
                } catch (NumberFormatException e) {
                    System.out.println("Formato de edad inválido.");
                }
                break;
            case "matricula":
                this.matricula = valor;
                break;
            default:
                System.out.println("Atributo no reconocido.");
        }
    }

    // Método para mostrar los datos
    public void mostrarDatos() {
        System.out.println("Nombre: " + this.nombre);
        System.out.println("Edad: " + this.edad);
        System.out.println("Matrícula: " + this.matricula);
    }
}
