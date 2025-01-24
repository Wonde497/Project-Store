package com.verifone.demo.emv.widget;
/*
 *  author: Derrick
 *  Time: 2019/8/23 16:10
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class FontsSampleView extends View {

    private Paint paint = new Paint();
    private Bitmap bitmap;
    public static final int SAMPLE_WIDTH = 192;

    public FontsSampleView(Context context) {
        super(context);
    }

    public FontsSampleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FontsSampleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FontsSampleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


    }
}
