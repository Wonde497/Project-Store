package com.verifone.demo.emv.crypto;

import android.os.RemoteException;
import android.util.Log;

import com.verifone.demo.emv.Utilities.Utility;
import com.vfi.smartpos.deviceservice.aidl.IPinpad;

/**
 * Created by Simon on 2019/4/19.
 */

public abstract class KeyBasic {
    private static final String TAG = "KeyBasic";

    // for key
    protected String key;
    protected String keyEncrypted;
    protected boolean load;
    protected int slot;

    // for klk
    protected int klkSlot;
    protected String klk;

    // for verify the key
    protected String plain;
    public String encrypted;

    protected static String comment;

    // the key type defines for clear, check exist, load ...
    protected static KeyType keyType;

    // the pinpad for service
    protected static IPinpad ipinpad;

    // key type defines
    protected class KeyType{
        /**
         * <li> 0x00-DES MK</li>
         * <li> 0x01-SM4 MK</li>
         * <li> 0x02-AES MK</li>
         * <li> 0x10-DES PIN</li>
         * <li> 0x11-SM4 PIN</li>
         * <li> 0x12-AES PIN</li>
         * <li> 0x20-DES MAC</li>
         * <li> 0x21-SM4 MAC</li>
         * <li> 0x22-AES MAC</li>
         * <li> 0x30-DES DATA</li>
         * <li> 0x31-SM4 DATA</li>
         * <li> 0x32-AES DATA</li>
         */
        public int clearKey;

        /**
         * <li>0 MASTER(main) key</li>
         * <li>1 MAC key</li>
         * <li>2 PIN(work) key</li>
         * <li>3 TD key</li>
         * <li>4 (SM) MASTER key</li>
         * <li>5 (SM) MAC key</li>
         * <li>6 (SM) PIN key</li>
         * <li>7 (SM) TD key</li>
         * <li>8 (AES) MASTER key</li>
         * <li>9 (AES) MAC key</li>
         * <li>10 (AES) PIN key</li>
         * <li>11 (AES) TD key</li>
         * <li>12 dukpt key</li>
         * <li>13 TEK</li>
         * <li>14 (SM)TEK</li>
         * <li>15 (AES)TEK</li>
         * </ul>
         * */
        public int keyExist;

        /**
         * 	 * @param keyType select the workkey type<BR>
         *     |---1-MAC key, 2-PIN key, 3-TD key<BR>
         *     |---5-(SM4)MAC key, 6-(SM4)PIN key, 7-(SM4)TD key<BR>
         *     |---9-(AES)MAC key, 10-(AES)PIN key, 11-(AES)TD key<BR>

         * */
        public int loadWorkKey;
    }

    // for test keys, verify the encryption
    protected KeyBasic(String key,
                       String keyEncrypted,
                       boolean load,
                       int slot,
                       int klkSlot,
                       String plain,
                       String encrypted,
                       String comment
                       ) {
        initialize(key,keyEncrypted,load,slot,null,klkSlot,plain,encrypted, comment);
    }

    // for test keys, verify the encryption
    protected KeyBasic(String key,
                       String keyEncrypted,
                       boolean load,
                       int slot,
                       String klk,
                       int klkSlot,
                       String plain,
                       String encrypted ) {
        initialize(key,keyEncrypted,load,slot,klk,klkSlot,plain,encrypted, "");
    }

    // for encrypted key, will download the klk
    protected KeyBasic(String keyEncrypted,
                       int slot,
                       String klk,
                       int klkSlot,
                       String comment) {
        initialize(null,keyEncrypted,true,slot,klk,klkSlot,null,null, comment);
    }

    // for encrypted key download klk before
    protected KeyBasic(String keyEncrypted,
                       int slot,
                       int klkSlot,
                       String comment) {
        initialize(null,keyEncrypted,true,slot,null,klkSlot,null,null, comment);
    }

    // for clear/plain key
    protected KeyBasic(String key,
                       int slot ) {
        initialize(key,null,true,slot,null,-1,null,null, "");
    }
    // for just encryption or decryption. do not need load keys
    public KeyBasic( int slot ){
        initialize(null,null,false,slot,null,-1,null,null, "");
    }

    protected void initialize(String key,
                            String keyEncrypted,
                            boolean load,
                            int slot,
                            String klk,
                            int klkSlot,
                            String plain,
                            String encrypted,
                            String comment){
        keyType = new KeyType();

        this.key = fixedKey( key );
        this.keyEncrypted = fixedKey( keyEncrypted );
        this.load = load;
        this.slot = slot;
        this.klk = fixedKey( klk );
        this.klkSlot = klkSlot;

        this.plain = fixedKey( plain );
        this.encrypted = fixedKey( encrypted );

        this.comment = comment;

        initKeyType();
    }


    protected abstract void initKeyType();

    // fix the key, remove space in string
    protected String fixedKey( String source ){
        if( source == null ){
            return "";
        } else {
            return source.replaceAll(" ", "");
        }
    }

    public static void setPinpad( IPinpad ipinpad){
        KeyBasic.ipinpad = ipinpad;
    }



    // need return true by default
    // load key
    public abstract boolean load();

    // check exist (current slot)
    public boolean isExist( ){
        Log.d(TAG, "call isExist"+ " of " + toString());
        return isExist(slot);
    }

    // check exist with given the slot
    public static boolean isExist( int slot ){
        try {
            if( ipinpad.isKeyExist( keyType.keyExist, slot) ){
                return true;
            } else {
                Log.w(TAG, "is key exist at slot:" + slot + " : not exist" );
                return false;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    // clear current key
    // will check if exist
    public boolean clear(){
        Log.d(TAG, "call clear of " + toString() );
        if( isExist() ){
            return clear(slot);
        } else {
            Log.w(TAG, "CLEAR " + comment + "at slot:" + slot + " : not exist" );
            return false;
        }
    }

    // clear key with given slot
    // do not check exist, directly clear key
    public static boolean clear( int slot ){
        try {
            if( ipinpad.clearKey( slot, keyType.clearKey ) ) {
                Log.d(TAG, "CLEAR " + comment + "at slot:" + slot + " : success" );

            } else {
                //
                Log.e(TAG, "CLEAR " + comment + "at slot:" + slot + " : fails" );
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return false;
    }

    // do encrypt with current plain
    public byte[] doEncrypt() {
        Log.d(TAG, "call doEncrypt()" );
        return this.encrypt( plain );
    }

    // do encrypt with given plain
    public byte[] encrypt(String plain) {
        Log.d(TAG, "call doEncrypt(String)" );
        return this.encrypt( slot, plain);
    }

    // do encrypt with given slot & plain
    public abstract byte[] encrypt(int slot, String plain);
//    {
//        Log.e(TAG, "Not encypt for current object");
//        return null;
//    }

    // verify the encrypted
    public boolean checkEncrypted(String encrypted ){
        if( this.encrypted.toUpperCase().equals(encrypted.toUpperCase()) ) {
            return true;
        } else {
            Log.e(TAG, "want: " + this.encrypted + ", got: " + encrypted + ", of " + toString() );
        }
        return false ;
    }

    public boolean loadKLK() {
        boolean ret = true; // set true as the default value
        if( klk.length() <= 0 ){
            return ret;
        }
        if( klkSlot < 0 ){
            klkSlot = 0;
        }

        try {
            ret = ipinpad.loadTEK( klkSlot, Utility.hexStr2Byte(klk), null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return ret;
    }


    public String toString(){
        if( null != comment ){
            return comment;
        } else {
            return "";
        }
    }


}
