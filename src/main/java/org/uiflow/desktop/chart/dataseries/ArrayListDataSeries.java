package org.uiflow.desktop.chart.dataseries;

import org.flowutils.ClassUtils;
import org.uiflow.desktop.chart.axis.Axis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.flowutils.Check.notNull;

/**
 * Simple DataSeries implementation backed by an ArrayList.
 */
public final class ArrayListDataSeries<T extends Number, V> extends IndexedDataSeriesBase<T, V> {

    private static final int DEFAULT_STEP_SIZE = 1;

    private final List<V> values = new ArrayList<V>(100);

    /**
     * @param seriesAxis axis that the positions of the values in this DataSeries are distributed along.
     * @param startPosition position for the first value on the seriesAxis.
     */
    public ArrayListDataSeries(Axis<T> seriesAxis, T startPosition) {
        this(seriesAxis, startPosition, ClassUtils.convertNumber(DEFAULT_STEP_SIZE, startPosition));
    }

    /**
     * @param seriesAxis axis that the positions of the values in this DataSeries are distributed along.
     * @param startPosition position for the first value on the seriesAxis.
     * @param stepSize the size of the step from one value to the next on the seriesAxis.  Defaults to one.
     */
    public ArrayListDataSeries(Axis<T> seriesAxis, T startPosition, T stepSize) {
        this(seriesAxis, startPosition, stepSize, null);
    }

    /**
     * @param seriesAxis axis that the positions of the values in this DataSeries are distributed along.
     * @param startPosition position for the first value on the seriesAxis.
     * @param stepSize the size of the step from one value to the next on the seriesAxis.  Defaults to one.
     * @param initialValues initial values to add to the DataSeries.
     */
    public ArrayListDataSeries(Axis<T> seriesAxis, T startPosition, T stepSize, Collection<V> initialValues) {
        super(seriesAxis, stepSize);
        setStart(startPosition);
        setEnd(startPosition);

        if (initialValues != null) {
            addValues(initialValues);
        }
    }

    /**
     * Adds a value to the end of the data series.
     * @param value new value to add.
     */
    public void addValue(V value) {
        values.add(value);

        // Increment end pos
        final T currentPos = getEnd();
        setEnd(ClassUtils.addNumbers(currentPos, getStepSize()));

        notifyValueAdded(currentPos, value);
    }

    /**
     * Add a number of values to the end of the data series.
     * @param values values to add.
     */
    public void addValues(Collection<V> values) {
        notNull(values, "values");

        values.addAll(values);

        T currentPos = getEnd();
        for (int i = 0; i < values.size(); i++) {
            currentPos = ClassUtils.addNumbers(currentPos, getStepSize());
        }
        setEnd(currentPos);

        notifySeriesModified();
    }

    /**
     * Clear the data series and change the end to be the same as the start.
     */
    public void clear() {
        setEnd(getStart());
        values.clear();
        notifySeriesModified();
    }

    @Override public V getValueAtIndex(long index) {
        if (index < 0 || index >= values.size()) {
            return null;
        }
        else {
            return values.get((int) index);
        }
    }
}
