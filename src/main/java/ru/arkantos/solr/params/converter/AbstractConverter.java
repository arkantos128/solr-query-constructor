package ru.arkantos.solr.params.converter;


import ru.arkantos.solr.params.extractor.DefaultPropertyExtractor;
import ru.arkantos.solr.params.extractor.PropertyExtractor;

public abstract class AbstractConverter<FIELD, RESULT> implements Converter<FIELD, RESULT> {

    private final String path;
    protected final String name;
    private static final PropertyExtractor extractor = new DefaultPropertyExtractor();

    public AbstractConverter(String path, String name) {
        if (path == null) {
            throw new IllegalArgumentException("'path' can't be null");
        }
        if (name == null) {
            throw new IllegalArgumentException("'name' can't be null");
        }
        this.path = path;
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    protected FIELD getField(Object source) {
        return (FIELD) extractor.extract(source, path);
    }
}
