package org.uiflow.desktop.chart;

import org.uiflow.desktop.chart.dataseries.DataSeries;

import java.util.List;

/**
 *
 */
public interface Visualization {

    List<String> getChannels();

    void setData(String channelId, DataSeries dataSeries);


}
