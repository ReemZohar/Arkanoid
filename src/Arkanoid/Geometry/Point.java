package Arkanoid.Geometry;

import java.util.Random;

import Arkanoid.Utils.Operations;

/**
 * Point object class, represents (x , y) coordinates.
 */
public class Point {
    private final double x;
    private final double y;

    /**
     * class constructor.
     *
     * @param x chosen x axis coordinates
     * @param y chosen y axis coordinates
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * calculates the distance between 2 points using the distance between 2 points formula.
     *
     * @param other the point we'll measure the distance to
     * @return distance between 2 points
     */
    public double distance(Point other) {
        double x1 = this.x, x2 = other.getX(), y1 = this.y, y2 = other.getY();
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    /**
     * checks if 2 points are identical.
     *
     * @param other the point we'll compare our point to
     * @return true if equal, false otherwise
     */
    public boolean equals(Point other) {
        return (Operations.doubleThreshold(this.getX(), other.getX()))
                && (Operations.doubleThreshold(this.getY(), other.getY()));
    }

    /**
     * used to access the x field through the class itself.
     *
     * @return x axis coordinates
     */
    public double getX() {
        return this.x;
    }

    /**
     * used to access the y field through the class itself.
     *
     * @return y axis coordinates
     */
    public double getY() {
        return this.y;
    }

    /**
     * creates a random point in a given rectangle.
     *
     * @param rec the rectangle
     * @param r   radius
     * @return random point in the range
     */
    public static Point createRandPoint(Rectangle rec, int r) {
        Random rand = new Random();
        //we add the radius to avoid having balls which a part of them is out of bounds
        int x = rand.nextInt((int) rec.getHighXBound()) + (r + (int) rec.getLowXBound());
        int y = rand.nextInt((int) rec.getHighYBound()) + (r + (int) rec.getLowYBound());
        return new Point(x, y);
    }
}