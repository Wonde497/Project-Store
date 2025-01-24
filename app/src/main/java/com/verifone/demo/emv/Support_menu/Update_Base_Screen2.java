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


public class Update_Base_Screen2 extends Fragment {
    static String TAG="UPDATE_MERID";
    public String user_type;
    String menu_title;
    private TextView ttitel;
    private TextView tv,tv1;
    private AlertDialog cashiercustomDialog;

    FrameLayout frame;
    private EditText merchant_id;
    private String merchantid;
    Button Update,Cancle;
    private DBHandler dbHandler;
    private DBHandler.terminal_functions terminal_fun;

    View update_frag;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       //Bundle bundle=new Bundle();
        //bundle=getActivity().getIntent().getExtras();
        //Bundle extras = getActivity().getIntent().getExtras();
      // if(bundle!=null) {
            //user_type = getArguments().getString("user_type");
       // }

        //Log.d(TAG, user_type);
        update_frag=inflater.inflate(R.layout.fragment_update__base__screen2, container, false);
        tv = (TextView) update_frag.findViewById(R.id.toolbar);
        tv1 = (TextView) update_frag.findViewById(R.id.currentmid);

       /* if (user_type.equals("supervisor"))
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
        loadDataMID();
        tv.setText("UPDATE MERCHANT ID");
        tv1.setText("Current MID: " + merchantid);
        Log.d(TAG, "Current Merchant_Id   "+merchantid);


        // Find the ImageButton
      //  ImageButton backButton = update_frag.findViewById(R.id.updatebackmid);

        // Set OnClickListener to the ImageButton
      /*  backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Navigate to Update_Base_Screen1 fragment
                ((Support_menu_activity)getActivity()).BackPressedTID();
            }
        });
*/

        initScreen2();
        // Add current fragment to the stack in Support_menu_activity
        ((Support_menu_activity) getActivity()).addToFragmentStack(this);
        return update_frag;
    }

    public void loadDataMID()
    {
        String selection = "";
        String[] selectionArgs = {};
        Log.d("DBRES", "Mhere 1");
        List<User> TermInfoList = new ArrayList<User>();
        dbHandler = new DBHandler(update_frag.getContext());
        DBHandler.terminal_functions terminal_fun=dbHandler.new terminal_functions();
        Log.d("DBRES", "Mhere 3");
        TermInfoList= terminal_fun.viewMidInfo(selection,selectionArgs);
        int rows = TermInfoList.size();
        Log.d("DBRES", "Mhere2  " + rows);
        Log.d("DBRES", "Mhere for loop");
        for(int i = 0; i < rows; i ++) {
            User row = null;
            if (i ==0) {
                row = TermInfoList.get(i);
                Log.d("DBRES", "Merchant_id: "+  row.getMerchant_id());
                merchantid=row.getMerchant_id();
                Log.d("DBRES", " :merchantid from loaddata "+  merchantid);

            }
        }
    }
    private void initScreen2() {

        merchant_id = (EditText) update_frag.findViewById(R.id.Tvupdatemerchantid);
        ttitel = (TextView) update_frag.findViewById(R.id.toolbar);

        Update = (Button) update_frag.findViewById(R.id.idBtnupdatemerchantid);
        merchant_id.setOnKeyListener(new View.OnKeyListener()
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

                String Merchant_id = merchant_id.getText().toString();
                String id="1";
                if (Merchant_id.length() < 8 || Merchant_id.length()>15) {
                    Toast.makeText(update_frag.getContext(), "Merchant id Between 8 - 15 characters.", Toast.LENGTH_LONG).show();
                    return;

                }
                else if ((Merchant_id.isEmpty())) {
                    Toast.makeText(update_frag.getContext(), "Merchant id cannot be  empty.", Toast.LENGTH_LONG).show();
                    return;

                }


                else {

                    DBHandler.terminal_functions terminal_fun = dbHandler.new terminal_functions();
                    terminal_fun.UpdateMid(id, Merchant_id);

                    Toast.makeText(update_frag.getContext(), "Merchantid has been update succefully.", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "finshed Updating ");
                }
                    //getActivity().onBackPressed();
                    ((Support_menu_activity)getActivity()).NextPressedMname();

            }
        });
        Cancle = (Button) update_frag.findViewById(R.id.idBtncancel);
        Cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((Support_menu_activity)getActivity()).NextPressedMname();
            }

        });
        dbHandler = new DBHandler(update_frag.getContext());
    }

    public void onBackPressed() {
        // Call method in activity to handle back action
        getActivity().onBackPressed();
    }





    //@Override
    /*public void onBackPressed() {
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

