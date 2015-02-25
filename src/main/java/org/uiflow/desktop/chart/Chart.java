package org.uiflow.desktop.chart;

import org.uiflow.desktop.chart.axis.AxisView;
import org.uiflow.desktop.chart.chartlayer.ChartLayer;
import org.uiflow.desktop.ui.Renderable;
import org.uiflow.desktop.ui.UiComponent;

/**
 *
 */
public interface Chart extends Renderable, UiComponent {

    void addAxisView(AxisView axisView);
    void removeAxisView(AxisView axisView);

    void addLayer(ChartLayer chartLayer);
    void removeLayer(ChartLayer chartLayer);

}
