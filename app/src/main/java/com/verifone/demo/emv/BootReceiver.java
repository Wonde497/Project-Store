package com.verifone.demo.emv;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent myIntent = new Intent(context, MenuActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(myIntent);
        SharedPreferences preferences = context.getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
       long current= preferences.getLong("Alarm",0)-System.currentTimeMillis();
        scheduleAlarms(context,current);

    }
    public void scheduleAlarms(Context ctxt,long duration) {
        AlarmManager mgr = (AlarmManager)ctxt.getSystemService(Context.ALARM_SERVICE);
        Intent i=new Intent(ctxt, Autosettlement.class);
        PendingIntent pi=PendingIntent.getBroadcast(ctxt, 0, i, 0);
        Intent i2=new Intent(ctxt, MenuActivity.class);
        PendingIntent pi2=PendingIntent.getActivity(ctxt, 0, i2, 0);
        AlarmManager.AlarmClockInfo ac=
                new AlarmManager.AlarmClockInfo(System.currentTimeMillis()+duration/*AlarmManager.INTERVAL_DAY*/,
                        pi2);
        mgr.setAlarmClock(ac, pi);
    }
}