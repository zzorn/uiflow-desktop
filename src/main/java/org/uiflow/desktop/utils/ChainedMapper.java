package org.uiflow.desktop.utils;

import static org.flowutils.Check.notNull;

/**
 * Chains together two ValueConverters.
 */
public final class ChainedMapper<S, M, O> implements Mapper<S, O> {

    private final Mapper<S, M> firstConverter;
    private final Mapper<M, O> lastConverter;

    public ChainedMapper(Mapper<S, M> firstConverter,
                         Mapper<M, O> lastConverter) {
        notNull(firstConverter, "firstConverter");
        notNull(lastConverter, "lastConverter");

        this.firstConverter = firstConverter;
        this.lastConverter = lastConverter;
    }

    public static <S, M, O> ChainedMapper<S, M, O> chain(Mapper<S, M> firstConverter,
                                                         Mapper<M, O> lastConverter) {
        return new ChainedMapper<S, M, O>(firstConverter, lastConverter);
    }

    public static <S, M1, M2, O> ChainedMapper<S, M2, O> chain(Mapper<S, M1> firstConverter,
                                                               Mapper<M1, M2> midConverter,
                                                               Mapper<M2, O> lastConverter) {
        return new ChainedMapper<S, M2, O>(chain(firstConverter, midConverter), lastConverter);
    }

    public static <S, M1, M2, M3, O> ChainedMapper<S, M3, O> chain(Mapper<S, M1> firstConverter,
                                                                   Mapper<M1, M2> mid1Converter,
                                                                   Mapper<M2, M3> mid2Converter,
                                                                   Mapper<M3, O> lastConverter) {
        return new ChainedMapper<S, M3, O>(chain(firstConverter, mid1Converter, mid2Converter), lastConverter);
    }

    public Mapper<S, M> getFirstConverter() {
        return firstConverter;
    }

    public Mapper<M, O> getLastConverter() {
        return lastConverter;
    }

    @Override public O convert(S sourceValue) {
        final M intermediateValue = firstConverter.convert(sourceValue);
        return lastConverter.convert(intermediateValue);
    }
}
