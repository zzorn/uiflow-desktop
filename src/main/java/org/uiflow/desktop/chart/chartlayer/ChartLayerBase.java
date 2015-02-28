package org.uiflow.desktop.chart.chartlayer;

import org.flowutils.Ranged;
import org.uiflow.desktop.chart.axis.Axis;
import org.uiflow.desktop.chart.dataseries.DataSeries;

import java.awt.*;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.flowutils.Check.notNull;

/**
 *
 */
public abstract class ChartLayerBase<T extends Number, V extends Number> implements ChartLayer<T, V> {

    private final Axis<T> horizontalAxis;
    private final Axis<V> verticalAxis;

    private final Map<String, VisualizationChannel<T, ?>> channels = new LinkedHashMap<String, VisualizationChannel<T, ?>>();

    private V firstVertical;
    private V lastVertical;
    private T firstHorizontal;
    private T lastHorizontal;

    protected ChartLayerBase(Axis<T> horizontalAxis, Axis<V> verticalAxis) {
        notNull(horizontalAxis, "horizontalAxis");
        notNull(verticalAxis, "verticalAxis");

        this.horizontalAxis = horizontalAxis;
        this.verticalAxis = verticalAxis;
    }

    public final Axis<T> getHorizontalAxis() {
        return horizontalAxis;
    }

    public final Axis<V> getVerticalAxis() {
        return verticalAxis;
    }

    @Override public final void onVisibleAreaChanged(Axis axis, Number firstVisible, Number lastVisible) {
        if (axis == verticalAxis) {
            firstVertical = (V) firstVisible;
            lastVertical = (V) lastVisible;
            onVerticalVisibleAreaChanged();
        }
        else if (axis == horizontalAxis) {
            firstHorizontal = (T) firstVisible;
            lastHorizontal = (T) lastVisible;
            onHorizontalVisibleAreaChanged();
        }
    }

    /**
     * Creates a new VisualizationChannel with the same type as the vertical axis and adds it to this ChartLayer.
     * @param name name of the channel.
     * @return the created visualization channel.
     */
    protected final <C> VisualizationChannel<T, V> addVerticalChannel(String name){
        return addChannel(name, verticalAxis.getType());
    }

    /**
     * Creates a new VisualizationChannel with the same type as the horizontal axis and adds it to this ChartLayer.
     * @param name name of the channel.
     * @return the created visualization channel.
     */
    protected final <C> VisualizationChannel<T, T> addHorizontalChannel(String name){
        return addChannel(name, horizontalAxis.getType());
    }

    /**
     * Creates a new VisualizationChannel with number type and adds it to this ChartLayer.
     * @param name name of the channel.
     * @return the created visualization channel.
     */
    protected final <C> VisualizationChannel<T, Number> addNumberChannel(String name){
        return addChannel(name, Number.class);
    }

    /**
     * Creates a new VisualizationChannel with double type and adds it to this ChartLayer.
     * @param name name of the channel.
     * @return the created visualization channel.
     */
    protected final <C> VisualizationChannel<T, Double> addDoubleChannel(String name){
        return addChannel(name, Double.class);
    }

    /**
     * Creates a new VisualizationChannel with float type and adds it to this ChartLayer.
     * @param name name of the channel.
     * @return the created visualization channel.
     */
    protected final <C> VisualizationChannel<T, Float> addFloatChannel(String name){
        return addChannel(name, Float.class);
    }

    /**
     * Creates a new VisualizationChannel with integer type and adds it to this ChartLayer.
     * @param name name of the channel.
     * @return the created visualization channel.
     */
    protected final <C> VisualizationChannel<T, Integer> addIntChannel(String name){
        return addChannel(name, Integer.class);
    }

    /**
     * Creates a new VisualizationChannel with Ranged type and adds it to this ChartLayer.
     * @param name name of the channel.
     * @return the created visualization channel.
     */
    protected final <C> VisualizationChannel<T, Ranged> addRangedChannel(String name){
        return addChannel(name, Ranged.class);
    }

    /**
     * Creates a new VisualizationChannel with color type and adds it to this ChartLayer.
     * @param name name of the channel.
     * @return the created visualization channel.
     */
    protected final <C> VisualizationChannel<T, Color> addColorChannel(String name){
        return addChannel(name, Color.class);
    }

    /**
     * Creates a new VisualizationChannel with boolean type and adds it to this ChartLayer.
     * @param name name of the channel.
     * @return the created visualization channel.
     */
    protected final <C> VisualizationChannel<T, Boolean> addBoolChannel(String name){
        return addChannel(name, Boolean.class);
    }

    /**
     * Creates a new VisualizationChannel and adds it to this ChartLayer.
     * @param name name of the channel.
     * @param type type of value in the channel.
     * @return the created visualization channel.
     */
    protected final <C> VisualizationChannel<T, C> addChannel(String name, Class<C> type){
        final VisualizationChannel<T, C> channel = new VisualizationChannel<T, C>(name, type, getHorizontalAxis());
        channels.put(name, channel);
        return channel;
    }

    @Override public final Collection<VisualizationChannel<T, ?>> getVisualizationChannels() {
        return Collections.unmodifiableCollection(channels.values());
    }

    @Override public final VisualizationChannel<T, ?> getVisualizationChannel(String name) {
        return channels.get(name);
    }

    @Override public final void setData(String visualizationChannel, DataSeries dataSeries) {
        final VisualizationChannel<T, ?> channel = getVisualizationChannel(visualizationChannel);
        if (channel != null) {
            channel.setData(dataSeries);
        }
        else {
            throw new IllegalArgumentException("Unknown visualization channel '" + visualizationChannel + "'");
        }
    }

    protected final V getFirstVertical() {
        return firstVertical;
    }

    protected final V getLastVertical() {
        return lastVertical;
    }

    protected final T getFirstHorizontal() {
        return firstHorizontal;
    }

    protected final T getLastHorizontal() {
        return lastHorizontal;
    }


    protected void onVerticalVisibleAreaChanged() {
    }

    protected void onHorizontalVisibleAreaChanged() {
    }
}
