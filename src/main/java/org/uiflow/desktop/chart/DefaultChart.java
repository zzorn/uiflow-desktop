package org.uiflow.desktop.chart;

import org.flowutils.Check;
import org.flowutils.collections.dataseries.Axis;
import org.flowutils.collections.dataseries.TimeAxis;
import org.uiflow.desktop.chart.axis.*;
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
    private Color titleColor = new Color(255, 255, 255);
    private String title;
    private boolean overlayTitle = true;
    private boolean centerTitle = true;
    private int titleMargin = 2;

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
        this(null);
    }

    public DefaultChart(String title) {
        setRenderable(this);
        setTitle(title);
    }

    public final String getTitle() {
        return title;
    }

    public final void setTitle(String title) {
        this.title = title;
    }

    public final boolean isOverlayTitle() {
        return overlayTitle;
    }

    public final void setOverlayTitle(boolean overlayTitle) {
        this.overlayTitle = overlayTitle;
    }

    @Override public final boolean isCenterTitle() {
        return centerTitle;
    }

    @Override public final void setCenterTitle(boolean centerTitle) {
        this.centerTitle = centerTitle;
    }

    @Override public final Color getTitleColor() {
        return titleColor;
    }

    @Override public final void setTitleColor(Color titleColor) {
        this.titleColor = titleColor;
    }

    /**
     * @return margin around title in pixels.
     */
    public final int getTitleMargin() {
        return titleMargin;
    }

    /**
     * @param titleMargin margin around title in pixels.
     */
    public final void setTitleMargin(int titleMargin) {
        this.titleMargin = titleMargin;
    }

    @Override public <T extends Number> AxisView<T> addAxis(String axisName,
                                                            Class<T> axisType,
                                                            AxisOrientation orientation) {
        return addAxis(axisName, axisType, orientation, null, null);
    }

    @Override public <T extends Number> AxisView<T> addAxis(String axisName,
                                                            Class<T> axisType,
                                                            AxisOrientation orientation,
                                                            T firstVisible,
                                                            T lastVisible) {
        return addAxis(axisName, axisType, orientation, firstVisible, lastVisible, new LinearAxisProjection<T>());
    }

    @Override public <T extends Number> AxisView<T> addAxis(String axisName,
                                                            Class<T> axisType,
                                                            AxisOrientation orientation,
                                                            T firstVisible,
                                                            T lastVisible,
                                                            AxisProjection<T> axisProjection) {
        return addAxis(new Axis<T>(axisName, axisType), orientation, firstVisible, lastVisible, axisProjection);
    }

    @Override public <T extends Number> AxisView<T> addAxis(Axis<T> axis,
                                                            AxisOrientation orientation) {
        return addAxisView(new DefaultAxisView<T>(axis,
                                                  null,
                                                  null,
                                                  orientation));
    }

    @Override public <T extends Number> AxisView<T> addAxis(Axis<T> axis,
                                                            AxisOrientation orientation,
                                                            T firstVisible,
                                                            T lastVisible) {
        return addAxisView(new DefaultAxisView<T>(axis,
                                                  firstVisible,
                                                  lastVisible,
                                                  orientation));
    }

    @Override public <T extends Number> AxisView<T> addAxis(Axis<T> axis,
                                                            AxisOrientation orientation,
                                                            T firstVisible,
                                                            T lastVisible,
                                                            AxisProjection<T> axisProjection) {
        return addAxisView(new DefaultAxisView<T>(axis,
                                                  firstVisible,
                                                  lastVisible,
                                                  orientation,
                                                  axisProjection));
    }

    @Override public AxisView<Long> addTimeAxis() {
        return addTimeAxis(null, null);
    }

    @Override public AxisView<Long> addTimeAxis(Long firstVisible,
                                                Long lastVisible) {
        return addTimeAxis("Time", firstVisible, lastVisible);
    }

    @Override public AxisView<Long> addTimeAxis(String axisName,
                                                Long firstVisible,
                                                Long lastVisible) {
        return addAxisView(new DefaultAxisView<Long>(new TimeAxis(axisName), firstVisible, lastVisible));
    }

    @Override public <T extends AxisView> T addAxisView(T axisView) {
        notNull(axisView, "axisView");
        final AxisView previousAxisView = axisViews.put(axisView.getAxis(), axisView);
        if (previousAxisView != null) {
            previousAxisView.removeListener(axisViewListener);
        }

        axisView.addListener(axisViewListener);

        return axisView;
    }

    @Override public void removeAxisView(AxisView axisView) {
        final AxisView removedAxisView = axisViews.remove(axisView.getAxis());
        if (removedAxisView != null) {
            removedAxisView.removeListener(axisViewListener);
        }
    }

    @Override public <T extends ChartLayer> T addLayer(T chartLayer) {
        notNull(chartLayer, "chartLayer");
        Check.notContained(chartLayer, chartLayers, "chartLayers");

        chartLayers.add(chartLayer);

        // Update visible area
        updateChartAxis(chartLayer, chartLayer.getVerticalAxis());
        updateChartAxis(chartLayer, chartLayer.getHorizontalAxis());

        return chartLayer;
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
        // Smooth edges using anti-aliasing
        final Object oldAntialias = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw background
        g2.setColor(backgroundColor);
        g2.fillRect(renderArea.x, renderArea.y, renderArea.width, renderArea.height);

        // If title is not overlaid, reserve space for it at the top.
        final String title = getTitle();
        int titleHeight = g2.getFontMetrics().getMaxAscent() +
                          g2.getFontMetrics().getMaxDescent() +
                          + 2 * titleMargin;
        int titleY = renderArea.y + titleMargin + g2.getFontMetrics().getMaxAscent();
        if (!overlayTitle) {
            renderArea = new Rectangle(renderArea);
            renderArea.y += titleHeight;
            renderArea.height -= titleHeight;
        }

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

        // Draw title
        if (title != null && !title.isEmpty()) {
            int titleX;
            if (centerTitle) {
                titleX = chartArea.x + (chartArea.width - g2.getFontMetrics().stringWidth(title)) / 2;
            }
            else {
                titleX = chartArea.x + titleMargin;
            }
            if (overlayTitle) {
                titleY = chartArea.y + titleMargin + g2.getFontMetrics().getMaxAscent();
            }

            // TODO: Draw dropshadow.  Implement the draw command in a GraphicsContext utility class.
            g2.setColor(titleColor);
            g2.drawString(title, titleX, titleY);
        }

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAntialias);
    }
}
