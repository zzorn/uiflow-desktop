package org.uiflow.desktop.chart.axis;

/**
 *
 */
public interface AxisViewListener {

    void onVisibleAreaChanged(Axis axis, Number firstVisible, Number lastVisible);

}
