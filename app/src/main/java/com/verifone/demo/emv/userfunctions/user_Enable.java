package com.verifone.demo.emv.userfunctions;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.verifone.demo.emv.DBHandler;
import com.verifone.demo.emv.data_handlers.User;

import java.util.ArrayList;
import java.util.List;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link user_Enable#newInstance} factory method to
 * create an instance of this fragment.
 */
public class user_Enable extends Fragment {

    public String user_type;
    static String TAG="USR_ENABLE";
    String menu_title;

    View en_frag;

    private EditText user_name;
    Button Enable;

    private DBHandler dbHandler;
    private DBHandler.user_functions user_fun;
    String struser_name,uname,utype1;
    private String adname,admin,upass,supp,supname,superv,supername,cashi,cashiname;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        en_frag= inflater.inflate(R.layout.fragment_user__enable, container, false);
        user_type = getArguments().getString("user_type");
        Log.d(TAG, user_type);
        dbHandler = new DBHandler(en_frag.getContext());
        TextView tv = (TextView) en_frag.findViewById(R.id.tview);
        user_name = (EditText)en_frag.findViewById(R.id.Etuser);
        Enable = (Button) en_frag.findViewById(R.id.idBtnenable);

        if (user_type.equals("supervisor"))
            menu_title="Cashier";
        else if (user_type.equals("support"))
            menu_title="Supervisor";
        else if (user_type.equals("administrator"))
            menu_title="Support";
        else if (user_type.equals("init")) {
            menu_title = "Administrator";
            user_type = "administrator";
        }


        tv.setText("Enable  "+menu_title);
        Log.d(TAG, "Here enable: " + user_type);

        //user_ck();

        initScreen();
        return en_frag;
    }

    /*public void user_ck()
    {

        String selection = "";
        String[] selectionArgs = {};
        Log.d("DBRES", "here 1");
        List<user> userList = new ArrayList<user>();
        dbHandler = new DBHandler(en_frag.getContext());
        DBHandler.user_functions user_fun=dbHandler.new user_functions();
        //DBTerminal.Terminal_functions1 Terminal_fun=dbTerminal.new Terminal_functions1();
        Log.d("DBRES", "here 3");
        userList= user_fun.viewUsers(selection,selectionArgs);
        int rows = userList.size();
        Log.d("DBRES", "Total Size  " + rows);
        // user u_list;

        Log.d("DBRES", "here");
        for(int i = 0; i < rows; i ++) {
            user row = null;
            if (i > -1) {
                row = userList.get(i);
                Log.d("DBRES", "login name: "+  row.getName());
                Log.d("DBRES", "login utype: "+  row.getPassword());
                Log.d("DBRES", "login utype: "+  row.getStatus());

                Log.d("DBRES", "login utype: "+  row.getTpe());

                uname=row.getName();
                utype1= row.getTpe();
                upass=row.getPassword();

                if (utype1.equals("administrator")) {
                    admin=utype1;
                    adname=uname;
                    Log.d(TAG, " administrator   " + admin+" "+adname);

                }
                if (utype1.equals("support")) {
                    supp=utype1;
                    supname=uname;
                    Log.d(TAG, " support   " + supp+" "+supname);

                }
                if (utype1.equals("supervisor")) {
                    superv=utype1;
                    supername=uname;
                    Log.d(TAG, " supervisor   " + superv+" "+supername);

                }
                if (utype1.equals("cashier")) {
                    cashi=utype1;
                    cashiname=uname;
                    Log.d(TAG, " supervisor   " + cashi+" "+cashiname);

                }
                else {

                    Log.d(TAG, " usertype is not defined ");

                }
            }
        }}
*/
    private void initScreen() {

        user_name.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            Enable.performClick();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        Enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String utype="";

                Log.d(TAG, "add clicked");

                struser_name=user_name.getText().toString();

                if (struser_name.length()<4 && struser_name.isEmpty())
                {
                    Toast.makeText(en_frag.getContext(), "PLEASE ENTER CORRECT USER.", Toast.LENGTH_LONG).show();
                    return;
                }
                else {
                    Log.d(TAG, "finshed username validation");

                    user_ck();

                    Log.d(TAG, "login name: "+  uname);
                    Log.d(TAG, "login utype: "+  utype1);


                    SharedPreferences S = en_frag.getContext().getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                    DBHandler.user_functions user_fun = dbHandler.new user_functions();

                 if (S.getString("entype", "none").equals("ensupport"))//
                    {
                        Log.d(TAG, "Administrator Menu is selected...........................");
                        Log.d(TAG, "Entered user is= "+struser_name);
                    if (struser_name.equals(uname) && utype1.equals("support"))
                    {
                        Log.d(TAG, "Current user from db is= "+uname);
                        user_fun.Enable(struser_name);
                        Toast.makeText(en_frag.getContext(), "support has been Enabled.", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(en_frag.getContext(), "User is not Support.", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "User is not support");
                    }
                    }
                 else if (S.getString("entype", "none").equals("ensupervisor"))//
                    {
                        Log.d(TAG, "Support Menu is selected.............................");
                        Log.d(TAG, "Entered user is= "+struser_name);
                        if (struser_name.equals(uname) && utype1.equals("supervisor"))
                        {
                            Log.d(TAG, "Current user from db is= "+uname);
                        user_fun.Enable(struser_name);
                        Toast.makeText(en_frag.getContext(), "supervisor has been Enabled.", Toast.LENGTH_SHORT).show();
                        }else
                        {
                            Toast.makeText(en_frag.getContext(), "User is not Supervisor.", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "User is not Supervisor");
                        }
                    }
                  else if (S.getString("entype", "none").equals("encashier"))//
                        {
                        Log.d(TAG, "Supervisor Menu is selected.............................");
                            Log.d(TAG, "Entered user is= "+struser_name);
                        if (struser_name.equals(uname) && utype1.equals("cashier"))
                        {
                            Log.d(TAG, "Current user from db is= "+uname);
                        user_fun.Enable(struser_name);
                        Toast.makeText(en_frag.getContext(), "cashier has been Enabled.", Toast.LENGTH_SHORT).show();
                        }else
                        {
                            Toast.makeText(en_frag.getContext(), "User is not Cashier.", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "User is not cashier");
                        }
                    }else
                 {
                     Toast.makeText(en_frag.getContext(), "Unable to Enable User.", Toast.LENGTH_SHORT).show();
                     Log.d(TAG, "Unable to Enable User");
                 }
                }

                getActivity().onBackPressed();

            }
        });

        dbHandler = new DBHandler(en_frag.getContext());

    }
    public void user_ck()
    {
        String selection = "name = ?";
        String[] selectionArgs = {struser_name};

        Log.d("DBRES", "here 1");
        List<User> userList = new ArrayList<User>();

        dbHandler = new DBHandler(en_frag.getContext());
        DBHandler.user_functions user_fun=dbHandler.new user_functions();
        Log.d("DBRES", "here 2");
        userList= user_fun.viewUsers(selection,selectionArgs);
        if(userList.size()>0)
        {
            for (User obj : userList)
            {
                Log.d("EMVDemo", "Users: " + obj.getName() + ":" + obj.getPassword() + ":" + obj.getStatus());
                struser_name=obj.getName();
                uname=struser_name;
                utype1=obj.getTpe();
                //ustatus=obj.getStatus();

                Log.d("Amex", "UserName : "+struser_name);
                Log.d("Amex", "Usertype : "+utype1);
                //Log.d("Amex", "Userstatus: "+ustatus);
            }
        }
    }
}