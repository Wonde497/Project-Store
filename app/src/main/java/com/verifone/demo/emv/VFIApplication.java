package com.verifone.demo.emv;
/*
 *  author: Derrick
 *  Time: 2019/5/27 14:09
 */

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.verifone.demo.emv.Utilities.ServiceHelper;
import com.verifone.demo.emv.Utilities.ToastUtil;
import com.verifone.demo.emv.transaction.AppParams;
import com.verifone.demo.emv.transaction.TransBasic;
import com.vfi.smartpos.deviceservice.aidl.IDeviceService;

public class VFIApplication extends Application {


    private static final String TAG = "VFIApplication";

    private static VFIApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "do Application");
        application = this;

        // Binding service, get instances from service.
        ServiceHelper.getInstance().initServiceHelper(application);
        // A callback to do init Transaction basic logic and init some App params
        ServiceHelper.getInstance().setOnServiceConnectedListener(new ServiceHelper.OnServiceConnectedListener() {
            @Override
            public void onConnected() {
                SharedPreferences sharedPreferences=getSharedPreferences("transaction", Context.MODE_PRIVATE);

                TransBasic.getInstance(sharedPreferences).initTransBasic( handler , application);
                AppParams.getInstance().initAppParam();

            }
        });

        ToastUtil.init(getApplicationContext());
    }

    public static VFIApplication getInstance(){
        return application;
    }


    // log & display
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String string = msg.getData().getString("string");
            super.handleMessage(msg);
            Log.d(TAG, msg.getData().getString("msg"));
            Toast.makeText(VFIApplication.this, msg.getData().getString("msg"), Toast.LENGTH_SHORT).show();
        }
    };

    public static void maskHomeKey(boolean isEnable){

    }

    private void setSystemTime() {
        try {
            ServiceHelper.getInstance().getDeviceInfo().updateSystemTime("20190525", "100433");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
