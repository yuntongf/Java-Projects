package org.cis120;

public class AdvancedManipulations {

    /**
     * Change the contrast of a picture.
     *
     * Your job is to change the intensity of the colors in the picture.
     * The simplest method of changing contrast is as follows:
     *
     * 1. Find the average color intensity of the picture.
     * a) Sum the values of all the color components for each pixel.
     * b) Divide the total by the number of pixels times the number of
     * components (3).
     * 2. Subtract the average color intensity from each color component of
     * each pixel. Note that you could underflow into negatives.
     * This will make the average color intensity zero.
     * 3. Scale the intensity of each pixel's color components by multiplying
     * them by the "multiplier" parameter. Note that the multiplier is a
     * double (a decimal value like 1.2 or 0.6) and color values are ints
     * between 0 and 255.
     * 4. Add the original average color intensity back to each component of
     * each pixel.
     * 5. Clip the color values so that all color component values are between
     * 0 and 255. (This should be handled by the Pixel class anyway!)
     *
     * Hint: You should use Math.round() before casting to an int for
     * the average color intensity and for the scaled RGB values.
     * (I.e., in particular, the average should be rounded to an int
     * before being used for further calculations...)
     *
     * @param pic        the original picture
     * @param multiplier the factor by which each color component
     *                   of each pixel should be scaled
     * @return the new adjusted picture
     * 
     */
    public static PixelPicture adjustContrast(
            PixelPicture pic, double multiplier
    ) {
        int w = pic.getWidth();
        int h = pic.getHeight();

        Pixel[][] src = pic.getBitmap();
        Pixel[][] tgt = new Pixel[h][w];

        double rTotal = 0.0;
        double gTotal = 0.0;
        double bTotal = 0.0;
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                Pixel p = src[row][col];
                // find average of colors
                rTotal += p.getRed();
                gTotal += p.getGreen();
                bTotal += p.getBlue();
            }
        }

        int avg = (int) Math.round((rTotal + gTotal + bTotal)
            / (3 * w * h));

        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                Pixel p = src[row][col];
                // subtract average, apply multiplier, add back average
                int r = (int) Math.round((p.getRed() - avg) * multiplier) + avg;
                int g = (int) Math.round((p.getGreen() - avg) * multiplier) + avg;
                int b = (int) Math.round((p.getBlue() - avg) * multiplier) + avg;

                tgt[row][col] = new Pixel(r, g, b);
            }
        }
        return new PixelPicture(tgt);
    }

    /**
     * Reduce a picture to its most common colors.
     *
     * You will need to make use of the ColorMap class to generate a map from
     * Pixels of a certain color to the frequency with which pixels of that
     * color appear in the image. If you go to the ColorMap class, you will
     * notice that it does not have an explicitly declared constructor. In
     * those cases, Java provides a default constructor, which you can call
     * with no arguments as follows:
     * 
     * ColorMap m = new ColorMap();
     * 
     * You will then go on to populate your ColorMap by adding pixels and their
     * corresponding frequencies.
     * 
     * Once you have generated your ColorMap, select your palette by
     * retrieving the pixels whose color appears in the picture with the
     * highest frequency. Then change each pixel in the picture to one with
     * the closest matching color from your palette.
     *
     * Note that if there are two different colors that are the *same* minimal
     * distance from the given color, your code should select the most
     * frequently appearing one as the new color for the pixel. If both colors
     * appear with the same frequency, your code should select the one that
     * appears *first* in the output of the ColorMap's getSortedPixels.
     *
     * Algorithms like this are widely used in image compression. GIFs in
     * particular compress the palette to no more than 255 colors. The variant
     * we have implemented here is a weak one, since it only counts color
     * frequency by exact match. Advanced palette reduction algorithms (known as
     * "indexing" algorithms) calculate color regions and distribute the palette
     * over the regions. For example, if our picture had a lot of shades of blue
     * and little red, our algorithm would likely choose a palette of
     * all blue colors. An advanced algorithm would recognize that blues look
     * similar and distribute the palette so that it would be possible to
     * display red as well.
     *
     * @param pic       the original picture
     * @param numColors the maximum number of colors that can be used in the
     *                  reduced picture
     * @return the new reduced picture
     */

    public static PixelPicture reducePalette(PixelPicture pic, int numColors) {
        int w = pic.getWidth();
        int h = pic.getHeight();
        Pixel[][] tgt = pic.getBitmap();

        // build a sorted color map
        ColorMap m = new ColorMap();
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                Pixel p = tgt[row][col];
                if (m.contains(p)) {
                    int freq = m.getValue(p);
                    m.put(p, freq + 1);
                } else {
                    m.put(p, 1);
                }
            }
        }

        Pixel[] palette = m.getSortedPixels();

        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                int minDistance = Integer.MAX_VALUE;
                int colorIndex = -1;
                for (int i = 0; i < numColors; i++) {
                    if (tgt[row][col].distance(palette[i]) < minDistance) {
                        minDistance = tgt[row][col].distance(palette[i]);
                        colorIndex = i;
                    }
                }
                tgt[row][col] = palette[colorIndex];
            }
        }

        return new PixelPicture(tgt);
    }

    /**
     * This method blurs an image.
     *
     * PLEASE read about the *required* division implementation below - even
     * if you understand the rest of the implementation, slight floating-point
     * errors can cause significant autograder deductions!
     *
     * The general idea is that to determine the color of a pixel at
     * coordinate (x, y) of the result, look at (x, y) in the input image
     * as well as the pixels within a box (details below) centered at (x, y).
     * The average color of the pixels in the box - determined by separately
     * averaging R, G, and B - will be the color of (x, y) in the result.
     *
     * How big is the box? That's defined by {@code radius}. A radius of 1
     * yields a 3x3 box (all pixels 1 step away, including diagonals).
     * Similarly, a radius of 2 yields a 5x5 box, a radius of 3 a 7x7 box, etc.
     *
     * As an example, say we have the following image - each pixel is written
     * as (r, g, b) - and the radius parameter is 1.
     *
     * ( 1, 13, 25) ( 2, 14, 26) ( 3, 15, 27) ( 4, 16, 28)
     * ( 5, 17, 29) ( 6, 18, 30) ( 7, 19, 31) ( 8, 20, 32)
     * ( 9, 21, 33) (10, 22, 34) (11, 23, 35) (12, 24, 36)
     *
     * If we wanted the color of the output pixel at (1, 1), we would look at
     * the radius-1 box surrounding (1, 1) in the original image, which is
     *
     * ( 1, 13, 25) ( 2, 14, 26) ( 3, 15, 27)
     * ( 5, 17, 29) ( 6, 18, 30) ( 7, 19, 31)
     * ( 9, 21, 33) (10, 22, 34) (11, 23, 35)
     *
     * The average red component is (1 + 2 + ... + 9) / 9 = 5, so the result
     * pixel at (1, 1) should have red component 5.
     *
     * If the target pixel is on the edge, you should average the pixels
     * within the radius that exist. So in the same example above, the color of
     * the output at (0, 0) would be the average of:
     *
     * ( 1, 13, 25) ( 2, 14, 26)
     * ( 5, 17, 29) ( 6, 18, 30)
     *
     * **IMPORTANT FLOATING POINT NOTE:** To compute the average in a way that's
     * compatible with our autograder, please do the following steps in order:
     *
     * 1. Use floating-point division (not integer division) to divide the
     * total red/green/blue amounts by the number of pixels.
     * 2. Use Math.round() on the result of 1. This is still a float, but it
     * has been rounded to the nearest integer value.
     * 3. Cast the result of 2 to an int. That should be the component's value
     * in the output picture.
     *
     * @param pic    The picture to be blurred.
     * @param radius The radius of the blurring box.
     * @return A blurred version of the original picture.
     */

    // a helper function to find the average color of a pixel given its coordinates and radius
    private static Pixel findAvgPixel(Pixel[][] ps, int w, int h, int x, int y, int r) {
        // find the coordinates of the pixels at the top left and bottom right corner
        int x1 = x - r; // X coordinate of the top left pixel
        int x2 = x + r; // X coordinate of the bottom right pixel
        int y1 = y - r;
        int y2 = y + r;

        if (x1 < 0) {
            x1 = 0;
        }
        if (y1 < 0) {
            y1 = 0;
        }

        if (x2 > w - 1) {
            x2 = w - 1;
        }
        if (y2 > h - 1) {
            y2 = h - 1;
        }

        int rTotal = 0;
        int gTotal = 0;
        int bTotal = 0;
        double count = 0.0;

        for (int row = y1; row < y2 + 1; row++) {
            for (int col = x1; col < x2 + 1; col++) {
                Pixel p = ps[row][col];
                rTotal += p.getRed();
                gTotal += p.getGreen();
                bTotal += p.getBlue();
                count ++;
            }
        }

        // now we compute the average color of the given pixel
        int avgR = (int) Math.round(rTotal / count);
        int avgG = (int) Math.round(gTotal / count);
        int avgB = (int) Math.round(bTotal / count);

        return new Pixel(avgR, avgG, avgB);
    }
    public static PixelPicture blur(PixelPicture pic, int radius) {
        int w = pic.getWidth();
        int h = pic.getHeight();

        Pixel[][] src = pic.getBitmap();
        Pixel[][] tgt = new Pixel[h][w];

        // perform the helper function on each pixel in the picture
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                tgt[row][col] = findAvgPixel(src, w, h, col, row, radius);
            }
        }

        return new PixelPicture(tgt);
    }

    // NOTE: You may want to add a static helper function here to
    // help find the average color around the pixel you are blurring.

    /**
     * Challenge Problem (this problem is worth 0 points):
     * Flood pixels of the same color with a different color.
     *
     * The name is short for flood fill, which is the familiar "paint bucket"
     * operation in graphics programs. In a paint program, the user clicks on a
     * point in the image. Every neighboring, similarly-colored point is then
     * "flooded" with the color the user selected.
     *
     * Suppose we want to flood color at (x,y). The simplest way to do flood
     * fill is as follows:
     *
     * 1. Let target be the color at (x,y).
     * 2. Create a set of points Q containing just the point (x,y).
     * 3. Take the first point p out of Q.
     * 4. Set the color at p to color.
     * 5. For each of p's non-diagonal neighbors - up, down, left, and right -
     * check to see if they have the same color as target. If they do, add
     * them to Q.
     * 6. If Q is empty, stop. Otherwise, go to 3.
     *
     * This is a naive algorithm that can be made significantly faster if you
     * wish to try.
     *
     * For Q, you should use the provided IntQueue class. It works very much
     * like the queues we implemented in OCaml.
     *
     * @param pic The original picture to be flooded.
     * @param c   The pixel the user "clicked" (representing the color that should
     *            be flooded).
     * @param row The row of the point on which the user "clicked."
     * @param col The column of the point on which the user "clicked."
     * @return A new picture with the appropriate region flooded.
     */
    public static PixelPicture flood(PixelPicture pic, Pixel c, int row, int col) {
        return pic;
    }
}
