package at.passini.ballnavigator.game.gameobjects;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by xeniu on 29.01.2018.
 */

public class Brick extends GameObject {
    private int hp;

    public Brick(Rect rect, int hp) {
        super(rect);
        this.isDestructable = true;
        this.hp = hp;
    }

    @Override
    public void onDrawUpdate(Canvas canvas, long timePassed) {
    }

    public void hit(int strength) {
        this.hp -= strength;
    }

    public int getHp() {
        return hp;
    }
}
