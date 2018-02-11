package at.passini.ballnavigator.game.gameobjects;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by xeniu on 08.02.2018.
 */

public class NonDrawableArea extends RectGameObject {
    public NonDrawableArea(int gridX, int gridY, int gridRight, int gridBottom) {
        super(gridX, gridY, gridRight, gridBottom);

        pColor.setColor(Color.parseColor("#80FF0000"));

        this.collidable = false;
    }

    @Override
    public void onHit(Ball ball) {

    }

}
