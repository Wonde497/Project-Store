package com.verifone.demo.emv.transaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.verifone.demo.emv.PrinterExActivity;
import com.verifone.demo.emv.Transactiondata;
import com.verifone.demo.emv.Utilities.Utility;
import com.verifone.demo.emv.basic.HostInformation;
import com.verifone.demo.emv.basic.ISO8583;
import com.verifone.demo.emv.transaction.canvas_printer.PrinterCanvas;
import com.verifone.demo.emv.transaction.canvas_printer.PrinterItem;
import com.vfi.smartpos.deviceservice.aidl.PrinterListener;

/**
 * Created by Simon on 2019/2/1.
 */

public class TransPrinter extends PrinterCanvas {

    static final String TAG = "TransPrinter";

    protected ISO8583 iso8583require;
    protected ISO8583 iso8583response;

    HostInformation hostInformation;
    static SharedPreferences sharedPreferences;
    // for print
    public TransPrinter(Context context) {
        super(context);
    }

    public void initializeData(ISO8583 require, ISO8583 response, HostInformation hostInformation, Bundle extraItems ){
        Log.d(TAG, "initializeData");
        PrinterItem.MERCHANT_NAME.value.sValue = "";
        PrinterItem.AMOUNT.value.sValue = "000200";
        PrinterItem.CARD_NO.value.sValue ="1324455668088978";

        //PrinterItem.MERCHANT_ID.value.sValue = "";
        this.iso8583require = require;
        this.iso8583response= response;
        this.hostInformation = hostInformation;

        for (PrinterItem p: PrinterItem.values()
             ) {
            p.restore();
        }

        if( null != hostInformation ) {
            if (null != hostInformation.merchantName) {
                if (hostInformation.merchantName.length() > 0) {
                    PrinterItem.MERCHANT_NAME.value.sValue = hostInformation.merchantName;
                }
            }
            if (null != hostInformation.merchantID) {
                if (hostInformation.merchantID.length() > 0) {
                    PrinterItem.MERCHANT_ID.value.sValue = "tranp";//hostInformation.merchantID;
                }
            }
            PrinterItem.MERCHANT_ID.value.sValue = "tranp";//hostInformation.merchantID;

            if (null != hostInformation.terminalID) {
                if (hostInformation.terminalID.length() > 0) {
                    PrinterItem.TERMINAL_ID.value.sValue = hostInformation.terminalID;
                }

            }
            if (null != hostInformation.description) {
                if (hostInformation.description.length() > 0) {
                    PrinterItem.HOST.value.sValue = hostInformation.description;
                }

            }
        }

        if (null != require) {
            PrinterItem.AMOUNT.value.sValue = require.getValue(ISO8583.ATTRIBUTE.Amount);
            PrinterItem.CARD_NO.value.sValue = Utility.fixCardNoWithMask( require.getValue(ISO8583.ATTRIBUTE.Track2) );
        }

        if ( null != response) {
            PrinterItem.DATE_TIME.value.sValue = response.getValue(ISO8583.ATTRIBUTE.Date) + " " + response.getValue(ISO8583.ATTRIBUTE.Time) ;
        }



    }


    public void print() {
     sharedPreferences = context.getSharedPreferences("Shared_data", Context.MODE_PRIVATE);

        Log.d(TAG, "print() type is " +sharedPreferences.getString("printtype", "none"));

        super.print(printerListener);

   if(sharedPreferences.getString("print2", "").equals("2") )
   {
    Log.d(TAG, "Hello Enter button is clicked here " );
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString("print2", "");
    editor.apply();

   }else {

    Intent intent = new Intent(context, PrinterExActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);

   }

    }

    PrinterListener printerListener = new PrinterListener.Stub() {
        @Override
        public void onFinish() throws RemoteException {
            Log.d(TAG, "Printer : Finish" );
        }

        @Override
        public void onError(int error) throws RemoteException {
            Log.e(TAG, "Printer error : " +error );
        }
    };

    public Resources getResources(){
        return context.getResources();
    }


}
