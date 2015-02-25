package org.uiflow.desktop.chart;

import java.util.List;

/**
 *
 */
public interface Visualization {

    List<String> getChannels();

    void setData(String channelId, DataSeries dataSeries);


}
