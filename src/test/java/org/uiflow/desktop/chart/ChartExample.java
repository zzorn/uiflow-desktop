package org.uiflow.desktop.chart;

import org.uiflow.desktop.chart.axis.Axis;
import org.uiflow.desktop.chart.axis.AxisOrientation;
import org.uiflow.desktop.chart.axis.DefaultAxisView;
import org.uiflow.desktop.chart.axis.TimeAxis;
import org.uiflow.desktop.ui.SimpleFrame;

import java.util.Date;

/**
 *
 */
public class ChartExample {

    public static void main(String[] args) {

        DefaultChart chart = new DefaultChart();

        final TimeAxis time = new TimeAxis();
        final Axis<Integer> numberOfPirates = new Axis<Integer>("Number of Pirates", Integer.class);
        final Axis<Double> globalWarming = new Axis<Double>("Global Warming", Double.class);

        chart.addAxisView(new DefaultAxisView<Long>(time, Date.UTC(-200, 1, 1, 0, 0, 0), Date.UTC(100, 1, 1, 0, 0, 0), AxisOrientation.HORIZONTAL_BOTTOM));
        chart.addAxisView(new DefaultAxisView<Integer>(numberOfPirates, 0, 1000, AxisOrientation.VERTICAL_RIGHT));
        chart.addAxisView(new DefaultAxisView<Double>(globalWarming, -5.0, 5.0, AxisOrientation.VERTICAL_LEFT));

        new SimpleFrame("Chart Example", chart.getUi());

    }

}
