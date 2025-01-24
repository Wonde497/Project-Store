package com.verifone.demo.emv.transaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.verifone.demo.emv.Utilities.ActivityCollector;
import com.verifone.demo.emv.Utilities.ToastUtil;

import java.text.DecimalFormat;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class MagInputPanActivity extends AppCompatActivity {
    private static final String TAG = "MAGINPUT_PAN";
    TextView Lastpan,tran_type,titleTextView;
    MyKeyListener myKeyListener,myKeyListener1;
    public static String Lastpan4="";
    SharedPreferences sharedPreferences;
    String  Txn_type="",Txn_Menu_Type="";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
        Txn_type = sharedPreferences.getString("txn_type", "");
        Txn_Menu_Type=sharedPreferences.getString("Txn_Menu_Type", "");

        Log.d("txn_type........", Txn_type);
        Log.d("Txn_Menu_Type........", Txn_Menu_Type);
        setContentView(R.layout.activity_mag_input_pan);

        ActivityCollector.addActivity(this);

        tran_type = findViewById(R.id.txt_txntype);
        tran_type.setText(Txn_type);

        titleTextView = (TextView) findViewById(R.id.trans_name);
        titleTextView.setText("ENTER LAST 4 DIGIT NUMBERS ON FRONT OF CARD (CCD)");
        findViewById(R.id.back_home).setVisibility(View.INVISIBLE);
        findViewById(R.id.setup_bar).setVisibility(View.INVISIBLE);

        Lastpan = (TextView) findViewById(R.id.pan_value);
        myKeyListener = new MyKeyListener(Lastpan);
        //myKeyListener = new InputAmountActivity.MyKeyListener(amount);

        findViewById(R.id.key1).setOnClickListener(myKeyListener);
        findViewById(R.id.key2).setOnClickListener(myKeyListener);
        findViewById(R.id.key3).setOnClickListener(myKeyListener);
        findViewById(R.id.key4).setOnClickListener(myKeyListener);
        findViewById(R.id.key5).setOnClickListener(myKeyListener);
        findViewById(R.id.key6).setOnClickListener(myKeyListener);
        findViewById(R.id.key7).setOnClickListener(myKeyListener);
        findViewById(R.id.key8).setOnClickListener(myKeyListener);
        findViewById(R.id.key9).setOnClickListener(myKeyListener);
        findViewById(R.id.key0).setOnClickListener(myKeyListener);
        findViewById(R.id.keyclr).setOnClickListener(myKeyListener);
        findViewById(R.id.key_delete).setOnClickListener(myKeyListener);
        findViewById(R.id.key_confirm).setOnClickListener(myKeyListener);

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("EMVDemo","Backded");
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            VFIApplication.maskHomeKey(true);
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    class MyKeyListener implements View.OnClickListener{
        StringBuilder stringBuilder = new StringBuilder("");
        TextView selectedpan;

        public MyKeyListener(TextView selectedpan)
        {
            this.selectedpan = selectedpan;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.key0:
                    if (stringBuilder.length() < 4) stringBuilder.append("0");
                    break;
                case R.id.key1:
                    if (stringBuilder.length() < 4) stringBuilder.append("1");
                    break;
                case R.id.key2:
                    if (stringBuilder.length() < 4) stringBuilder.append("2");
                    break;
                case R.id.key3:
                    if (stringBuilder.length() < 4) stringBuilder.append("3");
                    break;
                case R.id.key4:
                    if (stringBuilder.length() < 4) stringBuilder.append("4");
                    break;
                case R.id.key5:
                    if (stringBuilder.length() < 4) stringBuilder.append("5");
                    break;
                case R.id.key6:
                    if (stringBuilder.length() < 4) stringBuilder.append("6");
                    break;
                case R.id.key7:
                    if (stringBuilder.length() < 4) stringBuilder.append("7");
                    break;
                case R.id.key8:
                    if (stringBuilder.length() < 4) stringBuilder.append("8");
                    break;
                case R.id.key9:
                    if (stringBuilder.length() < 4) stringBuilder.append("9");
                    break;
                case R.id.keyclr:
                    if (stringBuilder.length() >= 0) {
                        stringBuilder.delete(0,stringBuilder.length());
                    }
                    break;
                case R.id.key_delete:
                    if (stringBuilder.length() > 0) {
                        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                    }
                    break;
                case R.id.key_confirm:
                    Lastpan4 = Lastpan.getText().toString();
                    if("".equals(Lastpan.getText().toString()))
                    {
                        ToastUtil.toastOnUiThread(MagInputPanActivity.this, "Please input Last 4 pan");
                    }
                    else if(Lastpan.getText().toString().length()!=4){
                        ToastUtil.toastOnUiThread(MagInputPanActivity.this, "Please input only 4 digit");

                    }

                    else if(!Lastpan4.equals(TransBasic.lastpannum))
                    {
                        ToastUtil.toastOnUiThread(MagInputPanActivity.this, "Incorrect input lastPan 4 digit");

                    } else {

                        Log.d("Confiurmed MAGSTRIP  ", "TXN is selected");

                       startActivity(new Intent(MagInputPanActivity.this, InputCvvActivity.class));
                        finish();


                        return;
                    }
                    break;
            }
            if (stringBuilder.length() > 0) {
                double num = Double.parseDouble(stringBuilder.toString());
                selectedpan.setText(stringBuilder);
            } else {
                selectedpan.setText("");
            }
        }
        private String big2(double d) {
            DecimalFormat format = new DecimalFormat("");
            return format.format(d);
        }

    }
    @SuppressLint("HandlerLeak")
    Handler pinInputHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            startActivity(new Intent(MagInputPanActivity.this, InputCvvActivity.class));
            finish();
        }
    };

}