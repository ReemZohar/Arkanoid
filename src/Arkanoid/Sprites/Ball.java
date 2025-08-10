package Arkanoid.Sprites;

import Arkanoid.GameAssets.GameEnvironment;
import Arkanoid.GameAssets.Game;

import Arkanoid.GameAssets.HitListener;
import Arkanoid.Geometry.Line;
import Arkanoid.Geometry.Point;
import Arkanoid.Geometry.Rectangle;
import Arkanoid.Geometry.Velocity;
import Arkanoid.Geometry.CollisionInfo;

import Arkanoid.Utils.Operations;

import biuoop.DrawSurface;

import java.awt.Color;

/**
 * represents a ball with certain color and speed based on it's size.
 */
public class Ball implements Sprite, HitListener {
    private Point center;
    private final int r;
    private Color color;
    private Velocity v = new Velocity(0, 0);
    private final GameEnvironment environment;

    /**
     * class constructor.
     *
     * @param center      ball's center
     * @param r           ball's radius
     * @param color       ball's color
     * @param environment ball's game environment
     */
    public Ball(Point center, int r, Color color, GameEnvironment environment) {
        this.center = center;
        this.r = r;
        this.color = color;
        this.environment = environment;
    }

    /**
     * gets the center point's x coordinates.
     *
     * @return center point x coordinates
     */
    public double getX() {
        return this.center.getX();
    }

    /**
     * gets the center point's y coordinates.
     *
     * @return center point y coordinates
     */
    public double getY() {
        return this.center.getY();
    }

    /**
     * gets the ball's radius.
     *
     * @return ball's radius
     */
    public int getSize() {
        return this.r;
    }

    /**
     * @return a copy of the ball's center point.
     */
    public Point getCenter() {
        return new Point(center.getX(), center.getY());
    }

    /**
     * @return a copy of the ball's color.
     */
    public Color getColor() {
        int r = color.getRed(), g = color.getGreen(), b = color.getBlue();
        return new Color(r, g, b);
    }

    /**
     * @return ball's current trajectory
     */
    public Line getTrajectory() {
        double x = center.getX(), y = center.getY();
        return new Line(getCenter(), new Point(x + v.getDx(), y + v.getDy()));
    }

    @Override
    public void drawOn(DrawSurface surface) {
        surface.setColor(color);
        surface.fillCircle((int) center.getX(), (int) center.getY(), r);
    }

    /**
     * sets the current ball's velocity to a new velocity.
     *
     * @param v new velocity
     */
    private void setVelocity(Velocity v) {
        this.v = v;
    }

    /**
     * gets the ball's current velocity.
     *
     * @return ball's velocity
     */
    public Velocity getVelocity() {
        return new Velocity(this.v.getDx(), this.v.getDy());
    }

    /**
     * checks if the ball's radius is too big compared to the given x or y range.
     *
     * @param bound max axis range
     * @param r     ball's radius
     * @return true if the radius is too big, false otherwise
     */
    public static boolean isRadiusBig(double bound, int r) {
        //by definition, circle's diameter = 2 * r
        int diameter = 2 * r;
        return diameter > bound;
    }

    /**
     * if a ball's radius is too big compared to the x or y range,
     * we change it to be the biggest radius possible within the given range.
     *
     * @param xBound x-axis max range
     * @param yBound y-axis max range
     * @param r      ball's radius
     * @return fixed radius if it was originally problematic,
     * original radius that was passed as a parameter otherwise
     */
    public static int fixRadius(double xBound, double yBound, int r) {
        //ball size is bigger than the given x-axis range scenario
        if (isRadiusBig(xBound, r)) {
            //we set the radius to fit exactly to the x-axis
            r = (int) xBound / 2;
        }
        //ball size is bigger than the given y-axis range scenario
        if (isRadiusBig(yBound, r)) {
            //we set the radius to fit exactly to the y-axis
            r = (int) yBound / 2;
        }
        return r;
    }

    @Override
    public void timePassed() {
        moveOneStep();
    }

    /**
     * adds the ball to the game's sprites collection.
     *
     * @param g the game's object
     */
    public void addToGame(Game g) {
        g.addSprite(this);
    }

    /**
     * moves the ball one step based on its velocity and possible collisions with other objects,
     * and alters its speed if it hits another object.
     */
    public void moveOneStep() {
        //screen size
        int width = 800, height = 600;
        Point screenUpperLeft = new Point(0, 0);
        Rectangle screen = new Rectangle(screenUpperLeft, width, height);
        Velocity newV;
        CollisionInfo collision = environment.getClosestCollision(getTrajectory());
        //collision exists scenario
        if (collision != null) {
            center = adjustCollPoint(collision.collisionPoint(), collision.collisionObject());
            //we change the ball's velocity based on the way it hit the obstacle
            newV = collision.collisionObject().hit(this, collision.collisionPoint(), getVelocity());
            setVelocity(newV);
        }
        if (isInBounds(screen)) {
            this.center = this.getVelocity().applyToPoint(this.center);
        }
    }

    /**
     * checks if the ball will hit one of the borders of a specific axis in its next move.
     *
     * @param lowBound   axis min range
     * @param highBound  axis max range
     * @param d          the change in position we'd like to apply on the next move
     * @param coordinate the current position on the axis
     * @return true if there's no hit, false otherwise
     */
    public boolean isInBound(double lowBound, double highBound, double d, double coordinate) {
        double newPos = coordinate + d;
        return (Operations.doubleCompareThreshold(newPos, getSize()))
                && (Operations.doubleCompareThreshold(highBound - getSize(), newPos))
                && (Operations.doubleCompareThreshold(newPos, lowBound + getSize()));
    }

    /**
     * checks if the ball will hit one of the rectangle borders in its next move.
     *
     * @param rec the rectangle
     * @return true if there's no hit, false otherwise
     */
    public boolean isInBounds(Rectangle rec) {
        double dx = this.v.getDx(), dy = this.v.getDy(), x = this.center.getX(), y = this.center.getY();
        return isInBound(rec.getLowXBound(), rec.getHighXBound(), dx, x)
                && isInBound(rec.getLowYBound(), rec.getHighYBound(), dy, y);
    }

    /**
     * adjust the collision point according to the ball's radius.
     *
     * @param collision collision point
     * @param c         object the ball collides with
     * @return adjusted collision point
     */
    private Point adjustCollPoint(Point collision, Collidable c) {
        //vertices indexes
        int rightVert = 0, leftVert = 1, upperVert = 2, lowerVert = 3;
        //scenarios
        int vertical = 2, horizontal = 3;
        //screen size
        int width = 800, height = 600;
        double newX = collision.getX(), newY = collision.getY();
        Line[] vertices = c.getCollisionRectangle().vertices();
        //ball hits one of the vertical edges scenario
        if (vertices[leftVert].isOnLine(collision, vertical)) {
            newX -= r;
        } else if (vertices[rightVert].isOnLine(collision, vertical)) {
            newX += r;
        }
        //ball hits one of the horizontal edges scenario
        if (vertices[upperVert].isOnLine(collision, horizontal)) {
            newY -= r;
        } else if (vertices[lowerVert].isOnLine(collision, horizontal)) {
            newY += r;
        }
        //makes sure the ball stays within the screen bounds
        newX = fixPos(newX, 0, width);
        newY = fixPos(newY, 0, height);
        return new Point(newX, newY);
    }

    /**
     * checks if a ball's center point x or y position is within bounds, considering the ball's size.
     *
     * @param pos       current x/y axis position
     * @param highBound x/y axis max range
     * @param lowBound  x/y axis min range
     * @return fixed position if ball is out of range, same position otherwise
     */
    private double fixPos(double pos, double lowBound, double highBound) {
        if (pos - this.r < lowBound) {
            return this.r + lowBound;
        } else if (pos + this.r > highBound) {
            return highBound - this.r;
        }
        return pos;
    }

    /**
     * checks if a ball's center point is within a rectangle bounds, considering the ball's size.
     *
     * @param rec the rectangle
     */
    private void fixStartPos(Rectangle rec) {
        //fixed x-axis position
        double newX = this.fixPos(this.getX(), rec.getLowXBound(), rec.getHighXBound());
        //fixed y-axis position
        double newY = this.fixPos(this.getY(), rec.getLowYBound(), rec.getHighYBound());
        center = new Point(newX, newY);
    }

    /**
     * creates a ball with given size and a random location inside the screen.
     * makes sure the ball isn't created inside an obstacle.
     *
     * @param size        each ball's size
     * @param screen      the rectangle containing the balls
     * @param obstacles   the blocks which the ball can't be created in
     * @param color       ball's color
     * @param environment ball's game environment
     * @return array of balls
     */
    public static Ball createRandomBall(int size, Rectangle screen, java.util.List<Rectangle> obstacles,
                                        java.awt.Color color, GameEnvironment environment) {
        boolean isInRect;
        Point p;
        Ball b;
        size = fixRadius(screen.getHighXBound(), screen.getHighYBound(), size);
        //loop runs until we successfully created a ball inside the screen but outside all the game's collidables
        do {
            isInRect = false;
            p = Point.createRandPoint(screen, size);
            b = new Ball(p, size, color, environment);
            b.fixStartPos(screen);
            b.setVelocity(Velocity.createRandomVelocity());
            //loop checks if the ball was created inside one of the program collidables
            for (Rectangle obstacle : obstacles) {
                if (obstacle.isBallInRectangle(b)) {
                    isInRect = true;
                    break;
                }
            }
        } while (isInRect);
        return b;
    }

    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        //if the hitter of the hit on block is the current ball we change its color to the block's color.
        if (hitter == this) {
            color = beingHit.getColor();
        }
    }

    /**
     * removes the current ball from the game's Sprite Collection.
     *
     * @param game our game's object.
     */
    public void removeFromGame(Game game) {
        game.removeSprite(this);
    }
}
