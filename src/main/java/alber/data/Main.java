package alber.data;

import alber.GUI.VentanaChat;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        String nombre = JOptionPane.showInputDialog(null,"Introduce tu nombre de usuario.", "Inicio de sesión");
        if(nombre != null && nombre.trim().isBlank())
            nombre = "Invitado";

        Usuario user = new Usuario(nombre);

        new VentanaChat(user);

    }
}