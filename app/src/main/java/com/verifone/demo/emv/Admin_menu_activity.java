package com.verifone.demo.emv;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.support.v4.app.FragmentTransaction;

import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.verifone.demo.emv.Admin_menu.admin_menu1;
import com.verifone.demo.emv.Help.AdminHelpMain;
import com.verifone.demo.emv.userfunctions.user_Disable;
import com.verifone.demo.emv.userfunctions.user_Enable;
import com.verifone.demo.emv.userfunctions.user_register;
import com.verifone.demo.emv.userfunctions.view_users;
import com.verifone.demo.emv.userfunctions.user_deletes;
import com.verifone.demo.emv.userfunctions.Change_user_pin;
import cn.verifone.simon.verifone_x9_demo_emv.Global.R;
public class Admin_menu_activity extends AppCompatActivity {

    static String TAG = "MENU";
    boolean homePressed = false;
    String utype;
    String menu_title;
    Fragment admin_frag = null;
    FrameLayout Admin_Frame,admin_fragment;
    FrameLayout Frame;
    private TextView toolbar,logoon;

    String user_type,logon_name;

    ImageButton regsupp;
    ImageButton viewsupp;
    ImageButton changeadminpin;
    ImageButton changesupppin;
    ImageButton dissupp;
    ImageButton ensupp;
    ImageButton delsupp;
    ImageButton adminmhelp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

          utype = "administrator";
        logon_name = getIntent().getExtras().getString("uname1");
        Log.d(TAG, "Logon : " + logon_name);
         Log.d(TAG, "Menu: " + utype);

        setContentView(R.layout.activity_admin_menu);
        Admin_Frame = (FrameLayout) findViewById(R.id.admin_frame);
        admin_fragment = (FrameLayout) findViewById(R.id.admin_fragment);
        Frame = (FrameLayout) findViewById(R.id.frame);
        toolbar=(TextView) findViewById(R.id.toolbar);
        logoon=(TextView) findViewById(R.id.logoon);


        toolbar.setText("Manage Support Menu");
        logoon.setText("Logged In User:  "+logon_name );
        Log.d(TAG, "Amex current user:  " + logon_name);
       initMenuView();
    }
    private void initMenuView() {
       final Bundle bundle = new Bundle();
        bundle.putString("user_type", utype);

        regsupp = (ImageButton) findViewById(R.id.regsupp);
        regsupp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Admin_Frame.setVisibility(View.VISIBLE);
                Frame.setVisibility(View.INVISIBLE);
                admin_frag=new user_register();
                admin_frag.setArguments(bundle);
                if (admin_frag != null) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.admin_frame, admin_frag).commit();
                }

            }
        });

        viewsupp = (ImageButton) findViewById(R.id.viewsupp);
       // viewsupp.setOnClickListener(this);
        viewsupp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Admin_Frame.setVisibility(View.VISIBLE);
                Frame.setVisibility(View.INVISIBLE);
                SharedPreferences sharedPreferences =getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("viewtype","viewsupport");
                editor.commit();
                admin_frag=new view_users();
                admin_frag.setArguments(bundle);
                if (admin_frag != null) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.admin_frame, admin_frag).commit();

                }

            }
        });
        changeadminpin = (ImageButton) findViewById(R.id.changeadminpin);
        //changeadminpin.setOnClickListener(this);
        changeadminpin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Frame.setVisibility(View.INVISIBLE);
                menu_title = "Admin";
                admin_frag=new Change_user_pin();
                bundle.putString("utype",menu_title);

                admin_frag.setArguments(bundle);
                if (admin_frag != null) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.admin_frame, admin_frag).commit();

                }

            }
        });
        changesupppin = (ImageButton) findViewById(R.id.changesupppin);
        //changesupppin.setOnClickListener(this);
        changesupppin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Frame.setVisibility(View.INVISIBLE);
                menu_title = "Support";
                bundle.putString("utype",menu_title);
                admin_frag=new Change_user_pin();
                admin_frag.setArguments(bundle);
                if (admin_frag != null) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.admin_frame, admin_frag).commit();

                }

            }
        });
        dissupp = (ImageButton) findViewById(R.id.dissupp);
        //dissupp.setOnClickListener(this);
        dissupp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Frame.setVisibility(View.INVISIBLE);
                SharedPreferences sharedPreferences =getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("distype","dissupport");
                editor.commit();
                admin_frag=new user_Disable();
                admin_frag.setArguments(bundle);
                if (admin_frag != null) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.admin_frame, admin_frag).commit();

                }

            }
        });
        ensupp = (ImageButton) findViewById(R.id.ensupp);
        //ensupp.setOnClickListener(this);
        ensupp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Frame.setVisibility(View.INVISIBLE);
                SharedPreferences sharedPreferences =getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("entype","ensupport");
                editor.commit();
                admin_frag=new user_Enable();
                admin_frag.setArguments(bundle);
                if (admin_frag != null) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.admin_frame, admin_frag).commit();

                }

            }
        });
        delsupp = (ImageButton) findViewById(R.id.delsupp);
        //delsupp.setOnClickListener(this);
        delsupp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Frame.setVisibility(View.INVISIBLE);
                SharedPreferences sharedPreferences =getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("deletetype","deletesupport");
                editor.commit();
                admin_frag=new user_deletes();
                admin_frag.setArguments(bundle);
                if (admin_frag != null) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.admin_frame, admin_frag).commit();

                }

            }
        });

//.........................Added by Bini for Admin Help..............................................
        adminmhelp = findViewById(R.id.adminmanagesupporthelp);
        adminmhelp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Admin_menu_activity.this, AdminHelpMain.class));
            }
        });
    }
    //@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.regsupp:
                //VFIApplication.maskHomeKey(false);
                admin_frag=new user_register();
                //frag.setArguments(bundle);
                if (admin_frag != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.admin_frame, admin_frag);
                    transaction.commit();
                }
                break;
            case R.id.viewsupp:
                admin_frag=new view_users();
                //frag.setArguments(bundle);
                if (admin_frag != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.admin_frame, admin_frag);
                    transaction.commit();
                }
                break;
            case R.id.changeadminpin:
                VFIApplication.maskHomeKey(false);
                startActivity(new Intent(Admin_menu_activity.this, view_users.class));
                break;
            case R.id.changesupppin:
                VFIApplication.maskHomeKey(false);
                startActivity(new Intent(Admin_menu_activity.this, view_users.class));
                break;
            case R.id.dissupp:
                VFIApplication.maskHomeKey(false);
                startActivity(new Intent(Admin_menu_activity.this, view_users.class));
                break;
            case R.id.ensupp:
                VFIApplication.maskHomeKey(false);
                startActivity(new Intent(Admin_menu_activity.this, view_users.class));
                break;
            case R.id.delsupp:
                VFIApplication.maskHomeKey(false);
                startActivity(new Intent(Admin_menu_activity.this, view_users.class));
                onResume();
                break;
        }

    }
    @Override
    public void onBackPressed() {
        Log.d(TAG, "Finished  here Back Pressed Method ");
        if (admin_frag != null) {
            Log.d(TAG, "Back Pressed Method if not null");
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(admin_frag);
            transaction.commit();
            admin_frag=null;
           Frame.setVisibility(View.VISIBLE);
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