package com.verifone.demo.emv.transaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.verifone.demo.emv.DBHandler;
import com.verifone.demo.emv.MenuActivity;
import com.verifone.demo.emv.Transactiondata;
import com.verifone.demo.emv.Utilities.ActivityCollector;
import com.verifone.demo.emv.Utilities.ToastUtil;
import com.verifone.demo.emv.basic.BaseActivity;
import com.verifone.demo.emv.basic.ChoosePayTypeDialog;
import com.verifone.demo.emv.connection;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class ManualTest_Exdate extends AppCompatActivity {
    private static final String TAG = "ManualTestExdate";
    private TransBasic transBasic;
    TextView Exdate,titleTextView;
    MyKeyListener myKeyListener;
   public static String expiredate="";
    SharedPreferences sharedPreferences;
    String  txntype,Txn_Menu_Type;
    Transactiondata transactiondata;
    DBHandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
        txntype = sharedPreferences.getString("txn_type", "");
        Txn_Menu_Type= sharedPreferences.getString("Txn_Menu_Type", "");
        Log.d("EMVDemo Input",txntype);

        setContentView(R.layout.activity_manual_test_exdate);
        ActivityCollector.addActivity(this);

        TextView tran_type = findViewById(R.id.txt_txntype);
        tran_type.setText(txntype);

        transBasic = TransBasic.getInstance(sharedPreferences);

        transactiondata=new Transactiondata(sharedPreferences,getApplicationContext());
        dbHandler = new DBHandler(getApplicationContext());


        titleTextView = (TextView) findViewById(R.id.inputinfo);
        titleTextView.setText("ENTER CARD EX_DATE ");

        findViewById(R.id.back_home).setVisibility(View.INVISIBLE);
        findViewById(R.id.setup_bar).setVisibility(View.INVISIBLE);


        Exdate = (TextView)findViewById(R.id.exdate);
        myKeyListener = new MyKeyListener(Exdate);


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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        transBasic.abortEMV();
    }


    class MyKeyListener implements View.OnClickListener{
        StringBuilder stringBuilder = new StringBuilder("");

        TextView selectedexdate;

        public MyKeyListener(TextView selectedexdate) {
            this.selectedexdate = selectedexdate;
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
                    if (stringBuilder.length() >0) {
                        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                    }
                    break;
                case R.id.key_confirm:
                    if("".equals(Exdate.getText().toString()))
                    {
                        ToastUtil.toastOnUiThread(ManualTest_Exdate.this, "Please input Card  Ex Date");
                    } else if(Exdate.getText().toString().length()<4)
                    {
                        ToastUtil.toastOnUiThread(ManualTest_Exdate.this, "Please input only 4 digit");

     }else
       {
        String inputyear,inputmonth,posyear,posmonth,date=new SimpleDateFormat("yy/MM/dd", Locale.getDefault()).format(new Date());
          Log.d("Manual card entry  ","date is "+date);
           posyear=date.substring(0,2);//String.valueOf(date.charAt(0)+date.charAt(1));
           posmonth=date.substring(3,5);//String.valueOf(date.charAt(3)+date.charAt(4));
           inputmonth=Exdate.getText().toString().substring(0,2);//String.valueOf(date.charAt(0)+date.charAt(1));
           inputyear=Exdate.getText().toString().substring(2,4);//String.valueOf(date.charAt(2)+date.charAt(3));
                        Log.d("Manual card entry  ","posyear "+posyear);
                        Log.d("Manual card entry  ","posmonth "+posmonth);
                        Log.d("Manual card entry  ","inpuyear "+inputyear);
                        Log.d("Manual card entry  ","inputmonth "+inputmonth);

    if(Integer.parseInt(inputmonth)<=12)
      {
     if(Integer.parseInt(posyear)<Integer.parseInt(inputyear)||
             (posyear.equals(inputyear)&&Integer.parseInt(posmonth)<Integer.parseInt(inputmonth)))
     {
         Log.d("Manual card entry  ", "TXN is selected");

         expiredate = Exdate.getText().toString();

         startActivity(new Intent(ManualTest_Exdate.this, InputCvvActivity.class));
         finish();
   }else{
    ToastUtil.toastOnUiThread(ManualTest_Exdate.this, "Your Card is Expired");

    }
  }else{
      ToastUtil.toastOnUiThread(ManualTest_Exdate.this, "Month Between 01-12");

        }
      return;
        }
           break;
            }
            if (stringBuilder.length() > 0)
            {
                double num = Double.parseDouble(stringBuilder.toString());
                selectedexdate.setText(stringBuilder);
            } else
              {

              selectedexdate.setText("");
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
            startActivity(new Intent(ManualTest_Exdate.this, InputPinActivity.class));
            finish();
        }
    };

}