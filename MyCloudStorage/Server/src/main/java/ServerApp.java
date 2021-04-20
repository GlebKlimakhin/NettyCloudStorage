import java.io.IOException;
import java.net.ServerSocket;

public class ServerApp {

    private static final int PORT = 8189;



    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
