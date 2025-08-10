package Arkanoid.Geometry;

import Arkanoid.Sprites.Collidable;

/**
 * if our object collides with another object, this class keeps their collision point and the object we collided with.
 */
public class CollisionInfo {
    private final Point collisionPoint;
    private final Collidable collisionObject;

    /**
     * class constructor.
     *
     * @param collisionPoint  collision point of 2 objects
     * @param collisionObject the object involved in the collision
     */
    public CollisionInfo(Point collisionPoint, Collidable collisionObject) {
        this.collisionPoint = collisionPoint;
        this.collisionObject = collisionObject;
    }

    /**
     * returns a copy of the collision point.
     *
     * @return copy of the collision point
     */
    public Point collisionPoint() {
        return new Point(collisionPoint.getX(), collisionPoint.getY());
    }

    /**
     * @return the object to be collided with
     */
    public Collidable collisionObject() {
        return collisionObject;
    }
}
