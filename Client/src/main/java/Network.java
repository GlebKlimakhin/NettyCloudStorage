import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import java.io.IOException;
import java.net.Socket;

public class Network {

    private static Socket socket;
    private static ObjectDecoderInputStream in;
    private static ObjectEncoderOutputStream out;

    static void start(){
        try {
            socket = new Socket("localhost", 9000);
            out = new ObjectEncoderOutputStream(socket.getOutputStream());
            in = new ObjectDecoderInputStream(socket.getInputStream(), 50*1024*1024);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    static void stop(){
        try {
            out.close();
            in.close();
            socket.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static boolean sendMessage(AbstractMessage message){
        try {
            out.writeObject(message);
            return true;
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }

    public static AbstractMessage readObject() throws IOException, ClassNotFoundException {
        Object object = in.readObject();
        return (AbstractMessage) object;
    }

}
