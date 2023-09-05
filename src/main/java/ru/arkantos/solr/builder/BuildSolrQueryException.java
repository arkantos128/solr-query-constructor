package ru.arkantos.solr.builder;

public class BuildSolrQueryException extends Exception {

    public BuildSolrQueryException(String message) {
        super(message);
    }

    public BuildSolrQueryException(String message, Throwable cause) {
        super(message, cause);
    }

    public BuildSolrQueryException(Throwable cause) {
        super(cause);
    }
}
