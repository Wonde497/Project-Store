package com.verifone.demo.emv.transaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.verifone.demo.emv.MenuActivity;
import com.verifone.demo.emv.DBHandler;
import com.verifone.demo.emv.Transactiondata;
import com.verifone.demo.emv.Utilities.ActivityCollector;
import com.verifone.demo.emv.Utilities.ToastUtil;
import com.verifone.demo.emv.connection;

import java.text.DecimalFormat;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class InputCvvActivity extends AppCompatActivity {
    private static final String TAG = "MAGINPUT_CVV";
    TextView cvvnum,tran_type,titleTextView;
    Button skip;
    MyKeyListener myKeyListener,myKeyListener1;
    public static String CVV="";
    private TransBasic transBasic;
    SharedPreferences sharedPreferences;
    String  Txn_type="",Txn_Menu_Type="";
    connection loading;
    Transactiondata transactiondata;
    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
        Txn_type = sharedPreferences.getString("txn_type", "");
        Txn_Menu_Type=sharedPreferences.getString("Txn_Menu_Type", "");
        Log.d("txn_type........", Txn_type);
        Log.d("Txn_Menu_Type........", Txn_Menu_Type);
        setContentView(R.layout.activity_input_cvv);
        sharedPreferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
        transBasic = TransBasic.getInstance(sharedPreferences);

        transactiondata=new Transactiondata(sharedPreferences,getApplicationContext());
        dbHandler = new DBHandler(getApplicationContext());

        ActivityCollector.addActivity(this);

        tran_type = findViewById(R.id.txt_txntype);
        tran_type.setText(Txn_type);

        titleTextView = (TextView) findViewById(R.id.trans_name);
        titleTextView.setText("ENTER CVV ON BACK OF CARD");
        findViewById(R.id.back_home).setVisibility(View.INVISIBLE);
        findViewById(R.id.setup_bar).setVisibility(View.INVISIBLE);

        cvvnum = (TextView) findViewById(R.id.cvv_value);
        skip=(Button) findViewById(R.id.skip);
        myKeyListener = new MyKeyListener(cvvnum);
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
        findViewById(R.id.skip).setOnClickListener(myKeyListener);

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("INPUTCVV","Backded");
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
        TextView selectedcvv;

        public MyKeyListener(TextView selectedcvv)
        {
            this.selectedcvv = selectedcvv;
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
                case R.id.skip:
                    CVV = "";
                    if(Txn_type.equals("REVERSAL"))
                    {
                        Log.d("Confiurmed cvv ", " SKIP CCV BOTH REVERSAL TXN IS SELECTED");
                        try {
                            TransBasic.getInstance(sharedPreferences).manualcardrequest();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        finish();
                    }
                    else if(Txn_Menu_Type.equals("Manualcard") && !(Txn_type.equals("REVERSAL")))
                    {
                        Log.d("input cvv ", "input cvv Manualcard SKIP TXN IS SELECTED");
                        try {
                            TransBasic.getInstance(sharedPreferences).manualcardrequest();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        finish();
                    }
                    /*else if(Txn_Menu_Type.equals("Magstrip") && TransBasic.magservice_code.equals("6"))
                    {
                        //PAN.charAt(PAN.length() - 1);
                        startActivity(new Intent(InputCvvActivity.this, InputPinActivity.class));
                        finish();

                    }*/


                    else {
                        Log.d(TAG, "SKIP CCV Magstrip TXN IS SELECTED");

                        Log.d(TAG, "input cvv Magstrip transBasic.doPinPad() is call");
                        //transBasic.doPinPad();
                        // transBasic.doPinPadMag();
                        startActivity(new Intent(InputCvvActivity.this, InputPinActivity.class));
                        finish();
                    }
                    break;

                case R.id.key_confirm:
                    CVV = cvvnum.getText().toString();
                    if("".equals(cvvnum.getText().toString()))
                    {
                        ToastUtil.toastOnUiThread(InputCvvActivity.this, "Cvv is not Empity");
                    }
                    else if(cvvnum.getText().toString().length()>4 || cvvnum.getText().toString().length()<3)
                    {
                        ToastUtil.toastOnUiThread(InputCvvActivity.this, "Please input only 3 or 4 digit");

                    } else if (Txn_Menu_Type.equals("Magstrip") && Txn_type.equals("REVERSAL"))
                      {
                        Log.d("Confiurmed cvv ", "CVV MAGSTRIP REVERSAL TXN IS SELECTED");
                          startActivity(new Intent(InputCvvActivity.this, ReversalActivity.class));
                        finish();

                    }else if (Txn_Menu_Type.equals("Manualcard") && Txn_type.equals("REVERSAL"))
                       {
                           Log.d("Confirmed cvv ", "CCV Manualcard REVERSAL TXN IS SELECTED");
                           try {
                               TransBasic.getInstance(sharedPreferences).manualcardrequest();
                           } catch (RemoteException e) {
                               e.printStackTrace();
                           }
                           finish();

                    }else if(Txn_Menu_Type.equals("Manualcard"))
                      {
                          Log.d("input cvv ", "input cvv Manualcard Other TXN IS SELECTED");
                        try {
                            TransBasic.getInstance(sharedPreferences).manualcardrequest();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        finish();
                  } /*else if(Txn_Menu_Type.equals("Magstrip") && TransBasic.magservice_code.equals("6"))
                    {
                        //PAN.charAt(PAN.length() - 1);
                        startActivity(new Intent(InputCvvActivity.this, InputPinActivity.class));
                        finish();

                    }
                    */

                    else
                    {

                        Log.d(TAG, "input cvv Magstrip transBasic.doPinPad() is call");
                       // transBasic.doPinPadMag();
                        startActivity(new Intent(InputCvvActivity.this, InputPinActivity.class));
                        finish();
                        //Log.d(TAG  ,"pinInputHandler pass.............");


                    }
                    finish();
                    break;

            }
            if (stringBuilder.length() > 0)
            {
                double num = Double.parseDouble(stringBuilder.toString());
                selectedcvv.setText(stringBuilder);
            } else {
                selectedcvv.setText("");
            }
        }
        private String big2(double d) {
            DecimalFormat format = new DecimalFormat("");
            return format.format(d);
        }

    }
    @SuppressLint("HandlerLeak")
    Handler pinInputHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            CVV = cvvnum.getText().toString();
            startActivity(new Intent(InputCvvActivity.this, InputPinActivity.class));
            finish();
        }
    };
}