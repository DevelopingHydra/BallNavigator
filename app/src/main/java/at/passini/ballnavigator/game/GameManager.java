package at.passini.ballnavigator.game;

/**
 * Created by xeniu on 29.01.2018.
 */

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.LinkedList;

import at.passini.ballnavigator.game.Helper.Vector;
import at.passini.ballnavigator.game.gameobjects.Ball;
import at.passini.ballnavigator.game.gameobjects.DrawingLine;
import at.passini.ballnavigator.game.gameobjects.GameObject;

/**
 * Manages the grid and location of the gameobjects
 * Manages all gameobjects
 */
public class GameManager {
    private final int gridColumns = 8;
    private final int gridRows = 12;

    private int deviceWidth, deviceHeight;
    private int displayUnitX, displayUnitY;

    private LinkedList<GameObject> gameElements;
    private LinkedList<Ball> balls;

    private LinkedList<DrawingLine> drawingLines;
    private DrawingLine currentDrawingLine;
    private boolean isCurrentlyDrawing;

    private static GameManager instance;

    private GameManager() {
        this.deviceHeight = 0;
        this.deviceWidth = 0;
        this.gameElements = new LinkedList<>();
        this.balls = new LinkedList<>();
        this.drawingLines = new LinkedList<>();
        this.isCurrentlyDrawing = false;
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public void onDrawUpdate(Canvas canvas, long timePassed) {
        // remove garbage
        removeUnusedDrawingLines();

        // move with collision detection
        moveBall();

        // draw all things
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, deviceWidth, deviceHeight, p);
        for (GameObject go : gameElements) {
            go.onDrawUpdate(canvas, timePassed);
        }

        for (Ball ball : balls) {
            ball.onDrawUpdate(canvas, timePassed);
        }

//        Log.d("gm","num drawing lines: "+this.drawingLines.size());
        for (DrawingLine lines : drawingLines) {
            lines.onDrawUpdate(canvas, timePassed);
        }
    }

    public void startGame() {
        // todo Map.getGameObjects
    }

    private void moveBall() {
        for (Ball ball : balls) {
            ball.moveTo((int) (ball.getPosX() + ball.getDirectionX()), (int) (ball.getPosY() + ball.getDirectionY()));
        }
    }

    private void removeUnusedDrawingLines() {
        for (DrawingLine line : this.drawingLines) {
            if (line.isDead()) {
                Log.d("gm","killing drawing line");
                this.drawingLines.remove(line);
            }
        }
    }

    private void updateDisplayUnits() {
        this.displayUnitX = this.deviceWidth / this.gridColumns;
        this.displayUnitY = this.deviceHeight / this.gridRows;
    }

    public void setDeviceWidth(int deviceWidth) {
        this.deviceWidth = deviceWidth;
    }

    public void setDeviceHeight(int deviceHeight) {
        this.deviceHeight = deviceHeight;
    }

    public int getDisplayUnitX() {
        return displayUnitX;
    }

    public int getDisplayUnitY() {
        return displayUnitY;
    }

    public void addGameObject(GameObject gameObject) {
        this.gameElements.add(gameObject);
    }

    public void addBall(Ball ball) {
        this.balls.add(ball);
    }

    /* swipe handling and drawing */

    public void startSwipe(float touchX, float touchY, long timePassed) {
        Log.d("gm", "start swipe");
        DrawingLine local =  new DrawingLine(timePassed);
        local.addPosition(new Vector(touchX, touchY));

        this.drawingLines.add(local);
        currentDrawingLine = local;
        this.isCurrentlyDrawing = true;
    }

    public void onSwipeMove(float touchX, float touchY, long timePassed) {
        Log.d("gm", "still swiping");
        if (this.isCurrentlyDrawing) {
            this.currentDrawingLine.addPosition(new Vector(touchX, touchY));
        } else {
            throw new RuntimeException("WTF?!");
        }
    }

    public void stopSwipe(float touchX, float touchY, long timePassed) {
        Log.d("gm", "stop swipe");
        if (this.currentDrawingLine != null) {
            this.currentDrawingLine.addPosition(new Vector(touchX, touchY));
        }
        this.isCurrentlyDrawing = false;
    }


    /* grid handling */

    public Vector getAbsoluteLocation(int gridX, int gridY) {
        return new Vector(deviceWidth / gridColumns * gridX, deviceHeight / gridRows * gridY);
    }

}
