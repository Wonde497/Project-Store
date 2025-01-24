package com.verifone.demo.emv.terminalfunctions;

import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.verifone.demo.emv.DBHandler;
import com.verifone.demo.emv.MenuActivity;
import com.verifone.demo.emv.Public_data.ISO8583msg;
import com.verifone.demo.emv.data_handlers.User;

import java.util.ArrayList;
import java.util.List;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class View_TerminalInfo extends Fragment {

    private TableLayout mTableLayout;
    ProgressDialog mProgressBar;
    private DBHandler dbHandler;
    private static final String TAG = "VIEW";
    private TextView tv1,tvmid,tvtid,tvmname,tvmaddress,tvtmode,tvcurrency,tvcomtype,versionnum;
    private View view_frag;
    private static String terminalId,merchantid,merchantname,merchantaddress,mode,currency,commtype;
    public String tid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view_frag=inflater.inflate(R.layout.fragment_view_terminal_info, container, false);

        Log.d("VIEW", "view Terminal information ");
        dbHandler = new DBHandler(view_frag.getContext());

        tv1 = (TextView) view_frag.findViewById(R.id.tv1);
        tvtid = (TextView) view_frag.findViewById(R.id.tvtid);
        tvmid = (TextView) view_frag.findViewById(R.id.tvmid);
        tvmname = (TextView) view_frag.findViewById(R.id.tvmname);
        tvmaddress = (TextView) view_frag.findViewById(R.id.tvmaddress);
        tvtmode = (TextView) view_frag.findViewById(R.id.tvtmode);
        tvcurrency = (TextView) view_frag.findViewById(R.id.tvcurrency);
        tvcomtype = (TextView) view_frag.findViewById(R.id.tvcomtype);
        versionnum = (TextView) view_frag.findViewById(R.id.version);
        loadDataTID();
        loadDataMID();
        loadDatamrname();
        loadMaddressData();
        loadTmodeData();
        loadCurrencyData();
        loadCommtypeData();

        View_loadData();
        return view_frag;
    }

    public void loadDataTID()
    {

        String selection = "";
        String[] selectionArgs = {};

        Log.d("DBRES", "There 1");
        List<User> TermInfoList = new ArrayList<User>();
        dbHandler = new DBHandler(view_frag.getContext());
        //dbHandler = new DBHandler();
        DBHandler.terminal_functions terminal_fun=dbHandler.new terminal_functions();
        Log.d("DBRES", "There 3");
        TermInfoList= terminal_fun.viewTidInfo(selection,selectionArgs);
        int rows = TermInfoList.size();
        Log.d("DBRES", "There2  " + rows);
        Log.d("DBRES", "There for loop");
        for(int i = 0; i < rows; i ++) {
            User row = null;
            if (i ==0) {
                row = TermInfoList.get(i);
                Log.d("DBRES", "Terminal_id: "+  row.getTerminal_id());
                terminalId=row.getTerminal_id();
                tid=row.getTerminal_id();
                ISO8583msg.SPDH_HEADER_VARS.terminal_Id=row.getTerminal_id();
                Log.d("DBRES", "terminal_Id : from loaddata "+  terminalId);
                tid=row.getTerminal_id();
            }
        }
    }
    public void loadDataMID()
    {
        String selection = "";
        String[] selectionArgs = {};
        Log.d("DBRES", "Mhere 1");
        List<User> TermInfoList = new ArrayList<User>();
        dbHandler = new DBHandler(view_frag.getContext());
        DBHandler.terminal_functions terminal_fun=dbHandler.new terminal_functions();
        Log.d("DBRES", "Mhere 3");
        TermInfoList= terminal_fun.viewMidInfo(selection,selectionArgs);
        int rows = TermInfoList.size();
        Log.d("DBRES", "Mhere2  " + rows);
        Log.d("DBRES", "Mhere for loop");
        for(int i = 0; i < rows; i ++) {
            User row = null;
            if (i ==0) {
                row = TermInfoList.get(i);
                Log.d("DBRES", "Merchant_id: "+  row.getMerchant_id());
                merchantid=row.getMerchant_id();
                Log.d("DBRES", " :merchantid from loaddata "+  merchantid);

            }
        }
    }
    public void loadDatamrname()//merchantname
    {
        String selection = "";
        String[] selectionArgs = {};
        Log.d("DBRES", "Mhere 1");
        List<User> TermInfoList = new ArrayList<User>();
        dbHandler = new DBHandler(view_frag.getContext());
        DBHandler.terminal_functions terminal_fun=dbHandler.new terminal_functions();
        Log.d("DBRES", "Mhere 3");
        TermInfoList= terminal_fun.viewMnameInfo(selection,selectionArgs);
        int rows = TermInfoList.size();
        Log.d("DBRES", "Mhere2  " + rows);
        Log.d("DBRES", "Mhere for loop");
        for(int i = 0; i < rows; i ++) {
            User row = null;
            if (i ==0) {
                row = TermInfoList.get(i);
                Log.d("DBRES", "Merchant_name: "+  row.getMerchant_name());
                merchantname=row.getMerchant_name();
                Log.d("DBRES", "Merchant Nmae : from loaddata "+ merchantname);

            }
        }
    }
    public  void loadCurrencyData()
    {
        String selection = "";
        String[] selectionArgs = {};
        Log.d("DBRES", "There 1");
        List<User> TermInfoList2 = new ArrayList<User>();
        dbHandler = new DBHandler(view_frag.getContext());
        DBHandler.terminal_functions terminal_fun=dbHandler.new terminal_functions();
        Log.d("DBRES", "There 3");
        TermInfoList2= terminal_fun.viewCurrency(selection,selectionArgs);
        int rows = TermInfoList2.size();
        Log.d("DBRES", "There2  " + rows);
        Log.d("DBRES", "There for loop");
        for(int i = 0; i < rows; i ++) {
            User row = null;
            if (i ==0) {
                row = TermInfoList2.get(i);
                Log.d("DBRES", "Currency type: "+  row.getCurrency());
                currency =row.getCurrency();
                Log.d("DBRES", "currency_code : from loaddata "+ currency);

            }
        }
    }
    public  void loadMaddressData()
    {
        String selection = "";
        String[] selectionArgs = {};
        Log.d("DBRES", "There 1");
        List<User> TermInfoList = new ArrayList<User>();
        dbHandler = new DBHandler(view_frag.getContext());
        DBHandler.terminal_functions terminal_fun=dbHandler.new terminal_functions();
        Log.d("DBRES", "There 3");
        TermInfoList= terminal_fun.viewMaddress(selection,selectionArgs);
        int rows = TermInfoList.size();
        Log.d("DBRES", "There2  " + rows);
        Log.d("DBRES", "There for loop");
        for(int i = 0; i < rows; i ++) {
            User row = null;
            if (i ==0) {
                row = TermInfoList.get(i);
                Log.d("DBRES", "Mrchant Address: "+  row.getMerchant_address());
                merchantaddress =row.getMerchant_address();
                Log.d("DBRES", "Mrchant Address: from loaddata "+ merchantaddress);

            }
        }
    }
    public  void loadTmodeData()
    {
        String selection = "";
        String[] selectionArgs = {};
        Log.d("DBRES", "There 1");
        List<User> TermInfoList = new ArrayList<User>();
        dbHandler = new DBHandler(view_frag.getContext());
        DBHandler.terminal_functions terminal_fun=dbHandler.new terminal_functions();
        Log.d("DBRES", "There 3");
        TermInfoList= terminal_fun.viewTmode(selection,selectionArgs);
        int rows = TermInfoList.size();
        Log.d("DBRES", "There2  " + rows);
        Log.d("DBRES", "There for loop");
        for(int i = 0; i < rows; i ++) {
            User row = null;
            if (i ==0) {
                row = TermInfoList.get(i);
                Log.d("DBRES", "Tmode: "+  row.getMode());
                mode =row.getMode();
                Log.d("DBRES", "Mode: from loaddata "+ mode);

            }
        }
    }
    public  void loadCommtypeData()
    {
        String selection = "";
        String[] selectionArgs = {};
        Log.d("DBRES", "There 1");
        List<User> TermInfoList = new ArrayList<User>();
        dbHandler = new DBHandler(view_frag.getContext());
        DBHandler.terminal_functions terminal_fun=dbHandler.new terminal_functions();
        Log.d("DBRES", "There 3");
        TermInfoList= terminal_fun.viewCommtype(selection,selectionArgs);
        int rows = TermInfoList.size();
        Log.d("DBRES", "There2  " + rows);
        Log.d("DBRES", "There for loop");
        for(int i = 0; i < rows; i ++) {
            User row = null;
            if (i ==0) {
                row = TermInfoList.get(i);
                Log.d("DBRES", "CommType: "+  row.getCommtype());
                commtype =row.getCommtype();
                Log.d("DBRES", "CommType: from loaddata "+ commtype);

            }
        }
    }

    public void View_loadData() {

        final TextView View = new TextView(view_frag.getContext());

        View.setBackgroundColor(Color.parseColor("#f8f8f8"));
        View.setTextColor(Color.parseColor("#000000"));

        tv1.setText("TERMINAL INFORMATION ");

        tvtid.setText("Terminal ID:                 " + terminalId.toUpperCase());

        tvmid.setText("Merchant ID:                " + merchantid.toUpperCase());

        tvmname.setText("Merchant Name:         " + merchantname.toUpperCase());

        tvmaddress.setText("Merchant ADD:            " +merchantaddress.toUpperCase());

        tvtmode.setText("Terminal Mode:           " + mode.toUpperCase());

        String Comm_Type=currency;
        String r_currency;
        Log.d(TAG, "Comm_Type............       "+Comm_Type);

        if(Comm_Type.equals("230"))//Local ETB
        {
            r_currency="ETB";
            tvcurrency.setText("Currency Type:             " + r_currency.toUpperCase());
            Log.d(TAG, "r_currency............"+r_currency);
        }
        else if(Comm_Type.equals("840"))//USD
        {
            r_currency="USD";
            tvcurrency.setText("Currency Type:            " + r_currency.toUpperCase());
            Log.d(TAG, "r_currency............"+r_currency);
        }
        else if(Comm_Type.equals("978"))//EUR
        {

            r_currency="EUR";
            tvcurrency.setText("Currency Type:            " + r_currency.toUpperCase());
            Log.d(TAG, "r_currency............"+r_currency);
        }else
        {
            r_currency="ETB";
            tvcurrency.setText("Currency Type:            " + r_currency.toUpperCase());
            Log.d(TAG, "r_currency............"+r_currency);
        }

        tvcomtype.setText("Comm Type:                " + commtype.toUpperCase());

        versionnum.setText("VERSION_No:              "+ MenuActivity.Version_Number);

        }

    }
