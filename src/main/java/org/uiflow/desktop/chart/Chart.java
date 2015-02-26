package org.uiflow.desktop.chart;

import org.uiflow.desktop.chart.axis.AxisView;
import org.uiflow.desktop.chart.chartlayer.ChartLayer;
import org.uiflow.desktop.ui.Renderable;
import org.uiflow.desktop.ui.UiComponent;

/**
 *
 */
public interface Chart extends Renderable, UiComponent {

    <T extends AxisView> T addAxisView(T axisView);
    void removeAxisView(AxisView axisView);

    <T extends ChartLayer> T addLayer(T chartLayer);
    void removeLayer(ChartLayer chartLayer);

}
