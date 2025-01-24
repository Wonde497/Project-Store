package com.verifone.demo.emv.transaction;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.verifone.demo.emv.DBHandler;
import com.verifone.demo.emv.MenuActivity;
import com.verifone.demo.emv.Public_data.ISO8583msg;
import com.verifone.demo.emv.Transactiondata;
import com.verifone.demo.emv.Utilities.ActivityCollector;
import com.verifone.demo.emv.Utilities.ToastUtil;
import com.verifone.demo.emv.Utilities.Utility;
import com.verifone.demo.emv.basic.BaseActivity;
import com.verifone.demo.emv.connection;
import com.verifone.demo.emv.loadingfragment;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class CheckCardActivity extends BaseActivity implements View.OnClickListener/*, connection */{
    static String TAG="CHECKCARD";
    private EditText cardNoEditText;
    private TextView amountTextView;
    private TextView titleTextView;
    private TextView readcard_prompt;
    private TextView readamount_prompt,purchase_amount_tv1;
    private Button button_ok;
    private boolean supportMag = true;
    private boolean supportIC = true;
    private boolean supportRF = true;
    private boolean supportHand = true;
    private boolean isEMVProcess = false;
    private boolean isHandInput = false;
    private String pan;
    private boolean checkFinished = false;
    private boolean isFallback = false;
    private int requestCodeAmt = 99;
    private boolean isSoftInput = false;
    private static final int UPDATE_PAN_VIEW = 1;
    private TransBasic transBasic;
    SharedPreferences sharedPreferences;
    FragmentManager manager;
    FragmentTransaction transaction;
    Fragment sample;
    connection loading;
    public static Context context;
    Transactiondata transactiondata;
    String  Txn_type="",Txn_Menu_Type="",utype="";
    private ISO8583msg sp;
    private Dialog customDialog;
    DBHandler dbHandler;
    TextView title;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_card);
        ActivityCollector.addActivity(this);
        context=this;
        TransBasic.checkCardActivity=this;

        sp = new ISO8583msg(CheckCardActivity.this);
        sp.loadCurrencyData();

//....................................Accept supervisor Usertype from the user login..........................................................
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {

            utype = getIntent().getExtras().getString("utype");
            Log.d(TAG, "User type is     : "+utype);

        }
        //...........................
       sharedPreferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
        Txn_type = sharedPreferences.getString("txn_type", "");
        Txn_Menu_Type = sharedPreferences.getString("Txn_Menu_Type", "");
        Log.d(TAG, "Txn_Menu_Type     : "+Txn_Menu_Type);
        Log.d(TAG, "Txn_type     : "+Txn_type);
         initView();

        if (Txn_type.equals("BALANCE_INQUIRY"))
        {
            transBasic = TransBasic.getInstance(sharedPreferences);
            transBasic.doBalance();
            transBasic.setOnGetCardNoListener(new TransBasic.OnGetCardNoListener() {
                @Override
                public void onGetCardNo(String pan) {
                    showCardNo(pan);
                }
            });
        }
        else {
            transBasic = TransBasic.getInstance(sharedPreferences);
            transBasic.doPurchase();
            //transBasic.doBalance();
            transBasic.setOnGetCardNoListener(new TransBasic.OnGetCardNoListener() {
                @Override
                public void onGetCardNo(String pan) {
                    showCardNo(pan);
                }
            });

        }


        transactiondata=new Transactiondata(sharedPreferences,getApplicationContext());
        dbHandler = new DBHandler(getApplicationContext());

        title=findViewById(R.id.trans_name);
        if (Txn_type.equals("PURCHASE"))
        {
            title.setText("SALE");
        }else
        {
            title.setText(Txn_type);
        }
        transaction = getSupportFragmentManager().beginTransaction();;
        sample = new loadingfragment();

    }
    public void assigncon(connection con){
        loading=con;
    }
    private void initView() {
        findViewById(R.id.read_card_setup_bar).setBackgroundColor(0xffffff);
        findViewById(R.id.back_home).setVisibility(View.INVISIBLE);

        cardNoEditText = (EditText) findViewById(R.id.read_cardno);

        cardNoEditText.setLongClickable(false);
        amountTextView = (TextView) findViewById(R.id.purchase_amount_tv);
        readamount_prompt= (TextView) findViewById(R.id.readcard_amttitle_prompt);

        button_ok = (Button) findViewById(R.id.button_ok);
        button_ok.setOnClickListener(this);
        readcard_prompt = (TextView) findViewById(R.id.readcard_prompt);


        //Show amount stored at InputAmount phase
        String amount = TransactionParams.getInstance().getTransactionAmount();

        if (Txn_type.equals("BALANCE_INQUIRY"))
        {
            String amount22="0";
            Log.d(TAG, "BALANCE_INQUIRY............. ");
            readamount_prompt.setText(" ");
            amountTextView.setText(ISO8583msg.r_currency1 + "            " );

        }
        else if (amount == null || amount.isEmpty())
        {
            amountTextView.setTextSize(1);
            findViewById(R.id.readcard_amttitle_prompt).setVisibility(View.GONE);
            findViewById(R.id.purchase_amount_tv).setVisibility(View.GONE);
            Log.d("checkcard", " amountttt1: "+ReversalFristActivity.amount);
        }

        else {
            amountTextView.setTextSize(21);
            if(Txn_type.equals("REVERSAL"))
            {

                    amountTextView.setText(ISO8583msg.r_currency1 + Utility.getReadableAmount(ReversalActivity.user.getField_04()));
                    Log.d("checkcard", " amountttt: " + ReversalActivity.user.getField_04());


            } else if (TransBasic.Txn_type.equals("BALANCE_INQUIRY")) {
                amountTextView.setVisibility(View.GONE);
            } else{
                amountTextView.setText(ISO8583msg.r_currency1 + Utility.getReadableAmount(amount));
                Log.d("checkcard", " amountttt2: "+ReversalFristActivity.amount);

            }
        }

        cardNoEditText.addTextChangedListener(panWatcher);
        findViewById(R.id.button_ok).setOnClickListener(this);

        //屏蔽一些双击的selection的选择mode
        ActionMode.Callback callback = new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
            }
        };
        cardNoEditText.setCustomSelectionActionModeCallback(callback);
    }

    private TextWatcher panWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s == null || s.length() <= 0) {
                cardNoEditText.setTextSize(18);
            } else {
                cardNoEditText.setTextSize(21);
            }
            if (s == null || (s.length() < 12 && isHandInput == true)) {
                isHandInput = false;
                button_ok.setBackgroundColor(Color.parseColor("#f09c9c"));
                //fee_amount.setText("");
            }
            if (s != null && s.length() == 12) {
                isHandInput = true;
                button_ok.setBackgroundColor(CheckCardActivity.this.getResources().getColor(R.color.dashencolour));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    };

    private boolean checkCardNo()
    {
        if (cardNoEditText.getText() == null || cardNoEditText.getText().toString().isEmpty())
        {
            if (isEMVProcess)
            {
                ToastUtil.toast("Processing, please wait");
            } else
            {
                ToastUtil.toast("Please use your card");
            }
            return false;
        }
        pan = cardNoEditText.getText().toString();
        if (pan.length() < 12 || pan.length() > 23) {
            ToastUtil.toast("Card No. incorrect, do double check");
            return false;
        }
        return true;
    }

//.....................................Show Card No.....................................................
    public void showCardNo(final String cardNo) {
        runOnUiThread(new Runnable() {
            public void run() {
                cardNoEditText.setInputType(InputType.TYPE_NULL);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(CheckCardActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                cardNoEditText.setTextColor(CheckCardActivity.this.getResources().getColor(R.color.globalyellow));
                cardNoEditText.setTextSize(22);
                      Log.d("TAG", "------>");

                Log.d("FixedCardNo",Utility.fixCardNoWithMask(cardNo));
                cardNoEditText.setText(Utility.fixCardNoWithMask(cardNo));
//                refreshHint(getResources().getString(R.string.recheckcardno));
                button_ok.setBackground(CheckCardActivity.this.getResources().getDrawable(R.drawable.yellowcorner_background));
                int color = getResources().getColor(R.color.blackgreen2);
                button_ok.setTextColor(color);

            }
        });
    }

    @Override
    public void onBackPressed() {
        showCustomDialog();

    }

    //    @Override
//    public void onFragmentItemClick(Fragment fragment) {
//       if (fragment instanceof ChooseDialogFragment) {
//           int appIndex = ((ChooseDialogFragment) fragment).getItemIndex() + 1;
//           importAppSelect(appIndex);
//       }
//    }

    @Override
 public void onClick(View v)
    {
    switch (v.getId())
    {
      case R.id.button_ok:

        if(Txn_type.equals("REVERSAL")) {

            Log.d("CheckCardActivity  ", "Reversal is selected");

            if (ReversalActivity.user.getField_02().equals(TransBasic.savedPan)) {
                Log.d(TAG, "Savedpannumber  " + ReversalActivity.user.getField_02());
                Log.d(TAG, "SavedTxn_type  " + ReversalActivity.user.getTxn_type());
                if (ReversalActivity.user.getTxn_type().equals("PURCHASE") || ReversalActivity.user.getTxn_type().equals("CASH_ADVANCE")) {


                    if (ReversalActivity.user.getLastflag().equals("0")) {

                        ToastUtil.toastOnUiThread(CheckCardActivity.this, "REVERSAL APPROVAL IN PROGRESS...");
                        Log.d("CheckCardActivity  ", "REVERSAL APPROVAL IN PROGRESS... Finshied, Card Number and txn Checking For Approval");
                        startActivity(new Intent(CheckCardActivity.this, Supervisorlogin.class));
                        finish();


                 } else {
                        Log.d("Flage! ", " Transaction is not success");
                        ToastUtil.toastOnUiThread(CheckCardActivity.this, " TXN ABORTED " + ReversalActivity.user.getLastflag());

                        //startActivity(new Intent(CheckCardActivity.this, MenuActivity.class));
                        finish();
                    }

                } else {
                    Log.d("check card ABORTED! ", "TXN CAN NOT BE REVERSED ");
                    ToastUtil.toastOnUiThread(CheckCardActivity.this, "TRANSACTION CAN NOT Be REVERSED ");
                    // startActivity(new Intent(CheckCardActivity.this, MenuActivity.class));
                    finish();
                }
            } else {
                Log.d("CARD ISSUE! ", "INCORRECT CARD INSERTED!, TRY AGAIN ");
                ToastUtil.toastOnUiThread(CheckCardActivity.this, "CARD ISSUE!, INCORRECT CARD INSERTED!, TRY AGAIN  ");
                //startActivity(new Intent(CheckCardActivity.this, MenuActivity.class));
                finish();
            }


        }

       else if(Txn_type.equals("REFUND"))
          {
              Log.d("CheckCardActivity  ","REFUND IS SELECTED");


                   ToastUtil.toastOnUiThread(CheckCardActivity.this, "REFUND APPROVAL IN PROGRESS...");
                    Log.d("CheckCardActivity  ", "APPROVAL IN PROGRESS...");

                    transBasic.importCardConfirmResult();
                    transBasic.pinInputHandler = pinInputHandler;

          }

        else if(Txn_type.equals("PRE_AUTH_COMPLETION"))
        {
            Log.d("CheckCardActivity  ","PRE_AUTH_COMPLETION IS SELECTED");


                        ToastUtil.toastOnUiThread(CheckCardActivity.this, "PRE_AUTH_COMPLETION APPROVAL IN PROGRESS...");
                        Log.d("CheckCardActivity  ", "PRE_AUTH_COMPLETION APPROVAL IN PROGRESS...");

                        transBasic.importCardConfirmResult();
                        transBasic.pinInputHandler = pinInputHandler;

        }
         else if(sharedPreferences.getString("printtype", "").equals("isphoneauth"))
          {
              Log.d("CheckCardActivity  ","isphoneauth is selected");
              startActivity(new Intent(CheckCardActivity.this, Pre_Auth_Completion.class));
              finish();
          }
        else//Other Transaction selected
             {
                     Log.d("CheckCardActivity  ","Normal TXN is selected");

                     transBasic.importCardConfirmResult();
                     Log.d("CheckCardActivity  ","CardConfirmResult() pass...........");
                     transBasic.pinInputHandler = pinInputHandler;

                 Log.d("CheckCardActivity  ","pinInputHandler pass.............");

                 }
//                startActivity(new Intent(CheckCardActivity.this, InputPinActivity.class));
          Log.d("CheckCardActivity  ","finishhhh.............");
        finish();

    }
//        switch (v.getId()) {
//            case R.id.button_ok:
//                Log.i(AppConstant.TAG, "Card number confirmed okbutton clicked...");
//                if (checkCardNo()) {
//                    if (isHandInput) {
//                        Log.i(AppConstant.TAG, "Input card number manually...");
//                        abortPBOC();
//                        stopCheckCard();
//                        setValue(TXNREC.PAN, Utils.trimChar(pan, 'F'));
//                        setValue(TXNREC.MODE, "012");
//                        finish(RESULT_OK);
//                    } else if (isEMVProcess) {
//                        Log.i(AppConstant.TAG, "IC card、contactless card confirming card number...");
//                        if (getPBOCStep() == PBOCStep.ConfirmCardInfo) {
//                            importCardConfirmResult(true);
//                        }
//                        Log.d("TAG", "------>");
//                        finish(RESULT_OK);
//                        Log.d("TAG", "<------");
//                    } else {
//                        finish(RESULT_OK);
//                    }
//                }
//                break;
//        }
    }

    @SuppressLint("HandlerLeak")
    Handler pinInputHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            startActivity(new Intent(CheckCardActivity.this, InputPinActivity.class));
            finish();
        }
    };

public void progress(){
    Toast.makeText(this,"no clipboard", Toast.LENGTH_SHORT).show();

}


    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog, null);
        builder.setView(dialogView);

        // Initialize the TextView and Buttons
        TextView title = dialogView.findViewById(R.id.messageTextView);
        Button positiveButton = dialogView.findViewById(R.id.positiveButton);
        Button negativeButton = dialogView.findViewById(R.id.negativeButton);
        title.setText(" Are you sure you want to cancel the transaction?");
        title.setTypeface(null, Typeface.BOLD_ITALIC); // Text Style



        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transBasic.abortEMV(); //abortEMV
                Intent intent = new Intent(CheckCardActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
                customDialog.dismiss(); // Dismiss the dialog
            }
        });

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog if user clicks No
                customDialog.dismiss();

            }
        });

        // Create and show the dialog
        customDialog = builder.create();
        customDialog.show();
    }

  /*@Override
    public void toast() {
   //Toast.makeText(this,"no clipboard", Toast.LENGTH_SHORT).show();

      /*  transaction.replace(R.id.progdial2, sample);
        transaction.addToBackStack(null);
        transaction.commit();*/
       ;
       /* Intent i = new Intent(this, loadingactivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public void finish() {
        Log.d("checkactiv", "finishcalled :" );

        commonvariable co = new commonvariable();
        co.getConnection().finish();

    }

    @Override
    public void back() {
        Log.d("checkactiv", "backcalled :" );
        commonvariable co = new commonvariable();
        loading = co.getConnection();
        loading.finish();
    }*/
}
