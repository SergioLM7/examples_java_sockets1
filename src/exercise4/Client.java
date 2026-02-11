package exercise4;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        final String HOST = "127.0.0.1";
        final int PORT = 5050;

        try(Scanner sc = new Scanner(System.in);
        Socket socket = new Socket(HOST, PORT);
        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());) {

            System.out.println(inputStream.readUTF());
            String user = sc.nextLine();
            outputStream.writeUTF(user);

            System.out.println(inputStream.readUTF());
            String pass =  sc.nextLine();
            outputStream.writeUTF(pass);

            String state = inputStream.readUTF(); //OK o denegado
            System.out.println("Servidor: " + state);

            boolean keepGoing = state.equals("OK");

            while(keepGoing) {
                String menu =  inputStream.readUTF();
                System.out.println(menu);

                String option = sc.nextLine();
                outputStream.writeUTF(option);

                String answer =  inputStream.readUTF();
                System.out.println(answer);

                if(option.equals("3")) {
                    keepGoing = false;
                }
            }
        } catch (UnknownHostException ex) {
            System.out.println("Cliente | Error en el servidor: " + ex.getMessage());
        } catch (IOException e) {
            System.out.println(" Cliente | Error: "  + e.getMessage());
        }

    }
}
