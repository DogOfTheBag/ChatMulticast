package alber.GUI;

import alber.data.Chat;
import alber.data.Receptor;
import alber.data.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class VentanaChat extends JDialog {
    private JPanel contentPane;
    private JButton buttonEnviar;
    private JButton buttonSalir;
    private JTextArea textAreaChat;
    private JTextArea textAreaInput;
    private Usuario user;
    private Chat chat;
    private Receptor receptor;

    public VentanaChat(Usuario user) {
        this.user = user;
        /******************CREACION DE LA VENTANA ANTES HECHA CON EL EDITOR****************/
        contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        textAreaChat = new JTextArea();
        textAreaChat.setEditable(false);
        JScrollPane scrollChat = new JScrollPane(textAreaChat);

        textAreaInput = new JTextArea(3, 20);
        JScrollPane scrollInput = new JScrollPane(textAreaInput);

        buttonEnviar = new JButton("Enviar");
        buttonSalir = new JButton("Salir");

        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 10, 0));
        panelBotones.add(buttonEnviar);
        panelBotones.add(buttonSalir);

        JPanel panelInferior = new JPanel(new BorderLayout(10, 0));
        panelInferior.add(scrollInput, BorderLayout.CENTER);
        panelInferior.add(panelBotones, BorderLayout.EAST);

        contentPane.add(scrollChat, BorderLayout.CENTER);
        contentPane.add(panelInferior, BorderLayout.SOUTH);

        setTitle("Chat - " + user.getNombre());
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonEnviar);
        setVisible(true);
        //esto hace que el foco este en el area de texto de escritura al entrar
        SwingUtilities.invokeLater(() -> textAreaInput.requestFocusInWindow());

        /******************CREACION DE LA VENTANA ANTES HECHA CON EL EDITOR****************/

        /*Todavia estamos en el constructor asi que cuando hagamos una instancia de la ventana, hacemos
        * un try catch para conectar con el chat.*/
        try {
            chat = new Chat();
            receptor = new Receptor(this);
            receptor.start();
            chat.enviarMensajeByte(user.getNombre() + " SE HA UNIDO AL CHAT");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //los eventos de los botones
        buttonEnviar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    enviarMensaje();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        buttonSalir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Salir();
            }
        });
        /*Esto basicamente es el listener del enter para enviar el mensaje*/
        textAreaInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    //consume hace que se quite el \n que pone el enter automaticamente y asi se envie el mensaje sin eso
                    e.consume();
                    try {
                        enviarMensaje();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        //para cerrar la app al cerrar la ventana
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                Salir();
            }
        });
    }

    //METODOS AUXILIARES
    private void Salir() {
        try {
            //al salir chapamos todo
            chat.enviarMensajeByte(user.getNombre() + " ha salido del chat");
            receptor.cerrar();
            chat.cerrarSocket();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dispose();
    }

    private void enviarMensaje() throws IOException {
        /*coge el texto, recorta espacios, lo manda, pone el input vacio y le devuelve el foco por si acaso*/
        String texto = textAreaInput.getText().trim();
        if (texto.isBlank()) {
            return;
        }
        chat.enviarMensajeByte(user.getNombre() + ": " + texto);
        textAreaInput.setText("");
        textAreaInput.requestFocusInWindow();
    }

    /*En caso de que mandes mensajes muy a la vez, es posible que Swing se quede pillado o explote, debido a que al parecer
    * no maneja demasiado bien el uso de varios hilos
    * para evitar problemas tenemos el uso de SwingUtilities.invokeLater, que basicamente lo que hace en este caso es que
    * pone el mensaje que añadamos en una especie de cola, y hasta que todo lo anterior no haya sido pintado no pinta este*/
    public void añadirMensaje(String texto) {
        SwingUtilities.invokeLater(() -> textAreaChat.append(texto + "\n"));
    }
}
