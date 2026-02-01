package ejercicio1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Servidor {
    public static void main(String[] args) {
        final int PORT = 5001;
        DataInputStream in;
        DataOutputStream out;
        String message = "";
        String clientMessage = "";
        boolean serverOn = true;
        Socket client;

        try (ServerSocket serverSocket = new ServerSocket(PORT);
        Scanner sc = new Scanner(System.in)) {
            System.out.println("Servidor CHAT iniciado en el puerto " + PORT);

            while(serverOn) {
                client = serverSocket.accept();

                in = new DataInputStream(client.getInputStream());
                out = new DataOutputStream(client.getOutputStream());

                clientMessage = in.readUTF();
                System.out.println("Mensaje recibido desde el cliente: " + clientMessage);

                System.out.println("Introduce un mensaje para responder:");
                message = sc.nextLine();
                out.writeUTF(message);
                System.out.println("Mensaje enviado al cliente: " + message);
                System.out.println("Cerrando conexión...");
                serverOn = false;
            }

        } catch (IOException e) {
            System.out.println("Error en el servidor: " + e.getMessage());
        }

    }
}
