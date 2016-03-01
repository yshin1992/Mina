package com.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * Demo3的改进
 * 通道之间的数据传输
*[一句话功能简述]<p>
*[功能详细描述]<p>
*@author YanShuai
*@version 1.0,2015年11月23日
*@See
*@since V1.0
 */
public class NIODemo5
{

    /**
     * 这种方式更漂亮
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException
    {
        // TODO Auto-generated method stub
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

}
