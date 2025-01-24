package com.verifone.demo.emv.Support_menu;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class Update_Base_Screen3 extends Fragment {
    static String TAG="UPDATE_MERNAME";
    public String user_type;
    String menu_title;
    private TextView ttitel;
    private TextView tv,tv1;
    FrameLayout frame;
    private AlertDialog cashiercustomDialog;

    private EditText merchant_name;
    private String merchantname;
    Button Update,Cancle;
    private DBHandler dbHandler;
    private DBHandler.terminal_functions terminal_fun;

    View update_frag;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       // user_type = getArguments().getString("user_type");
        //Log.d(TAG, user_type);

        update_frag=inflater.inflate(R.layout.fragment_update__base__screen3, container, false);
        tv = (TextView) update_frag.findViewById(R.id.toolbar);
        tv1 = (TextView) update_frag.findViewById(R.id.currentmname);

        /*if (user_type.equals("supervisor"))
            menu_title = "Cashier";
        else if (user_type.equals("support"))
            menu_title = "Supervisor";
        else if (user_type.equals("administrator"))
            menu_title = "Support";
        else if (user_type.equals("init")) {
            menu_title = "Administrator";
            user_type = "administrator";
        }

         */
        loadDatamrname();
        tv.setText("UPDATE MERCHANT NAME");
        tv1.setText("Current Mname :" + merchantname);
        Log.d(TAG, "Current Terminal name   "+merchantname);
       // Log.d(TAG, user_type);

        initScreen3();
        // Add current fragment to the stack in Support_menu_activity
        ((Support_menu_activity) getActivity()).addToFragmentStack(this);
        return update_frag;
    }

    public void loadDatamrname()//merchantname
    {
        String selection = "";
        String[] selectionArgs = {};
        Log.d("DBRES", "Mhere 1");
        List<User> TermInfoList = new ArrayList<User>();
        dbHandler = new DBHandler(update_frag.getContext());
        DBHandler.terminal_functions terminal_fun=dbHandler.new terminal_functions();
        Log.d("DBRES", "Mhere 3");
        TermInfoList= terminal_fun.viewMnameInfo(selection,selectionArgs);
        int rows = TermInfoList.size();
        Log.d("DBRES", "Mhere2  " + rows);
        Log.d("DBRES", "Mhere for loop");
        for(int i = 0; i < rows; i ++) {
            User row = null;
            if (i ==0) {
                row = TermInfoList.get(i);
                Log.d("DBRES", "Merchant_name: "+  row.getMerchant_name());
                merchantname=row.getMerchant_name();
                Log.d("DBRES", "Merchant Nmae : from loaddata "+ merchantname);

            }
        }
    }
    private void initScreen3() {

        merchant_name = (EditText) update_frag.findViewById(R.id.Tvupdatemerchantname);
        ttitel = (TextView) update_frag.findViewById(R.id.toolbar);

        Update = (Button) update_frag.findViewById(R.id.idBtnupdatemerchantname);
        merchant_name.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
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

                Log.d(TAG, "Update button clicked initScreen3");

                String Merchant_name = merchant_name.getText().toString();
                String id="1";
                if (Merchant_name.length() < 2) {
                    Toast.makeText(update_frag.getContext(), "Merchant name More than 2 characters.", Toast.LENGTH_LONG).show();
                    return;

                }
                else if (Merchant_name.isEmpty()) {
                    Toast.makeText(update_frag.getContext(), "Merchant name cannot be Empty.", Toast.LENGTH_LONG).show();
                    return;

                }

                else {

                    /*if (user_type.equals("supervisor"))
                        menu_title = "Cashier";
                    else if (user_type.equals("support"))
                        menu_title = "Supervisor";
                    else if (user_type.equals("administrator"))
                        menu_title = "Support";
                    else if (user_type.equals("init")) {
                        menu_title = "Administrator";
                        user_type = "administrator";
                    }


                     */
                    DBHandler.terminal_functions terminal_fun = dbHandler.new terminal_functions();
                    terminal_fun.UpdateMname(id,Merchant_name);

                    Toast.makeText(update_frag.getContext(), "Merchant Name has been update succefully.", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "finshed Updating ");
                }
                ((Support_menu_activity)getActivity()).NextPressedMaddress();


            }
        });
        Cancle = (Button) update_frag.findViewById(R.id.idBtncancel);
        Cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((Support_menu_activity)getActivity()).NextPressedMaddress();
            }

        });

        dbHandler = new DBHandler(update_frag.getContext());


    }

    public void onBackPressed() {
        // Call method in activity to handle back action
        getActivity().onBackPressed();
    }

    // @Override
    /*public void onBackPressed() {

            showCustomDialog();

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
                startActivity(new Intent(getActivity(), Support_menu_activity.class));

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
}

