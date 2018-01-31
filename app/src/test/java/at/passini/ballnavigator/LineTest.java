package at.passini.ballnavigator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;

import at.passini.ballnavigator.game.Helper.Line;
import at.passini.ballnavigator.game.Helper.Vector;

/**
 * Created by xeniu on 31.01.2018.
 */

public class LineTest {
    private Line lineA, lineB, lineC, lineD;

    @Before
    public void setupLines() {
        Vector vPointA = new Vector(2, 3);
        Vector vPointB = new Vector(8, 1);
        Vector vPointC = new Vector(3, 1);
        Vector vPointD = new Vector(9, 4);
        Vector vPointE = new Vector(6, 0);
        Vector vPointF = new Vector(18, 0);
        Vector vPointG = new Vector(3, 4);
        Vector vPointH = new Vector(9, 2);

        lineA = new Line(vPointA, vPointB);
        lineB = new Line(vPointC, vPointD);
        lineC = new Line(vPointE, vPointF);
        lineD = new Line(vPointG, vPointH);
    }

    @Test
    public void testIntersection() {
        // test intersection
        lineA.setLineSegment(false);
        lineB.setLineSegment(false);
        lineC.setLineSegment(false);
        lineD.setLineSegment(false);

        // AB intersect
        Vector vPointIntersect = lineA.intersectWithOtherLine(lineB);
        Vector idealIntersectionPoint = new Vector(5, 2);
        Assert.assertEquals(idealIntersectionPoint.getX(), vPointIntersect.getX(), 0.1);
        Assert.assertEquals(idealIntersectionPoint.getY(), vPointIntersect.getY(), 0.1);

        // BC intersect
        vPointIntersect = lineB.intersectWithOtherLine(lineC);
        idealIntersectionPoint = new Vector(1, 0);
        Assert.assertEquals(idealIntersectionPoint.getX(), vPointIntersect.getX(), 0.1);
        Assert.assertEquals(idealIntersectionPoint.getY(), vPointIntersect.getY(), 0.1);

        // AC intersect
        vPointIntersect = lineA.intersectWithOtherLine(lineC);
        idealIntersectionPoint = new Vector(11, 0);
        Assert.assertEquals(idealIntersectionPoint.getX(), vPointIntersect.getX(), 0.1);
        Assert.assertEquals(idealIntersectionPoint.getY(), vPointIntersect.getY(), 0.1);

        // AD are parallel so they do never intersect
        vPointIntersect = lineA.intersectWithOtherLine(lineD);
        idealIntersectionPoint = new Vector(Double.MAX_VALUE, Double.MAX_VALUE);
        Assert.assertEquals(idealIntersectionPoint.getX(), vPointIntersect.getX(), 0.1);
        Assert.assertEquals(idealIntersectionPoint.getY(), vPointIntersect.getY(), 0.1);
    }

    @Test
    public void testIntersectionAsSegments() {
        // test intersection
        lineA.setLineSegment(true);
        lineB.setLineSegment(true);
        lineC.setLineSegment(true);
        lineD.setLineSegment(true);

        // AB intersect
        Vector vPointIntersect = lineA.intersectWithOtherLine(lineB);
        Vector idealIntersectionPoint = new Vector(5, 2);
        Assert.assertEquals(idealIntersectionPoint.getX(), vPointIntersect.getX(), 0.1);
        Assert.assertEquals(idealIntersectionPoint.getY(), vPointIntersect.getY(), 0.1);

        // BC do not intersect
        vPointIntersect = lineB.intersectWithOtherLine(lineC);
        idealIntersectionPoint = new Vector(Double.MAX_VALUE, Double.MAX_VALUE);
        Assert.assertEquals(idealIntersectionPoint.getX(), vPointIntersect.getX(), 0.1);
        Assert.assertEquals(idealIntersectionPoint.getY(), vPointIntersect.getY(), 0.1);

        // AC do not intersect
        vPointIntersect = lineA.intersectWithOtherLine(lineC);
        idealIntersectionPoint = new Vector(Double.MAX_VALUE, Double.MAX_VALUE);
        Assert.assertEquals(idealIntersectionPoint.getX(), vPointIntersect.getX(), 0.1);
        Assert.assertEquals(idealIntersectionPoint.getY(), vPointIntersect.getY(), 0.1);

        // AD are parallel so they do never intersect
        vPointIntersect = lineA.intersectWithOtherLine(lineD);
        idealIntersectionPoint = new Vector(Double.MAX_VALUE, Double.MAX_VALUE);
        Assert.assertEquals(idealIntersectionPoint.getX(), vPointIntersect.getX(), 0.1);
        Assert.assertEquals(idealIntersectionPoint.getY(), vPointIntersect.getY(), 0.1);
    }

    @Test
    public void specialLineSegmentTest() {
        LinkedList<Line> ballRays = new LinkedList<>();
        LinkedList<Line> rectLines = new LinkedList<>();
        LinkedList<Boolean> expectedToIntersect = new LinkedList<>();


        // first ballRay
        // second rectLine

        ballRays.add(new Line(new Vector(69, 40), new Vector(6200, 10000)));
        rectLines.add(new Line(new Vector(10,1700),new Vector(0,1700)));
        expectedToIntersect.add(false);

        ballRays.add(new Line(new Vector(33,27), new Vector(-96000,-163831)));
        rectLines.add(new Line(new Vector(0, 0), new Vector(1000, 0)));
        expectedToIntersect.add(true);


        for (int i = 0; i < ballRays.size(); i++) {
            Line ballRay = ballRays.get(i);
            Line rectLine = rectLines.get(i);
            //Vector idealIntersectionPoint = new Vector(Double.MAX_VALUE, Double.MAX_VALUE);
            boolean shouldIntersect = expectedToIntersect.get(i);

            Vector vPointIntersect = ballRay.intersectWithOtherLine(rectLine);

            if (shouldIntersect) {
                Assert.assertTrue(vPointIntersect.getX() < Double.MAX_VALUE);
                Assert.assertTrue(vPointIntersect.getY() < Double.MAX_VALUE);
            } else {
                Assert.assertTrue(vPointIntersect.getX() == Double.MAX_VALUE);
                Assert.assertTrue(vPointIntersect.getY() == Double.MAX_VALUE);
            }

            vPointIntersect = rectLine.intersectWithOtherLine(ballRay);
            if (shouldIntersect) {
                Assert.assertTrue(vPointIntersect.getX() < Double.MAX_VALUE);
                Assert.assertTrue(vPointIntersect.getY() < Double.MAX_VALUE);
            } else {
                Assert.assertTrue(vPointIntersect.getX() == Double.MAX_VALUE);
                Assert.assertTrue(vPointIntersect.getY() == Double.MAX_VALUE);
            }
        }
    }
}
