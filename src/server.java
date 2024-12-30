import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

public class server {

    int totalCon = 0;
    private void connectToClient() {
        try {
            ServerSocket serverSocket = new ServerSocket(9999);
            System.out.println("Server started at " + new Date() + "\n");

            while (true) {
                Socket connectToClient = serverSocket.accept();
                totalCon++;


                InetSocketAddress socketAddress = (InetSocketAddress) connectToClient.getRemoteSocketAddress();
                String clientIpAddress = socketAddress.getAddress().getHostAddress();

                System.out.println("New connection made with player. Total connections = " + totalCon + "\n" +
                                "IP Address is " + clientIpAddress + "\n");

                // Create and start a new thread for the connection
                new Thread(new HandleAClient(connectToClient)).start();
            }

        }
        catch (IOException e) {
            System.err.println(e);
        }
    }

    class HandleAClient implements Runnable {
        private Socket socket; // A connected socket

        public HandleAClient(Socket socket) {
            this.socket = socket;
        }

        /**
         * Runs a thread to handle client requests.
         */
        public void run() {
            try {
                ObjectInputStream isFromClient = new ObjectInputStream(socket.getInputStream());
                DataOutputStream osToClient = new DataOutputStream(socket.getOutputStream());

            } catch (IOException e) {
                System.err.println("Error handling client: " + e.getMessage());
                totalCon--;
            } finally {
                try {
                    socket.close();
                } catch (IOException ex) {
                    System.err.println("Error closing socket: " + ex.getMessage());
                }
            }
        }

    }

    public static void main(String[] args){
        server serverInstance = new server();
        serverInstance.connectToClient();
    }
}
