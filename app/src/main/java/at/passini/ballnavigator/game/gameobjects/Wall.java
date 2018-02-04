package at.passini.ballnavigator.game.gameobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import at.passini.ballnavigator.game.GameManager;
import at.passini.ballnavigator.game.Helper.Line;
import at.passini.ballnavigator.game.Helper.Vector;

/**
 * Created by xeniu on 31.01.2018.
 */

public class Wall extends GameObject implements DrawableObject {

    private Paint pColor;
    private Line lAbsolute, lGrid;

    public Wall(int x, int y, int x2, int y2) {
        pColor = new Paint();
        pColor.setColor(Color.DKGRAY);
        pColor.setStyle(Paint.Style.FILL);


        Vector pointA = new Vector(x, y);
        Vector pointB = new Vector( x2,  y2);

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

    public Line getlGrid() {
        return lGrid;
    }
}
