package com.verifone.demo.emv;

import android.content.ContentValues;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.verifone.demo.emv.data_handlers.User;

import java.util.ArrayList;
import java.util.List;


public class DBHandler extends SQLiteOpenHelper {

    static String TAG = "DBHAND";

    private static final String DB_NAME = "posdb";

    private static final int DB_VERSION = 1;
    public String transactiontable="transactiontable", lastflag="lastflag",Header="Header",MTI="MTI",BITMAP="BITMAP",
            dateandtime="dateandtime",cashier="cashier",cup="cup",aid="aid",tvr="tvr",
            Current_Date="Current_Date",Current_Time="Current_Time",Card_Holder="Card_Holder",
            Txn_type="Txn_type", Currency="Currency",
            field_02="field_02",field_03="field_03",field_04="field_04",field_07="field_07",field_11="field_11",
            field_12="field_12",field_13="field_13", field_14="field_14",field_22="field_22",
            field_24="field_24",field_25="field_25",field_35="field_35",field_37="field_37",
            field_38="field_38",field_39="field_39",field_41="field_41",field_42="field_42",field_49="field_49",
            field_55="field_55",field_62="field_62",sign="sign";
    public String appcode,responsecode,appstatus,aidname,aidlable;

    private static final String senttransaction = "senttransaction",recievedtransaction = "recievedtransaction";
    //private static final String transactionnum = "transactionnum";
    private static final String masterkeytable = "masterkeytable";
    private static final String masterkey = "master";
    private static final String keyloaderpass = "keyloaderpass";
    private static final String keyloader = "keyloader";

    public String TABLE_NAME;
    public String TABLE_NAME_TID;
    public String TABLE_NAME_MID;
    public String TABLE_NAME_MNAME;
    public String TABLE_NAME_MADDRESS;
    public String TABLE_NAME_TMODE;
    public String TABLE_NAME_CURRENCY;
    public String TABLE_NAME_COMMTYPE;
    public String TABLE_NAME_TIME_OUT_DB;
    public String TABLE_NAME_CASHSTAT;

    private static final String ID_COL = "id";
private static final String transaction="transmisionno";
    private static final String NAME_COL = "name";

    private static final String DURATION_COL = "duration";

    private static final String DESCRIPTION_COL = "description";

    private static final String TRACKS_COL = "tracks";

//.......................................................................................

    //......................................................................................

    public int admin_flg = 0;

    public String query, query1,query2,masterkeyquery;
    SQLiteDatabase db;

    public DBHandler(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
     db=DBHandler.this.getWritableDatabase();
    }


    @Override

    public void onCreate(SQLiteDatabase db) {

        TABLE_NAME = "users";
        String table_name = "CREATE TABLE " + TABLE_NAME + " ("

                + "id INTEGER ,"

                + "name TEXT PRIMARY KEY,"

                + "password TEXT,"

                + "type TEXT,"

                + "status TEXT)";
        Log.d(TAG, "Inside create" + table_name);

 query2="CREATE TABLE " + transactiontable + " ("

     + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
         +lastflag+" TEXT,"
         +dateandtime+" TEXT,"
         +Header+" TEXT,"
         +MTI+" TEXT,"
         +BITMAP+" TEXT,"
         +cashier+" TEXT,"
         +cup+" TEXT,"
         +aid+" TEXT,"
         +tvr+" TEXT,"
         +Current_Date+" TEXT,"
         +Current_Time+" TEXT,"
         +Card_Holder+" TEXT,"
         +Txn_type+" TEXT,"
         +Currency+" TEXT,"
         +field_02+" TEXT,"
         +field_03+" TEXT,"
         +field_04+" TEXT,"
         +field_07+" TEXT,"
         +field_11+" TEXT,"
         +field_12+" TEXT,"
         +field_13+" TEXT,"
         +field_14+" TEXT,"
         +field_22+" TEXT,"
         +field_24+" TEXT,"
         +field_25+" TEXT,"
         +field_35+" TEXT,"
         +field_37+" TEXT,"
         +field_38+" TEXT,"
         +field_39+" TEXT,"
         +field_41+" TEXT,"
         +field_42+" TEXT,"
         +field_49+" TEXT,"
         +field_55+" TEXT,"
         +sign+" TEXT)";
         Log.d(TAG, "Inside create" + query2);
        masterkeyquery="CREATE TABLE " + masterkeytable + " ("

                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                +masterkey+" TEXT,"
                +keyloader+" TEXT,"
                +keyloaderpass+" TEXT)";


        TABLE_NAME_TID = "TID";
        String table_name_tid = "CREATE TABLE " + TABLE_NAME_TID + " ("

                + "id INTEGER PRIMARY KEY AUTOINCREMENT , "

                + "terminal_id TEXT )";
        Log.d(TAG, "Inside create" + table_name_tid);


        TABLE_NAME_TIME_OUT_DB = "TIME_OUT_DB";
        String table_name_Tdb = "CREATE TABLE " + TABLE_NAME_TIME_OUT_DB + " ("

                + "id INTEGER PRIMARY KEY AUTOINCREMENT , "

                + "timeoutdb TEXT )";
        Log.d(TAG, "Inside create" + table_name_Tdb);


        TABLE_NAME_MID = "MID";
        String table_name_mid = "CREATE TABLE " + TABLE_NAME_MID + " ("

                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "

                + "merchant_id TEXT)";
        Log.d(TAG, "Inside create" + table_name_mid);

        TABLE_NAME_MNAME = "MNAME";
        String table_name_mname = "CREATE TABLE " + TABLE_NAME_MNAME + " ("

                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "

                + "merchant_name TEXT)";
        Log.d(TAG, "Inside create" + table_name_mname);

        TABLE_NAME_MADDRESS = "MADDRESS";
        String table_name_maddress = "CREATE TABLE " + TABLE_NAME_MADDRESS + " ("

                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "

                + "merchant_address TEXT)";
        Log.d(TAG, "Inside create" + table_name_maddress);

        TABLE_NAME_TMODE = "TMODE";
        String table_name_mode = "CREATE TABLE " + TABLE_NAME_TMODE + " ("

                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "

                + "mode TEXT)";
        Log.d(TAG, "Inside create" + table_name_mode);

        TABLE_NAME_CURRENCY = "CURRENCYTYPE";
        String table_name_currency = "CREATE TABLE " + TABLE_NAME_CURRENCY + " ("

                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "

                + "currency TEXT )";
        Log.d(TAG, "Inside create currency" + table_name_currency);

        TABLE_NAME_COMMTYPE = "COMTYPE";
        String table_name_commtype = "CREATE TABLE " + TABLE_NAME_COMMTYPE + " ("

                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "

                + "commtype TEXT)";
        Log.d(TAG, "Inside create" + table_name_commtype);

        TABLE_NAME_CASHSTAT = "CASHIER_STATUS";
        String table_name_cashier = "CREATE TABLE " + TABLE_NAME_CASHSTAT + " ("

                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "cashierstatus TEXT)";
        Log.d(TAG, "Inside create" + table_name_cashier);
        db.execSQL(masterkeyquery);
        db.execSQL(table_name);
        db.execSQL(table_name_tid);
        db.execSQL(table_name_mid);
        db.execSQL(table_name_mname);
        db.execSQL(table_name_maddress);
        db.execSQL(table_name_mode);
        db.execSQL(table_name_currency);
        db.execSQL(table_name_commtype);
        db.execSQL( table_name_cashier);
        db.execSQL( table_name_Tdb);
        db.execSQL(query2);
        // db.execSQL(query);
        //db.execSQL(query1);

    }


    public void addNewCourse(String courseName, String courseDuration, String courseDescription, String courseTracks) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(NAME_COL, courseName);

        values.put(DURATION_COL, courseDuration);

        values.put(DESCRIPTION_COL, courseDescription);

        values.put(TRACKS_COL, courseTracks);

        db.insert(TABLE_NAME, null, values);

        //db.close();

    }


    @Override

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // this method is called to check if the table exists already.

        Log.d("EMVDemo", "Inside create" + TABLE_NAME);
        Log.d("EMVDemo", "Inside create" + TABLE_NAME_TID);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TID);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MID);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MNAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MADDRESS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TMODE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CURRENCY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_COMMTYPE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CASHSTAT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TIME_OUT_DB);
        onCreate(db);

    }

    // we have created a new method for reading all the courses.

    public ArrayList<test_class> readCourses() {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);


        ArrayList<test_class> courseModalArrayList = new ArrayList<>();

        if (cursorCourses.moveToFirst()) {

            do {

                courseModalArrayList.add(new test_class(cursorCourses.getString(1),

                        cursorCourses.getString(2),

                        cursorCourses.getString(3),

                        cursorCourses.getString(4)));
                Log.d("EMVDemo", "Inside handler: " + cursorCourses.getString(1) + cursorCourses.getString(2));
            } while (cursorCourses.moveToNext());

            // moving our cursor to next.

        }

        cursorCourses.close();

        return courseModalArrayList;
    }
    public void registerkeyloader(String user_name, String user_password) {
        SQLiteDatabase db = DBHandler.this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(keyloader, user_name);

        values.put(keyloaderpass, user_password);
        if(getkeyloadersize()>0){
            //db.update(masterkeytable, values,  ID_COL + "=?", new String[]{"1"});
        }else {
            db.insert(masterkeytable, null, values);
        }
        //db.close();

    }
    public void registermasterkey(String master) {
        SQLiteDatabase db = DBHandler.this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(masterkey, master);
        if(getkeyloadersize()>0){
            db.update(masterkeytable, values,  ID_COL + "=?", new String[]{"1"});
        }else {
            db.insert(masterkeytable, null, values);
        }
        //db.close();
    }
    public void changekeyloaderpin(String password) {
        SQLiteDatabase db = DBHandler.this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(keyloaderpass, password);

        db.update(masterkeytable, args,  ID_COL + "=?", new String[]{"1"});

    }
    public User getmasterkeydata() {
        String[] sta_columns = {

                masterkey,
                keyloader,
                keyloaderpass

        };
        // sorting orders
        User use=new User();
        SQLiteDatabase db = DBHandler.this.getReadableDatabase();
        Cursor cursor = db.query(masterkeytable, //Table to query
                sta_columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null); //The sort order

        Log.d(TAG,"cashierstat_COL DB cnt : " + cursor.getCount());

        if (cursor.moveToFirst()) {
            do {
                use.setMasterkey(cursor.getString(cursor.getColumnIndex(masterkey)));
                use.setkeyloader(cursor.getString(cursor.getColumnIndex(keyloader)));
                use.setkeyloaderpass(cursor.getString(cursor.getColumnIndex(keyloaderpass)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        //db.close();

        return use;
    }
    public int getkeyloadersize() {
        String countQuery = "SELECT  * FROM " + masterkeytable;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public class user_functions {

        private static final String TABLE_NAME = "users";
        private static final String TABLE_NAME_CASHSTAT = "CASHIER_STATUS";
        private static final String TABLE_NAME_TIME_OUT_DB = "TIME_OUT_DB";
        private static final String ID_COL = "id";

        private static final String NAME_COL = "name";

        private static final String PASSWORD_COL = "password";

        private static final String TYPE_COL = "type";

        private static final String STATUS_COL = "status";

        private static final String CASHIERSTAT_COL = "cashierstatus";

        private static final String TIME_OUT_DB_COL = "timeoutdb";

        public void addNewuser(String user_name, String user_password, String user_type) {

            Log.d("EMVDemo", "Inside handler" + user_name + user_password);

            SQLiteDatabase db = DBHandler.this.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(NAME_COL, user_name);

            values.put(PASSWORD_COL, user_password);

            values.put(TYPE_COL, user_type);

            values.put(STATUS_COL, "Enabled");

            db.insert(TABLE_NAME, null, values);

            //db.close();

        }
        public void Add_Time_outdb(String timeoutdb)
        {
            Log.d("EMVDemo", " DBHANDELER Added timeout reversal value ");
            SQLiteDatabase DB = DBHandler.this.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(TIME_OUT_DB_COL, timeoutdb);
            DB.insert(DBHandler.this.TABLE_NAME_TIME_OUT_DB, null, values);

            //DB.close();
        }

        public void Update_Time_outdb(String id,String timeoutdb) {
            Log.d("EMVDemo", "updating timeoutdb value............ ");
            String cashier="";
            SQLiteDatabase db = DBHandler.this.getWritableDatabase();

            ContentValues timeout = new ContentValues();
            timeout.put(TIME_OUT_DB_COL, timeoutdb);

            db.update(TABLE_NAME_TIME_OUT_DB, timeout, TIME_OUT_DB_COL+ "=?"  , new String[]{id});
            Log.d("EMVDemo", "Finished updating timeoutdb.............  " + timeoutdb);
            //db.close();
        }
        public void delete_Time_outdb()
        {
            Log.d(TAG, "DBHandeler RESETED Time_outdb .......");
            SQLiteDatabase db = DBHandler.this.getReadableDatabase();
            db.delete(TABLE_NAME_TIME_OUT_DB, null, null);

        }

        public int Time_outdb_count(String selection, String[] selectionArgs) {
            String[] columns_db = {

                    TIME_OUT_DB_COL
            };

            SQLiteDatabase db = DBHandler.this.getReadableDatabase();

            Cursor cursor = db.query("TIME_OUT_DB", //Table to query
                    columns_db,    //columns to return
                    null,        //columns for the WHERE clause
                    null,        //The values for the WHERE clause
                    null,       //group the rows
                    null,       //filter by row groups
                    null); //The sort order

            Log.d(TAG, "DB cnt  TIMEOUTDB:  " + cursor.getCount());

            return cursor.getCount();

        }

        public List<User> View_Timeoutdb(String selection, String[] selectionArgs) {
            String[] timeout_columns = {

                    TIME_OUT_DB_COL
            };
            // sorting orders

            List<User> TimeoutList = new ArrayList<User>();
            SQLiteDatabase db = DBHandler.this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME_TIME_OUT_DB, //Table to query
                    timeout_columns,    //columns to return
                    null,        //columns for the WHERE clause
                    null,        //The values for the WHERE clause
                    null,       //group the rows
                    null,       //filter by row groups
                    null); //The sort order

            Log.d(TAG,"Timeout DB cnt : " + cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    User timeout = new User();


                    timeout.setTimeoutdb(cursor.getString(cursor.getColumnIndex(TIME_OUT_DB_COL)));

                    TimeoutList.add(timeout);
                } while (cursor.moveToNext());
            }
            cursor.close();
            //db.close();

            return TimeoutList;
        }


        public void changepin(String usertype, String name, String password) {
            SQLiteDatabase db = DBHandler.this.getWritableDatabase();
            ContentValues args = new ContentValues();
            args.put(PASSWORD_COL, password);

            db.update(TABLE_NAME, args, NAME_COL + "=? AND " + TYPE_COL + "=?", new String[]{name, usertype});

        }

        public void Cashierstat(String cashierstatus1) {
            Log.d("EMVDemo", "Cashier Statuse  " + cashierstatus1);
            SQLiteDatabase db = DBHandler.this.getWritableDatabase();

            ContentValues cash = new ContentValues();

            cash.put(CASHIERSTAT_COL, cashierstatus1);

            db.insert(DBHandler.this.TABLE_NAME_CASHSTAT, null, cash);

            //db.close();

        }
        public void UpdateCashierstat(String cashierstatus) {
            Log.d("EMVDemo", "Cashier Statuse  " + cashierstatus);
            String cashier="";
            SQLiteDatabase db = DBHandler.this.getWritableDatabase();
            if(cashierstatus.equals("1")) {
                cashier="0";
            }else {
                cashier="1";
            }
            ContentValues cash = new ContentValues();
            cash.put(CASHIERSTAT_COL, cashier);

            db.update(TABLE_NAME_CASHSTAT, cash, CASHIERSTAT_COL + "=?" , new String[]{cashierstatus});
            Log.d("EMVDemo", "Finished Updation  " + cashier);
            //db.close();
        }
        public List<User> viewUsers(String selection, String[] selectionArgs) {
            String[] columns = {
                    NAME_COL,
                    PASSWORD_COL,
                    TYPE_COL,
                    STATUS_COL
            };
            // sorting orders
            String sortOrder =
                    NAME_COL + " ASC";
            List<User> userList = new ArrayList<User>();
            SQLiteDatabase db = DBHandler.this.getReadableDatabase();

            Cursor cursor = db.query(TABLE_NAME, //Table to query
                    columns,    //columns to return
                    selection,        //columns for the WHERE clause
                    selectionArgs,        //The values for the WHERE clause
                    null,       //group the rows
                    null,       //filter by row groups
                    sortOrder); //The sort order

            Log.d(TAG, "DB cnt: " + cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    User user = new User();

                    user.setName(cursor.getString(cursor.getColumnIndex(NAME_COL)));
                    user.setPassword(cursor.getString(cursor.getColumnIndex(PASSWORD_COL)));
                    user.setTpe(cursor.getString(cursor.getColumnIndex(TYPE_COL)));
                    user.setStatus(cursor.getString(cursor.getColumnIndex(STATUS_COL)));

                    userList.add(user);
                } while (cursor.moveToNext());
            }
            cursor.close();
            //db.close();

            return userList;
        }
        public List<User> Cashierstatview(String selection, String[] selectionArgs) {
            String[] sta_columns = {

                    CASHIERSTAT_COL
            };
            // sorting orders

            List<User> cashierList = new ArrayList<User>();
            SQLiteDatabase db = DBHandler.this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME_CASHSTAT, //Table to query
                    sta_columns,    //columns to return
                    null,        //columns for the WHERE clause
                    null,        //The values for the WHERE clause
                    null,       //group the rows
                    null,       //filter by row groups
                    null); //The sort order

            Log.d(TAG,"cashierstat_COL DB cnt : " + cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    User user1 = new User();


                    user1.setCashierstatus(cursor.getString(cursor.getColumnIndex(CASHIERSTAT_COL)));

                    cashierList.add(user1);
                } while (cursor.moveToNext());
            }
            cursor.close();
            //db.close();

            return cashierList;
        }
        public int Cshierstat_count(String selection, String[] selectionArgs) {
            String[] columns = {

                    CASHIERSTAT_COL
            };

            SQLiteDatabase db = DBHandler.this.getReadableDatabase();

            Cursor cursor = db.query("CASHIER_STATUS", //Table to query
                    columns,    //columns to return
                    null,        //columns for the WHERE clause
                    null,        //The values for the WHERE clause
                    null,       //group the rows
                    null,       //filter by row groups
                    null); //The sort order

            Log.d(TAG, "DB cnt  cashierstatus:  " + cursor.getCount());

            return cursor.getCount();

        }

        public int row_count(String selection, String[] selectionArgs) {
            String[] columns = {
                    NAME_COL,
                    PASSWORD_COL,
                    TYPE_COL,
                    STATUS_COL
            };

            SQLiteDatabase db = DBHandler.this.getReadableDatabase();

            Cursor cursor = db.query("users", //Table to query
                    columns,    //columns to return
                    null,        //columns for the WHERE clause
                    null,        //The values for the WHERE clause
                    null,       //group the rows
                    null,       //filter by row groups
                    null); //The sort order

            Log.d(TAG, "DB cnt: " + cursor.getCount());

            return cursor.getCount();

        }
public void enabledisable(String user,boolean enable){
             String e="";
            SQLiteDatabase db = DBHandler.this.getWritableDatabase();
             if(enable){
                 e="Enabled";
             }else {
                 e="DISABLED";
             }
            ContentValues args = new ContentValues();
            args.put(STATUS_COL, e);

           db.update(TABLE_NAME, args, NAME_COL + "=?" , new String[]{user});

        }
        public void changepin(String user_name, String user_password) {
            Log.d("EMVDemo", "Update Inside handler" + user_name + "  " + user_password);

            SQLiteDatabase db = DBHandler.this.getWritableDatabase();

            ContentValues Values = new ContentValues();

            Values.put(PASSWORD_COL, user_password);

            db.update(TABLE_NAME, Values, NAME_COL + "=?", new String[]{user_name});

            //db.close();
        }

        public void deleteuser(String user_name) {
            Log.d(TAG, "DBHandeler deletuser");
            Log.d("EMVDemo", "Inside handler" + user_name);
            // Cursor cursor=db.rawQuery("select * from ;
            SQLiteDatabase db = DBHandler.this.getReadableDatabase();
            String countQuery = "SELECT  * FROM " + TABLE_NAME;
            Cursor cursor = db.rawQuery(countQuery, null);
            Log.d(TAG, "DB count deleteuser: " + cursor.getCount());
            if (cursor.getCount() > 0)
            {
                //long resualt=db.delete(TABLE_NAME_ADMIN,  user_name +" = ?",new String[]{user_name});
                db.delete(TABLE_NAME, NAME_COL + " = ?", new String[]{(user_name)});
                Log.d(TAG, "Done Deleted: " + cursor.getCount());
                //db.close();
            }
        }


        public void delete_All_txn_data()
        {
            Log.d(TAG, "DBHandeler end of day  delete_All_txn_data.......");
            SQLiteDatabase db = DBHandler.this.getReadableDatabase();
            db.delete(transactiontable, null, null);

        }


        public void Enable(String user_name) {
            Log.d(TAG, "DBHandeler Enable");
            Log.d("EMVDemo", "Inside handler" + user_name);
            SQLiteDatabase db = DBHandler.this.getReadableDatabase();

            String E = "Enabled";
            ContentValues val = new ContentValues();
            val.put(STATUS_COL, E);
            db.update(TABLE_NAME, val, NAME_COL + "=?", new String[]{user_name});

        }

        public void Disable(String user_name) {
            Log.d(TAG, "DBHandeler Disable");
            Log.d("EMVDemo", "Inside handler" + user_name);
            SQLiteDatabase db = DBHandler.this.getReadableDatabase();

            String E = "Disabled";
            ContentValues val = new ContentValues();
            val.put(STATUS_COL, E);
            db.update(TABLE_NAME, val, NAME_COL + "=?", new String[]{user_name});

        }

        public void Enablecashier(String name) {
            Log.d("EMVDemo", "Inside handler txn Enable  " + name);
            SQLiteDatabase db = DBHandler.this.getReadableDatabase();

            ContentValues Encash = new ContentValues();

            Encash.put(STATUS_COL,"1");
            //db.update(TABLE_NAME_TRANSACTION, Encash, TRANSACTIONTYPE_COL + "=?", new String[]{transaction_type});
            Log.d("EMVDemo", "finshed Enabling txn statuse  ");
            //db.close();

        }
        public void Disablecashier(String transaction_type) {
            Log.d("EMVDemo", "Inside handler txn Disabling " + transaction_type);
            SQLiteDatabase db = DBHandler.this.getReadableDatabase();

            ContentValues Distxn = new ContentValues();

            Distxn.put(STATUS_COL,"0");
           // DB.update(TABLE_NAME_TRANSACTION, Distxn, TRANSACTIONTYPE_COL + "=?", new String[]{transaction_type});
            Log.d("EMVDemo", "finshed Disabling txn statuse  ");
            //db.close();
        }

    }


    public class terminal_functions {

        private static final String TABLE_NAME_TID = "TID";
        private static final String TABLE_NAME_MID = "MID";
        private static final String TABLE_NAME_MNAME = "MNAME";
        private static final String TABLE_NAME_MADDRESS = "MADDRESS";
        private static final String TABLE_NAME_TMODE = "TMODE";
        private static final String TABLE_NAME_CURRENCY = "CURRENCYTYPE";
        private static final String TABLE_NAME_COMMTYPE = "COMTYPE";

        private static final String ID_COL = "id";

        private static final String TERMID_COL = "terminal_id";

        private static final String MERID_COL = "merchant_id";

        private static final String MERNAME_COL = "merchant_name";

        private static final String MERADDRESS_COL = "merchant_address";

        private static final String TMODE_COL = "mode";

        private static final String CURRENCY_COL = "currency";

        private static final String COMTYPE_COL = "commtype";


        public void regterminal_id(String terminal_id) {
            Log.d("EMVDemo", "Inside regterminal_id = " + terminal_id);
            SQLiteDatabase DB = DBHandler.this.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(TERMID_COL, terminal_id);
            DB.insert(DBHandler.this.TABLE_NAME_TID, null, values);

            //DB.close();
        }

        public void update(String user, String column, String value) {
            String e = "";
            SQLiteDatabase db = DBHandler.this.getWritableDatabase();
            ContentValues args = new ContentValues();
            args.put(column, value);

            db.update(TABLE_NAME, args, NAME_COL + "=?", new String[]{user});

        }

        public void regmerchant_id(String merchant_id) {
            Log.d("EMVDemo", "regmerchant_id Inside handler  " + merchant_id);
            SQLiteDatabase DB = DBHandler.this.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(MERID_COL, merchant_id);
            DB.insert(DBHandler.this.TABLE_NAME_MID, null, values);

            //DB.close();
        }

        public void regmerchant_name(String merchant_name) {
            Log.d("EMVDemo", "merchant_name Inside handler  " + merchant_name);
            SQLiteDatabase DB = DBHandler.this.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(MERNAME_COL, merchant_name);
            DB.insert(DBHandler.this.TABLE_NAME_MNAME, null, values);

            //DB.close();
        }

        public void regmerchant_address(String merchant_address) {
            Log.d("EMVDemo", "merchant_address Inside handler  " + merchant_address);
            SQLiteDatabase DB = DBHandler.this.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(MERADDRESS_COL, merchant_address);
            DB.insert(DBHandler.this.TABLE_NAME_MADDRESS, null, values);

            //DB.close();
        }

        public void deleterow(String user) {
            SQLiteDatabase db = DBHandler.this.getWritableDatabase();
            db.delete(TABLE_NAME, NAME_COL + "=?", new String[]{user});

        }

        public void regTerminalmode(String mode) {
            Log.d("EMVDemo", "ModeType Inside handler  " + mode);
            SQLiteDatabase DB = DBHandler.this.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(TMODE_COL, mode);
            DB.insert(DBHandler.this.TABLE_NAME_TMODE, null, values);

            //DB.close();
        }

        public void regCurrencytype(String currency) {
            Log.d("EMVDemo", "CurruncyType Inside handler  " + currency);
            SQLiteDatabase DB = DBHandler.this.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(CURRENCY_COL, currency);
            DB.insert(DBHandler.this.TABLE_NAME_CURRENCY, null, values);

            //DB.close();
        }

        public void regCommtype(String commtype) {
            Log.d("EMVDemo", "CommType Inside handler  " + commtype);
            SQLiteDatabase DB = DBHandler.this.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(COMTYPE_COL, commtype);
            DB.insert(DBHandler.this.TABLE_NAME_COMMTYPE, null, values);

            //DB.close();
        }

        //UPDATE.................................................................................................UPDATE
        public void UpdateTid(String id, String Terminal_id) {

            Log.d("EMVDemo", "Inside handler Id and Terminal_id  " + id + "  and  " + Terminal_id);
            SQLiteDatabase db = DBHandler.this.getReadableDatabase();

            ContentValues val = new ContentValues();

            val.put(TERMID_COL, Terminal_id);
            db.update(TABLE_NAME_TID, val, ID_COL + "=?", new String[]{id});
            //db.update("contacts", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
            //update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
            Log.d(TAG, "Terminal id is updated");
        }

        public void UpdateMid(String id, String Merchant_id) {

            Log.d("EMVDemo", "Inside handler Merchant_id  " + Merchant_id);
            SQLiteDatabase db = DBHandler.this.getReadableDatabase();

            ContentValues val = new ContentValues();

            val.put(MERID_COL, Merchant_id);
            db.update(TABLE_NAME_MID, val, ID_COL + "=?", new String[]{id});
            Log.d(TAG, "Merchant id is updated");
        }




        public void UpdateMname(String id, String Merchant_name) {

            Log.d("EMVDemo", "Inside handler Merchant_name  " + Merchant_name);
            SQLiteDatabase db = DBHandler.this.getReadableDatabase();

            ContentValues val = new ContentValues();

            val.put(MERNAME_COL, Merchant_name);
            db.update(TABLE_NAME_MNAME, val, ID_COL + "=?", new String[]{id});
            Log.d(TAG, "Merchant_name is updated");
        }

        public void UpdateMaddress(String id, String Merchant_address) {

            Log.d("EMVDemo", "Inside handler Merchant_name  " + Merchant_address);
            SQLiteDatabase db = DBHandler.this.getReadableDatabase();

            ContentValues val = new ContentValues();

            val.put(MERADDRESS_COL, Merchant_address);
            db.update(TABLE_NAME_MADDRESS, val, ID_COL + "=?", new String[]{id});
            Log.d(TAG, "Merchant_Address is updated");
        }

        public void UpdateTmode(String id, String mode) {

            Log.d("EMVDemo", "Inside handler Merchant_name  " + mode);
            SQLiteDatabase db = DBHandler.this.getReadableDatabase();

            ContentValues val = new ContentValues();

            val.put(TMODE_COL, mode);
            db.update(TABLE_NAME_TMODE, val, ID_COL + "=?", new String[]{id});
            Log.d(TAG, "Terminal Mode is updated");
        }

        public void updatetransaction(String id, String senttrans, String recievedtrans) {
            String e = "";
            SQLiteDatabase db = DBHandler.this.getWritableDatabase();
            ContentValues args = new ContentValues();
            args.put(recievedtrans, recievedtrans);
            args.put(senttransaction, senttrans);

            db.update(transactiontable, args, ID_COL + "=?", new String[]{id});

        }

        public void UpdateCurrency(String id, String currency) {

            Log.d("EMVDemo", "Inside handler currency  " + currency);
            SQLiteDatabase db = DBHandler.this.getReadableDatabase();

            ContentValues val = new ContentValues();

            val.put(CURRENCY_COL, currency);
            db.update(TABLE_NAME_CURRENCY, val, ID_COL + "=?", new String[]{id});
            Log.d(TAG, "Terminal currency is updated");
        }

        public void UpdateCommtype(String id, String comm_type) {

            Log.d("EMVDemo", "Inside handler comm type  " + comm_type);
            SQLiteDatabase db = DBHandler.this.getReadableDatabase();

            ContentValues val = new ContentValues();

            val.put(COMTYPE_COL, comm_type);
            db.update(TABLE_NAME_COMMTYPE, val, ID_COL + "=?", new String[]{id});
            Log.d(TAG, "Comm type is updated");
        }

        //END.......................................................................................rowcount
        public int row_countTid(String selection, String[] selectionArgs) {
            String[] tid_columns = {
                    TERMID_COL

            };

            SQLiteDatabase DB = DBHandler.this.getReadableDatabase();

            Cursor cursor = DB.query("TID", //Table to query
                    tid_columns,    //columns to return
                    null,        //columns for the WHERE clause
                    null,        //The values for the WHERE clause
                    null,       //group the rows
                    null,       //filter by row groups
                    null); //The sort order

            Log.d(TAG, "DB cnt: " + cursor.getCount());

            return cursor.getCount();

        }



        public int row_countMid(String selection, String[] selectionArgs) {
            String[] mid_columns = {
                    MERID_COL
            };

            SQLiteDatabase DB = DBHandler.this.getReadableDatabase();

            Cursor cursor = DB.query("MID", //Table to query
                    mid_columns,    //columns to return
                    null,        //columns for the WHERE clause
                    null,        //The values for the WHERE clause
                    null,       //group the rows
                    null,       //filter by row groups
                    null); //The sort order

            Log.d(TAG, "DB cnt Mid: " + cursor.getCount());

            return cursor.getCount();

        }

        public int row_countMname(String selection, String[] selectionArgs) {
            String[] mname_columns = {

                    MERNAME_COL

            };

            SQLiteDatabase DB = DBHandler.this.getReadableDatabase();

            Cursor cursor = DB.query("MNAME", //Table to query
                    mname_columns,    //columns to return
                    null,        //columns for the WHERE clause
                    null,        //The values for the WHERE clause
                    null,       //group the rows
                    null,       //filter by row groups
                    null); //The sort order

            Log.d(TAG, "DB cnt Mname: " + cursor.getCount());

            return cursor.getCount();

        }

        public int row_countMaddrs(String selection, String[] selectionArgs) {
            String[] maddress_columns = {

                    MERADDRESS_COL
            };

            SQLiteDatabase DB = DBHandler.this.getReadableDatabase();

            Cursor cursor = DB.query("MADDRESS", //Table to query
                    maddress_columns,    //columns to return
                    null,        //columns for the WHERE clause
                    null,        //The values for the WHERE clause
                    null,       //group the rows
                    null,       //filter by row groups
                    null); //The sort order

            Log.d(TAG, "DB cnt Maddress: " + cursor.getCount());

            return cursor.getCount();

        }

        public int row_countmode(String selection, String[] selectionArgs) {
            String[] mode_columns = {

                    TMODE_COL
            };

            SQLiteDatabase DB = DBHandler.this.getReadableDatabase();

            Cursor cursor = DB.query("TMODE", //Table to query
                    mode_columns,    //columns to return
                    null,        //columns for the WHERE clause
                    null,        //The values for the WHERE clause
                    null,       //group the rows
                    null,       //filter by row groups
                    null); //The sort order

            Log.d(TAG, "DB cnt mode: " + cursor.getCount());

            return cursor.getCount();

        }

        public int row_countcurrency(String selection, String[] selectionArgs) {
            String[] currency_columns = {

                    CURRENCY_COL
            };

            SQLiteDatabase DB = DBHandler.this.getReadableDatabase();

            Cursor cursor = DB.query("CURRENCYTYPE", //Table to query
                    currency_columns,    //columns to return
                    null,        //columns for the WHERE clause
                    null,        //The values for the WHERE clause
                    null,       //group the rows
                    null,       //filter by row groups
                    null); //The sort order

            Log.d(TAG, "DB cnt Currency: " + cursor.getCount());

            return cursor.getCount();

        }

        public int row_countcommtype(String selection, String[] selectionArgs) {
            String[] commtype_columns = {

                    COMTYPE_COL
            };

            SQLiteDatabase DB = DBHandler.this.getReadableDatabase();

            Cursor cursor = DB.query("COMTYPE", //Table to query
                    commtype_columns,    //columns to return
                    null,        //columns for the WHERE clause
                    null,        //The values for the WHERE clause
                    null,       //group the rows
                    null,       //filter by row groups
                    null); //The sort order

            Log.d(TAG, "DB cnt commtype: " + cursor.getCount());

            return cursor.getCount();

        }

        //END..........................................................................................viewinformation
        public List<User> viewTidInfo(String selection, String[] selectionArgs) {
            String[] Tid_columns = {
                    TERMID_COL
            };
            List<User> TermInfoList = new ArrayList<User>();
            SQLiteDatabase db = DBHandler.this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME_TID, //Table to query
                    Tid_columns,    //columns to return
                    null,        //columns for the WHERE clause
                    null,        //The values for the WHERE clause
                    null,       //group the rows
                    null,       //filter by row groups
                    null); //The sort order

            Log.d(TAG, "TERMID_COL Terminal DB cnt : " + cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    User terminfo = new User();

                    terminfo.setTerminal_id(cursor.getString(cursor.getColumnIndex(TERMID_COL)));

                    TermInfoList.add(terminfo);
                } while (cursor.moveToNext());
            }
            cursor.close();
            //db.close();

            return TermInfoList;
        }

        public String getsenttransaction(int trnum) {
            String e = "";
            SQLiteDatabase db = DBHandler.this.getWritableDatabase();
            String[] columns = {
                    senttransaction
            };
            Cursor cursor = db.query(transactiontable, //Table to query
                    columns,    //columns to return
                    ID_COL + "=?",        //columns for the WHERE clause
                    new String[]{String.valueOf(trnum)},        //The values for the WHERE clause
                    null,       //group the rows
                    null,       //filter by row groups
                    null);
            if (cursor.moveToFirst()) {
                do {
                    e = cursor.getString(cursor.getColumnIndex(senttransaction));

                } while (cursor.moveToNext());
            }
            cursor.close();
            //db.close();
            return e;
        }



        public List<User> viewMidInfo(String selection, String[] selectionArgs) {
            String[] Mid_columns = {
                    MERID_COL
            };
            List<User> TermInfoList1 = new ArrayList<User>();
            SQLiteDatabase db = DBHandler.this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME_MID, //Table to query
                    Mid_columns,    //columns to return
                    null,        //columns for the WHERE clause
                    null,        //The values for the WHERE clause
                    null,       //group the rows
                    null,       //filter by row groups
                    null); //The sort order

            Log.d(TAG, "MERID_COL Terminal DB cnt : " + cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    User terminfo1 = new User();
                    terminfo1.setMerchant_id(cursor.getString(cursor.getColumnIndex(MERID_COL)));
                    TermInfoList1.add(terminfo1);
                } while (cursor.moveToNext());
            }
            cursor.close();
            //db.close();

            return TermInfoList1;
        }


        public List<User> viewMnameInfo(String selection, String[] selectionArgs) {
            String[] Mid_columns = {
                    MERNAME_COL
            };
            List<User> TermInfoList = new ArrayList<User>();
            SQLiteDatabase db = DBHandler.this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME_MNAME, //Table to query
                    Mid_columns,    //columns to return
                    null,        //columns for the WHERE clause
                    null,        //The values for the WHERE clause
                    null,       //group the rows
                    null,       //filter by row groups
                    null); //The sort order

            Log.d(TAG, "MERID_COL Terminal DB cnt : " + cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    User terminfo = new User();
                    terminfo.setMerchant_name(cursor.getString(cursor.getColumnIndex(MERNAME_COL)));
                    TermInfoList.add(terminfo);
                } while (cursor.moveToNext());
            }
            cursor.close();
            //db.close();

            return TermInfoList;
        }


        public List<User> viewCurrency(String selection, String[] selectionArgs) {
            String[] Cur_columns = {
                    CURRENCY_COL
            };
            List<User> TermInfoList2 = new ArrayList<User>();
            SQLiteDatabase db = DBHandler.this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME_CURRENCY, //Table to query
                    Cur_columns,    //columns to return
                    null,        //columns for the WHERE clause
                    null,        //The values for the WHERE clause
                    null,       //group the rows
                    null,       //filter by row groups
                    null); //The sort order

            Log.d(TAG, "Currency DB cnt : " + cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    User terminfo2 = new User();
                    terminfo2.setCurrency(cursor.getString(cursor.getColumnIndex(CURRENCY_COL)));
                    TermInfoList2.add(terminfo2);
                } while (cursor.moveToNext());
            }
            cursor.close();
            //db.close();

            return TermInfoList2;
        }

        public List<User> viewMaddress(String selection, String[] selectionArgs) {
            String[] add_columns = {
                    MERADDRESS_COL
            };
            List<User> TermInfoList = new ArrayList<User>();
            SQLiteDatabase db = DBHandler.this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME_MADDRESS, //Table to query
                    add_columns,    //columns to return
                    null,        //columns for the WHERE clause
                    null,        //The values for the WHERE clause
                    null,       //group the rows
                    null,       //filter by row groups
                    null); //The sort order

            Log.d(TAG, "Maddress DB cnt : " + cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    User terminfo = new User();
                    terminfo.setMerchant_address(cursor.getString(cursor.getColumnIndex(MERADDRESS_COL)));
                    TermInfoList.add(terminfo);
                } while (cursor.moveToNext());
            }
            cursor.close();
            //db.close();

            return TermInfoList;
        }

        public List<User> viewTmode(String selection, String[] selectionArgs) {
            String[] mode_columns = {
                    TMODE_COL
            };
            List<User> TermInfoList = new ArrayList<User>();
            SQLiteDatabase db = DBHandler.this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME_TMODE, //Table to query
                    mode_columns,    //columns to return
                    null,        //columns for the WHERE clause
                    null,        //The values for the WHERE clause
                    null,       //group the rows
                    null,       //filter by row groups
                    null); //The sort order

            Log.d(TAG, "Maddress DB cnt : " + cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    User terminfo = new User();
                    terminfo.setMode(cursor.getString(cursor.getColumnIndex(TMODE_COL)));
                    TermInfoList.add(terminfo);
                } while (cursor.moveToNext());
            }
            cursor.close();
            //db.close();

            return TermInfoList;
        }



        public List<User> viewCommtype(String selection, String[] selectionArgs) {
            String[] comm_columns = {
                    COMTYPE_COL
            };
            List<User> TermInfoList = new ArrayList<User>();
            SQLiteDatabase db = DBHandler.this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME_COMMTYPE, //Table to query
                    comm_columns,    //columns to return
                    null,        //columns for the WHERE clause
                    null,        //The values for the WHERE clause
                    null,       //group the rows
                    null,       //filter by row groups
                    null); //The sort order

            Log.d(TAG, "Commtype DB cnt : " + cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    User terminfo = new User();
                    terminfo.setCommtype(cursor.getString(cursor.getColumnIndex(COMTYPE_COL)));
                    TermInfoList.add(terminfo);
                } while (cursor.moveToNext());
            }
            cursor.close();
            //db.close();

            return TermInfoList;
        }
    }
    public int getdatasize() {
        String countQuery = "SELECT  * FROM " + transactiontable;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
    public String getRecievedtransaction(int trnum) {
        String e = "";
        SQLiteDatabase db = DBHandler.this.getWritableDatabase();
        String[] columns = {
                recievedtransaction
        };
        Cursor cursor = db.query(transactiontable, //Table to query
                columns,    //columns to return
                ID_COL + "=?",        //columns for the WHERE clause
                new String[]{String.valueOf(trnum)},        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null);
        if (cursor.moveToFirst()) {
            do {
                e = cursor.getString(cursor.getColumnIndex(recievedtransaction));

            } while (cursor.moveToNext());
        }
        cursor.close();
        //db.close();
        return e;
    }
    public String getamount(int trnum) {
        String e = "";
        SQLiteDatabase db = DBHandler.this.getWritableDatabase();
        String[] columns = {
                field_04
        };
        Cursor cursor = db.query(transactiontable, //Table to query
                columns,    //columns to return
                ID_COL + "=?",        //columns for the WHERE clause
                new String[]{String.valueOf(trnum)},        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null);
        if (cursor.moveToFirst()) {
            do {
                e = cursor.getString(cursor.getColumnIndex(field_04));

            } while (cursor.moveToNext());
        }
        cursor.close();
        //db.close();
        return e;
    }
    public String getflag(int trnum) {
        String e = "";
        SQLiteDatabase db = DBHandler.this.getWritableDatabase();
        String[] columns = {
                lastflag
        };
        Cursor cursor = db.query(transactiontable, //Table to query
                columns,    //columns to return
                ID_COL + "=?",        //columns for the WHERE clause
                new String[]{String.valueOf(trnum)},        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null);
        if (cursor.moveToFirst()) {
            do {
                e = cursor.getString(cursor.getColumnIndex(lastflag));

            } while (cursor.moveToNext());
        }
        cursor.close();
        //db.close();
        return e;
    }

    public String gettrantype(int trnum) {
        String e = "";
        SQLiteDatabase db = DBHandler.this.getWritableDatabase();
        String[] columns = {
                Txn_type
        };
        Cursor cursor = db.query(transactiontable, //Table to query
                columns,    //columns to return
                ID_COL + "=?",        //columns for the WHERE clause
                new String[]{String.valueOf(trnum)},        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null);
        if (cursor.moveToFirst()) {
            do {
                e = cursor.getString(cursor.getColumnIndex(Txn_type));

            } while (cursor.moveToNext());
        }
        cursor.close();
        //db.close();
        return e;
    }

    public void addtransactionfile(
                  String lastflag1,String dateandtime1,String Header1,String MTI1,String BITMAP1,
                  String cashier1,String cup1,String aid11,String tvr1, String  Current_Date1,
                  String Current_Time1 ,String Card_Holder1,String Txn_type1,String Currency1,
                   String  field_02_1,String field_03_1,String field_04_1,String field_07_1,String field_11_1,
                    String field_12_1,String field_13_1, String field_14_1,String field_22_1,
                     String field_24_1,String field_25_1, String field_35_1,String field_37_1, String  field_38_1,String field_39_1,
                      String field_41_1,String field_42_1,String field_49_1 ,String field_55_1,String sign1)
    {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues args = new ContentValues();
        args.put(lastflag,lastflag1);
        args.put(dateandtime, dateandtime1);
        args.put(Header, Header1);
        args.put(MTI, MTI1);
        args.put(BITMAP, BITMAP1);
        args.put(cashier, cashier1);
        args.put(cup, cup1);
        args.put(aid, aid11);
        args.put(tvr, tvr1);
        args.put(Current_Date, Current_Date1);
        args.put(Current_Time, Current_Time1);
        args.put(Card_Holder, Card_Holder1);
        args.put(Txn_type, Txn_type1);
        args.put(Currency,Currency1);
        args.put(field_02,field_02_1);
        args.put(field_03,field_03_1);
        args.put(field_04,field_04_1);
        args.put(field_07,field_07_1);
        args.put(field_11,field_11_1);
        args.put(field_12,field_12_1);
        args.put(field_13,field_13_1);
        args.put(field_14,field_14_1);
        args.put(field_22,field_22_1);
        args.put(field_24,field_24_1);
        args.put(field_25,field_25_1);
        args.put(field_35,field_35_1);
        args.put(field_37,field_37_1);
        args.put(field_38,field_38_1);
        args.put(field_39,field_39_1);
        args.put(field_41,field_41_1);
        args.put(field_42,field_42_1);
        args.put(field_49,field_49_1);
        args.put(field_55,field_55_1);
        args.put(sign,sign1);
        // args.put(senttransaction, senttrans);

        db.insert(transactiontable, null, args);

        //db.close();

    }
    public ArrayList<User> gettransactiondata() {
        SQLiteDatabase db = DBHandler.this.getReadableDatabase();


        String sql = "SELECT * FROM " + transactiontable;
        Cursor cursor = db.rawQuery(sql, null);
        //Cursor cursor = qb.query(null, null, null, null, null, null, null);



        ArrayList<User> arrayList = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                User my = new User();
                my.setLastflag(cursor.getString(cursor.getColumnIndex("lastflag")));
                my.setDateandtime(cursor.getString(cursor.getColumnIndex("dateandtime")));
                my.setHeader(cursor.getString(cursor.getColumnIndex("Header")));
                my.setMTI(cursor.getString(cursor.getColumnIndex("MTI")));
                my.setBITMAP(cursor.getString(cursor.getColumnIndex("BITMAP")));
                my.setCashier(cursor.getString(cursor.getColumnIndex("cashier")));
                my.setCup(cursor.getString(cursor.getColumnIndex("cup")));
                my.setAid(cursor.getString(cursor.getColumnIndex("aid")));
                my.setTvr(cursor.getString(cursor.getColumnIndex("tvr")));
                my.setCurrent_Date(cursor.getString(cursor.getColumnIndex("Current_Date")));
                my.setCurrent_Time(cursor.getString(cursor.getColumnIndex("Current_Time")));
                my.setCard_Holder(cursor.getString(cursor.getColumnIndex("Card_Holder")));
                my.setTxn_type(cursor.getString(cursor.getColumnIndex("Txn_type")));
                my.setCurrency(cursor.getString(cursor.getColumnIndex("Currency")));
                my.setField_02(cursor.getString(cursor.getColumnIndex("field_02")));
                my.setField_03(cursor.getString(cursor.getColumnIndex("field_03")));
                my.setField_04(cursor.getString(cursor.getColumnIndex("field_04")));
                //my.setField_07(cursor.getString(cursor.getColumnIndex("field_07")));
                my.setField_11(cursor.getString(cursor.getColumnIndex("field_11")));
                my.setField_12(cursor.getString(cursor.getColumnIndex("field_12")));
                my.setField_13(cursor.getString(cursor.getColumnIndex("field_13")));
                my.setField_14(cursor.getString(cursor.getColumnIndex("field_14")));
                my.setField_22(cursor.getString(cursor.getColumnIndex("field_22")));
                my.setField_24(cursor.getString(cursor.getColumnIndex("field_24")));
                //my.setField_25(cursor.getString(cursor.getColumnIndex("field_25")));
                //my.setField_35(cursor.getString(cursor.getColumnIndex("field_35")));
                my.setField_37(cursor.getString(cursor.getColumnIndex("field_37")));
                my.setField_38(cursor.getString(cursor.getColumnIndex("field_38")));
                my.setField_39(cursor.getString(cursor.getColumnIndex("field_39")));
                my.setField_41(cursor.getString(cursor.getColumnIndex("field_41")));
                my.setField_42(cursor.getString(cursor.getColumnIndex("field_42")));
                //my.setField_49(cursor.getString(cursor.getColumnIndex("field_49")));
                my.setField_55(cursor.getString(cursor.getColumnIndex("field_55")));
                my.setSign(cursor.getString(cursor.getColumnIndex("sign")));

                arrayList.add(my);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // db.close(); // Uncomment this line if needed

        return arrayList;
    }

    public User getTxnDataByApprovalCode(String approvalCode){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(transactiontable);
        String[] sqlSelect = {"lastflag","MTI","BITMAP","Txn_type", "field_02", "field_03", "field_04", "field_07", "field_11", "field_12", "field_13", "field_14", "field_22", "field_24", "field_25","field_35", "field_37", "field_38", "field_39", "field_41", "field_42", "field_49", "field_55","sign"};
        Cursor cursor= qb.query(db,sqlSelect,"field_38 LIKE ?",new String[]{"%"+approvalCode+"%"},null,null,null);
        if (cursor != null)
            cursor.moveToFirst();
        User my = null;

        if(cursor.moveToFirst()){
            do{
                my=new User();
                my.setLastflag(cursor.getString(cursor.getColumnIndex("lastflag")));
                //my.setDateandtime(cursor.getString(cursor.getColumnIndex("Dateandtime")));
                //my.setHeader(cursor.getString(cursor.getColumnIndex("Header")));
                my.setMTI(cursor.getString(cursor.getColumnIndex("MTI")));
                my.setBITMAP(cursor.getString(cursor.getColumnIndex("BITMAP")));
                //my.setCashier(cursor.getString(cursor.getColumnIndex("cashier")));
                //my.setCup(cursor.getString(cursor.getColumnIndex("cup")));
                //my.setAid(cursor.getString(cursor.getColumnIndex("aid")));
                //my.setTvr(cursor.getString(cursor.getColumnIndex("tvr")));
                //my.setCurrent_Date(cursor.getString(cursor.getColumnIndex("Current_Date")));
                //my.setCurrent_Time(cursor.getString(cursor.getColumnIndex("Current_Time")));
                //my.setCard_Holder(cursor.getString(cursor.getColumnIndex("Card_Holder")));
                my.setTxn_type(cursor.getString(cursor.getColumnIndex("Txn_type")));
                //my.setCurrency(cursor.getString(cursor.getColumnIndex("Currency")));
                my.setField_02(cursor.getString(cursor.getColumnIndex("field_02")));
                my.setField_03(cursor.getString(cursor.getColumnIndex("field_03")));
                my.setField_04(cursor.getString(cursor.getColumnIndex("field_04")));
                my.setField_07(cursor.getString(cursor.getColumnIndex("field_07")));
                my.setField_11(cursor.getString(cursor.getColumnIndex("field_11")));
                my.setField_12(cursor.getString(cursor.getColumnIndex("field_12")));
                my.setField_13(cursor.getString(cursor.getColumnIndex("field_13")));
                my.setField_14(cursor.getString(cursor.getColumnIndex("field_14")));
                my.setField_22(cursor.getString(cursor.getColumnIndex("field_22")));
                my.setField_24(cursor.getString(cursor.getColumnIndex("field_24")));
                my.setField_25(cursor.getString(cursor.getColumnIndex("field_25")));
                my.setField_35(cursor.getString(cursor.getColumnIndex("field_35")));
                my.setField_37(cursor.getString(cursor.getColumnIndex("field_37")));
                my.setField_38(cursor.getString(cursor.getColumnIndex("field_38")));
                my.setField_39(cursor.getString(cursor.getColumnIndex("field_39")));
                my.setField_41(cursor.getString(cursor.getColumnIndex("field_41")));
                my.setField_42(cursor.getString(cursor.getColumnIndex("field_42")));
                my.setField_49(cursor.getString(cursor.getColumnIndex("field_49")));
                my.setField_55(cursor.getString(cursor.getColumnIndex("field_55")));
                my.setSign(cursor.getString(cursor.getColumnIndex("sign")));


            }while (cursor.moveToNext());

        }
        cursor.close();
        ////db.close();

        return my;
    }

    public void updatelastflag() {
        SQLiteDatabase db = DBHandler.this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(lastflag,"1");

        String where = "id"+" = ?";

        //db.update(tablename, null, contentValues);
        db.update(transactiontable, contentValues, where, new String[]{String.valueOf(getdatasize())});

        //db.close();

    }

    }


