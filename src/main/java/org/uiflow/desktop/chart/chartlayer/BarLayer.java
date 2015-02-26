package org.uiflow.desktop.chart.chartlayer;

import org.flowutils.ClassUtils;
import org.flowutils.MathUtils;
import org.uiflow.desktop.chart.axis.Axis;
import org.uiflow.desktop.chart.dataseries.DataSeries;
import org.uiflow.desktop.chart.dataseries.IndexedDataSeries;

import java.awt.*;
import java.util.ArrayList;

/**
 *
 */
public class BarLayer<T extends Number, V extends Number> extends ChartLayerBase<T, V> {

    private static final int DEFAULT_NUMBER_OF_BARS = 20;
    public VisualizationChannel<T, V> barHeight;
    public VisualizationChannel<T, Color> barColor;

    private final ArrayList<V> tempBarHeights = new ArrayList<V>();
    private final ArrayList<Color> tempBarColors = new ArrayList<Color>();

    public BarLayer(Axis<T> horizontalAxis,
                       Axis<V> verticalAxis) {
        super(horizontalAxis, verticalAxis);
    }

    @Override protected void registerChannels() {
        barHeight = addVerticalChannel("Bar Height");
        barColor = addColorChannel("Bar Color");
    }

    @Override public void render(Graphics2D g2, Rectangle renderArea) {
        // TODO: Determine number of samples available in the range
        // Use something like: barHeight.getData().getNumberOfSteps(first, last, defaultNumberOfSteps)

        // TODO: Split into a generic render method and a render method in the Base that gets called for each
        // TODO  bar/sample of the most important visualization channel?
        // TODO: If the most important channel is not an IndexedDataSeries, use some default number of steps to divide the chart into, or some specific number of pixels.
        // (Or come up with a stepWidth based on the visible range)

        final T firstHorizontal = getFirstHorizontal();
        final T lastHorizontal = getLastHorizontal();

        final T stepSize = calculateStepSize();
        int numberOfSteps = Math.abs(ClassUtils.divNumbers(ClassUtils.subNumbers(lastHorizontal, firstHorizontal),
                                                           stepSize).intValue());
        barHeight.getData().getValues(firstHorizontal, stepSize, numberOfSteps, tempBarHeights);
        barColor.getData().getValues(firstHorizontal, stepSize, numberOfSteps, tempBarColors);

        int barWidth = (int) MathUtils.map(1, 0, tempBarHeights.size(), renderArea.getMinX(), renderArea.getMaxX());
        for (int i = 0; i < tempBarHeights.size(); i++) {
            final int barY = (int) MathUtils.map(tempBarHeights.get(i).floatValue(),
                                                 getFirstVertical().floatValue(),
                                                 getLastVertical().floatValue(),
                                                 (float) renderArea.getMaxY(),
                                                 (float) renderArea.getMinY());
            int barX1 = (int) MathUtils.map(i,     0, tempBarHeights.size(), renderArea.getMinX(), renderArea.getMaxX());

            final Color barColor = tempBarColors.get(i);

            g2.setColor(barColor);
            g2.fillRect(barX1, barY, barWidth, (int) (renderArea.getMaxY() - barY));
        }

    }

    private T calculateStepSize() {
        final T firstHorizontal = getFirstHorizontal();
        final T lastHorizontal = getLastHorizontal();

        T stepSize = ClassUtils.divNumbers(ClassUtils.subNumbers(lastHorizontal, firstHorizontal),
                                           ClassUtils.convertNumber(DEFAULT_NUMBER_OF_BARS, firstHorizontal));

        final DataSeries<T, V> barData = barHeight.getData();
        if (barData != null && barData instanceof IndexedDataSeries<?, ?>) {
            IndexedDataSeries indexedBarData = (IndexedDataSeries) barData;
            if (indexedBarData.getStepSize() != null) stepSize = (T) indexedBarData.getStepSize();
        }

        return stepSize;
    }
}
