package com.example.flightapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    gameView gv;
    static int gameOverMsgId = View.generateViewId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.gamelayout);
//        setContentView(rl);
        gv = new gameView(this.getApplicationContext(), this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        TextView txt = new TextView(getApplicationContext());
        txt.setText(R.string.finalMessage);
        txt.setId(gameOverMsgId);
        rl.addView(gv);

        txt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 50);

//        lp.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        txt.setLayoutParams(lp);


//        rl.addView(txt);


        Log.d("state", "Started" + rl.getChildAt(1));

        txt.setVisibility(View.INVISIBLE);

        gv.startThread();
    }

    public void endGame(int score) {
        Intent intnt = new Intent(this, endScreen.class);
        intnt.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intnt.putExtra("score", score);
        Log.d("transition", "started");
        startActivity(intnt);
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean res = super.onTouchEvent(event);

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            Log.d("TOUCH", "sensed");
            gv.touchDown(x, y);
        }
        return res;

    }
}

enum gameState {
    start, paused, manualExit, gameOver, active, toStart, crashed, birdCrash;
}

class gameView extends SurfaceView implements Runnable {
    Background[] backgrounds = new Background[2];
    List<missileSprite> missiles = new LinkedList<>();
    flightSprite flight;
    int cloudPosX = 0;
    Handler missileQ = new Handler();
    Context context;
    int cloudPosY = 0;
    gameState currentState = gameState.toStart;
    MainActivity myActivity;
    int canvasWidth = 0, canvasHeight = 0;
    int cloudIdx = 0;
    Canvas canvas;
    int height, width, cWidth, cHeight;
    SurfaceHolder ourHolder;
    int score = 0;
    bird[] birdsprite = new bird[6];
    Paint paint;
    int viewHeight = getResources().getDisplayMetrics().heightPixels;
    int viewWidth = getResources().getDisplayMetrics().widthPixels;

    @SuppressWarnings("")


    public gameView(Context contextIn, Activity myActivityIn) {
        super(contextIn);
        int score = 0;
        myActivity = (MainActivity) myActivityIn;
        context = contextIn;
        ourHolder = getHolder();
        width = getResources().getDisplayMetrics().widthPixels;
        height = getResources().getDisplayMetrics().heightPixels;
        for (int i = 0; i < birdsprite.length; i++)
            birdsprite[i] = new bird(context, new Random().nextInt(600), new Random().nextInt(30) + 1, new Random().nextInt(5));
        currentState = gameState.active;
//        setZOrderOnTop(true);

        Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.back7);
        Bitmap scaledImg = Bitmap.createScaledBitmap(img, width, height, false);

        for (int i = 0; i < backgrounds.length; i++)
            backgrounds[i] = new Background(context, i, 0, 0.1f, scaledImg, i);
        flight = new flightSprite(R.drawable.sprite2, context);


//        ourHolder.setFormat(PixelFormat.TRANSPARENT);

    }

    public gameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.back7);
        Bitmap scaledImg = Bitmap.createScaledBitmap(img, width, height, false);
        for (int i = 0; i < backgrounds.length; i++)
            backgrounds[i] = new Background(context, 0, 0, 0.1f, scaledImg, i);

    }

    Thread thd;

    public void startThread() {
        thd = new Thread((this));
        thd.start();
    }

    @Override
    public void run() {
//        System.out.println(" running ");
        MediaPlayer player= MediaPlayer.create(myActivity,R.raw.helicopter_sound);

        player.setLooping(true);
        player.start();

        while (true) {
            if (currentState == gameState.gameOver) {

                Log.d("gamestate ", " gameover");
                player.stop();
                myActivity.endGame(score);
                break;
            }
//            if(currentState == gameState.active);


            drawBackground();
            updateBackground();


            try {
                Thread.sleep(40);
            } catch (Exception e) {
//                System.out.println(" sleeping error");
                System.exit(-1);
            }

        }

    }


//        return;
//        if (ourHolder.getSurface().isValid()) {
////            System.out.println(" valid surface");
//
//
//            canvas = ourHolder.lockCanvas();
//            Paint p=new Paint();
//            p.setColor(Color.RED);
//            p.setStyle(Paint.Style.FILL);
//            int textLen = (int)Math.ceil(p.measureText("Gameover! The cat is Dead :-|"));
//            int len ="Gameover! The cat is Dead :-|".length();
//            int xText=width/2 - textLen;
//
//            Rect textB=new Rect();
//            Log.d("rect",(textB.bottom - textB.top)+""+textB+" "+len);
////            p.getTextBounds("Gameover! The cat is Dead :-|",0,len,textB);
//            canvas.drawRect(xText,height/2, xText +textLen, height/2 + (textB.bottom - textB.top) ,p);
//
//
//            p.setTextSize(150);
//
//            p.setColor(Color.BLACK);
//            canvas.drawText("Gameover! The cat is Dead :-|",width/2 - p.measureText("Gameover! The cat is Dead :-|")/2,height/2,p);
//            ourHolder.unlockCanvasAndPost(canvas);
//

    //}
    // }


    private void updateBackground() {
        for (Background bck : backgrounds)
            bck.update();
    }

    public void drawer() {
//        canvas.drawBitmap(cloud,0,0,null);
    }

    public void drawBackground() {
//        System.out.println(" updatingn cloud");

        if (ourHolder.getSurface().isValid()) {
//            System.out.println(" valid surface");
            canvas = ourHolder.lockCanvas();
            int id = 0;
            for (Background bgd : backgrounds) {
                if (bgd == null) {
                    continue;
                }
                bgd.draw(canvas);
                id++;
            }
            this.setLayerType(LAYER_TYPE_SOFTWARE, null);
            currentState = flight.drawSprite(canvas, birdsprite);
            for (int i = 0; i < missiles.size(); i++) {
                boolean res = shoot(missiles.get(i), canvas);
                if (!res) missiles.remove(i);
            }
            for (int idx = 0; idx < birdsprite.length; idx++) {
                bird b = birdsprite[idx];
                boolean res = true;
                if (b != null) res = b.drawSprite(canvas, missiles);
                else {
                    birdsprite[idx] = new bird(context, new Random().nextInt(600), new Random().nextInt(30) + 1, new Random().nextInt(5));
                }

                if (!res) {
                    birdsprite[idx] = null;
                    score++;
                }

            }
            Paint p = new Paint();
            p.setColor(Color.RED);
            p.setTextSize(55);
            Rect box = new Rect();
            int len = ("Score " + score).length();
            p.getTextBounds("Score " + score, 0, len, box);
            canvas.drawText(" Score " + score, viewWidth - p.measureText("Score " + score) - 20, 50, p);

            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    public boolean shoot(missileSprite missileIn, Canvas canvas) {
        boolean res = true;
        res = missileIn.drawSprite(canvas, flight.getTop());
        return res;

    }

    public void touchDown(float x, float y) {

        if (x >= width / 2) {
            Log.d("TOUCH", "Jump");
            flight.setOffset(100);
        } else {

            missiles.add(new missileSprite(context, flight.getTop() + flight.height - 10, -30, flight.getLeft()));
            MediaPlayer mp = MediaPlayer.create(myActivity,R.raw.missile);
            mp.start();


//            Log.d("missile", "loading missile");
//
//
//            missileQ.postDelayed(new Runnable(){
//
//                @Override
//                public void run() {
//
//                    if (shoot(missile)) {
//                        Log.d("missile", "moving");
//                        missileQ.postDelayed(this, 10);
//
//                    } else {
//                        Log.d("missile", "vanished");
//                    }
//                }
//            }, 10);
        }

    }

}