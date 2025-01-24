package com.verifone.demo.emv;

import static com.verifone.demo.emv.Manage_Transactions.Txn_col;
import static com.verifone.demo.emv.Public_data.ISO8583msg.Sta_col;
import static com.verifone.demo.emv.Public_data.ISO8583msg.txn_loadData;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.support.v4.app.FragmentTransaction;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.GravityCompat;


import android.widget.TextView;
import android.widget.Toast;

import com.verifone.demo.emv.Admin_menu.admin_menu1;
import com.verifone.demo.emv.Help.AboutActivity;
import com.verifone.demo.emv.Help.AdminHelpMain;
import com.verifone.demo.emv.Help.HelpMain;
import com.verifone.demo.emv.Help.SupervisorHelpMain;
import com.verifone.demo.emv.Public_data.ISO8583msg;
import com.verifone.demo.emv.Supervisor_menu.More_report;
import com.verifone.demo.emv.Supervisor_menu.Reprint;
import com.verifone.demo.emv.Supervisor_menu.Supervisor_manage_users;
import com.verifone.demo.emv.Support_menu.Settlment;
import com.verifone.demo.emv.Utilities.ToastUtil;
import com.verifone.demo.emv.basic.choosetxntype;
import com.verifone.demo.emv.basic.choosetxntype1;
import com.verifone.demo.emv.basic.common;
import com.verifone.demo.emv.basic.entershift;
import com.verifone.demo.emv.transaction.InputAmountActivity;
import com.verifone.demo.emv.transaction.InputSequenceNumber;
import com.verifone.demo.emv.transaction.ManualTestActivity;
import com.verifone.demo.emv.transaction.Pre_Auth_Completion;
import com.verifone.demo.emv.transaction.ReversalFristActivity;
import com.verifone.demo.emv.transaction.TransBasic;
import com.verifone.demo.emv.transaction.canvas_printer.PrinterItem;
import com.verifone.demo.emv.userfunctions.user_register;
import com.verifone.demo.emv.userfunctions.user_view;
import com.verifone.demo.emv.userfunctions.view_users;

import  cn.verifone.simon.verifone_x9_demo_emv.Global.R;


public class Supervisor_menu_activity extends AppCompatActivity {
    static String TAG = "SUPERVISOR_MENU";
    boolean homePressed = false;
    Fragment reprintfrag = null,transactionactivity;



    private DrawerLayout suppervisordrawerLayout;
    private NavigationView suppervisornavigationView;
    private ActionBarDrawerToggle suppervisoractionBarDrawerToggle;
    Toolbar suppervisortoolbar;
    private AlertDialog suppervisorcustomDialog;



    Fragment supervisor_frag = null;
    FrameLayout supervisor_frame;
    FrameLayout frame;
    String utype,Logon_name;

    private TextView toolbar,logoon;
    ImageButton keydown;
    ImageButton manualcardentry;
    ImageButton reprint;
    ImageButton detailreport;
    ImageButton summryreport;
    ImageButton manageusers;
    ImageButton resettimeout;
    ImageButton endoffday;
    ImageButton setkeys;
    ImageButton supervisormainhelp;

    private static String manualtxn="",manualstat="";
    public static String manual_txn_type1="";
    SharedPreferences preferences=null;
    SharedPreferences.Editor editor;

    private static DBHandler dbHandler;
    DBHandler.user_functions user_fun;
    private static ISO8583msg sp,sp1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        utype = "supervisor";
        Logon_name = getIntent().getExtras().getString("uname");
TransBasic.currentactivity = this;

        Log.d(TAG, "Menu: " + utype);

        setContentView(R.layout.activity_supervisor_menu);
        frame=(FrameLayout)findViewById(R.id.frame);
        manualcardentry = (ImageButton) findViewById(R.id.manualcardentry);
        keydown = (ImageButton) findViewById(R.id.keydownload);
        manageusers = (ImageButton) findViewById(R.id.manageusers);
        reprint = (ImageButton) findViewById(R.id.reprint);
        detailreport = (ImageButton) findViewById(R.id.detailreport);
        endoffday = (ImageButton) findViewById(R.id.endoffday);
        summryreport=(ImageButton) findViewById(R.id.sumreport);
        resettimeout=(ImageButton) findViewById(R.id.resettimeout);
        setkeys=(ImageButton) findViewById(R.id.setkeys);
        supervisormainhelp =(ImageButton) findViewById(R.id.supervisormainhelp);
        toolbar=(TextView) findViewById(R.id.toolbar);


        logoon=(TextView) findViewById(R.id.logoon);

        toolbar.setText("Supervisor Menu");



        suppervisordrawerLayout = findViewById(R.id.supervisor_drawer_layout);
        suppervisornavigationView = findViewById(R.id.suppervisornavigation_view);
        suppervisortoolbar = findViewById(R.id.suppervisortoolbar);
        suppervisortoolbar.setNavigationIcon(R.drawable.toolbar_menu_icon2_32px);
        setSupportActionBar(suppervisortoolbar);

        View headerView = suppervisornavigationView.getHeaderView(0);

        TextView usernameTextView = headerView.findViewById(R.id.username);
        TextView userroleTextView = headerView.findViewById(R.id.userrole);
        usernameTextView.setText("Username: "+Logon_name);
        userroleTextView.setText("Role:  Suppervisor");

        // Set up the ActionBarDrawerToggle
        suppervisoractionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                suppervisordrawerLayout,
                suppervisortoolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );



        // Set the DrawerLayout listener
        suppervisordrawerLayout.addDrawerListener(suppervisoractionBarDrawerToggle);

        // Enable the hamburger icon for the ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        suppervisornavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation item clicks here
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        break;
                    case R.id.nav_about:
                        Intent about_intent = new Intent(Supervisor_menu_activity.this, AboutActivity.class);
                        startActivity(about_intent);
                        break;
                    case R.id.nav_share:
                        break;
                    case R.id.nav_help:
                        Intent help_intent = new Intent(Supervisor_menu_activity.this, SupervisorHelpMain.class);
                        startActivity(help_intent);
                        break;
                    case R.id.nav_settings:
                        VFIApplication.maskHomeKey(false);
                        startActivity(new Intent(Supervisor_menu_activity.this, Supervisor_manage_users.class));
                        break;
                    case R.id.nav_logout:
                        showCustomDialog();
                        break;
                }

                // Close the drawer after handling the click
                suppervisordrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });





        logoon.setText("Logged In User:  "+Logon_name );
        Log.d(TAG, "Amex current user: " + Logon_name);


        //KALEB....................................
        preferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString("username",Logon_name);
        editor.putString("usertype",utype);
        editor.commit();

        //ISO8583msg.txn_loadData();

        sp1 = new ISO8583msg(Supervisor_menu_activity.this);
        sp1.txn_loadData();

        manualtxn = preferences.getString("MTxn_col", "");
        manualstat = preferences.getString("MSta_col", "");
        Log.d(TAG, "Manualcard Entry :  " + manualtxn+"   States  "+manualstat);
        initMenuView();

       /* Button logout=(Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("usertype","");
                editor.commit();
            }
        });*/
        //.............................
    }
    private void initMenuView() {
        final Bundle bundle = new Bundle();
        bundle.putString("user_type", utype);
        keydown.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        TransBasic.getInstance(preferences).downloadkey();

            }
        });

        manualcardentry.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
               // txn_loadData();

       if(manualtxn.equals("ManualCard") && manualstat.equals("1"))
        {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("Txn_Menu_Type","");
            editor.putString("Txn_Menu_Type","Manualcard");
            editor.putString("printtype", " ");
            editor.putString("txn_type", "");
            editor.putString("isphoneauth", "");
            editor.apply();

            TransBasic.tvr="";
            MenuActivity.ClearAllFiledData();

            sp = new ISO8583msg(Supervisor_menu_activity.this);
            sp.loadTmodeData();

            Log.d(TAG, "Txn_Menu_Type ....." +preferences.getString("Txn_Menu_Type", ""));
            Log.d("Menu Activity ","ManualCard Tmode Type............          "+ISO8583msg.mode);
            if(ISO8583msg.mode.equals("Merchant"))
            {
                // VFIApplication.maskHomeKey(false);
                //startActivity(new Intent(MenuActivity.this, InputAmountActivity.class));

                Log.d("EMVDemo", "MERCHANT MODE IS SELECTED.................");
                //Set a choose payment type dialog
                final choosetxntype dialog = new choosetxntype(Supervisor_menu_activity.this);
                dialog.setOnChooselistener(new choosetxntype.OnChooseListener() {
                    @Override
                    public void onClickpurchase()
                    {
                        SharedPreferences preferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("txn_type", "PURCHASE");
                        editor.putString("manualcard_txn_type", "ManualCard_PURCHASE");
                        editor.apply();
                        manual_txn_type1 = "PURCHASE";
                        Log.d("Supervisor", "PURCHASE");
                        VFIApplication.maskHomeKey(false);
                        startActivity(new Intent(Supervisor_menu_activity.this, InputAmountActivity.class));
                        dialog.dismiss();
                    }

                    @Override
                    public void onClickrefund() {
                        SharedPreferences preferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("txn_type", "REFUND");
                        editor.apply();
                        manual_txn_type1 = "REFUND";
                        Log.d("Supervisor", "REFUND");
                        VFIApplication.maskHomeKey(false);
                        startActivity(new Intent(Supervisor_menu_activity.this, Pre_Auth_Completion.class));
                        dialog.dismiss();
                    }

                    @Override
                    public void onClickvoid() {
                        SharedPreferences preferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("txn_type", "REVERSAL");
                        editor.apply();
                        manual_txn_type1 = "REVERSAL";
                        Log.d("Supervisor", "REVERSAL");
                        VFIApplication.maskHomeKey(false);
                        startActivity(new Intent(Supervisor_menu_activity.this, ReversalFristActivity.class));
                        dialog.dismiss();
                    }

                    @Override
                    public void onClickbalance() {
                        SharedPreferences preferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("txn_type", "BALANCE_INQUIRY");
                        editor.putString("manualcard_txn_type", "ManualCard_BALANCE_INQUIRY");
                        editor.apply();
                        manual_txn_type1 = "BALANCE_INQUIRY";
                       //manual_txn_type1=preferences.getString("txn_type", "");

                        Log.d("Supervisor", "BALANCE_INQUIRY");
                        VFIApplication.maskHomeKey(false);
                        startActivity(new Intent(Supervisor_menu_activity.this, ManualTestActivity.class));
                        dialog.dismiss();
                    }

                    @Override
                    public void onClickpreauth() {
                        SharedPreferences preferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("txn_type", "PRE_AUTH");
                        editor.apply();
                        manual_txn_type1 = "PRE-AUTH";
                        Log.d("Supervisor", "PRE_AUTH");
                        VFIApplication.maskHomeKey(false);
                        startActivity(new Intent(Supervisor_menu_activity.this, InputAmountActivity.class));
                        dialog.dismiss();
                    }
                    @Override
                    public void onClickpreauthcomp() {
                        SharedPreferences preferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("txn_type", "PRE_AUTH_COMPLETION");
                        editor.apply();
                        manual_txn_type1 = "PRE_AUTH_COMPLETION";
                        Log.d("Supervisor", "PRE_AUTH_COMPLETION");
                        VFIApplication.maskHomeKey(false);
                        startActivity(new Intent(Supervisor_menu_activity.this, InputSequenceNumber.class));
                        dialog.dismiss();

                    }
                    @Override
                    public void onClickperchasecash() {
                        SharedPreferences preferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("txn_type", "PURCHASE_WITH_CASHBACK");
                        editor.apply();
                        manual_txn_type1 = "PURCHASE_WITH_CASHBACK";
                        Log.d("Supervisor", "PURCHASE_WITH_CASHBACK");
                        VFIApplication.maskHomeKey(false);
                        //startActivity(new Intent(MenuActivity.this, InputAmountActivity.class));
                        dialog.dismiss();
                    }

                    @Override
                    public void onClickphoneAuth() {
                        SharedPreferences preferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("txn_type", "PHONE_AUTH");
                        editor.apply();
                        manual_txn_type1 = "PHONEAUTH";
                        Log.d("Supervisor", "PHONE_AUTH");
                        VFIApplication.maskHomeKey(false);
                        // startActivity(new Intent(MenuActivity.this, ManualTestActivity.class));
                        dialog.dismiss();


                    }

                });

                dialog.show();
            }else//Branch Mode is SELECTED
            {
                Toast.makeText(Supervisor_menu_activity.this,"BRANCH MODE IS NOT ALLOWED MANUALCARDENTRY!", Toast.LENGTH_LONG).show();
                Log.d(TAG, "BRANCH MODE IS NOT ALLOWED MANUALCARDENTRY!: ");

                /*
                Log.d("EMVDemo", "BRANCH MODE IS SELECTED.................");
                final choosetxntype1 dialog = new choosetxntype1(Supervisor_menu_activity.this);
                dialog.setOnChooselistener(new choosetxntype1.OnChooseListener() {
                    @Override
                    public void onClickcashadvance() {
                        SharedPreferences preferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("txn_type", "CASH_ADVANCE");
                        editor.apply();
                        manual_txn_type1 = "CASH_ADVANCE";
                        Log.d("Supervisor", "txn_type1  " + MenuActivity.txn_type1);
                        Log.d("Supervisor", "CASH_ADVANCE");
                        VFIApplication.maskHomeKey(false);
                        startActivity(new Intent(Supervisor_menu_activity.this, InputAmountActivity.class));
                        dialog.dismiss();

                    }

                    @Override
                    public void onClickvoid() {
                        SharedPreferences preferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("txn_type", "REVERSAL");
                        editor.apply();
                        manual_txn_type1 = "REVERSAL";
                        Log.d("Supervisor", "REVERSAL");
                        VFIApplication.maskHomeKey(false);
                        startActivity(new Intent(Supervisor_menu_activity.this, ReversalFristActivity.class));
                        dialog.dismiss();
                    }

                    @Override
                    public void onClickbalance() {
                        SharedPreferences preferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("txn_type", "BALANCE_INQUIRY");
                        editor.apply();
                        manual_txn_type1 = "BALANCE_INQUIRY";
                        Log.d("Supervisor", "BALANCE_INQUIRY");
                        VFIApplication.maskHomeKey(false);
                        startActivity(new Intent(Supervisor_menu_activity.this, ManualTestActivity.class));
                        dialog.dismiss();
                    }

                    @Override
                    public void onClickphoneAuth() {
                        SharedPreferences preferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("txn_type", "PHONE_AUTH");
                        editor.apply();
                        manual_txn_type1 = "PHONEAUTH";
                        Log.d("Supervisor", "PHONE_AUTH");
                        VFIApplication.maskHomeKey(false);
                        // startActivity(new Intent(MenuActivity.this, ManualTestActivity.class));
                        dialog.dismiss();


                    }

                    @Override
                    public void onClickdeposit() {
                        SharedPreferences preferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("txn_type", "DEPOSIT");
                        editor.apply();
                        manual_txn_type1 = "DEPOSIT";
                        Log.d("Supervisor", "DEPOSIT");
                        VFIApplication.maskHomeKey(false);
                        //startActivity(new Intent(MenuActivity.this, ManualTestActivity.class));
                        dialog.dismiss();

                    }
                });

                dialog.show();
                */
            }
         }else
                {
            Toast.makeText(Supervisor_menu_activity.this,"MANUALCARDENTRY TRANSACTION IS DISABLED.", Toast.LENGTH_LONG).show();
            Log.d(TAG, "MANUALCARDENTRY TRANSACTION IS DISABLED: ");

        }
            }
        });


        manageusers.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VFIApplication.maskHomeKey(false);
                startActivity(new Intent(Supervisor_menu_activity.this, Supervisor_manage_users.class));

            }
        });
        setkeys.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VFIApplication.maskHomeKey(false);
                startActivity(new Intent(Supervisor_menu_activity.this, SET_AID_RID_KEY.class));

            }
        });

        reprint.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                frame.setVisibility(View.INVISIBLE);
                supervisor_frag = new Reprint();
                supervisor_frag.setArguments(bundle);
                if (supervisor_frag != null) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.supervisor_frame, supervisor_frag).commit();
                }

            }
        });
        detailreport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                // editor.putString("print_status", "supervisor");
                DBHandler dbHandler = new DBHandler(getApplicationContext());
                TransBasic transBasic;
                transBasic = TransBasic.getInstance(sharedPreferences);

                if (dbHandler.gettransactiondata().size() > 0)
                {
                    for (int i = 0; i < dbHandler.gettransactiondata().size(); i++)
                    {
                        int check = i+1;
                        if (dbHandler.gettransactiondata().get(dbHandler.gettransactiondata().size() - check).getLastflag().equals("0"))
                        {

                            if(dbHandler.gettransactiondata().size()==1)
                            {
                                editor.putString("printtype", "detailreport_2");
                                editor.putString("trn", String.valueOf(check));
                                editor.commit();
                                transBasic.printTest(1);
                            }
                            else if (i == 0 && check==1)
                            {
                                Log.d(TAG, "printtype!, detailreport...........");
                                editor.putString("printtype", "detailreport");
                                editor.putString("trn", String.valueOf(check));
                                editor.commit();
                                transBasic.printTest(1);


                                // return;

                            }else if (i > 0 && i < dbHandler.gettransactiondata().size()-1)
                            {
                                try {
                                    Thread.sleep(1000);
                                    Log.d("VIEW", "detailreport_1");
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                    // Log.d("VIEW", "view users Amex here after loadDataTask error");
                                }

                                Log.d(TAG, "printtype!, detailreport_1...........");
                                editor.putString("printtype", "detailreport_1");
                                editor.putString("trn", String.valueOf(check));
                                editor.commit();
                                transBasic.printTest(1);


                            }
                            else {
                                try {
                                    Thread.sleep(1000);
                                    Log.d("VIEW", "detailreport_2");
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                    //Log.d("VIEW", "view users Amex here after loadDataTask error");
                                }

                                Log.d(TAG, "printtype!, detailreport_2...........");
                                editor.putString("printtype", "detailreport_2");
                                editor.putString("trn", String.valueOf(check));
                                editor.commit();
                                transBasic.printTest(1);
                            }

                        }else
                        {
                            Log.d(TAG, "Flag is 1 or txn is balance");

                            Toast.makeText(Supervisor_menu_activity.this, "Flag is 1 or txn is balance", Toast.LENGTH_LONG).show();

                        }
                    }

                } else {
                    Log.d(TAG, "DO TRANSACTION!, NO TXN RECORDED");

                    Toast.makeText(Supervisor_menu_activity.this,"DO TRANSACTION!, NO TXN RECORDED", Toast.LENGTH_LONG).show();

                }
            }
        });
        summryreport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //  frame.setVisibility(View.INVISIBLE);

                SharedPreferences sharedPreferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.commit();
                common c= new common(Supervisor_menu_activity.this);
                c.summaryreport();
            }
        });

        /*endoffday.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //dbHandler = new DBHandler(Supervisor_menu_activity.this);

                //clear all data form the data base
                common c= new common(Supervisor_menu_activity.this);
                c.endofday();
                Log.d(TAG, "clear all data form the data base......");


            }
        });*/
        resettimeout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                dbHandler = new DBHandler(Supervisor_menu_activity.this);
                DBHandler.user_functions user_fun = dbHandler.new user_functions();

                user_fun.delete_Time_outdb();
                //clear all data form the data base
                Log.d(TAG, "clear all data form the data base......");
                Toast.makeText(Supervisor_menu_activity.this,"TRANSACTION RESETED!", Toast.LENGTH_LONG).show();

            }
        });


        //............................Added by Amlakie for Supervisor Help.............................................
        supervisormainhelp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Supervisor_menu_activity.this, SupervisorHelpMain.class));
            }
        });

    }
    //@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.manualcardentry:
                //VFIApplication.maskHomeKey(false);
                supervisor_frag=new user_register();
                //supervisor_frag.setArguments(bundle);
                if (supervisor_frag != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.supervisor_frame, supervisor_frag);
                    transaction.commit();
                }
                break;

        }

    }
    @Override
    public void onBackPressed() {
        Log.d(TAG, "Back ");

        if (supervisor_frag != null) {
            Log.d(TAG, "Back 2");
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(supervisor_frag);
            transaction.commit();
            supervisor_frag=null;
            frame.setVisibility(View.VISIBLE);
        }
        else {
            Log.d(TAG, "Back 3");
           // super.onBackPressed();
            showCustomDialog();
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
    public void opentrandata(){
        frame.setVisibility(View.INVISIBLE);
        supervisor_frag = new transactionactivity();
        if (supervisor_frag != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.supervisor_frame, supervisor_frag).commit();
        }
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog, null);
        builder.setView(dialogView);

        // Initialize the TextView and Buttons
        TextView messageTextView = dialogView.findViewById(R.id.messageTextView);
        Button positiveButton = dialogView.findViewById(R.id.positiveButton);
        Button negativeButton = dialogView.findViewById(R.id.negativeButton);

        // Set the message
        // messageTextView.setText("Are you sure you want to logout?");

        // Set click listeners for the buttons
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform logout action here
                Intent intent = new Intent(Supervisor_menu_activity.this, MenuActivity.class);
                startActivity(intent);
                finish(); // Finish the current activity after logout
                suppervisorcustomDialog.dismiss(); // Dismiss the dialog after performing the action
            }
        });

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog if user clicks No
                suppervisorcustomDialog.dismiss();
            }
        });

        // Create and show the dialog
        suppervisorcustomDialog = builder.create();
        suppervisorcustomDialog.show();
    }

}