package at.passini.ballnavigator.game.gameobjects;

/**
 * Created by Benedikt on 08.02.2018.
 */

public class Gem extends RectGameObject{

    private boolean collected=false;

    @Override
    public void onHit(Ball ball) {
        collected=true;
    }

    public boolean isCollected() {
        return collected;
    }
}
