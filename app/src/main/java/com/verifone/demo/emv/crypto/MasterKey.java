package com.verifone.demo.emv.crypto;

import android.os.RemoteException;
import android.util.Log;

import com.verifone.demo.emv.Utilities.Utility;

/**
 * Created by Simon on 2019/4/24.
 */

public class MasterKey extends KeyBasic {
    private static final String TAG = "MasterKey";

    public MasterKey(String key, int slot ){
        super(key, slot );
    }

    public MasterKey(String keyEncrypted,
                     int slot,
                     String klk,
                     int klkIndex,
                     String comment){
        super(keyEncrypted, slot, klk, klkIndex, comment );
    }

    @Override
    protected void initKeyType() {
        keyType.loadWorkKey = -1;
        keyType.clearKey = 0;
        keyType.keyExist = 0;
    }

    @Override
    public boolean load() {
        // load master key
        boolean bRet = true;
        try {
            if( load) {
                if( klk.length() > 0 && klkSlot >= 0 ){
                    // load klk
                    bRet = ipinpad.loadTEK(klkSlot, Utility.hexStr2Byte(klk) , null);

                    // load encrypted master key
                    if( bRet ) {
                        bRet = ipinpad.loadEncryptMainKey(slot, Utility.hexStr2Byte(key), null);
                    }
                } else {
                    bRet = ipinpad.loadMainKey(slot, Utility.hexStr2Byte(key), null );

                }
                if( bRet ){
                    Log.d(TAG, "load master key success.");
                } else {
                    Log.e(TAG, "load master key fails");
                    return false;
                }

            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return bRet;
    }

    // do encrypt with given slot & plain
    @Override
    public byte[] encrypt(int slot, String plain){
        Log.e(TAG, "Not encypt for current object");
        return null;
    }

}
