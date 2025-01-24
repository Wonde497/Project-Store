package com.verifone.demo.emv.crypto;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.verifone.demo.emv.Utilities.Utility;

import java.util.Map;

/**
 * Created by Simon on 2019/4/19.
 */


public class DukptKey extends KeyBasic{
    private static final String TAG = "DukptKey";
    public String ksn;
    public boolean isPanDukpt;

    public DukptKey(String ksn,
                    String dukpt_ipek,
                    boolean load,
                    int slot,
                    String data,
                    String dataEncrypted){
        super(dukpt_ipek, "", load, slot, -1, data, dataEncrypted, "" );
        this.ksn = ksn.replaceAll(" ", "" );
        this.isPanDukpt = true;
    }
    public DukptKey(String ksn,
                    String dukpt_ipek_encrypted,
                    int slot,
                    String klk,
                    int klkSlot,
                    String data,
                    String dataEncrypted){
        super(null, dukpt_ipek_encrypted, true, slot, klk, klkSlot, data, dataEncrypted );

        this.ksn = ksn.replaceAll(" ", "" );
        this.isPanDukpt = false;
    }
    @Override
    protected void initKeyType() {
        keyType.loadWorkKey = -1;
        keyType.clearKey = -1;
        keyType.keyExist = -1;
    }

    @Override
    public boolean load() {
        boolean bRet = true;
        if( load) {
            try {

                if( klk.length() > 0 && isPanDukpt == false ){
                    // load klk to index 0
                    if( loadKLK() == false ){
                        Log.e(TAG, "load klk fails, try continue load the key");
                    }
                }

                Bundle dukptKeyExExtend = new Bundle();
                if( isPanDukpt ){
                    dukptKeyExExtend.putBoolean("loadPlainKey", true);
                }
                    bRet = ipinpad.loadDukptKeyEX(slot,
                            Utility.hexStr2Byte(ksn),
                            Utility.hexStr2Byte(key),
                            null, dukptKeyExExtend);
                if (bRet) {

                } else {
                    Log.e(TAG, "load loadDukptKeyEX key fails");
                    // return false;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        return bRet;

    }

    @Override
    public boolean clear() {
        //
        Log.e(TAG, "not support clear currently");
        return false;
    }
    public static boolean clear( int slot ){
        Log.e(TAG, "not support clear currently");
        return false;
    }

    @Override
    public byte[] encrypt(int slot, String plain) {
        return Encrypt( slot, plain );
    }

    public static byte[] Encrypt(int slot, String plain){
        if (plain.length() > 0) {
            try {
                byte[] ret = ipinpad.dukptEncryptData(1, 2,
                        slot,
                        Utility.hexStr2Byte(plain),
                        null);
                return ret;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
