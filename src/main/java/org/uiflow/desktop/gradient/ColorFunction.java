package org.uiflow.desktop.gradient;

import java.awt.*;

/**
 * Interface for a function from real values to Swing colors.
 */
public interface ColorFunction {

    /**
     * @return the color for the specified value.
     */
    Color colorForValue(double value);

    /**
     * @return the RGBA data of the color for the specified value, encoded in a 32 bit integer with 8 bits for each
     * color component, ordered in the same order as in Color.getRGB().
     */
    int colorCodeForValue(double value);
}
