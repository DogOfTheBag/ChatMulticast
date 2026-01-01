package alber.data;

import alber.GUI.VentanaChat;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        /*Lanzamos una ventana modal en la que recojamos el nombre del user. Si lo da vacío o null, le ponemos invitado, y creamos user*/
        String nombre = JOptionPane.showInputDialog(null,"Introduce tu nombre de usuario.", "Inicio de sesión",JOptionPane.QUESTION_MESSAGE);
        if(nombre == null || nombre.trim().isBlank())
            nombre = "Invitado";

        Usuario user = new Usuario(nombre);
        //arrancamos la ventana donde ya se hace todo
        new VentanaChat(user);

    }
}