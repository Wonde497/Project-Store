package com.verifone.demo.emv.userfunctions;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.verifone.demo.emv.DBHandler;
import com.verifone.demo.emv.data_handlers.User;

import java.util.ArrayList;
import java.util.List;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;
public class view_users extends Fragment {

    private TableLayout mTableLayout;
    ProgressDialog mProgressBar;
    private DBHandler dbHandler;
    private static final String TAG = "VIEW";
    View view_frag;
    public String SuppName_col;
    public String SuppStat_col;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view_frag=inflater.inflate(R.layout.fragment_view_users, container, false);

        Log.d("VIEW", "view users Amex here");
        dbHandler = new DBHandler(view_frag.getContext());
        mProgressBar = new ProgressDialog(view_frag.getContext());
        mTableLayout = (TableLayout) view_frag.findViewById(R.id.tableuser);
        mTableLayout.setStretchAllColumns(true);

        startLoadData();

        return view_frag;
    }

    public void startLoadData() {
        mProgressBar.setCancelable(false);
        mProgressBar.setMessage("Fetching User Information..");
        mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressBar.show();

        new LoadDataTask().execute(0);

    }

    public void loadData() {

        int leftRowMargin = 0;
        int topRowMargin = 0;
        int rightRowMargin = 0;
        int bottomRowMargin = 0;
        int textSize = 0, smallTextSize = 0, mediumTextSize = 0;
        textSize = 25;//(int) getResources().getDimension(R.dimen.font_size_verysmall);
        smallTextSize = 5;//(int) getResources().getDimension(R.dimen.font_size_small);
        mediumTextSize = 7;//(int)getResources().getDimension(R.dimen.font_size_medium);

        String selection = "";
        //String[] selectionArgs = {user_name,user_type,user_status,user_pwd};
        String[] selectionArgs = {};

        Log.d("DBRES", "AMEX here 1");

        List<User> userList = new ArrayList<User>();
        DBHandler.user_functions user_fun=dbHandler.new user_functions();

        Log.d("DBRES", "here 3");
        userList= user_fun.viewUsers(selection,selectionArgs);
        int rows = userList.size();
        Log.d("DBRES", "here2" + rows);
        // user u_list;

        TextView textSpacer = null;
        mTableLayout.removeAllViews();
        // -1 means heading row
        Log.d("DBRES", "here");
        for(int i = 0; i < rows; i ++) {
            User row = null;
            if (i > -1) {
                row = userList.get(i);
                Log.d("DBRES", "login name: " + row.getName());
                Log.d("DBRES", "login utype: " + row.getStatus());
                Log.d("DBRES", "login utype: " + row.getTpe());

            } else {
                textSpacer = new TextView(view_frag.getContext());
                textSpacer.setText("");
            }
            //TV
            SharedPreferences S = view_frag.getContext().getSharedPreferences("Shared_data", Context.MODE_PRIVATE);

   if (S.getString("viewtype", "none").equals("viewsupervisor") && row.getTpe().equals("supervisor"))//
            {
                Log.d("AMEX", "viewsupervisor Is Selected: ");
                final TextView tv = new TextView(view_frag.getContext());
                tv.setLayoutParams(new
                        TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv.setGravity(Gravity.LEFT);
                tv.setPadding(5, 15, 0, 15);
                if (i == -1) {
                    tv.setText("Inv.#");
                    tv.setBackgroundColor(Color.parseColor("#f0f0f0"));
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                } else {
                    tv.setBackgroundColor(Color.parseColor("#f8f8f8"));
                    tv.setTextColor(Color.parseColor("#000000"));
                    tv.setText(row.getName());
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                }
                //TV2
                final TextView tv2 = new TextView(view_frag.getContext());
                if (i == -1) {
                    tv2.setLayoutParams(new
                            TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                } else {
                    tv2.setLayoutParams(new
                            TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.MATCH_PARENT));
                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                }
                tv2.setGravity(Gravity.LEFT);
                tv2.setPadding(25, 15, 0, 15);
                if (i == -1) {
                    tv2.setText("Date");
                    tv2.setBackgroundColor(Color.parseColor("#f7f7f7"));
                } else {
                    tv2.setBackgroundColor(Color.parseColor("#ffffff"));
                    tv2.setTextColor(Color.parseColor("#000000"));
                    tv2.setText(row.getStatus());
                }
                //TV3
                final TextView tv3 = new TextView(view_frag.getContext());
                tv3.setLayoutParams(new
                        TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv3.setGravity(Gravity.LEFT);
                tv3.setPadding(50, 15, 0, 15);
                if (i == -1) {
                    tv3.setText("Inv.#");
                    tv3.setBackgroundColor(Color.parseColor("#f0f0f0"));
                    tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                } else {
                    tv3.setBackgroundColor(Color.parseColor("#f8f8f8"));
                    tv3.setTextColor(Color.parseColor("#000000"));
                    tv3.setText(row.getTpe());
                    tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                }
                // add table row
                final TableRow tr = new TableRow(view_frag.getContext());
                tr.setId(i + 1);
                TableLayout.LayoutParams trParams = new
                        TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT);
                trParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin,
                        bottomRowMargin);
                tr.setPadding(0, 0, 0, 0);
                tr.setLayoutParams(trParams);
                tr.addView(tv);
                tr.addView(tv3);
                tr.addView(tv2);


                if (i > -1) {
                    tr.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            TableRow tr = (TableRow) v;
                        }
                    });
                }
                mTableLayout.addView(tr, trParams);
                if (i > -1) {
                    // add separator row
                    final TableRow trSep = new TableRow(view_frag.getContext());
                    TableLayout.LayoutParams trParamsSep = new
                            TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT);
                    trParamsSep.setMargins(leftRowMargin, topRowMargin,
                            rightRowMargin, bottomRowMargin);
                    trSep.setLayoutParams(trParamsSep);
                    TextView tvSep = new TextView(view_frag.getContext());
                    TableRow.LayoutParams tvSepLay = new
                            TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT);
                    tvSepLay.span = 4;
                    tvSep.setLayoutParams(tvSepLay);
                    tvSep.setBackgroundColor(Color.parseColor("#d9d9d9"));
                    tvSep.setHeight(1);
                    trSep.addView(tvSep);
                    mTableLayout.addView(trSep, trParamsSep);
                }
            }
      else if (S.getString("viewtype", "none").equals("viewcashier") && row.getTpe().equals("cashier"))//
            {
                Log.d("AMEX", "viewcashier Is Selected: ");
                    final TextView tv = new TextView(view_frag.getContext());
                    tv.setLayoutParams(new
                            TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    tv.setGravity(Gravity.LEFT);
                    tv.setPadding(5, 15, 0, 15);
                    if (i == -1) {
                        tv.setText("Inv.#");
                        tv.setBackgroundColor(Color.parseColor("#f0f0f0"));
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                    } else {
                        tv.setBackgroundColor(Color.parseColor("#f8f8f8"));
                        tv.setTextColor(Color.parseColor("#000000"));
                        tv.setText(row.getName());
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                    }
                    //TV2
                    final TextView tv2 = new TextView(view_frag.getContext());
                    if (i == -1) {
                        tv2.setLayoutParams(new
                                TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                        tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                    } else {
                        tv2.setLayoutParams(new
                                TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.MATCH_PARENT));
                        tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                    }
                    tv2.setGravity(Gravity.LEFT);
                    tv2.setPadding(25, 15, 0, 15);
                    if (i == -1) {
                        tv2.setText("Date");
                        tv2.setBackgroundColor(Color.parseColor("#f7f7f7"));
                    } else {
                        tv2.setBackgroundColor(Color.parseColor("#ffffff"));
                        tv2.setTextColor(Color.parseColor("#000000"));
                        tv2.setText(row.getStatus());
                    }
                    //TV3
                    final TextView tv3 = new TextView(view_frag.getContext());
                    tv3.setLayoutParams(new
                            TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    tv3.setGravity(Gravity.LEFT);
                    tv3.setPadding(50, 15, 0, 15);
                    if (i == -1) {
                        tv3.setText("Inv.#");
                        tv3.setBackgroundColor(Color.parseColor("#f0f0f0"));
                        tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                    } else {
                        tv3.setBackgroundColor(Color.parseColor("#f8f8f8"));
                        tv3.setTextColor(Color.parseColor("#000000"));
                        tv3.setText(row.getTpe());
                        tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                    }
                    // add table row
                    final TableRow tr = new TableRow(view_frag.getContext());
                    tr.setId(i + 1);
                    TableLayout.LayoutParams trParams = new
                            TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT);
                    trParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin,
                            bottomRowMargin);
                    tr.setPadding(0, 0, 0, 0);
                    tr.setLayoutParams(trParams);
                    tr.addView(tv);
                    tr.addView(tv3);
                    tr.addView(tv2);


                    if (i > -1) {
                        tr.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                TableRow tr = (TableRow) v;
                            }
                        });
                    }
                    mTableLayout.addView(tr, trParams);
                    if (i > -1) {
                        // add separator row
                        final TableRow trSep = new TableRow(view_frag.getContext());
                        TableLayout.LayoutParams trParamsSep = new
                                TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                                TableLayout.LayoutParams.WRAP_CONTENT);
                        trParamsSep.setMargins(leftRowMargin, topRowMargin,
                                rightRowMargin, bottomRowMargin);
                        trSep.setLayoutParams(trParamsSep);
                        TextView tvSep = new TextView(view_frag.getContext());
                        TableRow.LayoutParams tvSepLay = new
                                TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT);
                        tvSepLay.span = 4;
                        tvSep.setLayoutParams(tvSepLay);
                        tvSep.setBackgroundColor(Color.parseColor("#d9d9d9"));
                        tvSep.setHeight(1);
                        trSep.addView(tvSep);
                        mTableLayout.addView(trSep, trParamsSep);
                    }
                }
    else if (S.getString("viewtype", "none").equals("viewsupport") && row.getTpe().equals("support"))//
            {
                Log.d("AMEX", "viewsupport Is Selected: ");
                final TextView tv = new TextView(view_frag.getContext());
                tv.setLayoutParams(new
                        TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv.setGravity(Gravity.LEFT);
                tv.setPadding(5, 15, 0, 15);
                if (i == -1)
                {
                    tv.setText("Inv.#");
                    tv.setBackgroundColor(Color.parseColor("#f0f0f0"));
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                } else {
                    tv.setBackgroundColor(Color.parseColor("#f8f8f8"));
                    tv.setTextColor(Color.parseColor("#000000"));
                    tv.setText(row.getName());
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                }
                //TV2
                final TextView tv2 = new TextView(view_frag.getContext());
                if (i == -1) {
                    tv2.setLayoutParams(new
                            TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                } else {
                    tv2.setLayoutParams(new
                            TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.MATCH_PARENT));
                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                }
                tv2.setGravity(Gravity.LEFT);
                tv2.setPadding(25, 15, 0, 15);
                if (i == -1) {
                    tv2.setText("Date");
                    tv2.setBackgroundColor(Color.parseColor("#f7f7f7"));
                } else {
                    tv2.setBackgroundColor(Color.parseColor("#ffffff"));
                    tv2.setTextColor(Color.parseColor("#000000"));
                    tv2.setText(row.getStatus());
                }
                //TV3
                final TextView tv3 = new TextView(view_frag.getContext());
                tv3.setLayoutParams(new
                        TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv3.setGravity(Gravity.LEFT);
                tv3.setPadding(50, 15, 0, 15);
                if (i == -1) {
                    tv3.setText("Inv.#");
                    tv3.setBackgroundColor(Color.parseColor("#f0f0f0"));
                    tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                } else {
                    tv3.setBackgroundColor(Color.parseColor("#f8f8f8"));
                    tv3.setTextColor(Color.parseColor("#000000"));
                    tv3.setText(row.getTpe());
                    tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                }
                // add table row
                final TableRow tr = new TableRow(view_frag.getContext());
                tr.setId(i + 1);
                TableLayout.LayoutParams trParams = new
                        TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT);
                trParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin,
                        bottomRowMargin);
                tr.setPadding(0, 0, 0, 0);
                tr.setLayoutParams(trParams);
                tr.addView(tv);
                tr.addView(tv3);
                tr.addView(tv2);


                if (i > -1) {
                    tr.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            TableRow tr = (TableRow) v;
                        }
                    });
                }
                mTableLayout.addView(tr, trParams);
                if (i > -1) {
                    // add separator row
                    final TableRow trSep = new TableRow(view_frag.getContext());
                    TableLayout.LayoutParams trParamsSep = new
                            TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT);
                    trParamsSep.setMargins(leftRowMargin, topRowMargin,
                            rightRowMargin, bottomRowMargin);
                    trSep.setLayoutParams(trParamsSep);
                    TextView tvSep = new TextView(view_frag.getContext());
                    TableRow.LayoutParams tvSepLay = new
                            TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT);
                    tvSepLay.span = 4;
                    tvSep.setLayoutParams(tvSepLay);
                    tvSep.setBackgroundColor(Color.parseColor("#d9d9d9"));
                    tvSep.setHeight(1);
                    trSep.addView(tvSep);
                    mTableLayout.addView(trSep, trParamsSep);
                }
            }


            }

        }



    class LoadDataTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... params) {
            try {
                Thread.sleep(1000);
                Log.d("VIEW", "view users Amex here loadDataTask try");
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.d("VIEW", "view users Amex here after loadDataTask error");
            }
            return "Task Completed.";

        }
        @Override
        protected void onPostExecute(String result) {
            mProgressBar.hide();
            Log.d("VIEW", " loadData bellow here");
            loadData();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }
}