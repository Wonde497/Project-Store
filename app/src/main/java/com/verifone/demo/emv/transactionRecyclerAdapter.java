package com.verifone.demo.emv;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.verifone.demo.emv.Public_data.ISO8583msg;
import com.verifone.demo.emv.Utilities.Utility;
import com.verifone.demo.emv.data_handlers.User;
import com.verifone.demo.emv.transaction.TransBasic;
import com.verifone.demo.emv.transaction.canvas_printer.PrinterElement;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;
public class transactionRecyclerAdapter extends RecyclerView.Adapter<transactionRecyclerAdapter.RecyclerItemViewHolder> {
    private ArrayList<User> myList; DBHandler dbHandler;
    int mLastPosition = 0;String s;
    TransBasic transBasic;
    Context con;
    SharedPreferences sharedPreferences;
    public transactionRecyclerAdapter(Context context,ArrayList<User> myList) {
        this.myList = myList;
con=context;
    }
    public RecyclerItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclertransaction, parent, false);
        RecyclerItemViewHolder holder = new RecyclerItemViewHolder(view);
        sharedPreferences = con.getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
transBasic= TransBasic.getInstance(sharedPreferences);
        return holder;
    }
    @Override
    public void onBindViewHolder(final RecyclerItemViewHolder holder, final int position) {
        Log.d("position", ""+position);

       // int trannum=Integer.parseInt(myList.get(position).getTransactionnum()) + 1;
        int trannum=position+ 1;
        ISO8583msg.loadDatamrname();
if(transactionactivity.search)
{
    trannum=Integer.parseInt(myList.get(position).getTransactionnum());
    holder.transaction.setText("TRANSACTION NO: "+trannum);
}else{
        holder.transaction.setText("TRANSACTION NO: "+trannum);
}
        holder.merchid.setText("MERCHANT ID: "+ ISO8583msg.Mer_id);
        holder.cashier.setText("MERCHANT NAME: "+ ISO8583msg.Mer_name);
        holder.terid.setText("TERMINAL ID: "+myList.get(position).getField_41());
        holder.cardnum.setText("CARD NO: "+ Utility.fixCardNoWithMask(myList.get(position).getField_02()));
        holder.balance.setText("AMOUNT: "+myList.get(position).getCurrency() + " " + Utility.getReadableAmount(myList.get(position).getField_04()));
        holder.dateandtime.setText(myList.get(position).getDateandtime());
        holder.appcode.setText("APPCODE: "+myList.get(position).getField_38());
        holder.cup.setText("CUP: "+myList.get(position).getCup());
        holder.aid.setText("AID: "+myList.get(position).getAid());
        holder.resposecode.setText("RESPONSE CODE: "+myList.get(position).getField_39());
        holder.rrn.setText("RRN: "+myList.get(position).getField_37());

        if(myList.get(position).getTxn_type().equals("REVERSAL"))
        {
            holder.appstatus.setText("APPROVED");

        }else {
            holder.appstatus.setText("APPROVED");

        }
        holder.tvr.setText("TVR: "+myList.get(position).getTvr());
        holder.trantype.setText(myList.get(position).getTxn_type());
        Bitmap  bitmap ;
        if(!sharedPreferences.getString("imageuri", "null").equals("null")){
            try {
                bitmap= MediaStore.Images.Media.getBitmap(con.getContentResolver(), Uri.parse(sharedPreferences.getString("imageuri","null")));
                bitmap=Bitmap.createScaledBitmap(bitmap,250,250,false);
                holder.imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else {
            try {
            PrinterElement printerElement = new PrinterElement();
            InputStream is = con.getAssets().open("amharic_gbe_black_logo.png");
            bitmap = BitmapFactory.decodeStream(is);
                holder.imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    holder.print.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("printtype","specificreport");
        if(transactionactivity.search){
            editor.putString("trn",String.valueOf(Integer.parseInt(myList.get(position).getTransactionnum())-1));

        }else{
            editor.putString("trn",String.valueOf(position));
        }
        editor.commit();
        transBasic.printTest(1);

    }
   });
        mLastPosition =position;

    }
    @Override
    public int getItemCount() {
        return(null != myList?myList.size():0);
    }
    public void notifyData(ArrayList<User> myList) {
        Log.d("notifyData ", myList.size() + "");
        this.myList = myList;
        notifyDataSetChanged();
    }
    public class RecyclerItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView trantype,dateandtime,transaction,cashier,merchid,terid,cardnum,balance,appcode,cup,aid,rrn,resposecode,appstatus,tvr;
        Button print;
        ImageView imageView;
        public RecyclerItemViewHolder(final View parent) {
            super(parent);
            dateandtime = (TextView)parent.findViewById(R.id.dateandtime);
            trantype = (TextView)parent.findViewById(R.id.trnstype);
            imageView = (ImageView) parent.findViewById(R.id.imagelayout);
            cashier = (TextView)parent.findViewById(R.id.cashiername);
            appcode = (TextView)parent.findViewById(R.id.appcode);
            cup = (TextView)parent.findViewById(R.id.cup);
            aid = (TextView)parent.findViewById(R.id.aid);
            rrn = (TextView)parent.findViewById(R.id.arpc);
            resposecode = (TextView)parent.findViewById(R.id.responsecode);
            appstatus = (TextView)parent.findViewById(R.id.appstatuse);
            tvr = (TextView)parent.findViewById(R.id.tvr);

            transaction = (TextView)parent.findViewById(R.id.transactionnumber);
            merchid = (TextView)parent.findViewById(R.id.merchantid);
            terid = (TextView)parent.findViewById(R.id.terminalid);
            cardnum = (TextView)parent.findViewById(R.id.cardnumber);
            balance = (TextView)parent.findViewById(R.id.balance);
            print = (Button)parent.findViewById(R.id.print);

        }
    }
}