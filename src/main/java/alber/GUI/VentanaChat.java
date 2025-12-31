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

        try {
            chat = new Chat();
            receptor = new Receptor(this);
            receptor.start();
            chat.enviarMensajeByte(user.getNombre() + " SE HA UNIDO AL CHAT");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
                onSalir();
            }
        });

        textAreaInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    try {
                        enviarMensaje();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onSalir();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onSalir();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onSalir() {
        try {
            chat.enviarMensajeByte(user.getNombre() + " ha salido del chat");
            receptor.cerrar();
            chat.cerrarSocket();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dispose();
    }

    private void enviarMensaje() throws IOException {
        String texto = textAreaInput.getText().trim();
        if (texto.isBlank()) {
            return;
        }
        chat.enviarMensajeByte(user.getNombre() + ": " + texto);
        textAreaInput.setText("");
        textAreaInput.requestFocusInWindow();
    }

    public void añadirMensaje(String texto) {
        SwingUtilities.invokeLater(() -> textAreaChat.append(texto + "\n"));
    }

    public Usuario getUser() {
        return user;
    }
}
