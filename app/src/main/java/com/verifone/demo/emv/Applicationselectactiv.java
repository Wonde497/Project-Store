package com.verifone.demo.emv;

import android.content.BroadcastReceiver;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.verifone.demo.emv.data_handlers.User;
import com.verifone.demo.emv.transaction.TransBasic;

import java.util.ArrayList;

import  cn.verifone.simon.verifone_x9_demo_emv.Global.R;
public class Applicationselectactiv extends AppCompatActivity /*implements connection*/  {
   public static ArrayList<User> myList;
    TransBasic transBasic;ProgressBar progressBar;
    BroadcastReceiver broadcastReceiver;

    private RecyclerView mRecyclerView;

    private Applicationrecycler mRecyclerAdapter;
    Transactiondata transactiondata;
    SharedPreferences sharedPreferences;
    RecyclerView.Adapter adapter;
    User mLog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityselectapplication);
       // myList = new ArrayList<>();
        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerAdapter = new Applicationrecycler(myList,this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mRecyclerAdapter);
       /* mLog = new user();
          mLog.setAid("aiii");
    myList.add(0,mLog);*/

    mRecyclerAdapter.notifyData(myList);
    }



    @Override
    public void onBackPressed() {
        finish();
    }

}
