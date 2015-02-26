package org.uiflow.desktop.chart.chartlayer;

import org.uiflow.desktop.chart.dataseries.DataSeries;

/**
 * Represents some view aspect that data of type V can be displayed on.
 */
public class VisualizationChannel<T extends Number, V> {

    private final String name;
    private final Class<V> visualizedType;
    private DataSeries<T, V> dataSeries;

    public VisualizationChannel(String name, Class<V> visualizedType) {
        this.name = name;
        this.visualizedType = visualizedType;
    }

    public String getName() {
        return name;
    }

    public Class<V> getVisualizedType() {
        return visualizedType;
    }

    public DataSeries<T, V> getDataSeries() {
        return dataSeries;
    }

    public void setDataSeries(DataSeries<T, V> dataSeries) {
        // TODO: Verify data series compatibility (time and value axis)
        this.dataSeries = dataSeries;
    }
}
