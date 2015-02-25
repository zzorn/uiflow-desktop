package org.uiflow.desktop.chart.axis;

import org.flowutils.MathUtils;

import java.awt.*;

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
                                   area.getMaxX());
    }

    public int getY(double relativeAxisValue, double relativeCrossAxisValue, Rectangle area) {
        return calculateCoordinate(relativeAxisValue, relativeCrossAxisValue,
                                   !horizontal,
                                   area.getMinY(),
                                   area.getMaxY());
    }

    private int calculateCoordinate(double relativeAxisValue,
                                    double relativeCrossAxisValue,
                                    final boolean coordinateAlongAxis,
                                    final double minCoordiante,
                                    final double maxCoordinate) {

        final double relativePos;
        if (coordinateAlongAxis) {
            relativePos = relativeAxisValue;
        }
        else {
            relativePos = before ? 1.0 - relativeCrossAxisValue : relativeCrossAxisValue;
        }

        return (int) MathUtils.mix(relativePos, minCoordiante, maxCoordinate);
    }

    public void splitArea(Rectangle availableArea, Rectangle preferredAreaOut, int minWidth, int minHeight) {
        int x = 0;
        int y = 0;
        int w = minWidth;
        int h = minHeight;

        final int availableH = availableArea.height;
        final int availableW = availableArea.width;

        if (horizontal) {
            w = availableW;
            availableArea.height -= minHeight;

            if (before) {
                availableArea.y = h;
            } else {
                y = availableH - h;
            }
        }
        else {
            h = availableH;
            availableArea.width -= minWidth;

            if (before) {
                availableArea.x = w;
            }
            else {
                x = availableW - w;
            }
        }

        preferredAreaOut.setBounds(x, y, w, h);
    }
}
