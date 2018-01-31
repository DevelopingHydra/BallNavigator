package at.passini.ballnavigator.game.gameobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import at.passini.ballnavigator.game.GameManager;
import at.passini.ballnavigator.game.Helper.Vector;

/**
 * Created by xeniu on 29.01.2018.
 */

public class Ball extends GameObject {

    private Vector vDirectionAbsolute, vDirectionGrid;
    private int radius;

    private Paint pColor;

    public Ball(int gridX, int gridY, Vector vDirectionGrid) {
        setvDirectionGrid(vDirectionGrid);

        this.moveTo(new Vector(gridX, gridY));
        radius = GameManager.getInstance().getGridX(20);

        pColor = new Paint();
        pColor.setColor(Color.DKGRAY);
        pColor.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDrawUpdate(Canvas canvas, long timePassed) {
        Rect absoluteRect = GameManager.getInstance().getAbsoluteRect(this.rBoxAbsolute);
        canvas.drawOval(absoluteRect.left, absoluteRect.top, absoluteRect.right, absoluteRect.bottom, this.pColor);
        Log.d("ball", "drawing at " + absoluteRect.toShortString());
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

    public void moveTo(Vector vPointNewPosition) {
        int posX = (int) vPointNewPosition.getX();
        int posY = (int) vPointNewPosition.getY();

        this.rBoxAbsolute.left = posX;
        this.rBoxAbsolute.top = posY;
        this.rBoxAbsolute.right = this.rBoxAbsolute.left + this.radius * 2;
        this.rBoxAbsolute.bottom = this.rBoxAbsolute.top + this.radius * 2;
    }

    @Override
    public int getAbsoluteX() {
        return this.rBoxAbsolute.centerX();
    }

    @Override
    public int getAbsoluteY() {
        return this.rBoxAbsolute.centerY();
    }

    @Override
    public int getGridX() {
        return this.rBoxGrid.centerX();
    }

    @Override
    public int getGridY() {
        return this.rBoxGrid.centerY();
    }

    public Vector getAbsoluteDirectionVector() {
        return vDirectionAbsolute;
    }

    public void setvDirectionAbsolute(Vector vDirectionAbsolute) {
        this.vDirectionAbsolute = vDirectionAbsolute;
        vDirectionGrid = GameManager.getInstance().getGridLocation(vDirectionAbsolute);
    }

    public void setvDirectionGrid(Vector vDirectionGrid) {
        this.vDirectionGrid = vDirectionGrid;
        vDirectionAbsolute = GameManager.getInstance().getAbsoluteLocation(vDirectionGrid);
    }

    /**
     * todo make the correct calculation
     *
     * @return
     */
    public Vector getAbsoluteContactPoint() {
        // should calculate to point that would touch another object
        return new Vector(this.rBoxAbsolute.centerX(), this.rBoxAbsolute.centerY());
    }
}
