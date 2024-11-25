// Clase Abstracta FiguraGeometrica
abstract class FiguraGeometrica {
    protected String color;

    public FiguraGeometrica(String color) {
        this.color = color;
    }

    // Métodos abstractos
    public abstract double calcularArea();
    public abstract double calcularPerimetro();
}

// Interfaz Dibujable
interface Dibujable {
    void dibujar();
}

// Clase Circulo que hereda de FiguraGeometrica e implementa Dibujable
class Circulo extends FiguraGeometrica implements Dibujable {
    private double radio;

    public Circulo(double radio, String color) {
        super(color);
        this.radio = radio;
    }

    @Override
    public double calcularArea() {
        return Math.PI * Math.pow(radio, 2);
    }

    @Override
    public double calcularPerimetro() {
        return 2 * Math.PI * radio;
    }

    @Override
    public void dibujar() {
        System.out.println("Dibujando un circulo de color " + color);
    }
}

// Clase Main para probar la implementación
public class Main {
    public static void main(String[] args) {
        // Crear un objeto Circulo
        Circulo circulo = new Circulo(5.0, "rojo");

        // Calcular y mostrar el área y perímetro
        System.out.println("Area del circulo: " + circulo.calcularArea());
        System.out.println("Perimetro del circulo: " + circulo.calcularPerimetro());

        // Dibujar el círculo
        circulo.dibujar();
    }
}
