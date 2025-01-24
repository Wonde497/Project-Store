package com.verifone.demo.emv.transaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.verifone.demo.emv.DBHandler;
import com.verifone.demo.emv.MenuActivity;
import com.verifone.demo.emv.Supervisor_menu_activity;
import com.verifone.demo.emv.Transactiondata;
import com.verifone.demo.emv.Utilities.ActivityCollector;
import com.verifone.demo.emv.Utilities.ToastUtil;
import com.verifone.demo.emv.Utilities.Utility;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;
public class ReversalFristActivity extends AppCompatActivity {
    private static final String TAG = "ReversalFirstActivity";
    String txn_typ;
    String txntype,Txn_Menu_Type;
    Button Enter,Cancle;
    DBHandler dbHandler;
    SharedPreferences sharedPreferences;
    Transactiondata transactiondata;
    public static String amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
        txntype = sharedPreferences.getString("txn_type", "");
        Txn_Menu_Type = sharedPreferences.getString("Txn_Menu_Type", "");
        Log.d(TAG, "Txn_type is "+txntype);
        Log.d(TAG, "Txn_Menu_Type is "+Txn_Menu_Type);

        setContentView(R.layout.activity_reversal_frist);
        ActivityCollector.addActivity(this);
        TextView tran_type = findViewById(R.id.txt_txntype);

        tran_type.setText(txntype);

//Read Amount from database
      dbHandler = new DBHandler(getApplicationContext());
      transactiondata=  new Transactiondata(sharedPreferences,getApplicationContext());


  if(dbHandler.gettransactiondata().size()>0)
    {
        dbHandler.gettransactiondata().get(dbHandler.getdatasize()-1);

        TransactionParams.getInstance().setTransactionAmount(Utility.getReadableAmount(dbHandler.gettransactiondata().get(dbHandler.getdatasize()-1).getField_04()));
        amount=dbHandler.gettransactiondata().get(dbHandler.getdatasize()-1).getField_04();

        if(Txn_Menu_Type.equals("Manualcard"))
          {
              Log.d(TAG, " Txn_Menu_Type...: Manualcard");
            startActivity(new Intent(ReversalFristActivity.this, ReversalActivity.class));
            finish();
          }
        else//Txn_Menu_Type is Chip or Contact or Magstrip
          {
              Log.d(TAG, " Txn_Menu_Type...: Not Manualcard");
            startActivity(new Intent(ReversalFristActivity.this, ReversalActivity.class));
            //dialog.dismiss();
            finish();
          }
   } else//Ohh.....Amex Empity database
      {
        if(Txn_Menu_Type.equals("Manualcard"))
        {
        Log.d(TAG, " DO PURCHASE!, NO DATA RECORDED");
        ToastUtil.toastOnUiThread(ReversalFristActivity.this, "DO PURCHASE!, NO DATA RECORDED");
        //startActivity(new Intent(ReversalFristActivity.this, Supervisor_menu_activity.class));
        finish();
        }else {
            //ToastUtil.toastOnUiThread(ReversalFristActivity.this, "DO PURCHASE!, NO DATA RECORDED");
            //startActivity(new Intent(ReversalFristActivity.this, MenuActivity.class));
            Toast.makeText(ReversalFristActivity.this, "Please Do Purchase First !", Toast.LENGTH_LONG).show();
            finish();
        }
    }
    }



    private void checkcard()
    {
      /*
        //TransactionParams.getInstance().setTransactionAmount(amtString.substring(amtString.length() - 12));

        //Set a choose payment type dialog
        //final ChoosePayTypeDialog dialog = new ChoosePayTypeDialog(ReversalFristActivity.this);
        dialog.setOnChooselistener(new ChoosePayTypeDialog.OnChooseListener() {
            @Override
            public void onClickCardType() {
                startActivity(new Intent(ReversalFristActivity.this, CheckCardActivity.class));
                dialog.dismiss();
                finish();
            }

            @Override
            public void onClickQRType() {
                startActivity(new Intent(ReversalFristActivity.this, QRTransActivity.class));
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();

        return;
*/
    }
}