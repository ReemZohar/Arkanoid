package Arkanoid.Sprites;

import Arkanoid.Geometry.Line;
import Arkanoid.Geometry.Point;
import Arkanoid.Geometry.Rectangle;

import Arkanoid.GameAssets.HitListener;
import Arkanoid.GameAssets.HitNotifier;
import Arkanoid.GameAssets.Game;

import Arkanoid.Geometry.Velocity;
import biuoop.DrawSurface;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * a block obstacle in our Arkanoid game.
 */
public class Block extends Rectangle implements Collidable, Sprite, HitNotifier {
    private final Color color;
    private final List<HitListener> hitListeners = new ArrayList<>();

    /**
     * class constructor. we construct it using the Arkanoid.Geometry.Rectangle's constructor.
     *
     * @param upperLeft block's upper left point
     * @param width     block's width
     * @param height    block's height
     * @param color     block's color
     */
    public Block(Point upperLeft, double width, double height, Color color) {
        super(upperLeft, width, height);
        this.color = color;
    }

    /**
     * @return a copy of the block's color
     */
    public Color getColor() {
        int r = color.getRed(), g = color.getGreen(), b = color.getBlue();
        return new Color(r, g, b);
    }

    @Override
    public Rectangle getCollisionRectangle() {
        return new Block(getUpperLeft(), getWidth(), getHeight(), getColor());
    }

    @Override
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        double newDx = currentVelocity.getDx(), newDy = currentVelocity.getDy();
        //vertices indexes
        int rightVert = 0, leftVert = 1, upperVert = 2, lowerVert = 3;
        //scenarios
        int vertical = 2, horizontal = 3;
        Line[] vertices = vertices();
        //ball hits one of the vertical vertices scenario
        if (vertices[rightVert].isOnLine(collisionPoint, vertical)
                || vertices[leftVert].isOnLine(collisionPoint, vertical)) {
            newDx = -newDx;
        }
        //ball hits one of the horizontal vertices scenario
        if (vertices[upperVert].isOnLine(collisionPoint, horizontal)
                || vertices[lowerVert].isOnLine(collisionPoint, horizontal)) {
            newDy = -newDy;
        }
        /*
         * when the color of the ball doesn't match the color of the block and there was a hit,
         * we notify the block's listeners that a hit event occurred.
         */
        if (!ballColorMatch(hitter)) {
            this.notifyHit(hitter);
        }
        return new Velocity(newDx, newDy);
    }

    @Override
    public void drawOn(DrawSurface d) {
        d.setColor(getColor());
        d.fillRectangle((int) getUpperLeft().getX(), (int) getUpperLeft().getY(), (int) getWidth(), (int) getHeight());
        d.setColor(Color.BLACK);
        d.drawRectangle((int) getUpperLeft().getX(), (int) getUpperLeft().getY(), (int) getWidth(), (int) getHeight());
    }

    @Override
    public void timePassed() {
    }

    /**
     * adds the block to the game's sprites collection and collidables list.
     *
     * @param g the game's object
     */
    public void addToGame(Game g) {
        g.addSprite(this);
        g.addCollidable(this);
    }

    /**
     * checks if the block has the same color as the given ball.
     *
     * @param ball the given ball.
     * @return true if their color is same, false otherwise.
     */
    public boolean ballColorMatch(Ball ball) {
        //Color class equals method compares the RGB components of the 2 given colors.
        return color.equals(ball.getColor());
    }

    /**
     * removes the current block from the game's Collidables list and Sprite Collection.
     *
     * @param game our game's object.
     */
    public void removeFromGame(Game game) {
        game.removeCollidable(this);
        game.removeSprite(this);
    }

    private void notifyHit(Ball hitter) {
        // Make a copy of the hitListeners before iterating over them.
        List<HitListener> listeners = new ArrayList<>(this.hitListeners);

        // Notify all listeners about a hit event:
        for (HitListener hl : listeners) {
            hl.hitEvent(this, hitter);
        }
    }

    @Override
    public void addHitListener(HitListener hl) {
        if (hl == null) {
            throw new IllegalArgumentException("Error: listener must not be null");
        }
        hitListeners.add(hl);
    }

    @Override
    public void removeHitListener(HitListener hl) {
        hitListeners.remove(hl);
    }
}
