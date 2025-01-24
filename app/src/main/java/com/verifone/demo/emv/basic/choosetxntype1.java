package com.verifone.demo.emv.basic;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.verifone.demo.emv.DBTerminal;
import com.verifone.demo.emv.data_handlers.terminal;

import java.util.ArrayList;
import java.util.List;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class choosetxntype1 extends  Dialog implements View.OnClickListener {

    private LinearLayout btndeposit;
    private LinearLayout btnvoid;
    private LinearLayout btnbalance;
    private LinearLayout btncashadv;
    private LinearLayout btnPhone;
    private static String Txn_col,Txn_colManualCard,Txn_colPreauth,Txn_colPreauthcmp,Txn_colPurchase,
            Txn_colBalance,Txn_colRefund,Txn_colPhoneAuth,Txn_colDeposit;
    private static String Sta_col,Sta_colManualCard,Sta_colPreauth,Sta_colPreauthcmp,Sta_colPurchase,
            Sta_colBalance,Sta_colRefund,Sta_colPhoneAuth,Sta_colDeposit;
    public int transaction_type1;

    private static DBTerminal dbTerminal;
    DBTerminal.Terminal_functions1 terminal_fun;
    LinearLayout lin;

    private Context mContext;

    OnChooseListener onChooselistener;

    public void setOnChooselistener(OnChooseListener onChooselistener) {
        this.onChooselistener = onChooselistener;
    }

    public choosetxntype1(Context context) {
        super(context);
        this.mContext = context;
    }

    public choosetxntype1(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public choosetxntype1(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choosetxntype1);
        setCanceledOnTouchOutside(false);
        initView();
        txn_loadData();
    }

    private void initView() {
        // lin = findViewById(R.id.linlay);
        btncashadv= findViewById(R.id.lincashad);
        btncashadv.setOnClickListener(this);

        btnvoid= findViewById(R.id.linvoid);
        btnvoid.setOnClickListener(this);

        btnbalance= findViewById(R.id.linbalance);
        btnbalance.setOnClickListener(this);

        btnPhone= findViewById(R.id.linPhone);
        btnPhone.setOnClickListener(this);

        btndeposit = findViewById(R.id.lindeposit);
        btndeposit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.lincashad:
                if(onChooselistener != null){
                    onChooselistener.onClickcashadvance();
                    Log.d("EMVDemo","Cashadvance");
                }
                this.dismiss();
                break;

            case R.id.linPhone:
                if(onChooselistener != null){
                    onChooselistener.onClickphoneAuth();
                    Log.d("EMVDemo","Due Date");
                }
                this.dismiss();
                break;
            case R.id.linvoid:
                if(onChooselistener != null){
                    onChooselistener.onClickvoid();
                    Log.d("EMVDemo","Reversal");
                }
                this.dismiss();
                break;

            case R.id.linbalance:
                if(onChooselistener != null){
                    onChooselistener.onClickbalance();;
                    Log.d("EMVDemo","BalanceInq");
                }
                this.dismiss();
                break;
            case R.id.lindeposit:
                if(onChooselistener != null){
                    onChooselistener.onClickdeposit();
                    Log.d("EMVDemo","Deposit");
                }
                this.dismiss();
                break;
        }
    }

    public interface OnChooseListener{
        void onClickcashadvance();
        void onClickvoid();
        void onClickbalance();
        void onClickphoneAuth();
        void onClickdeposit();

    }
    public void txn_loadData()
    {
        String selection = "";
        String[] selectionArgs = {};
        Log.d("DBRES", "txn here Started ");
        List<terminal> txninfolist = new ArrayList<terminal>();
        dbTerminal = new DBTerminal(mContext);

        Log.d("DBRES", "txn SPDH here Started 1 ");
        DBTerminal.Terminal_functions1 Terminal_fun=dbTerminal.new Terminal_functions1();
        Log.d("DBRES", "txn here3");
        txninfolist= Terminal_fun.viewtxn(selection,selectionArgs);
        int rows = txninfolist.size();
        transaction_type1=txninfolist.size();
        Log.d("DBRES", "txn here2  " + rows);
        Log.d("DBRES", "txn here for loop");
        for(int i = 0; i < rows; i ++) {
            terminal row = null;
            if (i > -1) {
                row = txninfolist.get(i);
                //txn = txninfolist.get(i);
                Log.d("DBRES", "Transaction_type : " + row.getTransaction_type());
                //Log.d("DBRES", "Preauth : "+  row.getPreauth());
                // Log.d("DBRES", "Preauthcmp : "+  row.getPreauthcmp());
                Log.d("DBRES", "Status : " + row.getStatus());

                Txn_col = row.getTransaction_type();
                Sta_col = row.getStatus();

                //Purchase.................................................

                //  Magstrip..............................................
                if (Txn_col.equals("Balance")) {
                    if (Txn_col.equals("Balance") && Sta_col.equals("1")) {
                        btnbalance.setVisibility(View.VISIBLE);
                        Txn_colBalance=Txn_col;
                        Sta_colBalance=Sta_col;
                    }else{
                        btnbalance.setVisibility(View.GONE);
                        Txn_colBalance=Txn_col;
                        Sta_colBalance=Sta_col;
                    }
                }
                // Refund................................................

                //Settlment..............................................
                if (Txn_col.equals("PhoneAuth") ) {
                    if (Txn_col.equals("PhoneAuth") && Sta_col.equals("1")) {
                        btnPhone.setVisibility(View.VISIBLE);
                        Txn_colPhoneAuth=Txn_col;
                        Sta_colPhoneAuth=Sta_col;
                    }else {
                        btnPhone.setVisibility(View.GONE);
                        Txn_colPhoneAuth=Txn_col;
                        Sta_colPhoneAuth=Sta_col;
                    }
                }

                /// Deposit..............................
                if (Txn_col.equals("Deposit") ) {
                    if (Txn_col.equals("Deposit") && Sta_col.equals("1")) {
                        btndeposit.setVisibility(View.VISIBLE);
                        Txn_colDeposit=Txn_col;
                        Sta_colDeposit=Sta_col;
                        // Log.d(TAG, " Statuse ManualCard is Clicked inside else if =" + Sta_col);
                    }else{
                        btndeposit.setVisibility(View.GONE);
                        Txn_colDeposit=Txn_col;
                        Sta_colDeposit=Sta_col;
                    }
                }

            }
            else
            {
                Log.d("DBRES", "Finished txn: ");
            }
        }
    }

}
