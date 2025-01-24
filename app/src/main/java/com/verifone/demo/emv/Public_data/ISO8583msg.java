package com.verifone.demo.emv.Public_data;

import static android.util.Log.*;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.*;

import com.verifone.demo.emv.DBHandler;
import com.verifone.demo.emv.DBTerminal;
import com.verifone.demo.emv.data_handlers.User;
import com.verifone.demo.emv.data_handlers.terminal;
import com.verifone.demo.emv.Utilities.Utility;

import java.nio.ByteBuffer;

public class ISO8583msg {

    public static Context Con1;
    public static Runnable Run1;

    public static String sHdrVars; //FOR SPDH_HEADER_VARS
    public static byte[] sHdrVars1;

    public static String sOptFldVars; //FOR SPDH_OPTIONAL_DATA_FIELD_VARS;
    public static byte[] sOptFldVars1;

    public static String sFID6Vars; //SPDH_FID_6_DATA ;
    public static byte[] sFID6Vars1;

    public static String spackDataVars; //FOR packed SPDH_DATA_VARS ; SUM OF ALL
    public static byte[] spackDataVars1;

    public static String sUnpackDataVars;//for unpached SPDH_DATA_VARS ; SUM OF ALL
    public static byte[] sUnpackDataVars1;

    public static byte[] Separator1;
    public static byte[] Separator2;
    private static final String TAG = "EMVSPDH";

    private static DBHandler dbHandler;
    private static DBTerminal dbTerminal;
    private static String response="9.84POS9TER1              220830121920FO000500001CB0000000000000100001CF071414  1CG@@@@@@@@1CP11CgAPPROVAL1Ch00000007901C61EQ0100E6E321499A98B72A3030";
    static SharedPreferences sharedPreferences;
    private static int row_cnt;
    public static String Mer_name="",Ter_id="",Mer_id="",Mer_address="",commtype="",mode="";
    public static String r_currency1="",currency="",r_currency="";


    public static String Txn_col="";
    public static String Sta_col="";
    public static int timeoutdbrow_cnt;
    public static String timeoutreversaldata="";
    public static String ipadd="";
    public static int portnum;

    public static class SPDH_HEADER_VARS
    {

        //SPDH_HEADER_VARS
        public static String device_Type;
        public static String transmission_No;
        public static String terminal_Id;

        public static String employee_ID;
        public static String current_Date;
        public static String current_Time;

        public static String message_Type;
        public static String message_Subtype;
        public static String transaction_Code;
        public static String processing_Flag1;
        public static String processing_Flag2;
        public static String processing_Flag3;
        public static String response_Code;

       //SPDH_HEADER_VARS sHdrVars;
     }
    public static class SPDH_OPTIONAL_DATA_FIELD_VARS
    {
        //SPDH_OPTIONAL_DATA_FIELD_VARS
        public static String A_customer_Billing_Address;
        public static String B_amount_1;//000000000700
        public static String C_amount2;//
        public String D_application_Account_Type;
        public String E_application_Account_Number;
        public static String F_approval_Code;//
        public static String G_authentication_Code;
        public String H_authentication_Key;
        public String I_data_Encryption_Key;
        public String J_available_Balance;
        public String K_business_Date;
        public String L_check_Type;
        public String M_communications_Key;
        public String N_customer_Id;
        public String O_customer_Id_Type;
        public static String P_draft_Capture_Flag;
        public String Q_echo_Data;
        public String R_card_Type;
        public String S_invoice_Number;
        public String T_invoice_Number_Original;
        public String U_language_code;
        public String V_mail_Download_Key;
        public String W_mail_Text_Download_Data;
        public static String X_iso_Response_Code;
        public String Y_customer_Zip_Code;
        public String Z_address_Verification_status;
        public String a_optional_Data;
        public static String b_pin_Customer;//
        public String c_pin_Suppervisor;
        public static String d_retailer_Id;//RMERCHANT
        public String e_pos_Condition_Code;
        public String f_pin_Length;
        public static String g_response_Display;
        public static String h_sequence_Number;//0010011540
        public static String i_sequence_Number_Original;
        public String j_state_Code;
        public String k_birth_Date;
        public String l_totals_Batch;
        public String m_totals_Day;
        public String n_totals_Employee;
        public String o_totals_Shift;
        public static String q_track2_Customer; ///;4243112438695027=18126261079581400000? not used
        public String r_track2_Supervisor;
        public String s_transaction_Description;
        public String t_pin_Pad_Identifier;
        public String u_acceptor_Posting_Date;
        public String _0_amex_Data_Collection;
        public String _1_ps2000_Data;
        public String _2_track1_Customer;
        public String _3_track1_Supervisor;
        public String _4_industry_Data;
        public String _6_product_Subfields;
        public String _7_product_Subfields;
        public String _8_product_Subfields;
        public String _9_product_Subfields;
        //SPDH_OPTIONAL_DATA_FIELD_VARS sOptFldVars;
    }

     public static class SPDH_FID_6_DATA {
         //SPDH_FID_6_DATA
         public static String A_host_original_data;
         public static String E_pos_entry_mode;
         public static String I_transaction_currency_code;
         public static String O_emv_request_data;
         public static String P_emv_additional_request_data;
         public static String Q_emv_response_data;
         public static String R_response_data;
         public static String B_CVD;
         public static String H_CVDIindicator;
         public static String j_stateid;
         public static String X_PointofService_data;

         //SPDH_FID_6_DATA sFID6Vars;

     }
    //unsigned char *pucOutputBuffer, SPDH_HEADER_VARS *psHeaderVars

    public static void load_Timeoutdata()
    {

        String selection = "";
        String[] selectionArgs = {};

        Log.d("SPDH aMEX", "load_Timeoutdata...");
        List<User> TimeoutList = new ArrayList<User>();
        dbHandler = new DBHandler(Con1);
        DBHandler.user_functions user_fun=dbHandler.new user_functions();

        TimeoutList= user_fun.View_Timeoutdb(selection,selectionArgs);
        int rows = TimeoutList.size();

        for(int i = 0; i < rows; i ++) {
            User row = null;
            if (i ==0) {
                row = TimeoutList.get(i);
                timeoutreversaldata=row.getTimeoutdb();

                Log.d("SPDH..", "timeoutreversaldata:.. "+ timeoutreversaldata);

            }
        }
    }


    public static void loadData()
    {

        String selection = "";
        String[] selectionArgs = {};

        Log.d("DBRES", "There 1");
        List<User> TermInfoList = new ArrayList<User>();
        dbHandler = new DBHandler(Con1);
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
                Ter_id=row.getTerminal_id();
                ISO8583msg.SPDH_HEADER_VARS.terminal_Id=row.getTerminal_id();
                Log.d("DBRES", "SPDH.SPDH_HEADER_VARS.terminal_Id : from loaddata "+  ISO8583msg.SPDH_HEADER_VARS.terminal_Id);

            }
        }
    }
    public static void loadData1()
    {
        String selection = "";
        String[] selectionArgs = {};
        Log.d("DBRES", "Mhere 1");
        List<User> TermInfoList = new ArrayList<User>();
        dbHandler = new DBHandler(Con1);
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
                ISO8583msg.SPDH_OPTIONAL_DATA_FIELD_VARS.d_retailer_Id=row.getMerchant_id();
                Mer_id=row.getMerchant_id();
                Log.d("DBRES", "SPDH.SPDH_HEADER_VARS.terminal_Id : from loaddata "+  ISO8583msg.SPDH_HEADER_VARS.terminal_Id);

            }
        }
    }
    public  void loadMaddressData()
    {
        String selection = "";
        String[] selectionArgs = {};
        Log.d("DBRES", "There 1");
        List<User> TermInfoList = new ArrayList<User>();
        dbHandler = new DBHandler(Con1);
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
                Mer_address =row.getMerchant_address();
                Log.d("DBRES", "Mrchant Address: from loaddata "+ Mer_address);

            }
        }
    }
    public static void loadDatamrname()//merchantname
    {
        String selection = "";
        String[] selectionArgs = {};
        Log.d("DBRES", "Mhere 1");
        List<User> TermInfoList = new ArrayList<User>();
        dbHandler = new DBHandler(Con1);
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
                Mer_name=row.getMerchant_name();
                Log.d("DBRES", "Merchant Nmae : from loaddata "+ Mer_name);

            }
        }
    }
    public static void loadData2()
    {
        String selection = "";
        String[] selectionArgs = {};
        Log.d("DBRES", "Transmission number here1");
        List<terminal> transinfolist = new ArrayList<terminal>();
        dbTerminal = new DBTerminal(Con1);
        DBTerminal.Terminal_functions1 terminal_fun=dbTerminal.new Terminal_functions1();
        Log.d("DBRES", "Transmission number here3");
        transinfolist= terminal_fun.viewtrans(selection,selectionArgs);
        int rows = transinfolist.size();
        Log.d("DBRES", "Transmission number here2  " + rows);
        Log.d("DBRES", "Transmission number here for loop");
        for(int i = 0; i < rows; i ++) {
            terminal row = null;
            if (i ==0) {
                row = transinfolist.get(i);
                Log.d("DBRES", "Transmssion number: "+  row.getTransmission_No());
                SPDH_HEADER_VARS.transmission_No=row.getTransmission_No();
                Log.d("DBRES", "SPDH.SPDH_HEADER_VARS.transmission_No : from loaddata "+  ISO8583msg.SPDH_HEADER_VARS.transmission_No);

            }
        }
    }
    public static void loadData3()
    {
        String selection = "";
        String[] selectionArgs = {};
        Log.d("DBRES", "H_sequence number here1");
        List<terminal> transinfolist = new ArrayList<terminal>();
        dbTerminal = new DBTerminal(Con1);
        DBTerminal.Terminal_functions1 terminal_fun=dbTerminal.new Terminal_functions1();
        Log.d("DBRES", "H_sequence number here3");
        transinfolist= terminal_fun.viewtrans(selection,selectionArgs);
        int rows = transinfolist.size();
        Log.d("DBRES", "H_sequence number here2  " + rows);

        Log.d("DBRES", "H_sequence number for loop");
        for(int i = 0; i < rows; i ++) {
            terminal row = null;
            if (i ==0) {
                row = transinfolist.get(i);
                Log.d("DBRES", "H_sequence number: "+  row.getH_sequence_Number());
                SPDH_OPTIONAL_DATA_FIELD_VARS.h_sequence_Number=row.getH_sequence_Number();
                Log.d("DBRES", "SPDH_OPTIONAL_DATA_FIELD_VARS.H_sequence number : from loaddata "+  ISO8583msg.SPDH_OPTIONAL_DATA_FIELD_VARS.h_sequence_Number);

            }
        }
    }

    public static void loadCurrencyData()
    {
        String selection = "";
        String[] selectionArgs = {};
        Log.d("DBRES", "There 1");
        List<User> TermInfoList2 = new ArrayList<User>();
        dbHandler = new DBHandler(Con1);
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
                ISO8583msg.SPDH_FID_6_DATA.I_transaction_currency_code =row.getCurrency();
                currency =row.getCurrency();
                String Comm_Type=currency;

                if(Comm_Type.equals("230"))//Local ETB
                {
                    r_currency1="ETB";
                }
                else if(Comm_Type.equals("840"))//USD
                {
                    r_currency1="USD";
                }
                else if(Comm_Type.equals("978"))//EUR
                {

                    r_currency1="EUR";

                }else
                {
                    r_currency1="ETB";

                }

            }

        }
        Log.d("DBRES", "Currency type: "+  r_currency);

    }
    public static void loadTmodeData()
    {
        String selection = "";
        String[] selectionArgs = {};
        Log.d("DBRES", "There 1");
        List<User> TermInfoList = new ArrayList<User>();
        dbHandler = new DBHandler(Con1);
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


                //sharedPreferences = Con1.getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
               // SharedPreferences.Editor editor = sharedPreferences.edit();
               // editor.putString("Tmode",mode);
               // editor.apply();


            }
        }
    }
    public  void loadCommtypeData()
    {
        String selection = "";
        String[] selectionArgs = {};
        Log.d("DBRES", "There 1");
        List<User> TermInfoList = new ArrayList<User>();
        dbHandler = new DBHandler(Con1);
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

    public static void BatRecData()
    {
        String selection = "";
        String[] selectionArgs = {};
        Log.d("DBRES", "There 1");
        List<terminal> batrecinfolist= new ArrayList<terminal>();
        dbTerminal = new DBTerminal(Con1);
        DBTerminal.Terminal_functions1 terminal_fun=dbTerminal.new Terminal_functions1();
        Log.d("DBRES", "There 3");
        batrecinfolist= terminal_fun.viewBatrec(selection,selectionArgs);
        int rows = batrecinfolist.size();
        Log.d("DBRES", "There2  " + rows);
        Log.d("DBRES", "There for loop");
        for(int i = 0; i < rows; i ++) {
            terminal row = null;
            if (i ==0) {
                row = batrecinfolist.get(i);
                Log.d("DBRES", "Amount: "+  row.getAmount_1());
                Log.d("DBRES", "approval_Code: "+  row.getApproval_Code());
                Log.d("DBRES", "Authentication_Code: "+  row.getAuthentication_Code());
                Log.d("DBRES", "response_Display: "+  row.getResponse_Display());
                Log.d("DBRES", "Sequence_Number: "+  row.getSequence_Number());
                Log.d("DBRES", "emv_response_data: "+  row.getEmv_response_data());

                SPDH_OPTIONAL_DATA_FIELD_VARS.B_amount_1=row.getAmount_1();
                SPDH_OPTIONAL_DATA_FIELD_VARS.F_approval_Code=row.getApproval_Code();
                SPDH_OPTIONAL_DATA_FIELD_VARS.G_authentication_Code=row.getAuthentication_Code();
                SPDH_OPTIONAL_DATA_FIELD_VARS.g_response_Display=row.getResponse_Display();
                SPDH_OPTIONAL_DATA_FIELD_VARS.h_sequence_Number=row.getSequence_Number();
                SPDH_FID_6_DATA.Q_emv_response_data=row.getEmv_response_data();

                Log.d("DBRES", "From BatRec B_amount_1 "+  SPDH_OPTIONAL_DATA_FIELD_VARS.B_amount_1);
                Log.d("DBRES", "From BatRec F_approval_Code "+  SPDH_OPTIONAL_DATA_FIELD_VARS.F_approval_Code);

            }
        }
    }
    public static void chk_data()
    {
        String selection = null;
        String[] selectionArgs =null;
        //int row_cnt;

        Bundle bundle = new Bundle();
        bundle.putString("", "");

        dbTerminal = new DBTerminal(Con1);
        DBTerminal.Terminal_functions1 terminal_fun=dbTerminal.new Terminal_functions1();

        row_cnt=terminal_fun.row_count(selection,selectionArgs);

        Log.d(TAG,"chack data method user cnt from trans cnt: " + row_cnt);

    }

    public static void chk_timeoutdb()
    {
        String selection = null;
        String[] selectionArgs =null;
        //int row_cnt;

        Bundle bundle = new Bundle();
        bundle.putString("", "");

        dbHandler=new DBHandler(Con1);
        DBHandler.user_functions user_fun=dbHandler.new user_functions();
        Log.d(TAG,"we are here time out: " + timeoutdbrow_cnt);
        timeoutdbrow_cnt=user_fun.Time_outdb_count(selection,selectionArgs);
        Log.d(TAG,"chack data timeoutdb row_cnt: " + timeoutdbrow_cnt);

    }
    public static void chk_data1()
    {
        String selection = null;
        String[] selectionArgs =null;
        //int row_cnt;

        Bundle bundle = new Bundle();
        bundle.putString("", "");

        dbTerminal = new DBTerminal(Con1);
        DBTerminal.Terminal_functions1 terminal_fun=dbTerminal.new Terminal_functions1();

        row_cnt=terminal_fun.row_count1(selection,selectionArgs);

        Log.d(TAG,"h_seq chack data method user cnt from trans cnt: " + row_cnt);

     }

    public static void txn_loadData()
    {
        String selection = "";
        String[] selectionArgs = {};
        Log.d("DBRES", "txn here Started ");
        List<terminal> txninfolist = new ArrayList<terminal>();
        dbTerminal = new DBTerminal(Con1);

        Log.d("DBRES", "txn SPDH here Started 1 ");
        DBTerminal.Terminal_functions1 Terminal_fun=dbTerminal.new Terminal_functions1();
        Log.d("DBRES", "txn here3");
        txninfolist= Terminal_fun.viewtxn(selection,selectionArgs);
        int rows = txninfolist.size();
        Log.d("DBRES", "txn here2  " + rows);
        Log.d("DBRES", "txn here for loop");
        for(int i = 0; i < rows; i ++) {
            terminal row = null;
            if (i > -1) {
                row = txninfolist.get(i);
                Log.d("DBRES", "Transaction_type : "+  row.getTransaction_type());
                //Log.d("DBRES", "Preauth : "+  row.getPreauth());
                // Log.d("DBRES", "Preauthcmp : "+  row.getPreauthcmp());
                Log.d("DBRES", "Status : "+  row.getStatus());

                Txn_col=row.getTransaction_type();

                Sta_col=row.getStatus();

                Log.d("DBRES", "txn col: "+Txn_col);
                Log.d("DBRES", "status col:     "+Sta_col);
           if(Txn_col.equals("ManualCard"))
            {

                sharedPreferences = Con1.getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("MTxn_col",Txn_col);
                editor.putString("MSta_col",Sta_col);
                editor.apply();
           }

            }
            else
            {
                Log.d("DBRES", "Finished txn: ");
            }
        }
    }

    public static void IP_port_loadData()
    {
        String selection = "";
        String[] selectionArgs = {};
        Log.d("DBRES", "IPPORT here Started ");
        List<terminal> IPportinfolist = new ArrayList<terminal>();
        dbTerminal = new DBTerminal(Con1);

        Log.d("DBRES", "IPPORT SPDH here Started 4 ");
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
                //Log.d("DBRES", "Preauth : "+  row.getPreauth());
                // Log.d("DBRES", "Preauthcmp : "+  row.getPreauthcmp());
                Log.d("DBRES", "PORT : "+  row.getPort());

                ipadd=row.getIpaddress();
                String portn=row.getPort();
                portnum=Integer.parseInt(portn);

            }
            else
            {
                Log.d("DBRES", "Finished ip: ");
            }
        }
    }


    public ISO8583msg(Context con){
      Con1= con;
   }


    public static void SPDH_PackHeader()
    {
        SPDH_HEADER_VARS psHeaderVars=new SPDH_HEADER_VARS();

        //Log.d(TAG, "Wellcome to SPDH_PackHeader.....................................................................");
        psHeaderVars.device_Type="9.";


        psHeaderVars.employee_ID="      ";
        psHeaderVars.message_Type="F";
        psHeaderVars.message_Subtype="O";
        psHeaderVars.transaction_Code="00";
        psHeaderVars.processing_Flag1="0";
        psHeaderVars.processing_Flag2="5";
        psHeaderVars.processing_Flag3="0";
        psHeaderVars.response_Code="000";


        String deviceType = String.valueOf(psHeaderVars.device_Type);

        //String transmissionNo;
// START.............................................................................REGISTERING transmission_No
        int transNo;
        chk_data();
        if(row_cnt>0) {
            loadData2();//calling transmission_No
            psHeaderVars.transmission_No = String.valueOf(SPDH_HEADER_VARS.transmission_No);//feach from database

            transNo=Integer.parseInt(SPDH_HEADER_VARS.transmission_No)+1;

            if (transNo == 100) {
                transNo = transNo - 90;
            }
            Log.d("DBRES", "transmission_No row_cnt>0: " );
        }else {
            transNo = 10;
            Log.d("DBRES", "transmission_No row_cnt<0: " );
        }
            //String transNo1=transNo.getText().toString();
Log.d("DBRES", "transmission_No Start to insert value to database: " + transNo);

         //transNo=transNo.getText().toString();

            String TABLE_NAME_TRANS = "TRANS";

            String ID_COL = "id";

            String TRANSNO_COL = "transmission_No";

            dbTerminal.query = "CREATE TABLE " + TABLE_NAME_TRANS + " ("

                     + ID_COL + " INTEGER  PRIMARY KEY AUTOINCREMENT,"

                    + TRANSNO_COL + " TEXT )";

            dbTerminal.TABLE_NAME_TRANS = "TRANS";

            DBTerminal.Terminal_functions1 terminal_fun = dbTerminal.new Terminal_functions1();
            terminal_fun.regtrans(transNo);
            Log.d("DBRES", "Created successfully: transmission_No = " + transNo);

            String transmissionNo = String.valueOf(transNo);
            Log.d("DBRES", "transmission_No from database after add: " + transNo);
        //..................................................................................END


        loadData();//calling terminal id.................................................START

       for(int i=psHeaderVars.terminal_Id.length(); i<16;i++) {

           psHeaderVars.terminal_Id=psHeaderVars.terminal_Id + " ";

        }
        String terminalId = String.valueOf( psHeaderVars.terminal_Id);
        Log.d("DBRES", "Terminal_id from database after add: " + psHeaderVars.terminal_Id);
        Log.d("DBRES", "Terminal_id from database: " +psHeaderVars.terminal_Id.length());



        String employeeID = String.valueOf(psHeaderVars.employee_ID);

        //............................................................................................start time and date
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        String datetime = dateformat.format(c.getTime());

        String time1="-";
        String time2=":";
        String time3=" ";
        String time4="";
        String datetime1=datetime.replace(time1,time4);
        String datetime2=datetime1.replace(time2,time4);
        String datetime3=datetime2.replace(time4,time4);

        //datetime3.substring(0, 4);
        psHeaderVars.current_Date=datetime3.substring(0, 6);
        Log.d("DBRES", "Terminal_id from database after add: " + psHeaderVars.terminal_Id);
        Log.d(TAG, ".............................. date  ="+ psHeaderVars.current_Date);
        //  2014-11-11 00:47:55  2208-29 14:41:22
        psHeaderVars.current_Time = datetime3.substring(6, datetime3.length());
        Log.d(TAG, "..............................time  ="+ psHeaderVars.current_Time);


        //..........................................................................................end time and date


        String currentDate = String.valueOf(psHeaderVars.current_Date);

        String currentTime = String.valueOf(psHeaderVars.current_Time);

        String messageType = String.valueOf(psHeaderVars.message_Type);

        String messageSubtype = String.valueOf(psHeaderVars.message_Subtype);

        String transactionCode = String.valueOf(psHeaderVars.transaction_Code);

        String processingFlag1 = String.valueOf(psHeaderVars.processing_Flag1);

        String processingFlag2 = String.valueOf(psHeaderVars.processing_Flag2);

        String processingFlag3 = String.valueOf(psHeaderVars.processing_Flag3);

        String responseCode = String.valueOf(psHeaderVars.response_Code);

                sHdrVars =String.valueOf(deviceType + transmissionNo + terminalId + employeeID
                + currentDate+ currentTime + messageType + messageSubtype + transactionCode
                + processingFlag1 + processingFlag2 +responseCode + processingFlag3);

                sHdrVars1=sHdrVars.getBytes();


    }
    public  String fillgapsequence(String data,int size){
        String result=data;
        while(!(result.length()==size)){
            result="0"+result;
        }
        return result;
    }
    public void SPDH_PackOPTIONALDATA() {
        SPDH_OPTIONAL_DATA_FIELD_VARS psOptionaldata=new SPDH_OPTIONAL_DATA_FIELD_VARS();
        //SPDH_PackSubField("","","");
        Log.d(TAG, "Wellcome to SPDH_PackOPTIONALDATA.....................................................................");

        psOptionaldata.F_approval_Code="";
        psOptionaldata.b_pin_Customer="";


      // SPDH_OPTIONAL_DATA_FIELD_VARS.h_sequence_Number="h"+fillgapsequence(String.valueOf(sequence),9)+"0";

       String SQ;
       int sequence=0;
        chk_data1();
        if(row_cnt>0) {
            loadData3();//calling transmission_No
            Log.d("DBRES", " row_cnt: " +row_cnt);
            sequence=row_cnt+1;
            SQ = fillgapsequence(String.valueOf(sequence), 9) + "0";
            Log.d("DBRES", "h_sequence_Number row_cnt>0: ");
           /* if(sequence<10) {
                SQ = fillgapsequence(String.valueOf(sequence), 9) + "0";
                Log.d("DBRES", "h_sequence_Number row_cnt>0: ");
            }else
            {
                SQ = fillgapsequence(String.valueOf(sequence), 8) + "00";
                Log.d("DBRES", "h_sequence_Number row_cnt>0: ");
            }

            */
            } else
            {
           SQ=fillgapsequence(String.valueOf(sequence),9)+"0";
            Log.d("DBRES", "h_sequence_Number row_cnt<0 Value of sq:  "+SQ);
        }
        //String transNo1=transNo.getText().toString();

        Log.d("DBRES", "h_sequence_Number Start to insert value to database: " + SQ);

        //transNo=transNo.getText().toString();

        String TABLE_NAME_TRANS1 = "TRANS1";

        String ID_COL = "id";

        String TRANSSEQNO_COL = "h_sequence_Number";

        dbTerminal.query = "CREATE TABLE " + TABLE_NAME_TRANS1 + " ("

                + ID_COL + " INTEGER  PRIMARY KEY AUTOINCREMENT,"

                + TRANSSEQNO_COL + " TEXT )";

        dbTerminal.TABLE_NAME_TRANS1 = "TRANS1";

        DBTerminal.Terminal_functions1 terminal_fun = dbTerminal.new Terminal_functions1();
        terminal_fun.regtrans1(SQ);
        Log.d("DBRES", "Created successfully: transmission_No = " + SQ);

        //String h_sequence_Number = String.valueOf(SQ);
        psOptionaldata.h_sequence_Number=String.valueOf(SQ);
        Log.d("DBRES", "h_sequence_Number from database after add: " + psOptionaldata.h_sequence_Number);
        //..................................................................................END

        psOptionaldata.i_sequence_Number_Original="";
        //psOptionaldata.q_track2_Customer=";4243112438695027=18126261079581400000?";
        //copy string values
        Log.d(TAG, ".............................. Original B_amount_1  ="+ psOptionaldata.B_amount_1);
        byte[] B_amount = psOptionaldata.B_amount_1.getBytes();
        Log.d(TAG, ".............................. B_amount_1  ="+ Utility.byte2HexStr(B_amount));
        byte[] C_amount = psOptionaldata.C_amount2.getBytes();
        Log.d(TAG, ".............................. C_amount_2  ="+ Utility.byte2HexStr(C_amount));
        byte[] F_appCode = ("F" +psOptionaldata.F_approval_Code).getBytes();
        byte[] b_pin_Custor = ("b" +psOptionaldata.b_pin_Customer).getBytes();

        loadData1();
        Log.d("DBRES", "MERCHANT_id from database: " +psOptionaldata.d_retailer_Id);
        byte[] d_retailerId = ("d" +psOptionaldata.d_retailer_Id).getBytes();
        Log.d(TAG, ".............................. d_retailerId  ="+ Utility.byte2HexStr(d_retailerId));

        byte[] h_sequ_Number = ("h" +psOptionaldata.h_sequence_Number).getBytes();
        byte[] i_sequ_Num_Original = (psOptionaldata.i_sequence_Number_Original).getBytes();
        byte[] q_track2Customer = (psOptionaldata.q_track2_Customer).getBytes();
        Log.d(TAG, ".............................. q_track2_Customer  ="+ Utility.byte2HexStr(q_track2Customer));

        String[] Separator= new String[1];
        Separator[0]="1C";
        Separator1= Utility.hexStr2Byte(Separator[0]);

       ByteBuffer sOptFldVars = ByteBuffer.allocate(Separator1.length +B_amount.length +Separator1.length +C_amount.length +Separator1.length
                + d_retailerId.length +Separator1.length+ h_sequ_Number.length +Separator1.length+ q_track2Customer.length +Separator1.length);
        sOptFldVars.put(Separator1);
        sOptFldVars.put(B_amount);
        sOptFldVars.put(Separator1);
        sOptFldVars.put(C_amount);
        //sOptFldVars.put(F_appCode);
        //sOptFldVars.put(Separator1);
       // sOptFldVars.put(b_pin_Custor);
        sOptFldVars.put(Separator1);
        sOptFldVars.put(d_retailerId);
        sOptFldVars.put(Separator1);
        sOptFldVars.put(h_sequ_Number);
        sOptFldVars.put(Separator1);
        //sOptFldVars.put(i_sequ_Num_Original);
        //sOptFldVars.put(Separator1);
        sOptFldVars.put(q_track2Customer);
        sOptFldVars.put(Separator1);

        sOptFldVars1 = sOptFldVars.array();

        // Log.d(TAG, "SPDH_PackOPTIONALDATA values  ="+sOptFldVars);

       // Log.d(TAG, "SPDH_PackOPTIONALDATA length of SPDH_PackOPTIONALDATA VARS  ="+sOptFldVars.length());

    }

    //=======================================================================================================
    public void SPDH_PackFID6Fields()
    {
        SPDH_FID_6_DATA psFID6Data=new SPDH_FID_6_DATA();
        //SPDH_PackEIOPSubField S =new SPDH_PackEIOPSubField();
        Log.d(TAG, "Wellcome to SPDH_PackFID6Fields.....................................................................");

        psFID6Data.A_host_original_data="";
        psFID6Data.E_pos_entry_mode="051";
       // psFID6Data.I_transaction_currency_code="230";
        loadCurrencyData();

        String Comm_Type= ISO8583msg.SPDH_FID_6_DATA.I_transaction_currency_code;
        Log.d(TAG, "Comm_Type............       "+Comm_Type);
        //(type.equals("0810"))
        if(Comm_Type.equals("230"))//Local ETB
        {
            psFID6Data.I_transaction_currency_code="230";
            r_currency="ETB";
            Log.d(TAG, "ETB is Selected............"+psFID6Data.I_transaction_currency_code);
            Log.d(TAG, "r_currency............"+r_currency);
        }
        else if(Comm_Type.equals("840"))//USD
        {
            psFID6Data.I_transaction_currency_code="840";
            r_currency="USD";
            Log.d(TAG, "USD is Selected............"+psFID6Data.I_transaction_currency_code);
            Log.d(TAG, "r_currency............"+r_currency);
        }
        else if(Comm_Type.equals("978"))//EUR
        {
            psFID6Data.I_transaction_currency_code="978";
            r_currency="EUR";
            Log.d(TAG, "EUR is Selected............"+psFID6Data.I_transaction_currency_code);
            Log.d(TAG, "r_currency............"+r_currency);
        }else
        {
           psFID6Data.I_transaction_currency_code="230";
            r_currency="ETB";
           Log.d(TAG, "Defualt Comm_Type is Selected............"+psFID6Data.I_transaction_currency_code);
            Log.d(TAG, "r_currency............"+r_currency);
       }


        psFID6Data.Q_emv_response_data="";
        psFID6Data.R_response_data="";
        psFID6Data.B_CVD="";
        psFID6Data.H_CVDIindicator="";
        psFID6Data.j_stateid="";
        psFID6Data.X_PointofService_data="000000";

        // pack FID6 subfields in order
        byte[] A_host = ('A'+psFID6Data.A_host_original_data).getBytes();
        byte[] E_pos = ('E'+psFID6Data.E_pos_entry_mode).getBytes();

        byte[] I_transaction = ('I'+psFID6Data.I_transaction_currency_code).getBytes();
        byte[] O_emv_request = psFID6Data.O_emv_request_data.getBytes();
        Log.d(TAG, ".............................. O_emv_request_data  ="+ Utility.byte2HexStr(O_emv_request));

        byte[] P_emv_additional = psFID6Data.P_emv_additional_request_data.getBytes();
        Log.d(TAG, ".............................. P_emv_additional_request_data  ="+ Utility.byte2HexStr(P_emv_additional));

        byte[] Q_emv_response = ('Q'+psFID6Data.Q_emv_response_data).getBytes();
        byte[] R_response = ('R'+psFID6Data.R_response_data).getBytes();
        byte[] BCVD = ('R'+psFID6Data.B_CVD).getBytes();
        byte[] HCVDIindicator = ('H'+psFID6Data.H_CVDIindicator).getBytes();
        byte[] X_PointofService = ('X'+psFID6Data.X_PointofService_data).getBytes();

        String[] Separator6= new String[1];
        Separator6[0]="1E";
        Separator2= Utility.hexStr2Byte(Separator6[0]);

        String f6="6";
        byte[] F6=f6.getBytes();

        ByteBuffer sFID6Vars = ByteBuffer.allocate(f6.length()+ Separator2.length + E_pos.length +Separator2.length + I_transaction.length +Separator2.length
                + O_emv_request.length +Separator2.length+ P_emv_additional.length +Separator2.length + X_PointofService.length);
        sFID6Vars.put(F6);
        sFID6Vars.put(Separator2);
        sFID6Vars.put(E_pos);
        sFID6Vars.put(Separator2);
        sFID6Vars.put(I_transaction);
        sFID6Vars.put(Separator2);
        sFID6Vars.put(O_emv_request);
        sFID6Vars.put(Separator2);
        sFID6Vars.put(P_emv_additional);
        sFID6Vars.put(Separator2);
        sFID6Vars.put(X_PointofService);

        sFID6Vars1 = sFID6Vars.array();

        //sFID6Vars1=concat(Separator2 ,E_pos,  I_transaction, +Separator2 + O_emv_request +Separator2+ P_emv_additional +Separator2 + X_PointofService);

        //Log.d(TAG, "SPDH_PackFID6Fields length of SPDH_HEADER_VARS  ="+sFID6Vars.length());

    }

    //======================================================================================================================================
    public void PackedMessage(){

        //SPDH_PackHeader();
        //d(TAG, "amount"+);

        d(TAG, "Wellcome to SPDH_PackHeader.....................................................................");
        d(TAG, "SPDH_PackHeader values Converted Byte 2HexStr  "+Utility.byte2HexStr(sHdrVars1));
        d(TAG, "SPDH_PackHeader length of SPDH_PackHeader  ="+sHdrVars1.length);

        //SPDH_PackOPTIONALDATA();
        d(TAG, "Wellcome to SPDH_PackOPTIONALDATA.....................................................................");
        d(TAG, "SPDH_PackOPTIONALDATA values Converted Byte 2HexStr  "+Utility.byte2HexStr(sOptFldVars1));
        d(TAG, "SPDH_PackOPTIONALDATA length of SPDH_PackHeader  ="+sOptFldVars1.length);

        //SPDH_PackFID6Fields();
        d(TAG, "Wellcome to SPDH_PackFID6Fields.....................................................................");
        d(TAG, "SPDH_PackFID6Fields values Converted Byte 2HexStr  "+Utility.byte2HexStr(sFID6Vars1));
        d(TAG, "SPDH_PackFID6Fields length of SPDH_PackHeader  ="+sFID6Vars1.length);

        // hex_dump("","","");
        d(TAG, "Wellcome to PackedMessage.....................................................................");
       ByteBuffer byteBuffer =ByteBuffer.allocate(sHdrVars1.length + sOptFldVars1.length + sFID6Vars1.length);
             byteBuffer.put(sHdrVars1);
             byteBuffer.put(sOptFldVars1);
             byteBuffer.put(sFID6Vars1);

        spackDataVars1 = byteBuffer.array();
        //Log.d(TAG, "PackedMessage of spackDataVars values1   "+new String(String.valueOf(spackDataVars1)));
        Log.d(TAG, "PackedMessage   "+Utility.byte2HexStr(spackDataVars1));
        Log.d(TAG, "PackedMessage length of spackDataVars  ="+spackDataVars1.length);
       // Log.d(TAG, "PackedMessage of spackDataVars values3   "+Arrays.toString(byteBuffer.array()));
        //Log.d(TAG, "PackedMessage length of spackDataVars  ="+spackDataVars1);
       }
    //======================================================================================================================================

    public void unPackedMessage() {

     //String response="9.84POS9TER1              220830121920FO00050000B000000000000010000F071414  G@@@@@@@@P1gAPPROVALh00000007906Q0100E6E321499A98B72A3030";
        SPDH_HEADER_VARS uppsHeaderVars=new SPDH_HEADER_VARS();
        int iMessegeSize=response.length();
        Log.d(TAG, "Response message size"+iMessegeSize);

        if( iMessegeSize < 48)
        {
            Log.d(TAG, "invalid argument iMessageSize"+iMessegeSize);

        }else {
            uppsHeaderVars.device_Type= response.substring(0,2);
            Log.d(TAG, "device_Type  "+uppsHeaderVars.device_Type);
            uppsHeaderVars.transmission_No= response.substring(2,4);
            Log.d(TAG, "uppsHeaderVars.transmission_No  "+uppsHeaderVars.transmission_No);
            uppsHeaderVars.terminal_Id=response.substring(4,20);
            Log.d(TAG, "terminal_Id  "+uppsHeaderVars.terminal_Id);
            uppsHeaderVars.employee_ID= response.substring(20,26);
            Log.d(TAG, "employee_ID  "+uppsHeaderVars.employee_ID);
            uppsHeaderVars.current_Date= response.substring(26,32);
            Log.d(TAG, "current_Date  "+uppsHeaderVars.current_Date);
            uppsHeaderVars.current_Time= response.substring(32,38);
            Log.d(TAG, "current_time  "+uppsHeaderVars.current_Time);
            uppsHeaderVars.message_Type= response.substring(38,39);
            Log.d(TAG, "message_Type  "+uppsHeaderVars.message_Type);
            uppsHeaderVars.message_Subtype= response.substring(39,40);
            Log.d(TAG, "message_Subtype  "+uppsHeaderVars.message_Subtype);
            uppsHeaderVars.transaction_Code= response.substring(40,41);
            Log.d(TAG, "transaction_Code  "+uppsHeaderVars.transaction_Code);
            uppsHeaderVars.processing_Flag1= response.substring(41,42);
            Log.d(TAG, "f1  "+uppsHeaderVars.processing_Flag1);
            uppsHeaderVars.processing_Flag2= response.substring(42,43);
            Log.d(TAG, "processing_Flag2  "+uppsHeaderVars.processing_Flag2);
            uppsHeaderVars.processing_Flag3= response.substring(43,44);
            Log.d(TAG, "processing_Flag3  "+uppsHeaderVars.processing_Flag3);
            uppsHeaderVars.response_Code= response.substring(44,48);
            Log.d(TAG, "response_Code  "+uppsHeaderVars.response_Code);


        }

    }
    public void SPDH_UnPackSubField() {
        SPDH_OPTIONAL_DATA_FIELD_VARS upOptionaldata = new SPDH_OPTIONAL_DATA_FIELD_VARS();

        SPDH_FID_6_DATA uppsFID6Data=new SPDH_FID_6_DATA();

               String response1 = response.substring(48);

                Log.d(TAG, " VALUES AFTER SUBSTRING OF HEADER VALUS.................... " + response1);

                char fieldType;
                int iCounter = 0;
                String filedA ="",filedB="",filedF="",filedG="",filedP="",filedg="",filedh="",filed1B="",filedH=""
                        ,filedE="",filedI="",filedO="",filed1P="",filedQ="",filedX="",filedR="";
 //response="9.84POS9TER1              220830121920FO000500001CB0000000000000100001CF071414  1CG@@@@@@@@1CP11CgAPPROVAL1Ch00000007901C61EQ0100E6E321499A98B72A3030";";
                    Log.d(TAG, "Doooooo  Started UnPackOPTIONALSubField .................... ");

 do {
                        //iCounter++;

  if (response1.charAt(iCounter) == '1' && response1.charAt(iCounter + 1) == 'C') {

      iCounter = iCounter + 2;// skipping the field separator 1c
      fieldType = response1.charAt(iCounter); //value of Case fieldType

      Log.d(TAG, "filed values filed type 1C.................... " + fieldType);
      //Log.d(TAG, " filed DATA values .................... "+fielddata);
      iCounter = iCounter + 3;  //filed type incrment

      switch (fieldType) {
          //!(x && y) is same as !x || !y
          case 'A':
              do {
                  filedA = filedA + response1.charAt(iCounter);//sum of filed data
                  iCounter++;
              } while (!(response1.charAt(iCounter) == '1' && response1.charAt(iCounter + 1) == 'C'));
              upOptionaldata.A_customer_Billing_Address = filedA;
              break;

          case 'B':
              do {
                  filedB = filedB + response1.charAt(iCounter);//sum of filed data
                  iCounter++;

              } while (!(response1.charAt(iCounter) == '1' && response1.charAt(iCounter + 1) == 'C'));
              Log.d(TAG, "Icounter  ........and Icounter +1............ " + response1.charAt(iCounter) + " && " + response1.charAt(iCounter + 1));
              upOptionaldata.B_amount_1 = filedB;  //response1.substring(iCounter, iCounter+19);
              Log.d(TAG, "B_amount_1 .................... " + upOptionaldata.B_amount_1);
              break;

          case 'F':
              Log.d(TAG, "F_approval_Code ...here................. ");
              do {
                  filedF = filedF + String.valueOf(response1.charAt(iCounter));//sum of filed data
                  iCounter++;
              } while (!(response1.charAt(iCounter) == '1' && response1.charAt(iCounter + 1) == 'C'));
              upOptionaldata.F_approval_Code = filedF;//"F" + response1.substring(iCounter+1, iCounter+9);
              Log.d(TAG, "F_approval_Code .................... " + upOptionaldata.F_approval_Code);
              break;

          case 'G':
              do {
                  filedG = filedG + String.valueOf(response1.charAt(iCounter));//sum of filed data
                  iCounter++;
              } while (!(response1.charAt(iCounter) == '1' && response1.charAt(iCounter + 1) == 'C'));
              upOptionaldata.G_authentication_Code = filedG;//"G" + response1.substring(iCounter+1, iCounter+9);
              Log.d(TAG, "G_authentication_Code .................... " + upOptionaldata.G_authentication_Code);
              break;

          case 'P':
              do {
                  filedP = filedP + String.valueOf(response1.charAt(iCounter));//sum of filed data
                  iCounter++;
              } while (!(response1.charAt(iCounter) == '1' && response1.charAt(iCounter + 1) == 'C'));
              upOptionaldata.P_draft_Capture_Flag = filedP;//"P" + response1.substring(iCounter+1, iCounter+2);
              Log.d(TAG, "P_draft_Capture_Flag .................... " + upOptionaldata.P_draft_Capture_Flag);
              break;

          case 'g':
              do {
                  filedg = filedg + String.valueOf(response1.charAt(iCounter));//sum of filed data
                  iCounter++;
              } while (!(response1.charAt(iCounter) == '1' && response1.charAt(iCounter + 1) == 'C'));
              upOptionaldata.g_response_Display = filedg;//"g" + response1.substring(iCounter+1, iCounter+9);
              Log.d(TAG, "g_response_Display .................... " + upOptionaldata.g_response_Display);
              break;

          case 'h':
              do {
                  filedh = filedh + response1.charAt(iCounter);//sum of filed data
                  iCounter++;
              } while (!(response1.charAt(iCounter) == '1' && response1.charAt(iCounter + 1) == 'C'));
              upOptionaldata.h_sequence_Number = filedh;//"h" + response1.substring(iCounter+1, iCounter+11);
              Log.d(TAG, "h_sequence_Number .................... " + upOptionaldata.h_sequence_Number);
              break;

          default:
              Log.d(TAG, "skipping unknown  filed type From 1C.................... " + fieldType);
              Log.d(TAG, "icounter ..fieldType 6.and value.................    " + iCounter +"  "+response1.charAt(iCounter));
              //CopyField = false;
              break;
      }
      }
     else if (response1.charAt(iCounter-2) == '1' && response1.charAt(iCounter-1) == 'E') {
                            Log.d(TAG, "  Eles if Condtion.started....response1.charAt(iCounter+1............... 1E   "+response1.charAt(iCounter+1));

                           int iCounter1 = iCounter;// skipping the field separator 1c
                            fieldType = response1.charAt(iCounter1); //value of Case fieldType

                            Log.d(TAG, "filed values filed type 1E.................... " + fieldType);
                            //iCounter++;  //filed type incrment
                            iCounter = iCounter + 1;
                            switch (fieldType) {

                                case 'B':
                                    do {
                                        filed1B = filedB + response1.charAt(iCounter);//sum of filed data
                                        iCounter++;
                                    } while (!(response1.charAt(iCounter) == '1' && response1.charAt(iCounter + 1) == 'E'));
                                    uppsFID6Data.B_CVD = filed1B;
                                    Log.d(TAG, "B_CVD .................... " + uppsFID6Data.B_CVD);
                                    break;

                                case 'H':
                                    do {
                                        filedH = filedH + response1.charAt(iCounter);//sum of filed data
                                        iCounter++;
                                    } while (!(response1.charAt(iCounter) == '1' && response1.charAt(iCounter + 1) == 'E'));
                                    uppsFID6Data.H_CVDIindicator = filedH;
                                    Log.d(TAG, "H_CVDIindicator .................... " + uppsFID6Data.H_CVDIindicator);
                                    break;

                                case 'E':
                                    do {
                                        filedE = filedE + response1.charAt(iCounter);//sum of filed data
                                        iCounter++;
                                    } while (!(response1.charAt(iCounter) == '1' && response1.charAt(iCounter + 1) == 'E'));
                                    uppsFID6Data.E_pos_entry_mode = filedE;
                                    Log.d(TAG, "E_pos_entry_mode .................... " + uppsFID6Data.E_pos_entry_mode);
                                    break;

                                case 'I':
                                    do {
                                        filedI = filedI + response1.charAt(iCounter);//sum of filed data
                                        iCounter++;
                                    } while (!(response1.charAt(iCounter) == '1' && response1.charAt(iCounter + 1) == 'E'));

                                    uppsFID6Data.I_transaction_currency_code = filedI;
                                    Log.d(TAG, "I_transaction_currency_code ............ " + uppsFID6Data.I_transaction_currency_code);
                                    break;
                                case 'O':
                                    do {
                                        filedO = filedO + response1.charAt(iCounter);//sum of filed data
                                        iCounter++;
                                    } while (!(response1.charAt(iCounter) == '1' && response1.charAt(iCounter + 1) == 'E'));

                                    SPDH_FID_6_DATA.O_emv_request_data = filedO;
                                    Log.d(TAG, "O_emv_request_data ............ " + SPDH_FID_6_DATA.O_emv_request_data);
                                    break;

                                case 'P':
                                    do {
                                        filed1P = filedP + response1.charAt(iCounter);//sum of filed data
                                        iCounter++;
                                    } while (!(response1.charAt(iCounter) == '1' && response1.charAt(iCounter + 1) == 'E'));

                                    SPDH_FID_6_DATA.P_emv_additional_request_data = filed1P;
                                    Log.d(TAG, "P_emv_additional_request_data ............ " + SPDH_FID_6_DATA.P_emv_additional_request_data);
                                    break;

                                case 'Q':
                                    do {
                                        filedQ = filedQ + response1.charAt(iCounter);//sum of filed data
                                        iCounter++;
                                   } while (iCounter<response1.length() && !(response1.charAt(iCounter) == '1' && response1.charAt(iCounter + 1) == 'E'));

                                        uppsFID6Data.Q_emv_response_data = filedQ;
                                        Log.d(TAG, "Q_emv_response_data .................... " + uppsFID6Data.Q_emv_response_data);
                                    break;

                                case 'R':
                                    do {
                                        filedR = filedR + response1.charAt(iCounter);//sum of filed data
                                        iCounter++;
                                    } while (!(response1.charAt(iCounter) == '1' && response1.charAt(iCounter + 1) == 'E'));
                                    uppsFID6Data.R_response_data = filedR;
                                    Log.d(TAG, "R_response_data .................... " + uppsFID6Data.R_response_data);
                                    break;

                                case 'X':
                                    do {
                                        filedX = filedX + response1.charAt(iCounter);//sum of filed data
                                        iCounter++;
                                    } while (iCounter<response1.length() && !(response1.charAt(iCounter) == '1' && response1.charAt(iCounter + 1) == 'E'));

                                    uppsFID6Data.X_PointofService_data = filedX;
                                    Log.d(TAG, "X_PointofService_data .................... " + uppsFID6Data.X_PointofService_data);
                                    break;

                                default:
                                    Log.d(TAG, "skipping unknown  filed type from field 6 1E.................... " + fieldType);
                                    //CopyField = false;
                                    break;
                            }

                        } else {
      //Log.d(TAG, "icounter....................    " + iCounter);
                     iCounter++;
      //Log.d(TAG, "icounter++....................    " + iCounter);
                        }
                        //iCounter++;
  }while (iCounter <response1.length());
                        Log.d(TAG, "length and index is Equal .................... ");


       /* String TABLE_NAME_BATREC = "BatRec";

        String ID_COL = "id";
        String AMOUNT_COL = "amount_1";
        String APPROVALCODE_COL = "approval_Code";
        String AUTHENTICATION_CODE_COL ="authentication_Code";
        String RESPONSE_DISPLAY_COL = "response_Display";
        String SEQUENCE_NUMBER_COL = "sequence_Number";
        String EMV_RESPONSEDATA_COL="emv_response_data";

        dbTerminal.query = "CREATE TABLE " + TABLE_NAME_BATREC + " ("

                + ID_COL + " INTEGER  PRIMARY KEY AUTOINCREMENT,"
                + AMOUNT_COL + " TEXT,"
                + APPROVALCODE_COL + " TEXT,"
                + AUTHENTICATION_CODE_COL + " TEXT,"
                + RESPONSE_DISPLAY_COL + " TEXT,"
                + SEQUENCE_NUMBER_COL + " TEXT,"
                + EMV_RESPONSEDATA_COL + " TEXT )";

        dbTerminal.TABLE_NAME_BATREC = "BatRec";

        DBTerminal.Terminal_functions1 terminal_fun = dbTerminal.new Terminal_functions1();
        terminal_fun.regBatRec(upOptionaldata.B_amount_1, upOptionaldata.F_approval_Code,
                upOptionaldata.G_authentication_Code, upOptionaldata.g_response_Display,
                upOptionaldata.h_sequence_Number, uppsFID6Data.Q_emv_response_data);
        Log.d("DBRES", "BatRec Created successfully:");

        BatRecData();
        ///SPDH_OPTIONAL_DATA_FIELD_VARS.B_amount_1=row.getAmount_1();
        //SPDH_OPTIONAL_DATA_FIELD_VARS.F_approval_Code=row.getApproval_Code();
        //SPDH_OPTIONAL_DATA_FIELD_VARS.G_authentication_Code=row.getAuthentication_Code();
        //SPDH_OPTIONAL_DATA_FIELD_VARS.g_response_Display=row.getResponse_Display();
       // SPDH_OPTIONAL_DATA_FIELD_VARS.h_sequence_Number=row.getSequence_Number();
       // SPDH_FID_6_DATA.Q_emv_response_data=row.getEmv_response_data();
        Log.d("DBRES", ":Q_emv_response_data  From the Database.... "+SPDH_FID_6_DATA.Q_emv_response_data);

        */
    }
}
