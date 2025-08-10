package Arkanoid.GameAssets;

import Arkanoid.Geometry.CollisionInfo;
import Arkanoid.Geometry.Line;
import Arkanoid.Geometry.Point;

import Arkanoid.Sprites.Collidable;

import java.util.ArrayList;
import java.util.List;

/**
 * contains all the objects our ball can possibly collide with.
 */
public class GameEnvironment {
    private final List<Collidable> collidables = new ArrayList<>();

    /**
     * adds a given collidable object into the list of all collidable objects.
     *
     * @param c the collidable object
     */
    public void addCollidable(Collidable c) {
        collidables.add(c);
    }

    /**
     * removes a given Collidable object from our collidables list.
     *
     * @param c the Collidable object we want to remove.
     */
    public void removeCollidable(Collidable c) {
        collidables.remove(c);
    }

    /**
     * given an object trajectory, we find the first obstacle he'll collide with and the collision point.
     *
     * @param trajectory object's trajectory
     * @return first obstacle to collide with and the collision point, or null if there are no collisions
     */
    public CollisionInfo getClosestCollision(Line trajectory) {
        double closestDistance = 0, tempDistance;
        Point closestCollision = null, temp;
        Collidable closestCollidable = null;
        //there's nothing to collide with scenario
        if (collidables.isEmpty()) {
            return null;
        }
        for (Collidable collidable : collidables) {
            /*
             * stores current collidable's closest intersection point to the start of the trajectory line,
             * or null if there's no intersection
             */
            temp = trajectory.closestIntersectionToStartOfLine(collidable.getCollisionRectangle());
            //current collidable and trajectory line are intersecting scenario
            if (temp != null) {
                //first intersection point found scenario
                if (closestCollision == null) {
                    closestCollision = temp;
                    closestCollidable = collidable;
                    closestDistance = trajectory.start().distance(closestCollision);
                } else {
                    tempDistance = trajectory.start().distance(temp);
                    //new point is closer to the start of the line than the current closest one scenario
                    if (tempDistance < closestDistance) {
                        closestCollision = temp;
                        closestCollidable = collidable;
                        closestDistance = tempDistance;
                    }
                }
            }
        }
        //no collisions in the trajectory scenario
        if (closestCollision == null) {
            return null;
        }
        return new CollisionInfo(closestCollision, closestCollidable);
    }
}