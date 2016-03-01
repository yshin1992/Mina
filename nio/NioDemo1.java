import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Channel的实现
这些是Java NIO中最重要的通道的实现：
    FileChannel
    DatagramChannel
    SocketChannel
    ServerSocketChannel
FileChannel 从文件中读写数据。
DatagramChannel 能通过UDP读写网络中的数据。
SocketChannel 能通过TCP读写网络中的数据。
ServerSocketChannel可以监听新进来的TCP连接，像Web服务器那样。对每一个新进来的连接都会创建一个SocketChannel
 */
public class NioDemo1
{

    public static void main(String[] args) throws IOException
    {
        // TODO Auto-generated method stub
        read();
    }

    private static void read() throws IOException
    {
        File f = new File(System.getProperty("user.dir") + "/src/nio-data.txt");
        RandomAccessFile aFile = new RandomAccessFile(f, "rw");
        FileChannel inChannel = aFile.getChannel();

        ByteBuffer buf = ByteBuffer.allocate(48);

        int bytesRead = inChannel.read(buf);
        while (bytesRead != -1)
        {

            System.out.println("Read " + bytesRead);
            buf.flip();//buf.flip() 的调用，首先读取数据到Buffer，然后反转Buffer,接着再从Buffer中读取数据

            while (buf.hasRemaining())
            {
                System.out.print((char) buf.get());
            }

            buf.clear();
            bytesRead = inChannel.read(buf);
        }
        aFile.close();
    }
}
