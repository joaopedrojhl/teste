import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.io.*;

public class Cliente {
    // endereço IP do servidor e porta
    private static final String SERVER_ADDRESS = "10.130.129.103";
    private static final int SERVER_PORT = 5000;

    public static void main(String[] args) throws ClassNotFoundException {
        try {
            // conectar servidor-socket
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);

            // entrada e saída
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            // ler entradas do usuário
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Connected to the server.");

            // pega e envia o nome do usuário para o servidor
            System.out.print("Enter your username: ");
            String username = stdIn.readLine();
            System.out.println("username: " + username);
            out.writeObject(username);
            out.flush();  // Assegura que o dado foi enviado

            // ler mensagens do servidor e mostar
            Thread readerThread = new Thread(() -> {
                try {
                    String serverMessage;
                    // Loop para mensagens
                    while ((serverMessage = (String) in.readObject()) != null) {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
            readerThread.start();  // thread para ler
            
            String inputLine;
            // loop para ler entradas do usuário e enviar
            while ((inputLine = stdIn.readLine()) != null) {
                out.writeObject(inputLine + "\n");
                out.flush();  
                // confirma envio
            }

            // fecha tudo
            in.close();
            out.close();
            stdIn.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
