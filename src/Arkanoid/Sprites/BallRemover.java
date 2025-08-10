package Arkanoid.Sprites;

import Arkanoid.GameAssets.Game;
import Arkanoid.GameAssets.HitListener;
import Arkanoid.Utils.Counter;

/**
 * the object in charge of removing a ball from the game in case of a hit event.
 */
public class BallRemover implements HitListener {
    private final Game game;
    private final Counter remainingBalls;

    /**
     * class constructor.
     *
     * @param game           the game's object.
     * @param remainingBalls counter that counts the remaining balls in our game.
     */
    public BallRemover(Game game, Counter remainingBalls) {
        this.game = game;
        this.remainingBalls = remainingBalls;
    }

    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        int ballRemoved = 1;
        remainingBalls.decrease(ballRemoved);
        hitter.removeFromGame(game);
    }

}
