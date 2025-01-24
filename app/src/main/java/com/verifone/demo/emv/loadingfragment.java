package com.verifone.demo.emv;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link loadingfragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class loadingfragment extends Fragment {
    public String user_type;
    private TableLayout mTableLayout;
    ProgressDialog mProgressBar;
    private DBHandler dbHandler;
    View view_frag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view_frag=inflater.inflate(R.layout.fragment_view_users, container, false);

        mProgressBar = new ProgressDialog(view_frag.getContext());

        return view_frag;
    }

    public void startLoadData() {
        mProgressBar.setCancelable(false);
        mProgressBar.setMessage("Fetching Invoices..");
        mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressBar.show();

    }


}