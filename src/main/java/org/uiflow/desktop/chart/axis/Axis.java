package org.uiflow.desktop.chart.axis;

/**
 *
 */
public final class Axis<T extends Number> {

    private final String name;
    private final Class<T> type;

    public Axis(String name) {
        this(name, null);
    }

    public Axis(String name, Class<T> type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Class<T> getType() {
        return type;
    }
}
