package cvut.fit;

import cvut.fit.server.Server;

public class Application {

    public static void main(String[] args) {

        System.out.println("Server is running:");
        Server server = new Server();
        server.run();

    }
}
