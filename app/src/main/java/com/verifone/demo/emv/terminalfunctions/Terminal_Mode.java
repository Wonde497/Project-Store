package com.verifone.demo.emv.terminalfunctions;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.verifone.demo.emv.DBHandler;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class Terminal_Mode extends AppCompatActivity {
    static String TAG="REG_Mode";
    ImageButton branchmode;
    ImageButton merchantmode;

    private DBHandler dbHandler;
    private DBHandler.terminal_functions terminal_fun;

    int row_cnt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminal_mode);

        Log.d(TAG, "Mode start ");

        chk_Mode();
        if (row_cnt==0) {
            ModeMenuView();
        }
        else if (row_cnt>0) {
            Log.d(TAG, "Mode aleardy Registered go Next Activity ");
            startActivity(new Intent(Terminal_Mode.this, Currency.class));
            finish();
        }
    }

    public void chk_Mode()
    {
        String selection = null;
        String[] selectionArgs =null;
        Bundle bundle = new Bundle();
        bundle.putString("mode", "mode");
        Log.d(TAG, "Mode check ");
        dbHandler = new DBHandler(Terminal_Mode.this);
        DBHandler.terminal_functions terminal_fun=dbHandler.new terminal_functions();
        Log.d(TAG, "Mode check here  "+ row_cnt);
        row_cnt=terminal_fun.row_countmode(selection,selectionArgs);

        Log.d(TAG,"Check MOde method user cnt : " + row_cnt);

    }
    private void ModeMenuView() {
        branchmode = (ImageButton) findViewById(R.id.branch);
        branchmode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //VFIApplication.maskHomeKey(false);
                Log.d(TAG,"inside creat function : ");

                String Bmode="Branch";
                String TABLE_NAME_TMODE = "TMODE";
                String ID_COL = "id";
                String TMODE_COL = "mode";
                dbHandler.query1="CREATE TABLE " + TABLE_NAME_TMODE + " ("
                        +ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + TMODE_COL + "TEXT)";

                dbHandler.TABLE_NAME_TMODE="TMODE";

                DBHandler.terminal_functions terminal_fun=dbHandler.new terminal_functions();
                terminal_fun.regTerminalmode(Bmode);

                Toast.makeText(Terminal_Mode.this,"Branch Mode has been Regiser succefully.", Toast.LENGTH_SHORT).show();

                onBackPressed();


            }

        });

        merchantmode = (ImageButton) findViewById(R.id.merchant);
        merchantmode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               // VFIApplication.maskHomeKey(false);

                String Mmode="Merchant";
                String TABLE_NAME_TMODE = "TMODE";
                String ID_COL = "id";
                String TMODE_COL = "mode";
                dbHandler.query1=" CREATE TABLE " +TABLE_NAME_TMODE + " ("
                        +ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + TMODE_COL + " TEXT )";

                dbHandler.TABLE_NAME_TMODE="TMODE";
                DBHandler.terminal_functions terminal_fun=dbHandler.new terminal_functions();
                terminal_fun.regTerminalmode(Mmode);

                Toast.makeText(Terminal_Mode.this,"Merchant Mode has been Regiser succefully.", Toast.LENGTH_SHORT).show();

                onBackPressed();


            }
        });
    }
    @Override
    public void onBackPressed() {

        // Log.d(TAG, " Merchant name onBackPressed Method  Back");
         //if(row_cnt==0) {
           //  chk_Mode();
       // }
         //else if (row_cnt>0) {
        Log.d(TAG, "onBackPressed method Tmode aleardy Registered go Currency");
        startActivity(new Intent(Terminal_Mode.this, Currency.class));
        finish();
         //}
        //else
          //finish();
    }
}