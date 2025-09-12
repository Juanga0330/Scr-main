package vista;
import javax.swing.JFrame;
public class VentanaView {
    public static void main(String[] args) {
        JFrame ventana = new JFrame("Hola tttt");
        ventana.setTitle("Klkkk");
        ventana.setSize(300, 400);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setVisible(true);
        ventana.setLocationRelativeTo(ventana);
    }   
}
