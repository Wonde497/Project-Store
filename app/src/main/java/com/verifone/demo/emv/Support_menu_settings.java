package com.verifone.demo.emv;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;


import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.verifone.demo.emv.Help.SupportHelpMain;
import com.verifone.demo.emv.Support_menu.Settlment;
import com.verifone.demo.emv.Support_menu.Support_manage_user;
import com.verifone.demo.emv.Support_menu.Update_Comm_Type;
import com.verifone.demo.emv.Utilities.ToastUtil;
import com.verifone.demo.emv.data_handlers.User;
import com.verifone.demo.emv.data_handlers.terminal;
import com.verifone.demo.emv.transaction.canvas_printer.PrinterCanvas;
import com.verifone.demo.emv.userfunctions.user_register;
import com.verifone.demo.emv.userfunctions.view_users;

import java.util.ArrayList;
import java.util.List;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class Support_menu_settings extends AppCompatActivity {
    static String TAG = "MENU";
    boolean homePressed = false;
    String utype;
    String menu_title;
    Fragment support_settings_frag = null,transactionactivity;
    //int cashierstatus=1;

    FrameLayout support_settings_frame;
    FrameLayout frame;
    ImageButton regsupervisor;
    ImageButton cashierlog;
    ImageButton changeadminpin;
    ImageButton setteltime, auto_settlement, settlement_time;
    ImageButton managetrans;
    ImageButton comsetup;
    ImageButton manageusers;
    ImageButton gallery;
    ImageButton supportsettinghelp;
    SharedPreferences sharedPreferences;
    private DBHandler dbHandler;

    //private static DBTerminal dbTerminal;
    //DBTerminal.Terminal_functions1 terminal_fun;
    private String Type_col,cashierstatus1,settlstatus1="",Type_col1="";
    private static DBTerminal dbTerminal;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        utype = "support";

        Log.d(TAG, "Menu: " + utype);


        setContentView(R.layout.activity_support_menu_settings);
        sharedPreferences=getSharedPreferences("Shared_data", Context.MODE_PRIVATE);

        support_settings_frame=(FrameLayout) findViewById(R.id.support_settings_frame);
        frame=(FrameLayout) findViewById(R.id.frame);

        regsupervisor = (ImageButton) findViewById(R.id.regsupervisor);
        comsetup = (ImageButton) findViewById(R.id.comsetup);
        cashierlog=(ImageButton) findViewById(R.id.cashierlog);

        manageusers = (ImageButton) findViewById(R.id.manageusers);
        managetrans = (ImageButton) findViewById(R.id.managetrans);
        gallery= (ImageButton) findViewById(R.id.gallery);

        auto_settlement =  (ImageButton) findViewById(R.id.auto_settlement);
        settlement_time =  (ImageButton) findViewById(R.id.settlement_time);

        user_loadData();

        initMenuView();

    }

    private void initMenuView() {
       boolean ison= sharedPreferences.getBoolean("issettlmenton",true);
        if (ison){
            auto_settlement.setImageResource(R.drawable.white_on_icon2_64px);
        }
        else {
            auto_settlement.setImageResource(R.drawable.white_off_icon2_64px);
        }

        final Bundle bundle = new Bundle();
        bundle.putString("user_type", utype);
        regsupervisor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                frame.setVisibility(View.INVISIBLE);
                support_settings_frag = new user_register();
                support_settings_frag.setArguments(bundle);
                if (support_settings_frag != null) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.support_settings_frame, support_settings_frag).commit();
                }

            }
        });

        comsetup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //support_settings_frame.setVisibility(View.VISIBLE);
                frame.setVisibility(View.INVISIBLE);
                support_settings_frag = new Update_Comm_Type();
                support_settings_frag.setArguments(bundle);
                if (support_settings_frag != null) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.support_settings_frame, support_settings_frag).commit();
                }

            }
        });

        cashierlog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                user_loadData();

                if (cashierstatus1.equals("1")){
                    DBHandler.user_functions user_fun = dbHandler.new user_functions();
                    user_fun.UpdateCashierstat(cashierstatus1);
                    Log.d(TAG, "cashierstatus  Disabled  ");
                    cashierlog.setImageResource(R.drawable.white_off_icon2_64px);
                }
                else {

                    DBHandler.user_functions user_fun = dbHandler.new user_functions();
                    user_fun.UpdateCashierstat(cashierstatus1);
                    Log.d(TAG, " cashierstatus Enabled   " );
                    cashierlog.setImageResource(R.drawable.white_on_icon2_64px);
                }


            }
        });
        managetrans.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VFIApplication.maskHomeKey(false);
                Toast.makeText(Support_menu_settings.this,"DISABLED FROM Admin", Toast.LENGTH_LONG).show();

                //startActivity(new Intent(Support_menu_settings.this, Manage_Transactions.class));
                //finish();

            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                imageChooser();
            }
        });
        manageusers.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VFIApplication.maskHomeKey(false);
                startActivity(new Intent(Support_menu_settings.this, Support_manage_user.class));
                finish();


            }
        });

 //............................Added by Amlakie for Support Help.............................................
      //  supportsettinghelp.setOnClickListener(new View.OnClickListener() {
            //public void onClick(View v) {

              //  startActivity(new Intent(Support_menu_settings.this, SupportHelpMain.class));

           // }
       // });

        auto_settlement.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, " Auto Settlement ON/OFF");
                ///setteltime.setChecked(sharedPreferences.getBoolean("iscontactless",false));
                editor = sharedPreferences.edit();
                boolean ison= sharedPreferences.getBoolean("issettlmenton",true);
                if (ison){
                    ToastUtil.toastOnUiThread(Support_menu_settings.this, "SETTLEMENT IS DISABLED.");
                    editor.putBoolean("issettlmenton",false);
                    editor.apply();

                    auto_settlement.setImageResource(R.drawable.white_off_icon2_64px);

                    }
                else {
                    editor.putBoolean("issettlmenton",true);
                    editor.apply();
                    Log.d(TAG, " Settlement Enabled   " );
                    ToastUtil.toastOnUiThread(Support_menu_settings.this, "SETTLEMENT IS ENABLED.");
                    auto_settlement.setImageResource(R.drawable.white_on_icon2_64px);
                }

            }

        });
        settlement_time.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, " Settlement Time");
                Log.d(TAG, "printtype!, Cashierpin");
                frame.setVisibility(View.INVISIBLE);
                support_settings_frag = new Settlment();
                support_settings_frag.setArguments(bundle);
                if (support_settings_frag != null) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.support_settings_frame, support_settings_frag).commit();
                }

            }

        });


    }
    public void imageChooser() {
        Intent i = new Intent();
       /* i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

           startActivityForResult(Intent.createChooser(i, "Select Picture"), 200);*/
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 200);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 200) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                String imageuri=selectedImageUri.toString();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(imageuri));
                    PrinterCanvas p=new PrinterCanvas(getApplicationContext());
                    p.assbitmap(bitmap);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("imageuri",imageuri);
                    editor.commit();

                }catch (Exception e){

                }
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                }
            }
        }
    }
    public void user_loadData()
    {
        String selection = "";
        String[] selectionArgs = {};
        Log.d("DBRES", "here 1");
        List<User> cashierList = new ArrayList<User>();
        dbHandler = new DBHandler(Support_menu_settings.this);
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

                Type_col=row.getCashierstatus();
                //Sta_col=row.getStatus();

                if (Type_col.equals("1")) {
                    cashierstatus1 = Type_col;
                    cashierlog.setImageResource(R.drawable.white_on_icon2_64px);

                    Log.d(TAG, " Cashier STATUS  " + cashierstatus1);

                } else {
                    cashierstatus1 = Type_col;
                    cashierlog.setImageResource(R.drawable.white_off_icon2_64px);

                    Log.d(TAG, " Cashier STATUSE else condtion " + cashierstatus1);

                }
            }
        }
    }
    //@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.regsupp:
                //VFIApplication.maskHomeKey(false);
                support_settings_frag=new user_register();
                //frag.setArguments(bundle);
                if (support_settings_frag != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.support_settings_frame, support_settings_frag);
                    transaction.commit();
                }
                break;
            case R.id.viewsupp:
                support_settings_frag=new view_users();
                //frag.setArguments(bundle);
                if (support_settings_frag != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.support_settings_frame, support_settings_frag);
                    transaction.commit();
                }
                break;

        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        homePressed = true;
        Log.d(TAG, "Resume");
    }

   /* @Override
    public void onPause() {
        super.onPause();
        if(homePressed)
        {
            Log.i(TAG, "Pause");
            startActivity(new Intent(Admin_menu_activity.this, MenuActivity.class));
           finish();
        }
    }*/






}