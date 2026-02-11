package chat_udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        final int PORT = 5001;

        byte[] buffer = new byte[1024];
        boolean active = true;

        System.out.println("=== Servidor CHAT UDP ===");
        System.out.println("Escuchando en el puerto " + PORT + "...");

        try (DatagramSocket socketUDP = new DatagramSocket(PORT);
                Scanner sc = new Scanner(System.in)
        ) {

            InetAddress clientIP = null;
            int clientPORT = -1;

            //Hilo receptor de mensajes del cliente
            Thread receptor = new Thread(() -> {

                boolean listening = true;

                // Buffer local para este hilo (evita problemas de compartir arrays)
                byte[] receptionBuffer = new byte[1024];

                while (listening) {
                    try {
                        //Preparo el paquete que voy a recibir y espero a recibirlo
                        DatagramPacket packetReceived = new DatagramPacket(receptionBuffer, receptionBuffer.length);
                        // receive() se BLOQUEA hasta que llegue un datagrama
                        socketUDP.receive(packetReceived);

                        //Indicamos la longitud del paquete recibido a la hora de transformarlo a String
                        //el offset especifica desde qué posición se va a empezar a leer
                        String message = new String(packetReceived.getData(), 0, packetReceived.getLength());

                        System.out.println("\nCliente: " + message);
                        System.out.println("\nTú: ");

                        // Guardamos IP y puerto del cliente (para responder)
                        // Nota: esto se actualiza cada vez que llega un mensaje
                        // (sirve incluso si cambia el puerto del cliente, aunque no es lo típico)
                        synchronized (Server.class) {
                            // Guardamos datos del remitente
                            // (en UDP no hay "conexión", solo origen/destino de datagramas)
                            // Solo los guardamos para poder contestar
                        }

                        if (message.equalsIgnoreCase("/exit")) {
                            listening = false;
                        }

                    } catch (IOException e) {
                        listening = false;
                        System.out.println("Servidor UDP | Error leyendo del cliente: " + e.getMessage());
                    }
                }
            });

            //Iniciamos el hilo receptor
            receptor.start();

            // =============================
            // BUCLE PRINCIPAL (envía mensajes)
            // =============================

            // Primero: el servidor NO sabe a quién enviar hasta recibir algo.
            // Por eso esperamos el primer mensaje de cliente de forma controlada.
            System.out.println("Espera: para poder enviar, primero el cliente debe mandar un mensaje.");

            while (active) {
                    //En caso de no tener la IP del cliente y/o su puerto, los intentamos obtener
                    if (clientIP == null || clientPORT == -1) {

                        DatagramPacket firstPacket = new DatagramPacket(buffer, buffer.length);
                        socketUDP.receive(firstPacket);

                        String firstMessage = new String(firstPacket.getData(), 0, firstPacket.getLength());
                        clientIP = firstPacket.getAddress();
                        clientPORT = firstPacket.getPort();

                        System.out.println("Cliente: " + firstMessage);
                        System.out.println("Cliente detectado: " + clientIP + ": " + clientPORT);

                        if (firstMessage.equalsIgnoreCase("/exit")) {
                            active = false;
                        }
                    } else {
                        //Sabemos a quién enviar
                        System.out.println("Tú: ");
                        String message = sc.nextLine();
                        byte[] data = message.getBytes();

                        //Creamos el paquete para enviarlo al cliente
                        DatagramPacket packetToSent = new DatagramPacket(data, data.length, clientIP, clientPORT);
                        socketUDP.send(packetToSent);

                        //Si en el servidor escribimos "exit", lo paramos
                        if (message.equalsIgnoreCase("/exit")) {
                            active = false;
                        }
                    }
            }

            System.out.println("Servidor UDP | Chat finalizado");

        } catch (SocketException e) {
                System.out.println("Servidor UDP | Error: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Servidor UDP | Error de lectura/escritura: " + e.getMessage());
            }
    }
}