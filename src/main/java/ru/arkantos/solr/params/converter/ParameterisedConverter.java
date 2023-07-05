package ru.arkantos.solr.params.converter;

import java.util.function.Function;

class ParameterisedConverter<FIELD, RESULT> extends AbstractConverter<FIELD, RESULT> {
    private final Function<FIELD, RESULT> convertFunction;

    public ParameterisedConverter(String path, String name, Function<FIELD, RESULT> convertFunction) {
        super(path, name);
        this.convertFunction = convertFunction;
    }

    public ParameterisedConverter(String path, String name) {
        super(path, name);
        this.convertFunction = f -> (RESULT) f;
    }

    @Override
    public RESULT convert(Object obj) {
        FIELD field = this.getField(obj);
        return this.convertFunction.apply(field);
    }
}
