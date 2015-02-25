package org.uiflow.desktop.chart;

import org.uiflow.desktop.chart.axis.AxisOrientation;
import org.uiflow.desktop.chart.axis.DefaultAxisView;
import org.uiflow.desktop.ui.SimpleFrame;

/**
 *
 */
public class ChartExample {

    public static void main(String[] args) {

        DefaultChart chart = new DefaultChart();

        chart.addAxisView(new DefaultAxisView("Time", 1600, 2000, AxisOrientation.HORIZONTAL_BOTTOM));
        chart.addAxisView(new DefaultAxisView("Number of Pirates", 0, 1000, AxisOrientation.VERTICAL_RIGHT));
        chart.addAxisView(new DefaultAxisView("Random Walk", 0.0, 1.0, AxisOrientation.VERTICAL_RIGHT));
        chart.addAxisView(new DefaultAxisView("Global Warming", -10, 10, AxisOrientation.VERTICAL_LEFT));

        new SimpleFrame("Chart Example", chart.getUi());

    }

}
