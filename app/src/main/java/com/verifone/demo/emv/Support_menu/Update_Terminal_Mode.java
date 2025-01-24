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

public class Update_Terminal_Mode extends Fragment {
    static String TAG="UPDATE_Mode";
    ImageButton branchmode;
    ImageButton merchantmode;

    public String user_type;
    String menu_title;

    private AlertDialog cashiercustomDialog;

    private TextView tv,tv1;
    private DBHandler dbHandler;
    private DBHandler.terminal_functions terminal_fun;
    View update_frag;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //user_type = getArguments().getString("user_type");
        //Log.d(TAG, user_type);

        update_frag= inflater.inflate(R.layout.fragment_update__terminal__mode, container, false);
       // tv = (TextView) update_frag.findViewById(R.id.toolbar);

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

        //tv.setText("SELECT TERMINAL MODE");
        ModeMenuView();
        ((Support_menu_activity) getActivity()).addToFragmentStack(this);
    return update_frag;
    }
    private void ModeMenuView() {
        branchmode = (ImageButton) update_frag.findViewById(R.id.branch);
        branchmode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //VFIApplication.maskHomeKey(false);
                Log.d(TAG, "inside creat function : ");

                String Bmode = "Branch";
                String id = "1";

                DBHandler.terminal_functions terminal_fun = dbHandler.new terminal_functions();
                terminal_fun.UpdateTmode(id,Bmode);

                Toast.makeText(update_frag.getContext(), "Branch Mode has been Updated succefully.", Toast.LENGTH_SHORT).show();

                ((Support_menu_activity)getActivity()).NextPressedCurrency();


            }

        });

        merchantmode = (ImageButton) update_frag.findViewById(R.id.merchant);
        merchantmode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // VFIApplication.maskHomeKey(false);

                String Mmode = "Merchant";
                String id = "1";

                DBHandler.terminal_functions terminal_fun = dbHandler.new terminal_functions();
                terminal_fun.UpdateTmode(id,Mmode);

                Toast.makeText(update_frag.getContext(), "Merchant Mode has been Updated succefully.", Toast.LENGTH_SHORT).show();

                ((Support_menu_activity)getActivity()).NextPressedCurrency();


            }
        });
        dbHandler = new DBHandler(update_frag.getContext());
    }
  //  @Override
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