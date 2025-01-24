package com.verifone.demo.emv;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.verifone.demo.emv.Help.HelpMain;
import com.verifone.demo.emv.Public_data.ISO8583msg;
import com.verifone.demo.emv.Utilities.SPHelper;
import com.verifone.demo.emv.basic.BaseActivity;
import com.verifone.demo.emv.basic.ISO8583;
import com.verifone.demo.emv.basic.choosetxntype;
import com.verifone.demo.emv.basic.choosetxntype1;
import com.verifone.demo.emv.data_handlers.User;
import com.verifone.demo.emv.transaction.AppParams;
import com.verifone.demo.emv.transaction.CheckCardActivity;
import com.verifone.demo.emv.transaction.InputAmountActivity;
import com.verifone.demo.emv.transaction.InputSequenceNumber;
import com.verifone.demo.emv.transaction.Pre_Auth_Completion;
import com.verifone.demo.emv.transaction.ReversalFristActivity;
import com.verifone.demo.emv.transaction.TransBasic;
import com.vfi.smartpos.deviceservice.aidl.IDeviceInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class MenuActivity extends BaseActivity implements View.OnClickListener {
    public static  ArrayList<User> arrayList = new ArrayList<>();
    private ViewPager advViewPaper;
    private List<ImageView> advImages;
    private List<View> advDotsView;
    private int advPosition;
    private String cashstatuse;
    private int advLastPosition = 0;

    private int[] advImageIdList = new int[]{R.drawable.amharic_gbe_logo21,R.drawable.globalcard, R.drawable.globalliyucard, R.drawable.globalhakikacard, R.drawable.amharic_gbe_logo21};
    private AdvViewPagerAdapter advAdapter;
    private ScheduledExecutorService scheduledExecutorService;
    private static final String TAG = "EMVDemo";
    private IDeviceInfo iDeviceInfo;
    private DBTerminal dbTerminal;
    public static String txn_type1="";
    public static String Version_Number="X990-03-07-23";
    LinearLayout qpay, helplinearlayout;
    LinearLayout login;
    LinearLayout setting;
    LinearLayout cahierlogin;
    TextView qpayCover, helpCover;
    TextView logonCover;
    TextView settingCover;
    TextView setCover;
    TransBasic transBasic;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private DBHandler dbHandler;
    private DBHandler.user_functions user_fun;
    int row_cnt,row_cnt1;
    public static Context context;
    private ISO8583msg sp;
    Transactiondata transactiondata;
    TextClock datetime;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        //chk_admin();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        datetime = findViewById(R.id.dateandtime);
        Log.d(TAG, "Wellcome to SSC MenuActivity Started");
        preferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
         editor = preferences.edit();
        editor.putString("Txn_Menu_Type","");
        editor.putString("txn_type", "");
        editor.putString("printtype", " ");
        editor.putString("isphoneauth", "");
        dbHandler = new DBHandler(MenuActivity.this);

        editor.commit();
        //TransBasic.tvr="";
        //ClearAllFiledData();
        sp = new ISO8583msg(MenuActivity.this);
        sp.loadTmodeData();

        initBannerView();
        initMenuView();
        ClearAllFiledData();
        judgeFirstLaunchSignIn();
        initAppParam();

        context=this;

        chk_Mtxn();
        if (row_cnt1==0){
            AddManageTransaction();
        }

        //add cashire statuse
        chk_cashirststus();
        if (row_cnt==0){
            cashierstatus();
        }

    }
    private void initAppParam() {
        AppParams.getInstance().setOffline(SPHelper.getInstance().getBoolean("offline", true));
        AppParams.getInstance().setShowScanBorder(SPHelper.getInstance().getBoolean("showScanBorder", true));
        AppParams.getInstance().setSupportSwipe(SPHelper.getInstance().getBoolean("supportSwipe", true));
        AppParams.getInstance().setSupportTap(SPHelper.getInstance().getBoolean("supportTap", true));
        AppParams.getInstance().setSupportInsert(SPHelper.getInstance().getBoolean("supportInsert", true));
    }



    public void chk_Mtxn()
    {
        String selection = null;
        String[] selectionArgs =null;
        Bundle bundle = new Bundle();
        bundle.putString("txn", "txn");

        dbTerminal = new DBTerminal(MenuActivity.this);
        //dbHandler = new DBHandler(MenuActivity.this);
        DBTerminal.Terminal_functions1 terminal_fun=dbTerminal.new Terminal_functions1();

        row_cnt1=terminal_fun.row_countManageTxn(selection,selectionArgs);

        Log.d(TAG,"Check Txn method user cnt : " + row_cnt1);

    }

    public void chk_cashirststus()
    {
        String selection = null;
        String[] selectionArgs =null;


        Bundle bundle = new Bundle();
        bundle.putString("user_type", "Cashier");

        dbHandler = new DBHandler(MenuActivity.this);

        DBHandler.user_functions user_fun=dbHandler.new user_functions();

        row_cnt=user_fun.Cshierstat_count(selection,selectionArgs);

        Log.d(TAG,"Check cashierstatus method DB cnt:  " + row_cnt);

    }

    public void user_loadData()
    {

        String selection = "";
        String[] selectionArgs = {};
        Log.d("DBRES", "here 1");
        List<User> cashierList = new ArrayList<User>();
        dbHandler = new DBHandler(MenuActivity.this);
        DBHandler.user_functions user_fun=dbHandler.new user_functions();
        //DBTerminal.Terminal_functions1 Terminal_fun=dbTerminal.new Terminal_functions1();
        Log.d("DBRES", "here 3");
        cashierList= user_fun.Cashierstatview(selection,selectionArgs);
        int rows = cashierList.size();
        Log.d("DBRES", "Total Size  " + rows);
        // user u_list;

        Log.d("DBRES", "here");
        for(int i = 0; i < rows; i ++) {
            User row = null;
            if (i ==0) {
                row = cashierList.get(i);
                Log.d("DBRES", "Cashierstatus : " + row.getCashierstatus());

                cashstatuse=row.getCashierstatus();
                //Sta_col=row.getStatus();


            }
        }}
    public void AddManageTransaction() {

        String[] Transaction_Type = {"ManualCard", "Preauth", "Preauthcmp", "Purchase", "Balance", "Refund", "PhoneAuth", "Deposit"};
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


    }

    private void cashierstatus()
    {
        String TABLE_NAME_CASHSTAT = "CASHIER_STATUS";
        String ID_COL = "id";
        String CASHIERSTAT_COL = "cashierstatus";
        String cashierstatus1="1";
        dbHandler = new DBHandler(MenuActivity.this);
        dbHandler.query= "CREATE TABLE"+TABLE_NAME_CASHSTAT + " ("

                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT,"

                + CASHIERSTAT_COL + " TEXT )";

        dbHandler.TABLE_NAME_CASHSTAT="CASHIER_STATUS";

        DBHandler.user_functions user_fun=dbHandler.new user_functions();
        user_fun.Cashierstat(cashierstatus1);
        Log.d(TAG, "Successfully Add  Cashierstatus  ");
        //Toast.makeText(reg_frag.getContext(), "User has been Registered.", Toast.LENGTH_SHORT).show();

    }

    private void initMenuView()
    {

        qpay = (LinearLayout) findViewById(R.id.qpay);

        qpayCover = (TextView) findViewById(R.id.qpay_cover);
        helpCover = (TextView) findViewById(R.id.help_cover);
        logonCover = (TextView) findViewById(R.id.logon_cover);
        settingCover = (TextView) findViewById(R.id.setting_cover);
        setCover = (TextView) findViewById(R.id.set_cover);
        qpay.setOnClickListener(this);
        qpay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    qpayCover.setVisibility(View.VISIBLE);
                } else if (action == MotionEvent.ACTION_UP) {
                    qpayCover.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });

        login = (LinearLayout) findViewById(R.id.logon);
        login.setOnClickListener(this);
        login.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    logonCover.setVisibility(View.VISIBLE);
                } else if (action == MotionEvent.ACTION_UP) {
                    logonCover.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
        setting = (LinearLayout) findViewById(R.id.setting);
        setting.setOnClickListener(this);
        setting.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    settingCover.setVisibility(View.VISIBLE);
                } else if (action == MotionEvent.ACTION_UP) {
                    settingCover.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
        cahierlogin = (LinearLayout) findViewById(R.id.cahierlogin);
        cahierlogin.setOnClickListener(this);
        cahierlogin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    setCover.setVisibility(View.VISIBLE);
                } else if (action == MotionEvent.ACTION_UP) {
                    setCover.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });

//............................Added for Help Onclick Listener...........................................
        helplinearlayout = (LinearLayout)  findViewById(R.id.menuhelp1);
        helplinearlayout.setOnClickListener(this);
        helplinearlayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    helpCover.setVisibility(View.VISIBLE);
                } else if (action == MotionEvent.ACTION_UP) {
                    helpCover.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
    }

    //Judge if it's the first launch, if so, do load aid & rid & keys and default configuration of app automatically
    private void judgeFirstLaunchSignIn() {
        boolean isFirstLaunch = SPHelper.getInstance().getBoolean("isFirstLaunch", true);
        if (isFirstLaunch){
            doFirstLaunchInit();
        }
    }

    private void doFirstLaunchInit() {
        SPHelper.getInstance().putBoolean("isFirstLaunch", false);
        startActivity(new Intent(MenuActivity.this, LoginActivity.class));

        Map<String, Boolean> defaultConfig = new HashMap<String, Boolean>();
        defaultConfig.put("offline", true);
        defaultConfig.put("showScanBorder", true);
        defaultConfig.put("supportTap", true);
        defaultConfig.put("supportInsert", true);
        defaultConfig.put("supportSwipe", true);
        SPHelper.getInstance().putBooleans(defaultConfig);
    }

    private void initBannerView() {
        //广告的显示
        advImages = new ArrayList<ImageView>();
        for (int i = 0; i < advImageIdList.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(advImageIdList[i]);
            advImages.add(imageView);
        }
        advDotsView = new ArrayList<View>();
        advDotsView.add(findViewById(R.id.dot_0));
        advDotsView.add(findViewById(R.id.dot_1));
        advDotsView.add(findViewById(R.id.dot_2));
        advDotsView.add(findViewById(R.id.dot_3));
        advDotsView.add(findViewById(R.id.dot_4));

        advAdapter = new AdvViewPagerAdapter();
        advViewPaper = (ViewPager) findViewById(R.id.advertisement);
        advViewPaper.setAdapter(advAdapter);
        advViewPaper.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                advDotsView.get(position).setBackgroundResource(R.drawable.dot_focused);
                advDotsView.get(advLastPosition).setBackgroundResource(R.drawable.dot_normal);

                advLastPosition = position;
                advPosition = position;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(new AdvViewPageTask(), 4, 3, TimeUnit.SECONDS);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdown();
            scheduledExecutorService = null;
        }
        //finish();
    }
     @Override
    protected void onResume() {
        super.onResume();

//        VFIApplication application = (VFIApplication) getApplication();
//        application.rebindService();
        Log.d(TAG, "TransBasic init");


    }




    /*@Override
    protected void onStart() {
        super.onStart();
        Log.d("lifecycle","onStart invoked");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lifecycle","onResume invoked");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("lifecycle","onPause invoked");

    }
    @Override
    protected void onStop() {
        super.onStop();
        finish();
        Log.d("lifecycle","onStop invoked");
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("lifecycle","onRestart invoked");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("lifecycle","onDestroy invoked");
    }*/



    @Override
    public void onClick(View v) {
     switch (v.getId())
      {

            case R.id.qpay:
                TransBasic.tvr="";
                ClearAllFiledData();

                transactiondata = new Transactiondata(preferences, context);
                dbHandler = new DBHandler(MenuActivity.this);
                if (dbHandler.gettransactiondata().size() >= 100)
                {
                    Log.d(TAG, "TRANSACTION ABOVE 100");

                    //Toast.makeText(getApplicationContext(), "Hello World!", Toast.LENGTH_LONG).show();

                    Toast.makeText(MenuActivity.this, "PLEASE DO END_OFF_DAY!...", Toast.LENGTH_LONG).show();

                }

                Log.d("Menu Activity ","Tmode Type............          "+ ISO8583msg.mode);
                editor.putString("printtype", " ");
                editor.putString("Txn_Menu_Type","");
                editor.putString("txn_type", "");
                editor.putString("isphoneauth", "");
                editor.apply();
                if(ISO8583msg.mode.equals("Merchant")) {

                   // VFIApplication.maskHomeKey(false);
                   //startActivity(new Intent(MenuActivity.this, InputAmountActivity.class));

                   Log.d("EMVDemo", "MERCHANT MODE IS SELECTED.................");
                   //Set a choose payment type dialog
                     final choosetxntype dialog = new choosetxntype(MenuActivity.this);
                   dialog.setOnChooselistener(new choosetxntype.OnChooseListener() {
                       @Override
                       public void onClickpurchase() {
                             editor.putString("Txn_Menu_Type","Chip || Magstrip || Contactless");
                           editor.putString("txn_type", "PURCHASE");
                           editor.apply();
                           txn_type1 = "PURCHASE";
                           Log.d("EMVDemo", "PURCHASE");
                           Log.d("EMVDemo", "txn_type1  " + txn_type1);
                           VFIApplication.maskHomeKey(false);
                           startActivity(new Intent(MenuActivity.this, InputAmountActivity.class));
                           dialog.dismiss();
                       }

                       @Override
                       public void onClickrefund() {
                           editor.putString("Txn_Menu_Type","Chip || Magstrip || Contactless");
                           editor.putString("txn_type", "REFUND");
                           editor.apply();
                           txn_type1 = "REFUND";
                           Log.d("EMVDemo", "REFUND");
                           VFIApplication.maskHomeKey(false);
                           startActivity(new Intent(MenuActivity.this, Pre_Auth_Completion.class));
                           dialog.dismiss();
                       }

                       @Override
                       public void onClickvoid() {
                           editor.putString("Txn_Menu_Type","Chip || Magstrip || Contactless");
                           editor.putString("txn_type", "REVERSAL");
                           editor.apply();
                           txn_type1 = "REVERSAL";
                           Log.d("EMVDemo", "REVERSAL");
                           VFIApplication.maskHomeKey(false);
                           startActivity(new Intent(MenuActivity.this, ReversalFristActivity.class));
                           dialog.dismiss();
                       }

                       @Override
                       public void onClickbalance() {
                           editor.putString("Txn_Menu_Type","Chip || Magstrip || Contactless");
                           editor.putString("txn_type", "BALANCE_INQUIRY");
                           editor.apply();
                           txn_type1 = "BALANCE_INQUIRY";
                           Log.d("EMVDemo", "BALANCE_INQUIRY");
                           VFIApplication.maskHomeKey(false);
                           startActivity(new Intent(MenuActivity.this, CheckCardActivity.class));
                           dialog.dismiss();
                       }

                       @Override
                       public void onClickpreauth() {
                           editor.putString("Txn_Menu_Type","Chip || Magstrip || Contactless");
                           editor.putString("txn_type", "PRE_AUTH");
                           editor.apply();
                           txn_type1 = "PRE-AUTH";
                           Log.d("EMVDemo", "PRE_AUTH");
                           VFIApplication.maskHomeKey(false);
                           startActivity(new Intent(MenuActivity.this, InputAmountActivity.class));
                           dialog.dismiss();
                       }
                       @Override
                       public void onClickpreauthcomp() {
                           editor.putString("Txn_Menu_Type","Chip || Magstrip || Contactless");
                           editor.putString("txn_type", "PRE_AUTH_COMPLETION");
                           editor.apply();
                           txn_type1 = "PRE_AUTH_COMPLETION";
                           Log.d("EMVDemo", "PRE_AUTH_COMPLETION");
                           VFIApplication.maskHomeKey(false);
                           startActivity(new Intent(MenuActivity.this, InputSequenceNumber.class));
                           dialog.dismiss();

                       }
                       @Override
                       public void onClickperchasecash() {
                           editor.putString("Txn_Menu_Type","Chip || Magstrip || Contactless");
                           editor.putString("txn_type", "PURCHASE_WITH_CASHBACK");
                           editor.apply();
                           txn_type1 = "PURCHASE_WITH_CASHBACK";
                           Log.d("EMVDemo", "PURCHASE_WITH_CASHBACK");
                           VFIApplication.maskHomeKey(false);
                           //startActivity(new Intent(MenuActivity.this, InputAmountActivity.class));
                           dialog.dismiss();
                       }

                       @Override
                       public void onClickphoneAuth() {
                            editor.putString("Txn_Menu_Type","Chip || Magstrip || Contactless");
                           editor.putString("txn_type", "PHONE_AUTH");
                           editor.apply();
                           txn_type1 = "PHONEAUTH";
                           Log.d("EMVDemo", "PHONE_AUTH");
                           VFIApplication.maskHomeKey(false);
                           // startActivity(new Intent(MenuActivity.this, ManualTestActivity.class));
                           dialog.dismiss();
                           dialog.dismiss();
                           phoneautoth();

                       }

                   });

                dialog.show();
     }else//Branch Mode is SELECTED
               {
                   Log.d("EMVDemo", "BRANCH MODE IS SELECTED.................");
                   final choosetxntype1 dialog = new choosetxntype1(MenuActivity.this);
                   dialog.setOnChooselistener(new choosetxntype1.OnChooseListener() {
                       @Override
                       public void onClickcashadvance() {
                           editor.putString("Txn_Menu_Type","Chip || Magstrip || Contactless");
                           editor.putString("txn_type", "CASH_ADVANCE");
                           editor.apply();
                           txn_type1 = "CASH_ADVANCE";
                           Log.d("EMVDemo", "txn_type1  " + txn_type1);
                           Log.d("EMVDemo", "CASH_ADVANCE");
                           VFIApplication.maskHomeKey(false);
                           startActivity(new Intent(MenuActivity.this, InputAmountActivity.class));
                           dialog.dismiss();

                       }

                       @Override
                       public void onClickvoid() {
                           editor.putString("Txn_Menu_Type","Chip || Magstrip || Contactless");
                           editor.putString("txn_type", "REVERSAL");
                           editor.apply();
                           txn_type1 = "REVERSAL";
                           Log.d("EMVDemo", "REVERSAL");
                           VFIApplication.maskHomeKey(false);
                           startActivity(new Intent(MenuActivity.this, ReversalFristActivity.class));
                           dialog.dismiss();
                       }

                       @Override
                       public void onClickbalance() {
                           editor.putString("Txn_Menu_Type","Chip || Magstrip || Contactless");
                           editor.putString("txn_type", "BALANCE_INQUIRY");
                           editor.apply();
                           txn_type1 = "BALANCE_INQUIRY";
                           Log.d("EMVDemo", "BALANCE_INQUIRY");
                           VFIApplication.maskHomeKey(false);
                           startActivity(new Intent(MenuActivity.this, CheckCardActivity.class));
                           dialog.dismiss();
                       }

                       @Override
                       public void onClickphoneAuth() {
                           editor.putString("Txn_Menu_Type","Chip || Magstrip || Contactless");
                           editor.putString("txn_type", "PHONE_AUTH");
                           editor.apply();
                           txn_type1 = "PHONEAUTH";
                           Log.d("EMVDemo", "PHONE_AUTH");
                           VFIApplication.maskHomeKey(false);
                           // startActivity(new Intent(MenuActivity.this, ManualTestActivity.class));

                           dialog.dismiss();
                           phoneautoth();

                       }

                       @Override
                       public void onClickdeposit() {
                           editor.putString("Txn_Menu_Type","Chip || Magstrip || Contactless");
                           editor.putString("txn_type", "DEPOSIT");
                           editor.putString("printtype", " ");
                           editor.apply();
                           txn_type1 = "DEPOSIT";
                           Log.d("EMVDemo", "DEPOSIT");
                           VFIApplication.maskHomeKey(false);
                           //startActivity(new Intent(MenuActivity.this, ManualTestActivity.class));
                           dialog.dismiss();

                       }
                   });

                   dialog.show();
               }

                break;
            case R.id.logon:
                TransBasic.tvr="";
                ClearAllFiledData();
                VFIApplication.maskHomeKey(false);
               /* if(!preferences.getString("usertype", "").equals("")){
                    if (preferences.getString("usertype", "").equals("support")) {
                        Intent new_menu = new Intent(MenuActivity.this, Support_menu_activity.class);
                        new_menu.putExtra("utype", preferences.getString("usertype", ""));
                        new_menu.putExtra("uname",preferences.getString("username", ""));
                        startActivity(new_menu);
                    }
                    else if (preferences.getString("usertype", "").equals("supervisor")) {
                        Intent new_menu = new Intent(MenuActivity.this, Supervisor_menu_activity.class);
                        new_menu.putExtra("utype", preferences.getString("usertype", ""));
                        new_menu.putExtra("uname",preferences.getString("username", ""));
                        startActivity(new_menu);
                    }
                    else if (preferences.getString("usertype", "").equals("administrator")) {
                        Intent new_menu = new Intent(MenuActivity.this, admin_menu1.class);
                        new_menu.putExtra("utype", preferences.getString("usertype", ""));
                        new_menu.putExtra("uname",preferences.getString("username", ""));
                        startActivity(new_menu);
                    }

                }else{
                    Intent new_menu = new Intent(MenuActivity.this, user_login.class);
                    new_menu.putExtra("txn_type",preferences.getString("username", ""));
                    startActivity(new Intent(MenuActivity.this, user_login.class));

                } */

                    Intent new_menu = new Intent(MenuActivity.this, user_login.class);
                    new_menu.putExtra("txn_type",preferences.getString("username", ""));
                    startActivity(new Intent(MenuActivity.this, user_login.class));

                break;
            case R.id.setting:
                TransBasic.tvr="";
                ClearAllFiledData();
                VFIApplication.maskHomeKey(false);
                //Transactiondata.HEADER_VARSresponse.rresponse_Code="";
                if (dbHandler.gettransactiondata().size() >= 300)
                {
                    Log.d(TAG, "TRANSACTION ABOVE 100");

                    //Toast.makeText(getApplicationContext(), "Hello World!", Toast.LENGTH_LONG).show();

                    Toast.makeText(MenuActivity.this, "PLEASE DO END_OFF_DAY!...", Toast.LENGTH_LONG).show();

                }
                editor.putString("printtype", " ");
                editor.putString("Txn_Menu_Type","");
                editor.putString("txn_type", "");

                editor.putString("Txn_Menu_Type","Chip || Magstrip || Contactless");

                Log.d("Menu Activity ","Tmode Type............          "+ ISO8583msg.mode);

                if(ISO8583msg.mode.equals("Merchant"))
                {
                    editor.putString("txn_type", "PURCHASE");
                    editor.apply();
                    txn_type1 = "PURCHASE";
                    Log.d("EMVDemo", "PURCHASE");
                    Log.d("EMVDemo", "txn_type1  " + txn_type1);

                    startActivity(new Intent(MenuActivity.this, InputAmountActivity.class));
                }else
                {
                    editor.putString("txn_type", "CASH_ADVANCE");
                    editor.apply();
                    txn_type1 = "CASH_ADVANCE";
                    Log.d("EMVDemo", "CASH_ADVANCE");
                    Log.d("EMVDemo", "txn_type1  " + txn_type1);

                    startActivity(new Intent(MenuActivity.this, InputAmountActivity.class));
                }
                //startActivity(new Intent(MenuActivity.this, SET_AID_RID_KEY.class));
                //startActivity(new Intent(MenuActivity.this, SettingsActivity.class));
               /* Transactiondata transactiondata=new Transactiondata(preferences());
                transactiondata.unpack("9.13POS9TER1              220831104947FO00050001B000000000000010000F062034  G@@@@@@@@P1gAPPROVALh00000009706Q01004B262C76612E77513030");
                */break;
            case R.id.cahierlogin:
                TransBasic.tvr="";
                ClearAllFiledData();
                user_loadData();
               if (cashstatuse.equals("0"))
                {
                    VFIApplication.maskHomeKey(false);
                    //startActivity(new Intent(MenuActivity.this, Cashier_menu_activity.class));

                    new_menu = new Intent(MenuActivity.this, Cashier_menu_activity.class);
                    new_menu.putExtra("utype", "");
                    startActivity(new_menu);
                }
                else
                {
                    Toast.makeText(MenuActivity.this, "CASHIER IS ENABLED,PLEASE LOGIN ", Toast.LENGTH_SHORT).show();

                    new_menu = new Intent(MenuActivity.this, user_login.class);
                    new_menu.putExtra("txn_type",preferences.getString("username", ""));
                    startActivity(new Intent(MenuActivity.this, user_login.class));

                }
              /*  dbHandler.addtransactionfile("0", ISO8583.daterecit + "   " +ISO8583.timereciet,
                        ISO8583.ISO8583_HEADER.Header,ISO8583.ISO8583_HEADER.MTI,ISO8583.ISO8583_HEADER.BITMAP,
                        SPDH.Mer_id, TransBasic.aidLabel1,TransBasic.aid1,TransBasic.tvr,ISO8583.ISO8583_FIELD.r_field_13,ISO8583.ISO8583_FIELD.r_field_12
                        ,TransBasic.cardholder,TransBasic.Txn_type,
                        SPDH.r_currency1,TransBasic.savedPan, ISO8583.ISO8583_FIELD.r_field_03,
                        ISO8583.ISO8583_FIELD.r_field_04,ISO8583.ISO8583_FIELD.r_field_11,ISO8583.ISO8583_FIELD.field_12,
                        ISO8583.ISO8583_FIELD.field_13,ISO8583.ISO8583_FIELD.field_14,

                        ISO8583.ISO8583_FIELD.field_22 ,ISO8583.ISO8583_FIELD.r_field_24, ISO8583.ISO8583_FIELD.r_field_37,
                        ISO8583.ISO8583_FIELD.r_field_38, ISO8583.ISO8583_FIELD.r_field_39,
                        ISO8583.ISO8583_FIELD.r_field_41, ISO8583.ISO8583_FIELD.field_42,
                        ISO8583.ISO8583_FIELD.r_field_55, ISO8583.ISO8583_FIELD.field_62);*/
               break;


//....................................Added for Help Onclick Listener..................................................

                case R.id.menuhelp1:
                  Intent new_menu3 = new Intent(MenuActivity.this, HelpMain.class);
                  new_menu3.putExtra("txn_type",preferences.getString("username", ""));
                  startActivity(new Intent(MenuActivity.this, HelpMain.class));
                  break;

        }
    }

    /**
     * 自定义Adapter
     * @author laikey
     */
    private class AdvViewPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return advImages.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup view, int position, Object object) {
            view.removeView(advImages.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            ImageView imageView = advImages.get(position);
            view.addView(imageView);

            // Check if the current position is the one where you want to introduce a delay
            if (position == 0) {
                // Introduce a delay of 3 seconds (adjust as needed)
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // After the delay, move to the next image
                        advPosition = (advPosition + 1) % advImageIdList.length;
                        mHandler.sendEmptyMessage(0);
                    }
                }, 5000); // 3000 milliseconds = 3 seconds
            }

            return imageView;
        }
    }

    /**
     * 图片轮播任务
     */
    private class AdvViewPageTask implements Runnable {
        @Override
        public void run() {
            advPosition = (advPosition + 1) % advImageIdList.length;
            mHandler.sendEmptyMessage(0);
        }
    }

    /**
     * 接收子线程传递过来的数据
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            advViewPaper.setCurrentItem(advPosition);
        }
    };
public void phoneautoth()
{
    final choosetxntype dialog = new choosetxntype(MenuActivity.this);
    dialog.setOnChooselistener(new choosetxntype.OnChooseListener() {
        @Override
        public void onClickpurchase() {
             editor.putString("Txn_Menu_Type","Chip || Magstrip || Contactless");
            editor.putString("txn_type", "PURCHASE");
            editor.putString("printtype", "isphoneauth");
            editor.apply();
            txn_type1 = "PURCHASE";
            Log.d("EMVDemo", "PURCHASE");
            Log.d("EMVDemo", "txn_type1  " + txn_type1);
            VFIApplication.maskHomeKey(false);
            startActivity(new Intent(MenuActivity.this, InputAmountActivity.class));
            dialog.dismiss();
        }

        @Override
        public void onClickrefund() {
             /*editor.putString("Txn_Menu_Type","Chip || Magstrip || Contactless");
            editor.putString("txn_type1", "REFUND");
            editor.putString("printtype", "isphoneauth");
            editor.apply();
            txn_type1 = "REFUND";
            Log.d("EMVDemo", "REFUND");
            VFIApplication.maskHomeKey(false);
            //startActivity(new Intent(MenuActivity.this, Pre_Auth_Completion.class));

              */
            dialog.dismiss();
        }

        @Override
        public void onClickvoid() {
             /*editor.putString("Txn_Menu_Type","Chip || Magstrip || Contactless");
            editor.putString("txn_type1", "REVERSAL");
            editor.putString("printtype", "isphoneauth");
            editor.apply();
            txn_type1 = "REVERSAL";
            Log.d("EMVDemo", "REVERSAL");
            VFIApplication.maskHomeKey(false);
            //startActivity(new Intent(MenuActivity.this, ReversalFristActivity.class));
            dialog.dismiss();

              */
        }

        @Override
        public void onClickbalance() {
            /* editor.putString("Txn_Menu_Type","Chip || Magstrip || Contactless");
            editor.putString("txn_type1", "BALANCE_INQUIRY");
            editor.putString("printtype", "isphoneauth");
            editor.apply();
            txn_type1 = "BALANCE_INQUIRY";
            Log.d("EMVDemo", "BALANCE_INQUIRY");
            VFIApplication.maskHomeKey(false);
            //startActivity(new Intent(MenuActivity.this, CheckCardActivity.class));

             */
            dialog.dismiss();
        }

        @Override
        public void onClickpreauth() {
            /* editor.putString("Txn_Menu_Type","Chip || Magstrip || Contactless");
            editor.putString("txn_type1", "PRE_AUTH");
            editor.putString("printtype", "isphoneauth");
            editor.apply();
            txn_type1 = "PRE-AUTH";
            Log.d("EMVDemo", "PRE_AUTH");
            VFIApplication.maskHomeKey(false);
            //startActivity(new Intent(MenuActivity.this, InputAmountActivity.class));

             */
            dialog.dismiss();
        }
        @Override
        public void onClickpreauthcomp() {
             /*editor.putString("Txn_Menu_Type","Chip || Magstrip || Contactless");
            editor.putString("txn_type1", "PRE_AUTH_COMPLETION");
            editor.putString("printtype", "isphoneauth");

            editor.apply();
            txn_type1 = "PRE_AUTH_COMPLETION";
            Log.d("EMVDemo", "PRE_AUTH_COMPLETION");
            VFIApplication.maskHomeKey(false);
            //startActivity(new Intent(MenuActivity.this, InputSequenceNumber.class));

              */
            dialog.dismiss();

        }
        @Override
        public void onClickperchasecash() {
            /*editor.putString("Txn_Menu_Type","Chip || Magstrip || Contactless");
            editor.putString("txn_type1", "PURCHASE_WITH_CASHBACK");
            editor.putString("printtype", "isphoneauth");
            editor.apply();
            txn_type1 = "PURCHASE_WITH_CASHBACK";
            Log.d("EMVDemo", "PURCHASE_WITH_CASHBACK");
            VFIApplication.maskHomeKey(false);
            //startActivity(new Intent(MenuActivity.this, InputAmountActivity.class));

             */
            dialog.dismiss();
        }

        @Override
        public void onClickphoneAuth() {

            dialog.dismiss();

        }

    });

    dialog.show();
    SharedPreferences.Editor editor = preferences.edit();
    editor.putString("txn_type", " ");
    editor.apply();

}


    @Override
    public void onBackPressed()
    {

    }
   /* @Override
    protected void onUserLeaveHint() {
       // Toast.makeText(MenuActivity.this,"Home buton pressed",Toast.LENGTH_LONG).show();
        finish();
        super.onUserLeaveHint();
    }*/

  public static void ClearAllFiledData()
  {
      Log.d(TAG, "ClearAllFiledData........................");
      ISO8583.ISO8583_HEADER.Header="";
      ISO8583.ISO8583_HEADER.MTI="";
      ISO8583.ISO8583_HEADER.BITMAP="";
      ISO8583.ISO8583_HEADER.STAN="";

        //*****************************
        // ISO 8583 VARIABLES

      ISO8583.ISO8583_FIELD.field_02="";  /* Primary_Account_Number */
      ISO8583.ISO8583_FIELD.field_03="";  /* Processing_Code */
      ISO8583.ISO8583_FIELD.field_06="";  /* Amount card holder billing */
      ISO8583.ISO8583_FIELD. field_04=""; /* Amount_Transaction */
      ISO8583.ISO8583_FIELD. field_05=""; /* Amount_Settlement */
        /* skip 06 	/* Amount_Cardholder_Billing */
      ISO8583.ISO8583_FIELD. field_07=""; /*Transmission_Date_and_Time */
      ISO8583.ISO8583_FIELD. field_08=""; /* Processing_Code */
        /* skip 08 	/* Amount_Cardholder_Billing_Fee */
      ISO8583.ISO8583_FIELD. field_09=""; /* Conversion_Rate_Settlement */
      ISO8583.ISO8583_FIELD. field_10=""; /* Processing_Code */
        /* skip 10 	/* Conversion_Rate_Cardholder_Billing */
      ISO8583.ISO8583_FIELD. field_11=""; /* Systems_Trace_Audit_Number */
      ISO8583.ISO8583_FIELD. field_12=""; /* Time_Local_Transaction */
      ISO8583.ISO8583_FIELD. field_13=""; /* Date_Local_Transaction */
      ISO8583.ISO8583_FIELD.field_14="";  /* Date_Expiration */
      ISO8583.ISO8583_FIELD. field_15=""; /* Date Settlement */
      ISO8583.ISO8583_FIELD. field_16=""; /* Date_Conversion */
      ISO8583.ISO8583_FIELD. field_17=""; /* Date_Capture */
      ISO8583.ISO8583_FIELD. field_18=""; /* Merchants Type */
      ISO8583.ISO8583_FIELD. field_19=""; /* Acquiring_Institution_Country_Code */
      ISO8583.ISO8583_FIELD. field_20=""; /* Primary_Account_Number_* Extended_Country_Code 	*/
      ISO8583.ISO8583_FIELD. field_21=""; /* Forwarding_Institution_Country_Code */
      ISO8583.ISO8583_FIELD. field_22=""; /* Point_of_Service_Entry_Mode */
      ISO8583.ISO8583_FIELD. field_23=""; /* Card_Sequence_Number */
      ISO8583.ISO8583_FIELD. field_24=""; /* Network_International_Identifier */
      ISO8583.ISO8583_FIELD. field_25=""; /* Point_of_Service_Condition_Code */
      ISO8583.ISO8583_FIELD. field_26=""; /* Point_of_Service_PIN_Capture_Code */
      ISO8583.ISO8583_FIELD. field_27=""; /* Authorization_Identification_* Response_Length */
      ISO8583.ISO8583_FIELD. field_28=""; /* Amount_Transaction_Fee */
      ISO8583.ISO8583_FIELD. field_29=""; /* Amount_Settlement_Fee */
      ISO8583.ISO8583_FIELD. field_30=""; /* Additional amount */
      ISO8583.ISO8583_FIELD. field_31=""; /* Amount settlement processsinf fee */
      ISO8583.ISO8583_FIELD. field_32=""; /* Acquiring_Institution_ID_Code */
      ISO8583.ISO8583_FIELD. field_33=""; /* Forwarding_Institution_ID_Code */
      ISO8583.ISO8583_FIELD. field_34=""; /* Primary_Account_Number_Extended */
      ISO8583.ISO8583_FIELD. field_35=""; /* Track_2_Data */
      ISO8583.ISO8583_FIELD. field_36=""; /* Track 3 data */
      ISO8583.ISO8583_FIELD. field_37=""; /* Retrieval_Reference_Number */
      ISO8583.ISO8583_FIELD. field_38=""; /* Authorization_Identification_Response*/
      ISO8583.ISO8583_FIELD. field_39=""; /* Response_Code */
      ISO8583.ISO8583_FIELD. field_40=""; /* Service restriction Code */
      ISO8583.ISO8583_FIELD. field_41=""; /* Card_Acceptor_Terminal_Identification*/
      ISO8583.ISO8583_FIELD. field_42=""; /* Card_Acceptor_Identification_Code */


      ISO8583.ISO8583_FIELD. field_49="";;   /* Currency_Code_Transaction */
      ISO8583.ISO8583_FIELD. field_50="";;   /* Currency_Code_Settlement */
      ISO8583.ISO8583_FIELD. field_51="";    /* Currency code */
      ISO8583.ISO8583_FIELD. field_53="";    /* Processing_Code */
      ISO8583.ISO8583_FIELD. field_54="";    /* Additional_Amounts */
      ISO8583.ISO8583_FIELD.field_55="";     /* ICC */
      ISO8583.ISO8583_FIELD.field_56="";     /* Processing_Code */
      ISO8583.ISO8583_FIELD. field_57="";    /* Processing_Code */
      ISO8583.ISO8583_FIELD.field_58="";     /* Processing_Code */
      ISO8583.ISO8583_FIELD. field_59="";    /* Processing_Code */
      ISO8583.ISO8583_FIELD. field_60="";    /* Processing_Code */
      ISO8583.ISO8583_FIELD. field_61="";
      ISO8583.ISO8583_FIELD. field_62="";    /* Processing_Code */
      ISO8583.ISO8583_FIELD. field_63="";    /* Processing_Code */

      ISO8583.ISO8583_FIELD. field_64="";    /* Message_Authentication_Code_Field */


   //************************************ Response Fields ***************************************************
      ISO8583.ISO8583_FIELD. r_field_02="";    /* Primary_Account_Number */
      ISO8583.ISO8583_FIELD. r_field_03="";    /* Processing_Code */
      ISO8583.ISO8583_FIELD. r_field_06="";    /* Amount card holder billing */
      ISO8583.ISO8583_FIELD. r_field_04="";    /* Amount_Transaction */
      ISO8583.ISO8583_FIELD. r_field_11="";    /* Point_of_Service_Entry_Mode */
      ISO8583.ISO8583_FIELD. r_field_12="";
      ISO8583.ISO8583_FIELD. r_field_13="";
      ISO8583.ISO8583_FIELD. r_field_14="";
      ISO8583.ISO8583_FIELD. r_field_23="";    /* Card_Sequence_Number */
      ISO8583.ISO8583_FIELD. r_field_24="";    /* Network_International_Identifier */
      ISO8583.ISO8583_FIELD. r_field_25="";    /* Point_of_Service_Condition_Code */
      ISO8583.ISO8583_FIELD. r_field_37="";
      ISO8583.ISO8583_FIELD. r_field_38="";    //App code
      ISO8583.ISO8583_FIELD. r_field_39="";    //response code
      ISO8583.ISO8583_FIELD. r_field_41="";    /* Card_Acceptor_Terminal_Identification*/
      ISO8583.ISO8583_FIELD. r_field_42="";    /* Card_Acceptor_Identification_Code */
      ISO8583.ISO8583_FIELD. r_field_55="";
      //.............................................................................................End
  }
public void retrievefromdatabase()
{
     // if(arrayList.size()<1) {
    Log.d(TAG, "clear and add");

    arrayList.clear();
          for (int i = 0; i < dbHandler.getdatasize(); i++) {
              User u;
              if (dbHandler.getdatasize() > i) {
                  u = dbHandler.gettransactiondata().get(i);
                  arrayList.add(u);
                  if (arrayList.size() > i) {
                      Log.d(TAG, arrayList.get(i).getAid());
                  }
              }
          }
    //  }
}
    class loaddata extends AsyncTask<String, Integer, String> {
        String urlll = "";

        public loaddata() {

        }

        @Override
        protected String doInBackground(String... params) {
            retrievefromdatabase();
            return "";

        }
           /* @Override
            protected void onPublish*/

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //String st=new String(String.valueOf(values).getBytes(),StandardCharsets.UTF_8);
            // prog.setText(st+"%");
//
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("menuactiv", "load finished");

            //  Toast.makeText(getApplicationContext(), " image is downloaded successfully",Toast.LENGTH_LONG).show();
        }

    }
    public static void empty(){
      arrayList.clear();
    }
}
