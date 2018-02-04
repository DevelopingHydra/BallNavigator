package at.passini.ballnavigator.game.gameobjects;

import android.graphics.Canvas;
import android.graphics.Rect;

import at.passini.ballnavigator.game.GameManager;
import at.passini.ballnavigator.game.Helper.Vector;

/**
 * Created by Benedikt on 04.02.2018.
 */

public class RectGameObject extends GameObject {
    protected Rect rBoxAbsolute, rBoxGrid;

    public RectGameObject() {
        this.rotation = 0;
        this.isDestructable = false;
        this.rBoxAbsolute = new Rect(0, 0, 0, 0);
        this.rBoxGrid = new Rect(0, 0, 0, 0);
    }

    public RectGameObject(Rect gridRect) {
        this();
        this.rBoxGrid.left = gridRect.left;
        this.rBoxGrid.top = gridRect.top;
        this.rBoxGrid.right = gridRect.right;
        this.rBoxGrid.bottom = gridRect.bottom;
    }


    public RectGameObject(int gridX, int gridY, int gridRight, int gridBottom) {
        this();
        setGridX(gridX);
        setGridY(gridY);
        setGridBottom(gridBottom);
        setGridRight(gridRight);
    }

    @Override
    public void onDrawUpdate(Canvas canvas, long timePassed) {

    }

    public boolean isAbsolutePointWithinObject(Vector vPoint) {
        double posX = vPoint.getX();
        double posY = vPoint.getY();
        return rBoxAbsolute.left < posX && rBoxAbsolute.right > posX
                && rBoxAbsolute.top < posY && rBoxAbsolute.bottom > posY;
    }

    public int getAbsoluteX() {
        return this.rBoxAbsolute.left;
    }

    public int getAbsoluteY() {
        return this.rBoxAbsolute.top;
    }

    public int getAbsoluteRight() {
        return this.rBoxAbsolute.right;
    }

    public int getAbsoluteBottom() {
        return this.rBoxAbsolute.bottom;
    }

    public void setAbsoluteX(double posX) {
        this.rBoxAbsolute.left = (int) posX;
        this.rBoxGrid.left = GameManager.getInstance().getGridX(posX);
    }

    public void setAbsoluteY(double posY) {
        this.rBoxAbsolute.top = (int) posY;
        this.rBoxGrid.top = GameManager.getInstance().getGridY(posY);
    }

    public void setAbsoluteRight(double posRight) {
        this.rBoxAbsolute.right = (int) posRight;
        this.rBoxGrid.right = GameManager.getInstance().getGridX(posRight);
    }

    public void setAbsoluteBottom(double posBottom) {
        this.rBoxAbsolute.bottom = (int) posBottom;
        this.rBoxGrid.bottom = GameManager.getInstance().getGridY(posBottom);
    }

    public Rect getAbsoluteRectangle() {
        return rBoxAbsolute;
    }

    public int getGridX() {
        return this.rBoxGrid.left;
    }

    public int getGridY() {
        return this.rBoxGrid.top;
    }

    public int getGridRight() {
        return this.rBoxGrid.right;
    }

    public int getGridBottom() {
        return this.rBoxGrid.bottom;
    }

    public void setGridX(int posX) {
        this.rBoxGrid.left = posX;
        this.rBoxAbsolute.left = (int) GameManager.getInstance().getAbsoluteX(posX);
    }

    public void setGridY(int posY) {
        this.rBoxGrid.top = posY;
        this.rBoxAbsolute.top = (int) GameManager.getInstance().getAbsoluteY(posY);
    }

    public void setGridRight(int posRight) {
        this.rBoxGrid.right = posRight;
        this.rBoxAbsolute.right = (int) GameManager.getInstance().getAbsoluteX(posRight);
    }

    public void setGridBottom(int posBottom) {
        this.rBoxGrid.bottom = posBottom;
        this.rBoxAbsolute.bottom = (int) GameManager.getInstance().getAbsoluteY(posBottom);
    }

    public Rect getGridRectangle() {
        return rBoxGrid;
    }


}
