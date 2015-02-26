package org.uiflow.desktop.chart.chartlayer;

import org.uiflow.desktop.chart.axis.Axis;
import org.uiflow.desktop.chart.axis.AxisViewListener;
import org.uiflow.desktop.chart.dataseries.DataSeries;
import org.uiflow.desktop.ui.Renderable;

import java.util.Collection;
import java.util.List;


/**
 *
 */
// TODO: Create visualizationChannel() method that creates channel, use in implementations to assign to public final variables.
// TODO: Create a Range or Interval or similar class, that contains a value from 0 to 1, and various accessors to get that value scaled to various intervals.
public interface ChartLayer<T extends Number, V extends Number> extends Renderable, AxisViewListener {

    Axis<T> getHorizontalAxis();

    Axis<V> getVerticalAxis();

    Collection<VisualizationChannel<T, ?>> getVisualizationChannels();

    VisualizationChannel getVisualizationChannel(String name);

    void setData(String visualizationChannel, DataSeries dataSeries);

}
