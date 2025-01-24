package com.verifone.demo.emv;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.RemoteException;
import android.support.v7.widget.RecyclerView;
//import android.cardview.widget.CardView;
import  android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.verifone.demo.emv.data_handlers.User;
import com.verifone.demo.emv.transaction.TransBasic;

import java.util.ArrayList;

import  cn.verifone.simon.verifone_x9_demo_emv.Global.R;
public class Applicationrecycler extends RecyclerView.Adapter<Applicationrecycler.RecyclerItemViewHolder> {
    private ArrayList<User> myList; DBHandler dbHandler;
    TransBasic transBasic;
    SharedPreferences sharedPreferences;
    int mLastPosition = 0;String s;Clicklistner click;Context con;String usertp;
    public Applicationrecycler(ArrayList<User> myList, Context context) {
        this.myList = myList;
        con=context;
    }
    public RecyclerItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.applicationrecycle, parent, false);
        RecyclerItemViewHolder holder = new RecyclerItemViewHolder(view);
        sharedPreferences = con.getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
        transBasic= TransBasic.getInstance(sharedPreferences);
        return holder;
    }
    @Override
    public void onBindViewHolder(final RecyclerItemViewHolder holder, final int position) {


holder.aidlab.setText(myList.get(position).getAidlable());
     holder.select.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        try {
            transBasic.selectapp(position+1);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        ((Activity)con).finish();

    }
});
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    transBasic.selectapp(position+1);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                ((Activity)con).finish();

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
        private final TextView aidlab;
        public CardView card;
        ImageButton select;
        public RecyclerItemViewHolder(final View parent) {
            super(parent);
             aidlab = (TextView)parent.findViewById(R.id.aidlable);
           select = (ImageButton) parent.findViewById(R.id.select);
            card = (CardView) parent.findViewById(R.id.card_view);

        }
    }
}