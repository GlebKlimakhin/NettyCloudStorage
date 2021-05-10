import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
public class MainHandler extends ChannelInboundHandlerAdapter {

    private int id;


    public MainHandler(int id) {
        this.id = id;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.writeAndFlush(new AuthMessage());

        if(msg == null){
            return;
        }
        if(msg instanceof DownloadRequestMessage){
            DownloadRequestMessage downloadMessage = (DownloadRequestMessage) msg;
            if(Files.exists(Paths.get("serverFiles_" + id + "/" + downloadMessage.getFilename()))) {
                FileMessage fileMessage = new FileMessage(Paths.get("serverFiles" + id + "/" +
                        downloadMessage.getFilename()));
                ctx.writeAndFlush(fileMessage);
            }
            if(msg instanceof FileMessage){
                FileMessage fileMessage = (FileMessage) msg;
                Files.write(Paths.get("serverFiles_" + id + "/" + fileMessage.getFilename()),
                        fileMessage.getData(), StandardOpenOption.CREATE);
                refreshServerListView(ctx);
            }
            if(msg instanceof RefreshServerFilesListMessage){
                refreshServerListView(ctx);
            }
            if(msg instanceof DeleteRequestMessage){
                DeleteRequestMessage deleteMessage = (DeleteRequestMessage) msg;
                Files.delete(Paths.get("serverFiles_" + id + "/" + deleteMessage.getFilename()));
                refreshServerListView(ctx);
            }


        }
    }


    private void refreshServerListView(ChannelHandlerContext ctx) throws IOException{
        ArrayList<String> serverFilesList = new ArrayList<>();
        try {
            Files.list(Paths.get("server_" + id + "/"))
                    .map(path -> path.getFileName().toString())
                    .forEach(serverFilesList::add);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ctx.writeAndFlush(new RefreshServerFilesListMessage(serverFilesList));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
