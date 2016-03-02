import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * SocketChannel
 * Java NIO中的SocketChannel是一个连接到TCP网络套接字的通道
 * 
 * 可以通过以下2种方式创建SocketChannel：
 * 
   1. 打开一个SocketChannel并连接到互联网上的某台服务器。
   2. 一个新连接到达ServerSocketChannel时，会创建一个SocketChannel。
 */
public class NioDemo7
{

    public static void main(String[] args) throws IOException, InterruptedException
    {
        // TODO Auto-generated method stub
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(6801));
        
        ByteBuffer buf = ByteBuffer.allocate(128);
        
        while(!socketChannel.finishConnect())
        {
            System.out.println("connect to remote server");
            Thread.sleep(500);
        }
        Selector selector = Selector.open();
        
        socketChannel.configureBlocking(false);
        SelectionKey key = socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        
        int selectResult = selector.select();
        
        Set<SelectionKey> selectedKey = selector.selectedKeys();
        
        Iterator<SelectionKey> keyIter = selectedKey.iterator();
        
        while(keyIter.hasNext())
        {
            SelectionKey keytemp = (SelectionKey) keyIter.next();
//            if(keytemp.isWritable())
//            {
//                buf.clear();
//                String sendStr = "TCP Data";
//                buf.put(sendStr.getBytes());
//                buf.flip();
//                socketChannel.write(buf);
//            }
            if(keytemp.isReadable())
            {
                buf.clear();
                int bytesRead = socketChannel.read(buf);
                buf.flip();
                System.out.println(bytesRead);
                while(buf.hasRemaining())
                {
                    System.out.print((char)buf.get());
                }
                System.out.println();
            }
        }
        
        selector.close();
        
        socketChannel.close();
    }

}
