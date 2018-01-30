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
}
