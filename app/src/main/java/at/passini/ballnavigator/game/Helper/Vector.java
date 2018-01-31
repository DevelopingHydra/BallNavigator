package at.passini.ballnavigator.game.Helper;

/**
 * Created by xeniu on 29.01.2018.
 */

public class Vector {
    private double x;
    private double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector add(Vector v) {
        return new Vector(x + v.getX(), y + v.getY());
    }

    public Vector subtract(Vector v) {
        return new Vector(x - v.getX(), y - v.getY());
    }

    public Vector getUnitVector() {
        double a = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        if (a == 0) {
            a = 1;
        }
        return new Vector(x / a, y / a);
    }

    public Vector multiplyWithScalar(double number) {
        return new Vector(x * number, y * number);
    }

    public double getLength() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public double getDistanceTo(Vector vPoint) {
        Vector vToPoint = vPoint.subtract(this);
        return vToPoint.getLength();
    }

    /* getter and setter */

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Vector{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
