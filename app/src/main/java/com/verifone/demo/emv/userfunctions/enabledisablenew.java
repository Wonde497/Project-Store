package com.verifone.demo.emv.userfunctions;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.verifone.demo.emv.Clicklistner;
import com.verifone.demo.emv.DBHandler;
import com.verifone.demo.emv.data_handlers.User;
import com.verifone.demo.emv.widget.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link enabledisablenew#newInstance} factory method to
 * create an instance of this fragment.
 */
public class enabledisablenew extends Fragment implements Clicklistner {
    TextView t;    private DBHandler dbHandler;

    Button b; //FirebaseDatabase f;
  private RecyclerView mRecyclerView;

    private RecyclerAdapter mRecyclerAdapter;
   // private RecyclerView.LayoutManager mLayoutManager;String s;//MainActivity1 n;
    Button btnAddItem,but;
    ArrayList<User> myList;public String user_type;
    EditText etTitle, etDescription;
    String title = "",description = "";
    ImageView crossImage;  public static List<String> list=new ArrayList<String>();
    // RecyclerView recyclerView;
    //RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    @Override
    public void delete(String name) {

    }

    @Override
    public void isenabled(boolean toggle) {

    }

    @Override
    public void createfragment() {

    }


    public interface OnFragmentInteractionListener {

        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public enabledisablenew() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab1Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static enabledisablenew newInstance(String param1, String param2) {
        enabledisablenew fragment = new enabledisablenew();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.viewusermy, container, false);
        user_type = getArguments().getString("user_type");
        myList = new ArrayList<>();
        mRecyclerView = (RecyclerView)root.findViewById(R.id.recyclerView);
        mRecyclerAdapter = new RecyclerAdapter(myList,this, root.getContext(),user_type);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        myList.clear();
        String selection="";
        String[] selectionArgs = {};
        dbHandler = new DBHandler(root.getContext());
        DBHandler.user_functions user_fun=dbHandler.new user_functions(); User mLog = new User();
        List<User> userList = new ArrayList<User>();
      //  userList= user_fun.viewUsers(selection,selectionArgs,user_type);
        if(userList.size()>0)
        {
            for(int i=0;i<userList.size();i++)
            {
                mLog.setName(userList.get(i).getName());
                mLog.setActiontype("enabledisable");
                mLog.setStatus(userList.get(i).getStatus());
                myList.add(mLog);
                mRecyclerAdapter.notifyData(myList);
            }

        }else{

        }
        return root;
    }
    public static void lista(String k){
        list.add(k);
    }


}