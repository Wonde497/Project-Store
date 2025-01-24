package com.verifone.demo.emv.Supervisor_menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.verifone.demo.emv.DBHandler;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class More_report extends Fragment {
    static String TAG="MORE_REPORT";
    public String user_type;
    String menu_title;
    ImageButton SALE;
    ImageButton VOID;
    ImageButton CASHADVANCE;
    private TextView tv,tv1;
    private DBHandler dbHandler;
    private DBHandler.terminal_functions terminal_fun;
    View morereport_frag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        morereport_frag= inflater.inflate(R.layout.fragment_more_report, container, false);
        tv = (TextView) morereport_frag.findViewById(R.id.toolbar);
        SALE = (ImageButton) morereport_frag.findViewById(R.id.sale);
        VOID = (ImageButton) morereport_frag.findViewById(R.id.void1);
        CASHADVANCE = (ImageButton) morereport_frag.findViewById(R.id.cashadv);

        tv.setText("MORE REPORT");
        MorereportMenuView();

        return morereport_frag;
    }
    public void MorereportMenuView() {

        SALE.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {




                getActivity().onBackPressed();


            }
        });

        VOID.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                getActivity().onBackPressed();


            }
        });

        CASHADVANCE.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                getActivity().onBackPressed();


            }
        });
        dbHandler = new DBHandler(morereport_frag.getContext());
        // Log.d(TAG, "out off initScreen2");
    }

}
