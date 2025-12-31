package alber.GUI;

import alber.data.Chat;
import alber.data.Receptor;
import alber.data.Usuario;

import javax.swing.*;
import java.awt.event.*;

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
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonEnviar);

        buttonEnviar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonSalir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

}
