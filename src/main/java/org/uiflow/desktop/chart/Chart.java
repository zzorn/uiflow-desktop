package org.uiflow.desktop.chart;

import org.uiflow.desktop.chart.axis.Axis;
import org.uiflow.desktop.chart.axis.AxisOrientation;
import org.uiflow.desktop.chart.axis.AxisProjection;
import org.uiflow.desktop.chart.axis.AxisView;
import org.uiflow.desktop.chart.chartlayer.ChartLayer;
import org.uiflow.desktop.ui.Renderable;
import org.uiflow.desktop.ui.UiComponent;

import java.awt.*;

/**
 *
 */
public interface Chart extends Renderable, UiComponent {

    <T extends Number> AxisView<T> addAxis(String axisName, Class<T> axisType, AxisOrientation orientation);
    <T extends Number> AxisView<T> addAxis(String axisName, Class<T> axisType, AxisOrientation orientation, T firstVisible, T lastVisible);
    <T extends Number> AxisView<T> addAxis(String axisName, Class<T> axisType, AxisOrientation orientation, T firstVisible, T lastVisible, AxisProjection<T> axisProjection);

    <T extends Number> AxisView<T> addAxis(Axis<T> axis,
                                           AxisOrientation orientation);

    <T extends Number> AxisView<T> addAxis(Axis<T> axis,
                                           AxisOrientation orientation,
                                           T firstVisible,
                                           T lastVisible);

    <T extends Number> AxisView<T> addAxis(Axis<T> axis,
                                           AxisOrientation orientation,
                                           T firstVisible,
                                           T lastVisible,
                                           AxisProjection<T> axisProjection);

    AxisView<Long> addTimeAxis();

    AxisView<Long> addTimeAxis(Long firstVisible,
                               Long lastVisible);

    AxisView<Long> addTimeAxis(String axisName,
                               Long firstVisible,
                               Long lastVisible);

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
