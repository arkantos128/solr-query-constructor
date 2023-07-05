package ru.arkantos.solr.params.converter.pipeline;


import ru.arkantos.solr.params.Container;
import ru.arkantos.solr.params.converter.Converter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DefaultConverterPipeline implements ConverterPipeline {

    private final boolean excludeEmptyParam;
    private final List<Converter<?, ?>> converters;

    public DefaultConverterPipeline(List<Converter<?, ?>> converters) {
        this.converters = converters;
        this.excludeEmptyParam = false;
    }

    public DefaultConverterPipeline(List<Converter<?, ?>> converters, boolean excludeEmptyParam) {
        this.converters = converters;
        this.excludeEmptyParam = excludeEmptyParam;
    }

    @Override
    public Container convert(Object source) {
        Map<String, Object> tmpMap = new HashMap<>();
        for (var converter : this.converters) {
            String key = converter.getName();
            Object value = converter.convert(source);
            if (this.excludeEmptyParam && value == null) {
                continue;
            }
            tmpMap.put(key, value);
        }
        return new Container(tmpMap);
    }
}
