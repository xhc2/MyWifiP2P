package com.example.tongmin.mywifip2p.gooddemo;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tongmin.mywifip2p.R;

import java.util.ArrayList;
import java.util.List;

public class PeerAdapter extends BaseAdapter {

    List<WifiP2pDevice> list;
    ViewHolder holder;
    GoodActivity context;
    LayoutInflater inflater;
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    public PeerAdapter(/*WifiP2pManager mManager,WifiP2pManager.Channel mChannel,*/ GoodActivity context) {
        this.list = new ArrayList<WifiP2pDevice>();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setList(List<WifiP2pDevice> list) {
        if (list != null && list.size() > 0) {
            this.list = list;
            notifyDataSetChanged();
        }

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.peer_item, parent, false);
            holder.bt = (Button) convertView.findViewById(R.id.bt_connect);
            holder.name = (TextView) convertView.findViewById(R.id.tvname);
            holder.status = (TextView) convertView.findViewById(R.id.status);
            holder.send = (Button) convertView.findViewById(R.id.send);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final WifiP2pDevice wd = list.get(position);

        String str = null;
        holder.name.setText(wd.deviceName);


        switch (wd.status) {
            case WifiP2pDevice.CONNECTED:
                str = "已连接";
                holder.bt.setClickable(true);
                holder.bt.setText("断开连接");
                holder.bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.disconnect();
                    }
                });
                holder.send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.sendInfo(wd);
                    }
                });

                break;
            case WifiP2pDevice.INVITED:
                str = "被邀请";
                break;
            case WifiP2pDevice.FAILED:
                str = "失败";

                break;
            case WifiP2pDevice.AVAILABLE:
                str = "可利用";
                holder.bt.setText("连接");
                holder.bt.setClickable(true);
                final WifiP2pConfig config = new WifiP2pConfig();
                config.deviceAddress = wd.deviceAddress;
                holder.bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.connect(config);
                    }
                });
                break;
            case WifiP2pDevice.UNAVAILABLE:
                str = "不可利用";
                holder.bt.setClickable(false);
                break;
        }
        holder.status.setText(str);

        return convertView;
    }

    class ViewHolder {
        TextView name, status;
        Button bt, send;
    }

}
