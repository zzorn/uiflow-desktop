package org.uiflow.desktop.chart.chartlayer;

import org.flowutils.Check;
import org.flowutils.ClassUtils;
import org.flowutils.collections.dataseries.Axis;
import org.flowutils.collections.dataseries.IndexedDataSeries;

import java.awt.*;
import java.util.Iterator;

/**
 * Base class for chart layers that consist of several vertical segments along the horizontal axis.
 * E.g. barcharts, linecharts, and so on.
 */
public abstract class SegmentedChartLayerBase<T extends Number, V extends Number> extends ChartLayerBase<T, V> {

    protected static final int DEFAULT_SEGMENT_COUNT = 20;

    private int defaultNumberOfSegments;
    private int segmentXGap = 2;
    private boolean clipRenderingOfSegment = false;

    private final Rectangle tempSegmentArea = new Rectangle();

    protected SegmentedChartLayerBase(Axis<T> horizontalAxis,
                                   Axis<V> verticalAxis) {
        this(horizontalAxis, verticalAxis, DEFAULT_SEGMENT_COUNT);
    }

    protected SegmentedChartLayerBase(Axis<T> horizontalAxis, Axis<V> verticalAxis, int defaultNumberOfSegments) {
        super(horizontalAxis, verticalAxis);
        setDefaultNumberOfSegments(defaultNumberOfSegments);
    }

    /**
     * @return number of segments to display when the step size is not available from the primary visualization channel DataSeries.
     */
    public final int getDefaultNumberOfSegments() {
        return defaultNumberOfSegments;
    }

    /**
     * @param defaultNumberOfSegments number of segments to display when the step size is not available from the primary visualization channel DataSeries.
     */
    public final void setDefaultNumberOfSegments(int defaultNumberOfSegments) {
        Check.positive(defaultNumberOfSegments, "defaultNumberOfSegments");

        this.defaultNumberOfSegments = defaultNumberOfSegments;
    }

    /**
     * @return number of pixels separating the segments.
     */
    public final int getSegmentXGap() {
        return segmentXGap;
    }

    /**
     * @param segmentXGap number of pixels separating the segments.
     */
    public final void setSegmentXGap(int segmentXGap) {
        this.segmentXGap = segmentXGap;
    }


    /**
     * @return if true, rendering of a segment will be clipped to only the area of the segment.
     */
    public final boolean isClipRenderingOfSegment() {
        return clipRenderingOfSegment;
    }

    /**
     * @param clipRenderingOfSegment if true, rendering of a segment will be clipped to only the area of the segment.
     */
    public final void setClipRenderingOfSegment(boolean clipRenderingOfSegment) {
        this.clipRenderingOfSegment = clipRenderingOfSegment;
    }

    @Override public void render(Graphics2D g2, Rectangle renderArea) {
        // Determine number of segments to render
        int numberOfSegments = getNumberOfSegments();

        // If we are scrolled to halfway overlap one segment at the start, show the half of the segment after the last segment as well.
        final int firstSegmentXOffset = getFirstSegmentXOffset();
        if (firstSegmentXOffset < 0) {
            numberOfSegments += 1;
        }

        // Skip rendering if there is nothing to render
        if (numberOfSegments <= 0) return;

        // Allow the implementation to fetch all the data that should be visualized from the visualization channels into temporary arrays.
        T stepSize = ClassUtils.divNumbers(ClassUtils.subNumbers(getLastHorizontal(), getFirstHorizontal()),
                                           ClassUtils.convertNumber(numberOfSegments, getFirstHorizontal()));
        fetchVisualizationData(getFirstHorizontal(), getLastHorizontal(), stepSize, numberOfSegments);

        // Get old clip area so we can restore it if we are going to clip segments
        final Shape oldClipArea = g2.getClip();

        // Determine segment start positions and size
        int x = renderArea.x + firstSegmentXOffset;
        int y = renderArea.y;
        int w = (int) ((renderArea.getWidth()) / numberOfSegments) - segmentXGap;
        int h = renderArea.height;
        for (int i = 0; i < numberOfSegments; i++) {
            // Define area for current segment
            tempSegmentArea.setBounds(x, y, w, h);

            // Restrict rendering to only segment area, if requested.
            if (clipRenderingOfSegment) {
                g2.setClip(tempSegmentArea);
            }

            // Render the segment
            renderSegment(g2, tempSegmentArea, i, numberOfSegments);

            // Move to next segment
            x += w + segmentXGap;
        }

        // Restore clipping area
        if (clipRenderingOfSegment) {
            g2.setClip(oldClipArea);
        }
    }

    /**
     * Fetches the data for the visible area for each visualization channel.
     */
    protected void fetchVisualizationData(T start, T end, T stepSize, int numberOfSegments) {
        for (VisualizationChannel<T, ?> visualizationChannel : getVisualizationChannels()) {
            visualizationChannel.fetchVisibleValues(start, stepSize, numberOfSegments);
        }
    }

    /**
     * Render a single segment.
     * Use the getVisibleValue(index) methods on the visualization channels to get the values to use when rendering the segment.
     *
     * @param segmentArea area that the segment should be rendered in.
     * @param segmentIndex index of the segment among the visible segments, starting with 0 at the leftmost segment.
     * @param numberOfVisibleSegments total number of visible segments.
     */
    protected abstract void renderSegment(Graphics2D g2, Rectangle segmentArea, int segmentIndex, int numberOfVisibleSegments);

    /**
     * @return number of segments to draw on the screen.
     */
    protected int getNumberOfSegments() {
        // If we have a primary visualization channel, try to determine its step size and calculate the
        // number of segments based on that and the visible interval.
        final VisualizationChannel<T, ?> primaryVisualizationChannel = getPrimaryVisualizationChannel();
        if (getFirstHorizontal() != null &&
            getLastHorizontal() != null &&
            primaryVisualizationChannel != null &&
            primaryVisualizationChannel.getData() != null &&
            primaryVisualizationChannel.getData() instanceof IndexedDataSeries &&
            ((IndexedDataSeries)primaryVisualizationChannel.getData()).getStepSize() != null) {

            final T visibleRange = ClassUtils.subNumbers(getLastHorizontal(), getFirstHorizontal());
            final Number stepSize = ((IndexedDataSeries) primaryVisualizationChannel.getData()).getStepSize();
            final int numberOfVisibleSegments = Math.abs(ClassUtils.divNumbers(visibleRange, stepSize).intValue());
            return numberOfVisibleSegments;
        }
        else {
            return getDefaultNumberOfSegments();
        }
    }

    /**
     * @return offset from left side of the visible area to the start of the first segment.  May be negative (usually is).
     */
    protected int getFirstSegmentXOffset() {
        return 0;
    }

    /**
     * @return The visualization channel to use for segmentation (if it has an IndexedDataSeries).
     */
    protected VisualizationChannel<T, ?> getPrimaryVisualizationChannel() {
        // By default the first registered visualization channel is the primary one.
        final Iterator<VisualizationChannel<T, ?>> channels = getVisualizationChannels().iterator();
        if (!channels.hasNext()) {
            return null;
        }
        else {
            return channels.next();
        }
    }

}
