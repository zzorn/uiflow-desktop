package org.uiflow.desktop.chart.axis;

import com.sun.corba.se.impl.orbutil.graph.Graph;
import org.uiflow.desktop.ui.Renderable;
import org.uiflow.desktop.ui.UiComponent;

import java.awt.*;
import java.awt.font.FontRenderContext;

/**
 *
 */
// TODO: Add listeners for range
// Scrolling could work by the chart sending pan and zoom events here?  Or maybe the axis could have an own panel that listens to scrolls.
// TODO: Add methods to specify minimum and maximum allowed axis values
// TODO: Have some flag that indicates whether to track the maximum axis value (=show latest values)
public interface AxisView<T extends Number> extends Renderable, UiComponent {

    Axis getAxis();

    T getFirstVisible();

    void setFirstVisible(T firstVisible);

    T getLastVisible();

    void setLastVisible(T lastVisible);

    void setVisibleRange(T first, T last);

    /**
     * @return projection to use to map axis values to the screen.  Could be e.g. a linear or a logarithmic projection.
     */
    AxisProjection<T> getProjection();
    void setProjection(AxisProjection<T> projection);

    int getNumberOfTicks();
    void setNumberOfTicks(int numberOfVisibleTicks);

    String createTickLabel(T value);

    AxisOrientation getOrientation();
    void setOrientation(AxisOrientation orientation);

    T getAxisValue(int coordinate, int startCoordinate, int endCoordinate);
    int getVisibleLocation(T axisValue, int startCoordinate, int endCoordinate);

    T getClosestRoundValue(T minimumValue, T preferredValue, T maximumValue);

    int getPreferredThickness_pixels(Graphics2D g2);

    /**
     * @param availableArea the available area for the chart and axis.  Will be modified to exclude the area reserved for the axis.
     * @param g2 needed to determine size of the labels.
     */
    void calculateChartArea(Rectangle availableArea, Graphics2D g2);

    /**
     * @param availableArea the available area for the chart and axis.  Will be modified to exclude the area reserved for the axis.
     * @param chartArea the actual location of the chart.
     * @param preferredAreaOut the rectangle to write the preferred location and size of this axis view.
     * @param g2 needed to determine size of the labels.
     */
    void calculatePreferredArea(Rectangle availableArea, Rectangle chartArea, Rectangle preferredAreaOut, Graphics2D g2);

    Color getBackgroundColor();

    void setBackgroundColor(Color backgroundColor);

    Color getLabelColor();

    void setLabelColor(Color labelColor);

    Color getTickColor();

    void setTickColor(Color tickColor);

    void addListener(AxisViewListener listener);
    void removeListener(AxisViewListener listener);

}
