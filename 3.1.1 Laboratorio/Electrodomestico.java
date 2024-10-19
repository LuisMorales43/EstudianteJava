public class Electrodomestico extends Producto {
    private String consumoEnergetico;
    private double peso;

    public Electrodomestico(String nombre, double precio, String marca, String consumoEnergetico, double peso) {
        super(nombre, precio, marca);
        this.consumoEnergetico = consumoEnergetico;
        this.peso = peso;
    }

    public String getConsumoEnergetico() {
        return consumoEnergetico;
    }

    public void setConsumoEnergetico(String consumoEnergetico) {
        this.consumoEnergetico = consumoEnergetico;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    @Override
    public void mostrarInfo() {
        super.mostrarInfo();
        System.out.println("Consumo Energético: " + consumoEnergetico + ", Peso: " + peso);
    }
}
