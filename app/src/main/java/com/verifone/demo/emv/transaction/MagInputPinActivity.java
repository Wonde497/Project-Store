package com.verifone.demo.emv.transaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.verifone.demo.emv.Utilities.ActivityCollector;
import com.verifone.demo.emv.Utilities.Utility;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class MagInputPinActivity extends AppCompatActivity {
    private String TAG = "MagInputPinActivity";

    TransBasic transBasic;
    TextView tvCardNo;
    TextView tvAmount;
    ImageView btnBack;
    TextView tvTransName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mag_input_pin);
        ActivityCollector.addActivity(this);
        initView();
        SharedPreferences sharedPreferences=getSharedPreferences("Shared_data", Context.MODE_PRIVATE);

        transBasic = TransBasic.getInstance(sharedPreferences);
        transBasic.doPinPad();


        transBasic.setOnInputPinConfirm(new TransBasic.OnInputPinConfirm() {
            @Override
            public void onConfirm() {
                transBasic.ESignHandler = EsignHandler;
            }
        });
    }


    private void initView() {
        tvCardNo = findViewById(R.id.tv_cardno);
        tvAmount = findViewById(R.id.tv_amount);
        btnBack = findViewById(R.id.back_home);
        tvTransName = findViewById(R.id.trans_name);
        tvCardNo.setText(Utility.fixCardNoWithMask(TransactionParams.getInstance().getPan()));
        tvAmount.setText("ETB "+ Utility.getReadableAmount(TransactionParams.getInstance().getTransactionAmount()));
        btnBack.setVisibility(View.INVISIBLE);
        tvTransName.setText("Input PIN");
    }

    @SuppressLint("HandlerLeak")
    Handler EsignHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (transBasic.transStatus){
                Log.d(TAG, "tranStatus = " + true);
                //startActivity(new Intent(InputPinActivity.this, ESignActivity.class));
                Log.d(TAG, "after = ");
                finish();
            }else {
                ActivityCollector.finishAllTransActivity();
            }

        }
    };
}
