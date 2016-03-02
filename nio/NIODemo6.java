import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/***
 * FileChannel
 * Java NIO中的FileChannel是一个连接到文件的通道。可以通过文件通道读写文件
 * FileChannel无法设置为非阻塞模式，它总是运行在阻塞模式下。
 */
public class NioDemo6
{

    public static void main(String[] args) throws IOException
    {
        // TODO Auto-generated method stub
        fileChannel();
    }
    
    public static void fileChannel() throws IOException
    {
        //在使用FileChannel之前，必须先打开它。但是，我们无法直接打开一个FileChannel，
        //需要通过使用一个InputStream、OutputStream或RandomAccessFile来获取一个FileChannel实例
        RandomAccessFile aFile = new RandomAccessFile(new File("D:\\win.png"), "rw");
        FileChannel fChannle = aFile.getChannel();
        
        //FileChannel的size()方法
        long fileSize = fChannle.size();
        System.out.println(fileSize);
        
        RandomAccessFile fileTo = new RandomAccessFile(new File("F:\\win2.png"), "rw");
        FileChannel fChannle2 = fileTo.getChannel();
        //调用多个read()方法之一从FileChannel中读取数据
        ByteBuffer buf = ByteBuffer.allocate(1024);
        
        //read()方法返回的int值表示了有多少字节被读到了Buffer中。如果返回-1，表示到了文件末尾
        int readBytes = fChannle.read(buf);
        while(readBytes!=-1)
        {
            System.out.println(fChannle.position());
            buf.flip();
            //使用FileChannel.write()方法向FileChannel写数据
            while(buf.hasRemaining())
            {
                fChannle2.write(buf);
            }
            buf.clear();
            readBytes=fChannle.read(buf);
        }
        //FileChannel.force()方法将通道里尚未写入磁盘的数据强制写到磁盘上。出于性能方面的考虑，操作系统会将数据缓存在内存中，所以无法保证写入到FileChannel里的数据一定会即时写到磁盘上。要保证这一点，需要调用force()方法。
        //force()方法有一个boolean类型的参数，指明是否同时将文件元数据（权限信息等）写到磁盘上。
        fChannle2.force(false);
        fileTo.close();
        aFile.close();
    }
    
    public static void truncate(){
        //可以使用FileChannel.truncate()方法截取一个文件。截取文件时，文件将中指定长度后面的部分将被删除
        //channel.truncate(1024);
    }
}
