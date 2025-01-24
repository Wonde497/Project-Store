package com.verifone.demo.emv.terminalfunctions;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.verifone.demo.emv.DBTerminal;
import com.verifone.demo.emv.MenuActivity;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class txn_mange extends AppCompatActivity {

    private static final String TAG = "Manage_txn";
    //public static Context Conn;
    private DBTerminal dbTerminal;
    private DBTerminal.Terminal_functions1 terminal_fun;
    ImageButton AddTxn;
    int row_cnt;

   // public txn_mange(Context conn1){
      //  Conn= conn1;
    //}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_txn_mange);

        chk_Mtxn();
        if (row_cnt==0) {
            AddManageTransaction();
            Log.d(TAG, "Register  txn");
        }
        else if (row_cnt>0)
        {
            Log.d(TAG, "txn aleardy Registered go Next Activity ");
            startActivity(new Intent(txn_mange.this, MenuActivity.class));
            finish();
        }

    }


    public void chk_Mtxn()
    {
        String selection = null;
        String[] selectionArgs =null;
        Bundle bundle = new Bundle();
        bundle.putString("txn", "txn");

        dbTerminal = new DBTerminal(txn_mange.this);

        DBTerminal.Terminal_functions1 terminal_fun=dbTerminal.new Terminal_functions1();

        row_cnt=terminal_fun.row_countManageTxn(selection,selectionArgs);

        Log.d(TAG,"Check Txn method user cnt : " + row_cnt);

    }
    public void AddManageTransaction() {
        AddTxn = (ImageButton) findViewById(R.id.add_txn);
        AddTxn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //DBTerminal dbTerminal=new DBTerminal(Conn);
               // DBTerminal dbTerminal = new DBTerminal(Conn);
                String[] Transaction_Type = {"ManualCard", "Preauth", "Preauthcmp", "Purchase", "Magstrip", "Refund", "Settlment", "Deposit"};
                String TABLE_NAME_TRANSACTION = "transactions";
                String ID_COL = "id";
                String TRANSACTIONTYPE_COL = "Transaction_Type";
                String STATUS_COL = "Status";


                dbTerminal.query = "CREATE TABLE " + TABLE_NAME_TRANSACTION + " ("

                        + ID_COL + " INTEGER,"

                        + TRANSACTIONTYPE_COL + " TEXT PRIMARY KEY,"

                        + STATUS_COL + " TEXT)";

                dbTerminal.TABLE_NAME_TRANSACTION = "transactions";
                Log.d(TAG, "Length of Transaction_Type  " + Transaction_Type.length);

                for (int i = 0; i < Transaction_Type.length; i++)
                {
                    DBTerminal.Terminal_functions1 terminal_fun = dbTerminal.new Terminal_functions1();
                    terminal_fun.addtransaction(Transaction_Type[i]);

                    Log.d(TAG, "Successfully Inserted  " + Transaction_Type[i]);
                }
                Log.d(TAG, "Finished txn Created  ");

                onBackPressed();

            }
        });
    }
    @Override
    public void onBackPressed() {

        // Log.d(TAG, " Merchant name onBackPressed Method  Back");
        //if(row_cnt==4) {
        // chk_MNMAE();
        //}
        //else if (row_cnt>4) {
        Log.d(TAG, "onBackPressed method txn aleardy Registered go next screen");
        startActivity(new Intent(txn_mange.this, MenuActivity.class));
        finish();
        // }
        //else
        // finish();
    }
}