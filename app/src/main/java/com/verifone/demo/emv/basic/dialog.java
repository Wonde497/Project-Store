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
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class dialog extends Dialog implements View.OnClickListener {
    private View change;
    public static boolean isdialog=false;
   // private View newpass;
   EditText newpass,name;
    String mess="",NAME_COL = "name";
TextView message;
    private Context mContext;

    OnChooseListener onChooselistener;String user_type;

    public void setOnChooselistener(OnChooseListener onChooselistener) {
        this.onChooselistener = onChooselistener;
    }

    public dialog(Context context,String message) {
        super(context);
        this.mContext = context;
        mess=message;
    }

    public dialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public dialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);
        setCanceledOnTouchOutside(false);
        initView();
        message.setText(mess);
        isdialog=true;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
              }
        }, 2000);
    }

    private void initView(){

        message = findViewById(R.id.name);

         }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.name:
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
