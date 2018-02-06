package at.passini.ballnavigator.game.gameobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import at.passini.ballnavigator.game.Helper.Vector;

/**
 * Created by xeniu on 29.01.2018.
 */

public class Brick extends RectGameObject {
    private int hp;

    public Brick(Rect rect, int hp) {
        super(rect);
        this.destructable = true;
        this.hp = hp;
    }

    @Override
    public void onHit(Ball ball) {
        this.hp -= 1;
    }

    public int getHp() {
        return hp;
    }
}
