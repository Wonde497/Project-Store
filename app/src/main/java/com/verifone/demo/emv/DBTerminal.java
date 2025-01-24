package com.verifone.demo.emv;

import android.content.ContentValues;

import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.verifone.demo.emv.data_handlers.terminal;

import java.util.ArrayList;
import java.util.List;

public class DBTerminal extends SQLiteOpenHelper {

    static String TAG = "DBTERMINAL";

    private static final String DB_NAME_TERMINAL = "TERMDB";

    private static final int DB_VERSION = 1;

    public String TABLE_NAME_TERMID;
    public String TABLE_NAME_MERID;
    public String TABLE_NAME_MERNAME;
    public String TABLE_NAME_MERADD;
    public String TABLE_NAME_TERMODE;
    public String TABLE_NAME_CURRUNCY;
    public String TABLE_NAME_COMTYPE;

    public String TABLE_NAME_TRANS;
    public String TABLE_NAME_TRANS1;

    public String TABLE_NAME_BATREC;

    public String TABLE_NAME_TRANSACTION;

    public  String TABLE_NAME_IP_PORT;
    public  String TABLE_NAME_SET;

    private static final String TERMID_COL = "terminal_id";

    private static final String MERID_COL = "merchant_id";

    private static final String MERNAME_COL = "merchant_name";

    private static final String MERADDRESS_COL = "merchant_address";


    private static final String TRANSNO_COL = "transmission_No";
    private static final String TRANSSEQNO_COL = "h_sequence_Number";
//batrec
    private static final String AMOUNT_COL = "amount_1";
    private static final String APPROVALCODE_COL = "approval_Code";
    private static final String AUTHENTICATION_CODE ="authentication_Code";
    private static final String RESPONSE_DISPLAY = "response_Display";
    private static final String SEQUENCE_NUMBER = "sequence_Number";
    private static final String EMV_RESPONSEDATA_COL="emv_response_data";

    public String TRANSACTIONTYPE_COL = "transaction_type";
    public String STATUS_COL = "status";

    private static final  String IPADDRESS_COL = "ip";
    private static final  String PORT_COL = "port";
    private static final  String SET_COL = "settlment";
    private static final  String ID_COL = "id";

    public String query;
    public String query1;

    public DBTerminal(Context context) {

        super(context, DB_NAME_TERMINAL, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase DB) {

        TABLE_NAME_TRANS="TRANS";
        String table_name_trans="CREATE TABLE " + TABLE_NAME_TRANS + " ("

                + "id INTEGER  PRIMARY KEY AUTOINCREMENT,"
                + "transmission_No TEXT)";

        Log.d(TAG, "Inside create" + table_name_trans);


        TABLE_NAME_TRANS1="TRANS1";
        String table_name_trans1="CREATE TABLE " + TABLE_NAME_TRANS1 + " ("

                + "id INTEGER  PRIMARY KEY AUTOINCREMENT,"
                + "h_sequence_Number)";

        Log.d(TAG, "Inside create" + table_name_trans1);

        TABLE_NAME_BATREC="BatRec";
        String table_name_batrec="CREATE TABLE " + TABLE_NAME_BATREC + " ("

                + "id INTEGER  PRIMARY KEY AUTOINCREMENT,"
                + "amount_1 TEXT,"
                + "approval_Code TEXT,"
                + "authentication_Code TEXT,"
                + "response_Display TEXT,"
                + "sequence_Number TEXT,"
                + "emv_response_data TEXT)";
        Log.d(TAG, "Inside create" + table_name_batrec);

        TABLE_NAME_TRANSACTION="transactions";
        String table_name_transaction="CREATE TABLE " + TABLE_NAME_TRANSACTION + " ("

                + "id INTEGER, "
                + "transaction_type TEXT PRIMARY KEY,"
                + "status TEXT)";

        Log.d(TAG, "Inside create" + table_name_transaction);


        TABLE_NAME_IP_PORT="ip_port";
        String table_name_ipport="CREATE TABLE " + TABLE_NAME_IP_PORT + " ("

                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "ipaddress TEXT,"
                + "port TEXT)";

        Log.d(TAG, "Inside create" + table_name_ipport);

        TABLE_NAME_SET="SETT";
        String table_name_time="CREATE TABLE " + TABLE_NAME_SET + " ("

                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "settlment TEXT)";

        Log.d(TAG, "Inside create" + table_name_time);

        DB.execSQL(table_name_trans);
        DB.execSQL(table_name_trans1);
        DB.execSQL(table_name_batrec);
        DB.execSQL(table_name_transaction);
        DB.execSQL(table_name_ipport);
        DB.execSQL(table_name_time);

    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        Log.d("EMVDemo", "Inside create" + TABLE_NAME_TERMID);

        DB.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TRANS);
        DB.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TRANS1);
        DB.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_BATREC);
        DB.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TRANSACTION);
        DB.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_IP_PORT);
        DB.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SET);
        onCreate(DB);
    }

    public class Terminal_functions1 {

        private static final String TABLE_NAME_TRANS = "TRANS";
        private static final String TABLE_NAME_TRANS1 = "TRANS1";

        private static final String TABLE_NAME_BATREC = "BatRec";

        private static final String TABLE_NAME_TRANSACTION = "transactions";
        private static final String TABLE_NAME_IP_PORT = "ip_port";
        private static final String TABLE_NAME_SET = "SETT";

        private static final String TRANSNO_COL = "transmission_No";
        private static final String TRANSSEQNO_COL = "h_sequence_Number";

        private static final String AMOUNT_COL = "amount_1";
        private static final String APPROVALCODE_COL = "approval_Code";
        private static final String AUTHENTICATION_CODE_COL ="authentication_Code";
        private static final String RESPONSE_DISPLAY_COL = "response_Display";
        private static final String SEQUENCE_NUMBER_COL = "sequence_Number";
        private static final String EMV_RESPONSEDATA_COL="emv_response_data";

        private static final String TRANSACTIONTYPE_COL = "transaction_type";
        private static final String STATUS_COL = "status";

        private static final String IPADDRESS_COL = "ipaddress";
        private static final String PORT_COL = "port";

        private static final String ID_COL = "id";

        private static final String SET_COL = "settlment";

        public void regtrans(int transmission_No) {
            // Log.d("EMVDemo", "Inside handler" + user_name+user_password);
            SQLiteDatabase DB = DBTerminal.this.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(TRANSNO_COL, transmission_No);
            DB.insert(DBTerminal.this.TABLE_NAME_TRANS, null, values);

            DB.close();
        }
        public void Updatesettlmentstatus(String settstatus) {
            Log.d("EMVDemo", "Cashier Statuse  " + settstatus);
            String settlment="";
            SQLiteDatabase db = DBTerminal.this.getWritableDatabase();
            if(settstatus.equals("1")) {
                settlment="0";
            }else {
                settlment="1";
            }
            ContentValues settstat = new ContentValues();
            settstat.put(SET_COL, settlment);

            db.update(TABLE_NAME_SET, settstat, SET_COL + "=?" , new String[]{settstatus});
            Log.d("EMVDemo", "Finished Updation  " + settlment);
            //db.close();
        }

        public List<terminal> settlstatview(String selection,String[] selectionArgs) {
            String[] sta_columns = {

                    SET_COL
            };
            // sorting orders

            List<terminal> settlList = new ArrayList<terminal>();
            SQLiteDatabase db = DBTerminal.this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME_SET, //Table to query
                    sta_columns,    //columns to return
                    null,        //columns for the WHERE clause
                    null,        //The values for the WHERE clause
                    null,       //group the rows
                    null,       //filter by row groups
                    null); //The sort order

            Log.d(TAG,"settstat_COL DB cnt : " + cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    terminal terminal1 = new terminal();


                    terminal1.setSettlmenttime(cursor.getString(cursor.getColumnIndex(SET_COL)));

                    settlList.add(terminal1);
                } while (cursor.moveToNext());
            }
            cursor.close();
            //db.close();

            return settlList;
        }

        public void regtrans1(String h_sequence_Number) {
            // Log.d("EMVDemo", "Inside handler" + user_name+user_password);
            SQLiteDatabase DB = DBTerminal.this.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(TRANSSEQNO_COL,h_sequence_Number);
            DB.insert(DBTerminal.this.TABLE_NAME_TRANS1, null, values);

            DB.close();
        }

        public void regBatRec(String amount_1, String approval_Code, String authentication_Code,
                String response_Display, String sequence_Number, String emv_response_data) {
            SQLiteDatabase DB = DBTerminal.this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(AMOUNT_COL,amount_1);
            values.put(APPROVALCODE_COL,approval_Code);
            values.put(AUTHENTICATION_CODE_COL,authentication_Code);
            values.put(RESPONSE_DISPLAY_COL,response_Display);
            values.put(SEQUENCE_NUMBER_COL,sequence_Number);
            values.put(EMV_RESPONSEDATA_COL,emv_response_data);
            DB.insert(DBTerminal.this.TABLE_NAME_BATREC, null, values);

            DB.close();

        }

        public void regipport(String ipaddress, String port) {
            Log.d("EMVDemo", "Inside handler ip and port" + ipaddress+" "+port);
            SQLiteDatabase DB = DBTerminal.this.getWritableDatabase();

            ContentValues add = new ContentValues();

            add.put(IPADDRESS_COL,ipaddress);
            add.put(PORT_COL,port);

            DB.insert(DBTerminal.this.TABLE_NAME_IP_PORT, null, add);
            Log.d("EMVDemo", "finshed Insrting ip and port  " + ipaddress+"  "+port);
            DB.close();
        }
        public void updateipport(String id,String ipaddress, String port) {
            Log.d("EMVDemo", "Inside handler ip and port  "+id+" ip " + ipaddress+" port "+port);
            SQLiteDatabase DB = DBTerminal.this.getWritableDatabase();

            ContentValues add = new ContentValues();

            add.put(IPADDRESS_COL,ipaddress);
            add.put(PORT_COL,port);

            DB.update(TABLE_NAME_IP_PORT, add, ID_COL + "=?", new String[]{id});
            //db.update(TABLE_NAME_TID, val, ID_COL+ "=?", new String[]{id});
            Log.d("EMVDemo", "finshed updating ip and port  " + ipaddress+"  "+port);
            DB.close();
        }
        public void settlment(String settlmenttime) {
            Log.d("EMVDemo", "Inside handler time" + settlmenttime);
            SQLiteDatabase DB = DBTerminal.this.getWritableDatabase();

            ContentValues set = new ContentValues();

            set.put(SET_COL,settlmenttime);


            DB.insert(DBTerminal.this.TABLE_NAME_SET, null, set);
            Log.d("EMVDemo", "finshed Insrting TIME  " + settlmenttime);
            DB.close();
        }
        public void Updatesettlment(String id,String settlmenttime) {
            Log.d("EMVDemo", "Inside handler time" + settlmenttime);
            SQLiteDatabase DB = DBTerminal.this.getWritableDatabase();

            ContentValues set = new ContentValues();

            set.put(SET_COL,settlmenttime);

            DB.update(TABLE_NAME_SET, set, ID_COL + "=?", new String[]{id});

            Log.d("EMVDemo", "finshed Updating TIME  " + settlmenttime);
            DB.close();
        }
        public void Enabletxn(String transaction_type) {
            Log.d("EMVDemo", "Inside handler txn Enable  " + transaction_type);
            SQLiteDatabase DB = DBTerminal.this.getWritableDatabase();

            ContentValues Entxn = new ContentValues();

            Entxn.put(STATUS_COL,"1");
            DB.update(TABLE_NAME_TRANSACTION, Entxn, TRANSACTIONTYPE_COL + "=?", new String[]{transaction_type});
            Log.d("EMVDemo", "finshed Enabling txn statuse  ");
            DB.close();

        }
        public void Disabletxn(String transaction_type) {
            Log.d("EMVDemo", "Inside handler txn Disabling " + transaction_type);
            SQLiteDatabase DB = DBTerminal.this.getWritableDatabase();

            ContentValues Distxn = new ContentValues();

            Distxn.put(STATUS_COL,"0");
            DB.update(TABLE_NAME_TRANSACTION, Distxn, TRANSACTIONTYPE_COL + "=?", new String[]{transaction_type});
            Log.d("EMVDemo", "finshed Disabling txn statuse  ");
            DB.close();
        }

        public int row_countipport(String selection,String[] selectionArgs)
        {
            String[] ipcolumns = {
                    IPADDRESS_COL,
                    PORT_COL

            };

            SQLiteDatabase DB = DBTerminal.this.getReadableDatabase();
            Cursor cursor = DB.query("ip_port", //Table to query
                     ipcolumns,    //columns to return
                    null,        //columns for the WHERE clause
                    null,        //The values for the WHERE clause
                    null,       //group the rows
                    null,       //filter by row groups
                    null); //The sort order

            Log.d(TAG,"DB cnt for iprow: " + cursor.getCount());
            return cursor.getCount();

        }
        public int row_settlment(String selection,String[] selectionArgs)
        {
            String[] setcolumns = {
                    SET_COL

            };

            SQLiteDatabase DB = DBTerminal.this.getReadableDatabase();
            Cursor cursor = DB.query("SETT", //Table to query
                    setcolumns,    //columns to return
                    null,        //columns for the WHERE clause
                    null,        //The values for the WHERE clause
                    null,       //group the rows
                    null,       //filter by row groups
                    null); //The sort order

            Log.d(TAG,"DB cnt for TIME: " + cursor.getCount());
            return cursor.getCount();

        }

        public int row_countbatrec(String selection,String[] selectionArgs)
        {
            String[] columns = {
                    AMOUNT_COL,
                    APPROVALCODE_COL,
                    AUTHENTICATION_CODE_COL,
                    RESPONSE_DISPLAY_COL,
                    SEQUENCE_NUMBER_COL,
                    EMV_RESPONSEDATA_COL
            };

            SQLiteDatabase DB = DBTerminal.this.getReadableDatabase();

            Cursor cursor = DB.query("BatRec", //Table to query
                    columns,    //columns to return
                    null,        //columns for the WHERE clause
                    null,        //The values for the WHERE clause
                    null,       //group the rows
                    null,       //filter by row groups
                    null); //The sort order

            Log.d(TAG,"DB cnt for BatRec: " + cursor.getCount());

            return cursor.getCount();

        }

        public int row_count(String selection,String[] selectionArgs)
        {
            String[] columns = {
                    TRANSNO_COL

            };

            SQLiteDatabase DB = DBTerminal.this.getReadableDatabase();

            Cursor cursor = DB.query("TRANS", //Table to query
                    columns,    //columns to return
                    null,        //columns for the WHERE clause
                    null,        //The values for the WHERE clause
                    null,       //group the rows
                    null,       //filter by row groups
                    null); //The sort order

            Log.d(TAG,"DB cnt for transno: " + cursor.getCount());
            return cursor.getCount();

        }
        public int row_count1(String selection,String[] selectionArgs)
        {
            String[] columns = {
                    TRANSSEQNO_COL

            };

            SQLiteDatabase DB = DBTerminal.this.getReadableDatabase();

            Cursor cursor = DB.query("TRANS1", //Table to query
                    columns,    //columns to return
                    null,        //columns for the WHERE clause
                    null,        //The values for the WHERE clause
                    null,       //group the rows
                    null,       //filter by row groups
                    null); //The sort order

            Log.d(TAG,"DB cnt for transseqno: " + cursor.getCount());

            return cursor.getCount();

        }
        public List<terminal> viewtrans(String selection, String[] selectionArgs)
        {
            String[] columns = {
                    TRANSNO_COL
            };
            List<terminal> transinfolist = new ArrayList<terminal>();
            SQLiteDatabase db = DBTerminal.this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME_TRANS, //Table to query
                    columns,    //columns to return
                    null,        //columns for the WHERE clause
                    null,        //The values for the WHERE clause
                    null,       //group the rows
                    null,       //filter by row groups
                    null); //The sort order

            Log.d(TAG,"TRANSNO_COL DB cnt : " + cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    terminal transinfo = new terminal();

                    transinfo.setTransmission_No(cursor.getString(cursor.getColumnIndex(TRANSNO_COL)));

                    transinfolist.add(transinfo);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();

            return transinfolist;
        }
        public List<terminal> viewtrans1(String selection, String[] selectionArgs)
        {
            String[] columns = {
                    TRANSNO_COL
            };
            List<terminal> transinfolist1 = new ArrayList<terminal>();
            SQLiteDatabase db = DBTerminal.this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME_TRANS1, //Table to query
                    columns,    //columns to return
                    null,        //columns for the WHERE clause
                    null,        //The values for the WHERE clause
                    null,       //group the rows
                    null,       //filter by row groups
                    null); //The sort order

            Log.d(TAG,"TRANSSEQNO_COL DB cnt : " + cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    terminal transinfo1 = new terminal();

                    transinfo1.setH_sequence_Number(cursor.getString(cursor.getColumnIndex(TRANSSEQNO_COL)));

                    transinfolist1.add(transinfo1);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();

            return transinfolist1;
        }
        public List<terminal> viewBatrec(String selection, String[] selectionArgs)
        {
            String[] columns = {
                    AMOUNT_COL,
                    APPROVALCODE_COL,
                    AUTHENTICATION_CODE_COL,
                    RESPONSE_DISPLAY_COL,
                    SEQUENCE_NUMBER_COL,
                    EMV_RESPONSEDATA_COL
            };
            // sorting orders
            String sortOrder =
                    AMOUNT_COL + " ASC";
            List<terminal> batrecinfolist = new ArrayList<terminal>();
            SQLiteDatabase db = DBTerminal.this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME_BATREC, //Table to query
                    columns,    //columns to return
                    null,        //columns for the WHERE clause
                    null,        //The values for the WHERE clause
                    null,       //group the rows
                    null,       //filter by row groups
                    null); //The sort order

            Log.d(TAG,"BatRec listview : " + cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    terminal batrecinfo = new terminal();

                    batrecinfo.setAmount_1(cursor.getString(cursor.getColumnIndex(AMOUNT_COL)));
                    batrecinfo.setApproval_Code(cursor.getString(cursor.getColumnIndex(APPROVALCODE_COL)));
                    batrecinfo.setAuthentication_Code(cursor.getString(cursor.getColumnIndex(AUTHENTICATION_CODE_COL)));
                    batrecinfo.setResponse_Display(cursor.getString(cursor.getColumnIndex(RESPONSE_DISPLAY_COL)));
                    batrecinfo.setSequence_Number(cursor.getString(cursor.getColumnIndex(SEQUENCE_NUMBER_COL)));
                    batrecinfo.setEmv_response_data(cursor.getString(cursor.getColumnIndex(EMV_RESPONSEDATA_COL)));

                    batrecinfolist.add(batrecinfo);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();

            return batrecinfolist;
        }
        public void addtransaction( String transaction_type)
        {
            SQLiteDatabase DB = DBTerminal.this.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(TRANSACTIONTYPE_COL,transaction_type);
            values.put(STATUS_COL,"0");
            DB.insert(DBTerminal.this.TABLE_NAME_TRANSACTION, null, values);
            DB.close();
        }
        public int row_countManageTxn(String selection,String[] selectionArgs)
        {
            String[] txn_columns = {
                   TRANSACTIONTYPE_COL,
                    STATUS_COL

            };

            SQLiteDatabase DB = DBTerminal.this.getReadableDatabase();

            Cursor cursor = DB.query("transactions", //Table to query
                    txn_columns,    //columns to return
                    null,        //columns for the WHERE clause
                    null,        //The values for the WHERE clause
                    null,       //group the rows
                    null,       //filter by row groups
                    null); //The sort order

            Log.d(TAG,"DB cnt for Manage_transactions: " + cursor.getCount());
            return cursor.getCount();

        }

        public List<terminal> viewtxn(String selection, String[] selectionArgs)
        {
            String[] txn_columns = {
                    TRANSACTIONTYPE_COL,
                    STATUS_COL
            };
            List<terminal> txninfolist = new ArrayList<terminal>();
            SQLiteDatabase db = DBTerminal.this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME_TRANSACTION, //Table to query
                    txn_columns,    //columns to return
                    null,        //columns for the WHERE clause
                    null,        //The values for the WHERE clause
                    null,       //group the rows
                    null,       //filter by row groups
                    null); //The sort order

            Log.d(TAG,"TRANSACTIONTYPE_COL DB cnt : " + cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    terminal txninfo = new terminal();

                    txninfo.setTransaction_type(cursor.getString(cursor.getColumnIndex(TRANSACTIONTYPE_COL)));
                    txninfo.setStatus(cursor.getString(cursor.getColumnIndex(STATUS_COL)));



                    txninfolist.add(txninfo);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();

            return txninfolist;
        }

        public List<terminal> viewipport(String selection, String[] selectionArgs)
        {
            String[] ipport_columns = {
                    IPADDRESS_COL,
                    PORT_COL
            };
            List<terminal> IPportinfolist = new ArrayList<terminal>();
            SQLiteDatabase db = DBTerminal.this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME_IP_PORT, //Table to query
                    ipport_columns,    //columns to return
                    null,        //columns for the WHERE clause
                    null,        //The values for the WHERE clause
                    null,       //group the rows
                    null,       //filter by row groups
                    null); //The sort order

            Log.d(TAG,"IP AND PORT_COL DB cnt : " + cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    terminal terminal = new terminal();

                    terminal.setIpaddress(cursor.getString(cursor.getColumnIndex(IPADDRESS_COL)));

                    terminal.setPort(cursor.getString(cursor.getColumnIndex(PORT_COL)));


                    IPportinfolist.add(terminal);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();

            return IPportinfolist;
        }
}

}
