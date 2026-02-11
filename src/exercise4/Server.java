package exercise4;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Server {
    public static void main(String[] args) {

        final int PORT = 5050;

        Map<String, String> users = new HashMap<>();
        //Mini BBDD de usuarios con contraseñas
        users.put("admin", "1234");
        users.put("user", "5431");
        users.put("user2", "1333");

        Random rand = new Random();

        try (ServerSocket serverSocket = new ServerSocket(PORT);) {
            System.out.println("Login iniciado en el puerto " + PORT);

            while (true) {
                try(Socket socket = serverSocket.accept();
                        DataInputStream input = new DataInputStream(socket.getInputStream());
                        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                ) {
                    System.out.println("Cliente conectado: " + socket.getInetAddress() + ":" + socket.getPort());

                    output.writeUTF("LOGIN: introduce tu usuario");
                    String user =  input.readUTF();
                    output.writeUTF("LOGIN: introduce tu contraseña");
                    String pass =  input.readUTF();

                    boolean loginOK = users.containsKey(user) && users.get(user).equals(pass);

                    if(!loginOK){
                        output.writeUTF("DENEGADO");
                        System.out.println("Login fallido para usuario: " + user);
                    } else {
                        output.writeUTF("OK");
                        System.out.println("Login correcto para usuario: " + user);

                        boolean onSession = true;

                        while(onSession){
                            output.writeUTF("MENU:\n"
                                    + "1)Hora\n"
                                    + "2)Random\n"
                                    + "3)Salir\n"
                                    + "Elige una opción de la 1 a la 3");

                            String option = input.readUTF();

                            if(option.equals("1")){
                                LocalDateTime now =  LocalDateTime.now();
                                DateTimeFormatter dtm =  DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                                output.writeUTF("HORA SERVIDOR: " + dtm.format(now));
                            } else if (option.equals("2")){
                                int num = rand.nextInt(100) + 1;
                                output.writeUTF("NÚMERO ALEATORIO (1-100): " + num);
                            } else if(option.equals("3")){
                                output.writeUTF("SESIÓN CERRADA: HASTA LA PRÓXIMA");
                                onSession = false;
                            } else {
                                output.writeUTF("OPCIÓN INVÁLIDA: debes escribir 1, 2 o 3");
                            }
                        }
                    }
                }
                System.out.println("Fin de atención al cliente");
            }
        } catch (IOException e) {
            System.out.println("Servidor | Error : " + e.getMessage());
        }

    }
}
