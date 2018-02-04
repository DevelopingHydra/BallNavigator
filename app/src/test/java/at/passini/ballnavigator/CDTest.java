package at.passini.ballnavigator;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

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
        ArrayList<Vector> vDirectoinAbsoluteValues = new ArrayList<>();
        ArrayList<Vector> ballCenterValues = new ArrayList<>();
        ArrayList<Integer> ballRadiusValues = new ArrayList<>();
        ArrayList<Vector> expectedPoints = new ArrayList<>();

        vDirectoinAbsoluteValues.add(new Vector(10, 10));
        ballCenterValues.add(new Vector(5, 5));
        ballRadiusValues.add(2);
        expectedPoints.add(new Vector(6.41, 6.41));

        vDirectoinAbsoluteValues.add(new Vector(10, -10));
        ballCenterValues.add(new Vector(5, 5));
        ballRadiusValues.add(2);
        expectedPoints.add(new Vector(6.41, 3.59));


        vDirectoinAbsoluteValues.add(new Vector(-14, 20));
        ballCenterValues.add(new Vector(9, 5));
        ballRadiusValues.add(3);
        expectedPoints.add(new Vector(7.28, 7.46));

        vDirectoinAbsoluteValues.add(new Vector(-4, -4));
        ballCenterValues.add(new Vector(4, 5));
        ballRadiusValues.add(1);
        expectedPoints.add(new Vector(3.29, 4.29));

        vDirectoinAbsoluteValues.add(new Vector(.001, -.001));
        ballCenterValues.add(new Vector(60, 105));
        ballRadiusValues.add(20);
        expectedPoints.add(new Vector(74.14, 90.86));


        if (vDirectoinAbsoluteValues.size() != ballCenterValues.size() || ballCenterValues.size() != ballRadiusValues.size() || ballRadiusValues.size() != expectedPoints.size()) {
            Assert.fail();
        }

        for (int i = 0; i < vDirectoinAbsoluteValues.size(); i++) {

            Vector vDirectionAbsolute = vDirectoinAbsoluteValues.get(i);
            Vector uDirection = vDirectionAbsolute.getUnitVector();
            Vector ballCenter = ballCenterValues.get(i);
            int radius = ballRadiusValues.get(i);
            Vector contactPoint = ballCenter.add(uDirection.multiplyWithScalar(radius));

            Assert.assertEquals(expectedPoints.get(i).getX(), contactPoint.getX(), 0.01);
            Assert.assertEquals(expectedPoints.get(i).getY(), contactPoint.getY(), 0.01);
        }

    }


    @Test
    public void ballMoveTo() {
        ArrayList<Vector> ballCenterValues = new ArrayList<>();
        ArrayList<Vector> contactPoints = new ArrayList<>();
        ArrayList<Vector> targetPositions = new ArrayList<>();
        ArrayList<Vector> expectedNewPosition = new ArrayList<>();

        ballCenterValues.add(new Vector(40, 75));
        contactPoints.add(new Vector(30.077, 57.6351));
        targetPositions.add(new Vector(22.0599, 44.5019));
        expectedNewPosition.add(new Vector(31.98, 61.87));

        ballCenterValues.add(new Vector(40, 12));
        contactPoints.add(new Vector(19, 20));
        targetPositions.add(new Vector(-52.8, -19.2));
        expectedNewPosition.add(new Vector(-31.8, -27.2));

        if (ballCenterValues.size() != contactPoints.size() || contactPoints.size() != targetPositions.size() || targetPositions.size() != expectedNewPosition.size()) {
            Assert.fail();
        }

        for (int i = 0; i < ballCenterValues.size(); i++) {
            Vector ballCenter = ballCenterValues.get(i);
            Vector contactPoint = contactPoints.get(i);
            Vector targetPosition = targetPositions.get(i);
            Vector expectedNewPos = expectedNewPosition.get(i);

            Vector diff = ballCenter.subtract(contactPoint); // order matters!

            Vector newPos = targetPosition.add(diff);

            Assert.assertEquals(expectedNewPos.getX(), newPos.getX(), 0.01);
            Assert.assertEquals(expectedNewPos.getY(), newPos.getY(), 0.01);
        }
    }
}
