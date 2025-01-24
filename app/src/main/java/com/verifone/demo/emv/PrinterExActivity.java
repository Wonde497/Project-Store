/*
package com.verifone.demo.emv;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.verifone.demo.emv.Utilities.ActivityCollector;
import com.verifone.demo.emv.transaction.TransPrinter;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class PrinterExActivity extends AppCompatActivity {
public static Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=this;
        setContentView(R.layout.activity_printer_ex);
     //   setContentView( new CustomView(this));
        ActivityCollector.addActivity(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CustomView customView=new CustomView(this);
        LinearLayout linear=(LinearLayout) findViewById(R.id.linear);
        linear.addView(customView);
    }

   class CustomView extends View{

        /*
         * Simple constructor to use when creating a view from code.
         *
         * @param context The Context the view is running in, through which it can
         *                access the current theme, resources, etc.
         */
      /*

       public CustomView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas){
            super.onDraw(canvas);

            Point screenSize = new Point();
            getWindowManager().getDefaultDisplay().getSize(screenSize);

            Bitmap bitmap = TransPrinter.printerEx.getBitmap(false);
            int height = TransPrinter.printerEx.getHeight(false);

            int margin = 72;
            int fixedTop = margin;
            float zoom = 1.0f*(screenSize.y - margin - margin)/height;
            float zoomMax = (screenSize.x  - margin - margin )/384.0f;
            if( zoom > zoomMax ){
                zoom = zoomMax;

            }

            int fixWidth = (int)(384*zoom);
            int fixedHeight = (int)(height*zoom);

            fixedTop = screenSize.y - fixedHeight;
            fixedTop /= 2;

            margin = screenSize.x - fixWidth;
            margin /= 2;

            Paint paint = new Paint();
            paint.setStrokeWidth(1);
            paint.setTextSize(50);
            paint.setColor(Color.GRAY);
            // Set the underline of text
            paint.setUnderlineText(true);

            canvas.drawRect(margin-1,fixedTop-1,fixWidth+margin+1,fixedHeight+fixedTop+1, paint );

            canvas.drawBitmap(bitmap, new Rect(0,0,384,height),
                    new Rect(margin,fixedTop,fixWidth+margin,fixedHeight+fixedTop), null);

        }

    }

}*/


package com.verifone.demo.emv;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.verifone.demo.emv.Supervisor_menu.Supervisor_manage_users;
import com.verifone.demo.emv.Utilities.ActivityCollector;
import com.verifone.demo.emv.transaction.TransBasic;
import com.verifone.demo.emv.transaction.TransPrinter;
import com.verifone.demo.emv.transaction.canvas_printer.PrinterCanvas;
import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class PrinterExActivity extends AppCompatActivity {
    static String TAG = "PrinterExActivity";
    private TextView tv, toolbar;
    private Button Enter, skip;
    public Context context;
    SharedPreferences S;

    // public PrinterExActivity(Context context){
    //Context= context;
    //}
    public static Activity activity,inputpin;
    String Logon_name;
    public static boolean HIDE_ENTER_BUTTON=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 //...........................................................................................................................added ameeeeee
        //SharedPreferences S=context.getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
        //setContentView(new CustomView(this));
        activity=this;
       /* if(Supervisor_menu_activity.utype1.equals("supervisor"))
        {
            Logon_name = Supervisor_menu_activity.Logon_name1;
        }else
        {
            Logon_name = Cashier_menu_activity.Logon_name1;
        }

        */
   S = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);

    Log.e(TAG, "printtype   :" + S.getString("printtype", "none"));


    if (S.getString("printtype", "none").equals("yes") || S.getString("printtype", "none").equals("specificreport"))
        {
            Log.d(TAG, "Amee Reprint is Allowed hereeeeeeeeeee ");

            Log.d(TAG, "Amee Reprint is Allowed hereeeeeeeeeee ");
            Log.e(TAG, "Reciept :" + S.getString("printtype", "none"));

            setContentView(R.layout.activity_printer_ex);
            CustomView customView=new CustomView(this);
            LinearLayout linear=(LinearLayout) findViewById(R.id.linear);
            linear.addView(customView);
            ActivityCollector.addActivity(this);

        toolbar = (TextView) findViewById(R.id.toolbar);
        tv = (TextView) findViewById(R.id.press);
        Enter = (Button) findViewById(R.id.enter);
        skip = (Button) findViewById(R.id.skip);

        Log.d(TAG, "Amexxxxx hereeeeeeeeeee ");
            Log.d(TAG, "Txn_Menu_Type   "+TransBasic.Txn_Menu_Type);
            Log.d(TAG, "Txn_Type   "+TransBasic.Txn_type);

            toolbar.setText("PRINT OUT");
         tv.setText("PRESS ENTER TO PRINT CUSTOMER COPY");

            if (S.getString("printtype", "none").equals("settlement")) {
                Enter.setVisibility(View.GONE);
                tv.setText("Print Settlement Receipt");

                Log.e(TAG, "printtype   is :" + "Settlement");
            }

            if(TransBasic.Txn_type.equals("BALANCE_INQUIRY")){
            Enter.setVisibility(View.GONE);
        }  else{
            Enter.setVisibility(View.VISIBLE);
        }

    Enter.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            Enter.setVisibility(View.GONE);
            SharedPreferences.Editor editor = S.edit();
            DBHandler dbHandler=new DBHandler(v.getContext());
            TransBasic transBasic;
            transBasic = TransBasic.getInstance(S);

            Log.d(TAG, "print type "+S.getString("printtype", "none"));
            if(S.getString("printtype", "none").equals("specificreport") || TransBasic.Txn_Menu_Type.equals("Manualcard"))
            {
                editor.putString("trn",S.getString("trn", "0"));
                editor.putString("printtype", " ");
                editor.putString("print2", "2");
                editor.putString("Txn_Menu_Type", "");
                editor.putString("Txn_type", "");
                editor.commit();
                transBasic.printTest(2);
                TransBasic.tvr="";

                //finish();
                //startActivity(new Intent(PrinterExActivity.this, MenuActivity.class));
                finish();

            }
            else{
                editor.putString("printtype", " ");
                editor.putString("Txn_Menu_Type", "");
                editor.putString("Txn_type", "");
                editor.putString("print2", "2");
                editor.putString("isphoneauth", "");
                editor.putString("trn", String.valueOf(dbHandler.getdatasize() - 1));
                editor.commit();
                transBasic.printTest(2);
                TransBasic.tvr="";

                // startActivity(new Intent(PrinterExActivity.this, MenuActivity.class));
                finish();
            }


        }
    });


            //hideEnterButton();

            skip.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    SharedPreferences.Editor editor = S.edit();
                    if (S.getString("printtype", "none").equals("specificreport") || TransBasic.Txn_Menu_Type.equals("Manualcard"))
                    {
                        editor.putString("printtype", " ");
                        editor.putString("Txn_Menu_Type", "");
                        editor.putString("Txn_type", "");
                        editor.putString("isphoneauth", "");
                        editor.commit();
                        TransBasic.tvr="";
                        finish();

                    } else {
                        editor.putString("printtype", " ");
                        editor.putString("Txn_Menu_Type", "");
                        editor.putString("Txn_type", "");
                        editor.putString("isphoneauth", "");
                        editor.commit();
                        TransBasic.tvr="";

                        //startActivity(new Intent(PrinterExActivity.this, MenuActivity.class));
                        finish();


                    }

                }
            });
        }

    else
    {
        setContentView(new CustomView(this));
        ActivityCollector.addActivity(this);

        if(S.getString("printtype", "").equals("detailreport_2") || S.getString("printtype", "").equals("detailreport") ||
        S.getString("printtype", "").equals("detailreport_1"))
        {
            finish();
        }

    }
    if(inputpin!=null)
    {
    inputpin.finish();
    }
//........................................................................................................
       // setContentView(new CustomView(this));
        //ActivityCollector.addActivity(this);
}

    private void hideEnterButton() {
        Enter.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed()
    {
        if (!(S.getString("printtype", "none").equals("detailreport") || S.getString("printtype", "none").equals("detailreport_1")
                ||S.getString("printtype", "none").equals("detailreport_2") || S.getString("printtype", "none").equals("endoffday")
                || S.getString("printtype", "none").equals("summryreport") || S.getString("printtype", "none").equals("settlement")||S.getString("printtype", "none").equals("terminalsetup")
                || S.getString("printtype", "none").equals("terminalinfo")|| S.getString("printtype", "none").equals("specificreport")
                || TransBasic.Txn_Menu_Type.equals("Manualcard")|| S.getString("printtype", "none").equals("isoffline")))
            {
            SharedPreferences.Editor editor = S.edit();
            editor.putString("printtype", "no");
            editor.putString("Txn_Menu_Type", "");
            editor.putString("Txn_type", "");
            editor.putString("txn_type", "");
            editor.putString("printtype", " ");
            editor.putString("isphoneauth", "");
            TransBasic.tvr="";
            editor.commit();

            //startActivity(new Intent(PrinterExActivity.this, MenuActivity.class));
            finish();

        } else {
            SharedPreferences.Editor editor = S.edit();
            editor.putString("Txn_Menu_Type", "");
            editor.putString("Txn_type", "");
            editor.putString("printtype", " ");
            editor.putString("isphoneauth", "");
            TransBasic.tvr="";
            editor.commit();
            finish();

        }



        /*SharedPreferences.Editor editor = S.edit();
        editor.putString("printtype", "no");
        editor.putString("Txn_Menu_Type", "");
        editor.putString("txn_type", "");
        editor.commit();
        startActivity(new Intent(PrinterExActivity.this, MenuActivity.class));
        finish();

         */
    }
     class CustomView extends View {

         public CustomView(Context context) {
             super(context);
         }

         @Override
         protected void onDraw(Canvas canvas) {
             super.onDraw(canvas);
             Log.d(TAG, "Amee No reprint is selected hereeeeeeeeeee ");
             //tv.setText("CUSTOMER COPY PRESS ENTER");
             Point screenSize = new Point();
             getWindowManager().getDefaultDisplay().getSize(screenSize);

             Bitmap bitmap = TransPrinter.printerEx.getBitmap(false);
             int height = TransPrinter.printerEx.getHeight(false);

             int margin = 72;
             int fixedTop = margin;
             float zoom = 1.0f * (screenSize.y - margin - margin) / height;
             float zoomMax = (screenSize.x - margin - margin) / 384.0f;
             if (zoom > zoomMax) {
                 zoom = zoomMax;

             }

             int fixWidth = (int) (384 * zoom);
             int fixedHeight = (int) (height * zoom);

             fixedTop = screenSize.y - fixedHeight;
             fixedTop /= 2;

             margin = screenSize.x - fixWidth;
             margin /= 2;

             Paint paint = new Paint();
             paint.setStrokeWidth(1);
             paint.setTextSize(50);

             paint.setColor(Color.GRAY);
             // Set the underline of text
             paint.setUnderlineText(true);

             canvas.drawRect(margin - 1, fixedTop - 1, fixWidth + margin + 1, fixedHeight + fixedTop + 1, paint);

             canvas.drawBitmap(bitmap, new Rect(0, 0, 384, height),
                     new Rect(margin, fixedTop, fixWidth + margin, fixedHeight + fixedTop), null);

             Log.d(TAG, "amee finshed ");
         }
         @Override
         protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
             super.onMeasure(widthMeasureSpec, heightMeasureSpec);
             int height = 1800; // should be calculated based on the content
             int width = 700; // should be calculated based on the content
             setMeasuredDimension(width, height);
         }
     }
}


