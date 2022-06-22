package org.cis120;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Use this file to test your implementation of Pixel.
 * 
 * We will manually grade this file to give you feedback
 * about the completeness of your test cases.
 */

public class MyPixelTest {

    /*
     * Remember, UNIT tests should ideally have one point of failure. Below we
     * give you two examples of unit tests for the Pixel constructor, one that
     * takes in three ints as arguments and one that takes in an array. We use
     * the getRed(), getGreen(), and getBlue() methods to check that the values
     * were set correctly. These two tests do not comprehensively test all of
     * Pixel so you must add your own.
     * 
     * You might want to look into assertEquals, assertTrue, assertFalse, and
     * assertArrayEquals at the following:
     * http://junit.sourceforge.net/javadoc/org/junit/Assert.html
     *
     * Note, if you want to add global variables so that you can reuse Pixels
     * in multiple tests, feel free to do so.
     */

    @Test
    public void testConstructInBounds() {
        Pixel p = new Pixel(40, 50, 60);
        assertEquals(40, p.getRed());
        assertEquals(50, p.getGreen());
        assertEquals(60, p.getBlue());
    }

    @Test
    public void testConstructArrayLongerThan3() {
        int[] arr = { 10, 20, 30, 40 };
        Pixel p = new Pixel(arr);
        assertEquals(10, p.getRed());
        assertEquals(20, p.getGreen());
        assertEquals(30, p.getBlue());
    }

    @Test
    public void testConstructOutOfBounds() {
        Pixel p = new Pixel(-30, 50, 280);
        assertEquals(0, p.getRed());
        assertEquals(50, p.getGreen());
        assertEquals(255, p.getBlue());
    }

    @Test
    public void testConstructArrayOutOfBounds() {
        int[] arr = { 0, 3999, -200 };
        Pixel p = new Pixel(arr);
        assertEquals(0, p.getRed());
        assertEquals(255, p.getGreen());
        assertEquals(0, p.getBlue());
    }
    /*
     * @Test
     * public void testConstructArrayLengthLessThanThree() {
     * int[] arr = {1; 3};
     * assert
     * }
     */

    @Test
    public void testGetComponents() {
        Pixel p = new Pixel(30, 40, 50);
        int[] actual = p.getComponents();
        int[] expected = { 30, 40, 50 };
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetComponentsRepeatedElements() {
        Pixel p = new Pixel(30, 30, 30);
        int[] actual = p.getComponents();
        int[] expected = { 30, 30, 30 };
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testDistanceNullPixel() {
        Pixel p1 = new Pixel(30, 40, 50);
        Pixel p2 = null;
        int distanceActual = p1.distance(p2);
        assertEquals(-1, distanceActual);
    }

    @Test
    public void testDistanceNegativeDistance() {
        Pixel p1 = new Pixel(10, 15, 20);
        Pixel p2 = new Pixel(150, 200, 250);
        int distanceActual = p1.distance(p2);
        assertEquals(555, distanceActual);
    }

    @Test
    public void testDistancePositiveDistance() {
        Pixel p1 = new Pixel(150, 200, 250);
        Pixel p2 = new Pixel(10, 15, 20);
        int distanceActual = p1.distance(p2);
        assertEquals(555, distanceActual);
    }

    @Test
    public void testToStringNonRepeating() {
        Pixel p = new Pixel(51, 52, 53);
        String expected = "(51, 52, 53)";
        assertTrue(expected.equals(p.toString()));
    }

    @Test
    public void testToStringRepeated() {
        Pixel p = new Pixel(50, 50, 50);
        String expected = "(50, 50, 50)";
        assertTrue(expected.equals(p.toString()));
    }

    @Test
    public void testSameRGB() {
        Pixel p1 = new Pixel(31, 65, 88);
        Pixel p2 = new Pixel(31, 65, 88);
        assertTrue(p1.sameRGB(p2));
    }

    @Test
    public void testSameRGBWrongOrder() {
        Pixel p1 = new Pixel(31, 65, 88);
        Pixel p2 = new Pixel(88, 31, 65);
        assertFalse(p1.sameRGB(p2));
    }

    @Test
    public void testSameRGBOneWrongValue() {
        Pixel p1 = new Pixel(31, 65, 88);
        Pixel p2 = new Pixel(31, 64, 88);
        assertFalse(p1.sameRGB(p2));
    }
}
