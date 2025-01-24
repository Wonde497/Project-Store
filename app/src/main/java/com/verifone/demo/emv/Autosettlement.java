package com.verifone.demo.emv;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import com.verifone.demo.emv.basic.common;
import com.verifone.demo.emv.basic.errordialog;
import com.verifone.demo.emv.transaction.CheckCardActivity;
import com.verifone.demo.emv.transaction.TransBasic;

public class Autosettlement extends BroadcastReceiver {
    MediaPlayer mp;
    Context context;
    static String TAG = "AUTO";
    SharedPreferences.Editor editor;
    @Override
     public void onReceive(Context context, Intent intent) {
        this.context=context;
      //   Toast.makeText(context, " Alarmmmm",Toast.LENGTH_LONG).show();
        SharedPreferences preferences = context.getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
        editor = preferences.edit();
        Log.d(TAG, "Settl_Time...."+preferences.getString("Settl_Time", "none"));
         if(preferences.getBoolean("issettlmenton", true)) {
            editor.putString("Txn_Menu_Type","Settlement");
            editor.putString("txn_type","SETTLEMENT_GBE");
            editor.putString("printtype","settlement");
            editor.commit();
            common common = new common(context);
            common.settlementGBE();
             //common.summaryreport();
            scheduleAlarms(context, AlarmManager.INTERVAL_DAY);
          }else
          {
               Log.d(TAG, "SETTLEMENT IS DISABLED");
              Toast.makeText(context, "SETTLEMENT IS DISABLED", Toast.LENGTH_SHORT).show();
              scheduleAlarms(context, AlarmManager.INTERVAL_DAY);
          }

         }

     public void scheduleAlarms(Context ctxt ,long  duration) {
        editor.putLong("Alarm",System.currentTimeMillis()+duration);
        editor.apply();
        AlarmManager mgr = (AlarmManager)ctxt.getSystemService(Context.ALARM_SERVICE);
        Intent i=new Intent(ctxt, Autosettlement.class);
        PendingIntent pi=PendingIntent.getBroadcast(ctxt, 0, i, 0);
        Intent i2=new Intent(ctxt, MenuActivity.class);
        PendingIntent pi2=PendingIntent.getActivity(ctxt, 0, i2, 0);
        AlarmManager.AlarmClockInfo ac=new AlarmManager.AlarmClockInfo(System.currentTimeMillis()+duration, pi2);
        mgr.setAlarmClock(ac, pi);

      }

}