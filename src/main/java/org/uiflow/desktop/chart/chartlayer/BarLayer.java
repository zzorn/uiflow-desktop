package org.uiflow.desktop.chart.chartlayer;

import org.flowutils.Ranged;
import org.uiflow.desktop.chart.axis.Axis;

import java.awt.*;

/**
 *
 */
public class BarLayer<T extends Number, V extends Number> extends SegmentedChartLayerBase<T, V> {

    private static final Ranged DEFAULT_SEGMENT_WIDTH = new Ranged(1);

    public final VisualizationChannel<T, V> barHeight;
    public final VisualizationChannel<T, Color> barColor;
    public final VisualizationChannel<T, Ranged> barWidth;

    private int edgeThickness = 2;

    public BarLayer(Axis<T> horizontalAxis,
                       Axis<V> verticalAxis) {
        super(horizontalAxis, verticalAxis);

        barHeight = addVerticalChannel("Height");
        barColor = addColorChannel("Color");
        barWidth = addRangedChannel("Width");
    }

    @Override protected void renderSegment(Graphics2D g2, Rectangle segmentArea, int segmentIndex, int numberOfVisibleSegments) {
        Color color = barColor.getVisibleValue(segmentIndex, Color.DARK_GRAY);
        int height = (int) (segmentArea.height * barHeight.getVisibleValueRelativePos(segmentIndex, getFirstVertical(), getLastVertical(), 0));
        final int y = segmentArea.y + segmentArea.height - height;

        int width = barWidth.getVisibleValue(segmentIndex, DEFAULT_SEGMENT_WIDTH).map(segmentArea.width);
        int x =  segmentArea.x + (segmentArea.width - width) / 2;

        g2.setColor(color.darker());
        g2.fillRect(x, y, width, height);
        g2.setColor(color);
        g2.fillRect(x+edgeThickness, y+edgeThickness, width-2*edgeThickness, height-2*edgeThickness);
    }

}
