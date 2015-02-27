package org.uiflow.desktop.chart.dataseries;

import org.uiflow.desktop.chart.axis.Axis;
import org.uiflow.desktop.chart.axis.AxisView;
import org.uiflow.desktop.chart.axis.AxisViewListener;
import org.uiflow.desktop.utils.Mapper;

import static org.flowutils.Check.notNull;

/**
 * Converts values to the range 0..1, where 0 is the first visible value and 1 is the last visible value.
 * Returns values outside this range when the values extend beyond the visible range.
 */
public final class RelativePosMapper<V extends Number> implements Mapper<V, Double>,
                                                                              AxisViewListener {

    private final boolean isIntegerValues;
    private final AxisView<V> valueAxisView;

    private long firstVisibleAsLong;
    private long lastVisibleAsLong;
    private double firstVisibleAsDouble;
    private double lastVisibleAsDouble;

    /**
     * @param valueAxisView view of the axis that values should be converted for.
     */
    public RelativePosMapper(AxisView<V> valueAxisView) {
        notNull(valueAxisView, "valueAxisView");
        this.valueAxisView = valueAxisView;
        isIntegerValues = isIntegerAxis(valueAxisView);

        // Get initial first and last visible values
        onVisibleAreaChanged(valueAxisView.getAxis(), valueAxisView.getFirstVisible(), valueAxisView.getLastVisible());

        // Listen to future changes to the first and last visible values along the value axis.
        valueAxisView.addListener(this);
    }

    private boolean isIntegerAxis(AxisView<V> valueAxisView) {
        notNull(valueAxisView.getAxis(), "valueAxisView.getAxis()");
        notNull(valueAxisView.getAxis().getType(), "valueAxisView.getAxis().getType()");

        final Class<V> axisType = valueAxisView.getAxis().getType();
        return axisType == Long.class ||
               axisType == Integer.class ||
               axisType == Short.class ||
               axisType == Byte.class;
    }

    @Override public Double convert(V sourceValue) {
        if (sourceValue == null) return null;
        else {
            if (isIntegerValues) {
                if (lastVisibleAsLong == firstVisibleAsLong) return 0.5;
                else return (double)(sourceValue.longValue() - firstVisibleAsLong) / (lastVisibleAsLong - firstVisibleAsLong);
            }
            else {
                if (lastVisibleAsDouble == firstVisibleAsDouble) return 0.5;
                else return (sourceValue.doubleValue() - firstVisibleAsDouble) / (lastVisibleAsDouble - firstVisibleAsDouble);
            }
        }
    }

    @Override public void onVisibleAreaChanged(Axis axis, Number firstVisible, Number lastVisible) {
        if (axis == valueAxisView.getAxis()) {
            if (firstVisible != null) {
                firstVisibleAsDouble = firstVisible.doubleValue();
                firstVisibleAsLong = firstVisible.longValue();
            } else {
                firstVisibleAsDouble = 0.0;
                firstVisibleAsLong = 0;
            }

            if (lastVisible != null) {
                lastVisibleAsDouble = lastVisible.doubleValue();
                lastVisibleAsLong = lastVisible.longValue();
            } else {
                lastVisibleAsDouble = 0.0;
                lastVisibleAsLong = 0;
            }
        }
    }
}
