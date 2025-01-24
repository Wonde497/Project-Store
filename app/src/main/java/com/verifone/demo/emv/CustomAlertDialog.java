package com.verifone.demo.emv;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class CustomAlertDialog {

    public interface DialogClickListener {
        void onPositiveButtonClick();
        void onNegativeButtonClick();
    }

    public static void showDialog(Context context, String message, final DialogClickListener listener) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialog);

        TextView messageTextView = dialog.findViewById(R.id.messageTextView);
        messageTextView.setText(message);

        Button positiveButton = dialog.findViewById(R.id.positiveButton);
        Button negativeButton = dialog.findViewById(R.id.negativeButton);

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPositiveButtonClick();
                dialog.dismiss();
            }
        });

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNegativeButtonClick();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static void dismissDialog(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

}
