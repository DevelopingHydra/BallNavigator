package at.passini.ballnavigator.game.gameobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import at.passini.ballnavigator.game.GameManager;
import at.passini.ballnavigator.game.Helper.Line;
import at.passini.ballnavigator.game.Helper.Vector;

/**
 * Created by xeniu on 06.02.2018.
 */

public abstract class LineGameObject extends GameObject {

    protected Paint pColor;
    protected Line lAbsolute, lGrid;

    public LineGameObject(int x, int y, int x2, int y2) {
        super();
        pColor = new Paint();
        pColor.setColor(Color.DKGRAY);
        pColor.setStyle(Paint.Style.FILL);


        Vector pointA = new Vector(x, y);
        Vector pointB = new Vector(x2, y2);

        lAbsolute = new Line(new Vector(0, 0), new Vector(0, 0));
        lGrid = new Line(new Vector(0, 0), new Vector(0, 0));

        setGridPointA(pointA);
        setGridPointB(pointB);
    }

    @Override
    public void onDrawUpdate(Canvas canvas, long timePassed) {
        canvas.drawLine((float) lAbsolute.getPointA().getX(), (float) lAbsolute.getPointA().getY(),
                (float) lAbsolute.getPointB().getX(), (float) lAbsolute.getPointB().getY(), pColor);
    }

    @Override
    public void resizeAbsolute() {
        setGridPointA(this.lGrid.getPointA());
        setGridPointB(this.lGrid.getPointB());
    }

    @Override
    public void moveToAbsoluteLocation(Vector newPosition) {
        Vector vToMove = newPosition.subtract(lAbsolute.getPointA());
        setAbsolutePointA(lAbsolute.getPointA().add(vToMove));
        setAbsolutePointB(lAbsolute.getPointB().add(vToMove));
    }

    @Override
    public Vector getAbsolutePosition() {
        return this.lAbsolute.getPointA().add(this.lAbsolute.getPointB()).devideByScalar(.5);
    }

    public void setAbsolutePointA(Vector pointA) {
        this.lAbsolute.setPointA(pointA);
        this.lGrid.setPointA(GameManager.getInstance().getGridLocation(pointA));
    }

    public void setAbsolutePointB(Vector pointB) {
        this.lAbsolute.setPointB(pointB);
        this.lGrid.setPointB(GameManager.getInstance().getGridLocation(pointB));
    }

    public void setGridPointA(Vector pointA) {
        this.lGrid.setPointA(pointA);
        this.lAbsolute.setPointA(GameManager.getInstance().getAbsoluteLocation(pointA));
    }

    public void setGridPointB(Vector pointB) {
        this.lGrid.setPointB(pointB);
        this.lAbsolute.setPointB(GameManager.getInstance().getAbsoluteLocation(pointB));
    }

    public void setAbsoluteLine(Line l) {
        setAbsolutePointA(l.getPointA());
        setAbsolutePointB(l.getPointB());
    }

    public Line getAbsolute() {
        return lAbsolute;
    }

    public Line getGrid() {
        return lGrid;
    }
}
