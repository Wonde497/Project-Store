package com.verifone.demo.emv;

import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.verifone.demo.emv.Utilities.ServiceHelper;
import com.verifone.demo.emv.Utilities.ToastUtil;
import com.vfi.smartpos.deviceservice.aidl.IDeviceInfo;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class DeviceInfoActivity extends AppCompatActivity {

    private static final String TAG = "DeviceInfoActivity";
    IDeviceInfo iDeviceInfo;
    TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);
        content = findViewById(R.id.content);
        iDeviceInfo = ServiceHelper.getInstance().getiDeviceInfo();

        try {
            String SN = iDeviceInfo.getSerialNo();
            String IMEI = iDeviceInfo.getIMEI();
            String modelType = iDeviceInfo.getModel();
            String batteryLevel = iDeviceInfo.getBatteryLevel();

            ToastUtil.toast("SN = " + SN +", IMEI = "+ IMEI + ",  modelType = "+modelType);
            content.setText("SN = " + SN +", IMEI = "+ IMEI + ",  modelType = "+modelType+"\n,  battery level = "+ batteryLevel);
            Log.d(TAG, "SN = " + SN +", IMEI = "+ IMEI + ",  modelType = "+modelType+",  battery level = "+ batteryLevel);

        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
}
