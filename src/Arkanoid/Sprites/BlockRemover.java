package Arkanoid.Sprites;

import Arkanoid.GameAssets.Game;
import Arkanoid.GameAssets.HitListener;

import Arkanoid.Utils.Counter;

/**
 * A BlockRemover is in charge of removing blocks from the game,
 * as well as keeping count of the number of blocks that remain.
 */
public class BlockRemover implements HitListener {
    private final Game game;
    private final Counter remainingBlocks;

    /**
     * class constructor.
     *
     * @param game            the game's object.
     * @param remainingBlocks counter that counts the remaining blocks in our game.
     */
    public BlockRemover(Game game, Counter remainingBlocks) {
        this.game = game;
        this.remainingBlocks = remainingBlocks;
    }

    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        int blockRemoved = 1;
        remainingBlocks.decrease(blockRemoved);
        beingHit.removeHitListener(this);
        beingHit.removeFromGame(game);
    }
}