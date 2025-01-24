package com.verifone.demo.emv.transaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.verifone.demo.emv.DBHandler;
import com.verifone.demo.emv.MenuActivity;
import com.verifone.demo.emv.Supervisor_menu_activity;
import com.verifone.demo.emv.Transactiondata;
import com.verifone.demo.emv.Utilities.ActivityCollector;
import com.verifone.demo.emv.Utilities.ToastUtil;
import com.verifone.demo.emv.basic.BaseActivity;
import com.verifone.demo.emv.basic.ChoosePayTypeDialog;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class ManualTestActivity extends AppCompatActivity
{
    private static final String TAG = "ManualTestActivity";
    TextView Cardnum,titleTextView;
    MyKeyListener myKeyListener;
    public static String cardnumber="";
    SharedPreferences sharedPreferences;
    private TransBasic transBasic;
    Transactiondata transactiondata;
    String  txn_type="",Txn_Menu_Type="";
    DBHandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
        txn_type = sharedPreferences.getString("txn_type", "");
        Txn_Menu_Type = sharedPreferences.getString("Txn_Menu_Type", "");
        Log.d(TAG, "Txn_type is   "+txn_type);
        Log.d(TAG, "Txn_Menu_Type is "+Txn_Menu_Type);

        setContentView(R.layout.activity_manual_test);
        ActivityCollector.addActivity(this);

        transBasic = TransBasic.getInstance(sharedPreferences);
        transactiondata=new Transactiondata(sharedPreferences,getApplicationContext());
        dbHandler = new DBHandler(getApplicationContext());

        TextView tran_type = findViewById(R.id.txt_txntype);
        tran_type.setText(txn_type);

        titleTextView = (TextView) findViewById(R.id.inputinfo);
        titleTextView.setText("ENTER CARD NUMBER ");

        findViewById(R.id.back_home).setVisibility(View.INVISIBLE);
        findViewById(R.id.setup_bar).setVisibility(View.INVISIBLE);

        Cardnum = (TextView)findViewById(R.id.cardnum);

        myKeyListener = new MyKeyListener(Cardnum);

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

//        VFIApplication.maskHomeKey(false);
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

        TextView selectedcard;


        public MyKeyListener(TextView selectedcard) {
            this.selectedcard = selectedcard;

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.key0:
                    if (stringBuilder.length() < 19) stringBuilder.append("0");
                    break;
                case R.id.key1:
                    if (stringBuilder.length() < 19) stringBuilder.append("1");
                    break;
                case R.id.key2:
                    if (stringBuilder.length() < 19) stringBuilder.append("2");
                    break;
                case R.id.key3:
                    if (stringBuilder.length() < 19) stringBuilder.append("3");
                    break;
                case R.id.key4:
                    if (stringBuilder.length() < 19) stringBuilder.append("4");
                    break;
                case R.id.key5:
                    if (stringBuilder.length() < 19) stringBuilder.append("5");
                    break;
                case R.id.key6:
                    if (stringBuilder.length() < 19) stringBuilder.append("6");
                    break;
                case R.id.key7:
                    if (stringBuilder.length() < 19) stringBuilder.append("7");
                    break;
                case R.id.key8:
                    if (stringBuilder.length() < 19) stringBuilder.append("8");
                    break;
                case R.id.key9:
                    if (stringBuilder.length() < 19) stringBuilder.append("9");
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
                    cardnumber=Cardnum.getText().toString();
                    if("".equals(Cardnum.getText().toString()))
                    {
                        ToastUtil.toastOnUiThread(ManualTestActivity.this, "Please input Card number");
                  } else if(Cardnum.getText().toString().length()>19 || Cardnum.getText().toString().length()<13)
                    {
                        ToastUtil.toastOnUiThread(ManualTestActivity.this, "Please input only from 13 to 19 digit");

                    }else if(!checkLuhn(Cardnum.getText().toString()))
                    {
                        ToastUtil.toastOnUiThread(ManualTestActivity.this, "Please input only valid character");

                    }
                    else if(txn_type.equals("REVERSAL"))
                      {

                        Log.d("ManualTestActivity  ", "Reversal is selected");
                          Log.d("ManualTestActivity  ", "Input cardnumber  "+cardnumber);
                            if (ReversalActivity.user.getField_02().equals(cardnumber))
                            {
                                Log.d("ManualTestActivity  ", "Savedpannumber  "+ ReversalActivity.user.getField_02());
                                Log.d("ManualTestActivity  ", "Saved Txn_type  "+ ReversalActivity.user.getTxn_type());
                                if (ReversalActivity.user.getTxn_type().equals("PURCHASE") || ReversalActivity.user.getTxn_type().equals("CASH_ADVANCE"))
                                {
                                if (ReversalActivity.user.getLastflag().equals("0"))
                                 {
                                        ToastUtil.toastOnUiThread(ManualTestActivity.this, "CURRENT TXN!,APPROVAL IN PROGRESS...");
                                        Log.d(TAG, "APPROVAL IN PROGRESS... Finshied, Card Number and Last_txn Checking For Approval");

                                        startActivity(new Intent(ManualTestActivity.this, ManualTest_Exdate.class));
                                        finish();
                                    } else
                                       {
                                        Log.d("Flage! ", "Last Transaction Aborted");
                                        ToastUtil.toastOnUiThread(ManualTestActivity.this, "LAST TXN ABORTED  " +
                                                ReversalActivity.user.getLastflag());
                                           finish();
                                       }

                                } else
                                   {
                                    Log.d("ABORTED! ", "TXN CAN NOT REVERSED, TXN NOT PURCHASE");
                                    ToastUtil.toastOnUiThread(ManualTestActivity.this, "ABORTED!,TRANSACTION CAN NOT BE REVERSED ");
                                       finish();
                                   }
                          } else
                             {
                                Log.d("CARD ISSUE! ", "INCORRECT CARD INSERTED!, TRY AGAIN ");
                                ToastUtil.toastOnUiThread(ManualTestActivity.this, "CARD ISSUE!, INCORRECT CARD INSERTED!, TRY AGAIN  ");
                                 finish();
                        }
                 } else {//TXN NOT REVERSAL
                        Log.d("ManualTestActivity! ", "OTHER TXN, IS NOT REVERSAL ");
                        startActivity(new Intent(ManualTestActivity.this, ManualTest_Exdate.class));
                        finish();

                        return;
                    }
                    break;
            }
            if (stringBuilder.length() > 0) {
                double num = Double.parseDouble(stringBuilder.toString());
                selectedcard.setText(stringBuilder);

            } else {
                selectedcard.setText("");


            }
        }
        private String big2(double d) {
            DecimalFormat format = new DecimalFormat("");
            return format.format(d);
        }
    }
    static boolean checkLuhn(String cardNo)
    {
        int nDigits = cardNo.length();

        int nSum = 0;
        boolean isSecond = false;
        for (int i = nDigits - 1; i >= 0; i--)
        {

            int d = cardNo.charAt(i) - '0';

            if (isSecond == true)
                d = d * 2;

            // We add two digits to handle
            // cases that make two digits
            // after doubling
            nSum += d / 10;
            nSum += d % 10;

            isSecond = !isSecond;
        }
        return (nSum % 10 == 0);
    }
    public static boolean isValidPanCardNo(String panCardNo)
    {

        // Regex to check valid PAN Card number.
        String regex = "[A-Z]{5}[0-9]{4}[A-Z]{1}";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the PAN Card number
        // is empty return false
        if (panCardNo == null)
        {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given
        // PAN Card number using regular expression.
        Matcher m = p.matcher(panCardNo);

        // Return if the PAN Card number
        // matched the ReGex
        return m.matches();
    }
}