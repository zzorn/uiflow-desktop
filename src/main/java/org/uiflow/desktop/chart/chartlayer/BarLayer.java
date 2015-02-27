package org.uiflow.desktop.chart.chartlayer;

import org.uiflow.desktop.chart.axis.Axis;

import java.awt.*;

/**
 *
 */
public class BarLayer<T extends Number, V extends Number> extends SegmentedChartLayerBase<T, V> {

    public VisualizationChannel<T, V> barHeight;
    public VisualizationChannel<T, Color> barColor;

    private int edgeThickness = 2;

    public BarLayer(Axis<T> horizontalAxis,
                       Axis<V> verticalAxis) {
        super(horizontalAxis, verticalAxis);
    }

    @Override protected void registerChannels() {
        barHeight = addVerticalChannel("Bar Height");
        barColor = addColorChannel("Bar Color");
    }

    @Override protected void renderSegment(Graphics2D g2, Rectangle segmentArea, int segmentIndex, int numberOfVisibleSegments) {
        Color color = barColor.getVisibleValue(segmentIndex, Color.DARK_GRAY);
        int height = (int) (segmentArea.height * barHeight.getVisibleValueRelativePos(segmentIndex, getFirstVertical(), getLastVertical(), 0));
        final int y = segmentArea.y + segmentArea.height - height;

        g2.setColor(color.darker());
        g2.fillRect(segmentArea.x, y, segmentArea.width, height);
        g2.setColor(color);
        g2.fillRect(segmentArea.x+edgeThickness, y+edgeThickness, segmentArea.width-2*edgeThickness, height-2*edgeThickness);
    }

}
