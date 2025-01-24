package com.verifone.demo.emv.Support_menu;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.verifone.demo.emv.DBHandler;
import com.verifone.demo.emv.Support_menu_activity;
import com.verifone.demo.emv.data_handlers.User;

import java.util.ArrayList;
import java.util.List;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class Update_Base_Screen1 extends Fragment {

    static String TAG = "UPDATE_TERID";
    View Ttitel;
    public String user_type;
    String menu_title;
    private AlertDialog cashiercustomDialog;

    private EditText terminalid;
    private TextView ttitel;
    private TextView tv, tv1;
    FrameLayout frame;
    Button Update, Cancle;
    private String terminalId;
    private DBHandler dbHandler;
    private DBHandler.terminal_functions terminal_fun;
    View update_frag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Bundle bundle = getArguments();
        //String userType = bundle.getString("user_type");

        user_type = getArguments().getString("user_type");
        Log.d(TAG, user_type);

        update_frag = inflater.inflate(R.layout.fragment_update__base__screen1, container, false);
        tv = (TextView) update_frag.findViewById(R.id.toolbar);
        tv1 = (TextView) update_frag.findViewById(R.id.currenttid);

        if (user_type.equals("supervisor"))
            menu_title = "Cashier";
        else if (user_type.equals("support"))
            menu_title = "Supervisor";
        else if (user_type.equals("administrator"))
            menu_title = "Support";
        else if (user_type.equals("init")) {
            menu_title = "Administrator";
            user_type = "administrator";
        }
        loadDataTID1();

        tv.setText("UPDATE TERMINAL ID");
        tv1.setText("Current TID: " + terminalId);
        Log.d(TAG, "Current Terminal Id   " + terminalId);
        Log.d(TAG, user_type);


        initScreen2();
        // Add current fragment to the stack in Support_menu_activity
        ((Support_menu_activity) getActivity()).addToFragmentStack(this);
        //initScreen3();
        return update_frag;
    }

    public void loadDataTID1() {

        String selection = "";
        String[] selectionArgs = {};

        Log.d("DBRES", "There 1");
        List<User> TermInfoList = new ArrayList<User>();
        dbHandler = new DBHandler(update_frag.getContext());
        //dbHandler = new DBHandler();
        DBHandler.terminal_functions terminal_fun = dbHandler.new terminal_functions();
        Log.d("DBRES", "There 3");
        TermInfoList = terminal_fun.viewTidInfo(selection, selectionArgs);
        int rows = TermInfoList.size();
        Log.d("DBRES", "There2  " + rows);
        Log.d("DBRES", "There for loop");
        for (int i = 0; i < rows; i++) {
            User row = null;
            if (i == 0) {
                row = TermInfoList.get(i);
                Log.d("DBRES", "Terminal_id: " + row.getTerminal_id());
                terminalId = row.getTerminal_id();

                Log.d("DBRES", "terminal_Id : from loaddata " + terminalId);

            }
        }
    }

    private void initScreen2() {

        terminalid = (EditText) update_frag.findViewById(R.id.Tvupdateterminalid);
        ttitel = (TextView) update_frag.findViewById(R.id.toolbar);

        Update = (Button) update_frag.findViewById(R.id.idBtnupdateterminalid);
        terminalid.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            Update.performClick();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "Update button clicked initScreen2");

                String Terminal_id = terminalid.getText().toString();
                String id = "1";
                if (Terminal_id.length() < 8 || Terminal_id.length() > 8) {
                    Toast.makeText(update_frag.getContext(), "Terminal_id Must be  8 characters.", Toast.LENGTH_LONG).show();
                    return;
                } else if (Terminal_id.isEmpty()) {

                    Toast.makeText(update_frag.getContext(), "Please enter Terminal_id.", Toast.LENGTH_SHORT).show();
                    return;

                } else {

                    if (user_type.equals("supervisor"))
                        menu_title = "Cashier";
                    else if (user_type.equals("support"))
                        menu_title = "Supervisor";
                    else if (user_type.equals("administrator"))
                        menu_title = "Support";
                    else if (user_type.equals("init")) {
                        menu_title = "Administrator";
                        user_type = "administrator";
                    }

                    DBHandler.terminal_functions terminal_fun = dbHandler.new terminal_functions();
                    terminal_fun.UpdateTid(id, Terminal_id);

                    Toast.makeText(update_frag.getContext(), "Terminal has been update succefully.", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "finshed Updating ");
                }
                // getActivity().onBackPressed();
                ((Support_menu_activity) getActivity()).NextPressedMid();
                // onBackPressed();


            }

        });
        Cancle = (Button) update_frag.findViewById(R.id.idBtncancel);
        Cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((Support_menu_activity) getActivity()).NextPressedMid();
            }

        });

        dbHandler = new DBHandler(update_frag.getContext());
        // Log.d(TAG, "out off initScreen2");
    }
    //@Override
    //public void onBackPressed() {
        // Call method in activity to handle back action
        //((Support_menu_activity) getActivity()).BackPressedTID();
    //}



}

    //@Override
    //@Override

  /*  private void showCustomDialog() {
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
    }*/

