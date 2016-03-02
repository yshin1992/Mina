import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


/**
 * 
*Scatter/Gather
*
1.分散（scatter）从Channel中读取是指在读操作时将读取的数据写入多个buffer中。
因此，Channel将从Channel中读取的数据“分散（scatter）”到多个Buffer中。
2.聚集（gather）写入Channel是指在写操作时将多个buffer的数据写入同一个Channel，
因此，Channel 将多个Buffer中的数据“聚集（gather）”后发送到Channel。
 */
public class NioDemo3
{

    public static void main(String[] args) throws IOException
    {
        // TODO Auto-generated method stub
        scatterRead();
    }
    
    public static void scatterRead() throws IOException
    {
        RandomAccessFile afile=new RandomAccessFile(new File(System.getProperty("user.dir")+"/src/nio-data4.txt"), "rw");
        FileChannel fChannel = afile.getChannel();
        
        ByteBuffer header = ByteBuffer.allocate(20);
        ByteBuffer body = ByteBuffer.allocate(48);
        
        ByteBuffer[] byteArr = {header , body};
        fChannel.read(byteArr);
        
        //注意buffer首先被插入到数组，然后再将数组作为channel.read() 的输入参数。
        //read()方法按照buffer在数组中的顺序将从channel中读取的数据写入到buffer，当一个buffer被写满后，channel紧接着向另一个buffer中写。

        //Scattering Reads在移动下一个buffer前，必须填满当前的buffer，这也意味着它不适用于动态消息(译者注：消息大小不固定)。
        //换句话说，如果存在消息头和消息体，消息头必须完成填充（例如 128byte），Scattering Reads才能正常工作。
        header.flip();
//        while(header.hasRemaining())
//        {
//            System.out.print((char)header.get());
//        }
//        System.out.println();
        body.flip();
//        while(body.hasRemaining())
//        {
//            System.out.print((char)body.get());
//        }
        RandomAccessFile afile2=new RandomAccessFile(new File(System.getProperty("user.dir")+"/src/nio-data3.txt"), "rw");
        FileChannel fChannel2 = afile2.getChannel();
        fChannel2.write(byteArr);
        afile2.close();
        afile.close();
    }
    
}
