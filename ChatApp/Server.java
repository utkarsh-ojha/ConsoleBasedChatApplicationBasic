import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {


    ServerSocket server;
    Socket socket;

    BufferedReader br;
    PrintWriter out;

    public Server() {
        try {
            server = new ServerSocket(7777);
            System.out.println("Server is ready to accept connection");
            System.out.println("Waiting...");

            socket = server.accept();
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();


        } catch (IOException e) {
//            throw new RuntimeException(e);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server();
    }

    private void startReading() {
        //Thread to read data

        Runnable r1 = () -> {
            System.out.println("Reader started");
            try {
                while (true) {
                    String message = br.readLine();
                    if (message.equals("exit")) {
                        System.out.println("Client terminated the chat");
                        socket.close();
                        break;
                    }
                    System.out.println("Client :" + message);
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
                    if (content.equals("exit")) {
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