package com.example.flightapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;

import java.net.ConnectException;

public class Background {

    Bitmap cloudMap;
    Context cntxt;
    int pos;
    Paint paint;
    Resources res;

    Bitmap img ;
    int leftOffset, top, left, width, height, initLeft=Integer.MIN_VALUE;
    float rate=0;

    public Background(Context cntxtIn, int leftFactor, int topFactor, float rateIn, Bitmap imgIn, int posIn){
        cntxt = cntxtIn;
        res=cntxt.getResources();
        img = imgIn;
        left = leftFactor * img.getWidth();
        pos=posIn;
        rate=rateIn;
        paint = new Paint();
    }


    public void update() {
        int newLeft= left -10;
        System.out.println( newLeft + img.getWidth()+" "+newLeft+" "+img.getWidth());
        if ( newLeft + img.getWidth() < 0 ) left=img.getWidth() ;
        else left = newLeft;
    }

    public void draw(Canvas canvas) {

//        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvas.drawBitmap(img,left,top,paint);

    }
}
