package cvut.fit.server;

import cvut.fit.exception.LogicException;
import cvut.fit.exception.SyntaxErrorException;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.regex.Pattern;

public class Util {

    public int stringToAscii(String string) {
        int sum = 0;
        for(int i = 0; i < string.length(); i++) {
            sum += (int) string.charAt(i);
        }
        return sum;
    }

    public Pair parseCoordinates(String input) {
        String data = input.substring(3, input.length());
        String[] parts = data.split(" ");

        return new Pair(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }

    public boolean validateClientInput(String input) {
        if(!Pattern.matches("((OK -?\\d+ -?\\d+)|RECHARGING|FULL POWER)", input)) {
            return false;
        }
        return true;
    }

    public String readByChar(Socket clientSocket, BufferedReader in, int timeout) throws SyntaxErrorException, LogicException, IOException {
        String buffer = "";
        int input;

        clientSocket.setSoTimeout(timeout);
        while((input = in.read()) != -1) {

            buffer += (char) input;

            if(buffer.endsWith("\r\n")) {
                buffer = buffer.substring(0, buffer.length() - 2);
                break;
            }

            if((buffer.length() > 99) ||
                    ((buffer.startsWith("OK") || buffer.startsWith("RECHARGING") || buffer.startsWith("FULL POWER")) && buffer.length() > 12)) {
                throw new SyntaxErrorException();
            }
        }

        if(buffer.equals("RECHARGING")) {

            String fullPower = readByChar(clientSocket, in, 5000);

            if(!fullPower.equals("FULL POWER")) {
                throw new LogicException();
            }

            buffer = readByChar(clientSocket, in, 1000);

            System.out.println(buffer);
        }

        System.out.println("full message:" + buffer + "|");

        return buffer;
    }
}
