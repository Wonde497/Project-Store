package com.verifone.demo.emv.transaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.verifone.demo.emv.Public_data.ISO8583msg;
import com.verifone.demo.emv.Utilities.ActivityCollector;
import com.verifone.demo.emv.Utilities.Utility;
import com.verifone.demo.emv.basic.BaseActivity;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import cn.verifone.simon.verifone_x9_demo_emv.Global.R;

public class ESignActivity extends BaseActivity{

    private static final String TAG = "ESignActivity";
    TextView tvAmount;
    String Txn_type;
    SharedPreferences sharedPreferences;
    Button btnToPrint;
    private String conditionCode;
    public SurfaceView surfaceView;
    public ImageView imageView;
    public Canvas canvas;//
    public boolean isOnTouch = false;//
    public boolean isCoverTrait = false;//
    public int down_x = 0;
    public int down_y = 0;
    public Paint paint, paintText, mPaint;//
    public Bitmap bitmap;//
    // public Bitmap originBitmap;//
    public int mov_x;//
    public int mov_y;
    public String signaValue;
    final static int BUFFER_SIZE = 10000;
    public byte[] top;
    public int width=0, height=0;
    public int zoomMultiples = 1;// 放大倍数
    SurfaceHolder holder;
    boolean threadFlag = false;

    public Rect textRect;
    public int textW = 0;
    public int textH = 0;
    public int leftOffset = 0;
    public int reSign_x = 0;
    public int reSign_y = 0;
    boolean isfinished=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esign_new);

        ActivityCollector.addActivity(this);
        Log.d(TAG, "in esign...");
        initView();
        Log.d(TAG, "in esign 2...");
        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                Log.d("Surface","toucheddd");

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (!isOnTouch) {
                        down_x = (int) event.getX();
                        down_y = (int) event.getY();
                    }
                }
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    canvas.drawLine(mov_x, mov_y, event.getX(), event.getY(), paint);
                    if (!isOnTouch && (Math.abs(down_x - mov_x) > 100 || Math.abs(down_y - mov_y) > 50)) {
                        isOnTouch = true;
                    }
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mov_x = (int) event.getX();
                    mov_y = (int) event.getY();
                }
                mov_x = (int) event.getX();
                mov_y = (int) event.getY();

                if ((mov_x > (reSign_x - 20) && mov_x < width) && (mov_y > (reSign_y - 20) && mov_y < height)) {
                    resetCanvas();
                    myDrawText(canvas, mPaint);
                }
                return true;
            }
        });
        holder = surfaceView.getHolder();
        //holder.addCallback(surfaceView.getcal);
        String numEnd = "";

        numEnd = "5673487";

        signaValue = numEnd;
        width = 700;
        height = 480;
        Log.d("", "screenWidth" + 360);
        Log.d("", "width" + width);
        if (width > 360) {
            leftOffset = (width - 360) / 2;
        }
        Log.d("", "" + leftOffset);

        init();
        new MyThread().start();
        threadFlag = true;
    }

    @Override
    public void onAttachedToWindow() {
        Log.d(TAG, "in onAttachedToWindow 2...");
        super.onAttachedToWindow();
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        super.onAttachedToWindow();
        initESign();
    }

    //init other view
    private void initView() {
        tvAmount = findViewById(R.id.input_amount_tv);
        btnToPrint = findViewById(R.id.to_print);
        sharedPreferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
         Txn_type = sharedPreferences.getString("txn_type", "");
        if(Txn_type.equals("REVERSAL")){

                Log.d(TAG,"amount:"+ ISO8583msg.r_currency1 + " " + Utility.getReadableAmount(ReversalActivity.user.getField_04()));
                tvAmount.setText(ISO8583msg.r_currency1 + " " + Utility.getReadableAmount(ReversalActivity.user.getField_04()));

        } else if (TransBasic.Txn_type.equals("BALANCE_INQUIRY")) {
            tvAmount.setVisibility(View.GONE);
        } else {
            tvAmount.setText(ISO8583msg.r_currency1+ " "+ Utility.getReadableAmount(TransactionParams.getInstance().getTransactionAmount()));
        }
        btnToPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // canvas.setVisibility(View.GONE);
                 if(isfinished){
                    isfinished = false;
                    saveEsignData();
                  SharedPreferences sharedPreferences = getSharedPreferences("Shared_data", Context.MODE_PRIVATE);
                 TransBasic.getInstance(sharedPreferences).addtodatabase();
                 if(TransBasic.Txn_type.equals("BALANCE_INQUIRY")){
                     TransBasic.getInstance(sharedPreferences).printTest(2);
                 }else {
                     TransBasic.getInstance(sharedPreferences).printTest(1);
                       }
                  finish();
                  }
            }
        });

    }

    private boolean saveEsignData() {
        //Bitmap bitmap = Bitmap.createBitmap(700,480,Bitmap.Config.ARGB_8888);
        Bitmap bitmap=saveCanvas();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

        byte[] signData = baos.toByteArray();
        if (signData != null && signData.length > 0) {
            TransBasic.signdatabase=Utility.bcd2Asc(signData);

            Log.i(TAG, "compress bmp data(length=" + signData.length + ")=" +  Utility.bcd2Asc(signData));
            TransactionParams.getInstance().setConditionCode(conditionCode);
            TransactionParams.getInstance().setEsignData(Utility.bcd2Asc(signData));
            TransactionParams.getInstance().setEsignWidth(String.valueOf(bitmap.getWidth()));
            TransactionParams.getInstance().setEsignHeight(String.valueOf(bitmap.getHeight()));
            TransactionParams.getInstance().setEsignUploadFlag(false);
        }
        return true;
    }

    //init e-sign
    @SuppressLint("ClickableViewAccessibility")
    private void initESign() {

    }

    private String getConditionCode() {
        int i = 0;
        TransactionParams.getInstance().setDate(Utility.getSystemDatetime().substring(0, 8));
        TransactionParams.getInstance().setReferNum(Utility.getSystemDatetime().substring(6));

        String str = TransactionParams.getInstance().getDate() + TransactionParams.getInstance().getReferNum();
        if (str != null && !str.equals("")) {
            byte[] block = Utility.asc2Bcd(str, 16);
            for (i = 0; i < 4; i++) {
                block[i] ^= block[i + 4];
            }
            conditionCode = Utility.bcd2Asc(block, 4);
            return conditionCode;
        } else
            return str;
    }

    //Set menu / home / back button invalid
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    protected void myDrawText(Canvas canvas, Paint mPaint) {
        String signString = "Signature";

        Rect bounds = new Rect();
        mPaint.getTextBounds(signString, 0, signString.length(), bounds);
        canvas.drawText(signString, bounds.width() / 4, bounds.height(), mPaint);

        reSign_x = width - bounds.width();
        reSign_y = height - bounds.height();

        Bitmap resign_bmp = null;//
        if (Locale.getDefault().getLanguage().equals("zh"))
            resign_bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.resign_en);
        else
            resign_bmp = BitmapFactory.decodeResource(getResources(),R.mipmap.resign_en);
        Log.d("Surface text: ", String.valueOf(resign_bmp));
        if (resign_bmp != null) {
            Log.d("Surface text: ", "not nulll");


        }
        canvas.drawBitmap(resign_bmp, 570, 358, paint);
    }
    protected void myDraw() {
        Canvas draw_canvas = holder.lockCanvas();
        if (draw_canvas == null) {
            Log.d("Surface canva", " is  null");

            return;
        }else{
//            Log.d("Surface canva", " is not null");

        }

        //draw_canvas.drawCircle(bitmap.getWidth()/2, bitmap.getHeight()/2, bitmap.getWidth()/5, paint);

        draw_canvas.drawBitmap(bitmap, 0, 0, paint);
        //Log.d("Surface", "drawinggg");

        holder.unlockCanvasAndPost(draw_canvas);
    }
    public void resetCanvas() {
        isOnTouch = false;
        isCoverTrait = false;
        bitmap = Bitmap.createBitmap(width + 30, height, Bitmap.Config.ARGB_8888); // 设置位图的宽高
        bitmap.eraseColor(Color.parseColor("#e5e6ea"));
        canvas.setBitmap(bitmap);
//        canvas.drawText(this.signaValue, (width - textW) / 2, (height - textH) / 2 + textH, paintText);
    }

    public Bitmap saveCanvas() {
        if (isOnTouch) {
            saveBitmapFile(bitmap);

            if (bitmap == null) {
                Log.d("save can", "dToWindow 2...");
                return null;
            }
            Log.d("save can", "dToWindow 3...");
            myDrawText(canvas, mPaint);
            Bitmap newbmp = mycompress(bitmap);
            return newbmp;
        } else {

            Log.d("save can", "dToWindow 4...");
            return null;
        }
    }

    @SuppressLint("WrongThread")
    public void saveBitmapFile(Bitmap bitmap) {
        File file = new File("/mnt/sdcard/simple.bmp");//将要保存图片的路径
        try {
            mPaint.setColor(Color.parseColor("#e5e6ea"));
            myDrawText(canvas, mPaint);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


/*    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }*/

    public Bitmap mycompress(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postScale((float) 384 / bitmap.getWidth(), (float) 200 / bitmap.getHeight());
        // matrix.postRotate(180);
        Bitmap compressBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        return compressBitmap;
    }
    public void init() {
        zoomMultiples = height / 100;
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        // paint.setStyle(Style.FILL);//
        paint.setStrokeWidth(4);//
        // paint.setFakeBoldText(true);
        paint.setLinearText(true);
        // paint.setSubpixelText(true);
        // paint.setFilterBitmap(true);
        paint.setStrokeCap(Paint.Cap.ROUND);//
        paint.setDither(true);//
        paint.setAntiAlias(true);// ?


        paintText = new Paint();
        String familyName = "Times New Roman";
        Typeface font = Typeface.create(familyName, Typeface.NORMAL);
        paintText.setTypeface(font);
        paintText.setTextSize(15 * zoomMultiples);
        paintText.setAntiAlias(true);// 锯齿不显示
        paintText.setColor(getResources().getColor(R.color.white));
        paintText.setStyle(Paint.Style.FILL);// 设置非填充
        // paintText.setStrokeWidth(4* zoomMultiples);// 笔宽5像素

        mPaint = new Paint();
        mPaint.setStrokeWidth(3);
        mPaint.setTextSize(40);
        mPaint.setColor(Color.GRAY);
        mPaint.setTextAlign(Paint.Align.LEFT);

        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888); //
        bitmap.eraseColor(Color.parseColor("#e5e6ea"));
        canvas = new Canvas(bitmap);
        canvas.setBitmap(bitmap);

        textRect = new Rect();
        // 获取特征值所需矩阵
        paintText.getTextBounds(signaValue, 0, signaValue.length(), textRect);
        textW = textRect.width();// 获取特征值矩阵的宽
        textH = textRect.height();// 获取特征值矩阵的高

        textRect = new Rect((width - textW) / 2 + leftOffset,
                (height - textH) / 2, (width - textW) / 2 + textW + leftOffset,
                (height - textH) / 2 + textH);

        // canvas.drawRect(textRect, paint);
        // 画上特征值， 不知为何画文字时会以左下为坐标原点
//        canvas.drawText(this.signaValue, (width - textW) / 2, (height - textH) / 2 + textH, paintText);

//        canvas.setBitmap(bitmap);

        Log.d("Surface", "w:" + bitmap.getWidth());
        Log.d("Surface", "h:" + bitmap.getHeight());
        myDrawText(canvas, mPaint);
    }

    class MyThread extends Thread {
        @Override
        public void run() {
            while (threadFlag) {
                long startTime = System.currentTimeMillis();
                //Log.d("drawing", "");

                myDraw();

                long endTime = System.currentTimeMillis();
                /*
                 * 1000ms /60 = 16.67ms
                 */
                if (endTime - startTime < 30) {
                    try {
                        Thread.sleep(30 - (endTime - startTime));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("lifecycle","onStart invoked");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lifecycle","onResume invoked");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("lifecycle","onPause invoked");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("lifecycle","onStop invoked");
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("lifecycle","onRestart invoked");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        threadFlag = false;
        Log.d("lifecycle","onDestroy invoked");
    }

}
