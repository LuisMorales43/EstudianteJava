import java.util.LinkedList;
import java.util.Queue;

// Clase que representa un Pedido
class Pedido {
    private int idPedido;
    private String estado;

    // Constructor
    public Pedido(int idPedido) {
        this.idPedido = idPedido;
        this.estado = "Pendiente";
    }

    // Actualizar el estado del pedido
    public synchronized void actualizarEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
        System.out.println("Pedido " + idPedido + " actualizado a: " + estado);
    }

    public int getIdPedido() {
        return idPedido;
    }

    public String getEstado() {
        return estado;
    }
}

// Clase que representa el Sistema de Gestión de Pedidos
class SistemaGestionPedidos {
    private Queue<Pedido> pedidos = new LinkedList<>();

    // Agregar un pedido a la cola
    public synchronized void agregarPedido(Pedido pedido) {
        pedidos.add(pedido);
        System.out.println("Pedido " + pedido.getIdPedido() + " agregado a la cola.");
        notifyAll(); // Notifica a los hilos en espera
    }

    // Obtener un pedido de la cola
    public synchronized Pedido obtenerPedido() throws InterruptedException {
        while (pedidos.isEmpty()) {
            wait(); // Espera si no hay pedidos
        }
        Pedido pedido = pedidos.poll();
        System.out.println("Pedido " + pedido.getIdPedido() + " procesado de la cola.");
        return pedido;
    }
}

// Clase que representa un Cocinero
class Cocinero extends Thread {
    private SistemaGestionPedidos sistema;

    public Cocinero(SistemaGestionPedidos sistema) {
        this.sistema = sistema;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Pedido pedido = sistema.obtenerPedido();
                System.out.println("Cocinero está preparando el pedido " + pedido.getIdPedido());
                Thread.sleep(2000); // Simula el tiempo de preparación
                pedido.actualizarEstado("Preparado");
            }
        } catch (InterruptedException e) {
            System.out.println("Cocinero interrumpido.");
        }
    }
}

// Clase que representa un Mesero
class Mesero extends Thread {
    private SistemaGestionPedidos sistema;

    public Mesero(SistemaGestionPedidos sistema) {
        this.sistema = sistema;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Pedido pedido = sistema.obtenerPedido();
                System.out.println("Mesero está entregando el pedido " + pedido.getIdPedido());
                Thread.sleep(1000); // Simula el tiempo de entrega
                pedido.actualizarEstado("Entregado");
            }
        } catch (InterruptedException e) {
            System.out.println("Mesero interrumpido.");
        }
    }
}

// Clase principal
public class Restaurante {
    public static void main(String[] args) {
        SistemaGestionPedidos sistema = new SistemaGestionPedidos();

        // Crear hilos de cocineros y meseros
        Cocinero cocinero1 = new Cocinero(sistema);
        Mesero mesero1 = new Mesero(sistema);

        cocinero1.start();
        mesero1.start();

        // Simular pedidos realizados por clientes
        for (int i = 1; i <= 10; i++) {
            Pedido pedido = new Pedido(i);
            sistema.agregarPedido(pedido);
            try {
                Thread.sleep(500); // Simula tiempo entre pedidos
            } catch (InterruptedException e) {
                System.out.println("Interrupción en el sistema.");
            }
        }
    }
}
