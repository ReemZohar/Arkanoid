import Arkanoid.GameAssets.Game;

/**
 * runs the Arkanoid game.
 */
public class Ass5Game {
    /**
     * runs the Arkanoid game by creating a new game object, initialize it and run the game through it.
     *
     * @param args user command line input, unnecessary in our program.
     */
    public static void main(String[] args) {
        Game g = new Game();
        g.initialize();
        g.run();
    }
}
