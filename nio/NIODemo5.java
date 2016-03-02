import java.io.IOException;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO Selector
*Selector（选择器）是Java NIO中能够检测一到多个NIO通道，并能够知晓通道是否为诸如读写事件做好准备的组件。
*这样，一个单独的线程可以管理多个channel，从而管理多个网络连接。
 */
public class NioDemo5
{

    public static void main(String[] args)
    {
        // TODO Auto-generated method stub

    }
    
    /**
     * NIO selector
     * @throws IOException 
     */
    public static void selector() throws IOException
    {
        SocketChannel channel=SocketChannel.open();
        
        //创建selector
        Selector selector = Selector.open();
        //向selector注册通道
        //为了将Channel和Selector配合使用，必须将channel注册到selector上。通过SelectableChannel.register()方法来实现
        //与Selector一起使用时，Channel必须处于非阻塞模式下。这意味着不能将FileChannel与Selector一起使用，因为FileChannel不能切换到非阻塞模式。而套接字通道都可以。
        channel.configureBlocking(false);
        SelectionKey key = channel.register(selector, SelectionKey.OP_READ);
        
        //获取interest集合
        int interestSet = key.interestOps();
        boolean isInterestedInAccept  = (interestSet & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT;
        boolean isInterestedInConnect = (interestSet & SelectionKey.OP_CONNECT) == SelectionKey.OP_CONNECT;
        boolean isInterestedInRead    = (interestSet & SelectionKey.OP_READ) == SelectionKey.OP_READ;
        boolean isInterestedInWrite   = (interestSet & SelectionKey.OP_WRITE) == SelectionKey.OP_WRITE;
        //获取ready集合
        //ready 集合是通道已经准备就绪的操作的集合。在一次选择(Selection)之后，你会首先访问这个ready set
        int readySet = key.readyOps();
        //可以用像检测interest集合那样的方法，来检测channel中什么事件或操作已经就绪。但是，也可以使用以下四个方法，它们都会返回一个布尔类型
        boolean isAcceptable = key.isAcceptable();
        boolean isConnectable = key.isConnectable();
        boolean isReadable = key.isReadable();
        boolean isWritable = key.isWritable();
        
        //从SelectionKey访问Channel和Selector很简单。如下：
        Channel keyChannel = key.channel();
        Selector keySelector = key.selector();
        
        //可以将一个对象或者更多信息附着到SelectionKey上，这样就能方便的识别某个给定的通道。
        //例如，可以附加 与通道一起使用的Buffer，或是包含聚集数据的某个对象。使用方法如下
        Object attchObj = 1223;
        key.attach(attchObj);
        Object getAttachObj = key.attachment();
        System.out.println(getAttachObj);
        
        //还可以在用register()方法向Selector注册Channel的时候附加对象。
        SelectionKey key2 = channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE,attchObj);
        
        //通过Selector选择通道
        //一旦向Selector注册了一或多个通道，就可以调用几个重载的select()方法。这些方法返回你所感兴趣的事件（如连接、接受、读或写）已经准备就绪的那些通道。
        //换句话说，如果你对“读就绪”的通道感兴趣，select()方法会返回读事件已经就绪的那些通道。
        //下面是select()方法
        int selectResult = selector.select();//select()阻塞到至少有一个通道在你注册的事件上就绪了
        long timeout = 1000;
        selectResult = selector.select(timeout);//和select()一样，除了最长会阻塞timeout毫秒(参数)
        selectResult = selector.selectNow();//不会阻塞，不管什么通道就绪都立刻返回(此方法执行非阻塞的选择操作。如果自从前一次选择操作后，没有通道变成可选择的，则此方法直接返回零。)
        //select()方法返回的int值表示有多少通道已经就绪。亦即，自上次调用select()方法后有多少通道变成就绪状态。
        
        //一旦调用了select()方法，并且返回值表明有一个或更多个通道就绪了，然后可以通过调用selector的selectedKeys()方法，访问“已选择键集（selected key set）”中的就绪通道
        Set<SelectionKey> selectedKeys = selector.selectedKeys();
        
        //当像Selector注册Channel时，Channel.register()方法会返回一个SelectionKey 对象。这个对象代表了注册到该Selector的通道。可以通过SelectionKey的selectedKeySet()方法访问这些对象。
        Iterator keyIterator = selectedKeys.iterator();
        while(keyIterator.hasNext()) {
            SelectionKey keytemp = (SelectionKey) keyIterator.next();
            if(keytemp.isAcceptable()) {
                // a connection was accepted by a ServerSocketChannel.
            } else if (keytemp.isConnectable()) {
                // a connection was established with a remote server.
            } else if (keytemp.isReadable()) {
                // a channel is ready for reading
            } else if (keytemp.isWritable()) {
                // a channel is ready for writing
            }
            keyIterator.remove();
        }
        
        //注意每次迭代末尾的keyIterator.remove()调用。Selector不会自己从已选择键集中移除SelectionKey实例。
        //必须在处理完通道时自己移除。下次该通道变成就绪时，Selector会再次将其放入已选择键集中。
        
        //SelectionKey.channel()方法返回的通道需要转型成你要处理的类型，如ServerSocketChannel或SocketChannel等。
        
        //某个线程调用select()方法后阻塞了，即使没有通道已经就绪，也有办法让其从select()方法返回。
        //只要让其它线程在第一个线程调用select()方法的那个对象上调用Selector.wakeup()方法即可。
        //阻塞在select()方法上的线程会立马返回。如果有其它线程调用了wakeup()方法，但当前没有线程阻塞在select()方法上，下个调用select()方法的线程会立即“醒来（wake up）”
        
        //用完Selector后调用其close()方法会关闭该Selector，且使注册到该Selector上的所有SelectionKey实例无效。通道本身并不会关闭。
    }
}
