package com.example.flightapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

public class missileSprite {


    Context cntxt;
    int missileIdx = 0;
    int width, height, left, top;
    int dx = 0;
    int start=0;
    Bitmap[] imgs = new Bitmap[6];

    public missileSprite(Context cntxtIn, int topIn, int dxIn, int startIn) {
        cntxt = cntxtIn;
        start=startIn;
        left= start;

        imgs[0] = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.m1);
        imgs[1] = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.m2);
        imgs[2] = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.m3);
        imgs[3] = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.m4);
        dx = dxIn;
        top = topIn;
    }
    public void setTop(int topIn){
        top = topIn;
    }
    public boolean drawSprite(Canvas canvas, int topIn) {
        Paint pt = new Paint();
        Log.d("missile", "drawing missile at "+left+" "+top);
        width=imgs[missileIdx].getWidth();
        height=imgs[missileIdx].getHeight();
        canvas.drawBitmap(imgs[missileIdx], left - dx, top, pt);
        left=left-dx;
        if (left >= canvas.getWidth()) {

            return false;
        }
        missileIdx = (missileIdx + 1) % 4;
        return true;
    }
}

