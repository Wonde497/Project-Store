package com.verifone.demo.emv;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.verifone.demo.emv.userfunctions.user_register;
import com.verifone.demo.emv.userfunctions.user_view;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

public class test_activity extends AppCompatActivity implements View.OnClickListener {

    // creating variables for our edittext, button and dbhandler

    private EditText user_name, user_password,conf_pwd;
    String user_type;
    private static final String TAG = "TESTACT";

    Button add_user,view_user;

    private DBHandler dbHandler;
    private DBHandler.user_functions user_fun;



    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);

        // initializing all our variables
        initScreen();

    }


    private void initScreen() {

        String selection = null;
        String[] selectionArgs =null;

        Fragment frag = null;
        Log.d("Menact","started menact 2");

        int row_cnt;
        Bundle bundle = new Bundle();
        bundle.putString("user_type", "administrator");

        dbHandler = new DBHandler(test_activity.this);
        DBHandler.user_functions user_fun=dbHandler.new user_functions();

        Log.d("Menact","started menact 3");

        row_cnt=user_fun.row_count(selection,selectionArgs);

        Log.d(TAG,"user cnt: " + row_cnt);

        frag=new user_register();
        frag.setArguments(bundle);
        if (frag != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, frag);
            transaction.commit();
        }

        /*user_name = findViewById(R.id.idaddusername);
        user_password = findViewById(R.id.idadduserpwd);
        conf_pwd = findViewById(R.id.idconfirmuserpwd);

        add_user = findViewById(R.id.idBtnAdduser);
        add_user.setOnClickListener(this);

        view_user = findViewById(R.id.idBtnviewuser);
        view_user.setOnClickListener(this);

        dbHandler = new DBHandler(test_activity.this);

        Log.d("EMVDemo", "inited");*/
    }

    @Override
    public void onClick (View v){
        Log.d("EMVDemo", "clickedddd");
        switch (v.getId()) {
            case R.id.idBtnAdduser:

                String struser_name=user_name.getText().toString();
                String struser_pwd=user_password.getText().toString();
                String strconf_pwd=conf_pwd.getText().toString();

                if (struser_name.length()<4)
                {
                    Toast.makeText(test_activity.this, "User Name cannot be smaller than 4 characters.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (struser_pwd.length()<4)
                {
                    Toast.makeText(test_activity.this, "User Password cannot be smaller than 4 characters.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (struser_name.isEmpty()){

                    Toast.makeText(test_activity.this, "Please enter User Name.", Toast.LENGTH_SHORT).show();
                    return;

                }
                if (struser_pwd.isEmpty()){

                    Toast.makeText(test_activity.this, "Please enter User Password.", Toast.LENGTH_SHORT).show();
                    return;

                }
                if (!(struser_pwd.equals(strconf_pwd))){

                    Toast.makeText(test_activity.this, "Entered passwords should be similar.", Toast.LENGTH_SHORT).show();
                    Log.d("EMVDemo", "click read" + strconf_pwd + "-" + struser_pwd);
                    return;

                }

                user_type="support";

                String TABLE_NAME = "users";

                String ID_COL = "id";

                String NAME_COL = "name";

                String PASSWORD_COL = "password";

                String TYPE_COL = "type";

                String STATUS_COL = "status";

                dbHandler.query="CREATE TABLE " + TABLE_NAME + " ("

                    + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "

                    + NAME_COL + " TEXT,"

                        + PASSWORD_COL + " TEXT,"

                        + TYPE_COL + " TEXT,"

                    + STATUS_COL + " TEXT)";

                dbHandler.TABLE_NAME="users";

                DBHandler.user_functions user_fun=dbHandler.new user_functions();
                user_fun.addNewuser(struser_name, struser_pwd, user_type);

                Toast.makeText(test_activity.this, "User has been Registered.", Toast.LENGTH_SHORT).show();

                break;
            case R.id.idBtnviewuser:
                Log.d("EMVDemo", "click read");
                startActivity(new Intent(test_activity.this, user_view.class));
                break;
        }

    }
}