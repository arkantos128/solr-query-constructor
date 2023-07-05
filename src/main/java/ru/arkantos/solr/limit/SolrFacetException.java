package ru.arkantos.solr.limit;

public class SolrFacetException extends Exception {
    public SolrFacetException() {
    }

    public SolrFacetException(String message) {
        super(message);
    }

    public SolrFacetException(String message, Throwable cause) {
        super(message, cause);
    }

    public SolrFacetException(Throwable cause) {
        super(cause);
    }

    public SolrFacetException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
