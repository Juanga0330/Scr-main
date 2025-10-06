package model;

public abstract class MetodoDePago {
    private String nombreCliente;
    private double precio;

    public MetodoDePago(String nombreCliente, double precio) {
        this.nombreCliente = nombreCliente;
        this.precio = precio;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public double getPrecio() {
        return precio;
    }

    
    public abstract void procesarPago();
        System.out.println("Procesando pago de $" + precio + " para " + nombreCliente);
    }
