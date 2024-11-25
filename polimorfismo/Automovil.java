/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LAB;

/**
 *
 * @author ebers
 */
public class Automovil extends Vehiculo {
     public Automovil(String matrícula, String modelo) {
        super(matrícula, modelo);
    }

    // Implementación del método abstracto
    @Override
    public void conducir() {
        System.out.println("Automóvil " + getModelo() + " con matrícula " + getMatrícula() + " está siendo conducido.");
    }
}
