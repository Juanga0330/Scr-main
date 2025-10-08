package model;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CargaDeProductos {
    public static List<Producto> CargaDeProducto(String pathFile){
        List<Producto> productos = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(pathFile))) {
            String linea;
            while ((linea = bufferedReader.readLine()) != null) {
                String[] valores = linea.split(",");
                int id = Integer.parseInt(valores[0]);
                String nombre = valores[1];
                double precio = Double.parseDouble(valores[2]);

                productos.add(new Producto(id, nombre, precio));
            }
        }catch (FileNotFoundException e){
            System.out.println("No se encontro el archivo" + pathFile);
        
        }catch (IOException e){
            System.out.println("Error al leer el archivo" + e.getMessage());

        }
        return productos;
       
    }
}
