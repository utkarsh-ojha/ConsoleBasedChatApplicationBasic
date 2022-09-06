import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    Socket socket;
    BufferedReader br;
    PrintWriter out;

    public Client() {
        try {
            System.out.println("Sending request to server");
            socket = new Socket("127.0.0.1", 7777);
            System.out.println("Connected with the server");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println("This is client");
        new Client();
    }

    private void startReading() {
        //Thread to read data

        Runnable r1 = () -> {
            System.out.println("Reader started");
            try {
                while (true) {

                    String message = br.readLine();
                    if (message.equals("exit")) {
                        System.out.println("Server terminated the chat");
                        socket.close();
                        break;
                    }
                    System.out.println("Server :" + message);
                }

            } catch (IOException e) {
//                throw new RuntimeException(e);
                System.out.println("Connection closed");

            }
        };

        new Thread(r1).start();
    }

    private void startWriting() {
        //Thread to write data to client
        Runnable r2 = () -> {

            System.out.println("Writer started...");
            try {
                while (!socket.isClosed()) {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();
                    if(content.equals("exit"))
                    {
                        socket.close();
                        break;
                    }

                }
            } catch (IOException e) {
//                throw new RuntimeException(e);
                System.out.println("Connection closed");
            }

        };
        new Thread(r2).start();
    }
}
