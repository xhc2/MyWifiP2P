package com.example.tongmin.mywifip2p.gooddemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tongmin.mywifip2p.R;

public class GoodActivity extends AppCompatActivity {


    TextView tvName , tvMessage;
    Button btSearch ;
    ListView listView;
    ProgressBar pro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_good);
        findViewById();
    }
    private void findViewById(){
        tvName = (TextView)findViewById(R.id.name);
        tvMessage = (TextView)findViewById(R.id.msg);
        btSearch = (Button)findViewById(R.id.bt_search);
        listView = (ListView)findViewById(R.id.listview);
        pro = (ProgressBar)findViewById(R.id.pro);
    }


}
