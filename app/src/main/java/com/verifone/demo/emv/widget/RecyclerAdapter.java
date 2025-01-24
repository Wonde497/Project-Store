package com.verifone.demo.emv.widget;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
//import android.support.v7.widget.cardview;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.verifone.demo.emv.Clicklistner;
import com.verifone.demo.emv.DBHandler;
import com.verifone.demo.emv.data_handlers.User;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerItemViewHolder> {
    private ArrayList<User> myList; DBHandler dbHandler;
    int mLastPosition = 0;String s;Clicklistner click;Context c;String usertp;
    public RecyclerAdapter(ArrayList<User> myList, Clicklistner cl, Context con, String user) {
        this.myList = myList; this.click=cl; this.c=con;this.usertp=user;
        //android.support.v7.cardview.R
    }
    public RecyclerItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view, parent, false);
        RecyclerItemViewHolder holder = new RecyclerItemViewHolder(view);

        return holder;
    }
    @Override
    public void onBindViewHolder(final RecyclerItemViewHolder holder, final int position) {
        Log.d("onBindViewHoler ", myList.size() + "");
        Toast.makeText(c, myList.get(0).getName(), Toast.LENGTH_SHORT).show();
        dbHandler = new DBHandler(c);
        final DBHandler.user_functions user_fun=dbHandler.new user_functions();
        String selection="";
        String[] selectionArgs = {};
        List<User> userList = new ArrayList<User>();
        //userList= user_fun.viewUsers(selection,selectionArgs,usertp);
        holder.etDescriptionTextView.setText(userList.get(position).getName());
        if(Objects.equals(userList.get(position).getStatus(),"Enabled")){
           holder.swi.setChecked(true);
           holder.swi.setText("Enabled");
       }else if(Objects.equals(userList.get(position).getStatus(),"DISABLED")){
           holder.swi.setChecked(false);
           holder.swi.setText("Disabled");
       }

if(Objects.equals(myList.get(position).getActiontype(),"enabledisable")){
    holder.icon.setVisibility(View.GONE);
    holder.swi.setVisibility(View.VISIBLE);
}
else if(Objects.equals(myList.get(position).getActiontype(),"view")){
    holder.icon.setVisibility(View.GONE);
    holder.swi.setVisibility(View.GONE);

}
else if(Objects.equals(myList.get(position).getActiontype(),"delete")){
    holder.icon.setVisibility(View.VISIBLE);
    holder.swi.setVisibility(View.GONE);
}

        final List<User> finalUserList1 = userList;
        holder.icon.setOnClickListener(
                new View.OnClickListener() {
    @Override
    public void onClick(View view) {
     // click.delete(myList.get(position).getName());
     // user_fun.deleterow(finalUserList1.get(position).getName());
       // myList.clear();
click.createfragment();

      //notifyDataSetChanged();
    }
});
        final List<User> finalUserList = userList;
        holder.swi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b){
             holder.swi.setText("Enabled");
             user_fun.enabledisable(finalUserList.get(position).getName(),true);
         }else{
                    holder.swi.setText("Disabled");
                    user_fun.enabledisable(finalUserList.get(position).getName(),false);
              }
            }
        });
        mLastPosition =position;

    }
    @Override
    public int getItemCount() {
        return(null != myList?myList.size():0);
    }public void notifyData(ArrayList<User> myList) {
        Log.d("notifyData ", myList.size() + "");
        this.myList = myList;
        notifyDataSetChanged();
    }
    public class RecyclerItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView etDescriptionTextView;
        private FrameLayout mainLayout;
        LinearLayout linearLayout,linearLayoutmain;
        Switch swi;
        public ImageView icon;
       // public CardView cardViewfor,cardViewgif,card;
        public RecyclerItemViewHolder(final View parent) {
            super(parent);
            etDescriptionTextView = (TextView)parent.findViewById(R.id.txtDescription);
            swi=(Switch) parent.findViewById(R.id.switch1);
           icon = (ImageView) parent.findViewById(R.id.imageButton);

        }
    }
}