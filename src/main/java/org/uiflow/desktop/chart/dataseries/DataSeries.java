package org.uiflow.desktop.chart.dataseries;

import org.uiflow.desktop.chart.axis.Axis;

import java.util.List;

/**
 * A series of values along some positional axis (e.g. time).
 */
public interface DataSeries<T extends Number, V extends Number> {

    /**
     * @return the axis that the data series is distributed along.
     */
    Axis<T> getSeriesAxis();

    /**
     * @return the axis that the data series values are measured on.
     */
    Axis<V> getValueAxis();

    /**
     * @return position of initial value in the data series.
     */
    T getStart();

    /**
     * @return position of last value in the data series.
     */
    T getEnd();

    /**
     * @return default distance between values in the data series.
     *         Some data series may not have a fixed step size, in that case this is some arbitrary default.
     */
    T getStepSize();

    /**
     * @return the value at the specified position along the data series.
     *         May be null if there is no value at that position and the data series does not interpolate values.
     */
    V getValue(T position);

    /**
     * @param startPosition position to start getting values from (inclusive)
     * @param numberOfSteps number of values to get (if available).
     * @return list with numberOfStep values from the specified start position added, using the default step size between values.
     */
    List<V> getValues(T startPosition, int numberOfSteps);

    /**
     * @param startPosition position to start getting values from (inclusive)
     * @param numberOfSteps number of values to get (if available).
     * @param outputList list to add values to.  The list will first be cleared.  If null, a new ArrayList will be created.
     * @return list with numberOfStep values from the specified start position added, using the default step size between values.
     */
    List<V> getValues(T startPosition, int numberOfSteps, List<V> outputList);

    /**
     * @param listener listener that will be notified when values are added to the DataSeries or it otherwise changes.
     */
    void addListener(DataSeriesListener listener);

    /**
     * @param listener listener to remove.
     */
    void removeListener(DataSeriesListener listener);
}
