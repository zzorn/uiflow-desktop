package org.uiflow.desktop.chart;


import org.flowutils.collections.dataseries.DataSeries;

import java.util.List;

/**
 *
 */
public interface Visualization {

    List<String> getChannels();

    void setData(String channelId, DataSeries dataSeries);


}
