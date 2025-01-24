package com.verifone.demo.emv.Support_menu;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;


import android.support.v7.widget.Toolbar;

import com.verifone.demo.emv.Help.SupportHelpMain;
import com.verifone.demo.emv.userfunctions.Change_user_pin;
import com.verifone.demo.emv.userfunctions.user_Disable;
import com.verifone.demo.emv.userfunctions.user_Enable;
import com.verifone.demo.emv.userfunctions.user_deletes;
import com.verifone.demo.emv.userfunctions.user_register;
import com.verifone.demo.emv.userfunctions.view_users;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class Support_manage_user extends AppCompatActivity {

    static String TAG = "MENU";
    boolean homePressed = false;
    String utype;
    String menu_title;
    Fragment support_manageuser_frag = null;
    //int cashierstatus=1;

    FrameLayout support_manageusers_frame;
    FrameLayout frame;
    ImageButton viewsupervisor;
    ImageButton viewcashier;
    ImageButton deletesupervisor;
    ImageButton disablesupervisor;
    ImageButton enablesupervisor;
    ImageButton changesupervisorpin;
    ImageButton supporthelp;
    //private DBHandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        utype = "support";

        Log.d(TAG, "Menu: " + utype);
        setContentView(R.layout.activity_support_manage_user);

        support_manageusers_frame=(FrameLayout) findViewById(R.id.support_manageusers_frame);
        frame=(FrameLayout) findViewById(R.id.frame);

        viewsupervisor = (ImageButton) findViewById(R.id.viewsupervisor);
        viewcashier = (ImageButton) findViewById(R.id.viewcashier);
        deletesupervisor=(ImageButton) findViewById(R.id.deletesupervisor);
        disablesupervisor = (ImageButton) findViewById(R.id.dissupervisor);
        enablesupervisor = (ImageButton) findViewById(R.id.ensupervisor);
        changesupervisorpin=(ImageButton) findViewById(R.id.changesupervisorpin);
        supporthelp=(ImageButton) findViewById(R.id.supportmanageuserthelp);






        initMenuView();
    }
    private void initMenuView() {
        final Bundle bundle = new Bundle();
        bundle.putString("user_type", utype);

        viewsupervisor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //support_settings_frame.setVisibility(View.VISIBLE);
                frame.setVisibility(View.INVISIBLE);

                SharedPreferences sharedPreferences =getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("viewtype","viewsupervisor");
                editor.commit();
                support_manageuser_frag = new view_users();
                support_manageuser_frag.setArguments(bundle);
                if (support_manageuser_frag != null) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.support_manageusers_frame, support_manageuser_frag).commit();
                }

            }
        });

        viewcashier.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //support_settings_frame.setVisibility(View.VISIBLE);
                frame.setVisibility(View.INVISIBLE);
                SharedPreferences sharedPreferences =getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("viewtype","viewcashier");
                editor.commit();
                support_manageuser_frag = new view_users();
                support_manageuser_frag.setArguments(bundle);
                if (support_manageuser_frag != null) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.support_manageusers_frame, support_manageuser_frag).commit();
                }


            }
        });
        deletesupervisor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //support_settings_frame.setVisibility(View.VISIBLE);
                frame.setVisibility(View.INVISIBLE);
                SharedPreferences sharedPreferences =getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("deletetype","deletesupervisor");
                editor.commit();
                support_manageuser_frag = new user_deletes();
                support_manageuser_frag.setArguments(bundle);
                if (support_manageuser_frag != null) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.support_manageusers_frame, support_manageuser_frag).commit();
                }


            }
        });
        disablesupervisor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //support_settings_frame.setVisibility(View.VISIBLE);
                frame.setVisibility(View.INVISIBLE);
                SharedPreferences sharedPreferences =getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("distype","dissupervisor");
                editor.commit();
                support_manageuser_frag = new user_Disable();
                support_manageuser_frag.setArguments(bundle);
                if (support_manageuser_frag != null) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.support_manageusers_frame, support_manageuser_frag).commit();
                }


            }
        });
        enablesupervisor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //support_settings_frame.setVisibility(View.VISIBLE);
                frame.setVisibility(View.INVISIBLE);
                SharedPreferences sharedPreferences =getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("entype","ensupervisor");
                editor.commit();
                support_manageuser_frag = new user_Enable();
                support_manageuser_frag.setArguments(bundle);
                if (support_manageuser_frag != null) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.support_manageusers_frame, support_manageuser_frag).commit();
                }


            }
        });
        changesupervisorpin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //support_settings_frame.setVisibility(View.VISIBLE);
                frame.setVisibility(View.INVISIBLE);
                support_manageuser_frag = new Change_user_pin();
                support_manageuser_frag.setArguments(bundle);
                if (support_manageuser_frag != null) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.support_manageusers_frame, support_manageuser_frag).commit();
                }

            }
        });
//........................Added by Amlakie for Support Help..............................................
       // supporthelp = findViewById(R.id.supportmanageuserthelp);
        supporthelp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Support_manage_user.this, SupportHelpMain.class));
            }
        });
    }

    //@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.regsupp:
                //VFIApplication.maskHomeKey(false);
                support_manageuser_frag=new user_register();
                //frag.setArguments(bundle);
                if (support_manageuser_frag != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.support_manageusers_frame, support_manageuser_frag);
                    transaction.commit();
                }
                break;
            case R.id.viewsupp:
                support_manageuser_frag=new view_users();
                //frag.setArguments(bundle);
                if (support_manageuser_frag != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.support_manageusers_frame, support_manageuser_frag);
                    transaction.commit();
                }
                break;

        }

    }
    @Override
    public void onBackPressed() {
        Log.d(TAG, "Finished  here Back Pressed Method ");
        if (support_manageuser_frag != null) {
            Log.d(TAG, "Back Pressed Method if not null");
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(support_manageuser_frag);
            transaction.commit();
            support_manageuser_frag=null;
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

   /* @Override
    public void onPause() {
        super.onPause();
        if(homePressed)
        {
            Log.i(TAG, "Pause");
            startActivity(new Intent(Admin_menu_activity.this, MenuActivity.class));
           finish();
        }
    }*/

}