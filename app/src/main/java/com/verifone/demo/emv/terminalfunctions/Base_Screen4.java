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

public class Base_Screen4 extends AppCompatActivity {
    static String TAG="REG_MERADD";
    private EditText merchant_address;
    private TextView toolbar;
    FrameLayout frame;
    Button register;
    private DBHandler dbHandler;
    private DBHandler.terminal_functions terminal_fun;
    int row_cnt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_screen4);
        Log.d(TAG, "MERCHANT ADD REGISTER PAGE");
        chk_MADD();
        if (row_cnt==0) {
            initScreen5();
            Log.d(TAG, "Register  Merchant ADD onCreate  method initScreen4 Done");
        }
        else if (row_cnt>0) {
            Log.d(TAG, "MNAME aleardy Registered go Next Activity ");
            startActivity(new Intent(Base_Screen4.this, Terminal_Mode.class));
            finish();
        }
    }
    public void chk_MADD()
    {
        String selection = null;
        String[] selectionArgs =null;
        Bundle bundle = new Bundle();
        bundle.putString("ADD", "ADD");

        dbHandler = new DBHandler(Base_Screen4.this);
        DBHandler.terminal_functions terminal_fun=dbHandler.new terminal_functions();

        row_cnt=terminal_fun.row_countMaddrs(selection,selectionArgs);

        Log.d(TAG,"Check Maddress method user cnt merchant add col: " + row_cnt);

    }

    private void initScreen5() {

        merchant_address = (EditText) findViewById(R.id.Tvregmerchantaddress);
        toolbar = (TextView) findViewById(R.id.toolbar);
        register = (Button) findViewById(R.id.idBtnregmerchantaddress);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "Register button clicked initScreen4 On MerchantName");

                String strmeraddress=merchant_address.getText().toString();

                if (strmeraddress.length()<2)
                {
                    Toast.makeText(Base_Screen4.this,"Merchant_address Morethan 2 characters.", Toast.LENGTH_LONG).show();
                    return;
                }
                else if (strmeraddress.isEmpty())
                {

                    Toast.makeText(Base_Screen4.this,"Please enter Merchant_add Is not NULL.", Toast.LENGTH_SHORT).show();
                    return;

                }

                String TABLE_NAME_MADDRESS = "MADDRESS";
                String ID_COL = "id";
                String MERADDRESS_COL = "merchant_address";
                dbHandler.query1="CREATE TABLE " + TABLE_NAME_MADDRESS + " ("
                        +ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + MERADDRESS_COL + " TEXT)";

                dbHandler.TABLE_NAME_MADDRESS="MADDRESS";

                DBHandler.terminal_functions terminal_fun=dbHandler.new terminal_functions();
                terminal_fun.regmerchant_address(strmeraddress);

                Toast.makeText(Base_Screen4.this,"Merchant Address has been Regiser succefully.", Toast.LENGTH_SHORT).show();


                onBackPressed();
            }

        });

        //dbHandler = new DBHandler(Base_Screen1.this);

        merchant_address.setOnKeyListener(new View.OnKeyListener()
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

        Log.d(TAG, "out off initScreen4");
    }

    @Override
    public void onBackPressed() {

       // Log.d(TAG, " Merchant name onBackPressed Method  Back");
       // if(row_cnt==6) {
           // chk_MADD();
        //}
       // else if (row_cnt>6) {
            Log.d(TAG, "onBackPressed method MAddress aleardy Registered go Tmode");
            startActivity(new Intent(Base_Screen4.this, Terminal_Mode.class));
            finish();
       // }
        //else
          //  finish();
    }

}