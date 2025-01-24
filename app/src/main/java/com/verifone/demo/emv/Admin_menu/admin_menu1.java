package com.verifone.demo.emv.Admin_menu;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.view.GravityCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.verifone.demo.emv.Admin_menu_activity;
import com.verifone.demo.emv.CustomAlertDialog;
import com.verifone.demo.emv.Help.AboutActivity;
import com.verifone.demo.emv.Help.AdminHelpMain;
import com.verifone.demo.emv.Manage_Transactions;
import com.verifone.demo.emv.MenuActivity;
import com.verifone.demo.emv.VFIApplication;
import com.verifone.demo.emv.keytransfer;
import com.verifone.demo.emv.transaction.TransBasic;
import com.verifone.demo.emv.user_login;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;
public class admin_menu1 extends AppCompatActivity {
    static String TAG = "MENU";
    boolean homePressed = false;
    private TextView toolbar, logoon;

    FrameLayout Admin_Frame1;

    ImageButton settingss, recieve;
    ImageButton directorylist;
    ImageButton back;
    ImageButton managetrans;
    ImageButton adminmhelp;
    ImageView toolbar1;

    String value = null;
    String user_type, uname, user_name, user_name1;
    String menu_title;
    SharedPreferences sharedPreferences;

    // Declare variables
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar3;
    private AlertDialog customDialog1;
    // Define a class-level variable to hold the dialog instance
    private Dialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu1);
        user_type = "administrator";

        uname = getIntent().getExtras().getString("uname");
        Log.d(TAG, "AMEX Current user  :" + uname);
        Log.d(TAG, "Menu: " + user_type);

        user_name1=uname;
        //*************************
        final SharedPreferences preferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username",uname);
        editor.putString("usertype",user_type);
        editor.commit();
       /* Button logout=(Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("usertype","");
                editor.commit();
            }
        });*/

        //0000000000000000
        Admin_Frame1 = (FrameLayout) findViewById(R.id.admin_frame);
        toolbar1 = (ImageView) findViewById(R.id.toolbar1);
        toolbar=(TextView) findViewById(R.id.toolbar);
        logoon=(TextView) findViewById(R.id.logoon);

        toolbar.setText("Administrator Menu");
        logoon.setText("Logged In User:  "+user_name1 );
        // Initialize the DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.bsc_drawer_layout);
        navigationView = findViewById(R.id.navigation_viewplus);
        toolbar3 = findViewById(R.id.toolbarplus);
        toolbar3.setNavigationIcon(R.drawable.toolbar_menu_icon2_32px);
        setSupportActionBar(toolbar3);
        View headerView = navigationView.getHeaderView(0);

        TextView usernameTextView = headerView.findViewById(R.id.username);
        TextView userroleTextView = headerView.findViewById(R.id.userrole);
       // TextView username =  findViewById(R.id.username);
       // TextView userrole =  findViewById(R.id.userrole);
        usernameTextView.setText("Username: "+user_name1);
        userroleTextView.setText("Role: Admin");

        // Set up the ActionBarDrawerToggle
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar3,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );

        // Set the DrawerLayout listener
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        // Enable the hamburger icon for the ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation item clicks here
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        break;
                    case R.id.nav_about:
                        Intent about_intent = new Intent(admin_menu1.this, AboutActivity.class);
                        startActivity(about_intent);
                        break;
                    case R.id.nav_share:
                        break;
                    case R.id.nav_help:
                        Intent help_intent = new Intent(admin_menu1.this, AdminHelpMain.class);
                        startActivity(help_intent);
                        break;
                    case R.id.nav_settings:
                        Intent new_menu2 = new Intent(admin_menu1.this, Admin_menu_activity.class);
                        new_menu2.putExtra("utype", user_type);
                        new_menu2.putExtra("uname1", user_name1);
                        startActivity(new_menu2);
                        Log.d(TAG, "We are admin manage user");
                        break;
                    case R.id.nav_logout:
                        showCustomDialog();
                                break;
                }

                // Close the drawer after handling the click
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        initMenuView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle ActionBarDrawerToggle clicks
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initMenuView() {
        Log.d(TAG, "initMenuView here");
        settingss = findViewById(R.id.settingss);
        settingss.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent new_menu = new Intent(admin_menu1.this, Admin_menu_activity.class);
                new_menu.putExtra("utype", user_type);
                new_menu.putExtra("uname1", user_name1);
                startActivity(new_menu);
                Log.d(TAG, "We are admin manage user");
            }
        });

        directorylist = findViewById(R.id.directorylist);
        directorylist.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TransBasic transBasic = TransBasic.getInstance(sharedPreferences);
                SharedPreferences sharedPreferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("printtype", "directorylist");
                editor.commit();

                transBasic.printTest(0);
                Log.d(TAG, "We are Directory list");
            }
        });

        managetrans = findViewById(R.id.managetrans);
        managetrans.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent new_menu = new Intent(admin_menu1.this, Manage_Transactions.class);
                new_menu.putExtra("utype", user_type);
                startActivity(new_menu);
                Log.d(TAG, "We are back to home");
            }
        });

        recieve = findViewById(R.id.recieve);
        recieve.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(admin_menu1.this, keytransfer.class));
            }
        });

//........................Added by Amlakie for Admin Help..............................................
        adminmhelp = findViewById(R.id.adminmenuhelp);
        adminmhelp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(admin_menu1.this, AdminHelpMain.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
           showCustomDialog();
        }
    }
    // Define a method to show the dialog box
    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Perform logout action here
                // For example, you can start the login activity to logout the user
                // Replace LoginActivity.class with your actual login activity class
                Intent intent = new Intent(admin_menu1.this, user_login.class);
                startActivity(intent);
                finish(); // Finish the current activity after logout
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog if user clicks No
                dialog.dismiss();
            }
        });
        // Create and show the dialog box
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    // Inside your admin_menu1 activity


    // Method to show the custom dialog
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
                Intent intent = new Intent(admin_menu1.this, MenuActivity.class);
                startActivity(intent);
                finish(); // Finish the current activity after logout
                customDialog.dismiss(); // Dismiss the dialog after performing the action
            }
        });

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog if user clicks No
                customDialog.dismiss();
            }
        });

        // Create and show the dialog
        customDialog = builder.create();
        customDialog.show();
    }

}
