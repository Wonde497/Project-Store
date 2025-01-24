package com.verifone.demo.emv.testKeys;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.verifone.demo.emv.Utilities.Utility;
import com.verifone.demo.emv.crypto.DataKey;
import com.verifone.demo.emv.crypto.DukptKey;
import com.verifone.demo.emv.crypto.KeyBasic;
import com.verifone.demo.emv.crypto.MasterKey;
import com.verifone.demo.emv.crypto.PinKey;
import com.vfi.smartpos.deviceservice.aidl.IPinpad;

/**
 * Created by Simon on 2019/4/19.
 */

public class TestKeys {

    private static final String TAG = "TestKeys";

    protected IPinpad ipinpad;
    protected Context context;

    public TestKeys(IPinpad ipinpad, Context context){
        this.ipinpad = ipinpad;
        this.context = context;

        KeyBasic.setPinpad( ipinpad );
    }

    public void startTest(){
        testKeysSelectType();
    }

    TestMasterKey testMasterKeys[] = {
            // index 0
            new TestMasterKey(
                    "649B2398947916EF70E3A86DE685A1AE",
                    true,
                    14,
                    "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                    "B6319578502413A2B6319578502413A2",
                    11,
                    "11111111111111111111111111111111",
                    "A202EE26F8A71460A202EE26F8A71460"
            ),

            // index 1
            new TestMasterKey(
                    "33333333333333333333333333333333",
                    true,
                    14,
                    "00000000000000000000000000000000",
                    "ADC67D8473BF2F06ADC67D8473BF2F06",
                    11,
                    "11111111111111111111111111111111",
                    "89B07B35A1B3F47E89B07B35A1B3F47E"
            ),

            // index 2
            new TestMasterKey(
                    "649B2398947916EF70E3A86DE685A1AE",
                    false,
                    14,
                    "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                    "B6319578502413A2B6319578502413A2",
                    11,
                    "11111111111111111111111111111111",
                    "A202EE26F8A71460A202EE26F8A71460"
            ),
    };

    MasterKey masterKeys[] = {
            // index 0
            new MasterKey(
                    "649B2398947916EF70E3A86DE685A1AE",
                    14
            ),

            // index 1
            new MasterKey(
                    "33333333333333333333333333333333",
                    14
            ),

            // index 2
            new MasterKey(
                    "649B2398947916EF70E3A86DE685A1AE",
                    14,
                    null,
                    0,
                    "encrypted master key"
            ),
    };



    DukptKey dukptKeys[] = {
            // index 0
            new DukptKey( // clear key
                    "FFFF9876543210E00001",
                    "6AC292FA A1315B4D 858AB3A3 D7D5933A ",
                    true,
                    2,
                    "0123456789ABCDEFFEDCBA9876543210",
                    "82E85167 88FC52E6 C1EE7488 4B4426E5 "),
            // index 1
            new DukptKey( // clear key
                    "FFFF9876543210E00001",
                    "6AC292FA A1315B4D 858AB3A3 D7D5933A ",
                    false,
                    3,
                    "0123456789ABCDEFFEDCBA9876543210",
                    "82E85167 88FC52E6 C1EE7488 4B4426E5 "),
            // index 2
            new DukptKey( // encrypted key
                    "FFFF9876543210E00001",
                    "6AC292FA A1315B4D 858AB3A3 D7D5933A ",
                    3,
                    "klk",
                    3,
                    "0123456789ABCDEFFEDCBA9876543210",
                    "82E85167 88FC52E6 C1EE7488 4B4426E5 "),
    };


    DataKey dataKeys[] = {
            // index 0
            new DataKey(
                    "B6319578502413A2B6319578502413A2",
                    11,
                    14,
                    "11111111111111111111111111111111",
                    "A202EE26F8A71460A202EE26F8A71460",
                    "plain key: AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
            ),
            // index 1
            new DataKey(
                    11,
                    "11111111111111111111111111111111",
                    "A202EE26F8A71460A202EE26F8A71460",
                    "plain key: AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
            ),

            //  index 2
            new DataKey(
                    "ADC67D8473BF2F06ADC67D8473BF2F06",
                    11,
                    14,
                    "11111111111111111111111111111111",
                    "89B07B35A1B3F47E89B07B35A1B3F47E",
                    "plain key: 00000000000000000000000000000000"
            ),
            //  index 3
            new DataKey(
                    11,
                    "11111111111111111111111111111111",
                    "89B07B35A1B3F47E89B07B35A1B3F47E",
                    "plain key: 00000000000000000000000000000000"
            ),


            // index 4
            new DataKey(
                    "B6319578502413A2B6319578502413A2",
                    11,
                    14,
                    "11111111111111111111111111111111",
                    "A202EE26F8A71460A202EE26F8A71460",
                    "plain key: AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
            ),

            // index 5
            new DataKey(
                    11,
                    "11111111111111111111111111111111",
                    "A202EE26F8A71460A202EE26F8A71460",
                    "plain key: AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
            ),

            // index 6
            new DataKey(
                    "95141F12F8E9DABC95141F12F8E9DABC",
                    12,
                    3,
                    "A202EE26F8A71460A202EE26F8A71460",
                    "5D50E84B50F84A1E5D50E84B50F84A1E",
                    "plain key : A202EE26F8A71460A202EE26F8A71460"

                    ),

            new DataKey(11),
    };


    PinKey pinKeys[] = {
            // index 0
            new PinKey(
                    "A7A2D6495F703A5EB6319578502413A2",
                    11,
                    14,
                    "pinkey: BBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
            ),

            // index 1
            new PinKey(
//                    "BBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
//                    "A7A2D6495F703A5EB6319578502413A2",
//                    false,
                    11,
//                    14,
                    "123456",
                    "43219876543210987",
                    "0612AC20ABCDEF67",
                    "17569A0AC48C6E0D",
                    "pin key: BBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
            ),

            // index 2
            new PinKey(
                    "11000000000000000000000000000000",
                    "87D25EFA28CA1E9BADC67D8473BF2F06",
                    true,
                    11,
                    14,
                    "123456",
                    "43219876543210987",
                    "0612AC20ABCDEF67",
                    "AF1E1EF659A5FAF1",
                    ""
            ),

            // index 3
            new PinKey(
                    "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                    "B6319578502413A2B6319578502413A2",
                    true,
                    11,
                    14,
                    "123456",
                    "43219876543210987",
                    "0612AC20ABCDEF67",
                    "D6E8CCF4C772A329",
                    ""
            ),

    };


    public void testKeys2(final int typeIndex ){
        Log.d(TAG, "input index to Test");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Index to Test");
        final EditText et = new EditText(context);
        et.setHint("Input the index to test" );
        et.setSingleLine(true);
        et.setInputType(InputType.TYPE_CLASS_NUMBER );
        et.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        builder.setView(et);
        builder.setNegativeButton("Cancel",null);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strIndex = et.getText().toString();
                int dataIndex = Integer.valueOf(strIndex);
                boolean ret = false ;
                KeyBasic key = null;
                switch ( typeIndex ){
                    case 0:
                        if( dataIndex >= testMasterKeys.length ){
                            return ;
                        }

                        key = masterKeys[dataIndex];
                        break;
                    case 1:
                        if( dataIndex >= dukptKeys.length ){
                            return ;
                        }

                        key = dukptKeys[dataIndex];
                        break;
                    case 2:
                        if( dataIndex >= pinKeys.length ){
                            return ;
                        }

                        key = pinKeys[dataIndex];
                        break;
                    case 3:
                        if( dataIndex >= dataKeys.length ){
                            return ;
                        }

                        key = dataKeys[dataIndex];
                        break;
                    case 4:
                        if( dataIndex >= testMasterKeys.length ){
                            return ;
                        }

                        key = testMasterKeys[dataIndex];
                        break;
                }
                if( null == key ){
                    ret = false;
                } else {
                    ret = testKey( key );
                }
                if( ret ){
                    showConfirm( "Key test PASS", "Test pass on index: " + strIndex );
                } else {
                    showConfirm( "Key test FAIL", "Test failure on index: " + strIndex );
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // ask the type to clear
    public void testKeysSelectType() {
        Log.d(TAG, "Call input key type to clear");

        String[] typeNameList = {
                "MASTER KEY",
                "DUKPT",
                "PIN KEY",
                "DATA KEY",
                "MASTER DATE KEY",
        };
        final int[] selectIndex = {0};

        AlertDialog.Builder builder = new AlertDialog.Builder( context );
        builder.setTitle("Select types to Test");
        builder.setSingleChoiceItems(typeNameList, selectIndex[0], new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectIndex[0] = which;
            }
        } );

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "on confirm");
                testKeys2( selectIndex[0]);
            }
        });
        builder.setNegativeButton("Cancel",null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public boolean testKey(KeyBasic key ){

        // load
        if( key.load() ) {
            //
        } else {
            return false;
        }

        // do encrypt
        byte[] ret = key.doEncrypt();
        if( null == ret ){
            return false;
        }
        // check the result
        String result = Utility.byte2HexStr(ret);
        if( key.checkEncrypted( result ) ){
            Log.d(TAG, "encrypted success: " + result  );
        } else {
            Log.e(TAG, "encrypted fails: " + ", want: " + key.encrypted + ", got: " + result);
            return false;
        }
        return true;
    }

    void showConfirm(String title, String message ){
        Log.d(TAG, "show confirm dialog");
        AlertDialog.Builder build = new AlertDialog.Builder(context);
        build.setTitle( title );
        build.setMessage( message );
        build.setPositiveButton("Dismiss", null);
        AlertDialog alertDialog = build.create();
        alertDialog.show();
    }

}
