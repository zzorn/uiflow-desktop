package org.uiflow.desktop.chart.chartlayer;

import org.flowutils.MathUtils;
import org.flowutils.Ranged;
import org.uiflow.desktop.chart.axis.Axis;

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

    @Override public void render(Graphics2D g2, Rectangle renderArea) {

        if (dropShadow) {
            renderDropShadowNow = true;
            super.render(g2, renderArea);
        }

        renderDropShadowNow = false;
        super.render(g2, renderArea);
    }

    @Override protected void renderSegment(Graphics2D g2,
                                           Rectangle segmentArea,
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

                // TODO: add average function to MathUtils
                prevPos = MathUtils.mix(0.5, prevPos, currentPos);
                nextPos = MathUtils.mix(0.5, nextPos, currentPos);

                final Color color = lineColor.getVisibleValue(segmentIndex, defaultColor);
                float thickness = MathUtils.clamp(this.thickness.getVisibleValue(segmentIndex, defaultLineThickness),
                                                  0,
                                                  MAX_THICKNESS);
                if (renderDropShadowNow) thickness *= dropShadowSizeFactor;

                int x1 = (int) segmentArea.getMinX();
                int x2 = (int) segmentArea.getCenterX();
                int x3 = (int) segmentArea.getMaxX();

                g2.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                if (hasPrevPos) drawLine(g2, color, thickness, segmentArea, prevPos, currentPos, x1, x2);
                if (hasNextPos) drawLine(g2, color, thickness, segmentArea, currentPos, nextPos, x2, x3);
            }

            // TODO: Draw marker
        }
    }

    private void drawLine(Graphics2D g2,
                          Color color,
                          float thickness,
                          Rectangle segmentArea,
                          double prevPos,
                          double currentPos,
                          int x1,
                          int x2) {
        int y1 = (int) MathUtils.mix(prevPos, segmentArea.getMaxY(), segmentArea.getMinY());
        int y2 = (int) MathUtils.mix(currentPos, segmentArea.getMaxY(), segmentArea.getMinY());

        if (renderDropShadowNow) {
            int offs = (int) (thickness * dropShadowOffsetFactor);
            g2.setColor(dropShadowColor);
            g2.drawLine(x1+ offs, y1+ offs, x2+ offs, y2+ offs);
        }
        else {
            g2.setColor(color);
            g2.drawLine(x1, y1, x2, y2);
        }
    }


}
