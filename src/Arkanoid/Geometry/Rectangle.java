package Arkanoid.Geometry;

import java.util.ArrayList;

import Arkanoid.Sprites.Ball;

/**
 * represents a rectangle with a given color.
 */
public class Rectangle {
    private final Point upperLeft;
    private final double width;
    private final double height;

    /**
     * class constructor.
     *
     * @param upperLeft rectangle's upper left point
     * @param width     rectangle's width
     * @param height    rectangle's height
     */
    public Rectangle(Point upperLeft, double width, double height) {
        this.upperLeft = upperLeft;
        this.width = width;
        this.height = height;
    }

    /**
     * provides safe access to the rectangle's width.
     *
     * @return rectangle's width
     */
    public double getWidth() {
        return this.width;
    }

    /**
     * provides safe access to the rectangle's height.
     *
     * @return rectangle's height
     */
    public double getHeight() {
        return this.height;
    }

    /**
     * provides safe access to the rectangle's upper left point.
     *
     * @return copy of the rectangle's upper left point
     */
    public Point getUpperLeft() {
        return new Point(this.upperLeft.getX(), this.upperLeft.getY());
    }

    /**
     * creates the lower right point of the rectangle.
     *
     * @return the lower right point of the rectangle
     */
    public Point getLowerRight() {
        double x = upperLeft.getX() + getWidth(), y = upperLeft.getY() + getHeight();
        return new Point(x, y);
    }

    /**
     * creates the lower left point of the rectangle.
     *
     * @return the lower left point of the rectangle
     */
    public Point getLowerLeft() {
        double x = upperLeft.getX(), y = upperLeft.getY() + getHeight();
        return new Point(x, y);
    }

    /**
     * creates the down left point of the rectangle.
     *
     * @return the down left point of the rectangle
     */
    public Point getUpperRight() {
        double x = upperLeft.getX() + getWidth(), y = upperLeft.getY();
        return new Point(x, y);
    }

    /**
     * gets the minimal x value inside the rectangle.
     *
     * @return minimal x value inside the rectangle
     */
    public double getLowXBound() {
        return upperLeft.getX();
    }

    /**
     * gets the maximal x value inside the rectangle.
     *
     * @return maximal x value inside the rectangle
     */
    public double getHighXBound() {
        return getLowerRight().getX();
    }

    /**
     * gets the minimal y value inside the rectangle.
     *
     * @return minimal y value inside the rectangle
     */
    public double getLowYBound() {
        return getUpperRight().getY();
    }

    /**
     * gets the maximal y value inside the rectangle.
     *
     * @return maximal y value inside the rectangle
     */
    public double getHighYBound() {
        return getLowerLeft().getY();
    }

    /**
     * creates an array which contains the rectangle's vertices.
     *
     * @return array of the rectangle's vertices
     */
    public Line[] vertices() {
        Line leftVert = new Line(getUpperLeft(), getLowerLeft());
        Line rightVert = new Line(getUpperRight(), getLowerRight());
        Line upperVert = new Line(getUpperLeft(), getUpperRight());
        Line lowerVert = new Line(getLowerLeft(), getLowerRight());
        return new Line[]{rightVert, leftVert, upperVert, lowerVert};
    }

    /**
     * checks if a ball is partially inside the rectangle.
     *
     * @param ball the ball
     * @return true if the point is inside, false otherwise
     */
    public boolean isBallInRectangle(Ball ball) {
        //determines the minimal x and y values of points inside the rectangle
        double xStart = getLowXBound(), yStart = getLowYBound();
        //determines the maximal x and y values of points inside the rectangle
        double xEnd = getHighXBound(), yEnd = getHighYBound();
        //checks if the given point coordinates are inside the rectangle
        return ((ball.getX() + ball.getSize() >= xStart && ball.getX() - ball.getSize() <= xEnd)
                && (ball.getY() + ball.getSize() >= yStart && ball.getY() - ball.getSize() <= yEnd));
    }

    /**
     * creates a list containing the rectangle intersection points with a given line.
     *
     * @param line the given line
     * @return intersection points list
     */
    public java.util.List<Point> intersectionPoints(Line line) {
        int numVert = 4;
        Line[] vertices = vertices();
        java.util.List<Point> intersections = new ArrayList<>();
        Point intersection;
        /*
         *loop finds the intersection point of the line with one of the rectangle's vertices,
         *and adds it to the intersections list only if the number of intersections is finite/isn't 0
         */
        for (int i = 0; i < numVert; i++) {
            intersection = line.intersectionWith(vertices[i]);
            if (intersection != null) {
                intersections.add(intersection);
            }
        }
        return intersections;
    }
}