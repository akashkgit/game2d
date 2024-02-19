package com.example.flightapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.util.Log;


public class flightSprite {


    Context cntxt;
    int flightIdx = 0;
    int width, height, left=550, top, dyOffset = 0, dy = 10;
    Bitmap[] imgs = new Bitmap[4];

    public void setOffset(int i) {
        dyOffset = i;
    }

    public flightSprite(int imgIn, Context cntxtIn) {
        cntxt = cntxtIn;
        Bitmap img = BitmapFactory.decodeResource(cntxtIn.getResources(), imgIn);


        imgs[0] = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.d1);
        imgs[1] = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.d2);
        imgs[2] = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.d3);
        imgs[3] = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.d4);

//        imgs[2]= Bitmap.createBitmap(img,0,img.getHeight()/2,img.getWidth()/2,img.getHeight()/2);

//        imgs[3]= Bitmap.createBitmap(img,img.getWidth()/2,img.getHeight()/2,img.getWidth()/2,img.getHeight()/2);


    }
    public int getTop(){
        return top;
    }
    public gameState drawSprite(Canvas canvas, bird[] birdSprite) {
        Paint pt = new Paint();

//        PorterDuffXfermode mode = new PorterDuffXfermode(PorterDuff.Mode.DST);
//        pt.setXfermode(mode);
//        canvas.drawRect(10,10,imgs[0].getWidth(),imgs[0].getHeight(),pt);
        if (0 == dyOffset) top += dy;
        else top -= dyOffset;
        dyOffset = 0;
        canvas.drawBitmap(imgs[flightIdx], left, top, pt);
        if (top >= 600) {
           Bitmap eraser = imgs[flightIdx].copy(Bitmap.Config.ARGB_8888,true);
            pt.setColor(Color.RED);
            pt.setStyle(Paint.Style.FILL);
            Log.d("crashed",eraser+" is the bitmap"+" "+imgs[flightIdx]);
//            canvas.drawRect(left,top,left+imgs[flightIdx].getWidth(),top+imgs[flightIdx].getHeight(),pt);
            return gameState.gameOver;
        }
        width=imgs[flightIdx].getWidth();
        height=imgs[flightIdx].getHeight();

//        Log.d("descend", top + " " + dyOffset + " " + dy);
        Rect r1= new Rect(left,top,left+width, top+height);
        pt.setStyle(Paint.Style.STROKE);pt.setColor(Color.RED);
//        canvas.drawRect(r1,pt);
        for (int i=0;i<birdSprite.length;i++) {
            bird bd = birdSprite[i];
            if(bd == null) continue;
            Bitmap b = bd.imgs[bd.birdIdx/2];

            Log.d("collision",r1.toString()+" "+bd.left+" "+bd.top+" "+(bd.left+ bd.width)+" "+(bd.top+bd.height));
            if ( r1.intersect(bd.left, bd.top, bd.left+bd.width,bd.top+bd.height)){
                Log.d("collision"," Flight sprite collides with Bird ");
                Bitmap eraser = imgs[flightIdx].copy(Bitmap.Config.ARGB_8888,true);
                eraser.eraseColor(Color.TRANSPARENT);
                Log.d("crashed",eraser+" is the bitmap"+":crashed with bird");
                canvas.drawBitmap(eraser, left, top, pt);
                return gameState.gameOver;
            }
        }



        flightIdx = (flightIdx + 1) % 4;
//        System.out.println(" drawn the plane");
        return gameState.active;
    }


    public int getLeft() {
        return left;
    }
}
