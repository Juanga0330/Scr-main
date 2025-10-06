package model;

public class TransferenciaBancaria extends MetodoDePago {
    private String nombreBanco;
    private int numeroCuenta;

    public TransferenciaBancaria(String nombreCliente, double precio, String nombreBanco, int numeroCuenta) {
        super(nombreCliente, precio);
        this.nombreBanco = nombreBanco;
        this.numeroCuenta = numeroCuenta;
    }

    public String getNombreBanco() {
        return nombreBanco;
    }

    public int getNumeroCuenta() {
        return numeroCuenta;
    }

    @Override
    
    public void procesarPago() {
        String numeroCuentaString = String.valueOf(numeroCuenta);
        System.out.println("Procesando pago mediante transferencia bancaria a la cuenta: @@@@" 
                           + numeroCuentaString.substring(numeroCuentaString.length() - 4));
    }
}
