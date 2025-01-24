package com.verifone.demo.emv.transaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.verifone.demo.emv.Public_data.ISO8583msg;
import com.verifone.demo.emv.Utilities.ActivityCollector;
import com.verifone.demo.emv.Utilities.ToastUtil;
import com.verifone.demo.emv.basic.BaseActivity;

import java.text.DecimalFormat;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

/**
 * created by laikey for verifone smartpos demo at 2017/06/30
 */

public class InputAmountActivity extends BaseActivity {
    private static final String TAG = "InputAmountActivity";
    TextView amount,tran_type,titleTextView,titleTextView1;
    MyKeyListener myKeyListener,myKeyListener1;
    SharedPreferences sharedPreferences;
    String txntype,Txn_Menu_Type;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
        txntype = sharedPreferences.getString("txn_type", "");
        Txn_Menu_Type = sharedPreferences.getString("Txn_Menu_Type", "");
        Log.d(TAG, "Txn_type is "+txntype);
        Log.d(TAG, "Txn_Menu_Type is "+Txn_Menu_Type);

        setContentView(R.layout.activity_input_amount);
        ActivityCollector.addActivity(this);

        tran_type = findViewById(R.id.txt_txntype);

        if (txntype.equals("PURCHASE"))
        {
            tran_type.setText("SALE");
        }else
        {
            tran_type.setText(txntype);
        }
        //tran_type.setText(txntype);

        titleTextView = (TextView) findViewById(R.id.trans_name);
        titleTextView1 = (TextView) findViewById(R.id.inputamount_prompt);

        ISO8583msg sp = new ISO8583msg(InputAmountActivity.this);
        sp.loadCurrencyData();

        if(txntype.equals("REFUND"))
        {
            Log.d(TAG,"loadCurrencyData  "+ ISO8583msg.r_currency1);
            titleTextView1.setText("Refund Amount  ("+ ISO8583msg.r_currency1 +")");
        }
        else
        {
            Log.d(TAG,"loadCurrencyData  "+ ISO8583msg.r_currency1);
            titleTextView1.setText("Purchase Amount  ("+ ISO8583msg.r_currency1 +")");
        }

        findViewById(R.id.back_home).setVisibility(View.INVISIBLE);
        findViewById(R.id.setup_bar).setVisibility(View.INVISIBLE);

        amount = (TextView) findViewById(R.id.amount_value);
        myKeyListener = new MyKeyListener(amount);


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
        TextView selectedAmount;

        public MyKeyListener(TextView selectedAmount) {
            this.selectedAmount = selectedAmount;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.key0:
                    if (stringBuilder.length() > 0 & stringBuilder.length() < 12)
                        stringBuilder.append("0");
                    break;
                case R.id.key1:
                    if (stringBuilder.length() < 12) stringBuilder.append("1");
                    break;
                case R.id.key2:
                    if (stringBuilder.length() < 12) stringBuilder.append("2");
                    break;
                case R.id.key3:
                    if (stringBuilder.length() < 12) stringBuilder.append("3");
                    break;
                case R.id.key4:
                    if (stringBuilder.length() < 12) stringBuilder.append("4");
                    break;
                case R.id.key5:
                    if (stringBuilder.length() < 12) stringBuilder.append("5");
                    break;
                case R.id.key6:
                    if (stringBuilder.length() < 12) stringBuilder.append("6");
                    break;
                case R.id.key7:
                    if (stringBuilder.length() < 12) stringBuilder.append("7");
                    break;
                case R.id.key8:
                    if (stringBuilder.length() < 12) stringBuilder.append("8");
                    break;
                case R.id.key9:
                    if (stringBuilder.length() < 12) stringBuilder.append("9");
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
                    if("0.00".equals(amount.getText().toString()))
                    {
                        ToastUtil.toastOnUiThread(InputAmountActivity.this, "Please input amount");
                    }else {
                        String amtString = "";
                        // In Português, the decimal point symbol is ","  not "." Here check the amount
                        if (!amount.getText().toString().contains(".")){
                            amtString = "000000000000" + amount.getText().toString().replace(",", "");
                        }else {
                            amtString = "000000000000" + amount.getText().toString().replace(".", "");
                        }
                        Log.d(TAG, "onClick: ");


                        TransactionParams.getInstance().setTransactionAmount(amtString.substring(amtString.length() - 12));

                        //Set a choose payment type dialog
                        if(Txn_Menu_Type.equals("Manualcard"))
                        {
                            startActivity(new Intent(InputAmountActivity.this, ManualTestActivity.class));
                            finish();
                        }else {
                            startActivity(new Intent(InputAmountActivity.this, CheckCardActivity.class));
                            //dialog.dismiss();
                            finish();

                        }


                        /*final ChoosePayTypeDialog dialog = new ChoosePayTypeDialog(InputAmountActivity.this);
                        dialog.setOnChooselistener(new ChoosePayTypeDialog.OnChooseListener() {
                            @Override
                            public void onClickCardType() {
                                startActivity(new Intent(InputAmountActivity.this, CheckCardActivity.class));
                                dialog.dismiss();
                                finish();
                            }

                           /* @Override
                            public void onClickQRType() {
                                startActivity(new Intent(InputAmountActivity.this, QRTransActivity.class));
                                dialog.dismiss();
                                finish();

                        });
                        dialog.show();
}*/
                        return;
                    }
                    break;
            }
            if (stringBuilder.length() > 0) {
                double num = Double.parseDouble(stringBuilder.toString());
                selectedAmount.setText(big2(num / 100));
            } else {
                selectedAmount.setText("0.00");
            }
        }
        private String big2(double d) {
            DecimalFormat format = new DecimalFormat("0.00");
            return format.format(d);
        }
    }
    @Override
    public void onBackPressed()
    {
finish();
    }
}

