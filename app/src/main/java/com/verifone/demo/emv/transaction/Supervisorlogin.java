package com.verifone.demo.emv.transaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.verifone.demo.emv.DBHandler;
import com.verifone.demo.emv.Transactiondata;
import com.verifone.demo.emv.Utilities.ActivityCollector;
import com.verifone.demo.emv.data_handlers.User;

import java.util.ArrayList;
import java.util.List;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;
public class Supervisorlogin extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "Supervisorlogin";
    EditText super_name,super_password;
    Button Enter,Cancle;
    String superuser_pwd,superuser_name,superuser_type,superuser_status,txntype="",Txn_Menu_Type="";
    private DBHandler dbHandler;
    SharedPreferences sharedPreferences;
    Transactiondata transactiondata;
    private TransBasic transBasic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisorlogin);
        sharedPreferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);

        txntype = sharedPreferences.getString("txn_type", "");
        Txn_Menu_Type = sharedPreferences.getString("Txn_Menu_Type", "");

        Log.d(TAG, "Txn_type is "+txntype);
        Log.d(TAG, "Txn_Menu_Type is "+Txn_Menu_Type);
        //txntype = getIntent().getExtras().getString("txn_type");
        dbHandler = new DBHandler(getApplicationContext());

        sharedPreferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
        transBasic = TransBasic.getInstance(sharedPreferences);

        transactiondata=new Transactiondata(sharedPreferences,getApplicationContext());
        ActivityCollector.addActivity(this);


        Log.d(TAG, "WELCOME Supervisorlogin ..............Menu: ");
        super_name=(EditText) findViewById(R.id.super_name);
        super_password=(EditText) findViewById(R.id.super_password);
        Enter = (Button) findViewById(R.id.Enter);
        Enter.setOnClickListener(this);
        super_password.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                        Enter.performClick();
                        return true;
                        default:
                        break;
                    }
                }
                return false;
            }
        });
        //Cancle.setOnClickListener(this);
       // check_ini();

    }
    @Override
    public void onClick (View v) {

        switch (v.getId())
        {
            case R.id.Enter:
                superuser_name=super_name.getText().toString();
                superuser_pwd=super_password.getText().toString();
                superuser_status="Enabled";

                chk_login();
                Log.d(TAG, "login utype: "+  superuser_type);
                break;
    }
    }

    private void chk_login() {
        String selection = "name = ? and password = ?";
        String[] selectionArgs = {superuser_name, superuser_pwd};
        //String[] selectionArgs = {"chal","support","Enabled","1234"};

        List<User> userList = new ArrayList<User>();
        DBHandler.user_functions user_fun = dbHandler.new user_functions();

        userList = user_fun.viewUsers(selection, selectionArgs);

        Log.d("EMVDemo", "Inside login");

        for (User obj : userList) {
            Log.d("EMVDemo", "Users: " + obj.getName() + ":" + obj.getPassword() + ":" + obj.getStatus());
            //Logon=obj.getName();
            superuser_status=obj.getStatus();
            superuser_type=obj.getTpe();

            Log.d("Amex", "UserName : "+obj.getName());
            Log.d("Amex", "Usertype : "+obj.getTpe());
            Log.d("Amex", "Userstatus: "+obj.getStatus());

        }
        if (userList.isEmpty())
        {
            Toast.makeText(Supervisorlogin.this, "Wrong Credentials! Please Check User name or Password", Toast.LENGTH_SHORT).show();
        }
        else if(superuser_type.equals("supervisor") && superuser_status.equals("Enabled"))
        {
            Toast.makeText(Supervisorlogin.this, "You are Supervisor :" + superuser_name, Toast.LENGTH_SHORT).show();

            Log.d("Amex..", "Good, Supervisor is logged in............... : ");
            Log.d("Amex..", "Txn_Menu_Type.. : "+Txn_Menu_Type);
           if(txntype.equals("REVERSAL") && Txn_Menu_Type.equals("Magstrip"))
           {
           Log.d("Amex..sup", "Good, Magstrip Reversal is selected............... : ");
            //transBasic.initializeMAG();
               startActivity(new Intent(Supervisorlogin.this, InputPinActivity.class));
               finish();
           }
          else if(txntype.equals("REFUND"))
           {
                Log.d("Amex..", "Good, REFUND is Selected............... : ");
                Log.d("REFUND ", "REFUND TXN IS SELECTED");
                startActivity(new Intent(Supervisorlogin.this, InputAmountActivity.class));
                finish();
              }
           else
             {
             Log.d("Amex..", "Good, Normal txn Reversal is selected............... : ");
            TransBasic transBasic=TransBasic.getInstance(sharedPreferences);
            transBasic.importCardConfirmResult();
            transBasic.pinInputHandler = pinInputHandler;
            //finish();
            //logged_in();
        }
}else
     {
    Log.d("Amex..", "Himmmmmmmmmmmm..User isn't Supervisor................ : ");
    Toast.makeText(Supervisorlogin.this, "UserType  "+ superuser_name+"  is not Supervisor " , Toast.LENGTH_SHORT).show();

    }
    }
    private void logged_in()
    {
        chk_login();
        Log.d("Amex", "UserName : "+superuser_name);
        Log.d("Amex", "Usertype : "+superuser_type);
        Log.d("Amex", "Userstatus: "+superuser_status);
        if (superuser_type.equals("supervisor"))
        {
            Intent new_menu = new Intent(Supervisorlogin.this, InputAmountActivity.class);
            new_menu.putExtra("txn_type", txntype);
            startActivity(new_menu);
        }

        else
        {
            Toast.makeText(Supervisorlogin.this, "UserType  "+ superuser_name+"  is not Supervisor " , Toast.LENGTH_SHORT).show();
        }
    }
    Handler pinInputHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            startActivity(new Intent(Supervisorlogin.this, InputPinActivity.class));
            finish();
        }
    };
}