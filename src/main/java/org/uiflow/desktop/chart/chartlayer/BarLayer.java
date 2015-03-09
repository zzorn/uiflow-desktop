package org.uiflow.desktop.chart.chartlayer;

import org.flowutils.Ranged;
import org.flowutils.collections.dataseries.Axis;
import org.flowutils.drawcontext.DrawContext;
import org.flowutils.rectangle.Rectangle;

import java.awt.Color;


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

    @Override protected void renderSegment(DrawContext drawContext,
                                           Rectangle segmentArea,
                                           int segmentIndex,
                                           int numberOfVisibleSegments) {
        Color color = barColor.getVisibleValue(segmentIndex, Color.DARK_GRAY);
        float height = (float) (segmentArea.getSizeY() * barHeight.getVisibleValueRelativePos(segmentIndex, getFirstVertical(), getLastVertical(), 0));
        final float y = (float) segmentArea.getMinY() + (float) segmentArea.getSizeY()- height;

        float width = (float) barWidth.getVisibleValue(segmentIndex, DEFAULT_SEGMENT_WIDTH).map(segmentArea.getSizeX());
        float x = (float) (segmentArea.getMinX() + (segmentArea.getSizeX() - width) / 2f);

        drawContext.outlineRectangle(color.darker(), x, y, width, height, color, edgeThickness);
    }

}
