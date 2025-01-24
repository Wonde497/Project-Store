package com.verifone.demo.emv;

//import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import com.verifone.demo.emv.terminalfunctions.Base_Screen3;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Base_Screen2 extends AppCompatActivity {

    static String TAG="REG_MERID";
    View Ttitel;
    private EditText merchant_id;
    private TextView mtitel;
    FrameLayout frame;
    Button register;
    private DBHandler dbHandler;
    private DBHandler.terminal_functions terminal_fun;
    int row_cntmid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_base_screen2);
        Log.d(TAG, "MERCHANT REGISTER PAGE");
        chk_MID();
        if (row_cntmid==0) {
            initScreen3();
            Log.d(TAG, "Register  Merchant onCreate  method initScreen3 Done");
        }
        else if (row_cntmid>0) {
            Log.d(TAG, "MID aleardy Registered go Next Activity ");
            startActivity(new Intent(Base_Screen2.this, Base_Screen3.class));
            finish();
        }

    }
    public void chk_MID()
    {
        String selection = null;
        String[] selectionArgs =null;
        Bundle bundle = new Bundle();
        bundle.putString("merchantlid", "merchantlid");

        dbHandler = new DBHandler(Base_Screen2.this);
        DBHandler.terminal_functions terminal_fun=dbHandler.new terminal_functions();

        row_cntmid=terminal_fun.row_countMid(selection,selectionArgs);

        Log.d(TAG,"Check MID method user cnt merchant columon: " + row_cntmid);

    }
    private void initScreen3() {

        merchant_id = (EditText) findViewById(R.id.Tvregmerchantid);
        mtitel = (TextView) findViewById(R.id.toolbar);
        register = (Button) findViewById(R.id.idBtnregmerchantlid);
        merchant_id.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)}) ;



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "Register button clicked initScreen3 On Merchantid");

                String strmerid=merchant_id.getText().toString();

                if ( strmerid.length()<15)
                {
                    Toast.makeText(Base_Screen2.this,"Merchant_id must be 15 characters.", Toast.LENGTH_LONG).show();
                    return;
                }
                else if (strmerid.isEmpty()){


                    Toast.makeText(Base_Screen2.this,"Please enter Merchant_id is not empity.", Toast.LENGTH_SHORT).show();
                    return;

                }

                String TABLE_NAME_MID = "MID";
                String ID_COL = "id";
                String MERID_COL = "merchant_id";
                dbHandler.query1="CREATE TABLE " +TABLE_NAME_MID + " ("
                        +ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + MERID_COL + " TEXT)";

                dbHandler.TABLE_NAME_MID="MID";

                DBHandler.terminal_functions terminal_fun=dbHandler.new terminal_functions();
                terminal_fun.regmerchant_id(strmerid);

                Toast.makeText(Base_Screen2.this,"Terminal has been Regiser succefully.", Toast.LENGTH_SHORT).show();


                onBackPressed();
            }

        });

        //dbHandler = new DBHandler(Base_Screen1.this);

        merchant_id.setOnKeyListener(new View.OnKeyListener()
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

        Log.d(TAG, "out off initScreen2");
    }
    @Override
    public void onBackPressed() {

        //Log.d(TAG, "onBackPressed Method  Back");
        //if(row_cntmid==2) {
           // chk_MID();
        //}
        //else if (row_cntmid>2) {
            Log.d(TAG, "onBackPressed method Admin aleardy Registered go base_screen1");
            startActivity(new Intent(Base_Screen2.this, Base_Screen3.class));
            finish();
       // }
        //else
           // finish();
    }
}
