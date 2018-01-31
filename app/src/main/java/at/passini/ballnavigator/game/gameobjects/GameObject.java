package at.passini.ballnavigator.game.gameobjects;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by xeniu on 29.01.2018.
 */

public abstract class GameObject implements DrawableObject {
    protected Rect rBox;
    protected int rotation;
    protected boolean isDestructable;

    public GameObject() {
        this.rotation = 0;
        this.isDestructable = false;
        this.rBox = new Rect(0, 0, 0, 0);
    }

    public GameObject(Rect rect) {
        this();
        this.rBox.left = rect.left;
        this.rBox.top = rect.top;
        this.rBox.right = rect.right;
        this.rBox.bottom = rect.bottom;
    }

    public GameObject(int x, int y, int width, int height) {
        this();
        this.rBox.left = x;
        this.rBox.top = y;
        this.rBox.right = x + width;
        this.rBox.bottom = x + height;
    }

    public void onHit(Ball ball) {
        // subclass can implement this
    }

    public void onHit(Ball ball){
        // subclass shoudl implement this
    }

    public boolean isColliding(Rect rCollider) {
        return this.rBox.left < rCollider.left && this.rBox.right > rCollider.right
                && this.rBox.top < rCollider.top && this.rBox.bottom > rCollider.bottom;
    }

    public int getPosX() {
        return this.rBox.left;
    }

    public int getPosY() {
        return this.rBox.top;
    }

    public int getWidth() {
        return this.rBox.right - this.rBox.left;
    }

    public int getHeight() {
        return this.rBox.bottom - this.rBox.top;
    }

    public void setPosX(int posX) {
        this.rBox.left = posX;
    }

    public void setPosY(int posY) {
        this.rBox.top = posY;
    }

    public Rect getRectangle() {
        return rBox;
    }
}
