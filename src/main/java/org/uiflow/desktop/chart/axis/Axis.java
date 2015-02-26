package org.uiflow.desktop.chart.axis;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Represents an axis for some kind of data.
 */
// TODO: Add function to get a longer more complete version of the axis label, e.g. to provide date context to a zoomed time axis.
public class Axis<T extends Number> {

    private static final NumberFormat NUMBER_FORMAT = new DecimalFormat("#.###");

    private final String name;
    private final Class<T> type;

    public Axis(String name, Class<T> type) {
        this.name = name;
        this.type = type;
    }

    /**
     * @return the name of the axis.
     */
    public String getName() {
        return name;
    }

    /**
     * @return the (number) type to use for the positions along the axis.
     */
    public Class<T> getType() {
        return type;
    }

    /**
     * @param axisValue the value to get a label for
     * @return a user readable value for the specified position on the axis.
     */
    public String getAxisLabel(T axisValue) {
        return getAxisLabel(axisValue, null, null);
    }

    /**
     * @param axisValue the value to get a label for
     * @param firstVisibleValue the first visible value on the axis.  Used to determine the precision to use for e.g. dates.
     * @param lastVisibleValue the last visible value on the axis.  Used to determine the precision to use for e.g. dates.
     * @return a user readable value for the specified position on the axis.
     */
    public String getAxisLabel(T axisValue, T firstVisibleValue, T lastVisibleValue) {

        // TODO: Add support to format things like dates.
        // TODO: Determine range of visible values, and the precision to use for the specified value depending on that

        if (axisValue instanceof Float ||
            axisValue instanceof Double) return NUMBER_FORMAT.format(axisValue);
        else {
            return axisValue.toString();
        }
    }

    @Override public String toString() {
        return "Axis{" +
               "name='" + name + '\'' +
               '}';
    }
}
