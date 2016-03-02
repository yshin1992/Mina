import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * 通道之间的数据传输
 */
public class NioDemo4
{

    public static void main(String[] args) throws IOException
    {
        // TODO Auto-generated method stub
        transTo();
    }
    
    /**
     * FileChannel的transferFrom()方法可以将数据从源通道传输到FileChannel中
     * @throws IOException
     */
    public static void transFrom() throws IOException
    {
        RandomAccessFile afile=new RandomAccessFile("D:/win.png", "r");
        RandomAccessFile outputFile=new RandomAccessFile("E:/win_back2.png","rw");
        FileChannel fromChannel=afile.getChannel();
        FileChannel toChannel=outputFile.getChannel();
        
        long position=0;
        long count=fromChannel.size();
        
        toChannel.transferFrom(fromChannel, position, count);
        outputFile.close();
        afile.close();
    }
    
    /**
     * transferTo()方法将数据从FileChannel传输到其他的channel中
     */
    public static void transTo() throws IOException 
    {
        RandomAccessFile afile=new RandomAccessFile("D:/win.png", "r");
        RandomAccessFile outputFile=new RandomAccessFile("F:/win_back2.png","rw");
        FileChannel fromChannel=afile.getChannel();
        FileChannel toChannel=outputFile.getChannel();
        
        long position=0;
        long count=fromChannel.size();
        
        fromChannel.transferTo(position, count, toChannel);
        outputFile.close();
        afile.close();
    }
}
