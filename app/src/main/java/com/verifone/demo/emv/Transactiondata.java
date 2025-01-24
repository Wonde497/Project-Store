package com.verifone.demo.emv;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.verifone.demo.emv.Public_data.ISO8583msg;
import com.verifone.demo.emv.Utilities.Utility;
import com.verifone.demo.emv.transaction.ManualTest_Exdate;
import com.verifone.demo.emv.transaction.TransBasic;
import com.verifone.demo.emv.transaction.TransactionParams;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class  Transactiondata  {
    static SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;
    static Context context;
    static String TAG = "Transactiondata";
    public  Transactiondata(SharedPreferences sharedPrefere,Context cont){
        sharedPreferences=sharedPrefere;
        editor=sharedPreferences.edit();

        context=cont;
    }



    public static class GLobalFields {
        public static   String MTI="";
        public static   String PrimaryBitmap="";
        public static   String Field02="";//cardnumber
        public static   String Field03="";
        public static   String Field04="";//transaction amount
        public static   String Field05="";
        public static   String Field07="";//transaction time
        public static   String Field11="000000";//stan increament for every transaction
        public static   String Field12="";//stan increament for every transaction
        public static   String Field14="";//expiredate
        public static   String Field15="";
        public static   String Field22="";
        public static   String Field24="";
        public static   String Field25="00";
        public static   String Field35="";//track2data
        public static   String Field41="";//terminalid
        public static   String Field42="";//merchantid
        public static   String Field49="230";//currency
        public static   String Field52="";//pinblock
        public static   String Field55="";
        public static   String Field64="";





    }






    public static void assign() {
        String manualCardTransactionType = Supervisor_menu_activity.manual_txn_type1;
        SharedPreferences sharedPreferences2 = context.getSharedPreferences("Shared_data", Context.MODE_PRIVATE);

        ISO8583msg sp = new ISO8583msg(context);
        sp.loadData();//terminal id
        sp.loadData1();//merchant id
        sp.loadCurrencyData();//currency type

        GLobalFields.Field02=TransBasic.savedPan;
        GLobalFields.Field03 = "000000";


        if(!TransBasic.Txn_Menu_Type.equals("Manualcard") && TransBasic.Txn_type.equals("PURCHASE")){
                GLobalFields.MTI="0200";

                if(TransBasic.isonline){
                    GLobalFields.PrimaryBitmap="7234058020C09200";
                    GLobalFields.Field52=Utility.byte2HexStr(TransBasic.savedPinblock);
                }  else{
                    GLobalFields.PrimaryBitmap="7234058020C08200";
                }
                GLobalFields.Field03 = "000000";
                GLobalFields.Field22 = "051";
                GLobalFields.Field24="200";
        }else if (TransBasic.Txn_type.equals("CASH_ADVANCE")) {
                GLobalFields.MTI="0200";
                if(TransBasic.isonline){
                    GLobalFields.PrimaryBitmap="7234058020C09200";
                    GLobalFields.Field52=Utility.byte2HexStr(TransBasic.savedPinblock);
                }else{
                    GLobalFields.PrimaryBitmap="7234058020C08200";
                }
                GLobalFields.Field24="200";
                GLobalFields.Field03="010000";
        }else if (!TransBasic.Txn_Menu_Type.equals("Manualcard") && TransBasic.Txn_type.equals("REVERSAL")) {
                GLobalFields.MTI="0400";
                GLobalFields.PrimaryBitmap="7230058028C08200";
                GLobalFields.Field24="400";

        }else if ( !TransBasic.Txn_Menu_Type.equals("Manualcard") && TransBasic.Txn_type.equals("BALANCE_INQUIRY"))  {
                if(TransBasic.isonline){
                    GLobalFields.PrimaryBitmap="7234058020C09200";
                    GLobalFields.Field52=Utility.byte2HexStr(TransBasic.savedPinblock);
                }else{
                    GLobalFields.PrimaryBitmap="7234058020C08200";
                }
                 GLobalFields.MTI="0100";
                 GLobalFields.Field22="051";
                 GLobalFields.Field24="100";
                 GLobalFields.Field04= "000000000000";
                 GLobalFields.Field03= "310000";
        }else if (TransBasic.Txn_Menu_Type.equals("Manualcard") && (manualCardTransactionType.equals("BALANCE_INQUIRY") && TransBasic.Txn_type.equals("BALANCE_INQUIRY")) ) {
                Log.d(TAG, "Manual Card Transaction Type " + manualCardTransactionType);

                 GLobalFields.PrimaryBitmap = "7234058000C08000";
                 GLobalFields.Field22 = "016";
                 GLobalFields.Field24="100";
                 GLobalFields.Field04= "000000000000";
                 GLobalFields.Field03= "310000";
        }else if (TransBasic.Txn_Menu_Type.equals("Manualcard") && (manualCardTransactionType.equals("PURCHASE") && TransBasic.Txn_type.equals("PURCHASE"))) {

                Log.d(TAG, "Manual Card Transaction Type " + manualCardTransactionType);
                GLobalFields.PrimaryBitmap = "7234058000C08000";
                GLobalFields.MTI="0200";
                GLobalFields.Field22 = "016";
                GLobalFields.Field24="200";
                GLobalFields.Field03= "000000";
        }else if (TransBasic.Txn_Menu_Type.equals("Manualcard") && (manualCardTransactionType.equals("REVERSAL") && TransBasic.Txn_type.equals("REVERSAL"))) {

                Log.d(TAG, "Manual Card Transaction Type " + manualCardTransactionType);
                GLobalFields.PrimaryBitmap = "7234058000C08000";
                GLobalFields.MTI="0400";
                GLobalFields.Field24="400";
                GLobalFields.Field22 = "016";
        }else {
                Log.d(TAG, "Error Transaction Type ");
        }








        if (!TransBasic.Txn_type.equals("BALANCE_INQUIRY")) {
            GLobalFields.Field04= TransactionParams.getInstance().getTransactionAmount();
          }
        GLobalFields.Field07=new SimpleDateFormat("MMddhhmmss", Locale.getDefault()).format(new Date());
        String stan = sharedPreferences.getString("STAN","1");
        int st= Integer.parseInt(stan)+1;
        editor.putString("STAN", String.valueOf(st));
        editor.commit();
           /* int stan = sharedPreferences.getInt("STAN",1);
            stan=stan+1;
          editor.putInt("STAN", stan);
          editor.commit();*/
        GLobalFields.Field11 = fillgapsequence(String.valueOf(st),6);
        GLobalFields.Field12=new SimpleDateFormat("yyMMddhhmmss", Locale.getDefault()).format(new Date());
        Log.d(TAG, "Field12222.........:" + GLobalFields.Field12);

        GLobalFields.Field14= TransBasic.Ex_Date+"01";
        if(Supervisor_menu_activity.manual_txn_type1.equals("PURCHASE") || Supervisor_menu_activity.manual_txn_type1.equals("BALANCE_INQUIRY")||Supervisor_menu_activity.manual_txn_type1.equals("REVERSAL")){
            String MM=ManualTest_Exdate.expiredate.substring(0,2);
            String YY=ManualTest_Exdate.expiredate.substring(2,4);
            String YYMM=YY+MM;

            GLobalFields.Field14=YYMM+"01";
        }


        Log.d(TAG, "Field14.........:" +  GLobalFields.Field14);

        if(!TransBasic.Txn_Menu_Type.equals("Manualcard")){
            GLobalFields.Field35=TransBasic.track2carddata;
            if(GLobalFields.Field35.charAt(GLobalFields.Field35.length()-1)=='F'){
                GLobalFields.Field35 = GLobalFields.Field35.substring(0,GLobalFields.Field35.length()-1);
        }

        }


        GLobalFields.Field41 = ISO8583msg.Ter_id;
        GLobalFields.Field42 = ISO8583msg.Mer_id;
        GLobalFields.Field49 = ISO8583msg.currency;



    }



    public byte[] sendtimeoutreversal() {

        int bitmaplength = GLobalFields.PrimaryBitmap.length() / 2;
        Log.d(TAG, "bitmaptry.........:" + GLobalFields.PrimaryBitmap);
        String[] primarybitmap = new String[bitmaplength];
        int i;
        int j = 0;
        char[] charArray = GLobalFields.PrimaryBitmap.toCharArray();
        for (i = 0; i < bitmaplength * 2; i++) {
            primarybitmap[j] = String.valueOf(charArray[i]) + charArray[i + 1];
            j++;
            i = i + 1;
        }

        byte[] header1=GLobalFields.MTI.getBytes();


        List<byte[]> listOfByteArrays1 = new ArrayList<>();
        for(int r=0;r<primarybitmap.length;r++){
            listOfByteArrays1.add(Utility.hexStr2Byte(primarybitmap[r]));
        }
        Log.d(TAG,"list of byte arrays1"+listOfByteArrays1);
        int lbm=0;
        for(byte[]byteArray1:listOfByteArrays1){
            lbm=lbm+byteArray1.length;
        }


        byte[] field02b=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field02));
        //GLobalFields.Field02.length();
        byte[]field02_length=String.valueOf(GLobalFields.Field02.length()).getBytes();
        byte[]field03 = null;
        byte[]field04=null;
        byte[]field07=null;
        byte[]field11=null;
        byte[]field12=null;
        if(TransBasic.Txn_type.equals("PURCHASE")){
            field03=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field03));
            field04=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field04));
            field07=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field07));
            field11=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field11));
            field12=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field12));

        } else if (TransBasic.Txn_type.equals("REVERSAL")) {
            field03=Utility.hexStr2Byte(Utility.asc2Hex(ResponseFields.field03));
            field04=Utility.hexStr2Byte(Utility.asc2Hex(ResponseFields.field04));

            field07=Utility.hexStr2Byte(Utility.asc2Hex(ResponseFields.field07));
            field11=Utility.hexStr2Byte(Utility.asc2Hex(ResponseFields.field11));
            field12=Utility.hexStr2Byte(Utility.asc2Hex(ResponseFields.field12));
        }
        Log.d(TAG,"fld04"+Utility.byte2HexStr(field04));
        //byte[]field14=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field14));
        byte[]field22=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field22));
        byte[]field24=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field24));
        byte[]field25=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field25));
        byte[]field35=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field35));
        byte[]field35_length=String.valueOf(GLobalFields.Field35.length()).getBytes();
        byte[]field37=Utility.hexStr2Byte(Utility.asc2Hex(ResponseFields.field37));



        byte[]field41=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field41));
        byte[]field42=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field42));
        byte[]field49=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field49));
        Log.d(TAG,"fld49"+Utility.byte2HexStr(field49));

        Log.d(TAG,"fld49"+Utility.byte2HexStr(field49));
        ByteBuffer byteBuffer = null;


        if(TransBasic.Txn_type.equals("PURCHASE")){
            byteBuffer=ByteBuffer.allocate(header1.length+lbm+
                    field02_length.length+
                    field02b.length+field03.length+
                    field04.length+field07.length+
                    field11.length+field12.length+//field14.length+
                    field22.length+field24.length+
                    field35_length.length+
                    field25.length+field35.length+
                    field41.length+field42.length+
                    field49.length);
            byteBuffer.put(header1);
            for(byte[] byteArray1:listOfByteArrays1){

                byteBuffer.put(byteArray1);
            }

            byteBuffer.put(field02_length);
            byteBuffer.put(field02b);
            byteBuffer.put(field03);
            byteBuffer.put(field04);
            byteBuffer.put(field07);
            byteBuffer.put(field11);
            byteBuffer.put(field12);
           // byteBuffer.put(field14);
            byteBuffer.put(field22);
            byteBuffer.put(field24);
            byteBuffer.put(field25);
            byteBuffer.put(field35_length);
            byteBuffer.put(field35);
            byteBuffer.put(field41);
            byteBuffer.put(field42);
            byteBuffer.put(field49);
        } else if (TransBasic.Txn_type.equals("REVERSAL")) {
            byteBuffer=ByteBuffer.allocate(header1.length+lbm+
                    field02_length.length+
                    field02b.length+field03.length+
                    field04.length+field07.length+
                    field11.length+field12.length+
                    field22.length+field24.length+
                    field25.length+field35_length.length+
                    field35.length+field37.length+
                    field41.length+field42.length+
                    field49.length);
            byteBuffer.put(header1);
            for(byte[] byteArray1:listOfByteArrays1){
                byteBuffer.put(byteArray1);

            }

            byteBuffer.put(field02_length);
            byteBuffer.put(field02b);
            byteBuffer.put(field03);
            byteBuffer.put(field04);
            byteBuffer.put(field07);
            byteBuffer.put(field11);
            byteBuffer.put(field12);
            byteBuffer.put(field22);
            byteBuffer.put(field24);
            byteBuffer.put(field25);
            byteBuffer.put(field35_length);
            byteBuffer.put(field35);
            byteBuffer.put(field37);
            byteBuffer.put(field41);
            byteBuffer.put(field42);
            byteBuffer.put(field49);


        }

        byte[] bytBuffer1=byteBuffer.array();
        Log.d(TAG,"mti_bitmap_fld2_to_fld49....."+Utility.byte2HexStr(bytBuffer1));
        Log.d(TAG,"length....."+Utility.byte2HexStr(bytBuffer1).length());

        byte[]field52=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field52));

        byte[]field52len=String.valueOf(GLobalFields.Field52.length()).getBytes();
        ByteBuffer byteBuffer2=ByteBuffer.allocate(field52len.length+field52.length);

        byteBuffer2.put(field52len);
        byteBuffer2.put(field52);
        byte[]bytBuffer2=byteBuffer2.array();
        int field55Length= GLobalFields.Field55.length() / 2;
        Log.d(TAG, "fld55try.........:" + GLobalFields.Field55);
        String[] field55 = new String[field55Length];
        int l;
        int m = 0;
        char[] charArray1 = GLobalFields.Field55.toCharArray();
        for (l = 0; l < field55Length * 2; l++) {
            field55[m] = String.valueOf(charArray1[l]) + charArray1[l + 1];
            m++;
            l = l + 1;
        }

        List<byte[]> listOfByteArrays = new ArrayList<>();
        for(int c=0;c<field55.length;c++){
            listOfByteArrays.add(Utility.hexStr2Byte(field55[c]));
        }
        Log.d(TAG,"list of byte arrays"+listOfByteArrays);
        int l55=0;
        for(byte[]byteArray:listOfByteArrays){
            l55=l55+byteArray.length;
        }
        byte[]len_byte=String.valueOf(l55).getBytes();
        Log.d(TAG,"len55:"+Utility.byte2HexStr(len_byte));
        ByteBuffer byteBufferF55=ByteBuffer.allocate(len_byte.length+l55);
        byteBufferF55.put(len_byte);

        for(byte[] byteArray:listOfByteArrays){

            byteBufferF55.put(byteArray);
        }
        byte[]bufferF55=byteBufferF55.array();
        Log.d(TAG,"bufferF55:"+Utility.byte2HexStr(bufferF55));



        ByteBuffer combinedBuffer = null;

        if(TransBasic.Txn_type.equals("PURCHASE")){
            if(TransBasic.isonline){
                combinedBuffer=ByteBuffer.allocate(bytBuffer1.length/*+bytBuffer2.length+bufferF55.length*/);
                combinedBuffer.put(bytBuffer1);
               // combinedBuffer.put(bytBuffer2);
                //combinedBuffer.put(bufferF55);

            }else{
                combinedBuffer=ByteBuffer.allocate(bytBuffer1.length/*+bufferF55.length*/);
                combinedBuffer.put(bytBuffer1);
               // combinedBuffer.put(bufferF55);
            }

        } else if (TransBasic.Txn_type.equals("REVERSAL")) {
            combinedBuffer=ByteBuffer.allocate(bytBuffer1.length+bufferF55.length);
            combinedBuffer.put(bytBuffer1);
            //combinedBuffer.put(bufferF55);

        }

        byte[] resultByte =combinedBuffer.array();
        Log.d(TAG," SEND hex timeout....  "+ Utility.byte2HexStr(resultByte));
        return resultByte;


    }





    public byte[] send() {
        int bitmaplength = GLobalFields.PrimaryBitmap.length() / 2;
        Log.d(TAG, "bitmaptry.........:" + GLobalFields.PrimaryBitmap);
        String[] primarybitmap = new String[bitmaplength];
        int i;
        int j = 0;
        char[] charArray = GLobalFields.PrimaryBitmap.toCharArray();
        for (i = 0; i < bitmaplength * 2; i++) {
            primarybitmap[j] = String.valueOf(charArray[i]) + charArray[i + 1];
            j++;
            i = i + 1;
        }

        byte[] header1=GLobalFields.MTI.getBytes();


        List<byte[]> listOfByteArrays1 = new ArrayList<>();
        for(int r=0;r<primarybitmap.length;r++){
            listOfByteArrays1.add(Utility.hexStr2Byte(primarybitmap[r]));
        }
        Log.d(TAG,"list of byte arrays1"+listOfByteArrays1);
        int lbm=0;
        for(byte[]byteArray1:listOfByteArrays1){
            lbm=lbm+byteArray1.length;
        }


        byte[] field02b=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field02));
        //GLobalFields.Field02.length();
        byte[]field02_length=String.valueOf(GLobalFields.Field02.length()).getBytes();
        byte[]field03 = null;
        byte[]field04=null;
        byte[]field07=null;
        byte[]field11=null;
        byte[]field12=null;
        if(TransBasic.Txn_type.equals("PURCHASE")||TransBasic.Txn_type.equals("BALANCE_INQUIRY")||TransBasic.Txn_type.equals("CASH_ADVANCE")){

            field03=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field03));
            field04=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field04));
            field07=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field07));
            field11=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field11));
            field12=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field12));

        } else {
            if (TransBasic.Txn_type.equals("REVERSAL")) {
                field03=Utility.hexStr2Byte(Utility.asc2Hex(ResponseFields.field03));
                field04=Utility.hexStr2Byte(Utility.asc2Hex(ResponseFields.field04));
                field07=Utility.hexStr2Byte(Utility.asc2Hex(ResponseFields.field07));
                field11=Utility.hexStr2Byte(Utility.asc2Hex(ResponseFields.field11));
                field12=Utility.hexStr2Byte(Utility.asc2Hex(ResponseFields.field12));
           }
        }
        /*} else if (TransBasic.Txn_Menu_Type.equals("Manualcard") && (Supervisor_menu_activity.manual_txn_type1.equals("PURCHASE") || Supervisor_menu_activity.manual_txn_type1.equals("BALANCE_INQUIRY"))) {
            field03=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field03));
            field04=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field04));
            field07=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field07));
            field11=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field11));
            field12=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field12));
        }*/
        Log.d(TAG,"fld04"+Utility.byte2HexStr(field04));
        byte[]field14=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field14));
        byte[]field22=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field22));
        byte[]field24=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field24));
        byte[]field25=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field25));
        Log.d(TAG,"field25"+Utility.byte2HexStr(field25));
        byte[]field35=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field35));
        byte[]field35_length=String.valueOf(GLobalFields.Field35.length()).getBytes();
        byte[]field37=Utility.hexStr2Byte(Utility.asc2Hex(ResponseFields.field37));
        byte[]field41=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field41));
        Log.d(TAG,"field41"+Utility.byte2HexStr(field41));
        byte[]field42=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field42));
        byte[]field49=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field49));
        Log.d(TAG,"fld49"+Utility.byte2HexStr(field49));
        Log.d(TAG,"fld49"+Utility.byte2HexStr(field49));
        ByteBuffer byteBuffer = null;
       if (TransBasic.Txn_Menu_Type.equals("Manualcard") && (Supervisor_menu_activity.manual_txn_type1.equals("PURCHASE") || Supervisor_menu_activity.manual_txn_type1.equals("BALANCE_INQUIRY")|| Supervisor_menu_activity.manual_txn_type1.equals("REVERSAL"))) {
            byteBuffer=ByteBuffer.allocate(header1.length+lbm+
                    field02_length.length+
                    field02b.length+
                    field03.length+
                    field04.length+
                    field07.length+
                    field11.length+
                    field12.length+
                    field14.length+
                    field22.length+
                    field24.length+
                    field25.length+
                    field41.length+
                    field42.length+
                    field49.length);
            byteBuffer.put(header1);
            for(byte[] byteArray1:listOfByteArrays1){

                byteBuffer.put(byteArray1);
            }
                    byteBuffer.put(field02_length);
                    byteBuffer.put(field02b);
                    byteBuffer.put(field03);
                    byteBuffer.put(field04);
                    byteBuffer.put(field07);
                    byteBuffer.put(field11);
                    byteBuffer.put(field12);
                    byteBuffer.put(field14);
                    byteBuffer.put(field22);
                    byteBuffer.put(field24);
                    byteBuffer.put(field25);
                    Log.d(TAG,"upto field25"+Utility.byte2HexStr(byteBuffer.array()));
                    byteBuffer.put(field41);
                    byteBuffer.put(field42);
                    byteBuffer.put(field49);
                    Log.d(TAG,"upto field49"+Utility.byte2HexStr(byteBuffer.array()));
       } else if(TransBasic.Txn_type.equals("PURCHASE")||TransBasic.Txn_type.equals("BALANCE_INQUIRY")||TransBasic.Txn_type.equals("CASH_ADVANCE")){
            byteBuffer=ByteBuffer.allocate(header1.length+lbm+
                    field02_length.length+
                    field02b.length+
                    field03.length+
                    field04.length+
                    field07.length+
                    field11.length+
                    field12.length+
                    field14.length+
                    field22.length+
                    field24.length+
                    field35_length.length+
                    field25.length+
                    field35.length+
                    field41.length+
                    field42.length+
                    field49.length);
            byteBuffer.put(header1);
            for(byte[] byteArray1:listOfByteArrays1){
            byteBuffer.put(byteArray1);
                  }
                    byteBuffer.put(field02_length);
                    byteBuffer.put(field02b);
                    byteBuffer.put(field03);
                    byteBuffer.put(field04);
                    byteBuffer.put(field07);
                    byteBuffer.put(field11);
                    byteBuffer.put(field12);
                    byteBuffer.put(field14);
                    byteBuffer.put(field22);
                    byteBuffer.put(field24);
                    byteBuffer.put(field25);
                    byteBuffer.put(field35_length);
                    byteBuffer.put(field35);
                    byteBuffer.put(field41);
                    byteBuffer.put(field42);
                    byteBuffer.put(field49);
         Log.d(TAG,"upto field49...."+Utility.byte2HexStr(byteBuffer.array()));
     } else if (TransBasic.Txn_type.equals("REVERSAL")) {
            byteBuffer=ByteBuffer.allocate(header1.length+lbm+
                    field02_length.length+
                    field02b.length+
                    field03.length+
                    field04.length+
                    field07.length+
                    field11.length+
                    field12.length+
                    field22.length+
                    field24.length+
                    field25.length+
                    field35_length.length+
                    field35.length+
                    field37.length+
                    field41.length+
                    field42.length+
                    field49.length);
            byteBuffer.put(header1);
            for(byte[] byteArray1:listOfByteArrays1){
              byteBuffer.put(byteArray1);

           }

                    byteBuffer.put(field02_length);
                    byteBuffer.put(field02b);
                    byteBuffer.put(field03);
                    byteBuffer.put(field04);
                    byteBuffer.put(field07);
                    byteBuffer.put(field11);
                    byteBuffer.put(field12);
                    byteBuffer.put(field22);
                    byteBuffer.put(field24);
                    byteBuffer.put(field25);
                    byteBuffer.put(field35_length);
                    byteBuffer.put(field35);
                    byteBuffer.put(field37);
                    byteBuffer.put(field41);
                    byteBuffer.put(field42);
                    byteBuffer.put(field49);

         }

        byte[] bytBuffer1=byteBuffer.array();
        Log.d(TAG,"mti_bitmap_fld2_to_fld49....."+Utility.byte2HexStr(bytBuffer1));
        Log.d(TAG,"length....."+Utility.byte2HexStr(bytBuffer1).length());
        byte[]field52=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field52));
        byte[]field52len=String.valueOf(GLobalFields.Field52.length()).getBytes();
        ByteBuffer byteBuffer2=ByteBuffer.allocate(field52len.length+field52.length);
        byteBuffer2.put(field52len);
        byteBuffer2.put(field52);
        byte[]bytBuffer2=byteBuffer2.array();
        int field55Length= GLobalFields.Field55.length() / 2;
            Log.d(TAG, "fld55try.........:" + GLobalFields.Field55);
            String[] field55 = new String[field55Length];
        int l;
        int m = 0;
        char[] charArray1 = GLobalFields.Field55.toCharArray();
        for (l = 0; l < field55Length * 2; l++) {
            field55[m] = String.valueOf(charArray1[l]) + charArray1[l + 1];
            m++;
            l = l + 1;
        }

        List<byte[]> listOfByteArrays = new ArrayList<>();
        for(int c=0;c<field55.length;c++){
            listOfByteArrays.add(Utility.hexStr2Byte(field55[c]));
        }
        Log.d(TAG,"list of byte arrays"+listOfByteArrays);
        int l55=0;
        for(byte[]byteArray:listOfByteArrays){
            l55=l55+byteArray.length;
        }
        byte[]len_byte=String.valueOf(l55).getBytes();
        Log.d(TAG,"len55:"+Utility.byte2HexStr(len_byte));
        ByteBuffer byteBufferF55=ByteBuffer.allocate(len_byte.length+l55);
        byteBufferF55.put(len_byte);
        for(byte[] byteArray:listOfByteArrays){

            byteBufferF55.put(byteArray);
        }
        byte[]bufferF55=byteBufferF55.array();
        Log.d(TAG,"bufferF55:"+Utility.byte2HexStr(bufferF55));



        ByteBuffer combinedBuffer = null;
        if (TransBasic.Txn_Menu_Type.equals("Manualcard") && Supervisor_menu_activity.manual_txn_type1.equals("PURCHASE")) {
            combinedBuffer=ByteBuffer.allocate(bytBuffer1.length);
            combinedBuffer.put(bytBuffer1);
        } else if (TransBasic.Txn_Menu_Type.equals("Manualcard") && Supervisor_menu_activity.manual_txn_type1.equals("BALANCE_INQUIRY")) {
            combinedBuffer=ByteBuffer.allocate(bytBuffer1.length);
            combinedBuffer.put(bytBuffer1);
        } else if (TransBasic.Txn_Menu_Type.equals("Manualcard") && Supervisor_menu_activity.manual_txn_type1.equals("REVERSAL")) {
            combinedBuffer=ByteBuffer.allocate(bytBuffer1.length);
            combinedBuffer.put(bytBuffer1);
        } else if(TransBasic.Txn_type.equals("PURCHASE")||TransBasic.Txn_type.equals("BALANCE_INQUIRY")||TransBasic.Txn_type.equals("CASH_ADVANCE")){
            if(TransBasic.isonline){
                combinedBuffer=ByteBuffer.allocate(bytBuffer1.length+bytBuffer2.length+bufferF55.length);
                combinedBuffer.put(bytBuffer1);
                combinedBuffer.put(bytBuffer2);
                combinedBuffer.put(bufferF55);

            } else{
                combinedBuffer=ByteBuffer.allocate(bytBuffer1.length+bufferF55.length);
                combinedBuffer.put(bytBuffer1);
                combinedBuffer.put(bufferF55);
            }

        } else if (TransBasic.Txn_type.equals("REVERSAL")) {
            combinedBuffer=ByteBuffer.allocate(bytBuffer1.length+bufferF55.length);
            combinedBuffer.put(bytBuffer1);
            combinedBuffer.put(bufferF55);

        }

        byte[] resultByte =combinedBuffer.array();
        Log.d(TAG," SEND hex....  "+ Utility.byte2HexStr(resultByte));
        Log.d(TAG," lengthsent...  "+ Utility.byte2HexStr(resultByte).length());
        return resultByte;


    }



    public static String fillgapsequence(String data, int size){
        String result=data;
        while(!(result.length()==size)){
            result="0"+result;
        }
        return result;
    }
    public static String responsemessage="30323130723000000E808000313939323331343134313539343130363330373536303030303030303030303030303030323030303232353133333033373030303134363136303232353037323834363030303133383834383631343834383631343030305030303030303032323330";
    //System.out.print(responsemessage);
    //iso8583unpack(responsemessage);


    public static void iso8583Unpack(String responsemessage) {
        //this.responsemessage=responsemessage;


        ResponseFields.mti=responsemessage.substring(0,8);
        // System.out.println(hexmti);
        String MTI = hexToAscii(ResponseFields.mti);
        System.out.println("mti string: " + MTI);
        ResponseFields.primaryBitmap=responsemessage.substring(8,24);
        System.out.println("primarybitmap : " +  ResponseFields.primaryBitmap);
        String responsebody="";
        responsebody=hexToAscii(responsemessage.substring(24));
        System.out.println("responsebody : " + responsebody);
        String binarybitmap1="";
        binarybitmap1=hexToBinary( ResponseFields.primaryBitmap);
        System.out.println("binarybitmap1 : " + binarybitmap1);
        assignfields(binarybitmap1,responsebody);
    }


    public static String hexToBinary(String hexString) {
        StringBuilder binary = new StringBuilder();
        for (int i = 0; i < hexString.length(); i++) {
            char hexChar = hexString.charAt(i);
            String binaryString = Integer.toBinaryString(Integer.parseInt(String.valueOf(hexChar), 16));
            binary.append(String.format("%4s", binaryString).replace(' ', '0'));
        }
        return binary.toString();
    }

     public static String hexToAscii(String hexString) {
        StringBuilder output = new StringBuilder("");
        for (int i = 0; i < hexString.length(); i += 2) {
            String hex = hexString.substring(i, i + 2);
            output.append((char) Integer.parseInt(hex, 16));
        }
        return output.toString();
      }
     public static class ResponseFields{
        public static String primaryBitmap="";
        public static String mti="";
        public static String field02="";
        public static String field03="";
        public static String field04="";
        public static String field05="";
        public static String field06="";
        public static String field07="";
        public static String field08="";
        public static String field09="";
        public static String field10="";
        public static String field11="";
        public static String field12="";
        public static String field13="";
        public static String field14="";
        public static String field15="";
        public static String field16="";
        public static String field17="";
        public static String field18="";
        public static String field19="";
        public static String field20="";
        public static String field21="";
        public static String field22="";
        public static String field23="";
        public static String field24="";
        public static String field25="";
        public static String field26="";
        public static String field27="";
        public static String field28="";
        public static String field29="";
        public static String field30="";
        public static String field31="";
        public static String field32="";
        public static String field33="";
        public static String field34="";
        public static String field35="";
        public static String field36="";



        public static String field37="";
        public static String field38="";
        public static String field39="";
        public static String field40="";
        public static String field41="";
        public static String field42="";
        public static String field43="";
        public static String field44="";
        public static String field45="";
        public static String field46="";
        public static String field47="";
        public static String field48="";
        public static String field49="";
        public static String field50="";
        public static String field51="";
        public static String field52="";
        public static String field53="";
        public static String field54="";
        public static String field55="";
        public static String field56="";
        public static String field57="";
        public static String field58="";
        public static String field59="";
        public static String field60="";
        public static String field61="";
        public static String field62="";
        public static String field63="";
        public static String field64="";
        public static String field65="";
        public static String field66="";
        public static String field67="";
        public static String field68="";
        public static String field69="";
        public static String field70="";
        public static String field71="";
        public static String field72="";
        public static String field73="";
        public static String field74="";
        public static String field75="";
        public static String field76="";
        public static String field77="";
        public static String field78="";
        public static String field79="";
        public static String field80="";
        public static String field81="";
        public static String field82="";
        public static String field83="";
        public static String field84="";
        public static String field85="";
        public static String field86="";
        public static String field87="";
        public static String field88="";
        public static String field89="";
        public static String field90="";
        public static String field91="";
        public static String field92="";
        public static String field93="";
        public static String field94="";
        public static String field95="";
        public static String field96="";
        public static String field97="";
        public static String field98="";
        public static String field99="";
        public static String field100="";
    }

    static void assignfields(String binarybitmap, String responsebody) {

        int i;
        int j;
        int n=0;
        String prefix="";
        int num;
        char[] charArray = binarybitmap.toCharArray();
        String[] strArraybitmap = new String[charArray.length];
        for ( i = 0; i < charArray.length; i++) {
            strArraybitmap[i] = String.valueOf(charArray[i]);
        }
        System.out.println("binarybitmap1 : " + strArraybitmap[1]);
        for(i=0;i<64;i++)
        {
            if(strArraybitmap[i].equals("1"))
            {
                j=i+1;
                System.out.println("bitmap available : " + j);
                if(j==2)
                {
                    prefix=responsebody.substring(0,2);
                    System.out.println("prefix: " + prefix);
                    num = Integer.parseInt(prefix);
                    n=n+2;
                    ResponseFields.field02=responsebody.substring(n,n+num);
                    Log.d(TAG,"field02: " + ResponseFields.field02);
                    n=n+num;

                }
                else if(j==3)
                {

                    ResponseFields.field03=responsebody.substring(n,n+6);
                    Log.d(TAG,"field03: " + ResponseFields.field03);
                    n=n+6;

                }
                else if(j==4)
                {

                    ResponseFields.field04=responsebody.substring(n,n+12);
                    Log.d(TAG,"field04: " + ResponseFields.field04);
                    n=n+12;

                }
                else if(j==7)
                {

                    ResponseFields.field07=responsebody.substring(n,n+10);
                    Log.d(TAG,"field07: " + ResponseFields.field07);
                    n=n+10;

                }
                else if(j==11)
                {

                    ResponseFields.field11=responsebody.substring(n,n+6);
                    Log.d(TAG,"field11: " + ResponseFields.field11);
                    n=n+6;

                }
                else if(j==12)
                {

                    ResponseFields.field12=responsebody.substring(n,n+12);
                    Log.d(TAG,"field12: " + ResponseFields.field12);

                    n=n+12;

                }
                else if(j==37)
                {

                    ResponseFields.field37=responsebody.substring(n,n+12);
                     Log.d(TAG,"field37: " + ResponseFields.field37);
                    n=n+12;

                }
                else if(j==38)
                {

                    ResponseFields.field38=responsebody.substring(n,n+6);
                    Log.d(TAG,"field38: " + ResponseFields.field38);
                    n=n+6;

                }
                else if(j==39)
                {

                    ResponseFields.field39=responsebody.substring(n,n+3);
                    Log.d(TAG,"field39: " + ResponseFields.field39);
                    n=n+3;

                }
                else if(j==41)
                {

                    ResponseFields.field41=responsebody.substring(n,n+8);
                    Log.d(TAG,"field41: " + ResponseFields.field41);
                    n=n+8;

                }
                else if(j==49)
                {

                    ResponseFields.field49=responsebody.substring(n,n+3);
                    Log.d(TAG,"field49: " + ResponseFields.field49);
                    n=n+3;

                }
                else if(j==55)
                {
                    prefix=responsebody.substring(n,n+3);
                    System.out.println("prefix: " + prefix);
                    num = Integer.parseInt(prefix);
                    n=n+3;
                    ResponseFields.field55=responsebody.substring(n,n+num);
                    Log.d(TAG,"field55: " + ResponseFields.field55);


                }
            }
        }


        }
    public static class keyDownloadRequestFields{
        public  static String mti="0800";
        public  static String primaryBitMap="2220010000C00000";
        public  static String field03="990000";
        public  static String field07=new SimpleDateFormat("MMddhhmmss", Locale.getDefault()).format(new Date());
        public  static String field11="";
        public  static String field24="811";
        public  static String field41="";
        public  static String field42="";
    }
    public static byte[] packKeyDownloadRequestFields(){

        int bitmaplength = keyDownloadRequestFields.primaryBitMap.length()/2;
        Log.d(TAG, "bitmaptryfor key download.........:" + keyDownloadRequestFields.primaryBitMap);
        String[] primarybitmap = new String[bitmaplength];
        int i;
        int j = 0;
        char[] charArray =keyDownloadRequestFields.primaryBitMap.toCharArray();
        for (i = 0; i < bitmaplength * 2; i++) {
            primarybitmap[j] = String.valueOf(charArray[i]) + charArray[i + 1];
            j++;
            i = i + 1;
        }
        byte[] header=keyDownloadRequestFields.mti.getBytes();

// adding the primary bit map in array list
        List<byte[]> listOfByteArrays = new ArrayList<>();
        for(int r=0;r<primarybitmap.length;r++){
            listOfByteArrays.add(Utility.hexStr2Byte(primarybitmap[r]));
        }
        Log.d(TAG,"list of byte arraysfor bitmap"+listOfByteArrays);
        //compute length of primary bitmap
        int lengthOfBitmap=0;
        for(byte[]byteArray1:listOfByteArrays){
            lengthOfBitmap=lengthOfBitmap+byteArray1.length;
        }


        byte[]field03=Utility.hexStr2Byte(Utility.asc2Hex(keyDownloadRequestFields.field03));
        byte[]field07=Utility.hexStr2Byte(Utility.asc2Hex(keyDownloadRequestFields.field07));
        byte[]field11=Utility.hexStr2Byte(Utility.asc2Hex(keyDownloadRequestFields.field11));
        byte[]field24=Utility.hexStr2Byte(Utility.asc2Hex(keyDownloadRequestFields.field24));
        byte[]field41=Utility.hexStr2Byte(Utility.asc2Hex(keyDownloadRequestFields.field41));
        byte[]field42=Utility.hexStr2Byte(Utility.asc2Hex(keyDownloadRequestFields.field42));
        ByteBuffer byteBuffer=ByteBuffer.allocate(header.length+
                lengthOfBitmap+
                field03.length+
                field07.length+
                field11.length+
                field24.length+
                field41.length+
                field42.length);
        byteBuffer.put(header);
        for(byte[] byteArray:listOfByteArrays){

            byteBuffer.put(byteArray);
        }
        byteBuffer.put(field03);
        Log.d(TAG,"fid3 :"+Utility.byte2HexStr(field03));

        byteBuffer.put(field07);
        byteBuffer.put(field11);
        byteBuffer.put(field24);
        byteBuffer.put(field41);
        byteBuffer.put(field42);
        byte[] result=byteBuffer.array();
        Log.d(TAG,"result for key download :"+Utility.byte2HexStr(result));


        return result;
    }
    public static class KeyDownloadResponseFields{
        public static String mti="";
        public static String primaryBitmap="";
        public static String field02="";
        public static String field03="";
        public static String field04="";
        public static String field05="";
        public static String field06="";
        public static String field07="";
        public static String field08="";
        public static String field09="";
        public static String field10="";
        public static String field11="";
        public static String field12="";
        public static String field13="";
        public static String field14="";
        public static String field15="";
        public static String field16="";
        public static String field17="";
        public static String field18="";
        public static String field19="";
        public static String field20="";
        public static String field21="";
        public static String field22="";
        public static String field23="";
        public static String field24="";
        public static String field25="";
        public static String field26="";
        public static String field27="";
        public static String field28="";
        public static String field29="";
        public static String field30="";
        public static String field31="";
        public static String field32="";
        public static String field33="";
        public static String field34="";
        public static String field35="";
        public static String field36="";



        public static String field37="";
        public static String field38="";
        public static String field39="";
        public static String field40="";
        public static String field41="";
        public static String field42="";
        public static String field43="";
        public static String field44="";
        public static String field45="";
        public static String field46="";
        public static String field47="";
        public static String field48="";
        public static String field49="";
        public static String field50="";
        public static String field51="";
        public static String field52="";
        public static String field53="";
        public static String field54="";
        //public static String field55="";
        public static String field56="";
        public static String field57="";
        public static String field58="";
        public static String field59="";
        public static String field60="";
        public static String field61="";
        public static String field62="";
        public static String field63="";
        public static String field64="";
        public static String field65="";
        public static String field66="";
        public static String field67="";
        public static String field68="";
        public static String field69="";
        public static String field70="";
        public static String field71="";
        public static String field72="";
        public static String field73="";
        public static String field74="";
        public static String field75="";
        public static String field76="";
        public static String field77="";
        public static String field78="";
        public static String field79="";
        public static String field80="";
        public static String field81="";
        public static String field82="";
        public static String field83="";
        public static String field84="";
        public static String field85="";
        public static String field86="";
        public static String field87="";
        public static String field88="";
        public static String field89="";
        public static String field90="";
        public static String field91="";
        public static String field92="";
        public static String field93="";
        public static String field94="";
        public static String field95="";
        public static String field96="";
        public static String field97="";
        public static String field98="";
        public static String field99="";
        public static String field100="";
    }
    public static void iso8583UnpackKeyDownload(String responsemessage) {
        //this.responsemessage=responsemessage;


        KeyDownloadResponseFields.mti=responsemessage.substring(0,8);
        // System.out.println(hexmti);
        String MTI = hexToAscii(KeyDownloadResponseFields.mti);
        Log.d(TAG ,"mti:"+ MTI);

        KeyDownloadResponseFields.primaryBitmap=responsemessage.substring(8,24);
        Log.d(TAG,"primarybitmap" +  KeyDownloadResponseFields.primaryBitmap);
        String responsebody="";
        responsebody=hexToAscii(responsemessage.substring(24));
        System.out.println("responsebody : " + responsebody);
        String binarybitmap1="";
        binarybitmap1=hexToBinary( KeyDownloadResponseFields.primaryBitmap);
        Log.d(TAG,"binarybitmap1:" +binarybitmap1);
        assignfieldsForKeyDownload(binarybitmap1,responsebody);
    }
    static void assignfieldsForKeyDownload(String binarybitmap, String responsebody) {

        int i;
        int j;
        int n=0;
        String prefix="";
        int num;
        char[] charArray = binarybitmap.toCharArray();
        String[] strArraybitmap = new String[charArray.length];
        for ( i = 0; i < charArray.length; i++) {
            strArraybitmap[i] = String.valueOf(charArray[i]);
        }
        Log.d(TAG,"binarybitmap1 :" +strArraybitmap[1]);

        for(i=0;i<64;i++)
        {
            if(strArraybitmap[i].equals("1"))
            {
                j=i+1;
                System.out.println("bitmap available : " + j);
                /*if(j==2)
                {
                    prefix=responsebody.substring(0,2);
                    System.out.println("prefix: " + prefix);
                    num = Integer.parseInt(prefix);
                    n=n+2;
                    ResponseFields.field02=responsebody.substring(n,n+num);
                    System.out.println("field02: " + ResponseFields.field02);
                    n=n+num;
                }*/
                if(j==3)
                {
                    KeyDownloadResponseFields.field03=responsebody.substring(n,n+6);
                    Log.d(TAG,"field03: " +KeyDownloadResponseFields.field03);
                    n=n+6;
                }
                else if(j==7)
                {
                    KeyDownloadResponseFields.field07=responsebody.substring(n,n+10);
                    Log.d(TAG,"field07: " +KeyDownloadResponseFields.field07);
                    n=n+10;
                }
                else if(j==11)
                {
                    KeyDownloadResponseFields.field11=responsebody.substring(n,n+6);
                    Log.d(TAG,"field11: " +KeyDownloadResponseFields.field11);
                    n=n+6;
                }
                else if(j==37)
                {
                    KeyDownloadResponseFields.field37=responsebody.substring(n,n+12);
                    Log.d(TAG,"field37: " +KeyDownloadResponseFields.field37);
                    n=n+12;
                }
                else if(j==39)
                {
                    KeyDownloadResponseFields.field39=responsebody.substring(n,n+3);
                    Log.d(TAG,"field39: " +KeyDownloadResponseFields.field39);
                    n=n+3;
                }
                else if(j==41)
                {
                    KeyDownloadResponseFields.field41=responsebody.substring(n,n+8);
                    Log.d(TAG,"field41: " +KeyDownloadResponseFields.field41);
                    n=n+8;
                }
                else if(j==53)
                {
                    prefix=responsebody.substring(n,n+2);
                    num=Integer.parseInt(prefix);
                    n=n+2;

                    KeyDownloadResponseFields.field53=responsebody.substring(n,n+num);
                    //Log.d(TAG,"field53: " +Utility.(KeyDownloadResponseFields.field53));
                }
            }
        }
    }
    public static byte[] packSettlementRequestFields(){
        int bitmaplength = GLobalFields.PrimaryBitmap.length()/2;
        Log.d(TAG, "bitmaptryfor settlement.........:" + GLobalFields.PrimaryBitmap);
        String[] primarybitmap = new String[bitmaplength];
        int i;
        int j = 0;
        char[] charArray =GLobalFields.PrimaryBitmap.toCharArray();
        for (i = 0; i < bitmaplength * 2; i++) {
            primarybitmap[j] = String.valueOf(charArray[i]) + charArray[i + 1];
            j++;
            i = i + 1;
        }
        byte[] header=GLobalFields.MTI.getBytes();

// adding the primary bit map in array list
        List<byte[]> listOfByteArrays = new ArrayList<>();
        for(int r=0;r<primarybitmap.length;r++){
            listOfByteArrays.add(Utility.hexStr2Byte(primarybitmap[r]));
        }
        Log.d(TAG,"list of byte arraysfor bitmap"+listOfByteArrays);
        //compute length of primary bitmap
        int lengthOfBitmap=0;
        for(byte[]byteArray1:listOfByteArrays){
            lengthOfBitmap=lengthOfBitmap+byteArray1.length;
        }
        byte[]field05=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field05));
        byte[]field11=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field11));
        byte[]field15=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field15));
        byte[]field24=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field24));
        byte[]field41=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field41));
        byte[]field42=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field42));
        ByteBuffer byteBuffer=ByteBuffer.allocate(header.length+lengthOfBitmap+field05.length+field11.length+
                                                          field15.length+ field24.length+field41.length+field42.length);
        byteBuffer.put(header);
        for(byte[] byteArray:listOfByteArrays){

            byteBuffer.put(byteArray);
        }
        byteBuffer.put(field05);
        byteBuffer.put(field11);

        byteBuffer.put(field15);

        byteBuffer.put(field24);

        byteBuffer.put(field41);

        byteBuffer.put(field42);

        byte[] packet=byteBuffer.array();
        return packet;
    }
    public static byte[] packFieldsForNetworkReset(){
        int bitmaplength = GLobalFields.PrimaryBitmap.length()/2;
        Log.d(TAG, "bitmaptryfor network reset.........:" + GLobalFields.PrimaryBitmap);
        String[] primarybitmap = new String[bitmaplength];
        int i;
        int j = 0;
        char[] charArray =GLobalFields.PrimaryBitmap.toCharArray();
        for (i = 0; i < bitmaplength * 2; i++) {
            primarybitmap[j] = String.valueOf(charArray[i]) + charArray[i + 1];
            j++;
            i = i + 1;
        }
        byte[] header=GLobalFields.MTI.getBytes();

// adding the primary bit map in array list
        List<byte[]> listOfByteArrays = new ArrayList<>();
        for(int r=0;r<primarybitmap.length;r++){
            listOfByteArrays.add(Utility.hexStr2Byte(primarybitmap[r]));
        }
        Log.d(TAG,"list of byte arraysfor bitmap"+listOfByteArrays);
        //compute length of primary bitmap
        int lengthOfBitmap=0;
        for(byte[]byteArray1:listOfByteArrays){
            lengthOfBitmap=lengthOfBitmap+byteArray1.length;
        }
        byte[]field03=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field03));
        byte[]field07=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field07));
        byte[]field11=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field11));
        byte[]field24=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field24));
        byte[]field41=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field41));
        byte[]field42=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field42));
        byte[]field64=Utility.hexStr2Byte(Utility.asc2Hex(GLobalFields.Field64));

        ByteBuffer byteBuffer=ByteBuffer.allocate(header.length+
                lengthOfBitmap+
                field03.length+
                field07.length+
                field11.length+
                field24.length+
                field41.length+
                field42.length+
                field64.length);
        byteBuffer.put(header);
        for(byte[] byteArray:listOfByteArrays){
            byteBuffer.put(byteArray);
        }
        byteBuffer.put(field03);
        byteBuffer.put(field07);
        byteBuffer.put(field11);
        byteBuffer.put(field24);
        byteBuffer.put(field41);
        byteBuffer.put(field42);
        byteBuffer.put(field64);
        byte[] packet=byteBuffer.array();
        return packet;
    }
    public static void unpackSettlementResponse(String responsemessage) {
        ResponseFields.mti=responsemessage.substring(0,8);
        String MTI = hexToAscii(ResponseFields.mti);
        Log.d(TAG ,"mti:"+ MTI);
        ResponseFields.primaryBitmap=responsemessage.substring(8,24);
        Log.d(TAG,"primarybitmap" +  ResponseFields.primaryBitmap);
        String responsebody="";
        responsebody=hexToAscii(responsemessage.substring(24));
        System.out.println("responsebody : " + responsebody);
        String binarybitmap1="";
        binarybitmap1=hexToBinary( ResponseFields.primaryBitmap);
        Log.d(TAG,"binarybitmap1:" +binarybitmap1);
        assignfieldsForSettlement(binarybitmap1,responsebody);
    }
    static void assignfieldsForSettlement(String binarybitmap, String responsebody) {
        int i;
        int j;
        int n=0;
        String prefix="";
        int num;
        char[] charArray = binarybitmap.toCharArray();
        String[] strArraybitmap = new String[charArray.length];
        for ( i = 0; i < charArray.length; i++) {
            strArraybitmap[i] = String.valueOf(charArray[i]);
        }
        Log.d(TAG,"binarybitmap1 :" +strArraybitmap[1]);

        for(i=0;i<64;i++)
        {
            if(strArraybitmap[i].equals("1"))
            {
                j=i+1;
                System.out.println("bitmap available : " + j);
                /*if(j==2)
                {
                    prefix=responsebody.substring(0,2);
                    System.out.println("prefix: " + prefix);
                    num = Integer.parseInt(prefix);
                    n=n+2;
                    ResponseFields.field02=responsebody.substring(n,n+num);
                    System.out.println("field02: " + ResponseFields.field02);
                    n=n+num;
                }*/
                if(j==5)
                {
                    ResponseFields.field05=responsebody.substring(n,n+13);
                    Log.d(TAG,"field05: " +ResponseFields.field05);
                    n=n+13;
                }
                else if(j==7)
                {
                    ResponseFields.field07=responsebody.substring(n,n+10);
                    Log.d(TAG,"field07: " +ResponseFields.field07);
                    n=n+10;
                }
                else if(j==11)
                {
                    ResponseFields.field11=responsebody.substring(n,n+6);
                    Log.d(TAG,"field11: " +ResponseFields.field11);
                    n=n+6;
                }
                else if(j==15)
                {
                    ResponseFields.field15=responsebody.substring(n,n+6);
                    Log.d(TAG,"field15: " +ResponseFields.field15);
                    n=n+6;
                }
                else if(j==30)
                {
                    ResponseFields.field30=responsebody.substring(n,n+13);
                    Log.d(TAG,"field30: " +ResponseFields.field30);
                    n=n+13;
                }
                else if(j==37)
                {
                    ResponseFields.field37=responsebody.substring(n,n+12);
                    Log.d(TAG,"field37: " +ResponseFields.field37);
                    n=n+12;
                }
                else if(j==38)
                {
                    ResponseFields.field38=responsebody.substring(n,n+6);
                    Log.d(TAG,"field38: " +ResponseFields.field38);
                    n=n+6;
                }
                else if(j==39)
                {
                    ResponseFields.field39=responsebody.substring(n,n+3);
                    Log.d(TAG,"field39: " +ResponseFields.field39);
                    n=n+3;
                }
                else if(j==41)
                {
                    ResponseFields.field41=responsebody.substring(n,n+8);
                    Log.d(TAG,"field41: " +ResponseFields.field41);
                    n=n+8;
                }
                else if(j==44)
                {
                    prefix=responsebody.substring(n,n+2);
                    num=Integer.parseInt(prefix);
                    n=n+2;
                    ResponseFields.field44=responsebody.substring(n,n+num);
                    Log.d(TAG,"field44: " +ResponseFields.field44);
                }
            }
        }
    }
}