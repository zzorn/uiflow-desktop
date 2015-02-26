package org.uiflow.desktop.chart.dataseries;

import java.util.List;

/**
 *
 */
public interface IndexedDataSeries<T extends Number, V> extends DataSeries<T, V> {

    /**
     * @return distance between values in the data series.
     */
    T getStepSize();

    /**
     * @return index of the specified position, where 0 is the index of the start position.
     */
    long getIndexOf(T position);

    /**
     * @return the value at the specified index along the data series.
     *         May be null if there is no value at that position and the data series does not interpolate values.
     */
    V getValueAtIndex(long index);

    /**
     * @param startIndex index to start getting values from (inclusive)
     * @param numberOfSteps number of values to get (if available).
     * @return list with numberOfStep values from the specified start position added, using the default step size between values.
     */
    List<V> getValuesFromIndex(long startIndex, int numberOfSteps);

    /**
     * @param startIndex index to start getting values from (inclusive)
     * @param numberOfSteps number of values to get (if available).
     * @param outputList list to add values to.  The list will first be cleared.  Should not be null.
     * @return list with numberOfStep values from the specified start position added, using the default step size between values.
     */
    List<V> getValuesFromIndex(long startIndex, int numberOfSteps, List<V> outputList);



}
