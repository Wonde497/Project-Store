package com.verifone.demo.emv.basic;
/*
 *  author: Derrick
 *  Time: 2019/5/24 17:18
 */

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class entershift extends Dialog implements View.OnClickListener {
    private View change;
   // private View newpass;
   EditText newpass,name;
    String NAME_COL = "name";
Button entershift,exitshift,exit;
    private Context mContext;

    OnChooseListener onChooselistener;

    public void setOnChooselistener(OnChooseListener onChooselistener) {
        this.onChooselistener = onChooselistener;
    }

    public entershift(Context context) {
        super(context);
        this.mContext = context;
    }

    public entershift(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public entershift(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entershift);
        setCanceledOnTouchOutside(false);
        initView();

    }

    private void initView(){

        entershift = findViewById(R.id.entershift);
        entershift.setOnClickListener(this);

        exit = findViewById(R.id.exit);
        exit.setOnClickListener(this);

        exitshift = findViewById(R.id.exitshift);
        exitshift.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.entershift:
                if(onChooselistener != null){
                    onChooselistener.onClickentershift();;
                    Log.d("Manageshift","Entershift");
                }
                this.dismiss();
                break;
            case R.id.exitshift:
                if(onChooselistener != null){
                    onChooselistener.onClickexitshift();;
                    Log.d("Manageshift","Exitshift");
                }
                this.dismiss();
                break;
            case R.id.exit:
                if(onChooselistener != null){
                    onChooselistener.onClickexit();;
                    Log.d("Manageshift","Exit");
                }
                this.dismiss();
                break;

            default:
                this.dismiss();
                break;
        }
    }

    public interface OnChooseListener
    {
        void onClickentershift();
        void onClickexitshift();
        void onClickexit();

    }
}
