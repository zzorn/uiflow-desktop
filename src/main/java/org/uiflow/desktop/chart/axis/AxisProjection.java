package org.uiflow.desktop.chart.axis;

/**
 * Projection of an axis to the screen (and back).
 */
public interface AxisProjection<T extends Number> {

    /**
     * @param relativePosition relative screen position, 0 = at first visible value, 1 = at last visible value.
     * @param firstVisible first visible value of the axis.
     * @param lastVisible last visible value of the axis.
     * @return axis value for the relative screen position between first and last visible axis value.
     */
    T getAxisValue(double relativePosition, T firstVisible, T lastVisible);

    /**
     * @param axisValue value on the axis to get the coordinate for
     * @param firstVisible first visible value of the axis.
     * @param lastVisible last visible value of the axis.
     * @return location between 0 and 1, indicating the relative position that the axisValue should be drawn at,
     * between firstVisible and lastVisible.
     */
    double getVisibleLocation(T axisValue, T firstVisible, T lastVisible);

}
