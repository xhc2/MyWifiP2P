package com.example.tongmin.mywifip2p.olddemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.tongmin.mywifip2p.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener , WifiP2pManager.ConnectionInfoListener{

    List<WifiP2pDevice > listDevice = new ArrayList<>();
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    WiFiDirectBroadcastReceiver mReceiver;
    WifiP2pInfo info;

    Button bt , disconnect ;
    TextView tv;
    Button send;
    ListView listView ;
    private final int CHOOSE_FILE_RESULT_CODE = 20 ;

    ArrayAdapter<String> arrayAdapter ;
    ClientThread client ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        listView =(ListView)findViewById(R.id.listview);
        tv = (TextView)findViewById(R.id.tv);
        disconnect = (Button)findViewById(R.id.dissconnect);
        listView.setOnItemClickListener(this);
        bt = (Button)findViewById(R.id.bt);
        send = (Button)findViewById(R.id.send);

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);

        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mManager.removeGroup(mChannel, new WifiP2pManager.ActionListener()
                {

                    @Override
                    public void onFailure(int reasonCode)
                    {

                    }

                    @Override
                    public void onSuccess()
                    {
                        Toast.makeText(MainActivity.this,"断开成功!",Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, CHOOSE_FILE_RESULT_CODE);

            }
        });
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {

                    @Override
                    public void onSuccess() {
                        //会发送广播
                        Log.e("xhc","搜索成功");
                    }

                    @Override
                    public void onFailure(int reasonCode) {
                        Log.e("xhc","搜索失败-->"+reasonCode);
                    }
                });
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        Uri uri = data.getData();
        client =  new ClientThread(MainActivity.this,info.groupOwnerAddress.getHostAddress(),uri);
        client.start();
    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, getIntentFilter());
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        WifiP2pDevice device = listDevice.get(position);
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {

                Log.e("xhc", "链接设备成功");

            }

            @Override
            public void onFailure(int reason) {
                Log.e("xhc", " 链接失败 " + reason);
                //failure logic
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
        if (info.groupFormed && info.isGroupOwner)
        {
            new ServerThread(MainActivity.this).start();
            //开启服务线程
        }
        this.info = info;
//        host  = info.groupOwnerAddress.getHostAddress();
        tv.setText(info.toString());
    }
    public void requestConnectionInfo(
            WifiP2pManager.ConnectionInfoListener listener) {
        mManager.requestConnectionInfo(mChannel, listener);
    }

    private IntentFilter getIntentFilter() {
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        return mIntentFilter;
    }


    class WiFiDirectBroadcastReceiver extends BroadcastReceiver implements  WifiP2pManager.PeerListListener{

        private WifiP2pManager mManager;
        private WifiP2pManager.Channel mChannel;
        private MainActivity mActivity;
        //    private  WifiP2pManager.PeerListListener myPeerListListener;
        public WiFiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel,
                                           MainActivity activity) {
            super();
            this.mManager = manager;
            this.mChannel = channel;
            this.mActivity = activity;
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
                NetworkInfo networkInfo = (NetworkInfo) intent
                        .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                if (networkInfo.isConnected()) {
                    requestConnectionInfo(MainActivity.this);
                    // we are connected with the other device, request connection
                    // info to find group owner IP
                }
            } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
                // Respond to this device's wifi state changing
            }
        }

        @Override
        public void onPeersAvailable(WifiP2pDeviceList peers) {

            List<String> listStr = new ArrayList<>();
            Collection<WifiP2pDevice> collection = peers.getDeviceList();
            Iterator<WifiP2pDevice> iter = collection.iterator();
            while (iter.hasNext()){
                WifiP2pDevice wd = (WifiP2pDevice)iter.next();
                listStr.add("device name "+wd.deviceName +" device address "+ wd.deviceAddress);
                listDevice.add(wd);
            }

            arrayAdapter = new ArrayAdapter<String>(MainActivity.this,R.layout.tvitem,listStr);
            listView.setAdapter(arrayAdapter);
        }
    }
}
