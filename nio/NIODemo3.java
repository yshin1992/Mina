package com.nio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
/**
 * 利用NIO的byteBuffer读取图片文件
*[一句话功能简述]<p>
*[功能详细描述]<p>
*@author YanShuai
*@version 1.0,2015年11月23日
*@See
*@since V1.0
 */
public class NIODemo3
{

    public static void main(String[] args) throws IOException
    {
        // TODO Auto-generated method stub
        RandomAccessFile afile=new RandomAccessFile("D:/win.png", "r");
        File outputFile=new File("E:/win_back.png");
        OutputStream out=new FileOutputStream(outputFile);
        ByteBuffer buf=ByteBuffer.allocate(1024);
        FileChannel channel=afile.getChannel();
        int readBytes=channel.read(buf);
        while(readBytes!=-1){
            buf.flip();//写模式转读模式
            out.write(buf.array());
            buf.clear();
            readBytes=channel.read(buf);
        }
        out.close();
        afile.close();
    }

}
