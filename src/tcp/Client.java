package tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {

        final String HOST = "127.0.0.1";
        final int PORT = 5001;

        //Flujo de entrada
        DataInputStream in;

        //Flujo de salida
        DataOutputStream out;

        //Clase Socket de Java es la que gestionará nuestro Socket
        try (Socket socket = new Socket(HOST, PORT)) {

            //Inicializamos los flujos de entrada y de salida
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            //Mensaje que enviamos al servidor
            out.writeUTF("¡Hola, mundo desde el Cliente!");

            //Variable donde almacenaremos la respuesta del servidor
            String mensaje = in.readUTF();

            //Lo imprimimos por pantalla
            System.out.println(mensaje);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Cliente desconectado.");
    }
}
