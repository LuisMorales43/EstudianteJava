import java.util.ArrayList;

// Clase Libro que representa los libros en la biblioteca
class Libro {
    private String titulo;
    private String autor;
    private String isbn;

    // Constructor
    public Libro(String titulo, String autor, String isbn) {
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
    }

    // Métodos get para acceder a los atributos
    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getIsbn() {
        return isbn;
    }

    // Método que simula la persistencia de un libro en una base de datos
    public void guardarEnBD() {
        // Lógica para guardar en base de datos (simulada en consola)
        System.out.println("Guardando libro en base de datos: " + this.titulo);
    }
}

// Clase Biblioteca que contiene una lista de libros
class Biblioteca {
    private ArrayList<Libro> libros;

    // Constructor
    public Biblioteca() {
        libros = new ArrayList<>();
    }

    // Método para agregar un libro
    public void agregarLibro(Libro libro) {
        libros.add(libro);
    }

    // Método que simula el proceso de persistir todos los libros de la biblioteca
    public void guardarLibrosEnBD() {
        for (Libro libro : libros) {
            libro.guardarEnBD();
        }
    }
}

public class SistemaBiblioteca {
    public static void main(String[] args) {
        // Crear instancias de libros
        Libro libro1 = new Libro("El Quijote", "Miguel de Cervantes", "1234567890");
        Libro libro2 = new Libro("1984", "George Orwell", "0987654321");

        // Crear la biblioteca
        Biblioteca biblioteca = new Biblioteca();

        // Agregar libros a la biblioteca
        biblioteca.agregarLibro(libro1);
        biblioteca.agregarLibro(libro2);

        // Guardar libros en la base de datos (simulada)
        biblioteca.guardarLibrosEnBD();
    }
}
