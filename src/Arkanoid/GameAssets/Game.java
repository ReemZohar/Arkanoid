package Arkanoid.GameAssets;

import Arkanoid.Geometry.Point;
import Arkanoid.Geometry.Rectangle;

import Arkanoid.Sprites.Sprite;
import Arkanoid.Sprites.Collidable;
import Arkanoid.Sprites.Paddle;
import Arkanoid.Sprites.Block;
import Arkanoid.Sprites.SpriteCollection;
import Arkanoid.Sprites.ScoreIndicator;
import Arkanoid.Sprites.BlockRemover;
import Arkanoid.Sprites.Ball;
import Arkanoid.Sprites.BallRemover;

import Arkanoid.Utils.Counter;
import Arkanoid.Utils.Operations;

import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.Sleeper;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * stores the game's sprite collection and collidables list and runs the game animation.
 */
public class Game {
    private SpriteCollection sprites;
    private GameEnvironment environment;
    private GUI gui;
    private final Counter blockCounter = new Counter();
    private final Counter ballCounter = new Counter();
    private final Counter score = new Counter();

    /**
     * adds a given collidable object to the game's collidable objects list.
     *
     * @param c the given collidable object
     */
    public void addCollidable(Collidable c) {
        environment.addCollidable(c);
    }

    /**
     * adds a given sprite to the game's sprites collection.
     *
     * @param s the given sprite
     */
    public void addSprite(Sprite s) {
        sprites.addSprite(s);
    }

    /**
     * initializes all our game components: sprite collection, game environment, gui, paddle, balls and blocks.
     */
    public void initialize() {
        sprites = new SpriteCollection();
        environment = new GameEnvironment();
        gui = new GUI("Arkanoid", 800, 600);

        Point screenUpperLeft = new Point(0, 0), upperLeft;
        Rectangle screen = new Rectangle(screenUpperLeft, 800, 600);
        int width = 50, height = 15, numBlocks = 12, numRows = 6, numBalls = 3, r = 5, blockAdded = 1, ballAdded = 1;
        List<Rectangle> obstacles = new ArrayList<>();
        List<Block> blocks = new ArrayList<>();
        Color color;
        Block block;
        Ball ball;
        Paddle paddle = new Paddle(gui.getKeyboardSensor());
        BlockRemover blockRemover = new BlockRemover(this, blockCounter);
        ScoreTrackingListener stl = new ScoreTrackingListener(score);
        createBounds(obstacles);
        ScoreIndicator si = new ScoreIndicator(score);
        obstacles.add(si);
        environment.addCollidable(si);
        sprites.addSprite(si);

        //loop decided how many blocks will be in a row
        for (int i = 0; i < numRows; i++) {
            color = Operations.getRandColor();
            //loop creates the blocks in the current row
            for (int j = 0; j < numBlocks - i; j++) {
                upperLeft = new Point(745 - j * width, 150 + i * height);
                block = new Block(upperLeft, width, height, color);
                block.addToGame(this);
                obstacles.add(block);
                blocks.add(block);
                //we update the block's counter accordingly
                blockCounter.increase(blockAdded);
                block.addHitListener(blockRemover);
                block.addHitListener(stl);
            }
        }
        paddle.addToGame(this);
        obstacles.add(paddle.getBlock());
        //loop creates 2 balls with random location and speed and adds them to the game's sprites collection
        for (int i = 0; i < numBalls; i++) {
            color = Color.RED;
            ball = Ball.createRandomBall(r, screen, obstacles, color, environment);
            ball.addToGame(this);
            ballCounter.increase(ballAdded);
            //adds the new ball as a listener to all the removable blocks in the game
            for (Block b : blocks) {
                b.addHitListener(ball);
            }
        }
    }

    /**
     * runs the game's animation loop.
     */
    public void run() {
        Sleeper sleeper = new Sleeper();
        int framesPerSecond = 60, screenWidth = 800, screenHeight = 600, levelPassed = 100;
        int millisecondsPerFrame = 1000 / framesPerSecond;

        while (true) {
            //if there are no block left to remove or all the balls have gone out we close the game.
            if ((blockCounter.getValue() == 0) || (ballCounter.getValue() == 0)) {
                //no blocks left scenario, we add 100 points to the player's score
                if (blockCounter.getValue() == 0) {
                    score.increase(levelPassed);
                }
                gui.close();
                return;
            }
            long startTime = System.currentTimeMillis(); // timing
            DrawSurface d = gui.getDrawSurface();
            //screen's background color
            d.setColor(new Color(204, 255, 255));
            d.fillRectangle(0, 0, screenWidth, screenHeight);
            //draws all the game's sprites
            this.sprites.drawAllOn(d);
            gui.show(d);
            this.sprites.notifyAllTimePassed();
            long usedTime = System.currentTimeMillis() - startTime;
            long milliSecondLeftToSleep = millisecondsPerFrame - usedTime;
            if (milliSecondLeftToSleep > 0) {
                sleeper.sleepFor(milliSecondLeftToSleep);
            }
        }
    }

    /**
     * removes a given Collidable object from the game environment's collidables list.
     *
     * @param c the Collidable object we want to remove.
     */
    public void removeCollidable(Collidable c) {
        environment.removeCollidable(c);
    }

    /**
     * removes a given sprite from our game's sprite collection.
     *
     * @param s the Sprite object we want to remove.
     */
    public void removeSprite(Sprite s) {
        sprites.removeSprite(s);
    }

    private void createDeathRegion(List<Rectangle> obstacles) {
        Block deathRegion = new Block(new Point(0, 595), 800, 600, Color.GRAY);
        BallRemover ballRemover = new BallRemover(this, ballCounter);
        deathRegion.addHitListener(ballRemover);
        environment.addCollidable(deathRegion);
        sprites.addSprite(deathRegion);
        obstacles.add(deathRegion);
    }

    private void createBounds(List<Rectangle> obstacles) {
        int screenWidth = 800, screenHeight = 600, thickness = 5, scoreIndThick = 20;
        Color borderColor = Color.GRAY;
        //creates the upper screen border and adds it to the Game's collidable list and sprite collection
        Block up = new Block(new Point(0, 0), screenWidth, scoreIndThick + thickness, borderColor);
        environment.addCollidable(up);
        sprites.addSprite(up);
        obstacles.add(up);
        //the death region is the down block
        createDeathRegion(obstacles);
        //creates the right screen border and adds it to the Game's collidable list and sprite collection
        Block right = new Block(new Point(screenWidth - thickness, 0), thickness, screenHeight, borderColor);
        environment.addCollidable(right);
        sprites.addSprite(right);
        obstacles.add(right);
        //creates the left screen border and adds it to the Game's collidable list and sprite collection
        Block left = new Block(new Point(0, 0), thickness, screenHeight, borderColor);
        environment.addCollidable(left);
        sprites.addSprite(left);
        obstacles.add(left);
    }
}
