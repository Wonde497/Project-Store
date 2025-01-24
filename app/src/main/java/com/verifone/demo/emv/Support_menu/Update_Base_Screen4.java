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

public class Update_Base_Screen4 extends Fragment {

    static String TAG="UPDATE_MERADD";
    public String user_type;
    String menu_title;

    private AlertDialog cashiercustomDialog;

    private TextView ttitel;
    FrameLayout frame;
    private EditText merchant_address;
    private TextView toolbar,currentmadd;
    Button Update,Cancle;
    private String merchantaddress;
    private DBHandler dbHandler;
    private DBHandler.terminal_functions terminal_fun;

    View update_frag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        update_frag= inflater.inflate(R.layout.fragment_update__base__screen4, container, false);

        merchant_address = (EditText) update_frag.findViewById(R.id.Tvregmerchantaddress);
        toolbar = (TextView) update_frag.findViewById(R.id.toolbar);
        currentmadd = (TextView) update_frag.findViewById(R.id.currentmadd);

        loadMaddressData();
        toolbar.setText("UPDATE MERCHANT ADDRESS");
        currentmadd.setText("Current Madd: "+merchantaddress);
        Log.d(TAG, "Current Merchant address  "+merchantaddress);

        initScreen5();
        ((Support_menu_activity) getActivity()).addToFragmentStack(this);
        return update_frag;
    }

    public  void loadMaddressData()
    {
        String selection = "";
        String[] selectionArgs = {};
        Log.d("DBRES", "Amex here");
        List<User> TermInfoList = new ArrayList<User>();
        dbHandler = new DBHandler(update_frag.getContext());
        DBHandler.terminal_functions terminal_fun=dbHandler.new terminal_functions();
        Log.d("DBRES", "There 3");
        TermInfoList= terminal_fun.viewMaddress(selection,selectionArgs);
        int rows = TermInfoList.size();
        Log.d("DBRES", "There2  " + rows);
        Log.d("DBRES", "Amex for loop started");
        for(int i = 0; i < rows; i ++) {
            User row = null;
            if (i ==0) {
                row = TermInfoList.get(i);
                Log.d("DBRES", "Mrchant Address: "+  row.getMerchant_address());

                merchantaddress =row.getMerchant_address();
                Log.d("DBRES", "Mrchant Address: from loaddata "+ merchantaddress);

            }
        }
    }
    private void initScreen5() {

        Update = (Button) update_frag.findViewById(R.id.idBtnregmerchantaddress);
        merchant_address.setOnKeyListener(new View.OnKeyListener()
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

                Log.d(TAG, "Update button clicked initScreen5 On MerchantName");

                String strmeraddress=merchant_address.getText().toString();
                String id="1";
                if (strmeraddress.length()<2)
                {
                    Toast.makeText(update_frag.getContext(),"Merchant_add Morethan 2 characters.", Toast.LENGTH_LONG).show();
                    return;
                }
                else if (strmeraddress.isEmpty())
                {
                    Toast.makeText(update_frag.getContext(),"Please enter Merchant_add is not Empity.", Toast.LENGTH_SHORT).show();
                    return;

                }

                DBHandler.terminal_functions terminal_fun=dbHandler.new terminal_functions();
                terminal_fun.UpdateMaddress(id,strmeraddress);

                Toast.makeText(update_frag.getContext(),"Merchant Address has been Updated succefully.", Toast.LENGTH_SHORT).show();

                ((Support_menu_activity)getActivity()).NextPressedTmode();

            }

        });
        Cancle = (Button) update_frag.findViewById(R.id.idBtncancel);
        Cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((Support_menu_activity)getActivity()).NextPressedTmode();
            }

        });

        dbHandler = new DBHandler(update_frag.getContext());
        // Log.d(TAG, "out off initScreen2");
    }
    public void onBackPressed() {
        // Call method in activity to handle back action
        ((Support_menu_activity) getActivity()).BackPressedTID();
    }

    // @Override
   /* public void onBackPressed() {

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