package org.uiflow.desktop.chart;

import org.uiflow.desktop.chart.axis.Axis;
import org.uiflow.desktop.chart.axis.AxisOrientation;
import org.uiflow.desktop.chart.axis.DefaultAxisView;
import org.uiflow.desktop.ui.SimpleFrame;

/**
 *
 */
public class ChartExample {

    public static void main(String[] args) {

        DefaultChart chart = new DefaultChart();

        final Axis time = new Axis("Time");
        final Axis numberOfPirates = new Axis("Number of Pirates");
        final Axis globalWarming = new Axis("Global Warming");

        chart.addAxisView(new DefaultAxisView(time, 1600, 2000, AxisOrientation.HORIZONTAL_BOTTOM));
        chart.addAxisView(new DefaultAxisView(numberOfPirates, 0, 1000, AxisOrientation.VERTICAL_RIGHT));
        chart.addAxisView(new DefaultAxisView(globalWarming, -10, 10, AxisOrientation.VERTICAL_LEFT));

        new SimpleFrame("Chart Example", chart.getUi());

    }

}
