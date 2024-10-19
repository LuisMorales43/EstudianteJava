public class Barco extends Vehiculo implements Navegable {
    private int numMastiles;

    public Barco(String marca, String modelo, int numMastiles) {
        super(marca, modelo);
        this.numMastiles = numMastiles;
    }

    @Override
    public void conducir() {
        System.out.println("Conduciendo el barco " + getMarca() + " " + getModelo());
    }

    @Override
    public void navegar() {
        System.out.println("Navegando el barco con " + numMastiles + " mastiles.");
    }
}
