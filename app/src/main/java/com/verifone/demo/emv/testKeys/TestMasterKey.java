package com.verifone.demo.emv.testKeys;

import android.os.RemoteException;
import android.util.Log;

import com.verifone.demo.emv.Utilities.Utility;
import com.verifone.demo.emv.crypto.DataKey;
import com.verifone.demo.emv.crypto.KeyBasic;
import com.verifone.demo.emv.crypto.MasterKey;
import com.vfi.smartpos.deviceservice.aidl.PinpadKeyType;

/**
 * Created by Simon on 2019/4/19.
 */


public class TestMasterKey extends MasterKey {

    private static final String TAG = "TestMasterKey";
    String dataKey;
    String dataKeyEncrypted;
    int dataKeySlot;

    public TestMasterKey(String key,
                         boolean load,
                         int slot,
                         String dataKey,
                         String dataKeyEncrypted,
                         int dataKeySlot,
                         String plain,
                         String encrypted){
        super(key,slot );

        this.dataKey = dataKey;
        this.dataKeyEncrypted = dataKeyEncrypted;
        this.dataKeySlot = dataKeySlot;
        this.load = load;

        this.plain = fixedKey(plain);
        this.encrypted = fixedKey(encrypted);
    }

    @Override
    protected void initKeyType() {
        keyType.loadWorkKey = PinpadKeyType.TDKEY;
        keyType.clearKey = 0;
        keyType.keyExist = 0;
//        new ClearKeyTypeItem("Master Key - DES", 0x00, 0),
//                new ClearKeyTypeItem("Master Key - SM4", 0x01, 4),
//                new ClearKeyTypeItem("Master Key - AES", 0x02, 8),

    }

    public boolean load() {
        Log.d(TAG, "call load()" );
        // load master key
        boolean bRet = true;
        try {
        if( load) {
                bRet = ipinpad.loadMainKey(slot, Utility.hexStr2Byte(key), null );
            if( bRet ){
                Log.d(TAG, "load master key success.");
            } else {
                Log.e(TAG, "load master key fails");
                return false;
            }

        }

        // load plain key
        if( dataKeySlot >= 0 ){
            bRet = ipinpad.loadWorkKey(keyType.loadWorkKey, slot, dataKeySlot, Utility.hexStr2Byte(dataKeyEncrypted), null );
            if( bRet ){

            } else {
                Log.e(TAG, "load plain key fails");
                // return false;
            }
        }

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return bRet;
    }

    // do encrypt with given plain
    @Override
    public byte[] encrypt(String plain) {
        Log.d(TAG, "call doEncrypt(String)" );
        return this.encrypt( dataKeySlot, plain);
    }


    @Override
    public byte[] encrypt(int slot, String plain){
        Log.d(TAG, "call encrypt(int , String )" );
        return DataKey.Encrypt(slot, plain);
    }
}
