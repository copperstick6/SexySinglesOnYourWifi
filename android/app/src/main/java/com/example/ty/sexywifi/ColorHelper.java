package com.example.ty.sexywifi;

/**
 * Created by tinsi on 2/25/2018.
 */

/**
 * Projekt ANNtool
 *
 * Copyright (c) 2011 github.com/timaschew/jANN
 *
 * timaschew
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Color gradient map from blue to red in 1200 steps.<br>
 * Returns a Color for a double value.
 * <ul>
 * http://stackoverflow.com/questions/2245842/sorting-colors-in-matlab</url>
 *
 * @author timaschew
 *
 */
public class ColorHelper {

    private final static int LOW = 0;
    private final static int HIGH = 255;
    private final static int HALF = (HIGH + 1) / 2;

    private final static Map<Integer, triplet> map = initNumberToColorMap();
    private static int factor;

    public static class triplet {
        public int r,g,b;
        public triplet (int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }
    }
    /**
     *
     * @param value
     *            should be from 0 unti 100
     */
    public static triplet numberToColor(final double value) {
        if (value < 0 || value > 100) {
            return null;
        }
        return numberToColorPercentage(1 - value / 100);
    }

    /**
     * @param value
     *            should be from 0 unti 1
     * @return
     */
    public static triplet numberToColorPercentage(final double value) {
        if (value < 0 || value > 1) {
            return null;
        }
        Double d = value * factor;
        int index = d.intValue();
        if (index == factor) {
            index--;
        }
        return map.get(index);
    }

    /**
     * @return
     */
    private static Map<Integer, triplet> initNumberToColorMap() {
        HashMap<Integer, triplet> localMap = new HashMap<Integer, triplet>();
        int r = LOW;
        int g = LOW;
        int b = HALF;

        // factor (increment or decrement)
        int rF = 0;
        int gF = 0;
        int bF = 1;

        int count = 0;
        // 1276 steps
        while (true) {
            localMap.put(count++, new triplet(r, g, b));
            if (b == HIGH) {
                gF = 1; // increment green
            }
            if (g == HIGH) {
                bF = -1; // decrement blue
                // rF = +1; // increment red
            }
            if (b == LOW) {
                rF = +1; // increment red
            }
            if (r == HIGH) {
                gF = -1; // decrement green
            }
            if (g == LOW && b == LOW) {
                rF = -1; // decrement red
            }
            if (r < HALF && g == LOW && b == LOW) {
                break; // finish
            }
            r += rF;
            g += gF;
            b += bF;
            r = rangeCheck(r);
            g = rangeCheck(g);
            b = rangeCheck(b);
        }
        initList(localMap);
        return localMap;
    }

    /**
     * @param localMap
     */
    private static void initList(final HashMap<Integer, triplet> localMap) {
        List<Integer> list = new ArrayList<Integer>(localMap.keySet());
        Collections.sort(list);
        Integer min = list.get(0);
        Integer max = list.get(list.size() - 1);
        factor = max + 1;
        System.out.println(factor);
    }

    /**
     * @param value
     * @return
     */
    private static int rangeCheck(final int value) {
        if (value > HIGH) {
            return HIGH;
        } else if (value < LOW) {
            return LOW;
        }
        return value;
    }

    /**
     * blue-green-red 1276 steps
     *
     * <pre>
     * if (b == HIGH) {
     * 	gF = 1; // increment green
     * }
     * if (g == HIGH) {
     * 	bF = -1; // decrement blue
     * 	// rF = +1; // increment red
     * }
     * if (b == LOW) {
     * 	rF = +1; // increment red
     * }
     * if (r == HIGH) {
     * 	gF = -1; // decrement green
     * }
     * if (g == LOW &amp;&amp; b == LOW) {
     * 	rF = -1; // decrement red
     * }
     * if (r &lt; HALF &amp;&amp; g == LOW &amp;&amp; b == LOW) {
     * 	break; // finish
     * }
     * </pre>
     */

    /**
     * blue-short green-red 1200 steps
     *
     * <pre>
     * if (b == HIGH) {
     * 	gF = 1; // increment green
     * }
     * if (g == HIGH) {
     * 	bF = -1; // decrement blue
     * 	rF = +1; // increment red
     * }
     * if (r == HIGH) {
     * 	gF = -1; // decrement green
     * }
     * if (g == LOW &amp;&amp; b == LOW) {
     * 	rF = -1; // decrement red
     * }
     * if (r &lt; HALF &amp;&amp; b == LOW) {
     * 	break; // finish
     * }
     * </pre>
     */
}