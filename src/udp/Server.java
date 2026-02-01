package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server {
    public static void main(String[] args) {

        //Puerto en el que escucha el servidor
        final int PORT = 5001;

        byte[] buffer = new byte[1024];

        try (DatagramSocket socketUDP = new DatagramSocket(PORT)){
            System.out.println("Servidor UDP iniciado");

            while(true) {
                //Paquete que vamos a recibir desde el cliente
                DatagramPacket clientRequest = new DatagramPacket(buffer, buffer.length);
                socketUDP.receive(clientRequest);

                System.out.println("Recibo petición del cliente");

                //Recogemos el mensaje que viene del cliente
                String mensaje = new String(clientRequest.getData());
                System.out.println(mensaje);

                //Puerto e IP del cliente
                int clientHost = clientRequest.getPort();
                InetAddress cliente = clientRequest.getAddress();

                mensaje = "¡Hola mundo, desde el servidor UDP!";

                buffer = mensaje.getBytes();

                //Paquete de respuesta del servidor
                DatagramPacket serverAnswer = new DatagramPacket(buffer, buffer.length, cliente, clientHost);

                System.out.println("Enviando respuesta al cliente...");
                socketUDP.send(serverAnswer);
            }

        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
