package at.passini.ballnavigator;

import org.junit.Assert;
import org.junit.Test;

import at.passini.ballnavigator.game.GameManager;
import at.passini.ballnavigator.game.Helper.Vector;

/**
 * Created by xeniu on 31.01.2018.
 */

public class CDTest {
    @Test
    public void timeNeededToMoveTo() {
        double distance = 100;
        Vector v = new Vector(10, 10);
        double timeNeeded = GameManager.getInstance().timeNeededToMoveDistance(distance, v);

        Assert.assertEquals(7.07, timeNeeded, 0.01);
    }
}
