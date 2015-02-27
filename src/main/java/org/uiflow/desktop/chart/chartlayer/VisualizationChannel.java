package org.uiflow.desktop.chart.chartlayer;

import org.flowutils.Check;
import org.flowutils.MathUtils;
import org.uiflow.desktop.chart.axis.Axis;
import org.uiflow.desktop.chart.dataseries.DataSeries;

import java.util.ArrayList;
import java.util.List;

import static org.flowutils.Check.notNull;

/**
 * Represents some view aspect that data of type V can be displayed on.
 */
public class VisualizationChannel<T extends Number, V> {

    private final String name;
    private final Class<V> visualizedType;
    private final Axis<T> seriesAxis;
    private DataSeries<T, V> data;

    private final List<V> visibleData = new ArrayList<V>();

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
        if (data != null) {
            if (data.getSeriesAxis() != seriesAxis) throw new IllegalArgumentException("Incompatible data series axis, was " + data.getSeriesAxis() + " expected " + seriesAxis);
            // TODO: Verify data value type
        }
        this.data = data;
    }

    public void fetchVisibleValues(T firstVisible, T stepSize, int stepCount) {
        visibleData.clear();

        if (data != null) {
            data.getValues(firstVisible, stepSize, stepCount, visibleData);
        }
    }

    public V getVisibleValue(int index) {
        return getVisibleValue(index, null);
    }

    public V getVisibleValue(int index, V defaultValue) {
        if (index < 0 || index >= visibleData.size()) {
            return defaultValue;
        }
        else {
            final V value = visibleData.get(index);
            if (value == null) return defaultValue;
            else return value;
        }
    }

    public double getVisibleValueRelativePos(int index, V firstVisible, V lastVisible, double defaultPos) {
        if (index < 0 || index >= visibleData.size()) {
            return defaultPos;
        }
        else {
            final V value = visibleData.get(index);
            if (value == null) return defaultPos;
            else if (value instanceof Double ||
                     value instanceof Float) {
                return MathUtils.relPos(((Number) value).doubleValue(),
                                        ((Number) firstVisible).doubleValue(),
                                        ((Number) lastVisible).doubleValue());
            }
            else if (value instanceof Long ||
                     value instanceof Integer ||
                     value instanceof Short ||
                     value instanceof Byte) {
                return relPos(((Number) value).longValue(),
                              ((Number) firstVisible).longValue(),
                              ((Number) lastVisible).longValue());
            }
            else {
                throw new UnsupportedOperationException("Can not return relative position for non numerical value '" + value + "'");
            }
        }
    }

    /**
     * @return relative position of t between start and end.
     *         if t == start, returns 0, if t == end, returns 1, etc.
     *         In case start equals end, returns 0.5.
     */
    private static double relPos(long t, long start, long end) {
        if (end == start) return 0.5;
        else return (double)(t - start) / (end - start);
    }


}
