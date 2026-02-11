package exercise3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientManager implements Runnable {

    private final Socket clientSocket;

    public ClientManager(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {

        boolean connected = true;

        try(Socket sc = clientSocket;
                DataInputStream in = new DataInputStream(sc.getInputStream());
                DataOutputStream out = new DataOutputStream(sc.getOutputStream())) {

            //Mensaje para cada usuario una vez se conecte al servidor
            out.writeUTF("Servidor: ¡Bienvenido! Escribe 'salir' para cerrar.");

            while(connected) {
                //Espera mensaje del cliente
                String message = in.readUTF();
                System.out.println("[" + sc.getInetAddress() + ":" + sc.getPort() + "] " + message);

                if(message.equalsIgnoreCase("salir")) {
                    out.writeUTF("Servidor: conexión cerrada. ¡Adiós!");
                    connected = false;
                } else {
                    out.writeUTF("Servidor(eco): " + message);
                }
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("Cliente desconectado: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
    }
}
