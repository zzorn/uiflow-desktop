package org.uiflow.desktop.chart.dataseries;

import org.uiflow.desktop.chart.axis.Axis;

import java.util.List;

/**
 * A series of values along some positional axis (e.g. time).
 */
public interface DataSeries<T extends Number, V> {

    /**
     * @return the axis that the data series is distributed along.
     */
    Axis<T> getSeriesAxis();

    // TODO: Add getter for value type?

    /**
     * @return position of initial value in the data series.
     */
    T getStart();

    /**
     * @return position of last value in the data series.
     */
    T getEnd();

    /**
     * @return the value at the specified position along the data series.
     *         May be null if there is no value at that position and the data series does not interpolate values.
     */
    V getValue(T position);

    /**
     * @param startPosition position to start getting values from (inclusive)
     * @param stepSize amount to advance the position between each value.
     * @param valueCount number of values to get.
     * @return list with numberOfStep values from the specified start position added, using the default step size between values.
     */
    List<V> getValues(T startPosition, T stepSize, int valueCount);

    /**
     * @param startPosition position to start getting values from (inclusive)
     * @param stepSize amount to advance the position between each value.
     * @param valueCount number of values to get.
     * @param outputList list to add values to.  The list will first be cleared.  Should not be null.
     * @return list with numberOfStep values from the specified start position added, using the default step size between values.
     */
    List<V> getValues(T startPosition, T stepSize, int valueCount, List<V> outputList);

    /**
     * @param listener listener that will be notified when values are added to the DataSeries or it otherwise changes.
     */
    void addListener(DataSeriesListener<T, V> listener);

    /**
     * @param listener listener to remove.
     */
    void removeListener(DataSeriesListener<T, V> listener);
}
