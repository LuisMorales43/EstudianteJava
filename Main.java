public class Main {
    public static void main(String[] args) {
        // Crear tres objetos de tipo Persona
        Persona persona1 = new Persona();
        persona1.setNombre("Ana");
        persona1.setEdad(25);
        persona1.setGenero("Femenino");
        persona1.setNacionalidad("Mexicana");
        persona1.setOcupacion("Estudiante");

        Persona persona2 = new Persona();
        persona2.setNombre("Luis");
        persona2.setEdad(30);
        persona2.setGenero("Masculino");
        persona2.setNacionalidad("Mexicano");
        persona2.setOcupacion("Ingeniero");

        Persona persona3 = new Persona();
        persona3.setNombre("Carlos");
        persona3.setEdad(28);
        persona3.setGenero("Masculino");
        persona3.setNacionalidad("Argentino");
        persona3.setOcupacion("Disenador");

        //Aqui se im los datos de cada persona
        System.out.println("Nombre: " + persona1.getNombre());
        System.out.println("Edad: " + persona1.getEdad());
        System.out.println("Genero: " + persona1.getGenero());
        System.out.println("Nacionalidad: " + persona1.getNacionalidad());
        System.out.println("Ocupacion: " + persona1.getOcupacion());
        persona1.comer("pizza");
        persona1.dormir(8);
        persona1.hacerEjercicio("tarea");
        System.out.println();

        System.out.println("Nombre: " + persona2.getNombre());
        System.out.println("Edad: " + persona2.getEdad());
        System.out.println("Genero: " + persona2.getGenero());
        System.out.println("Nacionalidad: " + persona2.getNacionalidad());
        System.out.println("Ocupacion: " + persona2.getOcupacion());
        persona2.comer("hamburguesa");
        persona2.dormir(7);
        persona2.hacerEjercicio("ejercicio");
        System.out.println();

        System.out.println("Nombre: " + persona3.getNombre());
        System.out.println("Edad: " + persona3.getEdad());
        System.out.println("Genero: " + persona3.getGenero());
        System.out.println("Nacionalidad: " + persona3.getNacionalidad());
        System.out.println("Ocupacion: " + persona3.getOcupacion());
        persona3.comer("ensalada");
        persona3.dormir(6);
        persona3.hacerEjercicio("pesas");
        System.out.println();
    }
}
