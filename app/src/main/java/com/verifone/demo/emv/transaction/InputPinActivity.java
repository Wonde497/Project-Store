package com.verifone.demo.emv.transaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.verifone.demo.emv.MenuActivity;
import com.verifone.demo.emv.PrinterExActivity;
import com.verifone.demo.emv.Public_data.ISO8583msg;
import com.verifone.demo.emv.Utilities.ActivityCollector;
import com.verifone.demo.emv.Utilities.Utility;
import com.verifone.demo.emv.basic.BaseActivity;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class InputPinActivity extends BaseActivity {

    private String TAG = "InputPinActivity";

    TransBasic transBasic;
    TextView tvCardNo;
    TextView tvAmount;
    ImageView btnBack;
    TextView tvTransName;
    private Dialog customDialog;
    SharedPreferences sharedPreferences;
    String  Txn_type="",Txn_Menu_Type="";
    private ISO8583msg sp;
    public static Context context;
    Activity inputpinactiv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_pin);
        context = this;
        //TransBasic.pininputActivity = this;
        sharedPreferences=getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
        Txn_type = sharedPreferences.getString("txn_type", "");
        Txn_Menu_Type=sharedPreferences.getString("Txn_Menu_Type", "");
        Log.d("txn_type........", Txn_type);
        Log.d("Txn_Menu_Type........", Txn_Menu_Type);

        sp = new ISO8583msg(InputPinActivity.this);
        sp.loadCurrencyData();
        ActivityCollector.addActivity(this);
        Log.d(TAG,"Start do pinActivity");
        PrinterExActivity.inputpin=this;
        initView();

       /* Button transactionCancel = (Button) findViewById(R.id.transactionCancel);
        Button pininput = (Button) findViewById(R.id.pininputButton);

        pininput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transBasic.doPinPad();
            }
        });
        transactionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog if user clicks No
               showCustomDialog();

            }
        });*/

        transBasic = TransBasic.getInstance(sharedPreferences);
        if(Txn_Menu_Type.equals("Magstrip"))
        {
            transBasic.doPinPadMag();
        }else {
            transBasic.doPinPad();
        }
        Log.d(TAG,"Finished doPinPad....................");
        transBasic.setOnInputPinConfirm(new TransBasic.OnInputPinConfirm() {
            @Override
            public void onConfirm() {
                transBasic.ESignHandler = EsignHandler;
            }
        });
        Log.d(TAG,"Finished confirm pinActivity");
    }

    private void initView() {
        tvCardNo = findViewById(R.id.tv_cardno);
        tvAmount = findViewById(R.id.tv_amount);
        btnBack = findViewById(R.id.back_home);
        tvTransName = findViewById(R.id.trans_name);


        Log.d(TAG, "Input PIN loadCurrencyData = " + ISO8583msg.r_currency1);
        tvCardNo.setText(Utility.fixCardNoWithMask(TransactionParams.getInstance().getPan()));
        if(Txn_type.equals("REVERSAL")){

                Log.d(TAG,"amount:"+ISO8583msg.r_currency1 + " " + Utility.getReadableAmount(ReversalActivity.user.getField_04()));
                tvAmount.setText(ISO8583msg.r_currency1 + " " + Utility.getReadableAmount(ReversalActivity.user.getField_04()));

        } else if (TransBasic.Txn_type.equals("BALANCE_INQUIRY")) {
            tvAmount.setVisibility(View.GONE);
        } else {
            tvAmount.setText(ISO8583msg.r_currency1+ " "+ Utility.getReadableAmount(TransactionParams.getInstance().getTransactionAmount()));
            }
        btnBack.setVisibility(View.INVISIBLE);
        tvTransName.setText("Input Your PIN to Verify \n The Transaction ");

      //  tvTransName.setBackgroundColor(R.color.blackgreen2);
        int colorbg = getResources().getColor(R.color.blackgreen2);
        int colortxt =  getResources().getColor(R.color.white);
        tvTransName.setBackgroundColor(colorbg);
        tvTransName.setTextColor(colortxt);


    }

    @SuppressLint("HandlerLeak")
    Handler EsignHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "EsignHandler tranStatus =.... " + true);
            if (transBasic.transStatus)
            {
                Log.d(TAG, "tranStatus = " + true);
                startActivity(new Intent(InputPinActivity.this, ESignActivity.class));
                finish();
            }else {
                ActivityCollector.finishAllTransActivity();
            }

        }
    };
    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog, null);
        builder.setView(dialogView);

        // Initialize the TextView and Buttons
        TextView title = dialogView.findViewById(R.id.messageTextView);
        Button positiveButton = dialogView.findViewById(R.id.positiveButton);
        Button negativeButton = dialogView.findViewById(R.id.negativeButton);
        title.setText(" Are you sure you want to cancel the transaction?");
        title.setTypeface(null, Typeface.BOLD_ITALIC);


        // Set the message
        // messageTextView.setText("Are you sure you want to logout?");

        // Set click listeners for the buttons
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform logout action here
                Intent intent = new Intent(InputPinActivity.this, MenuActivity.class);
                startActivity(intent);
                finish(); // Finish the current activity after logout
                customDialog.dismiss(); // Dismiss the dialog after performing the action
            }
        });

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog if user clicks No
                customDialog.dismiss();
                transBasic.doPinPad();
            }
        });

        // Create and show the dialog
        customDialog = builder.create();
        customDialog.show();
    }
    @Override
    public void onBackPressed() {
            showCustomDialog();
    }

}
