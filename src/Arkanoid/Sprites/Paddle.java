package Arkanoid.Sprites;

import Arkanoid.GameAssets.Game;
import Arkanoid.Geometry.Line;
import Arkanoid.Geometry.Point;
import Arkanoid.Geometry.Rectangle;
import Arkanoid.Geometry.Velocity;
import Arkanoid.Utils.Operations;

import biuoop.DrawSurface;
import biuoop.KeyboardSensor;

/**
 * represents the paddle in our Arkanoid game.
 */
public class Paddle implements Sprite, Collidable {
    private final biuoop.KeyboardSensor keyboard;
    private Block block;

    /**
     * class constructor.
     *
     * @param keyboardSensor the keyboard sensor
     */
    public Paddle(biuoop.KeyboardSensor keyboardSensor) {
        int midX = 400, y = 590, width = 100, height = 10;
        Point upperLeft = new Point(midX, y);
        this.keyboard = keyboardSensor;
        block = new Block(upperLeft, width, height, Operations.getRandColor());

    }

    /**
     * @return a copy of the block representing the paddle.
     */
    public Block getBlock() {
        return new Block(block.getUpperLeft(), block.getWidth(), block.getHeight(), block.getColor());
    }

    /**
     * moves the paddle to the left.
     */
    public void moveLeft() {
        int step = 5, screenXStart = 0, screenWidth = 800;
        double x = block.getUpperRight().getX(), y = block.getUpperRight().getY();
        Point upperLeft;
        //paddle is completely or partially within the screen bounds scenario
        if (Operations.doubleCompareThreshold(x, screenXStart)) {
            //left step
            upperLeft = new Point(x - block.getWidth() - step, y);
        } else { //paddle is completely outside the screen scenario
            //creates the upper left point of the paddle in the screen's lower border right side and takes a left step
            upperLeft = new Point(screenWidth + x - step, y);
        }
        //paddle's updated location
        block = new Block(upperLeft, block.getWidth(), block.getHeight(), block.getColor());
    }

    /**
     * moves the paddle to the right.
     */
    public void moveRight() {
        int step = 5, screenWidth = 800;
        double x = block.getUpperLeft().getX(), y = block.getUpperLeft().getY();
        Point upperLeft;
        //paddle is completely or partially within the screen bounds scenario
        if (Operations.doubleCompareThreshold(screenWidth, x)) {
            //right step
            upperLeft = new Point(x + step, y);
        } else { //paddle is completely outside the screen scenario
            //creates the upper left point of the paddle in the screen's lower border left side and takes a right step
            upperLeft = new Point(x - screenWidth - block.getWidth() + step, y);
        }
        //paddle's updated location
        block = new Block(upperLeft, block.getWidth(), block.getHeight(), block.getColor());
    }

    @Override
    public void timePassed() {
        //left key is pressed scenario
        if (keyboard.isPressed(KeyboardSensor.LEFT_KEY)) {
            moveLeft();
        }
        //right key is pressed scenario
        if (keyboard.isPressed(KeyboardSensor.RIGHT_KEY)) {
            moveRight();
        }
    }

    @Override
    public void drawOn(DrawSurface d) {
        block.drawOn(d);
    }

    @Override
    public Rectangle getCollisionRectangle() {
        return getBlock();
    }

    @Override
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        int region = 0, vertical = 2, horizontal = 3, speed = 5;
        double partitions = 5.0;
        double x = block.getUpperLeft().getX(), y = block.getUpperLeft().getY();
        Line[] regions = new Line[(int) partitions], vertices = block.vertices();
        Line leftVert = vertices[1], rightVert = vertices[0];
        //creates 5 equal partitions of the paddle's upper edge.
        for (int i = 0; i < (int) partitions; i++) {
            regions[i] = new Line(x + ((double) i / partitions) * block.getWidth(), y,
                    x + ((double) (i + 1) / partitions) * block.getWidth(), y);
            //collision point is on the i + 1 region scenario
            if (regions[i].isOnLine(collisionPoint, horizontal)) {
                region = i + 1;
                break;
            }
        }
        if (region == 1) {
            return Velocity.fromAngleAndSpeed(300, speed);
        }
        if (region == 2) {
            return Velocity.fromAngleAndSpeed(330, speed);
        }
        if (region == 3) {
            return new Velocity(currentVelocity.getDx(), -currentVelocity.getDy());
        }
        if (region == 4) {
            return Velocity.fromAngleAndSpeed(30, speed);
        }
        if (region == 5) {
            return Velocity.fromAngleAndSpeed(60, speed);
        }
        //ball hits the paddle's left or right edge scenario
        if (rightVert.isOnLine(collisionPoint, vertical) || leftVert.isOnLine(collisionPoint, vertical)) {
            return new Velocity(-currentVelocity.getDx(), currentVelocity.getDy());
        }
        return currentVelocity;
    }

    /**
     * adds the paddle to the game's sprites collection and collidables list.
     *
     * @param g the game's object
     */
    public void addToGame(Game g) {
        g.addSprite(this);
        g.addCollidable(this);
    }
}
