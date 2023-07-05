package ru.arkantos.solr.params.converter;

import java.util.function.Function;

public interface Converter<FIELD, RESULT> {

    String getName();

    RESULT convert(Object obj);

    static <F, R> Converter<F, R> defaultConverter(String path, String name, Function<F, R> converterFn) {
        return new ParameterisedConverter<>(path, name, converterFn);
    }

    static <F, R> Converter<F, R> defaultConverter(String path, String name) {
        return new ParameterisedConverter<>(path, name);
    }
}
