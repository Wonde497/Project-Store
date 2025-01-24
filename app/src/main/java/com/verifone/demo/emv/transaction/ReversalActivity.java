package com.verifone.demo.emv.transaction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.verifone.demo.emv.DBHandler;
import com.verifone.demo.emv.MenuActivity;
import com.verifone.demo.emv.Support_menu_activity;
import com.verifone.demo.emv.Transactiondata;
import com.verifone.demo.emv.Utilities.ActivityCollector;
import com.verifone.demo.emv.Utilities.Utility;
import com.verifone.demo.emv.data_handlers.User;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class ReversalActivity extends AppCompatActivity
{
    private static final String TAG = "ReversalActivity";
    String txn_type;
    String txntype="",Txn_Menu_Type="";
    SharedPreferences sharedPreferences;
    Button search;
    EditText searchInput;
   DBHandler dbHandler;
   public static User user;
    private AlertDialog reversalConfirmDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
        txntype = sharedPreferences.getString("txn_type", "");
        Txn_Menu_Type = sharedPreferences.getString("Txn_Menu_Type", "");
        Log.d(TAG, "Txn_type is "+txntype);
        Log.d(TAG, "Txn_Menu_Type is "+Txn_Menu_Type);
        //txn_typ=txntype;
        setContentView(R.layout.activity_reversal);
        ActivityCollector.addActivity(this);
//Read Amount from database
        dbHandler = new DBHandler(getApplicationContext());
       Transactiondata tr=new Transactiondata(sharedPreferences,getApplicationContext());
        TextView titleTextView = (TextView) findViewById(R.id.trans_name);

        search=findViewById(R.id.search);
        searchInput=findViewById(R.id.inputsearch);



      // checkcard();

     init();
    }

    private void init() {



     search.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             if(!searchInput.getText().toString().equals("")){
                 user = dbHandler.getTxnDataByApprovalCode(searchInput.getText().toString());
                if(user!=null){

                    Transactiondata.ResponseFields.field03=user.getField_03();
                    Transactiondata.ResponseFields.field04=user.getField_04();
                    Transactiondata.ResponseFields.field07=user.getField_07();
                    Transactiondata.ResponseFields.field11=user.getField_11();
                    Transactiondata.ResponseFields.field12=user.getField_12();
                    Transactiondata.ResponseFields.field37=user.getField_37();
                    Transactiondata.GLobalFields.Field55=user.getField_55();

                    Log.d(TAG,"Txn_Menu_Type   "+sharedPreferences.getString("Txn_Menu_Type", ""));

                    if(Txn_Menu_Type.equals("Manualcard"))
                    {
                        showCustomDialog();

                    }else {//isn't Manualcard
                        /*AlertDialog.Builder builder=new AlertDialog.Builder(ReversalActivity.this);
                        builder.setTitle("Confirm");
                        builder.setMessage("You are Reversing ETB"+Utility.getReadableAmount(Transactiondata.ResponseFields.field04));

                        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(ReversalActivity.this, CheckCardActivity.class));
                                finish();

                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.show();
                        */

                        showCustomDialog();
                    }
                }else{
                    Log.d(TAG,"No txn done  with this approval code");
                    Toast.makeText(ReversalActivity.this,"No Txn done with this approval code",Toast.LENGTH_SHORT).show();

                }
             }else{
                 Toast.makeText(ReversalActivity.this,"Please Enter Approval Code",Toast.LENGTH_SHORT).show();
                  }

         }
     });
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
        messageTextView.setText(" Are you sure you want to reverse ETB"+Utility.getReadableAmount(Transactiondata.ResponseFields.field04));
        // Set the message
        // messageTextView.setText("Are you sure you want to logout?");

        // Set click listeners for the buttons
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform logout action here
                if(Txn_Menu_Type.equals("Manualcard"))
                {
                    startActivity(new Intent(ReversalActivity.this, ManualTestActivity.class));
                    finish();
                    reversalConfirmDialog.dismiss();
                }else {
                    startActivity(new Intent(ReversalActivity.this, CheckCardActivity.class));
                    finish(); // Finish the current activity after logout
                    reversalConfirmDialog.dismiss();
                }// Dismiss the dialog after performing the action
            }
        });

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog if user clicks No
                reversalConfirmDialog.dismiss();
            }
        });

        // Create and show the dialog
        reversalConfirmDialog = builder.create();
        reversalConfirmDialog.show();
    }

  }



