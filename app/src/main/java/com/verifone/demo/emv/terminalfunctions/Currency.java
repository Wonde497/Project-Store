package com.verifone.demo.emv.terminalfunctions;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.verifone.demo.emv.DBHandler;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class Currency extends AppCompatActivity {
    FrameLayout currency_frame;
    static String TAG="REG_CURRENCY";
    private DBHandler dbHandler;
    private DBHandler.terminal_functions terminal_fun;
    ImageButton ETB;
    ImageButton USD;
    ImageButton EUR;
    public static String ETB1;
    public static String USD1;
    public static String EUR1;
    int row_cnt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);

        currency_frame = (FrameLayout) findViewById(R.id.currency_frame);
        ETB=(ImageButton) findViewById(R.id.etb);
        USD=(ImageButton) findViewById(R.id.usd);
        EUR=(ImageButton) findViewById(R.id.eur);
        Log.d(TAG, "Start Currency Registered");

        chk_Currency();
        if (row_cnt==0) {
            CurrMenuView();
            Log.d(TAG, "Register currency onCreate method CurrMenuView() Done");

        }
        else if (row_cnt>0) {
            Log.d(TAG, "Currency aleardy Registered go Next Activity ");
            startActivity(new Intent(Currency.this, Comm_Type.class));
            finish();
        }
        //CurrMenuView();

    }
    public void chk_Currency()
    {
        String selection = null;
        String[] selectionArgs =null;
        Log.d(TAG,"Start Check Currency method user cnt: " + row_cnt);
        Bundle bundle = new Bundle();
        bundle.putString("currency", "currency");

        dbHandler = new DBHandler(Currency.this);
        DBHandler.terminal_functions terminal_fun=dbHandler.new terminal_functions();

        row_cnt=terminal_fun.row_countcurrency(selection,selectionArgs);

        Log.d(TAG,"Check Currency method user cnt: " + row_cnt);

    }
    private void CurrMenuView() {
        ETB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //VFIApplication.maskHomeKey(false);

                ETB1="230";

                String TABLE_NAME_CURRENCY = "CURRENCYTYPE";
                String ID_COL = "id";
                String CURRENCY_COL = "currency";
                dbHandler.query1="CREATE TABLE " +TABLE_NAME_CURRENCY + " ("
                        +ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + CURRENCY_COL + " TEXT)";

                dbHandler.TABLE_NAME_CURRENCY="CURRENCYTYPE";

                DBHandler.terminal_functions terminal_fun=dbHandler.new terminal_functions();
                terminal_fun.regCurrencytype(ETB1);

                Toast.makeText(Currency.this,"ETB has been Regiser succefully.", Toast.LENGTH_SHORT).show();

                onBackPressed();


            }
        });
        USD.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //VFIApplication.maskHomeKey(false);
                USD1="840";

                String TABLE_NAME_CURRENCY = "CURRENCYTYPE";
                String ID_COL = "id";
                String CURRENCY_COL = "currency";
                dbHandler.query1="CREATE TABLE " + TABLE_NAME_CURRENCY + " ("
                        +ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + CURRENCY_COL + " TEXT)";

                dbHandler.TABLE_NAME_CURRENCY="CURRENCYTYPE";

                DBHandler.terminal_functions terminal_fun=dbHandler.new terminal_functions();
                terminal_fun.regCurrencytype(USD1);

                Toast.makeText(Currency.this,"USD has been Regiser succefully.", Toast.LENGTH_SHORT).show();

                onBackPressed();

                //startActivity(new Intent(Currency.this, Comm_Type.class));
                //finish();


            }
        });
        EUR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //VFIApplication.maskHomeKey(false);
                EUR1="978";

                String TABLE_NAME_CURRENCY = "CURRENCYTYPE";
                String ID_COL = "id";
                String CURRENCY_COL = "currency";
                dbHandler.query1="CREATE TABLE " + TABLE_NAME_CURRENCY + " ("
                        +ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + CURRENCY_COL + " TEXT)";

                dbHandler.TABLE_NAME_CURRENCY="CURRENCYTYPE";

                DBHandler.terminal_functions terminal_fun=dbHandler.new terminal_functions();
                terminal_fun.regCurrencytype(EUR1);

                Toast.makeText(Currency.this,"EUR has been Regiser succefully.", Toast.LENGTH_SHORT).show();

                onBackPressed();

                //startActivity(new Intent(Currency.this, Comm_Type.class));
                //finish();


            }
        });
}
    @Override
    public void onBackPressed() {

        //Log.d(TAG, "onBackPressed Method  Back");
        //if(row_cnt==0) {
        // chk_TID();
        // }
        //else if (row_cnt>0) {
        Log.d(TAG, "onBackPressed method Currency aleardy Registered go Commtype");
        startActivity(new Intent(Currency.this, Comm_Type.class));
        finish();
        //}
        //else
        //finish();
    }
}