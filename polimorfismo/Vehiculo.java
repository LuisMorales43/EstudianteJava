/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LAB;

/**
 *
 * @author ebers
 */
public abstract class Vehiculo {
     private String matrícula;
    private String modelo;

    public Vehiculo(String matrícula, String modelo) {
        this.matrícula = matrícula;
        this.modelo = modelo;
    }

    public String getMatrícula() {
        return matrícula;
    }

    public String getModelo() {
        return modelo;
    }

    // Método abstracto a ser implementado por subclases
    public abstract void conducir();
}

