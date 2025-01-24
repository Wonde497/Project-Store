package com.verifone.demo.emv.basic;
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
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class changepassdialog extends Dialog implements View.OnClickListener {

    private View change;
   // private View newpass;
   EditText newpass,name;
    String NAME_COL = "name";

    private Context mContext;

    OnChooseListener onChooselistener;String user_type;

    public void setOnChooselistener(OnChooseListener onChooselistener) {
        this.onChooselistener = onChooselistener;
    }

    public changepassdialog(Context context,String usertyp) {
        super(context);
        this.mContext = context;this.user_type=usertyp;
    }

    public changepassdialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public changepassdialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepassdialog);
        setCanceledOnTouchOutside(false);
        initView();

    }

    private void initView(){

        change = findViewById(R.id.change);
       change.setOnClickListener(this);
       newpass = findViewById(R.id.newpass);
        name=findViewById(R.id.name);

         }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.change:
               /* if(onChooselistener != null){
                    onChooselistener.onchange(newpass.getText().toString());
                    Log.d("EMVDemo","TransBasic init");
                }*/
          if(Objects.equals(name.getText().toString(), "")){
              Toast.makeText(mContext,"Enter name", Toast.LENGTH_SHORT).show();


          }else if(Objects.equals(newpass.getText().toString(), "")){
              Toast.makeText(mContext,"Enter password", Toast.LENGTH_SHORT).show();

          }
          else{
               /* String[] selectionArgs = {name.getText().toString()};
                DBHandler dbHandler;
                dbHandler = new DBHandler(mContext);
                DBHandler.user_functions user_fun=dbHandler.new user_functions();
                if(user_fun.viewUsers(NAME_COL + "=?",selectionArgs,user_type).size()>0){
                    user_fun.changepin(user_type,name.getText().toString(),newpass.getText().toString());
                    Toast.makeText(mContext,"password changed", Toast.LENGTH_SHORT).show();
                    this.dismiss();
                }else{
                    Toast.makeText(mContext,"name doesnt exist", Toast.LENGTH_SHORT).show();
                }*/
          }
                this.dismiss();
                break;

        }
    }

    public interface OnChooseListener{
        void onchange(String newpas);

    }


}
