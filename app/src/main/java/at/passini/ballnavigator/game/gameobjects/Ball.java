package at.passini.ballnavigator.game.gameobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import at.passini.ballnavigator.game.Helper.Vector;

/**
 * Created by xeniu on 29.01.2018.
 */

public class Ball extends GameObject implements DrawableObject {

    private Vector vDirectionAbsolute, vAbsolutePosition;
    private int radius;

    private Paint pColor;

    public Ball(Vector vPosition, Vector vDirectionAbsolute) {
        this.vDirectionAbsolute = vDirectionAbsolute;

        radius = 20;// absolute value! should be called before setting the absolutePosition()
        this.vAbsolutePosition = vPosition;

        pColor = new Paint();
        pColor.setColor(Color.DKGRAY);
        pColor.setStrokeWidth(5);
        pColor.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void onDrawUpdate(Canvas canvas, long timePassed) {
        float left = (float) (vAbsolutePosition.getX() - radius);
        float top = (float) (vAbsolutePosition.getY() - radius);
        float right = (float) (vAbsolutePosition.getX() + radius);
        float bottom = (float) (vAbsolutePosition.getY() + radius);
        canvas.drawOval(left, top, right, bottom, this.pColor);
        Log.d("ball", "drawing at " + this.vAbsolutePosition.toString() + " with speed: " + this.vDirectionAbsolute.toString() + " and " + timePassed + " ms have passed");
    }

    @Override
    public void onHit(Ball ball) {
        // well :)
    }

    @Override
    public void moveToAbsoluteLocation(Vector targetPosition) {
        if (isAbsolutePointWithinObject(targetPosition)) {
            this.vAbsolutePosition = targetPosition;
        } else {
            // first we calculate the vector from the ball center to the contactPoint
            Vector ballCenter = this.vAbsolutePosition;
            Vector contactPoint = this.getAbsoluteContactPoint();
            Vector diff = ballCenter.subtract(contactPoint); // order matters!

            // now we calculate the reachable position
            Vector newPos = targetPosition.add(diff);

            // now we move the ball to the location
            this.vAbsolutePosition = newPos;
        }
    }

    public boolean isAbsolutePointWithinObject(Vector vPoint) {
        Vector ballCenter = this.vAbsolutePosition;
        Vector vTargetToBallPos = ballCenter.subtract(vPoint);
        return vTargetToBallPos.getLength() <= radius;
    }

    public boolean isMovingUp() {
        return this.vDirectionAbsolute.getY() < 0;
    }

    public boolean isMovingDown() {
        return this.vDirectionAbsolute.getY() > 0;
    }

    public boolean isMovingLeft() {
        return this.vDirectionAbsolute.getX() < 0;
    }

    public boolean isMovingRight() {
        return this.vDirectionAbsolute.getX() > 0;
    }

    public void flipDirectionX() {
        this.vDirectionAbsolute.setX(-this.vDirectionAbsolute.getX());
    }

    public void flipDirectionY() {
        this.vDirectionAbsolute.setY(-this.vDirectionAbsolute.getY());
    }


    private void setvAbsolutePosition(Vector vAbsolutePosition) {
        this.vAbsolutePosition = vAbsolutePosition;
    }

    public Vector getAbsolutePosition() {
        return vAbsolutePosition;
    }

    @Override
    public void resizeAbsolute() {
        // todo
    }

    public Vector getAbsoluteDirectionVector() {
        return vDirectionAbsolute;
    }

    public void setDirectionAbsolute(Vector vDirectionAbsolute) {
        this.vDirectionAbsolute = vDirectionAbsolute;
    }

    public int getAbsoluteRadius() {
        return radius;
    }

    /**
     * @return
     */
    public Vector getAbsoluteContactPoint() {
        // should calculate to point that would touch another object
        // tip: take a vector from location to direction. then make a unit vector out of it. then multiply that by the radius
        Vector uDirection = vDirectionAbsolute.getUnitVector();
        Vector ballCenter = this.vAbsolutePosition;
        return ballCenter.add(uDirection.multiplyWithScalar(radius));
    }

    @Override
    public String toString() {
        return "Ball{" +
                "vDirectionAbsolute=" + vDirectionAbsolute +
                ", vAbsolutePosition=" + vAbsolutePosition +
                ", radius=" + radius +
                '}';
    }
}
