package chat_tcp;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        final String HOST = "127.0.0.1";
        final int PORT = 5001;

        boolean clientIsActive = true;

        System.out.println("=== Cliente CHAT TCP ===");

        try (Socket socket = new Socket(HOST, PORT);
                Scanner sc = new Scanner(System.in);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                //Enviará el mensaje inmediatamente cuando configuramos el autoFlush a true
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        ) {
            System.out.println("Conectando con el servidor...");

            //Hilo receptor para los mensajes del servidor
            Thread receptor = new Thread(() -> {

                boolean listenning = true;
                String message;

                try {
                    while(listenning && (message = in.readLine()) != null) {
                        System.out.println("\nServidor: "+ message);
                        System.out.println("Tú: ");

                        if(message.equalsIgnoreCase("exit")){
                            listenning = false;
                        }

                    }
                } catch (IOException e) {
                    System.out.println("Cliente | Error al leer del servidor:  " + e.getMessage());
                }
            });

            //Iniciamos el hilo
            receptor.start();

            while(clientIsActive) {
                System.out.println("Tú: ");
                String message = sc.nextLine();

                out.println(message);

                if(message.equalsIgnoreCase("exit")){
                    clientIsActive = false;
                }
            }

            System.out.println("Cliente CHAT finalizado");

        } catch (UnknownHostException e) {
            System.out.println("Cliente | Error con el Host: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Cliente | Error: " + e.getMessage());
        }
    }
}
