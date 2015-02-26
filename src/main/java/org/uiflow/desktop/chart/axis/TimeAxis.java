package org.uiflow.desktop.chart.axis;

import java.util.Date;

/**
 * An axis for time.
 * Assumes the values are milliseconds since the epoch (1970), same format as used by Date.
 */
public final class TimeAxis extends Axis<Long> {

    private final Date tempDate = new Date();

    public TimeAxis() {
        this("Time");
    }

    public TimeAxis(String name) {
        super(name, Long.class);
    }

    public Date toDate(Long time) {
        if (time == null) return null;
        else return new Date(time);
    }

    public Long fromDate(Date date) {
        if (date == null) return null;
        else return date.getTime();
    }

    @Override public String getAxisLabel(Long axisValue, Long firstVisibleValue, Long lastVisibleValue) {
        if (axisValue == null) return "";

        // TODO: Determine the best format to use depending on the size of the visible interval (years, years+months, months+days, days+time, time+minutes, etc.

        tempDate.setTime(axisValue);
        return tempDate.toGMTString();
    }
}
