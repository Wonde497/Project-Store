package com.verifone.demo.emv.terminalfunctions;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.verifone.demo.emv.DBHandler;
import com.verifone.demo.emv.MenuActivity;
import com.verifone.demo.emv.transaction.TransBasic;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class Comm_Type extends AppCompatActivity {
 static String TAG="REG_Commtype";
  ImageButton Lte1;
  ImageButton Ethernet1;
    ImageButton Wifi1;
  String Lte;
  String Ethernet;
  String Wifi;
    private DBHandler dbHandler;
    private DBHandler.terminal_functions terminal_fun;
    int row_cnt;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm_type);
        sharedPreferences=getSharedPreferences("Shared_data", Context.MODE_PRIVATE);

        Wifi1 = (ImageButton) findViewById(R.id.Wifi);
        Lte1 = (ImageButton) findViewById(R.id.Lte);



        chk_commtype();
        if (row_cnt==0)
        {
            CommMenuView();
        }
        else if (row_cnt>0) {
            Log.d(TAG, "Comm_Type aleardy Registered go Next Activity ");
            startActivity(new Intent(Comm_Type.this, MenuActivity.class));
            finish();
        }
    }
    public void chk_commtype()
    {
        String selection = null;
        String[] selectionArgs =null;
        Bundle bundle = new Bundle();
        bundle.putString("commtype", "commtype");
        dbHandler = new DBHandler(Comm_Type.this);
        DBHandler.terminal_functions terminal_fun=dbHandler.new terminal_functions();
        row_cnt=terminal_fun.row_countcommtype(selection,selectionArgs);
        Log.d(TAG,"Check commtype method user cnt : " + row_cnt);

    }
    public void CommMenuView()
    {

        Lte1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

               Lte="4G";
              String TABLE_NAME_COMMTYPE = "COMTYPE";
              String ID_COL = "id";
              String COMTYPE_COL = "commtype";
              dbHandler.query1="CREATE TABLE " + TABLE_NAME_COMMTYPE + " ("
                      +ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                      + COMTYPE_COL + " TEXT  )";

              dbHandler.TABLE_NAME_COMMTYPE="COMTYPE";

              DBHandler.terminal_functions terminal_fun=dbHandler.new terminal_functions();
              terminal_fun.regCommtype(Lte);

              Toast.makeText(Comm_Type.this,"Lte/4G has been Regiser succefully.", Toast.LENGTH_SHORT).show();

                TransBasic transBasic = TransBasic.getInstance(sharedPreferences);
                SharedPreferences sharedPreferences =getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("printtype","terminalinfo");
                editor.commit();
                Log.d(TAG, "print terminal information");
                transBasic.printTest(0);
              onBackPressed();


          }
      });


        Wifi1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Wifi="Wifi";
                String TABLE_NAME_COMMTYPE = "COMTYPE";
                String ID_COL = "id";
                String COMTYPE_COL = "commtype";
                dbHandler.query1="CREATE TABLE " + TABLE_NAME_COMMTYPE + " ("
                        +ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + COMTYPE_COL + " TEXT)";

                dbHandler.TABLE_NAME_COMMTYPE="COMTYPE";

                DBHandler.terminal_functions terminal_fun=dbHandler.new terminal_functions();
                terminal_fun.regCommtype(Wifi);

                Toast.makeText(Comm_Type.this,"Wifi has been Regiser succefully.", Toast.LENGTH_SHORT).show();

                TransBasic transBasic = TransBasic.getInstance(sharedPreferences);
                SharedPreferences sharedPreferences =getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("printtype","terminalinfo");
                editor.commit();
                Log.d(TAG, "print terminal information");
                transBasic.printTest(0);

                onBackPressed();

            }
        });

    }
    @Override
    public void onBackPressed() {

        startActivity(new Intent(Comm_Type.this, MenuActivity.class));
        finish();
}
}