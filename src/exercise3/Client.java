package exercise3;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {

        final String HOST = "127.0.0.1";
        final int PORT = 5002 ;

        boolean clientIsActive = true;

        try (Socket socket = new Socket(HOST, PORT);
        Scanner sc = new Scanner(System.in);
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

            System.out.println(input.readUTF());

            while(clientIsActive) {
                System.out.println("> ");
                String message = sc.nextLine();

                output.writeUTF(message);

                String answer = input.readUTF();
                System.out.println(answer);

                if(message.equalsIgnoreCase("salir")){
                    clientIsActive = false;
                }
            }
        } catch (UnknownHostException e) {
            System.out.println("Cliente | Error con el Host: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Cliente | Error: " + e.getMessage());
        }
    }
}
