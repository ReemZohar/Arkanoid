package Arkanoid.Utils;

import java.awt.Color;
import java.util.Random;

/**
 * class contains useful general methods for our program.
 */
public class Operations {
    public static final double EPSILON = 0.000001;

    /**
     * generates a random color.
     *
     * @return random color
     */
    public static Color getRandColor() {
        Random rand = new Random();
        int rgbLimit = 256;
        int r = rand.nextInt(rgbLimit), g = rand.nextInt(rgbLimit), b = rand.nextInt(rgbLimit);
        return new Color(r, g, b);
    }

    /**
     * used to decide if two doubles are the same. since computers representation of number are finite,
     * some calculations that are supposed to be equal to the same double may differ.
     * this method checks if their difference is negligible, therefore meaning the doubles are the same.
     *
     * @param val1 first double
     * @param val2 second double
     * @return true if the values are the same, false otherwise
     */
    public static boolean doubleThreshold(double val1, double val2) {
        if (val1 > val2) {
            return (val1 - val2) <= EPSILON;
        } else {
            return (val2 - val1) <= EPSILON;
        }
    }

    /**
     * used to decide if one double is bigger or equal to another double.
     *
     * @param largeVal the suspected larger double
     * @param smallVal the suspected smaller double
     * @return true if largeVal is indeed bigger, equal or smaller by less than epsilon, or false otherwise
     */
    public static boolean doubleCompareThreshold(double largeVal, double smallVal) {
        return largeVal - smallVal >= -EPSILON;
    }
}
