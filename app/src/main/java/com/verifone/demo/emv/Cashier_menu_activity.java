package com.verifone.demo.emv;

import static com.verifone.demo.emv.Transactiondata.context;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.GravityCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;
import android.widget.Toast;

import com.verifone.demo.emv.Help.AboutActivity;
import com.verifone.demo.emv.Help.CashierHelpMain;
import com.verifone.demo.emv.Public_data.ISO8583msg;
import com.verifone.demo.emv.Supervisor_menu.Reprint;
import com.verifone.demo.emv.Support_menu.Settlment;
import com.verifone.demo.emv.Utilities.ActivityCollector;
import com.verifone.demo.emv.Utilities.Comm;
import com.verifone.demo.emv.Utilities.ToastUtil;
import com.verifone.demo.emv.Utilities.Utility;
import com.verifone.demo.emv.basic.HostInformation;
import com.verifone.demo.emv.basic.common;
import com.verifone.demo.emv.basic.entershift;
import com.verifone.demo.emv.data_handlers.User;
import com.verifone.demo.emv.terminalfunctions.View_TerminalInfo;
import com.verifone.demo.emv.transaction.TransBasic;
import com.verifone.demo.emv.userfunctions.Change_user_pin;
import com.verifone.demo.emv.userfunctions.user_register;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.prefs.Preferences;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class Cashier_menu_activity extends AppCompatActivity {

    static String TAG = "MENU";
    boolean homePressed = false;
    String utype;
    public  static String Logon_name;

    private TextView toolbar,ntkReset,logoon;


    private DrawerLayout cashierdrawerLayout;
    private NavigationView cashiernavigationView;
    private ActionBarDrawerToggle cashieractionBarDrawerToggle;
    Toolbar cashiertoolbar;
    private AlertDialog cashiercustomDialog;
    Fragment cashier_frag = null;
    FrameLayout cashier_frame;
    FrameLayout frame;
    ImageButton manualdownload,viewinfo;
    ImageButton reprint;
    ImageButton detailreport;
    ImageButton summryreport;
    ImageButton changecashierpin;
    ImageButton Settle;
    ImageButton resettimeout;
    private DBHandler dbHandler;
    private String cashstatuse;
    public static Activity activity;
    SharedPreferences preferences=null;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user_loadData();
        utype = "cashier";
        activity=this;
        TransBasic.currentactivity = this;
        if(cashstatuse.equals("0"))
        {
            Logon_name="" ;
        }else
        {
            Logon_name = getIntent().getExtras().getString("uname");
            Log.d(TAG, "AMEX,casheirLogon_name: " + Logon_name);
        }


        Log.d(TAG, "Menu: " + utype);
        Log.d(TAG, "AMEX,casheirLogon_name: " + Logon_name);

        setContentView(R.layout.activity_cashier_menu);
        cashier_frame=(FrameLayout) findViewById(R.id.cashier_frame);
        frame=(FrameLayout) findViewById(R.id.frame);
        preferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString("username",Logon_name);
        editor.putString("usertype",utype);
        editor.commit();

        manualdownload = (ImageButton) findViewById(R.id.manualdownload);
        viewinfo = (ImageButton) findViewById(R.id.viewinfo);

        reprint = (ImageButton) findViewById(R.id.reprint);
        detailreport = (ImageButton) findViewById(R.id.detailreport);
        summryreport = (ImageButton) findViewById(R.id.sumreport);
        changecashierpin = (ImageButton) findViewById(R.id.changecashierpin);
        Settle = (ImageButton) findViewById(R.id.settle);
        resettimeout = (ImageButton) findViewById(R.id.timeout);
        toolbar=(TextView) findViewById(R.id.toolbar);


        logoon=(TextView) findViewById(R.id.logoon);

        //toolbar.setText("Cashier Menu");
        cashierdrawerLayout = findViewById(R.id.cashier_drawer_layout);
        cashiernavigationView = findViewById(R.id.cashiernavigation_view);
        cashiertoolbar = findViewById(R.id.cashiertoolbar);
        cashiertoolbar.setNavigationIcon(R.drawable.toolbar_menu_icon2_32px);
        setSupportActionBar(cashiertoolbar);

        View headerView = cashiernavigationView.getHeaderView(0);

        TextView usernameTextView = headerView.findViewById(R.id.username);
        TextView userroleTextView = headerView.findViewById(R.id.userrole);
        usernameTextView.setText("Username: "+Logon_name);
        userroleTextView.setText("Role:  Cashier");



        // Set up the ActionBarDrawerToggle
        cashieractionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                cashierdrawerLayout,
                cashiertoolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );

        // Set the DrawerLayout listener
        cashierdrawerLayout.addDrawerListener(cashieractionBarDrawerToggle);

        // Enable the hamburger icon for the ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cashiernavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation item clicks here
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        break;
                    case R.id.nav_about:
                        Intent about_intent = new Intent(Cashier_menu_activity.this, AboutActivity.class);
                        startActivity(about_intent);
                        break;
                    case R.id.nav_settings:
                        break;
                    case R.id.nav_help:
                        Intent help_intent = new Intent(Cashier_menu_activity.this, CashierHelpMain.class);
                        startActivity(help_intent);
                        break;
                    case R.id.nav_logout:
                        showCustomDialog();
                        break;
                }

                // Close the drawer after handling the click
                cashierdrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        if(cashstatuse.equals("1"))
        {        Log.d(TAG, "AMEX,casheirLogon_name: " + Logon_name);

            logoon.setText("Logged In User:  "+Logon_name );

        }else
        {
            logoon.setText("Logged In User:  "+"" );
            Log.d(TAG, "AMEX,casheirLogon_name: NULL" );
        }

            initMenuView();


    }

    public void user_loadData()//Cashier Statuse 0  or 1
    {

        String selection = "";
        String[] selectionArgs = {};
        Log.d("AMEX", "here 1");
        List<User> cashierList = new ArrayList<User>();
        dbHandler = new DBHandler(Cashier_menu_activity.this);
        DBHandler.user_functions user_fun=dbHandler.new user_functions();
        Log.d("AMEX", "here 3");
        cashierList= user_fun.Cashierstatview(selection,selectionArgs);
        int rows = cashierList.size();
        Log.d("AMEX", "Total Size  " + rows);
        // user u_list;

        Log.d("AMEX", "here");
        for(int i = 0; i < rows; i ++) {
            User row = null;
            if (i ==0) {
                row = cashierList.get(i);
                Log.d("AMEX", "Cashierstatus : " + row.getCashierstatus());

                cashstatuse=row.getCashierstatus();
                //Sta_col=row.getStatus();


            }
        }}

    private void initMenuView() {
        utype = "cashier";
        final Bundle bundle = new Bundle();
        bundle.putString("user_type", utype);

        manualdownload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TransBasic.getInstance(preferences).downloadkey();
/*editor.putString("txn_type","download");
        editor.commit();
                Log.d(TAG, "cashier" +"keydownload"+preferences.getString("txn_type", ""));*/

            }
        });

        viewinfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                frame.setVisibility(View.INVISIBLE);
                cashier_frag=new View_TerminalInfo();
                cashier_frag.setArguments(bundle);
                if (cashier_frag != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.cashier_frame, cashier_frag);
                    transaction.commit();
                }
            }
        });
        changecashierpin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                frame.setVisibility(View.INVISIBLE);
                cashier_frag = new Change_user_pin();
                cashier_frag.setArguments(bundle);
                if (cashier_frag != null) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.cashier_frame, cashier_frag).commit();
                }


            }
        });
        reprint.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                editor.putString("cashier","cashierprint");
                editor.commit();

                frame.setVisibility(View.INVISIBLE);
                cashier_frag = new Reprint();
                cashier_frag.setArguments(bundle);
                if (cashier_frag != null) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.cashier_frame, cashier_frag).commit();
                }

            }
        });
        detailreport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                DBHandler dbHandler = new DBHandler(getApplicationContext());
                TransBasic transBasic;
                transBasic = TransBasic.getInstance(sharedPreferences);

                int dbsize= dbHandler.gettransactiondata().size();
                if (dbsize > 0)
                {
                    for (int i = 0; i < dbsize; i++)
                    {
                        if (dbHandler.gettransactiondata().get(i).getLastflag().equals("0"))
                        {
                            if(dbsize==1)
                            {
                                editor.putString("printtype", "detailreport_2");
                                editor.putString("trn", String.valueOf(i));
                                editor.commit();
                                transBasic.printTest(1);
                            }
                            else if (i == 0)
                            {
                                Log.d(TAG, "printtype!, detailreport...........");
                                editor.putString("printtype", "detailreport");
                                editor.putString("trn", String.valueOf(i));
                                editor.commit();
                                transBasic.printTest(1);
                                // return;
                            } else if (i < dbsize-1)
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
                                editor.putString("trn", String.valueOf(i));
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
                                editor.putString("trn", String.valueOf(i));
                                editor.commit();
                                transBasic.printTest(1);
                            }
                        }else {
                            Log.d(TAG, "Flag is 1 or txn is balance");
                            //Toast.makeText(Cashier_menu_activity.this, "Flag is 1 or txn is balance", Toast.LENGTH_LONG).show();
                            ToastUtil.toastOnUiThread(Cashier_menu_activity.this, "Flag is 1 or Txn is Balance.");
                        }
                    }
                } else {
                    Log.d(TAG, "DO TRANSACTION!, NO TXN RECORDED");
                    ToastUtil.toastOnUiThread((Activity) context, "DO TRANSACTION!, NO TXN RECORDED.");
                    //Toast.makeText(Cashier_menu_activity.this, "DO TRANSACTION!, NO TXN RECORDED", Toast.LENGTH_LONG).show();
                }
                //summrey.....................
                //editor.commit();
                //common c= new common(Cashier_menu_activity.this);
                // c.summaryreport();
            }

        });
        summryreport.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                SharedPreferences sharedPreferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                DBHandler dbHandler = new DBHandler(getApplicationContext());
                editor.commit();
                common c= new common(Cashier_menu_activity.this);
                c.summaryreport();

            }
          });

         Settle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                showCustomDialog2();
               //transBasic.printTest(1);
               }
          });

        resettimeout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //dbHandler = new DBHandler(Cashier_menu_activity.this);
                DBHandler dbHandler = new DBHandler(getApplicationContext());
                DBHandler.user_functions user_fun = dbHandler.new user_functions();

                user_fun.delete_Time_outdb();
                //clear all data form the data base
                Log.d(TAG, "clear all data form the data base......");
                Toast.makeText(Cashier_menu_activity.this,"CURRENT TRANSACTION RESETED...!", Toast.LENGTH_LONG).show();


            }
        });



    }
    //@Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.manualcardentry:
                //VFIApplication.maskHomeKey(false);
                cashier_frag=new user_register();
                //supervisor_frag.setArguments(bundle);
                if (cashier_frag != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.cashier_frame, cashier_frag);
                    transaction.commit();
                }
                break;

        }

    }
    @Override
    public void onBackPressed() {
        Log.d(TAG, "Back ");

        if (cashier_frag != null) {
            Log.d(TAG, "Back 2");
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(cashier_frag);
            transaction.commit();
            cashier_frag=null;
            frame.setVisibility(View.VISIBLE);

        }
        else  if (cashierdrawerLayout.isDrawerOpen(GravityCompat.START)) {
            // If the navigation drawer is open, close it
            cashierdrawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            Log.d(TAG, "Back 3");
            //super.onBackPressed();
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
        cashier_frag = new transactionactivity();
        if (cashier_frag != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.cashier_frame, cashier_frag).commit();

        }
    }
    public void makevisible()
    {
        frame.setVisibility(View.VISIBLE);

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
                Intent intent = new Intent(Cashier_menu_activity.this, MenuActivity.class);
                startActivity(intent);
                finish(); // Finish the current activity after logout
                cashiercustomDialog.dismiss(); // Dismiss the dialog after performing the action
            }
        });

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog if user clicks No
                cashiercustomDialog.dismiss();
            }
        });

        // Create and show the dialog
        cashiercustomDialog = builder.create();
        cashiercustomDialog.show();
    }

    private void showCustomDialog2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog, null);
        builder.setView(dialogView);

        // Initialize the TextView and Buttons
        TextView messageTextView = dialogView.findViewById(R.id.messageTextView);
        messageTextView.setText("Are you sure you want to settle all transactions?");
        Button yesButton = dialogView.findViewById(R.id.positiveButton);
        Button noButton = dialogView.findViewById(R.id.negativeButton);

        // Set the message
        // messageTextView.setText("Are you sure you want to logout?");

        // Set click listeners for the buttons
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                DBHandler dbHandler = new DBHandler(getApplicationContext());
                TransBasic transBasic;
                transBasic = TransBasic.getInstance(sharedPreferences);

                editor.putString("Txn_Menu_Type","Settlement");
                editor.putString("txn_type","SETTLEMENT_GBE");
                editor.putString("printtype","settlement");
                editor.commit();
                // Perform logout action here
                common common = new common(Cashier_menu_activity.this);
                common.settlementGBE();
                //transBasic.printTest(1);
                cashiercustomDialog.dismiss(); // Dismiss the dialog after performing the action
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog if user clicks No
                cashiercustomDialog.dismiss();
            }
        });

        // Create and show the dialog
        cashiercustomDialog = builder.create();
        cashiercustomDialog.show();
    }

}


