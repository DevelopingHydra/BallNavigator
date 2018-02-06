package at.passini.ballnavigator.game.gameobjects;

import at.passini.ballnavigator.game.Helper.Vector;

/**
 * Created by xeniu on 29.01.2018.
 */

public abstract class GameObject implements DrawableObject {
    protected int rotation;
    protected boolean destructable;

    public GameObject() {
        rotation = 0;
        destructable = false;
    }

    public abstract void onHit(Ball ball);

    public abstract void moveToAbsoluteLocation(Vector newPosition);

    public abstract Vector getAbsolutePosition();

    public abstract void resizeAbsolute();
}
