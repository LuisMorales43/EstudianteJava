/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LAB;

/**
 *
 * @author ebers
 */
public class Camion extends Vehiculo implements Carga {
    public Camion(String matrícula, String modelo) {
        super(matrícula, modelo);
    }

    // Implementación del método abstracto
    @Override
    public void conducir() {
        System.out.println("Camión " + getModelo() + " con matrícula " + getMatrícula() + " está siendo conducido.");
    }

    // Implementación de los métodos de la interfaz
    @Override
    public void cargar() {
        System.out.println("Camión " + getModelo() + " está siendo cargado.");
    }

    @Override
    public void descargar() {
        System.out.println("Camión " + getModelo() + " está siendo descargado.");
    }
}
