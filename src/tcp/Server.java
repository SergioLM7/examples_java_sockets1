package tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {

        //Socket para el cliente
        Socket socket = null;

        DataInputStream in;
        DataOutputStream out;

        final int PORT = 5001;

        //Gestión del flujo con el socket específico del servidor
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("Servidor TCP iniciado");

            //Un servidor siempre está a la escucha esperando a que el cliente se conecte
            while(true) {

                //El servidor espera/se bloquea hasta que un cliente se conecte (similar a dormir un hilo)
                socket = serverSocket.accept();

                System.out.println("Cliente conectado");

                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());

                String mensaje = in.readUTF();
                System.out.println("Mensaje recibido: " + mensaje);

                out.writeUTF("¡Hola, mundo, desde el servidor TCP!");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
