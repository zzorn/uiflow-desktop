package org.uiflow.desktop.chart.dataseries;

import org.flowutils.Check;
import org.uiflow.desktop.chart.axis.Axis;

import java.util.ArrayList;
import java.util.List;

import static org.flowutils.Check.notNull;

/**
 *
 */
public abstract class IndexedDataSeriesBase<T extends Number, V> extends DataSeriesBase<T, V> implements IndexedDataSeries<T, V> {

    private T stepSize;

    /**
     * @param seriesAxis axis that the positions of the values in this DataSeries are distributed along.
     */
    public IndexedDataSeriesBase(Axis<T> seriesAxis) {
        this(seriesAxis, null);
    }

    /**
     * @param seriesAxis axis that the positions of the values in this DataSeries are distributed along.
     * @param stepSize the size of the step from one value to the next on the seriesAxis.
     */
    public IndexedDataSeriesBase(Axis<T> seriesAxis, T stepSize) {
        super(seriesAxis);

        this.stepSize = stepSize;
    }

    @Override public final T getStepSize() {
        return stepSize;
    }

    public final void setStepSize(T stepSize) {
        if (stepSize != null) {
            if (stepSize instanceof Double ||
                stepSize instanceof Float) {
                Check.notZero(stepSize.doubleValue(), "stepSize", 0.00000000000000000000000000000001);
            }
            else {
                if (stepSize.longValue() == 0) throw new IllegalArgumentException("stepSize should not be zero");
            }
        }

        this.stepSize = stepSize;
    }

    @Override public long getIndexOf(T position) {
        notNull(position, "position");
        notNull(getStart(), "getStart()");
        notNull(getEnd(), "getEnd()");
        notNull(stepSize, "stepSize");

        final long index;
        if (position instanceof Double ||
            position instanceof Float) {
            index = (long) ((position.doubleValue() - getStart().doubleValue() + stepSize.doubleValue() / 2) / stepSize.doubleValue());
        }
        else {
            index = (position.longValue() - getStart().longValue() + stepSize.longValue() / 2) / stepSize.longValue();
        }
        return index;
    }

    @Override public V getValue(T position) {
        final long index = getIndexOf(position);

        if (index < 0) return null;
        else return getValueAtIndex(index);
    }

    @Override public List<V> getValues(T startPosition, T stepSize, int valueCount, List<V> outputList) {
        if (this.stepSize != null && this.stepSize.equals(stepSize)) {
            return getValuesFromIndex(getIndexOf(startPosition), valueCount, outputList);
        }
        else {
            return super.getValues(startPosition, stepSize, valueCount, outputList);
        }
    }

    @Override public final List<V> getValuesFromIndex(long startIndex, int numberOfSteps) {
        return getValuesFromIndex(startIndex, numberOfSteps, new ArrayList<V>(numberOfSteps));
    }

    @Override public List<V> getValuesFromIndex(long startIndex, int numberOfSteps, List<V> outputList) {
        Check.positiveOrZero(startIndex, "startIndex");
        Check.positiveOrZero(numberOfSteps, "numberOfSteps");
        notNull(outputList, "outputList");

        outputList.clear();

        long index  = startIndex;
        final long endIndex = startIndex + numberOfSteps;
        for ( ; index < endIndex; index++) {
            outputList.add(getValueAtIndex(index));
        }

        return outputList;
    }
}
