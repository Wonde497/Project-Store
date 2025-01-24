package com.verifone.demo.emv.Support_menu;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.verifone.demo.emv.Cashier_menu_activity;
import com.verifone.demo.emv.DBHandler;
import com.verifone.demo.emv.MenuActivity;
import com.verifone.demo.emv.Support_menu_activity;
import com.verifone.demo.emv.transaction.TransBasic;

import android.support.v4.app.FragmentTransaction;
import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class Update_Comm_Type extends Fragment {

    static String TAG="UPDATE_COMTYPE";
    public String user_type;
    ImageButton Lte1;
    ImageButton Ethernet1;

    private AlertDialog cashiercustomDialog;

    ImageButton Wifi1;
    String Lte;
    String Ethernet;
    String Wifi;
    private TextView tv,tv1;
    private DBHandler dbHandler;
    private DBHandler.terminal_functions terminal_fun;
    View update_frag;
    SharedPreferences sharedPreferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

    update_frag= inflater.inflate(R.layout.fragment_update__comm__type, container, false);
        sharedPreferences=update_frag.getContext().getSharedPreferences("transaction", Context.MODE_PRIVATE);
        tv = (TextView) update_frag.findViewById(R.id.toolbar);
        Lte1 = (ImageButton) update_frag.findViewById(R.id.Lte);
        Wifi1 = (ImageButton) update_frag.findViewById(R.id.Wifi);


        tv.setText("COMMUNICATION SETUP");

        CommMenuView();
//        ((Support_menu_activity) getActivity()).addToFragmentStack(this);

        return update_frag;

    }

    public void CommMenuView() {

        Lte1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Lte = "4G";
                String id = "1";


                DBHandler.terminal_functions terminal_fun = dbHandler.new terminal_functions();
                terminal_fun.UpdateCommtype(id,Lte);

                Toast.makeText(update_frag.getContext(), "Lte has been updated succefully.", Toast.LENGTH_SHORT).show();

                TransBasic transBasic = TransBasic.getInstance(sharedPreferences);
                SharedPreferences sharedPreferences =update_frag.getContext().getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("printtype","terminalinfo");
                editor.commit();
                Log.d(TAG, "print terminal information");
                transBasic.printTest(0);

               // getActivity().onBackPressed();
                //((Support_menu_activity)getActivity()).onBackPressed();

            }
        });

        Wifi1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Wifi = "Wifi";
                String id = "1";

                DBHandler.terminal_functions terminal_fun = dbHandler.new terminal_functions();
                terminal_fun.UpdateCommtype(id,Wifi);

                Toast.makeText(update_frag.getContext(), "Wifi has been updated succefully.", Toast.LENGTH_SHORT).show();


                TransBasic transBasic = TransBasic.getInstance(sharedPreferences);
                SharedPreferences sharedPreferences =update_frag.getContext().getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("printtype","terminalinfo");
                editor.commit();
                Log.d(TAG, "print terminal information");
                transBasic.printTest(0);
                //getActivity().onBackPressed();
               // ((Support_menu_activity)getActivity()).onBackPressed();

            }
        });

        dbHandler = new DBHandler(update_frag.getContext());
        // Log.d(TAG, "out off initScreen2");
    }
   // @Override
  /*  public void onBackPressed() {
        // Check if any fragment is currently attached to the activity

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