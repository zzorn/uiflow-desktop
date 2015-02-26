package org.uiflow.desktop.chart.chartlayer;

import org.uiflow.desktop.chart.axis.Axis;
import org.uiflow.desktop.chart.dataseries.DataSeries;

import static org.flowutils.Check.notNull;

/**
 * Represents some view aspect that data of type V can be displayed on.
 */
public class VisualizationChannel<T extends Number, V> {

    private final String name;
    private final Class<V> visualizedType;
    private final Axis<T> seriesAxis;
    private DataSeries<T, V> data;

    public VisualizationChannel(String name, Class<V> visualizedType, Axis<T> seriesAxis) {
        notNull(name, "name");
        notNull(visualizedType, "visualizedType");
        notNull(seriesAxis, "seriesAxis");

        this.name = name;
        this.visualizedType = visualizedType;
        this.seriesAxis = seriesAxis;
    }

    public String getName() {
        return name;
    }

    public Class<V> getVisualizedType() {
        return visualizedType;
    }

    public DataSeries<T, V> getData() {
        return data;
    }

    public void setData(DataSeries<T, V> data) {
        // TODO: Verify data series compatibility (time and value axis)
        this.data = data;
    }
}
