package com.verifone.demo.emv.basic;
/*
 *  author: Derrick
 *  Time: 2019/5/24 17:18
 */

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.verifone.demo.emv.DBTerminal;
import com.verifone.demo.emv.data_handlers.terminal;

import java.util.ArrayList;
import java.util.List;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class choosetxntype extends Dialog implements View.OnClickListener {
    private LinearLayout btndeposit;
    private LinearLayout btnpurchase;
    private LinearLayout btnrefund;
    private LinearLayout btnvoid;
    private LinearLayout btnpreauth;
    private LinearLayout btnbalance;
    private LinearLayout btnpurchcash;
    private LinearLayout btncashadv;
    private LinearLayout btnpreauthcomp;
    private LinearLayout btnmagn;
    private LinearLayout btnmanualcard;
    private LinearLayout btnPhone;
    private static String Txn_col,Txn_colManualCard,Txn_colPreauth,Txn_colPreauthcmp,Txn_colPurchase,
            Txn_colBalance,Txn_colRefund,Txn_colPhoneAuth,Txn_colDeposit;
    private static String Sta_col,Sta_colManualCard,Sta_colPreauth,Sta_colPreauthcmp,Sta_colPurchase,
            Sta_colBalance,Sta_colRefund,Sta_colPhoneAuth,Sta_colDeposit;
    public int transaction_type1;

    private static DBTerminal dbTerminal;
    DBTerminal.Terminal_functions1 terminal_fun;
    LinearLayout lin;
TextView title;
    private Context mContext;
ScrollView scrollView;
    OnChooseListener onChooselistener;

    public void setOnChooselistener(OnChooseListener onChooselistener) {
        this.onChooselistener = onChooselistener;
    }

    public choosetxntype(Context context) {
        super(context);
        this.mContext = context;
    }

    public choosetxntype(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public choosetxntype(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_txn_type);
        setCanceledOnTouchOutside(false);
        SharedPreferences preferences = mContext.getSharedPreferences("Shared_data", Context.MODE_PRIVATE);

        initView();
        txn_loadData();
        if(preferences.getString("txn_type"," ").equals("PHONE_AUTH")){
            title.setText("Choose Phone_auth Type");
            scrollView.setBackgroundColor(R.drawable.slash_background);
            btnPhone.setVisibility(View.GONE);

        }else{
           // btnPhone.setVisibility(View.VISIBLE);

        }
        //btnvoid.setVisibility(View.GONE);
        //lin.setVisibility(View.GONE);
/*        btnpreauth.setVisibility(View.GONE);
        btnpurchase.setVisibility(View.GONE);
        btnrefund.setVisibility(View.GONE);
        btnvoid.setVisibility(View.GONE);*/

    }

    private void initView() {
        // lin = findViewById(R.id.linlay);
        title = findViewById(R.id.title);
        scrollView = findViewById(R.id.scroll);

        btnpurchase = findViewById(R.id.linpurch);
        btnpurchase.setOnClickListener(this);

        btnrefund = findViewById(R.id.linrefund);
        btnrefund.setOnClickListener(this);

        btnvoid= findViewById(R.id.linvoid);
        btnvoid.setOnClickListener(this);

        btnpreauth= findViewById(R.id.linpreauth);
        btnpreauth.setOnClickListener(this);

        btnbalance= findViewById(R.id.linbalance);
        btnbalance.setOnClickListener(this);

        btnpurchcash= findViewById(R.id.linpurchcash);
        btnpurchcash.setOnClickListener(this);

        btnpreauthcomp= findViewById(R.id.linpreauthcomp);
        btnpreauthcomp.setOnClickListener(this);

        btnPhone= findViewById(R.id.linPhone);
        btnPhone.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linpurch:
                if(onChooselistener != null){
                    onChooselistener.onClickpurchase();
                    Log.d("EMVDemo","TransBasic init");
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
            case R.id.linpreauth:
                if(onChooselistener != null){
                    onChooselistener.onClickpreauth();
                    Log.d("EMVDemo","preauth");
                }
                this.dismiss();
                break;
            case R.id.linpreauthcomp:
                if(onChooselistener != null){
                    onChooselistener.onClickpreauthcomp();
                    Log.d("EMVDemo","preauth_Completion");
                }
                this.dismiss();
                break;
            case R.id.linrefund:
                if(onChooselistener != null){
                    onChooselistener.onClickrefund();
                    Log.d("EMVDemo","Refund");
                }
                this.dismiss();
                break;

            case R.id.linpurchcash:
                if(onChooselistener != null){
                    onChooselistener.onClickperchasecash();
                    Log.d("EMVDemo","perchasecashBack");
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
        }
    }

    public interface OnChooseListener{
        void onClickpurchase();
        void onClickrefund();
        void onClickvoid();
        void onClickbalance();
        void onClickpreauth();
        void onClickperchasecash();
        void onClickpreauthcomp();
        void onClickphoneAuth();


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

/*ManualCard...............................
                if (Txn_col.equals("ManualCard")) {
                    if (Txn_col.equals("ManualCard") && Sta_col.equals("1"))
                    {
                        btnmanualcard.setVisibility(View.VISIBLE);
                        Txn_colManualCard = Txn_col;
                        Sta_colManualCard = Sta_col;
                        //manualcardentry.setImageResource(R.drawable.manualcardentryen);

                    } else {
                        btnmanualcard.setVisibility(View.GONE);
                        Txn_colManualCard = Txn_col;
                        Sta_colManualCard= Sta_col;
                        //manualcardentry.setImageResource(R.drawable.manualcardentrydis);

                    }
                }

 */
//Preauth..................................................
                if (Txn_col.equals("Preauth")) {
                    if(Txn_col.equals("Preauth")  && Sta_col.equals("1")) {
                        btnpreauth.setVisibility(View.VISIBLE);
                        Txn_colPreauth=Txn_col;
                        Sta_colPreauth=Sta_col;

                    }else{
                        btnpreauth.setVisibility(View.GONE);
                        Txn_colPreauth=Txn_col;
                        Sta_colPreauth=Sta_col;
                    }
                }
                //Preauthcmp  .................................
                if (Txn_col.equals("Preauthcmp")) {
                    if (Txn_col.equals("Preauthcmp") && Sta_col.equals("1")) {
                        btnpreauthcomp.setVisibility(View.VISIBLE);
                        Txn_colPreauthcmp=Txn_col;
                        Sta_colPreauthcmp=Sta_col;
                    }else {
                        btnpreauthcomp.setVisibility(View.GONE);
                        Txn_colPreauthcmp = Txn_col;
                        Sta_colPreauthcmp = Sta_col;
                    }
                }
                //Purchase.................................................
                if (Txn_col.equals("Purchase")) {
                    if (Txn_col.equals("Purchase") && Sta_col.equals("1")) {
                        btnpurchcash.setVisibility(View.VISIBLE);
                        Txn_colPurchase=Txn_col;
                        Sta_colPurchase=Sta_col;

                    }else{
                        btnpurchcash.setVisibility(View.GONE);
                        Txn_colPurchase=Txn_col;
                        Sta_colPurchase=Sta_col;
                    }
                }
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
                if (Txn_col.equals("Refund")) {
                    if (Txn_col.equals("Refund") && Sta_col.equals("1")) {
                        btnrefund.setVisibility(View.VISIBLE);
                        Txn_colRefund=Txn_col;
                        Sta_colRefund=Sta_col;
                    }else {
                        btnrefund.setVisibility(View.GONE);
                        Txn_colRefund=Txn_col;
                        Sta_colRefund=Sta_col;
                    }
                }
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
            }
            else
            {
                Log.d("DBRES", "Finished txn: ");
            }
        }
    }

}
