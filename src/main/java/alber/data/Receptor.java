package alber.data;

import alber.GUI.VentanaChat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/*Es importante que esta clase extienda de hilo debido a que el programa ahora mismo quizá no, pero si hay mucha
* gente hablando a la vez, recibirá mensajes a la vez, por lo que lo gestionaremos usando hilos para que no explote.*/
public class Receptor extends Thread{
    //al receptor le pasamos los mismos parametros que al chat, ademas de la ventana para poner los mensajes que recibamos
    private final String GRUPO = "239.0.0.1";
    private final int PUERTO = 5000;
    private MulticastSocket socket;
    private InetAddress direccion;
    private VentanaChat ventana;

    private boolean activo;

    /*El constructor del receptor es algo diferente comparado con el del chat
    * en este le pasamos la ventana ya que usaremos esta clase para poner los mensajes que nos pasen, o sea que nos hace falta
    * por otro lado hacemos lo del socket y direccion, pero aparte como el socket del servidor (chat) ya está creado, lo que hacemos es
    * que el receptor se una al grupo del socket dandole la direccion ip.*/
    public Receptor(VentanaChat ventana) throws IOException {
        activo = true;
        this.ventana = ventana;
        socket = new MulticastSocket(PUERTO);
        direccion = InetAddress.getByName(GRUPO);
        socket.joinGroup(direccion);
    }

    @Override
    public void run() {
        try{
            while(activo){
                byte[] buffer = new byte[1024];
                /*aqui no es necesario que pongamo direccion y puerto, ya que eso es más enfocado para enviar datos, mas que recibirlos*/
                DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
                socket.receive(paquete);
                /* Para convertir los bytes que nos manda por el socket el programa podriamos usar el siguiente ejemplo de nacho
                de los ejercicios
                msg = new String(paquete.getData());
			    System.out.println("Recibo: " + msg.trim());
			    el problema de usar esto es que coge toda la longitud y la recorta
			    si nuestro mensaje fuera hola con 13 espacios después no se vería

			    para evitarnos problemas, le pasamos directamente al string desde que byte empezamos y cuanta longitud tiene el paquete
			    y asi leemos exactamente lo que queremos.*/
                String mensaje = new String(paquete.getData(),0, paquete.getLength());
                ventana.añadirMensaje(mensaje);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //lo mismo que en chat, cerramos todo con esto
    public void cerrar(){
        activo = false;
        socket.close();
    }
}
