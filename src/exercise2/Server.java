package exercise2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        boolean serverOn = true;
        final int PORT = 5001;
        DataInputStream in;
        DataOutputStream out;
        String message = "";

        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Servidor CHAT iniciado en el puerto " + PORT);

            while(serverOn) {
                Socket socket = server.accept();
                System.out.println("Cliente conectado en " + socket.getInetAddress());

                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());

                do {
                    message = in.readUTF();
                    System.out.println("Cliente dice: " + message);

                    out.writeUTF("Servidor recibió: " + message);

                } while (!message.equalsIgnoreCase("salir"));


                out.writeUTF("Servidor: conexión cerrada. ¡Adiós!");
                out.flush();

                serverOn = false;
            }
        } catch (IOException e) {
            System.out.println("Error en el servidor: " + e.getMessage());
        }
    }
}
