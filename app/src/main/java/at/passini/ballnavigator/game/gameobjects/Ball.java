package at.passini.ballnavigator.game.gameobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

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

        this.moveTo(posX, posY);

        pColor = new Paint();
        pColor.setColor(Color.DKGRAY);
        pColor.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDrawUpdate(Canvas canvas, long timePassed) {
        canvas.drawOval(this.rBox.left, this.rBox.top, this.rBox.right, this.rBox.bottom, this.pColor);
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

    public void moveTo(int posX, int posY) {
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
}
