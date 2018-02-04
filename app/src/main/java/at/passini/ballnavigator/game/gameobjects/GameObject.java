package at.passini.ballnavigator.game.gameobjects;

/**
 * Created by xeniu on 29.01.2018.
 */

public abstract class GameObject implements DrawableObject {
    protected int rotation;
    protected boolean isDestructable;

    public void onHit(Ball ball) {
        // subclass can implement this
    }

}
