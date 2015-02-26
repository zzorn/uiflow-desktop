package org.uiflow.desktop.chart.chartlayer;

import org.uiflow.desktop.chart.axis.Axis;

import java.awt.*;

import static org.flowutils.Check.notNull;

/**
 *
 */
public abstract class ChartLayerBase<T extends Number, V extends Number> implements ChartLayer<T, V> {

    private final Axis<T> horizontalAxis;
    private final Axis<V> verticalAxis;

    private V firstVertical;
    private V lastVertical;
    private T firstHorizontal;
    private T lastHorizontal;

    protected ChartLayerBase(Axis<T> horizontalAxis, Axis<V> verticalAxis) {
        notNull(horizontalAxis, "horizontalAxis");
        notNull(verticalAxis, "verticalAxis");

        this.horizontalAxis = horizontalAxis;
        this.verticalAxis = verticalAxis;
    }

    public final Axis<T> getHorizontalAxis() {
        return horizontalAxis;
    }

    public final Axis<V> getVerticalAxis() {
        return verticalAxis;
    }

    @Override public final void onVisibleAreaChanged(Axis axis, Number firstVisible, Number lastVisible) {
        if (axis == verticalAxis) {
            firstVertical = (V) firstVisible;
            lastVertical = (V) lastVisible;
            onVerticalVisibleAreaChanged();
        }
        else if (axis == horizontalAxis) {
            firstHorizontal = (T) firstVisible;
            lastHorizontal = (T) lastVisible;
            onHorizontalVisibleAreaChanged();
        }
    }

    protected final V getFirstVertical() {
        return firstVertical;
    }

    protected final V getLastVertical() {
        return lastVertical;
    }

    protected final T getFirstHorizontal() {
        return firstHorizontal;
    }

    protected final T getLastHorizontal() {
        return lastHorizontal;
    }

    protected void onVerticalVisibleAreaChanged() {
    }

    protected void onHorizontalVisibleAreaChanged() {
    }

}
