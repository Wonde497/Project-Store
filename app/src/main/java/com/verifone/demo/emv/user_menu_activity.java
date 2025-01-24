package com.verifone.demo.emv;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;


import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
//import android.widget.Toolbar;

import com.verifone.demo.emv.userfunctions.user_register;
import com.verifone.demo.emv.userfunctions.cashier_status;
import com.verifone.demo.emv.userfunctions.view_users;
import com.verifone.demo.emv.userfunctions.user_deletes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;
public class user_menu_activity extends AppCompatActivity {

    static String TAG = "MENU";
    boolean homePressed = false;
    String utype;
    String menu_title;
    Fragment frag = null;

    ExpandableListView expandableListViewExample;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableTitleList;
    HashMap<String, List<String>> expandableDetailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        utype = getIntent().getExtras().getString("utype");

        Log.d(TAG, "Menu: " + utype);
        setContentView(R.layout.activity_user_menu);

        expandableListViewExample = (ExpandableListView) findViewById(R.id.expandableListUserMenu);

        /*if (utype.equals("supervisor")) {
            startActivity(new Intent(user_menu_activity.this, Supervisor_menu_activity.class));
            finish();
        }
        else if (utype.equals("support")) {
            startActivity(new Intent(user_menu_activity.this, Support_menu_activity.class));
            finish();
        }
        else if (utype.equals("administrator")) {
            startActivity(new Intent(user_menu_activity.this, Admin_menu_activity.class));
            finish();
        }
        else if (utype.equals("cashier")) {
            startActivity(new Intent(user_menu_activity.this, Cashier_menu_activity.class));
            finish();
        }*/
        /*expandableTitleList = new ArrayList<String>(expandableDetailList.keySet());
        expandableListAdapter = new CustomizedExpandableListAdapter(this, expandableTitleList, expandableDetailList);
        expandableListViewExample.setAdapter(expandableListAdapter);

        expandableListViewExample.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //Toast.makeText(getApplicationContext(), expandableTitleList.get(groupPosition) + " List Expanded.", Toast.LENGTH_SHORT).show();
            }
        });

        // This method is called when the group is collapsed
        expandableListViewExample.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                //Toast.makeText(getApplicationContext(), expandableTitleList.get(groupPosition) + " List Collapsed.", Toast.LENGTH_SHORT).show();
            }
        });

        expandableListViewExample.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                String selected_menu= expandableDetailList.get(expandableTitleList.get(groupPosition)).get(childPosition);
                menu_sel(selected_menu);
                return false;
            }
        });
*/
        /////////////////////////////

       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        menu_title= utype.substring(0, 1).toUpperCase() + utype.substring(1).toLowerCase();
        toolbar.setTitle(menu_title + " Menu");
        setSupportActionBar(toolbar);

    }

    public void menu_sel(String selected_menu)
    {
        Bundle bundle = new Bundle();
        bundle.putString("user_type", utype);

        Log.d(TAG, "Selected: " +  selected_menu);
        if (utype.equals("supervisor"))
        {
            if (selected_menu.equals("Register Cashier"))
            {
                expandableListViewExample.setVisibility(View.INVISIBLE);
                frag=new user_register();
                frag.setArguments(bundle);
                if (frag != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame, frag);
                    transaction.commit();
                }
            }

        }
        else if (utype.equals("administrator"))
        {
            if (selected_menu.equals("Register Support"))
            {
                expandableListViewExample.setVisibility(View.INVISIBLE);
                frag=new user_register();
                frag.setArguments(bundle);
                if (frag != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame, frag);
                    transaction.commit();
                }
            }
            else if (selected_menu.equals("View Support"))
            {
                Log.d("EMVDemo", "view selected");

                expandableListViewExample.setVisibility(View.INVISIBLE);
                frag=new view_users();
                frag.setArguments(bundle);
                if (frag != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame, frag);
                    transaction.commit();
                }
            }
            else if (selected_menu.equals("Delete Support"))
            {
                Log.d("EMVDemo", "delete selected");

                expandableListViewExample.setVisibility(View.INVISIBLE);
                frag=new user_deletes();
                frag.setArguments(bundle);
                if (frag != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame, frag);
                    transaction.commit();
                }
            }
        }
        else if (utype.equals("support"))
        {
            if (selected_menu.equals("Register supervisor"))
            {
                expandableListViewExample.setVisibility(View.INVISIBLE);
                frag=new user_register();
                frag.setArguments(bundle);
                if (frag != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame, frag);
                    transaction.commit();
                }
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       /* MenuInflater inflater=getMenuInflater();
        if (utype.equals("supervisor"))
            inflater.inflate(R.menu.nav_items_supervisor,menu);
        else if (utype.equals("support"))
            inflater.inflate(R.menu.nav_items_support,menu);
        else if (utype.equals("administrator"))
            inflater.inflate(R.menu.nav_items_admin,menu);
        else if (utype.equals("cashier"))
            inflater.inflate(R.menu.nav_items_cashier,menu);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        Bundle bundle = new Bundle();

        bundle.putString("user_type", utype);


        switch (item.getItemId())
        {
            case R.id.reg_cashier:
                expandableListViewExample.setVisibility(View.INVISIBLE);
                frag=new user_register();
                frag.setArguments(bundle);
                if (frag != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame, frag);
                    transaction.commit();
                }
                break;
            case R.id.status_cashier:
                expandableListViewExample.setVisibility(View.INVISIBLE);
                frag=new cashier_status();
                if (frag != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame, frag);
                    transaction.commit();
                }
                break;
            case R.id.reg_super:
                expandableListViewExample.setVisibility(View.INVISIBLE);
                frag=new user_register();
                frag.setArguments(bundle);
                if (frag != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame, frag);
                    transaction.commit();
                }
                break;
            case R.id.reg_support:
                expandableListViewExample.setVisibility(View.INVISIBLE);
                frag=new user_register();
                frag.setArguments(bundle);
                if (frag != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame, frag);
                    transaction.commit();
                }
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "Back Pressed Method ");

        if (frag != null) {
            Log.d(TAG, "Back 2");
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(frag);
            transaction.commit();
            frag=null;
            expandableListViewExample.setVisibility(View.VISIBLE);
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
            startActivity(new Intent(user_menu_activity.this, MenuActivity.class));
            finish();
        }
    }
}