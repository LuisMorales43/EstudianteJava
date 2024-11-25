/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LAB;

/**
 *
 * @author ebers
 */
public class GestionVehiculos {
     public static void main(String[] args) {
        // Crear instancias de Automóvil, Camión y Motocicleta
        Automovil auto = new Automovil("ABC123", "Toyota Corolla");
        Camion camión = new Camion("DEF456", "Volvo FH16");
        Motocicleta moto = new Motocicleta("GHI789", "Harley Davidson");

        // Polimorfismo: Tratando los objetos como su clase base
        Vehiculo vehículo1 = auto;
        Vehiculo vehículo2 = camión;
        Vehiculo vehículo3 = moto;

        // Llamando al método conducir, que es polimórfico
        vehículo1.conducir();
        vehículo2.conducir();
        vehículo3.conducir();

        // Polimorfismo con interfaces
        Carga carga1 = camión;
        Carga carga2 = moto;

        carga1.cargar();
        carga1.descargar();
        carga2.cargar();
        carga2.descargar();
    }
}
