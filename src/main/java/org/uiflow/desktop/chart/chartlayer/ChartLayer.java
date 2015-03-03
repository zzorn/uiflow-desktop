package org.uiflow.desktop.chart.chartlayer;

import org.flowutils.collections.dataseries.Axis;
import org.flowutils.collections.dataseries.DataSeries;
import org.uiflow.desktop.chart.axis.AxisViewListener;
import org.uiflow.desktop.ui.Renderable;

import java.util.Collection;


/**
 *
 */
// TODO: Implement custom GraphicsContext or similar, with some additional rendering utility methods.  This could allow things to be ported to non-swing rendering more easily as well.
public interface ChartLayer<T extends Number, V extends Number> extends Renderable, AxisViewListener {

    Axis<T> getHorizontalAxis();

    Axis<V> getVerticalAxis();

    Collection<VisualizationChannel<T, ?>> getVisualizationChannels();

    VisualizationChannel getVisualizationChannel(String name);

    void setData(String visualizationChannel, DataSeries dataSeries);

}
