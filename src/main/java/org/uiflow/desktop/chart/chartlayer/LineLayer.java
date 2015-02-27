package org.uiflow.desktop.chart.chartlayer;

import org.flowutils.MathUtils;
import org.uiflow.desktop.chart.axis.Axis;

import java.awt.*;

/**
 *
 */
public class LineLayer<T extends Number, V extends Number> extends SegmentedChartLayerBase<T, V> {

    public VisualizationChannel<T, Color> lineColor;

    public VisualizationChannel<T, V> lineHeight;

    private float defaultLineThickness = 1;
    private Color defaultColor = Color.DARK_GRAY;

    public LineLayer(Axis<T> horizontalAxis,
                        Axis<V> verticalAxis) {
        super(horizontalAxis, verticalAxis);
        setSegmentXGap(0);
    }

    public LineLayer(Axis<T> horizontalAxis, Axis<V> verticalAxis, int defaultNumberOfSegments) {
        super(horizontalAxis, verticalAxis, defaultNumberOfSegments);
        setSegmentXGap(0);
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

    @Override protected void registerChannels() {
        lineHeight = addVerticalChannel("Line Height");
        lineColor = addColorChannel("Line Color");

        // TODO: Add thickness channel, of Range/Interval type
        // TODO: Add marker channel, with marker type?


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
                float thickness = defaultLineThickness;

                int x1 = (int) segmentArea.getMinX();
                int x2 = (int) segmentArea.getCenterX();
                int x3 = (int) segmentArea.getMaxX();

                g2.setColor(color);
                g2.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                if (hasPrevPos) drawLine(g2, segmentArea, prevPos, currentPos, x1, x2);
                if (hasNextPos) drawLine(g2, segmentArea, currentPos, nextPos, x2, x3);
            }

            // TODO: Draw marker
        }
    }

    private void drawLine(Graphics2D g2, Rectangle segmentArea, double prevPos, double currentPos, int x1, int x2) {
        int y1 = (int) MathUtils.mix(prevPos, segmentArea.getMaxY(), segmentArea.getMinY());
        int y2 = (int) MathUtils.mix(currentPos, segmentArea.getMaxY(), segmentArea.getMinY());

        g2.drawLine(x1, y1, x2, y2);
    }


}
