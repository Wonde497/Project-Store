package com.verifone.demo.emv.transaction.sale;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.verifone.demo.emv.DBHandler;
import com.verifone.demo.emv.MenuActivity;
import com.verifone.demo.emv.Public_data.ISO8583msg;
import com.verifone.demo.emv.Transactiondata;
import com.verifone.demo.emv.Utilities.Utility;
import com.verifone.demo.emv.basic.HostInformation;
import com.verifone.demo.emv.basic.ISO8583;
import com.verifone.demo.emv.basic.common;
import com.verifone.demo.emv.transaction.ManualTestActivity;
import com.verifone.demo.emv.transaction.ReversalActivity;
import com.verifone.demo.emv.transaction.ReversalFristActivity;
import com.verifone.demo.emv.transaction.TransBasic;
import com.verifone.demo.emv.transaction.TransPrinter;
import com.verifone.demo.emv.transaction.TransactionParams;
import com.verifone.demo.emv.transaction.canvas_printer.PrinterDefine;
import com.verifone.demo.emv.transaction.canvas_printer.PrinterItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;
public class PrintRecpSale extends TransPrinter {
    private static final String TAG = "PrintRecpSale";
    float Pamount=0,Rfamount=0,Rvamount=0,Camount=0,Prthamount=0,prcmpamount=0,Pcbamount=0,Total;

    Transactiondata transactiondata;
    String  txntype="",utype="";
    DBHandler dbHandler;
    Handler handler;    // for UI
    TransBasic transBasic;
    private DBHandler.user_functions user_fun;
    public PrintRecpSale(Context context) {
        super(context);

    }

    public String getCardTypeMode(int im_pan) {
        String cardTypeMode;
        switch (im_pan) {
            case 1:
                cardTypeMode = "M";
                break;
            case 2:
                cardTypeMode = "S";
                break;
            case 5:
                cardTypeMode = "C";
                break;
            case 7:
                cardTypeMode = "Q";
                break;
            default:
                cardTypeMode = "M";
                break;
        }
        return cardTypeMode;
    }

    void showUI(String str) {
        Message msg = new Message();
        msg.getData().putString("msg", str);
        handler.sendMessage(msg);
    }
    // Here to "draw" your receipt with various PrinterItem
    @SuppressLint("SuspiciousIndentation")
    public void initializeData(ISO8583 require, ISO8583 response, HostInformation hostInformation, Bundle extraItems )
    {
        super.initializeData(require, response, hostInformation, extraItems);
        Log.d(TAG, "initializeData");
        SharedPreferences S=context.getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
        int copyIndex = extraItems.getInt("copyIndex");

        ISO8583msg sp=new ISO8583msg(context);
        sp.loadData();          // Terminal ID
        sp.loadData1();         // Merchant ID
        sp.loadDatamrname();    // Merchant Name
        sp.loadCurrencyData();  // Currency Type
        sp.IP_port_loadData();  // IP Port
        sp.loadMaddressData();  // Merchant Address
        sp.loadTmodeData();     // Terminal Mode
        sp.loadCommtypeData();  // Communication Type

        // DATE/TIME
//            String dateString = extraItems.getString(TXNREC.DATE) + extraItems.getString(TXNREC.TIME);

        String current_Date,current_Time;
        current_Date=new SimpleDateFormat("yy/MM/dd", Locale.getDefault()).format(new Date());
        current_Time=new SimpleDateFormat("HH:mm:ss ", Locale.getDefault()).format(new Date());


        Log.e(TAG, "Current date :" + current_Date);
        Log.e(TAG, "Current time :" + current_Time);
        PrinterItem.DATE_TIME.value.sValue = current_Date+"     "+current_Time;//Utility.getFormattedDateTime(dateString, "yyMMddHHmmss", "yy/MM/dd          HH:mm:ss");
        Log.e(TAG, "Current date AND Current time:   " +PrinterItem.DATE_TIME.value.sValue);


        try {

            printerItems = new ArrayList<>();


            if (S.getString("printtype", "none").equals("terminalinfo"))//
            {
                Log.d(TAG, "Amex,TERMINAL INFO IS SELECTED");

                printerItems.add(PrinterItem.terminalinfo);
                printerItems.add(PrinterItem.FEED_LINE);
                // printerItems.add(PrinterItem.DATE_TIME);//Date and Time
                printerItems.add(PrinterItem.FEED_LINE);
                printerItems.add(PrinterItem.LINE);
                printerItems.add(PrinterItem.FEED_LINE);

                PrinterItem.TERMINAL_ID.value.sValue = ISO8583msg.Ter_id;//hostInformation.terminalID;
                printerItems.add(PrinterItem.TERMINAL_ID);
                printerItems.add(PrinterItem.FEED_LINE);

                PrinterItem.MERCHANT_ID.value.sValue = ISO8583msg.Mer_id;
                printerItems.add(PrinterItem.MERCHANT_ID);
                printerItems.add(PrinterItem.FEED_LINE);

                PrinterItem.MERCHANT_NAME.value.sValue = ISO8583msg.Mer_name;
                ;//hostInformation.merchantName;
                printerItems.add(PrinterItem.MERCHANT_NAME);
                printerItems.add(PrinterItem.FEED_LINE);

                PrinterItem.Merchant_Adress.value.sValue = ISO8583msg.Mer_address;
                printerItems.add(PrinterItem.Merchant_Adress);
                printerItems.add(PrinterItem.FEED_LINE);

                PrinterItem.TERMINAL_mode.value.sValue = ISO8583msg.mode;
                printerItems.add(PrinterItem.TERMINAL_mode);
                printerItems.add(PrinterItem.FEED_LINE);

                PrinterItem.Currency_type.value.sValue = ISO8583msg.r_currency1;
                printerItems.add(PrinterItem.Currency_type);
                printerItems.add(PrinterItem.FEED_LINE);

                PrinterItem.Comm_type.value.sValue = ISO8583msg.commtype;
                printerItems.add(PrinterItem.Comm_type);
                printerItems.add(PrinterItem.FEED_LINE);
                printerItems.add(PrinterItem.FEED_LINE);


            } else if (S.getString("printtype", "none").equals("terminalsetup"))//
            {
                Log.d(TAG, "Amex,TERMINAL SET UP IS SELECTED ");

                printerItems.add(PrinterItem.terminalsetup);
                printerItems.add(PrinterItem.FEED_LINE);
                // printerItems.add(PrinterItem.DATE_TIME);//Date and Time
                printerItems.add(PrinterItem.FEED_LINE);
                printerItems.add(PrinterItem.LINE);
                printerItems.add(PrinterItem.FEED_LINE);

                PrinterItem.Mode.value.sValue = ISO8583msg.mode;//terminal Mode;
                printerItems.add(PrinterItem.Mode);
                printerItems.add(PrinterItem.FEED_LINE);

                PrinterItem.Ipaddress.value.sValue = ISO8583msg.ipadd;//hostInformation.terminal IP;
                printerItems.add(PrinterItem.Ipaddress);
                printerItems.add(PrinterItem.FEED_LINE);

                PrinterItem.Port.value.sValue = String.valueOf(ISO8583msg.portnum);
                printerItems.add(PrinterItem.Port);
                printerItems.add(PrinterItem.FEED_LINE);
                printerItems.add(PrinterItem.FEED_LINE);
            }
            //DETAIL REPORT..............................................
            else if (S.getString("printtype", "none").equals("detailreport"))//DETAIL REPORT
            {

                int check=Integer.parseInt(S.getString("trn", "none"));
                Log.d(TAG, "Amex,DETAIL REPORT IS SELECTED...."+check);
                SharedPreferences sharedPreferences = context.getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                transactiondata = new Transactiondata(sharedPreferences, context);
                dbHandler = new DBHandler(context);
                printerItems.add(PrinterItem.Detailreport);
                printerItems.add(PrinterItem.FEED_LINE);
                // printerItems.add(PrinterItem.DATE_TIME);//Date and Time
                PrinterItem.DATE_TIME.value.sValue = current_Date + "    " + current_Time;
                printerItems.add(PrinterItem.DATE_TIME);//Date and Time
                printerItems.add(PrinterItem.LINE);

                if(dbHandler.gettransactiondata().size()>0)
                {
                    dbHandler.gettransactiondata().get(check);

                    printerItems.add(PrinterItem.Dashenbach);
                    //printerItems.add(PrinterItem.FEED_LINE);
                    //dbHandler.gettransactiondata().get(trannum).dateandtime
                    PrinterItem.DATE_TIME.value.sValue = dbHandler.gettransactiondata().get(check).dateandtime;
                    printerItems.add(PrinterItem.DATE_TIME);//Date and Time
                    // printerItems.add(PrinterItem.FEED_LINE);

                    PrinterItem.TERMINAL_ID.value.sValue = dbHandler.gettransactiondata().get( check).field_41;
                    Log.e(TAG, "Terminal ID................  " +dbHandler.gettransactiondata().get( check).field_41);
                    printerItems.add(PrinterItem.TERMINAL_ID);

                    // printerItems.add(PrinterItem.FEED_LINE);
                    //String.valueOf(trannum)
                    PrinterItem.TRNSNO.value.sValue = String.valueOf(check+1);
                    printerItems.add(PrinterItem.TRNSNO);

                    //printerItems.add(PrinterItem.FEED_LINE);
                    //PrinterItem.CASHIER.value.sValue = dbHandler.gettransactiondata().get(dbHandler.gettransactiondata().size() - check).getCashier();
                    //printerItems.add(PrinterItem.CASHIER);
                    ISO8583msg.loadDatamrname();
                    String Mrname= ISO8583msg.Mer_name;
                    PrinterItem.MERCHANT_NAME.value.sValue = Mrname;
                    Log.e(TAG, "Merchant Name................  " +Mrname);
                    //hostInformation.merchantName;
                    printerItems.add(PrinterItem.MERCHANT_NAME);

                    //printerItems.add(PrinterItem.FEED_LINE);
                    PrinterItem.CARD_NO.value.sValue = Utility.fixCardNoWithMask(dbHandler.gettransactiondata().get(check).getField_02());
                    printerItems.add(PrinterItem.CARD_NO);

                    //printerItems.add(PrinterItem.FEED_LINE);
                    PrinterItem.TRANS_TYPE.value.sValue = dbHandler.gettransactiondata().get(check).getTxn_type();
                    Log.e(TAG, "TRANSTYPE................  " +dbHandler.gettransactiondata().get(check).getTxn_type());

                    printerItems.add(PrinterItem.TRANS_TYPE);

                    //printerItems.add(PrinterItem.FEED_LINE);
                    PrinterItem.AMOUNT.value.sValue = dbHandler.gettransactiondata().get(check).getCurrency()
                            +" "+Utility.getReadableAmount(dbHandler.gettransactiondata().get(check).getField_04());
                    printerItems.add(PrinterItem.AMOUNT);
                    //printerItems.add(PrinterItem.FEED_LINE);

                    printerItems.add(PrinterItem.LINE);

                }else
                {
                    Log.d(TAG, "DO TRANSACTION!, NO TXN RECORDED");
                    showUI("DO TRANSACTION!, NO TXN RECORDED");
                    //ToastUtil.toastOnUiThread(PrintRecpSale.this, "DO TRANSACTION!, NO TXN RECORDED");
                }



            }
            else if ( S.getString("printtype", "none").equals("detailreport_1"))//DETAIL REPORT
            {
                int check=Integer.parseInt(S.getString("trn", "none"));

                Log.d(TAG, "Amex,detailreport_1 value of check...."+check);

                SharedPreferences sharedPreferences = context.getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                transactiondata = new Transactiondata(sharedPreferences, context);

                dbHandler = new DBHandler(context);
                if(dbHandler.gettransactiondata().size()>0)
                {
                    dbHandler.gettransactiondata().get(check);

                    printerItems.add(PrinterItem.Dashenbach);
                    //printerItems.add(PrinterItem.FEED_LINE);
                    //dbHandler.gettransactiondata().get(trannum).dateandtime
                    PrinterItem.DATE_TIME.value.sValue = dbHandler.gettransactiondata().get(check).dateandtime;
                    printerItems.add(PrinterItem.DATE_TIME);//Date and Time
                    //printerItems.add(PrinterItem.FEED_LINE);

                    PrinterItem.TERMINAL_ID.value.sValue = dbHandler.gettransactiondata().get(check).field_41;
                    Log.e(TAG, "Terminal ID................  " +dbHandler.gettransactiondata().get(check).field_41);
                    printerItems.add(PrinterItem.TERMINAL_ID);

                    //printerItems.add(PrinterItem.FEED_LINE);
                    //String.valueOf(trannum)
                    PrinterItem.TRNSNO.value.sValue = String.valueOf(check+1);
                    printerItems.add(PrinterItem.TRNSNO);

                    //printerItems.add(PrinterItem.FEED_LINE);
                    //PrinterItem.CASHIER.value.sValue = dbHandler.gettransactiondata().get(dbHandler.gettransactiondata().size() - check).getCashier();
                    //printerItems.add(PrinterItem.CASHIER);
                    ISO8583msg.loadDatamrname();
                    String Mrname= ISO8583msg.Mer_name;
                    PrinterItem.MERCHANT_NAME.value.sValue = Mrname;
                    Log.e(TAG, "Merchant Name................  " +Mrname);
                    //hostInformation.merchantName;
                    printerItems.add(PrinterItem.MERCHANT_NAME);

                    //printerItems.add(PrinterItem.FEED_LINE);
                    PrinterItem.CARD_NO.value.sValue = Utility.fixCardNoWithMask(dbHandler.gettransactiondata().get(check).getField_02());
                    printerItems.add(PrinterItem.CARD_NO);

                    //printerItems.add(PrinterItem.FEED_LINE);
                    PrinterItem.TRANS_TYPE.value.sValue = dbHandler.gettransactiondata().get(check).getTxn_type();
                    Log.e(TAG, "TRANSTYPE................  " +dbHandler.gettransactiondata().get(check).getTxn_type());

                    printerItems.add(PrinterItem.TRANS_TYPE);

                    //printerItems.add(PrinterItem.FEED_LINE);
                    PrinterItem.AMOUNT.value.sValue = dbHandler.gettransactiondata().get(check).getCurrency()
                            +" "+Utility.getReadableAmount(dbHandler.gettransactiondata().get(check).getField_04());
                    printerItems.add(PrinterItem.AMOUNT);
                    // printerItems.add(PrinterItem.FEED_LINE);

                    printerItems.add(PrinterItem.LINE);

                }else
                {
                    Log.d(TAG, "DO TRANSACTION!, NO TXN RECORDED");
                    showUI("DO TRANSACTION!, NO TXN RECORDED");
                    //ToastUtil.toastOnUiThread(PrintRecpSale.this, "DO TRANSACTION!, NO TXN RECORDED");
                }

            }

            else if ( S.getString("printtype", "none").equals("detailreport_2"))//DETAIL REPORT
            {
                int check=Integer.parseInt(S.getString("trn", "none"));

                Log.d(TAG, "Amex,detailreport_2 value of check...."+check);

                SharedPreferences sharedPreferences = context.getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                transactiondata = new Transactiondata(sharedPreferences, context);

                dbHandler = new DBHandler(context);
                if(dbHandler.gettransactiondata().size()>0)
                {
                    dbHandler.gettransactiondata().get(check);

                    printerItems.add(PrinterItem.Dashenbach);
                    // printerItems.add(PrinterItem.FEED_LINE);
                    //dbHandler.gettransactiondata().get(trannum).dateandtime
                    PrinterItem.DATE_TIME.value.sValue = dbHandler.gettransactiondata().get(check).dateandtime;
                    printerItems.add(PrinterItem.DATE_TIME);//Date and Time
                    // printerItems.add(PrinterItem.FEED_LINE);

                    PrinterItem.TERMINAL_ID.value.sValue = dbHandler.gettransactiondata().get(check).field_41;
                    Log.e(TAG, "Terminal ID................  " +dbHandler.gettransactiondata().get(check).field_41);
                    printerItems.add(PrinterItem.TERMINAL_ID);

                    //printerItems.add(PrinterItem.FEED_LINE);
                    //String.valueOf(trannum)
                    PrinterItem.TRNSNO.value.sValue = String.valueOf(check+1);
                    printerItems.add(PrinterItem.TRNSNO);

                    ISO8583msg.loadDatamrname();
                    String Mrname= ISO8583msg.Mer_name;
                    PrinterItem.MERCHANT_NAME.value.sValue = Mrname;
                    Log.e(TAG, "Merchant Name................  " +Mrname);
                    //hostInformation.merchantName;
                    printerItems.add(PrinterItem.MERCHANT_NAME);

                    //printerItems.add(PrinterItem.FEED_LINE);
                    PrinterItem.CARD_NO.value.sValue = Utility.fixCardNoWithMask(dbHandler.gettransactiondata().get( check).getField_02());
                    printerItems.add(PrinterItem.CARD_NO);

                    // printerItems.add(PrinterItem.FEED_LINE);
                    PrinterItem.TRANS_TYPE.value.sValue = dbHandler.gettransactiondata().get(check).getTxn_type();
                    Log.e(TAG, "TRANSTYPE................  " +dbHandler.gettransactiondata().get( check).getTxn_type());

                    printerItems.add(PrinterItem.TRANS_TYPE);

                    // printerItems.add(PrinterItem.FEED_LINE);
                    PrinterItem.AMOUNT.value.sValue = dbHandler.gettransactiondata().get(check).getCurrency()
                            +" "+Utility.getReadableAmount(dbHandler.gettransactiondata().get(check).getField_04());
                    printerItems.add(PrinterItem.AMOUNT);
                    //printerItems.add(PrinterItem.FEED_LINE);

                    printerItems.add(PrinterItem.LINE);
                    printerItems.add(PrinterItem.FEED_LINE);

                }else
                {
                    Log.d(TAG, "DO TRANSACTION!, NO TXN RECORDED");
                    showUI("DO TRANSACTION!, NO TXN RECORDED");
                    //ToastUtil.toastOnUiThread(PrintRecpSale.this, "DO TRANSACTION!, NO TXN RECORDED");
                }

            }
            //SUMMRY REPORT................................................................................
            else if (S.getString("printtype", "none").equals("summryreport") || S.getString("printtype", "none").equals("endoffday") || S.getString("printtype", "none").equals("settlement"))//
            {
                Log.d(TAG, "Amex,SUMMRY REPORT IS SELECTED ");
                SharedPreferences sharedPreferences = context.getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                transactiondata = new Transactiondata(sharedPreferences, context);
                // ISO8583 iso8385 = new ISO8583(sharedPreferences, context);
                dbHandler = new DBHandler(context);
                if(S.getString("printtype", "none").equals("summryreport") )
                {
                    printerItems.add(PrinterItem.Summryreport);

                } else if (S.getString("printtype", "none").equals("settlement")) {

                    printerItems.add(PrinterItem.Settlement);

                } else
                {
                    printerItems.add(PrinterItem.Endoffday);

                }
                printerItems.add(PrinterItem.FEED_LINE);
                // printerItems.add(PrinterItem.DATE_TIME);//Date and Time
                PrinterItem.DATE_TIME.value.sValue = current_Date + "   " + current_Time;
                printerItems.add(PrinterItem.DATE_TIME);//Date and Time
                printerItems.add(PrinterItem.FEED_LINE);
                ISO8583msg.loadCurrencyData();
                PrinterItem.Txn_Total.value.sValue = "TRANSACTIONS AMOUNT ("+ ISO8583msg.r_currency1+")";
                printerItems.add(PrinterItem.Txn_Total);
                printerItems.add(PrinterItem.FEED_LINE);

                if (dbHandler.gettransactiondata().size() > 0)
                {
                    Log.d(TAG, "Amex,SUMMRY REPORT1 ");

                    Pamount=common.Pamount;
                    Rvamount=common.Rvamount;
                    prcmpamount=common.prcmpamount;
                    Camount=common.Camount;
                    Pcbamount=common.Pcbamount;
                    Prthamount=common.Prthamount;
                    Rfamount=common.Rfamount;


                    Log.d(TAG, "Amex,SUMMRY REPORT END LOOP ");
                } else
                {
                    Log.d(TAG, "DO TRANSACTION!, NO TXN RECORDED");

                }

                if(ISO8583msg.mode.equals("Merchant"))
                {
                    Total=Pamount - Rfamount + Prthamount + prcmpamount + Pcbamount - Rvamount;

                    PrinterItem.PURCHASE.value.sValue =String.valueOf(Pamount);
                    printerItems.add(PrinterItem.PURCHASE);
                    Log.d(TAG, " Merchant..Total......,"+ Total);
                }else
                {
                    Total=Camount - Rfamount + Prthamount + prcmpamount + Pcbamount - Rvamount;

                    PrinterItem.CASHADVANCE.value.sValue =String.valueOf(Camount);
                    printerItems.add(PrinterItem.CASHADVANCE);
                    Log.d(TAG, " Branch..Total......,"+ Total);
                }
                printerItems.add(PrinterItem.FEED_LINE);
                PrinterItem.REFUND.value.sValue = String.valueOf(Rfamount);
                printerItems.add(PrinterItem.REFUND);
                printerItems.add(PrinterItem.FEED_LINE);

                PrinterItem.PRE_AUTH.value.sValue = String.valueOf(Prthamount);
                printerItems.add(PrinterItem.PRE_AUTH);
                printerItems.add(PrinterItem.FEED_LINE);

                PrinterItem.PRE_AUTHCOMPLATION.value.sValue = String.valueOf(prcmpamount);
                printerItems.add(PrinterItem.PRE_AUTHCOMPLATION);
                printerItems.add(PrinterItem.FEED_LINE);

                PrinterItem.PCASHBACK.value.sValue = String.valueOf(Pcbamount);
                printerItems.add(PrinterItem.PCASHBACK);
                printerItems.add(PrinterItem.FEED_LINE);

                PrinterItem.REVERSAL.value.sValue = String.valueOf(Rvamount);
                printerItems.add(PrinterItem.REVERSAL);
                printerItems.add(PrinterItem.FEED_LINE);

                if(ISO8583msg.mode.equals("Merchant"))
                {
                    PrinterItem.TOTALNET.value.sValue =String.valueOf(Total);
                    printerItems.add(PrinterItem.TOTALNET);
                }else
                {
                    PrinterItem.TOTALNET.value.sValue =String.valueOf(Total);
                    printerItems.add(PrinterItem.TOTALNET);
                }
                printerItems.add(PrinterItem.FEED_LINE);

            }
            //directorylist.............................................
            else if(S.getString("printtype", "none").equals("directorylist"))//
            {
                Log.d(TAG, "Amex,Directorylist IS SELECTED");

                printerItems.add(PrinterItem.directorylist);
                printerItems.add(PrinterItem.line1);
                printerItems.add(PrinterItem.FEED_LINE);

                printerItems.add(PrinterItem.Admin);
                printerItems.add(PrinterItem.line1);
                printerItems.add(PrinterItem.FEED_LINE);
                printerItems.add(PrinterItem.FEED_LINE);

                printerItems.add(PrinterItem.Support);
                printerItems.add(PrinterItem.line1);
                printerItems.add(PrinterItem.FEED_LINE);
                printerItems.add(PrinterItem.FEED_LINE);

                printerItems.add(PrinterItem.Supervisor);
                printerItems.add(PrinterItem.line1);
                printerItems.add(PrinterItem.FEED_LINE);
                printerItems.add(PrinterItem.FEED_LINE);

                printerItems.add(PrinterItem.Cashier1);
                printerItems.add(PrinterItem.line1);
                printerItems.add(PrinterItem.FEED_LINE);
                printerItems.add(PrinterItem.FEED_LINE);
            }
//iSOFFLINE
            else if(S.getString("printtype", "none").equals("isoffline"))
            {

                SharedPreferences.Editor editor1=S.edit();
                if(S.getString("responcenull", "none").equals("responcenull"))//
                 {
                    editor1.putString("responcenull","");
                    editor1.commit();

                    Log.d(TAG, "Amex,No Response FAILER IS SELECTED");

                    printerItems.add(PrinterItem.responcenull);

                    printerItems.add(PrinterItem.FEED_LINE);

                    PrinterItem.DATE_TIME.value.sValue = current_Date+"  "+current_Time;
                    printerItems.add(PrinterItem.DATE_TIME);//Date and Time
                    printerItems.add(PrinterItem.FEED_LINE);
                    //respocode
                    // PrinterItem.respocode.value.sValue = "";
                    //printerItems.add(PrinterItem.respocode);
                    printerItems.add(PrinterItem.FEED_LINE);
//response message
                    PrinterItem.r_resp_msg.value.sValue = "No Response";
                    printerItems.add(PrinterItem.r_resp_msg);
                    printerItems.add(PrinterItem.FEED_LINE);
//Approstatus
                    PrinterItem.Approstatus.value.sValue = "DECLINED";
                    printerItems.add(PrinterItem.Approstatus);
                    printerItems.add(PrinterItem.FEED_LINE);

                }
                else if(!Transactiondata.ResponseFields.field39.equals("000"))
                {
                    Log.d(TAG, "AUTHORIZATION FAILURE /RESPONSE ERROR IS SELECTED..........");
                    printerItems.add(PrinterItem.responceerror);

                    printerItems.add(PrinterItem.FEED_LINE);

                    PrinterItem.DATE_TIME.value.sValue = current_Date+"  "+current_Time;
                    printerItems.add(PrinterItem.DATE_TIME);//Date and Time
                    printerItems.add(PrinterItem.FEED_LINE);
                    //respocode
                    PrinterItem.respocode.value.sValue = Transactiondata.ResponseFields.field39;
                    printerItems.add(PrinterItem.respocode);
                    printerItems.add(PrinterItem.FEED_LINE);
//response message
                    PrinterItem.r_resp_msg.value.sValue = checkresponcecode(Transactiondata.ResponseFields.field39);
                    printerItems.add(PrinterItem.r_resp_msg);
                    printerItems.add(PrinterItem.FEED_LINE);
//Approstatus
                    PrinterItem.Approstatus.value.sValue = "DECLINED";
                    printerItems.add(PrinterItem.Approstatus);
                    printerItems.add(PrinterItem.FEED_LINE);

                }else{

                    Log.d(TAG, "AMEX,COMMUNICATION FAILURE");

                    printerItems.add(PrinterItem.Communicationfail);

                    printerItems.add(PrinterItem.FEED_LINE);

                    PrinterItem.DATE_TIME.value.sValue = current_Date+"  "+current_Time;
                    printerItems.add(PrinterItem.DATE_TIME);//Date and Time
                    printerItems.add(PrinterItem.FEED_LINE);
                    //respocode
                    PrinterItem.respocode.value.sValue = "....";
                    printerItems.add(PrinterItem.respocode);
                    printerItems.add(PrinterItem.FEED_LINE);
//response message
                    PrinterItem.r_resp_msg.value.sValue = checkresponcecode(Transactiondata.ResponseFields.field39);
                    printerItems.add(PrinterItem.r_resp_msg);
                    printerItems.add(PrinterItem.FEED_LINE);
//Approstatus
                    PrinterItem.Approstatus.value.sValue = "DECLINED";
                    printerItems.add(PrinterItem.Approstatus);
                    printerItems.add(PrinterItem.FEED_LINE);
                }

            }
            else if(S.getString("printtype", "none").equals("specificreport")&& !S.getString("trn", "none").equals("none"))
            {
                int trannum=Integer.parseInt(S.getString("trn", "0"));
                DBHandler dbHandler=new DBHandler(context);
                Transactiondata tr=new Transactiondata(S,context);
                dbHandler.gettransactiondata().get(trannum);
                //Get some extra infos about this receipt, like the index of copy and if it is reprint
                Log.d(TAG, "Amex,Specificreport IS SELECTED");
                String tmp;
                Boolean isReprint = extraItems.getBoolean("reprint", false);

                // A List to put PrintItems, printCanvas will resolve this list to draw receipt
                printerItems = new ArrayList<>();

                // The LOGO on the top of receipt, set stype to align center.
                PrinterItem.LOGO.title.sValue = "amharic_gbe_black_logo.png" ;
                //PrinterItem.LOGO.title.sValue = "verifone_logo.jpg";
                PrinterItem.LOGO.title.style = PrinterDefine.PStyle_align_center;
                printerItems.add(PrinterItem.LOGO);

                //tmp = getResources().getString(R.string.prn_merchantCopy);
                //editor.putString("printtype", "customercopy");
                switch (copyIndex) {
                    case 1:
                        tmp = getResources().getString(R.string.prn_merchantCopy); //"商户存根                           请妥善保管";
                        break;
                    case 2:
                        tmp = getResources().getString(R.string.prn_cardholderCopy); //"持卡人存根                         请妥善保管";
                        break;
                    case 3:
                    default:
                        tmp = getResources().getString(R.string.prn_bankCopy); //"银行存根                           请妥善保管";
                        break;
                }
                if(S.getString("printtype", "none").equals("specificreport") && S.getString("printtype", "none").equals("customercopy"))
                {
                    Log.d(TAG, "Amexxx,CUSTOMER COPY IS SELECTED");
                    tmp = getResources().getString(R.string.prn_merchantCopy);
                    PrinterItem.SUBTITLE.value.sValue =tmp;
                    //"[CUSTOMER COPY]";
                }
                PrinterItem.SUBTITLE.value.sValue = tmp+"[REPRINT]";
                printerItems.add(PrinterItem.SUBTITLE);
                printerItems.add(PrinterItem.LINE);

                PrinterItem.DATE_TIME.value.sValue = dbHandler.gettransactiondata().get(trannum).dateandtime;
                printerItems.add(PrinterItem.DATE_TIME);//Date and Time
                //printerItems.add(PrinterItem.FEED_LINE);

                // TERMINAL NO
                ISO8583msg.loadData();
                String Tid= ISO8583msg.Ter_id;
                PrinterItem.TERMINAL_ID.value.sValue = Tid;
                Log.e(TAG, "Terminal ID................  " +Tid);
                printerItems.add(PrinterItem.TERMINAL_ID);
                // MERCHANT NO.
                String Mrid= ISO8583msg.SPDH_OPTIONAL_DATA_FIELD_VARS.d_retailer_Id;
                PrinterItem.MERCHANT_ID.value.sValue = Mrid;
                Log.e(TAG, "Merchant ID................  " +Mrid);
                printerItems.add(PrinterItem.MERCHANT_ID);


                // CASHIER  NAME
                //  PrinterItem.MERCHANT_NAME.value.sValue = dbHandler.gettransactiondata().get(trannum).getCashier();
                // printerItems.add(PrinterItem.MERCHANT_NAME);
                //printerItems.add(PrinterItem.FEED_LINE);

                ISO8583msg.loadDatamrname();
                String Mrname= ISO8583msg.Mer_name;
                PrinterItem.MERCHANT_NAME.value.sValue = Mrname;
                Log.e(TAG, "Merchant Name................  " +Mrname);
                //hostInformation.merchantName;
                printerItems.add(PrinterItem.MERCHANT_NAME);

                PrinterItem.TRNSNO.value.sValue =  String.valueOf(trannum+1);
                Log.e(TAG, "TRNS#  "+String.valueOf(dbHandler.getdatasize()));
                printerItems.add(PrinterItem.TRNSNO);



                PrinterItem.Appcode.value.sValue = dbHandler.gettransactiondata().get(trannum).getField_38();
                printerItems.add(PrinterItem.Appcode);
                Log.e(TAG, "Appcode  "+dbHandler.gettransactiondata().get(trannum).getField_38());

                PrinterItem.RRN.value.sValue =dbHandler.gettransactiondata().get(trannum).getField_37() ;
                Log.e(TAG, "RRN...."+dbHandler.gettransactiondata().get(trannum).getField_37() );
                printerItems.add(PrinterItem.RRN);

                // CARD NO.
                //            String pansn = extraItems.getString(TXNREC.PANSN);
                String pan = TransactionParams.getInstance().getPan();
                PrinterItem.CARD_NO.title.sValue = getResources().getString(R.string.cardno);
                PrinterItem.CARD_NO.value.sValue = Utility.fixCardNoWithMask(dbHandler.gettransactiondata().get(trannum).getField_02());
                printerItems.add(PrinterItem.CARD_NO);
                //printerItems.add(PrinterItem.FEED_LINE);
//CARD HOLDER
                PrinterItem.CARD_HOLDER.value.sValue = dbHandler.gettransactiondata().get(trannum).getCard_Holder();
                printerItems.add(PrinterItem.CARD_HOLDER);
                Log.e(TAG, "CardHolder....."+dbHandler.gettransactiondata().get(trannum).getCard_Holder());
                //EX
                PrinterItem.CARD_VALID.value.sValue = maskAll(dbHandler.gettransactiondata().get(trannum).getField_14());
                printerItems.add(PrinterItem.CARD_VALID);
                //printerItems.add(PrinterItem.FEED_LINE);

                //TRNS TYPE
                PrinterItem.TRANS_TYPE.value.sValue = dbHandler.gettransactiondata().get(trannum).getTxn_type();
                if (dbHandler.gettransactiondata().get(trannum).getTxn_type().equals("PURCHASE"))
                {
                    PrinterItem.TRANS_TYPE.value.sValue = "SALE";
                }
                Log.e(TAG, "PrintReceptSale trans type....."+TransBasic.Txn_type);

                printerItems.add(PrinterItem.TRANS_TYPE);
                //printerItems.add(PrinterItem.FEED_LINE);

//aidlable cup

                PrinterItem.CUP.value.sValue = dbHandler.gettransactiondata().get(trannum).getCup();
                printerItems.add(PrinterItem.CUP);
                // printerItems.add(PrinterItem.FEED_LINE);


//aid
                PrinterItem.AID.value.sValue = dbHandler.gettransactiondata().get(trannum).getAid();
                printerItems.add(PrinterItem.AID);
                //printerItems.add(PrinterItem.FEED_LINE);

                String retamount = TransactionParams.getInstance().getTransactionAmount();
                PrinterItem.AMOUNT.value.sValue = dbHandler.gettransactiondata().get(trannum).getCurrency()
                        +" "+Utility.getReadableAmount(dbHandler.gettransactiondata().get(trannum).getField_04());
                printerItems.add(PrinterItem.AMOUNT);
                printerItems.add(PrinterItem.LINE);
                // printerItems.add(PrinterItem.FEED_LINE);

//respocode
                PrinterItem.respocode.value.sValue = dbHandler.gettransactiondata().get(trannum).getField_39();;
                printerItems.add(PrinterItem.respocode);
                //printerItems.add(PrinterItem.FEED_LINE);
//Approstatus
                //PrinterItem.Approstatus.value.sValue = Transactiondata.Subfieldresponse.g;
                Log.d(TAG, "Txn_type Approstatus  "+TransBasic.Txn_type);

                PrinterItem.Approstatus.value.sValue = "APPROVED";
                printerItems.add(PrinterItem.Approstatus);
                //printerItems.add(PrinterItem.FEED_LINE);

                //TVR
                PrinterItem.TVR.value.sValue = dbHandler.gettransactiondata().get(trannum).getTvr();// extraItems.getString(TXNREC.ISSBANKNAME).trim();
                Log.e(TAG, "tvr   "+TransBasic.tvr);
                printerItems.add(PrinterItem.TVR);

//Sign
                /*PrinterItem.E_SIGN.value.sValue = TransactionParams.getInstance().getEsignData();
                printerItems.add(PrinterItem.E_SIGN);*/
                PrinterItem.E_SIGN.value.sValue = dbHandler.gettransactiondata().get(trannum).getSign();
                Log.e(TAG, "E_SIGN   "+dbHandler.gettransactiondata().get(trannum).getSign());

                TransactionParams.getInstance().setEsignData(PrinterItem.E_SIGN.value.sValue);
                PrinterItem.E_SIGN.value.sValue= TransactionParams.getInstance().getEsignData();

                printerItems.add(PrinterItem.E_SIGN);
                Log.e(TAG, "E_SIGN 1  "+TransactionParams.getInstance().getEsignData());
                // REPRINT
                if (isReprint) {
                    printerItems.add(PrinterItem.RE_PRINT_NOTE);
                }

                PrinterItem.BARCODE_1.value.sValue = getResources().getString(R.string.prn_barcode);

                //printerItems.add(PrinterItem.FEED);
                printerItems.add(PrinterItem.BARCODE_1);
                // printerItems.add(PrinterItem.FEED);
                printerItems.add(PrinterItem.FEED);
                PrinterItem.QRCODE_1.value.sValue = getResources().getString(R.string.prn_qrcode2);
                printerItems.add(PrinterItem.QRCODE_1);

                //PrinterItem.QRCODE_2.value.sValue = getResources().getString(R.string.prn_qrcode1);
                //printerItems.add(PrinterItem.QRCODE_2);
                printerItems.add(PrinterItem.LINE);

                // PrinterItem.COMMENT_1.value.sValue = getResources().getString(R.string.prn_comment1);
                printerItems.add(PrinterItem.COMMENT_1);
                //PrinterItem.COMMENT_1.value.sValue = getResources().getString(R.string.prn_comment2);
                printerItems.add(PrinterItem.COMMENT_2);
                // PrinterItem.COMMENT_1.value.sValue = getResources().getString(R.string.prn_comment3);
                printerItems.add(PrinterItem.COMMENT_3);


            }
//Phoneauth TRANSACTION
            else if(S.getString("printtype", "").equals("isphoneauth"))
            {
                Log.d(TAG, "PHONE AUTH "+S.getString("isphoneauth", ""));

                Log.d(TAG, "PHONE AUTH TRANSACTION IS SELECTED");
                String tmp;
                Boolean isReprint = extraItems.getBoolean("reprint", false);

                // A List to put PrintItems, printCanvas will resolve this list to draw receipt
                printerItems = new ArrayList<>();

                // The LOGO on the top of receipt, set stype to align center.
                PrinterItem.LOGO.title.sValue = "amharic_gbe_black_logo.png";
                //PrinterItem.LOGO.title.sValue = "verifone_logo.jpg";
                PrinterItem.LOGO.title.style = PrinterDefine.PStyle_align_center;
                printerItems.add(PrinterItem.LOGO);

                switch (copyIndex) {
                    case 1:
                        tmp = getResources().getString(R.string.prn_merchantCopy); //"商户存根                           请妥善保管";
                        break;
                    case 2:
                        tmp = getResources().getString(R.string.prn_cardholderCopy); //"持卡人存根                         请妥善保管";
                        break;
                    case 3:
                    default:
                        tmp = getResources().getString(R.string.prn_bankCopy); //"银行存根                           请妥善保管";
                        break;
                }

                //tmp = getResources().getString(R.string.prn_merchantCopy);


                Log.d(TAG, "ONLINE Amexxx ELSE ,CUSTOMER COPY IS SELECTED");
                PrinterItem.SUBTITLE.value.sValue = tmp;

                printerItems.add(PrinterItem.SUBTITLE);

                printerItems.add(PrinterItem.LINE);

                PrinterItem.DATE_TIME.value.sValue = current_Date+"     "+current_Time;
                printerItems.add(PrinterItem.DATE_TIME);//Date and Time

                //printerItems.add(PrinterItem.LINE);
                //printerItems.add(PrinterItem.FEED_LINE);
                // TERMINAL NO
                ISO8583msg.loadData();
                String Tid= ISO8583msg.Ter_id;
                PrinterItem.TERMINAL_ID.value.sValue = Tid;
                //hostInformation.terminalID;
                Log.e(TAG, "Terminal ID................  " +Tid);
                printerItems.add(PrinterItem.TERMINAL_ID);
                // MERCHANT NO.
                String Mrid= ISO8583msg.SPDH_OPTIONAL_DATA_FIELD_VARS.d_retailer_Id;
                PrinterItem.MERCHANT_ID.value.sValue = Mrid;
                Log.e(TAG, "Merchant ID................  " +Mrid);
                // hostInformation.merchantID;
                printerItems.add(PrinterItem.MERCHANT_ID);


                // CASHIER  NAME
                ISO8583msg.loadDatamrname();
                String Mrname= ISO8583msg.Mer_name;
                PrinterItem.MERCHANT_NAME.value.sValue = Mrname;
                Log.e(TAG, "Merchant Name................  " +Mrname);
                //hostInformation.merchantName;
                printerItems.add(PrinterItem.MERCHANT_NAME);
                //printerItems.add(PrinterItem.FEED_LINE);
                DBHandler dbHandler=new DBHandler(context);
                if(dbHandler.getdatasize()>0)
                {
                    PrinterItem.TRNSNO.value.sValue =  String.valueOf(dbHandler.getdatasize()-1);
                } else{
                    PrinterItem.TRNSNO.value.sValue = "1";
                }
                Log.e(TAG, "TRNS#  "+String.valueOf(dbHandler.getdatasize()));

                // extraItems.getString(TXNREC.ISSBANKNAME).trim();
                printerItems.add(PrinterItem.TRNSNO);

                  /*  if (!Objects.equals(Transactiondata.Subfieldresponse.h, ""))
                    {
                        PrinterItem.Appcode.value.sValue = Transactiondata.Subfieldresponse.F;
                        printerItems.add(PrinterItem.Appcode);

                    }
                    Log.e(TAG, "seqqqqquence  "+Transactiondata.Subfieldresponse.h);
                    if(!Transactiondata.Subfieldresponse.h.equals(""))
                    {
                        PrinterItem.SEQNUM.value.sValue = Transactiondata.Subfieldresponse.h.substring(0, Transactiondata.Subfieldresponse.h.length() - 1);
                    }*/

                if(TransBasic.Txn_Menu_Type.equals("Manualcard"))
                {
                    Log.e(TAG, "PrintReceptSale Pannumber Manualcard IS SELECTED....."+ ManualTestActivity.cardnumber);
                    String pan = ManualTestActivity.cardnumber;
                    PrinterItem.CARD_NO.title.sValue = getResources().getString(R.string.cardno);
                    PrinterItem.CARD_NO.value.sValue = Utility.fixCardNoWithMask(pan);

                    printerItems.add(PrinterItem.CARD_NO);
                    //printerItems.add(PrinterItem.FEED_LINE);
                }else{
                    Log.e(TAG, "PrintReceptSale Pannumber other .....");
                    String pan = TransactionParams.getInstance().getPan();
                    PrinterItem.CARD_NO.title.sValue = getResources().getString(R.string.cardno);
                    PrinterItem.CARD_NO.value.sValue = Utility.fixCardNoWithMask(pan);

                    printerItems.add(PrinterItem.CARD_NO);
                    //printerItems.add(PrinterItem.FEED_LINE);

                    Log.e(TAG, "PrintReceptSale Pannumber....."+Utility.fixCardNoWithMask(pan));
                }

                PrinterItem.TRANS_TYPE.value.sValue = "PHONEAUTH "+MenuActivity.txn_type1;
                Log.e(TAG, "PrintReceptSale trans type....."+MenuActivity.txn_type1);


                printerItems.add(PrinterItem.TRANS_TYPE);
                // printerItems.add(PrinterItem.FEED_LINE);

//aidlable cup

                PrinterItem.CUP.value.sValue = TransBasic.aidLabel1;
                printerItems.add(PrinterItem.CUP);
                //printerItems.add(PrinterItem.FEED_LINE);

//aid
                PrinterItem.AID.value.sValue = TransBasic.aid1;
                printerItems.add(PrinterItem.AID);
                //printerItems.add(PrinterItem.FEED_LINE);
                String retamount="";
                if(TransBasic.Txn_type.equals("REVERSAL"))
                {

                        retamount = ReversalActivity.user.getField_04();


                }else if(TransBasic.Txn_type.equals("BALANCE_INQUIRY"))
                {
                    retamount=Transactiondata.ResponseFields.field04;

                    if(retamount!=null){

                        PrinterItem.BALANCE.value.sValue=ISO8583msg.r_currency1+" "+Utility.getReadableAmount(retamount);
                    }
                    printerItems.add(PrinterItem.BALANCE);
                    Log.e(TAG, "BALANCE_INQUIRY.....  "+retamount);
                }
                else{
                    retamount = TransactionParams.getInstance().getTransactionAmount();

                }
                if (retamount != null && !retamount.isEmpty()&&!TransBasic.Txn_type.equals("BALANCE_INQUIRY")) {

                    //PrinterItem.AMOUNT.value.sValue = getResources().getString(R.string.prn_currency) + Utility.getReadableAmount(retamount);
                    PrinterItem.AMOUNT.value.sValue = ISO8583msg.r_currency1 + " " + Utility.getReadableAmount(retamount);
                    printerItems.add(PrinterItem.AMOUNT);
                }
                //printerItems.add(PrinterItem.LINE);
                //printerItems.add(PrinterItem.FEED_LINE);


//ARPC
                Log.e(TAG, "Txn_Menu_Type  "+ TransBasic.Txn_Menu_Type);
                if(TransBasic.Txn_Menu_Type.equals("Chip")|| TransBasic.Txn_Menu_Type.equals("ContactLess"))
                {
         /*  if (!Transactiondata.Subfieldresponse.Q.equals(""))
           {
               PrinterItem.ARPC.value.sValue = Transactiondata.Subfieldresponse.Q.substring(4, 20);
               Log.e(TAG, "Q field main" + Transactiondata.Subfieldresponse.Q);
           } else {
               PrinterItem.ARPC.value.sValue = "0000000000000000";
               // PrinterItem.ARPC.value.sValue = "B262C76612E7751";

           }*/
                    Log.e(TAG, "qfield" + "01004B262C76612E77513030".substring(4, 20));
                    printerItems.add(PrinterItem.ARPC);
                    // printerItems.add(PrinterItem.FEED_LINE);
                }else
                {


                }
                // printerItems.add(PrinterItem.FEED_LINE);
//respocode
                //  PrinterItem.respocode.value.sValue = Transactiondata.HEADER_VARSresponse.rresponse_Code;
                // printerItems.add(PrinterItem.respocode);
                //printerItems.add(PrinterItem.FEED_LINE);
//Approstatus
                if(TransBasic.Txn_type.equals("REVERSAL"))
                {
                    PrinterItem.Approstatus.value.sValue = "APPROVED";
                }else
                {
                    PrinterItem.Approstatus.value.sValue = "APPROVEDL";
                }
                printerItems.add(PrinterItem.Approstatus);
                //printerItems.add(PrinterItem.FEED_LINE);
                //TVR
                PrinterItem.TVR.value.sValue = TransBasic.tvr;// extraItems.getString(TXNREC.ISSBANKNAME).trim();
                Log.e(TAG, "tvr   "+TransBasic.tvr);
                printerItems.add(PrinterItem.TVR);
                printerItems.add(PrinterItem.FEED_LINE);
                printerItems.add(PrinterItem.FEED_LINE);

                PrinterItem.E_SIGN.value.sValue = TransactionParams.getInstance().getEsignData();
                printerItems.add(PrinterItem.E_SIGN);



                // REPRINT
                if (isReprint) {
                    printerItems.add(PrinterItem.RE_PRINT_NOTE);
                }
                printerItems.add(PrinterItem.LINE);


                PrinterItem.QRCODE_1.value.sValue = getResources().getString(R.string.prn_qrcode2);


                // printerItems.add(PrinterItem.FEED);
                //printerItems.add(PrinterItem.BARCODE_1);
                //printerItems.add(PrinterItem.FEED);
                //printerItems.add(PrinterItem.FEED);
                printerItems.add(PrinterItem.QRCODE_1);
                printerItems.add(PrinterItem.LINE);

                printerItems.add(PrinterItem.COMMENT_1);
                printerItems.add(PrinterItem.COMMENT_2);
                printerItems.add(PrinterItem.COMMENT_3);

            }
            //Normal online txn started
            else
            {
                Log.d(TAG, "GOOD........ONLINE TRANSACTION IS SELECTED");
                String tmp;
                Boolean isReprint = extraItems.getBoolean("reprint", false);

                // A List to put PrintItems, printCanvas will resolve this list to draw receipt
                printerItems = new ArrayList<>();

                // The LOGO on the top of receipt, set stype to align center.
                PrinterItem.LOGO.title.sValue = "amharic_gbe_black_logo.png";
                //PrinterItem.LOGO.title.sValue = "verifone_logo.jpg";
                PrinterItem.LOGO.title.style = PrinterDefine.PStyle_align_center;
                printerItems.add(PrinterItem.LOGO);

                switch (copyIndex) {
                    case 1:
                        tmp = getResources().getString(R.string.prn_merchantCopy); //"商户存根                           请妥善保管";
                        break;
                    case 2:
                        tmp = getResources().getString(R.string.prn_cardholderCopy); //"持卡人存根                         请妥善保管";
                        break;
                    case 3:
                    default:
                        tmp = getResources().getString(R.string.prn_bankCopy); //"银行存根                           请妥善保管";
                        break;
                }

                //tmp = getResources().getString(R.string.prn_merchantCopy);


                Log.d(TAG, "ONLINE Amexxx ELSE ,CUSTOMER COPY IS SELECTED");
                PrinterItem.SUBTITLE.value.sValue = tmp;

                printerItems.add(PrinterItem.SUBTITLE);

                printerItems.add(PrinterItem.LINE);

                PrinterItem.DATE_TIME.value.sValue = current_Date+"    "+current_Time;
                printerItems.add(PrinterItem.DATE_TIME);//Date and Time

                //printerItems.add(PrinterItem.LINE);
                //printerItems.add(PrinterItem.FEED_LINE);
                // TERMINAL NO
                ISO8583msg.loadData();
                String Tid= ISO8583msg.Ter_id;
                PrinterItem.TERMINAL_ID.value.sValue = Tid;
                //hostInformation.terminalID;
                Log.e(TAG, "Terminal ID................  " +Tid);
                printerItems.add(PrinterItem.TERMINAL_ID);
                // printerItems.add(PrinterItem.FEED_LINE);
                // MERCHANT NO.
                String Mrid= ISO8583msg.SPDH_OPTIONAL_DATA_FIELD_VARS.d_retailer_Id;
                PrinterItem.MERCHANT_ID.value.sValue = Mrid;
                Log.e(TAG, "Merchant ID................  " +Mrid);
                // hostInformation.merchantID;
                printerItems.add(PrinterItem.MERCHANT_ID);
                // printerItems.add(PrinterItem.FEED_LINE);

                // CASHIER  NAME
                ISO8583msg.loadDatamrname();
                String Mrname= ISO8583msg.Mer_name;
                PrinterItem.MERCHANT_NAME.value.sValue = Mrname;
                Log.e(TAG, "Merchant Name................  " +Mrname);
                //hostInformation.merchantName;
                printerItems.add(PrinterItem.MERCHANT_NAME);
                // printerItems.add(PrinterItem.FEED_LINE);

                DBHandler dbHandler=new DBHandler(context);
                if(dbHandler.getdatasize()>0)
                {
                    PrinterItem.TRNSNO.value.sValue =  String.valueOf(dbHandler.getdatasize());
                } else{
                    PrinterItem.TRNSNO.value.sValue = "1";
                }
                Log.e(TAG, "TRNS#  "+String.valueOf(dbHandler.getdatasize()));

                // extraItems.getString(TXNREC.ISSBANKNAME).trim();
                printerItems.add(PrinterItem.TRNSNO);
                // printerItems.add(PrinterItem.FEED_LINE);
                if (!Objects.equals(Transactiondata.ResponseFields.field38, ""))
                {
                    PrinterItem.Appcode.value.sValue = Transactiondata.ResponseFields.field38;
                    printerItems.add(PrinterItem.Appcode);

                }

                //RRN
                PrinterItem.RRN.value.sValue =Transactiondata.ResponseFields.field37 ;
                Log.e(TAG, "RRN...."+Transactiondata.ResponseFields.field37 );

                printerItems.add(PrinterItem.RRN);



                // CARD NO.
//  String pansn = extraItems.getString(TXNREC.PANSN);Txn_Menu_Type","Manualcard

                if(TransBasic.Txn_Menu_Type.equals("Manualcard"))
                {
                    Log.e(TAG, "PrintReceptSale Pannumber Manualcard IS SELECTED....."+ ManualTestActivity.cardnumber);
                    String pan = ManualTestActivity.cardnumber;
                    PrinterItem.CARD_NO.title.sValue = getResources().getString(R.string.cardno);
                    PrinterItem.CARD_NO.value.sValue = Utility.fixCardNoWithMask(pan);

                    printerItems.add(PrinterItem.CARD_NO);
                    //printerItems.add(PrinterItem.FEED_LINE);
                }else{
                    Log.e(TAG, "PrintReceptSale Pannumber other .....");
                    String pan = TransactionParams.getInstance().getPan();
                    PrinterItem.CARD_NO.title.sValue = getResources().getString(R.string.cardno);
                    PrinterItem.CARD_NO.value.sValue = Utility.fixCardNoWithMask(pan);
                    printerItems.add(PrinterItem.CARD_NO);
                    //printerItems.add(PrinterItem.FEED_LINE);
                    Log.e(TAG, "PrintReceptSale Pannumber....."+Utility.fixCardNoWithMask(pan));
                    //............................................................................
                    PrinterItem.CARD_HOLDER.value.sValue = TransBasic.cardholder;
                    printerItems.add(PrinterItem.CARD_HOLDER);
                    // printerItems.add(PrinterItem.FEED_LINE);
                    Log.e(TAG, "CardHolder....."+TransBasic.cardholder);

                }
                PrinterItem.CARD_VALID.value.sValue = maskAll(TransBasic.Ex_Date);
                printerItems.add(PrinterItem.CARD_VALID);
                //printerItems.add(PrinterItem.FEED_LINE);
                Log.e(TAG, "CardVALID....."+TransBasic.cardholder);

                //TRNS TYPE
                PrinterItem.TRANS_TYPE.value.sValue = TransBasic.Txn_type;
                if (TransBasic.Txn_type.equals("PURCHASE"))
                {
                    PrinterItem.TRANS_TYPE.value.sValue = "SALE";
                }
                Log.e(TAG, "PrintReceptSale trans type....."+TransBasic.Txn_type);


                printerItems.add(PrinterItem.TRANS_TYPE);
                //printerItems.add(PrinterItem.FEED_LINE);
                // 011454

//aidlable cup

                PrinterItem.CUP.value.sValue = TransBasic.aidLabel1;
                printerItems.add(PrinterItem.CUP);
                // printerItems.add(PrinterItem.FEED_LINE);

//aid
                PrinterItem.AID.value.sValue = TransBasic.aid1;
                printerItems.add(PrinterItem.AID);
                //printerItems.add(PrinterItem.FEED_LINE);
                String retamount="";
                if(TransBasic.Txn_type.equals("REVERSAL"))
                {
                    retamount = ReversalActivity.user.getField_04();

                }else if(TransBasic.Txn_type.equals("BALANCE_INQUIRY"))
                {
                    retamount=Transactiondata.ResponseFields.field04;

                    if(retamount!=null){

                       PrinterItem.BALANCE.value.sValue=ISO8583msg.r_currency1+" "+Utility.getReadableAmount(retamount);
                   }
                    printerItems.add(PrinterItem.BALANCE);
                    Log.d(TAG, "BALANCE_INQUIRY.....  "+retamount);
                }
                else{
                    retamount = TransactionParams.getInstance().getTransactionAmount();


                }
                if (retamount != null && !retamount.isEmpty()&&!TransBasic.Txn_type.equals("BALANCE_INQUIRY"))
                {

                    //PrinterItem.AMOUNT.value.sValue = getResources().getString(R.string.prn_currency) + Utility.getReadableAmount(retamount);
                    PrinterItem.AMOUNT.value.sValue = ISO8583msg.r_currency1 + " " + Utility.getReadableAmount(retamount);
                    printerItems.add(PrinterItem.AMOUNT);
                }
                printerItems.add(PrinterItem.LINE);


//respocode
                PrinterItem.respocode.value.sValue = Transactiondata.ResponseFields.field39;
                printerItems.add(PrinterItem.respocode);
                //printerItems.add(PrinterItem.FEED_LINE);
//Approstatus
                if(Transactiondata.ResponseFields.field39.equals("000"))
                {
                    PrinterItem.Approstatus.value.sValue = "APPROVED";
                }
                else if(TransBasic.Txn_type.equals("REVERSAL"))
                {
                    PrinterItem.Approstatus.value.sValue = "APPROVED";
                }else
                {
                    PrinterItem.Approstatus.value.sValue = "DECLINED";
                }
                printerItems.add(PrinterItem.Approstatus);
                //printerItems.add(PrinterItem.FEED_LINE);
                //TVR
                PrinterItem.TVR.value.sValue = TransBasic.tvr;// extraItems.getString(TXNREC.ISSBANKNAME).trim();
                Log.e(TAG, "tvr   "+TransBasic.tvr);
                printerItems.add(PrinterItem.TVR);
//Sign
                PrinterItem.E_SIGN.value.sValue = TransactionParams.getInstance().getEsignData();
                printerItems.add(PrinterItem.E_SIGN);



                // REPRINT
                if (isReprint) {
                    printerItems.add(PrinterItem.RE_PRINT_NOTE);
                }
                //printerItems.add(PrinterItem.LINE);

                // CARDHOLDER SIGNATURE



//            if (!printEsign()) {
//                printerItems.add(PrinterItem.FEED_LINE);
//                printerItems.add(PrinterItem.FEED_LINE);
//            }



                PrinterItem.BARCODE_1.value.sValue = getResources().getString(R.string.prn_barcode);

                //printerItems.add(PrinterItem.FEED);
                printerItems.add(PrinterItem.BARCODE_1);
                // printerItems.add(PrinterItem.FEED);
                printerItems.add(PrinterItem.FEED);
                PrinterItem.QRCODE_1.value.sValue = getResources().getString(R.string.prn_qrcode2);
                printerItems.add(PrinterItem.QRCODE_1);

                //PrinterItem.QRCODE_2.value.sValue = getResources().getString(R.string.prn_qrcode1);
                //printerItems.add(PrinterItem.QRCODE_2);
                printerItems.add(PrinterItem.LINE);

                //PrinterItem.COMMENT_1.value.sValue = getResources().getString(R.string.prn_comment1);
                printerItems.add(PrinterItem.COMMENT_1);
                //PrinterItem.COMMENT_1.value.sValue = getResources().getString(R.string.prn_comment2);
                printerItems.add(PrinterItem.COMMENT_2);
                // PrinterItem.COMMENT_1.value.sValue = getResources().getString(R.string.prn_comment3);
                printerItems.add(PrinterItem.COMMENT_3);



            }
        } catch (Exception e) {
            Log.e(TAG, "Exception :" + e.getMessage());
            for (StackTraceElement m : e.getStackTrace()
            ) {
                Log.e(TAG, "Exception :" + m);

            }
        }
    }
    public String checkresponcecode(String responce_code) {
        Log.d(TAG, "SSC@Amex, Call Rsponse Message");
        String r_resp_msg="Unable to read error code";
        switch (responce_code)
        {

            case "002": {
                r_resp_msg = "ATM performed a partial dispense";
                break;
            }
            case "005": {
                r_resp_msg = "System Error";
                break;
            }
            case "020":{
                r_resp_msg=" Negative Balance";
                break;
            }
            case "095":{
                r_resp_msg="Reconcile Error";
                break;
            }
            case "100":{
                r_resp_msg="Do not honor transaction";
                break;
            }
            case "101":{
                r_resp_msg="Expired Card";
                break;
            }
            case "103":{
                r_resp_msg="Call Issuer";
                break;
            }
            case "104":{
                r_resp_msg="Card is restricted";
                break;
            }
            case "105":{
                r_resp_msg="Call security";
                break;
            }
            case "106":{
                r_resp_msg="Excessive pin failures";
                break;
            }
            case "107":{
                r_resp_msg="Call Issuer";
                break;
            }
            case "109":{
                r_resp_msg="Invalid merchant ID";
                break;
            }
            case "110":{
                r_resp_msg="Cannot process amount";
                break;
            }
            case "111":{
                r_resp_msg="Invalid account - retry";
                break;
            }
            case "116":{
                r_resp_msg="Insufficient funds - retry";
                break;
            }
            case "117":{
                r_resp_msg="Incorrect Pin";
                break;
            }
            case "118":{
                r_resp_msg="Forced post, no account on file";
                break;
            }
            case "119":{
                r_resp_msg="Transaction not permitted by law";
                break;
            }
            case "120":{
                r_resp_msg="Not permitted";
                break;
            }
            case "121":{
                r_resp_msg="Withdrawal limit exceeded - retry";
                break;
            }
            case "123":{
                r_resp_msg="Limit reached for total number of transactions in cycle";
                break;
            }
            case "125":{
                r_resp_msg="Bad Card";
                break;
            }
            case "126":{
                r_resp_msg="Pin processing error";
                break;
            }
            case "127":{
                r_resp_msg="Pin processing error";
                break;
            }
            case "128":{
                r_resp_msg="Pin processing error";
                break;
            }
            case "200":{
                r_resp_msg="Invalid card";
                break;
            }
            case "201":{
                r_resp_msg="Card expired";
                break;
            }
            case "202":{
                r_resp_msg="Invalid card";
                break;
            }
            case "203":{
                r_resp_msg="Call security";
                break;
            }
            case "204":{
                r_resp_msg="Account restricted";
                break;
            }
            case "205":{
                r_resp_msg="Call security";
                break;
            }
            case "206":{
                r_resp_msg="Invalid Pin";
                break;
            }
            case "208":{
                r_resp_msg="Lost Card";
                break;
            }
            case "209":{
                r_resp_msg="Stolen Card";
                break;
            }
            case "248":{
                r_resp_msg="Successful authorization with partially approved amount";
                break;
            }
            case "901":{
                r_resp_msg="Invalid payment parameters";
                break;
            }
            case "902":{
                r_resp_msg="Invalid transaction - retry";
                break;
            }
            case "903":{
                r_resp_msg="Transaction needs to be entered again";
                break;
            }
            case "904":{
                r_resp_msg="The message received was not within standards";
                break;
            }
            case "905":{
                r_resp_msg="Issuing institution is unknown";
                break;
            }
            case "907":{
                r_resp_msg="Issuer inoperative";
                break;
            }

            case "908":{
                r_resp_msg="Issuing institution is unknown";
                break;
            }

            case "909":{
                r_resp_msg="System malfunction";
                break;
            }

            case "910":{
                r_resp_msg="Issuer inoperative";
                break;
            }
            case "911":{
                r_resp_msg="SmartVista FE has no knowledge of any attempt to either authorize or deny the transaction";
                break;
            }
            case "912":{
                r_resp_msg="Time out waiting for response";
                break;
            }
            case "913":{
                r_resp_msg="Duplicate transaction received";
                break;
            }
            case "914":{
                r_resp_msg="Can't find the original transaction";
                break;
            }
            case "915":{
                r_resp_msg="Reversal amount problem";
                break;
            }
            case "916":{
                r_resp_msg="Debts not found";
                break;
            }
            case "920":{
                r_resp_msg="Pin processing erro";
                break;
            }
            case "923":{
                r_resp_msg="Request in progress";
                break;
            }

        }
        return r_resp_msg;
    }
    private String maskAll(String num){
        String masked=num.replace(num,"xxxx");
        return masked;
    }
}