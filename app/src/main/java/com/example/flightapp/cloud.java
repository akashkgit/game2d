package com.example.flightapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class cloud {
    Bitmap cloudMap;
    Context cntxt;
    int leftOffset, top, left, width, height, initLeft=Integer.MIN_VALUE;
    float rate;
    public cloud(Context cntxtIn, int img){
            cntxt=cntxtIn;
            // ------ finding image actual image dimensaion so as to pre scale as per the need
            BitmapFactory.Options opt= new BitmapFactory.Options();
            opt.inJustDecodeBounds = true;
            int screenWidth = cntxt.getResources().getDisplayMetrics().widthPixels;
            int screenHeight = cntxt.getResources().getDisplayMetrics().heightPixels;
            BitmapFactory.decodeResource(cntxt.getResources(), img, opt);
            opt.inSampleSize = calculateInSampleSize(opt, screenWidth/2, screenHeight);
            opt.inJustDecodeBounds = false;
            //------- Loading the image pre scaled before loading into memory
            cloudMap=BitmapFactory.decodeResource(cntxt.getResources(), img, opt);
            width=cloudMap.getWidth();
            height=cloudMap.getHeight();

    }
    public void setPos(float leftOffsetIn, float topIn, float rateIn){
            left=(int)(cntxt.getResources().getDisplayMetrics().widthPixels - leftOffsetIn * width);
            if(-Integer.MIN_VALUE == initLeft)initLeft = left;
            top =(int)(cntxt.getResources().getDisplayMetrics().heightPixels- (int)(topIn * height));
            rate=rateIn * width;
            System.out.println(height+" "+(int)(topIn * height)+" "+top+" "
                    +cntxt.getResources().getDisplayMetrics().heightPixels);
    }
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
    public void draw(Canvas canvas){

//        canvas.drawColor(Color.alpha(0));
        canvas.drawBitmap(cloudMap, left, top,null);
        System.out.println(" drawing cloud...");
        left -= rate;
        if(left <= width/2 * -1)left = initLeft;
    }
}
