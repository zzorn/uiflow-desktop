package org.uiflow.desktop.chart.axis;

import org.flowutils.MathUtils;
import org.flowutils.rectangle.MutableRectangle;
import org.flowutils.rectangle.Rectangle;


/**
 *
 */
public enum AxisOrientation {

    HORIZONTAL_TOP(true, true),
    HORIZONTAL_BOTTOM(true, false),
    VERTICAL_LEFT(false, true),
    VERTICAL_RIGHT(false, false);

    private final boolean horizontal;
    private final boolean before;

    AxisOrientation(boolean horizontal, boolean before) {
        this.horizontal = horizontal;
        this.before = before;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    /**
     * @return true if the axis is above or to the left of the content.
     */
    public boolean isBefore() {
        return before;
    }

    public int getX(double relativeAxisValue, double relativeCrossAxisValue, Rectangle area) {
        return calculateCoordinate(relativeAxisValue, relativeCrossAxisValue,
                                   horizontal,
                                   area.getMinX(),
                                   area.getMaxX() - 1);
    }

    public int getY(double relativeAxisValue, double relativeCrossAxisValue, Rectangle area) {
        return calculateCoordinate(relativeAxisValue, relativeCrossAxisValue,
                                   !horizontal,
                                   area.getMinY(),
                                   area.getMaxY() - 1);
    }

    private int calculateCoordinate(double relativeAxisValue,
                                    double relativeCrossAxisValue,
                                    final boolean coordinateAlongAxis,
                                    final double minCoordiante,
                                    final double maxCoordinate) {

        final double relativePos;
        if (coordinateAlongAxis) {
            relativePos = horizontal ? relativeAxisValue : 1.0 - relativeAxisValue; // Y axis starts at bottom and increases up.
        }
        else {
            relativePos = before ? 1.0 - relativeCrossAxisValue : relativeCrossAxisValue;
        }

        return (int) MathUtils.mix(relativePos, minCoordiante, maxCoordinate);
    }

    public void splitArea(MutableRectangle availableArea, MutableRectangle preferredAreaOut, float thickness) {
        double x = availableArea.getX();
        double y = availableArea.getY();
        double w = availableArea.getWidth();
        double h = availableArea.getHeight();

        if (horizontal) {
            if (before) {
                availableArea.changeMinY(thickness);
            } else {
                availableArea.changeMaxY(-thickness);
                y += h - thickness;
            }

            h = thickness;
        }
        else {
            if (before) {
                availableArea.changeMinX(thickness);
            }
            else {
                availableArea.changeMaxX(-thickness);
                x += w - thickness;
            }

            w = thickness;
        }

        if (preferredAreaOut != null) {
            preferredAreaOut.set(x, y, x+w, y+h);
        }
    }
}
