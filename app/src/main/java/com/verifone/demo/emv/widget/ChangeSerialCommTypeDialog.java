package com.verifone.demo.emv.widget;
/*
 *  author: Derrick
 *  Time: 2019/5/24 17:18
 */

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;
public class ChangeSerialCommTypeDialog extends Dialog implements View.OnClickListener {


    private Context mContext;
    OnChooseListener onChooselistener;

    Button btnConfirm;
    Button btnRs232;
    Button btnUsbRs232;
    Button btnUsb2Rs232VfRs232;
    Button btnV34Modem;
    Button btnC680Dongle;
    Button btnZtek;

    EditText etInput;



    public void setOnChooselistener(OnChooseListener onChooselistener) {
        this.onChooselistener = onChooselistener;
    }

    public ChangeSerialCommTypeDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public ChangeSerialCommTypeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public ChangeSerialCommTypeDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choose_serial_protocol);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView(){
        btnConfirm = findViewById(R.id.btn_confirm);
        btnRs232 = findViewById(R.id.bt_rs232);
        btnUsbRs232 = findViewById(R.id.btn_usb_rs232);
        btnUsb2Rs232VfRs232 = findViewById(R.id.btn_vf_rs232);
        btnV34Modem = findViewById(R.id.btn_v34modem);
        btnC680Dongle = findViewById(R.id.c680dongle);
        btnZtek = findViewById(R.id.btn_ztek);

        etInput = findViewById(R.id.et_protocol_type);

        btnConfirm.setOnClickListener(this);
        btnRs232.setOnClickListener(this);
        btnUsbRs232.setOnClickListener(this);
        btnUsb2Rs232VfRs232.setOnClickListener(this);
        btnV34Modem.setOnClickListener(this);
        btnC680Dongle.setOnClickListener(this);
        btnZtek.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.bt_rs232:
                onChooselistener.onTypeChosen("rs232");
                break;
            case R.id.btn_usb_rs232:
                onChooselistener.onTypeChosen("usb-rs232");
                break;
            case R.id.btn_vf_rs232:
                onChooselistener.onTypeChosen("usb2rs232-VF-RS232");
                break;
            case R.id.btn_v34modem:
                onChooselistener.onTypeChosen("usb2rs232-VF-V34Modem");
                break;
            case R.id.c680dongle:
                onChooselistener.onTypeChosen("usb2rs232-VF-C680DongleModem");
                break;
            case R.id.btn_ztek:
                onChooselistener.onTypeChosen("usb2rs232-Z-TEK");
                break;
            case R.id.btn_confirm:
                String type = etInput.getText().toString();
                onChooselistener.onTypeChosen(type);
                break;
            default:
                return;
        }

    }

    public interface OnChooseListener{
        void onTypeChosen(String type);
    }


}
