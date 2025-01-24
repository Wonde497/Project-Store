package com.verifone.demo.emv.crypto;

import android.os.RemoteException;
import android.util.Log;

import com.verifone.demo.emv.Utilities.Utility;

/**
 * Created by Simon on 2019/4/24.
 */

public abstract class WorkKey extends KeyBasic {
    private static final String TAG = "WorkKey";

    public WorkKey(String key,
                   String keyEncrypted,
                   boolean load,
                   int slot,
                   int klkSlot,
                   String plain,
                   String encrypted,
                   String comment) {
        super(key,keyEncrypted,load,slot,klkSlot,plain,encrypted, comment);
    }

    public WorkKey( int slot ){
        super(slot);
    }
    public WorkKey(String keyEncrypted,
                   int slot,
                   int klkSlot,
                   String comment) {
        super(keyEncrypted, slot, klkSlot, comment);
    }

    @Override
    public boolean load() {
        boolean bRet = false;
        Log.d(TAG, "call load" + " of " + toString() );
        // load key
        if( load && slot >= 0 && keyEncrypted.length() > 0 ){
            try {
                bRet = ipinpad.loadWorkKey(keyType.loadWorkKey, klkSlot, slot, Utility.hexStr2Byte(keyEncrypted), null );
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if( bRet ){

            } else {
                Log.e(TAG, "load work key fails");
                // return false;
            }
        }
        return bRet;
    }

}
