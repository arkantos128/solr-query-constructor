package ru.arkantos.solr.builder;

public class BuildSolrQueryException extends Exception {

    public BuildSolrQueryException() {
    }

    public BuildSolrQueryException(String message) {
        super(message);
    }

    public BuildSolrQueryException(String message, Throwable cause) {
        super(message, cause);
    }

    public BuildSolrQueryException(Throwable cause) {
        super(cause);
    }

    public BuildSolrQueryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
