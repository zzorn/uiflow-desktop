package org.uiflow.desktop.chart.chartlayer;

import org.uiflow.desktop.chart.axis.Axis;

import java.awt.*;

import static org.flowutils.Check.notNull;

/**
 *
 */
public abstract class ChartLayerBase implements ChartLayer {

    private final Axis verticalAxis;
    private final Axis horizontalAxis;

    private Number firstVertical;
    private Number lastVertical;
    private Number firstHorizontal;
    private Number lastHorizontal;

    protected ChartLayerBase(Axis verticalAxis, Axis horizontalAxis) {
        notNull(verticalAxis, "verticalAxis");
        notNull(horizontalAxis, "horizontalAxis");

        this.verticalAxis = verticalAxis;
        this.horizontalAxis = horizontalAxis;
    }

    public final Axis getVerticalAxis() {
        return verticalAxis;
    }

    public final Axis getHorizontalAxis() {
        return horizontalAxis;
    }

    @Override public final void onVisibleAreaChanged(Axis axis, Number firstVisible, Number lastVisible) {
        if (axis == verticalAxis) {
            firstVertical = firstVisible;
            lastVertical = lastVisible;
            onVerticalVisibleAreaChanged();
        }
        else if (axis == horizontalAxis) {
            firstHorizontal = firstVisible;
            lastHorizontal = lastVisible;
            onHorizontalVisibleAreaChanged();
        }
    }

    protected final Number getFirstVertical() {
        return firstVertical;
    }

    protected final Number getLastVertical() {
        return lastVertical;
    }

    protected final Number getFirstHorizontal() {
        return firstHorizontal;
    }

    protected final Number getLastHorizontal() {
        return lastHorizontal;
    }

    protected void onVerticalVisibleAreaChanged() {
    }

    protected void onHorizontalVisibleAreaChanged() {
    }

}
