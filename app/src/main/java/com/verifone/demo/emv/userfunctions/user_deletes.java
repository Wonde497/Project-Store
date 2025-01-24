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
public class user_deletes extends Fragment {

    public String user_type;
    static String TAG="USR_DELELE";
    String menu_title;

    View del_frag;

    private EditText user_name;
    Button delete;

    private DBHandler dbHandler;
    private DBHandler.user_functions user_fun;
    String uname,struser_name,utype1,ustatus,upwd;
    //private String adname,admin,uname,utype1,upass,supp,supname,superv,supername,cashi,cashiname;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        del_frag = inflater.inflate(R.layout.fragment_user_deletes, container, false);
        user_type = getArguments().getString("user_type");
        Log.d(TAG, user_type);
        dbHandler = new DBHandler(del_frag.getContext());
        TextView tv = (TextView) del_frag.findViewById(R.id.tview);
        user_name = (EditText)del_frag.findViewById(R.id.Tvregterminalid);
        delete = (Button) del_frag.findViewById(R.id.idBtnDisupp);

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


        tv.setText("Delete "+menu_title);
        Log.d(TAG, "Here Delete: " + user_type);
        //user_ck();

        initScreen1();

        return  del_frag;
    }

    private void initScreen1() {


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
                            delete.performClick();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String utype="";

                Log.d(TAG, "add clicked");

                struser_name=user_name.getText().toString();

                if (struser_name.length()<4)
                {
                    Toast.makeText(del_frag.getContext(), "User Name cannot be smaller than 4 characters.", Toast.LENGTH_LONG).show();
                    return;
                }
                else if (struser_name.isEmpty()){

                    Toast.makeText(del_frag.getContext(), "Please enter User Name.", Toast.LENGTH_SHORT).show();
                    return;

                }
                Log.d(TAG, "finshed username validation");

                user_ck();

                Log.d(TAG, "login name: "+  uname);
                Log.d(TAG, "login utype: "+  utype1);



                if (user_type.equals("supervisor"))
                    utype="cashier";
                else if (user_type.equals("support"))
                    utype="supervisor";
                else if (user_type.equals("administrator")) {
                    if (menu_title.equals("Support"))
                        utype = "support";
                    else if (menu_title.equals("Administrator"))
                        utype = "administrator";
                }

                Log.d(TAG, "utype: " + utype);
                SharedPreferences S = del_frag.getContext().getSharedPreferences("Shared_data", Context.MODE_PRIVATE);

                DBHandler.user_functions user_fun = dbHandler.new user_functions();
            if (S.getString("deletetype", "none").equals("deletesupport"))//
                {
                    Log.d(TAG, "Administrator Menu is selected.......................");
                    if (struser_name.equals(uname) && utype1.equals("support"))
                    {
                    user_fun.deleteuser(struser_name);
                    Toast.makeText(del_frag.getContext(), "support has been Deleted.", Toast.LENGTH_SHORT).show();

                }else{
                        Toast.makeText(del_frag.getContext(), "Support not Found!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "User is not Support");
                 }

                }
            else if (S.getString("deletetype", "none").equals("deletesupervisor"))//
                {
                    Log.d(TAG, "Support Menu is selected..........................");
                 if (struser_name.equals(uname) && utype1.equals("supervisor")) {
                    user_fun.deleteuser(struser_name);
                    Toast.makeText(del_frag.getContext(), "Supervisor has been Deleted.", Toast.LENGTH_SHORT).show();

                }else{
                     Toast.makeText(del_frag.getContext(), "Supervisor not Found!", Toast.LENGTH_SHORT).show();
                     Log.d(TAG, "User is not Supervisor");
                 }
                }
            else if (S.getString("deletetype", "none").equals("deletecashier"))//
                {
                    Log.d(TAG, "supervisor Menu is selected...........................");
                if (struser_name.equals(uname) && utype1.equals("cashier")) {
                    user_fun.deleteuser(struser_name);
                    Toast.makeText(del_frag.getContext(), "Cashier has been Deleted.", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(del_frag.getContext(), "Cashier not Found!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "User is not cashier");
                }
                }
            else{
                Toast.makeText(del_frag.getContext(), "UNABLE TO DELETE.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "UNABLE TO DELETE");
            }

                getActivity().onBackPressed();

            }
        });

        dbHandler = new DBHandler(del_frag.getContext());

    }
    public void user_ck()
    {
        String selection = "name = ?";
        String[] selectionArgs = {struser_name};

        Log.d("DBRES", "here 1");
        List<User> userList = new ArrayList<User>();

        dbHandler = new DBHandler(del_frag.getContext());
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