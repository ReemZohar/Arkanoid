package Arkanoid.Sprites;

import Arkanoid.Geometry.Point;
import Arkanoid.Geometry.Rectangle;
import Arkanoid.Geometry.Velocity;

/**
 * collidable object interface.
 */
public interface Collidable {
    /**
     * @return object which the ball collided with.
     */
    Rectangle getCollisionRectangle();

    /**
     * alters the ball's speed if there's a collision and change it's color if it's the same color as the block it hits.
     *
     * @param collisionPoint  the collidable and ball collision point
     * @param currentVelocity ball's current velocity
     * @param hitter          the ball
     * @return ball's altered velocity if there are collisions, same velocity otherwise
     */
    Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity);
}
