package com.verifone.demo.emv.Utilities;
/*
 *  author: Derrick
 *  Time: 2019/5/27 14:26
 */

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.vfi.smartpos.deviceservice.aidl.IBeeper;
import com.vfi.smartpos.deviceservice.aidl.IDeviceInfo;
import com.vfi.smartpos.deviceservice.aidl.IDeviceService;
import com.vfi.smartpos.deviceservice.aidl.IEMV;
import com.vfi.smartpos.deviceservice.aidl.IExternalSerialPort;
import com.vfi.smartpos.deviceservice.aidl.IInsertCardReader;
import com.vfi.smartpos.deviceservice.aidl.ILed;
import com.vfi.smartpos.deviceservice.aidl.IMagCardReader;
import com.vfi.smartpos.deviceservice.aidl.IPinpad;
import com.vfi.smartpos.deviceservice.aidl.IPrinter;
import com.vfi.smartpos.deviceservice.aidl.IRFCardReader;
import com.vfi.smartpos.deviceservice.aidl.IScanner;
import com.vfi.smartpos.deviceservice.aidl.ISerialPort;
import com.vfi.smartpos.deviceservice.aidl.IUsbSerialPort;
import com.vfi.smartpos.deviceservice.aidl.key_manager.IDukpt;


/*
* Binding your application with service, get instances from service.
* Also it's a singleton to store the instance of device, you could get any instance whenever & wherever you want
* For example: just call: ServiceHelper.getInstance().getPinpad() to get an instance of Pinpad.
* */
public class ServiceHelper {

    private static final String TAG = "ServiceHelper";

    private static ServiceHelper instance;
    private Application mApplication;

    private IPinpad pinpad;
    private IEMV iemv;
    private IBeeper beeper;
    private ILed led;
    private IPrinter printer;
    private IDeviceInfo deviceInfo;
    private ISerialPort serialPort;
    private IUsbSerialPort usbSerialPort;
    private IExternalSerialPort externalSerialPort;
    private IScanner scanner;
    private IMagCardReader magCardReader;
    private IInsertCardReader insertCardReader;
    private IRFCardReader rfCardReader;
    private IDeviceService deviceService;
    private IDukpt iDukpt;
    private IDeviceInfo iDeviceInfo;

    OnServiceConnectedListener onServiceConnectedListener;

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected, ServiceHelper init");
            deviceService = IDeviceService.Stub.asInterface(service);

            getAllDeviceInstance(deviceService);

            // A callback for initializing some preparation.
            onServiceConnectedListener.onConnected();

            try {
                String s = ServiceHelper.getInstance().getDeviceInfo().getCertificate(0);
                Log.d(TAG, "Cert:" + s);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, name.getPackageName() + " is disconnected");
            deviceService = null;
        }
    };

    private ServiceHelper(){
    }


    public static synchronized ServiceHelper getInstance() {
        if ( null == instance){
            instance = new ServiceHelper();
        }
        return instance;
    }

    public void initServiceHelper(Application application){
        mApplication = application;
        Log.i(TAG, "Start to bind service..., deviceService=" + (null != deviceService));
        if (null != deviceService) {
            return;
        }
        Intent intent = new Intent();
//        intent.setAction("com.vfi.smartpos.device_service");
//        intent.setPackage("com.vfi.smartpos.deviceservice");
        // or
        ComponentName componentName = new ComponentName("com.vfi.smartpos.deviceservice",
                "com.verifone.smartpos.service.VerifoneDeviceService");
        intent.setComponent(componentName);

        boolean isSucc = mApplication.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        if (!isSucc) {
            Log.i(TAG, "deviceService bind failed");
        } else {
            Log.i(TAG, "deviceService bind success");
        }
    }

    private void getAllDeviceInstance(IDeviceService deviceService){

        try {
            this.iemv = deviceService.getEMV();
            this.pinpad = deviceService.getPinpad(5);
            this.beeper = deviceService.getBeeper();
            this.led = deviceService.getLed();
            this.printer = deviceService.getPrinter();
            this.deviceInfo = deviceService.getDeviceInfo();
            this.scanner = deviceService.getScanner(0);
            this.iDukpt = deviceService.getDUKPT();
            this.serialPort = deviceService.getSerialPort("usb-rs232");
            this.usbSerialPort = deviceService.getUsbSerialPort();
            this.externalSerialPort = deviceService.getExternalSerialPort();
            this.iDeviceInfo = deviceService.getDeviceInfo();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public interface OnServiceConnectedListener{
        void onConnected();
    }

    public void setOnServiceConnectedListener(OnServiceConnectedListener onServiceConnectedListener) {
        this.onServiceConnectedListener = onServiceConnectedListener;
    }

    public IPinpad getPinpad() {
        return pinpad;
    }

    public void setPinpad(IPinpad pinpad) {
        this.pinpad = pinpad;
    }

    public IEMV getIemv() {
        return iemv;
    }

    public void setIemv(IEMV iemv) {
        this.iemv = iemv;
    }

    public IBeeper getBeeper() {
        return beeper;
    }

    public void setBeeper(IBeeper beeper) {
        this.beeper = beeper;
    }

    public ILed getLed() {
        return led;
    }

    public void setLed(ILed led) {
        this.led = led;
    }

    public IPrinter getPrinter() {
        return printer;
    }

    public void setPrinter(IPrinter printer) {
        this.printer = printer;
    }

    public IDeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(IDeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public ISerialPort getSerialPort() {
        return serialPort;
    }

    public ISerialPort getSerialPort(String type) {
        try {
            return deviceService.getSerialPort(type);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getSerialPort: failed");
        return null;
    }

    public void setSerialPort(ISerialPort serialPort) {
        this.serialPort = serialPort;
    }

    public IScanner getScanner() {
        return scanner;
    }

    public void setScanner(IScanner scanner) {
        this.scanner = scanner;
    }

    public IMagCardReader getMagCardReader() {
        return magCardReader;
    }

    public void setMagCardReader(IMagCardReader magCardReader) {
        this.magCardReader = magCardReader;
    }

    public IInsertCardReader getInsertCardReader() {
        return insertCardReader;
    }

    public void setInsertCardReader(IInsertCardReader insertCardReader) {
        this.insertCardReader = insertCardReader;
    }

    public IRFCardReader getRfCardReader() {
        return rfCardReader;
    }

    public void setRfCardReader(IRFCardReader rfCardReader) {
        this.rfCardReader = rfCardReader;
    }

    public IDeviceService getDeviceService() {
        return deviceService;
    }

    public void setDeviceService(IDeviceService deviceService) {
        this.deviceService = deviceService;
    }

    public IDeviceInfo getiDeviceInfo() {
        return iDeviceInfo;
    }

    public void setiDeviceInfo(IDeviceInfo iDeviceInfo) {
        this.iDeviceInfo = iDeviceInfo;
    }

    public void setiDukpt(IDukpt iDukpt) {
        this.iDukpt = iDukpt;
    }

    public IDukpt getiDukpt() {
        return iDukpt;
    }

    public IUsbSerialPort getUsbSerialPort() {
        return usbSerialPort;
    }

    public void setUsbSerialPort(IUsbSerialPort usbSerialPort) {
        this.usbSerialPort = usbSerialPort;
    }

    public IExternalSerialPort getExternalSerialPort() {
        return externalSerialPort;
    }

    public void setExternalSerialPort(IExternalSerialPort externalSerialPort) {
        this.externalSerialPort = externalSerialPort;
    }

    public void setSerialPortType(String type){
        try {

            this.serialPort = deviceService.getSerialPort(type);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
