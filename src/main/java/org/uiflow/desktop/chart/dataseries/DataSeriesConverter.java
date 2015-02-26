package org.uiflow.desktop.chart.dataseries;

import org.uiflow.desktop.chart.axis.Axis;
import org.uiflow.desktop.utils.ValueConverter;

import java.util.List;

/**
 * Converts a DataSeries with one types of values to a DataSeries with another type of values, using a ValueConverter to convert the values.
 */
public class DataSeriesConverter<T extends Number, S, O> extends DataSeriesBase<T, O> {

    private DataSeries<T, S> sourceSeries;
    private ValueConverter<S, O> valueConverter;

    private final DataSeriesListener<T, S> seriesListener = new DataSeriesListener<T, S>() {
        @Override public void onValueAddedToEnd(DataSeries<T, S> dataSeries, T position, S value) {
            notifyValueAdded(position, convertValue(value));
        }

        @Override public void onSeriesModified(DataSeries<T, S> dataSeries) {
            notifySeriesModified();
        }
    };

    /**
     * @param seriesAxis axis that this DataSeries uses for the position of data values.
     */
    public DataSeriesConverter(Axis<T> seriesAxis) {
        super(seriesAxis);
    }

    /**
     * @param seriesAxis axis that this DataSeries uses for the position of data values.
     * @param valueConverter the value converter to use for converting source values to the correct output type.
     */
    public DataSeriesConverter(Axis<T> seriesAxis,
                                  ValueConverter<S, O> valueConverter) {
        super(seriesAxis);
        setValueConverter(valueConverter);
    }

    /**
     * @param sourceSeries the data series to get the source data from.
     */
    public DataSeriesConverter(DataSeries<T, S> sourceSeries) {
        super(sourceSeries.getSeriesAxis());
        setSourceSeries(sourceSeries);
    }

    /**
     * @param sourceSeries the data series to get the source data from.
     * @param valueConverter the value converter to use for converting source values to the correct output type.
     */
    public DataSeriesConverter(DataSeries<T, S> sourceSeries,
                                  ValueConverter<S, O> valueConverter) {
        super(sourceSeries.getSeriesAxis());
        setSourceSeries(sourceSeries);
        setValueConverter(valueConverter);
    }

    /**
     * @return the data series to get the source data from.
     */
    public final DataSeries<T, S> getSourceSeries() {
        return sourceSeries;
    }

    /**
     * @param sourceSeries the data series to get the source data from.
     */
    public final void setSourceSeries(DataSeries<T, S> sourceSeries) {
        if (sourceSeries != null &&
            sourceSeries.getSeriesAxis() != getSeriesAxis()) {
            throw new IllegalArgumentException("The series axis of the specified sourceSeries does not match the current axis " + getSeriesAxis());
        }

        if (this.sourceSeries != sourceSeries) {
            if (this.sourceSeries != null) {
                this.sourceSeries.removeListener(seriesListener);
            }

            this.sourceSeries = sourceSeries;

            if (this.sourceSeries != null) {
                this.sourceSeries.addListener(seriesListener);
            }
        }
    }

    /**
     * @return the value converter to use for converting source values to the correct output type.
     */
    public final ValueConverter<S, O> getValueConverter() {
        return valueConverter;
    }

    /**
     * @param valueConverter the value converter to use for converting source values to the correct output type.
     */
    public final void setValueConverter(ValueConverter<S, O> valueConverter) {
        this.valueConverter = valueConverter;
    }

    @Override public O getValue(T position) {
        final S sourceValue = sourceSeries.getValue(position);

        if (sourceValue == null) return null;
        else return convertValue(sourceValue);
    }

    @Override public List<O> getValues(T startPosition, T stepSize, int valueCount, List<O> outputList) {
        if (sourceSeries != null &&
            sourceSeries instanceof IndexedDataSeries &&
            stepSize.equals(((IndexedDataSeries) sourceSeries).getStepSize())) {

            // Optimize the case for indexed source data series

            outputList.clear();

            final IndexedDataSeries<T, S> indexedSourceSeries = (IndexedDataSeries<T, S>) sourceSeries;

            long index = indexedSourceSeries.getIndexOf(startPosition);
            final long endIndex = index + valueCount;
            for (; index < endIndex; index++) {
                final S sourceValue = indexedSourceSeries.getValueAtIndex(index);

                if (sourceValue == null) {
                    outputList.add(null);
                }
                else {
                    outputList.add(convertValue(sourceValue));
                }
            }

            return outputList;
        }
        else {
            // Use default implementation
            return super.getValues(startPosition, stepSize, valueCount, outputList);
        }
    }

    /**
     * Convert the source value to the output value type.
     * Uses the valueConverter by default if it is specified.
     */
    protected O convertValue(S sourceValue) {
        if (valueConverter != null) {
            return valueConverter.convert(sourceValue);
        } else {
            return null;
        }
    }
}
