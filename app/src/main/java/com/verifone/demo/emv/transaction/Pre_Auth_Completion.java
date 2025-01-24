package com.verifone.demo.emv.transaction;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.verifone.demo.emv.DBHandler;
import com.verifone.demo.emv.MenuActivity;
import com.verifone.demo.emv.Transactiondata;
import com.verifone.demo.emv.Utilities.ActivityCollector;
import com.verifone.demo.emv.Utilities.ToastUtil;
import com.verifone.demo.emv.loadingfragment;

import java.text.DecimalFormat;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class Pre_Auth_Completion extends AppCompatActivity {
    private static final String TAG = "PRE_AUTH_COMP";
    TextView tran_type,titleTextView;

    private EditText approvalcode;
    Button Approve,Cancel;
    public static String approvcode="";

    String Txn_type,Txn_Menu_Type;
    private TransBasic transBasic;
    FragmentTransaction transaction;
    public static Context context;
    Transactiondata transactiondata;
    DBHandler dbHandler;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);

        Txn_type = sharedPreferences.getString("txn_type", "");
        Txn_Menu_Type = sharedPreferences.getString("Txn_Menu_Type", "");
        Log.d(TAG,"txntype........"+Txn_type);
        Log.d(TAG,"Txn_Menu_Type........"+Txn_Menu_Type);

        setContentView(R.layout.activity_pre_auth_completion);

        ActivityCollector.addActivity(this);
        transBasic = TransBasic.getInstance(sharedPreferences);

        transactiondata=new Transactiondata(sharedPreferences,getApplicationContext());
        dbHandler = new DBHandler(getApplicationContext());
        //transaction = getSupportFragmentManager().beginTransaction();

        tran_type = findViewById(R.id.txt_txntype);
        tran_type.setText(Txn_type);

        titleTextView = (TextView) findViewById(R.id.trans_name);
        titleTextView.setText("ENTER APPROVAL CODE");
        findViewById(R.id.back_home).setVisibility(View.INVISIBLE);
        findViewById(R.id.setup_bar).setVisibility(View.INVISIBLE);

        approvalcode = (EditText) findViewById(R.id.approval_value);
        Approve = (Button) findViewById(R.id.idBtnapprove);
        Cancel = (Button) findViewById(R.id.idBtncancel);

        Approvalcode();

    }
    private void Approvalcode()
    {
        approvalcode = (EditText) findViewById(R.id.approval_value);
        Approve = (Button) findViewById(R.id.idBtnapprove);

        Approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                approvcode = approvalcode.getText().toString();
                if("".equals(approvalcode.getText().toString()))
                {
                    ToastUtil.toastOnUiThread(Pre_Auth_Completion.this, "Please input approval code");
                }
                else if(approvalcode.getText().toString().length()!=6)
                {
                    ToastUtil.toastOnUiThread(Pre_Auth_Completion.this, "Please input only 6 digit");

                }
                else if(Txn_type.equals("REFUND") && Txn_Menu_Type.equals("Manualcard"))
                {

                    Log.d("REFUND ", "Manualcard REFUND TXN IS SELECTED");
                    startActivity(new Intent(Pre_Auth_Completion.this, InputAmountActivity.class));
                    // startActivity(new Intent(Pre_Auth_Completion.this, InputPinActivity.class));
                    finish();
                    return;

                } else if(sharedPreferences.getString("printtype", "").equals("isphoneauth"))
                {
                    Log.d(TAG, "isphoneauth IS SELECTED");
                    transBasic.printTest(1);
                    return;

                }
                else if(Txn_type.equals("REFUND"))
                {
                    Log.d("REFUND ", "REFUND TXN IS SELECTED");
                    startActivity(new Intent(Pre_Auth_Completion.this, Supervisorlogin.class));
                    finish();
                    return;

                }

                else {

                    Log.d("PRE_AUTH_COMPLETION ", "PRE_AUTH_COMPLETION TXN IS SELECTED");
                    startActivity(new Intent(Pre_Auth_Completion.this, InputAmountActivity.class));
                    finish();
                    return;
                }


            }
        });

        approvalcode.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            Approve.performClick();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approvcode = "";
                finish();
            }
        });

    };

}