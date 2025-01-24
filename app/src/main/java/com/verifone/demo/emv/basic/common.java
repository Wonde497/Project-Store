package com.verifone.demo.emv.basic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.verifone.demo.emv.DBHandler;
import com.verifone.demo.emv.MenuActivity;
import com.verifone.demo.emv.Utilities.Utility;
import com.verifone.demo.emv.data_handlers.User;
import com.verifone.demo.emv.loadingactivity;
import com.verifone.demo.emv.transaction.TransBasic;

import java.util.ArrayList;

public class common
{
  String amount="",type="";
  public static ArrayList<User> summaryarray = new ArrayList<>();
   public static float Pamount=0,Rfamount=0,Rvamount=0,Camount=0,Prthamount=0,prcmpamount=0,Pcbamount=0;
    public static int Sale_count=0, Void_count=0;
    private DBHandler dbHandler;
    TransBasic transBasic;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;
    ISO8583 iso85831;
    public common(Context con)
    {

        Log.d("commonclass", "commmmmmmmmmmmmmmm. ");
        context=con;
        dbHandler = new DBHandler(con);
        //iso85831=new ISO8583(con);
        preferences = con.getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
        editor = preferences.edit();
        transBasic = TransBasic.getInstance(preferences);

    }
      public  void summaryreport()
       {

        editor.putString("printtype","summryreport");
        editor.commit();
        if (dbHandler.gettransactiondata().size() > 0)
        {

             if (dbHandler.gettransactiondata().size() > 10)
             {
                Intent intent1=new Intent(new Intent(context, loadingactivity.class));
                intent1.putExtra("database",true);
                context.startActivity(intent1);
             }

            loaddatacash l=new loaddatacash();
            l.execute();

        }else
        {
            Toast.makeText(context,"NO RECORD FOUND", Toast.LENGTH_LONG).show();
           // transBasic.printTest(1);
        }

        }

    public void settlementGBE()
    {
        Log.d("commonclass", "commmmmmmmmmmmmmmmendofday. ");
        editor.putString("Txn_Menu_Type","SETTLEMENT_GBE");
        editor.putString("txn_type","SETTLEMENT_GBE");
        editor.putString("printtype","settlement");
        editor.commit();
        // editor.putString("Txn_Menu_Type","SETTLEMENT");
        // editor.commit();

        if (dbHandler.gettransactiondata().size() > 0)
        {
            if (dbHandler.gettransactiondata().size() > 10)
            {
                Intent intent1=new Intent(new Intent(context, loadingactivity.class));
                intent1.putExtra("database",true);
                context.startActivity(intent1);
            }
            loaddatacash l=new loaddatacash();
            l.execute();
            Log.d("common", "Call SettlmentRequest");
           /* try {
                TransBasic.getInstance(preferences).SettlmentRequest();
            } catch (RemoteException e) {
                e.printStackTrace();
            }*/
            //transBasic.SettlmentRequest();

        }else {
            /*editor.putString("printtype","endoffday");
            editor.commit();
            //Toast.makeText(context,"NO RECORD FOUND", Toast.LENGTH_LONG).show();
            transBasic.printTest(1);*/
            Log.d("Make Transaction", "Make Transaction No Data found"+Pamount);
            Message msg=new Message();
            msg.getData().putString("msg","DO TRANSACTION! NO DATA FOUND");
            TransBasic.getInstance(preferences).commondialog.sendMessage(msg);

        }

    }

  class loaddatacash extends AsyncTask<String, Integer, String> {
    String urlll = "";
    Context context;
    public loaddatacash() {

    }

    @Override
    protected String doInBackground(String... params)
    {
        ArrayList<User> summaryarray = dbHandler.gettransactiondata();
        int datasize= summaryarray.size();
        Pamount=0;Rfamount=0;Rvamount=0;Camount=0; Prthamount=0;prcmpamount=0;Pcbamount=0;Sale_count=0; Void_count=0;

        for (int i = 0; i < datasize; i++)
        {
            int check = i+1;
            String amount=summaryarray.get(datasize-check).getField_04();
            String transactiontype=summaryarray.get(datasize-check).getTxn_type();
            String lastflg=summaryarray.get(datasize-check).getLastflag();

           // Log.d("commonclass", "Amex,amount "+amount);
            if (lastflg.equals("0") && transactiontype.equals("PURCHASE"))
            {
                //transactiondata.unpack(dbHandler.gettransactiondata().get(dbHandler.gettransactiondata().size() - check).getRecievedtran());
                Log.d("commonclass", "Amex,Amount... "+ Utility.getReadableAmount(amount));
                Pamount=Pamount + Float.parseFloat(Utility.getReadableAmount(amount));
                Log.d("commonclass", "Amex,Pamount..... "+Pamount);

                Sale_count=Sale_count+1;
                Log.d("commonclass", "Amex,Sale_count..... "+Sale_count);
            }
            else if (lastflg.equals("0") && transactiontype.equals("REFUND"))
            {
                // transactiondata.unpack(dbHandler.gettransactiondata().get(dbHandler.gettransactiondata().size() - check).getRecievedtran());
                Rfamount=Rfamount + Float.parseFloat(Utility.getReadableAmount(amount));
                Log.d("commonclass", "Amex,Rfamount..... "+Rfamount);

                Void_count=Void_count+1;
                Log.d("commonclass", "Amex,Void_count..... "+Void_count);
            }
            else if (lastflg.equals("0") && transactiontype.equals("REVERSAL"))
            {
                // transactiondata.unpack(dbHandler.gettransactiondata().get(dbHandler.gettransactiondata().size() - check).getRecievedtran());
                Rvamount=Rvamount + Float.parseFloat(Utility.getReadableAmount(amount));
                Log.d("commonclass", "Amex,Rvamount..... "+Rvamount);

                Void_count=Void_count+1;
                Log.d("commonclass", "Amex,Void_count..... "+Void_count);
            }
            else if (lastflg.equals("0") && transactiontype.equals("PRE_AUTH"))
            {
                //  transactiondata.unpack(dbHandler.gettransactiondata().get(dbHandler.gettransactiondata().size() - check).getRecievedtran());
                Prthamount=Prthamount + Float.parseFloat(Utility.getReadableAmount(amount));
                Log.d("commonclass", "Amex,Prthamount..... "+Prthamount);

                Void_count=Void_count+1;
                Log.d("commonclass", "Amex,Void_count..... "+Void_count);
            }
            else if (lastflg.equals("0") && transactiontype.equals("PRE_AUTH_COMPLETION"))
            {
                //transactiondata.unpack(dbHandler.gettransactiondata().get(dbHandler.gettransactiondata().size() - check).getRecievedtran());
                prcmpamount=prcmpamount + Float.parseFloat(Utility.getReadableAmount(amount));
                Log.d("commonclass", "Amex,prcmpamount..... "+prcmpamount);

                Sale_count=Sale_count+1;
                Log.d("commonclass", "Amex,Sale_count..... "+Sale_count);
            }
            else if (lastflg.equals("0") && transactiontype.equals("PURCHASE_WITH_CASHBACK"))
            {
                // transactiondata.unpack(dbHandler.gettransactiondata().get(dbHandler.gettransactiondata().size() - check).getRecievedtran());
                Pcbamount=Pcbamount + Float.parseFloat(Utility.getReadableAmount(amount));
                Log.d("commonclass", "Amex,Pcbamount..... "+Pcbamount);
            }
            else if (lastflg.equals("0") && transactiontype.equals("CASH_ADVANCE"))
            {
                // transactiondata.unpack(dbHandler.gettransactiondata().get(dbHandler.gettransactiondata().size() - check).getRecievedtran());
                Camount=Camount + Float.parseFloat(Utility.getReadableAmount(amount));
                Log.d("commonclass", "Amex,Camount..... "+Camount);

                Sale_count=Sale_count+1;
                Log.d("commonclass", "Amex,Sale_count..... "+Sale_count);
            }else
            {
                Log.d("commonclass", "BALANCE INQUIRY TRANSACTION!");
            }
            //check++;
            Log.d("commonclass", " check...,"+ check);
        }

          return "";

        }
           /* @Override
            protected void onPublish*/

    @Override
    protected void onProgressUpdate(Integer... values) {
      super.onProgressUpdate(values);
      //String st=new String(String.valueOf(values).getBytes(),StandardCharsets.UTF_8);
      // prog.setText(st+"%");
      Log.d("menuactiv", "load .."+values[0]);
    }

    @Override
    protected void onPostExecute(String result)
    {
      super.onPostExecute(result);
        Log.d("menuactiv", "load finished");
        Log.d("Pamountttt", ""+Pamount);
        Log.d("reverseamountttt", ""+Rvamount);

        if(preferences.getString("printtype","").equals("summryreport"))
        {
            transBasic.printTest(1);
        }
         if(preferences.getString("printtype","").equals("settlement")) {
            TransBasic.getInstance(preferences).settlementRequest();
         }
         if(TransBasic.act!=null)
         {
         TransBasic.act.finish();
         }
      //  Toast.makeText(getApplicationContext(), " image is downloaded successfully",Toast.LENGTH_LONG).show();
       }
  }
}
