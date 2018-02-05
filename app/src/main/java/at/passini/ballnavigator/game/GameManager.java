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
import at.passini.ballnavigator.game.gameobjects.DrawingLine;
import at.passini.ballnavigator.game.gameobjects.GameObject;
import at.passini.ballnavigator.game.gameobjects.RectGameObject;
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

//    private ConcurrentLinkedQueue<DrawingLine> drawingLines;
    private DrawingLine currentDrawingLine;
    private boolean isCurrentlyDrawing;

    private static GameManager instance;

    private GameManager() {
        this.deviceHeight = 0;
        this.deviceWidth = 0;
        this.gameElements = new ConcurrentLinkedQueue<>();
        this.balls = new ConcurrentLinkedQueue<>();
//        this.drawingLines = new ConcurrentLinkedQueue<>();
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
            Vector ballNewPosition = ballPosition.add(ball.getAbsoluteDirectionVector());
            Paint p = new Paint();
            p.setColor(Color.BLUE);
            p.setStrokeWidth(5f);
            p.setStyle(Paint.Style.STROKE);
            canvas.drawLine((float) ballPosition.getX(), (float) ballPosition.getY(), (float) ballNewPosition.getX(), (float) ballNewPosition.getY(), p);
        }

//        Log.d("gm","num drawing lines: "+this.drawingLines.size());
//        for (DrawingLine lines : drawingLines) {
//            lines.onDrawUpdate(canvas, timePassed);
//        }
    }

    /* start game */

    public void startGame() {
        // reset everything
        this.balls.clear();
        this.gameElements.clear();
//        this.drawingLines.clear();

        // todo Map.getGameObjects

        // add the walls to the gameObject list
        Wall left = new Wall(0, 0, 0, gridRows);
        Wall top = new Wall(0, 0, gridColumns, 0);
        Wall right = new Wall(gridColumns, 0, gridColumns, gridRows);
        Wall bottom = new Wall(0, gridRows, gridColumns, gridRows);

//        Wall testWall=new Wall(70,0,0,100);
        this.gameElements.add(left);
        this.gameElements.add(right);
        this.gameElements.add(top);
        this.gameElements.add(bottom);
//        this.gameElements.add(testWall);

        // before map works set up ball statically
        balls.add(new Ball(getAbsoluteLocation(new Vector(5, 10)), new Vector(.1f, 1f)));
    }

    /* collision detection */

    private synchronized void updatePositions(float timePassed) {
        // this Hashmap records all the balls that can still move
        // it saves the ball and the time remaining
        ConcurrentHashMap<Ball, Float> ballsThatCanStillMove = new ConcurrentHashMap<>();

        for (Ball ball : this.balls) {
            ballsThatCanStillMove.put(ball, timePassed);
        }

        collisionDetectionBalls(ballsThatCanStillMove);
    }

    private void collisionDetectionBalls(ConcurrentHashMap<Ball, Float> ballsThatCanStillMove) {
        for (HashMap.Entry<Ball, Float> entry : ballsThatCanStillMove.entrySet()) {
            Ball ball = entry.getKey();
            float timeRemaining = entry.getValue();
            // get the line of the ball
            Vector ballPosition = ball.getAbsoluteContactPoint();
//            Vector ballNewPosition = new Vector(ball.getDirectionX() + ball.getPosX(), ball.getDirectionY() + ball.getPosY());
            Vector ballNewPosition = reachablePointWithinTime(ballPosition, ball.getAbsoluteDirectionVector(), timeRemaining);
            Line ballRay = new Line(ballPosition, ballNewPosition);

            boolean didCollisionHappen = false;

            for (GameObject gameObject : this.gameElements) {
                double timeToMove = 0;
                if (gameObject instanceof RectGameObject) {
                    RectGameObject rectGameObject = (RectGameObject) gameObject;
                    timeToMove = collisionDetectionRect(timeRemaining, rectGameObject.getAbsoluteRectangle(), gameObject, ballRay, ball);

                } else if (gameObject instanceof Wall) {
                    Wall wall = (Wall) gameObject;
                    timeToMove = collisionDetectionLine(timeRemaining, wall.getAbsolute(), gameObject, ballRay, ball);
                } else if (gameObject instanceof DrawingLine) {
                    DrawingLine drawingLine = (DrawingLine) gameObject;
                    timeToMove = collisionDetectionDrawingLine(timeRemaining, drawingLine, ballRay, ball);
                }
                if (timeToMove > 0) {
                    Log.d("gm", "collision with wall or brick");
                    didCollisionHappen = true;
                    timeRemaining -= timeToMove;

//                        break; // the ball can only collide with one object at a time
                    // above line is wrong
                    // we just have to get the new ballRay
                    ballPosition = ball.getAbsoluteContactPoint();
                    ballNewPosition = reachablePointWithinTime(ballPosition, ball.getAbsoluteDirectionVector(), timeRemaining);
                    ballRay = new Line(ballPosition, ballNewPosition);
                }
            }

            if (!didCollisionHappen) {
                // no collision --> the ball can move as he wants to so we move it
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

    private double collisionDetectionDrawingLine(double timeRemaining, DrawingLine drawingLine, Line ballRay, Ball ball) {
        Vector lastPos = null;
        for (Vector point : drawingLine.getPositions()) {
            if (lastPos != null) {
                Line currLine = new Line(lastPos, point);
                double timeNeeded = collisionDetectionLine(timeRemaining, currLine, drawingLine, ballRay, ball);
                if (timeNeeded > 0) {
                    return timeNeeded;
                }
            }
            lastPos = point;
        }

        // seems like there was no collisions
        // therefore the time it took to move (it didn't move) is 0
        return 0;
    }

    /**
     * @param ballRay
     * @return the time it took to move the ball if it was moved or if we didn't move then 0
     */
    private double collisionDetectionRect(double timeRemaining, Rect gameObjectRect, GameObject gameObject, Line ballRay, Ball ball) {
        // get all the brick lines
        Vector vPointA = new Vector(gameObjectRect.left, gameObjectRect.top);
        Vector vPointB = new Vector(gameObjectRect.right, gameObjectRect.top);
        Vector vPointC = new Vector(gameObjectRect.left, gameObjectRect.bottom);
        Vector vPointD = new Vector(gameObjectRect.right, gameObjectRect.bottom);

        Line[] rectLines = new Line[]{
                new Line(vPointA, vPointB),
                new Line(vPointA, vPointC),
                new Line(vPointD, vPointC),
                new Line(vPointD, vPointB)
        };

        // now loop over all Lines and check for an intersection
        for (Line rectLine : rectLines) {
            double timeNeeded = collisionDetectionLine(timeRemaining, rectLine, gameObject, ballRay, ball);
            if (timeNeeded > 0) {
                return timeNeeded;
            }
        }

        // seems like there was no collisions
        // therefore the time it took to move (it didn't move) is 0
        return 0;
    }

    private double collisionDetectionLine(double timeRemaining, Line gameObjectLine, GameObject gameObject, Line ballRay, Ball ball) {
        Vector vPointIntersection = ballRay.intersectWithOtherLine(gameObjectLine);
        if (vPointIntersection.getX() < Double.MAX_VALUE && vPointIntersection.getY() < Double.MAX_VALUE) {
            // there was an intersection
            gameObject.onHit(ball);
            // now return the time it took to move the ball there
            double timeNeeded = moveGameObjectToLocation(ball, vPointIntersection, timeRemaining, ball.getAbsoluteDirectionVector());

            // change the angle of the ball
            Log.d("gm", "ball should rotate now");
//            ball.moveToAbsolutePosition(getAbsoluteLocation(gridColumns / 2, gridRows / 2));
            if (ball.isMovingUp() && ball.getAbsolutePosition().getY() > vPointIntersection.getY()) {
                ball.flipDirectionY();
                Log.d("gm", "ball is moving up and flipping Y");
            }
            if (ball.isMovingDown() && ball.getAbsolutePosition().getY() < vPointIntersection.getY()) {
                ball.flipDirectionY();
                Log.d("gm", "ball is moving down and flipping Y");
            }
            if (ball.isMovingLeft() && ball.getAbsolutePosition().getX() > vPointIntersection.getX()) {
                ball.flipDirectionX();
                Log.d("gm", "ball is moving left and flipping X");
            }
            if (ball.isMovingRight() && ball.getAbsolutePosition().getX() < vPointIntersection.getX()) {
                ball.flipDirectionX();
                Log.d("gm", "ball is moving right and flipping X");
            }

            // now return
            return timeNeeded;
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
    public double moveGameObjectToLocation(GameObject gameObject, Vector vPointTarget, double timeAvailable, Vector vSpeedToMove) {
        Vector vPointGameObjectPosition;
        if (gameObject instanceof Ball) {
            Ball b = (Ball) gameObject;
            vPointGameObjectPosition = b.getAbsoluteContactPoint();
        } else if (gameObject instanceof RectGameObject) {
            RectGameObject rectGameObject = (RectGameObject) gameObject;
            vPointGameObjectPosition = new Vector(rectGameObject.getAbsoluteX(), rectGameObject.getAbsoluteY());
        } else {
            vPointGameObjectPosition = new Vector(Float.MAX_VALUE, Float.MAX_VALUE);
        }
        double distanceToTarget = vPointGameObjectPosition.getDistanceTo(vPointTarget);

        Vector vPointToMoveTo;

        // calculate the new position of the ball with the given remaining time
        // first we need to know how long it will take us to get there
        double timeNeeded = timeNeededToMoveDistance(distanceToTarget, vSpeedToMove);

        // now check if we have more time than we need
        if (timeAvailable > timeNeeded) {
            // we have enough time to move to the target
            // now move to the target
            vPointToMoveTo = vPointTarget;
        } else {
            // we cannot move to the target
            // now we calculate the position which we can reach
            vPointToMoveTo = reachablePointWithinTime(vPointGameObjectPosition, vSpeedToMove, timeAvailable);
        }

        // now move the ball
        if (gameObject instanceof Ball) {
            Ball ball = (Ball) gameObject;
            ball.moveToAbsolutePosition(vPointToMoveTo);
        } else if (gameObject instanceof RectGameObject) {
            RectGameObject rectGameObject = (RectGameObject) gameObject;
            rectGameObject.setAbsoluteX((int) vPointToMoveTo.getX());
            rectGameObject.setAbsoluteY((int) vPointToMoveTo.getY());
        }

        return timeNeeded;
    }

    public Vector reachablePointWithinTime(Vector vPointStart, Vector vDirection, double timeToMove) {
        Vector vUnit = vDirection.getUnitVector();
        double dinstanceReachable = reachableDistanceWithinTime(timeToMove, vDirection);
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
    public double timeNeededToMoveDistance(double distanceToTravel, Vector vSpeed) {
        // here we specify how long traveling a distance takes
        // t = s / v
        double velocity = vSpeed.getLength();
        return distanceToTravel / velocity;
    }

    /**
     * todo please fix calculation of speed
     *
     * @param timeToTravel
     * @param vSpeed
     * @return
     */
    public double reachableDistanceWithinTime(double timeToTravel, Vector vSpeed) {
        // s = v * t
        double v = vSpeed.getLength();
        return v * timeToTravel;
    }

    /* swipe handling and drawing */

    private void removeUnusedDrawingLines() {
        for (GameObject gameObject:this.gameElements) {
            if(gameObject instanceof DrawingLine){
            DrawingLine drawingLine= (DrawingLine) gameObject;
            if (drawingLine.isDead()) {
                this.gameElements.remove(drawingLine);
            }
        }}
    }

    public void startSwipe(float touchX, float touchY, long timePassed) {
        DrawingLine local = new DrawingLine(timePassed);
        local.addPosition(new Vector(touchX, touchY));

        this.gameElements.add(local);
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

    public float getAbsoluteX(int gridX) {
        return displayUnitX * gridX;
    }

    public float getAbsoluteY(int gridY) {
        return displayUnitY * gridY;
    }

    public int getGridX(double absoluteX) {
        return (int) (absoluteX / displayUnitX);
    }

    public int getGridY(double absoluteY) {
        return (int) (absoluteY / displayUnitY);
    }

    public Vector getAbsoluteLocation(int gridX, int gridY) {
        return new Vector(getAbsoluteX(gridX), getAbsoluteY(gridY));
    }

    public Vector getAbsoluteLocation(Vector vGrid) {
        return new Vector(getAbsoluteX((int) vGrid.getX()), getAbsoluteY((int) vGrid.getY()));
    }

    public Vector getGridLocation(double absoluteX, double absoluteY) {
        return new Vector(getGridX(absoluteX), getGridY(absoluteY));
    }

    public Vector getGridLocation(Vector vAbsolute) {
        return new Vector(getGridX(vAbsolute.getX()), getGridY(vAbsolute.getY()));
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
