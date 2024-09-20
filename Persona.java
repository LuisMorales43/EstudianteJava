public class Persona {
    private String nombre;
    private int edad;
    private String genero;
    private String nacionalidad;
    private String ocupacion;

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public int getEdad() {
        return edad;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getGenero() {
        return genero;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void comer(String comida) {
        System.out.println(nombre + " esta comiendo " + comida);
    }

    public void dormir(int horas) {
        System.out.println(nombre + " se va a dormir por " + horas + " horas.");
    }

    public void hacerEjercicio(String tipoEjercicio) {
        System.out.println(nombre + " esta haciendo " + tipoEjercicio + ".");
    }
}
