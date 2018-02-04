package at.passini.ballnavigator.game.gameobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by xeniu on 29.01.2018.
 */

public class Brick extends RectGameObject {
    private int hp;

    private Paint pColor;

    public Brick(Rect rect, int hp) {
        super(rect);
        this.isDestructable = true;
        this.hp = hp;
        pColor = new Paint();
        pColor.setColor(Color.MAGENTA);
        pColor.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDrawUpdate(Canvas canvas, long timePassed) {
        canvas.drawRect(this.rBoxAbsolute, pColor);
    }

    @Override
    public void onHit(Ball ball) {
        this.hp -= 1;
    }

    public int getHp() {
        return hp;
    }
}
