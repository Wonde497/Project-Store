package com.verifone.demo.emv.transaction;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.DateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseArray;

import com.verifone.demo.emv.Applicationselectactiv;
import com.verifone.demo.emv.Cashier_menu_activity;
import com.verifone.demo.emv.DBHandler;
import com.verifone.demo.emv.MenuActivity;
import com.verifone.demo.emv.PrinterExActivity;
import com.verifone.demo.emv.Public_data.ISO8583msg;
import com.verifone.demo.emv.Supervisor_menu_activity;
import com.verifone.demo.emv.Transactiondata;
import com.verifone.demo.emv.Utilities.ActivityCollector;
import com.verifone.demo.emv.Utilities.ServiceHelper;
import com.verifone.demo.emv.basic.common;
import com.verifone.demo.emv.basic.dialog;
import com.verifone.demo.emv.basic.errordialog;
import com.verifone.demo.emv.data_handlers.User;
import com.verifone.demo.emv.loadingactivity;
import com.verifone.demo.emv.transaction.sale.PrintRecpSale;
import com.verifone.demo.emv.user_menu_activity;
import com.vfi.smartpos.deviceservice.aidl.CheckCardListener;
import com.vfi.smartpos.deviceservice.aidl.EMVHandler;
import com.vfi.smartpos.deviceservice.aidl.IBeeper;
import com.vfi.smartpos.deviceservice.aidl.IDeviceService;
import com.vfi.smartpos.deviceservice.aidl.IEMV;
import com.vfi.smartpos.deviceservice.aidl.IExternalSerialPort;
import com.vfi.smartpos.deviceservice.aidl.IPinpad;
import com.vfi.smartpos.deviceservice.aidl.IPrinter;
import com.vfi.smartpos.deviceservice.aidl.ISerialPort;
import com.vfi.smartpos.deviceservice.aidl.IUsbSerialPort;
import com.vfi.smartpos.deviceservice.aidl.OnlineResultHandler;
import com.vfi.smartpos.deviceservice.aidl.PinInputListener;
import com.vfi.smartpos.deviceservice.aidl.PinpadKeyType;
import com.vfi.smartpos.deviceservice.aidl.key_manager.IDukpt;
import com.vfi.smartpos.deviceservice.constdefine.ConstCheckCardListener;
import com.vfi.smartpos.deviceservice.constdefine.ConstIPBOC;
import com.vfi.smartpos.deviceservice.constdefine.ConstIPinpad;
import com.vfi.smartpos.deviceservice.constdefine.ConstOnlineResultHandler;
import com.vfi.smartpos.deviceservice.constdefine.ConstPBOCHandler;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import com.verifone.demo.emv.Utilities.Comm;
import com.verifone.demo.emv.Utilities.Utility;
import com.verifone.demo.emv.basic.HostInformation;
import com.verifone.demo.emv.basic.ISO8583;
import com.verifone.demo.emv.caseA.ISO8583u;
import com.verifone.demo.emv.transaction.balance.BalancePrinter;
import com.verifone.demo.emv.transaction.sale.SalePrinter;
import com.verifone.demo.emv.usecase.EmvSetAidRid;
import com.verifone.demo.emv.usecase.MultiHostsConfig;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;
/**
 * Created by Simon on 2019/2/1.
 */

public class TransBasic /*implements connection*/ {
    static SharedPreferences sharedPreferences;

    public static boolean isonline=false;
    public static boolean isNonePin1=true;
    public static boolean manualCardTransaction = false;
    public static String lastpannum;
    public byte[] transend;
    public static String lastdata1="";
    public static String transend1="";
    public DBHandler dbHandler;
    int STAN=1;
    public static Activity act,checkCardActivity;
    static String TAG = "EMVDemo-TransBasic";
    private static TransBasic instance;
    private IDeviceService idevice;
    private IEMV iemv;
    private IPinpad ipinpad;
    private IBeeper iBeeper;
    private IPrinter iPrinter;
    private IDukpt iDukpt;
    public static EMVHandler emvHandler;
    public static IExternalSerialPort iExternalSerialPort = null;    // for RS232 in the Base
    public static ISerialPort iSerialPort = null;    // for USB cable, the UART port the PC side. need install the driver
    public static IUsbSerialPort iUsbSerialPort = null;  // for OTG+USB2Serial\
    public PinInputListener pinInputListener;
    private OnGetCardNoListener onGetCardNoListener;
    private OnInputPinConfirm onInputPinConfirm;
    ISO8583msg sp;
    public Handler pinInputHandler;
    public Handler ESignHandler;
    byte[] lastdata;
    //byte[] packet=null;
    byte[] timeout_reversal;
    public boolean transStatus;
    // some client static
    String terminalID = "01020304";
    String merchantName = "X990 EMV Demo";
    String merchantID = "ABCDE0123456789";
    //transaction entry type (contact or contactless)
    int TranEntType=0;
    // keys
    int masterKeyID = 97;
    int workKeyId = 1;
    public static String Txn_type="",Txn_Menu_Type="",settle;
    public static String aidLabel1="",aid1="",tvr="",cardholder="";
    //  373E47A1A09BF2CF08387CF2A0D20D68
    public static String signdatabase="";
    String pinKey_WorkKey = "B0BCE9315C0AA31E5E6667A037DE0AC4B0BCE9315C0AA31E";
    //    String pinKey_WorkKey = "B0BCE9315C0AA31E5E6667A037DE0AC4";
    public static String Ex_Date="",responce_code="",magservice_code="";
    String macKey = "";
   // String masterKey = "426609B4B1C06341399C660CAFA5E236";  //correct
    String masterKey = "429605B4B1C06341391C660CAFA5E237";
                //426609B4B1C06341399C660CAFA5E236
    public static boolean isonrequest = false;

    //    String savedPan = "8880197100005603384";
    public static String savedPan = "",track22="",field35="",track2carddata="";
    public boolean magenabled =false;
    public static byte[] savedPinblock = null;
    Transactiondata tr;
    ISO8583 iso85831;
    public static Activity currentactivity;
    /**
     * field-value map to save iso data
     */
    SparseArray<String> tagOfF55 = null;
    SparseArray<String> tagOfF551 = null;

    /**
     * \Brief the transaction type
     * <p>
     * Prefix
     * T_ means transaction
     * M_ means management
     */
   /* connection connect;
    public void callcheck(connection connn ){
        connect=connn;
    }

    @Override
    public void toast() {

    }

    @Override
    public void finish() {
        Log.d("transbasic", "finishcalledhgjkh :" );

    }

    @Override
    public void back() {

    }*/
    //SharedPreferences transmenu = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
    //String txntype = transmenu.getString("txn_type", "");


    enum TransType
    {
        T_BALANCE,T_PURCHASE,M_LOGON,T_REVERSAL,Default,T_KEYDOWNLOAD
    }

    TransType mTransType;
    user_menu_activity user=new user_menu_activity();
    ISO8583 mIso8583;

    Handler handler;    // for UI
    public Context context;

    // for printer
    TransPrinter transPrinter;


    public TransBasic() {

    }

      public static TransBasic getInstance(SharedPreferences share/*, Activity a*/) {
        if (null == instance) {
            instance = new TransBasic();
            sharedPreferences=share;
//act=a;           dbHandler=new DBHandler(context);
        }
        return instance;
        }


      public void initTransBasic(Handler handler, Context context) {
        Log.d(TAG, "TRANSBASIC MENU STARTED ");
        Log.d(TAG, "Transaction type  is     "+MenuActivity.txn_type1);
        this.handler = handler;
        this.context = context;
        this.iemv = ServiceHelper.getInstance().getIemv();
        this.iBeeper = ServiceHelper.getInstance().getBeeper();
        this.ipinpad = ServiceHelper.getInstance().getPinpad();
        this.iDukpt = ServiceHelper.getInstance().getiDukpt();
        this.iSerialPort = ServiceHelper.getInstance().getSerialPort();
        this.iUsbSerialPort = ServiceHelper.getInstance().getUsbSerialPort();
        initializeEMV();
        initializePinInputListener();
        TransPrinter.initialize();
        Log.d(TAG, "finished");
        }

    Long amount = 0L;

    /**
     * \Brief make purchase fields
     */
    public void doPurchase() {
        Log.d(TAG, "doPurchase()............");
        // get transaction Amount from global fiel
        if(TransactionParams.getInstance().getTransactionAmount()!=null)
        {
            amount = Long.parseLong(TransactionParams.getInstance().getTransactionAmount().replace(".",""));

            doTransaction(TransType.T_PURCHASE, -1);
        }

    }
    /**
     * \Brief make balance fields
     */
    public void doBalance()
    {
        Log.d(TAG, "doBalance()............");
        doTransaction(TransType.T_BALANCE, -1);
    }

    HostInformation hostInformation;

    /**
     * \Brief make logon fields
     */
    public void doLogon(int index) {
        doTransaction(TransType.M_LOGON, index);
    }

    void doTransaction(TransType transType, int index)
    {
        mTransType = transType;

        if (transType == TransType.M_LOGON)
        {
            mIso8583 = getIso8583Packet(index, "", "", transType);
            // management, no card need
            // start onlineRequest
            new Thread(onlineRequest).start();
        } else
        {
            transPrinter = null;
            switch (transType) {
                case T_PURCHASE:
                    Log.d(TAG, "T_PURCHASE:............");
//                     transPrinter = new TransPrinter(this.context);
                    transPrinter = new SalePrinter(this.context);
                    break;
                case T_BALANCE:
                    Log.d(TAG, "T_BALANCE:............");
                    transPrinter = new BalancePrinter(this.context);
                    break;
            }
            // do search card and online request
            doSearchCard(transType);
         }

     }


    void doSearchCard(final TransType transType) {
        showUI("Waiting for Card Selected...");
        mTransType = transType;
        Bundle cardOption = new Bundle();
        cardOption.putBoolean("supportCTLSCard", true);
        cardOption.putBoolean("supportRFCard", true);
        cardOption.putBoolean("supportSmartCard", true);
        cardOption.putBoolean("supportICCard", true);
        cardOption.putBoolean(ConstIPBOC.checkCard.cardOption.KEY_MagneticCard_boolean, AppParams.getInstance().isSupportSwipe());

//        iso8583 = iso8583u;

        try {
            iemv.checkCard(cardOption, 30, new CheckCardListener.Stub() {

                        @Override
                        public void onCardSwiped(Bundle track) throws RemoteException
                        {
                            Log.d(TAG, "onCardSwiped ..cardType_magstrip is selected.");

                            sharedPreferences=context.getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                            Txn_type=sharedPreferences.getString("txn_type", "");
                            Txn_Menu_Type=sharedPreferences.getString("Txn_Menu_Type", "");
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("Txn_Menu_Type","Magstrip");
                            editor.commit();

                            Txn_type=sharedPreferences.getString("txn_type", "");
                            Txn_Menu_Type=sharedPreferences.getString("Txn_Menu_Type", "");
                            //iemv.stopCheckCard();//
                            // iemv.abortPBOC();//
                            //iemv.abortEMV();//Add Amare
                            iBeeper.startBeep(200);

                            String pan = track.getString(ConstCheckCardListener.onCardSwiped.track.KEY_PAN_String);
                            String track1 = track.getString(ConstCheckCardListener.onCardSwiped.track.KEY_TRACK1_String);
                            String track2 = track.getString(ConstCheckCardListener.onCardSwiped.track.KEY_TRACK2_String);
                            String track3 = track.getString(ConstCheckCardListener.onCardSwiped.track.KEY_TRACK3_String);
                            String serviceCode = track.getString(ConstCheckCardListener.onCardSwiped.track.KEY_SERVICE_CODE_String);
                            Ex_Date = track.getString(ConstCheckCardListener.onCardSwiped.track.KEY_EXPIRED_DATE_String);
                            track22 = track.getString(ConstCheckCardListener.onCardSwiped.track.KEY_TRACK2_String);
                            if(serviceCode!=null && serviceCode.length()>0)
                            {
                                magservice_code = serviceCode.substring(0, 1);

                            }else{

                            }
                            Log.d(TAG, "1st digit of service_code ........" + magservice_code);

                            if(serviceCode!=null && magenabled ||(!(magservice_code.equals("6") || magservice_code.equals("2") )))
                            {
                                magenabled=false;

                                Log.d(TAG, "MAGSTRIP IN PROGRESS ........");
                                //showUI("MAGSTRIP IN PROGRESS...");

                                Log.d(TAG, "1st digit of service_code ........" + magservice_code);
                                savedPan = pan;
                                onGetCardNoListener.onGetCardNo(savedPan);
                                TransactionParams.getInstance().setPan(savedPan);

                                String Cardtype="";
                                if(savedPan!=null && savedPan.length()>0)
                                {
                                    Cardtype = savedPan.substring(0, 1);
                                    Log.d(TAG, "1st digit of pan Cardtype ........" + Cardtype);

                                    if (Cardtype.equals("4")) {
                                        aidLabel1 = "VISA CARD";
                                        Log.e(TAG, "CardType is " + aidLabel1);
                                    } else if (Cardtype.equals("2") || Cardtype.equals("5")) {
                                        aidLabel1 = "MASTER CARD";
                                        Log.e(TAG, "CardType is " + aidLabel1);
                                    } else if (Cardtype.equals("3")) {
                                        aidLabel1 = "AMEX CARD";
                                        Log.e(TAG, "CardType is " + aidLabel1);
                                    } else {
                                        aidLabel1 = "";
                                    }
                                }else
                                {

                                }
//...........................................................................demo
                                Log.d(TAG, "onCardSwiped ...1");
                                byte[] bytes = Utility.hexStr2Byte(track2);
                                Log.d(TAG, "Track2:" + track2 + " (" + Utility.byte2HexStr(bytes) + ")");

                                Boolean bIsKeyExist = ipinpad.isKeyExist(0, 20);
                                if (!bIsKeyExist) {
                                    Log.e(TAG, "no key exist type: 0, @: 20");
                                }

                                mIso8583 = getIso8583Packet(-1, track2, "", mTransType);
                                if (null != track3) {
                                    mIso8583.setValue(ISO8583.ATTRIBUTE.Track3, track3);
                                }

                                String validDate = track.getString(ConstCheckCardListener.onCardSwiped.track.KEY_EXPIRED_DATE_String);
                                if (null != validDate) {
                                    mIso8583.setValue(ISO8583.ATTRIBUTE.DateOfExpired, validDate);
                                }
                                Ex_Date = validDate;

                                if (null != Ex_Date) {
                                    mIso8583.setValue(ISO8583.ATTRIBUTE.DateOfExpired, Ex_Date);
                                }
// ..................................................................
                                if(pan!=null && pan.length()>4)
                                {
                                    lastpannum = pan.substring(pan.length() - 4, pan.length());
                                    Log.d(TAG, "last 4 didit panlength  ........" + lastpannum);

                                    if (MenuActivity.txn_type1.equals("REVERSAL"))
                                    {
                                        Log.d(TAG, "Savedpannumber  "+ dbHandler.gettransactiondata().get(dbHandler.gettransactiondata().size() - 1).getField_02());

                                        if(dbHandler.gettransactiondata().get(dbHandler.gettransactiondata().size()-1).getField_02().equals(savedPan))
                                        {
                                            Log.d(TAG, "Saved Txn_type  "+ dbHandler.gettransactiondata().get(dbHandler.gettransactiondata().size() - 1).getTpe());

                                            if (dbHandler.gettransactiondata().get(dbHandler.gettransactiondata().size() - 1).getTpe().equals("PURCHASE") ||
                                                    dbHandler.gettransactiondata().get(dbHandler.gettransactiondata().size() - 1).getTpe().equals("CASH_ADVANCE"))
                                            {

                                                if (dbHandler.gettransactiondata().get(dbHandler.gettransactiondata().size() - 1).getLastflag().equals("0"))
                                                {

                                                    showUI("APPROVAL IN PROGRESS...");
                                                    Log.d(TAG, "APPROVAL IN PROGRESS... Finshied, Card Number and Last_txn Checking For Approval");


                                                    Log.d(TAG, "onCardSwiped ...1 Reversal txn is selected");
                                                    byte[] bytes1 = Utility.hexStr2Byte(pan);
                                                    Log.d(TAG, "Track2:........" + track2 + " (" + Utility.byte2HexStr(bytes) + ")");
                                                    Log.d(TAG, "Pan:.........." + pan + " (" + Utility.byte2HexStr(bytes1) + ")");


                                                    Log.d(TAG, "SavedPan........  " + savedPan);
                                                    Log.d(TAG, "Track2.......   .  " + track2);
                                                    Log.d(TAG, "Card Ex date.......  " + Ex_Date);


                                                    //Transactiondata.GLobalFields.Field04 = "B" + ISO8583.B_Amount1;
                                                    Log.d(TAG, "B_amount_1.Magstrip.....    " + Transactiondata.GLobalFields.Field04);

                                                    //Transactiondata.GLobalFields.Field35 = "q" + ";" + track22 + "?";
                                                    Log.d(TAG, "q_track2_Customer.magstrip.........   " + Transactiondata.GLobalFields.Field35);

                                                    // read card data

                                                    String result = "onConfirmCardInfo callback on Magstrip........., " +
                                                            "\nPAN:" + pan +
                                                            "\nTRACK2:" + track2 +
                                                            "\nSERVICE_CODE:" + serviceCode +
                                                            "\nEXPIRED_DATE:" + Ex_Date;

                                                    Log.d(TAG, "  " + result);

                                                    //                         Log.i(TAG, "MagInputPanActivity...........................");

                                                    Intent ii = new Intent(context, MagInputPanActivity.class);
                                                    ii.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    context.startActivity(ii);

                                                } else {
                                                    Log.d("Flage! ", "Last Transaction is not success");
                                                    showUI("LAST TXN ABORTED " +
                                                            dbHandler.gettransactiondata().get(dbHandler.gettransactiondata().size() - 1).getLastflag());

                                                    Intent ii = new Intent(context, MenuActivity.class);
                                                    ii.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    context.startActivity(ii);

                                                }

                                            } else {
                                                Log.d("ABORTED! ", "TRANSACTION CAN NOT REVERSED, TXN NOT PURCHASE");
                                                showUI("TXN CAN NOT BE REVERSED ");

                                                Intent ii = new Intent(context, MenuActivity.class);
                                                ii.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                context.startActivity(ii);
                                            }
                                        } else {
                                            Log.d("CARD ISSUE! ","INCORRECT CARD INSERTED!, TRY AGAIN ");
                                            showUI("CARD ISSUE!, INCORRECT CARD INSERTED!, TRY AGAIN  ");

                                            Intent ii = new Intent(context, MenuActivity.class);
                                            ii.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            context.startActivity(ii);
                                        }

                                    } else {

                                        if(Txn_type.equals("REFUND"))
                                        {
                                            Log.d("Magstrip  ", "REFUND IS SELECTED");
                                            showUI("REFUND IS SELECTED");
                                        }
                                        if(Txn_type.equals("PRE_AUTH_COMPLETION"))
                                        {
                                            Log.d("Magstrip  ", "PRE_AUTH_COMPLETION IS SELECTED");
                                            showUI("PRE_AUTH_COMPLETION IS SELECTED");
                                        }

                                        Log.d(TAG, "Track2:........" + track2 + " (" + Utility.byte2HexStr(bytes) + ")");

                                        Log.d(TAG, "SavedPan........  " + savedPan);
                                        Log.d(TAG, "Track2.......   .  " + track2);
                                        Log.d(TAG, "Card Ex date.......  " + Ex_Date);

                                        Transactiondata.GLobalFields.Field04 = "B" + ISO8583.B_Amount1;
                                        Log.d(TAG, "B_amount_1.Magstrip.....    " + Transactiondata.GLobalFields.Field04);

                                        Transactiondata.GLobalFields.Field35 = "q" + ";" + track22 + "?";
                                        Log.d(TAG, "q_track2_Customer.magstrip.........   " + Transactiondata.GLobalFields.Field35);

                                        // read card data

                                        String result = "onConfirmCardInfo callback on Magstrip........., " +
                                                "\nPAN:" + pan +
                                                "\nTRACK2:" + track2 +
                                                "\nSERVICE_CODE:" + serviceCode +
                                                "\nEXPIRED_DATE:" + Ex_Date;

                                        Log.d(TAG, "  " + result);

                                        //                         Log.i(TAG, "MagInputPanActivity...........................");

                                        try{
                                            Thread.sleep(800);
                                            Log.d(TAG, "Waiting time for 1000 millis and Go MagInputPanActivity");
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                            // Log.d("VIEW", "view users Amex here after loadDataTask error");
                                        }
                                        checkCardActivity.finish();

                                        Intent ii = new Intent(context, MagInputPanActivity.class);
                                        ii.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(ii);

                                        // initializeMAG();

                                    }
                                }else
                                {
                                    doPurchase();
                                    // checkCardActivity.finish();
                                    //showUI("CARD READING ISSUE...Try Again!");
                                    Log.i(TAG, "Card Reading Problem............................try again");
                                    Message msg = new Message();
                                    msg.getData().putString("msg", "");
                                    dialog3.sendMessage(msg);
                                }

                            }else
                            {

                                try{
                                    Thread.sleep(800);
                                    Log.d(TAG, "Waiting time for 1000 millis ");
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                    // Log.d("VIEW", "view users Amex here after loadDataTask error");
                                }


                                doPurchase();
                                Message msg = new Message();
                                msg.getData().putString("msg", "");
                                dialog2.sendMessage(msg);


                            }


                            //onlineRequest.run();
                            //showUI("response:" + isoResponse.getField(ISO8583u.F_ResponseCode_39));

                            // printReceipt(1);
                        }

                        @Override
                        public void onCardPowerUp() throws RemoteException {

                            sharedPreferences=context.getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                            Txn_type=sharedPreferences.getString("txn_type", "");
                            Txn_Menu_Type=sharedPreferences.getString("Txn_Menu_Type", "");
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("Txn_Menu_Type","Chip");
                            editor.commit();
                            Txn_type=sharedPreferences.getString("txn_type", "");
                            Txn_Menu_Type=sharedPreferences.getString("Txn_Menu_Type", "");
                            //iemv.stopCheckCard();
                            //iemv.abortEMV();

                            iBeeper.startBeep(200);
                            Log.i(TAG, "oncard power up");

                            // set aid to kernel
                            List<String> tags = new ArrayList<String>();
                            tags.add("5F2A020230");
                            iemv.setEMVData(tags);
                            doEMV(ConstIPBOC.startEMV.intent.VALUE_cardType_smart_card, transType);


                        }

                        @Override
                        public void onCardActivate() throws RemoteException {

                            sharedPreferences=context.getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                            Txn_type=sharedPreferences.getString("txn_type", "");
                            Txn_Menu_Type=sharedPreferences.getString("Txn_Menu_Type", "");
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("Txn_Menu_Type","ContactLess");
                            editor.commit();
                            Txn_type=sharedPreferences.getString("txn_type", "");
                            Txn_Menu_Type=sharedPreferences.getString("Txn_Menu_Type", "");
                            //iemv.stopCheckCard();
                            //iemv.abortEMV();

                            iBeeper.startBeep(200);
                            doEMV(ConstIPBOC.startEMV.intent.VALUE_cardType_contactless, transType);
                            Log.i(TAG, "do emv ctls return");
                            //showUI("............");


                            //if(savedPan.equals(""))
                            // {
                            //   Message msg = new Message();
                            //  msg.getData().putString("msg", "");
                            // dialog3.sendMessage(msg);
                            // }




                        }

                        @Override
                        public void onTimeout() throws RemoteException {
                            showUI("timeout");
                        }

                        @Override
                        public void onError(int error, String message) throws RemoteException {
                            showUI("error:" + error + ", msg:" + message);
                        }

                    }
            );
        } catch (RemoteException e) {
            e.printStackTrace();
            Log.d(TAG, "onCardSwiped error ...");

        }
    }


    /**
     * \brief sample of EMV
     * <p>
     * \code{.java}
     * \endcode
     *
     * @see
     */
    void doEMV(int type, TransType transType) {
        //
        dbHandler = new DBHandler(context);
        Log.i(TAG, "start EMV demo");
        Log.i(TAG, "EMV demo TransType menuuu.......... ."+MenuActivity.txn_type1);
        Log.i(TAG, "EMV demo TransType.......... ."+transType);
        Bundle emvIntent = new Bundle();
        emvIntent.putBoolean("isForceOnline", true);
        emvIntent.putInt(ConstIPBOC.startEMV.intent.KEY_cardType_int, type);
        if (transType == TransType.T_PURCHASE)
        {

            emvIntent.putLong(ConstIPBOC.startEMV.intent.KEY_authAmount_long, amount);
        }
        emvIntent.putString(ConstIPBOC.startEMV.intent.KEY_merchantName_String, merchantName);

        emvIntent.putString(ConstIPBOC.startEMV.intent.KEY_merchantId_String, merchantID);  // 010001020270123
        emvIntent.putString(ConstIPBOC.startEMV.intent.KEY_terminalId_String, terminalID);   // 00000001
        emvIntent.putBoolean(ConstIPBOC.startEMV.intent.KEY_isSupportQ_boolean, ConstIPBOC.startEMV.intent.VALUE_supported);
//        emvIntent.putBoolean(ConstIPBOC.startEMV.intent.KEY_isSupportQ_boolean, ConstIPBOC.startEMV.intent.VALUE_unsupported);
        emvIntent.putBoolean(ConstIPBOC.startEMV.intent.KEY_isSupportSM_boolean, ConstIPBOC.startEMV.intent.VALUE_supported);
        emvIntent.putBoolean(ConstIPBOC.startEMV.intent.KEY_isQPBOCForceOnline_boolean, ConstIPBOC.startEMV.intent.VALUE_unforced);
        if (type == ConstIPBOC.startEMV.intent.VALUE_cardType_contactless)
        {   // todo, check here
            emvIntent.putByte(ConstIPBOC.startEMV.intent.KEY_transProcessCode_byte, (byte) 0x00);
            TranEntType=2;
        }
        else{
            TranEntType=1;
        }
        emvIntent.putBoolean("isSupportPBOCFirst", false);
//        emvIntent.putString(ConstIPBOC.startEMV.intent.KEY_transCurrCode_String, "0156");
//        emvIntent.putString(ConstIPBOC.startEMV.intent.KEY_otherAmount_String, "0");

        try {
            iemv.startEMV(ConstIPBOC.startEMV.processType.full_process, emvIntent, emvHandler);
            transStatus = true;
            Log.i(TAG, "start EMV demo1");
        } catch (RemoteException e) {
            e.printStackTrace();
            Log.i(TAG, "start EMV demo2 catch");
        }
        Log.i(TAG, "start EMV demo2");


        //Message mesg = new Message();
        //mesg.getData().putString("msg", "");
        // dialog.sendMessage(mesg);

    }

    public void keydown(){
        //emvHandler.onRequestOnlineProcess();
    }
    public void selectapp(int index) throws RemoteException {
        Log.i(TAG, "index is: "+String.valueOf(index));
        iemv.importAppSelection(index);
    }

    public void initializeEMV() {

        /**
         * \brief initialize the call back listener of EMV
         *
         *  \code{.java}
         * \endcode
         * @version
         * @see
         *
         */
        emvHandler = new EMVHandler.Stub() {
            @Override
            public void onRequestAmount() throws RemoteException {
                // this is an deprecated callback
                // need set Amount while calling startEMV
            }

            @Override
            public void onSelectApplication(List<Bundle> appList) throws RemoteException {
                ArrayList<User> list = new ArrayList<>();
                for (Bundle aidBundle : appList)
                {

                    String aidName = aidBundle.getString("aidName");
                    String aid = aidBundle.getString("aid");
                    String aidLabel = aidBundle.getString("aidLabel");
                    Log.i(TAG, "AID Name=" + aidName + " | AID Label=" + aidLabel + " | AID=" + aid);
                    //      showUI("onSelectApplication..." + appList.get(0));
                    if(!aidLabel.equals("")){
                        User mLog = new User();
                        mLog.setAidlable(aidLabel);
                        list.add(mLog);
                    }

                }
                if(appList.size()>3)
                {
                    Applicationselectactiv.myList=list;
                    Intent i = new Intent(context, Applicationselectactiv.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);

                }else {
                    iemv.importAppSelection(0);
                }

                showUI("onSelectApplication..." + appList.get(0));
            }
            // EMV Handler . onSelectApplication in List Bundle > appList
            /**
             * \brief confirm the card info
             *
             * showthe card info and import the confirm result
             * \code{.java}
             * \endcode
             *
             */
            @Override
            public void onConfirmCardInfo(Bundle info) throws RemoteException {
                Log.d(TAG, "onConfirmCardInfo...");
                // checkCardActivity.progress();
               /* Intent i = new Intent(context, loadingactivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);*/
                // ((Activity)con1).finish();
                dbHandler=new DBHandler(context);

                savedPan = info.getString(ConstPBOCHandler.onConfirmCardInfo.info.KEY_PAN_String);
                field35=info.getString(ConstPBOCHandler.onConfirmCardInfo.info.KEY_TRACK2_String);
                track2carddata=info.getString(ConstPBOCHandler.onConfirmCardInfo.info.KEY_TRACK2_String);
                onGetCardNoListener.onGetCardNo(savedPan);
                TransactionParams.getInstance().setPan(savedPan);
                Log.d(TAG, "savedPan..."+savedPan);
                if(savedPan.equals(""))
                {

                    Message msg = new Message();
                    msg.getData().putString("msg", "");
                    dialog3.sendMessage(msg);
                }

                String result = "onConfirmCardInfo callback, " +
                        "\nPAN:" + savedPan +
                        "\nTRACK2:" + info.getString(ConstPBOCHandler.onConfirmCardInfo.info.KEY_TRACK2_String) +
                        "\nCARD_SN:" + info.getString(ConstPBOCHandler.onConfirmCardInfo.info.KEY_CARD_SN_String) +
                        "\nSERVICE_CODE:" + info.getString(ConstPBOCHandler.onConfirmCardInfo.info.KEY_SERVICE_CODE_String) +
                        "\nEXPIRED_DATE:" + info.getString(ConstPBOCHandler.onConfirmCardInfo.info.KEY_EXPIRED_DATE_String);

                // read card data
                byte[] tlv = iemv.getCardData("9F51");
                result += ("\n9F51:" + Utility.byte2HexStr(tlv));
                Ex_Date=info.getString(ConstPBOCHandler.onConfirmCardInfo.info.KEY_EXPIRED_DATE_String);

                int[] tagList = {
                        0x4F,   // Application Identifier (AID) â€“ card
                        0x9F06, // Application Identifier (AID) â€“ terminal
                        0x98,
                        0x50,
                        0x9F12,
                        0x9F26,
                        0x9F27,
                        0x9F10,
                        0x9F37,
                        0x9F36,
                        0x95,
                        0x9A,
                        0x9C,
                        0x9F02,
                        0x5F2A,
                        0x82,
                        0x9F1A,
                        0x9F03,
                        0x9F33,
                        0x9F74,
                        0x9F24,
                        0x5F36,
                        0x5F20,//22
                        0x5F34,
                        0x9F35,
                        0x9F34,
                        0xF08,
                        84,
                        0x9F27,
                        0x55
                };
                String aid = "";
                List<String> tags = new ArrayList<String>();
                tags.add("5F2A020230");
                tags.add("9F1A020231");
                iemv.setEMVData(tags);

                for (int tag : tagList) {
                    tlv = iemv.getCardData(Integer.toHexString(tag).toUpperCase());
                    if (null != tlv && tlv.length > 0) {
                        Log.d(TAG, "1st TagList CardData:" + Integer.toHexString(tag).toUpperCase() + ", value:" + Utility.byte2HexStr(tlv));
                        switch (tag) {
                            case 0x4F:
                                aid = Utility.byte2HexStr(tlv);
                                Log.d(TAG, "AID tlv:" + aid);
                                break;
                        }
                    } else {
                        Log.e(TAG, "getCardData:" + Integer.toHexString(tag) + ", fails");
                    }
                }
                tvr  =Utility.byte2HexStr(iemv.getCardData(Integer.toHexString(tagList[10]).toUpperCase())) ;
                Log.e(TAG, "1st TAG TVR:" + tvr);

                aidLabel1=hexToASCII(Utility.byte2HexStr(iemv.getCardData(Integer.toHexString(tagList[4]).toUpperCase())));
                Log.e(TAG, "1st TAG AMEE HEXTOASCII aidLabel1:" + aidLabel1);
                //5F20
                cardholder=hexToASCII(Utility.byte2HexStr(iemv.getCardData(Integer.toHexString(tagList[22]).toUpperCase())));
                Log.e(TAG, "1st TAG AMEE HEXTOASCII cardholdername:" + cardholder);

                //hexToASCII(Utility.byte2HexStr(response)));

                aid1=Utility.byte2HexStr(iemv.getCardData(Integer.toHexString(tagList[0]).toUpperCase()));
                Log.e(TAG, "1ST TAG aid1:" + aid1);

                String track2 = info.getString(ConstPBOCHandler.onConfirmCardInfo.info.KEY_TRACK2_String);
                if (null != track2) {
                    int a = track2.indexOf('D');
                    if (a > 0) {
                        track2 = track2.substring(0, a);
                    }
//                    data8583.put(ISO8583u.F_Track_2_Data_35, track2);
                }
                mIso8583 = getIso8583Packet( -1, track2, aid, mTransType );

                mIso8583.setValue(ISO8583.ATTRIBUTE.Track2, track2);

                //showUI("onConfirmCardInfo:" + result);
                Log.d(TAG, "onConfirmCardInfo:" + result);

                //                importCardConfirmResult();
            }

            /**
             * \brief show the pin pad
             *
             *  \code{.java}
             * \endcode
             *
             */
            @Override
            public void onRequestInputPIN(boolean isOnlinePin, int retryTimes) throws RemoteException {
                //showUI("onRequestInputPIN isOnlinePin:" + isOnlinePin + ",Retry Times:" + retryTimes);
                Log.d(TAG, "Here requesting pin...");
                pinInputHandler.sendMessage(new Message());
                // show the pin pad, import the pin block
                TransactionParams.getInstance().setOnlinePin(isOnlinePin);
                TransactionParams.getInstance().setRetryTimes(retryTimes);
                isonline = isOnlinePin;
                Log.d(TAG, "isonline:"+isonline);

            }

            @Override
            public void onConfirmCertInfo(String certType, String certInfo) throws RemoteException {
                //showUI("onConfirmCertInfo, type:" + certType + ",info:" + certInfo);

                iemv.importCertConfirmResult(ConstIPBOC.importCertConfirmResult.option.CONFIRM);
            }

            @Override
            public void onRequestOnlineProcess(Bundle aaResult) throws RemoteException {
                Log.d(TAG, "onRequestOnlineProcess...");
                isonrequest=true;

                // ESignHandler.sendMessage(new Message());
                Log.d(TAG, "after send message...");

                int result = aaResult.getInt(ConstPBOCHandler.onRequestOnlineProcess.aaResult.KEY_RESULT_int);
                Log.d(TAG, "after result...");
//                boolean signature = aaResult.getBoolean(ConstPBOCHandler.onRequestOnlineProcess.aaResult.KEY_SIGNATURE_boolean);
                boolean signature = false;
                Log.d(TAG, "after signature false...");
                //showUI("onRequestOnlineProcess result=" + result + " signal=" + signature);
                switch (result) {
                    case ConstPBOCHandler.onRequestOnlineProcess.aaResult.VALUE_RESULT_AARESULT_ARQC:
                        Log.d(TAG, "VALUE_RESULT_AARESULT_ARQC...");
                    case ConstPBOCHandler.onRequestOnlineProcess.aaResult.VALUE_RESULT_QPBOC_ARQC:
                        Log.d(TAG, "VALUE_RESULT_QPBOC_ARQC...");
                        //showUI(aaResult.getString(ConstPBOCHandler.onRequestOnlineProcess.aaResult.KEY_ARQC_DATA_String));
                        break;
                    case ConstPBOCHandler.onRequestOnlineProcess.aaResult.VALUE_RESULT_PAYPASS_EMV_ARQC:
                        Log.d(TAG, "VALUE_RESULT_PAYPASS_EMV_ARQC...");
                        break;
                }

                //showUI("CVM = " + aaResult.getInt("CTLS_CVMR"));

                tagOfF551 = new SparseArray<>();
                byte[] tlv;
                int[] tagList = {
                        0x4F,   // 0 - Application Identifier (AID) - Card
                        0x9F06, // 1 - Application Identifier (AID) - Terminal
                        0x98,   // 2 - Personal Identification Number (PIN) for offline card authentication
                        0x50,   // 3 - Application Label
                        0x9F12, // 4 - Application Preferred Name
                        0x9F26, // 5 - Application Cryptogram
                        0x9F27, // 6 - Cryptogram Information Data
                        0x9F10, // 7 - Issuer Application Data (IAD)
                        0x9F37, // 8 - Unpredictable Number
                        0x9F36, // 9 - Application Transaction Counter (ATC)
                        0x95,   // 10 - Terminal Verification Results (TVR)
                        0x9A,   // 11 - Transaction Date
                        0x9C,   // 12 - Transaction Type
                        0x9F02, // 13 - Amount, Authorized (Numeric)
                        0x5F2A, // 14 - Transaction Currency Code
                        0x82,   // 15 - Application Interchange Profile
                        0x9F1A, // 16 - Terminal Country Code
                        0x9F03, // 17 - Amount, Other (Numeric)
                        0x9F33, // 18 - Terminal Capabilities
                        0x9F74, // 19 - Extended Selection
                        0x9F24, // 20 - Application Expiration Date
                        0x5F36, // 21 - Transaction Currency Exponent
                        0x5F20, // 22 - Cardholder Name
                        0x5F34, // 23 - Application Primary Account Number (PAN) Sequence Number
                        0x9F35, // 24 - Terminal Type
                        0x9F34, // 25 - Cardholder Verification Method (CVM) Results
                        0x9F08, // 26 - Application Version Number
                        0x84,   // 27 - Dedicated File (DF) Name
                        0x9F27, // 28 - Cryptogram Information Data
                        0x57,   // 29 - Track 2 Equivalent Data
                        0x5A,   // 30 - Application Primary Account Number (PAN)
                        0x9F6E, // 31 - Form Factor Indicator (FFI)
                        0x9F7C, // 32 - Card Transaction Qualifiers (CTQ)
                        0x9f66, // 33 - Terminal Transaction Qualifiers (TTQ)
                        0x9B,   // 34 - Personal Identification Number (PIN) Try Counter
                        0x55,   // 35 - Integrated Circuit Card (ICC) Public Key Certificate
                        0x9F09, // 36 - Application Version Number
                        0x9F1E, // 37 - Interface Device Serial Number
                        0x9F41  // 38 - Transaction Sequence Counter (TSC)
                };

//.........................Global Bank F55 For Sale..............................................................
                byte[] tlvdata;

                int[] tagListfield55 = {
                        0x82,    // 0: Application Interchange Profile
                        0x84,    // 1: Dedicated File (DF) Name
                        0x95,    // 2: Terminal Verification Results (TVR)
                        0x9A,    // 3: Transaction Date (YYMMDD)
                        0x9C,    // 4: Transaction Type
                        0x5F2A,  // 5: Transaction Currency Code
                        0x5F34,  // 6: Application Primary Account Number (PAN) Sequence Number
                        0x9F02,  // 7: Amount, Authorized (Numeric)
                        0x9F10,  // 8: Issuer Application Data (IAD)
                        0x9F1A,  // 9: Terminal Country Code
                        0x57,    // 10: Track 2 Equivalent Data
                        0x9F26,  // 11: Application Cryptogram
                        0x9F27,  // 12: Cryptogram Information Data
                        0x9F33,  // 13: Terminal Capabilities
                        0x9F34,  // 14: Cardholder Verification Method (CVM) Results
                        0x9F35,  // 15: Terminal Type
                        0x9F36,  // 16: Application Transaction Counter (ATC)
                        0x9F37   // 17: Unpredictable Number
                };



                tagOfF55 = new SparseArray<>();
                List<Integer> insertionOrder = new ArrayList<>();

                for (int tag : tagListfield55)
                {
                    tlvdata = iemv.getCardData(Integer.toHexString(tag).toUpperCase());
                    if (null != tlvdata && tlvdata.length > 0)
                    {
                        Log.d(TAG, "Tag Field_55 CardData:" + Integer.toHexString(tag).toUpperCase() + ", Tag Field_55_value:" + Utility.byte2HexStr(tlvdata));
                        tagOfF55.put(tag, Utility.byte2HexStr(tlvdata));  // build up the field 55 tags sorted in ascending order
                        //put the tags in the arraylist so that tags stay unsorted(in original order) for retrieval purpose
                        insertionOrder.add(tag);
                        Log.d("TAG","tagOfF55" + tagOfF55);
                    } else {
                        Log.e(TAG, "Tag Field_55 CardData: " + Integer.toHexString(tag) + ", fails");
                    }
                }

                String field55="";
                if (tagOfF55 != null)
                {
                    for (int itag : insertionOrder)
                    {
                        int tag = itag;
                        String value = tagOfF55.get(itag);
                        Log.i("TAG","Tag And Value"+tag+value);
                        if (value.length() > 0)
                        {
                            byte[] tmp = mIso8583.appendF55(tag, value);
                            Log.d("TAG","temp"+tmp);
                            if (tmp == null)
                            {
                                Log.e(TAG, "error of tag:" + Integer.toHexString(tag) + ", value:" + value);
                            } else
                            {
                                //Log.d(TAG, "append F55 tag:" + Integer.toHexString(tag) + ", value:" + Utility.byte2HexStr(tmp));
                                field55= field55 + Utility.byte2HexStr(tmp);
                                Log.d(TAG,"field55 :"+field55);
                            }
                        }
                    }
                    tagOfF55 = null;
                    int length55 = 0;
                    length55=field55.length();
                    length55=length55/2;
                    String leng55=Integer.toString(length55);
                    //field55 =  leng55+Utility.asc2Hex(field55);

                    Log.d(TAG, "value of field_55 : " +field55);


                    Log.d(TAG, "length55 : " +length55);


                }

                Transactiondata.GLobalFields.Field55 = field55;


                for (int tag : tagList)
                {
                    tlv = iemv.getCardData(Integer.toHexString(tag).toUpperCase());

                    Log.e(TAG, "tlv :" + tlv);

                    if (null != tlv && tlv.length > 0)
                    {
                        Log.d(TAG, "2nd TagList CardData:" + Integer.toHexString(tag).toUpperCase() + ", value:" + Utility.byte2HexStr(tlv));
                        tagOfF551.put(tag, Utility.byte2HexStr(tlv));  // build up the field 55
                    } else
                    {
                        Log.e(TAG, "getCardData:" + Integer.toHexString(tag) + ", fails");
                    }
                }

                // field_02 pan number
                if(savedPan.length()%2==0)
                {
                    ISO8583.ISO8583_FIELD.field_02=  savedPan.length() + savedPan;
                    Log.d(TAG, "field_02 :" + ISO8583.ISO8583_FIELD.field_02);
                    //ISO8583.ISO8583_FIELD.field_02=Utility.byte2HexStr((ISO8583.ISO8583_FIELD.field_02).getBytes());
                    Log.d(TAG, "field_02 hex:" + ISO8583.ISO8583_FIELD.field_02);
                }else
                {
                    ISO8583.ISO8583_FIELD.field_02=  savedPan.length() + "0" +  savedPan;
                    Log.d(TAG, "field_02 :" + ISO8583.ISO8583_FIELD.field_02);

                    //ISO8583.ISO8583_FIELD.field_02=Utility.byte2HexStr((ISO8583.ISO8583_FIELD.field_02).getBytes());
                    Log.d(TAG, "field_02 hex :" + ISO8583.ISO8583_FIELD.field_02);
                }


                // field_04 Amount
                ISO8583.ISO8583_FIELD.field_04=Utility.byte2HexStr(iemv.getCardData(Integer.toHexString(tagList[13]).toUpperCase()));
                Log.d(TAG, "field_04 :" +  ISO8583.ISO8583_FIELD.field_04);

                // ISO8583.ISO8583_FIELD.field_04=Utility.byte2HexStr((ISO8583.ISO8583_FIELD.field_04).getBytes());
                Log.d(TAG, "field_04 hex:" + ISO8583.ISO8583_FIELD.field_04);

                //Field_11
                STAN=Integer.parseInt(sharedPreferences.getString("STAN","1"));
                Log.d(TAG, "STAN "+fillgapsequence1(String.valueOf(STAN),6));
                STAN=STAN+1;
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("STAN",String.valueOf(STAN));
                editor.commit();

                ISO8583.ISO8583_FIELD.field_11=fillgapsequence1(String.valueOf(STAN),6);
                Log.e(TAG, "field_11...: " + ISO8583.ISO8583_FIELD.field_11);
                //ISO8583.ISO8583_FIELD.field_11=Utility.byte2HexStr((ISO8583.ISO8583_FIELD.field_11).getBytes());
                Log.e(TAG, "field_11 HEX...: " + ISO8583.ISO8583_FIELD.field_11);


                // field_14 EX DATE 5f24
                Log.d(TAG, "field_14 :" + Ex_Date);
                ISO8583.ISO8583_FIELD.field_14=Ex_Date;
                //ISO8583.ISO8583_FIELD.field_14=Ex_Date=Utility.byte2HexStr((ISO8583.ISO8583_FIELD.field_14=Ex_Date).getBytes());
                Log.d(TAG, "field_14 hex :" + ISO8583.ISO8583_FIELD.field_14);

// field_35 Track2 Tag 57
                int PANlength1=0;
                String PAN1=Utility.byte2HexStr(iemv.getCardData(Integer.toHexString(tagList[29]).toUpperCase()));
                PANlength1=PAN1.length();
                char lastchar1=PAN1.charAt(PAN1.length() - 1);
                if(PAN1.charAt(PAN1.length() - 1)!='F')
                {
                    PAN1=PAN1+"F";
                }
                //String track21=PAN1.replace('D','=');//replaces all occurrences of 'D' to '='

                field35=field35.replace("F","");//replaces all occurrences of 'F' to '?'
                Log.d(TAG, "field_35 :" + field35);


                //String q_track2_Customer1= Utility.byte2HexStr(iemv.getCardData(Integer.toHexString(tagList[29]).toUpperCase()));
                ISO8583.ISO8583_FIELD.field_35 = field35;//

                // ISO8583.ISO8583_FIELD.field_02= savedPan.length() +  savedPan;
                Log.e(TAG, "field_35 org...: " + field35);
                Log.e(TAG, "field_35 leng...: " + field35.length());
                if(field35.length()%2==0)
                {
                    ISO8583.ISO8583_FIELD.field_35 = field35.length() + field35;
                    Log.e(TAG, "field_35...: " + ISO8583.ISO8583_FIELD.field_35);
                    //ISO8583.ISO8583_FIELD.field_35=Utility.byte2HexStr((ISO8583.ISO8583_FIELD.field_35).getBytes());
                    // Log.d(TAG, "field_35 hex : " + ISO8583.ISO8583_FIELD.field_35);
                }else
                {
                    ISO8583.ISO8583_FIELD.field_35= field35.length() + "0" + field35;
                    Log.e(TAG, "field_35...: " + ISO8583.ISO8583_FIELD.field_35);

                }
                // field_41 tid
                ISO8583.ISO8583_FIELD.field_41= sharedPreferences.getString("terminalid","termid");
                Log.d(TAG, "field_41  : " + ISO8583.ISO8583_FIELD.field_41);

                ISO8583.ISO8583_FIELD.field_41=Utility.byte2HexStr((ISO8583.ISO8583_FIELD.field_41).getBytes());
                Log.d(TAG, "field_41 hex : " + ISO8583.ISO8583_FIELD.field_41);

                // field_42 mid
                ISO8583.ISO8583_FIELD.field_42= sharedPreferences.getString("merchantid","merchantid");
                Log.d(TAG, "field_42  : " + ISO8583.ISO8583_FIELD.field_42);
                ISO8583.ISO8583_FIELD.field_42=Utility.byte2HexStr((ISO8583.ISO8583_FIELD.field_42).getBytes());
                Log.d(TAG, "field_42 hex : " + ISO8583.ISO8583_FIELD.field_42);

// field_55 emv  Tag 55
                tagOfF55 = new SparseArray<>();
                byte[] tlv1;
                int[] tagList1 = {
                        0x82,   // 0 - Application Interchange Profile
                        0x84,   // 1 - Dedicated File (DF) Name
                        0x95,   // 2 - Terminal Verification Results (TVR)
                        0x9A,   // 3 - Transaction Date
                        0x9C,   // 4 - Transaction Type
                        0x5F2A, // 5 - Transaction Currency Code
                        0x5F34, // 6 - Application Primary Account Number (PAN) Sequence Number
                        0x9F02, // 7 - Amount, Authorized (Numeric)
                        0x9F09, // 8 - Application Version Number (Card)
                        0x9F10, // 9 - Issuer Application Data (IAD)
                        0x9F1A, // 10 - Terminal Country Code
                        0x9F1E, // 11 - Interface Device (IFD) Serial Number
                        0x9F26, // 12 - Application Cryptogram
                        0x9F27, // 13 - Cryptogram Information Data
                        0x9F33, // 14 - Terminal Capabilities
                        0x9F34, // 15 - Cardholder Verification Method (CVM) Results
                        0x9F35, // 16 - Terminal Type
                        0x9F36, // 17 - Application Transaction Counter (ATC)
                        0x9F37, // 18 - Unpredictable Number
                        0x9F41  // 19 - Transaction Sequence Counter (TSC)
                };

                for (int tag : tagList1)
                {
                    tlv1 = iemv.getCardData(Integer.toHexString(tag).toUpperCase());
                    if (null != tlv1 && tlv1.length > 0)
                    {
                        Log.d(TAG, "Tag Field_55 CardData:" + Integer.toHexString(tag).toUpperCase() + ", Tag Field_55_value:" + Utility.byte2HexStr(tlv1));
                        tagOfF55.put(tag, Utility.byte2HexStr(tlv1));  // build up the field 55
                    } else {
                        Log.e(TAG, "Tag Field_55 CardData: " + Integer.toHexString(tag) + ", fails");
                    }
                }

                String Total_value="";
                if (tagOfF55 != null)
                {
                    for (int i = 0; i < tagOfF55.size(); i++)
                    {
                        int tag = tagOfF55.keyAt(i);
                        String value = tagOfF55.valueAt(i);
                        if (value.length() > 0)
                        {
                            byte[] tmp = mIso8583.appendF55(tag, value);
                            if (tmp == null)
                            {
                                Log.e(TAG, "error of tag:" + Integer.toHexString(tag) + ", value:" + value);
                            } else
                            {
                                //Log.d(TAG, "append F55 tag:" + Integer.toHexString(tag) + ", value:" + Utility.byte2HexStr(tmp));

                                Total_value= Total_value + Utility.byte2HexStr(tmp);

                            }
                            //Log.d(TAG, "Total_value of filed_55 :" +Total_value);
                            ISO8583.ISO8583_FIELD.field_55=Total_value;
                        }
                    }
                    tagOfF55 = null;

                    Log.d(TAG, "value of filed_55 : " +Total_value);

                }


                int len55=Total_value.length();

                // Log.d(TAG, "Total length F55........... :"+len55);
                // String leng =Integer.toHexString(len55);
                len55=len55/2;
                String leng55=Integer.toString(len55);
                //String leng55 =Integer.toHexString(len55/2);

                if(leng55.length()==2)
                {
                    leng55="00"+leng55;

                }   else  if(leng55.length()==3)
                {
                    leng55="0"+leng55;

                } if(leng55.length()==4)
                {

                }
                Log.d(TAG, "lenhex F55........... :"+leng55);

                ISO8583.ISO8583_FIELD.field_55=leng55 + ISO8583.ISO8583_FIELD.field_55;

                Log.d(TAG, "Total_value of filed_55 : " +ISO8583.ISO8583_FIELD.field_55);

// Filed_62  Invoce number
                ISO8583.ISO8583_FIELD.field_62="0006";
                Log.d(TAG, "Filed_62  : "+ISO8583.ISO8583_FIELD.field_62);

                ISO8583.ISO8583_FIELD.field_62=ISO8583.ISO8583_FIELD.field_62 + (Utility.byte2HexStr(ISO8583.ISO8583_FIELD.field_11.getBytes()));
                //ISO8583.ISO8583_FIELD.field_62=Utility.byte2HexStr(Invoce_number.getBytes()) + hexToASCII(ISO8583.ISO8583_FIELD.field_11);
                Log.d(TAG, "Filed_62 : "+ISO8583.ISO8583_FIELD.field_62);



//.....................................................................

                Log.d(TAG, "mydata:" + Utility.byte2HexStr(iemv.getCardData(Integer.toHexString(tagList[13]).toUpperCase())));

                aid1=Utility.byte2HexStr(iemv.getCardData(Integer.toHexString(tagList[0]).toUpperCase()));
                Log.e(TAG, "2nd TAG aid1:" + aid1);

                tvr  =Utility.byte2HexStr(iemv.getCardData(Integer.toHexString(tagList[10]).toUpperCase())) ;
                Log.e(TAG, "2nd TAG TVR:" + tvr);

                aidLabel1=hexToASCII(Utility.byte2HexStr(iemv.getCardData(Integer.toHexString(tagList[4]).toUpperCase())));
                Log.e(TAG, "CardType From TAG AMEE HEXTOASCII aidLabel1:" + aidLabel1);

                cardholder=hexToASCII(Utility.byte2HexStr(iemv.getCardData(Integer.toHexString(tagList[22]).toUpperCase())));
                Log.e(TAG, "Cardholder name: " + cardholder);
                //savedPan
                String Cardtype="";
                Cardtype = savedPan.substring(0, 1);
                Log.d(TAG, "1st digit of pan Cardtype ........" + Cardtype);

                if(aidLabel1.equals(""))
                {
                    if(Cardtype.equals("4"))
                    {
                        aidLabel1="VISA CARD";
                        Log.e(TAG, "CardType is " + aidLabel1);
                    }
                    else if(Cardtype.equals("2")|| Cardtype.equals("5"))
                    {
                        aidLabel1="MASTER CARD";
                        Log.e(TAG, "CardType is " + aidLabel1);
                    }
                    else if(Cardtype.equals("3"))
                    {
                        aidLabel1= "AMEX CARD";
                        Log.e(TAG, "CardType is " + aidLabel1);
                    }else
                    {
                        aidLabel1= "";
                        Log.e(TAG, "CardType is " + aidLabel1);
                    }

                }
                //user.toast("toasted");
                sharedPreferences=context.getSharedPreferences("Shared_data", Context.MODE_PRIVATE);

                Txn_type=sharedPreferences.getString("txn_type", "");
                Txn_Menu_Type=sharedPreferences.getString("Txn_Menu_Type", "");

                Log.d(TAG, "Txn_type.Manual.........:" + Txn_type);
                Log.d(TAG, "Txn_menu_type.Manual.........:" + Txn_Menu_Type);

                tr=new Transactiondata(sharedPreferences,context);
                sp=new ISO8583msg(context);
                sp.loadData();         // Terminal id
                sp.loadData1();        // Merchant id
                sp.loadCurrencyData(); // Currency type
                if(Objects.equals(Txn_type,"REVERSAL"))
                {
                    ISO8583.ISO8583_FIELD.field_04="";
                    ISO8583.ISO8583_FIELD.field_37="";
                    ISO8583.ISO8583_FIELD.field_38="";
                    ISO8583.ISO8583_FIELD.field_39="";
                    ISO8583.ISO8583_FIELD.field_04 =dbHandler.gettransactiondata().get(dbHandler.gettransactiondata().size()-1).getField_04();
                    Log.d(TAG, "Normal reversalAmount..."+ ISO8583.ISO8583_FIELD.field_04);

                    // Filed_12   time
                    ISO8583.ISO8583_FIELD.field_12 =dbHandler.gettransactiondata().get(dbHandler.gettransactiondata().size()-1).getCurrent_Time();
                    Log.d(TAG, "Filed_12 Time "+ ISO8583.ISO8583_FIELD.field_12);
                    // Filed_13  date
                    ISO8583.ISO8583_FIELD.field_13 =dbHandler.gettransactiondata().get(dbHandler.gettransactiondata().size()-1).getCurrent_Date();;
                    Log.d(TAG, "Filed_13 Date "+ ISO8583.ISO8583_FIELD.field_13);

                    ISO8583.ISO8583_FIELD.field_37=dbHandler.gettransactiondata().get(dbHandler.gettransactiondata().size()-1).getField_37();
                    Log.d(TAG, "Normal reversal field_37..."+ ISO8583.ISO8583_FIELD.field_37);
                    ISO8583.ISO8583_FIELD.field_37=Utility.byte2HexStr((ISO8583.ISO8583_FIELD.field_37).getBytes());

                    ISO8583.ISO8583_FIELD.field_38=dbHandler.gettransactiondata().get(dbHandler.gettransactiondata().size()-1).getField_38();
                    Log.d(TAG, "Normal reversal field_38..."+ ISO8583.ISO8583_FIELD.field_38);
                    ISO8583.ISO8583_FIELD.field_38=Utility.byte2HexStr((ISO8583.ISO8583_FIELD.field_38).getBytes());

                    ISO8583.ISO8583_FIELD.field_39=dbHandler.gettransactiondata().get(dbHandler.gettransactiondata().size()-1).getField_39();
                    Log.d(TAG, "Normal reversal field_39..."+ ISO8583.ISO8583_FIELD.field_39);
                    ISO8583.ISO8583_FIELD.field_39=Utility.byte2HexStr((ISO8583.ISO8583_FIELD.field_39).getBytes());

                    // Filed_60
                    ISO8583.ISO8583_FIELD.field_60="0012";
                    ISO8583.ISO8583_FIELD.field_60=ISO8583.ISO8583_FIELD.field_60 + (Utility.byte2HexStr(ISO8583.ISO8583_FIELD.field_04.getBytes()));
                    Log.d(TAG, "Filed_60  : "+ISO8583.ISO8583_FIELD.field_60);


                    String F62=dbHandler.gettransactiondata().get(dbHandler.gettransactiondata().size()-1).getField_11();
                    ISO8583.ISO8583_FIELD.field_62="0006";
                    ISO8583.ISO8583_FIELD.field_62=ISO8583.ISO8583_FIELD.field_62 + Utility.byte2HexStr((F62).getBytes());
                    Log.d(TAG, "Filed_62 : "+ISO8583.ISO8583_FIELD.field_62);

                }
                else if (Objects.equals(Txn_type, "BALANCE_INQUIRY"))
                {
                    //Transactiondata.ISO8583msg_OPTIONAL_DATA_FIELD_VARS.B_amount_1="";
                }
                else
                {
                    //Transactiondata.ISO8583msg_OPTIONAL_DATA_FIELD_VARS.B_amount_1="B"+Utility.byte2HexStr(iemv.getCardData(Integer.toHexString(tagList[13]).toUpperCase()));

                }
                SharedPreferences.Editor editor1=sharedPreferences.edit();
                editor1.putString("merchantid",ISO8583msg.Mer_id);
                editor1.putString("currency",ISO8583msg.currency);
                editor1.putString("terminalid",ISO8583msg.Ter_id);
                editor1.commit();
                Log.d(TAG, "currrrrrrrrrrrrency" +sharedPreferences.getString("currency","230"));


                /*if(isonline)
                {
                    mIso8583.setField(ISO8583u.F_PINData_52, Utility.byte2HexStr(savedPinblock));
                    isonline=false;
                }*/

                Log.d(TAG, "start online request");
                Intent i = new Intent(context, loadingactivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
               /* Activity a= new Activity();

                ActivityCollector.addActivity();*/
                // checkCardActivity.progress();
                //connect.toast();

                onlineRequest.run();
                // connect.back();
                // ActivityCollector.finishloading();

                ActivityCollector acc=new ActivityCollector();
                ActivityCollector();


                //Offline

                //printTest(0);
                Log.d(TAG, "online request finished");
                String F="",Q="",responcecode="";
                //  responcecode=Transactiondata.HEADER_VARSresponse.rresponse_Code;
                responce_code=responcecode;
                //  F=Transactiondata.Subfieldresponse.F;
                // Q=Transactiondata.Subfieldresponse.Q;
                Log.d(TAG, "F: "+F);
                Log.d(TAG, "Q: "+Q);
                String issuerData = "";
                if(Q!=null && Q.length()>3)
                {
                    issuerData= Q.substring(4,Q.length());
                }
                String  tag95final="";
                tag95final=Utility.byte2HexStr(iemv.getCardData(Integer.toHexString(tagList[10]).toUpperCase())) ;
                Log.d(TAG, "tag95final:" + tag95final);
                Log.d(TAG, "issuerData:" + issuerData);


                if(responcecode!=null)
                {
                    Log.d(TAG, "responcecode: "+responcecode);}

                // import the online result
                Bundle onlineResult = new Bundle();
                onlineResult.putBoolean(ConstIPBOC.inputOnlineResult.onlineResult.KEY_isOnline_boolean, true);
                if(Objects.equals(responcecode, "001")) {
                    onlineResult.putString(ConstIPBOC.inputOnlineResult.onlineResult.KEY_respCode_String, "00");
                    Log.d(TAG, "online request Approved");

                }
                if(F.length()>0)
                {
                    onlineResult.putString(ConstIPBOC.inputOnlineResult.onlineResult.KEY_authCode_String, F);
                    Log.d(TAG, "online request approval code");
                }


                if (Q.length()>0)
                {
                    onlineResult.putString(ConstIPBOC.inputOnlineResult.onlineResult.KEY_field55_String, "91"+"0A"+  issuerData);
                    Log.d(TAG, "online request issuer data");
                }
                if (isoResponse.unpackValidField[ISO8583u.F_ResponseCode_39]) {
                    onlineResult.putString(ConstIPBOC.inputOnlineResult.onlineResult.KEY_field55_String, isoResponse.getUnpack(ISO8583u.F_ResponseCode_39));
                } else {
                    onlineResult.putString(ConstIPBOC.inputOnlineResult.onlineResult.KEY_respCode_String, "00");
                }

                if (isoResponse.unpackValidField[ISO8583u.F_AuthorizationIdentificationResponseCode_38]) {
                    //
                    onlineResult.putString(ConstIPBOC.inputOnlineResult.onlineResult.KEY_authCode_String, isoResponse.getUnpack(ISO8583u.F_AuthorizationIdentificationResponseCode_38));
                } else {
                    onlineResult.putString(ConstIPBOC.inputOnlineResult.onlineResult.KEY_authCode_String, "123456");
                }

                // onlineResult.putString( ConstIPBOC.inputOnlineResult.onlineResult.KEY_field55_String, "910A1A1B1C1D1E1F2A2B30307211860F04DA9F790A0000000100001A1B1C1D");
               /* if (isoResponse.unpackValidField[55]) {
                    onlineResult.putString(ConstIPBOC.inputOnlineResult.onlineResult.KEY_field55_String, isoResponse.getUnpack(55));

                } else {
                    onlineResult.putString(ConstIPBOC.inputOnlineResult.onlineResult.KEY_field55_String, "5F3401019F3303E0F9C8950500000000009F1A0202319A039707039F3704F965E43082027C009F3602041C9F260805142531F709C8669C01009F02060000000000125F2A0202309F101307010103A02000010A01000000000063213EC29F2701809F1E0831323334353637389F0306000000000000");
                }*/
                String  tag95final2="";
                tag95final2=Utility.byte2HexStr(iemv.getCardData(Integer.toHexString(tagList[10]).toUpperCase())) ;
                Log.d(TAG, "tag95final222:" + tag95final2);

//                onlineResult.putBoolean("getPBOCData", true);
//                onlineResult.putInt("importAppSelectResult", 1);
//                onlineResult.putInt("IsPinInput", 1);
//                onlineResult.putString("importPIN", "123456");
//                onlineResult.putInt("importAmount", 101);
//                onlineResult.putBoolean("cancelCardConfirmResult", false);


                iemv.inputOnlineResult(onlineResult, new OnlineResultHandler.Stub() {
                    @Override
                    public void onProccessResult(int result, Bundle data) throws RemoteException {
                        Log.i(TAG, "onProccessResult callback:");
                        Log.d(TAG, "Try to print");
                        showUI("Printing Receipt...");
                        if( null != transPrinter ){
                            // printReceipt(1);
                        }
                        String str = "RESULT:" + result +
                                "\nTC_DATA:" + data.getString(ConstOnlineResultHandler.onProccessResult.data.KEY_TC_DATA_String, "not defined") +
                                "\nSCRIPT_DATA:" + data.getString(ConstOnlineResultHandler.onProccessResult.data.KEY_SCRIPT_DATA_String, "not defined") +
                                "\nREVERSAL_DATA:" + data.getString(ConstOnlineResultHandler.onProccessResult.data.KEY_REVERSAL_DATA_String, "not defined");
                        //showUI(str);

                        String resultTLV = data.getString("TC_TLV");
                        Log.d(TAG, "CardData :" + "onResult TLV: " + resultTLV);
                        Log.d(TAG, "CardData :" + "onResult 98: " + Arrays.toString(iemv.getCardData("98")));

                        String[] tlv_tag_list = {"98"};

                        Log.d(TAG, "CardData :" + "onResult tlvlist: " + iemv.getAppTLVList(tlv_tag_list));
                        Log.d(TAG, "CardData :" + "onResult emvdata: " + iemv.getEMVData("98"));

                        switch (result) {
                            case ConstOnlineResultHandler.onProccessResult.result.TC:
                                //showUI("TC");
                                break;
                            case ConstOnlineResultHandler.onProccessResult.result.Online_AAC:
                                //showUI("Online_AAC");
                                break;
                            default:
                                //showUI("error, code:" + result);
                                break;
                        }
                        /*Log.d(TAG, "Try to print");
                        if( null != transPrinter ){
                            printReceipt(1);
                       }*/
                        return;
                    }
                });
            }

            @Override
            public void onTransactionResult(int result, Bundle data) throws RemoteException {
                Log.d(TAG, "onTransactionResult");
                String msg = data.getString("ERROR");
                Log.d(TAG, "onTransactionResult error"+msg);

                //showUI("onTransactionResult result = " + result + ",msg = " + msg);

                if(!isonrequest)
                {
                    //  ActivityCollector.finishAllTransActivity();
                    Message mesg = new Message();
                    mesg.getData().putString("msg", context.getResources().getString(R.string.cardfailed));
                    dialog.sendMessage(mesg);
                    isonrequest=false;
                    magenabled=true;
                    Log.d(TAG, "isonnnnnnnnnnnnn");

                }
                isonrequest=false;
                transStatus = false;

                ActivityCollector acc=new ActivityCollector();
                acc.finishloading();
                // ActivityCollector.finishloading();

                //Log.d(TAG, "Chip or CLS Card Reading issue");
                //Message mesg = new Message();
                //mesg.getData().putString("msg", "");
                //dialog3.sendMessage(mesg);

                switch (result) {

                    case ConstPBOCHandler.onTransactionResult.result.EMV_CARD_BIN_CHECK_FAIL:
                        // read card fail
                        //showUI("read card fail");
                        Log.d(TAG, "readcardfail");

                        return;
                    case ConstPBOCHandler.onTransactionResult.result.EMV_MULTI_CARD_ERROR:
                        // multi-cards found
                        //showUI(data.getString(ConstPBOCHandler.onTransactionResult.data.KEY_ERROR_String));
                        return;
                }
            }
        };
    }


    public void SettlmentRequest1() throws RemoteException
    {
        Log.d(TAG, "settlement txn is Selected:");
        dbHandler=new DBHandler(context);
        sharedPreferences=context.getSharedPreferences("Shared_data", Context.MODE_PRIVATE);

        //ISO8583 iso85831;
        //ISO8583 mIso8583;

        tr=new Transactiondata(sharedPreferences,context);
        sp=new ISO8583msg(context);
        sp.loadData();//terminal id
        sp.loadData1();//merchant id
        sp.loadCurrencyData();//currency type
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("merchantid",ISO8583msg.Mer_id);
        editor.putString("currency",ISO8583msg.currency);
        editor.putString("terminalid",ISO8583msg.Ter_id);
        editor.putString("Txn_Menu_Type","SETTLEMENT");
        editor.commit();

        Log.d(TAG, "currrrrrrrrrrrrency" +sharedPreferences.getString("currency","230"));

        Txn_type=sharedPreferences.getString("txn_type", "");
        Txn_Menu_Type=sharedPreferences.getString("Txn_Menu_Type", "");
        Log.d(TAG, "Txn_type.Manual.........:" + Txn_type);
        Log.d(TAG, "Txn_menu_type.settelment.........:" + Txn_Menu_Type);

        //Field_11
        STAN=Integer.parseInt(sharedPreferences.getString("STAN","1"));
        Log.d(TAG, "STAN "+fillgapsequence1(String.valueOf(STAN),6));
        STAN=STAN+1;
        //SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("STAN",String.valueOf(STAN));
        editor.commit();

        ISO8583.ISO8583_FIELD.field_11=fillgapsequence1(String.valueOf(STAN),6);
        Log.e(TAG, "field_11...: " + ISO8583.ISO8583_FIELD.field_11);
        // field_41 tid
        ISO8583.ISO8583_FIELD.field_41= sharedPreferences.getString("terminalid","termid");
        Log.d(TAG, "field_41  : " + ISO8583.ISO8583_FIELD.field_41);

        ISO8583.ISO8583_FIELD.field_41=Utility.byte2HexStr((ISO8583.ISO8583_FIELD.field_41).getBytes());
        Log.d(TAG, "field_41 hex : " + ISO8583.ISO8583_FIELD.field_41);

        // field_42 tid
        ISO8583.ISO8583_FIELD.field_42= sharedPreferences.getString("merchantid","merid");
        Log.d(TAG, "field_42  : " + ISO8583.ISO8583_FIELD.field_42);

        ISO8583.ISO8583_FIELD.field_42=Utility.byte2HexStr((ISO8583.ISO8583_FIELD.field_42).getBytes());
        Log.d(TAG, "field_42 hex : " + ISO8583.ISO8583_FIELD.field_42);

// Filed_60
        ISO8583.ISO8583_FIELD.field_60="0006";
        Log.d(TAG, "Filed_60  : "+ISO8583.ISO8583_FIELD.field_60);

        ISO8583.ISO8583_FIELD.field_60=ISO8583.ISO8583_FIELD.field_60 + (Utility.byte2HexStr(ISO8583.ISO8583_FIELD.field_11.getBytes()));
        //ISO8583.ISO8583_FIELD.field_62=Utility.byte2HexStr(Invoce_number.getBytes()) + hexToASCII(ISO8583.ISO8583_FIELD.field_11);
        Log.d(TAG, "Filed_60 : "+ISO8583.ISO8583_FIELD.field_60);

// Filed_62
        ISO8583.ISO8583_FIELD.field_62="00225665722E30312E313330312024243030313230313033";
        Log.d(TAG, "Filed_62 : "+ISO8583.ISO8583_FIELD.field_62);

        // Filed_63
        // field 63=total number of(Purchase ,Cashadvance,prauth_comp) +amount(12 digit)  +   total number of(reversal ,refund) +amount(12 digit)
        if (dbHandler.gettransactiondata().size() > 0)
        {
            Log.d(TAG, "......");
            String Total_sale= "",Total_Void="",Total_sale_cont= "",Total_Void_cont="";
            Total_sale=String.valueOf(common.Pamount+common.Camount+common.prcmpamount);
            Total_Void=String.valueOf(common.Rvamount+common.Rfamount+common.Prthamount);
            Total_sale_cont=String.valueOf(common.Sale_count);
            Total_Void_cont=String.valueOf(common.Void_count);

            //sale amount length

            if(Total_sale_cont.length()==1)
            {
                Total_sale_cont="00"+Total_sale_cont;

            }   else  if(Total_sale_cont.length()==2)
            {
                Total_sale_cont="0"+Total_sale_cont;

            } if(Total_sale_cont.length()==3)
        {

        }
            Log.d(TAG, "Total_sale_cont........... :"+Total_sale_cont);
            Total_sale_cont=Utility.byte2HexStr(Total_sale_cont.getBytes());
            Log.d(TAG, "Total_sale_cont.HEX.......... :"+Total_sale_cont);

//void amount length
            if(Total_Void_cont.length()==1)
            {
                Total_Void_cont="00"+Total_Void_cont;

            }   else  if(Total_Void_cont.length()==2)
            {
                Total_Void_cont="0"+Total_Void_cont;

            } if(Total_Void_cont.length()==3)
        {

        }
            Log.d(TAG, "Total_Void_cont........... :"+Total_Void_cont);
            Total_Void_cont=Utility.byte2HexStr(Total_Void_cont.getBytes());
            Log.d(TAG, "Total_Void_cont.HEX.......... :"+Total_Void_cont);

            Log.d(TAG, "Total_SAL........... :"+Total_sale);
            Total_sale = "000000000000" + Total_sale.replace(".", "0");
            TransactionParams.getInstance().setTransactionAmount(Total_sale.substring(Total_sale.length() - 12));
            Total_sale=TransactionParams.getInstance().getTransactionAmount();
            Log.d(TAG, "Total_SALLL.......... :"+Total_sale);

            Log.d(TAG, "Total_Void........... :"+Total_Void);
            Total_Void = "000000000000" + Total_Void.replace(".", "0");
            TransactionParams.getInstance().setTransactionAmount(Total_Void.substring(Total_Void.length() - 12));
            Total_Void=TransactionParams.getInstance().getTransactionAmount();
            Log.d(TAG, "Total_Void.......... :"+Total_Void);

            ISO8583.ISO8583_FIELD.field_63=Total_sale_cont+Total_sale+Total_Void_cont+Total_Void;
            Log.d(TAG, "field_63.......... :"+ISO8583.ISO8583_FIELD.field_63);

        } else
        {
            Log.d(TAG, "DO TRANSACTION!, NO TXN RECORDED");

        }


        //Log.d(TAG, "start online request  " +packet);
        Intent i = new Intent(context, loadingactivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
               /* Activity a= new Activity();

                ActivityCollector.addActivity();*/
        // checkCardActivity.progress();
        //connect.toast();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new Thread(onlineRequest).start();
            }
        }, 2000);

        Log.d(TAG, "online request finished............");


        // import the online result
        Bundle onlineResult = new Bundle();
        onlineResult.putBoolean(ConstIPBOC.inputOnlineResult.onlineResult.KEY_isOnline_boolean, true);
        if (Objects.equals(ISO8583.ISO8583_FIELD.r_field_39, "00"))
        {
            onlineResult.putString(ConstIPBOC.inputOnlineResult.onlineResult.KEY_respCode_String, "00");
            Log.d(TAG, "online request Approved");

        }
        else {

        }

    }

    public static String lrc2hex (String str) {
        byte[] bytes = str.getBytes ();
        int lrc = 0;
        for (int i = 0; i < str.length (); i++) {
            lrc ^= (bytes[i] & 0xFF);
        }
        return String.format ("%02X ", lrc);
    }
    //MAG.............


    public void initializeMAG() {

        Log.d(TAG, "@SSC Amex initializeMAG Method call...................................");
        Transactiondata.GLobalFields.Field41 =sharedPreferences.getString("terminalid","termid");
        Log.d(TAG, "Type.........................."+sharedPreferences.getString("Txn_Menu_Type", "0"));


        dbHandler=new DBHandler(context);

        sharedPreferences = context.getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
        Txn_type=sharedPreferences.getString("txn_type", "");
        Txn_Menu_Type=sharedPreferences.getString("Txn_Menu_Type", "");

        Log.d(TAG, "Txn_type.mag.........:" + Txn_type);
        Log.d(TAG, "Txn_menu_type.magstrip.........:" + Txn_Menu_Type);


        tr = new Transactiondata(sharedPreferences, context);
        sp = new ISO8583msg(context);
        sp.loadData();//terminal id
        sp.loadData1();//merchant id
        sp.loadCurrencyData();//currency type
        if (Objects.equals(Txn_type, "REVERSAL"))
        {
            dbHandler.gettransactiondata().get(dbHandler.gettransactiondata().size() - 1);
            ISO8583.ISO8583_FIELD.field_04 = dbHandler.gettransactiondata().get(dbHandler.gettransactiondata().size()-1).getField_04();
            Log.d(TAG, "field_04..."+ISO8583.ISO8583_FIELD.field_04);
            ISO8583.ISO8583_FIELD.field_38 = dbHandler.gettransactiondata().get(dbHandler.gettransactiondata().size()-1).getField_38();
            Log.d(TAG, "field_38.."+  ISO8583.ISO8583_FIELD.field_38);

        } else if (Objects.equals(Txn_type, "BALANCE_INQUIRY"))
        {
            // Transactiondata.ISO8583msg_OPTIONAL_DATA_FIELD_VARS.B_amount_1="";
        }
        else {
            // Transactiondata.ISO8583msg_OPTIONAL_DATA_FIELD_VARS.B_amount_1 = "B" + ISO8583.B_Amount1;
            //Transactiondata.ISO8583msg_OPTIONAL_DATA_FIELD_VARS.B_amount_1="B"+TransactionParams.getInstance().getTransactionAmount();

            //Log.d(TAG, "B_amount_1  " + Transactiondata.ISO8583msg_OPTIONAL_DATA_FIELD_VARS.B_amount_1);

        }

        //Track2
        //Transactiondata.ISO8583msg_OPTIONAL_DATA_FIELD_VARS.q_track2_Customer = "q" + ";" + track22 + "?";
        //Log.d(TAG, "q_track2_Customer.magstrip.........   " + Transactiondata.ISO8583msg_OPTIONAL_DATA_FIELD_VARS.q_track2_Customer);

        //Transactiondata.ISO8583msg_FID_6_DATA.B_CVD = InputCvvActivity.CVV;
        //Log.d(TAG, "CVV........ " + Transactiondata.ISO8583msg_FID_6_DATA.B_CVD);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("merchantid", ISO8583msg.Mer_id);
        editor.putString("currency", ISO8583msg.currency);
        editor.putString("terminalid", ISO8583msg.Ter_id);
        editor.putString("Txn_Menu_Type","Magstrip");
        //editor.putString("Type",sharedPreferences.getString("type", "0"));
        editor.commit();
        Log.d(TAG, "currrrrrrrrrrrrency " + sharedPreferences.getString("currency", "230"));
        Log.d(TAG, "Txn_Menu_Type............  "+sharedPreferences.getString("Txn_Menu_Type", "0"));

        Log.d(TAG, "CVV........ " + InputCvvActivity.CVV);

      /* if(InputCvvActivity.CVV!="")
       {
           Log.d(TAG, "Cvv length........ "+InputCvvActivity.CVV.length());
           if(InputCvvActivity.CVV.length()==3)
           {
               Transactiondata.ISO8583msg_FID_6_DATA.B_CVD = "B" + InputCvvActivity.CVV +" ";
               Log.d(TAG, "B_CVD........ " + Transactiondata.ISO8583msg_FID_6_DATA.B_CVD);

               Transactiondata.ISO8583msg_FID_6_DATA.H_CVDIindicator = "H" + "1P";
               Log.d(TAG, "H_CVD Iindicator........ " + Transactiondata.ISO8583msg_FID_6_DATA.H_CVDIindicator);
           }else
           {
               Transactiondata.ISO8583msg_FID_6_DATA.B_CVD = "B" + InputCvvActivity.CVV;
               Log.d(TAG, "B_CVD........ " + Transactiondata.ISO8583msg_FID_6_DATA.B_CVD);

               Transactiondata.ISO8583msg_FID_6_DATA.H_CVDIindicator = "H" + "1P";
               Log.d(TAG, "H_CVD Iindicator........ " + Transactiondata.ISO8583msg_FID_6_DATA.H_CVDIindicator);
           }

       }else{
           Log.d(TAG, "CVV Empity........ ");
           Transactiondata.ISO8583msg_FID_6_DATA.B_CVD = "";
           Transactiondata.ISO8583msg_FID_6_DATA.H_CVDIindicator = "";
       }*/

        Log.d(TAG, "assign()........... :");
        tr.assign();
        transend =tr.send();
        Log.d(TAG, "send()()........... :");

        String tmp=new String(transend);
        //String tmp1=new String(transend);
        //Log.d(TAG, "tmp transend........... :"+tmp);
        //tmp=Integer.toString(tmp.length(), 16)+tmp;
        int len=tmp.length();

        Log.d(TAG, "length........... :"+len);
        //String leng =Integer.toHexString(len);
        String leng =Integer.toHexString(len/2);

        if(leng.length()==2)
        {
            leng="00"+leng;

        }   else  if(leng.length()==3)
        {
            leng="0"+leng;

        } if(leng.length()==4)
        {

        }
        Log.d(TAG, "lenhex: " +leng);
        //leng=len/2;

        byte[] lengthpackate=Utility.hexStr2Byte(leng);
        ByteBuffer byt=ByteBuffer.allocate(transend.length+lengthpackate.length);
        // ByteBuffer byt=ByteBuffer.allocate(transend.length);
        byt.put(lengthpackate);
        byt.put(transend);
        lastdata=byt.array();
        tmp=new String(lastdata);
        Log.d(TAG, "outputmessage: " +tmp);
        Log.d(TAG, "PACKED MESSAGE:  " +new String(lastdata));
        Log.e(TAG, "PACKED MESSAGE Hex: "+Utility.byte2HexStr(lastdata));

        Log.d(TAG, "start Mag online request...............");
        Intent i = new Intent(context, loadingactivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);


        Log.d(TAG, "onCardSwiped ... CALL onlineRequest.run()");

        new Thread(onlineRequest).start();
        // connect.back();
        // ActivityCollector.finishloading();

        //ActivityCollector acc = new ActivityCollector();
        // acc.finishloading();


        //Offline

        //printTest(0);
        Log.d(TAG, "online request finished");
        String F, Q, responcecode;
        // responcecode = Transactiondata.HEADER_VARSresponse.rresponse_Code;
        // responce_code = responcecode;
        // F = Transactiondata.Subfieldresponse.F;
        // Q = Transactiondata.Subfieldresponse.Q;

        String issuerData = "";
      /* if (Q != null && Q.length() > 3) {
           issuerData = Q.substring(4, Q.length());
       }*/

      /* if (responcecode != null) {
           Log.d(TAG, "responcecode: " + responcecode);
       }*/

        // import the online result
        Bundle onlineResult = new Bundle();
        onlineResult.putBoolean(ConstIPBOC.inputOnlineResult.onlineResult.KEY_isOnline_boolean, true);
     /*  if (Objects.equals(responcecode, "001")) {
           onlineResult.putString(ConstIPBOC.inputOnlineResult.onlineResult.KEY_respCode_String, "00");
           Log.d(TAG, "online request Approved");

       }*/
      /* if (F.length() > 0) {
           onlineResult.putString(ConstIPBOC.inputOnlineResult.onlineResult.KEY_authCode_String, F);
           Log.d(TAG, "online request approval code");
       }*/


    }

    public void manualcardrequest() throws RemoteException
    {
        Log.d(TAG, "Manual Card txn is Selected:");

        dbHandler=new DBHandler(context);
        sharedPreferences=context.getSharedPreferences("Shared_data", Context.MODE_PRIVATE);

        Txn_type=sharedPreferences.getString("txn_type", "");
        Txn_Menu_Type=sharedPreferences.getString("Txn_Menu_Type", "");

        tr=new Transactiondata(sharedPreferences,context);
        sp=new ISO8583msg(context);
        sp.loadData();//terminal id
        sp.loadData1();//merchant id
        sp.loadCurrencyData();//currency type
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("merchantid",ISO8583msg.Mer_id);
        editor.putString("currency",ISO8583msg.currency);
        editor.putString("terminalid",ISO8583msg.Ter_id);
        editor.putString("Txn_Menu_Type","Manualcard");

        editor.commit();


        Log.d(TAG, "currrrrrrrrrrrrency" +sharedPreferences.getString("currency","230"));
        Log.d(TAG, "Txn_type.Manual.........:" + Txn_type);
        Log.d(TAG, "Txn_menu_type.Manual.........:" + Txn_Menu_Type);
        int PANlength=0;

        savedPan=ManualTestActivity.cardnumber;
        // Transactiondata.GLobalFields.Field22="016";

        //TransactionParams.getInstance().setPan(savedPan);
        Log.d(TAG, "savedPan.........:" +savedPan);
        String Cardtype="";
        Cardtype = savedPan.substring(0, 1);
        Log.d(TAG, "1st digit of pan Cardtype ........" + Cardtype);
        if(Cardtype.equals("4"))
        {
            aidLabel1="VISA CARD";
            Log.e(TAG, "CardType is " + aidLabel1);
        }
        else if(Cardtype.equals("2")|| Cardtype.equals("5"))
        {
            aidLabel1="MASTER CARD";
            Log.e(TAG, "CardType is " + aidLabel1);
        }
        else if(Cardtype.equals("3"))
        {
            aidLabel1= "AMEX CARD";
            Log.e(TAG, "CardType is " + aidLabel1);
        }else
        {
            aidLabel1= "";
        }

        String countrycode="";
        // countrycode=countrycode+ Utility.byte2HexStr(iemv.getCardData(Integer.toHexString(tagList[16]).toUpperCase())).charAt(1)+Utility.byte2HexStr(iemv.getCardData(Integer.toHexString(tagList[16]).toUpperCase())).charAt(2)+Utility.byte2HexStr(iemv.getCardData(Integer.toHexString(tagList[16]).toUpperCase())).charAt(3);
        Log.d(TAG, "countrycode:" + countrycode);
        //track2 is M and pannum and=and expdate and 000?
        //   epos entry 011
        // pannum is fr 13-19
        Log.d(TAG, "Txn_type is.......   "+Supervisor_menu_activity.manual_txn_type1);
        Log.d(TAG, "Txn_Menu_Type is  "+sharedPreferences.getString("Txn_Menu_Type", ""));

        if (Objects.equals(Supervisor_menu_activity.manual_txn_type1, "REVERSAL"))
        {
            Log.d(TAG, "Manual Card Reversal is Selected:");

            // tr.unpack(dbHandler.gettransactiondata().get(dbHandler.gettransactiondata().size() - 1).getRecievedtran());

            // Transactiondata.ISO8583msg_OPTIONAL_DATA_FIELD_VARS.B_amount_1 = "B" + Transactiondata.Subfieldresponse.B;
            // Log.d(TAG, "B_amount_1.........reversalamount manualcard entery  " + "B" + Transactiondata.Subfieldresponse.B);

            //Transactiondata.ISO8583msg_OPTIONAL_DATA_FIELD_VARS.F_approval_Code = "F" + Transactiondata.Subfieldresponse.F;
            // Log.d(TAG, "Manual reversalAppcode.."+"F" + Transactiondata.Subfieldresponse.F);
            // Transactiondata.ISO8583msg_HEADER_VARS.transaction_Code=Transactiondata.HEADER_VARSresponse.rtransaction_Code;
            // Log.d(TAG, "Manual reversaltransaction_Code..."+Transactiondata.ISO8583msg_HEADER_VARS.transaction_Code);

        }else if(Supervisor_menu_activity.manual_txn_type1.equals("BALANCE_INQUIRY") && Txn_type.equals("BALANCE_INQUIRY"))
        {
            //Txn_Menu_Type.equals("Manualcard")
            // Transactiondata.ISO8583msg_OPTIONAL_DATA_FIELD_VARS.B_amount_1="";
            Transactiondata.GLobalFields.Field22="016";
            Transactiondata.GLobalFields.Field24="100";
        }
        else if(Supervisor_menu_activity.manual_txn_type1.equals("PURCHASE") && Txn_type.equals("PURCHASE")){
            Log.d(TAG, "Manual Card purchase is Selected:");
            Log.d(TAG, "bitmap in manual card"+Transactiondata.GLobalFields.PrimaryBitmap);
            Transactiondata.GLobalFields.MTI="0200";
            //Transactiondata.GLobalFields.Field14= ManualTest_Exdate.expiredate+"01";
            Transactiondata.GLobalFields.Field22="016";
            Transactiondata.GLobalFields.Field24="200";
        }



        //Transactiondata.GLobalFields.Field35 = "q" + "M" + ManualTestActivity.cardnumber+"=" +ManualTest_Exdate.expiredate+ "000?";

        /*Log.d(TAG, "assign()........... :");
        tr.assign();
        transend =tr.send();
        Log.d(TAG, "send()()........... :");


        String tmp=new String(transend);
        String tmp1=new String(transend);
        //Log.d(TAG, "tmp transend........... :"+tmp);
        //tmp=Integer.toString(tmp.length(), 16)+tmp;
        int len=tmp.length();

        Log.d(TAG, "length........... :"+len);
        //String leng =Integer.toHexString(len);
        String leng =Integer.toHexString(len/2);

        if(leng.length()==2)
        {
            leng="00"+leng;

        }   else  if(leng.length()==3)
        {
            leng="0"+leng;

        } if(leng.length()==4)
    {

    }
        Log.d(TAG, "lenhex: " +leng);
        //leng=len/2;

        byte[] lengthpackate=Utility.hexStr2Byte(leng);
        ByteBuffer byt=ByteBuffer.allocate(transend.length+lengthpackate.length);
        // ByteBuffer byt=ByteBuffer.allocate(transend.length);
        byt.put(lengthpackate);
        byt.put(transend);
        lastdata=byt.array();
        tmp=new String(lastdata);
        Log.d(TAG, "outputmessage: " +tmp);
        Log.d(TAG, "PACKED MESSAGE:  " +new String(lastdata));
        Log.e(TAG, "PACKED MESSAGE Hex: "+Utility.byte2HexStr(lastdata));

        Log.d(TAG, "start online request");
        Intent i = new Intent(context, loadingactivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
           */    /* Activity a= new Activity();

                ActivityCollector.addActivity();*/
        // checkCardActivity.progress();
        //connect.toast();
        Intent i = new Intent(context, loadingactivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new Thread(onlineRequest).start();
            }
        }, 2000);

        // act.finish();
        // onlineRequest.run();
        // connect.back();
        // ActivityCollector.finishloading();

        /*ActivityCollector acc=new ActivityCollector();
        acc.finishloading();*/


        //Offline

        //printTest(0);
        Log.d(TAG, "online request finishedddddddddddddddd");

        String F,Q,responcecode;
        /*responcecode=Transactiondata.HEADER_VARSresponse.rresponse_Code;
        responce_code=responcecode;
        F=Transactiondata.Subfieldresponse.F;
        Q=Transactiondata.Subfieldresponse.Q;
        Log.d(TAG, "F: "+F);
        Log.d(TAG, "Q: "+Q);
        String issuerData = "";
        if(Q!=null&&Q.length()>3){
            issuerData= Q.substring(4,Q.length());
        }

        if(responcecode!=null){
            Log.d(TAG, "responcecode: "+responcecode);
        }
*/
        // import the online result
        Bundle onlineResult = new Bundle();
        onlineResult.putBoolean(ConstIPBOC.inputOnlineResult.onlineResult.KEY_isOnline_boolean, true);
       /*if (Objects.equals(responcecode, "001")) {
           onlineResult.putString(ConstIPBOC.inputOnlineResult.onlineResult.KEY_respCode_String, "00");
           Log.d(TAG, "online request Approved");

       }
       if (F.length() > 0) {
           onlineResult.putString(ConstIPBOC.inputOnlineResult.onlineResult.KEY_authCode_String, F);
           Log.d(TAG, "online request approval code");
       }*/

    }

    public void doSetKeys() {
        // Load Main key
        // 758F0CD0C866348099109BAF9EADFA6E
        boolean bRet;
        try {
            bRet = ipinpad.loadMainKey(masterKeyID, Utility.hexStr2Byte(masterKey), null);
            Log.d("PINPAD", "loadMainKey:" + bRet);
            //showUI("loadMainKey:" + bRet);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        String DukptSN = "01020304050607080900";
        String dukptKey = "343434343434343434343434343434343434343434343434";

        // test Sudarson's dukpt issue
//        String DukptSN = "FFFF9876543210E10004";
//        String dukptKey = "C1D0F8FB4958670DBA40AB1F3752EF0D";

        // Load work key
        // 89B07B35A1B3F47E89B07B35A1B3F488
        try {
            bRet = ipinpad.loadWorkKey(PinpadKeyType.PINKEY, masterKeyID, workKeyId, Utility.hexStr2Byte(pinKey_WorkKey), null);
            //Log.d("PINPAD", "loadWorkKey:" + bRet);
            //showUI("loadWorkKey:" + bRet);

            int keyId = 1;
            String ksn = "FFFF9876543210E10001";
            String key = "C1D0F8FB4958670DBA40AB1F3752EF0D";

//            bRet = ipinpad.loadDukptKey(1, Utility.hexStr2Byte(DukptSN), Utility.hexStr2Byte(dukptKey), null);
            Bundle extend = new Bundle();
            extend.putBoolean("isPlainKey", true);
            extend.putBoolean("KSNAutoIncrease", true);
            //bRet = iDukpt.loadDukptKey(1, Utility.hexStr2Byte(ksn), Utility.hexStr2Byte(key), null, extend);
            // Log.d("PINPAD", "loadDukptKey:" + bRet);

            //showUI("loadDukptKey:" + bRet);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    /**
     * \brief show the pinpad
     * <p>
     * \code{.java}
     * \endcode
     */

    public void doPinPadMag() {

        Log.d(TAG, "here for pin 4");

        Bundle extend = new Bundle();
        extend.putBoolean("isPlainKey", true);
        extend.putBoolean("KSNAutoIncrease", true);

        Log.d(TAG, "doSetKeys: charset" + Charset.defaultCharset());

        boolean bRet = false;
        boolean isOnlinePin=true;
        int retryTimes=3;

        /////////////////////////////////////////For Master/Session
        // masterKey ="";
        String masterKey22 = "F8BC8610CDF183A8";
        String component1 = "F8BC8610CDF183A883E6C46810688CA4";
        String component2 = "5E1AA4C49E70AD5EA4DF2F9443C72C1C";
        String component3 = "00000000000000000000000000000000";
        pinKey_WorkKey="373E47A1A09BF2CF08387CF2A0D20D67";
        byte[] cc1=Utility.hexStr2Byte(component1);
        byte[] cc2=Utility.hexStr2Byte(component2);
        byte[] cc3=Utility.hexStr2Byte(component3);
        byte[] result = new byte[cc1.length];
        int i = 0;

        for (byte b1: cc1) {
            byte b2 = cc2[i];
            byte b3 = cc3[i];
            result[i] = (byte) (b1 ^ b2 ^ b3);//final masterkey
            i++;
        }
        Log.d("PINPAD", "isKeyExist:" + Utility.byte2HexStr(result));
        try {
            bRet = ipinpad.isKeyExist(0, 20);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Log.d("PINPAD", "isKeyExist:" + bRet);

        //try {
        //    bRet = ipinpad.clearKey(20, 0);
        // }
        //catch (RemoteException e) {
        //  e.printStackTrace();
        //}


        /* comment for diplication key load
        try {
            bRet = ipinpad.isKeyExist(0, 20);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Log.d("PINPAD", "isKeyExist:" + bRet);
        try {
            bRet = ipinpad.loadMainKey(20, result, null);
            //    bRet=ipinpad.loadEncryptMainKeyEX(20,Utility.hexStr2Byte(masterKey),0x01,null,null);
            // bRet = ipinpad.loadMainKeyWithAlgorithmType(20,Utility.hexStr2Byte(masterKey22),0x86,null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }*/

        // Log.d("PINPAD", "loadMainKey:" + bRet);

       /* try {
            bRet = ipinpad.loadWorkKey(PinpadKeyType.PINKEY, 20, 1, Utility.hexStr2Byte(pinKey_WorkKey), null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }*/

        //Log.d("PINPAD", "loadWorkKey:" + bRet);

        /////////////////////////////////////////End Master/Session

        Bundle param = new Bundle();
        Bundle globeParam = new Bundle();
        String panBlock = savedPan;
        Log.d(TAG, "savedPaMag:.............." +panBlock);
        byte[] pinLimit = {0, 4, 5, 6}; // 0 means bypass pin input
        param.putString("promptString", "ONLINE PINPAD / የሚስጥር ቁጥር");
        param.putString("promptsFont", "/system/fonts/DroidSans-Bold.ttf");
//        param.putByteArray("displayKeyValue", nebyte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        param.putByteArray(ConstIPinpad.startPinInput.param.KEY_pinLimit_ByteArray, pinLimit);
        param.putInt(ConstIPinpad.startPinInput.param.KEY_timeout_int, 60);
        param.putBoolean(ConstIPinpad.startPinInput.param.KEY_isOnline_boolean, isOnlinePin);
        param.putString(ConstIPinpad.startPinInput.param.KEY_pan_String, panBlock);
        //param.putInt(ConstIPinpad.startPinInput.param.KEY_desType_int, ConstIPinpad.startPinInput.param.Value_desType_DUKPT_3DES);
        param.putInt(ConstIPinpad.startPinInput.param.KEY_desType_int, ConstIPinpad.startPinInput.param.Value_desType_3DES);
        Log.d(TAG, "Heyy boys, isOnlinePin:.............." +isOnlinePin);
        isonline=isOnlinePin;
        if (!isOnlinePin)
        {
            Log.d(TAG, "OFFLINE PIN.....is Selected" );
            param.putString(ConstIPinpad.startPinInput.param.KEY_promptString_String, "OFFLINE PINPAD , retry times:" + retryTimes);

        }
        globeParam.putString( ConstIPinpad.startPinInput.globleParam.KEY_Display_One_String, "[1]");
        try {
            Log.d(TAG, "KSN before pinInput    "+ Utility.byte2HexStr(iDukpt.getDukptKsn(1)) + " " + panBlock);
            ipinpad.startPinInput(1, param, globeParam, pinInputListener);
//            iDukpt.getDukptKsn(1);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    /**
     * \brief initialize the pin pad listener
     * <p>
     * \code{.java}
     * \endcode
     */


    public void doPinPad(boolean isOnlinePin, int retryTimes) {

        Log.d(TAG, "here for pin 4");

        Bundle extend = new Bundle();
        extend.putBoolean("isPlainKey", true);
        extend.putBoolean("KSNAutoIncrease", true);

        Log.d(TAG, "doSetKeys: charset" + Charset.defaultCharset());

        boolean bRet = false;

        /////////////////////////////////////////For Master/Session
        // masterKey ="";
        String masterKey22 = "F8BC8610CDF183A8";
        String component1 = "F8BC8610CDF183A883E6C46810688CA4";
        String component2 = "5E1AA4C49E70AD5EA4DF2F9443C72C1C";
        String component3 = "00000000000000000000000000000000";
        pinKey_WorkKey="373E47A1A09BF2CF08387CF2A0D20D67";
        byte[] cc1=Utility.hexStr2Byte(component1);
        byte[] cc2=Utility.hexStr2Byte(component2);
        byte[] cc3=Utility.hexStr2Byte(component3);
        byte[] result = new byte[cc1.length];
        int i = 0;

        for (byte b1: cc1) {
            byte b2 = cc2[i];
            byte b3 = cc3[i];
            result[i] = (byte) (b1 ^ b2 ^ b3);//final masterkey
            i++;
        }
        Log.d("PINPAD", "isKeyExist:" + Utility.byte2HexStr(result));

        //try {
        //    bRet = ipinpad.clearKey(20, 0);
        // }
        //catch (RemoteException e) {
        //  e.printStackTrace();
        //}


        /* comment for diplication key load
        try {
            bRet = ipinpad.isKeyExist(0, 20);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Log.d("PINPAD", "isKeyExist:" + bRet);
        try {
            bRet = ipinpad.loadMainKey(20, result, null);
            //    bRet=ipinpad.loadEncryptMainKeyEX(20,Utility.hexStr2Byte(masterKey),0x01,null,null);
            // bRet = ipinpad.loadMainKeyWithAlgorithmType(20,Utility.hexStr2Byte(masterKey22),0x86,null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }*/

        // Log.d("PINPAD", "loadMainKey:" + bRet);

       /* try {
            bRet = ipinpad.loadWorkKey(PinpadKeyType.PINKEY, 20, 1, Utility.hexStr2Byte(pinKey_WorkKey), null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }*/

        //Log.d("PINPAD", "loadWorkKey:" + bRet);

        /////////////////////////////////////////End Master/Session

        Bundle param = new Bundle();
        Bundle globeParam = new Bundle();
        String panBlock = savedPan;

        byte[] pinLimit = {0, 4, 5, 6}; // 0 means bypass pin input
        param.putString("promptString", "ONLINE PINPAD / የሚስጥር ቁጥር");
        param.putString("promptsFont", "/system/fonts/DroidSans-Bold.ttf");
//        param.putByteArray("displayKeyValue", nebyte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        param.putByteArray(ConstIPinpad.startPinInput.param.KEY_pinLimit_ByteArray, pinLimit);
        param.putInt(ConstIPinpad.startPinInput.param.KEY_timeout_int, 60);
        param.putBoolean(ConstIPinpad.startPinInput.param.KEY_isOnline_boolean, isOnlinePin);
        param.putString(ConstIPinpad.startPinInput.param.KEY_pan_String, panBlock);
        //param.putInt(ConstIPinpad.startPinInput.param.KEY_desType_int, ConstIPinpad.startPinInput.param.Value_desType_DUKPT_3DES);
        param.putInt(ConstIPinpad.startPinInput.param.KEY_desType_int, ConstIPinpad.startPinInput.param.Value_desType_3DES);
        Log.d(TAG, "Heyy boys, isOnlinePin:.............." +isOnlinePin);
        isonline=isOnlinePin;
        if (!isOnlinePin)
        {
            Log.d(TAG, "OFFLINE PIN.....is Selected" );
            param.putString(ConstIPinpad.startPinInput.param.KEY_promptString_String, "OFFLINE PINPAD, retry times:" + retryTimes);

        }
        globeParam.putString( ConstIPinpad.startPinInput.globleParam.KEY_Display_One_String, "[1]");
        try {
            Log.d(TAG, "KSN before pinInput    "+ Utility.byte2HexStr(iDukpt.getDukptKsn(1)) + " " + panBlock);
            ipinpad.startPinInput(1, param, globeParam, pinInputListener);
//            iDukpt.getDukptKsn(1);
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * \brief initialize the pin pad listener
     * <p>
     * \code{.java}
     * \endcode
     */
    public void initializePinInputListener() {
        pinInputListener = new PinInputListener.Stub() {
            @Override
            public void onInput(int len, int key) throws RemoteException {
                Log.d(TAG, "PinPad onInput, len:   " + len + ", key:" + key);
            }
            @Override
            @NonNull
            public void onConfirm(@NonNull byte[] data, boolean isNonePin) throws RemoteException {
                Log.d(TAG, "PinPad onConfirm, isNonePin:.............." + isNonePin);
                isNonePin1=isNonePin;
                Log.d(TAG, "Txn_Menu_Type..........  " + Txn_Menu_Type);

                if(Txn_Menu_Type.equals("Magstrip"))
                {
                    //onInputPinConfirm.onConfirm();

                    //if( !isNonePin ) {
                    Log.d(TAG, "Pinblock :" + Utility.byte2HexStr(data) + "KSN: " + Utility.byte2HexStr(iDukpt.getDukptKsn(1)));
                    //}

                    savedPinblock = data;

                    Log.d(TAG, "here for pin 3   "+ Utility.byte2HexStr(data));
                    Log.d(TAG, "isonlinepin.........   "+ isonline);
                    if(isonline && Utility.byte2HexStr(savedPinblock).equals(""))
                    {
                        Log.d(TAG, "PIN EMPITY---"+ Utility.byte2HexStr(savedPinblock));
                        Message msg = new Message();
                        msg.getData().putString("msg", "");
                        dialog4.sendMessage(msg);
                        PrinterExActivity.inputpin.finish();

                    }else{
                        iemv.importPin(1, data);

                    }

                    Log.d(TAG, "savedPinblock---"+ Utility.byte2HexStr(savedPinblock));

                    Log.d(TAG, "PinPad entered     " + Utility.byte2HexStr("0840".getBytes()));


                    Log.d(TAG, "PinPad initializeMAG call ");
                    initializeMAG();

                }else //without Magstrip
                {
                    onInputPinConfirm.onConfirm();

                    //if( !isNonePin ) {
                    Log.d(TAG, "Pinblock :" + Utility.byte2HexStr(data) + "KSN: " + Utility.byte2HexStr(iDukpt.getDukptKsn(1)));
                    //}

                    savedPinblock = data;

                    Log.d(TAG, "here for pin 3   " + Utility.byte2HexStr(data));
                    Log.d(TAG, "isonlinepin.........   " + isonline);
                    if (isonline && Utility.byte2HexStr(savedPinblock).equals(""))
                    {
                        Log.d(TAG, "PIN EMPITY---" + Utility.byte2HexStr(savedPinblock));
                        PrinterExActivity.inputpin.finish();
                        Message msg = new Message();
                        msg.getData().putString("msg", "");
                        dialog4.sendMessage(msg);

                    } else {
                        iemv.importPin(1, data);

                    }

                    Log.d(TAG, "savedPinblock---" + Utility.byte2HexStr(savedPinblock));

                    Log.d(TAG, "PinPad entered     " + Utility.byte2HexStr("0840".getBytes()));

                    Log.d(TAG, "Txn_Menu_Type  initializePinInputListener   " + Txn_Menu_Type);

                }
            }

            @Override
            public void onCancel() throws RemoteException {
                Log.d(TAG, "PinPad onCancel");
            }

            @Override
            public void onError(int errorCode) throws RemoteException {
                Log.d(TAG, "PinPad onError, code:  " + errorCode);
            }
        };
    }

    public void doSetAID(int type) {
        showUI("Set AID start");
        EmvSetAidRid emvSetAidRid = new EmvSetAidRid(iemv);
        emvSetAidRid.setAID(type);
        if (type == 2) {

        } else {
            try {
                iBeeper.startBeep(200);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        showUI("Set AID DONE");
    }

    public void doSetRID(int type) {
        showUI("Set RID start");
        EmvSetAidRid emvSetAidRid = new EmvSetAidRid(iemv);
        emvSetAidRid.setRID(type);
        if (type == 2) {

        } else {
            try {
                iBeeper.startBeep(200);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        showUI("Set RID DONE");
    }

    Handler onlineResponse = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            Log.i(TAG, "handle message:" + val);
        }
    };

    public ISO8583 isoResponse = null;

    Runnable onlineRequest = new Runnable() {

        @Override
        public void run() {

            Log.d(TAG, "Txn_type.........:" + Txn_type);
            Log.d(TAG, "Txn_menu_type.........:" + Txn_Menu_Type);

            sp.IP_port_loadData();
            sp.load_Timeoutdata();
            sp.chk_timeoutdb();
            //ISO8583.PACKET_TYPE.PACKET_TYPE_NONE
            Log.e(TAG, "SEND Packet.............");
            //if(Txn_Menu_Type.equals("SETTLEMENT"))
            //{
                //mIso8583 = getIso8583Packet(0, "", "", TransType.T_KEYDOWNLOAD);
                //byte[] packet = mIso8583.getPacket(ISO8583.PACKET_TYPE.PACKET_TYPE_HEXLEN_BUF);
            //}

            tr.assign();
//GLobalFields.PrimaryBitmap="7234058020C08200";
            byte[] packet = tr.send();
            if(Txn_Menu_Type.equals("Manualcard")&&Supervisor_menu_activity.manual_txn_type1.equals("PURCHASE")){
                String manualpack=Utility.byte2HexStr(packet).substring(24);
                String pack="303230307234058000C08000"+manualpack;
                packet=Utility.hexStr2Byte(pack);
            }else if(Txn_Menu_Type.equals("Manualcard")&&Supervisor_menu_activity.manual_txn_type1.equals("BALANCE_INQUIRY")){
                String manualpack=Utility.byte2HexStr(packet).substring(24);
                String pack="303130307234058000C08000"+manualpack;
                packet=Utility.hexStr2Byte(pack);

            }


            Log.d(TAG, "senddddddddddata" + Utility.byte2HexStr(packet));

            MultiHostsConfig.initialize();
            Log.e(TAG, "HostAddress "+HostInformation.hostAddr+"    Port   "+HostInformation.hostPort);

            Comm comm = new Comm(HostInformation.hostAddr, HostInformation.hostPort);
            // Log.d(TAG, "packet........:" + packet);
//connect server error

              if (!comm.connect())
              {
                Log.e(TAG, "connect server error");
               // dbHandler.updatelastflag();
                 ActivityCollector.finishloading();
                //d.addtransactionfile("transactionfile","transactionfile","transactionfile","transactionfile","transactionfile","transactionfile","noresponce");
                //Log.e(TAG, "transaction adedd");
                SharedPreferences Preferences = context.getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = Preferences.edit();
                // editor1.putString("response", "9.13POS9TER1              220831104947FO00050001B000000000000010000F062034  G@@@@@@@@P1gAPPROVALh00000009706Q01004B262C76612E77513030");
                //editor1.putString("response",Transactiondata.responsemessage);*/
                editor1.putString("printtype", "isoffline");
                editor1.commit();
//..................................TXN ADDED HARDCODE
                // String response="9.13POS9TER1              220831104947FO00050001B000000000000010000F062034  G@@@@@@@@P1gAPPROVALh00000009706Q01004B262C76612E77513030";

                /*String response1=Transactiondata.responsemessage;
                //byte[] response=(Utility.hexStr2Byte(response1));
                byte[] response=Utility.hexStr2Byte(response1);
                //tr.assignresponce();
                tr.iso8583unpack(response1);

                // Log.i(TAG, "TXN ADDED HARDCODE :");
                //dbHandler.addtransactionfile("0", Txn_type, Transactiondata.ISO8583msg_HEADER_VARS.transmission_No, Transactiondata.ISO8583msg_OPTIONAL_DATA_FIELD_VARS.h_sequence_Number, ISO8583msg.r_currency1, Transactiondata.daterecit + "     " + Transactiondata.timereciet, "", Cashier_menu_activity.Logon_name, savedPan, aidLabel1, aid1, tvr, response);

                Intent sign = new Intent(context, ESignActivity.class);
                sign.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(sign);*/
                 printTest(0);
                //Log.d(TAG, "print offline ");
                act.finish();
                return;
            }

            else {
                byte[] timeoutresponse=null;
                byte[] response=null;
//CHECK AND  SEND FRIST TIMEOUT DATA................................................Added by Ameeeex

                if(ISO8583msg.timeoutdbrow_cnt>0)//1st Send Timeout reversal next lastdata
                {
                    Log.e(TAG, " Stored Timeout reversal data   " + ISO8583msg.timeoutreversaldata);
                    timeout_reversal = Utility.hexStr2Byte(ISO8583msg.timeoutreversaldata);
                    Log.e(TAG, "send timeout.lastdata............................ " + new String(timeout_reversal));
                    if (!Txn_Menu_Type.equals("Manualcard")){
                        comm.send(timeout_reversal);
                }
                    try {
                        response = comm.receive(1024, 30);
                        Log.d(TAG, "Recived...:"+Utility.byte2HexStr(response));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, "timeout_reversal...:errrror");
                    }

                   if(response==null){
                        Log.d("VIEW", "TIMEOUT, REVERSAL Failed........");

                        showUI("TIMEOUT, REVERSAL Failed");
                    }
                    //clear all data form the data base
                    dbHandler=new DBHandler(context);
                    DBHandler.user_functions user_fun = dbHandler.new user_functions();
                    user_fun.delete_Time_outdb();
                    showUI("PROCESSING TIMEOUT, REVERSAL!");
                    try {
                        Thread.sleep(2000);
                        Log.d("VIEW", "waiting 1000 milliseconds........");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        //Log.d("VIEW", "view users Amex here after loadDataTask error");
                    }
                    comm.disconnect();
                    Log.e(TAG, "close timeoutreversal......finally");
                    ///time out reversal end......................................................................
                    comm = new Comm(HostInformation.hostAddr, HostInformation.hostPort);
                    comm.connect();
                    comm.send(packet);
                    response = comm.receive(1024, 30);
               //without time out reversal..........................................................................start

                }else
                {

                    Log.e(TAG, "Send ..........................");
                    comm.send(packet);
                    response = comm.receive(1024, 30);
                   // tr.iso8583unpack(Utility.byte2HexStr(response));
                    Log.d(TAG, "Recived...:"+Utility.byte2HexStr(response));
                }

           //..........................................................................................

                   // response = null;
                if (response == null)
                {
                    Log.e(TAG, " Null or Incorrect response");
                    ActivityCollector.finishloading();
                    comm.disconnect();
                    dbHandler.updatelastflag();
                    SharedPreferences Preferences=context.getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1=Preferences.edit();
                    editor1.putString("printtype","isoffline");
                    editor1.putString("responcenull","responcenull");
                    editor1.commit();
                    printTest(1);
                    Log.d(TAG, "Incorrect response print offline ");
                    //AMEX TIME OUT REVERSAL ADDED........................................................start

                    dbHandler=new DBHandler(context);
                    DBHandler.user_functions user_fun=dbHandler.new user_functions();
                         byte[] timeoutreversal = tr.sendtimeoutreversal();

                         String timeout_1 = Utility.byte2HexStr(timeoutreversal);
                         timeout_1 = timeout_1.substring(24);
                         timeout_1 = "303430307230058020C08000" + timeout_1;//"303430307234058020C08200"+timeout_1;
                         // 7230058020C08000
                         Log.d(TAG, "INSERTED Timeout reversal.... " + timeout_1);

                         if (ISO8583msg.timeoutdbrow_cnt == 0) {
                             dbHandler.TABLE_NAME_TIME_OUT_DB = "TIME_OUT_DB";
                             user_fun.Add_Time_outdb(timeout_1);
                             Log.d(TAG, "INSERTED Timeout reversal.... ");


                         } else {
                             String id = "1";
                             user_fun.Update_Time_outdb(id, timeout_1);
                             Log.d(TAG, "UPDATED Timeout Reversal.... ");
                             Log.d(TAG, "UPDATED Timeout Reversal.... ");
                         }
//.....................................................................end..
                    act.finish();
                } else//Server Connected print success full transactions
                {
                    comm.disconnect();
                    Log.i(TAG, "Server Connected........Test returnn length:" + response.length);

                    Log.i(TAG, "Response:"+Utility.byte2HexStr(response));
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("response", Utility.byte2HexStr(response));
                    editor.putString("printtype", " ");
                    editor.commit();

                    Log.i(TAG, "response Ascii: " + hexToASCII(Utility.byte2HexStr(response)));
                    //unpack the response message

                    tr.iso8583Unpack(Utility.byte2HexStr(response));

                   //isoResponse = hostInformation.new8583();


                        String message = "";
                        String s;
                        String type = "";
                        s=Transactiondata.ResponseFields.mti;
                        if (null != s) {
                            type = s;
                            message += "Message Type:";
                            message += s;
                            message += "\n";
                        }

                        s = Transactiondata.ResponseFields.field39;
                        if (null != s) {
                            message += "Response(39):";
                            message += s;
                            message += "\n";
                        }
                        if (type.equals("0810")) {
                            s = Transactiondata.ResponseFields.field53;
                            if (null != s) {
                                Log.d(TAG, "Field53:" + s);
                                if (s.length() == 48) {
                                    pinKey_WorkKey = s.substring(0, 32);
                                    macKey = s.substring(32, 48);
                                } else if (s.length() == 80) {
                                    pinKey_WorkKey = s.substring(0, 64);
                                    macKey = s.substring(64, 80);
                                } else if (s.length() == 120) {
                                    pinKey_WorkKey = s.substring(0, 64);
                                    macKey = s.substring(64, 80);
                                }
                                message += "pinKey:";
                                message += pinKey_WorkKey;
                                message += "\n";
                                message += "macKey:";
                                message += macKey;
                                message += "\n";
                            }
                        }/* else if (type.equals("0210")) {
                            s = Transactiondata.ResponseFields.field54;  // 1002764C000123456000
                            if (null != s && s.length() > 10) {
                                message += "Balance(54):";
                                message += s.substring(0, 2) + "," + s.substring(2, 4) + "," + s.substring(4, 7) + "," + s.charAt(7);
                                message += "\n" + Integer.valueOf(s.substring(8, s.length() - 1));
                                message += "\n";
                            }
                        }*/
                        //showUI(message);


                    Log.d(TAG, "print Online");
                    //editor.putString("printtype", " ");
                    if (Transactiondata.ResponseFields.field39.equals("000") && Txn_Menu_Type.equals("SETTLEMENT"))
                    {
                        SharedPreferences Preferences = context.getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = Preferences.edit();
                        editor1.putString("printtype", "endofday");
                        editor1.commit();
                        printTest(1);
                        Log.e(TAG, " SETTLEMENT");
                    }
                    else if(Transactiondata.ResponseFields.field39.equals("000") && !Txn_Menu_Type.equals("SETTLEMENT"))
                    {
                        Intent sign = new Intent(context, ESignActivity.class);
                        sign.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(sign);
                        Log.d(TAG, "Sign");

                    } else {
                        SharedPreferences Preferences = context.getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = Preferences.edit();
                        editor1.putString("printtype", "isoffline");
                        editor1.commit();
                        printTest(1);

                    }

                    //..................................................
                }

            }

            // Message msg = new Message();
            // Bundle data = new Bundle();
            // data.putString("value", "receive finished");
            // msg.setData(data);
            // onlineResponse.sendMessage(msg);
            Log.d(TAG, "finished actttttttttttttttttbefor ");

            act.finish();

        }

    };

    public void downloadkey()
    {
        Log.d(TAG, "keyyyy Download Selected");
        Intent i = new Intent(context, loadingactivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        new Thread(keydownload).start();
        // keydownload.run();

    }
    Runnable keydownload = new Runnable()
    {
        @Override
        public void run() {
            sp=new ISO8583msg(context);
            sp.loadData();//terminal id
            sp.loadData1();//merchant id
            sp.IP_port_loadData();
            sp.load_Timeoutdata();
            sp.chk_timeoutdb();
            MultiHostsConfig.initialize();
            ActivityCollector acc=new ActivityCollector();

            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("Txn_Menu_Type","Keydownload");
            editor.putString("txn_type","download");

            editor.putString("merchantid",ISO8583msg.Mer_id);
            editor.putString("currency",ISO8583msg.currency);
            editor.putString("terminalid",ISO8583msg.Ter_id);
            editor.commit();
            Txn_type=sharedPreferences.getString("txn_type", "");
            Txn_Menu_Type=sharedPreferences.getString("Txn_Menu_Type", "");
            Log.e(TAG, "Txn_type "+Txn_type+"    Txn_Menu_Type   "+Txn_Menu_Type);


            Log.d(TAG, "transbase" +"keydownload"+" "+sharedPreferences.getString("txn_type", ""));

            //Field_11
            STAN=Integer.parseInt(sharedPreferences.getString("STAN","1"));
            Log.d(TAG, "STAN "+fillgapsequence1(String.valueOf(STAN),6));
            STAN=STAN+1;
            //SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("STAN",String.valueOf(STAN));
            editor.commit();

            Transactiondata.keyDownloadRequestFields.field11=fillgapsequence1(String.valueOf(STAN),6);
            Log.e(TAG, "field_11...: " + Transactiondata.keyDownloadRequestFields.field11);

//filed_41
            Transactiondata.keyDownloadRequestFields.field41= sharedPreferences.getString("terminalid","termid");
            Log.d(TAG, "field_41  : " + Transactiondata.keyDownloadRequestFields.field41);


            // field_42 mid
            Transactiondata.keyDownloadRequestFields.field42= sharedPreferences.getString("merchantid","merid");
            Log.d(TAG, "field_42  : " + Transactiondata.keyDownloadRequestFields.field42);


            // Filed_62  Invoce number
            /*ISO8583.ISO8583_FIELD.field_62="0006";
            Log.d(TAG, "Filed_62  : "+ISO8583.ISO8583_FIELD.field_62);

            ISO8583.ISO8583_FIELD.field_62=ISO8583.ISO8583_FIELD.field_62 + (Utility.byte2HexStr(ISO8583.ISO8583_FIELD.field_11.getBytes()));
            //ISO8583.ISO8583_FIELD.field_62=Utility.byte2HexStr(Invoce_number.getBytes()) + hexToASCII(ISO8583.ISO8583_FIELD.field_11);
            Log.d(TAG, "Filed_62 : "+ISO8583.ISO8583_FIELD.field_62);*/

            //mIso8583=getIso8583Packet(0,"","",TransType.T_KEYDOWNLOAD);
            //byte[] packet = mIso8583.getPacket(ISO8583.PACKET_TYPE.PACKET_TYPE_HEXLEN_BUF);
            byte[]packet=Transactiondata.packKeyDownloadRequestFields();

            Log.d(TAG, "Key Download|Sending Packet..........:");
            MultiHostsConfig.initialize();

            Log.d(TAG, "HostAddress "+HostInformation.hostAddr+"    Port   "+HostInformation.hostPort);
            Comm comm = new Comm(HostInformation.hostAddr, HostInformation.hostPort);

            //editor.putString("txn_type","");
            //editor.commit();
            if (!comm.connect())
            {
                Log.e(TAG,"key download|Server Connection Error");
                acc.finishloading();
            }
            else
            {
                comm.send(packet);
                byte[] response = comm.receive(1024, 30);
                if (response == null)
                {
                    Message msg = new Message();
                    msg.getData().putString( "msg", "Null Response");
                    commondialog.sendMessage(msg);
                    // Toast.makeText(context, "FINISHED KEYDOWNLOAD,STATUS ",Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "response is null ");
                    //Toast.makeText(context, "responce null",Toast.LENGTH_SHORT).show();
                    editor.putString("download","");
                    editor.commit();

                    acc.finishloading();
                    comm.disconnect();

                } else {

                    //comm.disconnect();
                    Boolean  bRet = false;
                    Log.i(TAG, "Keydownload Test returnn length: " + response.length);
                    Log.i(TAG, "Keydownload response: "+Utility.byte2HexStr(response));
                    String filed53final=Utility.byte2HexStr(response).substring(Utility.byte2HexStr(response).length()-32);
                    Log.i(TAG, "fld53: "+Utility.byte2HexStr(response).substring(Utility.byte2HexStr(response).length()-32));

                    //Toast.makeText(context, "downloaded successfully",Toast.LENGTH_SHORT).show();

                    Log.i(TAG, "Keydownload response ascii: "+hexToASCII(Utility.byte2HexStr(response)));
                    acc.finishloading();
                    editor.putString("response", hexToASCII(Utility.byte2HexStr(response)));
                    editor.commit();

                    //iso unpack.......................................................................
                    //isoResponse = hostInformation.new8583();
                    Transactiondata.iso8583UnpackKeyDownload(Utility.byte2HexStr(response));

                        Log.d(TAG, "isoResponse.......................Herrr ");
                        String message = "";
                        String s;
                        String type = "";

                        s = Transactiondata.KeyDownloadResponseFields.mti;
                        if (null != s)
                        {
                            Log.d(TAG, "if.......................1 S_Value " + s);
                            type = s;
                            message += "Message Type:";
                            message += s;
                            message += "\n";
                        }
                        s = Transactiondata.KeyDownloadResponseFields.field39;
                        if (null != s) {
                            Log.d(TAG, "if.......................2 S_Value  " + s);
                            message += "Response(39):";
                            message += s;
                            message += "\n";
                        }
                        if (type.equals("0810"))
                        {
                            s =Transactiondata.KeyDownloadResponseFields.field53;

                            Log.d(TAG, "if.......................3 S_Value " + s);
                            if (null != s)
                            {
                                Log.d(TAG, "Field53:" + s);
                                if (s.length() == 48)
                                {
                                    Log.d(TAG, "s.length() == 48 ");
                                    pinKey_WorkKey = s.substring(0, 32);
                                    macKey = s.substring(32, 48);
                                }
                                else if (s.length() == 53)
                                {
                                    Log.d(TAG, "s.length() == 53 ");
                                    pinKey_WorkKey = s.substring(0, 35);
                                    macKey = s.substring(35, 53);
                                }

                                else if (s.length() == 80)
                                {
                                    Log.d(TAG, "s.length() == 80 ");
                                    pinKey_WorkKey = s.substring(0, 64);
                                    macKey = s.substring(64, 80);
                                } else if (s.length() == 120)
                                {
                                    Log.d(TAG, "s.length() == 120 ");
                                    pinKey_WorkKey = s.substring(0, 64);
                                    macKey = s.substring(64, 80);
                                }
                                message += "pinKey:";
                                message += pinKey_WorkKey;
                                message += "\n";
                                message += "macKey:";
                                message += macKey;
                                message += "\n";
                            }
                        } else if (type.equals("0210"))
                        {
                            s = Transactiondata.KeyDownloadResponseFields.field54;  // 1002764C000123456000
                            Log.d(TAG, "if.......................4 S_Value  "+ s);
                            if (null != s && s.length() > 10) {
                                message += "Balance(54):";
                                message += s.substring(0, 2) + "," + s.substring(2, 4) + "," + s.substring(4, 7) + "," + s.substring(7, 8);
                                message += "\n" + Integer.valueOf(s.substring(8, s.length() - 1));
                                message += "\n";
                            }
                        }
                        //showUI(message);


                    //Log.i(TAG, "Keydownload...:" + ISO8583.ISO8583_FIELD.r_field_62);
                    Log.i(TAG, "Keydownload...:" + Transactiondata.KeyDownloadResponseFields.field53);



                    try {
                        bRet = ipinpad.loadWorkKey(PinpadKeyType.PINKEY, 97, 1, Utility.hexStr2Byte(filed53final), null);
                    } catch (RemoteException e)
                    {
                        e.printStackTrace();
                    }
                    Log.i(TAG, "Keydownload Length:" + Transactiondata.KeyDownloadResponseFields.field53.length());
                    Message msg = new Message();

                    if(bRet=true && Transactiondata.KeyDownloadResponseFields.field53.length()==16)
                    {
                       // showUI(" SUCCESS,  KEY DOWNLOAD");
                        msg.getData().putString("msg", "SUCCESSFUL KEY DOWNLOAD");
                    }else
                    {
                        // showUI("FAILURE,  KEY DOWNLOAD");
                        msg.getData().putString("msg", "KEY DOWNLOAD FAILED");

                    }
                    commondialog.sendMessage(msg);
                    // Toast.makeText(context, "FINISHED KEYDOWNLOAD,STATUS ",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "KEYDOWNLOAD,STATUS IS :" + bRet);
                    editor.putString("download","");
                    editor.commit();

                }
            }
            //Message msg = new Message();
            //Bundle data = new Bundle();
            //data.putString("value", "receive finished");
            //msg.setData(data);
            //onlineResponse.sendMessage(msg);
        }
    };

    public  void settlementRequest()
    {
        Log.d(TAG, "Settlement Selected");
        Intent i = new Intent(context, loadingactivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        new Thread(settlement).start();
        // keydownload.run();

    }

      Runnable settlement= new Runnable()
     {
        @Override
        public void run() {
           //SharedPreferences sharedPreferences= context.getSharedPreferences("Shared_data",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            //SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("Txn_Menu_Type","SETTLEMENT_GBE");
            editor.putString("txn_type","SETTLEMENT_GBE");
            editor.putString("printtype","settlement");
            editor.commit();
            Transactiondata.GLobalFields.MTI="0520";
            Transactiondata.GLobalFields.PrimaryBitmap="0822010000C00000";
            float netAmount= common.Pamount - common.Rvamount+common.Camount;
            Log.d("Pamountttttrans", ""+ common.Pamount);
            Log.d("reverseamountttttrans", ""+ common.Rvamount);
            Log.d(TAG,"Settlemet netAmount.....:"+ netAmount);
            Log.d(TAG,"Settlemet netAmount.....:"+ Utility.getReadableAmount(""+netAmount));
            Log.d(TAG," netAmount.....:"+ Transactiondata.fillgapsequence(String.valueOf(netAmount).replace(".",""),12));
            Transactiondata.GLobalFields.Field05="C"+Transactiondata.fillgapsequence(String.valueOf(netAmount).replace(".",""),12);//"000000000012"
            String stan = sharedPreferences.getString("STAN","1");
            int st= Integer.parseInt(stan)+1;
            editor.putString("STAN", String.valueOf(st));
            editor.commit();
            Transactiondata.GLobalFields.Field11 = Transactiondata.fillgapsequence(String.valueOf(st),6);
            Transactiondata.GLobalFields.Field15=new SimpleDateFormat("yyMMdd", Locale.getDefault()).format(new Date());
            Transactiondata.GLobalFields.Field24="504";
            Transactiondata.GLobalFields.Field41 = sharedPreferences.getString("terminalid","");
            Transactiondata.GLobalFields.Field42 = sharedPreferences.getString("merchantid","");

            byte[] packet=Transactiondata.packSettlementRequestFields();

            Log.d(TAG,"Settlemet Request|Sending Packet.....:"+ Utility.byte2HexStr(packet));

            MultiHostsConfig.initialize();
            ActivityCollector acc=new ActivityCollector();
            Log.d(TAG, "HostAddress "+HostInformation.hostAddr+"    Port   "+HostInformation.hostPort);

            Comm comm=new Comm(HostInformation.hostAddr,HostInformation.hostPort);
            if (!comm.connect()) {
                Log.e(TAG, "Settlement|Server Connection error");
                SharedPreferences Preferences = context.getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = Preferences.edit();
                // editor1.putString("response", "9.13POS9TER1              220831104947FO00050001B000000000000010000F062034  G@@@@@@@@P1gAPPROVALh00000009706Q01004B262C76612E77513030");
                //editor1.putString("response",Transactiondata.responsemessage);*/
                editor1.putString("printtype", "isoffline");
                editor1.commit();
                printTest(1);
                acc.finishloading();
            } else {
                comm.send(packet);
                byte[] response = comm.receive(1024, 30);
                Log.d(TAG, "Settlement response...." + Utility.byte2HexStr(response));
                Log.d(TAG, "Settlement response(ASCII)...." + Utility.hexToAscii(Utility.byte2HexStr(response)));

                if (response == null) {
                    Log.e(TAG, " Null or Incorrect response");
                    editor.putString("printtype", "isoffline");
                    editor.putString("responcenull", "responcenull");
                    editor.commit();
                    comm.disconnect();
                    acc.finishloading(); // Finish loading due to null or incorrect response
                    printTest(1);
                } else {
                    Transactiondata.unpackSettlementResponse(Utility.byte2HexStr(response));
                    Message msg = new Message();

                    if (Transactiondata.ResponseFields.field39.equals("000")) {
                        Log.d(TAG, "Settlement Successful!");

                        networkManagementRequest();


                    } else if (Transactiondata.ResponseFields.field39.equals("005")) {
                        Transactiondata.GLobalFields.Field05 = Transactiondata.ResponseFields.field30;
                        packet = Transactiondata.packSettlementRequestFields();
                        comm.send(packet);
                        response = comm.receive(1024, 30);
                        Log.d(TAG, "Settlement response...." + Utility.byte2HexStr(response));
                        Log.d(TAG, "Settlement response(ASCII)...." + Utility.hexToAscii(Utility.byte2HexStr(response)));

                        if (response == null) {
                            Log.e(TAG, " Null or Incorrect response");
                            editor.putString("printtype", "isoffline");
                            editor.putString("responcenull", "responcenull");
                            editor.commit();
                            acc.finishloading();
                            printTest(1);
                            comm.disconnect();
                        } else {
                            Transactiondata.unpackSettlementResponse(Utility.byte2HexStr(response));

                            if (Transactiondata.ResponseFields.field39.equals("000")) {
                                Log.d(TAG, "Settlement Successful!");
                                 // Finish loading after successful settlement
                                networkManagementRequest();

                                Log.d(TAG, "Network Reseted!");
                            } else {
                                Log.d(TAG, "Settlement declined!");
                                msg.getData().putString("msg", "       " + Transactiondata.ResponseFields.field44);
                                commondialog.sendMessage(msg); // Sending message after processing settlement response
                                acc.finishloading();
                            }
                        }
                    } else {
                        Log.d(TAG, "Settlement declined!");
                        msg.getData().putString("msg", "       " + Transactiondata.ResponseFields.field44);
                        commondialog.sendMessage(msg); // Sending message after processing settlement response

                    }

                }
              }
             }
         };

     public  void networkManagementRequest()
      {
        Log.d(TAG, "Network Management Selected");
         new Thread(networkReset).start();
        // keydownload.run();
      }


    Runnable networkReset= new Runnable()
    {
        @Override
        public void run() {

            SharedPreferences sharedPreferences= context.getSharedPreferences("Shared_data",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();

            Transactiondata.GLobalFields.MTI="0800";
            Transactiondata.GLobalFields.PrimaryBitmap="2220010000C00001";

            Transactiondata.GLobalFields.Field03="990000";
            Transactiondata.GLobalFields.Field07=new SimpleDateFormat("MMddhhmmss",Locale.getDefault()).format(new Date());

            String stan = sharedPreferences.getString("STAN","1");
            int st= Integer.parseInt(stan)+1;
            editor.putString("STAN", String.valueOf(st));
            editor.commit();
            Transactiondata.GLobalFields.Field11 = Transactiondata.fillgapsequence(String.valueOf(st),6);
            Transactiondata.GLobalFields.Field24="821";
            Transactiondata.GLobalFields.Field41 = sharedPreferences.getString("terminalid","");
            Transactiondata.GLobalFields.Field42 = sharedPreferences.getString("merchantid","");
            Transactiondata.GLobalFields.Field64="0000000000000000";

            byte[] packet=Transactiondata.packFieldsForNetworkReset();

            Log.d(TAG,"Echo Request|Sending packet.....:"+ Utility.byte2HexStr(packet));

            MultiHostsConfig.initialize();
            ActivityCollector acc=new ActivityCollector();
            Log.d(TAG, "HostAddress "+HostInformation.hostAddr+"    Port   "+HostInformation.hostPort);
            Comm comm=new Comm(HostInformation.hostAddr,HostInformation.hostPort);

            if(!comm.connect()){
                Log.e(TAG,"Echo|Server Connection error");
                //acc.finishloading();
                Log.e(TAG, " Null or Incorrect response");
                editor.putString("printtype", "isoffline");
                editor.commit();
                acc.finishloading();
                printTest(1);
                comm.disconnect();
            }
            else
            {
                comm.send(packet);
                byte[] response=comm.receive(1024,30);
                Log.d(TAG,"Echo response...."+Utility.byte2HexStr(response));
                Log.d(TAG,"Echo response(ASCII)...."+Utility.hexToAscii(Utility.byte2HexStr(response)));
                Message msg = new Message();
                if(response==null){
                    editor.putString("printtype", "isoffline");
                    editor.putString("printtype", "isoffline");
                    editor.commit();
                    acc.finishloading();
                    printTest(1);
                    Log.e(TAG, " Null or Incorrect response");
                    comm.disconnect();
                    //msg.getData().putString("msg","TOTALS Not RESETTED");
                }else{
                    SharedPreferences.Editor editor1 = sharedPreferences.edit();
                    editor1.putString("printtype", "settlement");
                    editor1.commit();
                    printTest(1);
                    msg.getData().putString("msg","TOTALS RESETTED");
                    dbHandler=new DBHandler(context);
                    DBHandler.user_functions user_fun = dbHandler.new user_functions();
                    user_fun.delete_All_txn_data();
                    MenuActivity.empty();
                }
               acc.finishloading();
               
            }


        }
    };
    public  String fillgapsequence1(String data,int size)
    {
        String result=data;
        while(!(result.length()==size))
        {
            result="0"+result;
        }
        return result;
    }


    ISO8583 getIso8583Packet(int index, String cardBin, String AID, TransType transType)
    {
        ISO8583 iso8583;

        Date dt = new Date();


        // hexToASCII(Utility.byte2HexStr(response)))

        // get hostInformation first
        if (index >= 0)
        {
            hostInformation = MultiHostsConfig.getHost(index, null, null);
        } else
        {
            hostInformation = MultiHostsConfig.getHost(index, cardBin, AID);
        }

        iso8583 = hostInformation.new8583();    // make the iso8583 from various child class
        if(Txn_type.equals("REVERSAL"))
        {
            //iso8583.setValue(ISO8583.ATTRIBUTE.Time, hostInformation.timeFormat.format(dt));
            // iso8583.setValue(ISO8583.ATTRIBUTE.Date, hostInformation.dateFormat.format(dt));
        }
        return iso8583;

    }
    public int checkapproval(String appcode)
    {
        int trannum = -1;
        Boolean istrue=false;
        dbHandler=new DBHandler(context);
        tr=new Transactiondata(sharedPreferences,context);
        for(int i=0; i<dbHandler.gettransactiondata().size() - 1; i++)
        {
            // Log.i(TAG, "appcodegeneral: "+ Transactiondata.Subfieldresponse.F);

            if(dbHandler.gettransactiondata().get(i).getTpe().equals("PURCHASE"))
            {
                //tr.unpack(dbHandler.gettransactiondata().get(i).getRecievedtran());
                //Log.i(TAG, "appcode: "+ Transactiondata.Subfieldresponse.F);
                // if(Transactiondata.Subfieldresponse.F.equals(appcode))
                {
                    Log.i(TAG, "appcodefound:");
                    trannum=i;
                }
            }else {//eles if
                showUI("DO PURCHASE!, NO DATA RECORDED");

            }

        }
        return trannum;
    }
    public void abortEMV() {
        try {
            iemv.stopCheckCard();
            iemv.abortEMV();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    void showUI(String str) {
        Message msg = new Message();
        msg.getData().putString("msg", str);
        handler.sendMessage(msg);
    }


    public void printTest(int index) {

//        TransPrinter transPrinter = new TransPrinter(this.context);
//        TransPrinter.initialize(iPrinter);
//
//        transPrinter.print(1);
//        switch (index) {
//            case 1:
//                transPrinter = new PrintRecpSale(this.context);
//                break;
//            case 2:
//                transPrinter = new BalancePrinter(this.context);
//                break;
//            default:
//                transPrinter = new TransPrinter(this.context);
//                break;
//        }

        Log.d(TAG, "error is here");
        transPrinter = new PrintRecpSale(this.context);
        printReceipt(index);
    }

    //Judge transaction type : card/QR
    public void printReceipt(int index) {
        Log.d(TAG, "initialize default receipt data");

        Bundle bundle = new Bundle();
        bundle.putInt("copyIndex",index);
//        hostInformation.setMerchant("kaleb789","kaleb123","kaleb");
        transPrinter.initializeData(mIso8583, isoResponse, hostInformation, bundle);
        transPrinter.print();
        return;
//        Log.d(TAG, "print");
//        transPrinter.print();
    }

    private void startInputPINActivity(){
        Message msg = new Message();
        msg.what = TransactionParams.IMPORT_CARD_CONFIRM_RESULT;
        handleImport.sendMessage(msg);
    }
    Handler dialog = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(!errordialog.isdialog){
                final errordialog dialog = new errordialog(CheckCardActivity.context,msg.getData().getString("msg"));
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                Log.d(TAG, "dialog "+msg.getData().getString("msg"));

            }

        }
    };
    Handler dialog2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            final com.verifone.demo.emv.basic.dialog dia = new dialog(CheckCardActivity.context, "INSERT  CHIP");
            dia.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dia.show();
            Log.d(TAG, "dialog shown INSERT  CHIP");
        }
    };
    Handler dialog3 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            final com.verifone.demo.emv.basic.dialog dia3 = new dialog(CheckCardActivity.context, "Card Reading Issue! Try Again!");
            dia3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dia3.show();
            Log.d(TAG, "dialog shown reading issue.............");
        }
    };
    Handler dialog4 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "Empity pin.............");
            final com.verifone.demo.emv.basic.dialog dia4 = new dialog(PrinterExActivity.activity, "Please Insert pin");
            dia4.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dia4.show();
            Log.d(TAG, "dialog shown Empity pin.............");
        }
    };
    public Handler commondialog = new Handler( Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "dialog.............");
            String dialogmessage=msg.getData().getString("msg");
            final com.verifone.demo.emv.basic.dialog dia4 = new dialog(currentactivity/*Cashier_menu_activity.activity*/, dialogmessage);
            dia4.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dia4.show();
            Log.d(TAG, "dialog shown Empity pin.............");
        }
    };

    public void importCardConfirmResult() {
        Log.d(TAG, "EZEH Negn...importCardConfirmResult...............Dagi");
        Message msg = new Message();
        msg.what = TransactionParams.IMPORT_CARD_CONFIRM_RESULT;
        handleImport.sendMessage(msg);
    }

    public void doPinPad() {
        Log.d(TAG, "EZEH Negn..doPinPad................Dagi1");
        Message msg = new Message();
        msg.what = TransactionParams.IMPORT_PIN;
        handleImport.sendMessage(msg);
    }

    @SuppressLint("HandlerLeak")
    Handler handleImport = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {
                    case TransactionParams.IMPORT_APP_SELECT:
                        Log.d(TAG, "IMPORT_APP_SELECT.is selected.............Dagi");
                        iemv.importAppSelection(0);
                        break;
                    case TransactionParams.IMPORT_CARD_CONFIRM_RESULT:
                        Log.d(TAG, "IMPORT_CARD_CONFIRM_RESULT.is selected.............Dagi");
                        iemv.importCardConfirmResult(ConstIPBOC.importCardConfirmResult.pass.allowed);
                        break;
                    case TransactionParams.IMPORT_PIN:
                        Log.d(TAG, "IMPORT_PIN.is selected.............Dagi");
                        boolean isOnlinePin = TransactionParams.getInstance().isOnlinePin();
                        int retryTimes = TransactionParams.getInstance().getRetryTimes();
                        Log.d(TAG, "here for pin 1");
                        doPinPad(isOnlinePin, retryTimes);
                        break;
                    case TransactionParams.INPUT_ONLINE_RESULT:
                        Log.d(TAG, "INPUT_ONLINE_RESULT is selected..............Dagi");
                        break;
                    case TransactionParams.IMPORT_CERT_CONFIRM_RESULT:
                        Log.d(TAG, "IMPORT_CERT_CONFIRM_RESULT is selected..............Dagi");
                        break;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            Txn_Menu_Type=sharedPreferences.getString("Txn_Menu_Type", "");
            Log.d(TAG, "Txn_Menu_Type..............."+Txn_Menu_Type);

            if(Txn_Menu_Type.equals("Magstrip"))
            {
                //doPinPad();
                //boolean isOnlinePin=true;
                //int retryTimes=15;

                //Log.d(TAG, "Here requesting mag pin...");
                //pinInputHandler.sendMessage(new Message());
                //show the pin pad, import the pin block

                //TransactionParams.getInstance().setOnlinePin(isOnlinePin);
                //TransactionParams.getInstance().setRetryTimes(retryTimes);

                Log.d(TAG, "EZEH Negn.MAG Selected.handleMessage................WEtawwwwwwwwwwwww");
            }else {

                Log.d(TAG, "EZEH Negn..handleMessage................WEtawwwwwwwwwwwww");
            }

        }
    };
    public  String fillgapsequence(String data,int size){
        String result=data;
        while(!(result.length()==size)){
            result=" "+result;
        }
        return result;
    }

    public interface OnGetCardNoListener {
        void onGetCardNo(String pan);
    }

    public interface OnCardCheckedListener {
        void onSuccessful(String pan);

        void onError();

        void onTimeOut();
    }

    public void setOnGetCardNoListener(OnGetCardNoListener onGetCardNoListener) {
        this.onGetCardNoListener = onGetCardNoListener;
    }

    public interface OnInputPinConfirm {
        void onConfirm();
    }

    public void setOnInputPinConfirm(OnInputPinConfirm onInputPinConfirm) {
        this.onInputPinConfirm = onInputPinConfirm;
    }

    public static String hexToASCII(String hex)
    {
// initialize the ASCII code string as empty.
        String ascii = "" ;
        for ( int i = 0; i < hex.length(); i += 2 ) {
// extract two characters from hex string
            String part = hex.substring(i, i + 2 );
// change it into base 16 and typecast as the charact
            char ch = ( char )Integer.parseInt(part, 16 );
// add this char to final ASCII string
            ascii = ascii + ch;
        }
        return ascii;
    }
    public void checkresponcecode() {
        switch (responce_code)
        {
            //case ("007"):
            //r_resp_msg=="Keys Downloaded";
            // break;

            //case (50):

            // r_resp_msg="General Decline";
            // break;
        }
    }
    public void masterkeytrans(String comp1,String comp2){
        // masterKey ="";
        boolean bRet = false;
        String masterKey22 = "F8BC8610CDF183A8";
       /* String component1 = "F8BC8610CDF183A883E6C46810688CA4";
        String component2 = "5E1AA4C49E70AD5EA4DF2F9443C72C1C";*/
        String component1 = comp1;
        String component2 = comp2;

        String component3 = "00000000000000000000000000000000";
        pinKey_WorkKey="373E47A1A09BF2CF08387CF2A0D20D68";
        byte[] cc1=Utility.hexStr2Byte(component1);
        byte[] cc2=Utility.hexStr2Byte(component2);
        byte[] cc3=Utility.hexStr2Byte(component3);
        byte[] result = new byte[cc1.length];
        int i = 0;

        for (byte b1: cc1) {
            byte b2 = cc2[i];
            byte b3 = cc3[i];
            result[i] = (byte) (b1 ^ b2 ^ b3);//final masterkey
            i++;
        }
        Log.d("PINPAD", "isKeyExist:" + Utility.byte2HexStr(result));

        /*try {
            bRet = ipinpad.clearKey(20, 0);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }*/

        try {
            bRet = ipinpad.isKeyExist(0, 97);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Log.d("PINPAD", "isKeyExist:" + bRet);


        try {
            bRet = ipinpad.loadMainKey(97, result, null);
            //    bRet=ipinpad.loadEncryptMainKeyEX(20,Utility.hexStr2Byte(masterKey),0x01,null,null);
            // bRet = ipinpad.loadMainKeyWithAlgorithmType(20,Utility.hexStr2Byte(masterKey22),0x86,null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        //  Log.d("PINPAD", "loadMainKey:" + bRet);
          /*  try {
                bRet = ipinpad.loadWorkKey(PinpadKeyType.PINKEY, 20, 1, Utility.hexStr2Byte(pinKey_WorkKey), null);
            } catch (RemoteException e) {
                e.printStackTrace();
            }*/
        //  Log.i(TAG, "keydownstat:" + bRet);
        //   Log.i(TAG, "keydownstat:" + bRet);
    }


    public void addtodatabase()
    {
        if (Txn_Menu_Type.equals("Manualcard") && !Txn_type.equals("BALANCE_INQUIRY"))
        {

            Log.i(TAG, "Txn_Menu_Type  Manualcard is Selected :" + Txn_Menu_Type);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                dbHandler.addtransactionfile(
                        "0",
                        ISO8583.daterecit + "   " +ISO8583.timereciet,
                        ISO8583.ISO8583_HEADER.Header,
                        Transactiondata.ResponseFields.mti,
                        Transactiondata.ResponseFields.primaryBitmap,
                        ISO8583msg.Mer_id,
                        aidLabel1,
                        aid1,
                        tvr,
                        new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()),
                        DateFormat.getTimeInstance().format(new Date(System.currentTimeMillis())),
                        cardholder,
                        Txn_type,
                        ISO8583msg.r_currency1,
                        Transactiondata.GLobalFields.Field02,
                        Transactiondata.ResponseFields.field03,
                        Transactiondata.ResponseFields.field04,
                        Transactiondata.ResponseFields.field07,
                        Transactiondata.ResponseFields.field11,
                        Transactiondata.ResponseFields.field12,
                        Transactiondata.ResponseFields.field13,
                        Transactiondata.GLobalFields.Field14,
                        Transactiondata.GLobalFields.Field22,
                        Transactiondata.GLobalFields.Field24,
                        Transactiondata.GLobalFields.Field25,
                        Transactiondata.GLobalFields.Field35,
                        Transactiondata.ResponseFields.field37,
                        Transactiondata.ResponseFields.field38,
                        Transactiondata.ResponseFields.field39,
                        Transactiondata.ResponseFields.field41,
                        Transactiondata.GLobalFields.Field42,
                        Transactiondata.ResponseFields.field49,
                        Transactiondata.GLobalFields.Field55,
                        signdatabase);
            }


        } else
        if (Txn_type.equals("BALANCE_INQUIRY"))
        {
            Log.d(TAG, "TRANSACTION IS NOT ADDED IN TO DATABASE,BALANCE_INQUIRY");
        }else
        {
            Log.i(TAG, "Other Txn_Menu_Type is Selected :" + Txn_Menu_Type);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                dbHandler.addtransactionfile(
                        "0",
                        ISO8583.daterecit + "   " +ISO8583.timereciet,
                        ISO8583.ISO8583_HEADER.Header,
                        Transactiondata.ResponseFields.mti,
                        Transactiondata.ResponseFields.primaryBitmap,
                        ISO8583msg.Mer_id,
                        aidLabel1,
                        aid1,
                        tvr,
                        new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()),
                        DateFormat.getTimeInstance().format(new Date(System.currentTimeMillis())),
                        cardholder,
                        Txn_type,
                        ISO8583msg.r_currency1,
                        Transactiondata.GLobalFields.Field02,
                        Transactiondata.ResponseFields.field03,
                        Transactiondata.ResponseFields.field04,
                        Transactiondata.ResponseFields.field07,
                        Transactiondata.ResponseFields.field11,
                        Transactiondata.ResponseFields.field12,
                        Transactiondata.ResponseFields.field13,
                        Transactiondata.GLobalFields.Field14,
                        Transactiondata.GLobalFields.Field22,
                        Transactiondata.GLobalFields.Field24,
                        Transactiondata.GLobalFields.Field25,
                        Transactiondata.GLobalFields.Field35,
                        Transactiondata.ResponseFields.field37,
                        Transactiondata.ResponseFields.field38,
                        Transactiondata.ResponseFields.field39,
                        Transactiondata.ResponseFields.field41,
                        Transactiondata.GLobalFields.Field42,
                        Transactiondata.ResponseFields.field49,
                        Transactiondata.GLobalFields.Field55,
                        signdatabase);
            }

            //  Log.i(TAG, "filed02termin :"+ISO8583.ISO8583_FIELD.r_field_41+dbHandler.gettransactiondata().get(1).getField_41());

            Log.i(TAG, "sign value :"+signdatabase);

        }

    }
}
