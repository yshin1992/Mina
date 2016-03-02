import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;


public class NIOServer
{
    //标识数字
    private static int flag = 0;

    //缓冲区大小
    private static int BLOCK = 1024;
    
    private ByteBuffer sendBuffer = ByteBuffer.allocate(BLOCK);
    
    private ByteBuffer receiveBuffer = ByteBuffer.allocate(BLOCK);
    
    private Selector selector;
    
    public NIOServer(int port) throws IOException
    {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //服务器配置为非阻塞
        serverSocketChannel.configureBlocking(false);
        
        ServerSocket serverSocket = serverSocketChannel.socket();
        
        serverSocket.bind(new InetSocketAddress(port));
        
        selector = Selector.open();
        //注册serverSocketChannel到selector
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        
        System.out.println("server start ------");
    }
    
    public void listen() throws IOException
    {
        while(true)
        {
            //选择至少一个打开的通道
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIter = selectedKeys.iterator();
            while(keyIter.hasNext())
            {
                SelectionKey selectionKey = keyIter.next();
                
                handleKey(selectionKey);
                
                keyIter.remove();//必须
            }
            
        }
    }
    
    private void handleKey(SelectionKey key) throws IOException
    {
        ServerSocketChannel server = null;
        SocketChannel client = null;
        
        String receiveText;
        String sendText;
        
        int count = 0;
        if(key.isAcceptable())
        {
            server = (ServerSocketChannel) key.channel();
            client = server.accept();
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ);
        }
        else if(key.isReadable())
        {
            client = (SocketChannel) key.channel();
            receiveBuffer.clear();
            count = client.read(receiveBuffer);
            if(count > 0)
            {
                receiveText = new String(receiveBuffer.array(),0,count);
                System.out.println("服务器端接收数据 --"+receiveText);
                client.register(selector, SelectionKey.OP_WRITE);
            }
        }
        else if(key.isWritable())
        {
            sendBuffer.clear();
            client = (SocketChannel) key.channel();
            sendText = "message from server -- "+flag++;
            sendBuffer.put(sendText.getBytes());
            sendBuffer.flip();
            client.write(sendBuffer);
            System.out.println("服务器端向客户端发送数据-- : "+sendText);
            client.register(selector, SelectionKey.OP_READ);
        }
    }
    
    public static void main(String[] args) throws IOException
    {
        // TODO Auto-generated method stub
        NIOServer server = new NIOServer(6801);
        server.listen();
    }

}
