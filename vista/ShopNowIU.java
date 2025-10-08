package vista;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import model.Pedido;
import model.Producto;
import model.CargaDeProductos;
import model.Cliente;
import model.MetodoDePago;
import model.TransferenciaBancaria;
import model.Tarjeta;
import model.BilleteraDigital;

public class ShopNowIU extends JFrame {

    private JList<String> productoList;
    private DefaultListModel<String> productoModel;
    private JTextArea carArea;
    private JLabel totalLabel;
    private JButton btnTerminarCompra;
    private Pedido pedido;
    private List<Producto> products;
    private Cliente cliente;


    public ShopNowIU() {
        // Solicitar información del cliente al inicio
        solicitarInfoCliente();
        
        setTitle("Shopnow - Carrito de compras - Cliente: " + cliente.getNombre());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        products = CargaDeProductos.CargaDeProducto("C:\\Users\\Juanp\\Documents\\Scr-main\\Scr-main\\Catalogo.txt");

        pedido = new Pedido(1);

        productoModel = new DefaultListModel<>();
        for (Producto producto : products) {
            productoModel.addElement(
                producto.getId() + " - " +
                producto.getNombre() + " - $" +
                producto.getPrecio()
                
            );
        }

        productoList = new JList<>(productoModel);
        JScrollPane scrollCatalogo = new JScrollPane(productoList);

        JButton btnAgregar = new JButton("Agregar al carrito");
        btnAgregar.addActionListener(e -> {
            int selectedIndex = productoList.getSelectedIndex();
            if (selectedIndex != -1) {
                Producto seleccionado = products.get(selectedIndex);
                pedido.agregarProducto(seleccionado); 
                carArea.append(seleccionado.getNombre() + " - $" + seleccionado.getPrecio() + "\n");
                actualizarTotal();
            }
        });

        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.add(new JLabel("Catálogo de Productos"), BorderLayout.NORTH);
        panelIzquierdo.add(scrollCatalogo, BorderLayout.CENTER);
        panelIzquierdo.add(btnAgregar, BorderLayout.SOUTH);

        carArea = new JTextArea();
        carArea.setEditable(false);
        JScrollPane scrollCarrito = new JScrollPane(carArea);

        totalLabel = new JLabel("Total: $0.0");

        btnTerminarCompra = new JButton("Terminar compra");
        btnTerminarCompra.addActionListener(e -> procesarCompra());

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(totalLabel, BorderLayout.CENTER);
        panelInferior.add(btnTerminarCompra, BorderLayout.EAST);

        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.add(new JLabel("Carrito de Compras"), BorderLayout.NORTH);
        panelDerecho.add(scrollCarrito, BorderLayout.CENTER);
        panelDerecho.add(panelInferior, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelIzquierdo, panelDerecho);
        splitPane.setDividerLocation(400);

        add(splitPane, BorderLayout.CENTER);
    }

    private void solicitarInfoCliente() {
        String nombre = JOptionPane.showInputDialog(this, 
            "Ingrese su nombre:", 
            "Información del Cliente", 
            JOptionPane.QUESTION_MESSAGE);
        
        if (nombre == null || nombre.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Debe ingresar un nombre para continuar", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        
        String email = JOptionPane.showInputDialog(this, 
            "Ingrese su email:", 
            "Información del Cliente", 
            JOptionPane.QUESTION_MESSAGE);
        
        if (email == null || email.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Debe ingresar un email para continuar", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        
        cliente = new Cliente(nombre.trim(), email.trim());
    }

    private void procesarCompra() {
        if (pedido.getProductos().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "El carrito está vacío. Agregue productos antes de finalizar la compra.", 
                "Carrito Vacío", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Solicitar método de pago
        String[] opciones = {"Transferencia Bancaria", "Tarjeta de Crédito", "Billetera Digital"};
        int metodoPago = JOptionPane.showOptionDialog(this,
            "Seleccione el método de pago:",
            "Método de Pago",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[0]);
        
        if (metodoPago == -1) {
            return; // Usuario canceló
        }
        
        MetodoDePago pago = null;
        double total = pedido.calcularTotal();
        boolean pagoRealizado = false;
        
        switch (metodoPago) {
            case 0: // Transferencia Bancaria
                String banco = JOptionPane.showInputDialog(this,
                    "Ingrese el nombre del banco:",
                    "Transferencia Bancaria",
                    JOptionPane.QUESTION_MESSAGE);
                
                if (banco == null || banco.trim().isEmpty()) return;
                
                String numCuentaStr = JOptionPane.showInputDialog(this,
                    "Ingrese el número de cuenta:",
                    "Transferencia Bancaria",
                    JOptionPane.QUESTION_MESSAGE);
                
                if (numCuentaStr == null || numCuentaStr.trim().isEmpty()) return;
                
                try {
                    int numCuenta = Integer.parseInt(numCuentaStr);
                    pago = new TransferenciaBancaria(cliente.getNombre(), total, banco, numCuenta);
                    pagoRealizado = true;
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, 
                        "Número de cuenta inválido", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                break;
                
            case 1: // Tarjeta de Crédito
                String numTarjetaStr = JOptionPane.showInputDialog(this,
                    "Ingrese el número de tarjeta (16 dígitos):",
                    "Tarjeta de Crédito",
                    JOptionPane.QUESTION_MESSAGE);
                
                if (numTarjetaStr == null || numTarjetaStr.trim().isEmpty()) return;
                
                String cvvStr = JOptionPane.showInputDialog(this,
                    "Ingrese código CVV (3 dígitos):",
                    "Tarjeta de Crédito",
                    JOptionPane.QUESTION_MESSAGE);
                
                if (cvvStr == null || cvvStr.trim().isEmpty()) return;
                
                String fechaExpStr = JOptionPane.showInputDialog(this,
                    "Ingrese fecha de expiración (yyyy-mm-dd):",
                    "Tarjeta de Crédito",
                    JOptionPane.QUESTION_MESSAGE);
                
                if (fechaExpStr == null || fechaExpStr.trim().isEmpty()) return;
                
                try {
                    int numTarjeta = Integer.parseInt(numTarjetaStr);
                    short cvv = Short.parseShort(cvvStr);
                    java.sql.Date fechaExp = java.sql.Date.valueOf(fechaExpStr);
                    pago = new Tarjeta(cliente.getNombre(), total, cvv, numTarjeta, fechaExp);
                    pagoRealizado = true;
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, 
                        "Datos de tarjeta inválidos", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                break;
                
            case 2: // Billetera Digital
                String proveedor = JOptionPane.showInputDialog(this,
                    "Ingrese el proveedor (Nequi/Daviplata):",
                    "Billetera Digital",
                    JOptionPane.QUESTION_MESSAGE);
                
                if (proveedor == null || proveedor.trim().isEmpty()) return;
                
                String telefono = JOptionPane.showInputDialog(this,
                    "Ingrese el número de teléfono:",
                    "Billetera Digital",
                    JOptionPane.QUESTION_MESSAGE);
                
                if (telefono == null || telefono.trim().isEmpty()) return;
                
                pago = new BilleteraDigital(cliente.getNombre(), total, telefono, proveedor);
                pagoRealizado = true;
                break;
        }
        
        // Procesar pago (esto imprimirá en consola)
        if (pago != null) {
            System.out.println("\n=== PROCESANDO PAGO ===");
            pago.procesarPago();
            System.out.println("Cliente: " + cliente.getNombre());
            System.out.println("Total: $" + total);
        }
        
        // Mostrar confirmación con mensaje de pago realizado
        LocalDateTime ahora = pedido.getFechaDeCompra();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy HH:mm", new Locale("es", "ES"));
        String fechaHora = ahora.format(formatter);
        
        String mensaje = "";
        if (pagoRealizado) {
            mensaje = "======================================\n" +
                     "✅ Pago realizado con éxito.\n" +
                     "Se ha descontado $" + total + " del total de la compra.\n" +
                     "Gracias por su pago, " + cliente.getNombre() + ".\n" +
                     "======================================\n\n" +
                     "Saldo restante: $0.0\n" +
                     "Fecha de compra: " + fechaHora;
        }
        
        JOptionPane.showMessageDialog(
            this,
            mensaje,
            "Compra Exitosa",
            JOptionPane.INFORMATION_MESSAGE
        );
        
        // Limpiar carrito
        carArea.setText("");
        pedido = new Pedido(2);
        actualizarTotal();
    }

    private void actualizarTotal() {
        double total = 0.0;
        for (Producto p : pedido.getProductos()) { 
            total += p.getPrecio();
        }
        totalLabel.setText("Total: $" + total);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ShopNowIU().setVisible(true);
        });
    }
}