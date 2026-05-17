package supermercado;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Representa una cajera que atiende clientes de forma secuencial (cliente a cliente).
 * Implementa Runnable para ejecutarse en su propio hilo.
 */
public class Cajera implements Runnable {
    private final int id;
    private final String nombre;
    private final Queue<Cliente> colaClientes;
    private final AtomicLong tiempoTotalGlobal;
    private long tiempoTotalCajera = 0;

    public Cajera(int id, String nombre, Queue<Cliente> colaClientes, AtomicLong tiempoTotalGlobal) {
        this.id = id;
        this.nombre = nombre;
        this.colaClientes = colaClientes;
        this.tiempoTotalGlobal = tiempoTotalGlobal;
    }

    @Override
    public void run() {
        Cliente cliente;
        // Extraer clientes de la cola de forma segura entre hilos
        while ((cliente = poll()) != null) {
            atenderCliente(cliente);
        }
        log("Ha terminado su turno. Tiempo total atendiendo: " + tiempoTotalCajera + " ms");
    }

    private synchronized Cliente poll() {
        return colaClientes.poll();
    }

    private void atenderCliente(Cliente cliente) {
        log("Comenzando a cobrar a: " + cliente.getNombre());
        long inicioCliente = System.currentTimeMillis();

        System.out.println("\n  ┌─ Productos de " + cliente.getNombre() + ":");
        for (Producto producto : cliente.getProductos()) {
            System.out.println("  │" + producto.toString());
            // Simular el tiempo de escaneo de cada producto
            try {
                Thread.sleep(producto.getTiempoProcesamiento());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        long tiempoCliente = System.currentTimeMillis() - inicioCliente;
        tiempoTotalCajera += tiempoCliente;
        tiempoTotalGlobal.addAndGet(tiempoCliente);

        System.out.printf("  └─ Total de %s: $%.2f | Tiempo cobro: %d ms%n%n",
                cliente.getNombre(), cliente.getTotalCompra(), tiempoCliente);
    }

    private void log(String msg) {
        System.out.printf("[Cajera %d - %s] %s%n", id, nombre, msg);
    }

    public String getNombre() { return nombre; }
    public long getTiempoTotalCajera() { return tiempoTotalCajera; }
}
