package com.verifone.demo.emv;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.verifone.demo.emv.userfunctions.user_register;

import  cn.verifone.simon.verifone_x9_demo_emv.Global.R;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Base_Screen extends AppCompatActivity {

    String user_type;
    private static final String TAG = "BASE_SCR";
    //String bin = "Bini";

    private DBHandler dbHandler;
    Fragment frag = null;

    int row_cnt;
TextView t;
Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_base_screen);


        chk_admin();

        if (row_cnt==0)
            initScreen();
        else if (row_cnt>0)
        {
            Log.d(TAG, "Admin aleardy Registered go base_screen1");
           startActivity(new Intent(Base_Screen.this, Base_Screen1.class));
           finish();
        }

    }

    public void chk_admin()
    {
        String selection = null;
        String[] selectionArgs =null;


        Bundle bundle = new Bundle();
        bundle.putString("user_type", "administrator");

        dbHandler = new DBHandler(Base_Screen.this);
        DBHandler.user_functions user_fun=dbHandler.new user_functions();

        row_cnt=user_fun.row_count(selection,selectionArgs);

        Log.d(TAG,"Check admin method user cnt: " + row_cnt);

    }

    private void initScreen() {

        Bundle bundle = new Bundle();
        bundle.putString("user_type", "init");
        Log.d(TAG,"Here initScreen method ");
        frag = new user_register();
        frag.setArguments(bundle);
        if (frag != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, frag);
            transaction.commit();
        }
    }

    @Override
    public void onBackPressed() {

        Log.d(TAG, "onBackPressed Method  Back");
        if (frag != null) {

            row_cnt=0;
            chk_admin();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(frag);
            transaction.commit();
            frag=null;

            if (row_cnt>0) {
                Log.d(TAG, "onBackPressed method Admin aleardy Registered go base_screen1");
                startActivity(new Intent(Base_Screen.this, Base_Screen1.class));
                finish();
            }
            else
                finish();
        }
    }
}