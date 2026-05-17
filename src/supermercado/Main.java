package supermercado;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * EA2: Optimización del Proceso de Cobro en un Supermercado mediante Hilos y Concurrencia
 * Curso: Desarrollo de Software Seguro
 * Estudiante: Daniel Andres Duque Alvarez
 * IU Digital de Antioquia – 2026
 *
 * Punto de entrada de la simulación.
 * Se crean múltiples cajeras (hilos) y clientes; cada cajera atiende
 * su cola de clientes de forma secuencial (cliente a cliente).
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("=======================================================");
        System.out.println("  SIMULACIÓN DE COBRO EN SUPERMERCADO");
        System.out.println("  Hilos y Concurrencia – IU Digital de Antioquia 2026");
        System.out.println("=======================================================\n");

        // ── 1. Crear productos de ejemplo ──────────────────────────────────
        Producto leche    = new Producto("Leche entera 1L",   2800, 2, 300);
        Producto pan      = new Producto("Pan tajado",         4500, 1, 200);
        Producto arroz    = new Producto("Arroz Diana 1kg",    3200, 3, 250);
        Producto aceite   = new Producto("Aceite Girasol 1L",  7800, 1, 350);
        Producto jabon    = new Producto("Jabón Rey x3",       5500, 2, 400);
        Producto cafe     = new Producto("Café Colcafé 250g",  9200, 1, 300);
        Producto galletas = new Producto("Galletas Festival",  2100, 4, 150);
        Producto shampoo  = new Producto("Shampoo Sedal",     12000, 1, 500);
        Producto yogur    = new Producto("Yogur Alpina 200g",  1800, 3, 200);
        Producto detergente = new Producto("Detergente Ariel", 15000, 1, 450);

        // ── 2. Crear clientes y asignarles productos ───────────────────────
        List<Cliente> todosLosClientes = new ArrayList<>();

        Cliente c1 = new Cliente(1, "María González");
        c1.agregarProducto(leche); c1.agregarProducto(pan); c1.agregarProducto(arroz);

        Cliente c2 = new Cliente(2, "Carlos Pérez");
        c2.agregarProducto(aceite); c2.agregarProducto(jabon); c2.agregarProducto(cafe);

        Cliente c3 = new Cliente(3, "Luisa Martínez");
        c3.agregarProducto(galletas); c3.agregarProducto(shampoo);

        Cliente c4 = new Cliente(4, "Andrés Torres");
        c4.agregarProducto(yogur); c4.agregarProducto(detergente); c4.agregarProducto(pan);

        Cliente c5 = new Cliente(5, "Sofía Ramírez");
        c5.agregarProducto(leche); c5.agregarProducto(cafe); c5.agregarProducto(galletas);

        Cliente c6 = new Cliente(6, "Jorge Vargas");
        c6.agregarProducto(arroz); c6.agregarProducto(aceite);

        todosLosClientes.add(c1);
        todosLosClientes.add(c2);
        todosLosClientes.add(c3);
        todosLosClientes.add(c4);
        todosLosClientes.add(c5);
        todosLosClientes.add(c6);

        // ── 3. Distribuir clientes en colas por cajera ────────────────────
        // Cola compartida: todas las cajeras toman clientes de la misma cola
        Queue<Cliente> colaCompartida = new LinkedList<>(todosLosClientes);

        // ── 4. Crear cajeras ───────────────────────────────────────────────
        int numeroCajeras = 2;
        AtomicLong tiempoTotalGlobal = new AtomicLong(0);
        List<Cajera> cajeras = new ArrayList<>();

        for (int i = 1; i <= numeroCajeras; i++) {
            cajeras.add(new Cajera(i, "Cajera-" + i, colaCompartida, tiempoTotalGlobal));
        }

        // ── 5. Ejecutar cajeras en hilos concurrentes ──────────────────────
        System.out.printf("Iniciando simulación con %d cajera(s) y %d cliente(s)...%n%n",
                numeroCajeras, todosLosClientes.size());

        ExecutorService executor = Executors.newFixedThreadPool(numeroCajeras);
        long inicioSimulacion = System.currentTimeMillis();

        for (Cajera cajera : cajeras) {
            executor.submit(cajera);
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.MINUTES);

        long tiempoReal = System.currentTimeMillis() - inicioSimulacion;

        // ── 6. Resumen final ───────────────────────────────────────────────
        System.out.println("=======================================================");
        System.out.println("  RESUMEN FINAL DE LA SIMULACIÓN");
        System.out.println("=======================================================");
        for (Cajera cajera : cajeras) {
            System.out.printf("  %-12s → Tiempo total atendiendo: %d ms%n",
                    cajera.getNombre(), cajera.getTiempoTotalCajera());
        }
        System.out.println("-------------------------------------------------------");
        System.out.printf("  Suma de tiempos de cobro (todos los clientes): %d ms%n",
                tiempoTotalGlobal.get());
        System.out.printf("  Tiempo real de ejecución (con concurrencia):   %d ms%n", tiempoReal);
        System.out.printf("  Clientes atendidos: %d%n", todosLosClientes.size());
        System.out.println("=======================================================");
    }
}
