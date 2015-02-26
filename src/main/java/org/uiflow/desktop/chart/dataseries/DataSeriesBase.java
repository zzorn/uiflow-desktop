package org.uiflow.desktop.chart.dataseries;

import org.flowutils.Check;
import org.flowutils.ClassUtils;
import org.uiflow.desktop.chart.axis.Axis;

import java.util.ArrayList;
import java.util.List;

import static org.flowutils.Check.notNull;

/**
 * Common functionality for a data series.
 */
public abstract class DataSeriesBase<T extends Number, V> implements DataSeries<T, V> {

    private final Axis<T> seriesAxis;
    private T start;
    private T end;

    private final List<DataSeriesListener<T, V>> listeners = new ArrayList<DataSeriesListener<T, V>>();

    /**
     * @param seriesAxis axis that the positions of the values in this DataSeries are distributed along.
     */
    protected DataSeriesBase(Axis<T> seriesAxis) {
        notNull(seriesAxis, "seriesAxis");

        this.seriesAxis = seriesAxis;
    }

    @Override public final Axis<T> getSeriesAxis() {
        return seriesAxis;
    }

    @Override public final T getStart() {
        return start;
    }

    @Override public final T getEnd() {
        return end;
    }

    public final void setStart(T start) {
        this.start = start;
    }

    public final void setEnd(T end) {
        this.end = end;
    }

    public final void setRange(T start, T end) {
        setStart(start);
        setEnd(end);
    }

    @Override public final List<V> getValues(T startPosition, T stepSize, int valueCount) {
        return getValues(startPosition, stepSize, valueCount, new ArrayList<V>(valueCount));
    }

    @Override public List<V> getValues(T startPosition, T stepSize, final int valueCount, List<V> outputList) {
        notNull(startPosition, "startPosition");
        notNull(stepSize, "stepSize");
        notNull(outputList, "outputList");
        Check.positiveOrZero(valueCount, "valueCount");

        outputList.clear();

        T position = startPosition;
        for (int i = 0; i < valueCount; i++) {
            outputList.add(getValue(position));
            position = ClassUtils.addNumbers(position, stepSize);
        }

        return outputList;
    }

    @Override public final void addListener(DataSeriesListener<T, V> listener) {
        notNull(listener, "listener");
        Check.notContained(listener, listeners, "listeners");
        listeners.add(listener);
    }

    @Override public final void removeListener(DataSeriesListener<T, V> listener) {
        listeners.remove(listener);
    }

    protected final void notifyValueAdded(T position, V value) {
        for (DataSeriesListener<T, V> listener : listeners) {
            listener.onValueAddedToEnd(this, position, value);
        }
    }

    protected final void notifySeriesModified() {
        for (DataSeriesListener<T, V> listener : listeners) {
            listener.onSeriesModified(this);
        }
    }
}
