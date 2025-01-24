package com.verifone.demo.emv;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.verifone.demo.emv.basic.BaseActivity;
import com.verifone.demo.emv.transaction.TransBasic;
import com.verifone.demo.emv.transaction.canvas_printer.PrinterDefine;
import com.verifone.demo.emv.widget.FontsSampleView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class PreviewFontsActivity extends BaseActivity {

    private static final String TAG = "PreviewFontsActivity";
    private LinearLayout content;
    private TextView btnPrint;
    private Paint paint;
    private Canvas canvas;

    private TransBasic transBasic;


    private List<String> fontsList;
    private List<RadioGroup> radioGroupList;
    private List<RadioButton> normalRadioButtonList;
    private List<RadioButton> boldRadioButtonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_fonts);
        SharedPreferences sharedPreferences=getSharedPreferences("Shared_data", Context.MODE_PRIVATE);

        transBasic = TransBasic.getInstance(sharedPreferences);
        btnPrint = findViewById(R.id.btnPrint);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == btnPrint){
                    doShowPrint();
                }
            }
        };
        btnPrint.setOnClickListener(clickListener);

        content = findViewById(R.id.content);
        radioGroupList = new ArrayList<>();
        normalRadioButtonList = new ArrayList<>();
        boldRadioButtonList = new ArrayList<>();

        getFilesAllName("/system/fonts");

        for (final String fonts : fontsList){
            Bitmap bitmap = creatSampleFontsCanvas(fonts.replace("/system/fonts/", ""),fonts);
            Log.d(TAG, fonts);
            // initialize the linearlayout that contain Preview of fonts and Radio button
            LinearLayout layout = new LinearLayout(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layout.setLayoutParams(layoutParams);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setGravity(Gravity.CENTER);

            // initialize ImageView
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(new LinearLayout.LayoutParams(450, 200));
            iv.setImageBitmap(bitmap);
            layout.addView(iv);

            // initialize Radio button
            final LinearLayout radioGroup = new LinearLayout(this);
            LinearLayout.LayoutParams radioGroupParams = new LinearLayout.LayoutParams(210, 60);
            radioGroup.setLayoutParams(radioGroupParams);
            radioGroup.setOrientation(LinearLayout.HORIZONTAL);
//            radioGroup.setBackgroundColor(Color.BLUE);

            final RadioButton buttonNormal = new RadioButton(this);
            LinearLayout.LayoutParams paramsNormal = new LinearLayout.LayoutParams(60, 60);
            paramsNormal.setMargins(40, 0, 0, 0);
            buttonNormal.setLayoutParams(paramsNormal);
//            buttonNormal.setBackgroundColor(Color.RED);
//            buttonNormal.setText("Normal");
//            buttonNormal.setTextSize(12);

            final RadioButton buttonBold = new RadioButton(this);
            LinearLayout.LayoutParams paramsBold = new LinearLayout.LayoutParams(60, 60);
            paramsBold.setMargins(50, 0, 0, 0);
            buttonBold.setLayoutParams(paramsBold);
//            buttonBold.setBackgroundColor(Color.YELLOW);
//            buttonBold.setText("Bold");
//            buttonBold.setTextSize(12);

            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v == buttonNormal){
                        buttonNormal.setChecked(true);
//                        buttonBold.setChecked(false);
                        for (int i = 0; i< normalRadioButtonList.size(); i++){
                            if ( v != normalRadioButtonList.get(i)){
                                normalRadioButtonList.get(i).setChecked(false);
                            }else {
                                PrinterDefine.Font_default = fontsList.get(i);
                            }
                        }
                    }else if (v == buttonBold){
//                        buttonNormal.setChecked(false);
                        buttonBold.setChecked(true);
                        for (int i = 0; i< boldRadioButtonList.size(); i++){
                            if ( v != boldRadioButtonList.get(i)){
                                boldRadioButtonList.get(i).setChecked(false);
                            }else {
                                PrinterDefine.Font_Bold = fontsList.get(i);
                            }
                        }
                    }
//                    for (RadioGroup radioGroupItem: radioGroupList){
//                        if (radioGroup != radioGroupItem){
//                            radioGroupItem.clearCheck();
//                        }
//                    }


                }
            };

            buttonNormal.setOnClickListener(onClickListener);
            buttonBold.setOnClickListener(onClickListener);
            normalRadioButtonList.add(buttonNormal);
            boldRadioButtonList.add(buttonBold);

            radioGroup.addView(buttonNormal);
            radioGroup.addView(buttonBold);
//            radioGroupList.add(radioGroup);

            layout.addView(radioGroup);

            content.addView(layout);
        }
    }

    private int transBase_print_index = 0;
    void doShowPrint(){
        transBasic.printTest(transBase_print_index );
        ++ transBase_print_index;
        if( transBase_print_index > 2 ){
            transBase_print_index = 0;
        }
        finish();
    }

    //Generate fonts preview bitmap
    private Bitmap creatSampleFontsCanvas(String fontsTitle, String fontsPath) {
        paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setTextSize(17);
//        paint.setColor(Color.BLACK);
        paint.setColor(Color.BLACK);
        Bitmap bitmap = Bitmap.createBitmap(250, 100, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.WHITE);
        canvas = new Canvas(bitmap);

        paint.setTypeface(Typeface.createFromFile(fontsPath));
        paint.setFakeBoldText(false);
        canvas.drawText(fontsTitle, 10, 20, paint);
        canvas.drawText("merchant name", 10, 50, paint);
        canvas.drawText("AMOUNT", 10, 80, paint);
        return bitmap;
    }

    // To traverse all fonts file in /system/fonts/
    private void getFilesAllName(String path) {
        File file=new File(path);
        File[] files=file.listFiles();
        if (files == null){
            Log.e("error","空目录");
            return;
        }
        fontsList = new ArrayList<>();
        for(int i =0;i<files.length;i++){
            fontsList.add(files[i].getAbsolutePath());
        }
    }

}
