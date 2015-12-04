package com.example.tongmin.mywifip2p.gooddemo;

import android.os.Handler;
import android.os.Message;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Create by xhc
 */
public class ClientThread extends Thread {

    private Handler handler ;
    private Socket socket ;
    private String host;
    private String message;
    public ClientThread(Handler handler ,String host ,String message ){
        this.handler = handler ;
        this.host = host;
        socket = new Socket();
        this.message = message;
    }


    @Override
    public void run() {
        super.run();
        try{
            socket.bind(null);
            socket.connect((new InetSocketAddress(host, Constant.PORT)), 500);
            OutputStream os = socket.getOutputStream();
            os.write(message.getBytes());
            os.close();
            Message msg = new Message();
            msg.what = 2;
            handler.sendMessage(msg);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
