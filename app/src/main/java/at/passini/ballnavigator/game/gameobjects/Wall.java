package at.passini.ballnavigator.game.gameobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import at.passini.ballnavigator.game.GameManager;

/**
 * Created by xeniu on 31.01.2018.
 */

public class Wall extends GameObject {

    private Paint pColor;

    public Wall(int x, int y, int width, int height) {
        super(x, y, width, height);
        pColor = new Paint();
        pColor.setColor(Color.DKGRAY);
        pColor.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDrawUpdate(Canvas canvas, long timePassed) {
        canvas.drawRect(this.rBoxAbsolute, pColor);
    }
}
