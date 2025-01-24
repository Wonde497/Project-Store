package com.verifone.demo.emv.Support_menu;

import static android.content.Context.ALARM_SERVICE;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.verifone.demo.emv.Autosettlement;
import com.verifone.demo.emv.DBTerminal;
import com.verifone.demo.emv.MenuActivity;
import com.verifone.demo.emv.Utilities.ToastUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class Settlment extends Fragment {

    static String TAG = "SETTLMENT";
    View Ttitel;
    public String user_type;
    String menu_title;
    private EditText settlment;
    private TextView ttitel;
    private TextView tv,tv1,tv2;
    FrameLayout frame;
    private String Selectedtime="";
    public static String Settl_Time="";
    public static String Settl_TimeStatus="0";
    private static DBTerminal dbTerminal;
    private static DBTerminal.Terminal_functions1 Terminal_fun;
    View settlment_frag;
    int row_cnt;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private String lenhourOfDay="",lenminute="";
    private Button pickTimeBtn,set,Cancle;
    private TextView selectedTimeTV,pickTimeTV;
    TimePicker alarmTimePicker;
    final Calendar calendar=Calendar.getInstance();;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        user_type = getArguments().getString("user_type");
        Log.d(TAG, user_type);
        settlment_frag= inflater.inflate(R.layout.fragment_settlment, container, false);
        tv = (TextView) settlment_frag.findViewById(R.id.toolbar);
        tv1 = (TextView) settlment_frag.findViewById(R.id.tv1);
        tv2 = (TextView) settlment_frag.findViewById(R.id.tv2);
        dbTerminal = new DBTerminal(settlment_frag.getContext());

        preferences = settlment_frag.getContext().getSharedPreferences("Shared_data", Context.MODE_PRIVATE);

        pickTimeBtn = settlment_frag.findViewById(R.id.picktime);
        selectedTimeTV = settlment_frag.findViewById(R.id.Tvsettlment);

        tv.setText("SETTLMENT TIME");
        tv1.setText("ENTER SETTLMENT TIME ");
        tv2.setText("24 HOURS FORMAT ");
        Log.d(TAG, user_type);

        pickTime();
        settime();
        Selectedtime=preferences.getString("Selectedtime","18:00:00");
        Log.d(TAG, "Selectedtime  "+Selectedtime);
        if(!Selectedtime.equals(""))
        {
            selectedTimeTV.setText(Selectedtime);
        }else {
            selectedTimeTV.setText("18:00:00");
        }
        return settlment_frag;
    }

       private void settime() {

        settlment = (EditText) settlment_frag.findViewById(R.id.Tvsettlment);
        set = (Button) settlment_frag.findViewById(R.id.idBtnset);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // int second=(int)(GetTimeDifference()/1000);
                long time=GetTimeDifference();
                scheduleAlarms(settlment_frag.getContext(),time);
                setAlarm(time);
                // startAlert(second);
                // startAlert(4);

                 }
                });

         }


       void scheduleAlarms(Context ctxt,long duration) {
        editor = preferences.edit();
        editor.putLong("Alarm",System.currentTimeMillis()+duration);
        editor.apply();
        AlarmManager mgr=
                (AlarmManager)ctxt.getSystemService(Context.ALARM_SERVICE);
        Intent i=new Intent(ctxt, Autosettlement.class);
        PendingIntent pi=PendingIntent.getBroadcast(ctxt, 0, i, 0);
        Intent i2=new Intent(ctxt, MenuActivity.class);
        PendingIntent pi2=PendingIntent.getActivity(ctxt, 0, i2, 0);

        AlarmManager.AlarmClockInfo ac= new AlarmManager.AlarmClockInfo(System.currentTimeMillis()+duration, pi2);
        mgr.setAlarmClock(ac, pi);

        }


      public long GetTimeDifference(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("kk:mm:ss");
        // SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss aaa");
        long lastmilisecond;

        Date date1, date2;
        try {
            date1 = simpleDateFormat.parse(selectedTimeTV.getText().toString());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        try {
            date2 = simpleDateFormat.parse(simpleDateFormat.format(calendar.getTime()).toString());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        lastmilisecond=date1.getTime() - date2.getTime();
        if(lastmilisecond<0){
            lastmilisecond= 86400000+lastmilisecond;
        }

        int days = (int) (lastmilisecond / (1000*60*60*24));
        int hour = (int) (lastmilisecond / (1000*60*60));
        int minute = (int) (lastmilisecond / (1000*60));

        int remainedminute = (int)(lastmilisecond- (hour*60*60*1000))/(1000*60);
        ToastUtil.toastOnUiThread((Activity) settlment_frag.getContext(), "Settlement is After "+hour+":"+remainedminute);

        return lastmilisecond;

    }

    public void startAlert(int i){
/*
        long time;
        time = (calendar.getTimeInMillis() - (calendar.getTimeInMillis() % 60000));
        if (System.currentTimeMillis() > time) {
            // setting time as AM and PM
            if (Calendar.AM_PM == 0)
                time = time + (1000 * 60 * 60 * 12);
            else
                time = time + (1000 * 60 * 60 * 24);
        }
        Intent intent = new Intent(settlment_frag.getContext(), Autosettlement.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                settlment_frag.getContext(), 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) settlment_frag.getContext().getSystemService(ALARM_SERVICE);
         assert alarmManager != null;
         Calendar calendar = Calendar.getInstance();
         calendar.setTimeInMillis(System.currentTimeMillis());
         calendar.set(Calendar.MINUTE,calendar.getTime().getMinutes()+1);

         alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()/*System.currentTimeMillis()*/
                /*+ (i * 1000L),AlarmManager.INTERVAL_DAY, pendingIntent);

        Toast.makeText(settlment_frag.getContext(), "Alarm set in " + calendar.getTime().getMinutes() + " seconds",Toast.LENGTH_LONG).show();
*/
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.MINUTE,calendar.getTime().getMinutes()+1);
        // calendar.set(Calendar.HOUR,calendar.getTime().getHours()+1);


        Intent intent = new Intent(settlment_frag.getContext(), Autosettlement.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                settlment_frag.getContext(), 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) settlment_frag.getContext().getSystemService(ALARM_SERVICE);

        /* alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()/*System.currentTimeMillis()*/
        /*+ (i * 1000L)*//*AlarmManager.INTERVAL_HOUR*//*,AlarmManager.INTERVAL_DAY, pendingIntent);*/

        alarmManager.setExact(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+AlarmManager.INTERVAL_HOUR,pendingIntent);

        //  Toast.makeText(settlment_frag.getContext(), "Alarm set in " + calendar.getTime().getMinutes() + " minute",Toast.LENGTH_LONG).show();
    }
    private void pickTime()
    {
//for only time .........................
        pickTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting
                final Calendar c = Calendar.getInstance();

                // on below line we are getting our hour, minute.
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(settlment_frag.getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                // on below line we are setting selected time
                                // in our text view.
                                lenhourOfDay=String.valueOf(hourOfDay);
                                lenminute=String.valueOf(minute);
                                if((lenhourOfDay.length()!=2) && lenminute.length()!=2)
                                {
                                    selectedTimeTV.setText("0"+hourOfDay + ":0" + minute +":00");
                                }
                                else if(lenhourOfDay.length()!=2)
                                {
                                    selectedTimeTV.setText("0"+hourOfDay + ":" + minute +":00");
                                }else if(lenminute.length()!=2)
                                {
                                    selectedTimeTV.setText(hourOfDay + ":0" + minute +":00");
                                } else
                                {
                                    selectedTimeTV.setText(hourOfDay + ":" + minute +":00");
                                }
                                editor = preferences.edit();
                                editor.putString("Selectedtime",selectedTimeTV.getText().toString());
                                editor.commit();

                                Log.d(TAG, "Selectedtime............"+preferences.getString("Selectedtime","1"));

                            }
                        }, hour, minute, true);

                timePickerDialog.show();
            }
        });

    }


    /*private void pickTime()
    {
//for only time .........................
   pickTimeBtn.setOnClickListener(new View.OnClickListener() {
                 @Override![](C:/Users/kaleb/Downloads/Tikscroller (3).png)
                 public void onClick(View v) {

                     int hour = calendar.get(Calendar.HOUR_OF_DAY);
                     int minute = calendar.get(Calendar.MINUTE);

                     // on below line we are initializing our Time Picker Dialog
                     TimePickerDialog timePickerDialog = new TimePickerDialog(settlment_frag.getContext(),
                             new TimePickerDialog.OnTimeSetListener() {
                                 @Override
                                 public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                     // on below line we are setting selected time
                                     // in our text view.
                                     lenhourOfDay=String.valueOf(hourOfDay);
                                     lenminute=String.valueOf(minute);
                                     if((lenhourOfDay.length()!=2) && lenminute.length()!=2)
                                     {
                                         selectedTimeTV.setText("0"+hourOfDay + ":" + minute +"0:00");
                                     }
                                     else if(lenhourOfDay.length()!=2)
                                     {
                                         selectedTimeTV.setText("0"+hourOfDay + ":" + minute +":00");
                                     }else if(lenminute.length()!=2)
                                     {
                                         selectedTimeTV.setText(hourOfDay + ":" + minute +"0:00");
                                     } else
                                     {
                                         selectedTimeTV.setText(hourOfDay + ":" + minute +":00");
                                     }

                                 }

                             }, hour, minute, true);


                     timePickerDialog.show();
                     calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
                     calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
                 }
             });


        // Log.d(TAG, "out off initScreen2");
    }*/
    /*
    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //Passing data
        String title = mTitle.getText().toString();
        String message = mMessage.getText().toString();

        id = (int) System.currentTimeMillis();
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("Title", title);
        intent.putExtra("Message", message);
        intent.putExtra("ID", id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);

    }
     */

    //preferences = settlment_frag.getContext().getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
    //preferences= settlment_frag.getContext().getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
    // preferences.getString("Settl_Time", "none").equals(Time) && pref.getString("Settl_TimeStatus", "none").equals("1"))
    // Settl_Time=settlment_frag.getContext().getString("Settl_Time", "none");
    //  Settl_TimeStatus=preferences.getString("Settl_TimeStatus", "none");
    public void setAlarm(long time){

        preferences = settlment_frag.getContext().getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putLong("Alarm",System.currentTimeMillis()+time);
        editor.commit();

        //Log.d(TAG,"difference Time: " +System.currentTimeMillis());

    }




}

