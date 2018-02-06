package at.passini.ballnavigator;

import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;

import at.passini.ballnavigator.game.Helper.Vector;

/**
 * Created by xeniu on 06.02.2018.
 */

public class VectorTest {
    @Test
    public void testProjection() {
        // https://www.geogebra.org/m/zeaqX2oz
        LinkedList<Vector> vectorFrom = new LinkedList<>();
        LinkedList<Vector> vectorProjectTo = new LinkedList<>();
        LinkedList<Vector> expectedNewVector = new LinkedList<>();

        vectorFrom.add(new Vector(3, 3));
        vectorProjectTo.add(new Vector(6, 0));
        expectedNewVector.add(new Vector(3, 0));

        vectorFrom.add(new Vector(5, 4));
        vectorProjectTo.add(new Vector(6, 0));
        expectedNewVector.add(new Vector(5, 0));

        vectorFrom.add(new Vector(7, 4));
        vectorProjectTo.add(new Vector(6, -4));
        expectedNewVector.add(new Vector(3, -2));

        vectorFrom.add(new Vector(-2, 10));
        vectorProjectTo.add(new Vector(6, -4));
        expectedNewVector.add(new Vector(-6, 4));

        for (int i = 0; i < vectorFrom.size(); i++) {
            Vector vFrom = vectorFrom.get(i);
            Vector vTo = vectorProjectTo.get(i);
            Vector expected = expectedNewVector.get(i);

            Vector result = vFrom.projectOnto(vTo);

            Assert.assertEquals(expected.getX(), result.getX(), 0.001);
            Assert.assertEquals(expected.getY(), result.getY(), 0.001);
        }
    }
}
