package model;

public class BilleteraDigital extends MetodoDePago {
    private String numeroTelefono;
    private String proveedor;

    public BilleteraDigital(String nombreCliente, double precio, String numeroTelefono, String proveedor) {
        super(nombreCliente, precio);
        this.numeroTelefono = numeroTelefono;
        this.proveedor = proveedor;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public String getProveedor() {
        return proveedor;
    }

    @Override
    public void procesarPago() {
        String numeroTelefonoString = numeroTelefono;
        System.out.println("Procesando pago mediante billetera digital (" + proveedor + ") al número: @@@@" 
                           + numeroTelefonoString.substring(numeroTelefonoString.length() - 4));
    }
}