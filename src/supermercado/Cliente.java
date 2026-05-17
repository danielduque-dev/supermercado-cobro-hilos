package supermercado;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa un cliente con una lista de productos a comprar.
 */
public class Cliente {
    private int id;
    private String nombre;
    private List<Producto> productos;

    public Cliente(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.productos = new ArrayList<>();
    }

    public void agregarProducto(Producto p) {
        productos.add(p);
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public List<Producto> getProductos() { return productos; }

    public double getTotalCompra() {
        return productos.stream().mapToDouble(Producto::getCostoTotal).sum();
    }

    /** Tiempo total estimado para cobrar todos sus productos (suma de tiempos) */
    public int getTiempoTotalMs() {
        return productos.stream().mapToInt(Producto::getTiempoProcesamiento).sum();
    }
}
