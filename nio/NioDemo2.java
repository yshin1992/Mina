import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;

/**
 * Java NIO中的Buffer用于和NIO通道进行交互。如你所知，数据是从通道读入缓冲区，从缓冲区写入到通道中的。
缓冲区本质上是一块可以写入数据，然后可以从中读取数据的内存。这块内存被包装成NIO Buffer对象，并提供了一组方法，
用来方便的访问该块内存。

Buffer的基本用法

使用Buffer读写数据一般遵循以下四个步骤：

    写入数据到Buffer
    调用flip()方法
    从Buffer中读取数据
    调用clear()方法或者compact()方法

当向buffer写入数据时，buffer会记录下写了多少数据。一旦要读取数据，需要通过flip()方法将Buffer从写模式切换到读模式。
在读模式下，可以读取之前写入到buffer的所有数据。一旦读完了所有的数据，就需要清空缓冲区，让它可以再次被写入。
有两种方式能清空缓冲区：调用clear()或compact()方法。clear()方法会清空整个缓冲区。compact()方法只会清除已经读过的数据。
任何未读的数据都被移到缓冲区的起始处，新写入的数据将放到缓冲区未读数据的后面。
 */
public class NioDemo2
{

    public static void main(String[] args) throws IOException
    {
        // TODO Auto-generated method stub
        read();
    }
    
    private static void read() throws IOException{
        RandomAccessFile aFile = new RandomAccessFile(new File(System.getProperty("user.dir") + "/src/nio-data.txt"), "rw");
        FileChannel inChannel = aFile.getChannel();

        //create buffer with capacity of 48 bytes
        ByteBuffer buf = ByteBuffer.allocate(48);
        System.out.println(buf.capacity());
        int bytesRead = inChannel.read(buf); //read into buffer.
        while (bytesRead != -1) {
          System.out.println(buf.limit());
          System.out.println(buf.position());
          buf.flip();  //make buffer ready for read
          while(buf.hasRemaining()){
              System.out.print((char) buf.get()); // read 1 byte at a time
              System.out.println(buf.position());
          }

          buf.clear(); //make buffer ready for writing
          bytesRead = inChannel.read(buf);
        }
        aFile.close();

    }
    
    private static void readChar() throws IOException{
        RandomAccessFile aFile = new RandomAccessFile(new File(System.getProperty("user.dir") + "/src/nio-data2.txt"), "rw");
        FileChannel inChannel = aFile.getChannel();
        
        //第一步:create buffer with capacity of 512 characters
        CharBuffer buf = CharBuffer.allocate(256);
        
    }

}
