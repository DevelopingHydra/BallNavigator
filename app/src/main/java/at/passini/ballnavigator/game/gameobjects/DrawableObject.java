package at.passini.ballnavigator.game.gameobjects;

import android.graphics.Canvas;

/**
 * Created by xeniu on 29.01.2018.
 */

public interface DrawableObject {
    void onDrawUpdate(Canvas canvas, long timePassed);
}
