package Arkanoid.Sprites;

import Arkanoid.Geometry.Point;
import Arkanoid.Utils.Counter;

import biuoop.DrawSurface;

import java.awt.Color;

/**
 * a block with a constant size and location that has a score counter in its middle.
 */
public class ScoreIndicator extends Block {
    private final Counter score;
    private static final int HEIGHT = 20;
    private static final int WIDTH = 800;
    private static final Point UPPER_LEFT = new Point(0, 0);

    /**
     * class constructor.
     *
     * @param score the game's score counter.
     */
    public ScoreIndicator(Counter score) {
        //Block's constructor
        super(UPPER_LEFT, WIDTH, HEIGHT, Color.WHITE);
        this.score = score;
    }

    @Override
    public void drawOn(DrawSurface d) {
        super.drawOn(d);
        int textXPos = 370, textYPos = 15, fontSize = 15;
        d.drawText(textXPos, textYPos, "Score:" + score.getValue(), fontSize);
    }
}
