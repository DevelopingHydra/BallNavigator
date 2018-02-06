package at.passini.ballnavigator.game.gameobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.util.concurrent.ConcurrentLinkedQueue;

import at.passini.ballnavigator.game.GameManager;
import at.passini.ballnavigator.game.Helper.Vector;

/**
 * Created by xeniu on 29.01.2018.
 */

public class DrawingLine extends GameObject implements DrawableObject {
    private ConcurrentLinkedQueue<LinePoint> positions;

    private Paint paintLine;

    public DrawingLine(long timeWhenStarted) {
        this.positions = new ConcurrentLinkedQueue <>();

        paintLine = new Paint();
        paintLine.setColor(Color.DKGRAY);
        paintLine.setStyle(Paint.Style.FILL_AND_STROKE);
        paintLine.setStrokeWidth(10f);
    }

    public void addPosition(Vector vPosition) {
        this.positions.add(new LinePoint(vPosition));
    }

    @Override
    public void onDrawUpdate(Canvas canvas, long timePassed) {
        if (this.positions.size() > 0) {
            // draw Polygon
            Vector lastPoint = null;

            for (LinePoint point : positions) {
                point.onTimePassed(timePassed);
                if (point.isDead()) {
                    positions.remove(point);
                } else {
                    if (lastPoint != null) {
                        canvas.drawLine((float) lastPoint.getX(), (float) lastPoint.getY(), (float) point.getX(), (float) point.getY(), paintLine);
                        lastPoint = point;
                    } else {
                        lastPoint = point;
                    }
                }
            }
        }
    }

    public ConcurrentLinkedQueue<LinePoint> getPositions() {
        return positions;
    }

    public boolean isDead() {
        return this.positions.size() == 0;
    }

    @Override
    public void onHit(Ball ball) {

    }

    @Override
    public void moveToAbsoluteLocation(Vector newPosition) {
        throw new UnsupportedOperationException("a drawingLine cannot move");
    }

    @Override
    public Vector getAbsolutePosition() {
        throw new UnsupportedOperationException("a drawingLine has no position, it has multiple");
    }

    @Override
    public void resizeAbsolute() {
        // todo
    }
}

class LinePoint extends Vector {
    private final int timeTillDisapearance = 1000;
    private long timeLeft;

    public LinePoint(Vector v) {
        super(v.getX(), v.getY());
        this.timeLeft = timeTillDisapearance;
    }

    public long getTimeLeft() {
        return timeLeft;
    }

    public void onTimePassed(long timePassed) {
        this.timeLeft -= timePassed;
    }

    public boolean isDead() {
        return this.timeLeft < 0;
    }
}