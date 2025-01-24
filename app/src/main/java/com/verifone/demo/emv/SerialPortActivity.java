package com.verifone.demo.emv;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.verifone.demo.emv.Utilities.ServiceHelper;
import com.verifone.demo.emv.Utilities.Utility;
import com.verifone.demo.emv.widget.ChangeSerialCommTypeDialog;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class SerialPortActivity extends AppCompatActivity {
    static final String TAG = "EMVDemo-SerialPort";

    private TextView mText2Read;
    private EditText mText2Write;
    private TextView mTextDescription;

    Button btnWrite;
    Button btnRead;

    Button btnChangeProtocol;
    TextView mTextCurrProtocol;

    SerialCom serialCom;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_usb_uart:
                    mTextDescription.setText(R.string.description_COM_USB_UART);
                    mText2Read.setText(R.string.title_COM_USB_UART);
                    serialPortProtocolShow();
                    serialCom.initialize( SerialCom.SerialType.UART, 115200, 0, 8 );
                    serialCom.open();
                    return true;
                case R.id.navigation_com_base_rs232:
                    mTextDescription.setText(R.string.description_COM_BASE_RS232);
                    mText2Read.setText(R.string.title_COM_BASE_RS232);
                    serialCom.initialize( SerialCom.SerialType.RS232, 115200, 0, 8 );
                    serialCom.open();
                    serialPortProtocolGone();
                    return true;
                case R.id.navigation_com_otg_usb2Serial:
                    mTextDescription.setText(R.string.description_COM_OTG_USB2Serial);
                    mText2Read.setText(R.string.title_COM_OTG_USB2Serial);
                    serialCom.initialize( SerialCom.SerialType.OTGSerial, 115200, 0, 8 );
                    serialCom.open();
                    serialPortProtocolGone();
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_port);

        mText2Read = (TextView) findViewById(R.id.message);
        mText2Write = findViewById(R.id.messageOut);
        mTextDescription = findViewById(R.id.description);
        mTextCurrProtocol = findViewById(R.id.tv_protocol_status);
        btnChangeProtocol = findViewById(R.id.bt_change_protocol);
        btnChangeProtocol.setOnClickListener(onClickListener);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        btnRead = findViewById(R.id.btnSerialRead);
        btnRead.setOnClickListener(onClickListener);
        btnWrite= findViewById(R.id.btnSerialWrite);
        btnWrite.setOnClickListener(onClickListener);

        serialCom = new SerialCom();
        serialCom.initialize( SerialCom.SerialType.UART, 115200, 0, 8 );
        serialCom.open();

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @SuppressLint("ResourceAsColor")
        @Override
        public void onClick(View v) {
            if( v == btnRead ){
                if( serialCom.open() ){
                    byte[] buffer = new byte[32];
                    btnRead.setClickable(false);
//                    btnRead.setTextColor(R.color.gray);

                    int len = serialCom.read( buffer, 8,20000);
//                    btnRead.setTextColor(R.color.black);
                    btnRead.setClickable(true);
                    String msg = Utility.byte2HexStr(buffer);
                    Log.d( TAG, "read:" + msg);
                    mText2Read.append("\r\nRead:\r\n");
                    mText2Read.append(msg);


                }
            } else if( v == btnWrite ){
                if( serialCom.open() ){
                    String msg = mText2Write.getText().toString();
                    serialCom.write( msg.getBytes(), msg.length() );

                }

            } else if (v == btnChangeProtocol){
                final ChangeSerialCommTypeDialog dialog = new ChangeSerialCommTypeDialog(SerialPortActivity.this);
                dialog.setOnChooselistener(new ChangeSerialCommTypeDialog.OnChooseListener() {
                    @Override
                    public void onTypeChosen(String type) {

                        serialCom.initialize(SerialCom.SerialType.UART, type, 115200, 0, 8);
                        serialCom.open();
                        mTextCurrProtocol.setText("Current protocol:\n" + type);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        }
    };
    void serialPortProtocolGone() {
        mTextCurrProtocol.setVisibility(View.INVISIBLE);
        btnChangeProtocol.setVisibility(View.INVISIBLE);
    }

    void serialPortProtocolShow() {
        mTextCurrProtocol.setVisibility(View.VISIBLE);
        btnChangeProtocol.setVisibility(View.VISIBLE);
    }



}
