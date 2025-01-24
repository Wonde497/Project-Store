package com.verifone.demo.emv.Support_menu;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Intent;
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

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class Update_Currency extends Fragment {
    static String TAG="UPDATE_CURRENCY";
    private DBHandler dbHandler;
    private DBHandler.terminal_functions terminal_fun;
    ImageButton ETB;
    ImageButton USD;
    ImageButton EUR;

    private AlertDialog cashiercustomDialog;

    public static String ETB1;
    public static String USD1;
    public static String EUR1;

    public String user_type;
    String menu_title;
    private TextView tv,tv1;
    View update_frag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        update_frag= inflater.inflate(R.layout.fragment_update__currency, container, false);
        tv = (TextView) update_frag.findViewById(R.id.toolbar);



        tv.setText("SELECT CURRENCY");
        CurrMenuView();
        ((Support_menu_activity) getActivity()).addToFragmentStack(this);
        return update_frag;
    }
    private void CurrMenuView() {
        ETB=(ImageButton) update_frag.findViewById(R.id.etb);
        ETB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ETB1="230";
                String id = "1";
                DBHandler.terminal_functions terminal_fun=dbHandler.new terminal_functions();
                terminal_fun.UpdateCurrency(id,ETB1);

                Toast.makeText(update_frag.getContext(),"ETB has been updated succefully.", Toast.LENGTH_SHORT).show();

                ((Support_menu_activity)getActivity()).NextPressedComtype();


            }
        });
        USD=(ImageButton) update_frag.findViewById(R.id.usd);
        USD.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                USD1="840";
                String id = "1";

                DBHandler.terminal_functions terminal_fun=dbHandler.new terminal_functions();
                terminal_fun.UpdateCurrency(id,USD1);

                Toast.makeText(update_frag.getContext(),"USD has been updated succefully.", Toast.LENGTH_SHORT).show();

                ((Support_menu_activity)getActivity()).NextPressedComtype();



            }
        });
        EUR=(ImageButton) update_frag.findViewById(R.id.eur);
        EUR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EUR1="978";
                String id = "1";

                DBHandler.terminal_functions terminal_fun=dbHandler.new terminal_functions();
                terminal_fun.UpdateCurrency(id,EUR1);

                Toast.makeText(update_frag.getContext(),"EUR has been updated succefully.", Toast.LENGTH_SHORT).show();

                ((Support_menu_activity)getActivity()).NextPressedComtype();


            }
        });
        dbHandler = new DBHandler(update_frag.getContext());
    }
   // @Override
   /* public void onBackPressed() {
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