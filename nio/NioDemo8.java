import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * ServerSocketChannel
 * Java NIO中的 ServerSocketChannel 是一个可以监听新进来的TCP连接的通道, 就像标准IO中的ServerSocket一样
 */
public class NioDemo8
{

    public static void main(String[] args) throws IOException
    {
        // TODO Auto-generated method stub
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(6801));
        String defaultStr = "7E2323070100013030303030303030303030303031303037000000317E";
        
        Selector selector = Selector.open();
        
        ByteBuffer buf = ByteBuffer.allocate(128);
        while(true)
        {
            //当 accept()方法返回的时候,它返回一个包含新进来的连接的 SocketChannel。因此, accept()方法会一直阻塞到有新连接到达。
            SocketChannel socketChannel = serverSocketChannel.accept();//阻塞式的
            
            if(socketChannel.finishConnect())
            {
                socketChannel.configureBlocking(false);
                SelectionKey key = socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                
                int selectResult = selector.select();
                
                Set<SelectionKey> selectedKey = selector.selectedKeys();
                
                Iterator<SelectionKey> keyIter = selectedKey.iterator();
                
                while(keyIter.hasNext())
                {
                    SelectionKey keytemp = (SelectionKey) keyIter.next();
                    if(keytemp.isReadable())
                    {
                        System.out.println("Receive Data:");
                        buf.clear();
                        socketChannel.read(buf);
                        buf.flip();
                        while(buf.hasRemaining())
                        {
                            System.out.print((char)buf.get());
                        }
                        System.out.println();
                    }
                    else if(keytemp.isWritable())
                    {
                        System.out.println("New Connect is established!");
                        buf.clear();
                        buf.put(defaultStr.getBytes());
                        buf.flip();
                        while(buf.hasRemaining())
                        {
                            socketChannel.write(buf);
                        }
                    }
                    keyIter.remove();
                }
            }
        }
    
    }
    

}
