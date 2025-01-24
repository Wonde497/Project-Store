package com.verifone.demo.emv;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.verifone.demo.emv.Admin_menu.admin_menu1;
import com.verifone.demo.emv.data_handlers.User;
import com.verifone.demo.emv.transaction.InputPinActivity;
import com.verifone.demo.emv.transaction.TransBasic;

import java.util.ArrayList;
import java.util.List;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class user_login extends AppCompatActivity implements View.OnClickListener{

    Spinner spin_utype;
    static String TAG = "LOGIN";
    Button login;
    int iCurrentSelection;
    String mTestArray[];
    TextView login_title;

    private EditText user_name_edt, user_password_edt;
    String user_pwd,user_name,user_type,user_status;
    String Logon,txntype="";
    private DBHandler dbHandler;
   private String cashstatuse;
    boolean homePressed = false;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_login);
     Bundle bundle=getIntent().getExtras();
   if(bundle!=null)
    {
        txntype = getIntent().getExtras().getString("txn_type");
    }
         sharedPreferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
//txntype = "Void";
        dbHandler = new DBHandler(user_login.this);
        mTestArray = getResources().getStringArray(R.array.user_types);


        user_name_edt = findViewById(R.id.idaddusername);
        user_password_edt = findViewById(R.id.idadduserpwd);
        user_password_edt.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            login.performClick();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        login_title=findViewById(R.id.txttitle);

        spin_utype = findViewById(R.id.cbousertype);
        iCurrentSelection = spin_utype.getSelectedItemPosition();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.user_types, R.layout.custom_spinner_text);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spin_utype.setAdapter(adapter);

        login = findViewById(R.id.idBtnlogin);
        login.setOnClickListener(this);

        spin_utype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                iCurrentSelection = i;
                Log.d(TAG, "login"+ iCurrentSelection);

                login_title.setText(mTestArray[iCurrentSelection] + " Login");
                user_type=mTestArray[iCurrentSelection].toLowerCase();

                user_loadData();

                if (user_type.equals("cashier") && cashstatuse.equals("0")) {
                    Intent new_menu = new Intent(user_login.this, Cashier_menu_activity.class);
                    new_menu.putExtra("utype", user_type);
                    startActivity(new_menu);
                }
            }
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
    }

    @Override
    public void onClick (View v) {

        switch (v.getId()) {
            case R.id.idBtnlogin:

                user_name=user_name_edt.getText().toString();
                user_pwd=user_password_edt.getText().toString();
                user_status="Enabled";

                chk_login();
                Log.d(TAG, "login utype: "+  user_type);
                break;
        }
    }

    private void chk_login()
    {
        String selection = "name = ? and type = ? and status = ? and password = ?";
        String[] selectionArgs = {user_name,user_type,user_status,user_pwd};
        //String[] selectionArgs = {"chal","support","Enabled","1234"};

        List<User> userList = new ArrayList<User>();
        DBHandler.user_functions user_fun=dbHandler.new user_functions();

        userList= user_fun.viewUsers(selection,selectionArgs);

        Log.d("EMVDemo", "Inside login");

        if (userList.isEmpty())
            Toast.makeText(user_login.this, "Wrong Credentials! Please Check User name or Password",Toast.LENGTH_SHORT).show();
        else {
            Toast.makeText(user_login.this, "Welcome " + user_name, Toast.LENGTH_SHORT).show();

            logged_in();
        }

        for (User obj : userList) {
            Log.d("EMVDemo", "Users: " + obj.getName() + ":" + obj.getPassword() + ":" + obj.getStatus());
            Logon=obj.getName();

            Log.d("EMVDemo", "logon Name : "+Logon);
        }
    }

    public void user_loadData()
    {

        String selection = "";
        String[] selectionArgs = {};
        Log.d("DBRES", "here 1");
        List<User> cashierList = new ArrayList<User>();
        dbHandler = new DBHandler(user_login.this);
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
    private void logged_in()
    {


        if (user_type.equals("support")) {
            Intent new_menu = new Intent(user_login.this, Support_menu_activity.class);
            new_menu.putExtra("utype", user_type);
            new_menu.putExtra("uname",user_name);
            startActivity(new_menu);
        }

        else if (user_type.equals("supervisor") && (txntype.equals("REVERSAL")||txntype.equals("refund")))
        {
            Log.d("EMVDemo", "AMEX  ,Read txn_type : "+txntype);
            TransBasic transBasic=TransBasic.getInstance(sharedPreferences);
            transBasic.importCardConfirmResult();
            transBasic.pinInputHandler = pinInputHandler;

                /*Intent new_menu = new Intent(user_login.this, CheckCardActivity.class);
                new_menu.putExtra("txn_type", txntype);
                new_menu.putExtra("utype", user_type);
                startActivity(new_menu);*/

        }
      else if (user_type.equals("supervisor"))
      {
         Intent new_menu = new Intent(user_login.this, Supervisor_menu_activity.class);
          new_menu.putExtra("utype", user_type);
           new_menu.putExtra("uname", user_name);
              startActivity(new_menu);
          }
       else if (user_type.equals("administrator"))
       {
        Intent new_menu = new Intent(user_login.this, admin_menu1.class);
        new_menu.putExtra("utype", user_type);
            new_menu.putExtra("uname",user_name);
            Log.d("EMVDemo", "Admin logon Name : "+user_name);
        startActivity(new_menu);
    }
       else if (user_type.equals("cashier")) {
        Intent new_menu = new Intent(user_login.this, Cashier_menu_activity.class);
        new_menu.putExtra("utype", user_type);
            new_menu.putExtra("uname",user_name);
        startActivity(new_menu);
    }

}

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        homePressed = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        homePressed = true;
        Log.d(TAG, "Resume");
    }

    @Override
    public void onPause() {
        super.onPause();
        if(homePressed)
        {
            Log.i(TAG, "Pause");
            finish();
        }
    }
    Handler pinInputHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            startActivity(new Intent(user_login.this, InputPinActivity.class));
            finish();
        }
    };
}