package cvut.fit.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main(String[] args) throws IOException {

        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            echoSocket = new Socket("localhost", 8899);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
                    echoSocket.getInputStream()));
        } catch(UnknownHostException e) {
            System.err.println("Don't know about host: localhost.");
            System.exit(1);
        } catch(IOException e) {
            System.err.println("Couldn't get I/O for localhost");
            System.exit(1);
        }
        System.out.println("zadost: " + in.readLine());

        System.out.println("Posilam data1");
        out.print("ahoj");
        out.flush();

        System.out.println("Posilam data2");
        out.print("jak se mas\r\n");
        out.flush();

        System.out.println("zadost: " + in.readLine());

        System.out.println("Posilam data3");
        out.print("test\r\n");
        out.flush();


        out.close();
        in.close();
        echoSocket.close();
    }

}
