package Arkanoid.Sprites;

import biuoop.DrawSurface;

import java.util.ArrayList;
import java.util.List;

/**
 * stores a list of all sprites in our program.
 */
public class SpriteCollection {
    private final List<Sprite> sprites;

    /**
     * class constructor.
     */
    public SpriteCollection() {
        sprites = new ArrayList<>();
    }

    /**
     * adds a given sprite to the sprite collection.
     *
     * @param s the given sprite
     */
    public void addSprite(Sprite s) {
        sprites.add(s);
    }

    /**
     * removes a given sprite from our sprite collection.
     *
     * @param s the given sprite
     */
    public void removeSprite(Sprite s) {
        sprites.remove(s);
    }

    /**
     * notifies all the sprites in the collection that certain time has passed.
     */
    public void notifyAllTimePassed() {
        List<Sprite> spritesDupe = new ArrayList<>(sprites);
        for (Sprite sprite : spritesDupe) {
            sprite.timePassed();
        }
    }

    /**
     * draws all the sprites in the collection on the given draw surface.
     *
     * @param d the draw surface
     */
    public void drawAllOn(DrawSurface d) {
        for (Sprite sprite : sprites) {
            sprite.drawOn(d);
        }
    }
}
