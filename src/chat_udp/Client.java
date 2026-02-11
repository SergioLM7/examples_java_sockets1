package chat_udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {

        // IP del servidor (localhost si es en el mismo PC)
        final String SERVER_HOST = "127.0.0.1";

        // Puerto del servidor al que vamos a enviar datagramas
        final int SERVER_PORT= 5001;

        // Buffer para recibir
        byte[] bufferReceptor = new byte[1024];

        // Control del bucle (sin break)
        boolean active = true;

        System.out.println("=== CLIENTE CHAT UDP ===");

        // Creamos socket UDP del cliente
        // Sin puerto => el SO le asigna uno libre automáticamente
        try (DatagramSocket socketUDP = new DatagramSocket();
                Scanner scn = new Scanner(System.in)){
            // Resolvemos el nombre/IP del servidor a InetAddress
            InetAddress serverIP = InetAddress.getByName(SERVER_HOST);

            System.out.println("Cliente UDP listo. Escribe mensajes. /exit para salir.");

            // =============================
            // HILO RECEPTOR (recibe mensajes del servidor)
            // =============================
            Thread receptor = new Thread(() -> {

                boolean listening = true;

                while (listening) {
                    try {
                        DatagramPacket packetReceived =
                                new DatagramPacket(bufferReceptor, bufferReceptor.length);

                        // Espera a recibir un datagrama
                        socketUDP.receive(packetReceived);

                        String msg = new String(packetReceived.getData(), 0, packetReceived.getLength());

                        System.out.println("\nServidor: " + msg);
                        System.out.print("Tú: ");

                        if (msg.equalsIgnoreCase("/exit")) {
                            listening = false;
                        }

                    } catch (IOException e) {
                        listening = false;
                        System.out.println("\n[Cliente UDP] Error recibiendo.");
                    }
                }
            });

            receptor.start();

            // =============================
            // BUCLE PRINCIPAL (envía mensajes al servidor)
            // =============================
            while (active) {

                System.out.print("Tú: ");
                String msg = scn.nextLine();

                byte[] data = msg.getBytes();

                // Creamos datagrama con destino IP:PUERTO del servidor
                DatagramPacket packetToSent =
                        new DatagramPacket(data, data.length, serverIP, SERVER_PORT);

                // Enviamos
                socketUDP.send(packetToSent);

                // Si escribimos /exit, terminamos el bucle
                if (msg.equalsIgnoreCase("/exit")) {
                    active = false;
                }
            }

            System.out.println("[Cliente UDP] Chat finalizado.");

        } catch (IOException e) {
            System.out.println("[Cliente UDP] Error: " + e.getMessage());
        }
    }
}
