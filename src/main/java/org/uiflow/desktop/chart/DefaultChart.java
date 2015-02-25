package org.uiflow.desktop.chart;

import org.flowutils.Check;
import org.uiflow.desktop.chart.axis.Axis;
import org.uiflow.desktop.chart.axis.AxisView;
import org.uiflow.desktop.chart.axis.AxisViewListener;
import org.uiflow.desktop.chart.chartlayer.ChartLayer;
import org.uiflow.desktop.ui.RenderableUiComponent;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.flowutils.Check.notNull;

/**
 *
 */
public class DefaultChart extends RenderableUiComponent implements Chart {

    private Color backgroundColor = new Color(0,0,0);

    private final Map<Axis, AxisView> axisViews = new LinkedHashMap<Axis, AxisView>();
    private final List<ChartLayer> chartLayers = new ArrayList<ChartLayer>();

    private final AxisViewListener axisViewListener = new AxisViewListener() {
        @Override public void onVisibleAreaChanged(Axis axis, Number firstVisible, Number lastVisible) {
            for (ChartLayer chartLayer : chartLayers) {
                if (chartLayer.getHorizontalAxis() == axis ||
                    chartLayer.getVerticalAxis() == axis) {
                    chartLayer.onVisibleAreaChanged(axis, firstVisible, lastVisible);
                }
            }
        }
    };

    public DefaultChart() {
        setRenderable(this);
    }

    @Override public void addAxisView(AxisView axisView) {
        notNull(axisView, "axisView");
        final AxisView previousAxisView = axisViews.put(axisView.getAxis(), axisView);
        if (previousAxisView != null) {
            previousAxisView.removeListener(axisViewListener);
        }

        axisView.addListener(axisViewListener);
    }

    @Override public void removeAxisView(AxisView axisView) {
        final AxisView removedAxisView = axisViews.remove(axisView.getAxis());
        if (removedAxisView != null) {
            removedAxisView.removeListener(axisViewListener);
        }
    }

    @Override public void addLayer(ChartLayer chartLayer) {
        notNull(chartLayer, "chartLayer");
        Check.notContained(chartLayer, chartLayers, "chartLayers");

        chartLayers.add(chartLayer);

        // Update visible area
        updateChartAxis(chartLayer, chartLayer.getVerticalAxis());
        updateChartAxis(chartLayer, chartLayer.getHorizontalAxis());
    }

    private void updateChartAxis(ChartLayer chartLayer, final Axis axis) {
        final AxisView axisView = axisViews.get(axis);
        if (axisView != null) {
            chartLayer.onVisibleAreaChanged(axisView.getAxis(),
                                            axisView.getFirstVisible(),
                                            axisView.getLastVisible());
        }
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

        // Calculate chart area
        Rectangle chartArea = new Rectangle(renderArea);
        for (AxisView axisView : axisViews.values()) {
            axisView.calculateChartArea(chartArea, g2);
        }

        // Draw axis
        Rectangle axisDrawArea = new Rectangle(renderArea);
        Rectangle axisArea = new Rectangle();
        for (AxisView axisView : axisViews.values()) {
            axisView.calculatePreferredArea(axisDrawArea, chartArea, axisArea, g2);
            axisView.render(g2, axisArea);
        }

        // Draw chart layers
        for (ChartLayer chartLayer : chartLayers) {
            chartLayer.render(g2, chartArea);
        }
    }
}
