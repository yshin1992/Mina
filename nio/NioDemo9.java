import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * Java NIO DatagramChannel
*Java NIO中的DatagramChannel是一个能收发UDP包的通道。
*因为UDP是无连接的网络协议，所以不能像其它通道那样读取和写入。它发送和接收的是数据包。
 */
public class NioDemo9
{

    public static void main(String[] args) throws IOException
    {
        // TODO Auto-generated method stub
        DatagramChannel channel = DatagramChannel.open();
        
        channel.socket().bind(new InetSocketAddress(6801));
        
        ByteBuffer buf = ByteBuffer.allocate(128);
        //通过receive()方法从DatagramChannel接收数据
        buf.clear();
        channel.receive(buf);
        
        buf.flip();
        
        while(buf.hasRemaining())
        {
            System.out.print((char)buf.get());
        }
        
        buf.clear();
        String sendStr = "UDP Data";
        buf.put(sendStr.getBytes());
        buf.flip();
        
        //通过send()方法从DatagramChannel发送数据
        channel.send(buf, new InetSocketAddress(6801));
        
        buf.clear();
        
        channel.close();
        //这里当连接后，也可以使用read()和write()方法，就像在用传统的通道一样。只是在数据传送方面没有任何保证。
        //这里省略了read()和write()方法的实例
    }

}
