package com.verifone.demo.emv.Support_menu;

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

import com.verifone.demo.emv.DBTerminal;
import com.verifone.demo.emv.Support_menu_activity;
import com.verifone.demo.emv.data_handlers.terminal;

import java.util.ArrayList;
import java.util.List;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;


public class Communication_config extends Fragment {

    public String user_type;
    static String TAG="COMMCONFIG";
    String menu_title;

    View reg_frag;

    private EditText ipaddress, port;
    private TextView tv,currentip, currentport;
    Button add,skip;

    private static DBTerminal dbTerminal;
    private static DBTerminal.Terminal_functions1 Terminal_fun;
    private static String currip;
    private static int currport;

    int row_cnt;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        user_type = getArguments().getString("user_type");
       // Log.d(TAG, user_type);

        reg_frag = inflater.inflate(R.layout.fragment_communication_config, container, false);
         tv = (TextView) reg_frag.findViewById(R.id.txttitle);
         currentip = (TextView) reg_frag.findViewById(R.id.currentip);
         currentport = (TextView) reg_frag.findViewById(R.id.currentport);

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

        //Log.d(TAG, user_type);
        chk_ip();
        if(row_cnt==0){
            initScreen();
        }
        else{
            initScreen1();
        }

        IPport_loadData();

        tv.setText("Communication Config" );
        currentip.setText("Current IP:   "+ currip);
        Log.d(TAG, "Current IP  "+currip);
        currentport.setText("Current Port:    "+ currport);
        Log.d(TAG, "Current PORT  "+currport);
        ((Support_menu_activity) getActivity()).addToFragmentStack(this);
        return  reg_frag;
    }
    public void chk_ip()
    {
        String selection = null;
        String[] selectionArgs =null;
        Bundle bundle = new Bundle();
        bundle.putString("IP", "PORT");

        dbTerminal = new DBTerminal(reg_frag.getContext());
        DBTerminal.Terminal_functions1 Terminal_fun=dbTerminal.new Terminal_functions1();

        row_cnt=Terminal_fun.row_countipport(selection,selectionArgs);

        Log.d(TAG,"Check IP AND PORT COL:  " + row_cnt);

    }
    public void IPport_loadData()
    {
        String selection = "";
        String[] selectionArgs = {};
        Log.d("DBRES", "IPPORT here Started ");
        List<terminal> IPportinfolist = new ArrayList<terminal>();
        dbTerminal = new DBTerminal(reg_frag.getContext());

        Log.d("DBRES", "IPPORT here Started 4 ");
        DBTerminal.Terminal_functions1 Terminal_fun=dbTerminal.new Terminal_functions1();
        Log.d("DBRES", "IPPORT here3");
        IPportinfolist= Terminal_fun.viewipport(selection,selectionArgs);
        int rows = IPportinfolist.size();
        Log.d("DBRES", "IPPORT here2  " + rows);
        Log.d("DBRES", "IPPORT here for loop");
        for(int i = 0; i < rows; i ++) {
            terminal row = null;
            if (i > -1) {
                row = IPportinfolist.get(i);
                Log.d("DBRES", "IPADDRESS : "+  row.getIpaddress());

                Log.d("DBRES", "PORT : "+  row.getPort());

                currip=row.getIpaddress();
                 String portn=row.getPort();
                currport=Integer.parseInt(portn);

                Log.d("DBRES", "IPADDDRESS col: "+currip);
                Log.d("DBRES", "PORT col:     "+currport);

            }
            else
            {
                Log.d("DBRES", "Finished ip: ");
            }
        }
    }


    private void initScreen() {
        Log.d(TAG, "INSERTING IP AND PORT row_cnt<0");
        ipaddress = (EditText) reg_frag.findViewById(R.id.idaddip);
        port = (EditText) reg_frag.findViewById(R.id.idaddport);

        add = (Button) reg_frag.findViewById(R.id.idBtnAddip);
        port.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                           add.performClick();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String utype="";

                Log.d(TAG, "add clicked");

                String ipadd=ipaddress.getText().toString();
                String portnum=port.getText().toString();

                if (ipadd.isEmpty() || portnum.isEmpty()){

                    Toast.makeText(reg_frag.getContext(), "ENTER CORRECT FORMAT OF IPADDRESS AND PORT NUMBER.", Toast.LENGTH_SHORT).show();
                    return;

                }else {

                    if (user_type.equals("supervisor"))
                        utype = "cashier";
                    else if (user_type.equals("support"))
                        utype = "supervisor";
                    else if (user_type.equals("administrator")) {
                        if (menu_title.equals("Support"))
                            utype = "support";
                        else if (menu_title.equals("Administrator"))
                            utype = "administrator";
                    }

                    Log.d(TAG, "menu_title: " + user_type);

                    String TABLE_NAME_IP_PORT = "ip_port";

                    String ID_COL = "id";

                    String IPADDRESS_COL = "ipaddress";

                    String PORT_COL = "port";

                    dbTerminal.query = "CREATE TABLE " + TABLE_NAME_IP_PORT + " ("

                            + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT,"

                            + IPADDRESS_COL + " TEXT ,"

                            + PORT_COL + " TEXT)";

                    dbTerminal.TABLE_NAME_IP_PORT = "ip_port";

                    DBTerminal.Terminal_functions1 Terminal_fun = dbTerminal.new Terminal_functions1();
                    Terminal_fun.regipport(ipadd, portnum);


                    Toast.makeText(reg_frag.getContext(), "ip and port has been Registered.", Toast.LENGTH_SHORT).show();
                }
               getActivity().onBackPressed();

            }
        });

        dbTerminal = new DBTerminal(reg_frag.getContext());

    }
    private void initScreen1() {
        Log.d(TAG, "UPDATING IP AND PORT row_cnt>0");
        ipaddress = (EditText) reg_frag.findViewById(R.id.idaddip);
        port = (EditText) reg_frag.findViewById(R.id.idaddport);


        add = (Button) reg_frag.findViewById(R.id.idBtnAddip);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String utype="";

                Log.d(TAG, "add clicked");

                String ipadd=ipaddress.getText().toString();
                String portnum=port.getText().toString();
                String id="1";
                if (ipadd.isEmpty() || portnum.isEmpty())
                {
                    Toast.makeText(reg_frag.getContext(), "ENTER CORRECT FORMAT OF IPADDRESS AND PORT NUMBER.", Toast.LENGTH_SHORT).show();
                    return;

                }else {

                    DBTerminal.Terminal_functions1 Terminal_fun = dbTerminal.new Terminal_functions1();
                    Terminal_fun.updateipport(id, ipadd, portnum);

                    Toast.makeText(reg_frag.getContext(), "ip and port has been Updated.", Toast.LENGTH_SHORT).show();
                }
                getActivity().onBackPressed();

            }
        });
        skip = (Button) reg_frag.findViewById(R.id.idBtnSkipip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String utype="";

                Log.d(TAG, "add clicked");

               getActivity().onBackPressed();

            }
        });

        dbTerminal = new DBTerminal(reg_frag.getContext());

    }
}