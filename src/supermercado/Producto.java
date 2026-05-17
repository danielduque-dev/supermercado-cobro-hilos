package supermercado;

/**
 * Representa un producto adquirido por un cliente en el supermercado.
 */
public class Producto {
    private String nombre;
    private double precio;
    private int cantidad;
    // Tiempo simulado para procesar este producto en la caja (ms)
    private int tiempoProcesamiento;

    public Producto(String nombre, double precio, int cantidad, int tiempoProcesamiento) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.tiempoProcesamiento = tiempoProcesamiento;
    }

    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public int getCantidad() { return cantidad; }
    public int getTiempoProcesamiento() { return tiempoProcesamiento; }
    public double getCostoTotal() { return precio * cantidad; }

    @Override
    public String toString() {
        return String.format("  %-20s x%d  $%-8.2f (costo total: $%.2f, tiempo: %dms)",
                nombre, cantidad, precio, getCostoTotal(), tiempoProcesamiento);
    }
}
