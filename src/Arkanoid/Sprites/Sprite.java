package Arkanoid.Sprites;

import biuoop.DrawSurface;

/**
 * contains method signatures that define sprites in our program.
 */
public interface Sprite {
    /**
     * draws a sprite to the screen.
     *
     * @param d the draw surface
     */
    void drawOn(DrawSurface d);

    /**
     * notifies to a sprite that certain time has passed,
     * so the sprite will perform the actions it needed to perform within that timeframe.
     */
    void timePassed();
}
