package model;
public class Cliente {
    private String nombre;
    private String email;

    public Cliente(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
    }

    public void showInfo() {
        System.out.println("Información del cliente: \nNombre: " + nombre + "\nEmail: " + email);
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }
}