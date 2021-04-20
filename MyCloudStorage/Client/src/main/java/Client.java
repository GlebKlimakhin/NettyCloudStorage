import java.io.IOException;
import java.net.Socket;
public class Client {
    private final int PORT = 9000;
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 9000);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
