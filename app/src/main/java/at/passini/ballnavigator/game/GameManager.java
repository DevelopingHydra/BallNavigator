package at.passini.ballnavigator.game;

/**
 * Created by xeniu on 29.01.2018.
 */

import android.graphics.Canvas;

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

    private LinkedList<GameObject> gameElements;
    private LinkedList<Ball> balls;
    private LinkedList<DrawingLine> drawingLines;
    private DrawingLine currentDrawingLine;

    private static GameManager instance;

    private GameManager() {
        this.deviceHeight = 0;
        this.deviceWidth = 0;
        this.gameElements = new LinkedList<>();
        this.balls = new LinkedList<>();
        this.drawingLines = new LinkedList<>();
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public void onDrawUpdate(Canvas canvas, long timePassed) {
        moveBall();

        for (GameObject go : gameElements) {
            go.onDrawUpdate(canvas, timePassed);
        }

        for (Ball ball : balls) {
            ball.onDrawUpdate(canvas, timePassed);
        }

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


    public void setDeviceWidth(int deviceWidth) {
        this.deviceWidth = deviceWidth;
    }

    public void setDeviceHeight(int deviceHeight) {
        this.deviceHeight = deviceHeight;
    }

    public void addGameObject(GameObject gameObject) {
        this.gameElements.add(gameObject);
    }

    public void addBall(Ball ball) {
        this.balls.add(ball);
    }

    /* swipe handling and drawing */

    public void startSwipe(float touchX, float touchY) {
        currentDrawingLine = new DrawingLine();
        currentDrawingLine.addPosition(new Vector(touchX, touchY));

        this.drawingLines.add(currentDrawingLine);
    }

    public void onSwipeMove(float touchX, float touchY) {
        if (this.currentDrawingLine != null) {
            this.currentDrawingLine.addPosition(new Vector(touchX, touchY));
        } else {
            throw new RuntimeException("WTF?!");
        }
    }

    public void stopSwipe(float touchX, float touchY) {
        this.currentDrawingLine = null;
    }


    /* grid handling */

    public Vector getAbsoluteLocation(int gridX, int gridY) {
        return new Vector(deviceWidth / gridColumns * gridX, deviceHeight / gridRows * gridY);
    }

}
