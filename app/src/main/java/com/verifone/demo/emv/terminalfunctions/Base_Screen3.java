package com.verifone.demo.emv.terminalfunctions;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.verifone.demo.emv.DBHandler;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class Base_Screen3 extends AppCompatActivity {
    static String TAG="REG_MERNAME";
    View Ttitel;
    private EditText merchant_name;
    private TextView toolbar;
    FrameLayout frame;
    Button register;
    private DBHandler dbHandler;
    private DBHandler.terminal_functions terminal_fun;
    int row_cnt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_screen3);

        Log.d(TAG, "MERCHANT NAME REGISTER PAGE");
        chk_MNMAE();
        if (row_cnt==0) {
            initScreen4();
            Log.d(TAG, "Register  Merchant Name onCreate  method initScreen4 Done");
        }
        else if (row_cnt>0) {
            Log.d(TAG, "MNAME aleardy Registered go Next Activity ");
            startActivity(new Intent(Base_Screen3.this, Base_Screen4.class));
            finish();
        }
    }
    public void chk_MNMAE()
    {
        String selection = null;
        String[] selectionArgs =null;
        Bundle bundle = new Bundle();
        bundle.putString("merchantlname", "merchantlname");

        dbHandler = new DBHandler(Base_Screen3.this);
        DBHandler.terminal_functions terminal_fun=dbHandler.new terminal_functions();

        row_cnt=terminal_fun.row_countMname(selection,selectionArgs);

        Log.d(TAG,"Check MID method user cnt merchant NAME col: " + row_cnt);

    }

    private void initScreen4() {

        merchant_name = (EditText) findViewById(R.id.Tvregmerchantname);
        toolbar = (TextView) findViewById(R.id.toolbar);
        register = (Button) findViewById(R.id.idBtnregmerchantname);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "Register button clicked initScreen4 On MerchantName");

                String strmername=merchant_name.getText().toString();

                if (strmername.length()<2)
                {
                    Toast.makeText(Base_Screen3.this,"Merchant_name Must more than 2 characters.", Toast.LENGTH_LONG).show();
                    return;
                }
                else if (strmername.isEmpty()){


                    Toast.makeText(Base_Screen3.this,"Please enter Merchant_name is not null.", Toast.LENGTH_SHORT).show();
                    return;

                }

                String TABLE_NAME_MNAME = "MNAME";
                String ID_COL = "id";
                String MERNAME_COL = "merchant_name";
                dbHandler.query1="CREATE TABLE " +TABLE_NAME_MNAME + " ("
                        +ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + MERNAME_COL + " TEXT )";

                dbHandler.TABLE_NAME_MNAME="MNAME";

                DBHandler.terminal_functions terminal_fun=dbHandler.new terminal_functions();
                terminal_fun.regmerchant_name(strmername);

                Toast.makeText(Base_Screen3.this,"Merchantname has been Regiser succefully.", Toast.LENGTH_SHORT).show();


                onBackPressed();
            }

        });

        //dbHandler = new DBHandler(Base_Screen1.this);

        merchant_name.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            register.performClick();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        Log.d(TAG, "out off initScreen3");
    }

    @Override
    public void onBackPressed() {

       // Log.d(TAG, " Merchant name onBackPressed Method  Back");
        //if(row_cnt==4) {
           // chk_MNMAE();
        //}
        //else if (row_cnt>4) {
            Log.d(TAG, "onBackPressed method Admin aleardy Registered go base_screen1");
            startActivity(new Intent(Base_Screen3.this, Base_Screen4.class));
            finish();
       // }
        //else
           // finish();
    }
}