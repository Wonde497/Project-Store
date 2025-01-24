package com.verifone.demo.emv;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.verifone.demo.emv.Utilities.ActivityCollector;
import com.verifone.demo.emv.basic.BaseActivity;
import com.verifone.demo.emv.transaction.CheckCardActivity;
import com.verifone.demo.emv.transaction.TransBasic;
import com.verifone.demo.emv.widget.commonvariable;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class loadingactivity extends AppCompatActivity /*implements connection*/  {

    TransBasic transBasic;
    ProgressBar progressBar;
    BroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        ActivityCollector.setactivity(this);
       /* broadcastReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
                //
            }
        };
        registerReceiver(broadcastReceiver,new IntentFilter("brodcast"));*/
        Log.d("loading", "brodcast");

        TransBasic.act=this;

        commonvariable co=new commonvariable();
       //co.setConnection(this);
    }



    @Override
    public void onBackPressed() {
        finish();
    }

   /* @Override
    public void toast() {
        Toast.makeText(this,  "toast", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void finish() {
     //   finish();
        Log.d("loading", "finishcalled :" );

    }

    @Override
    public void back() {
        Log.d("loading", "backcalled :" );

    }*/

}
