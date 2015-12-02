package com.example.tongmin.mywifip2p;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by TongMin on 2015/12/1.
 */
public class ClientThread extends Thread {

    Context context;
    Socket socket = new Socket();
    String host;
    int port = 8988;
    int len;

    public ClientThread(Context context,String host) {
        this.context = context;
        this.host = host;
    }



    @Override
    public void run() {
        try {
            byte buf[] = new byte[1024];

            socket.bind(null);
            socket.connect((new InetSocketAddress(host, port)), 5000);
            Log.e("xhc", "看看是否连接成功"+socket.isBound());

            OutputStream outputStream = socket.getOutputStream();
            ContentResolver cr = context.getContentResolver();
            InputStream inputStream = null;
            inputStream = cr.openInputStream(Uri.parse("/mnt/sdcard/DCIM/Camera/1140.jpg"));
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void close(){
        try{
            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
