package ru.arkantos.solr.params.extractor;

public interface PropertyExtractor {

    Object extract(Object source, String path);

}
