package cvut.fit.server;

public class Message {

    public static String SERVER_USER = "100 LOGIN\r\n";
    public static String SERVER_PASSWORD = "101 PASSWORD\r\n";

    public static String SERVER_OK = "200 OK\r\n";
    public static String SERVER_LOGIN_FAILED = "300 LOGIN FAILED\r\n";

    public static String SERVER_MOVE = "102 MOVE\r\n";
    public static String SERVER_TURN_LEFT = "103 TURN LEFT\r\n";
    public static String SERVER_TURN_RIGHT = "104 TURN RIGHT\r\n";
    public static String SERVER_MESSAGE = "105 GET MESSAGE\r\n";
    public static String SERVER_SYNTAX_ERROR = "301 SYNTAX ERROR\r\n";
    public static String SERVER_LOGIC_ERROR = "302 LOGIC ERROR\r\n";

}
