package com.verifone.demo.emv.crypto;

import android.os.RemoteException;
import android.util.Log;

import com.verifone.demo.emv.Utilities.Utility;

/**
 * Created by Simon on 2019/4/24.
 */

public class KeyLoadKey extends KeyBasic {
    private static final String TAG = "KeyLoadKey";
    protected KeyLoadKey(String key, int slot) {
        super(key, slot);
    }

    @Override
    protected void initKeyType() {
        keyType.keyExist = 13;
        keyType.clearKey = -1;
        keyType.loadWorkKey = -1;
    }

    @Override
    public boolean load() {
        boolean ret = true; // set true as the default value
        if( key.length() <= 0 ){
            return ret;
        }
        if( slot < 0 ){
            slot = 0;
        }

        try {
            ret = ipinpad.loadTEK( slot, Utility.hexStr2Byte(key), null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return ret;
    }

    @Override
    public byte[] encrypt(int slot, String plain) {
        Log.e(TAG, "no encrypt for this object");
        return null;
    }
}
