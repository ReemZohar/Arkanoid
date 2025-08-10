package Arkanoid.GameAssets;

import Arkanoid.Sprites.Ball;
import Arkanoid.Sprites.Block;

import Arkanoid.Utils.Counter;

/**
 * tracks the score of the player in our Arkanoid game.
 */
public class ScoreTrackingListener implements HitListener {
    private final Counter currentScore;

    /**
     * class constructor.
     *
     * @param scoreCounter the Game's object score counter.
     */
    public ScoreTrackingListener(Counter scoreCounter) {
        this.currentScore = scoreCounter;
    }

    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        //each block removed worth 5 points
        int blockRemovePts = 5;
        currentScore.increase(blockRemovePts);
    }
}
