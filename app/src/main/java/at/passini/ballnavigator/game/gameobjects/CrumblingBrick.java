package at.passini.ballnavigator.game.gameobjects;

import android.graphics.Canvas;

/**
 * Created by xeniu on 29.01.2018.
 */

public class CrumblingBrick extends GameObject {
    private int hp;

    public CrumblingBrick(int hp) {
        super();
        this.isDestructable = true;
        this.hp = hp;
    }

    @Override
    public void onDrawUpdate(Canvas canvas, long timePassed) {
        super.onDrawUpdate(canvas, timePassed);

    }

    public void hit(int strength) {
        this.hp -= strength;
    }

    public int getHp() {
        return hp;
    }
}
