package org.uiflow.desktop.chart.chartlayer;

import org.uiflow.desktop.chart.axis.Axis;
import org.uiflow.desktop.chart.axis.AxisViewListener;
import org.uiflow.desktop.ui.Renderable;

import java.awt.*;

/**
 *
 */
public interface ChartLayer extends Renderable, AxisViewListener {

    Axis getVerticalAxis();

    Axis getHorizontalAxis();



}
