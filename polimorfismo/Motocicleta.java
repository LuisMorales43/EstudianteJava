/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LAB;

/**
 *
 * @author ebers
 */
public class Motocicleta extends Vehiculo implements Carga {
    public Motocicleta(String matrícula, String modelo) {
        super(matrícula, modelo);
    }

    // Implementación del método abstracto
    @Override
    public void conducir() {
        System.out.println("Motocicleta " + getModelo() + " con matrícula " + getMatrícula() + " está siendo conducida.");
    }

    // Implementación de los métodos de la interfaz
    @Override
    public void cargar() {
        System.out.println("Motocicleta " + getModelo() + " está siendo cargada.");
    }

    @Override
    public void descargar() {
        System.out.println("Motocicleta " + getModelo() + " está siendo descargada.");
    }
}
