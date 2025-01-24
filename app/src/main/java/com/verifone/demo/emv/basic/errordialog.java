package com.verifone.demo.emv.basic;
/*
 *  author: Derrick
 *  Time: 2019/5/24 17:18
 */

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.verifone.demo.emv.transaction.CheckCardActivity;
import com.verifone.demo.emv.transaction.TransBasic;

import java.util.Objects;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class errordialog extends Dialog implements View.OnClickListener {
    private View change;
    public static boolean isdialog=false;
    // private View newpass;
    EditText newpass,name;
    String NAME_COL = "name";
    String mess="";
    TextView message;
    Button cancel,ok;

    private Context mContext;
    TransBasic transBasic;
    OnChooseListener onChooselistener;String user_type;

    public void setOnChooselistener(OnChooseListener onChooselistener) {
        this.onChooselistener = onChooselistener;
    }

    public errordialog(Context context) {
        super(context);
        this.mContext = context;
    }
    public errordialog(Context context,String message) {
        super(context);
        this.mContext = context;
        mess=message;
    }
    public errordialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public errordialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.errordialog);
        setCanceledOnTouchOutside(false);
        initView();
        Log.d("error", "error dialog");
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("Shared_data", Context.MODE_PRIVATE);

        transBasic = TransBasic.getInstance(sharedPreferences);
        isdialog=true;
        if(!mess.equals("")){
            message.setText(mess);
            cancel.setVisibility(View.VISIBLE);
            ok.setText("swipe");
        }else {
            cancel.setVisibility(View.GONE);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isdialog=false;
                    dismiss();
                }
            }, 2000);
        }


    }

    private void initView(){
        cancel = findViewById(R.id.cancel);
        message = findViewById(R.id.name);
        ok = findViewById(R.id.change);
        change = findViewById(R.id.change);

        change.setOnClickListener(this);
        cancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.change:
                transBasic.doPurchase();
                isdialog=false;
                this.dismiss();
                break;
            case R.id.cancel:
                TransBasic.checkCardActivity.finish();
                isdialog=false;
                this.dismiss();
                break;
            default:
                isdialog=false;
                this.dismiss();
                break;
        }
    }

    public interface OnChooseListener{
        void onchange(String newpas);

    }


}
