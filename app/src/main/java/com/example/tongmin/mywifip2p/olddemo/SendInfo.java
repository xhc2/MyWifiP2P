package com.example.tongmin.mywifip2p.olddemo;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by TongMin on 2015/12/1.
 */
public class SendInfo {

    class SendStringRunable implements Runnable {
        private String host;
        private int port;
        private String data;

        //        private AppNetService netService;
        SendStringRunable(String host, int port, String data/*, AppNetService netService*/) {
            this.host = host;
            this.port = port;
            this.data = data;
//            this.netService = netService;
        }

        @Override
        public void run() {
            if (sendString()) ;
//                netService.postSendStringResult(data.length());
            else ;
//                netService.postSendStringResult(-1);
        }

        private boolean sendString() {
            Socket socket = new Socket();
            boolean result = true;

            try {
                Log.d(this.getClass().getName(), "Opening client socket - ");
                socket.bind(null);
                socket.connect((new InetSocketAddress(host, port)), ConfigInfo.SOCKET_TIMEOUT);

                Log.d(this.getClass().getName(), "Client socket - " + socket.isConnected());
                OutputStream outs = socket.getOutputStream();
                outs.write(ConfigInfo.COMMAND_ID_SEND_STRING);
                outs.write(data.length());// NOTE: MAX = 255
                outs.write(data.getBytes());
                outs.close();
                Log.d(this.getClass().getName(), "send string ok.");

            } catch (IOException e) {
                Log.e(this.getClass().getName(), e.getMessage());
                result = false;
            } finally {
                if (socket != null) {
                    if (socket.isConnected()) {
                        try {
                            socket.close();
                            Log.d(this.getClass().getName(), "socket.close();");
                        } catch (IOException e) {
                            // Give up
                            e.printStackTrace();
                        }
                    }
                }
            }
            return result;
        }
    }
}
