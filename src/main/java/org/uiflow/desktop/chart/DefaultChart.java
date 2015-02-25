package org.uiflow.desktop.chart;

import org.flowutils.Check;
import org.uiflow.desktop.chart.axis.AxisView;
import org.uiflow.desktop.ui.RenderableUiComponent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.flowutils.Check.notNull;

/**
 *
 */
public class DefaultChart extends RenderableUiComponent implements Chart {

    private Color backgroundColor = new Color(0,0,0);

    private final List<AxisView> axisViews = new ArrayList<AxisView>();
    private final List<ChartLayer> chartLayers = new ArrayList<ChartLayer>();

    public DefaultChart() {
        setRenderable(this);
    }

    @Override public void addAxisView(AxisView axisView) {
        notNull(axisView, "axisView");
        Check.notContained(axisView, axisViews, "axisViews");

        axisViews.add(axisView);
    }

    @Override public void removeAxisView(AxisView axisView) {
        axisViews.remove(axisView);
    }

    @Override public void addLayer(ChartLayer chartLayer) {
        notNull(chartLayer, "chartLayer");
        Check.notContained(chartLayer, chartLayers, "chartLayers");

        chartLayers.add(chartLayer);
    }

    @Override public void removeLayer(ChartLayer chartLayer) {
        chartLayers.remove(chartLayer);
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override public void render(Graphics2D g2, Rectangle renderArea) {
        // Draw background
        g2.setColor(backgroundColor);
        g2.fillRect(renderArea.x, renderArea.y, renderArea.width, renderArea.height);

        // TODO: Add min thickness in pixels field to axis.  Shrink chart area by the thickness of each axis on all sides of it.

        // Draw axis
        Rectangle chartArea = new Rectangle(renderArea);
        Rectangle axisArea = new Rectangle();
        for (AxisView axisView : axisViews) {
            axisView.calculatePreferredArea(chartArea, axisArea, g2);
            axisView.render(g2, axisArea);
        }

        // Draw chart layers
        for (ChartLayer chartLayer : chartLayers) {
            chartLayer.render(g2, chartArea);
        }
    }
}
