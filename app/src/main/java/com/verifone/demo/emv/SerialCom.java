package com.verifone.demo.emv;

import android.os.RemoteException;
import android.util.Log;

import com.verifone.demo.emv.Utilities.ServiceHelper;
import com.verifone.demo.emv.Utilities.ToastUtil;
import com.verifone.demo.emv.Utilities.Utility;
import com.vfi.smartpos.deviceservice.aidl.ExternalSerialConst;
import com.vfi.smartpos.deviceservice.aidl.IExternalSerialPort;
import com.vfi.smartpos.deviceservice.aidl.ISerialPort;
import com.vfi.smartpos.deviceservice.aidl.IUsbSerialPort;
import com.vfi.smartpos.deviceservice.aidl.SerialDataControl;

import com.verifone.demo.emv.transaction.TransBasic;

import java.util.Arrays;

/**
 * Created by Simon on 2019/2/15.
 */

public class SerialCom {
    static final String TAG = "SerialCom";

    IExternalSerialPort iExternalSerialPort = null;    // for RS232 in the Base
    ISerialPort iSerialPort = null;    // for USB cable, the UART port the PC side. need install the driver
    IUsbSerialPort iUsbSerialPort = null;  // for OTG+USB2Serial

    int bps;
    int par;
    int dbs;

    String mUARTType;

    boolean isOpend = false;

    enum SerialType {
        UART,
        RS232,
        OTGSerial
    };

    SerialType serialType;


    void initialize(SerialType serialType, int bps, int par, int dbs ) {
        if( isOpend ){
            if( this.serialType != serialType
                    || this.bps != bps
                    || this.par != par
                    || this.dbs != dbs ) {
                close();
            }
        }
        this.serialType = serialType;
        this.bps = bps;
        this.par = par;
        this.dbs = dbs;
    }

    void initialize(SerialType serialType, String uartType, int bps, int par, int dbs ){
        initialize(serialType, bps, par, dbs);
        mUARTType = uartType;
    }

    boolean open(){
//        if( isOpend )
//            return true;
        Log.d(TAG, "try open" + " serial type:" + serialType.ordinal() );


        boolean ret = false;
        switch ( serialType ){
            case UART:

                if (mUARTType != null){
                    iSerialPort = ServiceHelper.getInstance().getSerialPort(mUARTType);
                    Log.d(TAG, "try open serial port protocol type: "+ mUARTType);
                }else {
                    iSerialPort = ServiceHelper.getInstance().getSerialPort("rs232");
                }

                if (iSerialPort == null){
                    ToastUtil.toast("Illegal protocol name!");
                    return false;
                }

                try {
                    if( iSerialPort.open() ) {
                        ret = iSerialPort.init(115200, 0, 8);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                break;
            case RS232:
                if( null == iExternalSerialPort ) {
                    iExternalSerialPort = ServiceHelper.getInstance().getExternalSerialPort();
                }
                try {
                    if( ExternalSerialConst.MODE_TRANSPARENT  == iExternalSerialPort.setExtPinpadPortMode( ExternalSerialConst.MODE_TRANSPARENT ) ) {   // normal mode
                        // set ok
                        SerialDataControl dataControl = new SerialDataControl(ExternalSerialConst.BD115200, ExternalSerialConst.DATA_8, ExternalSerialConst.DSTOP_1, ExternalSerialConst.DPARITY_N);
                        ret = iExternalSerialPort.openSerialPort(ExternalSerialConst.PORT_RS232, dataControl );
                        if( ret ){
                            ret = iExternalSerialPort.isExternalConnected();
                        } else {
                            Log.e(TAG, "error while openSerialPort");
                        }
                    } else {
                        Log.e(TAG, "error open port");
                    }

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case OTGSerial:
                if( null == iUsbSerialPort ) {
                    iUsbSerialPort = ServiceHelper.getInstance().getUsbSerialPort();
                }
                try {
                    ret = iUsbSerialPort.isUsbSerialConnect();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
        }
        if( ret ){
            isOpend = true;
            Log.d(TAG, "open success");
        } else {
            Log.e(TAG, "open fails");
        }
        return ret;
    }

    int read( byte[] buffer, int expectLength, int timeout_ms ) {
        int ret = 0;
        int offset = 0;
        Log.d(TAG, "Try read, expect size:" + expectLength + " , timeout:" + timeout_ms + " ,serial type:" + serialType.ordinal());
        if (!isOpend) {
            open();
        }
        if (isOpend) {
            // read
            switch ( serialType ) {
                case UART:
                case RS232:
//                    byte[] temp = new byte[2];
//                    ret = read(temp, timeout_ms);  // try read 1 byte
//                    while (ret > 0) {
//                        buffer[offset] = temp[0];
//                        ++offset;
//                        ret = read(temp, 500);
//                    }
                    byte[] result = new byte[32];
                    try {
                        int length = iSerialPort.read(result, 8, 10000);
                        System.arraycopy(result, 0, buffer, 0, result.length);
                        Log.d(TAG, "read: length = "+ length +"data: "+ Utility.byte2HexStr(buffer));

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case OTGSerial:
                try {
                    ret = iUsbSerialPort.read(buffer, timeout_ms);
                    Log.d(TAG, "return size:" + ret);
                    offset = ret;
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return offset;
    }
    private int read( byte[] buffer, int timeout_ms ) {
        int ret = 0;
        int expectLength = 1;
            switch ( serialType ){
                case UART:
                    try {
                        ret = iSerialPort.read(buffer,expectLength,timeout_ms);
                        Log.d(TAG, "read temp: " + ret);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    break;
                case RS232:
                    try {
                        ret = iExternalSerialPort.readSerialPort(ExternalSerialConst.PORT_RS232, buffer, expectLength);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case OTGSerial:
                    try {
                        ret = iUsbSerialPort.read(buffer, timeout_ms );
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        Log.d(TAG, "return size:" + ret);
        return ret;
    }

    int write( byte[] buffer, int length ){
        int ret = 0;
        Log.d(TAG, "try write size:" + length + " ,serial type:" + serialType.ordinal() );
        if( !isOpend ){
            open();
        }
        if( isOpend ){
            // write
            switch ( serialType ){
                case UART:
                    try {
                        ret = iSerialPort.write(buffer, length );
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case RS232:
                    try {
                        ret = iExternalSerialPort.writeSerialPort(ExternalSerialConst.PORT_RS232, buffer, length);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case OTGSerial:
                    try {
                        iUsbSerialPort.write(buffer);
                        ret = buffer.length;
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
        Log.d(TAG, "write return:" + ret );
        return ret;
    }

    void clearBuffer(){
        switch ( serialType ){
            case UART:
                try {
                    iSerialPort.clearInputBuffer();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case RS232:

                break;
            case OTGSerial:
                //
                break;
        }
    }

    void close(){
        switch ( serialType ){
            case UART:
                try {
                    iSerialPort.close();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case RS232:
                try {
                    iExternalSerialPort.closeSerialPort( ExternalSerialConst.MODE_TRANSPARENT );
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case OTGSerial:
                //
                break;
        }
        isOpend = false;
    }




}
