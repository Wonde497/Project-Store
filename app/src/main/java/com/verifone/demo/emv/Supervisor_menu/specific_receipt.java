package com.verifone.demo.emv.Supervisor_menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.verifone.demo.emv.DBTerminal;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;
public class specific_receipt extends Fragment {

        static String TAG = "SPECIFIC";
        View Ttitel;
        public String user_type;
        String menu_title;
        private EditText spreprint;
        private TextView ttitel;
        private TextView tv,tv1,tv2;
        FrameLayout frame;
        Button set,Cancle;
        private String terminalId;
        private static DBTerminal dbTerminal;
        private static DBTerminal.Terminal_functions1 Terminal_fun;
        View spreprint_frag;
        int row_cnt;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            user_type = getArguments().getString("user_type");
            Log.d(TAG, user_type);

            spreprint_frag= inflater.inflate(R.layout.fragment_specific_receipt, container, false);
            tv = (TextView) spreprint_frag.findViewById(R.id.toolbar);
            tv1 = (TextView) spreprint_frag.findViewById(R.id.tv1);
            tv2 = (TextView) spreprint_frag.findViewById(R.id.tv2);

            if (user_type.equals("supervisor"))
                menu_title = "Cashier";
            else if (user_type.equals("support"))
                menu_title = "Supervisor";
            else if (user_type.equals("administrator"))
                menu_title = "Support";
            else if (user_type.equals("init")) {
                menu_title = "Administrator";
                user_type = "administrator";
            }

            tv.setText("SETTLMENT TIME");
            tv1.setText("ENTER SETTLMENT TIME ");
            tv2.setText("24 HOURS FORMAT ");
            Log.d(TAG, user_type);


                initScreen();


            return spreprint_frag;
        }

        private void initScreen() {

            spreprint = (EditText) spreprint_frag.findViewById(R.id.Tvsettlment);

            set = (Button) spreprint_frag.findViewById(R.id.idBtnset);
            set.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d(TAG, "set button clicked ");

                    String settlmenttime = spreprint.getText().toString();

                    if (settlmenttime.isEmpty()) {

                        Toast.makeText(spreprint_frag.getContext(), "spreprint is not empty.", Toast.LENGTH_SHORT).show();
                        return;

                    } else {

                        if (user_type.equals("supervisor"))
                            menu_title = "Cashier";
                        else if (user_type.equals("support"))
                            menu_title = "Supervisor";
                        else if (user_type.equals("administrator"))
                            menu_title = "Support";
                        else if (user_type.equals("init")) {
                            menu_title = "Administrator";
                            user_type = "administrator";
                        }

                        Log.d(TAG, "menu_title: " + user_type);

                        String TABLE_NAME_SET = "SETT";

                        String ID_COL = "id";

                        String SET_COL = "settlment";

                        dbTerminal.query = "CREATE TABLE " + TABLE_NAME_SET + " ("

                                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT,"

                                + SET_COL + " TEXT)";

                        dbTerminal.TABLE_NAME_SET = "SETT";

                        DBTerminal.Terminal_functions1 Terminal_fun = dbTerminal.new Terminal_functions1();
                        Terminal_fun.settlment(settlmenttime);

                        Toast.makeText(spreprint_frag.getContext(), "Settliment has been setted succefully.", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "finshed settlment time ");
                    }
                    getActivity().onBackPressed();

                }

            });
            dbTerminal = new DBTerminal(spreprint_frag.getContext());
        }
    }