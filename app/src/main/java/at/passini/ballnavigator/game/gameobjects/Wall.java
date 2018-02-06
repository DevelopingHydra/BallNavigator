package at.passini.ballnavigator.game.gameobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import at.passini.ballnavigator.game.GameManager;
import at.passini.ballnavigator.game.Helper.Line;
import at.passini.ballnavigator.game.Helper.Vector;

/**
 * Created by xeniu on 31.01.2018.
 */

public class Wall extends LineGameObject {

    public Wall(int x, int y, int x2, int y2) {
        super(x, y, x2, y2);
    }

    @Override
    public void onHit(Ball ball) {

    }

}
