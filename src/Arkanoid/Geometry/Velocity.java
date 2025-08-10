package Arkanoid.Geometry;

import java.util.Random;

/**
 * represents the change in x,y position of an object.
 */
public class Velocity {
    private final double dx;
    private final double dy;

    /**
     * represents the change in x,y position of an object.
     *
     * @param dx the position change in the x-axis
     * @param dy the position change in the y-axis
     */
    public Velocity(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * creates a new velocity given with an angle and speed, we calculate each axis speed using the formula:
     * v_x = v * cos(angle).
     * v_y = v * sin(angle).
     *
     * @param angle speed's angle
     * @param speed the speed
     * @return new velocity based on the values passed to the method
     */
    public static Velocity fromAngleAndSpeed(double angle, double speed) {
        int corrector = 90;
        double radAngle = Math.toRadians(angle - corrector);
        double dx = speed * Math.cos(radAngle), dy = speed * Math.sin(radAngle);
        return new Velocity(dx, dy);
    }

    /**
     * @return random velocity
     */
    public static Velocity createRandomVelocity() {
        Random rand = new Random();
        int maxAngle = 360, speed = 5;
        int angle = rand.nextInt(maxAngle);
        return Velocity.fromAngleAndSpeed(angle, speed);
    }

    /**
     * gets the dx value.
     *
     * @return dx
     */
    public double getDx() {
        return this.dx;
    }

    /**
     * gets the dy value.
     *
     * @return dy
     */
    public double getDy() {
        return this.dy;
    }

    /**
     * changes the position of a point based on its velocity.
     *
     * @param p the point
     * @return new point with updated position
     */
    public Point applyToPoint(Point p) {
        return new Point(p.getX() + dx, p.getY() + dy);
    }
}