package com.nio;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO Selector
*[一句话功能简述]<p>
*[功能详细描述]<p>
*@author YanShuai
*@version 1.0,2015年11月23日
*@See
*@since V1.0
 */
public class NIODemo6
{

    public static void main(String[] args) throws IOException
    {
        // TODO Auto-generated method stub
        Selector selector = Selector.open();
        
        //向selector注册通道
        SocketChannel channel=SocketChannel.open();
        channel.configureBlocking(false);
        SelectionKey key = channel.register(selector,
            SelectionKey.OP_READ);
        
        Set selectedKeys = selector.selectedKeys();
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

    }

}
