package com.verifone.demo.emv;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;

import com.verifone.demo.emv.transaction.TransPrinter;

class Customview extends View {

    /*
     * Simple constructor to use when creating a view from code.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     */
    public Customview(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        Point screenSize = new Point();
        PrinterExActivity.activity.getWindowManager().getDefaultDisplay().getSize(screenSize);

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

