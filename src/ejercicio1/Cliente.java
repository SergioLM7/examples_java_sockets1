package ejercicio1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {

        final int PORT = 5001;
        final String HOST = "localhost";
        DataInputStream in;
        DataOutputStream out;
        String message = "";
        String serverMessage = "";

        try (Socket socket = new Socket(HOST, PORT);
        Scanner sc = new Scanner(System.in);) {
            System.out.println("Conectado con el servidor");

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            System.out.println("Escribe un mensaje para enviar:");
            message = sc.nextLine();

            out.writeUTF(message);
            System.out.println("Mensaje enviado al servidor: " + message);

            serverMessage = in.readUTF();
            System.out.println("Mensaje recibido del servidor: " + serverMessage);

            System.out.println("Conexión terminada con el servidor.");

        } catch (IOException e) {
            System.out.println("No se pudo conectar con el servidor: " + e.getMessage());
        }

    }
}
