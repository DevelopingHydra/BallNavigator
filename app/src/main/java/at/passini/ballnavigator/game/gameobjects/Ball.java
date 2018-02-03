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

    public Ball(Vector vPosition, Vector vDirectionGrid) {
        setDirectionGrid(vDirectionGrid);

        radius = 20;// absolute value! should be called before setting the absolutePosition()
        this.setGridPosition(vPosition);

        pColor = new Paint();
        pColor.setColor(Color.DKGRAY);
        pColor.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDrawUpdate(Canvas canvas, long timePassed) {
        canvas.drawOval(rBoxAbsolute.left, rBoxAbsolute.top, rBoxAbsolute.right, rBoxAbsolute.bottom, this.pColor);
        Log.d("ball", "drawing at " + rBoxAbsolute.toShortString() + " with speed: " + this.vDirectionAbsolute.toString());
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


    public void moveToAbsolutePosition(Vector targetPosition){
        // first we calculate the vector from the ball center to the contactPoint
        Vector ballCenter = new Vector(getAbsoluteX(),getAbsoluteY());
        Vector contactPoint=this.getAbsoluteContactPoint();
        Vector diff=ballCenter.subtract(contactPoint); // order matters!

        // now we calculate the reachable position
        Vector newPos=targetPosition.add(diff);

        // now we move the ball to the location
        this.setAbsolutePosition(newPos);
    }

    private void setAbsolutePosition(Vector vPointNewPosition) {
        double posX =  vPointNewPosition.getX() - this.radius;
        double posY =  vPointNewPosition.getY() - this.radius;
        double posRight = posX + this.radius;
        double posBottom = posY + this.radius;

        setAbsoluteX(posX);
        setAbsoluteY(posY);
        setAbsoluteRight(posRight);
        setAbsoluteBottom(posBottom);
    }


    private void setGridPosition(Vector gridPosition) {
        // we have a hard time to sset the radius using grid values
        // therefore we translate them to absolute values and then set everything
        Vector absolutePosition=GameManager.getInstance().getAbsoluteLocation(gridPosition);
        setAbsolutePosition(absolutePosition);
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
        Vector ballPosition = new Vector(this.getAbsoluteX(), this.getAbsoluteY());
        Vector uDirection = ballPosition.getUnitVector();
        return ballPosition.add(uDirection.multiplyWithScalar(radius));
    }

}
