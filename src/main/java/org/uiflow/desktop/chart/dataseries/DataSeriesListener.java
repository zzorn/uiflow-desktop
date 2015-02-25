package org.uiflow.desktop.chart.dataseries;

/**
 *
 */
public interface DataSeriesListener<T extends Number, V extends Number> {

    /**
     * Called when a value is added to the end of the data series.
     * @param position position of the added value.  This is the new endPosition of the data series.
     * @param value added value.
     */
    void onValueAddedToEnd(T position, V value);

    /**
     * Called when the data series is otherwise modified.
     */
    void onSeriesModified();

}
