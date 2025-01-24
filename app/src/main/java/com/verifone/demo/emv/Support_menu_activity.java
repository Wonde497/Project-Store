package com.verifone.demo.emv;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import java.util.Stack;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.GravityCompat;

import android.widget.TextView;

import com.verifone.demo.emv.Admin_menu.admin_menu1;
import com.verifone.demo.emv.Help.AboutActivity;
import com.verifone.demo.emv.Help.AdminHelpMain;
import com.verifone.demo.emv.Help.HelpMain;
import com.verifone.demo.emv.Help.SupportHelpMain;
import com.verifone.demo.emv.Supervisor_menu.Supervisor_manage_users;
import com.verifone.demo.emv.Support_menu.Communication_config;
import com.verifone.demo.emv.Support_menu.Update_Base_Screen1;
import com.verifone.demo.emv.Support_menu.Update_Base_Screen2;
import com.verifone.demo.emv.Support_menu.Update_Base_Screen3;
import com.verifone.demo.emv.Support_menu.Update_Base_Screen4;
import com.verifone.demo.emv.Support_menu.Update_Comm_Type;
import com.verifone.demo.emv.Support_menu.Update_Currency;
import com.verifone.demo.emv.Support_menu.Update_Terminal_Mode;
import com.verifone.demo.emv.terminalfunctions.View_TerminalInfo;
import com.verifone.demo.emv.transaction.TransBasic;
import com.verifone.demo.emv.userfunctions.user_register;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class Support_menu_activity extends AppCompatActivity {
    static String TAG = "MENU";
    boolean homePressed = false;
    String user_type;


    String menu_title;
    Fragment support_frag = null;
    private Stack<Fragment> fragmentStack; // Stack to maintain fragment history
    FrameLayout support_frame;
    FrameLayout frame;
    String utype,Logon_name;
    private TextView toolbar,logoon;
    ImageButton comconfig;
    ImageButton termconfig;
    ImageButton viewterminfo;
    ImageButton comsetupreprint;
    ImageButton termsetupreprint;
    ImageButton settings;
    ImageButton managetrans;
    ImageButton supportmainhelp;
    SharedPreferences sharedPreferences;



    private DrawerLayout supportdrawerLayout;
    private NavigationView supportnavigationView;
    private ActionBarDrawerToggle supportactionBarDrawerToggle;
    Toolbar supporttoolbar;
    private AlertDialog supportcustomDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        fragmentStack = new Stack<>();


        utype = "support";
        Log.d(TAG, "Menu: " + utype);
        Logon_name = getIntent().getExtras().getString("uname");

        setContentView(R.layout.activity_support_menu);
        sharedPreferences=getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
        support_frame=(FrameLayout) findViewById(R.id.support_frame);
        frame=(FrameLayout) findViewById(R.id.frame);
        toolbar=(TextView) findViewById(R.id.toolbar);
        managetrans = (ImageButton) findViewById(R.id.managetrans);
        logoon=(TextView) findViewById(R.id.logoon);
        toolbar.setText("Support Menu");
        logoon.setText("Logged In User:  "+Logon_name );
        initMenuView();
        final SharedPreferences preferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username",Logon_name);
        editor.putString("usertype",utype);
        editor.commit();



        supportdrawerLayout = findViewById(R.id.support_drawer_layout);
        supportnavigationView = findViewById(R.id.supportnavigation_view);
        supporttoolbar = findViewById(R.id.supporttoolbar);
        supporttoolbar.setNavigationIcon(R.drawable.toolbar_menu_icon2_32px);
        setSupportActionBar(supporttoolbar);

        View headerView = supportnavigationView.getHeaderView(0);

        TextView usernameTextView = headerView.findViewById(R.id.username);
        TextView userroleTextView = headerView.findViewById(R.id.userrole);
        usernameTextView.setText("Username: "+Logon_name);
        userroleTextView.setText("Role:  Support");

        // Set up the ActionBarDrawerToggle
        supportactionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                supportdrawerLayout,
                supporttoolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );



        // Set the DrawerLayout listener
        supportdrawerLayout.addDrawerListener(supportactionBarDrawerToggle);

        // Enable the hamburger icon for the ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        supportnavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation item clicks here
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        if (support_frag != null) {
                            support_frag = null;
                            support_frame.setVisibility(View.VISIBLE);
                           // frame.setVisibility(View.VISIBLE);
                        }
                        break;
                    case R.id.nav_about:
                        Intent about_intent = new Intent(Support_menu_activity.this, AboutActivity.class);
                        startActivity(about_intent);
                        break;
                    case R.id.nav_share:
                        break;
                    case R.id.nav_help:

                        break;
                    case R.id.nav_settings:
                        VFIApplication.maskHomeKey(false);
                        startActivity(new Intent(Support_menu_activity.this, Support_menu_settings.class));
                        break;
                    case R.id.nav_logout:
                        showCustomDialog();
                        break;
                }

                // Close the drawer after handling the click
                supportdrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });



       /* Button logout=(Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("usertype","");
                editor.commit();
            }
        });*/



    }
    // Method to add fragment to the stack
    public void addToFragmentStack(Fragment fragment) {
        fragmentStack.push(fragment);
    }

    private void initMenuView() {
        final Bundle bundle = new Bundle();
        bundle.putString("user_type", utype);

        comconfig = (ImageButton) findViewById(R.id.comconfig);
        // regsupp.setOnClickListener(this);
        comconfig.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                frame.setVisibility(View.INVISIBLE);
                support_frag=new Communication_config();
                support_frag.setArguments(bundle);
                if (support_frag != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.support_frame, support_frag);
                    transaction.commit();
                }

            }
        });

        viewterminfo = (ImageButton) findViewById(R.id.viewterminfo);
        // regsupp.setOnClickListener(this);
        viewterminfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                frame.setVisibility(View.INVISIBLE);
                support_frag=new View_TerminalInfo();
                support_frag.setArguments(bundle);
                if (support_frag != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.support_frame, support_frag);
                    transaction.commit();
                }

            }
        });

        settings = (ImageButton) findViewById(R.id.settings);
        //ensupp.setOnClickListener(this);
        settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VFIApplication.maskHomeKey(false);
                startActivity(new Intent(Support_menu_activity.this, Support_menu_settings.class));


            }
        });

        termconfig = (ImageButton) findViewById(R.id.termconfig);
        termconfig.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                frame.setVisibility(View.INVISIBLE);
                support_frag=new Update_Base_Screen1();
                support_frag.setArguments(bundle);
                if (support_frag != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.support_frame, support_frag);
                    transaction.commit();
                }

            }
        });

        comsetupreprint = (ImageButton) findViewById(R.id.comsetupreprint);
        comsetupreprint.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                frame.setVisibility(View.INVISIBLE);
                TransBasic transBasic = TransBasic.getInstance(sharedPreferences);
                SharedPreferences sharedPreferences =getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("printtype","terminalsetup");
                editor.commit();
                Log.d(TAG, "print terminal setup");
                transBasic.printTest(0);
                frame.setVisibility(View.VISIBLE);

            }
        });
        termsetupreprint = (ImageButton) findViewById(R.id.termsetupreprint);
        termsetupreprint.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              frame.setVisibility(View.INVISIBLE);
                TransBasic transBasic = TransBasic.getInstance(sharedPreferences);
                SharedPreferences sharedPreferences =getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("printtype","terminalinfo");
                editor.commit();
                Log.d(TAG, "print terminal information");
                transBasic.printTest(0);
                frame.setVisibility(View.VISIBLE);


            }
        });


        //............................Added by Amlakie for Help onclick.............................................
        supportmainhelp = (ImageButton) findViewById(R.id.supportmenuhelp);
        supportmainhelp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                startActivity(new Intent(Support_menu_activity.this, SupportHelpMain.class));
            }

    });

    }
    //@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comconfig:
                //VFIApplication.maskHomeKey(false);
                support_frag=new user_register();
                //frag.setArguments(bundle);
                if (support_frag != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.support_frame, support_frag);
                    transaction.commit();
                }
                break;
            /*case R.id.termconfig:
                VFIApplication.maskHomeKey(false);
                //startActivity(new Intent(MenuActivity.this, SettingsActivity.class));
                startActivity(new Intent(Support_menu_activity.this, user_view.class));

                //VFIApplication.maskHomeKey(false);
                //frag=new view_users();
                //frag.setArguments(bundle);
                //if (frag != null) {
                // FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                // transaction.replace(R.id.frame, frag);
                // transaction.commit();
                //}
                break;
            case R.id.viewterminfo:

                startActivity(new Intent(Support_menu_activity.this, view_users.class));
                break;
            case R.id.comsetupreprint:

                startActivity(new Intent(Support_menu_activity.this, view_users.class));
                break;
            case R.id.termsetupreprint:

                startActivity(new Intent(Support_menu_activity.this, view_users.class));
                break;
            case R.id.settings:

                startActivity(new Intent(Support_menu_activity.this, view_users.class));
                break;
            case R.id.changesuperpin:

                startActivity(new Intent(Support_menu_activity.this, view_users.class));
                break;*/
        }

    }


    @Override
    public void onBackPressed() {
        Log.d(TAG, "Back pressed");

        if (!fragmentStack.isEmpty()) {
            // Pop the top fragment from the stack
            fragmentStack.pop();

            if (!fragmentStack.isEmpty()) {
                // Get the previous fragment from the stack (if available)
                Fragment previousFragment = fragmentStack.peek();

                // Replace the current fragment with the previous one
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.support_frame, previousFragment)
                        .commit();

                // Update support_frag to the previous fragment
                support_frag = previousFragment;

                // Make sure the frame is visible after replacing the fragment
                // frame.setVisibility(View.VISIBLE);
            } else {
                // If no more fragments in the stack, clear support_frag and show main view
                //support_frag = null;
                if (support_frag != null) {
                    // If there is a fragment, remove it and make frame visible
                    getSupportFragmentManager().beginTransaction()
                            .remove(support_frag)
                            .commit();
                    support_frag = null;
                    frame.setVisibility(View.VISIBLE);
                }
                //getSupportFragmentManager().beginTransaction()
                       // .remove(support_frag)
                      //  .commit();
              //  support_frag = null;
              //  frame.setVisibility(View.VISIBLE);
                //support_frame.setVisibility(View.VISIBLE); // Hide the support_frame
              //  frame.setVisibility(View.VISIBLE); // Show the main view (frame)
            }
        } else if (supportdrawerLayout.isDrawerOpen(GravityCompat.START)) {
            // If the navigation drawer is open, close it
            supportdrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            // If no fragment is present, show the custom dialog
            showCustomDialog();
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

    public void NextPressedMid()
    {
        support_frag = new Update_Base_Screen2();
        //support_frag.setArguments(bundle);
        if (support_frag != null)
        {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.support_frame, support_frag);
            transaction.commit();
            support_frag=null;
            frame.setVisibility(View.INVISIBLE);

        }
    }
    public void NextPressedMname()
    {
        support_frag = new Update_Base_Screen3();
        //update_frag.setArguments(bundle);
        if (support_frag != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.support_frame, support_frag);
            transaction.commit();
            support_frag=null;
            frame.setVisibility(View.INVISIBLE);
        }
    }
    public void NextPressedMaddress()
    {
        support_frag = new Update_Base_Screen4();
        //update_frag.setArguments(bundle);
        if (support_frag != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.support_frame, support_frag);
            transaction.commit();
            support_frag=null;
            frame.setVisibility(View.INVISIBLE);
        }
    }

    public void BackPressedTID()
    {
        if (support_frag != null && support_frag.isVisible()) {
            // If there is a fragment and it's visible, remove it
            getSupportFragmentManager().beginTransaction()
                    .remove(support_frag)
                    .commit();
            support_frag = null;
            //support_frame.setVisibility(View.GONE);
            frame.setVisibility(View.VISIBLE);
        } else if (supportdrawerLayout.isDrawerOpen(GravityCompat.START)) {
            // If the navigation drawer is open, close it
            supportdrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            // If no fragment is present, show custom dialog or perform default back press action
            showCustomDialog();
        }
    }
    public void NextPressedTmode()
    {
        support_frag = new Update_Terminal_Mode();
        //update_frag.setArguments(bundle);
        if (support_frag != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.support_frame, support_frag);
            transaction.commit();
            support_frag=null;
            frame.setVisibility(View.INVISIBLE);
        }
    }
    public void NextPressedCurrency()
    {
        support_frag = new Update_Currency();
        //update_frag.setArguments(bundle);
        if (support_frag != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.support_frame, support_frag);
            transaction.commit();
            support_frag=null;
            frame.setVisibility(View.INVISIBLE);
        }//NextPressedCurrency()
    }
    public void NextPressedComtype()
    {
        support_frag = new Update_Comm_Type();
        //update_frag.setArguments(bundle);
        if (support_frag != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.support_frame, support_frag);
            transaction.commit();
            support_frag=null;
            frame.setVisibility(View.INVISIBLE);
        }//NextPressedCurrency()
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
                Intent intent = new Intent(Support_menu_activity.this, MenuActivity.class);
                startActivity(intent);
                finish(); // Finish the current activity after logout
                supportcustomDialog.dismiss(); // Dismiss the dialog after performing the action
            }
        });

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog if user clicks No
                supportcustomDialog.dismiss();
            }
        });

        // Create and show the dialog
        supportcustomDialog = builder.create();
        supportcustomDialog.show();
    }
  }