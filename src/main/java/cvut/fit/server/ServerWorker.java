package cvut.fit.server;

import cvut.fit.exception.AuthorizeException;
import cvut.fit.exception.LogicException;
import cvut.fit.exception.SyntaxErrorException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerWorker implements Runnable {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private Util util;


    public ServerWorker(Socket clientSocket) {
        this.clientSocket = clientSocket;
        util = new Util();
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch(IOException e) {
            e.printStackTrace();
        }

        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void authorize() throws IOException, AuthorizeException, SyntaxErrorException, LogicException {
        //username
        out.print(Message.SERVER_USER);
        out.flush();
        String userName = util.readByChar(clientSocket, in, 1000);

        //password
        out.print(Message.SERVER_PASSWORD);
        out.flush();
        String password = util.readByChar(clientSocket, in, 1000);

        try {
            if(util.stringToAscii(userName) == Integer.parseInt(password)) {
                out.print(Message.SERVER_OK);
                out.flush();
            } else {
                throw new AuthorizeException();
            }
        } catch(NumberFormatException ex) {
            throw new AuthorizeException();
        }

    }

    private void navigate() throws IOException, SyntaxErrorException, LogicException {
        out.print(Message.SERVER_MOVE);
        out.flush();

        String input = util.readByChar(clientSocket, in, 1000);

        if(input.isEmpty()) {
            throw new IOException();
        }

        if(!util.validateClientInput(input)) {
            throw new SyntaxErrorException();
        }

        Pair coordinates = util.parseCoordinates(input);
        Robot robot = new Robot(coordinates.x, coordinates.y, clientSocket);

        System.out.println(robot);

        out.print(Message.SERVER_MOVE);
        out.flush();

        input = (util.readByChar(clientSocket, in, 1000));
        coordinates = util.parseCoordinates(input);

        if(!util.validateClientInput(input)) {
            throw new SyntaxErrorException();
        }

        boolean result = robot.initDirection(coordinates.x, coordinates.y);
        while(!result) {
            out.print(Message.SERVER_MOVE);
            out.flush();

            input = util.readByChar(clientSocket, in, 1000);
            coordinates = util.parseCoordinates(input);

            if(!util.validateClientInput(input)) {
                throw new SyntaxErrorException();
            }

            result = robot.initDirection(coordinates.x, coordinates.y);
        }

        robot.navigate(in, out);
    }


    private void receiveMessage() throws IOException, SyntaxErrorException, LogicException {
        out.print(Message.SERVER_MESSAGE);
        out.flush();

        util.readByChar(clientSocket, in, 1000);

        out.print(Message.SERVER_OK);
        out.flush();
    }

    @Override
    public void run() {
        try {
            authorize();
            navigate();
            receiveMessage();
        } catch(AuthorizeException e) {
            out.print(Message.SERVER_LOGIN_FAILED);
            out.flush();
        } catch(IOException e) {
            System.out.println("Server error");
        } catch(SyntaxErrorException | NumberFormatException e) {
            out.print(Message.SERVER_SYNTAX_ERROR);
            out.flush();
        } catch(LogicException e) {
            out.print(Message.SERVER_LOGIC_ERROR);
            out.flush();
        } finally {
            closeConnection();
        }
    }

    private void closeConnection() {
        try {
            in.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
        out.close();
        try {
            clientSocket.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

}
