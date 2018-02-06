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
        timePassed = 50;
        updatePositions(timePassed);

        // draw all things
        canvas.drawColor(Color.WHITE);
        for (GameObject go : gameElements) {
            go.onDrawUpdate(canvas, timePassed);
        }

        for (Ball ball : balls) {
            ball.onDrawUpdate(canvas, timePassed);

            // during dev draw the ball ray
            ConcurrentLinkedQueue<Line> ballRays = getBallRays(timePassed, ball);
            Paint p = new Paint();
            p.setColor(Color.BLUE);
            p.setStrokeWidth(5f);
            p.setStyle(Paint.Style.STROKE);
            for (Line ballRay : ballRays) {
                Vector ballPosition = ballRay.getPointA();
                Vector ballNewPosition = ballRay.getPointB();
                canvas.drawLine((float) ballPosition.getX(), (float) ballPosition.getY(), (float) ballNewPosition.getX(), (float) ballNewPosition.getY(), p);
            }
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

        Wall testWall = new Wall(70, 0, 70, 100);
        this.gameElements.add(left);
        this.gameElements.add(right);
        this.gameElements.add(top);
        this.gameElements.add(bottom);
        this.gameElements.add(testWall);

        // before map works set up ball statically
        balls.add(new Ball(getAbsoluteLocation(new Vector(5, 10)), new Vector(1f, 1.1f)));
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

        // todo collision detection with other balls
    }

    private void collisionDetectionBalls(ConcurrentHashMap<Ball, Float> ballsThatCanStillMove) {
        for (ConcurrentHashMap.Entry<Ball, Float> entry : ballsThatCanStillMove.entrySet()) {
            Ball ball = entry.getKey();
            float timeRemaining = entry.getValue();
            ConcurrentLinkedQueue<Line> ballRays = getBallRays(timeRemaining, ball);

//            boolean didCollisionHappen = false;

            // todo please refractor. we have a hashmap and a variable. maybe make an object
            HashMap<GameObject, Vector> collisionPoints = new HashMap<>();
            Vector newBallPositionAfterCollision = null;

            for (Line ballRay : ballRays) {
                for (GameObject gameObject : this.gameElements) {
//                    double timeToMove = 0;
                    Vector intersectionPoint = null;
                    if (gameObject instanceof RectGameObject) {
                        RectGameObject rectGameObject = (RectGameObject) gameObject;
                        intersectionPoint = collisionDetectionRect(timeRemaining, rectGameObject.getAbsoluteRectangle(), gameObject, ballRay, ball);
                    } else if (gameObject instanceof Wall) {
                        Wall wall = (Wall) gameObject;
                        intersectionPoint = collisionDetectionLine(timeRemaining, wall.getAbsolute(), gameObject, ballRay, ball);
                    } else if (gameObject instanceof DrawingLine) {
                        DrawingLine drawingLine = (DrawingLine) gameObject;
                        intersectionPoint = collisionDetectionDrawingLine(timeRemaining, drawingLine, ballRay, ball);
                    }
                    // now check if there was a collision
                    if (intersectionPoint != null && intersectionPoint.getX() < Double.MAX_VALUE && intersectionPoint.getY() < Double.MAX_VALUE) {
                        boolean useThisCollisionPoint = true;

                        if (collisionPoints.get(gameObject) != null) {
                            Vector otherCollisionPoint = collisionPoints.get(gameObject);
                            // check lengths
                            Vector oldCollisionPointToBallDistance = otherCollisionPoint.subtract(ball.getAbsolutePosition());
                            Vector newCollisionPointToBallDistance = intersectionPoint.subtract(ball.getAbsolutePosition());
                            if (oldCollisionPointToBallDistance.getLength() < newCollisionPointToBallDistance.getLength()) {
                                useThisCollisionPoint = false;
                            }
                        }

                        if (useThisCollisionPoint) {
                            // we only move the ball with its center --> that means we only set the collision point as the point where to move the ball to
                            // so we have to calculate the position to which the ball can move his center

                            // we project the vector (ballContactPoint -> intersectionPoint) to the ballRayCenter

                            Vector ballPosition = ball.getAbsoluteContactPoint();
                            Vector projectionVector = intersectionPoint.subtract(ballPosition);
                            Vector ballNewPosition = reachablePointWithinTime(ballPosition, ball.getAbsoluteDirectionVector(), timeRemaining);
                            Vector ballRayVector = ballNewPosition.subtract(ballPosition);

                            Vector newDirection = projectionVector.projectOnto(ballRayVector);
//                            collisionPoints.put(gameObject, ballPosition.add(newDirection));
                            newBallPositionAfterCollision = ballPosition.add(newDirection);
                            collisionPoints.put(gameObject, intersectionPoint);
//                            double ballRayLength = ballRayVector.getLength();
//                            Vector newEndPoint = ballRayVector.getUnitVector().multiplyWithScalar(ballRayLength - ball.getAbsoluteRadius() * 1.5);
//                            Vector newLocation = ballPosition.add(newEndPoint);
//                            collisionPoints.put(gameObject, newLocation);
                        }
                    }
                }
            }
            if (collisionPoints.isEmpty()) {
                // no collision --> the ball can move as he wants to so we move it
                Vector ballPosition = ball.getAbsoluteContactPoint();
                Vector ballNewPosition = reachablePointWithinTime(ballPosition, ball.getAbsoluteDirectionVector(), timeRemaining);
                moveGameObjectToLocation(ball, ballNewPosition, timeRemaining, ball.getAbsoluteDirectionVector());
                timeRemaining = 0;
            } else {
                Log.d("gm", "colliding with num elements: " + collisionPoints.size());
                if (collisionPoints.size() == 1) {
                    ConcurrentHashMap.Entry<GameObject, Vector> collisionEntry = collisionPoints.entrySet().iterator().next();
                    GameObject gameObject = collisionEntry.getKey();
                    Vector intersectionPoint = collisionEntry.getValue();
                    // there was an intersection
                    gameObject.onHit(ball);
                    // now return the time it took to move the ball there
                    double timeToMove = moveGameObjectToLocation(ball, newBallPositionAfterCollision, timeRemaining, ball.getAbsoluteDirectionVector());

                    // change the angle of the ball
                    Log.d("gm", "ball should rotate now");
                    Vector newVector = new Vector(intersectionPoint.getX() - ball.getAbsolutePosition().getX(), ball.getAbsolutePosition().getY() - intersectionPoint.getY());
                    Log.d("gm", "new Vector: " + newVector.toString());
                    double angle = Math.atan2(newVector.getY(), newVector.getX()); // first y, then x
                    Log.d("gm", "angle " + angle);

                    if ((angle > Math.PI / 4 && angle < Math.PI * 3 / 4) || (angle < -Math.PI / 4 && angle > -Math.PI * 3 / 4)) {
                        ball.flipDirectionY();
                        Log.d("gm", "flipping Y");
                    } else {
                        ball.flipDirectionX();
                        Log.d("gm", "flipping X");
                    }

                    Log.d("gm", "collision with wall or brick");
                    timeRemaining -= timeToMove;

                } else {
                    // more than one objects get hit by our ball rays
                    // we have to find a place to move the ball to which touches all collision points
                    // todo !!!
                    Log.e("gm", "ahhhhhhh");
                    timeRemaining = 0;
                }

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

    private Vector collisionDetectionDrawingLine(double timeRemaining, DrawingLine drawingLine, Line ballRay, Ball ball) {
        Vector lastPos = null;
        for (Vector point : drawingLine.getPositions()) {
            if (lastPos != null) {
                Line currLine = new Line(lastPos, point);
                Vector intersectionPoint = collisionDetectionLine(timeRemaining, currLine, drawingLine, ballRay, ball);
                if (intersectionPoint.getX() < Double.MAX_VALUE && intersectionPoint.getY() < Double.MAX_VALUE) {
                    return intersectionPoint;
                }
            }
            lastPos = point;
        }
        return new Vector(Double.MAX_VALUE, Double.MAX_VALUE);
    }

    /**
     * @param ballRay
     * @return the time it took to move the ball if it was moved or if we didn't move then 0
     */
    private Vector collisionDetectionRect(double timeRemaining, Rect gameObjectRect, GameObject gameObject, Line ballRay, Ball ball) {
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
            Vector intersectionPoint = collisionDetectionLine(timeRemaining, rectLine, gameObject, ballRay, ball);
            if (intersectionPoint.getX() < Double.MAX_VALUE && intersectionPoint.getY() < Double.MAX_VALUE) {
                return intersectionPoint;
            }
        }
        return new Vector(Double.MAX_VALUE, Double.MAX_VALUE);
    }

    private Vector collisionDetectionLine(double timeRemaining, Line gameObjectLine, GameObject gameObject, Line ballRay, Ball ball) {
        Vector vPointIntersection = ballRay.intersectWithOtherLine(gameObjectLine);
        return vPointIntersection;
    }

    /* moving */


    private ConcurrentLinkedQueue<Line> getBallRays(float timePassed, Ball ball) {
        // todo there's a lot of room for optimization in these calculations
        ConcurrentLinkedQueue<Line> ballRays = new ConcurrentLinkedQueue<>();
        // get the line of the ball
        Vector ballPosition = ball.getAbsoluteContactPoint();
        Vector ballNewPosition = reachablePointWithinTime(ballPosition, ball.getAbsoluteDirectionVector(), timePassed);
        Line ballRayCenter = new Line(ballPosition, ballNewPosition);

        Vector vContactPointToCenter = ball.getAbsolutePosition().subtract(ball.getAbsoluteContactPoint());
        Vector cCenterToContactPoint = ball.getAbsoluteContactPoint().subtract(ball.getAbsolutePosition());
        Vector ballModifierLeftRadius = vContactPointToCenter.getLeftNormal();
        Vector ballModifierRightRadius = vContactPointToCenter.getRightNormal();

        Vector ballNewPositionLeftRadius = ballNewPosition.add(ballModifierLeftRadius).subtract(cCenterToContactPoint);
        Vector ballNewPositionRightRadius = ballNewPosition.add(ballModifierRightRadius).subtract(cCenterToContactPoint);

        ballRays.add(new Line(ball.getAbsolutePosition().add(ballModifierLeftRadius), ballNewPositionLeftRadius));
        ballRays.add(new Line(ball.getAbsolutePosition().add(ballModifierRightRadius), ballNewPositionRightRadius));
        ballRays.add(ballRayCenter);

        return ballRays;
    }

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
        } else {
            vPointGameObjectPosition = gameObject.getAbsolutePosition();
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
//        if (gameObject instanceof Ball) {
//            Ball ball = (Ball) gameObject;
//            ball.moveToAbsolutePosition(vPointToMoveTo);
//        } else if (gameObject instanceof RectGameObject) {
//            RectGameObject rectGameObject = (RectGameObject) gameObject;
//            rectGameObject.setAbsoluteX((int) vPointToMoveTo.getX());
//            rectGameObject.setAbsoluteY((int) vPointToMoveTo.getY());
//        }
        gameObject.moveToAbsoluteLocation(vPointToMoveTo);

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
        for (GameObject gameObject : this.gameElements) {
            if (gameObject instanceof DrawingLine) {
                DrawingLine drawingLine = (DrawingLine) gameObject;
                if (drawingLine.isDead()) {
                    this.gameElements.remove(drawingLine);
                }
            }
        }
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

    public double getAbsoluteX(int gridX) {
        return displayUnitX * gridX;
    }

    public double getAbsoluteY(int gridY) {
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

        Log.e("gm", "resize everything!!!");
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
