package com.example.tongmin.mywifip2p;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by TongMin on 2015/12/1.
 */
public class ServerThread extends Thread {

    /**
     * Create a server socket and wait for client connections. This
     * call blocks until a connection is accepted from a client
     */
    ServerSocket serverSocket ;
    Context context;
    public ServerThread(Context context){
        this.context = context;
    }


    @Override
    public void run() {

        try{
            Looper.prepare();
            Toast.makeText(context,"服务开启监听",Toast.LENGTH_SHORT).show();
            Looper.loop();
            serverSocket = new ServerSocket(8988);
            Socket client = serverSocket.accept();

            final File f = new File(
                    Environment.getExternalStorageDirectory() + "/"
                            + context.getPackageName() + "/wifip2pshared-"
                            + System.currentTimeMillis() + ".jpg");

            File dirs = new File(f.getParent());
            if (!dirs.exists())
                dirs.mkdirs();
            f.createNewFile();

            InputStream inputstream = client.getInputStream();
            copyFile(inputstream, new FileOutputStream(f));
            Looper.prepare();
            Toast.makeText(context,"文件接收成功",Toast.LENGTH_LONG).show();
            Looper.loop();
            serverSocket.close();
        }catch (Exception e){
            Looper.prepare();
            Toast.makeText(context,"exception"+e.getMessage(),Toast.LENGTH_LONG).show();
            Looper.loop();
        }

    }
    public static boolean copyFile(InputStream inputStream, OutputStream out)
    {
        byte buf[] = new byte[1024];
        int len;
        try
        {
            while ((len = inputStream.read(buf)) != -1)
            {
                out.write(buf, 0, len);

            }
            out.close();
            inputStream.close();
        } catch (IOException e)
        {
            return false;
        }
        return true;
    }
}
