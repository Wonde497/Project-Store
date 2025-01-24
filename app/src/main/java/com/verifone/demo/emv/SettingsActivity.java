package com.verifone.demo.emv;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.RemoteException;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.verifone.demo.emv.Utilities.ServiceHelper;
import com.verifone.demo.emv.Utilities.SPHelper;
import com.verifone.demo.emv.Utilities.SSLClient;
import com.verifone.demo.emv.Utilities.SSLComm;
import com.verifone.demo.emv.Utilities.Utility;
import com.verifone.demo.emv.basic.BaseActivity;
import com.verifone.demo.emv.basic.HostInformation;
import com.verifone.demo.emv.testKeys.TestKeys;
import com.verifone.demo.emv.transaction.AppParams;
import com.verifone.demo.emv.transaction.TransBasic;
import com.verifone.demo.emv.usecase.MultiHostsConfig;
import com.vfi.smartpos.deviceservice.aidl.PinpadKeyType;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;


public class SettingsActivity extends BaseActivity {

    private static final String TAG = "Demo_EMV_Settings";
    Context context;
    TransBasic transBasic;

    ToggleButton toggleOffline;
    ToggleButton toggleScanborder;
    CheckBox cbTap;
    CheckBox cbInsert;
    CheckBox cbSwipe;
    Button btnCheckCard;
    Button btnPinPad;
    Button btnSetKeys;
    Button btnSetAID;
    Button btnSetRID;
    Button btnClearAID;
    Button btnClearRID;
    Button btnTest;
    Button btnTestSSL;
    Button btnDeviceInfo;
    Button btnTransLogon;
    Button btnTransLogon1;
    Button btnTransLogon2;
    Button btnTransLogon3;
    Button btnTransBalance;
    Button btnPurchase;
    Button btnCustomPin;
    Button btnSerialPort;
    Button btnPrint;
    ImageView btnBack;

    EditText edIP;
    EditText edPort;
    EditText edIP1;
    EditText edPort1;
    EditText edAmount;
    EditText edIP2;
    EditText edPort2;
    EditText edIP3;
    EditText edPort3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        context = this;
        SharedPreferences sharedPreferences=getSharedPreferences("Shared_data", Context.MODE_PRIVATE);

        transBasic = TransBasic.getInstance(sharedPreferences);

        initView();

        //initialize hosts
        MultiHostsConfig.initialize();
    }

    CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()){
                case R.id.toggleOffline:
                    AppParams.getInstance().setOffline(isChecked);
                    SPHelper.getInstance().putBoolean("offline", isChecked);
                    break;
                case R.id.toggleScanBorder:
                    AppParams.getInstance().setShowScanBorder(isChecked);
                    SPHelper.getInstance().putBoolean("showScanBorder", isChecked);
                    break;
                case R.id.cb_tap:
                    AppParams.getInstance().setSupportTap(isChecked);
                    SPHelper.getInstance().putBoolean("supportTap", isChecked);
                    break;
                case R.id.cb_insert:
                    AppParams.getInstance().setSupportInsert(isChecked);
                    SPHelper.getInstance().putBoolean("supportInsert", isChecked);
                    break;
                case R.id.cb_swipe:
                    AppParams.getInstance().setSupportSwipe(isChecked);
                    SPHelper.getInstance().putBoolean("supportSwipe", isChecked);
            }
        }
    };


    // button -- start
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == btnCheckCard) {
                // doBalance();
//                SSLClient sslClient = new SSLClient(view.getContext());
                 testTDKey();
//                TestKeys testKeys = new TestKeys(ServiceHelper.getInstance().getPinpad(), context );
//                testKeys.startTest();
            } else if (view == btnPinPad) {
                transBasic.doPinPad(true, 0);
            } else if (view == btnSetKeys) {
                transBasic.doSetKeys();
            } else if (view == btnSetAID) {
                transBasic.doSetAID(1);
            } else if (view == btnClearAID) {
                transBasic.doSetAID(3);
            } else if (view == btnSetRID) {
                transBasic.doSetRID(1);
            } else if (view == btnClearRID) {
                transBasic.doSetRID(3);
            } else if (view == btnBack){
                finish();
            }else if ( view.getId() == R.id.btnClearKeys ){
                ClearKeys clearKeys = new ClearKeys(SettingsActivity.this);
                clearKeys.clear();
            } else if (view == btnTransLogon
                    || view == btnTransLogon1
                    || view == btnTransLogon2
                    || view == btnTransLogon3) {
                int index = 0;
                String hostIP;
                String port;
                int hostPort;
                if ( view == btnTransLogon3){
                    index = 3;
                    hostIP = edIP3.getText().toString();
                    port = edPort3.getText().toString();
                }
                else if( view == btnTransLogon2) {
                    index = 2;
                    hostIP = edIP2.getText().toString();
                    port = edPort2.getText().toString();
                } else if( view == btnTransLogon1) {
                    index = 1;
                    hostIP = edIP1.getText().toString();
                    port = edPort1.getText().toString();
                } else {
                    hostIP = edIP.getText().toString();
                    port = edPort.getText().toString();
                }

                if (port.length() == 0) {
                    Log.e(TAG, "cannot read port");
                    hostPort = 5555;
                } else {
                    hostPort = Integer.valueOf(port);
                }
                Log.e(TAG, "Host:" + hostIP + ":" + hostPort);

                HostInformation hostInformation = MultiHostsConfig.get(index);
                hostInformation.hostAddr = hostIP;
                hostInformation.hostPort = hostPort;
                MultiHostsConfig.update(index, hostInformation);

                transBasic.doLogon(index);
            } else if (view == btnTransBalance) {
                transBasic.doBalance();

            } else if (view == btnTest) {
//                // test redefine the AID to kernel
//                Log.d(TAG, "re-define the AID to kernel");
//                Map<String, Integer> map = new HashMap<>();
//                map.put("A000000003", CTLSKernelID.CTLS_KERNEL_ID_01_VISA); // define the AID - Kernel may
//                map.put("A000000004", CTLSKernelID.CTLS_KERNEL_ID_01_VISA); // define the AID - Kernel may
//
//                try {
//                    ServiceHelper.getInstance().getIemv().registerKernelAID( map);   // set to kernel
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//                Log.d(TAG, "re-define the AID to kernel -- done");
                TestKeys testKeys = new TestKeys(ServiceHelper.getInstance().getPinpad(), context );
                testKeys.startTest();
            } else if (view == btnCustomPin) {
                doStartCustonViewPinpad();
            } else if (view == btnSerialPort ){
                doShowSerialPort();
            } else if ( view == btnPrint ){
                doShowPrint();
            }else if ( view == btnTestSSL){
                testSSL(context);
            }else if (view == btnDeviceInfo){
                showDeivceInfo(context);
            }
        }
    };

    private void showDeivceInfo(Context context) {
        startActivity(new Intent(SettingsActivity.this, DeviceInfoActivity.class));
    }

    private void initView() {

        toggleOffline = findViewById(R.id.toggleOffline);
        if (SPHelper.getInstance().getBoolean("offline", true)){
            toggleOffline.setChecked(true);
        }else {
            toggleOffline.setChecked(false);
        }
        toggleOffline.setOnCheckedChangeListener(checkedChangeListener);

        toggleScanborder = findViewById(R.id.toggleScanBorder);
        if (SPHelper.getInstance().getBoolean("showScanBorder", true)){
            toggleScanborder.setChecked(true);
        }else {
            toggleScanborder.setChecked(false);
        }
        toggleScanborder.setOnCheckedChangeListener(checkedChangeListener);
        cbTap = findViewById(R.id.cb_tap);
        if (SPHelper.getInstance().getBoolean("supportTap", true)){
            cbTap.setChecked(true);
        }else {
            cbTap.setChecked(false);
        }
        cbTap.setOnCheckedChangeListener(checkedChangeListener);

        cbInsert = findViewById(R.id.cb_insert);
        if (SPHelper.getInstance().getBoolean("supportInsert", true)){
            cbInsert.setChecked(true);
        }else {
            cbInsert.setChecked(false);
        }
        cbInsert.setOnCheckedChangeListener(checkedChangeListener);

        cbSwipe = findViewById(R.id.cb_swipe);
        if (SPHelper.getInstance().getBoolean("supportSwipe", true)){
            cbSwipe.setChecked(true);
        }else {
            cbSwipe.setChecked(false);
        }
        cbSwipe.setOnCheckedChangeListener(checkedChangeListener);

        btnBack = findViewById(R.id.back_home);
        btnBack.setOnClickListener(onClickListener);
        btnCheckCard = findViewById(R.id.btnCheckCard);
        btnCheckCard.setOnClickListener(onClickListener);
        btnPinPad = findViewById(R.id.btnPinPad);
        btnPinPad.setOnClickListener(onClickListener);
        btnSetKeys = findViewById(R.id.btnSetKeys);
        btnSetKeys.setOnClickListener(onClickListener);
        btnSetAID = findViewById(R.id.btnSetAID);
        btnSetAID.setOnClickListener(onClickListener);
        btnSetRID = findViewById(R.id.btnSetRID);
        btnSetRID.setOnClickListener(onClickListener);
        btnClearAID = findViewById(R.id.btnClearAID);
       //  btIndexnClearAID.setOnClickListener(onClickListener);
        btnClearRID = findViewById(R.id.btnClearRID);
        btnClearRID.setOnClickListener(onClickListener);
        findViewById(R.id.btnClearKeys).setOnClickListener(onClickListener);
        btnTest = findViewById(R.id.btnTest);
        btnTest.setOnClickListener(onClickListener);
        btnTestSSL = findViewById(R.id.btnTestSSL);
        btnTestSSL.setOnClickListener(onClickListener);

        btnDeviceInfo = findViewById(R.id.btnDeviceInfo);
        btnDeviceInfo.setOnClickListener(onClickListener);

        btnTransLogon = findViewById(R.id.btnTransLogon);
        btnTransLogon.setOnClickListener(onClickListener);

        btnTransLogon1 = findViewById(R.id.btnTransLogon1);
        btnTransLogon1.setOnClickListener(onClickListener);

        btnTransLogon2 = findViewById(R.id.btnTransLogon2);
        btnTransLogon2.setOnClickListener(onClickListener);

        btnTransLogon3 = findViewById(R.id.btnTransLogon3);
        btnTransLogon3.setOnClickListener(onClickListener);

        btnCustomPin = findViewById(R.id.btnCustomPinpad);
        btnCustomPin.setOnClickListener(onClickListener);

        btnSerialPort = findViewById(R.id.btnShowSerial);
        btnSerialPort.setOnClickListener(onClickListener);

        btnPrint = findViewById(R.id.btnPrint);
        btnPrint.setOnClickListener(onClickListener);

        edIP = findViewById(R.id.edIP);
        if (null == edIP) {
            Log.e(TAG, "cannot get the IP edit");
        }
        edPort = findViewById(R.id.edPort);
        if (null == edPort) {
            Log.e(TAG, "cannot get the Port edit");
        }
        edIP1 = findViewById(R.id.edIP1);
        edPort1 = findViewById(R.id.edPort1);

        edIP2 = findViewById(R.id.edIP2);
        edPort2 = findViewById(R.id.edPort2);

        edIP3 = findViewById(R.id.edIP3);
        edPort3 = findViewById(R.id.edPort3);

        edAmount = findViewById(R.id.edAmount);
    }


    public void testTDKey( ){
        Log.d(TAG, "input index to Test");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Index to Test");
        final EditText et = new EditText(this);
        et.setHint("Input the index to test TD Key, 0 ~ 99");
        et.setSingleLine(true);
        et.setInputType(InputType.TYPE_CLASS_NUMBER );
        et.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        builder.setView(et);
        builder.setNegativeButton("Cancel",null);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strIndex = et.getText().toString();
                if( Integer.valueOf(strIndex) < 10  )
                    if( testTDKey( Integer.valueOf(strIndex) ) ){
                        showConfirm( "Key test PASS", "Test pass on index: " + strIndex );
                    } else {
                        showConfirm( "Key test FAIL", "Test failure on index: " + strIndex );
                    }
                else {
                    testDukptKeys( Integer.valueOf(strIndex) - 10);
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private class Keys{
        String masterKey;
        boolean loadMasterKey;
        int masterKeySlot;
        String dataKey;
        String dataKeyEncrypted;
        int dataKeySlot;
        String data;
        String dataEncrypted;

        public Keys(String masterKey,
                    boolean loadMasterKey,
                    int masterKeySlot,
                    String dataKey,
                    String dataKeyEncrypted,
                    int dataKeySlot,
                    String data,
                    String dataEncrypted){
            this.masterKey = masterKey;
            this.loadMasterKey = loadMasterKey;
            this.masterKeySlot = masterKeySlot;
            this.dataKey = dataKey;
            this.dataKeyEncrypted = dataKeyEncrypted;
            this.dataKeySlot = dataKeySlot;
            this.data = data;
            this.dataEncrypted = dataEncrypted;
        }

    }

    Keys keys[] = {
            // step 0
            new Keys(
                    "649B2398947916EF70E3A86DE685A1AE",
                    true,
                    14,
                    "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                    "B6319578502413A2B6319578502413A2",
                    11,
                    "11111111111111111111111111111111",
                    "A202EE26F8A71460A202EE26F8A71460"
            ),

            // step 1
            new Keys(
                    "33333333333333333333333333333333",
                    true,
                    14,
                    "00000000000000000000000000000000",
                    "ADC67D8473BF2F06ADC67D8473BF2F06",
                    11,
                    "11111111111111111111111111111111",
                    "89B07B35A1B3F47E89B07B35A1B3F47E"
            ),

            // step 2
            new Keys(
                    "649B2398947916EF70E3A86DE685A1AE",
                    false,
                    14,
                    "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                    "B6319578502413A2B6319578502413A2",
                    11,
                    "11111111111111111111111111111111",
                    "A202EE26F8A71460A202EE26F8A71460"
            ),

            // index 3
            new Keys(
                    "649B2398947916EF70E3A86DE685A1AE",
                    false,
                    3,
                    "A202EE26F8A71460A202EE26F8A71460",
                    "95141F12F8E9DABC95141F12F8E9DABC",
                    11,
                    "A202EE26F8A71460A202EE26F8A71460",
                    "5D50E84B50F84A1E5D50E84B50F84A1E"
            ),

            // index 4
            new Keys(
                    "BBBAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                    false,
                    3,
                    "A202EE26F8A71460A202EE26F8A71460",
                    "95141F12F8E9DABC95141F12F8E9DABC",
                    11,
                    "A202EE26F8A71460A202EE26F8A71460",
                    "5D50E84B50F84A1E5D50E84B50F84A1F"
            ),
    };
    public boolean testTDKey( int index ){

        Keys key = keys[index];

        try {
            // load master key
            if( key.loadMasterKey ) {
                boolean bRet = ServiceHelper.getInstance().getPinpad().loadMainKey(key.masterKeySlot, Utility.hexStr2Byte(key.masterKey), null );
                if( bRet ){

                } else {
                    Log.e(TAG, "load master key fails");
                    return false;
                }

            }

            // load data key
            if( key.dataKeySlot >= 0 ){
                boolean bRet = ServiceHelper.getInstance().getPinpad().loadWorkKey(PinpadKeyType.TDKEY, key.masterKeySlot, key.dataKeySlot, Utility.hexStr2Byte(key.dataKeyEncrypted), null );
                if( bRet ){

                } else {
                    Log.e(TAG, "load data key fails");
                    // return false;
                }
            }

            // do data encrypt
            byte[] ret = ServiceHelper.getInstance().getPinpad().encryptTrackData(0, key.dataKeySlot, Utility.hexStr2Byte(key.data) );
            String result = Utility.byte2HexStr(ret);
            if( result.toUpperCase().equals(key.dataEncrypted.toUpperCase())  ){
                Log.d(TAG, "encrypted success: " + result  );
            } else {
                Log.e(TAG, "encrypted fails: " + result + ", want: " + key.dataEncrypted );
                return false;
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return true;
    }

    private class DukptKeys{
        public String ksn;
        public String dukpt_ipek;
        public boolean loadDukpt;
        public boolean isPanDukpt;
        public int dukptKeySlot;
        public String data;
        public String dataEncrypted;

        public DukptKeys( String ksn,
                          String dukpt_ipek,
                          boolean loadDukpt,
                          boolean isPanDukpt,
                          int dukptKeySlot,
                          String data,
                          String dataEncrypted){
            this.ksn = ksn.replaceAll(" ", "" );
            this.dukpt_ipek = dukpt_ipek.replaceAll(" ", "" );
            this.loadDukpt = loadDukpt;
            this.isPanDukpt = isPanDukpt;
            this.dukptKeySlot = dukptKeySlot;
            this.data = data.replaceAll(" ", "" );
            this.dataEncrypted = dataEncrypted.replaceAll(" ", "" );
        }
        boolean checkEncrypted( String encrypted ){
            if( dataEncrypted.toUpperCase().equals(encrypted.toUpperCase()) ) {
                return true;
            } else {
                Log.e(TAG, "want: " + this.dataEncrypted + ", got: " + encrypted);
            }
            return false ;
        }

    }

    DukptKeys dukptKeys[] = {
            new DukptKeys(
                    "FFFF9876543210E00001",
                    "6AC292FA A1315B4D 858AB3A3 D7D5933A ",
                    true, true, 2,
                    "0123456789ABCDEFFEDCBA9876543210",
                    "82E85167 88FC52E6 C1EE7488 4B4426E5 "),
    };

    public void testDukptKeys( int index ) {
        boolean bRet = false;

        Log.d(TAG, "call test dukpt keys with index:" + index);

        try {
            if( dukptKeys[index].loadDukpt ) {

                Bundle dukptKeyExExtend = new Bundle();
                dukptKeyExExtend.putBoolean("loadPlainKey", true);
                bRet = ServiceHelper.getInstance().getPinpad().loadDukptKeyEX(dukptKeys[index].dukptKeySlot,
                        Utility.hexStr2Byte(dukptKeys[index].ksn),
                        Utility.hexStr2Byte(dukptKeys[index].dukpt_ipek),
                        null, dukptKeyExExtend);
                if (bRet) {

                } else {
                    Log.e(TAG, "load loadDukptKeyEX key fails");
                    // return false;
                }
            }

            // do data encrypt
            if( dukptKeys[index].data.length() > 0 ) {

                byte[] ret = ServiceHelper.getInstance().getPinpad().dukptEncryptData(1, 2,
                        dukptKeys[index].dukptKeySlot,
                        Utility.hexStr2Byte( dukptKeys[index].data ),
                        null);
                String result = Utility.byte2HexStr(ret);
                Log.d(TAG, result);
                if( dukptKeys[index].checkEncrypted(result)){
                    Log.d( TAG, "check value success");
                } else {
                    Log.e( TAG, "check fails");
                }
            }


        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    void showConfirm(String title, String message ){
        Log.d(TAG, "show confirm dialog");
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle( title );
        build.setMessage( message );
        build.setPositiveButton("Dismiss", null);
        AlertDialog alertDialog = build.create();
        alertDialog.show();
    }

    public void doStartCustonViewPinpad() {
        Intent intent = new Intent(this, CustomPinActivity.class);
        startActivity(intent);
    }

    void doShowSerialPort() {
        Intent intent = new Intent(this, SerialPortActivity.class);
        startActivity(intent);
    }

    private int transBase_print_index = 0;
    private void doShowPrint(){

        startActivity(new Intent(SettingsActivity.this, PreviewFontsActivity.class));

//        transBasic.printTest(transBase_print_index );
//        ++ transBase_print_index;
//        if( transBase_print_index > 2 ){
//            transBase_print_index = 0;
//        }
    }

    SSLComm sslComm = null;
    int sslTestStep = 0;
    void testSSL( ){
        SSLClient sslClient = null;
        switch (sslTestStep){
            case 0:
                break;
            case 1:
                sslClient = new SSLClient(sslComm, SSLClient.ACTION.connect);
                break;
            case 2:
                sslClient = new SSLClient(sslComm, SSLClient.ACTION.send);
                break;
            case 3:
                sslClient = new SSLClient(sslComm, SSLClient.ACTION.receive);

                break;
            case 4:
                sslClient = new SSLClient(sslComm, SSLClient.ACTION.close);
                break;
        }

        if( null != sslClient)
        {
            sslClient.start();
        }

        ++sslTestStep;
    }
	void testSSL(final Context context){
        Log.d(TAG, "Call input key type to clear");

        if(sslComm == null ){
            Log.d(TAG, "Inside conn");
            //sslComm = new SSLComm(context, "keystore/kclient.bks", "10.172.24.90", 5502);
            sslComm = new SSLComm(context, "keystore/ca.pem", "172.26.208.123", 9095);
//            sslComm = new SSLComm(context, "comm/ca.pem", "127.0.0.1", 5003);
//            sslComm = new SSLComm(context, "comm/ca.pem", "10.172.8.50", 20444);
//            sslComm = new SSLComm(context, "comm/subCA7828.cer", "10.172.8.50", 20444);
//            sslComm = new SSLComm(context, "comm/VHQSrvSSL.pem", "192.30.122.153", 443 );

//            sslComm = new SSLComm(context, "comm/client1.crt", "10.172.8.50", 5678);
        }

        String[] typeNameList = {
                "Create",
                "Connect",
                "send",
                "receive",
                "close",
        };
        if( sslTestStep >= typeNameList.length ){
            sslTestStep = 0;
        }
        final int[] selectIndex = {sslTestStep};
//        sslTestStep++;
//        if( sslTestStep >= typeNameList.length ){
//            sslTestStep = 0;
//        }

        AlertDialog.Builder builder = new AlertDialog.Builder( context );
        builder.setTitle("Select types to Test");
        builder.setSingleChoiceItems(typeNameList, selectIndex[0], new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sslTestStep = which;
            }
        } );

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "on confirm");
                testSSL();

                //testSSL(context);
            }
        });
        builder.setNegativeButton("Cancel",null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

   /* void testSSL(Context context){
        Log.d(TAG, "Call input key type to clear");

        if(sslComm == null ){
            sslComm = new SSLComm(context, "keystore/kclient.bks", "10.172.24.90", 5502);
//            sslComm = new SSLComm(context, "comm/ca.pem", "127.0.0.1", 5003);
//            sslComm = new SSLComm(context, "comm/ca.pem", "10.172.8.50", 20444);
//            sslComm = new SSLComm(context, "comm/subCA7828.cer", "10.172.8.50", 20444);
//            sslComm = new SSLComm(context, "comm/VHQSrvSSL.pem", "192.30.122.153", 443 );

//            sslComm = new SSLComm(context, "comm/client1.crt", "10.172.8.50", 5678);
        }

        String[] typeNameList = {
                "Create",
                "Connect",
                "send",
                "receive",
                "close",
        };
        if( sslTestStep >= typeNameList.length ){
            sslTestStep = 0;
        }
        final int[] selectIndex = {sslTestStep};
//        sslTestStep++;
//        if( sslTestStep >= typeNameList.length ){
//            sslTestStep = 0;
//        }

        AlertDialog.Builder builder = new AlertDialog.Builder( context );
        builder.setTitle("Select types to Test");
        builder.setSingleChoiceItems(typeNameList, selectIndex[0], new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sslTestStep = which;
            }
        } );

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "on confirm");
                testSSL();
            }
        });
        builder.setNegativeButton("Cancel",null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }*/
	

}
