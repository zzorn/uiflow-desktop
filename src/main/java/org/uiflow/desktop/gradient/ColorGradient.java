package org.uiflow.desktop.gradient;


import java.awt.*;
import java.util.Map;
import java.util.TreeMap;

import static org.flowutils.MathUtils.map;
import static org.flowutils.MathUtils.mix;

/**
 * Simple Swing color gradient.
 */
public class ColorGradient implements ColorFunction {

    private final TreeMap<Double, Color> colors = new TreeMap<Double, Color>();

    /**
     * Adds a color to the gradient
     * @param value value for the color
     * @param r red component
     * @param g green component
     * @param b blue component
     */
    public void addColor(double value, double r, double g, double b) {
        addColor(value, r, g, b, 1);
    }

    /**
     * Adds a color to the gradient
     * @param value value for the color
     * @param r red component
     * @param g green component
     * @param b blue component
     * @param a alpha component
     */
    public void addColor(double value, double r, double g, double b, double a) {
        // Clamp
        if (r < 0) r = 0; else if (r > 1) r = 1;
        if (g < 0) g = 0; else if (g > 1) g = 1;
        if (b < 0) b = 0; else if (b > 1) b = 1;
        if (a < 0) a = 0; else if (a > 1) a = 1;

        addColor(value, new Color((float) r, (float) g, (float) b, (float) a));
    }

    /**
     * Adds a color to the gradient
     * @param value value for the color
     * @param color color for the specified value
     */
    public void addColor(double value, Color color) {
        colors.put(value, color);
    }

    /**
     * Removes the color closest to the specified value.
     */
    public void removeColor(double value) {
        final Map.Entry<Double, Color> floor = colors.floorEntry(value);
        final Map.Entry<Double, Color> ceiling = colors.ceilingEntry(value);

        // Remove closest existing entry.
        if (floor != null || ceiling != null) {
            if (floor == null) colors.remove(ceiling.getKey());
            else if (ceiling == null) colors.remove(floor.getKey());
            else {
                if (value - floor.getKey() < ceiling.getKey() - value) colors.remove(floor.getKey());
                else colors.remove(ceiling.getKey());
            }
        }
    }

    /**
     * @return the color for the specified point in the gradient.
     * Interpolated if between colors, but clamped to end colors if outside the gradient range.
     */
    public Color getColor(double value) {
        return getColorMixed(value, 1, Color.white);
    }

    /**
     * Removes all colors from the gradient.
     */
    public void clear() {
        colors.clear();
    }

    /**
     * @return the underlying treemap.
     * Modifications to it are allowed, although not normally necessary.
     */
    public TreeMap<Double, Color> getColors() {
        return colors;
    }

    /**
     * @param value point in gradient to get color from
     * @param baseColor a base color to mix the gradient color with.
     * @param mixAmount 0 to only show the specified baseColor, 1 to only show the gradient color, any value in between or outside to mix the two colors.
     * @return the color at the specified point in the gradient, mixed with a base color.
     */
    public Color getColorMixed(double value, double mixAmount, Color baseColor) {
        final Map.Entry<Double, Color> floor = colors.floorEntry(value);
        final Map.Entry<Double, Color> ceiling = colors.ceilingEntry(value);

        if (floor == null && ceiling == null) return mixColor(mixAmount, baseColor, Color.black);
        else if (floor == null) return mixColor(mixAmount, baseColor, ceiling.getValue());
        else if (ceiling == null) return mixColor(mixAmount, baseColor, floor.getValue());
        else {
            if (value == floor.getKey()) return mixColor(mixAmount, baseColor, floor.getValue());
            else if (value == ceiling.getKey()) return mixColor(mixAmount, baseColor, ceiling.getValue());
            else {
                double t = map(value, floor.getKey(), ceiling.getKey(), 0, 1);
                final Color fCol = floor.getValue();
                final Color cCol = ceiling.getValue();
                int r = mixComponent(mixAmount, t, baseColor.getRed(),   fCol.getRed(),   cCol.getRed());
                int g = mixComponent(mixAmount, t, baseColor.getGreen(), fCol.getGreen(), cCol.getGreen());
                int b = mixComponent(mixAmount, t, baseColor.getBlue(),  fCol.getBlue(),  cCol.getBlue());
                int a = mixComponent(mixAmount, t, baseColor.getAlpha(), fCol.getAlpha(), cCol.getAlpha());
                return new Color(r, g, b, a);
            }
        }

    }

    @Override public Color colorForValue(double value) {
        return getColor(value);
    }

    @Override public int colorCodeForValue(double value) {
        return getColor(value).getRGB();
    }

    private Color mixColor(double t, Color base, Color top) {
        if (t == 0) return base;
        else if (t == 1) return top;
        else return new Color(
                (int) mix(t, base.getRed(), top.getRed()),
                (int) mix(t, base.getGreen(), top.getGreen()),
                (int) mix(t, base.getBlue(), top.getBlue()),
                (int) mix(t, base.getAlpha(), top.getAlpha())
        );
    }

    private int mixComponent(double colorAmount, double t, final int base, final int floorComp, final int ceilingComp) {
        return (int) mix(colorAmount, base, mix(t, floorComp, ceilingComp));
    }
}
