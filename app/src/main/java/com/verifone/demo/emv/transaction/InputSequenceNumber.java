package com.verifone.demo.emv.transaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.verifone.demo.emv.Transactiondata;
import com.verifone.demo.emv.Utilities.ActivityCollector;
import com.verifone.demo.emv.Utilities.ToastUtil;

import java.text.DecimalFormat;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;
public class InputSequenceNumber extends AppCompatActivity {
    private static final String TAG = "INPUT_SEQUENCE";
    TextView sequencenum,tran_type,titleTextView;
    MyKeyListener myKeyListener;
    public  static String seqence="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prfs = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
        String txntype = prfs.getString("txn_type", "");
        Log.d("EMVDemo Input", txntype);

        setContentView(R.layout.activity_input_sequence_number);

        ActivityCollector.addActivity(this);

        tran_type = findViewById(R.id.txt_txntype);
        tran_type.setText(txntype);

        titleTextView = (TextView) findViewById(R.id.trans_name);
        titleTextView.setText("ENTER SEQUENCE NUMBER");
        findViewById(R.id.back_home).setVisibility(View.INVISIBLE);
        findViewById(R.id.setup_bar).setVisibility(View.INVISIBLE);

        sequencenum = (TextView) findViewById(R.id.sequencenumber_value);
        myKeyListener = new MyKeyListener(sequencenum);
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
        TextView selectedsequencenumber;

        public MyKeyListener(TextView selectedsequencenumber)
        {
            this.selectedsequencenumber = selectedsequencenumber;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.key0:
                    if (stringBuilder.length() < 9) stringBuilder.append("0");
                    break;
                case R.id.key1:
                    if (stringBuilder.length() < 9) stringBuilder.append("1");
                    break;
                case R.id.key2:
                    if (stringBuilder.length() < 9) stringBuilder.append("2");
                    break;
                case R.id.key3:
                    if (stringBuilder.length() < 9) stringBuilder.append("3");
                    break;
                case R.id.key4:
                    if (stringBuilder.length() < 9) stringBuilder.append("4");
                    break;
                case R.id.key5:
                    if (stringBuilder.length() < 9) stringBuilder.append("5");
                    break;
                case R.id.key6:
                    if (stringBuilder.length() < 9) stringBuilder.append("6");
                    break;
                case R.id.key7:
                    if (stringBuilder.length() < 9) stringBuilder.append("7");
                    break;
                case R.id.key8:
                    if (stringBuilder.length() < 9) stringBuilder.append("8");
                    break;
                case R.id.key9:
                    if (stringBuilder.length() < 9) stringBuilder.append("9");
                    break;
                case R.id.keyclr:
                    if (stringBuilder.length() >= 0) {
                        stringBuilder.delete(0,stringBuilder.length());
                    }
                    break;
                case R.id.key_delete:
                    if (stringBuilder.length() >0) {
                        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                    }
                    break;
                case R.id.key_confirm:
                    if("".equals(sequencenum.getText().toString()))
                    {
                        ToastUtil.toastOnUiThread(InputSequenceNumber.this, "Please input Sequence Number");
                    }else if(sequencenum.getText().toString().length()!=9){
                        ToastUtil.toastOnUiThread(InputSequenceNumber.this, "Please input only 9 digit");

                    }else {
                        seqence=sequencenum.getText().toString();
                        startActivity(new Intent(InputSequenceNumber.this, Pre_Auth_Completion.class));
                        finish();

                        return;
                    }
                    break;
            }
            if (stringBuilder.length() > 0) {
                double num = Double.parseDouble(stringBuilder.toString());
                selectedsequencenumber.setText(stringBuilder);
            } else {
                selectedsequencenumber.setText("");
            }
        }
        private String big2(double d) {
            DecimalFormat format = new DecimalFormat("");
            return format.format(d);
        }
    }
}