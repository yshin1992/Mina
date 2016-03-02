import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

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

Buffer的几个常用方法：limit(),position(),capacity(),clear(),compact(),flip()
 */
public class NioDemo2
{

    public static void main(String[] args) throws IOException
    {
        // TODO Auto-generated method stub
//        read();
//        put();
//        write();
//        rewind();
//        compact();
//        markReset();
//        equals();
        compareTo();
                
    }
    
    public static void read() throws IOException{
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
    
    /**
     * put()方法实例
     * @throws IOException
     */
    public static void put() throws IOException{
        RandomAccessFile afile=new RandomAccessFile(new File(System.getProperty("user.dir")+"/src/nio-data2.txt"),
                "rw");
        FileChannel fChannel = afile.getChannel();
        ByteBuffer buf=ByteBuffer.allocate(48);
        buf.put((byte) 97);
        buf.put((byte) 48);
        int bytesRead = fChannel.read(buf);
        System.out.println(bytesRead);
        //这里flip()方法是必须的，由读模式改成写模式，需要移动position游标，flip()方法实现这个操作
        buf.flip();
        while(buf.hasRemaining()){
            System.out.print((char)buf.get());
        }
        afile.close();
    }
    
    /**
     * 从Buffer中读取数据有两种方式：
               1. 从Buffer读取数据到Channel。
               2. 使用get()方法从Buffer中读取数据。
     * @throws IOException 
     */
    public static void write() throws IOException{
        RandomAccessFile afile=new RandomAccessFile(new File(System.getProperty("user.dir"))+"/src/nio-data2.txt","rw");
        FileChannel fChannel = afile.getChannel();
        //
        ByteBuffer buf = ByteBuffer.allocate(128);
        //
        fChannel.read(buf);
        //
        buf.flip();
        RandomAccessFile afile2=new RandomAccessFile(new File(System.getProperty("user.dir")+"/src/nio-data3.txt"),"rw");
        FileChannel fChannel2 = afile2.getChannel();
        
        while(buf.hasRemaining())
        {
            fChannel2.write(buf);
        }
        fChannel2.force(true);
        afile2.close();
        afile.close();
    }
    
    /**
     * rewind()方法，将position重置为0
     * @throws IOException
     */
    public static void rewind() throws IOException{
        RandomAccessFile afile=new RandomAccessFile(new File(System.getProperty("user.dir")+"/src/nio-data3.txt"),"rw");
        FileChannel fChannel = afile.getChannel();
        
        ByteBuffer buf = ByteBuffer.allocate(64);
        fChannel.read(buf);
        
        buf.flip();
        
        while(buf.hasRemaining())
        {
            System.out.print((char)buf.get());
        }
        System.out.println("position:" + buf.position()+"\tlimit:"+buf.limit());
        
        //重置position
        buf.rewind();
        
        System.out.println(buf.position());
        
        while(buf.hasRemaining())
        {
            System.out.print((char)buf.get());
        }
        
//        fChannel.close(); //不必写了，因为channel已经被包装在RandomAccessFile里了
        afile.close();
    }
    
    /**
     * clear()和compact()
     */
    public static void compact() throws IOException{
        RandomAccessFile afile= new RandomAccessFile(new File(System.getProperty("user.dir")+"/src/nio-data2.txt"),"rw");
        FileChannel fChannel = afile.getChannel();
        
        ByteBuffer buf = ByteBuffer.allocate(64);
        
        fChannel.read(buf);
        
        buf.flip();
        char[] byteArr = new char[buf.capacity()];
        
        for(int i=0 ;i < 30; i++)
        {
            byteArr[i]=(char)buf.get();
        }
        
        System.out.println(Arrays.toString(byteArr));
        
        System.out.println("position: "+buf.position() + "\t limit: "+buf.limit());
        
        //compact()方法将所有未读的数据拷贝到Buffer起始处。然后将position设到最后一个未读元素正后面。
        //limit属性依然像clear()方法一样，设置成capacity。现在Buffer准备好写数据了，但是不会覆盖未读的数据
        buf.compact();
        
        buf.flip();
        
        System.out.println("position: "+buf.position() + "\t limit: "+buf.limit());
        
        int index=0;
        Arrays.fill(byteArr,(char)30);
        while(buf.hasRemaining())
        {
            byteArr[index++] =(char) buf.get();
        }
        System.out.println(Arrays.toString(byteArr));
        afile.close();
    }
    
    /**
     * mark()和reset()方法
     * 通过调用Buffer.mark()方法，可以标记Buffer中的一个特定position。
     * 之后可以通过调用Buffer.reset()方法恢复到这个position。
     */
    public static void markReset() throws IOException{
        RandomAccessFile afile= new RandomAccessFile(new File(System.getProperty("user.dir")+"/src/nio-data2.txt"),"rw");
        FileChannel fChannel = afile.getChannel();
        
        ByteBuffer buf = ByteBuffer.allocate(64);
        
        fChannel.read(buf);
        
        buf.flip();
        
        for(int i=0;i<10;i++)
        {
            System.out.print((char)buf.get());
        }
        
        buf.mark();
        
        while(buf.hasRemaining())
        {
            System.out.print((char)buf.get());
        }
        
        System.out.println();
        System.out.println("buffer position reset");
        buf.reset();
        while(buf.hasRemaining())
        {
            System.out.print((char)buf.get());
        }
        afile.close();
    }
    
    /**
     * equals()
     * 当满足下列条件时，表示两个Buffer相等：
            1.有相同的类型（byte、char、int等）。
            2.Buffer中剩余的byte、char等的个数相等。
            3.Buffer中所有剩余的byte、char等都相同
     */
    public static void equals(){
        String str1 = "Haha,this is first string!";
        String str2 = "Hello,this is first string!";
        ByteBuffer buf1 = ByteBuffer.allocate(48);
        ByteBuffer buf2 = ByteBuffer.allocate(48);
        buf1.put(str1.getBytes());
        buf2.put(str2.getBytes());
        buf1.flip();
        buf2.flip();
        System.out.println(buf1.remaining());
        System.out.println(buf2.remaining());
        
        System.out.println(buf1.equals(buf2));
        
        for(int i=0;i<4;i++){
            buf1.get();
        }
        for(int i=0;i<5;i++){
            buf2.get();
        }
        System.out.println(buf1.remaining());
        System.out.println(buf2.remaining());
        System.out.println(buf1.equals(buf2));
        
    }
    
    /**
     * compareTo()方法比较两个Buffer的剩余元素(byte、char等)， 如果满足下列条件，则认为一个Buffer“小于”另一个Buffer：
            第一个不相等的元素小于另一个Buffer中对应的元素 。
            所有元素都相等，但第一个Buffer比另一个先耗尽(第一个Buffer的元素个数比另一个少)。
     */
    public static void compareTo() 
    {
        String str1 = "Haha,this is first string!";
        String str2 = "Haha,this is first string!hahaha";
        ByteBuffer buf1 = ByteBuffer.allocate(48);
        ByteBuffer buf2 = ByteBuffer.allocate(48);
        buf1.put(str1.getBytes());
        buf2.put(str2.getBytes());
        buf1.flip();
        buf2.flip();
        System.out.println(buf1.compareTo(buf2));
    }
}
