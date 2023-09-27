package game.utils;

import game.values.Dimensions;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**

 A utility class containing helper methods for the game.
 */
public class Utility {
    public static float ensureRange(float value, float min, float max) {
        return Math.min(Math.max(value, min), max);
    }

    public static long timePassed(long time) {
        return System.currentTimeMillis() -time;
    }

    public static boolean isValueInRange(int value, int min, int max) {
        return value >= min && value <= max;
    }

    /**

     Converts a dimension in pixels to a dimension in screen units, based on the default screen size.
     @param dim The dimension in pixels to be converted.
     @return The converted dimension in screen units.
     */
    public static int px(int dim) {
        return (int)px((double)dim);
    }

    public static Dimension getScreenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    public static double px(double dim) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        return (dim * ((screenSize.getWidth()) / Dimensions.DEFAULT_SCREEN_SIZE.getWidth()));
    }
    /**

     Loads an image from a file with the given file name.
     @param fileName The file name of the image to be loaded.
     @return The loaded image, or null if the file could not be found or read.
     */
    public static BufferedImage loadImage(String fileName) {
        try {
            // Use ClassLoader to load the image from the JAR file
            InputStream inputStream = Utility.class.getResourceAsStream("/" + fileName);

            if (inputStream != null) {
                return ImageIO.read(inputStream);
            } else {
                System.out.println("Can't find " + fileName + " in the JAR!");
                return null;
            }
        } catch (IOException e) {
            System.out.println("Error loading " + fileName + ": " + e.getMessage());
            return null;
        }
    }

    public static boolean chooseRandom(int chance){
        chance = Math.max(0, chance);
        chance = Math.min(100, chance);

        return Math.random() * 100 <= chance;
    }

    public static void runPercentage(int chance, Runnable runnable){
        if(chooseRandom(chance))
            runnable.run();
    }
}
