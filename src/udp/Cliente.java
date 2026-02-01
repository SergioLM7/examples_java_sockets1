package udp;

import java.io.IOException;
import java.net.*;

public class Cliente {
    public static void main(String[] args) {

        //Puerto del servidor
        final int PORT = 5001;

        //Es un buffer donde se almacenarán los datos a transportar
        byte[] buffer = new byte[1024];

        //Obtener la dirección IP del servidor
        try {
            InetAddress serverAddress = InetAddress.getByName("localhost");

            //Creamos el socket datagrama del cliente
            DatagramSocket socketUDP = new DatagramSocket();

            String mensaje = "¡Hola mundo desde el cliente!";

            //Transformar el mensaje a bytes, que es lo que transportar los datagramas.
            buffer = mensaje.getBytes();

            //Creamos el paquete (datagrama), con los datos,
            //el tamaño de los datos, la dirección del servidor y el puerto del servidor
            DatagramPacket serverRequest = new DatagramPacket(buffer, buffer.length, serverAddress, PORT);

            System.out.println("Enviando datagrama al servidor UDP...");
            socketUDP.send(serverRequest);

            //Preparamos un paquete vacío para recibir la respuesta del servidor
            DatagramPacket serverAnswer = new DatagramPacket(buffer, buffer.length);

            //Recibimos la respuesta del servidor
            socketUDP.receive(serverAnswer);
            System.out.println("Recibo la respuesta del servidor.");

            //Convertimos los datos recibidos a String
            mensaje = new String(serverAnswer.getData());
            System.out.println(mensaje);

        } catch (UnknownHostException | SocketException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
