import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.file.Files;
import java.nio.file.Paths;

public class RegHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof RegMessage){
            RegMessage regMessage = (RegMessage) msg;
            int id = DBConnection.getIdByLoginByLoginAndPassword(regMessage.getLogin(), regMessage.getPassword());
            if(id != 0){
                RegMessage registered = new RegMessage("/not_null_user_id");
                ctx.writeAndFlush(registered);
            }
            else {
                DBConnection.registration(regMessage.getLogin(), regMessage.getPassword());
                Files.createDirectory(Paths.get("server_" + regMessage.getLogin()));
                RegMessage success = new RegMessage("/registration_success " + regMessage.getLogin());
                ctx.writeAndFlush(success);
            }
            return;
        }
        ctx.fireChannelRead(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
