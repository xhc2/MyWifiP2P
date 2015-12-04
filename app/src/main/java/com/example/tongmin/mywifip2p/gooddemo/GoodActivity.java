package com.example.tongmin.mywifip2p.gooddemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tongmin.mywifip2p.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class GoodActivity extends AppCompatActivity implements WifiP2pManager.ConnectionInfoListener ,AdapterView.OnItemClickListener{

    List<WifiP2pDevice> listDevice = new ArrayList<>();
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    WiFiDirectBroadcastReceiver mReceiver;
//    List<String> listInfo = new ArrayList<>();
    TextView tvName, tvMessage;
    Button btSearch;
    WifiP2pInfo info;
    ListView listView;
    ProgressBar pro;
    WifiP2pDevice localDevice;
    PeerAdapter adapter;
    MyHandler handler;
    ServerThread serverThread;
    private static class MyHandler extends Handler{

        private final WeakReference<GoodActivity> mActivity;
        public MyHandler(GoodActivity activity){
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    String str = (String)msg.obj;
                    Toast.makeText(mActivity.get(),str,Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(mActivity.get(),"发送成功",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    public void sendInfo(WifiP2pDevice wd){

        if(info.groupFormed && info.isGroupOwner){

        }
        else if(info.groupFormed){
            new ClientThread(handler,info.groupOwnerAddress.getHostAddress(),"来自"+localDevice.deviceName+"的信息!").start();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_good);
        findViewById();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String str;
        str = listDevice.get(position).toString();
        tvMessage.setText(str);
    }

    private void findViewById() {
        handler = new MyHandler(this);
        tvName = (TextView) findViewById(R.id.name);
        tvMessage = (TextView) findViewById(R.id.msg);
        btSearch = (Button) findViewById(R.id.bt_search);
        listView = (ListView) findViewById(R.id.listview);
        pro = (ProgressBar) findViewById(R.id.pro);
        listView.setOnItemClickListener(this);
        stopLoading();

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel);

        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discovery();
            }
        });


        adapter = new PeerAdapter( this);
        listView.setAdapter(adapter);

        discovery();

        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel);

        serverThread = new ServerThread(handler);
        serverThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        serverThread.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, getIntentFilter());
    }

    public void disconnect(){

        mManager.removeGroup(mChannel, new WifiP2pManager.ActionListener() {

            @Override
            public void onFailure(int reasonCode) {
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onSuccess() {
                adapter.notifyDataSetChanged();
                Toast.makeText(GoodActivity.this, "断开成功!", Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void discovery() {
        loading();
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                //会发送广播  WIFI_P2P_PEERS_CHANGED_ACTION
                Log.e("xhc", "搜索成功");
            }

            @Override
            public void onFailure(int reasonCode) {
                Log.e("xhc", "搜索失败-->" + reasonCode);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {
        //连接后的状态
        this.info = info;
        tvMessage.setText(info.toString());

    }

    private IntentFilter getIntentFilter() {
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        return mIntentFilter;
    }

    public void requestConnectionInfo(
            WifiP2pManager.ConnectionInfoListener listener) {
        mManager.requestConnectionInfo(mChannel, listener);
    }

    public void connect( WifiP2pConfig config){
        loading();
        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                stopLoading();
                Toast.makeText(GoodActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int reason) {
                Log.e("xhc", " 链接失败 " + reason);
                //failure logic
                adapter.notifyDataSetChanged();
            }
        });
    }
    private void loading() {
        pro.setVisibility(View.VISIBLE);
    }

    private void stopLoading() {
        pro.setVisibility(View.GONE);
    }

    class WiFiDirectBroadcastReceiver extends BroadcastReceiver implements WifiP2pManager.PeerListListener {

        private WifiP2pManager mManager;
        private WifiP2pManager.Channel mChannel;
//        private GoodActivity mActivity;

        //    private  WifiP2pManager.PeerListListener myPeerListListener;
        public WiFiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel/*,
                                           GoodActivity activity*/) {
            super();
            this.mManager = manager;
            this.mChannel = channel;
//            this.mActivity = activity;
        }


        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {

                // Check to see if Wi-Fi is enabled and notify appropriate activity=
            } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
                // Call WifiP2pManager.requestPeers() to get a list of current peers
                if (mManager != null) {
                    mManager.requestPeers(mChannel, this);
                }
            } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
                // Respond to new connection or disconnections
                NetworkInfo networkInfo =  intent
                        .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                if (networkInfo.isConnected()) {
                    requestConnectionInfo(GoodActivity.this);
                    // we are connected with the other device, request connection
                    // info to find group owner IP
                }
            } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
                localDevice =  intent.getParcelableExtra(
                        WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
                tvName.setText(localDevice.deviceName);


            }
        }


        @Override
        public void onPeersAvailable(WifiP2pDeviceList peers) {
            stopLoading();
            listDevice.clear();
            listDevice.addAll(peers.getDeviceList());
            adapter.setList(listDevice);
        }
    }
}
