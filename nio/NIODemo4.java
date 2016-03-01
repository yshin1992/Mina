package com.nio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

/**
 * Scatter和Gather
 * 分散（scatter）从Channel中读取是指在读操作时将读取的数据写入多个buffer中。因此，Channel将从Channel中读取的数据“分散（scatter）”到多个Buffer中。
        聚集（gather）写入Channel是指在写操作时将多个buffer的数据写入同一个Channel，因此，Channel 将多个Buffer中的数据“聚集（gather）”后发送到Channel。
    scatter / gather经常用于需要将传输的数据分开处理的场合，例如传输一个由消息头和消息体组成的消息，你可能会将消息体和消息头分散到不同的buffer中，这样你可以方便的处理消息头和消息体。
*[一句话功能简述]<p>
*[功能详细描述]<p>
*@author YanShuai
*@version 1.0,2015年11月23日
*@See
*@since V1.0
 */
public class NIODemo4
{

    public static void main(String[] args) throws IOException, InterruptedException
    {
        // TODO Auto-generated method stub
        File f = new File(System.getProperty("user.dir") + "/src/nio-data2.txt");
        RandomAccessFile aFile = new RandomAccessFile(f, "rw");
        FileChannel inChannel = aFile.getChannel();
        ByteBuffer head = ByteBuffer.allocate(20*2);//文件里一个字符占两个字节
        ByteBuffer body = ByteBuffer.allocate(1024);
        //处理头部
        ByteBuffer[] bufs={head,body};
        inChannel.read(bufs);
        head.flip();//写模式转读模式
        while (head.hasRemaining())
        {
            System.out.print((char)head.get());
        }
        System.out.println("");
        {

            body.flip();//buf.flip() 的调用，首先读取数据到Buffer，然后反转Buffer,接着再从Buffer中读取数据

            while (body.hasRemaining())
            {
                System.out.print((char)body.get());
            }

            body.clear();
        }
        aFile.close();
    }


}
