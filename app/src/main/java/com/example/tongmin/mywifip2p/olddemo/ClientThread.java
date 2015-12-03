package com.example.tongmin.mywifip2p.olddemo;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import com.example.tongmin.mywifip2p.debugutil.DebugFile;

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
    int len;
    Uri uri ;

    public ClientThread(Context context,String host,Uri uri) {
        this.context = context;
        this.host = host;
        this.uri = uri;
    }



    @Override
    public void run() {
        try {
            byte buf[] = new byte[1024];

            socket.bind(null);
            DebugFile.getInstance(context).writeLog("客户县城连接", "尝试连接 host" +host+" port "+Constant.port);
            socket.connect((new InetSocketAddress(host, Constant.port)), 5000);
            DebugFile.getInstance(context).writeLog("客户县城连接","连接成功"+socket.isBound());

            OutputStream outputStream = socket.getOutputStream();
            ContentResolver cr = context.getContentResolver();
            InputStream inputStream = null;
            inputStream = cr.openInputStream(uri);
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();

        } catch (Exception e) {
            DebugFile.getInstance(context).writeException("client thread exception",e.getMessage());
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
