package com.verifone.demo.emv;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.support.v7.widget.SwitchCompat;
import android.widget.Toast;


import com.verifone.demo.emv.data_handlers.terminal;

import java.util.ArrayList;
import java.util.List;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class Manage_Transactions extends AppCompatActivity {

    private static final String TAG = "MANAGTRANS";
    SwitchCompat manualcardentry;
    SwitchCompat pre_authcmp;
    SwitchCompat Balance;
    SwitchCompat pre_auth;
    SwitchCompat switchCompatmanualcard;
    SwitchCompat purchasecashback;
    SwitchCompat refund;

    SwitchCompat PhoneAuth;
    SwitchCompat deposit;
    private static DBTerminal dbTerminal;
    DBTerminal.Terminal_functions1 terminal_fun;
    public static String Txn_col,Txn_colManualCard="",Txn_colPreauth,Txn_colPreauthcmp,Txn_colPurchase,
            Txn_colBalance,Txn_colRefund,Txn_colPhoneAuth,Txn_colDeposit;
    public static String Sta_col,Sta_colManualCard="",Sta_colPreauth,Sta_colPreauthcmp,Sta_colPurchase,
            Sta_colBalance,Sta_colRefund,Sta_colPhoneAuth,Sta_colDeposit;

    public int transaction_type1;
    public int postion;



  //private static int depo=0,settl=0,manucard=0,preauth=0,preauthcmp=0,refud=0,pruch=0,magstrip=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_transactions);


        manualcardentry = (SwitchCompat) findViewById(R.id.manualcardentry);
        pre_authcmp = (SwitchCompat) findViewById(R.id.pre_authcmp);
        Balance = (SwitchCompat) findViewById(R.id.balanceinq);
        pre_auth = (SwitchCompat) findViewById(R.id.pre_auth);
        purchasecashback = (SwitchCompat) findViewById(R.id.purchasecashback);
        refund = (SwitchCompat) findViewById(R.id.refund);
        PhoneAuth = (SwitchCompat) findViewById(R.id.Phone);
        deposit = (SwitchCompat) findViewById(R.id.deposit);
        // Find the SwitchCompat view

        txn_loadData();
        //
        TransMenuView();

    }

    public void txn_loadData()
    {
        String selection = "";
        String[] selectionArgs = {};
        Log.d("DBRES", "txn here Started ");
        List<terminal> txninfolist = new ArrayList<terminal>();
        dbTerminal = new DBTerminal(Manage_Transactions.this);

        Log.d("DBRES", "txn SPDH here Started 1 ");
        DBTerminal.Terminal_functions1 Terminal_fun=dbTerminal.new Terminal_functions1();
        Log.d("DBRES", "txn here3");
        txninfolist= Terminal_fun.viewtxn(selection,selectionArgs);
        int rows = txninfolist.size();
        transaction_type1=txninfolist.size();
        Log.d("DBRES", "txn here2  " + rows);
        Log.d("DBRES", "txn here for loop");
        for(int i = 0; i < rows; i ++) {
            terminal row = null;
            if (i > -1) {
                row = txninfolist.get(i);
                //txn = txninfolist.get(i);
                Log.d("DBRES", "Transaction_type : " + row.getTransaction_type());
                //Log.d("DBRES", "Preauth : "+  row.getPreauth());
                // Log.d("DBRES", "Preauthcmp : "+  row.getPreauthcmp());
                Log.d("DBRES", "Status : " + row.getStatus());

                Txn_col = row.getTransaction_type();
                Sta_col = row.getStatus();

//ManualCard...............................
                if (Txn_col.equals("ManualCard")) {
                    if (Txn_col.equals("ManualCard") && Sta_col.equals("1"))
                    {
                        Txn_colManualCard = Txn_col;
                        Sta_colManualCard = Sta_col;
                        manualcardentry.setChecked(true);
                    //manualcardentry.setImageResource(R.drawable.manualcardentryen);

                    Log.d(TAG, " ManualCard txn type if condtion " + Txn_colManualCard);
                    Log.d(TAG, " ManualCard Statuse   " + Sta_colManualCard);
                } else {
                    Txn_colManualCard = Txn_col;
                    Sta_colManualCard= Sta_col;
                    manualcardentry.setChecked(false);
                    //manualcardentry.setImageResource(R.drawable.manualcardentrydis);

                    Log.d(TAG, " ManualCard txn type else condtion  " + Txn_colManualCard);
                    Log.d(TAG, " ManualCard Statuse   " + Sta_colManualCard);
                }
            }
//Preauth..................................................
                if (Txn_col.equals("Preauth")) {
                    if(Txn_col.equals("Preauth")  && Sta_col.equals("1")) {
                    Txn_colPreauth=Txn_col;
                    Sta_colPreauth=Sta_col;
                    pre_auth.setChecked(true);

                }else{
                        Txn_colPreauth=Txn_col;
                        Sta_colPreauth=Sta_col;
                        pre_auth.setChecked(false);
                    }
                }
  // Preauthcmp  .................................
                if (Txn_col.equals("Preauthcmp")) {
                    if (Txn_col.equals("Preauthcmp") && Sta_col.equals("1")) {
                        Txn_colPreauthcmp=Txn_col;
                        Sta_colPreauthcmp=Sta_col;
                        pre_authcmp.setChecked(true);
                    }else {
                        Txn_colPreauthcmp = Txn_col;
                        Sta_colPreauthcmp = Sta_col;
                        pre_authcmp.setChecked(false);
                    }
                }
  // Purchase.................................................
                if (Txn_col.equals("Purchase")) {
                    if (Txn_col.equals("Purchase") && Sta_col.equals("1")) {
                        Txn_colPurchase=Txn_col;
                        Sta_colPurchase=Sta_col;
                   purchasecashback.setChecked(true);

                }else{
                        Txn_colPurchase=Txn_col;
                        Sta_colPurchase=Sta_col;
                        purchasecashback.setChecked(false);
                    }
                }
   //  Magstrip..............................................
                if (Txn_col.equals("Balance")) {
                    if (Txn_col.equals("Balance") && Sta_col.equals("1")) {
                        Txn_colBalance=Txn_col;
                        Sta_colBalance=Sta_col;
                        Balance.setChecked(true);
                }else{
                        Txn_colBalance=Txn_col;
                        Sta_colBalance=Sta_col;
                        Balance.setChecked(false);
                    }
                }
  // Refund................................................
                if (Txn_col.equals("Refund")) {
                    if (Txn_col.equals("Refund") && Sta_col.equals("1")) {
                    Txn_colRefund=Txn_col;
                    Sta_colRefund=Sta_col;
                    refund.setChecked(true);
                }else {
                        Txn_colRefund=Txn_col;
                        Sta_colRefund=Sta_col;
                        refund.setChecked(false);
                    }
                }
   //Settlment..............................................
                if (Txn_col.equals("PhoneAuth") ) {
                    if (Txn_col.equals("PhoneAuth") && Sta_col.equals("1")) {
                    Txn_colPhoneAuth=Txn_col;
                    Sta_colPhoneAuth=Sta_col;
                       PhoneAuth.setChecked(true);
                }else {
                        Txn_colPhoneAuth=Txn_col;
                        Sta_colPhoneAuth=Sta_col;
                        PhoneAuth.setChecked(false);
                    }
                }

   // Deposit..............................
                if (Txn_col.equals("Deposit") ) {
                    if (Txn_col.equals("Deposit") && Sta_col.equals("1")) {
                    Txn_colDeposit=Txn_col;
                    Sta_colDeposit=Sta_col;
                    // Log.d(TAG, " Statuse ManualCard is Clicked inside else if =" + Sta_col);
                    deposit.setChecked(true);
                }else{
                        Txn_colDeposit=Txn_col;
                        Sta_colDeposit=Sta_col;
                        deposit.setChecked(false);
                    }
                }


            }
            else
            {
                Log.d("DBRES", "Finished txn: ");
            }
        }
    }

    private void TransMenuView() {
        //txn_loadData();
        manualcardentry = (SwitchCompat) findViewById(R.id.manualcardentry);
        manualcardentry.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) //Line A
            {
                txn_loadData();
                Log.d(TAG, " Statuse and ManualCard is Clicked   " + Txn_colManualCard+"  and  "+Sta_colManualCard);
                  if (Txn_colManualCard.equals("ManualCard") && Sta_colManualCard.equals("1")) {

                      Log.d(TAG, " Statuse ManualCard is Clicked inside  if =" + Sta_colManualCard);

                      DBTerminal.Terminal_functions1 terminal_fun = dbTerminal.new Terminal_functions1();
                      terminal_fun.Disabletxn(Txn_colManualCard);
                      Log.d(TAG, "ManualCard  Disabled  ");
                      manualcardentry.setChecked(false);
                  }
                   else {

                      Log.d(TAG, " Statuse ManualCard is Clicked inside else  = " + Sta_colManualCard);

                      DBTerminal.Terminal_functions1 terminal_fun = dbTerminal.new Terminal_functions1();
                      terminal_fun.Enabletxn(Txn_colManualCard);
                      Log.d(TAG, " ManualCard Enabled   " );
                      manualcardentry.setChecked(true);


                  }

            }
        });

        pre_auth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txn_loadData();
                if (Txn_colPreauth.equals("Preauth") && Sta_colPreauth.equals("1")) {

                    DBTerminal.Terminal_functions1 terminal_fun = dbTerminal.new Terminal_functions1();
                    terminal_fun.Disabletxn(Txn_colPreauth);
                    Log.d(TAG, " Preauth   Disabled  ");
                    pre_auth.setChecked(false);

                }
                else {

                    DBTerminal.Terminal_functions1 terminal_fun = dbTerminal.new Terminal_functions1();
                    terminal_fun.Enabletxn(Txn_colPreauth);
                    Log.d(TAG, " Preauth Enabled  " );
                   pre_auth.setChecked(true);
                }

            }
        });

        pre_authcmp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txn_loadData();
                if (Txn_colPreauthcmp.equals("Preauthcmp") && Sta_colPreauthcmp.equals("1")) {

                    DBTerminal.Terminal_functions1 terminal_fun = dbTerminal.new Terminal_functions1();
                    terminal_fun.Disabletxn(Txn_colPreauthcmp);
                    Log.d(TAG, "Preauthcmp  Disabled  ");
                    pre_authcmp.setChecked(false);
                }
                else {

                    DBTerminal.Terminal_functions1 terminal_fun = dbTerminal.new Terminal_functions1();
                    terminal_fun.Enabletxn(Txn_colPreauthcmp);
                    Log.d(TAG, " Preauthcmp Enabled   " );
                    pre_authcmp.setChecked(true);
                }

            }
        });
        Balance.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txn_loadData();
                if (Txn_colBalance.equals("Balance") && Sta_colBalance.equals("1")) {

                    DBTerminal.Terminal_functions1 terminal_fun = dbTerminal.new Terminal_functions1();
                    terminal_fun.Disabletxn(Txn_colBalance);
                    Log.d(TAG, " balanceinq Disabled   " );
                    Balance.setChecked(false);
                }
                else {

                    DBTerminal.Terminal_functions1 terminal_fun = dbTerminal.new Terminal_functions1();
                    terminal_fun.Enabletxn(Txn_colBalance);
                    Log.d(TAG, " balanceinq Enabled   " );
                    Balance.setChecked(true);

                }



            }
        });

        refund.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txn_loadData();
                if (Txn_colRefund.equals("Refund") && Sta_colRefund.equals("1")) {



                    DBTerminal.Terminal_functions1 terminal_fun = dbTerminal.new Terminal_functions1();
                    terminal_fun.Disabletxn(Txn_colRefund);
                    refund.setChecked(false);
                }
                else {



                    DBTerminal.Terminal_functions1 terminal_fun = dbTerminal.new Terminal_functions1();
                    terminal_fun.Enabletxn(Txn_colRefund);
                    refund.setChecked(true);

                }



            }
        });
        purchasecashback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txn_loadData();
                if (Txn_colPurchase.equals("Purchase") && Sta_colPurchase.equals("1")) {

                    DBTerminal.Terminal_functions1 terminal_fun = dbTerminal.new Terminal_functions1();
                    terminal_fun.Disabletxn(Txn_colPurchase);
                    purchasecashback.setChecked(false);
                }
                else {

                    DBTerminal.Terminal_functions1 terminal_fun = dbTerminal.new Terminal_functions1();
                    terminal_fun.Enabletxn(Txn_colPurchase);
                    purchasecashback.setChecked(true);

                }


            }
        });

        PhoneAuth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txn_loadData();
                if (Txn_colPhoneAuth.equals("PhoneAuth") && Sta_colPhoneAuth.equals("1")) {

                    DBTerminal.Terminal_functions1 terminal_fun = dbTerminal.new Terminal_functions1();
                    terminal_fun.Disabletxn(Txn_colPhoneAuth);
                    PhoneAuth.setChecked(false);
                }
                else {

                    DBTerminal.Terminal_functions1 terminal_fun = dbTerminal.new Terminal_functions1();
                    terminal_fun.Enabletxn(Txn_colPhoneAuth);
                    PhoneAuth.setChecked(true);

                }



            }
        });

        deposit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txn_loadData();
                if (Txn_colDeposit.equals("Deposit") && Sta_colDeposit.equals("1")) {
                    DBTerminal.Terminal_functions1 terminal_fun = dbTerminal.new Terminal_functions1();
                    terminal_fun.Disabletxn(Txn_colDeposit);
                    Log.d(TAG, " Deposit Disabled   " );
                    deposit.setChecked(false);
                }
                else {

                    DBTerminal.Terminal_functions1 terminal_fun = dbTerminal.new Terminal_functions1();
                    terminal_fun.Enabletxn(Txn_colDeposit);
                    Log.d(TAG, " Deposit Enabled   " );
                    deposit.setChecked(true);

                }

            }
        });
    }

}