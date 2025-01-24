package com.verifone.demo.emv.Supervisor_menu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.verifone.demo.emv.Cashier_menu_activity;
import com.verifone.demo.emv.DBHandler;
import com.verifone.demo.emv.Supervisor_menu_activity;
import com.verifone.demo.emv.transaction.TransBasic;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class Reprint extends Fragment {
    static String TAG="REPRINT";
    boolean homePressed = false;
    public String user_type;
    String menu_title;
    ImageButton last;
    ImageButton specific;
    FrameLayout reprint_frame;
    private TextView tv,tv1;
    private DBHandler dbHandler;
    private DBHandler.terminal_functions terminal_fun;
    View reprint_frag;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        reprint_frag= inflater.inflate(R.layout.fragment_reprint, container, false);
        sharedPreferences = reprint_frag.getContext().getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        tv = (TextView) reprint_frag.findViewById(R.id.toolbar);
        last = (ImageButton) reprint_frag.findViewById(R.id.lastrep);
        specific = (ImageButton) reprint_frag.findViewById(R.id.specficrep);

        reprint_frame=(FrameLayout)reprint_frag.findViewById(R.id.reprint_frame);
        tv.setText("REPRINT ");

        ReprintMenuView();
        return reprint_frag;
    }

    public void ReprintMenuView() {

        last.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TransBasic transBasic;
                transBasic= TransBasic.getInstance(sharedPreferences);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                DBHandler dbHandler=new DBHandler(reprint_frag.getContext());
               if(dbHandler.gettransactiondata().size() > 0)
               {
               editor.putString("printtype","specificreport");
                editor.putString("trn",String.valueOf(dbHandler.getdatasize()-1));
                editor.commit();
                transBasic.printTest(1);

               /* for(int i=0; i<dbHandler.gettransactiondata().size();i++)
                {
                    editor.putString("printtype","specificreport");
                    editor.putString("trn",String.valueOf(i));
                    editor.commit();
                    transBasic.printTest(1);
                }

                */
               }else
               {
             Toast.makeText(reprint_frag.getContext(),"EMPITY TXN...", Toast.LENGTH_LONG).show();

               }

            }
        });

        specific.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              //  reprint_frame.setVisibility(View.INVISIBLE);
               if(sharedPreferences.getString("cashier", "").equals("cashierprint"))
               {
                   Log.d(TAG, "Cashier is selected: " );
                   editor = sharedPreferences.edit();
                   editor.putString("cashier","");
                   editor.commit();
                   ((Cashier_menu_activity)getActivity()).opentrandata();

               }
               else
               {
                   Log.d(TAG, "Supervisor is selected: " );
                   ((Supervisor_menu_activity)getActivity()).opentrandata();

               }


               // getActivity().onBackPressed();


            }
        });
        dbHandler = new DBHandler(reprint_frag.getContext());
        // Log.d(TAG, "out off initScreen2");
    }



}
