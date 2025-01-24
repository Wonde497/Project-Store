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
public class user_Disable extends Fragment {

    public String user_type;
    static String TAG="USR_DISABLE";
    String menu_title;

    View dis_frag;

    private EditText user_name;
    Button Disable;

    private DBHandler dbHandler;
    private DBHandler.user_functions user_fun;
    String uname,struser_name,utype1,ustatus,upwd;
    //private String adname,admin,uname,upass,supp,supname,superv,supername,cashi,cashiname;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dis_frag= inflater.inflate(R.layout.fragment_user__disable, container, false);
        user_type = getArguments().getString("user_type");
        Log.d(TAG, user_type);
        TextView tv = (TextView) dis_frag.findViewById(R.id.tview);

        dbHandler = new DBHandler(dis_frag.getContext());
        user_name = (EditText)dis_frag.findViewById(R.id.Etuser);
        Disable = (Button) dis_frag.findViewById(R.id.idBtndisable);


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


        tv.setText("Disable  "+menu_title);
        Log.d(TAG, "Here enable: " + user_type);


        //user_ck();

        initScreen();
        return dis_frag;
    }

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
                            Disable.performClick();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        Disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String utype="";

                Log.d(TAG, "add clicked");

                struser_name=user_name.getText().toString();

                if (struser_name.length()<4 && struser_name.isEmpty())
                {
                    Toast.makeText(dis_frag.getContext(), "USER NAME MUST 4 OR ABOVE CHARACTER  .", Toast.LENGTH_LONG).show();
                    return;
                }
               else if (struser_name.isEmpty())
                {
                    Toast.makeText(dis_frag.getContext(), "USER NAME IS NOT NULL.", Toast.LENGTH_LONG).show();
                    return;
                }
                else {
                    Log.d(TAG, "finshed username validation");

                    user_ck();

                    Log.d(TAG, "login name: "+  uname);
                    Log.d(TAG, "login utype: "+  utype1);
                    Log.d(TAG, "login ustatus: "+  ustatus);



                    SharedPreferences S = dis_frag.getContext().getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                    DBHandler.user_functions user_fun = dbHandler.new user_functions();
                if (S.getString("distype", "none").equals("dissupport"))//
                    {

                        Log.d(TAG, "Administrator Menu is selected..................................");
                        Log.d(TAG, "Enterd user is= "+struser_name);
                        //user_ck();
                        if (struser_name.equals(uname) && utype1.equals("support"))
                        {
                            Log.d(TAG, "Current user from db is= "+uname);

                        user_fun.Disable(struser_name);
                        Toast.makeText(dis_frag.getContext(), "Support has been Disabled.", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(dis_frag.getContext(), "User is not Support.", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "User is not Support");
                    }
                    }
                else if (S.getString("distype", "none").equals("dissupervisor"))//
                    {
                        //user_ck();
                    Log.d(TAG, "Support Menu is selected................................");
                   if (struser_name.equals(uname) && utype1.equals("supervisor"))
                   {
                        user_fun.Disable(struser_name);
                        Toast.makeText(dis_frag.getContext(), "supervisor has been Disabled.", Toast.LENGTH_SHORT).show();
                   }else{
                       Toast.makeText(dis_frag.getContext(), "User is not Supervisor.", Toast.LENGTH_SHORT).show();
                       Log.d(TAG, "User is not Supervisor");
                   }
                  }
                 else if (S.getString("distype", "none").equals("discashier"))
                    {
                        //user_ck();
                     Log.d(TAG, "Supervisor Menu is selected...............................");
                    if (struser_name.equals(uname) && utype1.equals("cashier")) {
                        user_fun.Disable(struser_name);
                        Toast.makeText(dis_frag.getContext(), "cashier has been Disabled.", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(dis_frag.getContext(), "User is not cashier.", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "User is not cashier");
                    }
                    }
                else
                {
                    Toast.makeText(dis_frag.getContext(), "Unable to Disable User.", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Unable to Disable User");
                }
                }

                getActivity().onBackPressed();

            }
        });

        dbHandler = new DBHandler(dis_frag.getContext());

    }
    public void user_ck()
    {
        String selection = "name = ?";
        String[] selectionArgs = {struser_name};

        Log.d("DBRES", "here 1");
        List<User> userList = new ArrayList<User>();

        dbHandler = new DBHandler(dis_frag.getContext());
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
                ustatus=obj.getStatus();

                Log.d("Amex", "UserName : "+struser_name);
                Log.d("Amex", "Usertype : "+utype1);
                Log.d("Amex", "Userstatus: "+ustatus);
            }
        }
    }
        /*

        int rows = userList.size();
        Log.d("DBRES", "Total Size  " + rows);
        // user u_list;

        Log.d("DBRES", "here");
        for(int i = 0; i < rows; i ++) {
            user row = null;
            if (i > -1) {
                row = userList.get(i);
                Log.d("DBRES", "login name:........... "+  row.getName());
                //Log.d("DBRES", "login pass: "+  row.getPassword());
                Log.d("DBRES", "login Status: "+  row.getStatus());

                Log.d("DBRES", "login utype: "+  row.getTpe());

                uname=row.getName();
                utype1= row.getTpe();
                upass=row.getPassword();

                //Log.d("DBRES", "login utype1: "+  utype1);

                if (utype1.equals("administrator")) {
                    admin=utype1;
                    adname=uname;
                    Log.d(TAG, " Administrator user...... type= " + admin+"   uname=  "+adname);

                }
                else if (utype1.equals("support")) {
                    supp=utype1;
                    supname=uname;
                    Log.d(TAG, " Support user...... type= " + supp+"    uname=  "+supname);

                }
                else if (utype1.equals("supervisor")) {
                    superv=utype1;
                    supername=uname;
                    Log.d(TAG, " supervisor  user...... type= " + superv+"    uname=  "+supername);

                }
               else if (utype1.equals("cashier")) {
                    cashi=utype1;
                    cashiname=uname;
                    Log.d(TAG, " supervisor  user...... type=  " + cashi+"    uname=   "+cashiname);

                }
                else {

                    Log.d(TAG, "");

                }
            }*/

}