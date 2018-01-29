package at.passini.ballnavigator.game;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.LinkedList;

import at.passini.ballnavigator.game.Helper.Vector;

/**
 * Created by Benedikt on 27.01.2018.
 */

public class GameView extends SurfaceView {
    private final SurfaceHolder holder;
    private final Context context;
    private boolean isRunning;
    private long timePassed;
    private long timeLastUpdate;

    public GameView(Context context) {
        super(context);
        this.context = context;
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (!isRunning) {
                    try {
                        updateCanvas();
                    } catch (Exception e) {
                    }
                    runGame();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                GameManager.getInstance().startSwipe(touchX, touchY, timePassed);
                break;
            case MotionEvent.ACTION_MOVE:
                GameManager.getInstance().onSwipeMove(touchX, touchY, timePassed);
                break;
            case MotionEvent.ACTION_UP:
                GameManager.getInstance().stopSwipe(touchX, touchY, timePassed);
                break;
        }

        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        GameManager.getInstance().setDeviceWidth(w);
        GameManager.getInstance().setDeviceHeight(h);
    }

    private void updateCanvas() {
        Canvas c = holder.lockCanvas();
        onDraw(c);
//        draw(c);
        holder.unlockCanvasAndPost(c);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        long currentTime = System.currentTimeMillis();
        timePassed = currentTime - timeLastUpdate;
        timeLastUpdate = currentTime;

        GameManager.getInstance().onDrawUpdate(canvas, timePassed);
    }

    /**
     * game loop
     */
    private void runGame() {
        isRunning = true;
        timeLastUpdate = System.currentTimeMillis();
        timePassed = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    try {
                        updateCanvas();
                    } catch (Exception e) {
                    }

                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

}
