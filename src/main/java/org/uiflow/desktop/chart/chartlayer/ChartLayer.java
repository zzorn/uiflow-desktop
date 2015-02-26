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
public interface ChartLayer<T extends Number, V extends Number> extends Renderable, AxisViewListener {

    Axis<T> getHorizontalAxis();

    Axis<V> getVerticalAxis();

    Collection<VisualizationChannel> getVisualizationChannels();

    VisualizationChannel getVisualizationChannel(String name);

    void setData(String visualizationChannel, DataSeries dataSeries);

}
