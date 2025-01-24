package com.verifone.demo.emv;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import com.verifone.demo.emv.basic.BaseActivity;
import com.verifone.demo.emv.transaction.TransBasic;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class LoginActivity extends BaseActivity {

    TransBasic transBasic;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       SharedPreferences sharedPreferences=getSharedPreferences("Shared_data", Context.MODE_PRIVATE);

        initView();
        transBasic = TransBasic.getInstance(sharedPreferences);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doSet();
            }
        },1000);
    }

    private void initView() {
        progressBar = findViewById(R.id.progress);

    }

    private void doSet() {
        transBasic.doSetAID(2);
        transBasic.doSetRID(2);
        transBasic.doSetKeys();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 13000);
    }

    @Override
    public void onBackPressed() {

    }
}
