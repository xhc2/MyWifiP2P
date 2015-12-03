package com.example.tongmin.mywifip2p.olddemo;

import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import com.example.tongmin.mywifip2p.debugutil.DebugFile;

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

    Context context;
    public ServerThread(Context context){
        this.context = context;
    }


    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try{
            DebugFile.getInstance(context).writeLog("服务开启监听","");
            serverSocket  = new ServerSocket(Constant.port);
            Socket client = serverSocket.accept();
            final File f = new File(
                    Environment.getExternalStorageDirectory() + "/"
                            + "wifi-direct/"
                            + System.currentTimeMillis() + ".jpg");
            File dirs = new File(f.getParent());
            if (!dirs.exists())
                dirs.mkdirs();
            f.createNewFile();

            InputStream inputstream = client.getInputStream();
            copyFile(inputstream, new FileOutputStream(f));
            serverSocket.close();
        }catch (Exception e){
            Looper.prepare();
            Toast.makeText(context,"exception"+e.getMessage(),Toast.LENGTH_LONG).show();
            Looper.loop();
        }
        finally {
            if(serverSocket != null){
                try {
                    serverSocket.close();
                }
                catch (Exception e){ }

            }

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
