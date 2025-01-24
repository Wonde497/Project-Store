package com.verifone.demo.emv;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.verifone.demo.emv.Public_data.ISO8583msg;
import com.verifone.demo.emv.data_handlers.User;

import java.util.ArrayList;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link transactionactivity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class transactionactivity extends Fragment {
       private DBHandler dbHandler;DBHandler.user_functions user_fun; User mLog;
public static Boolean search=false;
    private RecyclerView mRecyclerView;

    private transactionRecyclerAdapter mRecyclerAdapter;

    ArrayList<User> myList;public String user_type;
    Transactiondata transactiondata;
    SharedPreferences sharedPreferences;
    RecyclerView.Adapter adapter;
    View view_frag;
    Button searchbutton;
    ImageButton backk;
    EditText input;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view_frag=inflater.inflate(R.layout.activitytransaction, container, false);
        myList = new ArrayList<>();
        mRecyclerView = (RecyclerView)view_frag.findViewById(R.id.recycler);
        input = (EditText)view_frag.findViewById(R.id.inputsearch);
        searchbutton = (Button)view_frag.findViewById(R.id.search);
        backk = (ImageButton) view_frag.findViewById(R.id.back);
        mRecyclerAdapter = new transactionRecyclerAdapter(view_frag.getContext(),myList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        myList.clear();
        String selection="";
        String[] selectionArgs = {};
        search=false;
          /* dbHandler = new DBHandler(root.getContext());
        user_fun=dbHandler.new user_functions();
        mLog = new user();
        List<user> userList = new ArrayList<user>();
        userList= user_fun.viewUsers(selection,selectionArgs,user_type);
        if(userList.size()>0){
            for(int i=0;i<userList.size();i++){
                mLog.setName(userList.get(i).getName());
                mLog.setActiontype("delete");
                myList.add(mLog);
                mRecyclerAdapter.notifyData(myList);
            }

        }else{

        }*/
       /* mLog = new user();
        mLog.setTransactionnum("12"+String.valueOf(1));
        mLog.setMerchname("kaleb "+String.valueOf(1));
        mLog.setMerchid("189239"+String.valueOf(1));
        mLog.setTermid("45276879"+String.valueOf(1));
        mLog.setBalance("67"+String.valueOf(1));
        mLog.setCardnum("10002345678"+String.valueOf(1));
        myList.add(0,mLog);
        Log.d("TAG","1"+myList.get(0).getMerchname());
        user mLog1 = new user();

        //   mRecyclerAdapter.notifyData(myList);
        mLog1.setTransactionnum("12"+String.valueOf(2));
        mLog1.setMerchname("kaleb "+String.valueOf(2));
        mLog1.setMerchid("189239"+String.valueOf(2));
        mLog1.setTermid("45276879"+String.valueOf(2));
        mLog1.setBalance("67"+String.valueOf(2));
        mLog1.setCardnum("10002345678"+String.valueOf(2));


        myList.add(1,mLog1);
        Log.d("TAG","1"+myList.get(0).getMerchname());
        Log.d("TAG","2"+myList.get(1).getMerchname());
        //mRecyclerAdapter.notifyData(myList);*/
//card view specific report.......................................................
        sharedPreferences=view_frag.getContext().getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
       transactiondata=new Transactiondata(sharedPreferences, view_frag.getContext());
        dbHandler = new DBHandler(view_frag.getContext());
       // dbHandler.addtransactionfile("dateeeeeeeeeeeeeeee000000","transactionfile","transactionfile","transactionfile","transactionfile","transactionfile","transactionfile","9.13POS9TER1              220831104947FO00050001B000000000000010000F062034  G@@@@@@@@P1gAPPROVALh00000009706Q01004B262C76612E77513030");
       // Log.d(TAG, "checkd(ata "+dbHandler.gettransactiondata().get(0).getDateandtime());
        ISO8583msg sp=new ISO8583msg(view_frag.getContext());
        sp.loadData1();//merchant
        int size=dbHandler.getdatasize();
        User mLog1 = new User();
        ArrayList<User> arrayList = new ArrayList<>();
        arrayList.clear();
        arrayList=MenuActivity.arrayList;
        int recysize=1;
        Log.d("sizeeee recycle", ""+MenuActivity.arrayList.size());

       /* for(int i=0;i<50;i++)
        {
         //   transactiondata.unpack(dbHandler.gettransactiondata().get(i).getRecievedtran());
            recysize=arrayList.size();
         if(arrayList.size()>i) {
    mLog1.setTransactionnum(String.valueOf(i));
    mLog1.setDateandtime(arrayList.get(i).getDateandtime());

    mLog1.setCashier("");
    mLog1.setMerchid(SPDH.Mer_id);
    mLog1.setTermid(arrayList.get(i).getTerminal_id());
    mLog1.setAppcode(arrayList.get(i).getFfield());
    mLog1.setCardnum(arrayList.get(i).getCardnum());
    mLog1.setTpe(arrayList.get(i).getTpe());
             Log.d("aid", arrayList.get(i).getCup());

    //mLog1.setTpe(MenuActivity.txn_type1);
    mLog1.setCup(arrayList.get(i).getCup());
    mLog1.setAid(arrayList.get(i).getAid());
    mLog1.setBalance(arrayList.get(i).getCurrency() + " " + Utility.getReadableAmount(arrayList.get(i).getBfield()));

             if (!arrayList.get(i).getQfield().equals("") && arrayList.get(i).getQfield() != null &&
            arrayList.get(i).getQfield().length() >= 20) {
        mLog1.setArpc(arrayList.get(i).getQfield().substring(4, 20));
    }
    mLog1.setResponsecode(arrayList.get(i).getResponse_Code());
    mLog1.setAppstatus(arrayList.get(i).getgfield());
    mLog1.setTvr(arrayList.get(i).getTvr());

    myList.add(mLog1);
    mRecyclerAdapter.notifyData(myList);

}

        }*/

        mRecyclerAdapter.notifyData(dbHandler.gettransactiondata());

       /* for(int i=0;i<8;i++){
            user mLog1 = new user();
            mLog1.setTransactionnum("12"+String.valueOf(i));
            mLog1.setMerchname("kaleb "+String.valueOf(i));
            mLog1.setMerchid("189239"+String.valueOf(i));
            mLog1.setTermid("45276879"+String.valueOf(i));
            mLog1.setBalance("67"+String.valueOf(i));
            mLog1.setCardnum("10002345678"+String.valueOf(i));
            myList.add(mLog1);
            mRecyclerAdapter.notifyData(myList);
        }*/
searchbutton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view)
    {
        if(!input.getText().toString().equals("")&&dbHandler.getdatasize()>=Integer.valueOf(input.getText().toString())&&Integer.valueOf(input.getText().toString())>0)
        {
        search(input.getText().toString());
        }
        else{
             Toast.makeText(getContext(), "PLEASE ENTER TXN NUMBER, TOTAL TXN IS   "+dbHandler.getdatasize(), Toast.LENGTH_SHORT).show();
        }
    }
     });
        backk.setVisibility(View.GONE);

        backk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    search=false;
                mRecyclerAdapter.notifyData(dbHandler.gettransactiondata());
                    backk.setVisibility(View.GONE);
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!mRecyclerView.canScrollVertically(1)) {
                   // Toast.makeText(getContext(), "down end", Toast.LENGTH_SHORT).show();
                   if(!search) {
                     //  mRecyclerAdapter.notifyData(MenuActivity.arrayList);
                   }


                }
            }
        });
        return view_frag;
    }

public void search(String i ){
    backk.setVisibility(View.VISIBLE);
    search=true;
    ISO8583msg sp=new ISO8583msg(view_frag.getContext());
    sp.loadData1();
    User mLog1 = new User();
   myList.clear();
   /* transactiondata.unpack(dbHandler.getRecievedtransaction(Integer.valueOf(i)));
    mLog1.setTransactionnum(String.valueOf(Integer.valueOf(i)-1));
    mLog1.setMerchname("kaleb "+String.valueOf(i));
    mLog1.setMerchid("189239"+String.valueOf(i));
    mLog1.setTermid("45276879"+String.valueOf(i));
    mLog1.setBalance(Transactiondata.Subfieldresponse.B);
    mLog1.setCardnum("10002345678"+String.valueOf(i));
    myList.add(mLog1);*/

   /* mLog1.setTransactionnum(String.valueOf(i));
    mLog1.setDateandtime(dbHandler.gettransactiondata().get(Integer.valueOf(i)).getDateandtime());

    mLog1.setCashier("");
    mLog1.setMerchid(SPDH.Mer_id);
    mLog1.setTermid(dbHandler.gettransactiondata().get(Integer.valueOf(i)).getTerminal_id());
    mLog1.setAppcode(dbHandler.gettransactiondata().get(Integer.valueOf(i)).getFfield());
    mLog1.setCardnum(dbHandler.gettransactiondata().get(Integer.valueOf(i)).getCardnum());
    mLog1.setTpe(dbHandler.gettransactiondata().get(Integer.valueOf(i)).getTpe());

    //mLog1.setTpe(MenuActivity.txn_type1);
    mLog1.setCup(dbHandler.gettransactiondata().get(Integer.valueOf(i)).getCup());
    mLog1.setAid(dbHandler.gettransactiondata().get(Integer.valueOf(i)).getAid());
    mLog1.setBalance(dbHandler.gettransactiondata().get(Integer.valueOf(i)).getCurrency() + " " + Utility.getReadableAmount(dbHandler.gettransactiondata().get(Integer.valueOf(i)).getBfield()));

    if (!dbHandler.gettransactiondata().get(Integer.valueOf(i)).getQfield().equals("") && dbHandler.gettransactiondata().get(Integer.valueOf(i)).getQfield() != null &&
            dbHandler.gettransactiondata().get(Integer.valueOf(i)).getQfield().length() >= 20) {
        mLog1.setArpc(dbHandler.gettransactiondata().get(Integer.valueOf(i)).getQfield().substring(4, 20));
    }
    mLog1.setResponsecode(dbHandler.gettransactiondata().get(Integer.valueOf(i)).getResponse_Code());
    mLog1.setAppstatus(dbHandler.gettransactiondata().get(Integer.valueOf(i)).getAppstatus());
    mLog1.setTvr(dbHandler.gettransactiondata().get(Integer.valueOf(i)).getTvr());*/
    mLog1=dbHandler.gettransactiondata().get(Integer.valueOf(i)-1);
    mLog1.setTransactionnum(String.valueOf(i));
    myList.add(mLog1);
    mRecyclerAdapter.notifyData(myList);

}
    public void delete(String i ){
        User mLog1 = new User();
        myList.clear();
       // transactiondata.unpack(dbHandler.getRecievedtransaction(Integer.valueOf(i)));
        mLog1.setTransactionnum(String.valueOf(i));
        mLog1.setMerchname("kaleb "+String.valueOf(i));
        mLog1.setMerchid("189239"+String.valueOf(i));
        mLog1.setTermid("45276879"+String.valueOf(i));
       // mLog1.setBalance(Transactiondata.Subfieldresponse.B);
        mLog1.setField_02("10002345678"+String.valueOf(i));
        myList.add(mLog1);
        mRecyclerAdapter.notifyData(myList);
    }


}