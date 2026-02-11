package exercise3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {

        final int PORT = 5002;
        boolean serverRunning = true;

        try(ServerSocket server =  new ServerSocket(PORT)) {
            System.out.println("Servidor Multi-Cliente iniciado en puerto " + PORT);

            while(serverRunning) {
                //Esperando a que llegue el cliente
                Socket client = server.accept();
                System.out.println("Cliente conectado: " + client.getInetAddress() + ":" + client.getPort());

                //Creamos un hilo por cada cliente
                Thread thread = new Thread(new ClientManager(client));
                thread.start();
            }

        } catch (IOException e) {
            System.out.println("Server | Error: " + e.getMessage());
        }
    }
}
