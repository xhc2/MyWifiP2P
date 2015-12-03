package com.example.tongmin.mywifip2p.gooddemo;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.tongmin.mywifip2p.R;

import java.util.List;

public class PeerAdapter extends BaseAdapter {

    List<WifiP2pDevice> list;
    ViewHolder holder;
    Context context;
    LayoutInflater inflater;

    public PeerAdapter(Context context, List<WifiP2pDevice> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
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
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        WifiP2pDevice wd = list.get(position);
        String str ;
        switch (wd.status) {
            case WifiP2pDevice.CONNECTED:
                str = "已连接";
                holder.bt.setText("断开连接");
                holder.bt.setClickable(true);
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
                break;
            case WifiP2pDevice.UNAVAILABLE:
                str = "不可利用";
                holder.bt.setClickable(false);
                break;
        }


        return convertView;
    }

    class ViewHolder {
        TextView name, status;
        Button bt;
    }

}
