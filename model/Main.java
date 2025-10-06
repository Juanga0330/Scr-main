package model;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Pedido pedido = new Pedido(1);

        List<Producto> productos = CargaDeProductos.CargaDeProducto("C:\\Users\\Juanp\\Documents\\scr-main\\scr-main\\Catalogo.txt");

        System.out.println("=== Catálogo de productos ===");
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
                System.out.println(" " + productos.get(opcion - 1).getNombre() + " agregado al carrito.");
            } else {
                System.out.println(" Opción inválida. Intente de nuevo.");
            }

        } while (opcion != 0);

        System.out.println("\n=== Productos en el pedido ===");
        for (Producto p : pedido.getProductos()) {
            p.showInfo();
        }

        System.out.println("\n=== Total del pedido ===");
        double total = pedido.calcularTotal();
        System.out.println("Total a pagar: $" + total);

        MetodoDePago transferencia = new TransferenciaBancaria("Juan Pérez", total, "Banco Bogotá", 12345678);
        MetodoDePago tarjeta = new Tarjeta("Juan Pérez", total, "Visa", "1234567812345678");
        MetodoDePago billetera = new BilleteraDigital("Juan Pérez", total, "3109876543", "Nequi");

        System.out.println("\n=== Métodos de pago disponibles ===");
        System.out.println("1. Transferencia bancaria");
        System.out.println("2. Tarjeta de crédito");
        System.out.println("3. Billetera digital");

        System.out.print("\nSeleccione método de pago: ");
        int metodo = sc.nextInt();

        System.out.println();
        switch (metodo) {
            case 1:
                transferencia.procesarPago();
                break;
            case 2:
                tarjeta.procesarPago();
                break;
            case 3:
                billetera.procesarPago();
                break;
            default:
                System.out.println("Método de pago inválido.");
                break;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy HH:mm", new Locale("es", "ES"));
        System.out.println("\nFecha de compra: " + pedido.getFechaDeCompra().format(formatter));
        System.out.println("Fecha máxima de pago: " + pedido.getFechaDeCompra().plusHours(24).format(formatter));

        sc.close();
    }
}