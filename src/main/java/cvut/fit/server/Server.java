package cvut.fit.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Server implements Runnable {


    private static final int TIMEOUT = 1000;
    private static final int PORT = 8899;

    @Override
    public void run() {
        try(ServerSocket serverSocket = new ServerSocket(PORT)) {
            serverSocket.setSoTimeout(TIMEOUT);
            Socket clientSocket;

            while(!Thread.interrupted()) {
                try {
                    clientSocket = serverSocket.accept();

                    Thread worker = new Thread(new ServerWorker(clientSocket));
                    System.out.println("New connection:" + clientSocket);
                    worker.start();

                } catch(SocketTimeoutException ex) {
                    continue;
                }
            }

        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
