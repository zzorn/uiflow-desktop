package org.uiflow.desktop.chart;

import org.uiflow.desktop.chart.axis.AxisView;
import org.uiflow.desktop.chart.chartlayer.ChartLayer;
import org.uiflow.desktop.ui.Renderable;
import org.uiflow.desktop.ui.UiComponent;

import java.awt.*;

/**
 *
 */
public interface Chart extends Renderable, UiComponent {

    <T extends AxisView> T addAxisView(T axisView);
    void removeAxisView(AxisView axisView);

    <T extends ChartLayer> T addLayer(T chartLayer);
    void removeLayer(ChartLayer chartLayer);


    String getTitle();
    void setTitle(String title);

    boolean isOverlayTitle();
    void setOverlayTitle(boolean overlayTitle);

    boolean isCenterTitle();
    void setCenterTitle(boolean centerTitle);

    Color getTitleColor();
    void setTitleColor(Color titleColor);


}
