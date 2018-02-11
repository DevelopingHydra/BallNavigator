package at.passini.ballnavigator.game.gameobjects;

import at.passini.ballnavigator.game.Helper.Vector;

/**
 * Created by xeniu on 29.01.2018.
 */

public abstract class GameObject implements DrawableObject {
    protected int rotation;
    protected boolean destructable, collidable;

    public GameObject() {
        rotation = 0;
        destructable = false;
        collidable = true;
    }

    public abstract void onHit(Ball ball);

    public abstract void moveToAbsoluteLocation(Vector newPosition);

    public abstract Vector getAbsolutePosition();

    public abstract boolean isAbsolutePointInside(Vector vPoint);

    /**
     * Screen size has changed and now we have to resize the absoute values
     */
    public abstract void resizeAbsolute();

    public boolean isCollidable() {
        return collidable;
    }

    public void setCollidable(boolean collidable) {
        this.collidable = collidable;
    }
}
