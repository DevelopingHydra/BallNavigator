package at.passini.ballnavigator.game.Helper;

/**
 * Created by xeniu on 31.01.2018.
 */

public class Line {
    private Vector vPointA, vPointB;
    private boolean lineSegment;

    public Line(Vector vPointA, Vector vPointB) {
        this(vPointA, vPointB, true);
    }

    public Line(Vector vPointA, Vector vPointB, boolean isLineSegment) {
        this.vPointA = vPointA;
        this.vPointB = vPointB;
        this.lineSegment = isLineSegment;
    }

    public Vector intersectWithOtherLine(Line otherLine) {
        // from https://www.geeksforgeeks.org/program-for-point-of-intersection-of-two-lines/

        // Line AB represented as a1x + b1y = c1
        double a1 = vPointB.getY() - vPointA.getY();
        double b1 = vPointA.getX() - vPointB.getX();
        double c1 = a1 * (vPointA.getX()) + b1 * (vPointA.getY());

        // Line CD represented as a2x + b2y = c2
        Vector vPointC = otherLine.vPointA;
        Vector vPointD = otherLine.vPointB;
        double a2 = vPointD.getY() - vPointC.getY();
        double b2 = vPointC.getX() - vPointD.getX();
        double c2 = a2 * (vPointC.getX()) + b2 * (vPointC.getY());

        double determinant = a1 * b2 - a2 * b1;

        boolean doesIntersect = false;

        double x = (b2 * c1 - b1 * c2) / determinant;
        double y = (a1 * c2 - a2 * c1) / determinant;
        Vector vPointIntersection = new Vector(x, y);


        if (determinant != 0) {
            // there is an intersection somewhere

            // now we check if we have line segments
            // if so we have to check further before we can decide if there in intersection
            if (this.isLineSegment() || otherLine.isLineSegment()) {
                if (this.lineSegment) {
                    double pIntersectionX = vPointIntersection.getX();
                    double pIntersectionY = vPointIntersection.getY();

                    double pLeft = this.vPointA.getX() < this.vPointB.getX() ? this.vPointA.getX() : this.vPointB.getX();
                    double pRight = this.vPointA.getX() > this.vPointB.getX() ? this.vPointA.getX() : this.vPointB.getX();
                    double pTop = this.vPointA.getY() < this.vPointB.getY() ? this.vPointA.getY() : this.vPointB.getY();
                    double pBottom = this.vPointA.getY() > this.vPointB.getY() ? this.vPointA.getY() : this.vPointB.getY();

                    if (pIntersectionX > pLeft && pIntersectionX < pRight && pIntersectionY > pTop && pIntersectionY < pBottom) {
                        doesIntersect = true;
                    }
                }
                if (otherLine.isLineSegment()) {
                    double pIntersectionX = vPointIntersection.getX();
                    double pIntersectionY = vPointIntersection.getY();

                    double pLeft = otherLine.getPointA().getX() < otherLine.getPointB().getX() ? otherLine.getPointA().getX() : otherLine.getPointB().getX();
                    double pRight = otherLine.getPointA().getX() > otherLine.getPointB().getX() ? otherLine.getPointA().getX() : otherLine.getPointB().getX();
                    double pTop = otherLine.getPointA().getY() < otherLine.getPointB().getY() ? otherLine.getPointA().getY() : otherLine.getPointB().getY();
                    double pBottom = otherLine.getPointA().getY() > otherLine.getPointB().getY() ? otherLine.getPointA().getY() : otherLine.getPointB().getY();

                    if (pIntersectionX > pLeft && pIntersectionX < pRight && pIntersectionY > pTop && pIntersectionY < pBottom) {
                        doesIntersect = true;
                    }
                }
            } else {
                doesIntersect = true;
            }

        }

        if (doesIntersect) {
            return vPointIntersection;
        } else {
            // The lines are parallel if the determinant is 0
            // or we handle line segment and they don't intersect
            return new Vector(Double.MAX_VALUE, Double.MAX_VALUE);
        }
    }

    public Vector getPointA() {
        return vPointA;
    }

    public void setPointA(Vector vPointA) {
        this.vPointA = vPointA;
    }

    public Vector getPointB() {
        return vPointB;
    }

    public void setPointB(Vector vPointB) {
        this.vPointB = vPointB;
    }

    public boolean isLineSegment() {
        return lineSegment;
    }

    public void setLineSegment(boolean lineSegment) {
        this.lineSegment = lineSegment;
    }
}
