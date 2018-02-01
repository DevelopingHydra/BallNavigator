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

    @Test
    public void testBallContactPoint() {
        Vector vDirectionAbsolute = new Vector(10, 10);
        Vector ballCenter = new Vector(5, 5);
        int radius = 2;

        Vector direction = vDirectionAbsolute.subtract(ballCenter);
        Vector uDirection = direction.getUnitVector();
        Vector contactPoint = ballCenter.add(uDirection.multiplyWithScalar(radius));

        Vector expected = new Vector(6.41, 6.41);

        Assert.assertEquals(expected.getX(), contactPoint.getX(), 0.1);
        Assert.assertEquals(expected.getY(), contactPoint.getY(), 0.1);
    }

    @Test
    public void ballMoveTo() {
        Vector ballCenter = new Vector(40, 75);
        Vector contactPoint = new Vector(30.077, 57.6351);
        Vector targetPosition = new Vector(22.0599, 44.5019);

        Vector diff = ballCenter.subtract(contactPoint); // order matters!

        // now we calculate the reachable position
        Vector newPos = targetPosition.add(diff);

        Vector expectedNewPos = new Vector(32.04, 61.3);

        Assert.assertEquals(expectedNewPos.getX(), newPos.getX(), 1);
        Assert.assertEquals(expectedNewPos.getY(), newPos.getY(), 1);
    }
}
