import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class AuthHandler extends ChannelInboundHandlerAdapter {

    private boolean isAuth = false;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(isAuth){
            ctx.fireChannelRead(msg);
            return;
        }
        try {
            if (msg instanceof AuthMessage) {
                AuthMessage authMessage = (AuthMessage) msg;
            int id = DBConnection.getIdByLoginByLoginAndPassword(authMessage.getLogin(), authMessage.getPassword());
                if (id != 0) {
                    isAuth = true;
                    ctx.pipeline().addLast(new MainHandler(id));
                    ctx.writeAndFlush(new AuthMessage("/authenticated_" + id));
                } else if (authMessage.getMessage().equals("/connection_close")) {
                    isAuth = false;
                    ctx.writeAndFlush(new AuthMessage("/null_userId"));
                } else {
                    ctx.writeAndFlush(new AuthMessage("/null_userId"));
                }
            }
        }
    finally {
        ReferenceCountUtil.release(msg);
    }
}


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
