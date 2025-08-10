package Arkanoid.GameAssets;

import Arkanoid.Sprites.Ball;
import Arkanoid.Sprites.Block;

/**
 * used by any object in our game that needs to perform an action when a hit occurs between a ball and a block.
 */
public interface HitListener {

    /**
     * performs a certain action when a hit occurs between a ball and a block.
     *
     * @param beingHit the block being hit
     * @param hitter   the hitting ball
     */
    void hitEvent(Block beingHit, Ball hitter);
}
