package Arkanoid.Geometry;

import Arkanoid.Utils.Operations;

/**
 * represent a line object which is built from 2 point objects.
 */
public class Line {
    private final Point start;
    private final Point end;

    /**
     * constructing the line using 2 points.
     *
     * @param start start point
     * @param end   end point
     */
    public Line(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    /**
     * constructs the line by first using 2 sets of (x , y) coordinates to build 2 points,
     * and then setting them as the line fields.
     *
     * @param x1 first point x coordinates
     * @param y1 first point y coordinates
     * @param x2 second point x coordinates
     * @param y2 second point y coordinates
     */
    public Line(double x1, double y1, double x2, double y2) {
        Point p1 = new Point(x1, y1);
        Point p2 = new Point(x2, y2);
        this.start = p1;
        this.end = p2;
    }

    /**
     * calculates the line's length.
     *
     * @return line's length
     */
    public double length() {
        double d1 = this.start.distance(end), d2 = this.end.distance(start);
        int smallestPos = 0;
        if (d1 >= smallestPos) {
            return d1;
        }
        return d2;
    }

    /**
     * creates a copy of the line's start point.
     *
     * @return copy of the line's start point
     */
    public Point start() {
        return new Point(this.start.getX(), this.start.getY());
    }

    /**
     * creates a copy of the line's end point.
     *
     * @return copy of the line's end point
     */
    public Point end() {
        return new Point(this.end.getX(), this.end.getY());
    }

    /**
     * checks if 2 lines are intersecting, and then checks if their intersection point is on our current line.
     *
     * @param other the other line
     * @return true if one line is contained in the other, or if the intersection point is on both lines,
     * false if lines are parallel.
     */
    public boolean isIntersecting(Line other) {
        int regular = 1, vertical = 2, horizontal = 3;
        double m1 = this.gradient(), b1 = this.axisInterception();
        double m2 = other.gradient(), b2 = other.axisInterception();
        double hor = 0;
        //two horizontal lines scenario
        if ((Operations.doubleThreshold(m1, hor)) && (Operations.doubleThreshold(m2, hor))) {
            //same y scenario
            if (Operations.doubleThreshold(this.start().getY(), other.start().getY())) {
                return other.isOnLine(this.start(), horizontal) || other.isOnLine(this.end(), horizontal);
            }
            return false;
        }
        //one or more vertical lines scenario
        if (this.isVertical() || other.isVertical()) {
            //two vertical lines scenario
            if (this.isVertical() && other.isVertical()) {
                if (Operations.doubleThreshold(this.start().getX(), other.start().getX())) {
                    return other.isOnLine(this.start(), vertical) || other.isOnLine(this.end(), vertical);
                }
                return false;
            }
            //first line is vertical scenario
            if (this.isVertical()) {
                double xIntersection = this.start().getX();
                double yIntersection = other.findIntersectionY(xIntersection);
                Point intersection = new Point(xIntersection, yIntersection);
                return this.isOnLine(intersection, vertical) && other.isOnLine(intersection, regular);
            } else { // other line is vertical scenario
                double xIntersection = other.start().getX();
                double yIntersection = this.findIntersectionY(xIntersection);
                Point intersection = new Point(xIntersection, yIntersection);
                return this.isOnLine(intersection, regular) && other.isOnLine(intersection, vertical);
            }
        }
        //infinite intersection points scenario
        if ((Operations.doubleThreshold(m1, m2)) && (Operations.doubleThreshold(b1, b2))) {
            return true;
        } else if (Operations.doubleThreshold(m1, m2)) { //parallel lines scenario
            return false;
        }
        double xIntersection = this.findIntersectionX(other);
        double yIntersection = this.findIntersectionY(xIntersection);
        //intersection point
        Point intersection = new Point(xIntersection, yIntersection);
        return (this.isOnLine(intersection, regular)) && (other.isOnLine(intersection, regular));
    }

    /**
     * if 2 lines intersect, we create a new point for their intersection.
     *
     * @param other the second line
     * @return null if there are infinite/zero intersection points,
     * or returns the intersection point if it exists on both lines.
     */
    public Point intersectionWith(Line other) {
        double hor = 0;
        int vertical = 2, horizontal = 3, contained = 4;
        //parallel lines scenario
        if (!this.isIntersecting(other)) {
            return null;
        }
        //two identical lines scenario
        if (this.equals(other)) {
            //both line represent the same point in the axis system scenario
            if (this.isPoint() && other.isPoint()) {
                return this.start();
            }
            return null;
        }
        //one line is represented as a point in the axis system and the point is on the second line scenario
        if (this.isPoint() || other.isPoint()) {
            if (this.isPoint()) {
                return this.start();
            } else {
                return other.start();
            }
        }
        //one line is vertical scenario
        if (this.isVertical() || other.isVertical()) {
            //both lines are vertical scenario
            if (this.isVertical() && other.isVertical()) {
                //one of the lines is contained in the other scenario
                if (this.isContainedExists(other, vertical)) {
                    return null;
                } else {
                    if ((this.start().equals(other.start())) || (this.start().equals(this.end()))) {
                        return this.start();
                    } else {
                        return this.end();
                    }
                }
            }
            //first line is vertical, second line is regular scenario
            if (this.isVertical()) {
                double intersectionX = this.start().getX();
                double intersectionY = other.findIntersectionY(intersectionX);
                return new Point(intersectionX, intersectionY);
            } else { //second line is vertical scenario
                double intersectionX = other.start().getX();
                double intersectionY = this.findIntersectionY(intersectionX);
                return new Point(intersectionX, intersectionY);
            }
        }
        double m1 = this.gradient(), m2 = other.gradient(), b1 = this.axisInterception(), b2 = other.axisInterception();
        //same line equation scenario
        if ((Operations.doubleThreshold(m1, m2)) && (Operations.doubleThreshold(b1, b2))) {
            //both lines are horizontal scenario
            if ((Operations.doubleThreshold(m1, hor)) && (Operations.doubleThreshold(m2, hor))) {
                //infinite intersection points scenario
                if (this.isContainedExists(other, horizontal)) {
                    return null;
                }
            }
            //infinite intersection points scenario
            if (this.isContainedExists(other, contained)) {
                return null;
            }
            //one intersection point scenario
            if (this.start().equals(other.start())) {
                return this.start();
            }
            //one intersection point scenario
            if (this.start().equals(other.end())) {
                return this.start();
            }
            /*we'll return the end point if the start isn't the same as the others,
            since we know there's 1 intersection, and it's not with the start point*/
            return this.end();
        }
        double intersectionX = this.findIntersectionX(other);
        double intersectionY = this.findIntersectionY(intersectionX);
        //intersection point
        return new Point(intersectionX, intersectionY);
    }

    /**
     * checks if two lines are identical.
     *
     * @param other the line we'll compare our current line to
     * @return true if lines are equal, false otherwise
     */
    public boolean equals(Line other) {
        //start points and end points are equal scenario
        boolean scenario1 = (this.start().equals(other.start())) && (this.end().equals(other.end()));
        /*
        this object start point is the same as other point end point,
        and this object end point is the same as the other point start point scenario
        */
        boolean scenario2 = (this.end().equals(other.start())) && (this.start().equals(other.end()));
        return scenario1 || scenario2;
    }

    /**
     * calculates the gradient of our line using the formula: m = (y2 - y1) / (x2 - x1).
     *
     * @return the lines gradient
     */
    public double gradient() {
        //y2 - y1
        double yDiff = this.end().getY() - this.start().getY();
        //x2 - x1
        double xDiff = this.end().getX() - this.start().getX();
        double noDiff = 0;
        //line is vertical scenario, avoids dividing by zero
        if (Operations.doubleThreshold(xDiff, noDiff)) {
            return Double.POSITIVE_INFINITY;
        }
        return yDiff / xDiff;
    }

    /**
     * calculates b in the straight line equation y = mx + b.
     *
     * @return b (y-axis interception) or -x if the line is vertical
     */
    private double axisInterception() {
        double y = this.start().getY(), x = this.start().getX(), m = this.gradient();
        double opposite = -this.start().getX();
        /*
         * vertical line scenario,
         * in this scenario we'd like to return the opposite number to x,
         * to later make our line equation calculations correct
         */
        if (Operations.doubleThreshold(this.start().getX(), this.end().getX())) {
            return opposite;
        }
        //y = mx + b -> b = y - mx
        return y - m * x;
    }

    /**
     * takes a point and a scenario and checks if it's a point on our current line, based on the scenario.
     *
     * @param p        point
     * @param scenario determines which type of line we're dealing with.
     *                 the scenario will always be regular,
     *                 unless we try to check if a point is in the line but not equal to its start or end point.
     * @return true if the point is on the line, false otherwise
     */
    public boolean isOnLine(Point p, int scenario) {
        int regular = 1, vertical = 2, horizontal = 3, contained = 4;
        //minimal x, y values of the line
        double xStart = Math.min(this.start().getX(), this.end().getX());
        double yStart = Math.min(this.start().getY(), this.end().getY());
        //maximal x, y values of the line
        double xEnd = Math.max(this.start().getX(), this.end().getX());
        double yEnd = Math.max(this.start().getY(), this.end().getY());
        //the coordinates of the point
        double x = p.getX(), y = p.getY();
        boolean xInRange = (Operations.doubleCompareThreshold(x, xStart))
                && (Operations.doubleCompareThreshold(xEnd, x));
        boolean yInRange = (Operations.doubleCompareThreshold(y, yStart))
                && (Operations.doubleCompareThreshold(yEnd, y));

        if (scenario == regular) {
            //checks if the points x and y are in the lines range, and can be also be equal to the lines start/end point
            return (xInRange) && (yInRange);
        }
        if (scenario == vertical) {
            //checks if the points y coordinates are in the lines range
            return (yInRange) && (Operations.doubleThreshold(x, xStart));
        }
        if (scenario == horizontal) {
            //checks if the points x coordinates are in the lines range
            return (xInRange) && (Operations.doubleThreshold(y, yStart));
        }
        if (scenario == contained) {
            //checks if the points x and y are in the lines range
            return ((x > xStart) && (x < xEnd)) && ((y > yStart) && (y < yEnd));
        }
        //scenario doesn't match one of the above, never happens
        return false;
    }

    /**
     * if one of the start or end points is on the other line we'll return true.
     *
     * @param other    the second line
     * @param scenario determines which type of line we're dealing with
     * @return true if one of the line's start or end points is on the second line, but not equal to one of them,
     * and false otherwise
     */
    private boolean isContainedExists(Line other, int scenario) {
        return this.isOnLine(other.start(), scenario) || this.isOnLine(other.end(), scenario)
                || other.isOnLine(this.start(), scenario) || other.isOnLine(this.end(), scenario);
    }

    /**
     * finds the x coordinates of the intersection point of two not parallel lines (therefore m1 - m2 != 0).
     * we evaluated the equation m1x + b1 = m2x + b2 to x = (b2 - b1) / (m1 - m2).
     *
     * @param other second line which intersects with our current line equation (since m1 != m2)
     * @return intersection's x coordinates
     */
    public double findIntersectionX(Line other) {
        double m1 = this.gradient(), b1 = this.axisInterception();
        double m2 = other.gradient(), b2 = other.axisInterception();
        //x = (b2 - b1) / (m1 - m2)
        return (b2 - b1) / (m1 - m2);
    }

    /**
     * finds the y coordinates of 2 lines intersection using the formula y = mx + b.
     *
     * @param x x coordinates of the 2 lines intersection
     * @return y intersection point coordinates
     */
    public double findIntersectionY(double x) {
        double m = this.gradient(), b = this.axisInterception();
        //y = mx + b
        return m * x + b;
    }

    /**
     * checks if a line is vertical by comparing the x coordinates of the start point and the end point.
     *
     * @return true if vertical, false otherwise
     */
    public boolean isVertical() {
        return Operations.doubleThreshold(this.start().getX(), this.end().getX());
    }

    /**
     * checks if a line is made of 2 points which are the same ones.
     *
     * @return true if start and end are identical, false otherwise
     */
    public boolean isPoint() {
        //known size
        int pointLength = 0;
        if (Operations.doubleThreshold(this.length(), pointLength)) {
            return true;
        }
        return this.start().equals(this.end());
    }

    /**
     * finds which intersection point of the line and the given rectangle is the closest to the line's start point.
     *
     * @param rect the given rectangle
     * @return the closest intersection point to the line's start,
     * or null if there are no intersections/infinite intersections.
     */
    public Point closestIntersectionToStartOfLine(Rectangle rect) {
        int firstPointIndex = 0, secondPointIndex = 1;
        java.util.List<Point> intersections = rect.intersectionPoints(new Line(start(), end()));
        Point closestPoint;
        //no intersection between the line and the rectangle scenario
        if (intersections.isEmpty()) {
            return null;
        }
        /*
         * list isn't empty scenario, therefore there is at least 1 intersection point.
         * we temporarily set the closest point to be the first intersection point on the list.
         */
        closestPoint = intersections.get(firstPointIndex);
        for (int i = secondPointIndex; i < intersections.size(); i++) {
            //condition is met if a closer intersection point was found.
            if (start().distance(intersections.get(i)) < start().distance(closestPoint)) {
                closestPoint = intersections.get(i);
            }
        }
        return closestPoint;
    }
}