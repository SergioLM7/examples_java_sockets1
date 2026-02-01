package ejercicio2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        final String HOST = "127.0.0.1";
        final int PORT = 5001;

        DataInputStream in;
        DataOutputStream out;
        String message = "";
        String serverMessage = "";


        try (Socket socket = new Socket(HOST, PORT);
        Scanner sc = new Scanner(System.in)) {

            System.out.println("Conectado al servidor");

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());


            do {
                System.out.println("Escribe el message que quieras enviar al servidor");
                message = sc.nextLine();

                out.writeUTF(message);
                System.out.println("Enviando message al servidor...");

                serverMessage = in.readUTF();
                System.out.println("Mensaje recibido desde el servidor: " + serverMessage);

            }while(!message.equalsIgnoreCase("salir"));

            serverMessage = in.readUTF();
            System.out.println("Mensaje recibido desde el servidor: " + serverMessage);
            System.out.println("Cerrando conexión con el servidor");

        } catch (IOException e) {
            System.out.println("No se puede establecer la conexión: " + e.getMessage());
        }


    }
}
