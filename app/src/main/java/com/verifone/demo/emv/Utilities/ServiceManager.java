package com.verifone.demo.emv.Utilities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Simon on 2019/3/5.
 */

public class ServiceManager {

    Context context;

    ServiceManagerIF serviceManagerIF;

    public ServiceManager( Context context, ServiceManagerIF serviceManagerIF){
        this.context = context;
        this.serviceManagerIF = serviceManagerIF;
    }

    // connect service -- start
    public void connectVFService(){
        Intent intent = new Intent();
        intent.setAction("com.vfi.smartpos.device_service");
        intent.setPackage("com.vfi.smartpos.deviceservice");
        boolean isSucc = context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
        if (!isSucc) {
            Log.i("TAG", "deviceService connect fail!");
        } else {
            Log.i("TAG", "deviceService connect success");
            serviceManagerIF.onBindSuccess();
        }


    }
    /**
     * \Brief connect the VFI-Service and set some devices
     */
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            serviceManagerIF.onConnect(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    // connect service -- end
}
