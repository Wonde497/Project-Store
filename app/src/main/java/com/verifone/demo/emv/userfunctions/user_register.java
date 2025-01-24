package com.verifone.demo.emv.userfunctions;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.verifone.demo.emv.DBHandler;
import com.verifone.demo.emv.data_handlers.User;


import cn.verifone.simon.verifone_x9_demo_emv.Global.R;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

public class user_register extends Fragment {

    public String user_type;
    static String TAG="USER_REG";
    String menu_title;

    View reg_frag;

    private EditText user_name, user_password,conf_pwd;
    Button add_user,view_user;

    private DBHandler dbHandler;
    private DBHandler.user_functions user_fun;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        user_type = getArguments().getString("user_type");
        Log.d(TAG, user_type);

        reg_frag = inflater.inflate(R.layout.fragment_user_register, container, false);
        TextView tv = (TextView) reg_frag.findViewById(R.id.txttitle);

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

        tv.setText("Register " + menu_title);
        Log.d(TAG, user_type);

        initScreen();

        return  reg_frag;
    }

    private void initScreen() {

        user_name = (EditText) reg_frag.findViewById(R.id.idaddusername);
        user_password = (EditText) reg_frag.findViewById(R.id.idadduserpwd);
        conf_pwd = (EditText) reg_frag.findViewById(R.id.idconfirmuserpwd);

        add_user = (Button) reg_frag.findViewById(R.id.idBtnAdduser);

        add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String utype="";
                Log.d(TAG, "add clicked");
                String struser_name=user_name.getText().toString();
                String struser_pwd=user_password.getText().toString();
                String strconf_pwd=conf_pwd.getText().toString();

                if (struser_name.length()<4)
                {
                    Toast.makeText(reg_frag.getContext(), "User Name cannot be smaller than 4 characters.", Toast.LENGTH_LONG).show();
                    return;
                }
                else if (struser_pwd.length()<4)
                {
                    Toast.makeText(reg_frag.getContext(), "User Password cannot be smaller than 4 characters.", Toast.LENGTH_SHORT).show();
                    return;
                }

                else if (struser_name.isEmpty()){

                    Toast.makeText(reg_frag.getContext(), "Please enter User Name.", Toast.LENGTH_SHORT).show();
                    return;

                }
                else if (struser_pwd.isEmpty()){

                    Toast.makeText(reg_frag.getContext(), "Please enter User Password.", Toast.LENGTH_SHORT).show();
                    return;

                }
                else if (!(struser_pwd.equals(strconf_pwd))){

                    Toast.makeText(reg_frag.getContext(), "Entered passwords should be similar.", Toast.LENGTH_SHORT).show();
                    Log.d("EMVDemo", "click read" + strconf_pwd + "-" + struser_pwd);
                    return;
                }else

                {
                    if (user_type.equals("supervisor"))
                        utype="cashier";
                    else if (user_type.equals("support"))
                        utype="supervisor";
                    else if (user_type.equals("administrator"))
                    {
                        if (menu_title.equals("Support"))
                            utype = "support";
                        else if (menu_title.equals("Administrator"))
                            utype = "administrator";
                    }

                    Log.d(TAG, "utype: " + utype);

                    String TABLE_NAME = "users";

                    String ID_COL = "id";

                    String NAME_COL = "name";

                    String PASSWORD_COL = "password";

                    String TYPE_COL = "type";

                    String STATUS_COL = "status";

                    dbHandler.query="CREATE TABLE " + TABLE_NAME + " ("

                            + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "

                            + NAME_COL + " TEXT,"

                            + PASSWORD_COL + " TEXT,"

                            + TYPE_COL + " TEXT,"

                            + STATUS_COL + " TEXT)";

                    dbHandler.TABLE_NAME="users";
                    List<User> userList = new ArrayList<User>();
                    String selection = "";
                    String[] selectionArgs = {};

                    DBHandler.user_functions user_fun=dbHandler.new user_functions();
                    userList= user_fun.viewUsers(selection,selectionArgs);
                    //userList= user_fun.viewUsers(selection,selectionArgs,utype);

                    boolean isthere=false;
                    Log.d("EMVDemo", "Enterd Name "+struser_name);
                    for(int i=0; i<userList.size(); i++)
                    {
                        if(struser_name.equals(userList.get(i).getName()))
                        {
                            Log.d("EMVDemo", " user aleardy register");
                            isthere=true;
                        }
                    }
                    if(!isthere)
                    {
                        user_fun.addNewuser(struser_name, struser_pwd, utype);
                        Toast.makeText(reg_frag.getContext(), "User has been Registered.", Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    }else{
                        Toast.makeText(reg_frag.getContext(), "Username has aleardy Registerd", Toast.LENGTH_SHORT).show();
                        //return;
                    }
                }
            }
        });
        conf_pwd.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            add_user.performClick();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        dbHandler = new DBHandler(reg_frag.getContext());

    }

}