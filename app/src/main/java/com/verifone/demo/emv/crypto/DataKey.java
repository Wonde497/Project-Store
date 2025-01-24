package com.verifone.demo.emv.crypto;

import android.os.RemoteException;
import android.util.Log;

import com.verifone.demo.emv.Utilities.Utility;
import com.vfi.smartpos.deviceservice.aidl.PinpadKeyType;

/**
 * Created by Simon on 2019/4/19.
 */

public class DataKey extends WorkKey {
    private static final String TAG = "DataKey";

    public DataKey(
            String keyEncrypted,
            int slot,
            int klkSlot,
            String data,
            String dataEncrypted,
            String comment){
        super(null,keyEncrypted,true,slot,klkSlot,data,dataEncrypted, comment);
    }

    public DataKey(
            int slot,
            String data,
            String dataEncrypted,
            String comment){
        super(null,null,false,slot,-1,data,dataEncrypted, comment);
    }

    public DataKey( int slot ){
                super( slot );
    }

    @Override
    protected void initKeyType() {
        keyType.clearKey = 0x30;
        keyType.keyExist = 3;
        keyType.loadWorkKey = PinpadKeyType.TDKEY;
    }

    @Override
    public byte[] encrypt(int slot, String plain) {
        return Encrypt( slot, plain );
    }

    public static byte[] Encrypt(int slot, String plain) {
        if( plain.length() > 0 ){
            try {
                // checkEncrypted key exist
                if( ipinpad.isKeyExist(3, slot) ){
                    // key exist

                } else {
                    Log.e(TAG, "Data key on slot [" + slot +"] doesn't exist" );
                    return null;
                }
                // do plain encrypt
                byte[] ret = ipinpad.encryptTrackData(0, slot, Utility.hexStr2Byte(plain) );
                return ret;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}