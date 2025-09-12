
package model;
public class Producto {
    private int id;
    private String nombre;
    private double precio;

    public Producto(int id, String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getId() {
        return id;
    }

    public double getPrecio() {
        return precio;
    }

    public void showInfo() {
        System.out.println("Información del producto: \nNombre: " + nombre + "\nPrecio: $" + precio);
    }

}