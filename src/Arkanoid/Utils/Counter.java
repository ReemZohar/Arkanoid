package Arkanoid.Utils;

/**
 * represents a basic counter.
 */
public class Counter {
    private int counter = 0;

    /**
     * adds a given number to the counter.
     *
     * @param number the given number.
     */
    public void increase(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("Error: counter doesn't support adding negative values");
        }
        counter += number;
    }

    /**
     * subtract a given number from the counter.
     *
     * @param number the given number.
     */
    public void decrease(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("Error: counter doesn't support subtracting negative values");
        }
        if (number > counter) {
            throw new IllegalArgumentException("Error: cannot subtract a number that is bigger than the counter");
        }
        counter -= number;
    }

    /**
     * @return the counter's current count.
     */
    public int getValue() {
        return counter;
    }
}

