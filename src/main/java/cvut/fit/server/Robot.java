package cvut.fit.server;

import cvut.fit.exception.LogicException;
import cvut.fit.exception.SyntaxErrorException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Robot {

    private int x, y;
    private Direction direction;
    private BufferedReader in;
    private PrintWriter out;
    private Socket clientSocket;
    private Util util;

    public Robot(int x, int y, Socket clientSocket) {
        this.x = x;
        this.y = y;
        this.clientSocket = clientSocket;

        direction = null;
        util = new Util();
    }

    public boolean initDirection(int xNew, int yNew) {
        if(xNew == x && yNew == y) {
            return false;
        }

        if(xNew > x) {
            direction = Direction.RIGHT;
        } else if(xNew < x) {
            direction = Direction.LEFT;
        } else if(yNew > y) {
            direction = Direction.UP;
        } else if(yNew < y) {
            direction = Direction.DOWN;
        }

        x = xNew;
        y = yNew;

        return true;
    }

    public void navigate(BufferedReader in, PrintWriter out) throws IOException, SyntaxErrorException, LogicException {
        this.in = in;
        this.out = out;

        System.out.println(this);

        while(x != 0 || y != 0) {
            if(direction == Direction.UP) {
                if(absoluteValue(0, 1) < absoluteValue(0, 0)) {

                } else if(absoluteValue(1, 0) < absoluteValue(0, 0)) {
                    turnRight();
                    readNewPosition();
                } else if(absoluteValue(-1, 0) < absoluteValue(0, 0)) {
                    turnLeft();
                    readNewPosition();
                } else if(absoluteValue(0, -1) < absoluteValue(0, 0)) {
                    turnLeft();
                    readNewPosition();
                    turnLeft();
                    readNewPosition();
                }

            } else if(direction == Direction.DOWN) {
                if(absoluteValue(0, -1) < absoluteValue(0, 0)) {

                } else if(absoluteValue(-1, 0) < absoluteValue(0, 0)) {
                    turnRight();
                    readNewPosition();
                } else if(absoluteValue(1, 0) < absoluteValue(0, 0)) {
                    turnLeft();
                    readNewPosition();
                } else if(absoluteValue(0, 1) < absoluteValue(0, 0)) {
                    turnLeft();
                    readNewPosition();
                    turnLeft();
                    readNewPosition();
                }

            } else if(direction == Direction.LEFT) {
                if(absoluteValue(-1, 0) < absoluteValue(0, 0)) {

                } else if(absoluteValue(0, 1) < absoluteValue(0, 0)) {
                    turnRight();
                    readNewPosition();
                } else if(absoluteValue(0, -1) < absoluteValue(0, 0)) {
                    turnLeft();
                    readNewPosition();
                } else if(absoluteValue(1, 0) < absoluteValue(0, 0)) {
                    turnLeft();
                    readNewPosition();
                    turnLeft();
                    readNewPosition();
                }

            } else if(direction == Direction.RIGHT) {
                if(absoluteValue(1, 0) < absoluteValue(0, 0)) {

                } else if(absoluteValue(0, -1) < absoluteValue(0, 0)) {
                    turnRight();
                    readNewPosition();
                } else if(absoluteValue(0, 1) < absoluteValue(0, 0)) {
                    turnLeft();
                    readNewPosition();
                } else if(absoluteValue(-1, 0) < absoluteValue(0, 0)) {
                    turnLeft();
                    readNewPosition();
                    turnLeft();
                    readNewPosition();
                }
            }

            out.print(Message.SERVER_MOVE);
            out.flush();
            readNewPosition();
        }


    }

    private void moveRight() {
        if(direction == Direction.UP) {
            direction = Direction.RIGHT;
        } else if(direction == Direction.RIGHT) {
            direction = Direction.DOWN;
        } else if(direction == Direction.DOWN) {
            direction = Direction.LEFT;
        } else if(direction == Direction.LEFT) {
            direction = Direction.UP;
        }
    }

    private void moveLeft() {
        if(direction == Direction.UP) {
            direction = Direction.LEFT;
        } else if(direction == Direction.RIGHT) {
            direction = Direction.UP;
        } else if(direction == Direction.DOWN) {
            direction = Direction.RIGHT;
        } else if(direction == Direction.LEFT) {
            direction = Direction.DOWN;
        }
    }

    private void turnRight() {
        out.print(Message.SERVER_TURN_RIGHT);
        moveRight();
        out.flush();
    }

    private void turnLeft() {
        out.print(Message.SERVER_TURN_LEFT);
        moveLeft();
        out.flush();
    }

    private void readNewPosition() throws IOException, SyntaxErrorException, LogicException {
        String response = util.readByChar(clientSocket, in, 1000);
        Pair pair = util.parseCoordinates(response);

        x = pair.x;
        y = pair.y;
        System.out.println(this);
    }

    private int absoluteValue(int deltaX, int deltaY) {
        return Math.abs(x + deltaX) + Math.abs(y + deltaY);
    }

    public boolean isInCenter() {
        if(x == 0 && y == 0) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Robot{" +
                "direction=" + direction +
                ", y=" + y +
                ", x=" + x +
                '}';
    }
}
