package vista;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import model.Pedido;
import model.Producto;
import model.CargaDeProductos;
import model.Cliente;

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
        setTitle("Shopnow - Carrito de compras");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        products = CargaDeProductos.CargaDeProducto("Catalogo.txt");

        pedido = new Pedido(1);

        productoModel = new DefaultListModel<>();
        for (Producto producto : products) {
            productoModel.addElement(
                producto.getId() + " - " +
                producto.getNombre() + " -$ " +
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
                carArea.append(seleccionado.getNombre() + " -$ " + seleccionado.getPrecio() + "\n");
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
        btnTerminarCompra.addActionListener(e -> {
             LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de'  MMMM 'de' yyyy HH:mm:ss");
        String fechaHora = ahora.format(formatter);
        JOptionPane.showMessageDialog(
            this,
            "¡Gracias por su compra!\n" +
            totalLabel.getText() +
            "\nFecha y hora: " + fechaHora
                 );
    });

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(totalLabel, BorderLayout.CENTER);
        panelInferior.add(btnTerminarCompra, BorderLayout.EAST);

        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.add(scrollCarrito, BorderLayout.CENTER);
        panelDerecho.add(panelInferior, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelIzquierdo, panelDerecho);
        splitPane.setDividerLocation(400);

        add(splitPane, BorderLayout.CENTER);
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