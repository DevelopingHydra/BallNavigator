package at.passini.ballnavigator.game.gameobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
        setDirectionGrid(vDirectionGrid);

        radius = 20;// absolute value! should be called before setting the absolutePosition()
        this.setAbsolutePosition(new Vector(gridX, gridY));

        pColor = new Paint();
        pColor.setColor(Color.DKGRAY);
        pColor.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDrawUpdate(Canvas canvas, long timePassed) {
        canvas.drawOval(rBoxAbsolute.left, rBoxAbsolute.top, rBoxAbsolute.right, rBoxAbsolute.bottom, this.pColor);
        Log.d("ball", "drawing at " + rBoxAbsolute.toShortString()+" with speed: "+this.vDirectionAbsolute.toString());
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
        this.vDirectionGrid.setX(-this.vDirectionGrid.getX());
    }

    public void flipDirectionY() {
        this.vDirectionAbsolute.setY(-this.vDirectionAbsolute.getY());
        this.vDirectionGrid.setY(-this.vDirectionGrid.getY());
    }

    public void setAbsolutePosition(Vector vPointNewPosition) {
        int posX = (int) vPointNewPosition.getX();
        int posY = (int) vPointNewPosition.getY();
        int posRight = posX + this.radius * 2;
        int posBottom = posY + this.radius * 2;

        setAbsoluteX(posX);
        setAbsoluteY(posY);
        setAbsoluteRight(posRight);
        setAbsoluteBottom(posBottom);
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

    public void setDirectionAbsolute(Vector vDirectionAbsolute) {
        this.vDirectionAbsolute = vDirectionAbsolute;
        vDirectionGrid = GameManager.getInstance().getGridLocation(vDirectionAbsolute);
    }

    public void setDirectionGrid(Vector vDirectionGrid) {
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
        // tip: take a vector from location to direction. then make a unit vector out of it. then multiply that by the radius :)
        return new Vector(this.rBoxAbsolute.centerX(), this.rBoxAbsolute.centerY());
    }
}
