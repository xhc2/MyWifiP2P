
package com.example.tongmin.mywifip2p.gooddemo;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


import com.example.tongmin.mywifip2p.debugutil.DebugFile;

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
    private Context ctx;
    public ServerThread(Handler handler , Context ctx){
        this.handler = handler ;
        this.ctx = ctx;
    }


    @Override
    public void run() {
        super.run();
        try{
            while(running){
                DebugFile.getInstance(ctx).writeLog("监听before","监听before");
                ServerSocket serverSocket = new ServerSocket(Constant.PORT);
                Socket client = serverSocket.accept();
                DebugFile.getInstance(ctx).writeLog("监听after","监听after");
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
            int BUFFER_SIZE = 1024;

            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] data = new byte[BUFFER_SIZE];
            int count ;
            while((count = in.read(data,0,BUFFER_SIZE)) != -1)
                outStream.write(data, 0, count);

            String str =  new String(outStream.toByteArray(),"UTF-8");
            Message msg = new Message();
            if(str.startsWith("ip:")){
                str = str.replace("ip:", "");
//                [0] 是ip [1] device name
                String[] deviceInfo = str.split("name:");

                msg.what = 3;
                msg.obj = deviceInfo ;

            }
            else{
                msg.what = 1;
                msg.obj = str ;
            }
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
