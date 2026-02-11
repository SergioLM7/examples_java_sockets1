package chat_tcp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        final int PORT = 5001;
        boolean serverRunning = true;

        System.out.println("=== Servidor CHAT TCP ===");

        try (ServerSocket serverSocket = new ServerSocket(PORT);
                Socket socket = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                //Enviará el mensaje inmediatamente cuando configuramos el autoFlush a true
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                Scanner sc = new Scanner(System.in)
        ) {
            System.out.println("Cliente conectado correctamente.");

            //Hilo receptor de los mensajes del Cliente
            Thread receptor = new Thread(() -> {

                boolean listening = true;

                //Leemos línea a línea hasta que se acaben las que contiene el BufferedReader
                try {
                    String message;

                    while (listening && (message = in.readLine()) != null) {
                        System.out.println("\nCliente: " + message);
                        System.out.println("Tú: ");

                        //Finalizamos el hilo si el cliente envía "exit"
                        if (message.equalsIgnoreCase("exit")) {
                            listening = false;
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Servidor | Error al leer el mensaje del cliente: " + e.getMessage());
                }
            });

            //Aquí ejecutamos el hilo
            receptor.start();

            //Mientras el servidor esté activo
            while(serverRunning) {
                System.out.println("Tú: ");
                String serverMessage = sc.nextLine();

                //Se envía mensaje al cliente
                out.println(serverMessage);

                //Si el servidor escribe "exit", se cierra el chat
                if(serverMessage.equalsIgnoreCase("exit")) {
                    serverRunning = false;
                }
            }

            System.out.println("Servidor CHAT finalizado");

        } catch (IOException e) {
            System.out.println("Servidor | Error: " + e.getMessage());
        }
    }
}
