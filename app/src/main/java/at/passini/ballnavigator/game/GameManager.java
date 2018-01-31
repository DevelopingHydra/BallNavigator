package at.passini.ballnavigator.game;

/**
 * Created by xeniu on 29.01.2018.
 */

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import at.passini.ballnavigator.game.Helper.Line;
import at.passini.ballnavigator.game.Helper.Vector;
import at.passini.ballnavigator.game.gameobjects.Ball;
import at.passini.ballnavigator.game.gameobjects.Brick;
import at.passini.ballnavigator.game.gameobjects.DrawingLine;
import at.passini.ballnavigator.game.gameobjects.GameObject;
import at.passini.ballnavigator.game.gameobjects.Wall;

/**
 * Manages the grid and location of the gameobjects
 * Manages all gameobjects
 */
public class GameManager {
    private final int gridColumns = 100;
    private final int gridRows = 100;

    private int deviceWidth, deviceHeight;
    private int displayUnitX, displayUnitY;

    private ConcurrentLinkedQueue<GameObject> gameElements;
    private ConcurrentLinkedQueue<Ball> balls;

    private ConcurrentLinkedQueue<DrawingLine> drawingLines;
    private DrawingLine currentDrawingLine;
    private boolean isCurrentlyDrawing;

    private static GameManager instance;

    private GameManager() {
        this.deviceHeight = 0;
        this.deviceWidth = 0;
        this.gameElements = new ConcurrentLinkedQueue<>();
        this.balls = new ConcurrentLinkedQueue<>();
        this.drawingLines = new ConcurrentLinkedQueue<>();
        this.isCurrentlyDrawing = false;
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    /* drawing */

    public void onDrawUpdate(Canvas canvas, long timePassed) {
        // remove garbage
        removeUnusedDrawingLines();

        // move with collision detection
        updatePositions(timePassed);

        // draw all things
        canvas.drawColor(Color.WHITE);
        for (GameObject go : gameElements) {
            go.onDrawUpdate(canvas, timePassed);
        }

        for (Ball ball : balls) {
            ball.onDrawUpdate(canvas, timePassed);

            // during dev draw the ball ray
            Vector ballPosition = ball.getAbsoluteContactPoint();
            Vector ballNewPosition = reachablePointWithinTime(ballPosition, ball.getAbsoluteDirectionVector(), timePassed);
            Paint p = new Paint();
            p.setColor(Color.BLUE);
            p.setStrokeWidth(5f);
            p.setStyle(Paint.Style.STROKE);
            canvas.drawLine((float) ballPosition.getX(), (float) ballPosition.getY(), (float) ballNewPosition.getX(), (float) ballNewPosition.getY(), p);
        }

//        Log.d("gm","num drawing lines: "+this.drawingLines.size());
        for (DrawingLine lines : drawingLines) {
            lines.onDrawUpdate(canvas, timePassed);
        }
    }

    /* start game */

    public void startGame() {
        // reset everything
        this.balls.clear();
        this.gameElements.clear();
        this.drawingLines.clear();

        // todo Map.getGameObjects

        // add the walls to the gameObject list
        int thickness = 20;
        Wall left = new Wall(0, 0, thickness, gridRows);
        Wall top = new Wall(0, 0, gridColumns, thickness);
        Wall right = new Wall(gridColumns - thickness, 0, gridColumns, gridRows);
        Wall bottom = new Wall(0, gridRows - thickness, gridColumns, gridRows);
        this.gameElements.add(left);
        this.gameElements.add(right);
        this.gameElements.add(top);
        this.gameElements.add(bottom);

        // before map works set up ball statically
        balls.add(new Ball(gridColumns / 2, gridRows / 2, new Vector(10f, 10f)));
    }

    /* collision detection */

    private synchronized void updatePositions(long timePassed) {
        // this hashmap records all the balls that can still move
        // it saves the ball and the time remaining
        ConcurrentHashMap<Ball, Long> ballsThatCanStillMove = new ConcurrentHashMap<>();

        for (Ball ball : this.balls) {
            ballsThatCanStillMove.put(ball, timePassed);
        }

        collisionDetectionBalls(ballsThatCanStillMove);
    }

    private void collisionDetectionBalls(ConcurrentHashMap<Ball, Long> ballsThatCanStillMove) {
        for (HashMap.Entry<Ball, Long> entry : ballsThatCanStillMove.entrySet()) {
            Ball ball = entry.getKey();
            long timeRemaining = entry.getValue();
            // get the line of the ball
            Vector ballPosition = ball.getAbsoluteContactPoint();
//            Vector ballNewPosition = new Vector(ball.getDirectionX() + ball.getPosX(), ball.getDirectionY() + ball.getPosY());
            Vector ballNewPosition = reachablePointWithinTime(ballPosition, ball.getAbsoluteDirectionVector(), timeRemaining);
            Line ballRay = new Line(ballPosition, ballNewPosition);

            boolean didCollisionHappen = false;

            for (GameObject gameObject : this.gameElements) {
                if (gameObject instanceof Brick || gameObject instanceof Wall) {
                    long timeToMove = collisionDetectionRect(timeRemaining, gameObject, ballRay, ball);
                    if (timeToMove > 0) {
                        Log.d("gm", "collision with wall or brick");
                        didCollisionHappen = true;
                        timeRemaining -= timeToMove;
                    }
                }
            }

            if (!didCollisionHappen) {
                // no collision --> move the ball
                moveGameObjectToLocation(ball, ballNewPosition, timeRemaining, ball.getAbsoluteDirectionVector());
                timeRemaining = 0;
            }

            // now check if this ball can still move
            // if not remove it from the list
            if (timeRemaining <= 0) {
                ballsThatCanStillMove.remove(ball);
            } else {
                // otherwise update the time remaining to move
                ballsThatCanStillMove.put(ball, timeRemaining);
            }
        }

        // now check if there are still balls that can move
        // if so then call collision detection for these balls again
        if (ballsThatCanStillMove.size() > 0) {
            collisionDetectionBalls(ballsThatCanStillMove);
        }

    }

    /**
     * @param ballRay
     * @return the time it took to move the ball if it was moved, otherwise 0
     */
    private long collisionDetectionRect(long timeRemaining, GameObject gameObject, Line ballRay, Ball ball) {
        // get all the brick lines
        Rect rect = getAbsoluteRect(gameObject.getAbsoluteRectangle());
        Vector vPointA = new Vector(rect.left, rect.top);
        Vector vPointB = new Vector(rect.right, rect.top);
        Vector vPointC = new Vector(rect.left, rect.bottom);
        Vector vPointD = new Vector(rect.right, rect.bottom);

        Line[] rectLines = new Line[]{
                new Line(vPointA, vPointB),
                new Line(vPointA, vPointC),
                new Line(vPointD, vPointC),
                new Line(vPointD, vPointB)
        };

        // now loop over all Lines and check for an intersection
        for (Line rectLine : rectLines) {
            Vector vPointIntersection = ballRay.intersectWithOtherLine(rectLine);
            if (vPointIntersection.getX() < Double.MAX_VALUE && vPointIntersection.getY() < Double.MAX_VALUE) {
                // there was an intersection
                gameObject.onHit(ball);
                return moveGameObjectToLocation(ball, vPointIntersection, timeRemaining, ball.getAbsoluteDirectionVector());
            }
        }

        // seems like there was no collisions
        // therefore the time it took to move (it didn't move) is 0
        return 0;
    }

    /* moving */


    /**
     * Moves a ball to the location and returns how long that took
     *
     * @param gameObject
     * @return how long that move took
     */
    private long moveGameObjectToLocation(GameObject gameObject, Vector vPointTarget, long timeAvailable, Vector vSpeedToMove) {
        Vector vPointGameObjectPosition = getAbsoluteLocation(gameObject.getAbsoluteX(),gameObject.getAbsoluteY());
        double distanceToTarget = vPointTarget.getDistanceTo(vPointGameObjectPosition);

        Vector vPointToMoveTo;

        // calculate the new position of the ball with the given remaining time
        // first we need to know how long it will take us to get there
        long timeNeeded = timeNeededToMoveDistance(distanceToTarget, vSpeedToMove);

        // now check if we have more time than we need
        if (timeAvailable > timeNeeded) {
            // now move to the target
            vPointToMoveTo = vPointTarget;
            // because we needed only some of the remaining time we adjust it
            timeAvailable -= timeNeeded;
        } else {
            // we cannot move to the target
            // now we calculate the position which we can reach
            vPointToMoveTo = reachablePointWithinTime(vPointGameObjectPosition, vSpeedToMove, timeNeeded);

            // because we needed all the remaining time we set it 0
            timeAvailable = 0;
        }

        // now move the ball
        if (gameObject instanceof Ball) {
            Ball b = (Ball) gameObject;
            b.moveTo(vPointToMoveTo);
            // if it is a ball we also have to flip the direction
            // todo correct calculation with angle
            if (b.isMovingUp() && vPointTarget.getY() < b.getAbsoluteX()) {
                b.flipDirectionY();
            }
            if (b.isMovingDown() && vPointTarget.getY() > b.getAbsoluteY()) {
                b.flipDirectionY();
            }
            if (b.isMovingLeft() && vPointTarget.getX() < b.getAbsoluteX()) {
                b.flipDirectionX();
            }
            if (b.isMovingRight() && vPointTarget.getX() > b.getAbsoluteY()) {
                b.flipDirectionX();
            }
        } else {
            gameObject.setAbsoluteX((int) vPointToMoveTo.getX());
            gameObject.setAbsoluteY((int) vPointToMoveTo.getY());
        }

        return timeAvailable;
    }

    public Vector reachablePointWithinTime(Vector vPointStart, Vector vDirection, long timeToMove) {
        Vector vUnit = vDirection.getUnitVector();
        long dinstanceReachable = reachableDistanceWithinTime(timeToMove, vDirection);
        Vector vNewPosition = vUnit.multiplyWithScalar(dinstanceReachable);
        return vPointStart.add(vNewPosition);
    }

    /**
     * todo please fix the calculation of speed
     *
     * @param distanceToTravel
     * @param vSpeed
     * @return
     */
    public long timeNeededToMoveDistance(double distanceToTravel, Vector vSpeed) {
        // here we specify how long traveling a distance takes
        // t = s / v
        double v = vSpeed.getLength();
        long timeNeeded = (long) (distanceToTravel / v);
        return timeNeeded;
    }

    /**
     * todo please fix calculation of speed
     *
     * @param timeToTravel
     * @param vSpeed
     * @return
     */
    public long reachableDistanceWithinTime(long timeToTravel, Vector vSpeed) {
        // s = v * t
        double v = vSpeed.getLength();
        return (long) (v * timeToTravel / 500);
    }

    /* swipe handling and drawing */

    private void removeUnusedDrawingLines() {
        for (DrawingLine line : this.drawingLines) {
            if (line.isDead()) {
                this.drawingLines.remove(line);
            }
        }
    }

    public void startSwipe(float touchX, float touchY, long timePassed) {
        DrawingLine local = new DrawingLine(timePassed);
        local.addPosition(new Vector(touchX, touchY));

        this.drawingLines.add(local);
        currentDrawingLine = local;
        this.isCurrentlyDrawing = true;
    }

    public void onSwipeMove(float touchX, float touchY, long timePassed) {
        if (this.isCurrentlyDrawing) {
            this.currentDrawingLine.addPosition(new Vector(touchX, touchY));
        }
    }

    public void stopSwipe(float touchX, float touchY, long timePassed) {
        if (this.currentDrawingLine != null) {
            this.currentDrawingLine.addPosition(new Vector(touchX, touchY));
        }
        this.isCurrentlyDrawing = false;
    }

    /* grid handling */

    public double getAbsoluteX(int gridX){return displayUnitX*gridX;}
    public double getAbsoluteY(int gridY){return displayUnitY*gridY;}

    public int getGridX(double absoluteX){return (int) (absoluteX/displayUnitX);}
    public int getGridY(double absoluteY){return (int) (absoluteY/displayUnitY);}

    public Vector getAbsoluteLocation(int gridX, int gridY) {
        return new Vector(getAbsoluteX(gridX), getAbsoluteY(gridY));
    }

    public Vector getAbsoluteLocation(Vector vGrid) {
        return new Vector(getAbsoluteX((int) vGrid.getX()), getAbsoluteY((int) vGrid.getY()));
    }

    public Vector getGridLocation(double absoluteX, double absoluteY){
        return new Vector(getGridX(absoluteX),getGridY(absoluteY));
    }

    public Vector getGridLocation(Vector vAbsolute) {
        return new Vector(getGridX( vAbsolute.getX()), getGridY(vAbsolute.getY()));
    }

    public Rect getAbsoluteRect(int gridX, int gridY, int gridRight, int gridBottom) {
        return new Rect(displayUnitX * gridX, displayUnitY * gridY, displayUnitX * gridRight, displayUnitY * gridBottom);
    }

    public Rect getAbsoluteRect(Rect rGrid) {
        return getAbsoluteRect(rGrid.left, rGrid.top, rGrid.right, rGrid.bottom);
    }

    private void resizeEverything() {
        // first of all update the displayUnits
        this.displayUnitX = this.deviceWidth / this.gridColumns;
        this.displayUnitY = this.deviceHeight / this.gridRows;


    }

    public void setDeviceWidth(int deviceWidth) {
        this.deviceWidth = deviceWidth;
        resizeEverything();
    }

    public void setDeviceHeight(int deviceHeight) {
        this.deviceHeight = deviceHeight;
        resizeEverything();
    }

    public int getDisplayUnitX() {
        return displayUnitX;
    }

    public int getDisplayUnitY() {
        return displayUnitY;
    }

    /* getter and setter */
    public void addGameObject(GameObject gameObject) {
        this.gameElements.add(gameObject);
    }

    public void addBall(Ball ball) {
        this.balls.add(ball);
    }


}
