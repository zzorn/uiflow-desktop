package org.uiflow.desktop.chart.axis;

import org.flowutils.Check;
import org.flowutils.MathUtils;
import org.uiflow.desktop.ui.RenderableUiComponent;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import static org.flowutils.Check.notNull;

/**
 * A view utility for rendering an axis.
 */
public class DefaultAxisView<T extends Number> extends RenderableUiComponent implements AxisView<T> {

    protected static final int DEFAULT_NUMBER_OF_TICKS = 10;
    private static final int DEFAULT_MARGIN = 4;
    private static final String MINIMUM_VISIBLE_LABEL = "888888";

    private static final NumberFormat NUMBER_FORMAT = new DecimalFormat("#.###");

    private Color backgroundColor = new Color(0,0,0);
    private Color outlineColor = new Color(130, 130, 130);
    private Color tickColor = new Color(200, 200, 200);
    private Color labelColor = new Color(230, 230, 230);
    private Font labelFont;
    private int margin = DEFAULT_MARGIN;

    private final Axis axis;
    private T firstVisible;
    private T lastVisible;
    private int numberOfTicks = DEFAULT_NUMBER_OF_TICKS;
    private AxisOrientation orientation;
    private AxisProjection<T> projection;
    private final ArrayList<AxisViewListener> listeners = new ArrayList<AxisViewListener>();


    public DefaultAxisView(Axis axis) {
        this(axis, null, null);
    }

    public DefaultAxisView(Axis axis,
                           T firstVisible,
                           T lastVisible) {
        this(axis, firstVisible, lastVisible, AxisOrientation.HORIZONTAL_BOTTOM);
    }

    public DefaultAxisView(Axis axis,
                           T firstVisible,
                           T lastVisible,
                           AxisOrientation orientation) {
        this(axis, firstVisible, lastVisible, orientation, (AxisProjection<T>) LinearAxisProjection.LINEAR_AXIS_PROJECTION);
    }

    public DefaultAxisView(Axis axis,
                           T firstVisible,
                           T lastVisible,
                           AxisOrientation orientation,
                           AxisProjection<T> projection) {
        this.axis = axis;
        this.firstVisible = firstVisible;
        this.lastVisible = lastVisible;
        this.orientation = orientation;
        this.projection = projection;

        setRenderable(this);
    }

    public Axis getAxis() {
        return axis;
    }

    public final T getFirstVisible() {
        return firstVisible;
    }

    public final T getLastVisible() {
        return lastVisible;
    }

    @Override public final void setFirstVisible(T firstVisible) {
        notNull(firstVisible, "firstVisible");
        this.firstVisible = firstVisible;

        notifyRangeUpdated();
    }

    @Override public final void setLastVisible(T lastVisible) {
        notNull(lastVisible, "lastVisible");
        this.lastVisible = lastVisible;

        notifyRangeUpdated();
    }

    @Override public final void setVisibleRange(T firstVisible, T lastVisible) {
        notNull(firstVisible, "firstVisible");
        notNull(lastVisible, "lastVisible");
        this.firstVisible = firstVisible;
        this.lastVisible = lastVisible;

        notifyRangeUpdated();
    }

    public final AxisProjection<T> getProjection() {
        return projection;
    }

    public final void setProjection(AxisProjection<T> projection) {
        this.projection = projection;
    }

    public final int getNumberOfTicks() {
        return numberOfTicks;
    }

    public final void setNumberOfTicks(int numberOfTicks) {
        Check.positiveOrZero(numberOfTicks, "numberOfTicks");
        this.numberOfTicks = numberOfTicks;
    }

    public final AxisOrientation getOrientation() {
        return orientation;
    }

    public final void setOrientation(AxisOrientation orientation) {
        notNull(orientation, "orientation");
        this.orientation = orientation;
    }

    @Override public final T getAxisValue(int coordinate, int startCoordinate, int endCoordinate) {
        double relativeAxisPos = MathUtils.map(coordinate, startCoordinate, endCoordinate, 0.0, 1.0);
        return projection.getAxisValue(relativeAxisPos, getFirstVisible(), getLastVisible());
    }

    @Override public final int getVisibleLocation(T axisValue, int startCoordinate, int endCoordinate) {
        final double relativeLocation = projection.getVisibleLocation(axisValue, getFirstVisible(), getLastVisible());
        return (int) MathUtils.mix(relativeLocation, startCoordinate, endCoordinate);
    }

    @Override public final Color getBackgroundColor() {
        return backgroundColor;
    }

    @Override public final void setBackgroundColor(Color backgroundColor) {
        notNull(backgroundColor, "backgroundColor");
        this.backgroundColor = backgroundColor;
    }

    @Override public final Color getLabelColor() {
        return labelColor;
    }

    @Override public final void setLabelColor(Color labelColor) {
        notNull(labelColor, "labelColor");
        this.labelColor = labelColor;
    }

    @Override public final Color getTickColor() {
        return tickColor;
    }

    @Override public final void setTickColor(Color tickColor) {
        notNull(tickColor, "tickColor");
        this.tickColor = tickColor;
    }

    @Override public void addListener(AxisViewListener listener) {
        notNull(listener, "listener");
        listeners.add(listener);
    }

    @Override public void removeListener(AxisViewListener listener) {
        listeners.remove(listener);
    }

    public Color getOutlineColor() {
        return outlineColor;
    }

    public void setOutlineColor(Color outlineColor) {
        this.outlineColor = outlineColor;
    }

    public Font getLabelFont() {
        return labelFont;
    }

    public void setLabelFont(Font labelFont) {
        notNull(labelFont, "labelFont");
        this.labelFont = labelFont;
    }

    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    @Override public void calculateChartArea(Rectangle availableArea, Graphics2D g2) {
        notNull(availableArea, "availableArea");
        notNull(g2, "g2");
        orientation.splitArea(availableArea, null, getPreferredThickness_pixels(g2));
    }

    @Override public void calculatePreferredArea(Rectangle availableArea, Rectangle chartArea, Rectangle preferredAreaOut, Graphics2D g2) {
        notNull(availableArea, "availableArea");
        notNull(chartArea, "chartArea");
        notNull(preferredAreaOut, "preferredAreaOut");
        notNull(g2, "g2");

        orientation.splitArea(availableArea, preferredAreaOut, getPreferredThickness_pixels(g2));

        if (orientation.isHorizontal()) {
            preferredAreaOut.x = chartArea.x;
            preferredAreaOut.width = chartArea.width;
        }
        else {
            preferredAreaOut.y = chartArea.y;
            preferredAreaOut.height = chartArea.height;
        }
    }

    @Override public void render(Graphics2D g2, Rectangle axisArea) {
        ensureLabelFontAvailable(g2);

        // Draw background
        g2.setColor(backgroundColor);
        g2.fillRect(axisArea.x, axisArea.y, axisArea.width, axisArea.height);
        drawLine(g2, axisArea, outlineColor, 0, 0, 1, 0);
        drawLine(g2, axisArea, outlineColor, 0, 1, 1, 1);
        drawLine(g2, axisArea, outlineColor, 0, 0, 0, 1);
        drawLine(g2, axisArea, outlineColor, 1, 0, 1, 1);

        if (hasSpecifiedRange()) {
            for (int i = 0; i < numberOfTicks; i++) {
                // Determine tick location
                final T roundAxisValue = getClosestRoundValue(getTickValue(i - 1, numberOfTicks),
                                                              getTickValue(i, numberOfTicks),
                                                              getTickValue(i + 1, numberOfTicks));

                final double relativeAxisPos = projection.getVisibleLocation(roundAxisValue,
                                                                             getFirstVisible(),
                                                                             getLastVisible());

                // Draw tick line
                drawLine(g2, axisArea, tickColor, relativeAxisPos, 0, relativeAxisPos, 0.1);

                // Draw tick label
                int labelX = orientation.getX(relativeAxisPos, 0.5, axisArea);
                int labelY = orientation.getY(relativeAxisPos, 0.5, axisArea);

                final String tickLabel = createTickLabel(roundAxisValue);
                drawText(g2, labelColor, labelFont, labelX, labelY, tickLabel);
            }
        }
    }

    private void drawText(Graphics2D g2,
                          final Color color,
                          final Font labelFont,
                          int x,
                          int y,
                          String text) {
        g2.setColor(color);
        final Font previousFont = g2.getFont();
        g2.setFont(labelFont);
        final Rectangle2D stringBounds = labelFont.getStringBounds(text, g2.getFontRenderContext());
        g2.drawString(text, x - (int) stringBounds.getCenterX(), y - (int) stringBounds.getCenterY());
        g2.setFont(previousFont);
    }

    private void ensureLabelFontAvailable(Graphics2D g2) {
        // Make sure we have a label font
        if (labelFont == null) {
            labelFont = g2.getFont();
        }
    }

    protected final boolean hasSpecifiedRange() {
        return firstVisible != null &&
               lastVisible != null;
    }

    private void drawLine(Graphics2D g2, Rectangle axisArea,
                          final Color color,
                          double axisPos1,
                          double acrossPos1,
                          double axisPos2,
                          double acrossPos2) {
        int x1 = orientation.getX(axisPos1, acrossPos1, axisArea);
        int y1 = orientation.getY(axisPos1, acrossPos1, axisArea);
        int x2 = orientation.getX(axisPos2, acrossPos2, axisArea);
        int y2 = orientation.getY(axisPos2, acrossPos2, axisArea);

        g2.setColor(color);
        g2.drawLine(x1, y1, x2, y2);
    }

    private T getTickValue(int tickIndex, int maxTicks) {
        double relPos = MathUtils.map(tickIndex, -1, maxTicks, 0, 1);
        return projection.getAxisValue(relPos, getFirstVisible(), getLastVisible());
    }

    @Override public String createTickLabel(T value) {
        if (value instanceof Float ||
            value instanceof Double) return NUMBER_FORMAT.format(value);
        else {
            return value.toString();
        }
    }

    @Override public T getClosestRoundValue(T minimumValue, T preferredValue, T maximumValue) {
        return preferredValue;
    }

    @Override public int getPreferredThickness_pixels(Graphics2D g2) {
        ensureLabelFontAvailable(g2);

        final Rectangle2D stringBounds = labelFont.getStringBounds(MINIMUM_VISIBLE_LABEL, g2.getFontRenderContext());
        int minX = (int) stringBounds.getWidth() + 2*margin;
        int minY = (int) stringBounds.getHeight() + 2*margin;
        if (orientation.isHorizontal()) {
            return minY;
        }
        else {
            return minX;
        }
    }

    private void notifyRangeUpdated() {
        for (AxisViewListener listener : listeners) {
            listener.onVisibleAreaChanged(axis, firstVisible, lastVisible);
        }
    }

}
