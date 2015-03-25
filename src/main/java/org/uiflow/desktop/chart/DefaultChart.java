package org.uiflow.desktop.chart;

import org.flowutils.Check;
import org.flowutils.collections.dataseries.Axis;
import org.flowutils.collections.dataseries.TimeAxis;
import org.flowutils.drawcontext.DrawContext;
import org.flowutils.rectangle.MutableRectangle;
import org.flowutils.zoomandpan.DefaultZoomAndPannable;
import org.uiflow.desktop.chart.axis.*;
import org.uiflow.desktop.chart.chartlayer.ChartLayer;
import org.uiflow.desktop.ui.Renderable;
import org.uiflow.desktop.ui.RenderableUiComponent;
import org.uiflow.desktop.zoomandpan.ZoomAndPanGestureHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.flowutils.Check.notNull;

/**
 *
 */
public class DefaultChart extends RenderableUiComponent implements Chart, Renderable {

    private int backgroundColor = new Color(0,0,0).getRGB();
    private int titleColor = new Color(255, 255, 255).getRGB();
    private int titleShadowColor = new Color(0,0,0).getRGB();
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

    /*
    private JPanel mainPanel;
    private final RenderableUiComponent chartView = new RenderableUiComponent(new Renderable() {
        @Override public void render(DrawContext drawContext) {
            renderChart(drawContext);
        }
    });
    */

    private final DefaultZoomAndPannable zoomAndPannable = new DefaultZoomAndPannable();
    private final ZoomAndPanGestureHandler zoomAndPanGestureHandler = new ZoomAndPanGestureHandler(zoomAndPannable);

    public DefaultChart() {
        this(null);
    }

    public DefaultChart(String title) {
        setTitle(title);

        setRenderable(this);
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

    @Override public final int getTitleColor() {
        return titleColor;
    }

    @Override public final void setTitleColor(int titleColor) {
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

        // Have the axis listen to zoom and pan events.
        zoomAndPannable.addListener(axisView);

        /*
        rebuildUi();
        */

        return axisView;
    }

    @Override public void removeAxisView(AxisView axisView) {
        final AxisView removedAxisView = axisViews.remove(axisView.getAxis());
        if (removedAxisView != null) {
            removedAxisView.removeListener(axisViewListener);

            // Stop the axis from listening to zoom and pan events.
            zoomAndPannable.removeListener(axisView);

            /*
            rebuildUi();
            */
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

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /*
    @Override protected JComponent createUi() {
        mainPanel = new JPanel(new BorderLayout());

        rebuildUi();

        return mainPanel;
    }

    private void rebuildUi() {
        if (mainPanel != null) {
            mainPanel.removeAll();

            mainPanel.add(chartView.getUi(), BorderLayout.CENTER);

            // Add axis to the sides of the view
            for (AxisView axisView : axisViews.values()) {
                mainPanel.add(axisView.getUi(), axisView.getOrientation().getBorderLayoutConstraint());
            }
        }
    }
    */

    @Override public void render(DrawContext dc) {



        /* TODO: Figure out how to pan and zoom values, when the ranges for them are not immediately accessible here
        final double startX = zoomAndPannable.getVisibleWorldArea().getMinX();
        final double endX = zoomAndPannable.getVisibleWorldArea().getMaxX();
        final double startY = zoomAndPannable.getVisibleWorldArea().getMinY();
        final double endY = zoomAndPannable.getVisibleWorldArea().getMaxY();
        for (AxisView axisView : axisViews.values()) {
            if (axisView.getOrientation().isHorizontal()) {

                axisView.setFirstVisible(ClassUtils.convertNumber(startX, axisView.getAxis().getType()));
                axisView.setLastVisible(ClassUtils.convertNumber(endX, axisView.getAxis().getType()));
            }
            else {
                axisView.setFirstVisible(ClassUtils.convertNumber(startY, axisView.getAxis().getType()));
                axisView.setLastVisible(ClassUtils.convertNumber(endY, axisView.getAxis().getType()));
            }
        }
        */


        // Smooth edges using anti-aliasing
        final boolean oldAntialias = dc.setAntialias(true);

        // Draw background
        dc.clear(dc.getColorFromColorCode(backgroundColor));

        // If title is not overlaid, reserve space for it at the top.
        final String title = getTitle();
        final Object defaultFont = dc.getDefaultFont();
        float titleHeight = dc.getFontHeight(defaultFont) + 2 * titleMargin;

        MutableRectangle renderArea = dc.getSize(new MutableRectangle());
        if (!overlayTitle) {
            renderArea.changeMinY(titleHeight);
        }

        // Calculate chart area
        MutableRectangle chartArea = new MutableRectangle(renderArea);
        for (AxisView axisView : axisViews.values()) {
            axisView.calculateChartArea(chartArea, dc);
        }

        // Draw axis
        MutableRectangle axisDrawArea = new MutableRectangle(renderArea);
        MutableRectangle axisArea = new MutableRectangle();
        for (AxisView axisView : axisViews.values()) {
            axisView.calculatePreferredArea(axisDrawArea, chartArea, axisArea, dc);
            axisView.render(dc.subContext(axisArea));
        }

        // Draw chart layers
        final DrawContext chartContext = dc.subContext(chartArea);
        for (ChartLayer chartLayer : chartLayers) {
            chartLayer.render(chartContext);
        }

        // Draw title
        if (title != null && !title.isEmpty()) {
            dc.drawText(dc.getColorFromColorCode(titleColor),
                        (float) chartArea.getCenterX(),
                        (float) chartArea.getMinY() + titleHeight + titleMargin,
                        title,
                        dc.getDefaultFont(),
                        0.5f, 0f,
                        dc.getColorFromColorCode(titleShadowColor));
        }

        dc.setAntialias(oldAntialias);
    }

    @Override protected JComponent createUi() {
        final JComponent ui = super.createUi();

        /*
        // Listen to resizes
        ui.addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent e) {
                zoomAndPannable.updateViewSize(ui.getWidth(), ui.getHeight());
            }
        });
        */

        // Listen to zoom and pan gestures
        zoomAndPanGestureHandler.setComponentToListenTo(ui);

        return ui;
    }
}
