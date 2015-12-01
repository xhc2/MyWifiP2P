package com.example.tongmin.mywifip2p;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
            serverSocket = new ServerSocket(8888);
            Socket client = serverSocket.accept();

            /**
             * If this code is reached, a client has connected and transferred data
             * Save the input stream from the client as a JPEG file
             */
            final File f = new File(Environment.getExternalStorageDirectory() + "/"
                    + context.getPackageName() + "/wifip2pshared-" + System.currentTimeMillis()
                    + ".jpg");

            File dirs = new File(f.getParent());
            if (!dirs.exists())
                dirs.mkdirs();

            f.createNewFile();
            InputStream inputstream = client.getInputStream();
            recvFileAndSave(inputstream, "什么鬼");
            serverSocket.close();
        }catch (Exception e){

        }

    }
    public boolean recvFileAndSave(InputStream ins, String extName) {
        try {
            final File recvFile = new File(
                    Environment.getExternalStorageDirectory()
                            + "/wifi-direct/wifip2pshared-"
                            + System.currentTimeMillis() + extName);

            File dirs = new File(recvFile.getParent());
            if (!dirs.exists())
                dirs.mkdirs();
            recvFile.createNewFile();

            FileOutputStream fileOutS = new FileOutputStream(recvFile);

            byte buf[] = new byte[1024];
            int len;
            while ((len = ins.read(buf)) != -1) {
                fileOutS.write(buf, 0, len);
                // 通知界面发送/接收文件进度。
//                postSendRecvBytes(0, len);

            }
            fileOutS.close();
            String strFile = recvFile.getAbsolutePath();
            if (strFile != null) {
                //  Go, let's go and test a new cool & powerful method.
//                Utility.openFile((Activity)context, recvFile);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
