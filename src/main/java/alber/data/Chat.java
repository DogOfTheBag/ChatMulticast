package alber.data;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Chat {
    //IMPORTANTE GRUPO STRING Y PUERTO INT
    private final String GRUPO = "239.0.0.1";
    private final int PUERTO =  5000;
    private MulticastSocket socket;
    private InetAddress direccion;

    /*Cada vez que hagamos una instancia del chat, haremos el socket y le pasaremos la dirección ip del chat donde estamos*/
    public Chat() throws IOException {
        socket = new MulticastSocket(PUERTO);
        direccion = InetAddress.getByName(GRUPO); //hemos puesto grupo en string ya que ahora usamos getByName para coger la ip por el nombre
    }

    public void enviarMensajeByte(String texto) throws IOException {
        //pasamos el mensaje a bytes para mandarlo por el socket
        byte[] buffer = texto.getBytes();
        /*creamos el paquete de datos pasanado los bytes, la longitud, la direccion ip (siendo ya InetAdress) y el puerto donde lo tenemos que mandar
        * y se manda*/
        DatagramPacket paquete = new DatagramPacket(buffer, buffer.length, direccion, PUERTO);
        socket.send(paquete);
    }
    //esto lo usamos en la ventana para una vez que se salgan todos se cierre el socket
    public void cerrarSocket(){
        socket.close();
    }

}
