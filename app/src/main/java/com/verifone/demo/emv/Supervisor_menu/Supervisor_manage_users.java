package com.verifone.demo.emv.Supervisor_menu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;


import android.support.v7.widget.Toolbar;


import com.verifone.demo.emv.Admin_menu.admin_menu1;
import com.verifone.demo.emv.Help.AdminHelpMain;
import com.verifone.demo.emv.Help.SupervisorHelpMain;
import com.verifone.demo.emv.userfunctions.Change_user_pin;
import com.verifone.demo.emv.userfunctions.user_Disable;
import com.verifone.demo.emv.userfunctions.user_Enable;
import com.verifone.demo.emv.userfunctions.user_deletes;
import com.verifone.demo.emv.userfunctions.user_register;
import com.verifone.demo.emv.userfunctions.view_users;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class Supervisor_manage_users extends AppCompatActivity {

    static String TAG = "MENU";
    boolean homePressed = false;
    String utype;
    String menu_title;
    Fragment supervisor_manageuser_frag = null;
    //int cashierstatus=1;

    FrameLayout supervisor_manageusers_frame;
    FrameLayout frame;
    ImageButton regcashier;
    ImageButton viewcashier;
    ImageButton deletecashier;
    ImageButton disablecashier;
    ImageButton enablecashier;
    ImageButton changesupervisorpin;
    ImageButton supervisorhelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utype = "supervisor";

        Log.d(TAG, "Menu: " + utype);


        setContentView(R.layout.activity_supervisor_manage_users);

        supervisor_manageusers_frame=(FrameLayout) findViewById(R.id.supervisor_manageusers_frame);
        frame=(FrameLayout) findViewById(R.id.frame);

        regcashier = (ImageButton) findViewById(R.id.regcashier);
        viewcashier = (ImageButton) findViewById(R.id.viewcashier);
        deletecashier=(ImageButton) findViewById(R.id.deletecashier);
        disablecashier = (ImageButton) findViewById(R.id.discashier);
        enablecashier = (ImageButton) findViewById(R.id.encashier);
        changesupervisorpin=(ImageButton) findViewById(R.id.cangesupervisorpin);

        initMenuView();

    }
    private void initMenuView() {
        final Bundle bundle = new Bundle();
        bundle.putString("user_type", utype);

        regcashier.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                frame.setVisibility(View.INVISIBLE);
                supervisor_manageuser_frag = new user_register();
                supervisor_manageuser_frag.setArguments(bundle);
                if (supervisor_manageuser_frag != null) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.supervisor_manageusers_frame, supervisor_manageuser_frag).commit();
                }

            }
        });

        viewcashier.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                frame.setVisibility(View.INVISIBLE);
                SharedPreferences sharedPreferences =getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("viewtype","viewcashier");
                editor.commit();
                supervisor_manageuser_frag = new view_users();
                supervisor_manageuser_frag.setArguments(bundle);
                if (supervisor_manageuser_frag != null) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.supervisor_manageusers_frame, supervisor_manageuser_frag).commit();
                }

            }
        });

        disablecashier.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                frame.setVisibility(View.INVISIBLE);
                SharedPreferences sharedPreferences =getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("distype","discashier");
                editor.commit();
                supervisor_manageuser_frag = new user_Disable();
                supervisor_manageuser_frag.setArguments(bundle);
                if (supervisor_manageuser_frag != null) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.supervisor_manageusers_frame, supervisor_manageuser_frag).commit();
                }

            }
        });
        enablecashier.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                frame.setVisibility(View.INVISIBLE);
                SharedPreferences sharedPreferences =getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("entype","encashier");
                editor.commit();
                supervisor_manageuser_frag = new user_Enable();
                supervisor_manageuser_frag.setArguments(bundle);
                if (supervisor_manageuser_frag != null) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.supervisor_manageusers_frame, supervisor_manageuser_frag).commit();
                }

            }
        });
        deletecashier.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                frame.setVisibility(View.INVISIBLE);
                SharedPreferences sharedPreferences =getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("deletetype","deletecashier");
                editor.commit();
                supervisor_manageuser_frag = new user_deletes();
                supervisor_manageuser_frag.setArguments(bundle);
                if (supervisor_manageuser_frag != null) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.supervisor_manageusers_frame, supervisor_manageuser_frag).commit();
                }

            }
        });
        changesupervisorpin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                frame.setVisibility(View.INVISIBLE);
                supervisor_manageuser_frag = new Change_user_pin();
                supervisor_manageuser_frag.setArguments(bundle);
                if (supervisor_manageuser_frag != null) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.supervisor_manageusers_frame, supervisor_manageuser_frag).commit();
                }

            }
        });

//........................Added by Amlakie for Supervisor Help..............................................
        supervisorhelp = findViewById(R.id.supervisormanagecashierhelp);
        supervisorhelp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Supervisor_manage_users.this, SupervisorHelpMain.class));
            }
        });
    }
    //@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.manualcardentry:
                //VFIApplication.maskHomeKey(false);
                supervisor_manageuser_frag=new user_register();
                //supervisor_frag.setArguments(bundle);
                if (supervisor_manageuser_frag != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.supervisor_manageusers_frame, supervisor_manageuser_frag);
                    transaction.commit();
                }
                break;

        }

    }
    @Override
    public void onBackPressed() {
        Log.d(TAG, "Back ");

        if (supervisor_manageuser_frag != null) {
            Log.d(TAG, "Back 2");
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(supervisor_manageuser_frag);
            transaction.commit();
            supervisor_manageuser_frag=null;
            frame.setVisibility(View.VISIBLE);
        }
        else {
            Log.d(TAG, "Back 3");
            super.onBackPressed();
            homePressed = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        homePressed = true;
        Log.d(TAG, "Resume");
    }

    @Override
    public void onPause() {
        super.onPause();
        if(homePressed)
        {
            Log.i(TAG, "Pause");
            //startActivity(new Intent(user_menu_activity.this, Admin_menu_activity.class));
            // finish();
        }
    }
}