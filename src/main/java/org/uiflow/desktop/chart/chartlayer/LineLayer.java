package org.uiflow.desktop.chart.chartlayer;

import org.flowutils.MathUtils;
import org.flowutils.collections.dataseries.Axis;
import org.flowutils.drawcontext.DrawContext;
import org.flowutils.rectangle.Rectangle;

import java.awt.*;

/**
 *
 */
// TODO: Rename dropShadow to outline, and allow it to be colored as well?
public class LineLayer<T extends Number, V extends Number> extends SegmentedChartLayerBase<T, V> {

    private static final int MAX_THICKNESS = 200;
    public final VisualizationChannel<T, Color> lineColor;
    public final VisualizationChannel<T, V> lineHeight;
    public final VisualizationChannel<T, Float> thickness;

    private float defaultLineThickness = 1;
    private Color defaultColor = Color.DARK_GRAY;

    private boolean dropShadow = true;
    private Color dropShadowColor = Color.BLACK;

    private boolean renderDropShadowNow = false;
    private float dropShadowSizeFactor = 2f;
    private float dropShadowOffsetFactor = 0.1f;


    public LineLayer(Axis<T> horizontalAxis,
                        Axis<V> verticalAxis) {
        this(horizontalAxis, verticalAxis, DEFAULT_SEGMENT_COUNT);
    }

    public LineLayer(Axis<T> horizontalAxis, Axis<V> verticalAxis, int defaultNumberOfSegments) {
        super(horizontalAxis, verticalAxis, defaultNumberOfSegments);
        setSegmentXGap(0);

        lineHeight = addVerticalChannel("Height");
        lineColor = addColorChannel("Color");
        thickness = addFloatChannel("Thickness");

        // TODO: Add marker channel, with marker type?
    }

    public float getDefaultLineThickness() {
        return defaultLineThickness;
    }

    public void setDefaultLineThickness(float defaultLineThickness) {
        this.defaultLineThickness = defaultLineThickness;
    }

    public Color getDefaultColor() {
        return defaultColor;
    }

    public void setDefaultColor(Color defaultColor) {
        this.defaultColor = defaultColor;
    }

    public boolean isDropShadow() {
        return dropShadow;
    }

    public void setDropShadow(boolean dropShadow) {
        this.dropShadow = dropShadow;
    }

    public Color getDropShadowColor() {
        return dropShadowColor;
    }

    public void setDropShadowColor(Color dropShadowColor) {
        this.dropShadowColor = dropShadowColor;
    }

    public float getDropShadowSizeFactor() {
        return dropShadowSizeFactor;
    }

    public void setDropShadowSizeFactor(float dropShadowSizeFactor) {
        this.dropShadowSizeFactor = dropShadowSizeFactor;
    }

    public float getDropShadowOffsetFactor() {
        return dropShadowOffsetFactor;
    }

    public void setDropShadowOffsetFactor(float dropShadowOffsetFactor) {
        this.dropShadowOffsetFactor = dropShadowOffsetFactor;
    }

    @Override public void render(DrawContext drawContext) {

        if (dropShadow) {
            renderDropShadowNow = true;
            super.render(drawContext);
        }

        renderDropShadowNow = false;
        super.render(drawContext);
    }

    @Override protected void renderSegment(DrawContext drawContext,
                                           org.flowutils.rectangle.Rectangle segmentArea,
                                           int segmentIndex,
                                           int numberOfVisibleSegments) {

        if (lineHeight.getVisibleValue(segmentIndex) != null) {

            boolean hasPrevPos = lineHeight.getVisibleValue(segmentIndex - 1) != null;
            boolean hasNextPos = lineHeight.getVisibleValue(segmentIndex + 1) != null;

            if (hasPrevPos || hasNextPos) {
                double prevPos = lineHeight.getVisibleValueRelativePos(segmentIndex - 1,
                                                                       getFirstVertical(),
                                                                       getLastVertical(),
                                                                       0);
                final double currentPos = lineHeight.getVisibleValueRelativePos(segmentIndex,
                                                                                getFirstVertical(),
                                                                                getLastVertical(),
                                                                                0);
                double nextPos = lineHeight.getVisibleValueRelativePos(segmentIndex + 1,
                                                                       getFirstVertical(),
                                                                       getLastVertical(),
                                                                       0);

                prevPos = MathUtils.average(prevPos, currentPos);
                nextPos = MathUtils.average(nextPos, currentPos);

                final Color color = lineColor.getVisibleValue(segmentIndex, defaultColor);
                float thickness = MathUtils.clamp(this.thickness.getVisibleValue(segmentIndex, defaultLineThickness),
                                                  0,
                                                  MAX_THICKNESS);
                if (renderDropShadowNow) thickness *= dropShadowSizeFactor;

                float x1 = (float) segmentArea.getMinX();
                float x2 = (float) segmentArea.getCenterX();
                float x3 = (float) segmentArea.getMaxX();

                if (hasPrevPos) drawLine(drawContext, color, thickness, segmentArea, prevPos, currentPos, x1, x2);
                if (hasNextPos) drawLine(drawContext, color, thickness, segmentArea, currentPos, nextPos, x2, x3);
            }

            // TODO: Draw marker
        }
    }

    private void drawLine(DrawContext drawContext,
                          Color color,
                          float thickness,
                          Rectangle segmentArea,
                          double prevPos,
                          double currentPos,
                          float x1,
                          float x2) {
        float y1 = (float) MathUtils.mix(prevPos, segmentArea.getMaxY(), segmentArea.getMinY());
        float y2 = (float) MathUtils.mix(currentPos, segmentArea.getMaxY(), segmentArea.getMinY());

        if (renderDropShadowNow) {
            float offs = (int) (thickness * dropShadowOffsetFactor);
            drawContext.drawLine(dropShadowColor, x1+ offs, y1+ offs, x2+ offs, y2+ offs, thickness);
        }
        else {
            drawContext.drawLine(color, x1, y1, x2, y2, thickness);
        }
    }


}
