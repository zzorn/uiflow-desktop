package org.uiflow.desktop.chart.axis;

import org.flowutils.Check;
import org.flowutils.ClassUtils;
import org.flowutils.MathUtils;
import org.flowutils.collections.dataseries.Axis;
import org.flowutils.drawcontext.DrawContext;
import org.flowutils.rectangle.MutableRectangle;
import org.flowutils.rectangle.Rectangle;
import org.flowutils.zoomandpan.ZoomAndPannable;
import org.flowutils.zoomandpan.ZoomAndPannableListener;
import org.uiflow.desktop.ui.RenderableUiComponent;

import java.awt.*;
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

    private int backgroundColor = new Color(0,0,0).getRGB();
    private int outlineColor = new Color(130, 130, 130).getRGB();
    private int tickColor = new Color(200, 200, 200).getRGB();
    private int labelColor = new Color(230, 230, 230).getRGB();
    private Object labelFont;
    private int margin = DEFAULT_MARGIN;

    private final Axis<T> axis;
    private T firstVisible;
    private T lastVisible;
    private int numberOfTicks = DEFAULT_NUMBER_OF_TICKS;
    private AxisOrientation orientation;
    private AxisProjection<T> projection;
    private final ArrayList<AxisViewListener> listeners = new ArrayList<AxisViewListener>();


    public DefaultAxisView(Axis<T> axis) {
        this(axis, null, null);
    }

    public DefaultAxisView(Axis<T> axis,
                           T firstVisible,
                           T lastVisible) {
        this(axis, firstVisible, lastVisible, AxisOrientation.HORIZONTAL_BOTTOM);
    }

    public DefaultAxisView(Axis<T> axis,
                           T firstVisible,
                           T lastVisible,
                           AxisOrientation orientation) {
        this(axis, firstVisible, lastVisible, orientation, (AxisProjection<T>) LinearAxisProjection.LINEAR_AXIS_PROJECTION);
    }

    public DefaultAxisView(Axis<T> axis,
                           T firstVisible,
                           T lastVisible,
                           AxisOrientation orientation,
                           AxisProjection<T> projection) {
        notNull(axis, "axis");
        notNull(orientation, "orientation");
        notNull(projection, "projection");

        this.axis = axis;
        this.firstVisible = firstVisible;
        this.lastVisible = lastVisible;
        this.orientation = orientation;
        this.projection = projection;

        setRenderable(this);
    }

    public Axis<T> getAxis() {
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
        notNull(projection, "projection");

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

    @Override public final int getBackgroundColor() {
        return backgroundColor;
    }

    @Override public final void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override public final int getLabelColor() {
        return labelColor;
    }

    @Override public final void setLabelColor(int labelColor) {
        this.labelColor = labelColor;
    }

    @Override public final int getTickColor() {
        return tickColor;
    }

    @Override public final void setTickColor(int tickColor) {
        this.tickColor = tickColor;
    }

    @Override public void addListener(AxisViewListener listener) {
        notNull(listener, "listener");
        listeners.add(listener);
    }

    @Override public void removeListener(AxisViewListener listener) {
        listeners.remove(listener);
    }

    public int getOutlineColor() {
        return outlineColor;
    }

    public void setOutlineColor(int outlineColor) {
        this.outlineColor = outlineColor;
    }

    public Object getLabelFont() {
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

    @Override public void calculateChartArea(MutableRectangle availableArea, DrawContext drawContext) {
        notNull(availableArea, "availableArea");
        notNull(drawContext, "drawContext");

        orientation.splitArea(availableArea, null, getPreferredThickness_pixels(drawContext));
    }

    @Override public void calculatePreferredArea(MutableRectangle availableArea,
                                                 Rectangle chartArea,
                                                 MutableRectangle preferredAreaOut,
                                                 DrawContext drawContext) {
        notNull(availableArea, "availableArea");
        notNull(chartArea, "chartArea");
        notNull(preferredAreaOut, "preferredAreaOut");
        notNull(drawContext, "drawContext");

        orientation.splitArea(availableArea, preferredAreaOut, getPreferredThickness_pixels(drawContext));

        if (orientation.isHorizontal()) {
            preferredAreaOut.setX(chartArea.getMinX(), chartArea.getMaxX());
        }
        else {
            preferredAreaOut.setY(chartArea.getMinY(), chartArea.getMaxY());
        }
    }

    @Override public void render(DrawContext dc) {
        final Rectangle axisArea = dc.getSize();
        ensureLabelFontAvailable(dc);

        // Draw background
        dc.clear(dc.getColorFromColorCode(backgroundColor));

        final Object outlineCol = dc.getColorFromColorCode(outlineColor);
        drawLine(dc, outlineCol, 0, 0, 1, 0, axisArea);
        drawLine(dc, outlineCol, 0, 1, 1, 1, axisArea);
        drawLine(dc, outlineCol, 0, 0, 0, 1, axisArea);
        drawLine(dc, outlineCol, 1, 0, 1, 1, axisArea);


        final Object tickCol = dc.getColorFromColorCode(tickColor);
        final Object labelCol = dc.getColorFromColorCode(labelColor);
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
                drawLine(dc, tickCol, relativeAxisPos, 0, relativeAxisPos, 0.1, axisArea);

                // Draw tick label
                int labelX = orientation.getX(relativeAxisPos, 0.5, axisArea);
                int labelY = orientation.getY(relativeAxisPos, 0.5, axisArea);

                final String tickLabel = createTickLabel(roundAxisValue);
                dc.drawText(labelCol, labelX, labelY, tickLabel, labelFont, 0.5f, 0.5f);
            }
        }
    }

    private void ensureLabelFontAvailable(DrawContext drawContext) {
        // Make sure we have a label font
        if (labelFont == null) {
            labelFont = drawContext.getDefaultFont();
        }
    }

    protected final boolean hasSpecifiedRange() {
        return firstVisible != null &&
               lastVisible != null;
    }

    private void drawLine(DrawContext drawContext,
                          final Object color,
                          double axisPos1,
                          double acrossPos1,
                          double axisPos2,
                          double acrossPos2, final Rectangle axisArea) {
        int x1 = orientation.getX(axisPos1, acrossPos1, axisArea);
        int y1 = orientation.getY(axisPos1, acrossPos1, axisArea);
        int x2 = orientation.getX(axisPos2, acrossPos2, axisArea);
        int y2 = orientation.getY(axisPos2, acrossPos2, axisArea);

        drawContext.drawLine(color, x1, y1, x2, y2);
    }

    private T getTickValue(int tickIndex, int maxTicks) {
        double relPos = MathUtils.map(tickIndex, -1, maxTicks, 0, 1);
        return projection.getAxisValue(relPos, getFirstVisible(), getLastVisible());
    }

    @Override public String createTickLabel(T value) {
        return getAxis().getAxisLabel(value, getFirstVisible(), getLastVisible());
    }

    @Override public T getClosestRoundValue(T minimumValue, T preferredValue, T maximumValue) {
        return preferredValue;
    }

    @Override public float getPreferredThickness_pixels(DrawContext drawContext) {
        ensureLabelFontAvailable(drawContext);

        if (orientation.isHorizontal()) {
            return drawContext.getFontHeight(labelFont) + 2*margin;
        }
        else {
            return drawContext.getTextWidth(labelFont, MINIMUM_VISIBLE_LABEL) + 2*margin;
        }
    }

    private void notifyRangeUpdated() {
        for (AxisViewListener listener : listeners) {
            listener.onVisibleAreaChanged(axis, firstVisible, lastVisible);
        }
    }

    @Override public void onZoom(ZoomAndPannable zoomAndPannable, double zoomX, double zoomY) {
        double zoom = orientation.isHorizontal() ? zoomX : zoomY;

        // Scale the first and last visible by the amount zoom
        final T range = ClassUtils.subNumbers(lastVisible, firstVisible);
        final T newRange = ClassUtils.convertNumber(range.doubleValue() * zoom, axis.getType());
        final T delta = ClassUtils.subNumbers(newRange, range);
        final T deltaDiv2 = ClassUtils.divNumbers(delta, ClassUtils.convertNumber(2.0, axis.getType()));
        setVisibleRange(ClassUtils.subNumbers(firstVisible, deltaDiv2),
                        ClassUtils.addNumbers(lastVisible, deltaDiv2));
    }

    @Override public void onPan(ZoomAndPannable zoomAndPannable, double relativeDeltaX, double relativeDeltaY) {
        double relativeDelta = orientation.isHorizontal() ? relativeDeltaX : relativeDeltaY;

        // Move the first and last visible by the amount panned
        final T range = ClassUtils.subNumbers(lastVisible, firstVisible);
        final T delta = ClassUtils.convertNumber(range.doubleValue() * relativeDelta, axis.getType());
        setVisibleRange(ClassUtils.addNumbers(firstVisible, delta),
                        ClassUtils.addNumbers(lastVisible, delta));
    }
}
