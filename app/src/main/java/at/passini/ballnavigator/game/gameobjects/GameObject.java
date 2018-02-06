package at.passini.ballnavigator.game.gameobjects;

import at.passini.ballnavigator.game.Helper.Vector;

/**
 * Created by xeniu on 29.01.2018.
 */

public abstract class GameObject implements DrawableObject {
    protected int rotation;
    protected boolean isDestructable;

    public void onHit(Ball ball) {
        // subclass can implement this
    }
    public void moveToAbsoluteLocation(Vector newPosition){
        // subclass can implement this
    }
    public Vector getAbsolutePosition(){
        return null;
    }

}
