package com.verifone.demo.emv;

import android.content.Context;
import android.content.DialogInterface;
import android.os.RemoteException;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;

import com.verifone.demo.emv.Utilities.ServiceHelper;
import com.vfi.smartpos.deviceservice.aidl.IPinpad;


/**
 * Created by Simon on 2019/4/15.
 */

public class ClearKeys {

    public final static String TAG = "ClearKeys";

    public int clearKeysIndex = -1;
    Context context;

    IPinpad ipinpad;

    public ClearKeys(Context context ){
        this.context = context;
    }

    public void clear(){
        Log.d(TAG, "try clear" );
        clearKeysAskIndex();
    }

    /**
     *
     *
     * 	 * <li>0 MASTER(main) key</li>
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
     *
     * */
    // The key map for clear keys
    ClearKeyTypeItem[] keyTypeItems = {
            new ClearKeyTypeItem("Master Key - DES", 0x00, 0),
            new ClearKeyTypeItem("Master Key - SM4", 0x01, 4),
            new ClearKeyTypeItem("Master Key - AES", 0x02, 8),

            new ClearKeyTypeItem("Pin Key - DES", 0x10, 2),
            new ClearKeyTypeItem("Pin Key - SM4", 0x11, 6),
            new ClearKeyTypeItem("Pin Key - AES", 0x12, 10),

            new ClearKeyTypeItem("Mac Key - DES", 0x20, 1),
            new ClearKeyTypeItem("Mac Key - SM4", 0x21, 5),
            new ClearKeyTypeItem("Mac Key - AES", 0x22, 9),

            new ClearKeyTypeItem("Data Key - DES", 0x30, 3),
            new ClearKeyTypeItem("Data Key - SM4", 0x31, 7),
            new ClearKeyTypeItem("Data Key - AES", 0x32, 11),
    };

    // do clear keys
    public void clearKeys(){

        ipinpad = ServiceHelper.getInstance().getPinpad();

        int indexStart;
        int indexEnd;
        if( clearKeysIndex == -1 ){
            indexStart = 0;
            indexEnd = 99;
        } else {
            indexStart = clearKeysIndex;
            indexEnd = clearKeysIndex;
        }
        Log.d(TAG, "do clear Keys, range: " + indexStart + ", " + indexEnd );
        for (int i = indexStart; i <= indexEnd ; i++) {
            for (int j = 0; j < keyTypeItems.length ; j++) {
                if( keyTypeItems[j].selection ){
                    try {
                        if( ipinpad.isKeyExist( keyTypeItems[j].clearType, i) ){
                            if( ipinpad.clearKey( i, keyTypeItems[j].keyType ) ) {
                                Log.d(TAG, "CLEAR " + keyTypeItems[j].keyName + "at slot:" + i + " : success" );

                            } else {
                                //
                                Log.e(TAG, "CLEAR " + keyTypeItems[j].keyName + "at slot:" + i + " : fails" );
                            }
                        } else {
                            Log.w(TAG, "CLEAR " + keyTypeItems[j].keyName + "at slot:" + i + " : not exist" );
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    // ask the index to clear or all to clear
    public void clearKeysAskIndex() {
        Log.d(TAG, "call Ask index to clear");
        AlertDialog.Builder build = new AlertDialog.Builder(context);
        build.setTitle("Select the Slot to be clear");
        build.setMessage("Select clear ALL or input the index");
        build.setPositiveButton("Input", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // input index
                Log.d(TAG, "Input index to clear");
                clearKeysInputIndex();
            }
        });
        build.setNegativeButton("ALL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // all
                Log.d(TAG, "ALL index to clear");
                clearKeysIndex = -1;
                clearKeysAskType();
            }
        });
        build.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
                Log.d(TAG, "Cancel");
            }
        });
        AlertDialog alertDialog = build.create();
        alertDialog.show();
    }

    // input the index to clear
    public void clearKeysInputIndex(){
        Log.d(TAG, "input index to clear");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Index to clear");
        final EditText et = new EditText(context);
        et.setHint("Input the index to clear, 0 ~ 99");
        et.setSingleLine(true);
        et.setInputType(InputType.TYPE_CLASS_NUMBER );
        builder.setView(et);
        builder.setNegativeButton("Cancel",null);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strIndex = et.getText().toString();
                clearKeysIndex = Integer.valueOf(strIndex);
                Log.d(TAG, "confirm index: " + strIndex );
                clearKeysAskType();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // ask the type to clear or all type to clear
    public void clearKeysAskType(){

        Log.d(TAG, "ask type to clear");
        AlertDialog.Builder build = new AlertDialog.Builder(context);
        build.setTitle("Select the Key Type to be clear");
        build.setMessage("Select clear ALL Type or input the index");
        build.setPositiveButton("Input", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // input index
                Log.d(TAG, "input type to clear");
                clearKeysInputType();
            }
        });
        build.setNegativeButton("ALL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // all
                Log.d(TAG, "Set all key type " );
                for (int i = 0; i < keyTypeItems.length ; i++) {
                    keyTypeItems[i].selection = true;
                }
                clearKeys();
            }
        });
        build.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
                Log.d(TAG, "Cancel");
            }
        });
        AlertDialog alertDialog = build.create();
        alertDialog.show();
    }

    private class ClearKeyTypeItem {
        String keyName;
        int keyType;
        int clearType;
        boolean selection;
        public ClearKeyTypeItem(String keyName, int keyType, int clearType ){
            this.keyName = keyName;
            this.keyType = keyType;
            this.clearType = clearType;
            this.selection = false;
        }
    }

    private class ClearKeyTypeItems {
        public ClearKeyTypeItems(){
            for (int i = 0; i < keyTypeItems.length ; i++) {
                keyTypeItems[i].selection = false;
            }

        }
        public String[] getItems(){
            String[] items = new String[keyTypeItems.length];
            for (int i = 0; i < keyTypeItems.length ; i++) {
                items[i] = keyTypeItems[i].keyName;
            }
            return items;
        }

        public boolean [] getSelections(){
            boolean[] selections = new boolean[ keyTypeItems.length];
            for (int i = 0; i < keyTypeItems.length ; i++) {
                selections[i] = keyTypeItems[i].selection;
            }
            return selections;
        }

    }

    ClearKeyTypeItems clearKeyTypeItems;

    // ask the type to clear
    public void clearKeysInputType() {
        Log.d(TAG, "Call input key type to clear");

        clearKeyTypeItems  = new ClearKeyTypeItems();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select types");
        builder.setMultiChoiceItems(clearKeyTypeItems.getItems(), clearKeyTypeItems.getSelections(),
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                Log.d(TAG, "select: " + keyTypeItems[which].keyName);
                                keyTypeItems[which].selection = isChecked;
                            }
                        });
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "on confirm");
                clearKeys();
            }
        });
        builder.setNegativeButton("Cancel",null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
