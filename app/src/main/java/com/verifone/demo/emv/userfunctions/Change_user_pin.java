
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

public class Change_user_pin extends Fragment {

    public String user_type;
    static String TAG="USER_REG";
    String menu_title;

    View changepin_frag;

    private EditText user_name, curr_password,new_pwd;
    Button change,view_user;

    private DBHandler dbHandler;
    private DBHandler.user_functions user_fun;
    String struser_name,strcurr_pwd,strnew_pwd,uname,upass,utype1;
    //private String adname,admin,supp,supname,superv,supername,cashi,cashiname;
    // private String admincurpass,suppcurpass,supercurpass,cashcurpass;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        user_type = getArguments().getString("user_type");
        String utyp= getArguments().getString("utype");;
        Log.d(TAG, user_type);

        changepin_frag = inflater.inflate(R.layout.fragment_change_user_pin, container, false);
        TextView tv = (TextView) changepin_frag.findViewById(R.id.idtitle);
        dbHandler = new DBHandler(changepin_frag.getContext());
        user_name = (EditText) changepin_frag.findViewById(R.id.idusername);
        curr_password = (EditText) changepin_frag.findViewById(R.id.idcurrentpwd);
        new_pwd = (EditText) changepin_frag.findViewById(R.id.idnewpwd);

        change = (Button) changepin_frag.findViewById(R.id.idBtnchangeuser);

        if (user_type.equals("supervisor"))
            menu_title="Cashier";
        else if (user_type.equals("support"))
            menu_title="Supervisor";
        else if (user_type.equals("administrator"))
            menu_title=utyp;
        else if (user_type.equals("init")) {
            menu_title = "Administrator";
            user_type = "administrator";
        }

        tv.setText("Change " + menu_title+ "  Password");
        Log.d(TAG, user_type);


        initScreen();

        return  changepin_frag;
    }

    private void initScreen() {
        new_pwd.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            change.performClick();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String utype="";

                Log.d(TAG, "add clicked");

                struser_name=user_name.getText().toString();
                strcurr_pwd=curr_password.getText().toString();
                strnew_pwd=new_pwd.getText().toString();

                if (struser_name.length()<4)
                {
                    Toast.makeText(changepin_frag.getContext(), "User Name cannot be lessthan 4 characters.", Toast.LENGTH_LONG).show();
                    return;
                }
                else if (struser_name.isEmpty()){

                    Toast.makeText(changepin_frag.getContext(), "Please enter User Name.", Toast.LENGTH_SHORT).show();
                    return;

                }

                user_ck();

                Log.d(TAG, "login name: "+  uname);
                Log.d(TAG, "login utype: "+  utype1);
                //Log.d(TAG, "login utype: "+  upass);


                DBHandler.user_functions user_fun = dbHandler.new user_functions();

                if (struser_name.equals(uname) && strcurr_pwd.equals(upass) && utype1.equals("administrator")) {
                    Log.d(TAG, "Administrator Menu is selected...................................");
                    user_fun.changepin(struser_name, strnew_pwd);
                    Toast.makeText(changepin_frag.getContext(), "administrator password has been Changed.", Toast.LENGTH_SHORT).show();
                }
                else if (struser_name.equals(uname) && strcurr_pwd.equals(upass) && utype1.equals("support")) {
                    Log.d(TAG, "Support Menu is selected..................................");
                    user_fun.changepin(struser_name, strnew_pwd);
                    Toast.makeText(changepin_frag.getContext(), "support password has been Changed.", Toast.LENGTH_SHORT).show();

                }
                else if (struser_name.equals(uname) && strcurr_pwd.equals(upass) && utype1.equals("supervisor")) {
                    Log.d(TAG, "Supervisor Menu is selected................................");
                    user_fun.changepin(struser_name, strnew_pwd);
                    Toast.makeText(changepin_frag.getContext(), "supervisor password has been Changed.", Toast.LENGTH_SHORT).show();

                }
                else if (struser_name.equals(uname) && strcurr_pwd.equals(upass) && utype1.equals("cashier")) {
                    Log.d(TAG, "Cashier Menu is selected.....................");
                    user_fun.changepin(struser_name, strnew_pwd);
                    Toast.makeText(changepin_frag.getContext(), "Cashier password has been Changed.", Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(changepin_frag.getContext(), "Sorry Incorect user, Password is not Changed.", Toast.LENGTH_SHORT).show();
                }
                getActivity().onBackPressed();

            }
        });

        dbHandler = new DBHandler(changepin_frag.getContext());

    }
    public void user_ck()
    {
        String selection = "name = ? and password = ?";
        String[] selectionArgs = {struser_name,strcurr_pwd};

        Log.d("DBRES", "here 1");
        List<User> userList = new ArrayList<User>();

        dbHandler = new DBHandler(changepin_frag.getContext());
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
                strcurr_pwd=obj.getPassword();
                upass=strcurr_pwd;
                Log.d("Amex", "UserName : "+struser_name);
                Log.d("Amex", "Usertype : "+utype1);
                //Log.d("Amex", "Userstatus: "+ustatus);
            }
        }
    }
}
