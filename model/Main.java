package model;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        
        // Solicitar información del cliente
        System.out.println("=== Información del Cliente ===");
        System.out.print("Ingrese su nombre: ");
        String nombreCliente = sc.nextLine();
        System.out.print("Ingrese su email: ");
        String emailCliente = sc.nextLine();
        
        Cliente cliente = new Cliente(nombreCliente, emailCliente);
        System.out.println();
        cliente.showInfo();
        
        Pedido pedido = new Pedido(1);

        List<Producto> productos = CargaDeProductos.CargaDeProducto("C:\\Users\\Juanp\\Documents\\Scr-main\\Scr-main\\Catalogo.txt");

        System.out.println("\n=== Catálogo de productos ===");
        for (int i = 0; i < productos.size(); i++) {
            System.out.println((i + 1) + ". " + productos.get(i).getNombre() + " - $" + productos.get(i).getPrecio());
        }
        System.out.println("0. Finalizar compra");

        int opcion;
        do {
            System.out.print("\nSeleccione un producto (0 para finalizar): ");
            opcion = sc.nextInt();

            if (opcion == 0) {
                System.out.println("\nPedido finalizado.");
            } else if (opcion >= 1 && opcion <= productos.size()) {
                pedido.agregarProducto(productos.get(opcion - 1));
                System.out.println(productos.get(opcion - 1).getNombre() + " agregado al carrito.");
            } else {
                System.out.println("Opción inválida. Intente de nuevo.");
            }

        } while (opcion != 0);

        System.out.println("\n=== Productos en el pedido ===");
        for (Producto p : pedido.getProductos()) {
            p.showInfo();
        }

        System.out.println("\n=== Total del pedido ===");
        double total = pedido.calcularTotal();
        System.out.println("Total a pagar: $" + total);

        // Solicitar método de pago
        System.out.println("\n=== Métodos de pago disponibles ===");
        System.out.println("1. Transferencia bancaria");
        System.out.println("2. Tarjeta de crédito");
        System.out.println("3. Billetera digital");

        System.out.print("\nSeleccione método de pago: ");
        int metodo = sc.nextInt();
        sc.nextLine(); // Limpiar buffer

        MetodoDePago metodoPago = null;
        boolean pagoRealizado = false;

        switch (metodo) {
            case 1:
                System.out.print("Ingrese nombre del banco: ");
                String banco = sc.nextLine();
                System.out.print("Ingrese número de cuenta: ");
                int numCuenta = sc.nextInt();
                metodoPago = new TransferenciaBancaria(nombreCliente, total, banco, numCuenta);
                pagoRealizado = true;
                break;

            case 2:
                System.out.print("Ingrese número de tarjeta (16 dígitos): ");
                int numTarjeta = sc.nextInt();
                sc.nextLine(); // limpiar buffer

                System.out.print("Ingrese código CVV (3 dígitos): ");
                short cvv = sc.nextShort();
                sc.nextLine(); // limpiar buffer

                System.out.print("Ingrese fecha de expiración (yyyy-mm-dd): ");
                String fechaStr = sc.nextLine();
                java.sql.Date fechaExp = java.sql.Date.valueOf(fechaStr);

                metodoPago = new Tarjeta(nombreCliente, total, cvv, numTarjeta, fechaExp);
                pagoRealizado = true;
                break;

            case 3:
                System.out.print("Ingrese proveedor (Nequi/Daviplata/etc): ");
                String proveedor = sc.nextLine();
                System.out.print("Ingrese número de teléfono: ");
                String telefono = sc.nextLine();
                metodoPago = new BilleteraDigital(nombreCliente, total, telefono, proveedor);
                pagoRealizado = true;
                break;

            default:
                System.out.println("Método de pago inválido.");
                sc.close();
                return;
        }

        System.out.println();
        metodoPago.procesarPago();

        // 💰 Mostrar mensaje de confirmación del pago
        if (pagoRealizado) {
            System.out.println("\n======================================");
            System.out.println("✅ Pago realizado con éxito.");
            System.out.println("Se ha descontado $" + total + " del total de la compra.");
            System.out.println("Gracias por su pago, " + nombreCliente + ".");
            System.out.println("======================================");

            // Dejar total en 0
            total = 0;
            System.out.println("Saldo restante: $" + total);
        }

        // Mostrar fecha de compra, pero no fecha máxima si ya pagó
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy HH:mm", new Locale("es", "ES"));
        LocalDateTime fechaCompra = pedido.getFechaDeCompra();
        System.out.println("\nFecha de compra: " + fechaCompra.format(formatter));

        sc.close();
    }
}

