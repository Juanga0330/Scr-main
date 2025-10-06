package model;

import java.sql.Date;

public class Tarjeta extends MetodoDePago {
    private short cvv;
    private int numeroDeTarjeta;
    private Date fechaDeExpiracion;
    
    public Tarjeta(String nombreCliente, double precio, short cvv, int numeroDeTarjeta, Date fechaDeExpiracion) {
        super(nombreCliente, precio);
        this.cvv = cvv;
        this.numeroDeTarjeta = numeroDeTarjeta;
        this.fechaDeExpiracion = fechaDeExpiracion;
    }

    public short getCvv() {
        return cvv;
    }

    public int getNumeroDeTarjeta() {
        return numeroDeTarjeta;
    }

    public Date getFechaDeExpiracion() {
        return fechaDeExpiracion;
    }

    @Override
    public void procesarPago() {
        String numeroDeTarjetaString = String.valueOf(numeroDeTarjeta);
        System.out.println("Procesando pago con tarjeta:   @@@" + numeroDeTarjetaString.substring(numeroDeTarjetaString.length() - 4));
    }
}
