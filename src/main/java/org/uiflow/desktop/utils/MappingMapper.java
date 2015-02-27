package org.uiflow.desktop.utils;

import org.flowutils.ClassUtils;
import org.flowutils.MathUtils;

import static org.flowutils.Check.notNull;

/**
 * Maps input numbers in some range to an output range.
 *
 * Uses doubles internally.  Will not very work accurately for large long values.
 */
public final class MappingMapper<I extends Number, O extends Number> implements Mapper<I, O> {

    private double sourceStart;
    private double sourceEnd;
    private double targetStart;
    private double targetEnd;

    private boolean clampToRange = false;

    private final Class<O> outputType;

    /**
     * @param outputType type of the return value.
     */
    public MappingMapper(Class<O> outputType) {
        this(outputType, 0, 1);
    }

    /**
     * @param outputType type of the return value.
     * @param sourceStart start of the input range.
     * @param sourceEnd end of the input range.
     */
    public MappingMapper(Class<O> outputType, double sourceStart, double sourceEnd) {
        this(outputType, sourceStart, sourceEnd, 0, 1);
    }

    /**
     * @param outputType type of the return value.
     * @param sourceStart start of the input range.
     * @param sourceEnd end of the input range.
     * @param targetStart start of the output range.
     * @param targetEnd end of the output range.
     */
    public MappingMapper(Class<O> outputType,
                         double sourceStart,
                         double sourceEnd,
                         double targetStart,
                         double targetEnd) {
        this(outputType, sourceStart, sourceEnd, targetStart, targetEnd, false);
    }

    /**
     * @param outputType type of the return value.
     * @param sourceStart start of the input range.
     * @param sourceEnd end of the input range.
     * @param targetStart start of the output range.
     * @param targetEnd end of the output range.
     * @param clampToRange if true, the result will be clamped to the range from targetStart to targetEnd.
     */
    public MappingMapper(Class<O> outputType,
                         double sourceStart,
                         double sourceEnd,
                         double targetStart,
                         double targetEnd,
                         boolean clampToRange) {
        notNull(outputType, "outputType");

        this.outputType = outputType;
        this.sourceStart = sourceStart;
        this.sourceEnd = sourceEnd;
        this.targetStart = targetStart;
        this.targetEnd = targetEnd;
        this.clampToRange = clampToRange;
    }

    public void setMapping(double sourceStart, double sourceEnd) {
        setMapping(sourceStart, sourceEnd, 0, 1);
    }

    public void setMapping(double sourceStart, double sourceEnd, double targetStart, double targetEnd) {
        this.sourceStart = sourceStart;
        this.sourceEnd = sourceEnd;
        this.targetStart = targetStart;
        this.targetEnd = targetEnd;
    }

    /**
     * @return if true, the result will be clamped to the range from targetStart to targetEnd.
     */
    public boolean isClampToRange() {
        return clampToRange;
    }

    /**
     * @param clampToRange if true, the result will be clamped to the range from targetStart to targetEnd.
     */
    public void setClampToRange(boolean clampToRange) {
        this.clampToRange = clampToRange;
    }

    public double getSourceStart() {
        return sourceStart;
    }

    public double getSourceEnd() {
        return sourceEnd;
    }

    public double getTargetStart() {
        return targetStart;
    }

    public double getTargetEnd() {
        return targetEnd;
    }

    public Class<O> getOutputType() {
        return outputType;
    }

    @Override public O convert(I sourceValue) {
        double result;
        if (clampToRange) {
            result = MathUtils.mapAndClamp(sourceValue.doubleValue(), sourceStart, sourceEnd, targetStart, targetEnd);
        }
        else {
            result = MathUtils.map(sourceValue.doubleValue(), sourceStart, sourceEnd, targetStart, targetEnd);
        }

        return ClassUtils.convertNumber(result, outputType);
    }
}
