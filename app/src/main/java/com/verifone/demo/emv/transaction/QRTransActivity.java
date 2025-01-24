package com.verifone.demo.emv.transaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.verifone.demo.emv.Utilities.ServiceHelper;
import com.verifone.demo.emv.Utilities.ToastUtil;
import com.vfi.smartpos.deviceservice.aidl.IScanner;
import com.vfi.smartpos.deviceservice.aidl.ScannerListener;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class QRTransActivity extends AppCompatActivity {

    IScanner iScanner;
    QRTransActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrtrans);

        context = QRTransActivity.this;
        iScanner = ServiceHelper.getInstance().getScanner();
        Bundle bundle = new Bundle();
        bundle.putString("topTitleString", "Scanning");
        bundle.putString("upPromptString", "Please show QR code");
        bundle.putString("downPromptString", "Please show Bar code");
        bundle.putBoolean("showScannerBorder", AppParams.getInstance().isShowScanBorder());

        try {
            iScanner.startScan(bundle, 60, new ScannerListener.Stub() {
                @Override
                public void onSuccess(String barcode) throws RemoteException {

                    ToastUtil.toastOnUiThread(QRTransActivity.this, barcode);
                    TransactionParams.getInstance().setQRData(barcode);
                    SharedPreferences sharedPreferences=getSharedPreferences("Shared_data", Context.MODE_PRIVATE);

                    TransBasic.getInstance(sharedPreferences).printTest(1);
                    finish();
                }

                @Override
                public void onError(int error, String message) throws RemoteException {

                }

                @Override
                public void onTimeout() throws RemoteException {
                    ToastUtil.toastOnUiThread(QRTransActivity.this, "Scanner is timeout");
                    finish();
                }

                @Override
                public void onCancel() throws RemoteException {
                    ToastUtil.toastOnUiThread(QRTransActivity.this, "Scanning canceled");
                    finish();
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
