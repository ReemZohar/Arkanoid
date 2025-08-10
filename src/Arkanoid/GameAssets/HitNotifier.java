package Arkanoid.GameAssets;

/**
 * used by objects in the game that can be hit and needs to notify their listeners about that.
 */
public interface HitNotifier {

    /**
     * adds a hit listener to the listener list of the object.
     *
     * @param hl the hit listener
     */
    void addHitListener(HitListener hl);

    /**
     * removes a hit listener from the listener list of the object.
     *
     * @param hl the hit listener
     */
    void removeHitListener(HitListener hl);
}
