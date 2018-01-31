package at.passini.ballnavigator.game.gameobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import at.passini.ballnavigator.game.Helper.Vector;

/**
 * Created by xeniu on 29.01.2018.
 */

public class Ball extends GameObject {

    private Vector vDirection;
    private final int radius = 20;

    private Paint pColor;

    public Ball(int posX, int posY, Vector vDirection) {
        this.vDirection = vDirection;

        this.moveTo(new Vector(posX, posY));

        pColor = new Paint();
        pColor.setColor(Color.DKGRAY);
        pColor.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDrawUpdate(Canvas canvas, long timePassed) {
        canvas.drawOval(this.rBox.left, this.rBox.top, this.rBox.right, this.rBox.bottom, this.pColor);
        Log.d("ball", "drawing at " + this.rBox.toShortString());
    }

    public int getRadius() {
        return this.getWidth() / 2;
    }

    public double getDirectionX() {
        return vDirection.getX();
    }

    public double getDirectionY() {
        return vDirection.getY();
    }

    public boolean isMovingUp() {
        return this.vDirection.getY() < 0;
    }

    public boolean isMovingDown() {
        return this.vDirection.getY() > 0;
    }

    public boolean isMovingLeft() {
        return this.vDirection.getX() < 0;
    }

    public boolean isMovingRight() {
        return this.vDirection.getX() > 0;
    }

    public void flipDirectionX() {
        this.vDirection.setX(-this.vDirection.getX());
    }

    public void flipDirectionY() {
        this.vDirection.setY(-this.vDirection.getY());
    }

    public void moveTo(Vector vPointNewPosition) {
        int posX = (int) vPointNewPosition.getX();
        int posY = (int) vPointNewPosition.getY();

        this.rBox.left = posX;
        this.rBox.top = posY;
        this.rBox.right = this.rBox.left + this.radius * 2;
        this.rBox.bottom = this.rBox.top + this.radius * 2;
    }

    @Override
    public int getPosX() {
        return this.rBox.centerX();
    }

    @Override
    public int getPosY() {
        return this.rBox.centerY();
    }

    public void setDirectionVector(Vector vDirection) {
        this.vDirection = vDirection;
    }

    public Vector getDirectionVector() {
        return vDirection;
    }

    /**
     * todo make the correct calculation
     *
     * @return
     */
    public Vector getContactPoint() {
        // should calculate to point that would touch another object
        return new Vector(this.rBox.centerX(), this.rBox.centerY());
    }
}
