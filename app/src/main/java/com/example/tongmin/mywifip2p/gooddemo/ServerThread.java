
package com.example.tongmin.mywifip2p.gooddemo;

import android.os.Handler;
import android.os.Message;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * create by xhc
 */
public class ServerThread extends Thread {

   private boolean running = true;
    private Handler handler ;
    public ServerThread(Handler handler){
        this.handler = handler ;
    }


    @Override
    public void run() {
        super.run();
        try{
            while(running){
                ServerSocket serverSocket = new ServerSocket(Constant.PORT);
                Socket client = serverSocket.accept();
                getMessage(client.getInputStream());
                serverSocket.close();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void close(){
       this. running = false;
    }

    private void getMessage(InputStream in){
        try {
            int BUFFER_SIZE = 4096;

            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] data = new byte[BUFFER_SIZE];
            int count ;
            while((count = in.read(data,0,BUFFER_SIZE)) != -1)
                outStream.write(data, 0, count);


            String str =  new String(outStream.toByteArray(),"UTF-8");
            Message msg = new Message();
            msg.what = 1;
            msg.obj = str ;
            handler.sendMessage(msg);

        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try{
                if(in != null){
                    in.close();
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

}
