package org.uiflow.desktop.chart.axis;

import org.flowutils.ClassUtils;
import org.flowutils.MathUtils;

/**
 *
 */
public class LinearAxisProjection<T extends Number> implements AxisProjection<T> {

    public static final LinearAxisProjection LINEAR_AXIS_PROJECTION = new LinearAxisProjection();

    @Override public T getAxisValue(double relativePosition, T firstVisible, T lastVisible) {
        if (firstVisible instanceof  Long) {
            // Handle long separately as the precision in doubles may not be enough to handle large longs such as timestamps.
            final long range = lastVisible.longValue() - firstVisible.longValue();
            final long result = firstVisible.longValue() + (long) (relativePosition * range);
            return (T) Long.valueOf(result);
        }
        else {
            final double result = MathUtils.mix(relativePosition,
                                                firstVisible.doubleValue(),
                                                lastVisible.doubleValue());
            return ClassUtils.convertNumber(result, firstVisible);
        }
    }

    @Override public double getVisibleLocation(T axisValue, T firstVisible, T lastVisible) {
        if (firstVisible instanceof  Long) {
            // Handle long separately as the precision in doubles may not be enough to handle large longs such as timestamps.
            final long range = lastVisible.longValue() - firstVisible.longValue();
            long pos = axisValue.longValue() - firstVisible.longValue();
            return (double) pos / range;
        }
        else {
            return MathUtils.map(axisValue.doubleValue(),
                                 firstVisible.doubleValue(),
                                 lastVisible.doubleValue(),
                                 0,
                                 1);
        }
    }
}
