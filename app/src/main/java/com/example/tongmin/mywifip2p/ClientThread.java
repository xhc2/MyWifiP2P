package com.example.tongmin.mywifip2p;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

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
    int port = 8888;
    int len;

    public ClientThread(Context context) {
        this.context = context;

    }

    @Override
    public void run() {
        try {
            byte buf[] = new byte[1024];

            socket.bind(null);
            socket.connect((new InetSocketAddress(host, port)), 500);
            OutputStream outputStream = socket.getOutputStream();
            ContentResolver cr = context.getContentResolver();
            InputStream inputStream = null;
            inputStream = cr.openInputStream(Uri.parse("mnt/sdcard/DCIM/2.jpg"));
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
