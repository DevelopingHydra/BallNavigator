package at.passini.ballnavigator.game.gameobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.LinkedList;

import at.passini.ballnavigator.game.Helper.Vector;

/**
 * Created by xeniu on 29.01.2018.
 */

public class DrawingLine implements DrawableObject {
    private LinkedList<Vector> positions;

    private Paint paintLine;

    public DrawingLine() {
        this.positions = new LinkedList<>();

        paintLine = new Paint();
        paintLine.setColor(Color.WHITE);
        paintLine.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void addPosition(Vector vPosition) {
        this.positions.add(vPosition);
    }

    @Override
    public void onDrawUpdate(Canvas canvas, long timePassed) {
        // draw Polygon
        Vector lastPoint = null;
        for (Vector point : positions) {
            if (lastPoint != null) {
                canvas.drawLine((float) lastPoint.getX(), (float) lastPoint.getY(), (float) point.getX(), (float) point.getY(), paintLine);
                lastPoint = point;
            } else {
                lastPoint = point;
            }
        }
    }
}
