package com.example.flightapp;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.util.List;

public class bird {


    Context cntxt;
    int birdIdx = 0;
    int width, height, left, top;
    int dx = 0;
    int start=0;
    Bitmap[] imgs = new Bitmap[6];

    public bird(Context cntxtIn,int topIn, int dxIn, int startIn) {
        cntxt = cntxtIn;
        start=startIn;
        left=cntxtIn.getResources().getDisplayMetrics().widthPixels +start;
        imgs[0] = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.b1);
        imgs[1] = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.b2);
        imgs[2] = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.b3);
        imgs[3] = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.b4);
        imgs[4] = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.b5);
        imgs[5] = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.b6);
        dx = dxIn;

        top = topIn;
//        imgs[2]= Bitmap.createBitmap(img,0,img.getHeight()/2,img.getWidth()/2,img.getHeight()/2);

//        imgs[3]= Bitmap.createBitmap(img,img.getWidth()/2,img.getHeight()/2,img.getWidth()/2,img.getHeight()/2);


    }

    public boolean drawSprite(Canvas canvas, List<missileSprite> missiles) {
        Paint pt = new Paint();
        int tBirdIdx = birdIdx /2;
        pt.setStyle(Paint.Style.STROKE);pt.setColor(Color.RED);
        canvas.drawBitmap(imgs[tBirdIdx], left - dx, top, pt);
        left=left-dx;
        //----- cllision kill =---------
        Rect r1= new Rect(left,top,left+width, top+height);
//        canvas.drawRect(r1,pt);
        width=imgs[tBirdIdx].getWidth();
        height=imgs[tBirdIdx].getHeight();
        for (int i=0;i<missiles.size(); i++) {
            Log.d("missile",missiles.size()+"");
            missileSprite bd = missiles.get(i);
            Bitmap b = bd.imgs[bd.missileIdx];
            Log.d("missilecollision",r1.toString()+" "+bd.left+" "+bd.top+" "+(bd.left+ bd.width)+" "+(bd.top+bd.height));
            if ( r1.intersect(bd.left, bd.top, bd.left+bd.width,bd.top+bd.height)){
                Log.d("missilecollided"," missile sprite collides with Bird ");
                Bitmap eraser = imgs[tBirdIdx].copy(Bitmap.Config.ARGB_8888,true);
                eraser.eraseColor(Color.TRANSPARENT);
                canvas.drawBitmap(eraser, left, top, pt);
                Log.d("remove",missiles.size()+"");
                missiles.remove(i);
                Log.d("kill",missiles.size()+"");
                return false;
            }
        }



        ///
        if (left < 0) {
            left= cntxt.getResources().getDisplayMetrics().widthPixels + start;
            return true;
        }

        birdIdx = (birdIdx + 1) % 10;
//        System.out.println(" drawn the plane");
        return true;
    }
}

