package ru.arkantos.solr.params.converter.pipeline;


import ru.arkantos.solr.params.Container;
import ru.arkantos.solr.params.converter.Converter;

import java.util.Arrays;
import java.util.List;

public interface ConverterPipeline {

    Container convert(Object obj);

    static ConverterPipeline defaultPipeline(Converter<?, ?>... converters) {
        if (converters == null || converters.length == 0) {
            throw new UnsupportedOperationException("empty converters list");
        }
        List<Converter<?, ?>> converterList = Arrays.asList(converters);
        return new DefaultConverterPipeline(converterList);
    }

    static ConverterPipeline defaultPipeline(boolean excludeEmptyParams, Converter<?, ?>... converters) {
        if (converters == null || converters.length == 0) {
            throw new UnsupportedOperationException("empty converters list");
        }
        List<Converter<?, ?>> converterList = Arrays.asList(converters);
        return new DefaultConverterPipeline(converterList, excludeEmptyParams);
    }
}
