package org.uiflow.desktop.chart.axis;

import org.flowutils.collections.dataseries.Axis;

/**
 *
 */
public interface AxisViewListener {

    void onVisibleAreaChanged(Axis axis, Number firstVisible, Number lastVisible);

}
