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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tongmin.mywifip2p.R;
import com.example.tongmin.mywifip2p.olddemo.ServerThread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class GoodActivity extends AppCompatActivity implements WifiP2pManager.ConnectionInfoListener ,AdapterView.OnItemClickListener{

    List<WifiP2pDevice> listDevice = new ArrayList<>();
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    WiFiDirectBroadcastReceiver mReceiver;
    List<String> listInfo = new ArrayList<String>();
    TextView tvName, tvMessage;
    Button btSearch;
    ListView listView;
    ProgressBar pro;
    WifiP2pDevice localDevice;
    PeerAdapter adapter;

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
        tvName = (TextView) findViewById(R.id.name);
        tvMessage = (TextView) findViewById(R.id.msg);
        btSearch = (Button) findViewById(R.id.bt_search);
        listView = (ListView) findViewById(R.id.listview);
        pro = (ProgressBar) findViewById(R.id.pro);
        listView.setOnItemClickListener(this);
        stopLoading();

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);

        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discovery();
            }
        });


        adapter = new PeerAdapter( this);
        listView.setAdapter(adapter);

        discovery();

        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);


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
        listInfo.add(info.toString());
        //连接后的状态
        if (info.groupFormed && info.isGroupOwner)
        {

        }
        else if(info.groupFormed ){

        }

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
        private GoodActivity mActivity;

        //    private  WifiP2pManager.PeerListListener myPeerListListener;
        public WiFiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel,
                                           GoodActivity activity) {
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
                    requestConnectionInfo(GoodActivity.this);
                    // we are connected with the other device, request connection
                    // info to find group owner IP
                }
                else{
                    Toast.makeText(GoodActivity.this, "这个应该是断开连接", Toast.LENGTH_SHORT).show();
                }
            } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
                localDevice = (WifiP2pDevice) intent.getParcelableExtra(
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
