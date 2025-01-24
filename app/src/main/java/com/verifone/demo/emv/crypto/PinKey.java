package com.verifone.demo.emv.crypto;

import android.os.RemoteException;
import android.util.Log;

import com.vfi.smartpos.deviceservice.aidl.PinpadKeyType;

/**
 * Created by Simon on 2019/4/19.
 */

public class PinKey extends WorkKey {
    private static final String TAG = "PinKey";
    String pin;
    String pan;
    String pinBlock;

    public PinKey(
            String key,
            String keyEncrypted,
            boolean load,
            int slot,
            int klkSlot,
            String pin,
            String pan,
            String pinBlock,
            String encrypted,
            String comment){

        super(key,
                keyEncrypted,
                load,
                slot,
                klkSlot,
                pan,
                encrypted,
                comment );

        this.pin = pin;
        this.pan = pan;
        this.pinBlock = pinBlock;
    }
    public PinKey(
            String keyEncrypted,
            int slot,
            int klkSlot,
            String comment){
                super( keyEncrypted, slot, klkSlot, comment );
    }

    public PinKey(
            int slot,
            String pin,
            String pan,
            String pinBlock,
            String encrypted,
            String comment){

        super(null,
                null,
                false,
                slot,
                -1,
                pan,
                encrypted,
                comment );

        this.pin = pin;
        this.pan = pan;
        this.pinBlock = pinBlock;
    }

    @Override
    protected void initKeyType() {
        keyType.clearKey = 0x10;
        keyType.keyExist = 2;
        keyType.loadWorkKey = PinpadKeyType.PINKEY;
    }

    @Override
    public byte[] doEncrypt() {
        return this.encrypt( pin + " " + pan );
    }


    public byte[] encrypt(String pin, String pan) {
        return this.encrypt( slot, pin + " " + pan );
    }

    @Override
    public byte[] encrypt(String pin_space_pan) {
        return this.encrypt( slot, pin_space_pan);
    }

    @Override
    public byte[] encrypt(int slot, String plain) {
        return Encrypt(slot, plain);
    }


    public static byte[] Encrypt(int slot, String pin_space_pan) {
        if( pin_space_pan.length() > 0 ){
            try {

                // checkEncrypted key exist
                if( ipinpad.isKeyExist(2, slot ) ){
                    // key exist

                } else {
                    Log.e(TAG, "PIN key on slot [" + slot +"] doesn't exist" );
                    return null;
                }
                // do plain encrypt
                String[] strings = pin_space_pan.split("\\s+");
                if (strings.length != 2 ){
                    return null;
                }
                String pin = strings[0];
                String pan = strings[1];
                byte[] ret = ipinpad.encryptPinFormat0(slot, 1, pan.getBytes(), pin );
                return ret;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}